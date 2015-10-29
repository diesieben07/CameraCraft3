package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.CCPlayerData;
import de.take_weiland.mods.cameracraft.entity.EntityScreen;
import de.take_weiland.mods.cameracraft.entity.EntityVideoCamera;
import de.take_weiland.mods.cameracraft.network.PacketStreamID;
import de.take_weiland.mods.commons.client.Guis;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author Intektor
 */
public class GuiStreamID extends GuiScreen {

    private GuiTextField channel;

    public GuiStreamID() {
        super();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawCenteredString(fontRendererObj, "Please enter the stream channel!", width/2, height/2 - 20, Color.red.getRGB());
    }

    @Override
    public void initGui() {
        channel = Guis.addTextField(this, new GuiTextField(fontRendererObj, width/2 - 20, height/2 - 8, 40, 16));
        channel.setMaxStringLength(5);
        NBTTagCompound nbt = new NBTTagCompound();
        Entity entity = mc.theWorld.getEntityByID(CCPlayerData.get(mc.thePlayer).getLastClickedEntityID());
        entity.writeToNBT(nbt);
        channel.setText(nbt.getString("StreamID"));
    }

    @Override
    public void onGuiClosed() {
        new PacketStreamID(channel.getText()).sendToServer();
        Entity entity = mc.thePlayer.worldObj.getEntityByID(CCPlayerData.get(mc.thePlayer).getLastClickedEntityID());
        if(entity != null) {
            if(entity instanceof EntityVideoCamera) {
                EntityVideoCamera camera = (EntityVideoCamera) entity;
                camera.setStreamID(channel.getText());
            }else if(entity instanceof EntityScreen) {
                EntityScreen screen = (EntityScreen) entity;
                screen.setStreamID(channel.getText());
            }
        }
    }

    @Override
    public void handleKeyboardInput() {
        super.handleKeyboardInput();
        if(Keyboard.isCreated()) {
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                mc.setIngameFocus();
            }
        }
    }
}
