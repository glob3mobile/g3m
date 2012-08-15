//
//  CachedDownloader.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

#include "CachedDownloader.hpp"


class SaverDownloadListener : public IDownloadListener {
  IDownloadListener* _listener;
  const bool         _deleteListener;
  IStorage*          _cacheStorage;
  const URL          _cacheFileName;
  
public:
  SaverDownloadListener(IDownloadListener* listener,
                        bool deleteListener,
                        IStorage* cacheStorage,
                        const URL cacheFileName) :
  _listener(listener),
  _deleteListener(deleteListener),
  _cacheStorage(cacheStorage),
  _cacheFileName(cacheFileName)
  {
    
  }
  
  void deleteListener() {
    if (_deleteListener && (_listener != NULL)) {
      delete _listener;
      _listener = NULL;
    }
  }
  
  void saveResponse(const Response* response) {
    if (!_cacheStorage->contains(_cacheFileName)) {
      _cacheStorage->save(_cacheFileName,
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
  std::string result = path;
  std::replace(result.begin(), result.end(), '/', '_');
  return result;
}


const URL CachedDownloader::getCacheFileName(const URL& url) const {
  return URL(_cacheDirectory, removeInvalidChars(url.getPath()));
}

long CachedDownloader::request(const URL& url,
                               long priority,
                               IDownloadListener* listener,
                               bool deleteListener) {
  
  const URL cacheFileName = getCacheFileName(url);
  
  
  const ByteBuffer* cachedBuffer = _cacheStorage->read(cacheFileName);
  if (cachedBuffer == NULL) {
    // cache miss
    const long requestId = _downloader->request(url,
                                                priority,
                                                new SaverDownloadListener(listener,
                                                                          deleteListener,
                                                                          _cacheStorage,
                                                                          cacheFileName),
                                                true);
    
    delete cachedBuffer;
    return requestId;
  }
  else {
    // cache hit
    Response response(url, cachedBuffer);
    
    listener->onDownload(&response);
    
    if (deleteListener) {
      delete listener;
    }
    
    delete cachedBuffer;
    return -1;
  }
}
