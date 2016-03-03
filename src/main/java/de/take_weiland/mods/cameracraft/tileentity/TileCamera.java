package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.CCSounds;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.camera.Viewport;
import de.take_weiland.mods.cameracraft.api.camera.ViewportProvider;
import de.take_weiland.mods.commons.nbt.ToNbt;
import de.take_weiland.mods.commons.util.Scheduler;
import net.minecraft.tileentity.TileEntity;

import java.util.concurrent.CompletionStage;

/**
 * @author diesieben07
 */
public class TileCamera extends TileEntity implements Viewport {

	@ToNbt(key = "powered")
	private boolean isPowered;

    private ViewportProvider viewportProvider;

    @Override
    public void invalidate() {
        super.invalidate();
        if (viewportProvider != null) {
            viewportProvider.detach(this);
        }
    }

    @Override
    public void providerInvalidated() {
        viewportProvider.detach(this);
        chooseProvider();
    }

    private void chooseProvider() {
        viewportProvider = CameraCraft.api.getProvider(this);
        viewportProvider.attach(this);
    }

    public boolean isPowered() {
		return isPowered;
	}

	public void onPowerStateChange(boolean isPowered) {
		this.isPowered = isPowered;
		if (isPowered) {
			CCSounds.CAMERA_CLICK.play(worldObj, xCoord, yCoord, zCoord);
            if (viewportProvider == null) {
                chooseProvider();
            }
            CompletionStage<Long> future = CameraCraft.api.takePhoto(this);
            future.whenCompleteAsync((id, x) -> System.out.printf("Saved image as id %s, exception is %s\n", id, x), Scheduler.server());
        }
	}

    @Override
    public ViewportProvider getProvider() {
        return viewportProvider;
    }

    @Override
    public int dimension() {
        return worldObj.provider.dimensionId;
    }

    @Override
    public double x() {
        return xCoord + 0.5;
    }

    @Override
    public double y() {
        return yCoord + 0.5;
    }

    @Override
    public double z() {
        return zCoord + 0.5;
    }

    @Override
    public float pitch() {
        return 0;
    }

    @Override
    public float yaw() {
        return 0;
    }
}
