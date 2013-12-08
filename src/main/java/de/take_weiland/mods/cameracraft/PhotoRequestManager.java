package de.take_weiland.mods.cameracraft;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ReportedException;

import com.google.common.collect.MapMaker;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import de.take_weiland.mods.cameracraft.network.PacketRequestPhoto;
import de.take_weiland.mods.commons.util.Scheduler;

public final class PhotoRequestManager {

	private PhotoRequestManager() { }
	
	private static final ConcurrentMap<EntityPlayer, ConcurrentMap<Integer, SettableFuture<BufferedImage>>> listenerMap;
	
	static {
		listenerMap = new MapMaker().weakKeys().makeMap();
	}
	
	private static AtomicInteger nextTransferId = new AtomicInteger(0);
	
	public static ListenableFuture<BufferedImage> requestPhoto(final EntityPlayer player) {
		Integer transferId = Integer.valueOf(nextTransferId.getAndIncrement());
		SettableFuture<BufferedImage> future = SettableFuture.create();
		
		ConcurrentMap<Integer, SettableFuture<BufferedImage>> playerData = listenerMap.get(player);
		if (playerData == null) {
			ConcurrentMap<Integer, SettableFuture<BufferedImage>> newValue = new ConcurrentHashMap<Integer, SettableFuture<BufferedImage>>();
			if (listenerMap.putIfAbsent(player, newValue) == null) {
				playerData = newValue;
			}
		}
		
		if (playerData.putIfAbsent(transferId, future) != null) {
			Scheduler.server().throwInMainThread(new ReportedException(CrashReport.makeCrashReport(new IllegalStateException(), "Duplicate PhotoTransferID")));
			throw new IllegalArgumentException();
		} else {
			new PacketRequestPhoto(transferId.intValue()).sendTo(player);
			return future;
		}
	}

	public static void incomingPhoto(EntityPlayer player, int transferId, BufferedImage image) {
		Map<Integer, SettableFuture<BufferedImage>> playerData = listenerMap.get(player);
		if (playerData != null) {
			SettableFuture<BufferedImage> future = playerData.remove(Integer.valueOf(transferId));
			if (future != null) {
				future.set(image);
			}
		}
	}
}
