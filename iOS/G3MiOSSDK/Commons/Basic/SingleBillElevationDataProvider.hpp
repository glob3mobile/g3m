//
//  SingleBillElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#ifndef __G3MiOSSDK__SingleBillElevationDataProvider__
#define __G3MiOSSDK__SingleBillElevationDataProvider__

#include "ElevationDataProvider.hpp"

#include "URL.hpp"
#include "Sector.hpp"
#include <stddef.h>
class IDownloader;
class Vector2I;

class SingleBillElevationDataProvider : public ElevationDataProvider {
private:
  IDownloader* _downloader;

  ElevationData* _elevationData;
  bool _elevationDataResolved;
  const URL _bilUrl;
  const Sector _sector;
  const int _resolutionWidth;
  const int _resolutionHeight;

  
  void drainQueue() {
    int _WORKING;
  }

  const long long queueRequest(const Sector& sector,
                               const Vector2I& resolution,
                               IElevationDataListener* listener,
                               bool autodeleteListener) {
    int _WORKING;
    return -1;
  }

  void removeQueueRequest(const long long requestId) {
    int _WORKING;
  }
  

public:
  SingleBillElevationDataProvider(const URL& bilUrl,
                                  const Sector& sector,
                                  const Vector2I& resolution);
  
  bool isReadyToRender(const G3MRenderContext* rc) {
    return (_elevationDataResolved);
  }

  void initialize(const G3MContext* context);

  const long long requestElevationData(const Sector& sector,
                                       const Vector2I& resolution,
                                       IElevationDataListener* listener,
                                       bool autodeleteListener);

  void cancelRequest(const long long requestId);


  void onElevationData(ElevationData* elevationData);

};

#endif
