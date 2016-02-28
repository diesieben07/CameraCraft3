package de.take_weiland.mods.cameracraft.api.camera;

/**
 * <p>A viewport into the world such as an entity. A viewport can be fixed or moving.</p>
 * <p>A viewport has an attached provider which can provide the actual rendering of the viewport.</p>
 *
 * @author diesieben07
 */
public interface Viewport {

    /**
     * <p>Get the dimension ID the viewport is located in.</p>
     *
     * @return the dimension ID
     */
    int dimension();

    /**
     * <p>Get the x coordinate of the provider.</p>
     *
     * @return the x coordinate
     */
    double x();

    /**
     * <p>Get the y coordinate of the provider.</p>
     *
     * @return the y coordinate
     */
    double y();

    /**
     * <p>Get the z coordinate of the provider.</p>
     *
     * @return the z coordinate
     */
    double z();

    /**
     * <p>Get the camera pitch of the provider.</p>
     *
     * @return the camera pitch
     */
    float pitch();

    /**
     * <p>Get the camera yaw of the provider.</p>
     *
     * @return the camera yaw
     */
    float yaw();

    /**
     * <p>Callback method for when this viewport's attached provider is invalidated and the viewport must choose a different one.
     * Invalidation can occur for example when the provider is a player and that player logs out.</p>
     */
    void providerInvalidated();

    /**
     * <p>Get the provider currently attached to this viewport.</p>
     *
     * @return the provider
     */
    ViewportProvider getProvider();

}
