package de.take_weiland.mods.cameracraft.api;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import net.minecraft.item.ItemStack;

/**
 * @author diesieben07
 */
public interface FilmItem extends TrayContainable {

    static FilmItem get(ItemStack stack) {
        return CameraCraftApi.get().getCapability(stack, FilmItem.class, FilmItem::isFilm);
    }

    default boolean isFilm(ItemStack stack) {
        return true;
    }

    FilmState getFilmState(ItemStack film);

    ItemStack setFilmState(ItemStack film, FilmState state);

    default ImageFilter getFilmFilter(ItemStack film) {
        return null;
    }

}
