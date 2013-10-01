

package org.glob3.mobile.generated;

//
//  LeafRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

//
//  LeafRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//


//class GPUProgramState;

public abstract class LeafRenderer
         implements
            Renderer {
   private boolean _enable;


   public LeafRenderer() {
      _enable = true;

   }


   public LeafRenderer(final boolean enable) {
      _enable = enable;

   }


   @Override
   public void dispose() {
      super.dispose();

   }


   @Override
   public final boolean isEnable() {
      return _enable;
   }


   @Override
   public void setEnable(final boolean enable) {
      _enable = enable;
   }


   @Override
   public abstract void onResume(G3MContext context);


   @Override
   public abstract void onPause(G3MContext context);


   @Override
   public abstract void onDestroy(G3MContext context);


   @Override
   public abstract void initialize(G3MContext context);


   @Override
   public abstract RenderState getRenderState(G3MRenderContext rc);


   @Override
   public abstract void render(G3MRenderContext rc,
                               GLState glState);


   @Override
   public abstract boolean onTouchEvent(G3MEventContext ec,
                                        TouchEvent touchEvent);


   @Override
   public abstract void onResizeViewportEvent(G3MEventContext ec,
                                              int width,
                                              int height);


   @Override
   public abstract void start(G3MRenderContext rc);


   @Override
   public abstract void stop(G3MRenderContext rc);


   @Override
   public SurfaceElevationProvider getSurfaceElevationProvider() {
      return null;
   }


   @Override
   public PlanetRenderer getPlanetRenderer() {
      return null;
   }


   @Override
   public boolean isPlanetRenderer() {
      return false;
   }

}
