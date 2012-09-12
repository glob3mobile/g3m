//
//  CachedDownloader.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

#include "CachedDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "IStringBuilder.hpp"

class SaverDownloadListener : public IBufferDownloadListener {
  CachedDownloader*        _downloader;
  IBufferDownloadListener* _listener;
  const bool               _deleteListener;
  IStorage*                _cacheStorage;
  
public:
  SaverDownloadListener(CachedDownloader* downloader,
                        IStorage* cacheStorage,
                        IBufferDownloadListener* listener,
                        bool deleteListener) :
  _downloader(downloader),
  _cacheStorage(cacheStorage),
  _listener(listener),
  _deleteListener(deleteListener)
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
  
  void saveResponse(const URL& url,
                    const IByteBuffer& data) {
    if (!_cacheStorage->containsBuffer(url)) {
      _downloader->countSave();
      
      _cacheStorage->saveBuffer(url, data);
    }
  }
  
  void onDownload(const URL& url,
                  const IByteBuffer& data) {
    saveResponse(url, data);
    
    _listener->onDownload(url, data);
    
    deleteListener();
  }
  
  void onError(const URL& url,
               const IByteBuffer& data) {
    _listener->onError(url, data);
    
    deleteListener();
  }
  
  void onCanceledDownload(const URL& url,
                          const IByteBuffer& data) {
    saveResponse(url, data);
    
    _listener->onCanceledDownload(url, data);
    
    // no deleteListener() call, onCanceledDownload() is always called before onCancel().
  }
  
  void onCancel(const URL& url) {
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

void CachedDownloader::cancelRequest(long long requestId) {
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

long long CachedDownloader::requestBuffer(const URL& url,
                                          long long priority,
                                          IBufferDownloadListener* listener,
                                          bool deleteListener) {
  _requestsCounter++;
  
  const IByteBuffer* cachedBuffer = _cacheStorage->readBuffer(url);
  if (cachedBuffer == NULL) {
    // cache miss
    return _downloader->requestBuffer(url,
                                      priority,
                                      new SaverDownloadListener(this,
                                                                _cacheStorage,
                                                                listener,
                                                                deleteListener),
                                      true);
  }
  else {
    // cache hit
    _cacheHitsCounter++;
    
    listener->onDownload(url, *cachedBuffer);
    
    if (deleteListener) {
#ifdef C_CODE
      delete listener;
#else
      listener = NULL;
#endif
    }
    
    delete cachedBuffer;
    return -1;
  }
}

const std::string CachedDownloader::statistics() {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("CachedDownloader(cache hits=");
  isb->add(_cacheHitsCounter);
  isb->add("/");
  isb->add(_requestsCounter);
  isb->add(", saves=");
  isb->add(_savesCounter);
  isb->add(", downloader=");
  isb->add(_downloader->statistics());
  std::string s = isb->getString();
  delete isb;
  return s;
}
