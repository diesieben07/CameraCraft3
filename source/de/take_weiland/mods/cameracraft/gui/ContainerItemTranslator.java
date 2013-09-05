package de.take_weiland.mods.cameracraft.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.tileentity.TileItemMutator;
import de.take_weiland.mods.commons.gui.AbstractContainer;
import de.take_weiland.mods.commons.gui.AdvancedSlot;
import de.take_weiland.mods.commons.syncing.Synced;

public class ContainerItemTranslator extends AbstractContainer<TileItemMutator> implements Synced {
	
	@Sync
	private short transmuteTime = -2; // -1 is taken
	
	@Sync
	private short selectedResult = -1;
	
	protected ContainerItemTranslator(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player, 48, 84);
	}
	
	@Override
	protected void addSlots() {
		addSlotToContainer(new AdvancedSlot(inventory, 0, 20, 24));
		addSlotToContainer(new AdvancedSlot(inventory, 1, 78, 24));
	}

	@Override
	public int getMergeTargetSlot(ItemStack stack) {
		return OreDictionary.getOreID(stack) >= 0 ? 0 : -1;
	}

	@Override
	public void downloadSyncedFields() {
		transmuteTime = inventory.getTransmuteTime();
		selectedResult = inventory.getSelectedResult();
	}

	@Override
	public void uploadSyncedFields() {
		inventory.setTransmuteTime(transmuteTime);
		inventory.rawSelectResult(selectedResult);
	}

	@Override
	public void clickButton(Side side, EntityPlayer player, int buttonId) {
		List<ItemStack> results = inventory.getTransmutingResult();
		if (buttonId >= 0 && buttonId < results.size()) {
			inventory.selectResult((short) buttonId);
		}
	}

}
