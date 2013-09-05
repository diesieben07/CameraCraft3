package de.take_weiland.mods.cameracraft.tileentity;

import java.util.List;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Sides;

public class TileItemMutator extends TileEntityInventory implements ISidedInventory {

	private static final int TRANSMUTE_DURATION = 200;
	
	private short transmuteTime = -1;
	private short selectedResult = -1;
	
	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	protected String getDefaultName() {
		return Blocks.getUnlocalizedName(CCBlock.machines, MachineType.ITEM_MUTATOR);
	}
	
	@Override
	public void updateEntity() {
		if (transmuteTime > 0) {
			transmuteTime--;
		}
		if (Sides.logical(this).isServer()) {
			boolean canTransmute = canTransmute();
			if (!hasSourceItem()) {
				selectedResult = -1;
			}
			
			if (transmuteTime == -1 && canTransmute) {
				startTransmuting();
			} else if (transmuteTime == 0) {
				transmute();
				transmuteTime = -1;
			} else if (transmuteTime > 0 && !canTransmute) {
				transmuteTime = -1;
			}
		}
	}
	
	private void startTransmuting() {
		transmuteTime = TRANSMUTE_DURATION;
	}
	
	private void transmute() {
		if (hasSelectedResult() && hasSourceItem()) {
			List<ItemStack> result = getTransmutingResult();
			ItemStack selected;
			if (result != null && ItemStacks.canMergeFully((selected = result.get(selectedResult)), storage[1])) {
				decrStackSize(0, 1);
				storage[1] = ItemStacks.merge(selected.copy(), storage[1], true);
			}
		}
	}

	private static List<ItemStack> getTransmutingResult(ItemStack item) {
		int oreId = OreDictionary.getOreID(item);
		if (oreId < 0) {
			return null;
		} else {
			List<ItemStack> similarOres = OreDictionary.getOres(Integer.valueOf(oreId));
			return similarOres.isEmpty() ? null : similarOres;
		}
	}
	
	public List<ItemStack> getTransmutingResult() {
		return getTransmutingResult(storage[0]);
	}

	private boolean hasSourceItem() {
		return storage[0] != null;
	}
	
	private boolean hasSelectedResult() {
		return selectedResult >= 0;
	}
	
	public short getSelectedResult() {
		return selectedResult;
	}
	
	public void rawSelectResult(short selectedResult) {
		this.selectedResult = selectedResult;
	}
	
	public void selectResult(short selectedResult) {
		if (selectedResult != this.selectedResult) {
			this.selectedResult = selectedResult;
			transmuteTime = -1;
		}
	}
	
	private boolean canTransmute() {
		List<ItemStack> temp;
		return hasSelectedResult() && hasSourceItem() && (temp = getTransmutingResult()) != null && ItemStacks.canMergeFully(temp.get(selectedResult), storage[1]);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		transmuteTime = nbt.getShort("transmuteTime");
		selectedResult = nbt.getShort("selectedResult");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("transmuteTime", transmuteTime);
		nbt.setShort("selectedResult", selectedResult);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		if (slot == 0) {
			return OreDictionary.getOreID(item) >= 0;
		}
		return false;
	}

	private static final int[] EXTRACT_SLOTS = new int[] { 1 };
	private static final int[] INSERT_SLOTS = new int[] { 0 };
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if (ForgeDirection.getOrientation(side) == ForgeDirection.DOWN) {
			return EXTRACT_SLOTS;
		} else {
			return INSERT_SLOTS;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, int side) {
		return true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		return true;
	}

	public short getTransmuteTime() {
		return transmuteTime;
	}
	
	public int scaleTransmuteProgress(int max) {
		return transmuteTime >= 0 ? MathHelper.floor_float(((float)max / TRANSMUTE_DURATION) * (TRANSMUTE_DURATION - transmuteTime)) : transmuteTime;
	}

	public void setTransmuteTime(short transmuteTime) {
		if (Sides.logical(this).isClient()) {
			this.transmuteTime = transmuteTime;
		}
	}

}
