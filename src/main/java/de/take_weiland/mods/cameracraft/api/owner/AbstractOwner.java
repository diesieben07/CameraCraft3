package de.take_weiland.mods.cameracraft.api.owner;

import javax.annotation.Nullable;

/**
 * <p>Base class for {@code Owner} implementations.</p>
 *
 * @author diesieben07
 */
public abstract class AbstractOwner implements Owner {

    protected final Owner owner;

    protected AbstractOwner() {
        this(null);
    }

    protected AbstractOwner(Owner owner) {
        this.owner = owner;
    }

    @Nullable
    @Override
    public final Owner getOwner() {
        return owner;
    }
}
