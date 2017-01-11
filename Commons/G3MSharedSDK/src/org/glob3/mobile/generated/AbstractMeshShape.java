package org.glob3.mobile.generated; 
//
//  AbstractMeshShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

//
//  AbstractMeshShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//



//class Mesh;

public abstract class AbstractMeshShape extends Shape
{
  private Mesh _mesh;

  private Sphere boundingSphere = null;

  protected abstract Mesh createMesh(G3MRenderContext rc);

  protected final Mesh getMesh(G3MRenderContext rc)
  {
    if (_mesh == null)
    {
      _mesh = createMesh(rc);
    }
    return _mesh;
  }


  protected boolean touchesFrustum(G3MRenderContext rc) {
    Sphere bound = (Sphere) getBoundingSphere(rc);
    Vector3D centerInModelCoordinates = bound._center.transformedBy(
            getTransformMatrix(rc.getPlanet()), 1.0f);

    // bounding sphere in model coordinates
    Sphere bsInModelCoordinates = new Sphere(centerInModelCoordinates,
            bound._radius);

    final Frustum frustum = rc.getCurrentCamera().getFrustumInModelCoordinates();

    return bsInModelCoordinates.touchesFrustum(frustum);
  }

  private BoundingVolume getBoundingSphere(G3MRenderContext rc) {
    if (boundingSphere == null) {
      boundingSphere = getMesh(rc).getBoundingVolume().createSphere();
    }
    return boundingSphere;
  }

  protected final void cleanMesh()
  {
    if (_mesh != null)
       _mesh.dispose();
    _mesh = null;
  }

  protected AbstractMeshShape(Geodetic3D position, AltitudeMode altitudeMode)
  {
     super(position, altitudeMode);
     _mesh = null;

  }

  protected AbstractMeshShape(Geodetic3D position, AltitudeMode altitudeMode, Mesh mesh)
  {
     super(position, altitudeMode);
     _mesh = mesh;

  }


  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    final Mesh mesh = getMesh(rc);
    return (mesh != null);
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
  {
    Mesh mesh = getMesh(rc);
    if (mesh != null && touchesFrustum(rc))
    {
      mesh.render(rc, parentState);
    }
  }

  public void dispose()
  {
    if (_mesh != null)
       _mesh.dispose();
  
    super.dispose();
  
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    final Mesh mesh = getMesh(rc);
    if (mesh == null)
    {
      return false;
    }
    return mesh.isTransparent(rc);
  }
}