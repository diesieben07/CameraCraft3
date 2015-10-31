package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.CCSounds;
import de.take_weiland.mods.commons.nbt.ToNbt;
import net.minecraft.tileentity.TileEntity;

/**
 * @author diesieben07
 */
public class TileCamera extends TileEntity {

	@ToNbt(key = "powered")
	private boolean isPowered;

	public boolean isPowered() {
		return isPowered;
	}

	public void onPowerStateChange(boolean isPowered) {
		this.isPowered = isPowered;
		if (isPowered) {
			CCSounds.CAMERA_CLICK.play(worldObj, xCoord, yCoord, zCoord);
		}
	}
}
