package de.take_weiland.mods.cameracraft.api.camera;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletionStage;

/**
 * <p>A provider for a viewport. A ViewportProvider performs the actual rendering for a viewport.</p>
 * <p>Viewports must be {@linkplain #attach(Viewport) attached} to a provider before they can be rendered. To release any resources associated with an attached
 * viewport, the viewport must be {@linkplain #detach(Viewport) detached}. Multiple viewports can be attached to a single provider.</p>
 *
 * @author diesieben07
 */
public interface ViewportProvider {

    /**
     * <p>Asynchronously grab a rendered image currently visible from the viewport.</p>
     *
     * @param viewport the viewport
     * @return a CompletionStage containing the rendered image or any exception
     */
    CompletionStage<BufferedImage> grabImage(Viewport viewport);

    /**
     * <p>Attach the given Viewport to this provider.</p>
     *
     * @param viewport the viewport
     */
    void attach(Viewport viewport);

    /**
     * <p>Detach the given Viewport and release any resources associated with it. Detached viewports can no longer be rendered through this provider.</p>
     *
     * @param viewport the viewport to detach
     */
    void detach(Viewport viewport);

}
