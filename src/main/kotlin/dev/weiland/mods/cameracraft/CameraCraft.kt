package dev.weiland.mods.cameracraft

import dev.weiland.mods.cameracraft.network.CreateViewportPacket
import dev.weiland.mods.cameracraft.util.msg
import dev.weiland.mods.cameracraft.util.syncHandler
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.simple.SimpleChannel
import org.apache.logging.log4j.LogManager

@Mod(CameraCraft.MOD_ID)
@Mod.EventBusSubscriber(modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
internal class CameraCraft {

    init {
        CCEntities.init()
        CCBlocks.init()
        System.setProperty("java.awt.headless", "false")
    }

    companion object {
        const val MOD_ID = "cameracraft"

        val LOGGER = LogManager.getLogger("CameraCraft")

        const val PROTOCOL_VERSION = "1"
        lateinit var NETWORK: SimpleChannel

        val ITEM_GROUP = object : ItemGroup("cameracraft") {
            override fun createIcon(): ItemStack = ItemStack(CCBlocks.TRIPOD)
        }

        @JvmStatic
        @SubscribeEvent
        fun setup(event: FMLCommonSetupEvent) {
            NETWORK = NetworkRegistry.newSimpleChannel(
                ResourceLocation(MOD_ID, "main"),
                { PROTOCOL_VERSION },
                { serverVersion -> PROTOCOL_VERSION == serverVersion },
                { clientVersion -> PROTOCOL_VERSION.equals(clientVersion) }
            ).apply {
                msg<CreateViewportPacket>(0, NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(CreateViewportPacket.Companion::read)
                    .encoder(CreateViewportPacket::write)
                    .syncHandler(CreateViewportPacket::handle)
                    .add()
            }

            event.enqueueWork {
                GlobalEntityTypeAttributes.put(
                    CCEntities.TEST_ENTITY.get(),
                    LivingEntity.registerAttributes().create()
                )
            }
        }

    }

}