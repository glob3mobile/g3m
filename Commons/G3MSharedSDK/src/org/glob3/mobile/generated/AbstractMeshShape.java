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

  protected abstract Mesh createMesh(G3MRenderContext rc);

  protected final Mesh getMesh(G3MRenderContext rc)
  {
    if (_mesh == null)
    {
      _mesh = createMesh(rc);
    }
    return _mesh;
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
    if (mesh != null)
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