package de.take_weiland.mods.cameracraft.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.CommonUtils;
import de.take_weiland.mods.commons.util.Items;
import static de.take_weiland.mods.commons.util.Items.*;

public class BlockCCOre extends CCBlock implements Typed<OreType> {

	private Icon[] icons;
	
	public BlockCCOre(int defaultId) {
		super("ores", defaultId, Material.rock);
		setHardness(3);
		setResistance(5);
		setStepSound(soundStoneFootstep);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", EnumToolMaterial.IRON.getHarvestLevel());
	}

	@Override
	public int idDropped(int meta, Random random, int fortune) {
		if (getType(this, meta) == OreType.ALKALINE) {
			return CCItem.battery.itemID;
		} else {
			return blockID;
		}
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		if (getType(this, meta) == OreType.ALKALINE) {
			return 2 + random.nextInt(2 + fortune);
		} else {
			return 1;
		}
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float destroyChance, int fortune) {
		super.dropBlockAsItemWithChance(world, x, y, z, meta, destroyChance, fortune);
		if (getType(this, meta) == OreType.ALKALINE) {
			dropXpOnBlockBreak(world, x, y, z, MathHelper.getRandomIntegerInRange(world.rand, 2, 5));
		}
	}

	@Override
	public OreType[] getTypes() {
		return OreType.values();
	}

	@Override
	public OreType getDefault() {
		return OreType.TIN;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int blockId, CreativeTabs tab, @SuppressWarnings("rawtypes") List list) {
		Blocks.addSubtypes(this, list);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + Items.getType(this, stack).getName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return CommonUtils.safeArrayAccess(icons, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		icons = Blocks.registerIcons(this, register);
	}

}
