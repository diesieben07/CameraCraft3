package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.client.gui.*;
import de.take_weiland.mods.cameracraft.client.gui.memory.handler.FolderGuiMemoryHandler;
import de.take_weiland.mods.cameracraft.client.gui.printer.GuiPrinter;
import de.take_weiland.mods.cameracraft.inv.InventoryCameraImpl;
import de.take_weiland.mods.cameracraft.inv.InventoryDrawingBoard;
import de.take_weiland.mods.cameracraft.inv.InventoryMemoryHandler;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.commons.util.GuiIdentifier;
import net.minecraft.entity.player.EntityPlayer;

public enum CCGuis implements GuiIdentifier {

	PHOTO_PROCESSOR,
	CAMERA,
	PRINTER,
	PHOTO,
	SCANNER,
	CAMERA_PLACED,
    SET_STREAM_ID,
    PEN,
    DRAWING_BOARD,
    MEMORY_HANDLER,
    DARKROOM_TABLE
    ;

    @Override
    public Object mod() {
        return CameraCraft.instance;
    }

    public static void register() {
        GuiIdentifier.builder()
                .add(PHOTO_PROCESSOR, ContainerPhotoProcessor::new, () -> GuiPhotoProcessor::new)
                .add(CAMERA, CCGuis::createCameraContainer, () -> GuiCamera::new)
                .add(PRINTER, ContainerPrinter::new, () -> GuiPrinter::new)
                .add(SCANNER, ContainerScanner::new, () -> GuiScanner::new)
                .add(PEN, () -> GuiPen::new)
                .add(SET_STREAM_ID, () -> GuiStreamID::new)
                .add(DRAWING_BOARD, (player, world, x, y, z) -> new ContainerDrawingBoard(player, new InventoryDrawingBoard(), x, y, z), () -> GuiDrawingBoard::new)
                .add(MEMORY_HANDLER, (player, world, x, y, z) -> new ContainerMemoryHandler(player, new InventoryMemoryHandler(), x, y, z), () -> FolderGuiMemoryHandler::new)
                .add(DARKROOM_TABLE, ContainerDarkroomTable::new, () -> GuiDarkroomTable::new)
                .done();
    }

    private static ContainerCamera createCameraContainer(EntityPlayer player) {
        InventoryCameraImpl camera = (InventoryCameraImpl) CCItem.camera.createCamera(player);
        return new ContainerCamera(camera, player, camera.hasLid() ? camera.storageSlot() : -1);
    }

}
