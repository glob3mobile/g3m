package org.glob3.mobile.generated; 
//
//  CityGMLBuildingTessellator.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 13/4/16.
//
//

//
//  CityGMLBuildingTessellator.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 13/4/16.
//
//



public class DefaultCityGMLBuildingTessellatorData extends CityGMLBuildingTessellatorData
{
  public Mesh _containerMesh;
  public short _firstVertexIndexWithinContainerMesh;
  public short _lastVertexIndexWithinContainerMesh;
  public final Sphere _bSphere;

  public DefaultCityGMLBuildingTessellatorData(Mesh containerMesh, short firstVertexIndexWithinContainerMesh, short lastVertexIndexWithinContainerMesh, Sphere bSphere)
  {
     _containerMesh = containerMesh;
     _firstVertexIndexWithinContainerMesh = firstVertexIndexWithinContainerMesh;
     _lastVertexIndexWithinContainerMesh = lastVertexIndexWithinContainerMesh;
     _bSphere = bSphere;

    if (_firstVertexIndexWithinContainerMesh < 0 || _lastVertexIndexWithinContainerMesh < 0 || _lastVertexIndexWithinContainerMesh < _firstVertexIndexWithinContainerMesh)
    {
      throw new RuntimeException("DefaultCityGMLBuildingTessellatorData constructor. Wrong parameters.");
    }

  }

  public void dispose()
  {
    if (_bSphere != null)
       _bSphere.dispose();
  }

}