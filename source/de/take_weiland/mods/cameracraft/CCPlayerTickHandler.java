package de.take_weiland.mods.cameracraft;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class CCPlayerTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		CCPlayerData.get((EntityPlayer)tickData[0]).onUpdate();
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

	private static final EnumSet<TickType> TICKS = EnumSet.of(TickType.PLAYER);
	
	@Override
	public EnumSet<TickType> ticks() {
		return TICKS;
	}

	@Override
	public String getLabel() {
		return "CCPlayerTicks";
	}

}
