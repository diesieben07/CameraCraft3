package de.take_weiland.mods.cameracraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class CCWorldData extends WorldSavedData {

	private static final String IDENTIFIER = "cameracraft_worlddata";
	
	public CCWorldData(String id) {
		super(id);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
	}
	
	public static CCWorldData get(World world) {
		CCWorldData data = (CCWorldData) world.mapStorage.loadData(CCWorldData.class, IDENTIFIER);
		if (data == null) {
			world.mapStorage.setData(IDENTIFIER, (data = new CCWorldData(IDENTIFIER)));
		}
		return data;
	}

}
