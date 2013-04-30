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



  class CompositeElevationDataProvider_Request: public IElevationDataListener{

    ElevationDataProvider* _currentRequestEDP;
    long long _currentRequestID;
    CompositeElevationDataProvider* const _compProvider;

    bool _hasBeenCanceled;

  public:

    CompositeElevationData* _compData;
    IElevationDataListener * _listener;
    const bool _autodelete;
    const Vector2I _resolution;
    const Sector& _sector;

    std::vector<ElevationDataProvider*> _providers;

    ElevationDataProvider* popBestProvider(std::vector<ElevationDataProvider*>& ps,
                                           const Vector2I& extent) const;

    CompositeElevationDataProvider_Request(CompositeElevationDataProvider* provider,
                                           const Sector& sector,
                                           const Vector2I &resolution,
                                           IElevationDataListener *listener,
                                           bool autodelete);

    bool launchNewRequest();

    void onData(const Sector& sector,
                const Vector2I& extent,
                ElevationData* elevationData);

    void cancel();

    void onError(const Sector& sector,
                 const Vector2I& extent);

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
