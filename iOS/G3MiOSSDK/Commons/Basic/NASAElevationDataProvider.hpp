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

class NASAElevationDataProviderListener: public IElevationDataListener{
  IElevationDataListener* _listener;
  const Sector  _requestedSector;
  
#ifdef C_CODE
  const Vector2I _requestedExtent;
#endif
#ifdef JAVA_CODE
  private Vector2I _requestedExtent;
#endif
  const bool _autoDelete;
public:
  
  NASAElevationDataProviderListener(IElevationDataListener* listener,
                                    bool autodelete,
                                    const Sector& requestedSector,
                                    const Vector2I& requestedExtent):
  _listener(listener),
  _autoDelete(autodelete),
  _requestedSector(requestedSector),
  _requestedExtent(requestedExtent){
    
  }
  
  ~NASAElevationDataProviderListener();
  
  virtual void onData(const Sector& sector,
                      const Vector2I& extent,
                      ElevationData* elevationData);
  
  virtual void onError(const Sector& sector,
                       const Vector2I& extent);
  
  virtual void onCancel(const Sector& sector,
                        const Vector2I& extent);
};

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
