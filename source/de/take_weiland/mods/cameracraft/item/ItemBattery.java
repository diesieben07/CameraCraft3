package de.take_weiland.mods.cameracraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.commons.util.CollectionUtils;

public class ItemBattery extends CCItem {

	@SideOnly(Side.CLIENT)
	private Icon[] icons = new Icon[7];
	
	private final List<ItemStack> subItems;
	
	public ItemBattery(int defaultId) {
		super("battery", defaultId);
		setHasSubtypes(true);
		
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
		for (int i = 0; i < 7; i++) {
			builder.add(new ItemStack(this, 1, i));
		}
		subItems = builder.build();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int meta) {
		return CollectionUtils.safeArrayAccess(icons, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		for (int i = 0; i < 7; i++) {
			icons[i] = register.registerIcon("cameracraft:battery" + i);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int itemId, CreativeTabs tab, List itemList) {
		itemList.addAll(subItems);
	}

}
