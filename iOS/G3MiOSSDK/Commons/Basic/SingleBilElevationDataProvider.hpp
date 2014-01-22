//
//  SingleBilElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#ifndef __G3MiOSSDK__SingleBilElevationDataProvider__
#define __G3MiOSSDK__SingleBilElevationDataProvider__

#include "ElevationDataProvider.hpp"

#include "URL.hpp"
#include "Sector.hpp"
#include "Vector2I.hpp"
#include <stddef.h>
#include <map>
#include "Sector.hpp"

class SingleBilElevationDataProvider_BufferDownloadListener;


struct SingleBilElevationDataProvider_Request {
  const Sector _sector;
#ifdef C_CODE
  const Vector2I _extent;
#endif
#ifdef JAVA_CODE
  public final Vector2I _extent;
#endif
  IElevationDataListener* const _listener;
  const bool _autodeleteListener;

  SingleBilElevationDataProvider_Request(const Sector& sector,
                                          const Vector2I& extent,
                                          IElevationDataListener* listener,
                                          bool autodeleteListener):
  _sector(sector),
  _extent(extent),
  _listener(listener),
  _autodeleteListener(autodeleteListener)
  {
  }

  ~SingleBilElevationDataProvider_Request() {
  }
  
};

class SingleBilElevationDataProvider : public ElevationDataProvider {
private:


  long long _currentRequestID;
  std::map<long long, SingleBilElevationDataProvider_Request*> _requestsQueue;


  ElevationData* _elevationData;
  bool _elevationDataResolved;
#ifdef C_CODE
  const URL _bilUrl;
#endif
#ifdef JAVA_CODE
  private final URL _bilUrl;
#endif
  const Sector _sector;
  const int _extentWidth;
  const int _extentHeight;

  const double _deltaHeight;

  void drainQueue();

  const long long queueRequest(const Sector& sector,
                               const Vector2I& extent,
                               IElevationDataListener* listener,
                               bool autodeleteListener);

  void removeQueueRequest(const long long requestId);

  IDownloader* _downloader;
  long long    _requestToDownloaderID;
  SingleBilElevationDataProvider_BufferDownloadListener* _listener;

public:
  SingleBilElevationDataProvider(const URL& bilUrl,
                                  const Sector& sector,
                                  const Vector2I& extent,
                                  double deltaHeight = 0);

  ~SingleBilElevationDataProvider();

  bool isReadyToRender(const G3MRenderContext* rc) {
    return (_elevationDataResolved);
  }

  void initialize(const G3MContext* context);

  const long long requestElevationData(const Sector& sector,
                                       const Vector2I& extent,
                                       IElevationDataListener* listener,
                                       bool autodeleteListener);

  void cancelRequest(const long long requestId);


  void onElevationData(ElevationData* elevationData);

  std::vector<const Sector*> getSectors() const{
    std::vector<const Sector*> sectors;
    sectors.push_back(&_sector);
    return sectors;
  }

  const Vector2I getMinResolution() const{
    return Vector2I(_extentWidth, _extentHeight);
  }

};

#endif
