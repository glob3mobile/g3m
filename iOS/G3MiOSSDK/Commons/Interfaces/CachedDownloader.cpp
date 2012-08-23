//
//  CachedDownloader.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

#include "CachedDownloader.hpp"
#include "IDownloadListener.hpp"

#include "IStringBuilder.hpp"

class SaverDownloadListener : public IDownloadListener {
  CachedDownloader*  _downloader;
  IDownloadListener* _listener;
  const bool         _deleteListener;
  IStorage*          _cacheStorage;
  const URL          _url;
  
public:
  SaverDownloadListener(CachedDownloader* downloader,
                        IDownloadListener* listener,
                        bool deleteListener,
                        IStorage* cacheStorage,
                        const URL url) :
  _downloader(downloader),
  _listener(listener),
  _deleteListener(deleteListener),
  _cacheStorage(cacheStorage),
  _url(url)
  {
    
  }
  
  void deleteListener() {
    if (_deleteListener && (_listener != NULL)) {
#ifdef C_CODE
      delete _listener;
#endif
      _listener = NULL;
    }
  }
  
  void saveResponse(const Response* response) {
    if (!_cacheStorage->contains(_url)) {
      _downloader->countSave();
      
      const ByteBuffer* bb = response->getByteBuffer();
      _cacheStorage->save(_url, *bb);
    }
  }
  
  void onDownload(const Response* response) {
    saveResponse(response);
    
    _listener->onDownload(response);
    
    deleteListener();
  }
  
  void onError(const Response* response) {
    _listener->onError(response);
    
    deleteListener();
  }

  void onCanceledDownload(const Response* response) {
    saveResponse(response);
    
    _listener->onCanceledDownload(response);
    
    // no deleteListener() call, onCanceledDownload() is always called before onCancel().
  }

  void onCancel(const URL* url) {
    _listener->onCancel(url);
    
    deleteListener();
  }
  
};


void CachedDownloader::start() {
  _downloader->start();
}

void CachedDownloader::cancelRequest(long requestId) {
  _downloader->cancelRequest(requestId);
}

std::string CachedDownloader::removeInvalidChars(const std::string& path) const {
#ifdef C_CODE
  std::string result = path;
  std::replace(result.begin(), result.end(), '/', '_');
  return result;
#endif
#ifdef JAVA_CODE
  return path.replaceAll("/", "_");
#endif
}


//const URL CachedDownloader::getCacheFileName(const URL& url) const {
//  return URL(_cacheDirectory, removeInvalidChars(url.getPath()));
//}

long CachedDownloader::request(const URL& url,
                               long priority,
                               IDownloadListener* listener,
                               bool deleteListener) {
  _requestsCounter++;
  
  const ByteBuffer* cachedBuffer = _cacheStorage->read(url);
  if (cachedBuffer == NULL) {
    // cache miss
    const long requestId = _downloader->request(url,
                                                priority,
                                                new SaverDownloadListener(this,
                                                                          listener,
                                                                          deleteListener,
                                                                          _cacheStorage,
                                                                          url),
                                                true);
    
//    delete cachedBuffer;
    return requestId;
  }
  else {
    // cache hit
    _cacheHitsCounter++;
    
    Response response(url, cachedBuffer);
    
    listener->onDownload(&response);
    
    if (deleteListener) {
      delete listener;
    }
    
    delete cachedBuffer;
    return -1;
  }
}


const std::string CachedDownloader::statistics() {
  
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("CachedDownloader(cache hits=")->add(_cacheHitsCounter)->add("/")->add(_requestsCounter)->add(", saves=");
  isb->add(_savesCounter)->add(", downloader=")->add(_downloader->statistics());
  std::string s = isb->getString();
  delete isb;
  return s;
}
