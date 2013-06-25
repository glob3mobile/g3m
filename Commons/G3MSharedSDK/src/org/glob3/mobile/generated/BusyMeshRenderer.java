package org.glob3.mobile.generated; 
//
//  BusyMeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//
//  BusyMeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//






//***************************************************************


public class BusyMeshRenderer extends LeafRenderer
{
  private Mesh _mesh;
  private double _degrees;
  private Color _backgroundColor;

  private MutableMatrix44D _projectionMatrix = new MutableMatrix44D();
  private MutableMatrix44D _modelviewMatrix = new MutableMatrix44D();

  private GLState _glState = new GLState();

  private void createGLState()
  {
  
    GLGlobalState globalState = _glState.getGLGlobalState();
  
    globalState.enableBlend();
    globalState.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
    globalState.setClearColor(_backgroundColor);
  
    GPUProgramState progState = _glState.getGPUProgramState();
  
    progState.setUniformValue(GPUVariable.EnableTexture, false);
    progState.setUniformValue(GPUVariable.POINT_SIZE, (float)1.0);
    progState.setUniformValue(GPUVariable.SCALE_TEXTURE_COORDS, new Vector2D(1.0,1.0));
    progState.setUniformValue(GPUVariable.TRANSLATION_TEXTURE_COORDS, new Vector2D(0.0,0.0));
  
    progState.setUniformValue(GPUVariable.ColorPerVertexIntensity, (float)0.0);
    progState.setUniformValue(GPUVariable.EnableFlatColor, false);
    progState.setUniformValue(GPUVariable.FLAT_COLOR, (float)0.0, (float)0.0, (float)0.0, (float)0.0);
    progState.setUniformValue(GPUVariable.FlatColorIntensity, (float)0.0);
  
    progState.setAttributeEnabled(GPUVariable.TEXTURE_COORDS, false);
    progState.setAttributeEnabled(GPUVariable.COLOR, false);
  
    //Modelview and projection
    _modelviewMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, -1));
    _glState.getGPUProgramState().setUniformMatrixValue(GPUVariable.MODELVIEW, _projectionMatrix.multiply(_modelviewMatrix), false);
  
  }

  public BusyMeshRenderer(Color backgroundColor)
  {
     _degrees = 0;
     _backgroundColor = backgroundColor;
    _modelviewMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, -1));
    _projectionMatrix = MutableMatrix44D.invalid();
  }

  public final void initialize(G3MContext context)
  {
    int numStrides = 60;
  
    FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    int indicesCounter = 0;
    final float r1 = 12F;
    final float r2 = 18F;
    for (int step = 0; step<=numStrides; step++)
    {
      final double angle = (double) step * 2 * IMathUtils.instance().pi() / numStrides;
      final double c = IMathUtils.instance().cos(angle);
      final double s = IMathUtils.instance().sin(angle);
  
      vertices.add((r1 * c), (r1 * s), 0);
      vertices.add((r2 * c), (r2 * s), 0);
  
      indices.add((short)(indicesCounter++));
      indices.add((short)(indicesCounter++));
  
      float col = (float)(1.1 * step / numStrides);
      if (col>1)
      {
        colors.add(255, 255, 255, 0);
        colors.add(255, 255, 255, 0);
      }
      else
      {
        colors.add(255, 255, 255, 1 - col);
        colors.add(255, 255, 255, 1 - col);
      }
    }
  
    // the two last indices
    indices.add((short) 0);
    indices.add((short) 1);
  
    // create mesh
    _mesh = new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, null, colors.create());
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void render(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
  
    createGLState();
  
    if (!_projectionMatrix.isValid())
    {
      // init modelview matrix
      int[] currentViewport = new int[4];
      gl.getViewport(currentViewport);
      final int halfWidth = currentViewport[2] / 2;
      final int halfHeight = currentViewport[3] / 2;
      _projectionMatrix = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
  
      _glState.getGPUProgramState().setUniformMatrixValue(GPUVariable.MODELVIEW, _projectionMatrix.multiply(_modelviewMatrix), false);
    }
  
    _glState.getGLGlobalState().setClearColor(_backgroundColor);
    gl.clearScreen(_glState.getGLGlobalState());
  
    _mesh.render(rc, _glState);
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    final int halfWidth = width / 2;
    final int halfHeight = height / 2;
    _projectionMatrix = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);

    _glState.getGPUProgramState().setUniformMatrixValue(GPUVariable.MODELVIEW, _projectionMatrix.multiply(_modelviewMatrix), false);
  }

  public void dispose()
  {
    if (_mesh != null)
       _mesh.dispose();
    if (_backgroundColor != null)
       _backgroundColor.dispose();
  }

  public final void incDegrees(double value)
  {
    _degrees += value;
    if (_degrees>360)
       _degrees -= 360;
    _modelviewMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, -1));

    _glState.getGPUProgramState().setUniformMatrixValue(GPUVariable.MODELVIEW, _projectionMatrix.multiply(_modelviewMatrix), false);
  }

  public final void start(G3MRenderContext rc)
  {
    Effect effect = new BusyMeshEffect(this);
    rc.getEffectsScheduler().startEffect(effect, this);
  }

  public final void stop(G3MRenderContext rc)
  {
    rc.getEffectsScheduler().cancelAllEffectsFor(this);
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