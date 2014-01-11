package de.take_weiland.mods.cameracraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.PhotoStorageProvider;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.networking.NetworkNodeImpl;
import de.take_weiland.mods.commons.templates.NameableTileEntity;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.Multitypes;

public class TileScanner extends TileEntityInventory<TileScanner> implements NetworkTile, NameableTileEntity, PhotoStorageProvider {

	private final NetworkNodeImpl node = new NetworkNodeImpl(this);
	
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof PhotoStorageItem && ((PhotoStorageItem) item).isScannable(stack);
	}

	@Override
	protected String getDefaultName() {
		return Multitypes.fullName(MachineType.SCANNER);
	}

	@Override
	public PhotoStorage getPhotoStorage() {
		ItemStack storage = getPhotoStorageItem();
		return storage == null ? null : ((PhotoStorageItem) storage.getItem()).getPhotoStorage(storage);
	}
	
	private ItemStack getPhotoStorageItem() {
		ItemStack item = storage[0];
		return item != null && item.getItem() instanceof PhotoStorageItem ? item : null;
	}

	@Override
	public String getDisplayName() {
		return "Scanner [x=" + xCoord + ", y=" + yCoord + ", z=" + zCoord + "]";
	}

	@Override
	public NetworkNode getNode() {
		return node;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		node.update();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		node.shutdown();
	}

}