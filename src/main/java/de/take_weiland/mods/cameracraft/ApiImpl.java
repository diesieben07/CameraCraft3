package de.take_weiland.mods.cameracraft;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.camera.Viewport;
import de.take_weiland.mods.cameracraft.api.camera.ViewportProvider;
import de.take_weiland.mods.cameracraft.api.camera.ViewportProviderFactory;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Verify.verify;

public final class ApiImpl implements CameraCraftApi {

    private final Map<Item, BatteryHandler> batteryHandlers = Maps.newHashMap();
    private Map<ResourceLocation, ViewportProviderFactory> viewportProviderFactories = new HashMap<>();
    private ImmutableList<ViewportProviderFactory> bakedFactories;

    @Override
	public CompletionStage<Long> defaultTakePhoto(EntityPlayer player, ImageFilter filter) {
        CCPlayerData playerData = CCPlayerData.get(player);
        return takePhoto(playerData, filter);
	}

	@Override
    public CompletionStage<Long> takePhoto(Viewport viewport, ImageFilter filter) {
        return viewport.getProvider().grabImage(viewport).thenCompose(image -> CameraCraft.currentDatabase().saveNewImage(image, filter));
    }

	@Override
	public PhotoDatabase getDatabase() {
		return CameraCraft.currentDatabase();
	}

	@Override
	public ViewportProvider getProvider(Viewport viewport) {
        for (ViewportProviderFactory factory : bakedFactories) {
            ViewportProvider provider = factory.getProvider(viewport);
            if (provider != null) {
                return provider;
            }
        }
        throw new UnsupportedOperationException("Could not find a suitable provider for " + viewport);
    }

    @Override
    public void registerViewportProviderFactory(String name, ViewportProviderFactory factory) {
        checkNotFrozen();

        ModContainer active = Loader.instance().activeModContainer();
        checkState(active != null, "ViewportProviderFactory registration outside of mod-loading");
        checkState(name.charAt(0) != '-', "Name cannot start with '-'");
        ResourceLocation key = new ResourceLocation(active.getModId(), name);
        checkState(viewportProviderFactories.putIfAbsent(key, factory) == null, "Duplicate name (%s) for ViewportProviderFactory", key);
    }

    @Override
	public BatteryHandler findBatteryHandler(ItemStack battery) {
		Item item = battery.getItem();
		if (item instanceof BatteryHandler) {
			return (BatteryHandler) item;
		}
		BatteryHandler handler = batteryHandlers.get(item);
		if (handler != null) {
			return handler;
		} else {
			return NullBatteryHandler.INSTANCE;
		}
	}
	
	@Override
	public void registerBatteryHandler(Item item, BatteryHandler handler) {
		batteryHandlers.put(item, handler);
	}

    public void bake() {
        Property property = CameraCraft.config.get(Configuration.CATEGORY_GENERAL,
                "viewport_providers",
                new String[]{PlayerViewportFactory.IDENTIFIER},
                "List of ViewportProviders to use, prioritized in the order specified. To disable a certain provider, prefix the name with -.");

        Stream<Map.Entry<ResourceLocation, Boolean>> configProviders = Arrays.stream(property.getStringList())
                .map(ApiImpl::parseProvider)
                .filter(provider -> viewportProviderFactories.containsKey(provider.getKey()));

        Stream<Map.Entry<ResourceLocation, Boolean>> registeredProviders = viewportProviderFactories.keySet().stream()
                .sorted(Comparator.comparing(ResourceLocation::toString, String.CASE_INSENSITIVE_ORDER))
                .map(rl -> Pair.of(rl, true));

        Map<ResourceLocation, Boolean> allProviders = Stream.concat(configProviders, registeredProviders)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Boolean::logicalAnd, LinkedHashMap::new));

        if (!allProviders.containsValue(true)) {
            CameraCraft.logger.error("No enabled ViewportProviders, enabling default provider");
            allProviders.put(new ResourceLocation(CameraCraft.MOD_ID, PlayerViewportFactory.IDENTIFIER), true);
        }

        String[] newConfigData = allProviders.entrySet().stream()
                .map(e -> (e.getValue() ? "" : "-") + e.getKey().toString())
                .toArray(String[]::new);
        property.set(newConfigData);

        bakedFactories = ImmutableList.copyOf(allProviders.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .map(viewportProviderFactories::get)
                .toArray(ViewportProviderFactory[]::new));

        verify(!bakedFactories.isEmpty());
    }

    private static Map.Entry<ResourceLocation, Boolean> parseProvider(String s) {
        boolean enabled = s.isEmpty() || s.charAt(0) != '-';
        return Pair.of(new ResourceLocation(enabled ? s : s.substring(1)), enabled);
    }

    private void checkNotFrozen() {
        checkState(!Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION), "API registrations are frozen, registrations must happen before postInit");
    }

    private void checkFrozen() {
        checkState(bakedFactories == null, "Cannot query API before postInit");
    }

	private enum NullBatteryHandler implements BatteryHandler {
		INSTANCE;

		@Override
		public boolean isBattery(ItemStack stack) {
			return false;
		}
		
		@Override
		public int getCharge(ItemStack stack) {
			return 0;
		}

		@Override
		public boolean isRechargable(ItemStack stack) {
			return false;
		}

		@Override
		public int getCapacity(ItemStack stack) {
			return 0;
		}

		@Override
		public int charge(ItemStack stack, int amount) {
			return 0;
		}

		@Override
		public int drain(ItemStack stack, int amount) {
			return 0;
		}

	}

}
