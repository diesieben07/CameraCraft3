package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.util.Items;
import net.minecraft.item.Item;

public class CCItem extends Item {

	public static ItemBattery battery;
	
	public CCItem(String name, int defaultId) {
		super(getId(name, defaultId));
		Items.init(this, name);
		
		setCreativeTab(CameraCraft.tab);
	}
	
	public static final void createItems() {
		battery = new ItemBattery(9876);
	}

	private static int getId(String name, int defaultId) {
		return CameraCraft.config.getItem(name, defaultId).getInt();
	}
}
