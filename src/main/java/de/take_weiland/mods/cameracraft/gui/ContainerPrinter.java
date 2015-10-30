package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.ShiftClickTarget;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import de.take_weiland.mods.commons.inv.SpecialShiftClick;
import de.take_weiland.mods.commons.sync.Sync;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ContainerPrinter extends AbstractContainer<TilePrinter> implements SpecialShiftClick {

    public static final int PROGRESSBAR_WIDTH = 100;

	protected ContainerPrinter(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player, 8, 118);
	}

	@Override
    public ShiftClickTarget getShiftClickTarget(ItemStack stack, EntityPlayer player) {
        Item item;
        if (ItemStacks.is(stack, Items.paper)) {
            return ShiftClickTarget.of(4);
        } else if ((item = stack.getItem()) instanceof InkItem) {
            return ShiftClickTarget.of(TilePrinter.INK_COLOR_TO_SLOT[((InkItem) item).getColor(stack).ordinal()]);
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

    @Sync
    public int printProgress;

    @Override
    public void detectAndSendChanges() {
        printProgress = inventory.getJobProgress();
        super.detectAndSendChanges();
    }
}
