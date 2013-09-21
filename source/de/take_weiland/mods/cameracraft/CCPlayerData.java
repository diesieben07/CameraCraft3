package de.take_weiland.mods.cameracraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class CCPlayerData implements IExtendedEntityProperties {

	public static final String INDENTIFIER = "cameracraft.playerdata";

	public static CCPlayerData get(EntityPlayer player) {
		return (CCPlayerData)player.getExtendedProperties(INDENTIFIER);
	}
	
	public int nextId() {
		return nextId++;
	}
	
	private static final String NEXT_PHOTO_ID = "nextPhotoId";

	private int nextId;
	
	@Override
	public void saveNBTData(NBTTagCompound nbt) {
		nbt.setInteger(NEXT_PHOTO_ID, nextId);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		nextId = nbt.getInteger(NEXT_PHOTO_ID);
	}
	
	@Override
	public void init(Entity entity, World world) {
	}

}
