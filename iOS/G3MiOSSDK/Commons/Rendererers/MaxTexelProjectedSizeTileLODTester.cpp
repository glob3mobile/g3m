//
//  MaxTexelProjectedSizeTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/2/16.
//
//

#include "MaxTexelProjectedSizeTileLODTester.hpp"

#include "Tile.hpp"
#include "TileTessellator.hpp"
#include "PlanetRenderContext.hpp"
#include "IMathUtils.hpp"

bool MaxTexelProjectedSizeTileLODTester::meetsRenderCriteria(const G3MRenderContext* rc,
                                                             const PlanetRenderContext* prc,
                                                             const Tile* tile) const{
  
  
  TileTessellatorMeshData* meshData = ((TileTessellatorMeshData*)tile->getTessellatorData());
  if (meshData == NULL){
    return true;
  }
  const IMathUtils* mu = IMathUtils::instance();
  
  const double texelsPerTriangleLat = mu->sqrt(prc->_texHeightSquared) / meshData->_meshResLat;
  const double texelsPerTriangleLon = mu->sqrt(prc->_texWidthSquared) / meshData->_meshResLon;
  
  const double texelLatSize = meshData->_maxTriangleLatitudeLenght / texelsPerTriangleLat;
  const double texelLonSize = meshData->_maxTriangleLongitudeLenght / texelsPerTriangleLon;
  
  const double texelSize = texelLatSize > texelLonSize? texelLatSize : texelLonSize;
  
  
}

void MaxTexelProjectedSizeTileLODTester::onTileHasChangedMesh(const Tile* tile) const {
  tile->createBoundingBox();
}
