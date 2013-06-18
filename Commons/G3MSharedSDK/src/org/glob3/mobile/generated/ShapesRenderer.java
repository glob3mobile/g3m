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
    _glState.getGLGlobalState().enableDepthTest();
  
    GPUProgramState progState = _glState.getGPUProgramState();
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
    _glState.getGPUProgramState().setUniformMatrixValue("Modelview", rc.getCurrentCamera().getModelMatrix(), false);
    _glState.getGPUProgramState().setUniformMatrixValue("Projection", rc.getCurrentCamera().getProjectionMatrix(), false);
  
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