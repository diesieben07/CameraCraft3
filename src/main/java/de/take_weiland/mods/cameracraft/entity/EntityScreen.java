package de.take_weiland.mods.cameracraft.entity;

import de.take_weiland.mods.cameracraft.item.CCItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.world.World;

/**
 * @author Intektor
 */
public class EntityScreen extends EntityHanging{

    private int streamID;

    public EntityScreen(World world) {
        super(world);
    }

    public EntityScreen(World world, int x, int y, int z, int dir) {
        super(world, x, y, z, dir);
        setDirection(dir);
    }

    @Override
    public int getWidthPixels() {
        return 64;
    }

    @Override
    public int getHeightPixels() {
        return 64;
    }

    @Override
    public void onBroken(Entity entity) {
        dropItem(CCItem.screen, 1);
    }

    public int getStreamID() {
        return streamID;
    }

    public void setStreamID(int streamID) {
        this.streamID = streamID;
    }
}
