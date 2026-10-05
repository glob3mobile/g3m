//
//  DownloaderImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#include "DownloaderImageFactory.hpp"

#include "G3MContext.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"
#include "IImageFactoryListener.hpp"


class DownloaderImageFactory_ImageDownloadListener : public IImageDownloadListener {
private:
  IImageFactoryListener* _listener;
  const bool             _deleteListener;

public:
  DownloaderImageFactory_ImageDownloadListener(IImageFactoryListener* listener,
                                               bool deleteListener) :
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }

  ~DownloaderImageFactory_ImageDownloadListener() {
    if (_deleteListener) {
      delete _listener;
    }
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
    _listener->imageCreated(image,
                            url._path);
    if (_deleteListener) {
      delete _listener;
      _listener = NULL;
    }
  }

  void onError(const URL& url) {
    _listener->onError("Error downloading image from \"" + url._path + "\"");
    if (_deleteListener) {
      delete _listener;
      _listener = NULL;
    }
  }

  void onCancel(const URL& url) {
    _listener->onError("Canceled download image from \"" + url._path + "\"");
    if (_deleteListener) {
      delete _listener;
      _listener = NULL;
    }
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }

};


void DownloaderImageFactory::create(const G3MContext* context,
                                   IImageFactoryListener* listener,
                                   bool deleteListener) {
  IDownloader* downloader = context->getDownloader();

  downloader->requestImage(_url,
                           _priority,
                           _timeToCache,
                           _readExpired,
                           new DownloaderImageFactory_ImageDownloadListener(listener,
                                                                            deleteListener),
                           true);
}
