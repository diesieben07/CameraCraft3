package de.take_weiland.mods.cameracraft.networking;

import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ChunkloadingHandler extends WorldSavedData {

	public static class LoadListener {
		
		@ForgeSubscribe
		public void onChunkLoad(ChunkEvent.Load event) {
			List<ChunkloadListener> waiting = get(event.world).listeners.remove(event.getChunk().getChunkCoordIntPair());
			if (waiting != null) {
				for (ChunkloadListener listener : waiting) {
					listener.onChunkLoad();
				}
			}
		}
		
	}
	
	static {
		MinecraftForge.EVENT_BUS.register(new LoadListener());
	}
	
	private static final String IDENTIFIER = "cameracraft.chunkloader";
	
	public static ChunkloadingHandler get(World world) {
		ChunkloadingHandler handler = (ChunkloadingHandler)world.loadItemData(ChunkloadingHandler.class, IDENTIFIER);
		if (handler == null) {
			handler = new ChunkloadingHandler(IDENTIFIER);
			world.setItemData(IDENTIFIER, handler);
		}
		return handler;
	}
	
	public ChunkloadingHandler(String identifier) {
		super(identifier);
	}
	
	final Map<ChunkCoordIntPair, List<ChunkloadListener>> listeners = Maps.newHashMap();
	
	public void registerListener(int chunkX, int chunkZ, ChunkloadListener listener) {
		ChunkCoordIntPair coords = new ChunkCoordIntPair(chunkX, chunkZ);
		List<ChunkloadListener> listenersPerChunk = listeners.get(coords);
		if (listenersPerChunk == null) {
			listeners.put(coords, (listenersPerChunk = Lists.newArrayList()));
		}
		listenersPerChunk.add(listener);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		throw new IllegalStateException();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		throw new IllegalStateException();
	}
	
}
