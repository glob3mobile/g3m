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


//C++ TO JAVA CONVERTER TODO TASK: Multiple inheritance is not available in Java:
public class BusyMeshRenderer extends LeafRenderer, SceneGraphNode
{
  private Mesh _mesh;
  private double _degrees;
  private Color _backgroundColor;

  private MutableMatrix44D _projectionMatrix = new MutableMatrix44D();
  private MutableMatrix44D _modelviewMatrix = new MutableMatrix44D();

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
  
    notifyGLClientChildrenParentHasChanged();
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void render(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
  
    // set mesh GLGlobalState
    GLGlobalState state = new GLGlobalState();
    state.enableBlend();
  
    state.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
  
    if (!_projectionMatrix.isValid())
    {
      // init modelview matrix
      int[] currentViewport = new int[4];
      gl.getViewport(currentViewport);
      final int halfWidth = currentViewport[2] / 2;
      final int halfHeight = currentViewport[3] / 2;
      _projectionMatrix = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
    }
  
    state.setClearColor(_backgroundColor);
    gl.clearScreen(state);
  
    notifyGLClientChildrenParentHasChanged();
    _mesh.render(rc);
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

  public final void notifyGLClientChildrenParentHasChanged()
  {
    _mesh.actualizeGLGlobalState(this);
  }

  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
    GLGlobalState.enableBlend();
    GLGlobalState.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
    GLGlobalState.setClearColor(_backgroundColor);
  }

  public final void modifyGPUProgramState(GPUProgramState progState)
  {
    progState.setUniformValue("EnableTexture", false);
    progState.setUniformValue("PointSize", (float)1.0);
    progState.setUniformValue("ScaleTexCoord", new Vector2D(1.0,1.0));
    progState.setUniformValue("TranslationTexCoord", new Vector2D(0.0,0.0));
  
    progState.setUniformValue("ColorPerVertexIntensity", (float)0.0);
    progState.setUniformValue("EnableFlatColor", false);
    progState.setUniformValue("FlatColor", (float)0.0, (float)0.0, (float)0.0, (float)0.0);
    progState.setUniformValue("FlatColorIntensity", (float)0.0);
  
    progState.setAttributeEnabled("TextureCoord", false);
    progState.setAttributeEnabled("Color", false);
  
    //Modelview and projection
    _modelviewMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, -1));
    progState.setUniformValue("Modelview", _modelviewMatrix); //Program state will store a pointer
  
    progState.setUniformValue("Projection", _projectionMatrix);
  }

  //Scene Graph Node
  public final void rawRender(G3MRenderContext rc, GLStateTreeNode myStateTreeNode)
  {
    GL gl = rc.getGL();
  
  //  // set mesh GLGlobalState
  //  GLGlobalState state;
  //  state.enableBlend();
  //
  //  state.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  
    if (!_projectionMatrix.isValid())
    {
      // init modelview matrix
      int[] currentViewport = new int[4];
      gl.getViewport(currentViewport);
      final int halfWidth = currentViewport[2] / 2;
      final int halfHeight = currentViewport[3] / 2;
      _projectionMatrix = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
    }
  
    GLGlobalState state = myStateTreeNode.getGLState().getGLGlobalState();
  
    gl.clearScreen(state);
  
  //  _mesh->render(rc);
  }
  public final boolean isInsideCameraFrustum(G3MRenderContext rc)
  {
    return true;
  }
  public final void modifiyGLState(GLState state)
  {
  
    GLGlobalState globalState = state.getGLGlobalState();
  
    globalState.enableBlend();
    globalState.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
    globalState.setClearColor(_backgroundColor);
  
    GPUProgramState progState = state.getGPUProgramState();
  
    progState.setUniformValue("EnableTexture", false);
    progState.setUniformValue("PointSize", (float)1.0);
    progState.setUniformValue("ScaleTexCoord", new Vector2D(1.0,1.0));
    progState.setUniformValue("TranslationTexCoord", new Vector2D(0.0,0.0));
  
    progState.setUniformValue("ColorPerVertexIntensity", (float)0.0);
    progState.setUniformValue("EnableFlatColor", false);
    progState.setUniformValue("FlatColor", (float)0.0, (float)0.0, (float)0.0, (float)0.0);
    progState.setUniformValue("FlatColorIntensity", (float)0.0);
  
    progState.setAttributeEnabled("TextureCoord", false);
    progState.setAttributeEnabled("Color", false);
  
    //Modelview and projection
    _modelviewMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, -1));
    progState.setUniformValue("Modelview", _modelviewMatrix); //Program state will store a pointer
  
    progState.setUniformValue("Projection", _projectionMatrix);
  
  }
  public final void onInitialize(G3MContext context)
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
    addChild(_mesh);
  }

}