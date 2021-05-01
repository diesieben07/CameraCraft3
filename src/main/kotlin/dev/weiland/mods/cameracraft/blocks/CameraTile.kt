package dev.weiland.mods.cameracraft.blocks

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.weiland.mods.cameracraft.CCTEs
import dev.weiland.mods.cameracraft.client.DummyModelData
import dev.weiland.mods.cameracraft.network.sendToAllTracking
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.client.model.data.IModelData
import net.minecraftforge.common.util.Constants

internal class CameraTile(tileEntityTypeIn: TileEntityType<*> = CCTEs.CAMERA) : TileEntity(tileEntityTypeIn) {

    companion object {
        val ROTATION_STEPS = 36
//
//        private val c = run<Codec<CameraTile>> {
//            RecordCodecBuilder.create { builder ->
//                builder.group(
//                        Codec.BYTE.xmap(Byte::toInt, Int::toByte).fieldOf("rotation").forGetter(CameraTile::rotation)
//                ).appl
//            }
//        }

//        private val CODEC = Codec.BYTE.xmap(
//                { byte -> byte.toInt() },
//                { int -> int.toByte() }
//        ).fieldOf("rotation").forGetter(CameraTile::rotation).

    }

    private var _rotation: Int = 0
    @Volatile private var modelData = ModelData(_rotation)

    var rotation: Int
        get() = _rotation
        set(newValue) {
            require(newValue in 0 until ROTATION_STEPS)

            _rotation = newValue
            setChanged()
            updatePacket.sendToAllTracking(this)
        }

    private fun clientSetRotation(rotation: Int) {
        _rotation = rotation.coerceIn(0 until ROTATION_STEPS)
        modelData = ModelData(rotation)
        requestModelDataUpdate()
        level?.sendBlockUpdated(blockPos, blockState, blockState, Constants.BlockFlags.DEFAULT)
    }

    override fun save(compound: CompoundNBT): CompoundNBT {
        val nbt = super.save(compound)
        nbt.putByte("rotation", rotation.toByte())
        return nbt
    }

    override fun load(state: BlockState, nbt: CompoundNBT) {
        super.load(state, nbt)
        _rotation = nbt.getByte("rotation").toInt()
    }

    override fun getUpdateTag(): CompoundNBT {
        val nbt = super.getUpdateTag()
        nbt.putByte("rotation", _rotation.toByte())
        return nbt
    }

    override fun handleUpdateTag(state: BlockState?, tag: CompoundNBT) {
        super.handleUpdateTag(state, tag)
        clientSetRotation(tag.getByte("rotation").toInt())
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket {
        return SUpdateTileEntityPacket(blockPos, _rotation, CompoundNBT())
    }

    override fun onDataPacket(net: NetworkManager?, pkt: SUpdateTileEntityPacket) {
        clientSetRotation(pkt.type)
    }

    data class ModelData(val rotation: Int) : DummyModelData

    override fun getModelData(): IModelData {
        return modelData
    }

}