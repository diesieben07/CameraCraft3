package dev.weiland.mods.cameracraft.entity

import dev.weiland.mods.cameracraft.CCBlocks
import dev.weiland.mods.cameracraft.CCEntities
import dev.weiland.mods.cameracraft.CCItems
import dev.weiland.mods.cameracraft.blocks.CameraTile
import dev.weiland.mods.cameracraft.items.CameraItem
import dev.weiland.mods.cameracraft.util.getValue
import dev.weiland.mods.cameracraft.util.setValue
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntitySize
import net.minecraft.entity.EntityType
import net.minecraft.entity.Pose
import net.minecraft.entity.item.minecart.AbstractMinecartEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
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
        val CAMERA_STACK: DataParameter<ItemStack> = EntityDataManager.defineId(TripodMinecartEntity::class.java, DataSerializers.ITEM_STACK)
        val CAMERA_ROTATION: DataParameter<Byte> = EntityDataManager.defineId(TripodMinecartEntity::class.java, DataSerializers.BYTE)
    }

    var cameraStack: ItemStack by CAMERA_STACK
    var cameraRotation: Byte by CAMERA_ROTATION

    val cameraBlock: BlockState
        get() = ((cameraStack.item as? BlockItem)?.block ?: Blocks.AIR).defaultBlockState()

    override fun interact(player: PlayerEntity, hand: Hand): ActionResultType {
        val stack = player.getItemInHand(hand)
        val item = stack.item
        return if (item is CameraItem) {
            if (!cameraStack.isEmpty) {
                ActionResultType.FAIL
            } else {
                if (!level.isClientSide) {
                    cameraStack = stack.copy()
                }
                ActionResultType.sidedSuccess(level.isClientSide)
            }
        } else if (!cameraStack.isEmpty) {
            if (!level.isClientSide) {
                cameraRotation = (cameraRotation + 1).rem(CameraTile.ROTATION_STEPS).toByte()
            }
            ActionResultType.sidedSuccess(level.isClientSide)
        } else {
            ActionResultType.PASS
        }
    }

    override fun onSyncedDataUpdated(key: DataParameter<*>) {
        super.onSyncedDataUpdated(key)
        if (key == CAMERA_STACK) {
            refreshDimensions()
        }
    }

    override fun getDimensions(poseIn: Pose): EntitySize {
        val baseSize = super.getDimensions(poseIn)
        return if (cameraStack.isEmpty) {
            baseSize
        } else {
            EntitySize.scalable(baseSize.width, baseSize.height + 0.5f)
        }
    }


    override fun defineSynchedData() {
        super.defineSynchedData()
        entityData.define(CAMERA_STACK, ItemStack.EMPTY)
        entityData.define(CAMERA_ROTATION, 0)
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun addAdditionalSaveData(nbt: CompoundNBT) {
        super.addAdditionalSaveData(nbt)
        nbt.put("camera_stack", cameraStack.save(CompoundNBT()))
        nbt.putByte("camera_rotation", cameraRotation)
    }

    override fun readAdditionalSaveData(nbt: CompoundNBT) {
        super.readAdditionalSaveData(nbt)
        cameraStack = ItemStack.of(nbt.getCompound("camera_stack"))
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

    override fun getDefaultDisplayBlockState(): BlockState {
        return CCBlocks.TRIPOD.defaultBlockState()
    }

    override fun hasCustomDisplay(): Boolean {
        // Not supported
        return false
    }

    override fun destroy(source: DamageSource) {
        super.destroy(source)
        if (level.gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            spawnAtLocation(CCBlocks.TRIPOD)
            if (!cameraStack.isEmpty) {
                spawnAtLocation(cameraStack)
            }
        }
    }

    override fun applyNaturalSlowdown() {
        deltaMovement = deltaMovement.multiply(0.997, 0.0, 0.997)
    }

}