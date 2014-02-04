package de.take_weiland.mods.cameracraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Sides;

public class ItemBattery extends CCItem implements BatteryHandler {

	private static final int CAPACITY = 50;

	private static final String CHARGE_NBT_KEY = "cameracraft.charge";

	private Icon[] icons = new Icon[7];
	
	private final ItemStack[] subStacks;
	
	public ItemBattery(int defaultId) {
		super("battery", defaultId);
		setHasSubtypes(true);
		
		subStacks = new ItemStack[2];
		subStacks[0] = ItemStacks.of(this);
		setCharge(subStacks[0], CAPACITY);
		subStacks[1] = ItemStacks.of(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean debug) {
		lines.add(getCharge(stack) + " / " + getCapacity(stack));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int itemId, CreativeTabs tab, List itemList) {
		ItemStack charged = ItemStacks.of(this);
		setCharge(charged, CAPACITY);
		itemList.add(charged);
		itemList.add(ItemStacks.of(this));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		for (int i = 0; i < 7; i++) {
			icons[i] = register.registerIcon("cameracraft:battery" + i);
		}
	}
	
	@Override
	public Icon getIconIndex(ItemStack stack) {
		int charge = getCharge(stack);
		return icons[MathHelper.floor_float(charge / (float)getCapacity(stack) * 6)];
	}
	
	@Override
	public int getDisplayDamage(ItemStack stack) {
		return getCapacity(stack) - getCharge(stack);
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return getCapacity(stack);
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int idx, boolean active) {
		if (active && Sides.logical(world).isServer() && world.rand.nextInt(5) == 0) {
			charge(stack, 1);
		}
	}

	// BatteryHandler
	
	@Override
	public boolean isBattery(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getCharge(ItemStack stack) {
		return ItemStacks.getNbt(stack).getInteger(CHARGE_NBT_KEY);
	}

	@Override
	public boolean isRechargable(ItemStack stack) {
		return true;
	}

	@Override
	public int getCapacity(ItemStack stack) {
		return CAPACITY;
	}

	@Override
	public int charge(ItemStack stack, int amount) {
		int prevCharge = getCharge(stack);
		amount = MathHelper.clamp_int(amount, 0, CAPACITY - prevCharge);
		setCharge(stack, prevCharge + amount);
		return amount;
	}

	@Override
	public int drain(ItemStack stack, int amount) {
		int prevCharge = getCharge(stack);
		amount = MathHelper.clamp_int(amount, 0, prevCharge);
		setCharge(stack, prevCharge - amount);
		return amount;
	}

	private int setCharge(ItemStack stack, int newCharge) {
		newCharge = MathHelper.clamp_int(newCharge, 0, CAPACITY);
		ItemStacks.getNbt(stack).setInteger(CHARGE_NBT_KEY, newCharge);
		return newCharge;
	}

}
