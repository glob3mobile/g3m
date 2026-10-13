//
//  DownloaderImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#ifndef __G3M__DownloaderImageFactory__
#define __G3M__DownloaderImageFactory__

#include "AbstractImageFactory.hpp"
#include "URL.hpp"
#include "TimeInterval.hpp"
#include "DownloadPriority.hpp"

class DownloaderImageFactory : public AbstractImageFactory {
private:
  const URL          _url;
  const TimeInterval _timeToCache;
  const long long    _priority;
  const bool         _readExpired;

protected:
  ~DownloaderImageFactory() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  DownloaderImageFactory(const URL& url) :
  _url(url),
  _priority(DownloadPriority::MEDIUM),
  _timeToCache(TimeInterval::fromDays(30)),
  _readExpired(true)
  {
  }

  DownloaderImageFactory(const URL& url,
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


  void create(const G3MContext* context,
             IImageFactoryListener* listener,
             bool deleteListener);

};

#endif
