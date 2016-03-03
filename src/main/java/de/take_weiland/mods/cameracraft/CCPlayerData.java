package de.take_weiland.mods.cameracraft;

import de.take_weiland.mods.cameracraft.api.camera.Viewport;
import de.take_weiland.mods.cameracraft.api.camera.ViewportProvider;
import de.take_weiland.mods.cameracraft.network.*;
import gnu.trove.impl.Constants;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletionStage;

public class CCPlayerData implements IExtendedEntityProperties, ViewportProvider, Viewport {

    public static final String INDENTIFIER = "cameracraft.playerdata";

    public static CCPlayerData get(EntityPlayer player) {
        return (CCPlayerData) player.getExtendedProperties(INDENTIFIER);
    }

    private final EntityPlayer player;
    private long cooldownEnd = 0;
    private int lastClickedEntityID;

    private       int                         nextViewportId = 0;
    private final TIntObjectMap<Viewport>     viewports      = new TIntObjectHashMap<>(Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR, -1);
    private final TObjectIntHashMap<Viewport> viewportIds    = new TObjectIntHashMap<>();

    CCPlayerData(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public CompletionStage<BufferedImage> grabImage(Viewport viewport) {
        if (viewport == this) {
            return new PacketRequestStandardPhoto().sendTo(player).thenApply(PacketImageResponse::getImage);
        } else {
            return new PacketRequestViewportPhoto(viewportIds.get(viewport)).sendTo(player).thenApply(PacketImageResponse::getImage);
        }
    }

    @Override
    public void attach(Viewport viewport) {
        int id = nextViewportId++;
        viewports.put(id, viewport);
        viewportIds.put(viewport, id);

        new PacketNewViewport(id, viewport.dimension(), viewport.x(), viewport.y(), viewport.z(), viewport.pitch(), viewport.yaw()).sendTo(player);
    }

    @Override
    public void detach(Viewport viewport) {
        int id = viewportIds.remove(viewport);
        if (viewports.remove(id) == viewport) {
            new PacketKillViewport(id).sendTo(player);
        }
    }

    @Override
    public void providerInvalidated() {
        throw new UnsupportedOperationException("player's viewport cannot invalidate itself");
    }

    @Override
    public ViewportProvider getProvider() {
        return this;
    }

    public boolean isOnCooldown() {
        return player.worldObj.getTotalWorldTime() - cooldownEnd < 0;
    }

    public void setCooldown(int cooldown) {
        cooldownEnd = player.worldObj.getTotalWorldTime() + cooldown;
    }

    public int getLastClickedEntityID() {
        return lastClickedEntityID;
    }

    public void setLastClickedEntityID(int id) {
        lastClickedEntityID = id;
    }

    public TObjectIntHashMap<Viewport> getViewports() {
        return viewportIds;
    }

    private static final String COOLDOWN     = "cooldown";
    private static final String LAST_CLICKED = "last.clicked.entity.id";

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        nbt.setLong(COOLDOWN, cooldownEnd);
        nbt.setInteger(LAST_CLICKED, lastClickedEntityID);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        cooldownEnd = nbt.getLong(COOLDOWN);
        lastClickedEntityID = nbt.getInteger(LAST_CLICKED);
    }

    @Override
    public void init(Entity entity, World world) {
    }

    @Override
    public int dimension() {
        return player.dimension;
    }

    @Override
    public double x() {
        return player.posX;
    }

    @Override
    public double y() {
        return player.posY;
    }

    @Override
    public double z() {
        return player.posZ;
    }

    @Override
    public float pitch() {
        return player.rotationPitch;
    }

    @Override
    public float yaw() {
        return player.rotationYaw;
    }

}
