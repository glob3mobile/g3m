//
//  CompositeElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/04/13.
//
//

#ifndef __G3MiOSSDK__CompositeElevationDataProvider__
#define __G3MiOSSDK__CompositeElevationDataProvider__

#include <vector>
#include <map>

#include "ElevationDataProvider.hpp"
#include "Sector.hpp"

#include "ElevationData.hpp"

class CompositeElevationData;

class CompositeElevationDataProvider: public ElevationDataProvider {
private:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

  std::vector<ElevationDataProvider*> _providers;

  std::vector<ElevationDataProvider*> getProviders(const Sector& s) const;
  long long _currentID;

  class CompositeElevationDataProvider_Request;
  
  class CompositeElevationDataProvider_RequestStepListener: public IElevationDataListener {
    
  public:
    
    CompositeElevationDataProvider_Request* _request;
    
    CompositeElevationDataProvider_RequestStepListener(CompositeElevationDataProvider_Request* request);

    void onData(const Sector& sector,
                const Vector2I& extent,
                ElevationData* elevationData);
    
    void onError(const Sector& sector,
                 const Vector2I& extent);
    
    void onCancel(const Sector& sector,
                  const Vector2I& extent);

    ~CompositeElevationDataProvider_RequestStepListener() {
    }
    
  };
  
  
  
  class CompositeElevationDataProvider_Request {

    CompositeElevationDataProvider* const       _compProvider;
    
    ElevationDataProvider*                      _currentProvider;
    CompositeElevationDataProvider_RequestStepListener* _currentStep;
    long long                                   _currentID;
    
    ElevationData* _compData;
    IElevationDataListener* _listener;
    const bool _autodelete;
#ifdef C_CODE
    const Vector2I _resolution;
#endif
#ifdef JAVA_CODE
    private final Vector2I _resolution;
#endif
    const Sector _sector;
    
  public:
    
    std::vector<ElevationDataProvider*> _providers;

    ElevationDataProvider* popBestProvider(std::vector<ElevationDataProvider*>& ps,
                                           const Vector2I& extent) const;

    CompositeElevationDataProvider_Request(CompositeElevationDataProvider* provider,
                                           const Sector& sector,
                                           const Vector2I &resolution,
                                           IElevationDataListener *listener,
                                           bool autodelete);

    ~CompositeElevationDataProvider_Request() {

    }
    
    bool launchNewStep();
    
    void onData(const Sector& sector,
                const Vector2I& extent,
                ElevationData* elevationData);

    void cancel();

    void onError(const Sector& sector,
                 const Vector2I& extent);

    void onCancel(const Sector& sector,
                  const Vector2I& resolution);
    
    void respondToListener() const;

  };


  std::map<long long, CompositeElevationDataProvider_Request*> _requests;

  void requestFinished(CompositeElevationDataProvider_Request* req);

public:
  CompositeElevationDataProvider():_context(NULL), _currentID(0) {}

  ~CompositeElevationDataProvider() {
    int size =  _providers.size();
    for (int i = 0; i < size; i++) {
      delete _providers[i];
    }
    
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  void addElevationDataProvider(ElevationDataProvider* edp);

  bool isReadyToRender(const G3MRenderContext* rc);

  void initialize(const G3MContext* context);

  const long long requestElevationData(const Sector& sector,
                                       const Vector2I& extent,
                                       IElevationDataListener* listener,
                                       bool autodeleteListener);

  void cancelRequest(const long long requestId);

  std::vector<const Sector*> getSectors() const;

  const Vector2I getMinResolution() const;

};

#endif
