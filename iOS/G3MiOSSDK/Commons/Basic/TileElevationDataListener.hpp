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
  
public:
  TileElevationDataListener(Tile* tile,
                            const Vector2I& extent,
                            ElevationDataProvider* provider) :
  _tile(tile),
  _extent(extent),
  _provider(provider),
  _requestID(-1)
  {
    
  }
  
  ~TileElevationDataListener() {}
  
  void onData(const Sector& sector,
              const Vector2I& extent,
              ElevationData* elevationData) {
    if (_tile != NULL){
      _tile->setElevationData(elevationData, _tile->getLevel());
      _tile->onElevationDataListenerDeleted();
    }
  }
  
  void onError(const Sector& sector,
<<<<<<< HEAD
               const Vector2I& extent) {
    _isFinished = true;
    if (_deletingWhenFinished){
      delete this;
=======
               const Vector2I& resolution) {
    if (_tile != NULL){
      _tile->onElevationDataListenerDeleted();
    }
  }
  
  void onCancel(const Sector& sector,
               const Vector2I& resolution) {
    if (_tile != NULL){
      _tile->onElevationDataListenerDeleted();
>>>>>>> 7fb860e4b4f43468814fc002eedb4be0455427e2
    }
  }
  
  void sendRequest(){
<<<<<<< HEAD
    _requestID = _provider->requestElevationData(_tile->getSector(), _extent, this, false);
=======
    _requestID = _provider->requestElevationData(_tile->getSector(), _resolution, this, true);
>>>>>>> 7fb860e4b4f43468814fc002eedb4be0455427e2
  }
  
  void cancelRequest(){
    _tile = NULL;
    if (_requestID != -1){
      _provider->cancelRequest(_requestID);
    }
  }
};

#endif
