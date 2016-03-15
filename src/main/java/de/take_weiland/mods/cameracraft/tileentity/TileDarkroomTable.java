package de.take_weiland.mods.cameracraft.tileentity;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;
import de.take_weiland.mods.cameracraft.api.ChemicalItem;
import de.take_weiland.mods.cameracraft.api.FilmItem;
import de.take_weiland.mods.cameracraft.api.FilmState;
import de.take_weiland.mods.cameracraft.api.TrayItem;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.commons.tileentity.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * @author diesieben07
 */
public class TileDarkroomTable extends TileEntityInventory implements ISidedInventory {

    private static final int SLOT_DEVELOPER_TRAY = 0, SLOT_FIXER_TRAY = 1, SLOT_WATER_TRAY = 2,
            SLOT_DEVELOPER_INPUT                 = 3, SLOT_FIXER_INPUT = 4, SLOT_WATER_INPUT = 5,
            SLOT_DEVELOPER_OUTPUT                = 6, SLOT_FIXER_OUTPUT = 7, SLOT_WATER_OUTPUT = 8;

    private static final int IDLE           = -1;
    private static final int TRANSFER_DELAY = 20; // 10 ticks = 0.5 seconds

    private final int[] transferDelays   = {IDLE, IDLE, IDLE};
    private final int[] applicationTimes = {-1, -1, -1};

    public TileDarkroomTable() {
        super(9);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        boolean markDirty = false;

        for (int i = 0; i < 3; i++) {
            if (doCountdown(applicationTimes, i)) {
                markDirty |= applyChemical(i);
            } else if (doCountdown(transferDelays, i)) {
                markDirty |= doTransfer(i);
            }
        }

        if (markDirty) {
            markDirty();
        }
    }

    private boolean doCountdown(int[] arr, int i) {
        int c = arr[i];
        if (c < 0) {
            return false;
        } else {
            arr[i]--;
            return c == 0;
        }
    }

    private boolean doTransfer(int traySlot) {
        int inputSlot = getInputSlot(traySlot);
        int outputSlot = getOutputSlot(traySlot);
        ItemStack input = getStackInSlot(inputSlot);
        ItemStack tray = getStackInSlot(traySlot);
        ItemStack output = getStackInSlot(outputSlot);
        ItemStack origTray = tray;

        TrayItem trayItem = TrayItem.get(tray);
        if (trayItem == null || input == null) {
            return false;
        }

        boolean didTransfer = false;

        ChemicalItem inputChemicalItem = ChemicalItem.get(input);
        FilmItem inputFilmItem = FilmItem.get(input);

        ItemStack existingChemicalStack = trayItem.getContainedChemical(tray);
        ItemStack existingFilmStack = trayItem.getContainedFilm(tray);

        ChemicalItem chemicalItem;
        ItemStack chemicalStack;

        FilmItem filmItem;
        ItemStack filmStack;

        // try to transfer a chemical
        if (inputChemicalItem != null && existingChemicalStack == null && output == null) {
            tray = trayItem.setContainedChemical(tray, input);
            didTransfer = true;
            chemicalStack = input;
            chemicalItem = inputChemicalItem;
            setSlotNoMark(outputSlot, new ItemStack(Items.glass_bottle));
        } else {
            chemicalStack = existingChemicalStack;
            chemicalItem = ChemicalItem.get(chemicalStack);
        }

        // try to transfer a film
        if (inputChemicalItem == null && inputFilmItem != null && existingFilmStack == null) {
            tray = trayItem.setContainedFilm(tray, input);
            didTransfer = true;
            filmStack = input;
            filmItem = inputFilmItem;
        } else {
            filmStack = existingFilmStack;
            filmItem = FilmItem.get(filmStack);
        }

        if (input.getItem() == Items.glass_bottle && existingChemicalStack != null && output == null) {
            setSlotNoMark(outputSlot, existingChemicalStack);
            tray = trayItem.setContainedChemical(tray, null);
            didTransfer = true;
        }

        if (tray != origTray) {
            setSlotNoMark(traySlot, tray);
        }

        if (didTransfer) {
            setSlotNoMark(inputSlot, ItemStacks.decreaseSize(input));

            if (filmItem != null && chemicalItem != null) {
                applicationTimes[traySlot] = chemicalItem.applicationTime(chemicalStack, filmStack);
            }
        }

        return didTransfer;
    }

    private boolean applyChemical(int traySlot) {
        int outputSlot = getOutputSlot(traySlot);

        ItemStack trayStack = getStackInSlot(traySlot);
        TrayItem trayItem;

        ItemStack chemicalStack;
        ChemicalItem chemicalItem;
        ItemStack filmStack;

        if (trayStack != null
                && (trayItem = TrayItem.get(trayStack)) != null
                && (chemicalItem = ChemicalItem.get(chemicalStack = trayItem.getContainedChemical(trayStack))) != null
                && FilmItem.get(filmStack = trayItem.getContainedFilm(trayStack)) != null
                && getStackInSlot(outputSlot) == null) {

            filmStack = chemicalItem.applyToFilm(chemicalStack, filmStack);
            chemicalStack = chemicalItem.onChemicalUsed(chemicalStack);

            ItemStack trayOrig = trayStack;
            trayStack = trayItem.setContainedFilm(trayStack, null);
            trayStack = trayItem.setContainedChemical(trayStack, chemicalStack);
            if (trayStack != trayOrig) {
                setSlotNoMark(traySlot, trayStack);
            }
            setSlotNoMark(outputSlot, filmStack);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, @Nullable ItemStack stack) {
        super.setInventorySlotContents(slot, stack);

        transferDelays[getTraySlot(slot)] = TRANSFER_DELAY;
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (isTraySlot(slot)) {
            return stack.getItem() == CCItem.misc && CCItem.misc.getType(stack) == MiscItemType.TRAY;
        } else if (isInputSlot(slot)) {
            FilmItem filmItem;
            return stack.getItem() == Items.glass_bottle || ChemicalItem.get(stack) != null || (filmItem = FilmItem.get(stack)) != null && filmItem.getFilmState(stack) == FilmState.EXPOSED;
        } else if (isOutputSlot(slot)) {
            return false;
        } else {
            return false;
        }
    }

    private static boolean isTraySlot(int slot) {
        return slot <= 2;
    }

    private static boolean isInputSlot(int slot) {
        return slot >= 3 && slot <= 5;
    }

    private static boolean isOutputSlot(int slot) {
        return slot >= 6 && slot <= 8;
    }

    public static int getOutputSlot(int traySlot) {
        return traySlot + 6;
    }

    public static int getInputSlot(int traySlot) {
        return traySlot + 3;
    }

    public static int getTraySlot(int slot) {
        return slot % 3;
    }

    @Override
    public String getDefaultName() {
        return CCBlock.machines.get(MachineType.DARKROOM_TABLE).getUnlocalizedName();
    }

    private static final int[] slots = Ints.toArray(ContiguousSet.create(Range.closedOpen(0, 9), DiscreteDomain.integers()));

    @Override
    public int[] getSlotsForFace(int side) {
        return slots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return false;
    }
}
