package de.take_weiland.mods.cameracraft.api;

public interface CameraCraftApiHandler {

	public static final String IMC_REQUEST_KEY = "RequestApi";
	public static final String CAMERACRAFT_MODID = "CameraCraft";
	
	void injectApi(CameraCraftApi api);
	
}
