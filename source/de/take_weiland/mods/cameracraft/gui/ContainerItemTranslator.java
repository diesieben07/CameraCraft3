package de.take_weiland.mods.cameracraft.gui;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import de.take_weiland.mods.cameracraft.tileentity.TileItemMutator;
import de.take_weiland.mods.commons.gui.AbstractContainer;
import de.take_weiland.mods.commons.gui.AdvancedSlot;

public class ContainerItemTranslator extends AbstractContainer<TileItemMutator> {
	
	private int lastTransmuteTime = -2; // -1 is taken
	
	protected ContainerItemTranslator(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player);
	}
	
	@Override
	protected void addSlots() {
		addSlotToContainer(new AdvancedSlot(inventory, 0, 10, 10));
		addSlotToContainer(new AdvancedSlot(inventory, 1, 50, 10));
	}

	@Override
	public int getMergeTargetSlot(ItemStack stack) {
		return OreDictionary.getOreID(stack) >= 0 ? 0 : -1;
	}

	@Override
	protected boolean enableSyncing() {
		return true;
	}

	@Override
	public boolean prepareSyncData() {
		int newTransmuteTime = inventory.getTransmuteTime();
		boolean syncNeeded = lastTransmuteTime != newTransmuteTime;
		lastTransmuteTime = newTransmuteTime;
		return syncNeeded;
	}

	@Override
	public void writeSyncData(ByteArrayDataOutput out, boolean all) {
		out.writeShort(lastTransmuteTime);
	}

	@Override
	public void readSyncData(ByteArrayDataInput in) {
		inventory.setTransmuteTime(in.readShort());
	}

}
