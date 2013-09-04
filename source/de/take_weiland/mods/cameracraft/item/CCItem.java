package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.util.Items;

public class CCItem extends Item {

	public static ItemBattery battery;
	public static ItemCamera camera;
	public static ItemIngotsDusts ingotsDusts;
	
	public CCItem(String name, int defaultId) {
		super(getId(name, defaultId));
		Items.init(this, name);
		
		setCreativeTab(CameraCraft.tab);
	}
	
	public static final void createItems() {
		battery = new ItemBattery(9876);
		camera = new ItemCamera(9877);
		ingotsDusts = new ItemIngotsDusts(9878);
		
		OreDictionary.registerOre("ingotTin", Items.getStack(ingotsDusts, IngotDustType.TIN_INGOT));
	}

	private static int getId(String name, int defaultId) {
		return CameraCraft.config.getItem(name, defaultId).getInt();
	}
}
