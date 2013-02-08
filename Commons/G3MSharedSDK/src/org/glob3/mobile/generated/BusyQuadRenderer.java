package org.glob3.mobile.generated; 
//
//  BusyQuadRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 13/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//



//
//  BusyQuadRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 13/08/12.
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
  
    final float halfSize = 16F;
    FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
    vertices.add(-halfSize, +halfSize, 0);
    vertices.add(-halfSize, -halfSize, 0);
    vertices.add(+halfSize, +halfSize, 0);
    vertices.add(+halfSize, -halfSize, 0);
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
    indices.add((short) 0);
    indices.add((short) 1);
    indices.add((short) 2);
    indices.add((short) 3);
  
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
    texCoords.add(0, 0);
    texCoords.add(0, 1);
    texCoords.add(1, 0);
    texCoords.add(1, 1);
  
    IndexedMesh im = new IndexedMesh(GLPrimitive.triangleStrip(), true, Vector3D.zero(), vertices.create(), indices.create(), 1);
  
    TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, false);
  
    _quadMesh = new TexturedMesh(im, true, texMap, true, false);
  
    return true;
  }



  public BusyQuadRenderer(IImage image)
  {
     _degrees = 0;
     _quadMesh = null;
     _image = image;
  }

  public final void initialize(G3MContext context)
  {
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  private boolean render_firstTime = true;
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
  
  
    // init effect in the first render
//    static boolean firstTime = true;
    if (render_firstTime)
    {
      render_firstTime = false;
      Effect effect = new BusyEffect(this);
      rc.getEffectsScheduler().startEffect(effect, this);
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
    gl.clearScreen(0.0f, 0.0f, 0.0f, 1.0f);
  
    gl.setState(state);
  
    gl.setBlendFuncSrcAlpha();
  
    gl.pushMatrix();
    MutableMatrix44D R1 = MutableMatrix44D.createRotationMatrix(Angle.zero(), new Vector3D(-1, 0, 0));
    MutableMatrix44D R2 = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, 1));
    gl.multMatrixf(R1.multiply(R2));
  
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
  }

  public final void incDegrees(double value)
  {
    _degrees += value;
    if (_degrees>360)
       _degrees -= 360;
  }

  public final void start()
  {
    //int _TODO_start_effects;
  }

  public final void stop()
  {
    //int _TODO_stop_effects;
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