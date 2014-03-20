

package org.glob3.mobile.generated;

//
//  BusyQuadRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 13/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  BusyQuadRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 13/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//***************************************************************


//C++ TO JAVA CONVERTER TODO TASK: Multiple inheritance is not available in Java:
public class BusyQuadRenderer
         implements
            ProtoRenderer,
            EffectTarget {
   private double         _degrees;
   //  const std::string _textureFilename;
   private final IImage   _image;
   private Mesh           _quadMesh;

   private final boolean  _animated;
   private final Vector2D _size;
   private final Color    _backgroundColor;


   private boolean initMesh(final G3MRenderContext rc) {
      TextureIDReference texId = null;

      texId = rc.getTexturesHandler().getTextureIDReference(_image, GLFormat.rgba(), "BusyQuadRenderer-Texture", false);

      if (texId == null) {
         rc.getLogger().logError("Can't upload texture to GPU");
         return false;
      }

      final double halfWidth = _size._x / 2;
      final double hadfHeight = _size._y / 2;
      final FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      vertices.add(-halfWidth, +hadfHeight, 0);
      vertices.add(-halfWidth, -hadfHeight, 0);
      vertices.add(+halfWidth, +hadfHeight, 0);
      vertices.add(+halfWidth, -hadfHeight, 0);

      final FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
      texCoords.add(0, 0);
      texCoords.add(0, 1);
      texCoords.add(1, 0);
      texCoords.add(1, 1);

      final DirectMesh im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1);

      if (vertices != null) {
         vertices.dispose();
      }

      final TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, false);

      _quadMesh = new TexturedMesh(im, true, texMap, true, true);

      return true;
   }

   private MutableMatrix44D _modelviewMatrix  = new MutableMatrix44D();
   private MutableMatrix44D _projectionMatrix = new MutableMatrix44D();

   private final GLState    _glState;


   private void createGLState() {

      //Modelview and projection
      _modelviewMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, 1));
      _glState.clearGLFeatureGroup(GLFeatureGroupName.CAMERA_GROUP);
      _glState.addGLFeature(new ProjectionGLFeature(_projectionMatrix.asMatrix44D()), false);
      _glState.addGLFeature(new ModelGLFeature(_modelviewMatrix.asMatrix44D()), false);
   }


   public BusyQuadRenderer(final IImage image,
                           final Color backgroundColor,
                           final Vector2D size,
                           final boolean animated) {
      _degrees = 0;
      _quadMesh = null;
      _image = image;
      _backgroundColor = backgroundColor;
      _animated = animated;
      _size = new Vector2D(size);
      _projectionMatrix = new MutableMatrix44D(MutableMatrix44D.invalid());
      _glState = new GLState();
      createGLState();
   }


   @Override
   public final void initialize(final G3MContext context) {
   }


   //TODO: REMOVE???
   @Override
   public final void render(final G3MRenderContext rc,
                            final GLState glState) {
      final GL gl = rc.getGL();

      if (_quadMesh == null) {
         if (!initMesh(rc)) {
            return;
         }
      }

      createGLState();

      // clear screen
      gl.clearScreen(_backgroundColor);

      // draw mesh
      _quadMesh.render(rc, _glState);
   }


   @Override
   public final void onResizeViewportEvent(final G3MEventContext ec,
                                           final int width,
                                           final int height) {
      final int halfWidth = width / 2;
      final int halfHeight = height / 2;
      _projectionMatrix = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight,
               -halfWidth, halfWidth);
   }


   @Override
   public void dispose() {
      //rc->getFactory()->deleteImage(_image);
      //_image = NULL;
      if (_image != null) {
         _image.dispose();
      }
      if (_quadMesh != null) {
         _quadMesh.dispose();
      }
      if (_backgroundColor != null) {
         _backgroundColor.dispose();
      }

      _glState._release();

      super.dispose();

   }


   public final void incDegrees(final double value) {
      _degrees += value;
      if (_degrees > 360) {
         _degrees -= 360;
      }
      _modelviewMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, 1));
   }


   @Override
   public final void start(final G3MRenderContext rc) {
      if (_animated) {
         final Effect effect = new BusyEffect(this);
         rc.getEffectsScheduler().startEffect(effect, this);
      }
   }


   @Override
   public final void stop(final G3MRenderContext rc) {
      if (_animated) {
         rc.getEffectsScheduler().cancelAllEffectsFor(this);
      }

      if (_quadMesh != null) {
         _quadMesh.dispose();
      }
      _quadMesh = null;
   }


   @Override
   public final void onResume(final G3MContext context) {

   }


   @Override
   public final void onPause(final G3MContext context) {

   }


   @Override
   public final void onDestroy(final G3MContext context) {

   }

}
