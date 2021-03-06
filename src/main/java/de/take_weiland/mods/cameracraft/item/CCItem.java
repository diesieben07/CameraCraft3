package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.util.Items;
import net.minecraft.item.Item;

public abstract class CCItem extends Item {

    public static final ItemBattery battery = new ItemBattery();
    public static final ItemCamera camera = new ItemCamera();
    public static final CCItemMisc misc = new CCItemMisc();
    public static final ItemPhotoStorages photoStorage = new ItemPhotoStorages();
    public static final ItemLens lens = new ItemLens();
    public static final ItemPhoto photo = new ItemPhoto();
    public static final ItemVideoCamera video_camera = new ItemVideoCamera();
    public static final ItemScreen screen = new ItemScreen();
    public static final ItemPen pen = new ItemPen();
    public static final ItemRubber rubber = new ItemRubber();

    private final String baseName;

    public CCItem(String name) {
        this.baseName = name;
    }

    private String getBaseName() {
        return baseName;
    }

    public static void init() {
        Items.initAll(CCItem::getBaseName, CameraCraft.tab,
                battery, camera, misc, photoStorage, lens, photo, video_camera, screen, pen, rubber);
    }
}
