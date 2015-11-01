package de.take_weiland.mods.cameracraft.inv;

import de.take_weiland.mods.cameracraft.CCPlayerData;
import de.take_weiland.mods.cameracraft.CCSounds;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.camera.Camera;
import de.take_weiland.mods.cameracraft.api.camera.LensItem;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.cameracraft.img.ImageUtil;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.commons.inv.ItemInventory;
import gnu.trove.iterator.TLongIterator;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static de.take_weiland.mods.commons.util.Sides.sideOf;

public class InventoryCameraImpl extends ItemInventory implements Camera {

    public static final int LENS_SLOT = 0;

    private boolean isLidClosed;

    private final CameraType type;

    private final World world;
    private final Vec3 position;

    public InventoryCameraImpl(CameraType type, ItemStack stack, Consumer<ItemStack> stackCallback, World world, Vec3 position) {
        super(type.slotCount, stack, stackCallback);
        this.type = type;
        this.world = world;
        this.position = position;
        loadData();

    }

    public final CameraType getType() {
        return type;
    }

    public final int storageSlot() {
        return type == CameraType.DIGITAL ? 2 : 1;
    }

    public final int batterySlot() {
        return type == CameraType.DIGITAL ? 1 : -1;
    }

    @Override
    public boolean hasLid() {
        return type == CameraType.FILM;
    }

    @Override
    public boolean canRewind() {
        return type == CameraType.FILM && storage[storageSlot()] != null;
    }

    @Override
    public boolean needsBattery() {
        return type == CameraType.DIGITAL;
    }

    @Override
    public void setLidState(boolean close) {
        if (hasLid()) {
            if (!close) {
                openLid();
            }
            isLidClosed = close;
            markDirty();
        }
    }

    @Override
    public boolean takePhoto() {
        // TODO
        return false;
    }

    @Override
    public void openGui(EntityPlayer player) {
        CCGuis.CAMERA.open(player);
    }

    private CompletionStage<Void>[] convertTasks;

    private void openLid() {
        if (sideOf(world).isClient()) {
            return;
        }
        PhotoStorage storage = getPhotoStorage();
        if (storage != null && !storage.isSealed()) {
            waitForRemainingTasks();
            convertTasks = ImageUtil.applyFilter(storage, ImageFilters.OVEREXPOSE);
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return position.squareDistanceTo(player.posX, player.posY, player.posZ) <= 64D;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (hasLid()) {
            nbt.setBoolean("lid", isLidClosed);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (type == null) return; // not yet initialized, call from super constructor
        super.readFromNBT(nbt);
        if (hasLid()) {
            isLidClosed = nbt.getBoolean("lid");
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        if (slot == LENS_SLOT) {
            return item.getItem() instanceof LensItem;
        } else {
            return getType().isItemValid(slot, item);
        }
    }

    @Override
    public void closeChest() {
        super.closeChest();
        dispose();
    }

    private ItemStack lastStorageStack;
    private PhotoStorage lastStorage;

    private void waitForRemainingTasks() {
        if (convertTasks != null) {
            try {
                long start = System.currentTimeMillis();
                for (CompletionStage<Void> task : convertTasks) {
                    task.toCompletableFuture().get();
                }
                System.out.println("spent " + (System.currentTimeMillis() - start) + " ms cleaning up " + convertTasks.length + " tasks");
            } catch (ExecutionException e) {
                // TODO
                CrashReport cr = CrashReport.makeCrashReport(e, "Applying filters to CameraCraft photos");
                throw new ReportedException(cr);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            convertTasks = null;
        }
    }

    // Public API

    @Override
    public PhotoStorage getPhotoStorage() {
        ItemStack storageSlot = storage[storageSlot()];
        if (storageSlot == lastStorageStack) {
            return lastStorage;
        } else {
            lastStorageStack = storageSlot;
            if (storageSlot == null) {
                lastStorage = null;
            } else {
                Item item = storageSlot.getItem();
                if (item instanceof PhotoStorageItem) {
                    lastStorage = new WrappedPhotoStorage(((PhotoStorageItem) item).getPhotoStorage(storageSlot), this);
                } else {
                    lastStorage = null;
                }
            }
            return lastStorage;
        }
    }

    @Override
    public boolean hasBattery() {
        return getBattery() != null;
    }

    @Override
    public ItemStack getBattery() {
        return needsBattery() ? getStackInSlot(batterySlot()) : null;
    }

    private int getBatteryCharge() {
        if (!needsBattery()) {
            return Integer.MAX_VALUE;
        }
        ItemStack b = getBattery();
        if (b == null) {
            return 0;
        }
        BatteryHandler handler = CameraCraft.api.findBatteryHandler(b);
        return handler.getCharge(b);
    }

    @Override
    public ItemStack getCamera() {
        return stack.copy();
    }

    @Override
    public ItemStack getLens() {
        ItemStack lens = storage[LENS_SLOT];
        return lens == null || !(lens.getItem() instanceof LensItem) ? null : lens;
    }

    @Override
    public boolean hasStorage() {
        ItemStack storageSlot = storage[storageSlot()];
        return storageSlot != null && storageSlot.getItem() instanceof PhotoStorageItem;
    }

    @Override
    public ImageFilter getFilter() {
        ItemStack lensStack = getLens();
        LensItem lens = lensStack == null ? null : (LensItem) lensStack.getItem();
        PhotoStorage storage = getPhotoStorage();

        ImageFilter lensFilter = lens == null ? null : lens.getFilter(lensStack);
        ImageFilter storageFilter = storage == null ? null : storage.getFilter();

        if (hasLid() && !isLidClosed()) {
            return ImageFilters.combine(lensFilter, ImageFilters.OVEREXPOSE, storageFilter);
        } else {
            return ImageFilters.combine(lensFilter, storageFilter);
        }
    }

    @Override
    public boolean isLidClosed() {
        return isLidClosed;
    }

    @Override
    public boolean canTakePhoto() {
        return getBatteryCharge() > 0 && hasStorage() && getPhotoStorage().canStore();
    }

    @Override
    public void dispose() {
        waitForRemainingTasks();
    }

    @Override
    public void onTakePhoto() {
        CCSounds.CAMERA_CLICK.play(world, position.xCoord, position.yCoord, position.zCoord);
        ItemStack battery = getBattery(); // is null if we don't need battery
        if (battery != null) {
            BatteryHandler handler = CameraCraft.api.findBatteryHandler(battery);
            handler.drain(battery, 10);
        }
    }

    @Override
    public boolean rewind() {
        if (canRewind()) {
            ItemStack film = storage[storageSlot()];
            if (film != null) {
                Item filmItem = film.getItem();
                if (filmItem instanceof PhotoStorageItem) {
                    storage[storageSlot()] = ((PhotoStorageItem) filmItem).rewind(film);
                    setLidState(OPEN);
                    CCSounds.CAMERA_REWIND.play(world, position.xCoord, position.yCoord, position.zCoord);
                    return true;
                }
            }
        }
        return false;
    }

    public static class WithPlayer extends InventoryCameraImpl {

        private static final int COOLDOWN = 30;
        private final EntityPlayer player;

        public WithPlayer(EntityPlayer player, CameraType type, ItemStack stack, Consumer<ItemStack> stackCallback, World world, Vec3 position) {
            super(type, stack, stackCallback, world, position);
            this.player = player;
        }

        @Override
        public boolean canTakePhoto() {
            return !CCPlayerData.get(player).isOnCooldown() && super.canTakePhoto();
        }

        @Override
        public void onTakePhoto() {
            super.onTakePhoto();
            CCPlayerData.get(player).setCooldown(COOLDOWN);
        }
    }

    private static class WrappedPhotoStorage implements PhotoStorage {

        private final PhotoStorage delegate;
        private final InventoryCameraImpl camera;

        WrappedPhotoStorage(PhotoStorage delegate, InventoryCameraImpl camera) {
            this.delegate = delegate;
            this.camera = camera;
        }

        @Override
        public TLongIterator longIterator() {
            return delegate.longIterator();
        }

        @Override
        public boolean isSealed() {
            return delegate.isSealed();
        }

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public int indexOf(long photoId) {
            return delegate.indexOf(photoId);
        }

        @Override
        public void remove(int index) {
            delegate.remove(index);
            camera.markDirty();
        }

        @Override
        public ImageFilter getFilter() {
            return delegate.getFilter();
        }

        @Override
        public int store(long photoId) {
            System.out.println(photoId);
            int result = delegate.store(photoId);
            camera.markDirty();
            return result;
        }

        @Override
        public long[] toLongArray() {
            return delegate.toLongArray();
        }

        @Override
        public void forEach(Consumer<? super Long> action) {
            delegate.forEach(action);
        }

        @Override
        public long get(int index) {
            return delegate.get(index);
        }

        @Override
        public boolean isFull() {
            return delegate.isFull();
        }

        @Override
        public int capacity() {
            return delegate.capacity();
        }

        @Override
        public void clear() {
            delegate.clear();
            camera.markDirty();
        }

        @Override
        public boolean canStore() {
            return delegate.canStore();
        }

        @Override
        public Iterator<Long> iterator() {
            Iterator<Long> it = delegate.iterator();
            return new Iterator<Long>() {
                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public Long next() {
                    return it.next();
                }

                @Override
                public void remove() {
                    it.remove();
                    camera.markDirty();
                }
            };
        }

        @Override
        public Spliterator<Long> spliterator() {
            return delegate.spliterator();
        }
    }

}
