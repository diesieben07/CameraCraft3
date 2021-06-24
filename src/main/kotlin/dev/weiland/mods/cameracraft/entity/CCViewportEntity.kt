package dev.weiland.mods.cameracraft.entity

import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.network.CreateViewportPacket
import dev.weiland.mods.cameracraft.util.isServer
import dev.weiland.mods.cameracraft.viewport.ServerViewportManager
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.HandSide
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.fml.network.PacketDistributor
import java.util.*

class CCViewportEntity(type: EntityType<out CCViewportEntity>, worldIn: World) : LivingEntity(type, worldIn) {

    private val armor = Collections.nCopies(4, ItemStack.EMPTY).toMutableList()
    private val hands = Collections.nCopies(2, ItemStack.EMPTY).toMutableList()

    override fun getArmorSlots(): MutableIterable<ItemStack> {
        return armor
    }

    override fun setItemSlot(slotIn: EquipmentSlotType, stack: ItemStack) {
        when (checkNotNull(slotIn.type)) {
            EquipmentSlotType.Group.HAND -> hands[slotIn.index] = stack
            EquipmentSlotType.Group.ARMOR -> armor[slotIn.index] = stack
        }
    }

    override fun getItemBySlot(slotIn: EquipmentSlotType): ItemStack {
        return when (checkNotNull(slotIn.type)) {
            EquipmentSlotType.Group.HAND -> hands[slotIn.index]
            EquipmentSlotType.Group.ARMOR -> armor[slotIn.index]
        }
    }

    override fun getMainArm(): HandSide {
        return HandSide.RIGHT
    }

    override fun interactAt(player: PlayerEntity, vec: Vector3d, hand: Hand): ActionResultType {
        return if (hand == Hand.MAIN_HAND) {
            if (player.isServer()) {
                ServerViewportManager.get(player.level as ServerWorld).createViewport(
                    this, player
                )
//                CameraCraft.NETWORK.send(
//                    PacketDistributor.PLAYER.with { player },
//                    CreateViewportPacket(id)
//                )
            }
            ActionResultType.SUCCESS
        } else {
            ActionResultType.PASS
        }
    }

}