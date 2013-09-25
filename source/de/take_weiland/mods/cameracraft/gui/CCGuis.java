package de.take_weiland.mods.cameracraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.client.gui.GuiCamera;
import de.take_weiland.mods.cameracraft.client.gui.GuiItemTranslator;
import de.take_weiland.mods.cameracraft.client.gui.GuiPhotoProcessor;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.commons.util.JavaUtils;

public enum CCGuis {

	PHOTO_PROCESSOR,
	ORE_DICTIONARY,
	CAMERA;
	
	static final CCGuis[] VALUES = values(); // don't create a new array each time we switch
	
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
		public Container getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			CCGuis gui = JavaUtils.safeArrayAccess(VALUES, id);
			if (gui == null) {
				return null;
			}
			switch (gui) {
			case PHOTO_PROCESSOR:
				return new ContainerPhotoProcessor(world, x, y, z, player);
			case ORE_DICTIONARY:
				return new ContainerItemTranslator(world, x, y, z, player);
			case CAMERA:
				return new ContainerCamera(CCItem.camera.newInventory(player, player.getCurrentEquippedItem()), player);
			default:
				throw new IncompatibleClassChangeError("Unexpected CCGui Enum!");
			}
		}

		@Override
		public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			Container c = getServerGuiElement(id, player, world, x, y, z);
			if (c == null) {
				return null;
			}
			CCGuis gui = JavaUtils.safeArrayAccess(values(), id);
			if (gui == null) {
				return null;
			}
			
			switch (gui) {
			case PHOTO_PROCESSOR:
				return new GuiPhotoProcessor((ContainerPhotoProcessor) c);
			case ORE_DICTIONARY:
				return new GuiItemTranslator((ContainerItemTranslator) c);
			case CAMERA:
				return new GuiCamera((ContainerCamera) c);
			default:
				throw new IncompatibleClassChangeError("Unexpected CCGui Enum!");
			}
		}
	}
	
}
