package de.take_weiland.mods.cameracraft.client.gui.memory.handler;

import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageRenamable;
import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.item.ItemPhotoStorages;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Intektor
 */
public class ImageFile {

    protected long photoID;
    protected int x, y;
    protected boolean isSelected;
    protected int position = -1;
    public static final int width = 20, height = 20;
    Minecraft mc = Minecraft.getMinecraft();
    protected GuiScreen gui;
    private boolean isRendered;
    private int index;
    private int positionInArray;
    private boolean isLeft;

    public ImageFile(GuiScreen gui, long photoID, int x, int y, int index, int positionInArray, boolean left) {
        this.gui = gui;
        this.photoID = photoID;
        this.x = x;
        this.y = y;
        this.index = index;
        this.positionInArray = positionInArray;
        isLeft = left;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getPhotoID() {
        return photoID;
    }

    public void setPhotoID(long photoID) {
        this.photoID = photoID;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void renderFile(int mouseX, int mouseY, int x, int y, int guiTop, int guiLeft, int state, ItemStack stack, int xSize, int ySize) {
        isRendered = true;
        setPosition(x + guiLeft, y + guiTop);
        position = (y + guiTop - 2) / height;
        ItemPhotoStorages itemStorage = (ItemPhotoStorages) stack.getItem();
        PhotoStorageRenamable storage = (PhotoStorageRenamable) itemStorage.getPhotoStorage(stack);
        if (state == 1) {
            if (isSelected()) {
                Rendering.drawColoredQuad(x + guiLeft, y + guiTop, state == 1 ? 176 - 6 : state == 2 ? 113 : 0, height, Color.CYAN.getRGB());
            }
            PhotoDataCache.bindTexture(photoID);
            GL11.glColor3f(1, 1, 1);
            Rendering.drawTexturedQuadFit(x + guiLeft, y + guiTop, width, height);
            mc.fontRendererObj.drawString(storage.getName(getIndex()), x + width + 2 + guiLeft, y + 6 + guiTop, 0);
//        mc.fontRendererObj.drawString("DCIM_" + getPhotoName(), x + width + 2 + guiLeft, y + 6 + guiTop, 0);
//        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
//        mc.fontRendererObj.drawString(formatter.format(new Date(DatabaseImpl.current.getPhoto(index).getTime())), x + 140, y + 6, 0);
        } else if (state == 2) {
            if (isSelected()) {
                Rendering.drawColoredQuad(x + guiLeft, y + guiTop, 107, height, Color.CYAN.getRGB());
            }
            PhotoDataCache.bindTexture(photoID);
            GL11.glColor3f(1, 1, 1);
            Rendering.drawTexturedQuadFit(x + guiLeft, y + guiTop, width, height);
            mc.fontRendererObj.drawString(storage.getName(getIndex()), x + width + 2 + guiLeft, y + 6 + guiTop, 0);
        }
    }

    public void updateFile(int mouseX, int mouseY, int guiTop, int guiLeft, int state, int xSize, int ySize) {
        if (matchCoords(mouseX, mouseY, guiTop, guiLeft, state, xSize, ySize) == 1) {

        }
        if (!isRendered && (state == 1)) {
            isSelected = false;
            position = -1;
        }
        isRendered = false;
    }

    public void click(int mouseX, int mouseY, int mouseButton, int guiTop, int guiLeft, int state, int xSize, int ySize) {
        if (matchCoords(mouseX, mouseY, guiTop, guiLeft, state, xSize, ySize) == 1) {
            isSelected = mouseButton == 0 ? true : false;
        } else if (matchCoords(mouseX, mouseY, guiTop, guiLeft, state, xSize, ySize) == 0) {
            isSelected = false;
        }
    }

    public void doubleClick(int mouseX, int mouseY, int mouseButton, int guiTop, int guiLeft, int state) {
    }

    public boolean isSelected() {
        return isSelected;
    }

    protected int matchCoords(int theX, int theY, int guiTop, int guiLeft, int state, int xSize, int ySize) {
        if (state == 1) {
            return Guis.isPointInRegion(x - guiLeft, y - guiTop, width + 176, height, theX - guiLeft, theY - guiTop) ? 1 : !Guis.isPointInRegion(guiLeft, guiTop, 176, 166, theX, theY) ? 2 : 0;
        } else if (state == 2) {
            if (isOnLeftSide(theX, guiLeft) && isLeft) {
                return Guis.isPointInRegion(0, y - guiTop, guiLeft + xSize, height, 1, theY - guiTop) ? 1 : 0;
            } else if (isOnRightSide(theX, guiLeft, xSize) && !isLeft) {
                return Guis.isPointInRegion(0, y - guiTop, guiLeft + xSize, height, 1, theY - guiTop) ? 1 : 0;
            } else {
                return 2;
            }
        }
        return 0;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getPosition() {
        return position;
    }

    public void setIsRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }

    public boolean getIsRendered() {
        return isRendered;
    }

    public int getIndex() {
        return index;
    }

    public String getPhotoName(ItemStack stack) {
        ItemPhotoStorages itemStorage = (ItemPhotoStorages) stack.getItem();
        PhotoStorageRenamable storage = (PhotoStorageRenamable) itemStorage.getPhotoStorage(stack);
        return storage.getName(getIndex());
    }

    public int getPositionInArray() {
        return positionInArray;
    }

    public boolean isOnLeftSide(int x, int guiLeft) {
        return x >= guiLeft && x <= guiLeft + 113;
    }

    public boolean isOnRightSide(int x, int guiLeft, int xSize) {
        return x > guiLeft + 117 && x < guiLeft + xSize;
    }
}
