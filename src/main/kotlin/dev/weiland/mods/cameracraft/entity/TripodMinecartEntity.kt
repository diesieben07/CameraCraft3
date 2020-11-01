package dev.weiland.mods.cameracraft.entity

import dev.weiland.mods.cameracraft.CCBlocks
import dev.weiland.mods.cameracraft.CCEntities
import dev.weiland.mods.cameracraft.CCItems
import dev.weiland.mods.cameracraft.blocks.CameraBlock
import dev.weiland.mods.cameracraft.blocks.CameraTile
import dev.weiland.mods.cameracraft.items.CameraItem
import dev.weiland.mods.cameracraft.util.getValue
import dev.weiland.mods.cameracraft.util.setValue
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntitySize
import net.minecraft.entity.EntityType
import net.minecraft.entity.Pose
import net.minecraft.entity.item.minecart.AbstractMinecartEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.network.IPacket
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.ActionResultType
import net.minecraft.util.DamageSource
import net.minecraft.util.Hand
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks

internal class TripodMinecartEntity : AbstractMinecartEntity {

    constructor(type: EntityType<*>, worldIn: World, posX: Double, posY: Double, posZ: Double) : super(type, worldIn, posX, posY, posZ)
    constructor(worldIn: World, posX: Double, posY: Double, posZ: Double) : super(CCEntities.TRIPOD_MINECART, worldIn, posX, posY, posZ)
    constructor(type: EntityType<*>, worldIn: World) : super(type, worldIn)

    private companion object {
        val CAMERA_STACK: DataParameter<ItemStack> = EntityDataManager.createKey(TripodMinecartEntity::class.java, DataSerializers.ITEMSTACK)
        val CAMERA_ROTATION: DataParameter<Byte> = EntityDataManager.createKey(TripodMinecartEntity::class.java, DataSerializers.BYTE)
    }

    var cameraStack: ItemStack by CAMERA_STACK
    var cameraRotation: Byte by CAMERA_ROTATION

    val cameraBlock: BlockState
        get() = ((cameraStack.item as? BlockItem)?.block ?: Blocks.AIR).defaultState

    override fun processInitialInteract(player: PlayerEntity, hand: Hand): ActionResultType {
        val stack = player.getHeldItem(hand)
        val item = stack.item
        return if (item is CameraItem) {
            if (!cameraStack.isEmpty) {
                ActionResultType.FAIL
            } else {
                if (!world.isRemote) {
                    cameraStack = stack.copy()
                }
                ActionResultType.func_233537_a_(world.isRemote)
            }
        } else if (!cameraStack.isEmpty) {
            if (!world.isRemote) {
                cameraRotation = (cameraRotation + 1).rem(CameraTile.ROTATION_STEPS).toByte()
            }
            ActionResultType.func_233537_a_(world.isRemote)
        } else {
            ActionResultType.PASS
        }
    }

    override fun notifyDataManagerChange(key: DataParameter<*>) {
        super.notifyDataManagerChange(key)
        if (key == CAMERA_STACK) {
            recalculateSize()
        }
    }

    override fun getSize(poseIn: Pose): EntitySize {
        val baseSize = super.getSize(poseIn)
        return if (cameraStack.isEmpty) {
            baseSize
        } else {
            EntitySize.flexible(baseSize.width, baseSize.height + 0.5f)
        }
    }

    override fun registerData() {
        super.registerData()
        dataManager.register(CAMERA_STACK, ItemStack.EMPTY)
        dataManager.register(CAMERA_ROTATION, 0)
    }

    override fun createSpawnPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun writeAdditional(nbt: CompoundNBT) {
        super.writeAdditional(nbt)
        nbt.put("camera_stack", cameraStack.write(CompoundNBT()))
        nbt.putByte("camera_rotation", cameraRotation)
    }

    override fun readAdditional(nbt: CompoundNBT) {
        super.readAdditional(nbt)
        cameraStack = ItemStack.read(nbt.getCompound("camera_stack"))
        cameraRotation = nbt.getByte("camera_rotation")
    }

    override fun getMinecartType(): Type {
        // TODO: Huh?
        return Type.CHEST
    }

    override fun getCartItem(): ItemStack {
        return ItemStack(CCItems.TRIPOD_MINECART)
    }

    override fun canBeRidden(): Boolean {
        return false
    }

    override fun isPoweredCart(): Boolean {
        return false
    }

    override fun getDefaultDisplayTile(): BlockState {
        return CCBlocks.TRIPOD.defaultState
    }

    override fun hasDisplayTile(): Boolean {
        // Not supported
        return false
    }

    override fun killMinecart(source: DamageSource) {
        super.killMinecart(source)
        if (world.gameRules.getBoolean(GameRules.DO_ENTITY_DROPS)) {
            entityDropItem(CCBlocks.TRIPOD)
            if (!cameraStack.isEmpty) {
                entityDropItem(cameraStack)
            }
        }
    }

    override fun applyDrag() {
        motion = motion.mul(0.997, 0.0, 0.997)
    }

}