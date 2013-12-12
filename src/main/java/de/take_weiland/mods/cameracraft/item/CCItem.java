package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.util.Items;

public abstract class CCItem extends Item {

	public static ItemBattery battery;
	public static ItemCamera camera;
	public static CCItemMisc miscItems;
	public static ItemPhotoStorages photoStorage;
	public static ItemPhoto photo;
	public static ItemLens lenses;
	
	private final String baseName;
	
	public CCItem(String name, int defaultId) {
		super(getId(name, defaultId));
		this.baseName = name;
	}
	
	protected void lateInit() {
		Items.init(this, baseName);
		
		setCreativeTab(CameraCraft.tab);
	}
	
	public static final void createItems() {
		(battery = new ItemBattery(9876)).lateInit();
		(camera = new ItemCamera(9877)).lateInit();
		(miscItems = new CCItemMisc(9878)).lateInit();
		(photoStorage = new ItemPhotoStorages(9879)).lateInit();
		(lenses = new ItemLens(9880)).lateInit();
	}

	private static int getId(String name, int defaultId) {
		return CameraCraft.config.getItem(name, defaultId).getInt();
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack item) {
		return getUnlocalizedName(item); // some optimization
	}
	
}
