package de.take_weiland.mods.cameracraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.take_weiland.mods.cameracraft.api.CameraCraftApiHandler;

@Mod(modid = "testmod", name = "testmod", version = "1.0")
public class testmod {

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		FMLInterModComms.sendMessage(CameraCraftApiHandler.CAMERACRAFT_MODID, CameraCraftApiHandler.IMC_REQUEST_KEY, "de.take_weiland.mods.cameracraft.testapihandler");
	}

}
