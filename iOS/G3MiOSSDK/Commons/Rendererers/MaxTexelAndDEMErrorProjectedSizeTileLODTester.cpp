//
//  MaxTexelAndDEMErrorProjectedSizeTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/2/16.
//
//

#include "MaxTexelAndDEMErrorProjectedSizeTileLODTester.hpp"

#include "Tile.hpp"
#include "TileTessellator.hpp"
#include "PlanetRenderContext.hpp"
#include "IMathUtils.hpp"
#include "Camera.hpp"
#include "Box.hpp"
#include "ILogger.hpp"

bool MaxTexelAndDEMErrorProjectedSizeTileLODTester::meetsRenderCriteria(const G3MRenderContext* rc,
                                                             const PlanetRenderContext* prc,
                                                             const Tile* tile) const{
  
  PvtData* data = (PvtData*) tile->getData(MaxTexelProjectedSizeTLTDataID);
  
  if (data == NULL){
    data = createData(tile);
  }
  
  if (data != NULL){
    
    const Box* box = data->_boundingBox;
    const Camera* cam = rc->getCurrentCamera();
    const Vector3D pos = cam->getCartesianPosition();
    
    if (box->contains(pos)){
      return false;
    }
    
    const TileTessellatorMeshData* meshData = tile->getTessellatorMeshData();
    if (meshData == NULL){
      return true;
    }
    const IMathUtils* mu = IMathUtils::instance();
    
    const double texelsPerTriangleLat = mu->sqrt(prc->_texHeightSquared) / meshData->_meshResLat;
    const double texelsPerTriangleLon = mu->sqrt(prc->_texWidthSquared) / meshData->_meshResLon;
    
    const double texelLatSize = meshData->_maxTriangleLatitudeLenght / texelsPerTriangleLat;
    const double texelLonSize = meshData->_maxTriangleLongitudeLenght / texelsPerTriangleLon;
    
    double texelSize = texelLatSize;
    if (texelLonSize > texelLatSize){
      texelSize = texelLonSize;
    }
    
    //Position of closest possible texel
    const Vector3D closestPossiblePointOnTile = box->closestPoint(cam->getCartesianPosition());
    
    //Distance of the view plane containing texelPos
    const double sizeTexel = cam->maxScreenSizeOf(texelSize, closestPossiblePointOnTile);
    
    if (sizeTexel >= _maxAllowedPixelsForTexel){
//      ILogger::instance()->logInfo("Insufficient Texture %s", tile->_id.c_str());
      return false;
    } else{
      
      if (meshData->_demDistanceToNextLoD <= 0){
        return true;
      }
      
      //Checking DEM
      const double demErrorScreenSize = cam->maxScreenSizeOf(meshData->_demDistanceToNextLoD, closestPossiblePointOnTile);

      bool demOK = demErrorScreenSize < _maxAllowedPixelsForDEMError;
      
//      if (!demOK){
//        ILogger::instance()->logInfo("Insufficient DEM %s", tile->_id.c_str());
//      }
      
      return demOK;
      
    }
  }
  
  return true;
}

MaxTexelAndDEMErrorProjectedSizeTileLODTester::PvtData* MaxTexelAndDEMErrorProjectedSizeTileLODTester::createData(const Tile* tile) const{
  Box* box = tile->createBoundingBox();
  if (box != NULL){
    PvtData* data = new PvtData(box);
    tile->setData(data);
    return data;
  } else{
    tile->clearDataWithID(MaxTexelProjectedSizeTLTDataID);
  }
  return NULL;
}

void MaxTexelAndDEMErrorProjectedSizeTileLODTester::onTileHasChangedMesh(const Tile* tile) const {
//  createData(tile);
  tile->clearDataWithID(MaxTexelProjectedSizeTLTDataID);
}
