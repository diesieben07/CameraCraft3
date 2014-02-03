package de.take_weiland.mods.cameracraft;

import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum CCSounds {

	CAMERA_CLICK("camera_click"),
	CAMERA_REWIND("camera_rewind");

	private final String name;
	
	private CCSounds(String name) {
		this.name = name;
	}
	
	public void playAt(Entity e) {
		playAt(e, 1, 1);
	}
	
	public void playAt(Entity e, float volume, float pitch) {
		e.worldObj.playSoundAtEntity(e, getName(), volume, pitch);
	}
	
	public void playAt(World world, float x, float y, float z) {
		playAt(world, x, y, z, 1, 1);
	}
	
	public void playAt(World world, float x, float y, float z, float volume, float pitch) {
		world.playSoundEffect(x, y, z, getName(), volume, pitch); // does nothing on client
	}

	private String getName() {
		return "cameracraft:" + name;
	}
	
	@SideOnly(Side.CLIENT)
	public void register(SoundManager manager) {
		manager.addSound(getName() + ".ogg");
	}

}
