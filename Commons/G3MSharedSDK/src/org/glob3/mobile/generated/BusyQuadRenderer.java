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


public class BusyQuadRenderer implements ProtoRenderer, EffectTarget
{
  private double _degrees;
  //  const std::string _textureFilename;
  private IImage _image;
  private Mesh _quadMesh;

  private final boolean _animated;
  private final Vector2D _size ;
  private Color _backgroundColor;

  private boolean initMesh(G3MRenderContext rc)
  {
    TextureIDReference texId = null;
  
    texId = rc.getTexturesHandler().getTextureIDReference(_image, GLFormat.rgba(), "BusyQuadRenderer-Texture", false);
  
    if (texId == null)
    {
      rc.getLogger().logError("Can't upload texture to GPU");
      return false;
    }
  
    final double halfWidth = _size._x / 2;
    final double hadfHeight = _size._y / 2;
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
    vertices.add(-halfWidth, +hadfHeight, 0);
    vertices.add(-halfWidth, -hadfHeight, 0);
    vertices.add(+halfWidth, +hadfHeight, 0);
    vertices.add(+halfWidth, -hadfHeight, 0);
  
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
    texCoords.add(0, 0);
    texCoords.add(0, 1);
    texCoords.add(1, 0);
    texCoords.add(1, 1);
  
    DirectMesh im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1);
  
    if (vertices != null)
       vertices.dispose();
  
    TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, false);
  
    _quadMesh = new TexturedMesh(im, true, texMap, true, true);
  
    return true;
  }

  private MutableMatrix44D _modelviewMatrix = new MutableMatrix44D();
  private MutableMatrix44D _projectionMatrix = new MutableMatrix44D();

  private GLState _glState;
  private void createGLState()
  {
    //Modelview and projection
    _modelviewMatrix.copyValue(MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, 1)));
    _glState.clearGLFeatureGroup(GLFeatureGroupName.CAMERA_GROUP);
    _glState.addGLFeature(new ProjectionGLFeature(_projectionMatrix.asMatrix44D()), false);
    _glState.addGLFeature(new ModelGLFeature(_modelviewMatrix.asMatrix44D()), false);
  }


  public BusyQuadRenderer(IImage image, Color backgroundColor, Vector2D size, boolean animated)
  {
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

  public final void initialize(G3MContext context)
  {

  }


  //TODO: REMOVE???
  public final void render(G3MRenderContext rc, GLState glState)
  {
    GL gl = rc.getGL();
  
    if (_quadMesh == null)
    {
      if (!initMesh(rc))
      {
        return;
      }
    }
  
    createGLState();
  
    // clear screen
    gl.clearScreen(_backgroundColor);
  
    // draw mesh
    _quadMesh.render(rc, _glState);
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    final int halfWidth = width / 2;
    final int halfHeight = height / 2;
    _projectionMatrix.copyValue(MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth));
  }

  public void dispose()
  {
    //rc->getFactory()->deleteImage(_image);
    //_image = NULL;
    if (_image != null)
       _image.dispose();
    if (_quadMesh != null)
       _quadMesh.dispose();
    if (_backgroundColor != null)
       _backgroundColor.dispose();

    _glState._release();
  }

  public final void incDegrees(double value)
  {
    _degrees += value;
    if (_degrees>360)
       _degrees -= 360;
    _modelviewMatrix.copyValue(MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, 1)));
  }

  public final void start(G3MRenderContext rc)
  {
    if (_animated)
    {
      Effect effect = new BusyEffect(this);
      rc.getEffectsScheduler().startEffect(effect, this);
    }
  }

  public final void stop(G3MRenderContext rc)
  {
    if (_animated)
    {
      rc.getEffectsScheduler().cancelAllEffectsFor(this);
    }
  
    if (_quadMesh != null)
       _quadMesh.dispose();
    _quadMesh = null;
  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

}