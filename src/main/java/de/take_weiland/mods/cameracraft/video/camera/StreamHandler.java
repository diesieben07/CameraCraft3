package de.take_weiland.mods.cameracraft.video.camera;

import java.util.ArrayList;

/**
 * @author Intektor
 */
public class StreamHandler {

    private static ArrayList<VideoStream> streams = new ArrayList<VideoStream>();

    public static void addVideoStream(VideoStream stream) {
        System.out.println("add");
        if (!hasVideoStream(stream.getStreamID())) {
            streams.add(stream);
        }
    }

    public static VideoStream getStreamByID(String channel) {
        for (VideoStream stream : streams) {
            if (stream.getStreamID().equals(channel)) {
                return stream;
            }
        }
        return null;
    }

    public static boolean hasVideoStream(String channel) {
        for (VideoStream stream : streams) {
            if (stream.getStreamID().equals(channel)) {
                return true;
            }
        }
        return false;
    }
}
