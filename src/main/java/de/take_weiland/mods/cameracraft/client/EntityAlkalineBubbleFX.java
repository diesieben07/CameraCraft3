package de.take_weiland.mods.cameracraft.client;

import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.world.World;

public class EntityAlkalineBubbleFX extends EntityBubbleFX {

	public EntityAlkalineBubbleFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
		super(world, x, y, z, motionX, motionY, motionZ);
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY += 0.002D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.8500000238418579D;
		motionY *= 0.8500000238418579D;
		motionZ *= 0.8500000238418579D;

		if (particleMaxAge-- <= 0) {
			this.setDead();
		}
	}
}
