

#include "Downloader_Wasm.hpp"

#include <emscripten/html5.h>

#include <limits>
#include <sstream>
#include <vector>

#include "URL.hpp"
#include "IBufferDownloadListener.hpp"
#include "IImageDownloadListener.hpp"


class ListenerEntry {
private:
  IBufferDownloadListener* _bufferListener;
  IImageDownloadListener*  _imageListener;
  const bool               _deleteListener;

  bool _canceled;
  
public:
  const long long   _requestID;
  const std::string _tag;
  
  ListenerEntry(IBufferDownloadListener* bufferListener,
		IImageDownloadListener*  imageListener,
		const bool               deleteListener,
		const long long          requestID,
		const std::string&       tag) :
    _bufferListener(bufferListener),
    _imageListener(imageListener),
    _deleteListener(deleteListener),
    _requestID(requestID),
    _tag(tag),
    _canceled(false)
  {
  }

  void onCancel(const URL& url) {
    if (_bufferListener != NULL) {
      _bufferListener->onCancel(url);
      if (_deleteListener) {
	delete _bufferListener;
      }
      _bufferListener = NULL;
    }
    
    if (_imageListener != NULL) {
      _imageListener->onCancel(url);
      if (_deleteListener) {
	delete _imageListener;
      }
      _imageListener = NULL;
    }
  }

  void onError(const URL& url) {
    if (_bufferListener != NULL) {
      _bufferListener->onError(url);
      if (_deleteListener) {
	delete _bufferListener;
      }
      _bufferListener = NULL;
    }
    
    if (_imageListener != NULL) {
      _imageListener->onError(url);
      if (_deleteListener) {
	delete _imageListener;
      }
      _imageListener = NULL;
    }
  }

  void cancel() {
    _canceled = true;
  }

};


class Downloader_Wasm_Handler {
private:
  long long _priority;

  std::vector<ListenerEntry*> _listeners;

  
public:
  const std::string _urlPath;
  const bool        _isImageRequest;
  
  Downloader_Wasm_Handler(const std::string&       urlPath,
			  IBufferDownloadListener* bufferListener,
                          const bool               deleteListener,
                          const long long          priority,
                          const long long          requestID,
                          const std::string&       tag) :
    _urlPath(urlPath),
    _priority(priority),
    _isImageRequest(false)
  {
    _listeners.push_back(new ListenerEntry(bufferListener, NULL, deleteListener, requestID, tag));
  }

  Downloader_Wasm_Handler(const std::string&      urlPath,
			  IImageDownloadListener* imageListener,
                          const bool              deleteListener,
                          const long long         priority,
                          const long long         requestID,
                          const std::string&      tag) :
    _urlPath(urlPath),
    _priority(priority),
    _isImageRequest(false)
  {
    _listeners.push_back(new ListenerEntry(NULL, imageListener, deleteListener, requestID, tag));
  }

  ~Downloader_Wasm_Handler() {
  }

  const long long getPriority() const {
    return _priority;
  }

  void addListener(IBufferDownloadListener* listener,
		   const bool               deleteListener,
		   const long long          priority,
		   const long long          requestID,
		   const std::string&       tag) {
    _listeners.push_back(new ListenerEntry(listener, NULL, deleteListener, requestID, tag));
    if (priority > _priority) {
      _priority = priority;
    }
  }

  void addListener(IImageDownloadListener*  listener,
		   const bool               deleteListener,
		   const long long          priority,
		   const long long          requestID,
		   const std::string&       tag) {
    _listeners.push_back(new ListenerEntry(NULL, listener, deleteListener, requestID, tag));
    if (priority > _priority) {
      _priority = priority;
    }
  }

  bool removeListenerForRequestId(const long long requestID) {
    for (size_t i = 0; i < _listeners.size(); i++) {
      ListenerEntry* listener = _listeners[i];
      if (listener->_requestID == requestID) {
	listener->onCancel( URL(_urlPath) );
	delete listener;
	return true;
      }
    }
    
    return false;
  }

  bool cancelListenerForRequestId(const long long requestID) {
    for (size_t i = 0; i < _listeners.size(); i++) {
      ListenerEntry* listener = _listeners[i];
      if (listener->_requestID == requestID) {
	listener->cancel();
	return true;
      }
    }

    return false;
  }

  bool hasListener() const {
    return !_listeners.empty();
  }

  bool removeListenersTagged(const std::string& tag) {
    bool anyRemoved = false;

    const URL url(_urlPath);

    std::vector<ListenerEntry*>::iterator it = _listeners.begin();
    while ( it != _listeners.end() ) {
      ListenerEntry* listener = *it;
      if (listener->_tag == tag) {
	listener->onCancel(url);

	it = _listeners.erase(it);
	anyRemoved = true;
      }
      else {
	++it;
      }
    }

    return anyRemoved;
  }

  void cancelListenersTagged(const std::string& tag) {
    for (size_t i = 0; i < _listeners.size(); i++) {
      ListenerEntry* listener = _listeners[i];
      if (listener->_tag == tag) {
	listener->cancel();
      }
    }
  }

  void runWithDownloader(Downloader_Wasm* downloader) {
#error TODO
  }

};


Downloader_Wasm::Downloader_Wasm(const int  maxConcurrentOperationCount,
				 const int  delayMillis,
				 const bool verboseErrors) :
  _maxConcurrentOperationCount(maxConcurrentOperationCount),
  _requestIDCounter(1),
  _requestsCounter(0),
  _cancelsCounter(0),
  _delayMillis(delayMillis),
  _verboseErrors(verboseErrors),
  _timeoutID(0)
{
}

void Downloader_Wasm::onResume(const G3MContext* context) {
  // do nothing    
}
  
void Downloader_Wasm::onPause(const G3MContext* context) {
  // do nothing    
}
  
void Downloader_Wasm::onDestroy(const G3MContext* context) {
  // do nothing    
}

void Downloader_Wasm::initialize(const G3MContext* context,
				 FrameTasksExecutor* frameTasksExecutor) {
  // do nothing    
}

const std::string Downloader_Wasm::statistics() {
  std::ostringstream oss;

  oss << "Downloader_Wasm ";
  oss << "downloading="     << _downloadingHandlers.size();
  oss << ", queued="        << _queuedHandlers.size();
  oss << ", totalRequests=" << _requestsCounter;
  oss << ", totalCancels="  << _cancelsCounter;

  return oss.str();
}

long long Downloader_Wasm::requestBuffer(const URL& url,
					 long long priority,
					 const TimeInterval& timeToCache,
					 bool readExpired,
					 IBufferDownloadListener* listener,
					 bool deleteListener,
					 const std::string& tag) {
  _requestsCounter++;

  const std::string urlPath = url._path;
  Downloader_Wasm_Handler* handler =  _downloadingHandlers.count(urlPath) ? _downloadingHandlers[urlPath] : NULL;

  const long long requestID = _requestIDCounter++;

  if ((handler != NULL) && !handler->_isImageRequest) {
    // the URL is being downloaded, just add the new listener
    handler->addListener(listener, deleteListener, priority, requestID, tag);
  }
  else {
    handler = _queuedHandlers.count(urlPath) ? _queuedHandlers[urlPath] : NULL;
    if ((handler != NULL) && !handler->_isImageRequest) {
      // the URL is queued for future download, just add the new listener
      handler->addListener(listener, deleteListener, priority, requestID, tag);
    }
    else {
      // new handler, queue it
      handler = new Downloader_Wasm_Handler(urlPath, listener, deleteListener, priority, requestID, tag);
      _queuedHandlers[urlPath] = handler;
    }
  }

  return requestID;
}

long long Downloader_Wasm::requestImage(const URL& url,
					long long priority,
					const TimeInterval& timeToCache,
					bool readExpired,
					IImageDownloadListener* listener,
					bool deleteListener,
					const std::string& tag) {
  _requestsCounter++;

  const std::string urlPath = url._path;
  Downloader_Wasm_Handler* handler =  _downloadingHandlers.count(urlPath) ? _downloadingHandlers[urlPath] : NULL;

  const long long requestID = _requestIDCounter++;

  if ((handler != NULL) && !handler->_isImageRequest) {
    // the URL is being downloaded, just add the new listener
    handler->addListener(listener, deleteListener, priority, requestID, tag);
  }
  else {
    handler = _queuedHandlers.count(urlPath) ? _queuedHandlers[urlPath] : NULL;
    if ((handler != NULL) && !handler->_isImageRequest) {
      // the URL is queued for future download, just add the new listener
      handler->addListener(listener, deleteListener, priority, requestID, tag);
    }
    else {
      // new handler, queue it
      handler = new Downloader_Wasm_Handler(urlPath, listener, deleteListener, priority, requestID, tag);
      _queuedHandlers[urlPath] = handler;
    }
  }

  return requestID;
}

bool Downloader_Wasm::cancelRequest(long long requestID) {
  if (requestID < 0) {
    return false;
  }

  _cancelsCounter++;

  for (const std::pair<const std::string, Downloader_Wasm_Handler*> element : _queuedHandlers) {
    Downloader_Wasm_Handler* handler  = element.second;
    if (handler->removeListenerForRequestId(requestID)) {
      if (!handler->hasListener()) {
	_queuedHandlers.erase( handler->_urlPath );
      }
      return true;
    }
  }
  
  for (const std::pair<const std::string, Downloader_Wasm_Handler*> element : _downloadingHandlers) {
    Downloader_Wasm_Handler* handler  = element.second;
    if (handler->cancelListenerForRequestId(requestID)) {
      return true;
    }
  }

  return false;
}

void Downloader_Wasm::cancelRequestsTagged(const std::string& tag) {
  if (tag.empty()) {
    return;
  }

  _cancelsCounter++;

  std::map<const std::string, Downloader_Wasm_Handler*>::iterator it = _queuedHandlers.begin();
  while (it != _queuedHandlers.end()) {
    Downloader_Wasm_Handler* handler = it->second;
    if (handler->removeListenersTagged(tag)) {
      if (!handler->hasListener()) {
	it = _queuedHandlers.erase(it);
      }
      else {
	++it;
      }
    }
    else {
      ++it;
    }
  }

  for (const std::pair<const std::string, Downloader_Wasm_Handler*> element : _downloadingHandlers) {
    Downloader_Wasm_Handler* handler  = element.second;
    handler->cancelListenersTagged(tag);
  }

}

void __downloaderHeartbeat(void *userData) {
  Downloader_Wasm* downloader = (Downloader_Wasm*) userData;
  downloader->__heartbeat();
}

void Downloader_Wasm::queueHeartbeat() {
  _timeoutID = emscripten_set_timeout(__downloaderHeartbeat,
				      _delayMillis,
				      (void*) this);
}

void Downloader_Wasm::__heartbeat() {
  if (_downloadingHandlers.size() < _maxConcurrentOperationCount) {
    Downloader_Wasm_Handler* handler = getHandlerToRun();
    if (handler != NULL) {
      handler->runWithDownloader(this);
      delete handler;
    }
  }
  queueHeartbeat();
}

void Downloader_Wasm::start() {
  queueHeartbeat();
}

void Downloader_Wasm::stop() {
  emscripten_clear_timeout(_timeoutID);
}

Downloader_Wasm_Handler* Downloader_Wasm::getHandlerToRun() {
  if (_downloadingHandlers.size() >= _maxConcurrentOperationCount) {
    return NULL;
  }
  if (_queuedHandlers.empty()) {
    return NULL;
  }

  Downloader_Wasm_Handler* selectedHandler  = NULL;
  long long                selectedPriority = -(std::numeric_limits<long long>::max());
  for (std::pair<const std::string, Downloader_Wasm_Handler*> element : _queuedHandlers) {
    Downloader_Wasm_Handler* candidateHandler  = element.second;
    const long long          candidatePriority = candidateHandler->getPriority();

    if (candidatePriority > selectedPriority) {
      const std::string urlPath = element.first;
      selectedPriority = candidatePriority;
      selectedHandler  = candidateHandler;
    }
  }

  if (selectedHandler != NULL) {
    const std::string selectedURLPath = selectedHandler->_urlPath;

    // move the selected handler to _downloadingHandlers collection
    _queuedHandlers.erase(selectedURLPath);
    _downloadingHandlers[selectedURLPath] = selectedHandler;
  }

  return selectedHandler;
}
