package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.client.gui.memory.handler.Folder;
import de.take_weiland.mods.cameracraft.client.gui.memory.handler.FolderHandler;
import de.take_weiland.mods.cameracraft.client.gui.memory.handler.ImageFile;
import de.take_weiland.mods.cameracraft.client.gui.state.GuiContainerGuiState;
import net.minecraft.inventory.Container;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public abstract class FolderGui<C extends Container> extends GuiContainerGuiState<C> implements FolderHandler {

    protected List<Folder> folders = new ArrayList<>();

    public FolderGui(C container) {
        super(container);
    }

    @Override
    public void allowClicks(int folderID, boolean allow) {
        folders.get(folderID).allowClicks(allow);
    }

    @Override
    public boolean allowsClicks(int folderID) {
        return folders.get(folderID).allowClicks();
    }

    @Override
    public void updateFolders() {
        for (Folder folder : folders) {
            folder.updateFolder();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (Folder f : folders) {
            if (f.matchPosition(mouseX, mouseY)) {
                f.click(mouseX, mouseY);
                return;
            }
        }
    }

    public ImageFile getSelectedFile(int folderID) {
        return folders.get(folderID).getSelectedFile();
    }

    public ImageFile getFileAtPosition(int folderID, int position) {
        for (ImageFile file : folders.get(folderID)) {
            if (file.getPosition() == position) {
                return file;
            }
        }
        return null;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        for (Folder f : folders) {
            if (f.shouldRender()) {
                f.renderFolder();
            }
        }
    }

    public int getStartFileID(int folderID) {
        return folders.get(folderID).getStartFileID();
    }

    public void setStartFileID(int folderID, int start) {
        folders.get(folderID).setStartFileID(start);
    }

    public void clearFiles() {
        for (int i = 0; i < folders.size(); i++) {
            clearFile(i);
        }
    }

    public void clearFile(int folderID) {
        folders.get(folderID).clear();
    }

    @Override
    public void allowClicks(boolean allow) {
        for(Folder folder : folders) {
            folder.allowClicks(allow);
        }
    }
}
