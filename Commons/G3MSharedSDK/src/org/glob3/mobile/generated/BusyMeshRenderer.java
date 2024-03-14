package org.glob3.mobile.generated;
//
//  BusyMeshRenderer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 20/07/12.
//



//
//  BusyMeshRenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 20/07/12.
//




public class BusyMeshRenderer implements ProtoRenderer, EffectTarget
{
  private Mesh _mesh;
  private double _degrees;
  private final Color _backgroundColor ;
  private final Color _meshOuterColor ;
  private final Color _meshInnerColor ;

  private MutableMatrix44D _projectionMatrix = new MutableMatrix44D();
  private MutableMatrix44D _modelviewMatrix = new MutableMatrix44D();


  private ProjectionGLFeature _projectionFeature;
  private ModelGLFeature _modelFeature;

  private GLState _glState;

  private void createGLState()
  {
    if (_projectionFeature == null)
    {
      _projectionFeature = new ProjectionGLFeature(_projectionMatrix.asMatrix44D());
      _glState.addGLFeature(_projectionFeature, false);
    }
    else
    {
      _projectionFeature.setMatrix(_projectionMatrix.asMatrix44D());
    }
  
    if (_modelFeature == null)
    {
      _modelFeature = new ModelGLFeature(_modelviewMatrix.asMatrix44D());
      _glState.addGLFeature(_modelFeature, false);
    }
    else
    {
      _modelFeature.setMatrix(_modelviewMatrix.asMatrix44D());
    }
  }

  private Mesh createMesh(G3MRenderContext rc)
  {
    final int numStrides = 5;
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    int indicesCounter = 0;
  
    final float innerRadius = 0F;
  
    //  const float r2=50;
    final Camera camera = rc.getCurrentCamera();
    int viewPortWidth = camera.getViewPortWidth();
    if (rc.getViewMode() == ViewMode.STEREO)
    {
      viewPortWidth /= 2;
    }
    final int viewPortHeight = camera.getViewPortHeight();
    final int minSize = (viewPortWidth < viewPortHeight) ? viewPortWidth : viewPortHeight;
    final float outerRadius = minSize / 15.0f;
  
    for (int step = 0; step <= numStrides; step++)
    {
      final double angle = (double) step * 2 * DefineConstants.PI / numStrides;
      final double c = Math.cos(angle);
      final double s = Math.sin(angle);
  
      vertices.add((innerRadius * c), (innerRadius * s), 0);
      vertices.add((outerRadius * c), (outerRadius * s), 0);
  
      indices.add((short)(indicesCounter++));
      indices.add((short)(indicesCounter++));
  
      //    float col = (float) (1.0 * step / numStrides);
      //    if (col>1) {
      //      colors.add(1, 1, 1, 0);
      //      colors.add(1, 1, 1, 0);
      //    }
      //    else {
      //      colors.add(1, 1, 1, 1 - col);
      //      colors.add(1, 1, 1, 1 - col);
      //    }
  
      //    colors.add(Color::red().wheelStep(numStrides, step));
      //    colors.add(Color::red().wheelStep(numStrides, step));
  
      colors.add(_meshInnerColor);
      colors.add(_meshOuterColor);
    }
  
    // the two last indices
    indices.add((short) 0);
    indices.add((short) 1);
  
  
    Mesh result = new IndexedMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), vertices.create(), true, indices.create(), true, 1, 1, null, colors.create());
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }
  private Mesh getMesh(G3MRenderContext rc)
  {
    if (_mesh == null)
    {
      _mesh = createMesh(rc);
    }
    return _mesh;
  }


  public BusyMeshRenderer(Color backgroundColor, Color meshOuterColor, Color meshInnerColor)
  {
     _degrees = 0;
     _backgroundColor = backgroundColor;
     _meshOuterColor = meshOuterColor;
     _meshInnerColor = meshInnerColor;
     _projectionFeature = null;
     _modelFeature = null;
     _glState = new GLState();
     _mesh = null;

  }

  public final void initialize(G3MContext context)
  {

  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    createGLState();
  
    rc.getGL().clearScreen(_backgroundColor);
  
    Mesh mesh = getMesh(rc);
    if (mesh != null)
    {
      mesh.render(rc, _glState);
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    int logicWidth = width;
    if (ec.getViewMode() == ViewMode.STEREO)
    {
      logicWidth /= 2;
    }
    final int halfWidth = logicWidth / 2;
    final int halfHeight = height / 2;
    _projectionMatrix.copyValue(MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth));
  
    if (_mesh != null)
       _mesh.dispose();
    _mesh = null;
  }

  public void dispose()
  {
    if (_mesh != null)
       _mesh.dispose();

    _glState._release();
  }

  public final void incDegrees(double value)
  {
    _degrees += value;
    if (_degrees>360)
    {
      _degrees -= 360;
    }
    _modelviewMatrix.copyValue(MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, -1)));
  }

  public final void start(G3MRenderContext rc)
  {
    Effect effect = new BusyMeshEffect(this);
    rc.getEffectsScheduler().startEffect(effect, this);
  }

  public final void stop(G3MRenderContext rc)
  {
    rc.getEffectsScheduler().cancelAllEffectsFor(this);
  
    if (_mesh != null)
       _mesh.dispose();
    _mesh = null;
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