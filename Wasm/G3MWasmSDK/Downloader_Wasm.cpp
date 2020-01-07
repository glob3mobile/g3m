

#include "Downloader_Wasm.hpp"

#include <emscripten/html5.h>

#include <limits>
#include <sstream>
#include <vector>

#include "URL.hpp"


class ListenerEntry {
private:
  bool _canceled;
  
public:
  IBufferDownloadListener* _bufferListener;
  IImageDownloadListener*  _imageListener;
  const bool               _deleteListener;
  const long long          _requestID;
  const std::string        _tag;
  
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

  void runWithDownloader(Downloader_Wasm* downloader) {
#error TODO
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
#error TODO
}

void Downloader_Wasm::cancelRequestsTagged(const std::string& tag) {
#error TODO
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
