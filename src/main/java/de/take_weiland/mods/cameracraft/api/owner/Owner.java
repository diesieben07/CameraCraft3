package de.take_weiland.mods.cameracraft.api.owner;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

/**
 * <p>The owner of a photo. This can be a player or a machine.</p>
 *
 * @author diesieben07
 */
public interface Owner {

    /**
     * <p>The owner of this owner, used e.g. with machines to determine the player (or other machine) who created this owner.</p>
     *
     * @return the owner or null if none
     */
    @Nullable
    Owner getOwner();

    /**
     * <p>Get the root owner, which is usually a player. This method walks up the owner chain until no further parent owner
     * exists.</p>
     *
     * @return the root owner
     */
    default Owner getRoot() {
        Owner result = this;
        do {
            Owner next = result.getOwner();
            if (next != null) {
                result = next;
            } else {
                break;
            }
        } while (true);
        return result;
    }

    /**
     * <p>The type of this owner.</p>
     *
     * @return the type
     */
    Type getType();

    // player stuff

    /**
     * <p>The username of the player at the time the photo was taken.</p>
     * <p>This method must only be called when {@link #getType()} is {@code PLAYER}.</p>
     *
     * @return the username
     */
    String getName();

    /**
     * <p>The player's UUID.</p>
     * <p>This method must only be called when {@link #getType()} is {@code PLAYER}.</p>
     *
     * @return the UUID
     */
    UUID getId();

    // machine stuff

    /**
     * <p>The block type of the machine.</p>
     * <p>This method must only be called when {@link #getType()} is {@code MACHINE}.</p>
     *
     * @return the block type
     */
    Block getBlock();

    /**
     * <p>The block metadata of the machine as given by {@link net.minecraft.world.World#getBlockMetadata(int, int, int)}.</p>
     * <p>This method must only be called when {@link #getType()} is {@code MACHINE}.</p>
     *
     * @return the block metadata
     */
    int getMetadata();

    /**
     * <p>The dimension ID of the world that contained the machine.</p>
     * <p>This method must only be called when {@link #getType()} is {@code MACHINE}.</p>
     *
     * @return the dimension ID
     */
    int getDimension();

    /**
     * <p>The machine's block x coordinate.</p>
     * <p>This method must only be called when {@link #getType()} is {@code MACHINE}.</p>
     *
     * @return the x coordinate
     */
    int getX();

    /**
     * <p>The machine's block y coordinate.</p>
     * <p>This method must only be called when {@link #getType()} is {@code MACHINE}.</p>
     *
     * @return the y coordinate
     */
    int getY();

    /**
     * <p>The machine's block z coordinate.</p>
     * <p>This method must only be called when {@link #getType()} is {@code MACHINE}.</p>
     *
     * @return the z coordinate
     */
    int getZ();

    /**
     * <p>The type of owner.</p>
     */
    enum Type {

        PLAYER,
        MACHINE

    }

    default void write(DataOutput out) throws IOException {
        Owner parent = getOwner();
        out.writeByte(getType().ordinal() | (parent == null ? 0 : 0b1000_0000));
        if (parent != null) {
            parent.write(out);
        }
        if (getType() == Type.PLAYER) {
            out.writeUTF(getName());
            out.writeLong(getId().getLeastSignificantBits());
            out.writeLong(getId().getMostSignificantBits());
        } else {
            out.writeUTF(Block.blockRegistry.getNameForObject(getBlock()));
            out.writeByte(getMetadata());
            out.writeInt(getX());
            out.writeByte(getY());
            out.writeInt(getZ());
            out.writeInt(getDimension());

        }
    }

    static Owner read(DataInput in) throws IOException {
        int typeId = in.readUnsignedByte();
        Owner parent;
        if ((typeId & 0b1000_0000) == 0) {
            parent = null;
        } else {
            parent = read(in);
        }

        if ((typeId & 0b1) == 0) {
            String name = in.readUTF();
            long lsb = in.readLong();
            long msb = in.readLong();
            return new PlayerOwner(parent, new UUID(msb, lsb), name);
        } else {
            Block block = GameData.getBlockRegistry().getObject(in.readUTF());
            int meta = in.readUnsignedByte();
            int x = in.readInt();
            int y = in.readUnsignedByte();
            int z = in.readInt();
            int dim = in.readInt();
            return new MachineOwner(parent, block, meta, dim, x, y, z);
        }
    }

}
