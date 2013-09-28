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
		(ingotsDusts = new ItemIngotsDusts(9878)).lateInit();
		(photoStorage = new ItemPhotoStorages(9879)).lateInit();
		(lenses = new ItemLens(9880)).lateInit();
		
		OreDictionary.registerOre("ingotTin", IngotDustType.TIN_INGOT.stack());
	}

	private static int getId(String name, int defaultId) {
		return CameraCraft.config.getItem(name, defaultId).getInt();
	}

	@Override
	public ItemStack stack() {
		return stack(1);
	}

	@Override
	public ItemStack stack(int quantity) {
		return new ItemStack(this, quantity);
	}

	@Override
	public boolean isThis(ItemStack stack) {
		return stack != null && stack.itemID == itemID;
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
