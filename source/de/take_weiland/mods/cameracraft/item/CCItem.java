package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.templates.AdvancedItem;
import de.take_weiland.mods.commons.util.Items;

public abstract class CCItem extends Item implements AdvancedItem {

	public static ItemBattery battery;
	public static ItemCamera camera;
	public static ItemIngotsDusts ingotsDusts;
	public static ItemPhotoStorages photoStorage;
	
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
		(ingotsDusts = new ItemIngotsDusts(9878)).lateInit();
		(photoStorage = new ItemPhotoStorages(9879)).lateInit();
		
		OreDictionary.registerOre("ingotTin", IngotDustType.TIN_INGOT.stack());
	}

	private static int getId(String name, int defaultId) {
		return CameraCraft.config.getItem(name, defaultId).getInt();
	}

	@Override
	public ItemStack stack() {
		return stack(1, 0);
	}

	@Override
	public ItemStack stack(int quantity) {
		return stack(quantity, 0);
	}

	@Override
	public ItemStack stack(int quantity, int meta) {
		return new ItemStack(this, quantity, meta);
	}
	
	@Override
	public String unlocalizedName() {
		return getUnlocalizedName();
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack item) {
		return getUnlocalizedName(item); // some optimization
	}
	
}
