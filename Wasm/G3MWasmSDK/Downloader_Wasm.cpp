

#include "Downloader_Wasm.hpp"

#include <emscripten/html5.h>


Downloader_Wasm::Downloader_Wasm(const int  maxConcurrentOperationCount,
				 const int  delayMillis,
				 const bool verboseErrors) :
  _maxConcurrentOperationCount(maxConcurrentOperationCount),
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
#warning TODO
  return "";
}

long long Downloader_Wasm::requestBuffer(const URL& url,
					 long long priority,
					 const TimeInterval& timeToCache,
					 bool readExpired,
					 IBufferDownloadListener* listener,
					 bool deleteListener,
					 const std::string& tag) {
#error TODO
}

long long Downloader_Wasm::requestImage(const URL& url,
					long long priority,
					const TimeInterval& timeToCache,
					bool readExpired,
					IImageDownloadListener* listener,
					bool deleteListener,
					const std::string& tag) {
#error TODO
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
      delete handler?;
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
