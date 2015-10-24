package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.inv.Inventories;
import de.take_weiland.mods.commons.util.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CCBlock extends Block implements CCBlockName {

    public static final Fluid alkalineFluid = new Fluid("cameracraft.alkaline").setLuminosity(7);
    public static final BlockCCOre ores = new BlockCCOre();
    public static final BlockCCMachine machines = new BlockCCMachine();
    public static final BlockAlkaline alkaline = new BlockAlkaline();

    public static void createBlocks() {
        Blocks.initAll((Block block) -> ((CCBlockName) block).getBaseName(), CameraCraft.tab,
                ores, machines, alkaline);
    }

    private final String baseName;

    protected CCBlock(String name, Material material) {
        super(material);
        this.baseName = name;
    }

    static Fluid getAlkaline() {
        if (!FluidRegistry.isFluidRegistered(alkalineFluid)) {
            FluidRegistry.registerFluid(alkalineFluid);
        }
        return alkalineFluid;
    }

    @Override
    public String getBaseName() {
        return baseName;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (hasTileEntity(metadata)) {
            Inventories.spillIfInventory(world.getTileEntity(x, y, z));
        }
        super.breakBlock(world, x, y, z, block, metadata);
    }

}

interface CCBlockName {

    String getBaseName();


}