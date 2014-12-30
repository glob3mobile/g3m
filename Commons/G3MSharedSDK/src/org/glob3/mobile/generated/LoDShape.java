package org.glob3.mobile.generated; 
public class LoDShape extends Shape
{



  private java.util.ArrayList<LoDLevel> _nodes = new java.util.ArrayList<LoDLevel>();

  private GLState _glState;

  private LoDLevel _lastRenderedLevel;

  private void calculateRenderableLevel(G3MRenderContext rc)
  {
  
    double bestDistanceDifference = IMathUtils.instance().maxDouble();
  
    Vector3D shapePos = rc.getPlanet().toCartesian(getPosition());
    Vector3D cameraPos = rc.getCurrentCamera().getCartesianPosition();
  
    double distSq = shapePos.squaredDistanceTo(cameraPos);
  
    System.out.printf("DIST %f\n", Math.sqrt(distSq));
  
    IMathUtils mu = IMathUtils.instance();
  
    for (int i = 0; i < _nodes.size(); i++)
    {
      LoDLevel level = _nodes.get(i);
  
      double distDiff = mu.abs(level._perfectDistanceSquared - distSq);
      if (bestDistanceDifference > distDiff)
      {
        _lastRenderedLevel = level;
        bestDistanceDifference = distDiff;
      }
    }
  }


  protected final BoundingVolume getBoundingVolume(G3MRenderContext rc)
  {
    if (_lastRenderedLevel._boundingVolume == null)
    {
      Box boundingBox = _lastRenderedLevel._node.getCopyBoundingBox();
      _lastRenderedLevel._boundingVolume = new OrientedBox(boundingBox, getTransformMatrix(rc.getPlanet()));
  
      if (boundingBox != null)
         boundingBox.dispose();
    }
    return _lastRenderedLevel._boundingVolume;
  }



  public LoDShape(java.util.ArrayList<LoDLevel> lodLevels, Geodetic3D position, AltitudeMode altitudeMode)
  {
     super(position, altitudeMode);
     _lastRenderedLevel = null;
    _glState = new GLState();

    if (lodLevels.size() < 1)
    {
      ILogger.instance().logError("LoDShape not valid");
      return;
    }

    boolean transparent = false;
    for (int i = 0; i < lodLevels.size(); i++)
    {
      _nodes.add(lodLevels.get(i));
      if (lodLevels.get(i)._isTransparent)
      {
        transparent = true;
      }
    }

    _lastRenderedLevel = lodLevels.get(0);

    if (transparent)
    {
      _glState.addGLFeature(new BlendingModeGLFeature(true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
    }
    else
    {
      _glState.addGLFeature(new BlendingModeGLFeature(false, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
    }
  }

  public void dispose()
  {
    _glState._release();
  
    for (int i = 0; i < _nodes.size(); i++)
    {
      if (_nodes.get(i) != null)
         _nodes.get(i).dispose();
    }
  }

  public final SGNode getNode()
  {

    if (_lastRenderedLevel != null)
    {
      return _lastRenderedLevel._node;
    }

    return null;
  }

  public final String getURIPrefix()
  {
    if (_lastRenderedLevel != null)
    {
      return _lastRenderedLevel._uriPrefix;
    }

    return "NOT RENDERED YET. MANY URIS";
  }

  public final void initialize(G3MContext context)
  {
    for (int i = 0; i < _nodes.size(); i++)
    {
  
      LoDLevel level = _nodes.get(i);
  
      SGShape shape = new SGShape(level._node, level._uriPrefix, level._isTransparent, new Geodetic3D(Geodetic3D.fromDegrees(0, 0, 0)), AltitudeMode.ABSOLUTE);
      shape.initialize(context);
      level._sgShape = shape;
    }
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    for (int i = 0; i < _nodes.size(); i++)
    {
      if (!_nodes.get(i)._node.isReadyToRender(rc))
      {
        return false;
      }
    }
  
    return true;
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
  {
    _glState.setParent(parentState);
  
    calculateRenderableLevel(rc);
  
    _lastRenderedLevel._node.render(rc, _glState, renderNotReadyShapes);
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {

    if (_lastRenderedLevel != null)
    {
      return _lastRenderedLevel._isTransparent;
    }

    return false;
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Camera camera, Vector3D origin, Vector3D direction)
  {
    return _lastRenderedLevel._boundingVolume.intersectionsDistances(origin, direction);
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, Vector3D direction)
  {
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
    return intersections;
  }

  public final void zRawRender(G3MRenderContext rc, GLState parentState)
  {
  
    GLState glState = new GLState();
    if (_lastRenderedLevel._isTransparent)
    {
      glState.addGLFeature(new BlendingModeGLFeature(true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
    }
    else
    {
      glState.addGLFeature(new BlendingModeGLFeature(false, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
    }
    glState.setParent(parentState);
  
    _lastRenderedLevel._node.zRender(rc, glState);
  
    glState._release();
  }


  public final boolean isVisible(G3MRenderContext rc)
  {
    return getBoundingVolume(rc).touchesFrustum(rc.getCurrentCamera().getFrustumInModelCoordinates());
  }

  public final void setSelectedDrawMode(boolean mode)
  {
  }

  public final GEORasterSymbol createRasterSymbolIfNeeded()
  {
    return null;
  }


}