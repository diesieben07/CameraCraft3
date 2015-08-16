package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.inv.Inventories;
import de.take_weiland.mods.commons.util.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CCBlock extends Block {

    public static BlockCCOre ores;
    public static BlockCCMachine machines;
    public static BlockAlkaline alkaline;

    public static void createBlocks() {
        alkalineFluid = new Fluid("cameracraft.alkaline").setLuminosity(7);
        FluidRegistry.registerFluid(alkalineFluid);

        (ores = new BlockCCOre(3078)).lateInit();
        (machines = new BlockCCMachine(3079)).lateInit();
        (alkaline = new BlockAlkaline()).lateInit();
    }

    private final String baseName;
    public static Fluid alkalineFluid;

    protected CCBlock(String name, Material material) {
        super(material);
        this.baseName = name;
    }

    protected void lateInit() {
        lateInitBlock(this, baseName);
    }

    static void lateInitBlock(Block block, String baseName) {
        Blocks.init(block, baseName);
        block.setCreativeTab(CameraCraft.tab);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (hasTileEntity(metadata)) {
            Inventories.spillIfInventory(world.getTileEntity(x, y, z));
        }
        super.breakBlock(world, x, y, z, block, metadata);
    }

}