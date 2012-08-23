//
//  CachedDownloader.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

#include "CachedDownloader.hpp"
#include "IDownloadListener.hpp"

#include <sstream>

class SaverDownloadListener : public IDownloadListener {
  CachedDownloader*  _downloader;
  IDownloadListener* _listener;
  const bool         _deleteListener;
  IStorage*          _cacheStorage;
  const URL          _url;
  
public:
  SaverDownloadListener(CachedDownloader* downloader,
                        IStorage* cacheStorage,
                        const URL url,
                        IDownloadListener* listener,
                        bool deleteListener) :
  _downloader(downloader),
  _cacheStorage(cacheStorage),
  _url(url),
  _listener(listener),
  _deleteListener(deleteListener)
  {
    
  }
  
  void deleteListener() {
    if (_deleteListener && (_listener != NULL)) {
      delete _listener;
      _listener = NULL;
    }
  }
  
  void saveResponse(const Response* response) {
    if (!_cacheStorage->contains(_url)) {
      _downloader->countSave();
      _cacheStorage->save(_url,
                          *response->getByteBuffer());
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

void CachedDownloader::stop() {
  _downloader->stop();
}

void CachedDownloader::cancelRequest(long requestId) {
  _downloader->cancelRequest(requestId);
}

std::string CachedDownloader::removeInvalidChars(const std::string& path) const {
  std::string result = path;
  std::replace(result.begin(), result.end(), '/', '_');
  return result;
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
    return _downloader->request(url,
                                priority,
                                new SaverDownloadListener(this,
                                                          _cacheStorage,
                                                          url,
                                                          listener,
                                                          deleteListener),
                                true);
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
  std::ostringstream buffer;
  
  buffer << "CachedDownloader(cache hits=";
  buffer << _cacheHitsCounter;
  buffer << "/";
  buffer << _requestsCounter;
  buffer << ", saves=";
  buffer << _savesCounter;
  buffer << ", downloader=";
  buffer << _downloader->statistics();
  buffer << ")";
  
  return buffer.str();
}
