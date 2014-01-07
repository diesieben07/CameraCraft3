package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.tileentity.TileCardReader;
import de.take_weiland.mods.cameracraft.tileentity.TileItemMutator;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.cameracraft.tileentity.TileScanner;
import de.take_weiland.mods.commons.templates.Metadata.BlockMeta;

public enum MachineType implements BlockMeta {
	
	PHOTO_PROCESSOR("processor", TilePhotoProcessor.class, CCGuis.PHOTO_PROCESSOR),
	ITEM_MUTATOR("oreDictionary", TileItemMutator.class, CCGuis.ORE_DICTIONARY),
	CARD_READER("cardReader", TileCardReader.class, CCGuis.CARD_READER),
	PRINTER("printer", TilePrinter.class, CCGuis.PRINTER),
	SCANNER("scanner", TileScanner.class, CCGuis.SCANNER);

	private final String name;
	private final Class<? extends TileEntity> teClass;
	private final CCGuis gui;
	
	private MachineType(String name, CCGuis gui) {
		this(name, null, gui);
	}
	
	private MachineType(String name, Class<? extends TileEntity> teClass, CCGuis gui) {
		this.name = name;
		this.teClass = teClass;
		this.gui = gui;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Block getBlock() {
		return CCBlock.machines;
	}
	
	public void openGui(EntityPlayer player, int x, int y, int z) {
		if (gui != null) {
			gui.open(player, x, y, z);
		}
	}

	public boolean hasTileEntity() {
		return teClass != null;
	}
	
	public TileEntity createTileEntity() {
		try {
			return teClass.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Failed to create TileEntity instance for " + this + "!", e);
		}
	}
	
	public static void registerTileEntities() {
		for (MachineType type : values()) {
			if (type.hasTileEntity()) {
				GameRegistry.registerTileEntity(type.teClass, "cameracraft." + type.name);
			}
		}
	}

	public boolean canCableConnect(ForgeDirection side, de.take_weiland.mods.cameracraft.api.cable.CableType type) {
		return type == CableType.POWER || (teClass != null && NetworkTile.class.isAssignableFrom(teClass));
	}

}
