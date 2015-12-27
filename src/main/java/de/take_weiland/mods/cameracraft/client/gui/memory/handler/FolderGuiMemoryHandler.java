package de.take_weiland.mods.cameracraft.client.gui.memory.handler;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.client.gui.FolderGui;
import de.take_weiland.mods.cameracraft.client.gui.state.GuiStateContainer;
import de.take_weiland.mods.cameracraft.gui.ContainerMemoryHandler;
import de.take_weiland.mods.cameracraft.network.PacketMemoryHandlerDeletePicture;
import de.take_weiland.mods.cameracraft.network.PacketMemoryHandlerRename;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;
import de.take_weiland.mods.commons.inv.SimpleGuiButton;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class FolderGuiMemoryHandler extends FolderGui<ContainerMemoryHandler> {

    protected GuiTextField renameField;

    private ItemStack[] prevStack = new ItemStack[2];
    private int requestedSide;
    private int showingPhotoFile = -1;

    private List<Folder> helpFolders = new ArrayList<>();

    public FolderGuiMemoryHandler(ContainerMemoryHandler container) {
        super(container);
    }

    public void initGui() {
        super.initGui();

        renameField = Guis.addTextField(this, new GuiTextField(fontRendererObj, width / 2 - 50, height / 2 - 10, 100, 20));


        GuiButton[] buttons1 = new GuiButton[]{
                new SimpleGuiButton(0, width / 2 - 63, height / 2 - 23, 126, 20, "Use this Memory Card"),
        };

        guiStates.add(new GuiStateContainer(0, container, this, provideTexture(), new int[]{0, 1}, buttons1, true));

        GuiButton[] buttons2 = new GuiButton[]{
                new SimpleGuiButton(0, guiLeft - 20, guiTop + 126, 20, 20, "^"),
                new SimpleGuiButton(1, guiLeft - 20, guiTop + 146, 20, 20, "v"),

                new SimpleGuiButton(2, guiLeft + 176, guiTop, 50, 20, "Open"),
                new SimpleGuiButton(3, guiLeft + 176, guiTop + 20, 50, 20, "Rename"),
                new SimpleGuiButton(4, guiLeft + 176, guiTop + 40, 50, 20, "Delete"),

                new SimpleGuiButton(5, width / 2 - 170, height / 2 - 10, 20, 20, EnumChatFormatting.GREEN + "OK"),
                new SimpleGuiButton(6, width / 2 - 150, height / 2 - 10, 300, 20, EnumChatFormatting.RED + "CANCEL"),

                new SimpleGuiButton(7, width / 2 - 25, height / 2 + 10, 50, 20, "Rename!"),
        };

        guiStates.add(new GuiStateContainer(1, container, this, null, new int[]{0}, buttons2, false, -1, -1, new int[]{5, 6, 7}, new int[]{}));

        SimpleGuiButton[] buttons20 = new SimpleGuiButton[]{
                new SimpleGuiButton(0, guiLeft - 20, guiTop + 126, 20, 20, "^"),
                new SimpleGuiButton(1, guiLeft - 20, guiTop + 146, 20, 20, "v"),

                new SimpleGuiButton(2, guiLeft + 229, guiTop + 126, 20, 20, "^"),
                new SimpleGuiButton(3, guiLeft + 229, guiTop + 146, 20, 20, "v"),

                new SimpleGuiButton(4, guiLeft - 50, guiTop, 50, 20, "Rename"),
                new SimpleGuiButton(5, guiLeft - 50, guiTop + 20, 50, 20, "Delete"),

                new SimpleGuiButton(6, guiLeft + 229, guiTop, 50, 20, "Rename"),
                new SimpleGuiButton(7, guiLeft + 229, guiTop + 20, 50, 20, "Delete"),

                new SimpleGuiButton(8, width / 2 - 170, height / 2 - 10, 20, 20, EnumChatFormatting.GREEN + "OK"),
                new SimpleGuiButton(9, width / 2 - 150, height / 2 - 10, 300, 20, EnumChatFormatting.RED + "CANCEL"),

                new SimpleGuiButton(10, width / 2 - 25, height / 2 + 10, 50, 20, "Rename!"),
        };

        guiStates.add(new GuiStateContainer(2, container, this, null, new int[]{0, 1}, buttons20, false, -1, -1, new int[]{8, 9, 10}, new int[]{}, 229, 166));

        SimpleGuiButton[] button3 = new SimpleGuiButton[]{
                new SimpleGuiButton(0, (width - computePhotoDim()) / 2 - 50, height / 2 - 10, 50, 20, "Prev"),
                new SimpleGuiButton(1, (width - computePhotoDim()) / 2 + computePhotoDim(), height / 2 - 10, 50, 20, "Next"),

                new SimpleGuiButton(2, (width - computePhotoDim()) / 2, (height - computePhotoDim()) / 2 + computePhotoDim(), computePhotoDim() / 3, 20, "Delete"),
                new SimpleGuiButton(3, (width - computePhotoDim()) / 2 + computePhotoDim() / 3, (height - computePhotoDim()) / 2 + computePhotoDim(), computePhotoDim() / 3, 20, "Rename"),
                new SimpleGuiButton(4, (width - computePhotoDim()) / 2 + computePhotoDim() / 3 * 2, (height - computePhotoDim()) / 2 + computePhotoDim(), computePhotoDim() / 3, 20, "Back"),

                new SimpleGuiButton(5, width / 2 - 170, height / 2 - 10, 20, 20, EnumChatFormatting.GREEN + "OK"),
                new SimpleGuiButton(6, width / 2 - 150, height / 2 - 10, 300, 20, EnumChatFormatting.RED + "CANCEL"),

                new SimpleGuiButton(7, width / 2 - 25, height / 2 + 10, 50, 20, "Rename!"),
        };

        guiStates.add(new GuiStateContainer(3, container, this, null, new int[]{}, button3, false, 0, 0, new int[]{5, 6, 7}, new int[]{}));

        initGuiState();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        if (activeGuiState == 1 || activeGuiState == 3) {
            if (getCurrentGuiState().buttonList.get(5).xPosition > 0) {
                drawCenteredString(fontRendererObj, EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "Do you really want to delete this picture?", width / 2 - guiLeft, height / 2 - 50 - guiTop, 0);
                drawCenteredString(fontRendererObj, EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "THIS CANNOT BE UNDONE!", width / 2 - guiLeft, height / 2 - 40 - guiTop, 0);
            }
        } else if(activeGuiState == 2) {
            if(getCurrentGuiState().buttonList.get(8).xPosition > 0) {
                drawCenteredString(fontRendererObj, EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "Do you really want to delete this picture?", width / 2 - guiLeft, height / 2 - 50 - guiTop, 0);
                drawCenteredString(fontRendererObj, EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "THIS CANNOT BE UNDONE!", width / 2 - guiLeft, height / 2 - 40 - guiTop, 0);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        if (activeGuiState == 3) {

            int size = computePhotoDim();

            int x = (width - size) / 2;
            int y = (height - size) / 2;

            drawRect(x, y, x + size, y + size, 0xffffff00);

            if (showingPhotoFile != -1) {
                PhotoDataCache.bindTexture(folders.get(0).getFileAt(showingPhotoFile).photoID);
                GL11.glColor3f(1, 1, 1);
                Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);
                drawCenteredString(fontRendererObj, folders.get(0).getFileAt(showingPhotoFile).getPhotoName(container.getSlot(0).getStack()), width / 2, 4, Color.red.getRGB());
            } else {
                setGuiState(1);
            }
        }
    }

    @Override
    protected void buttonPressed(int activeGuiState, GuiButton button) {
        updateFolders();
        if (activeGuiState == 0) {
            if (button.id == 0) {
                if (container.getSlot(0).getStack() != null && container.getSlot(1).getStack() != null) {
                    setGuiState(2);
                } else {
                    setGuiState(1);
                }
            }
        } else if (activeGuiState == 1) {
            switch (button.id) {
                case 0:
                    if (getStartFileID(0) > 0) {
                        setStartFileID(0, getStartFileID(0) - 1);
                    }
                    break;
                case 1:
                    if (getStartFileID(0) < folders.get(0).size() - 8) {
                        setStartFileID(0, getStartFileID(0) + 1);
                    }
                    break;
                case 2:
                    showingPhotoFile = getSelectedFile(0).getIndex();
                    setGuiState(3);
                    break;
                case 3:
                    getCurrentGuiState().hideButton(7, false);
                    allowClicks(false);
                    renameField.setText(getSelectedFile(0).getPhotoName(container.getSlot(0).getStack()));
                    break;
                case 4:
                    allowClicks(false);
                    getCurrentGuiState().hideButton(5, false);
                    getCurrentGuiState().hideButton(6, false);
                    break;
                case 5:
                    new PacketMemoryHandlerDeletePicture(getSelectedFile(0).getIndex(), container.windowId, 0).sendToServer();
                    getCurrentGuiState().hideButton(5, true);
                    getCurrentGuiState().hideButton(6, true);
                    allowClicks(true);
                    break;
                case 6:
                    getCurrentGuiState().hideButton(5, true);
                    getCurrentGuiState().hideButton(6, true);
                    allowClicks(true);
                    break;
                case 7:
                    getCurrentGuiState().hideButton(7, true);
                    allowClicks(true);
                    new PacketMemoryHandlerRename(getSelectedFile(0).getIndex(), renameField.getText(), container.windowId, 0).sendToServer();
                    break;
            }
        } else if (activeGuiState == 2) {
            switch (button.id) {
                case 0:
                    if (getStartFileID(0) > 0) {
                        setStartFileID(0, getStartFileID(0) - 1);
                    }
                    break;
                case 1:
                    if (getStartFileID(0) < folders.get(0).size() - 8) {
                        setStartFileID(0, getStartFileID(0) + 1);
                    }
                    break;
                case 2:
                    if (getStartFileID(1) > 0) {
                        setStartFileID(0, getStartFileID(0) - 1);
                    }
                    break;
                case 3:
                    if (getStartFileID(1) < folders.get(1).size() - 8) {
                        setStartFileID(1, getStartFileID(1) - 1);
                    }
                    break;
                case 4:
                    allowClicks(false);
                    getCurrentGuiState().hideButton(10, false);
                    requestedSide = 1;
                    break;
                case 5:
                    allowClicks(false);
                    getCurrentGuiState().hideButton(8, false);
                    getCurrentGuiState().hideButton(9, false);
                    requestedSide = 1;
                    break;
                case 6:
                    allowClicks(false);
                    getCurrentGuiState().hideButton(10, false);
                    requestedSide = 2;
                    break;
                case 7:
                    allowClicks(false);
                    getCurrentGuiState().hideButton(8, false);
                    getCurrentGuiState().hideButton(9, false);
                    requestedSide = 2;
                    break;
                case 8:
                    allowClicks(true);
                    getCurrentGuiState().hideButton(8, true);
                    getCurrentGuiState().hideButton(9, true);
                    new PacketMemoryHandlerDeletePicture(requestedSide == 1 ? getSelectedFile(0).getIndex() : getSelectedFile(1).getIndex(), container.windowId, requestedSide).sendToServer();
                    break;
                case 9:
                    allowClicks(true);
                    getCurrentGuiState().hideButton(8, true);
                    getCurrentGuiState().hideButton(9, true);
                    break;
                case 10:
                    getCurrentGuiState().hideButton(10, true);
                    new PacketMemoryHandlerRename(requestedSide == 1 ? getSelectedFile(0).getIndex() : getSelectedFile(1).getIndex(), renameField.getText(), container.windowId, requestedSide).sendToServer();
                    break;
            }

        } else if (activeGuiState == 3) {
            switch (button.id) {
                case 0:
                    if (showingPhotoFile > 0) {
                        showingPhotoFile--;
                    }
                    break;
                case 1:
                    if (showingPhotoFile < folders.get(0).size() - 1) {
                        showingPhotoFile++;
                    }
                    break;
                case 2:
                    getCurrentGuiState().hideButton(0, true);
                    getCurrentGuiState().hideButton(1, true);
                    getCurrentGuiState().hideButton(5, false);
                    getCurrentGuiState().hideButton(6, false);
                    break;
                case 3:
                    getCurrentGuiState().hideButton(7, false);
                    renameField.setText(folders.get(0).getFileAt(showingPhotoFile).getPhotoName(container.getSlot(0).getStack()));
                    break;
                case 4:
                    setGuiState(1);
                    break;
                case 5:
                    new PacketMemoryHandlerDeletePicture(folders.get(0).getFileAt(showingPhotoFile).getIndex(), container.windowId, 1).sendToServer();
                    getCurrentGuiState().hideButton(0, false);
                    getCurrentGuiState().hideButton(1, false);
                    getCurrentGuiState().hideButton(5, true);
                    getCurrentGuiState().hideButton(6, true);
                    if (folders.get(0).size() == 0) {
                        setGuiState(1);
                    }
                    break;
                case 6:
                    getCurrentGuiState().hideButton(0, false);
                    getCurrentGuiState().hideButton(1, false);
                    getCurrentGuiState().hideButton(5, true);
                    getCurrentGuiState().hideButton(6, true);
                    break;
                case 7:
                    getCurrentGuiState().hideButton(7, true);
                    new PacketMemoryHandlerRename(folders.get(0).getFileAt(showingPhotoFile).getIndex(), renameField.getText(), container.windowId, 1).sendToServer();
                    break;
            }
        }
    }

    @Override
    protected void initState(int state) {
        System.out.println("init state");
        if(state != 3) {
            folders.clear();
        }

        if (state == 0) {
        } else if (state == 1) {
            folders.add(new Folder(width/2 - 176/2, height/2 - 160/2, 176, 160, 20, container.getSlot(0).getStack(), 8).setFiles(helpFolders.get(0).getFiles()));
            SimpleSlot slot0 = (SimpleSlot) container.getSlot(0);
            slot0.setDisplayPosition(-18, -18);
            slot0.allowPickUp(false);

        } else if (state == 2) {
            SimpleSlot slot0 = (SimpleSlot) container.getSlot(0);
            SimpleSlot slot1 = (SimpleSlot) container.getSlot(1);

            slot0.setDisplayPosition(0, -18);
            slot1.setDisplayPosition(116, -18);

            slot0.allowPickUp(false);
            slot1.allowPickUp(false);
        }
    }

    @Override
    protected void exitState(int state, boolean exitGui) {
        if (renameField.isFocused()) {

        } else {
            if (exitGui) {
                this.mc.thePlayer.closeScreen();
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        renameField.xPosition = -10000;
        renameField.yPosition = -10000;

        if (!ItemStacks.identical(prevStack[0], container.getSlot(0).getStack())) {
            prevStack[0] = container.getSlot(0).getStack();
            updateScreenCard();
            folders.get(0).setFiles(helpFolders.get(0).getFiles());
        }
        if (!ItemStacks.identical(prevStack[1], container.getSlot(1).getStack())) {
            prevStack[1] = container.getSlot(1).getStack();
            updateScreenCard();
            folders.get(1).setFiles(helpFolders.get(1).getFiles());
        }

        if (activeGuiState == 0) {
            guiStates.get(0).buttonList.get(0).enabled = prevStack[0] != null || prevStack[1] != null;
            guiStates.get(0).buttonList.get(0).displayString = prevStack[0] != null && prevStack[1] != null ? "Use these Memory Cards" : "Use this Memory Card";
        } else if (activeGuiState == 1) {
            for (int i = 2; i <= 4; i++) {
                if (getSelectedFile(0) != null) {
                    ((SimpleGuiButton) getCurrentGuiState().buttonList.get(i)).setNormalPosition();
                } else {
                    ((SimpleGuiButton) getCurrentGuiState().buttonList.get(i)).hideButton();
                }
            }
            if (getCurrentGuiState().buttonList.get(7).xPosition > 0) {

                renameField.xPosition = width / 2 - 50;
                renameField.yPosition = height / 2 - 10;
            }
        } else if (activeGuiState == 2) {

            getCurrentGuiState().hideButton(4, getSelectedFile(0) == null);
            getCurrentGuiState().hideButton(5, getSelectedFile(0) == null);
            getCurrentGuiState().hideButton(6, getSelectedFile(1) == null);
            getCurrentGuiState().hideButton(7, getSelectedFile(1) == null);

            if (getCurrentGuiState().buttonList.get(10).xPosition > 0) {
                renameField.xPosition = width / 2 - 50;
                renameField.yPosition = height / 2 - 10;
            }
        } else if (activeGuiState == 3) {
            if (getCurrentGuiState().buttonList.get(7).xPosition > 0) {
                renameField.xPosition = width / 2 - 50;
                renameField.yPosition = height / 2 - 10;
            }
        }
    }

    public void updateScreenCard() {
        ItemStack stack = container.getSlot(0).getStack();
        helpFolders.clear();
        helpFolders.add(new Folder(0, 0, 0, 0, 20, null, 8));
        helpFolders.add(new Folder(0, 0, 0, 0, 20, null, 8));
        clearFiles();
        if (stack != null) {
            PhotoStorageItem item = (PhotoStorageItem) stack.getItem();
            for (int index = 0; index < item.getPhotoStorage(stack).size(); index++) {
                helpFolders.get(0).getFiles().add(new ImageFile(item.getPhotoStorage(stack).get(index), index));
            }
        }
        stack = container.getSlot(1).getStack();
        if (stack != null) {
            PhotoStorageItem item = (PhotoStorageItem) stack.getItem();
            for (int index = 0; index < item.getPhotoStorage(stack).size(); index++) {
                helpFolders.get(0).getFiles().add(new ImageFile(item.getPhotoStorage(stack).get(index), index));
            }
        }
    }

    @Override
    protected boolean shouldExitOnKeyboardType(char typedChar, int keyCode) {
        return false;
    }

    @Override
    protected ResourceLocation provideTexture() {
        return new ResourceLocation(CameraCraft.MOD_ID + ":textures/gui/memory-handler.png");
    }

    private int computePhotoDim() {
        return Math.min(height, width) - 40;
    }
}
