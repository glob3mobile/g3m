//
//  DownloaderImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#ifndef __G3MiOSSDK__DownloaderImageBuilder__
#define __G3MiOSSDK__DownloaderImageBuilder__

#include "IImageBuilder.hpp"
#include "URL.hpp"
#include "TimeInterval.hpp"
#include "DownloadPriority.hpp"

class DownloaderImageBuilder : public IImageBuilder {
private:
#ifdef C_CODE
  const URL _url;
#endif
#ifdef JAVA_CODE
  private final URL _url;
#endif
  const long long    _priority;
  const TimeInterval _timeToCache;
  const bool         _readExpired;


public:
  DownloaderImageBuilder(const URL& url,
                         long long priority = DownloadPriority::MEDIUM,
                         const TimeInterval& timeToCache = TimeInterval::fromDays(30),
                         const bool readExpired = true) :
  _url(url),
  _priority(priority),
  _timeToCache(timeToCache),
  _readExpired(readExpired)
  {
  }

  virtual ~DownloaderImageBuilder() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);

};

#endif
