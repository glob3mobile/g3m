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
#include "Vector2I.hpp"
#include <stddef.h>
#include <map>
class Vector2I;

class SingleBillElevationDataProvider : public ElevationDataProvider {
private:
  
  struct SingleBillElevationDataProvider_Request{
    const Sector _sector;
    const Vector2I& _resolution;
    IElevationDataListener* const _listener;
    const bool _autodeleteListener;

    SingleBillElevationDataProvider_Request(const Sector& sector,
                                            const Vector2I& resolution,
                                            IElevationDataListener* listener,
                                            bool autodeleteListener):
    _sector(sector),
    _resolution(resolution),
    _listener(listener),
    _autodeleteListener(autodeleteListener)
    {
    }
  };
  
  long long _currentRequestID;
  std::map<long long, SingleBillElevationDataProvider_Request*> _requests;
  
  
  ElevationData* _elevationData;
  bool _elevationDataResolved;
#ifdef C_CODE
  const URL _bilUrl;
#endif
#ifdef JAVA_CODE
  private final URL _bilUrl;
#endif
  const Sector _sector;
  const int _resolutionWidth;
  const int _resolutionHeight;
  const double _noDataValue;
  
  const bool _useFloat;
  
  
  void drainQueue();
  
  const long long queueRequest(const Sector& sector,
                               const Vector2I& resolution,
                               IElevationDataListener* listener,
                               bool autodeleteListener);
  
  void removeQueueRequest(const long long requestId);
  
  
public:
  SingleBillElevationDataProvider(const URL& bilUrl,
                                  const Sector& sector,
                                  const Vector2I& resolution,
                                  const double noDataValue,
                                  const bool useFloat = false);
  
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
  
  std::vector<const Sector*> getSectors() const{
    std::vector<const Sector*> sectors;
    sectors.push_back(&_sector);
    return sectors;
  }
  
  Vector2I getMinResolution() const{
    return Vector2I(_resolutionWidth,_resolutionHeight);
  }
  
};

#endif
