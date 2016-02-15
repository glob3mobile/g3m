//
//  MaxTexelProjectedSizeTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/2/16.
//
//

#ifndef MaxTexelProjectedSizeTileLODTester_hpp
#define MaxTexelProjectedSizeTileLODTester_hpp

#include "TileLODTester.hpp"

#include "TileData.hpp"
#include "Vector3D.hpp"
#include "Box.hpp"

class Planet;
class Camera;


class MaxTexelProjectedSizeTileLODTester : public TileLODTester {
  
  class PvtData: public TileData {
  private:
    
  public:
#ifdef C_CODE
    const Box* const _boundingBox;
#endif
#ifdef JAVA_CODE
    public final Box _boundingBox;
#endif
    PvtData(const Box* boundingBox):
    TileData(MaxTexelProjectedSizeTLTDataID), _boundingBox(boundingBox){}
  };
  
  const double _maxAllowedPixelsForTexel;
  
  PvtData* createData(const Tile* tile) const;
  
public:
  
  MaxTexelProjectedSizeTileLODTester(double maxAllowedPixelsForTexel):
  _maxAllowedPixelsForTexel(maxAllowedPixelsForTexel){}
  
  ~MaxTexelProjectedSizeTileLODTester(){}
  
  bool meetsRenderCriteria(const G3MRenderContext* rc,
                           const PlanetRenderContext* prc,
                           const Tile* tile) const;
  
  void onTileHasChangedMesh(const Tile* tile) const;
  
  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp){}
  
  void renderStarted() const{}
  

};

#endif /* MaxTexelProjectedSizeTileLODTester_hpp */
