package org.glob3.mobile.generated; 
//
//  ShapesRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

//
//  ShapesRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//



public class ShapesRenderer extends LeafRenderer
{
  private final boolean _renderNotReadyShapes;

  private java.util.ArrayList<Shape> _shapes = new java.util.ArrayList<Shape>();

  private G3MContext _context;

  private GLState _glState = new GLState();
  private void createGLState()
  {
  //  _glState.getGLGlobalState()->enableDepthTest();
  
  //  GPUProgramState& progState = *_glState.getGPUProgramState();
  //  progState.setUniformValue(GPUVariable::EnableTexture, false);
  //  progState.setUniformValue(GPUVariable::POINT_SIZE, (float)1.0);
  //  progState.setUniformValue(GPUVariable::SCALE_TEXTURE_COORDS, Vector2D(1.0,1.0));
  //  progState.setUniformValue(GPUVariable::TRANSLATION_TEXTURE_COORDS, Vector2D(0.0,0.0));
  //
  //  progState.setUniformValue(GPUVariable::ColorPerVertexIntensity, (float)0.0);
  //  progState.setUniformValue(GPUVariable::EnableFlatColor, false);
  //  progState.setUniformValue(GPUVariable::FLAT_COLOR, (float)0.0, (float)0.0, (float)0.0, (float)0.0);
  //  progState.setUniformValue(GPUVariable::FlatColorIntensity, (float)0.0);
  //
  //  progState.setAttributeEnabled(GPUVariable::TEXTURE_COORDS, false);
  //  progState.setAttributeEnabled(GPUVariable::COLOR, false);
  }


  public ShapesRenderer()
  {
     this(true);
  }
  public ShapesRenderer(boolean renderNotReadyShapes)
  {
     _renderNotReadyShapes = renderNotReadyShapes;
     _context = null;
    createGLState();
  }

  public void dispose()
  {
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      if (shape != null)
         shape.dispose();
    }
  }

  public final void addShape(Shape shape)
  {
    _shapes.add(shape);
    if (_context != null)
    {
      shape.initialize(_context);
    }
  }

  public final void removeAllShapes()
  {
     removeAllShapes(true);
  }
  public final void removeAllShapes(boolean deleteShapes)
  {
    if (deleteShapes)
    {
      final int shapesCount = _shapes.size();
      for (int i = 0; i < shapesCount; i++)
      {
        Shape shape = _shapes.get(i);
        if (shape != null)
           shape.dispose();
      }
    }
  
    _shapes.clear();
  }

  public final void onResume(G3MContext context)
  {
    _context = context;
  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

  public final void initialize(G3MContext context)
  {
    _context = context;

    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      shape.initialize(context);
    }
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    if (_renderNotReadyShapes)
    {
      return true;
    }
  
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      final boolean shapeReady = shape.isReadyToRender(rc);
      if (!shapeReady)
      {
        return false;
      }
    }
    return true;
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public final void start(G3MRenderContext rc)
  {
  }

  public final void stop(G3MRenderContext rc)
  {
  }

  public final void render(G3MRenderContext rc)
  {
    final Vector3D cameraPosition = rc.getCurrentCamera().getCartesianPosition();
  
    //Setting camera matrixes
    _glState.getGPUProgramState().setUniformMatrixValue(GPUVariable.MODELVIEW, rc.getCurrentCamera().getModelViewMatrix(), false);
  
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      if (shape.isEnable())
      {
        if (shape.isTransparent(rc))
        {
          final Planet planet = rc.getPlanet();
          final Vector3D shapePosition = planet.toCartesian(shape.getPosition());
          final double squaredDistanceFromEye = shapePosition.sub(cameraPosition).squaredLength();
  
          rc.addOrderedRenderable(new TransparentShapeWrapper(shape, squaredDistanceFromEye, _glState, _renderNotReadyShapes));
        }
        else
        {
          shape.render(rc, _glState, _renderNotReadyShapes);
        }
      }
    }
  }

}