package de.take_weiland.mods.cameracraft.blocks;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.inv.Inventories;
import de.take_weiland.mods.commons.util.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class CCBlock extends Block implements CCBlockName {

    public static final BlockCCOre                       ores        = new BlockCCOre();
    public static final BlockSafetyLight                 safetyLight = new BlockSafetyLight();
    public static final Map<MachineType, BlockCCMachine> machines    = new EnumMap<>(Maps.toMap(Arrays.asList(MachineType.values()), BlockCCMachine::new));

    public static void createBlocks() {
        Blocks.initAll((Block block) -> ((CCBlockName) block).getBaseName(), CameraCraft.tab,
                Iterables.concat(Arrays.asList(ores, safetyLight), machines.values()));
    }

    private final String baseName;

    protected CCBlock(String name, Material material) {
        super(material);
        this.baseName = name;
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

