package de.take_weiland.mods.cameracraft;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import static de.take_weiland.mods.commons.util.Sides.sideOf;

public class CCPlayerTickHandler {

	@SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && sideOf(event.player).isServer()) {
            CCPlayerData.get(event.player).onUpdate();
        }
    }

}
