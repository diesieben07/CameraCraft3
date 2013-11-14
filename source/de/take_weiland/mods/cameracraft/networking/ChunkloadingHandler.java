package de.take_weiland.mods.cameracraft.networking;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.EventBus;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.IEventListener;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import de.take_weiland.mods.commons.util.JavaUtils;

public final class ChunkloadingHandler {

	private ChunkloadingHandler() { }
	
	@SuppressWarnings("boxing")
	static final int EVENT_BUS_BUSID = ReflectionHelper.getPrivateValue(EventBus.class, MinecraftForge.EVENT_BUS, "busID");
	
	static ChunkEvent.Load DUMMY_EVENT;
	
	static {
		try {
			DUMMY_EVENT = ChunkEvent.Load.class.newInstance();
		} catch (ReflectiveOperationException e) {
			JavaUtils.throwUnchecked(e);
		}
	}
	
	public static void register(final World world, final int chunkX, final int chunkZ, final ChunkloadListener listener) {
		DUMMY_EVENT.getListenerList().register(EVENT_BUS_BUSID, EventPriority.NORMAL, new IEventListener() {
			
			@Override
			public void invoke(Event event) {
				ChunkEvent.Load evt = (ChunkEvent.Load)event;
				if (evt.world == world && evt.getChunk().xPosition == chunkX && evt.getChunk().zPosition == chunkZ) {
					listener.onChunkLoad();
					DUMMY_EVENT.getListenerList().unregister(EVENT_BUS_BUSID, this);
				}
			}
		});
	}
	
}
