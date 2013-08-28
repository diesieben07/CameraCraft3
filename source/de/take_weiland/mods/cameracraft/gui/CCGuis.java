package de.take_weiland.mods.cameracraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.client.gui.GuiItemTranslator;
import de.take_weiland.mods.cameracraft.client.gui.GuiPhotoProcessor;
import de.take_weiland.mods.commons.util.CollectionUtils;

public enum CCGuis {

	PHOTO_PROCESSOR, ORE_DICTIONARY;
	
	public void open(EntityPlayer player, int x, int y, int z) {
		player.openGui(CameraCraft.instance, ordinal(), player.worldObj, x, y, z);
	}
	
	public static class Handler implements IGuiHandler {
		@Override
		public Container getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			CCGuis gui = CollectionUtils.safeArrayAccess(values(), id);
			if (gui == null) {
				return null;
			}
			switch (gui) {
			case PHOTO_PROCESSOR:
				return new ContainerPhotoProcessor(world, x, y, z, player);
			case ORE_DICTIONARY:
				return new ContainerItemTranslator(world, x, y, z, player);
			default:
				return null;
			}
		}

		@Override
		public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			Container c = getServerGuiElement(id, player, world, x, y, z);
			if (c == null) {
				return null;
			}
			CCGuis gui = CollectionUtils.safeArrayAccess(values(), id);
			if (gui == null) {
				return null;
			}
			
			switch (gui) {
			case PHOTO_PROCESSOR:
				return new GuiPhotoProcessor((ContainerPhotoProcessor) c);
			case ORE_DICTIONARY:
				return new GuiItemTranslator((ContainerItemTranslator) c);
			default:
				return null;
			}
		}
	}
	
}
