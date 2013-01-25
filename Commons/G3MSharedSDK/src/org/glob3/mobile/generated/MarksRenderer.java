

package org.glob3.mobile.generated;

//
//  MarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  MarksRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mark;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MarkTouchListener;

public class MarksRenderer
         extends
            LeafRenderer {
   private final boolean                   _readyWhenMarksReady;
   private final java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();

   private G3MContext                      _context;
   private Camera                          _lastCamera;

   private MarkTouchListener               _markTouchListener;
   private boolean                         _autoDeleteMarkTouchListener;


   public MarksRenderer(final boolean readyWhenMarksReady) {
      _readyWhenMarksReady = readyWhenMarksReady;
      _context = null;
      _lastCamera = null;
      _markTouchListener = null;
      _autoDeleteMarkTouchListener = false;
   }


   public final void setMarkTouchListener(final MarkTouchListener markTouchListener,
                                          final boolean autoDelete) {
      if (_autoDeleteMarkTouchListener) {
         if (_markTouchListener != null) {
            _markTouchListener.dispose();
         }
      }

      _markTouchListener = markTouchListener;
      _autoDeleteMarkTouchListener = autoDelete;
   }


   @Override
   public void dispose() {
      final int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++) {
         if (_marks.get(i) != null) {
            _marks.get(i).dispose();
         }
      }

      if (_autoDeleteMarkTouchListener) {
         if (_markTouchListener != null) {
            _markTouchListener.dispose();
         }
      }
      _markTouchListener = null;
   }


   @Override
   public void initialize(final G3MContext context) {
      _context = context;

      final int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++) {
         final Mark mark = _marks.get(i);
         mark.initialize(context);
      }
   }

   //C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
   private final Vector2D render_textureTranslation = new Vector2D(0.0, 0.0);
   //C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
   private final Vector2D render_textureScale       = new Vector2D(1.0, 1.0);


   @Override
   public void render(final G3MRenderContext rc,
                      final GLState parentState) {
      //  rc.getLogger()->logInfo("MarksRenderer::render()");

      // Saving camera for use in onTouchEvent
      _lastCamera = rc.getCurrentCamera();

      final GL gl = rc.getGL();

      final GLState state = new GLState(parentState);
      state.disableDepthTest();
      state.enableBlend();
      state.enableTextures();
      state.enableTexture2D();
      state.enableVerticesPosition();
      gl.setState(state);

      //C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
      //	static Vector2D textureTranslation(0.0, 0.0);
      //C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
      //	static Vector2D textureScale(1.0, 1.0);
      gl.transformTexCoords(render_textureScale, render_textureTranslation);

      gl.setBlendFuncSrcAlpha();

      final Camera camera = rc.getCurrentCamera();

      gl.startBillBoardDrawing(camera.getWidth(), camera.getHeight());

      final int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++) {
         final Mark mark = _marks.get(i);
         //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());

         if (mark.isReady()) {
            mark.render(rc);
         }
      }

      gl.stopBillBoardDrawing();
   }


   public final void addMark(final Mark mark) {
      _marks.add(mark);
      if (_context != null) {
         mark.initialize(_context);
      }
   }


   public final void removeMark(final Mark mark) {
      for (int i = 0; i < _marks.size(); i++) {
         if (_marks.get(i) == mark) {
            _marks.remove(i);
         }
         break;
      }
   }


   public final void removeAllMarks() {
      for (int i = 0; i < _marks.size(); i++) {
         if (_marks.get(i) != null) {
            _marks.get(i).dispose();
         }
      }
      _marks.clear();
   }


   @Override
   public final boolean onTouchEvent(final G3MEventContext ec,
                                     final TouchEvent touchEvent) {

      boolean handled = false;

      if ((touchEvent.getType() == TouchEventType.Down) && (touchEvent.getTouchCount() == 1)) {

         if (_lastCamera != null) {
            final Vector2I touchedPixel = touchEvent.getTouch(0).getPos();
            final Planet planet = ec.getPlanet();

            double minSqDistance = IMathUtils.instance().maxDouble();
            Mark nearestMark = null;

            final int marksSize = _marks.size();
            for (int i = 0; i < marksSize; i++) {
               final Mark mark = _marks.get(i);

               if (!mark.isReady()) {
                  continue;
               }
               if (!mark.isRendered()) {
                  continue;
               }

               final int textureWidth = mark.getTextureWidth();
               if (textureWidth <= 0) {
                  continue;
               }

               final int textureHeight = mark.getTextureHeight();
               if (textureHeight <= 0) {
                  continue;
               }

               final Vector3D cartesianMarkPosition = mark.getCartesianPosition(planet);
               final Vector2I markPixel = _lastCamera.point2Pixel(cartesianMarkPosition);

               final RectangleI markPixelBounds = new RectangleI(markPixel._x - (textureWidth / 2), markPixel._y
                                                                                                    - (textureHeight / 2),
                        textureWidth, textureHeight);

               if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y)) {
                  final double distance = markPixel.sub(touchedPixel).squaredLength();
                  if (distance < minSqDistance) {
                     nearestMark = mark;
                     minSqDistance = distance;
                  }
               }
            }

            if (nearestMark != null) {
               handled = nearestMark.touched();
               if (!handled) {
                  if (_markTouchListener != null) {
                     handled = _markTouchListener.touchedMark(nearestMark);
                  }
               }
            }
         }
      }

      return handled;
   }


   @Override
   public final void onResizeViewportEvent(final G3MEventContext ec,
                                           final int width,
                                           final int height) {
   }


   @Override
   public final boolean isReadyToRender(final G3MRenderContext rc) {
      if (_readyWhenMarksReady) {
         final int marksSize = _marks.size();
         for (int i = 0; i < marksSize; i++) {
            if (!_marks.get(i).isReady()) {
               return false;
            }
         }
      }

      return true;
   }


   @Override
   public final void start() {
   }


   @Override
   public final void stop() {
   }


   @Override
   public final void onResume(final G3MContext context) {
      _context = context;
   }


   @Override
   public final void onPause(final G3MContext context) {
   }


   @Override
   public final void onDestroy(final G3MContext context) {
   }

}
