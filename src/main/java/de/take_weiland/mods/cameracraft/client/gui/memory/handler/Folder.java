package de.take_weiland.mods.cameracraft.client.gui.memory.handler;

import de.take_weiland.mods.cameracraft.api.photo.NamedPhotoStorage;
import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.item.ItemPhotoStorages;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Intektor
 */
public class Folder implements Iterable<ImageFile> {

    protected List<ImageFile> files = new ArrayList<>();

    protected final int awayLeft, awayTop, width, height, imageSize, showingPhotos;

    /**
     * The id of the file that gets rendered as first position
     * The position on the screen selected
     * The index of the selected file
     */
    protected int startFile = 0, selectedPosition, selectedIndex;

    protected ItemStack stack;

    protected boolean shouldRender = true, allowClicks = true;

    public Folder(int awayLeft, int awayTop, int width, int height, int imageSize, ItemStack stack, int showingPhotos) {
        System.out.println("new foldrr");
        this.awayLeft = awayLeft;
        this.awayTop = awayTop;
        this.width = width;
        this.height = height;
        this.imageSize = imageSize;
        this.stack = stack;
        this.showingPhotos = showingPhotos;
    }

    public boolean matchPosition(int x, int y) {
        return Guis.isPointInRegion(awayLeft, awayTop, width, height, x, y);
    }

    public void click(int mouseX, int mouseY) {
        if (allowClicks) {
            int y2 = 0;
            for (int z = startFile; z < startFile + showingPhotos && z < files.size(); z++) {
                if (Guis.isPointInRegion(awayLeft, awayTop + y2 * imageSize, width, imageSize, mouseX, mouseY)) {
                    selectedPosition = y2;
                    selectedIndex = z;
                    break;
                }
                y2++;
            }
        }
    }

    public ImageFile getSelectedFile() {
        if (selectedIndex >= files.size()) {

            return null;
        }
        return files.get(selectedIndex);
    }

    public void renderFolder() {
        if (shouldRender()) {
            Rendering.drawColoredQuad(awayLeft, awayTop, width, height, Color.GRAY.getRGB());
            int y = 0;
            for (int z = startFile; z < startFile + showingPhotos && z < files.size(); z++) {
                if (z == selectedIndex) {
                    Rendering.drawColoredQuad(awayLeft, y * imageSize + awayTop, width, imageSize, Color.blue.getRGB());
                }
                PhotoDataCache.bindTexture(files.get(z).photoID);
                GL11.glColor3f(1, 1, 1);
                Rendering.drawTexturedQuadFit(awayLeft, y * imageSize + awayTop, imageSize, imageSize);
                ItemPhotoStorages itemStorage = (ItemPhotoStorages) stack.getItem();
                NamedPhotoStorage storage = (NamedPhotoStorage) itemStorage.getPhotoStorage(stack);
                Minecraft.getMinecraft().fontRendererObj.drawString(storage.getName(files.get(z).getIndex()), awayLeft + imageSize + 2, y * imageSize + 6 + awayTop, 0);
                y++;
            }
        }
    }

    /**
     * Should get called when something changes
     */
    public void updateFolder() {
        int y = 0;
        for (int z = startFile; z < startFile + showingPhotos && z < files.size(); z++) {
            files.get(z).position = y;
            y++;
        }
    }

    public void shouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    public boolean shouldRender() {
        return shouldRender;
    }

    @Override
    public Iterator<ImageFile> iterator() {
        return files.iterator();
    }

    /**
     * @return the start id where the folder starts rendering the files in the folder
     */
    public int getStartFileID() {
        return startFile;
    }

    public void setStartFileID(int startFile) {
        this.startFile = startFile;
    }

    public int size() {
        return files.size();
    }

    public ImageFile getFileAt(int position) {
        return files.get(position);
    }

    public void clear() {
        files.clear();
    }

    public List<ImageFile> getFiles() {
        return files;
    }

    public Folder setFiles(List<ImageFile> files) {
        this.files = files;
        return this;
    }

    public void allowClicks(boolean allow) {
        this.allowClicks = allow;
    }

    public boolean allowClicks() {
        return this.allowClicks;
    }
}