//
//  TileElevationDataListener.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//

#ifndef __G3MiOSSDK__TileElevationDataRequest__
#define __G3MiOSSDK__TileElevationDataRequest__

#include "ElevationDataProvider.hpp"
#include "Tile.hpp"
#include "Vector2I.hpp"


class TileElevationDataRequestListener : public IElevationDataListener {
public:
  TileElevationDataRequest* _request;

  TileElevationDataRequestListener(TileElevationDataRequest* request);

  void onData(const Sector& sector,
              const Vector2I& resolution,
              ElevationData* elevationData);

  void onError(const Sector& sector,
               const Vector2I& resolution);

  void onCancel(const Sector& sector,
                const Vector2I& resolution);

  ~TileElevationDataRequestListener() {
  }
  
};


class TileElevationDataRequest {
private:
  Tile*                  _tile;
  long long              _requestID;
#ifdef C_CODE
  const Vector2I         _resolution;
#endif
#ifdef JAVA_CODE
  private final Vector2I _resolution;
#endif
  ElevationDataProvider* _provider;
  
  TileElevationDataRequestListener* _listener;
  
public:
  TileElevationDataRequest(Tile* tile,
                           const Vector2I& resolution,
                           ElevationDataProvider* provider);
  
  ~TileElevationDataRequest() {
    
  }
  
  void onData(const Sector& sector,
              const Vector2I& resolution,
              ElevationData* elevationData);
  
  void onError(const Sector& sector,
               const Vector2I& resolution);
  
  void onCancel(const Sector& sector,
                const Vector2I& resolution);
  
  void sendRequest();
  
  void cancelRequest();
  
};

#endif
