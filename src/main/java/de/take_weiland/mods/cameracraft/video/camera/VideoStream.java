package de.take_weiland.mods.cameracraft.video.camera;

import com.xcompwiz.lookingglass.api.view.IWorldView;
import net.minecraft.entity.Entity;

/**
 * @author Intektor
 */
public class VideoStream {

    private Entity viewer;
    private String streamID;
    private IWorldView view;

    public VideoStream(String streamID, Entity viewer) {
        this.streamID = streamID;
        this.viewer = viewer;
    }

    public String getStreamID() {
        return streamID;
    }

    public Entity getViewer() {
        return viewer;
    }

    public void setView(IWorldView view) {
        this.view = view;
    }

    public IWorldView getView() {
        return view;
    }
}
