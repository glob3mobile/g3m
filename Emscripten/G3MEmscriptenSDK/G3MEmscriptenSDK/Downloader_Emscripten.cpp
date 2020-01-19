

#include "Downloader_Emscripten.hpp"

#include <emscripten/html5.h>

//#define __USE_FETCH__
#ifdef __USE_FETCH__
#include <emscripten/fetch.h>
#endif

#define __USE_VAL__
#ifdef __USE_VAL__
#include <emscripten.h>
#include <emscripten/val.h>
using namespace emscripten;

#include "EMStorage.hpp"
#endif

#include <limits>
#include <sstream>
#include <vector>

#include "URL.hpp"
#include "IBufferDownloadListener.hpp"
#include "IImageDownloadListener.hpp"
#include "ByteBuffer_Emscripten.hpp"
#include "Image_Emscripten.hpp"


extern "C" {
EMSCRIPTEN_KEEPALIVE
void Downloader_Emscripten_Handler_onLoad(int xhrStatus,
                                          int xhrResponseID,
                                          void* voidHandler);

EMSCRIPTEN_KEEPALIVE
void Downloader_Emscripten_Handler_processResponse(int xhrStatus,
                                                   int domImageID,
                                                   void* voidHandler);

};


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
  _canceled(false),
  _requestID(requestID),
  _tag(tag)
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

  bool isCanceled() const {
    return _canceled;
  }

  void onCanceledDownload(const URL& url,
                          const val& data) {
    if (_bufferListener != NULL) {
      IByteBuffer* byteBuffer = new ByteBuffer_Emscripten(data);

      _bufferListener->onCanceledDownload(url, byteBuffer, false);
    }

    if (_imageListener != NULL) {
      Image_Emscripten* image = new Image_Emscripten(data);

      if (image->getDOMImage().as<bool>()) {
        _imageListener->onCanceledDownload(url, image, false);
      }
      else {
        emscripten_console_error("Can't create image from data (1)");
      }
      delete image;
    }
  }

  void onDownload(const URL& url,
                  const val& data) {
    if (_bufferListener != NULL) {
      IByteBuffer* byteBuffer = new ByteBuffer_Emscripten(data);

      _bufferListener->onDownload(url, byteBuffer, false);
      if (_deleteListener) {
        delete _bufferListener;
      }
    }

    if (_imageListener != NULL) {
      Image_Emscripten* image = new Image_Emscripten(data);

      if (image->getDOMImage().as<bool>()) {
        _imageListener->onDownload(url, image, false);
      }
      else {
        emscripten_console_error("Can't create image from data (2)");
        _imageListener->onError(url);
      }

      if (_deleteListener) {
        delete _imageListener;
      }
    }
  }

};


#ifdef __USE_FETCH__
void __downloadSucceeded(emscripten_fetch_t* fetch);

void __downloadFailed(emscripten_fetch_t* fetch);
#endif


class Downloader_Emscripten_Handler {
private:
  long long _priority;

  std::vector<ListenerEntry*> _listeners;

#ifdef __USE_VAL__
  Downloader_Emscripten* _downloader;
#endif

public:
  const std::string _urlPath;
  const bool        _isImageRequest;
  
  Downloader_Emscripten_Handler(const std::string&       urlPath,
                                IBufferDownloadListener* bufferListener,
                                const bool               deleteListener,
                                const long long          priority,
                                const long long          requestID,
                                const std::string&       tag) :
  _priority(priority),
  _urlPath(urlPath),
  _isImageRequest(false)
  {
#ifdef __USE_VAL__
    _downloader = NULL;
#endif
    _listeners.push_back(new ListenerEntry(bufferListener, NULL, deleteListener, requestID, tag));
  }

  Downloader_Emscripten_Handler(const std::string&      urlPath,
                                IImageDownloadListener* imageListener,
                                const bool              deleteListener,
                                const long long         priority,
                                const long long         requestID,
                                const std::string&      tag) :
  _priority(priority),
  _urlPath(urlPath),
  _isImageRequest(true)
  {
#ifdef __USE_VAL__
    _downloader = NULL;
#endif
    _listeners.push_back(new ListenerEntry(NULL, imageListener, deleteListener, requestID, tag));
  }

private:
  ~Downloader_Emscripten_Handler() {
    emscripten_console_log("### ~Downloader_Emscripten_Handler()");
  }
public:

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
      if ((listener != NULL) && (listener->_requestID == requestID)) {
        listener->onCancel( URL(_urlPath) );
        delete listener;
        // _listeners[i] = NULL;
        _listeners.erase(_listeners.begin() + i);
        return true;
      }
    }
    
    return false;
  }

  bool cancelListenerForRequestId(const long long requestID) {
    emscripten_console_log("---> cancelListenerForRequestId()");
    for (size_t i = 0; i < _listeners.size(); i++) {
      ListenerEntry* listener = _listeners[i];
      if ((listener != NULL) && (listener->_requestID == requestID)) {
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
      if ((listener != NULL) && (listener->_tag == tag)) {
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
    emscripten_console_log("---> cancelListenersTagged()");
    for (size_t i = 0; i < _listeners.size(); i++) {
      ListenerEntry* listener = _listeners[i];
      if ((listener != NULL) && (listener->_tag == tag)) {
        listener->cancel();
      }
    }
  }

#ifdef __USE_FETCH__
  void runWithDownloader(Downloader_Emscripten* downloader) {
    emscripten_fetch_attr_t attr;
    emscripten_fetch_attr_init(&attr);
    strcpy(attr.requestMethod, "GET");
    attr.attributes = EMSCRIPTEN_FETCH_LOAD_TO_MEMORY;
    attr.onsuccess  = __downloadSucceeded;
    attr.onerror    = __downloadFailed;
    attr.userData   = this;

    // pub struct emscripten_fetch_attr_t {
    //    pub requestMethod: [c_char; 32],
    // 	  pub userData: *mut c_void,
    // 	  pub onsuccess: Option<unsafe extern fn(_: *mut emscripten_fetch_t)>,
    // 	  pub onerror: Option<unsafe extern fn(_: *mut emscripten_fetch_t)>,
    // 	  pub onprogress: Option<unsafe extern fn(_: *mut emscripten_fetch_t)>,
    // 	  pub attributes: u32,
    // 	  pub timeoutMSecs: c_ulong,
    // 	  pub withCredentials: c_int,
    // 	  pub destinationPath: *const c_char,
    // 	  pub userName: *const c_char,
    // 	  pub password: *const c_char,
    // 	  pub requestHeaders: *const *const c_char,
    // 	  pub overriddenMimeType: *const c_char,
    // 	  pub requestData: *const c_char,
    // 	  pub requestDataSize: usize,
    // }

    emscripten_fetch(&attr, _urlPath.c_str());
  }
#endif


#ifdef __USE_VAL__
  void runWithDownloader(Downloader_Emscripten* downloader) {
    _downloader = downloader;

    const int urlID = EMStorage::put( val(_urlPath) );

    EM_ASM({
      var url            = document.EMStorage.take($0);
      var isImageRequest = $1;
      var handler        = $2;

      var xhr = new XMLHttpRequest();
      xhr.open("GET", url, true);
      xhr.responseType = isImageRequest ? "blob" : "arraybuffer";

      xhr.onload = function() {
        if (xhr.readyState == 4) {
          Module.ccall('Downloader_Emscripten_Handler_onLoad',
                       'void',
                       [ 'int',      'int',                                'number' ],
                       [ xhr.status, document.EMStorage.put(xhr.response), handler  ]);
        }
      };

      xhr.send();

    }, urlID, _isImageRequest, this, downloader);
  }

  void processResponse(int statusCode,
                       const val& data) {
    emscripten_console_log("** processResponse (1)");
    const bool dataIsValid = (statusCode == 200) && !data.isNull();

    emscripten_console_log("** processResponse (2)");
    const URL url(_urlPath);

    if (dataIsValid) {
      emscripten_console_log("** processResponse (3)");
      for (size_t i = 0; i < _listeners.size(); i++) {
        emscripten_console_log("** processResponse (4)");
        ListenerEntry* listener = _listeners[i];
        if (listener != NULL) {
          if (listener->isCanceled()) {
            emscripten_console_log("** processResponse (5)");
            listener->onCanceledDownload(url, data);
            listener->onCancel(url);
          }
          else {
            emscripten_console_log("** processResponse (6)");
            listener->onDownload(url, data);
          }
        }
      }
    }
    else {
      {
        std::ostringstream oss;

        oss << "runWithDownloader: statusCode=";
        oss << statusCode;
        oss << ", data=";
        oss << data.call<std::string>("toString");
        oss << ", url=";
        oss << _urlPath;

        emscripten_console_error( oss.str().c_str() );
      }

      for (size_t i = 0; i < _listeners.size(); i++) {
        ListenerEntry* listener = _listeners[i];
        if (listener != NULL) {
          listener->onError(url);
        }
      }
    }
  }

  void createImageFromBlobAndProcessResponse(int xhrStatus,
                                             const val& blob) {
    emscripten_console_warn("createImageFromBlobAndProcessResponse");

    const int blobID = EMStorage::put(blob);

    EM_ASM({
      var img = new Image();

      var xhrStatus = $0;
      var blob = document.EMStorage.take($1);
      var imgURL = URL.createObjectURL(blob);

      img.onload = function() {
        Module.ccall('Downloader_Emscripten_Handler_processResponse', 'void', ['int', 'int', 'number'], [ xhrStatus, document.EMStorage.put(img), $2 ]);
        URL.revokeObjectURL(imgURL);
      };
      img.onerror = function() {
        Module.ccall('Downloader_Emscripten_Handler_processResponse', 'void', ['int', 'int', 'number'], [ xhrStatus, document.EMStorage.null(), $2 ]);
        URL.revokeObjectURL(imgURL);
      };
      img.onabort = function() {
        Module.ccall('Downloader_Emscripten_Handler_processResponse', 'void', ['int', 'int', 'number'], [ xhrStatus, document.EMStorage.null(), $2 ]);
        URL.revokeObjectURL(imgURL);
      };

      img.src = imgURL;

    }, xhrStatus, blobID, this);

    //    var that = this;
    //
    //    var auxImg = new Image();
    //    var imgURL = $wnd.g3mURL.createObjectURL(blob);
    //
    //    auxImg.onload = function() {
    //      that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhrStatus, auxImg);
    //      $wnd.g3mURL.revokeObjectURL(imgURL);
    //    };
    //    auxImg.onerror = function() {
    //      that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhrStatus, null);
    //      $wnd.g3mURL.revokeObjectURL(imgURL);
    //    };
    //    auxImg.onabort = function() {
    //      that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhrStatus, null);
    //      $wnd.g3mURL.revokeObjectURL(imgURL);
    //    };
    //
    //    auxImg.src = imgURL;
  }

  void onLoad(int xhrStatus,
              const val& xhrResponse) {
    emscripten_console_log( xhrResponse.call<std::string>("toString").c_str() );

    emscripten_console_log("===> onLoad 1" );
    _downloader->removeDownloadingHandlerForURLPath(_urlPath);

    if (xhrStatus == 200) {
      emscripten_console_log("===> onLoad 2");
      if (_isImageRequest) {
        emscripten_console_log("===> onLoad 3");
        createImageFromBlobAndProcessResponse(xhrStatus, xhrResponse);
      }
      else {
        emscripten_console_log("===> onLoad 4");
        processResponse(xhrStatus, xhrResponse);
      }
    }
    else {
      emscripten_console_log("===> onLoad 5");
      processResponse(xhrStatus, val::null());
    }
  }

#endif


#ifdef __USE_FETCH__
  void onFetchDownloadSucceeded(emscripten_fetch_t* fetch) {
    emscripten_console_log("onFetchDownloadSucceeded 1");
    printf("Finished downloading %llu bytes from URL %s.\n", fetch->numBytes, fetch->url);

    // The data is now available at fetch->data[0] through fetch->data[fetch->numBytes-1];

    emscripten_console_log("onFetchDownloadSucceeded 2");
    URL url(fetch->url);

#warning TODO create image or create buffer
    emscripten_console_log("onFetchDownloadSucceeded 3");
    for (size_t i = 0; i < _listeners.size(); i++) {
      emscripten_console_log("onFetchDownloadSucceeded 4");
      ListenerEntry* listener = _listeners[i];
      if (listener != NULL) {
        emscripten_console_log("onFetchDownloadSucceeded 5");
        listener->onError(url);
      }
    }

    emscripten_console_log("onFetchDownloadSucceeded 6");
    emscripten_fetch_close(fetch); // Free data associated with the fetch.
  }

  void onFetchDownloadFailed(emscripten_fetch_t* fetch) {
    printf("Downloading %s failed, HTTP failure status code: %d.\n", fetch->url, fetch->status);

    URL url(fetch->url);

    for (size_t i = 0; i < _listeners.size(); i++) {
      ListenerEntry* listener = _listeners[i];
      if (listener != NULL) {
        listener->onError(url);
      }
    }

    emscripten_fetch_close(fetch); // Also free data on failure.
  }
#endif
};

void Downloader_Emscripten_Handler_onLoad(int xhrStatus,
                                          int xhrResponseID,
                                          void* voidHandler) {
  emscripten_console_log("Downloader_Emscripten_Handler_onLoad 1");
  val xhrResponse = EMStorage::take(xhrResponseID);

  emscripten_console_log("Downloader_Emscripten_Handler_onLoad 2");
  Downloader_Emscripten_Handler* handler = (Downloader_Emscripten_Handler*) voidHandler;

  emscripten_console_log("Downloader_Emscripten_Handler_onLoad 3");
  handler->onLoad(xhrStatus, xhrResponse);
}

void Downloader_Emscripten_Handler_processResponse(int xhrStatus,
                                                   int domImageID,
                                                   void* voidHandler) {
  emscripten_console_log("Downloader_Emscripten_Handler_processResponse 1");
  val domImage = EMStorage::take(domImageID);

  emscripten_console_log("Downloader_Emscripten_Handler_processResponse 2");
  Downloader_Emscripten_Handler* handler = (Downloader_Emscripten_Handler*) voidHandler;

  emscripten_console_log("Downloader_Emscripten_Handler_processResponse 3");
  handler->processResponse(xhrStatus, domImage);
}


#ifdef __USE_FETCH__
void __downloadSucceeded(emscripten_fetch_t* fetch) {
  Downloader_Emscripten_Handler* handler = (Downloader_Emscripten_Handler*) fetch->userData;
  handler->onFetchDownloadSucceeded(fetch);
}

void __downloadFailed(emscripten_fetch_t* fetch) {
  Downloader_Emscripten_Handler* handler = (Downloader_Emscripten_Handler*) fetch->userData;
  handler->onFetchDownloadFailed(fetch);
}
#endif

Downloader_Emscripten::Downloader_Emscripten(const int  maxConcurrentOperationCount,
                                             const int  delayMillis) :
_maxConcurrentOperationCount(maxConcurrentOperationCount),
_delayMillis(delayMillis),
_timeoutID(0),
_requestIDCounter(1),
_requestsCounter(0),
_cancelsCounter(0)
{
}

void Downloader_Emscripten::onResume(const G3MContext* context) {
  // do nothing
}

void Downloader_Emscripten::onPause(const G3MContext* context) {
  // do nothing
}

void Downloader_Emscripten::onDestroy(const G3MContext* context) {
  // do nothing
}

void Downloader_Emscripten::initialize(const G3MContext* context,
                                       FrameTasksExecutor* frameTasksExecutor) {
  // do nothing
}

const std::string Downloader_Emscripten::statistics() {
  std::ostringstream oss;

  oss << "Downloader_Emscripten ";
  oss << "downloading="     << _downloadingHandlers.size();
  oss << ", queued="        << _queuedHandlers.size();
  oss << ", totalRequests=" << _requestsCounter;
  oss << ", totalCancels="  << _cancelsCounter;

  return oss.str();
}

long long Downloader_Emscripten::requestBuffer(const URL& url,
                                               long long priority,
                                               const TimeInterval& timeToCache,
                                               bool readExpired,
                                               IBufferDownloadListener* listener,
                                               bool deleteListener,
                                               const std::string& tag) {
  _requestsCounter++;

  const std::string urlPath = url._path;
  Downloader_Emscripten_Handler* handler =  _downloadingHandlers.count(urlPath) ? _downloadingHandlers[urlPath] : NULL;

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
      handler = new Downloader_Emscripten_Handler(urlPath, listener, deleteListener, priority, requestID, tag);
      _queuedHandlers[urlPath] = handler;
    }
  }

  return requestID;
}

long long Downloader_Emscripten::requestImage(const URL& url,
                                              long long priority,
                                              const TimeInterval& timeToCache,
                                              bool readExpired,
                                              IImageDownloadListener* listener,
                                              bool deleteListener,
                                              const std::string& tag) {
  _requestsCounter++;

  const std::string urlPath = url._path;
  Downloader_Emscripten_Handler* handler =  _downloadingHandlers.count(urlPath) ? _downloadingHandlers[urlPath] : NULL;

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
      handler = new Downloader_Emscripten_Handler(urlPath, listener, deleteListener, priority, requestID, tag);
      _queuedHandlers[urlPath] = handler;
    }
  }

  return requestID;
}

bool Downloader_Emscripten::cancelRequest(long long requestID) {
  if (requestID < 0) {
    return false;
  }

  _cancelsCounter++;

  for (const std::pair<const std::string, Downloader_Emscripten_Handler*>& element : _queuedHandlers) {
    Downloader_Emscripten_Handler* handler  = element.second;
    if (handler->removeListenerForRequestId(requestID)) {
      if (!handler->hasListener()) {
        _queuedHandlers.erase( handler->_urlPath );
      }
      return true;
    }
  }
  
  for (const std::pair<const std::string, Downloader_Emscripten_Handler*>& element : _downloadingHandlers) {
    Downloader_Emscripten_Handler* handler  = element.second;
    if (handler->cancelListenerForRequestId(requestID)) {
      return true;
    }
  }

  return false;
}

void Downloader_Emscripten::cancelRequestsTagged(const std::string& tag) {
  if (tag.empty()) {
    return;
  }

  _cancelsCounter++;

  std::map<const std::string, Downloader_Emscripten_Handler*>::iterator it = _queuedHandlers.begin();
  while (it != _queuedHandlers.end()) {
    Downloader_Emscripten_Handler* handler = it->second;
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

  for (const std::pair<const std::string, Downloader_Emscripten_Handler*>& element : _downloadingHandlers) {
    Downloader_Emscripten_Handler* handler  = element.second;
    handler->cancelListenersTagged(tag);
  }

}

void __downloaderHeartbeat(void *userData) {
  Downloader_Emscripten* downloader = (Downloader_Emscripten*) userData;
  downloader->__heartbeat();
}

void Downloader_Emscripten::queueHeartbeat() {
  _timeoutID = emscripten_set_timeout(__downloaderHeartbeat,
                                      _delayMillis,
                                      (void*) this);
}

void Downloader_Emscripten::__heartbeat() {
  if (_downloadingHandlers.size() < _maxConcurrentOperationCount) {
    Downloader_Emscripten_Handler* handler = getHandlerToRun();
    if (handler != NULL) {
      handler->runWithDownloader(this);
#warning FIX MEMORY LEAK
//      delete handler;
    }
  }
  queueHeartbeat();
}

void Downloader_Emscripten::start() {
  queueHeartbeat();
}

void Downloader_Emscripten::stop() {
  emscripten_clear_timeout(_timeoutID);
}

Downloader_Emscripten_Handler* Downloader_Emscripten::getHandlerToRun() {
  if (_downloadingHandlers.size() >= _maxConcurrentOperationCount) {
    return NULL;
  }
  if (_queuedHandlers.empty()) {
    return NULL;
  }

  Downloader_Emscripten_Handler* selectedHandler  = NULL;
  long long                      selectedPriority = -(std::numeric_limits<long long>::max());
  for (std::pair<const std::string, Downloader_Emscripten_Handler*>& element : _queuedHandlers) {
    Downloader_Emscripten_Handler* candidateHandler  = element.second;
    const long long                candidatePriority = candidateHandler->getPriority();

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

void Downloader_Emscripten::removeDownloadingHandlerForURLPath(const std::string& urlPath) {
  _downloadingHandlers.erase(urlPath);
}
