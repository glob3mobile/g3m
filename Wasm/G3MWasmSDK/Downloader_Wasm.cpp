

#include "Downloader_Wasm.hpp"


Downloader_Wasm::Downloader_Wasm(const int          maxConcurrentOperationCount,
				 const int          delayMillis,
				 const bool         verboseErrors,
				 const std::string& proxy) :
  _maxConcurrentOperationCount(maxConcurrentOperationCount),
  _delayMillis(delayMillis),
  _verboseErrors(verboseErrors),
  _proxy(proxy)
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

void Downloader_Wasm::start() {
#error TODO
}

void Downloader_Wasm::stop() {
#error TODO
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
