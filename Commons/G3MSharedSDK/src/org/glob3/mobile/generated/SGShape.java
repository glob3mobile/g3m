package org.glob3.mobile.generated;
//
//  SGShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//




//class SGNode;

public class SGShape extends Shape
{
  private SGNode _node;
  private final String _uriPrefix;

  private final boolean _isTransparent;

  private GLState _glState;
  private Sphere _boundingSphere;
  private boolean _renderBounds;

  public SGShape(SGNode node, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
    super(position, altitudeMode);
    _node = node;
    _uriPrefix = uriPrefix;
    _isTransparent = isTransparent;
    _renderBounds = false;
    _glState = new GLState();
    if (_isTransparent)
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
    if (_node != null)
       _node.dispose();
    if (_boundingSphere != null)
       _boundingSphere.dispose();
  }

  public final SGNode getNode()
  {
    return _node;
  }

  public final String getURIPrefix()
  {
    return _uriPrefix;
  }

  public final void initialize(G3MContext context)
  {
    _node.initialize(context, getURIPrefix());
  }

  public final void setRenderBounds(boolean newState) {
    _renderBounds = newState;
  }

  public final boolean isRenderBounds() {
    return _renderBounds;
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return _node.isReadyToRender(rc);
  }

  protected boolean touchesFrustum(G3MRenderContext rc) {
    Sphere bound = (Sphere) getBoundingSphere();
    Vector3D centerInModelCoordinates = bound._center.transformedBy(
                getTransformMatrix(rc.getPlanet()), 1.0f);

    // get the largest scale on any of the scale axis
    double radiusScale = 0.0;
    final Vector3D scale = getScale();
    if (scale._x > radiusScale) {
      radiusScale = scale._x;
    }
    if (scale._y > radiusScale) {
      radiusScale = scale._y;
    }
    if (scale._z > radiusScale) {
      radiusScale = scale._z;
    }
    if (radiusScale <= 0.0) {
      radiusScale = 1.0;
    }

    // bounding sphere in model coordinates
    Sphere bsInModelCoordinates = new Sphere(centerInModelCoordinates,
                                                 bound._radius * radiusScale);

    final Frustum frustum = rc.getCurrentCamera().getFrustumInModelCoordinates();

    return bsInModelCoordinates.touchesFrustum(frustum);
  }

  private BoundingVolume getBoundingSphere() {
    if (_boundingSphere == null) {
        _boundingSphere = _node.getBoundingVolume().createSphere();
    }
    return _boundingSphere;
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
  {
    if (_node != null && touchesFrustum(rc)) {
      _glState.setParent(parentState);
      _node.render(rc, _glState, renderNotReadyShapes);
      if (_renderBounds && _boundingSphere != null) {
        _boundingSphere.render(rc, parentState, Color.magenta());
      }
    }
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _isTransparent;
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
    return intersections;
  }
}
