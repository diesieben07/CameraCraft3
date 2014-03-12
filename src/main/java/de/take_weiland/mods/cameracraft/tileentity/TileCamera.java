package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.CCSounds;
import de.take_weiland.mods.commons.tileentity.AbstractTileEntity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author diesieben07
 */
public class TileCamera extends AbstractTileEntity {

	private boolean isPowered;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isPowered = nbt.getBoolean("powered");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("powered", isPowered);
	}

	public boolean isPowered() {
		return isPowered;
	}

	public void onPowerStateChange(boolean isPowered) {
		this.isPowered = isPowered;
		if (isPowered) {
			CCSounds.CAMERA_CLICK.playAt(worldObj, xCoord, yCoord, zCoord);
		}
	}
}
