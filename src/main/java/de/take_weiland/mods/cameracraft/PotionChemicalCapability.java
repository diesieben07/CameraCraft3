package de.take_weiland.mods.cameracraft;

import de.take_weiland.mods.cameracraft.api.ChemicalItem;
import de.take_weiland.mods.cameracraft.api.FilmItem;
import de.take_weiland.mods.cameracraft.api.FilmState;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * @author diesieben07
 */
final class PotionChemicalCapability implements ChemicalItem {

    @Override
    public boolean isChemical(ItemStack stack) {
        return stack.getMetadata() == 0;
    }

    @Override
    public int applicationTime(ItemStack stack, ItemStack film) {
        return 200;
    }

    @Override
    public ItemStack applyToFilm(ItemStack stack, ItemStack film) {
        FilmItem filmItem = FilmItem.get(film);
        if (filmItem != null) {
            film = filmItem.setFilmState(film, FilmState.PROCESSED);
        }
        return film;
    }

    @Override
    public ItemStack onChemicalUsed(ItemStack stack) {
        // TODO turn into water with progress bar
        return CCItem.misc.getStack(MiscItemType.DIRTY_WATER);
    }

    @Override
    public String getDisplayInTray(ItemStack stack) {
        return StatCollector.translateToLocal("cameracraft.chemical.water");
    }
}
