package de.take_weiland.mods.cameracraft.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.item.ItemDraw;
import de.take_weiland.mods.cameracraft.network.PacketPaint;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

/**
 * @author Intektor
 */
public abstract class EntityPaintable extends EntityHanging implements IEntityAdditionalSpawnData {

    protected long photoId;
    protected ItemStack stack;
    protected BufferedImage bufImage;
    protected DynamicTexture dt;
    private final int resolution = 64;
    protected int dimensionX, dimensionY;
    protected final int scale = 8;

    private boolean forceUpdate;

    public EntityPaintable(World world) {
        super(world);
    }

    public EntityPaintable(World world, int x, int y, int z, int dir, ItemStack stack, int dimX, int dimY) {
        super(world, x, y, z, dir);
        this.photoId = ((PhotoItem) stack.getItem()).getPhotoId(stack);
        this.stack = stack;
        if(stack.getTagCompound().hasKey("imageOverload")) {
            try {
                InputStream in = new ByteArrayInputStream(stack.getTagCompound().getByteArray("imageOverload"));
                bufImage = ImageIO.read(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            bufImage = new BufferedImage(dimX * resolution/scale, dimY * resolution/scale, BufferedImage.TYPE_INT_ARGB);
        }
        dimensionX = dimX;
        dimensionY = dimY;
        setDirection(dir);
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {
        return false;
    }

    public void handlePaintPreCalc(EntityPlayer player, MovingObjectPosition mop){
        double x = 0;
        double y = boundingBox.maxY - mop.hitVec.yCoord;
        switch (hangingDirection) {
            case 0:
                x = mop.hitVec.xCoord - boundingBox.minX;
                break;
            case 1:
                x = mop.hitVec.zCoord - boundingBox.minZ;
                break;
            case 2:
                x = boundingBox.maxX - mop.hitVec.xCoord;
                break;
            case 3:
                x = boundingBox.maxZ - mop.hitVec.zCoord;
                break;
        }

        if (worldObj.isRemote) {
            ItemDraw item = (ItemDraw)player.getCurrentEquippedItem().getItem();
            new PacketPaint(this.getEntityId(), x, y, item.getColorCode(player.getCurrentEquippedItem())).sendToServer();
            paint(player, x, y, item.getColorCode(player.getCurrentEquippedItem()), player.getCurrentEquippedItem());
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setTag("photoStack", stack.writeToNBT(new NBTTagCompound()));
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(bufImage, "png", stream);
            byte[] image = stream.toByteArray();
            nbt.setByteArray("imageOverload", image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        nbt.setInteger("dimX", dimensionX);
        nbt.setInteger("dimY", dimensionY);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("photoStack"));
        Item item = stack != null ? stack.getItem() : null;
        if (item instanceof PhotoItem) {
            photoId = ((PhotoItem) item).getPhotoId(stack);
        } else {
            CameraCraft.logger.warn("Invalid Item in EntityPaintable at " + posX + ", " + posY + ", " + posZ);
            setDead();
        }
        try {
            InputStream in = new ByteArrayInputStream(nbt.getByteArray("imageOverload"));
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dimensionY = nbt.getInteger("dimY");
        dimensionX = nbt.getInteger("dimX");

    }

    public BufferedImage getBufImage() {
        return bufImage;
    }

    public DynamicTexture getDynamicTexture() {
        if (dt == null) {
            dt = new DynamicTexture(bufImage);
        }
        if (forceUpdate) {
            dt.updateDynamicTexture();
        }
        return dt;
    }

    public int getDimensionX() {
        return dimensionX;
    }

    public int getDimensionY() {
        return dimensionY;
    }

    @Override
    public int getWidthPixels() {
        return 64;
    }

    @Override
    public int getHeightPixels() {
        return 64;
    }

    @Override
    public void writeSpawnData(ByteBuf out) {
        out.writeLong(photoId);
        out.writeInt(field_146063_b);
        out.writeInt(field_146064_c);
        out.writeInt(field_146062_d);
        out.writeInt(dimensionX);
        out.writeInt(dimensionY);
        out.writeByte(hangingDirection);
        ByteBufOutputStream bb = new ByteBufOutputStream(out);
        try {
            ImageIO.write(bufImage, "png", bb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readSpawnData(ByteBuf in) {
        photoId = in.readLong();
        field_146063_b = in.readInt();
        field_146064_c = in.readInt();
        field_146062_d = in.readInt(); // setDirection needs these
        dimensionX = in.readInt();
        dimensionY = in.readInt();
        setDirection(in.readByte());
        ByteBufInputStream bi = new ByteBufInputStream(in);
        try {
            bufImage = ImageIO.read(bi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

    }

    public void paint(EntityPlayer painter, double x, double y, int colorCode, ItemStack stack) {
        int resolution = this.resolution/scale;
        int pixelX = (int) (x * resolution);
        int pixelY = (int) (y * resolution);
        if (bufImage != null) {
            if (pixelX >= 0 && pixelY >= 0 && pixelX < bufImage.getWidth() && pixelY < bufImage.getHeight()) {
                bufImage.setRGB(pixelX, pixelY, colorCode);
                forceUpdate = true;
                int[] theDATA = new int[bufImage.getWidth() * bufImage.getHeight()];
                bufImage.getRGB(0, 0, bufImage.getWidth(), bufImage.getHeight(), theDATA, 0, bufImage.getWidth());
                try {
                    dataSetter.invokeExact(dt, theDATA);
                } catch (Throwable t) {
                }
            }
        }
    }

    private static final MethodHandle dataSetter;
    public static final MethodHandle dataGetter;

    static {
        try {
            Field field = DynamicTexture.class.getDeclaredField("dynamicTextureData");
            field.setAccessible(true);
            dataSetter = MethodHandles.publicLookup().unreflectSetter(field);
            dataGetter = MethodHandles.publicLookup().unreflectGetter(field);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBroken(Entity entity) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(bufImage, "png", stream);
            byte[] image = stream.toByteArray();
            stack.getTagCompound().setByteArray("imageOverload", image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        entityDropItem(stack, 0);
    }
}
