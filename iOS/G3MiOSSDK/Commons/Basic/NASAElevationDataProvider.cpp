//
//  NASAElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/7/15.
//
//

#include "NASAElevationDataProvider.hpp"

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
  
  return _provider->requestElevationData(sector, extent, listener, autodeleteListener);
  
}

void NASAElevationDataProvider::cancelRequest(const long long requestId){
  _provider->cancelRequest(requestId);
}

