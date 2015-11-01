package de.take_weiland.mods.cameracraft;

import de.take_weiland.mods.commons.util.Sound;

public enum CCSounds implements Sound {

	CAMERA_CLICK("camera_click"),
	CAMERA_REWIND("camera_rewind");

	private final String name;
	
	CCSounds(String name) {
		this.name = name;
	}

	@Override
	public String getDomain() {
		return CameraCraft.MOD_ID;
	}

	@Override
	public String getName() {
		return name;
	}
	
}
