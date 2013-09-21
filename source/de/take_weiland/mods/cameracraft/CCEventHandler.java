package de.take_weiland.mods.cameracraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent;

public final class CCEventHandler {

	@ForgeSubscribe
	public void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(CCPlayerData.INDENTIFIER, new CCPlayerData());
		}
	}
	
}
