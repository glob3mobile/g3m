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
  const G3MContext* _context;
  std::vector<ElevationDataProvider*> _providers;

  std::vector<ElevationDataProvider*> getProviders(const Sector& s) const;
  long long _currentID;
//<<<<<<< HEAD
//
//
//
//  class CompositeElevationDataProvider_Request: public IElevationDataListener{
//
//    ElevationDataProvider* _currentRequestEDP;
//    long long _currentRequestID;
//    CompositeElevationDataProvider* const _compProvider;
//
//    bool _hasBeenCanceled;
//
//  public:
//
//    CompositeElevationData* _compData;
//    IElevationDataListener * _listener;
//    const bool _autodelete;
//    const Vector2I _resolution;
//    const Sector& _sector;
//
//=======

  class CompositeElevationDataProvider_Request;
  
  class CompositeElevationDataProvider_RequestStep: public IElevationDataListener{
    
  public:
    
    CompositeElevationDataProvider_Request* _request;
    ElevationDataProvider* _provider;
    long long _id;
    Sector _sector;
    Vector2I _resolution;
    
    CompositeElevationDataProvider_RequestStep(CompositeElevationDataProvider_Request* request,
                                               ElevationDataProvider* provider,
                                               const Sector& sector,
                                               const Vector2I &resolution);
    
    void send();
    
    void cancel();
    
    void onData(const Sector& sector,
                const Vector2I& resolution,
                ElevationData* elevationData);
    
    void onError(const Sector& sector,
                 const Vector2I& resolution);
    
    void onCancel(const Sector& sector,
                  const Vector2I& resolution);
    
  };
  
  
  
  class CompositeElevationDataProvider_Request: public IElevationDataListener{

    CompositeElevationDataProvider* const _compProvider;
    CompositeElevationDataProvider_RequestStep* _currentStep;
    
    ElevationData* _compData;
    IElevationDataListener* _listener;
    const bool _autodelete;
    const Vector2I _resolution;
    const Sector _sector;
    
  public:
    
//>>>>>>> 7fb860e4b4f43468814fc002eedb4be0455427e2
    std::vector<ElevationDataProvider*> _providers;

    ElevationDataProvider* popBestProvider(std::vector<ElevationDataProvider*>& ps,
                                           const Vector2I& extent) const;

    CompositeElevationDataProvider_Request(CompositeElevationDataProvider* provider,
                                           const Sector& sector,
                                           const Vector2I &resolution,
                                           IElevationDataListener *listener,
                                           bool autodelete);
//<<<<<<< HEAD

    bool launchNewRequest();

//=======

    ~CompositeElevationDataProvider_Request(){}
    
    bool launchNewStep();
    
//>>>>>>> 7fb860e4b4f43468814fc002eedb4be0455427e2
    void onData(const Sector& sector,
                const Vector2I& extent,
                ElevationData* elevationData);

    void cancel();

    void onError(const Sector& sector,
                 const Vector2I& extent);

    void onCancel(const Sector& sector,
                  const Vector2I& resolution);
    
    void respondToListener() const;
    
//>>>>>>> 7fb860e4b4f43468814fc002eedb4be0455427e2
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
                                       const Vector2I& extent,
                                       IElevationDataListener* listener,
                                       bool autodeleteListener);

  void cancelRequest(const long long requestId);

  std::vector<const Sector*> getSectors() const;

  const Vector2I getMinResolution() const;

  //  ElevationData* createSubviewOfElevationData(ElevationData* elevationData,
  //                                              const Sector& sector,
  //                                              const Vector2I& extent) const;

};

#endif
