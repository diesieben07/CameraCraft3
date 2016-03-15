package de.take_weiland.mods.cameracraft;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.take_weiland.mods.cameracraft.db.DatabaseImpl;
import de.take_weiland.mods.commons.SaveWorldsEvent;
import de.take_weiland.mods.commons.util.Scheduler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.world.WorldEvent;

public final class ForgeEventHandler implements Scheduler.Task {

    public static final ForgeEventHandler INSTANCE = new ForgeEventHandler();

    static volatile DatabaseImpl currentDb;

    @SubscribeEvent(priority = EventPriority.HIGHEST) // highest so that DB is available during normal world load handlers
    public void onWorldLoad(WorldEvent.Load event) {
        if (CameraCraft.serverStartingUp && event.world.provider.dimensionId == 0) {
            setDatabase(new DatabaseImpl(DimensionManager.getCurrentSaveRootDirectory().toPath().resolve("cameracraft")));
        }
    }

    static void setDatabase(DatabaseImpl db) {
        DatabaseImpl old = currentDb;
        currentDb = db;

        if (old != null) {
            old.save();
        }
    }

    @Override
    public boolean execute() {
        DatabaseImpl db = CameraCraft.currentDatabase();
        if (db != null) {
            db.requestCleanup();
        }
        return true;
    }

    @SubscribeEvent
    public void saveWorlds(SaveWorldsEvent event) {
        DatabaseImpl db = CameraCraft.currentDatabase();
        if (db != null) {
            db.save();
        }
    }

	@SubscribeEvent
	public void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(CCPlayerData.INDENTIFIER, new CCPlayerData((EntityPlayer) event.entity));
		}
	}
	
	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event) {
		Entity entity = event.target;
		if(event.entityPlayer != null) {
			CCPlayerData.get(event.entityPlayer).setLastClickedEntityID(entity.getEntityId());
		}
	}
}
