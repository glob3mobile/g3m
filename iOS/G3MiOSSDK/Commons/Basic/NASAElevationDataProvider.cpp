//
//  NASAElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/7/15.
//
//

#include "NASAElevationDataProvider.hpp"
#include "InterpolatedSubviewElevationData.hpp"


void NASAElevationDataProviderListener::onData(const Sector& sector,
                    const Vector2I& extent,
                    ElevationData* elevationData){
  
  ElevationData* sub = new InterpolatedSubviewElevationData(elevationData,
                                                _requestedSector,
                                                _requestedExtent);
  
  _listener->onData(sector, _requestedExtent, sub);
  sub->_release();
  
  if (_autoDelete){
    delete _listener;
  }
}

void NASAElevationDataProviderListener::onError(const Sector& sector,
                     const Vector2I& extent){
  
  _listener->onError(sector, _requestedExtent);
  if (_autoDelete){
    delete _listener;
  }
}

void NASAElevationDataProviderListener::onCancel(const Sector& sector,
                      const Vector2I& extent){
  
  _listener->onCancel(sector, _requestedExtent);
  if (_autoDelete){
    delete _listener;
  }
  
}

NASAElevationDataProvider::NASAElevationDataProvider(){
  _provider = new WMSBilElevationDataProvider(URL("http://data.worldwind.arc.nasa.gov/elev"),
                                              "srtm30",
                                              Sector::FULL_SPHERE,
                                              0);
}


const long long NASAElevationDataProvider::requestElevationData(const Sector& sector,
                                             const Vector2I& extent,
                                             IElevationDataListener* listener,
                                                                        bool autodeleteListener){
  
  NASAElevationDataProviderListener* list = new NASAElevationDataProviderListener(listener,
                                                                                  autodeleteListener,
                                                                                  sector,
                                                                                  extent);
  
  int factor = 4.0;
  
  Sector sector2 = sector.shrinkedByPercent(-factor);
  Vector2I extent2(extent._x * factor, extent._y * factor);
  
  return _provider->requestElevationData(sector2, extent2, list, true);
  
  //return _provider->requestElevationData(sector, extent, listener, autodeleteListener);
  
}

void NASAElevationDataProvider::cancelRequest(const long long requestId){
  _provider->cancelRequest(requestId);
}

