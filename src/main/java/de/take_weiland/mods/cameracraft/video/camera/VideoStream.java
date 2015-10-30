package de.take_weiland.mods.cameracraft.video.camera;

import com.xcompwiz.lookingglass.api.view.IWorldView;

/**
 * @author Intektor
 */
public class VideoStream {

    private IWorldView view;
    private String streamID;

    public VideoStream(String streamID, IWorldView view) {
        this.streamID = streamID;
        this.view = view;
    }

    public String getStreamID() {
        return streamID;
    }

    public IWorldView getView() {
        return view;
    }
}
