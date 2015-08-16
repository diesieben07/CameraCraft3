package de.take_weiland.mods.cameracraft.item;

import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.util.Items;
import net.minecraft.item.Item;

@GameRegistry.ObjectHolder(CameraCraft.MOD_ID)
public abstract class CCItem extends Item {

    public static final ItemBattery battery = new ItemBattery();
    public static final ItemCamera camera = new ItemCamera();
    public static final CCItemMisc misc = new CCItemMisc();
    public static final ItemPhotoStorages photoStorage = new ItemPhotoStorages();
    public static final ItemLens lens = new ItemLens();
    public static final ItemPhoto photo = new ItemPhoto();

    private final String baseName;

    public CCItem(String name) {
        this.baseName = name;
    }

    private String getBaseName() {
        return baseName;
    }

    public static void init() {
        Items.initAll(CCItem::getBaseName, battery, camera, misc, photoStorage, lens, photo)
                .forEach((item) -> item.setCreativeTab(CameraCraft.tab));
    }
}
