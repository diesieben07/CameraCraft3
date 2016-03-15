package de.take_weiland.mods.cameracraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.cameracraft.api.ChemicalItem;
import de.take_weiland.mods.cameracraft.api.FilmItem;
import de.take_weiland.mods.cameracraft.api.FilmState;
import de.take_weiland.mods.cameracraft.api.TrayItem;
import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.commons.client.I18n;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import de.take_weiland.mods.commons.nbt.NBT;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

import java.util.List;

public class CCItemMisc extends CCItemMultitype<MiscItemType> implements InkItem, TrayItem, ChemicalItem {

    private static final MetadataProperty<MiscItemType> type = MetadataProperty.newProperty(0, MiscItemType.class);

    public CCItemMisc() {
        super("misc");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
        if (isTray(stack)) {
            ItemStack chemical = getContainedChemical(stack);
            ItemStack film = getContainedFilm(stack);
            ChemicalItem chemicalItem = ChemicalItem.get(chemical);
            FilmItem filmItem = FilmItem.get(film);

            if (chemicalItem == null && filmItem == null) {
                list.add(I18n.translate("cameracraft.tray.empty"));
            } else {
                if (chemicalItem != null) {
                    list.add(I18n.translate("cameracraft.tray.contains", chemicalItem.getDisplayInTray(chemical)));
                }
                if (filmItem != null) {
                    list.add(I18n.translate("cameracraft.tray.contains", filmItem.getDisplayInTray(film)));
                }
            }
        }
    }

    @Override
    public MetadataProperty<MiscItemType> subtypeProperty() {
        return type;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return isInk(stack) || isChemical(stack) && chemicalUses(stack) != 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (isInk(stack)) {
            return getInkAmount0(stack) / (float) MAX_INK_AMOUNT;
        } else if (isChemical(stack)) {
            return chemicalUses(stack) / (float) CHEM_MAX_USES;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return getType(stack).getStackSize(stack);
    }

    // InkItem

    public static final  int    MAX_INK_AMOUNT = 100;
    private static final String INK_AMNT_KEY   = "cameracraft.ink";

    @Override
    public boolean isInk(ItemStack stack) {
        return getType(stack).isInk();
    }

    @Override
    public int getInkAmount(ItemStack stack) {
        if (isInk(stack)) {
            return MAX_INK_AMOUNT - getInkAmount0(stack);
        } else {
            return -1;
        }
    }

    private int getInkAmount0(ItemStack stack) {
        return ItemStacks.getNbt(stack).getInteger(INK_AMNT_KEY);
    }

    @Override
    public ItemStack setInkAmount(ItemStack stack, int newAmount) {
        if (isInk(stack)) {
            ItemStacks.getNbt(stack).setInteger(INK_AMNT_KEY, MAX_INK_AMOUNT - newAmount);
        }
        return stack;
    }

    @Override
    public InkItem.Color getColor(ItemStack stack) {
        return getType(stack).getInkColor();
    }

    // TrayItem

    private static final String TRAY_FILM_KEY = "cameracraft.trayFilm";
    private static final String TRAY_CHEM_KEY = "cameracraft.trayChem";

    @Override
    public boolean isTray(ItemStack stack) {
        return getType(stack) == MiscItemType.TRAY;
    }

    @Override
    public ItemStack getContainedFilm(ItemStack stack) {
        NBTBase nbt = ItemStacks.getNbt(stack).getTag(TRAY_FILM_KEY);
        if (nbt == null || nbt.getId() != NBT.TAG_COMPOUND) {
            return null;
        } else {
            return ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt);
        }
    }

    @Override
    public ItemStack setContainedFilm(ItemStack stack, ItemStack film) {
        if (film == null) {
            ItemStacks.getNbt(stack).removeTag(TRAY_FILM_KEY);
        } else {
            NBTTagCompound nbt = film.writeToNBT(new NBTTagCompound());
            ItemStacks.getNbt(stack).setTag(TRAY_FILM_KEY, nbt);
        }
        return stack;
    }


    @Override
    public ItemStack getContainedChemical(ItemStack stack) {
        NBTBase tag = ItemStacks.getNbt(stack).getTag(TRAY_CHEM_KEY);
        if (tag == null || tag.getId() != NBT.TAG_COMPOUND) {
            return null;
        } else {
            return ItemStack.loadItemStackFromNBT((NBTTagCompound) tag);
        }
    }

    @Override
    public ItemStack setContainedChemical(ItemStack stack, ItemStack chemical) {
        NBTTagCompound nbt = ItemStacks.getNbt(stack);
        if (chemical == null) {
            nbt.removeTag(TRAY_CHEM_KEY);
        } else {
            nbt.setTag(TRAY_CHEM_KEY, chemical.writeToNBT(new NBTTagCompound()));
        }
        return stack;
    }

    // ChemicalItem

    private static final String CHEM_USES_KEY = "cameracraft.chem.use";
    private static final int    CHEM_MAX_USES = 10;

    @Override
    public boolean isChemical(ItemStack stack) {
        return getType(stack).isChemical();
    }

    private static int chemicalUses(ItemStack stack) {
        NBTBase tag = ItemStacks.getNbt(stack).getTag(CHEM_USES_KEY);
        if (tag != null && tag instanceof NBTBase.NBTPrimitive) {
            return MathHelper.clamp_int(((NBTBase.NBTPrimitive) tag).getInt(), 0, CHEM_MAX_USES);
        }
        return 0;
    }

    @Override
    public ItemStack onChemicalUsed(ItemStack stack) {
        int uses = chemicalUses(stack);
        if (uses != CHEM_MAX_USES) {
            ItemStacks.getNbt(stack).setInteger(CHEM_USES_KEY, uses + 1);
        }
        return stack;
    }

    @Override
    public ItemStack applyToFilm(ItemStack stack, ItemStack film) {
        FilmItem filmItem = FilmItem.get(film);
        if (filmItem == null || filmItem.getFilmState(film) != FilmState.READY_TO_PROCESS) {
            return film;
        } else {
            MiscItemType type = getType(stack);
            if (type == MiscItemType.DEVELOPER) {
                // TODO
                return stack;
            } else { // fixer
                // TODO
                return stack;
            }
        }
    }

    @Override
    public int applicationTime(ItemStack stack, ItemStack film) {
        return 100;
    }
}
