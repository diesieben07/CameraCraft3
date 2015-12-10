package de.take_weiland.mods.cameracraft.blocks;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.inv.Inventories;
import de.take_weiland.mods.commons.util.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Arrays;
import java.util.Map;

public class CCBlock extends Block implements CCBlockName {

    public static final Fluid alkalineFluid                       = new Fluid("cameracraft.alkaline").setLuminosity(7);

    public static final BlockCCOre ores                           = new BlockCCOre();
    public static final Map<MachineType, BlockCCMachine> machines = Maps.toMap(Arrays.asList(MachineType.values()), BlockCCMachine::new);
    public static final BlockAlkaline alkaline = new BlockAlkaline();

    public static void createBlocks() {
        Blocks.initAll((Block block) -> ((CCBlockName) block).getBaseName(), CameraCraft.tab,
                Iterables.concat(Arrays.asList(ores, alkaline), machines.values()));
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

