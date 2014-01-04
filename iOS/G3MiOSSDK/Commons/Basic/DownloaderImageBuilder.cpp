//
//  DownloaderImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#include "DownloaderImageBuilder.hpp"

#include "Context.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"
#include "IImageBuilderListener.hpp"


class DownloaderImageBuilder_ImageDownloadListener : public IImageDownloadListener {
private:
  IImageBuilderListener* _listener;
  const bool             _deleteListener;

public:
  DownloaderImageBuilder_ImageDownloadListener(IImageBuilderListener* listener,
                                               bool deleteListener) :
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }

  ~DownloaderImageBuilder_ImageDownloadListener() {
    if (_deleteListener) {
      delete _listener;
    }
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
    _listener->imageCreated(image,
                            url.getPath());
    if (_deleteListener) {
      delete _listener;
      _listener = NULL;
    }
  }

  void onError(const URL& url) {
    _listener->onError("Error downloading image from \"" + url.getPath() + "\"");
    if (_deleteListener) {
      delete _listener;
      _listener = NULL;
    }
  }

  void onCancel(const URL& url) {
    _listener->onError("Canceled download image from \"" + url.getPath() + "\"");
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


void DownloaderImageBuilder::build(const G3MContext* context,
                                   IImageBuilderListener* listener,
                                   bool deleteListener) {
  IDownloader* downloader = context->getDownloader();

  downloader->requestImage(_url,
                           _priority,
                           _timeToCache,
                           _readExpired,
                           new DownloaderImageBuilder_ImageDownloadListener(listener,
                                                                            deleteListener),
                           true);
}
