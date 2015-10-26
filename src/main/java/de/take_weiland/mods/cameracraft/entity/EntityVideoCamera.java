package de.take_weiland.mods.cameracraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * @author Intektor
 */
public class EntityVideoCamera extends Entity {

    public EntityVideoCamera(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
    }

    public EntityVideoCamera(World world, double posX, double posY, double posZ) {
        super(world);
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.setSize(1, 1);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        setDead();
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
}

