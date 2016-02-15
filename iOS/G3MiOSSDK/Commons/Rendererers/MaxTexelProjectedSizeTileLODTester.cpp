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
#include "Camera.hpp"


bool MaxTexelProjectedSizeTileLODTester::meetsRenderCriteria(const G3MRenderContext* rc,
                                                             const PlanetRenderContext* prc,
                                                             const Tile* tile) const{
  
  PvtData* data = (PvtData*) tile->getData(MaxTexelProjectedSizeTLTDataID);
  if (data != NULL){
    
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
    
    //Position of closest possible texel
    const Vector3D texelPos = data->_boundingBox->closestPoint(rc->getCurrentCamera()->getCartesianPosition());
    
    //Distance of the view plane containing texelPos
    const double size = rc->getCurrentCamera()->maxScreenSizeOf(texelSize, texelPos);
    
    return size < _maxAllowedPixelsForTexel;
    
    
  }
  
  return true;
  
}

void MaxTexelProjectedSizeTileLODTester::onTileHasChangedMesh(const Tile* tile) const {
  Box* box = tile->createBoundingBox();
  if (box != NULL){
    tile->setData(new PvtData(box));
  } else{
    tile->clearDataWithID(MaxTexelProjectedSizeTLTDataID);
  }
}
