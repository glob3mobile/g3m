//
//  WMSBillElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#ifndef __G3MiOSSDK__WMSBillElevationDataProvider__
#define __G3MiOSSDK__WMSBillElevationDataProvider__

#include "ElevationDataProvider.hpp"
#include <stddef.h>

#include "Vector2I.hpp"

class IDownloader;

class WMSBillElevationDataProvider : public ElevationDataProvider {
private:
  IDownloader* _downloader;

public:
  WMSBillElevationDataProvider() :
  _downloader(NULL) {

  }

  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }

  void initialize(const G3MContext* context);

  const long long requestElevationData(const Sector& sector,
                                       const Vector2I& resolution,
                                       IElevationDataListener* listener,
                                       bool autodeleteListener);

  void cancelRequest(const long long requestId);
  
  std::vector<const Sector*> getSectors() const{
    int WORKING_JM;
    return std::vector<const Sector*>();
  }
  
  Vector2I getMinResolution() const{
    int WORKING_JM;
    return Vector2I(0,0);
  }
  
  ElevationData* createSubviewOfElevationData(ElevationData* elevationData,
                                              const Sector& sector,
                                              const Vector2I& resolution) const{
  //TODO
  }

};

#endif
