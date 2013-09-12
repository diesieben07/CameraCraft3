package de.take_weiland.mods.cameracraft.item;

public class ItemCamera extends CCItemMultitype<CameraType> {

	public ItemCamera(int defaultId) {
		super("camera", defaultId);
	}

	@Override
	public CameraType[] getTypes() {
		return CameraType.values();
	}

}
