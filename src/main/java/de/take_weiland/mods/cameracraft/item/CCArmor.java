package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.util.Items;
import net.minecraft.item.ItemArmor;

/**
 * @author Intektor
 */
public abstract class CCArmor extends ItemArmor {

    public static final ItemHelmetCamera helmet_camera = new ItemHelmetCamera();

    private final String baseName;

    public CCArmor(String name, int armorType) {
        super(ArmorMaterial.IRON, 0, armorType);
        baseName = name;
    }

    private String getBaseName() {
        return baseName;
    }

    public static void init(){
        Items.initAll(CCArmor::getBaseName, CameraCraft.tab, helmet_camera);
    }

}
