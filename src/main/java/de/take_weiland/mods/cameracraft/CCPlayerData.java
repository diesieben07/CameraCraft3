package de.take_weiland.mods.cameracraft;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CCPlayerData implements IExtendedEntityProperties {

	public static final String INDENTIFIER = "cameracraft.playerdata";

	public static CCPlayerData get(EntityPlayer player) {
		return (CCPlayerData)player.getExtendedProperties(INDENTIFIER);
	}

	private EntityPlayer player;
	private int nextTransferId = 0;
	private ConcurrentMap<Integer, PhotoRequest> requests = new ConcurrentHashMap<Integer, PhotoRequest>();

	public int nextTransferId() {
		return nextTransferId++;
	}

	public ListenableFuture<BufferedImage> requestStandardPhoto() {
		int transferId = nextTransferId++;
		PhotoRequest request = new StandardPhotoRequest(transferId);
		if ((requests.putIfAbsent(Integer.valueOf(transferId), request)) != null) {
			// should never happen, but just in case
			throw new ReportedException(CrashReport.makeCrashReport(new IllegalStateException(), "Duplicate PhotoTransferID"));
		}
		request.send(player);
		return request.getFuture();
	}

	public void onPhoto(int transferId, BufferedImage image) {
		PhotoRequest request = requests.remove(Integer.valueOf(transferId));
		if (request == null) {
			CameraCraft.logger.warn(String.format("Unknown transferId %d by %s!", transferId, player));
		} else {
			request.setImage(image);
		}
	}

	public boolean isOnCooldown() {
		return cooldown > 0;
	}
	
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	public void onUpdate() {
		if (cooldown > 0) {
			cooldown--;
		}
	}
	
	private static final String COOLDOWN = "cooldown";

	private int cooldown = 0;
	
	@Override
	public void saveNBTData(NBTTagCompound nbt) {
		nbt.setInteger(COOLDOWN, cooldown);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		cooldown = nbt.getInteger(COOLDOWN);
	}
	
	@Override
	public void init(Entity entity, World world) {
		player = (EntityPlayer) entity;
	}

}
