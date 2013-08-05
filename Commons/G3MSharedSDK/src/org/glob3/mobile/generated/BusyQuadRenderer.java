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
public class BusyQuadRenderer extends LeafRenderer implements EffectTarget
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
    //TEXTURED
    IGLTextureId texId = null;
  //  IImage* image = rc->getFactory()->createImageFromFileName(_textureFilename);
  
    texId = rc.getTexturesHandler().getGLTextureId(_image, GLFormat.rgba(), "BusyQuadRenderer-Texture", false);
  
    rc.getFactory().deleteImage(_image);
    _image = null;
  
    if (texId == null)
    {
      rc.getLogger().logError("Can't upload texture to GPU");
      return false;
    }
  
    final double halfWidth = _size._x / 2;
    final double hadfHeight = _size._y / 2;
    FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
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
  
    TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, false);
  
    _quadMesh = new TexturedMesh(im, true, texMap, true, true);
  
    return true;
  }


  public BusyQuadRenderer(IImage image, Color backgroundColor, Vector2D size, boolean animated)
  {
     _degrees = 0;
     _quadMesh = null;
     _image = image;
     _backgroundColor = backgroundColor;
     _animated = animated;
     _size = new Vector2D(size);
  }

  public final void initialize(G3MContext context)
  {
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
    GL gl = rc.getGL();
  
    GLState state = new GLState(parentState);
    state.enableBlend();
  
    if (_quadMesh == null)
    {
      if (!initMesh(rc))
      {
        return;
      }
    }
  
    // init modelview matrix
    int[] currentViewport = new int[4];
    gl.getViewport(currentViewport);
    final int halfWidth = currentViewport[2] / 2;
    final int halfHeight = currentViewport[3] / 2;
    MutableMatrix44D M = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
    gl.setProjection(M);
    gl.loadMatrixf(MutableMatrix44D.identity());
  
    // clear screen
    gl.clearScreen(_backgroundColor.getRed(), _backgroundColor.getGreen(), _backgroundColor.getBlue(), _backgroundColor.getAlpha());
  
    gl.setState(state);
  
    gl.setBlendFuncSrcAlpha();
  
    gl.pushMatrix();
    MutableMatrix44D R2 = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, 1));
    gl.multMatrixf(R2);
  
    // draw mesh
    _quadMesh.render(rc, parentState);
  
    gl.popMatrix();
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public void dispose()
  {
    if (_quadMesh != null)
       _quadMesh.dispose();
    if (_backgroundColor != null)
       _backgroundColor.dispose();
  }

  public final void incDegrees(double value)
  {
    _degrees += value;
    if (_degrees>360)
       _degrees -= 360;
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