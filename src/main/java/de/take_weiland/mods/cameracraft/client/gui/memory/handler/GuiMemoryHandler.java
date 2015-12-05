package de.take_weiland.mods.cameracraft.client.gui.memory.handler;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageRenamable;
import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.client.gui.state.GuiContainerGuiState;
import de.take_weiland.mods.cameracraft.client.gui.state.GuiStateContainer;
import de.take_weiland.mods.cameracraft.gui.ContainerMemoryHandler;
import de.take_weiland.mods.cameracraft.item.ItemPhotoStorages;
import de.take_weiland.mods.cameracraft.network.PacketMemoryHandlerDeletePicture;
import de.take_weiland.mods.cameracraft.network.PacketMemoryHandlerRename;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;
import de.take_weiland.mods.commons.inv.SimpleGuiButton;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glColor3f;

/**
 * @author Intektor
 */
public class GuiMemoryHandler extends GuiContainerGuiState<ContainerMemoryHandler> {

    protected ArrayList<ImageFile> files1 = new ArrayList<>();
    protected ArrayList<ImageFile> files2 = new ArrayList<>();
    private ItemStack[] prevStack = new ItemStack[2];

    private static ResourceLocation singleFolder = new ResourceLocation(CameraCraft.MODID + ":textures/gui/memory-handler1.png");
    private static ResourceLocation dualFolder = new ResourceLocation(CameraCraft.MODID + ":textures/gui/memory-handler2.png");

    private int startFile1 = 0;
    private int startFile2 = 0;

    protected GuiTextField renameField;

    private int showingPhotoFile, requestedSide;

    public GuiMemoryHandler(ContainerMemoryHandler container) {
        super(container);
    }

    @Override
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

        guiStates.add(new GuiStateContainer(1, container, this, singleFolder, new int[]{0}, buttons2, false, -1, -1, new int[]{5, 6, 7}, new int[]{}));

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

        guiStates.add(new GuiStateContainer(2, container, this, dualFolder, new int[]{0, 1}, buttons20, false, -1, -1, new int[]{8, 9, 10}, new int[]{}, 229, 166));

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
        if (getActiveGuiStateNumber() == 1) {
            int y = 0;
            for (int z = startFile1; z < startFile1 + 8 && z < files1.size(); z++) {
                files1.get(z).renderFile(mouseX, mouseY, 3, y * ImageFile.height + 2, guiTop, guiLeft, getActiveGuiStateNumber(), container.getSlot(0).getStack(), xSize, ySize);
                y++;
            }
        } else if (activeGuiState == 3) {
            int size = computePhotoDim();

            int x = (width - size) / 2;
            int y = (height - size) / 2;

            drawRect(x, y, x + size, y + size, 0xffffff00);

            if (files1.size() - 1 >= showingPhotoFile) {
                PhotoDataCache.bindTexture(files1.get(showingPhotoFile).photoID);
                glColor3f(1, 1, 1);
                Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);
                drawCenteredString(fontRendererObj, files1.get(showingPhotoFile).getPhotoName(container.getSlot(0).getStack()), width / 2, 4, Color.red.getRGB());
            } else {
                setGuiState(1);
            }
        } else if (getActiveGuiStateNumber() == 2) {
            int y = 0;
            for (int z = startFile1; z < startFile1 + 8 && z < files1.size(); z++) {
                files1.get(z).renderFile(mouseX, mouseY, 3, y * ImageFile.height + 2, guiTop, guiLeft, getActiveGuiStateNumber(), container.getSlot(0).getStack(), xSize, ySize);
                y++;
            }
            y = 0;
            for (int z = startFile2; z < startFile2 + 8 && z < files2.size(); z++) {
                files2.get(z).renderFile(mouseX, mouseY, 116 + 3, y * ImageFile.height + 2, guiTop, guiLeft, getActiveGuiStateNumber(), container.getSlot(1).getStack(), xSize, ySize);
                y++;
            }
        }
    }

    @Override
    protected void buttonPressed(int activeGuiState, GuiButton button) {
        if (activeGuiState == 0) {
            if (button.id == 0) {
                if (container.getSlot(0).getStack() != null && container.getSlot(1).getStack() != null) {
                    setGuiState(2);
                } else {
                    setGuiState(1);
                }
            }
        } else if (activeGuiState == 1) {
            int selected = getSelectedFile1() != null ? getSelectedFile1().position : -10;
            Slot slot = (Slot) container.inventorySlots.get(0);
            ItemStack stack = slot.getStack();
            ItemPhotoStorages itemStorage = (ItemPhotoStorages) stack.getItem();
            PhotoStorageRenamable storage = (PhotoStorageRenamable) itemStorage.getPhotoStorage(stack);
            switch (button.id) {
                case 0:
                    if (startFile1 > 0) {
                        startFile1 += -1;
                        selectAllFiles(false, 1);
                        if (getFileAtPosition1(selected) != null) {
                            getFileAtPosition1(selected).setSelected(true);
                        }
                    }
                    break;
                case 1:
                    if (startFile1 < files1.size() - 8) {
                        startFile1 += 1;
                        selectAllFiles(false, 1);
                        if (getFileAtPosition1(selected) != null) {
                            getFileAtPosition1(selected).setSelected(true);
                        }
                    }
                    break;
                case 2:
                    showingPhotoFile = getSelectedFile1().getPositionInArray();
                    setGuiState(3);
                    break;
                case 3:
                    getCurrentGuiState().hideButton(7, false);
                    renameField.setText(getSelectedFile1().getPhotoName(container.getSlot(0).getStack()));
                    break;
                case 4:
                    getCurrentGuiState().hideButton(5, false);
                    getCurrentGuiState().hideButton(6, false);
                    break;
                case 5:
                    new PacketMemoryHandlerDeletePicture(getSelectedFile1().getIndex(), container.windowId, 1).sendToServer();
                    getCurrentGuiState().hideButton(5, true);
                    getCurrentGuiState().hideButton(6, true);
                    break;
                case 6:
                    getCurrentGuiState().hideButton(5, true);
                    getCurrentGuiState().hideButton(6, true);
                    break;
                case 7:
                    getCurrentGuiState().hideButton(7, true);
                    new PacketMemoryHandlerRename(getSelectedFile1().getIndex(), renameField.getText(), container.windowId, 1).sendToServer();
                    break;
            }

        } else if (activeGuiState == 2) {
            int selected1 = getSelectedFile1() != null ? getSelectedFile1().position : -10;
            int selected2 = getSelectedFile2() != null ? getSelectedFile2().position : -10;
            switch (button.id) {
                case 0:
                    if (startFile1 > 0) {
                        startFile1 += -1;
                        selectAllFiles(false, 1);
                        if (getFileAtPosition1(selected1) != null) {
                            getFileAtPosition1(selected1).setSelected(true);
                        }
                    }
                    break;
                case 1:
                    if (startFile1 < files1.size() - 8) {
                        startFile1 += 1;
                        selectAllFiles(false, 1);
                        if (getFileAtPosition1(selected1) != null) {
                            getFileAtPosition1(selected1).setSelected(true);
                        }
                    }
                    break;
                case 2:
                    if (startFile2 > 0) {
                        startFile2 += -1;
                        selectAllFiles(false, 2);
                        if (getFileAtPosition2(selected2) != null) {
                            getFileAtPosition2(selected2).setSelected(true);
                        }
                    }
                    break;
                case 3:
                    if (startFile2 < files2.size() - 8) {
                        startFile2 += 1;
                        selectAllFiles(false, 2);
                        if (getFileAtPosition2(selected2) != null) {
                            getFileAtPosition2(selected2).setSelected(true);
                        }
                    }
                    break;
                case 4:
                    getCurrentGuiState().hideButton(10, false);
                    requestedSide = 1;
                    break;
                case 5:
                    getCurrentGuiState().hideButton(8, false);
                    getCurrentGuiState().hideButton(9, false);
                    requestedSide = 1;
                    break;
                case 6:
                    getCurrentGuiState().hideButton(10, false);
                    requestedSide = 2;
                    break;
                case 7:
                    getCurrentGuiState().hideButton(8, false);
                    getCurrentGuiState().hideButton(9, false);
                    requestedSide = 2;
                    break;
                case 8:
                    getCurrentGuiState().hideButton(8, true);
                    getCurrentGuiState().hideButton(9, true);
                    new PacketMemoryHandlerDeletePicture(requestedSide == 1 ? getSelectedFile1().getIndex() : getSelectedFile2().getIndex(), container.windowId, requestedSide).sendToServer();
                    break;
                case 9:
                    getCurrentGuiState().hideButton(8, true);
                    getCurrentGuiState().hideButton(9, true);
                    break;
                case 10:
                    getCurrentGuiState().hideButton(10, true);
                    new PacketMemoryHandlerRename(requestedSide == 1 ? getSelectedFile1().getIndex() : getSelectedFile2().getIndex(), renameField.getText(), container.windowId, requestedSide).sendToServer();
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
                    if (showingPhotoFile < files1.size() - 1) {
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
                    renameField.setText(files1.get(showingPhotoFile).getPhotoName(container.getSlot(0).getStack()));
                    break;
                case 4:
                    setGuiState(1);
                    break;
                case 5:
                    new PacketMemoryHandlerDeletePicture(files1.get(showingPhotoFile).getIndex(), container.windowId, 1).sendToServer();
                    getCurrentGuiState().hideButton(0, false);
                    getCurrentGuiState().hideButton(1, false);
                    getCurrentGuiState().hideButton(5, true);
                    getCurrentGuiState().hideButton(6, true);
                    if (files1.size() == 0) {
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
                    new PacketMemoryHandlerRename(files1.get(showingPhotoFile).getIndex(), renameField.getText(), container.windowId, 1).sendToServer();
                    break;
            }
        }
    }

    @Override
    protected void initState(int state) {
        if (state == 0) {

        } else if (state == 1) {
            SimpleSlot slot0 = (SimpleSlot) container.getSlot(0);
            slot0.setDisplayPosition(-18, -18);
            slot0.allowPickUp(false);

            startFile1 = 0;
        } else if (state == 2) {

            startFile1 = 0;
            startFile2 = 0;

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
    protected ResourceLocation provideTexture() {
        return new ResourceLocation(CameraCraft.MOD_ID + ":textures/gui/memory-handler.png");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (ImageFile file : files1) {
            file.updateFile(mouseX, mouseY, guiTop, guiLeft, getActiveGuiStateNumber(), xSize, ySize);
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
        }
        if (!ItemStacks.identical(prevStack[1], container.getSlot(1).getStack())) {
            prevStack[1] = container.getSlot(1).getStack();
            updateScreenCard();
        }


        if (activeGuiState == 0) {
            guiStates.get(0).buttonList.get(0).enabled = prevStack[0] != null || prevStack[1] != null;
            guiStates.get(0).buttonList.get(0).displayString = prevStack[0] != null && prevStack[1] != null ? "Use these Memory Cards" : "Use this Memory Card";
        } else if (activeGuiState == 1) {
            for (int i = 2; i <= 4; i++) {
                if (getSelectedFile1() != null) {
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

            getCurrentGuiState().hideButton(4, getSelectedFile1() == null);
            getCurrentGuiState().hideButton(5, getSelectedFile1() == null);
            getCurrentGuiState().hideButton(6, getSelectedFile2() == null);
            getCurrentGuiState().hideButton(7, getSelectedFile2() == null);

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
        files1.clear();
        files2.clear();
        if (stack != null) {
            ItemPhotoStorages storage = (ItemPhotoStorages) stack.getItem();
            int posina = 0;
            for (long l : storage.getPhotoStorage(stack)) {
                files1.add(new ImageFile(this, l, 0, 0, storage.getPhotoStorage(stack).indexOf(l), posina++, true));
            }
        }
        stack = container.getSlot(1).getStack();
        if (stack != null) {
            ItemPhotoStorages storage = (ItemPhotoStorages) stack.getItem();
            int posina = 0;
            for (long l : storage.getPhotoStorage(stack)) {
                files2.add(new ImageFile(this, l, 0, 0, storage.getPhotoStorage(stack).indexOf(l), posina++, false));
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (getActiveGuiStateNumber() == 1) {
            if (getCurrentGuiState().buttonList.get(5).xPosition < 0 && getCurrentGuiState().buttonList.get(7).xPosition < 0) {
                for (ImageFile file : files1) {
                    file.click(mouseX, mouseY, mouseButton, guiTop, guiLeft, getActiveGuiStateNumber(), xSize, ySize);
                }
            }
        } else if (getActiveGuiStateNumber() == 2) {
            if (getCurrentGuiState().buttonList.get(10).xPosition < 0) {
                for (ImageFile file : files1) {
                    file.click(mouseX, mouseY, mouseButton, guiTop, guiLeft, getActiveGuiStateNumber(), xSize, ySize);
                }
                for (ImageFile file : files2) {
                    file.click(mouseX, mouseY, mouseButton, guiTop, guiLeft, getActiveGuiStateNumber(), xSize, ySize);
                }
            }
        }
    }

    @Override
    protected void doubleClick(int mouseX, int mouseY, int mouseButton) {
        for (ImageFile file : files1) {
            file.doubleClick(mouseX, mouseY, mouseButton, guiTop, guiLeft, getActiveGuiStateNumber());
        }
    }

    @Override
    protected boolean shouldExitOnKeyboardType(char typedChar, int keyCode) {
        return !renameField.isFocused();
    }

    public void selectAllFiles(boolean select, int side) {
        if (side == 1) {
            for (ImageFile file : files1) {
                file.setSelected(select);
            }
        } else {
            for (ImageFile file : files2) {
                file.setSelected(select);
            }
        }
    }

    public ImageFile getFileAtPosition1(int position) {
        for (ImageFile file : files1) {
            if (file.getPosition() == position) {
                return file;
            }
        }
        return null;
    }

    public ImageFile getFileAtPosition2(int position) {
        for (ImageFile file : files2) {
            if (file.getPosition() == position) {
                return file;
            }
        }
        return null;
    }

    public ImageFile getSelectedFile1() {
        for (ImageFile file : files1) {
            if (file.isSelected()) {
                return file;
            }
        }
        return null;
    }

    public ImageFile getSelectedFile2() {
        for (ImageFile file : files2) {
            if (file.isSelected()) {
                return file;
            }
        }
        return null;
    }

    private int computePhotoDim() {
        return Math.min(height, width) - 40;
    }
}
