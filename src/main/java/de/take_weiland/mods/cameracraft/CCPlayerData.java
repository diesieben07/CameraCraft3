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
	}

}
