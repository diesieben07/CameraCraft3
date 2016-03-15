package de.take_weiland.mods.cameracraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Supplier;

public enum MachineType {
	
	PHOTO_PROCESSOR("processor", TilePhotoProcessor.class, TilePhotoProcessor::new, CCGuis.PHOTO_PROCESSOR),
	PRINTER("printer", TilePrinter.class, TilePrinter::new, CCGuis.PRINTER),
	CAMERA("camera", TileCamera.class, TileCamera::new, CCGuis.CAMERA_PLACED),
	SCANNER("scanner", TileScanner.class, TileScanner::new, CCGuis.SCANNER),
	DARKROOM_TABLE("darkroomTable", TileDarkroomTable.class, TileDarkroomTable::new, CCGuis.DARKROOM_TABLE),
	DRAWING_BOARD("drawing.board", TileDrawingBoard.class, TileDrawingBoard::new, CCGuis.DRAWING_BOARD),
	MEMORY_HANDLER("memory.handler", CCGuis.MEMORY_HANDLER);

	private final String                      name;
	private final Class<? extends TileEntity> teClass;
	private final Supplier<? extends TileEntity> teCstr;
	private final CCGuis                      gui;

    MachineType(String name) {
        this(name, null, null, null);
    }

	MachineType(String name, CCGuis gui) {
		this(name, null, null, gui);
	}
	
	<T extends TileEntity> MachineType(String name, Class<T> teClass, Supplier<T> teCstr, CCGuis gui) {
		this.name = name;
		this.teClass = teClass;
		this.gui = gui;
        this.teCstr = teCstr;
	}

    public static void main(String[] args) {
        System.out.println(PHOTO_PROCESSOR.createTileEntity());
    }

    public String subtypeName() {
		return name;
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
        return teCstr.get();
    }
	
	public static void registerTileEntities() {
		for (MachineType type : values()) {
			if (type.hasTileEntity()) {
				GameRegistry.registerTileEntity(type.teClass, "cameracraft." + type.name);
			}
		}
	}
}
