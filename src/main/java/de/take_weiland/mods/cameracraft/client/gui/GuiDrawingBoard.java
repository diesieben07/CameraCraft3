package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.client.gui.state.GuiContainerGuiState;
import de.take_weiland.mods.cameracraft.client.gui.state.GuiStateContainer;
import de.take_weiland.mods.cameracraft.client.texture.DynTexture;
import de.take_weiland.mods.cameracraft.gui.ContainerDrawingBoard;
import de.take_weiland.mods.cameracraft.item.ItemPhoto;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Intektor
 */
public class GuiDrawingBoard extends GuiContainerGuiState<ContainerDrawingBoard> {

    protected BufferedImage image;
    protected DynTexture texture;
    protected final int scale = 8;
    private int red, blue, green;

    public GuiDrawingBoard(ContainerDrawingBoard container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
        guiStates.add(new GuiStateContainer(container, this, new ResourceLocation("cameracraft:textures/gui/drawing_board.png"), new int[]{0}, new GuiButton[]{new GuiButton(0, width / 2 - 60, height / 2 - guiTop / 2 - 5, 120, 20, "Use this for drawing")}, true, guiTop, guiLeft));

        GuiButton[] buttons1 = new GuiButton[]{
                new GuiButton(0, 0, 20, 75, 20, "Finish Drawing"),

                new GuiButton(1, width - 90, 5, 20, 20, "-"),
                new GuiButton(2, width - 25, 5, 20, 20, "+"),

                new GuiButton(3, width - 90, 30, 20, 20, "-"),
                new GuiButton(4, width - 25, 30, 20, 20, "+"),

                new GuiButton(5, width - 90, 55, 20, 20, "-"),
                new GuiButton(6, width - 25, 55, 20, 20, "+"),

                new GuiButton(7, width - 40, height - 20, 40, 20, "Clear"),
        };

        guiStates.add(new GuiStateContainer(container, this, null, new int[]{1}, buttons1, false, 0, 0));
        initGuiState();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (getActiveGuiStateNumber() == 1) {

            drawCenteredString(mc.fontRendererObj, "Red: " + red, ((width - 90) + (width - 25)) / 2 + 10, 5 + 6, Color.red.getRGB());
            drawCenteredString(mc.fontRendererObj, "Green: " + green, ((width - 90) + (width - 25)) / 2 + 10, 30 + 6, Color.green.getRGB());
            drawCenteredString(mc.fontRendererObj, "Blue: " + blue, ((width - 90) + (width - 25)) / 2 + 10, 55 + 6, Color.blue.getRGB());

            int red2 = ((int) (red * 25.5) << 16) & 0x00FF0000;
            int green2 = ((int) (green * 25.5) << 8) & 0x0000FF00;
            int blue2 = (int) (blue * 25.5) & 0x000000FF;
            int color = 0xFF000000 | red2 | green2 | blue2;


            Rendering.drawColoredQuad(((width - 90) + (width - 25)) / 2 + 2, 80, 15, 15, color);

            int size = Math.min(height, width) - 4;

            int x = (width - size) / 2;
            int y = (height - size) / 2;

            drawRect(x, y, x + size, y + size, Color.black.getRGB());
            ItemPhoto photo = (ItemPhoto) container.getSlot(1).getStack().getItem();
            PhotoDataCache.bindTexture(photo.getPhotoId(container.getSlot(1).getStack()));
            GL11.glColor3f(1, 1, 1);
            Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);

            mc.renderEngine.bindTexture(mc.renderEngine.getDynamicTextureLocation("camera.craft", texture));
            Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);
        }
    }


    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
//        if (container.getSlot(0) != null) {
//            ItemStack photo = container.getSlot(0).getStack();
//            if (photo != null) {
//                NBTTagCompound nbt = ItemStacks.getNbt(photo);
//                try {
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    ImageIO.write(image, "png", stream);
//                    byte[] image = stream.toByteArray();
//                    nbt.setByteArray("imageOverload", image);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }


    @Override
    protected void buttonPressed(int activeGuiState, GuiButton button) {
        if (activeGuiState == 0) {
            if (button.id == 0) {
                setGuiState(1);
            }
        }
        if (activeGuiState == 1) {
            switch (button.id) {
                case 0:
                    setGuiState(0);
                    break;
                case 1:
                    if (red > 0) {
                        red--;
                    }
                    break;
                case 2:
                    if (red < 10) {
                        red++;
                    }
                    break;
                case 3:
                    if (green > 0) {
                        green--;
                    }
                    break;
                case 4:
                    if (green < 10) {
                        green++;
                    }
                    break;
                case 5:
                    if (blue > 0) {
                        blue--;
                    }
                    break;
                case 6:
                    if (blue < 10) {
                        blue++;
                    }
                    break;
                case 7:
                    initState(1);
                    break;
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (activeGuiState == 0) {
            Slot slot = (Slot) (container.inventorySlots.get(0));
            if (getCurrentGuiState() != null) {
                if (getCurrentGuiState().buttonList != null) {
                    if (!getCurrentGuiState().buttonList.isEmpty()) {
                        if (getCurrentGuiState().buttonList.get(0) != null) {
                            getCurrentGuiState().buttonList.get(0).enabled = false;
                            if (slot != null) {
                                if (slot.getStack() != null) {
                                    getCurrentGuiState().buttonList.get(0).enabled = slot.getStack().getItem() instanceof ItemPhoto;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        if ((clickedMouseButton == 0 || clickedMouseButton == 1) && getActiveGuiStateNumber() == 1) {
            drawLine(mouseX, mouseY, clickedMouseButton);
        }
    }

    @Override
    protected ResourceLocation provideTexture() {
        return new ResourceLocation("cameracraft:textures/gui/drawing_board.png");
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if ((originX != -1 && originY != -1) && getActiveGuiStateNumber() == 1) {
            draw(mouseX, mouseY, mouseButton);
        }
    }

    private int originX = -1, originY = -1;

    protected void drawLine(int mouseX, int mouseY, int mouseButton) {
        if (Math.abs(originX - mouseX) > 1 || Math.abs(originY - mouseY) > 1 && originX != -1 && originY != 0) {
            int resolutionX = 265;
            int resolutionY = 265;
            Graphics2D g = (Graphics2D) image.getGraphics();
            if(mouseButton == 0) {
                g.setColor(new Color(getColor()));
                g.drawLine(getMouseXinImage(resolutionX, mouseX), getMouseYinImage(resolutionY, mouseY), originX, originY);
            } else {
                g.setColor(new Color(0, 0, 0, 0));
                g.drawLine(getMouseXinImage(resolutionX, mouseX), getMouseYinImage(resolutionY, mouseY), originX, originY);
            }
            texture.updateBufferedImage(image);
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
            image.setRGB(pixelX, pixelY, mouseButton == 0 ? getColor() : 0);
            texture.updateBufferedImage(image);
            originX = pixelX;
            originY = pixelY;
        }
    }

    @Override
    protected void initState(int state) {
        if (state == 1) {
            ItemStack stack = container.getSlot(0).getStack();
            ItemPhoto photo = (ItemPhoto) stack.getItem();
            PhotoItem.Size size = photo.getSize(stack);
            if (stack.getTagCompound().hasKey("imageOverload")) {
                try {
                    InputStream in = new ByteArrayInputStream(stack.getTagCompound().getByteArray("imageOverload"));
                    image = ImageIO.read(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                int sizeZ = Math.min(height, width) - 4;
                if (size.getWidth() != 0) {
                    image = new BufferedImage(size.getWidth(), size.getHeight(), BufferedImage.TYPE_INT_ARGB);
                } else {
                    image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
                }
            }
            texture = new DynTexture(image);
        }
    }

    public int getMouseXinImage(int reolutionX, int mouseX) {
        int size = Math.min(height, width) - 8;
        int x = (width - size) / 2;
        int resolutionX = 256;
        int mouseXinFrame = mouseX - x;
        double scaleX = (double) size / (double) resolutionX;
        int pixelX = (int) (mouseXinFrame / scaleX);
        return pixelX;
    }

    public int getMouseYinImage(int reolutionY, int mouseY) {
        int size = Math.min(height, width) - 8;
        int y = (height - size) / 2;
        int resolutionY = 256;
        int mouseYinFrame = mouseY - y;
        double scaleY = (double) size / (double) resolutionY;
        int pixelY = (int) (mouseYinFrame / scaleY);
        return pixelY;
    }

    protected int getColor() {
        int red2 = ((int) (red * 25.5) << 16) & 0x00FF0000;
        int green2 = ((int) (green * 25.5) << 8) & 0x0000FF00;
        int blue2 = (int) (blue * 25.5) & 0x000000FF;
        int color = 0xFF000000 | red2 | green2 | blue2;
        return color;
    }
}
