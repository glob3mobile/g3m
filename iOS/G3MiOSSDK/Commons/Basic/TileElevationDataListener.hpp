//
//  TileElevationDataListener.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//

#ifndef __G3MiOSSDK__TileElevationDataListener__
#define __G3MiOSSDK__TileElevationDataListener__

#include "ElevationDataProvider.hpp"
#include "Tile.hpp"
#include "Vector2I.hpp"

class TileElevationDataListener : public IElevationDataListener {
private:
  Tile*                  _tile;
  long long              _requestID;
  const Vector2I _extent;
  ElevationDataProvider* _provider;
  bool                   _isFinished;
  bool                   _deletingWhenFinished;
  
public:
  TileElevationDataListener(Tile* tile,
                            const Vector2I& extent,
                            ElevationDataProvider* provider) :
  _tile(tile),
  _extent(extent),
  _provider(provider),
  _requestID(-1),
  _isFinished(false),
  _deletingWhenFinished(false)
  {
    
  }
  
  ~TileElevationDataListener() {}
  
  void onData(const Sector& sector,
              const Vector2I& extent,
              ElevationData* elevationData) {
    
    if (_tile != NULL){
      _tile->setElevationData(elevationData, _tile->getLevel());
    }
    
    _isFinished = true;
    
    if (_deletingWhenFinished){
      delete this;
    }
  }
  
  void onError(const Sector& sector,
               const Vector2I& extent) {
    _isFinished = true;
    if (_deletingWhenFinished){
      delete this;
    }
  }
  
  void sendRequest(){
    _requestID = _provider->requestElevationData(_tile->getSector(), _extent, this, false);
  }
  
  void cancelRequest(){
    _tile = NULL;
    if (_requestID != -1 && !_isFinished){
      _provider->cancelRequest(_requestID);
    }
    _deletingWhenFinished = true;
    if (_isFinished){
      delete this;
    }
  }
};

#endif
