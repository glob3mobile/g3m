package org.glob3.mobile.generated; 
//
//  MeshShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

//
//  MeshShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//



public abstract class MeshShape extends AbstractMeshShape
{
  protected final Mesh createMesh(G3MRenderContext rc)
  {
    return null;
  }

  public MeshShape(Geodetic3D position, AltitudeMode altitudeMode, Mesh mesh)
  {
     super(position, altitudeMode, mesh);

  }

}