package de.take_weiland.mods.cameracraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.commons.util.ItemStacks;

public final class CCEventHandler {

	@ForgeSubscribe
	public void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(CCPlayerData.INDENTIFIER, new CCPlayerData());
		}
	}
	
	@ForgeSubscribe
	public void onBucketUse(FillBucketEvent event) {
		int x = event.target.blockX;
		int y = event.target.blockY;
		int z = event.target.blockZ;
		if (event.world.getBlockId(x, y, z) == CCBlock.alkaline.blockID && event.world.getBlockMetadata(x, y, z) == 0) {
			event.world.setBlockToAir(x, y, z);
			event.setResult(Result.ALLOW);
			event.result = ItemStacks.of(MiscItemType.ALKALINE_BUCKET);
		}
	}
	
}
