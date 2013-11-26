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
//class OrientedBox;
//class BoundingVolume;

public class SGShape extends Shape
{
  private SGNode _node;
  private final String _uriPrefix;

  private final boolean _isTransparent;

  private GLState _glState;

  private OrientedBox _boundingVolume;

  protected final BoundingVolume getBoundingVolume(G3MRenderContext rc)
  {
    if (_boundingVolume == null)
    {
      Box boundingBox = _node.getCopyBoundingBox();
      _boundingVolume = new OrientedBox(boundingBox, getTransformMatrix(rc.getPlanet()));
      if (boundingBox != null)
         boundingBox.dispose();
    }
    return _boundingVolume;
  }



  public SGShape(SGNode node, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
     super(position, altitudeMode);
     _node = node;
     _uriPrefix = uriPrefix;
     _isTransparent = isTransparent;
     _boundingVolume = null;
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
    if (_boundingVolume != null)
      if (_boundingVolume != null)
         _boundingVolume.dispose();
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
    _node.initialize(context, this);
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return _node.isReadyToRender(rc);
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
  {
    _glState.setParent(parentState);
    _node.render(rc, _glState, renderNotReadyShapes);
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _isTransparent;
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Camera camera, Vector3D origin, Vector3D direction)
  {
    return _boundingVolume.intersectionsDistances(origin, direction);
  }


  public final boolean isVisible(G3MRenderContext rc)
  {
    return getBoundingVolume(rc).touchesFrustum(rc.getCurrentCamera().getFrustumInModelCoordinates());
  }

  public final void setSelectedDrawMode(boolean mode)
  {
  }


}