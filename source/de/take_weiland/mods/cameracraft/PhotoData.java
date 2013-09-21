package de.take_weiland.mods.cameracraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class PhotoData {

	private String username;
	private String photoName;
	
	public PhotoData(String username, String photoName) {
		this.username = username;
		this.photoName = photoName;
	}
	
	public PhotoData() { }
	
	public static PhotoData fromNbtFile(File file) throws IOException {
		return fromNbtInputStream(new FileInputStream(file));
	}
	
	public static PhotoData fromNbtInputStream(InputStream in) throws IOException {
		return fromNbt(CompressedStreamTools.readCompressed(in));
	}

	public static PhotoData fromNbt(NBTTagCompound nbt) {
		return new PhotoData().readFromNbt(nbt);
	}
	
	public void writeToNbt(NBTTagCompound nbt) {
		nbt.setString("username", username);
		nbt.setString("photoName", photoName);
	}
	
	public PhotoData readFromNbt(NBTTagCompound nbt) {
		username = nbt.getString("username");
		photoName = nbt.getString("photoName");
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	
}
