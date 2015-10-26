package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.entity.EntityScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

/**
 * @author Intektor
 */
public class ItemScreen extends CCItem {

    public ItemScreen() {
        super("screen");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        EntityScreen screen = new EntityScreen(world, x, y, z, Direction.facingToDirection[side]);
        if(screen.onValidSurface() && !world.isRemote) {
            System.out.println("slod");
            world.spawnEntityInWorld(screen);
            return true;
        }

        return false;
    }
}
