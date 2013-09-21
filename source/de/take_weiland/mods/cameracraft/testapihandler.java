package de.take_weiland.mods.cameracraft;

import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.CameraCraftApiHandler;

public class testapihandler implements CameraCraftApiHandler {

	@Override
	public void injectApi(CameraCraftApi api) {
		System.out.println("received api!" + api);
	}

}
