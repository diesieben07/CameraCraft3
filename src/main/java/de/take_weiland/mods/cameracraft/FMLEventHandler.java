package de.take_weiland.mods.cameracraft;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author diesieben07
 */
public class FMLEventHandler {

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        handleLogout(event.player);
    }

    public static void handleLogout(EntityPlayer player) {
        CCPlayerData.get(player).getViewports().forEach(v -> {
            v.providerInvalidated();
            return true;
        });
    }

}
