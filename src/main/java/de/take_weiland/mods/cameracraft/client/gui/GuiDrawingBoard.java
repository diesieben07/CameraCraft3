package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.client.CColor;
import de.take_weiland.mods.cameracraft.client.ClientUtil;
import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.client.gui.state.GuiContainerGuiState;
import de.take_weiland.mods.cameracraft.client.gui.state.GuiStateContainer;
import de.take_weiland.mods.cameracraft.gui.ContainerDrawingBoard;
import de.take_weiland.mods.cameracraft.img.ImageUtil;
import de.take_weiland.mods.cameracraft.item.ItemPhoto;
import de.take_weiland.mods.cameracraft.network.PacketDrawingBoard;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Intektor
 */
public class GuiDrawingBoard extends GuiContainerGuiState<ContainerDrawingBoard> {

    protected BufferedImage overlay;
    protected BufferedImage colorCircle;
    protected DynamicTexture overDynText;
    protected DynamicTexture colorDynText;
    protected ResourceLocation overDynRsc;
    protected ResourceLocation colorDynRsc;
    protected final int scale = 8;
    private int color = 0;
    protected int tool = 0;
    protected final int DRAWING_TOOL = 0, PIPETTE_TOOL = 1;
    private boolean rainbowMode = false;
    private CColor ccolor = new CColor(new Color(0));

    public GuiDrawingBoard(ContainerDrawingBoard container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();

        guiStates.add(new GuiStateContainer(0, container, this, new ResourceLocation("cameracraft:textures/gui/drawing_board.png"), new int[]{0}, new GuiButton[]{new GuiButton(0, width / 2 - 60, height / 2 - guiTop / 2 - 5, 120, 20, "Use this for drawing")}, true, guiTop, guiLeft));

        GuiButton[] buttons1 = new GuiButton[]{
                new GuiButton(0, 0, 20, 75, 20, "Finish Drawing"),

                new GuiButton(1, 0, 60, 75, 20, "Drawing Tool"),
                new GuiButton(2, 0, 80, 75, 20, "Pipette"),

                new GuiButton(3, width - 80, 100, 80, 20, "White"),
                new GuiButton(4, width - 80, 120, 80, 20, "Rainbow Mode"),

                new GuiButton(7, width - 40, height - 20, 40, 20, "Clear"),
        };

        guiStates.add(new GuiStateContainer(1, container, this, null, new int[]{1}, buttons1, false, 0, 0));

        GuiButton[] buttons2 = new GuiButton[]{
                new GuiButton(0, width / 2 - 170, height / 2 - 10, 20, 20, EnumChatFormatting.GREEN + "OK"),
                new GuiButton(1, width / 2 - 150, height / 2 - 10, 300, 20, EnumChatFormatting.RED + "CANCEL"),
        };

        guiStates.add(new GuiStateContainer(2, container, this, null, new int[]{}, buttons2, false, 0, 0));

        try (InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("cameracraft:textures/gui/Farbkreis.png")).getInputStream()) {
            colorCircle = ImageIO.read(stream);
            colorDynText = new DynamicTexture(colorCircle);
            colorDynRsc = mc.renderEngine.getDynamicTextureLocation("camera.craft", colorDynText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        initGuiState();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int size = Math.min(height, width) - 4;
        int x = (width - size) / 2;
        int y = (height - size) / 2;

        if (getActiveGuiStateNumber() == 1) {

            if (rainbowMode) {
                ccolor.loopThroughColor(1);
                color = ccolor.getColor().getRGB();
            }

            Rendering.drawColoredQuad(((width - 90) + (width - 25)) / 2 + 2, 80, 15, 15, color);

            drawRect(x, y, x + size, y + size, Color.black.getRGB());

            ItemStack stack = container.getSlot(1).getStack();
            ItemPhoto photo = (ItemPhoto) stack.getItem();
            PhotoDataCache.bindTexture(photo.getPhotoId(stack));

            GL11.glColor3f(1, 1, 1);
            Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            mc.renderEngine.bindTexture(overDynRsc);
            Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            mc.renderEngine.bindTexture(colorDynRsc);
            Rendering.drawTexturedQuadFit(width - x + x / 2 - colorCircle.getWidth() / 2, 5, colorCircle.getWidth(), colorCircle.getHeight());

        } else if (getActiveGuiStateNumber() == 2) {

            drawRect(x, y, x + size, y + size, Color.black.getRGB());

            ItemStack stack = container.getSlot(1).getStack();
            ItemPhoto photo = (ItemPhoto) stack.getItem();
            PhotoDataCache.bindTexture(photo.getPhotoId(stack));

            GL11.glColor3f(1, 1, 1);
            Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            mc.renderEngine.bindTexture(mc.renderEngine.getDynamicTextureLocation("camera.craft", overDynText));
            Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        if (getActiveGuiStateNumber() == 2) {
            drawCenteredString(fontRendererObj, EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "If you leave now, your drawings will be reset, do you really want this?", width / 2, height / 2 - 50, 0);

            drawCenteredString(fontRendererObj, EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "THIS CANNOT BE UNDONE!", width / 2, height / 2 - 40, 0);
        }
    }

    @Override
    protected void buttonPressed(int activeGuiState, GuiButton button) {
        if (activeGuiState == 0) {
            if (button.id == 0) {
                setGuiState(1);
            }
        } else if (activeGuiState == 1) {
            switch (button.id) {
                case 0:
                    saveImage();
                    setGuiState(0);
                    break;
                case 1:
                    getCurrentGuiState().enableAllButtons(true);

                    button.enabled = false;
                    tool = DRAWING_TOOL;
                    break;
                case 2:
                    getCurrentGuiState().enableAllButtons(true);

                    button.enabled = false;

                    tool = PIPETTE_TOOL;
                    rainbowMode = false;
                    getCurrentGuiState().buttonList.get(4).displayString = "Rainbow Mode";
                    break;
                case 3:
                    color = new Color(255, 255, 255, 255).getRGB();
                    ccolor = new CColor(new Color(color));
                    rainbowMode = false;
                    getCurrentGuiState().buttonList.get(4).displayString = "Rainbow Mode";
                    break;
                case 4:
                    rainbowMode = !rainbowMode;
                    getCurrentGuiState().buttonList.get(4).displayString = rainbowMode ? ClientUtil.colorEveryCharacterInString("Rainbow Mode") : "Rainbow Mode";

                    ccolor = new CColor(new Color(color));
                    break;
                case 7:
                    ImageUtil.resetImage(overlay);
                    initState(1);
                    break;
            }
        } else if (activeGuiState == 2) {
            switch (button.id) {
                case 0:
                    this.mc.thePlayer.closeScreen();
                    break;
                case 1:
                    setGuiState(1);
                    break;
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (activeGuiState == 0) {
            Slot slot = (Slot) (container.inventorySlots.get(0));
            getCurrentGuiState().buttonList.get(0).enabled = slot.getStack() != null;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        int size = Math.min(height, width) - 8;
        int x = (width - size) / 2;
        int y = (height - size) / 2;

        if ((clickedMouseButton == 0 || clickedMouseButton == 1) && getActiveGuiStateNumber() == 1 && Guis.isPointInRegion(x, y, size, size, mouseX, mouseY) && tool == DRAWING_TOOL && (originX > -1 && originY > -1)) {
            drawLine(mouseX, mouseY, clickedMouseButton);
        }
        selectColor(mouseX, mouseY, clickedMouseButton);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (getActiveGuiStateNumber() == 1 && tool == DRAWING_TOOL) {
            draw(mouseX, mouseY, mouseButton);
        }
        selectColor(mouseX, mouseY, mouseButton);
    }

    @Override
    protected boolean shouldExitOnKeyboardType(char typedChar, int keyCode) {
        return keyCode == 1 || keyCode == 18;
    }

    @Override
    protected ResourceLocation provideTexture() {
        return new ResourceLocation("cameracraft:textures/gui/drawing_board.png");
    }

    protected void selectColor(int mouseX, int mouseY, int mouseButton) {
        int size = Math.min(height, width) - 4;
        int x = (width - size) / 2;
        int y = (height - size) / 2;

        if (getActiveGuiStateNumber() == 1 && mouseButton == 0) {
            if (Guis.isPointInRegion(width - x + x / 2 - colorCircle.getWidth() / 2, 5, colorCircle.getWidth(), colorCircle.getHeight(), mouseX, mouseY)) {
                int pointX = mouseX - (width - x + x / 2 - colorCircle.getWidth() / 2);
                int pointY = mouseY - 5;

                color = colorCircle.getRGB(pointX, pointY);
                ccolor = new CColor(new Color(color));
            }
            if (Guis.isPointInRegion(x, y, size, size, mouseX, mouseY) && tool == PIPETTE_TOOL) {
                if (overlay.getRGB(getMouseXinImage(256, mouseX), getMouseYinImage(256, mouseY)) != 0) {

                    color = overlay.getRGB(getMouseXinImage(256, mouseX), getMouseYinImage(256, mouseY));
                    ccolor = new CColor(new Color(color));
                } else {
//                    ItemPhoto photo = (ItemPhoto) container.getSlot(1).getStack().getItem();
//                    DynamicTexture text = PhotoDataCache.getDynTexture(photo.getPhotoId(container.getSlot(1).getStack()));
//                    BufferedImage copy = ClientUtil.getBufferedImagefromDynamicTexture(text);
//                    color = copy.getRGB(getMouseXinImage(256, mouseX), getMouseYinImage(256, mouseY));
                }
            }
        }

    }

    private int originX = -1, originY = -1;

    protected void drawLine(int mouseX, int mouseY, int mouseButton) {
        if (Math.abs(originX - mouseX) > 1 || Math.abs(originY - mouseY) > 1 && originX > -1 && originY > -1) {
            int resolutionX = 256;
            int resolutionY = 256;

            Graphics2D g = (Graphics2D) overlay.getGraphics();

            g.setColor(mouseButton == 0 ? ccolor.getColor() : new Color(0, 0, 0, 0));
            g.drawLine(getMouseXinImage(resolutionX, mouseX), getMouseYinImage(resolutionY, mouseY), originX, originY);

            g.dispose();

            ClientUtil.updateDynamicTexture(overDynText, overlay);
        }

        draw(mouseX, mouseY, mouseButton);
    }

    protected void draw(int mouseX, int mouseY, int mouseButton) {
        int size = Math.min(height, width) - 8;

        int x = (width - size) / 2;
        int y = (height - size) / 2;

        int resolutionX = 256;
        int resolutionY = 256;

        if (Guis.isPointInRegion(x, y, size, size, mouseX, mouseY)) {
            int mouseXinFrame = mouseX - x;
            int mouseYinFrame = mouseY - y;

            double scaleX = (double) size / (double) resolutionX;
            double scaleY = (double) size / (double) resolutionY;

            int pixelX = (int) (mouseXinFrame / scaleX);
            int pixelY = (int) (mouseYinFrame / scaleY);

            overlay.setRGB(pixelX, pixelY, mouseButton == 0 ? ccolor.getColor().getRGB() : 0);

            ClientUtil.updateDynamicTexture(overDynText, overlay);

            originX = pixelX;
            originY = pixelY;
        }
    }

    @Override
    protected void initState(int state) {
        if (state == 1) {
            ccolor = new CColor(new Color(0));

            getCurrentGuiState().enableAllButtons(true);
            getCurrentGuiState().buttonList.get(1).enabled = false;

            ItemStack stack = container.getSlot(0).getStack();
            ItemPhoto photo = (ItemPhoto) stack.getItem();

            PhotoItem.Size size = photo.getSize(stack);

            if (overlay == null) {
                overlay = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
            }
            overDynText = new DynamicTexture(overlay);
            overDynRsc = mc.renderEngine.getDynamicTextureLocation("camera.craft", overDynText);

            tool = DRAWING_TOOL;
        }
    }

    @Override
    protected void exitState(int state, boolean exitGui) {
        switch (state) {
            case 1:
                if (exitGui) {
                    setGuiState(2);
                }
                break;
            case 2:
                if (exitGui) {
                    setGuiState(1);
                }
                break;
        }
        if (exitGui && state != 1 && state != 2) {
            Rendering.unloadTexture(colorDynRsc);
            Rendering.unloadTexture(overDynRsc);
            this.mc.thePlayer.closeScreen();
        }
    }

    private void saveImage() {
        new PacketDrawingBoard(container.windowId, overlay).sendToServer();
    }

    public int getMouseXinImage(int resolutionX, int mouseX) {
        int size = Math.min(height, width) - 8;

        int x = (width - size) / 2;
        int mouseXinFrame = mouseX - x;
        double scaleX = (double) size / (double) resolutionX;

        return (int) (mouseXinFrame / scaleX);
    }

    public int getMouseYinImage(int resolutionY, int mouseY) {
        int size = Math.min(height, width) - 8;

        int y = (height - size) / 2;
        int mouseYinFrame = mouseY - y;
        double scaleY = (double) size / (double) resolutionY;

        return (int) (mouseYinFrame / scaleY);
    }
}
