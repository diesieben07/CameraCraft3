package de.take_weiland.mods.cameracraft.entity;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.item.CCItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author Intektor
 */
public class EntityScreen extends EntityHanging implements IEntityAdditionalSpawnData {

    private String streamID = "cam";

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

    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }

    @Override
    protected void entityInit() {

    }

    @Override
    public boolean interactFirst(EntityPlayer player) {
        System.out.println("open");
        CCGuis.SET_STREAM_ID.open(player, (int) player.posX, (int) player.posY, (int) player.posZ);
        return true;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, streamID);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        streamID = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        streamID = nbt.getString("StreamID");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setString("StreamID", streamID + "");
    }
}
