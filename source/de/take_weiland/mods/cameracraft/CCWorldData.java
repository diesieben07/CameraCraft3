package de.take_weiland.mods.cameracraft;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.take_weiland.mods.cameracraft.photo.PhotoData;

public class CCWorldData extends WorldSavedData {

	private static final String IDENTIFIER = "cameracraft_worlddata";
	
	private static final CacheBuilder<Object, Object> CACHE_BUILDER = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES);
	
	private static CacheLoader<String, PhotoData> DATA_LOADER = new CacheLoader<String, PhotoData>() {

		@Override
		public PhotoData load(String key) throws IOException {
			return null;
		}
		
	};
	
	private final LoadingCache<String, PhotoData> photoDataCache = CACHE_BUILDER.build(new CacheLoader<String, PhotoData>() {

		@Override
		public PhotoData load(String photoId) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	});
	
	public PhotoData get(String photoId) throws ExecutionException {
		return photoDataCache.get(photoId);
	}
	
	public CCWorldData(String id) {
		super(id);
	}

	public static CCWorldData get(World world) {
		CCWorldData data = (CCWorldData) world.mapStorage.loadData(CCWorldData.class, IDENTIFIER);
		if (data == null) {
			world.mapStorage.setData(IDENTIFIER, (data = new CCWorldData(IDENTIFIER)));
		}
		return data;
	}
	
	private int nextId = 0;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		nextId = nbt.getInteger("nextId");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("photoId", nextId);
	}
	
	public String nextId() {
		markDirty();
		return "photo_" + nextId++;
	}

}
