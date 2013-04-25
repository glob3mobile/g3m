//
//  CompositeElevationDataProvider.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/04/13.
//
//

#ifndef __G3MiOSSDK__CompositeElevationDataProvider__
#define __G3MiOSSDK__CompositeElevationDataProvider__

#include <iostream>
#include <vector.h>
#include <map.h>

#include "ElevationDataProvider.hpp"
#include "Sector.hpp"

#include "ElevationData.hpp"

class CompositeElevationData;

class CompositeElevationDataProvider: public ElevationDataProvider {
private:
  const G3MContext* _context;
  std::vector<ElevationDataProvider*> _providers;
  
  std::vector<ElevationDataProvider*> getProviders(const Sector& s) const;
  long long _currentID;
  
  
  
  class CompositeElevationDataProvider_Request: public IElevationDataListener{
    
    
  public:
    
    CompositeElevationData* _compData;
    IElevationDataListener * _listener;
    const bool _autodelete;
    const Vector2I _resolution;
    const Sector& _sector;
    ElevationDataProvider* _currentRequestEDP;
    long long _currentRequestID;
    CompositeElevationDataProvider* const _compProvider;
    
    std::vector<ElevationDataProvider*> _providers;
    
    ElevationDataProvider* popBestProvider(std::vector<ElevationDataProvider*>& ps, const Vector2I& resolution) const;
    
//    void respondToListener();

    
    CompositeElevationDataProvider_Request(CompositeElevationDataProvider* provider,
                                           const Sector& sector,
                                           const Vector2I &resolution,
                                           IElevationDataListener *listener,
                                           bool autodelete);
    
    bool launchNewRequest();
    
    void onData(const Sector& sector,
                const Vector2I& resolution,
                ElevationData* elevationData);
    
    void cancel();
    
    void onError(const Sector& sector,
                 const Vector2I& resolution);
    
  };
  
  
  std::map<long long, CompositeElevationDataProvider_Request*> _requests;
  
  void requestFinished(CompositeElevationDataProvider_Request* req);
  
public:
  CompositeElevationDataProvider():_context(NULL), _currentID(0){}
  
  ~CompositeElevationDataProvider() {
    int size =  _providers.size();
    for (int i = 0; i < size; i++) {
      delete _providers[i];
    }
  }
  
  void addElevationDataProvider(ElevationDataProvider* edp);
  
  bool isReadyToRender(const G3MRenderContext* rc);
  
  void initialize(const G3MContext* context);
  
  const long long requestElevationData(const Sector& sector,
                                       const Vector2I& resolution,
                                       IElevationDataListener* listener,
                                       bool autodeleteListener);
  
  void cancelRequest(const long long requestId);
  
  std::vector<const Sector*> getSectors() const;
  
  Vector2I getMinResolution() const;
  
  ElevationData* createSubviewOfElevationData(ElevationData* elevationData,
                                              const Sector& sector,
                                              const Vector2I& resolution) const;
  
};

#endif /* defined(__G3MiOSSDK__CompositeElevationDataProvider__) */
