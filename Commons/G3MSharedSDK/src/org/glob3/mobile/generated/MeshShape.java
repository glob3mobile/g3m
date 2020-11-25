package org.glob3.mobile.generated;
//
//  MeshShape.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

//
//  MeshShape.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//



public class MeshShape extends AbstractMeshShape
{
  protected final Mesh createMesh(G3MRenderContext rc)
  {
    return null;
  }

  public MeshShape(Geodetic3D position, AltitudeMode altitudeMode, Mesh mesh)
  {
     super(position, altitudeMode, mesh);
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction)
  {
    return new java.util.ArrayList<Double>();
  }

}