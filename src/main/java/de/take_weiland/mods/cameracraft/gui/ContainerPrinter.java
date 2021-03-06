package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.ShiftClickTarget;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import de.take_weiland.mods.commons.inv.SpecialShiftClick;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ContainerPrinter extends AbstractContainer<TilePrinter> implements SpecialShiftClick {

    public static final int PROGRESSBAR_WIDTH = 100;

    public ContainerPrinter(EntityPlayer player, TilePrinter te) {
        super(player, 8, 118, te);
    }

	@Override
    public ShiftClickTarget getShiftClickTarget(ItemStack stack, EntityPlayer player) {
        Item item;
        if (ItemStacks.is(stack, Items.paper)) {
            return ShiftClickTarget.of(4);
        } else if ((item = stack.getItem()) instanceof InkItem) {
            return ShiftClickTarget.of(TilePrinter.INK_COLOR_TO_SLOT[((InkItem) item).getColor(stack).ordinal()]);
        } else if (item instanceof PhotoStorageItem && ((PhotoStorageItem) item).isRandomAccess(stack)) {
            return ShiftClickTarget.of(5);
        } else {
            return ShiftClickTarget.none();
        }
    }

	@Override
	protected void addSlots() {
		for (int x = 0; x < 5; ++x) {
			addSlotToContainer(new SimpleSlot(inventory, x, x * 27 + 44, 91));
		}

        addSlotToContainer(new SimpleSlot(inventory, TilePrinter.SLOT_STORAGE, 17, 91));
	}

}
