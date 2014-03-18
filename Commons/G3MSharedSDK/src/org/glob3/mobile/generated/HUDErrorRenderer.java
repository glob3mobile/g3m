

package org.glob3.mobile.generated;

//
//  HUDErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

//
//  HUDErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//


//class HUDImageRenderer;


public class HUDErrorRenderer
         extends
            ErrorRenderer {
   private final HUDImageRenderer _hudImageRenderer;


   public HUDErrorRenderer() {
      _hudImageRenderer = new HUDImageRenderer(new HUDErrorRenderer_ImageFactory());
   }


   @Override
   public void dispose() {
      super.dispose();
   }


   @Override
   public final void setErrors(final java.util.ArrayList<String> errors) {
      final HUDErrorRenderer_ImageFactory factory = (HUDErrorRenderer_ImageFactory) (_hudImageRenderer.getImageFactory());
      if (factory.setErrors(errors)) {
         _hudImageRenderer.recreateImage();
      }
   }


   @Override
   public final boolean isEnable() {
      return _hudImageRenderer.isEnable();
   }


   @Override
   public final void setEnable(final boolean enable) {
      _hudImageRenderer.setEnable(enable);
   }


   @Override
   public final RenderState getRenderState(final G3MRenderContext rc) {
      return _hudImageRenderer.getRenderState(rc);
   }


   @Override
   public final boolean isPlanetRenderer() {
      return _hudImageRenderer.isPlanetRenderer();
   }


   @Override
   public final SurfaceElevationProvider getSurfaceElevationProvider() {
      return _hudImageRenderer.getSurfaceElevationProvider();
   }


   @Override
   public final PlanetRenderer getPlanetRenderer() {
      return _hudImageRenderer.getPlanetRenderer();
   }


   @Override
   public final void initialize(final G3MContext context) {
      _hudImageRenderer.initialize(context);
   }


   @Override
   public final void render(final G3MRenderContext rc,
                            final GLState glState) {
      _hudImageRenderer.render(rc, glState);
   }


   @Override
   public final boolean onTouchEvent(final G3MEventContext ec,
                                     final TouchEvent touchEvent) {
      return _hudImageRenderer.onTouchEvent(ec, touchEvent);
   }


   @Override
   public final void onResizeViewportEvent(final G3MEventContext ec,
                                           final int width,
                                           final int height) {
      _hudImageRenderer.onResizeViewportEvent(ec, width, height);
   }


   @Override
   public final void start(final G3MRenderContext rc) {
      _hudImageRenderer.start(rc);
   }


   @Override
   public final void stop(final G3MRenderContext rc) {
      _hudImageRenderer.stop(rc);
   }


   @Override
   public final void onResume(final G3MContext context) {
      _hudImageRenderer.onResume(context);
   }


   @Override
   public final void onPause(final G3MContext context) {
      _hudImageRenderer.onPause(context);
   }


   @Override
   public final void onDestroy(final G3MContext context) {
      _hudImageRenderer.onDestroy(context);
   }

}
