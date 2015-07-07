//
//  NASAElevationDataProvider.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/7/15.
//
//

#ifndef __G3MiOSSDK__NASAElevationDataProvider__
#define __G3MiOSSDK__NASAElevationDataProvider__

#include "WMSBilElevationDataProvider.hpp"

class NASAElevationDataProvider: public ElevationDataProvider{
  
private:
  WMSBilElevationDataProvider* _provider;
public:
  
  NASAElevationDataProvider();
  
  bool isReadyToRender(const G3MRenderContext* rc){
    return _provider->isReadyToRender(rc);
  }
  
  void initialize(const G3MContext* context){
    _provider->initialize(context);
  }
  
  virtual const long long requestElevationData(const Sector& sector,
                                               const Vector2I& extent,
                                               IElevationDataListener* listener,
                                               bool autodeleteListener);
  
  virtual void cancelRequest(const long long requestId);
  
  virtual std::vector<const Sector*> getSectors() const{
    return _provider->getSectors();
  }
  
  virtual const Vector2I getMinResolution() const{
    return _provider->getMinResolution();
  }
  
};

#endif /* defined(__G3MiOSSDK__NASAElevationDataProvider__) */
