package de.take_weiland.mods.cameracraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.tileentity.TileItemMutator;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.templates.Type;

public enum MachineType implements Type {
	
	PHOTO_PROCESSOR("processor", TilePhotoProcessor.class, CCGuis.PHOTO_PROCESSOR),
	ITEM_MUTATOR("oreDictionary", TileItemMutator.class, CCGuis.ORE_DICTIONARY);

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
	
	public void openGui(EntityPlayer player, int x, int y, int z) {
		if (gui != null) {
			gui.open(player, x, y, z);
		}
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getMeta() {
		return ordinal();
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

}
