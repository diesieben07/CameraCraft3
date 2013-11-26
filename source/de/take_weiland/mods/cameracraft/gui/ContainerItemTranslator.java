package de.take_weiland.mods.cameracraft.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.tileentity.TileItemMutator;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;

public class ContainerItemTranslator extends AbstractContainer.Synced<TileItemMutator> {
	
	protected ContainerItemTranslator(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player, 48, 84);
	}
	
	@Override
	protected void addSlots() {
		addSlotToContainer(new AdvancedSlot(inventory, 0, 20, 24));
		addSlotToContainer(new AdvancedSlot(inventory, 1, 78, 24));
	}

	@Override
	public int getSlotFor(ItemStack stack) {
		return OreDictionary.getOreID(stack) >= 0 ? 0 : -1;
	}

	@Override
	public boolean writeSyncData(DataOutputStream out) throws IOException {
		out.writeShort(inventory.getTransmuteTime());
		out.writeShort(inventory.getSelectedResult());
		return true;
	}

	@Override
	public void readSyncData(DataInputStream in) throws IOException {
		inventory.setTransmuteTime(in.readShort());
		inventory.rawSelectResult(in.readShort());
	}

	@Override
	public void onButtonClick(Side side, EntityPlayer player, int buttonId) {
		List<ItemStack> results = inventory.getTransmutingResult();
		if (buttonId >= 0 && buttonId < results.size()) {
			inventory.selectResult((short) buttonId);
		}
	}

}
