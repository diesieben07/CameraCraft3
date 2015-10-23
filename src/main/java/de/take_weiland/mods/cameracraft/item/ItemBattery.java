package de.take_weiland.mods.cameracraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

import static de.take_weiland.mods.commons.util.Sides.sideOf;

public class ItemBattery extends CCItem implements BatteryHandler {

	private static final int CAPACITY = 50;

	private static final String CHARGE_NBT_KEY = "cameracraft.charge";

	private IIcon[] icons = new IIcon[7];

    public ItemBattery() {
		super("battery");
		setHasSubtypes(true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean debug) {
		lines.add(getCharge(stack) + " / " + getCapacity(stack));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List itemList) {
		ItemStack charged = new ItemStack(this);
		setCharge(charged, CAPACITY);
		itemList.add(charged);

        itemList.add(new ItemStack(this));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		for (int i = 0; i < 7; i++) {
			icons[i] = register.registerIcon("cameracraft:battery" + i);
		}
	}
	
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		int charge = getCharge(stack);
		return icons[MathHelper.floor_float(charge / (float)getCapacity(stack) * 6)];
	}

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (double) getCharge(stack) / (double) getCapacity(stack);
    }

	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int idx, boolean selected) {
		if (selected && sideOf(world).isServer() && world.rand.nextInt(5) == 0) {
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
