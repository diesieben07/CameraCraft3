package de.take_weiland.mods.cameracraft.tileentity;

import static net.minecraftforge.common.ForgeDirection.VALID_DIRECTIONS;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.ForgeDirection;
import de.take_weiland.mods.commons.templates.TileEntityAbstract;

public class TileEntityCable extends TileEntityAbstract {

	// bits 0-6 represent if the given side has a connection
	private byte connectedSides = 0;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		connectedSides = nbt.getByte("connections");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setByte("connections", connectedSides);
	}
	
	public void updateConnections() {
		byte connectedSides = 0;
		for (int i = 0; i < VALID_DIRECTIONS.length; i++) {
			ForgeDirection dir = VALID_DIRECTIONS[i];
			if (worldObj.getBlockId(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ) == getBlockType().blockID) {
				connectedSides |= 1 << i;
			} else {
				connectedSides &= ~(1 << i);
			}
		}
		if (connectedSides != this.connectedSides) {
			this.connectedSides = connectedSides;
			onInventoryChanged();
		}
	}

	public byte getConnectedSides() {
		return connectedSides;
	}

	@Override
	public Packet getDescriptionPacket() {
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, connectedSides, null);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		System.out.println(pkt.actionType);
		connectedSides = (byte) pkt.actionType;
	}

}
