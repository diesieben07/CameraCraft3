package de.take_weiland.mods.cameracraft.api.camera;

/**
 * <p>Factory for ViewportProviders. Can be used to create an alternative way to obtain images of Viewports, such as a
 * remote rendering server.</p>
 *
 * @author diesieben07
 */
public interface ViewportProviderFactory {

    /**
     * <p>Get a ViewportProvider for the given Viewport or null if this factory cannot support the given Viewport.</p>
     *
     * @param viewport the viewport
     * @return a provider or null
     */
    ViewportProvider getProvider(Viewport viewport);

}
