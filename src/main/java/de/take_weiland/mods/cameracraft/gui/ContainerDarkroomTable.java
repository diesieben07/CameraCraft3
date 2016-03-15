package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.ChemicalItem;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.cameracraft.tileentity.TileDarkroomTable;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.ShiftClickTarget;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import de.take_weiland.mods.commons.inv.SpecialShiftClick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author diesieben07
 */
public class ContainerDarkroomTable extends AbstractContainer<TileDarkroomTable> implements SpecialShiftClick {

    public ContainerDarkroomTable(EntityPlayer player, TileDarkroomTable inventory) {
        super(player, inventory);
    }

    @Override
    protected void addSlots() {
        for (int i = 0; i < 3; i++) {
            int x = 52 + 28 * i;
            int y = 12 + 22 * i;
            addSlotToContainer(new SimpleSlot(inventory, TileDarkroomTable.getInputSlot(i), x, 12));
            addSlotToContainer(new TraySlot(inventory, TileDarkroomTable.getTraySlot(i), x, 12 + 22));
            addSlotToContainer(new SimpleSlot(inventory, TileDarkroomTable.getOutputSlot(i), x, 12 + 22 * 2));
        }
    }

    @Override
    public ShiftClickTarget getShiftClickTarget(@Nonnull ItemStack stack, @Nonnull EntityPlayer player) {
        if (stack.getItem() == CCItem.misc && CCItem.misc.getType(stack) == MiscItemType.TRAY) {
            return ShiftClickTarget.of(1, 4, 7);
        } else if (CameraCraftApi.get().getCapability(stack, ChemicalItem.class, ChemicalItem::isChemical) != null) {
            return ShiftClickTarget.of(0, 3, 6);
        }
        return ShiftClickTarget.none();
    }

    static final class TraySlot extends SimpleSlot {

        public TraySlot(IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}
