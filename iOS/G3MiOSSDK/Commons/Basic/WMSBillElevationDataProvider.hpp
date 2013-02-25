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

};

#endif
