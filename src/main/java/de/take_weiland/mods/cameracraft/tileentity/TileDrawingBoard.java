package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.commons.tileentity.TileEntityInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

/**
 * @author Intektor
 */
public class TileDrawingBoard extends TileEntityInventory implements ISidedInventory {

    public TileDrawingBoard() {
    }

    public TileDrawingBoard(int size) {
        super(size);
    }

    @Override
    public int[] getSlotsForFace(int num) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int num1, ItemStack stack, int num2) {
        return false;
    }

    @Override
    public boolean canExtractItem(int num1, ItemStack stack, int num2) {
        return false;
    }

    @Override
    public String getDefaultName() {
        return "drawing.board";
    }
}
