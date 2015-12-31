package de.take_weiland.mods.cameracraft.db;

import de.take_weiland.mods.cameracraft.api.owner.Owner;
import de.take_weiland.mods.cameracraft.api.photo.Photo;
import de.take_weiland.mods.cameracraft.api.photo.PhotoData;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;

import java.awt.image.BufferedImage;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

/**
 * @author diesieben07
 */
final class DelegatingPhoto implements Photo {

    private final PhotoData                        data;
    private final long                             id;
    private final PhotoDatabase                    database;

    DelegatingPhoto(PhotoData data, long id, PhotoDatabase database) {
        this.data = data;
        this.id = id;
        this.database = database;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public BufferedImage getImage() throws CompletionException {
        return database.getImage(id);
    }

    @Override
    public CompletionStage<BufferedImage> getImageAsync() {
        return database.getImageAsync(id);
    }

    @Override
    public Owner getOwner() {
        return data.getOwner();
    }

    @Override
    public double getX() {
        return data.getX();
    }

    @Override
    public double getY() {
        return data.getY();
    }

    @Override
    public double getZ() {
        return data.getZ();
    }

    @Override
    public int getDimension() {
        return data.getDimension();
    }

    @Override
    public long getTime() {
        return data.getTime();
    }

    @Override
    public long getUnixTime() {
        return data.getUnixTime();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        data.write(out);
    }
}
