package de.take_weiland.mods.cameracraft.entity;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.video.camera.StreamHandler;
import de.take_weiland.mods.cameracraft.video.camera.VideoStream;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * @author Intektor
 */
public class EntityVideoCamera extends Entity implements IEntityAdditionalSpawnData{

    private String streamID = "cam";

    public EntityVideoCamera(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
    }

    public EntityVideoCamera(World world, double posX, double posY, double posZ) {
        super(world);
        this.posX = posX + 0.5;
        this.posY = posY + 1;
        this.posZ = posZ + 0.5;
        this.setSize(1, 1);
        StreamHandler.addVideoStream(new VideoStream(streamID == null ? "cam" : streamID, this));
    }

    public EntityVideoCamera(World world, double posX, double posY, double posZ, float yaw, float pitch) {
        super(world);
        this.posX = posX + 0.5;
        this.posY = posY + 1;
        this.posZ = posZ + 0.5;
        this.setSize(1, 1);
        this.setRotation(yaw, pitch);
        StreamHandler.addVideoStream(new VideoStream(streamID == null ? "cam" : streamID, this));
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        streamID = nbt.getString("StreamID");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setString("StreamID", streamID);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        setDead();
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {

        System.out.println("hi");
        CCGuis.SET_STREAM_ID.open(player, (int)player.posX, (int)player.posY, (int)player.posZ);

        return true;
    }

    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String id) {
        streamID = id;
        StreamHandler.addVideoStream(new VideoStream(streamID, this));
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, streamID);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        streamID = ByteBufUtils.readUTF8String(buffer);
    }
}

