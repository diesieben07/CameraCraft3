package de.take_weiland.mods.cameracraft.api;

/**
 * <p>Represents the various stages a film can be in.</p>
 * <ul>
 * <li>{@code NEW} is a newly crafted film, ready to be used in a camera.</li>
 * <li>{@code EXPOSED} is film that as been used to take photos in a camera. It can be used in the camera again, causing a second
 * exposure, which is usually undesirable.</li>
 * <li>{@code READY_TO_PROCESS} is film that has been taken out of it's protective casing and ready to be used with chemicals.</li>
 * <li>{@code PROCESSED} is film that has had any chemicals washed off in the water bath so is potentially ready to be projected onto photo paper.
 * This does not necessarily mean the photos on the film have been developed properly.</li>
 * </ul>
 *
 * @author diesieben07
 */
public enum FilmState {

    NEW,
    EXPOSED,
    READY_TO_PROCESS,
    PROCESSED

}
