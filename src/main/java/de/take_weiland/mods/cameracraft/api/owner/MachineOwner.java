package de.take_weiland.mods.cameracraft.api.owner;

import net.minecraft.block.Block;

import java.util.UUID;

/**
 * <p>Represents a machine as an owner.</p>
 *
 * @author diesieben07
 */
public final class MachineOwner extends AbstractOwner {

    private final Block block;
    private final int   metadata, dimension, x, y, z;

    public MachineOwner(Owner owner, Block block, int metadata, int dimension, int x, int y, int z) {
        super(owner);
        this.block = block;
        this.metadata = metadata;
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MachineOwner(Block block, int metadata, int dimension, int x, int y, int z) {
        this.block = block;
        this.metadata = metadata;
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Type getType() {
        return Type.MACHINE;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public int getMetadata() {
        return metadata;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UUID getId() {
        throw new UnsupportedOperationException();
    }
}
