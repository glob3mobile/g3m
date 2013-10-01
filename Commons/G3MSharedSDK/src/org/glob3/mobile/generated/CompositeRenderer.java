

package org.glob3.mobile.generated;

//
//  CompositeRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CompositeRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


public class CompositeRenderer
         implements
            Renderer {
   private final java.util.ArrayList<Renderer> _renderers = new java.util.ArrayList<Renderer>();
   private int                                 _renderersSize;

   private G3MContext                          _context;

   private boolean                             _enable;

   private final java.util.ArrayList<String>   _errors    = new java.util.ArrayList<String>();


   public CompositeRenderer() {
      _context = null;
      _enable = true;
      _renderersSize = 0;
      //    _renderers = std::vector<Renderer*>();
   }


   @Override
   public void dispose() {
      super.dispose();
   }


   @Override
   public final boolean isEnable() {
      if (!_enable) {
         return false;
      }

      for (int i = 0; i < _renderersSize; i++) {
         if (_renderers.get(i).isEnable()) {
            return true;
         }
      }
      return false;
   }


   @Override
   public final void setEnable(final boolean enable) {
      _enable = enable;
   }


   @Override
   public final void initialize(final G3MContext context) {
      _context = context;

      for (int i = 0; i < _renderersSize; i++) {
         _renderers.get(i).initialize(context);
      }
   }


   @Override
   public final RenderState getRenderState(final G3MRenderContext rc) {
      _errors.clear();
      boolean busyFlag = false;
      boolean errorFlag = false;

      for (int i = 0; i < _renderersSize; i++) {
         final Renderer child = _renderers.get(i);
         if (child.isEnable()) {
            final RenderState childRenderState = child.getRenderState(rc);

            final RenderState_Type childRenderStateType = childRenderState._type;

            if (childRenderStateType == RenderState_Type.RENDER_ERROR) {
               errorFlag = true;

               final java.util.ArrayList<String> childErrors = childRenderState.getErrors();
               _errors.addAll(childErrors);
            }
            else if (childRenderStateType == RenderState_Type.RENDER_BUSY) {
               busyFlag = true;
            }
         }
      }

      if (errorFlag) {
         return RenderState.error(_errors);
      }
      else if (busyFlag) {
         return RenderState.busy();
      }
      else {
         return RenderState.ready();
      }
   }


   @Override
   public final void render(final G3MRenderContext rc,
                            final GLState glState) {
      //rc->getLogger()->logInfo("CompositeRenderer::render()");

      for (int i = 0; i < _renderersSize; i++) {
         final Renderer renderer = _renderers.get(i);
         if (renderer.isEnable()) {
            renderer.render(rc, glState);
         }
      }
   }


   @Override
   public final boolean onTouchEvent(final G3MEventContext ec,
                                     final TouchEvent touchEvent) {
      // the events are processed bottom to top
      for (int i = _renderersSize - 1; i >= 0; i--) {
         final Renderer renderer = _renderers.get(i);
         if (renderer.isEnable()) {
            if (renderer.onTouchEvent(ec, touchEvent)) {
               return true;
            }
         }
      }
      return false;
   }


   @Override
   public final void onResizeViewportEvent(final G3MEventContext ec,
                                           final int width,
                                           final int height) {
      // the events are processed bottom to top
      for (int i = _renderersSize - 1; i >= 0; i--) {
         _renderers.get(i).onResizeViewportEvent(ec, width, height);
      }
   }


   public final void addRenderer(final Renderer renderer) {
      _renderers.add(renderer);
      _renderersSize = _renderers.size();

      if (_context != null) {
         renderer.initialize(_context);
      }
   }


   @Override
   public final void start(final G3MRenderContext rc) {
      for (int i = 0; i < _renderersSize; i++) {
         _renderers.get(i).start(rc);
      }
   }


   @Override
   public final void stop(final G3MRenderContext rc) {
      for (int i = 0; i < _renderersSize; i++) {
         _renderers.get(i).stop(rc);
      }
   }


   @Override
   public final void onResume(final G3MContext context) {
      for (int i = 0; i < _renderersSize; i++) {
         _renderers.get(i).onResume(context);
      }
   }


   @Override
   public final void onPause(final G3MContext context) {
      for (int i = 0; i < _renderersSize; i++) {
         _renderers.get(i).onPause(context);
      }
   }


   @Override
   public final void onDestroy(final G3MContext context) {
      for (int i = 0; i < _renderersSize; i++) {
         _renderers.get(i).onDestroy(context);
      }
   }


   @Override
   public final SurfaceElevationProvider getSurfaceElevationProvider() {
      SurfaceElevationProvider result = null;

      for (int i = 0; i < _renderersSize; i++) {
         final Renderer renderer = _renderers.get(i);
         final SurfaceElevationProvider childSurfaceElevationProvider = renderer.getSurfaceElevationProvider();
         if (childSurfaceElevationProvider != null) {
            if (result == null) {
               result = childSurfaceElevationProvider;
            }
            else {
               ILogger.instance().logError("Inconsistency in Renderers: more than one SurfaceElevationProvider");
            }
         }
      }

      return result;
   }


   @Override
   public final PlanetRenderer getPlanetRenderer() {
      PlanetRenderer result = null;

      for (int i = 0; i < _renderersSize; i++) {
         final Renderer renderer = _renderers.get(i);
         final PlanetRenderer planetRenderer = renderer.getPlanetRenderer();
         if (planetRenderer != null) {
            if (result == null) {
               result = planetRenderer;
            }
            else {
               ILogger.instance().logError("Inconsistency in Renderers: more than one PlanetRenderer");
            }
         }
      }

      return result;
   }


   @Override
   public boolean isPlanetRenderer() {
      return false;
   }

}
