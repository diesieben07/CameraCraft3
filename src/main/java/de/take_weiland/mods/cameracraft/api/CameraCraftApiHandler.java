package de.take_weiland.mods.cameracraft.api;

public interface CameraCraftApiHandler {

	String IMC_REQUEST_KEY = "RequestApi";
	String CAMERACRAFT_MODID = "CameraCraft";
	
	void injectApi(CameraCraftApi api);
	
}
