package de.take_weiland.mods.cameracraft;

import de.take_weiland.mods.cameracraft.api.camera.Viewport;
import de.take_weiland.mods.cameracraft.api.camera.ViewportProvider;
import de.take_weiland.mods.cameracraft.api.camera.ViewportProviderFactory;
import de.take_weiland.mods.commons.util.Players;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

/**
 * @author diesieben07
 */
public final class PlayerViewportFactory implements ViewportProviderFactory {

    public static final String IDENTIFIER = "players";

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override
    public ViewportProvider getProvider(Viewport viewport) {
        // find player with the least attached viewports
        List<EntityPlayerMP> all = Players.getAll();

        CCPlayerData least = null;
        int leastCount = 0;
        for (int i = 0, len = all.size(); i < len; i++) {
            EntityPlayerMP player = all.get(i);
            CCPlayerData data = CCPlayerData.get(player);
            int size = data.getViewports().size();
            if (least == null || leastCount > size) {
                least = data;
                leastCount = size;
            }
        }

//        above is unrolled version of:
//        Players.getAll().stream()
//                .map(CCPlayerData::get)
//                .min(Comparator.comparingInt(data -> data.getViewports().size()))
//                .orElse(null);

        return least;
    }

}
