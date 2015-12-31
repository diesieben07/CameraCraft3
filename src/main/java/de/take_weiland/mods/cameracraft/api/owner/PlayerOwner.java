package de.take_weiland.mods.cameracraft.api.owner;

import net.minecraft.block.Block;

import java.util.UUID;

/**
 * <p>Represents a player as an owner.</p>
 *
 * @author diesieben07
 */
public final class PlayerOwner extends AbstractOwner {

    private final UUID   id;
    private final String name;

    public PlayerOwner(Owner owner, UUID id, String name) {
        super(owner);
        this.id = id;
        this.name = name;
    }

    public PlayerOwner(UUID id, String name) {
        this(null, id, name);
    }

    @Override
    public Type getType() {
        return Type.PLAYER;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Block getBlock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMetadata() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDimension() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getX() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getY() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getZ() {
        throw new UnsupportedOperationException();
    }
}
