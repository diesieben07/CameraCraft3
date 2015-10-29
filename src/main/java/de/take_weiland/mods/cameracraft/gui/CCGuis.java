package de.take_weiland.mods.cameracraft.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.client.gui.*;
import de.take_weiland.mods.cameracraft.client.gui.printer.GuiPrinter;
import de.take_weiland.mods.cameracraft.inv.InventoryCameraImpl;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.commons.util.EnumUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public enum CCGuis {

	PHOTO_PROCESSOR,
	CAMERA,
	PRINTER,
	PRINTER_ADVANCED,
	PHOTO,
	SCANNER,
	CAMERA_PLACED,
    SET_STREAM_ID,
    PEN;
	
	public void open(EntityPlayer player) {
		open(player, 0, 0, 0);
	}
	
	public void open(EntityPlayer player, int data) {
		open(player, data, 0, 0);
	}
	
	public void open(EntityPlayer player, int x, int y, int z) {
		player.openGui(CameraCraft.instance, ordinal(), player.worldObj, x, y, z);
	}
	
	public static class Handler implements IGuiHandler {
		
		@Override
		public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			CCGuis gui = EnumUtils.byOrdinal(CCGuis.class, id);

			return gui == null ? null : getContainer0(gui, player, world, x, y, z);
		}

		private Container getContainer0(CCGuis gui, EntityPlayer player, World world, int x, int y, int z) {
            switch (gui) {
                case PHOTO_PROCESSOR:
                    return new ContainerPhotoProcessor(world, x, y, z, player);
                case CAMERA:
                    InventoryCameraImpl camera = (InventoryCameraImpl) CCItem.camera.createCamera(player);
                    return new ContainerCamera(camera, player, camera.hasLid() ? camera.storageSlot() : -1);
                case PRINTER:
                    return new ContainerPrinter(world, x, y, z, player);
                case PRINTER_ADVANCED:
                    return new ContainerPrinter(world, x, y, z, player);
                case SCANNER:
                    return new ContainerScanner(world, x, y, z, player);
                case PEN:
                    return null;
                case SET_STREAM_ID:
                    return null;
                default:
                    throw new IncompatibleClassChangeError("Unexpected CCGui Enum!");
            }
		}

		@Override
		public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            CCGuis gui = EnumUtils.byOrdinal(CCGuis.class, id);
            if (gui == null) {
                return null;
            }
            Container c = getContainer0(gui, player, world, x, y, z);

            switch (gui) {
                case PHOTO_PROCESSOR:
                    return new GuiPhotoProcessor((ContainerPhotoProcessor) c);
                case CAMERA:
                    return new GuiCamera((ContainerCamera) c);
                case PRINTER:
                case PRINTER_ADVANCED:
                    return new GuiPrinter((ContainerPrinter) c);
                case SCANNER:
                    return new GuiScanner((ContainerScanner) c);
                case PEN:
                    return new GuiPen();
                case SET_STREAM_ID:
                    return new GuiStreamID();
                default:
                    throw new IncompatibleClassChangeError("Unexpected CCGui Enum!");
			}
		}
	}
	
}
