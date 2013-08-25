package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.tileentity.TileEntity;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.templates.Type;

public enum MachineType implements Type {
	PHOTO_PROCESSOR("processor", TilePhotoProcessor.class);

	private final String name;
	private final Class<? extends TileEntity> teClass;
	
	private MachineType(String name, Class<? extends TileEntity> teClass) {
		this.name = name;
		this.teClass = teClass;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getMeta() {
		return ordinal();
	}
	
	public TileEntity createTileEntity() {
		try {
			return teClass.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Failed to create TileEntity instance for " + this + "!", e);
		}
	}

}
