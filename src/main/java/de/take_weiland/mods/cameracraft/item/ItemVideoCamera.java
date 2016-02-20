package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.entity.EntityVideoCamera;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Intektor
 */
public class ItemVideoCamera extends CCItem {


    public ItemVideoCamera() {
        super("ItemVideoCamera");
    }


    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        System.out.println("use");
        if (!world.isRemote) {
            EntityVideoCamera camera = new EntityVideoCamera(world, x, y, z, player.rotationYaw, player.rotationPitch);
            world.spawnEntityInWorld(camera);
            return true;
        }
        return false;
    }
}
