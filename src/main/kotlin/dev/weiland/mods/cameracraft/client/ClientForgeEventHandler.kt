package dev.weiland.mods.cameracraft.client

import dev.weiland.mods.cameracraft.CameraCraft
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ScreenshotEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.awt.EventQueue
import java.awt.Image
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

@Mod.EventBusSubscriber(Dist.CLIENT, modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object ClientForgeEventHandler {

    private class ImageSelection(
        private val image: Image
    ) : Transferable {
        override fun getTransferDataFlavors(): Array<DataFlavor> {
            return arrayOf(DataFlavor.imageFlavor)
        }

        override fun isDataFlavorSupported(flavor: DataFlavor?): Boolean {
            return DataFlavor.imageFlavor == flavor
        }

        override fun getTransferData(flavor: DataFlavor?): Any {
            if (flavor != DataFlavor.imageFlavor) throw UnsupportedFlavorException(flavor)
            return image
        }

    }

    @JvmStatic
    @SubscribeEvent
    fun onScreenshot(evt: ScreenshotEvent) {
        val pngBytes = evt.image.bytes
        val bufferedImage = ByteArrayInputStream(pngBytes).use { pngInput ->
            ImageIO.read(pngInput)
        }
        val imageSelection = ImageSelection(bufferedImage)
        EventQueue.invokeLater {
            Toolkit.getDefaultToolkit().systemClipboard.setContents(imageSelection, null)
        }
    }

}