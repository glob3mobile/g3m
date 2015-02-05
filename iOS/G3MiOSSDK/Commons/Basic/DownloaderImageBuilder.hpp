//
//  DownloaderImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#ifndef __G3MiOSSDK__DownloaderImageBuilder__
#define __G3MiOSSDK__DownloaderImageBuilder__

#include "AbstractImageBuilder.hpp"
#include "URL.hpp"
#include "TimeInterval.hpp"
#include "DownloadPriority.hpp"

class DownloaderImageBuilder : public AbstractImageBuilder {
private:
#ifdef C_CODE
  const URL          _url;
  const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
  private final URL          _url;
  private final TimeInterval _timeToCache;
#endif
  const long long _priority;
  const bool      _readExpired;


public:
  DownloaderImageBuilder(const URL& url) :
  _url(url),
  _priority(DownloadPriority::MEDIUM),
  _timeToCache(TimeInterval::fromDays(30)),
  _readExpired(true)
  {
  }

  DownloaderImageBuilder(const URL& url,
                         long long priority,
                         const TimeInterval& timeToCache,
                         const bool readExpired) :
  _url(url),
  _priority(priority),
  _timeToCache(timeToCache),
  _readExpired(readExpired)
  {
  }

  bool isMutable() const {
    return false;
  }

  ~DownloaderImageBuilder() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);

};

#endif
