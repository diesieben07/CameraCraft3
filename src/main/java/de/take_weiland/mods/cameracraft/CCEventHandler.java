package de.take_weiland.mods.cameracraft;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.cameracraft.photo.DatabaseImpl;
import de.take_weiland.mods.commons.SaveWorldsEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public final class CCEventHandler {

	@SubscribeEvent
	public void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(CCPlayerData.INDENTIFIER, new CCPlayerData((EntityPlayer) event.entity));
		}
	}
	
	@SubscribeEvent
	public void onBucketUse(FillBucketEvent event) {
		int x = event.target.blockX;
		int y = event.target.blockY;
		int z = event.target.blockZ;
		if (event.world.getBlock(x, y, z) == CCBlock.alkaline && event.world.getBlockMetadata(x, y, z) == 0) {
			event.world.setBlockToAir(x, y, z);
			event.setResult(Event.Result.ALLOW);
			event.result = CCItem.misc.getStack(MiscItemType.ALKALINE_BUCKET);
		}
	}

    @SubscribeEvent
    public void onWorldSave(SaveWorldsEvent event) {
        DatabaseImpl.current.save();
    }


}
