package dev.weiland.mods.cameracraft.entity

import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.network.CreateViewportPacket
import dev.weiland.mods.cameracraft.util.isServer
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.HandSide
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.fml.network.PacketDistributor
import java.util.*

class CCTestEntity(type: EntityType<out CCTestEntity>, worldIn: World) : LivingEntity(type, worldIn) {

    private val armor = Collections.nCopies(4, ItemStack.EMPTY).toMutableList()
    private val hands = Collections.nCopies(2, ItemStack.EMPTY).toMutableList()

    override fun getArmorInventoryList(): MutableIterable<ItemStack> {
        return armor
    }

    override fun setItemStackToSlot(slotIn: EquipmentSlotType, stack: ItemStack) {
        when (checkNotNull(slotIn.slotType)) {
            EquipmentSlotType.Group.HAND -> hands[slotIn.index] = stack
            EquipmentSlotType.Group.ARMOR -> armor[slotIn.index] = stack
        }
    }

    override fun getItemStackFromSlot(slotIn: EquipmentSlotType): ItemStack {
        return when (checkNotNull(slotIn.slotType)) {
            EquipmentSlotType.Group.HAND -> hands[slotIn.index]
            EquipmentSlotType.Group.ARMOR -> armor[slotIn.index]
        }
    }

    override fun getPrimaryHand(): HandSide {
        return HandSide.RIGHT
    }

    override fun applyPlayerInteraction(player: PlayerEntity, vec: Vector3d, hand: Hand): ActionResultType {
        return if (hand == Hand.MAIN_HAND) {
            if (player.isServer()) {
                CameraCraft.NETWORK.send(
                    PacketDistributor.PLAYER.with { player },
                    CreateViewportPacket(entityId)
                )
            }
            ActionResultType.SUCCESS
        } else {
            ActionResultType.PASS
        }
    }

}