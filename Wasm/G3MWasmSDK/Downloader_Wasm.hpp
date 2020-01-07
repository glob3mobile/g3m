
#ifndef Downloader_Wasm_hpp
#define Downloader_Wasm_hpp

#include "IDownloader.hpp"

#include <map>
#include <string>

class Downloader_Wasm_Handler;


class Downloader_Wasm : public IDownloader {
private:
  const int  _maxConcurrentOperationCount;
  const int  _delayMillis;
  const bool _verboseErrors;

  std::map<const std::string, Downloader_Wasm_Handler*> _downloadingHandlers;
  std::map<const std::string, Downloader_Wasm_Handler*> _queuedHandlers;

  long _timeoutID;

  long long _requestIDCounter;
  long long _requestsCounter;
  long long _cancelsCounter;

  void queueHeartbeat();

  Downloader_Wasm_Handler* getHandlerToRun();

public:
  Downloader_Wasm(const int  maxConcurrentOperationCount,
		  const int  delayMillis,
		  const bool verboseErrors);
 
  void onResume(const G3MContext* context);
  
  void onPause(const G3MContext* context);
  
  void onDestroy(const G3MContext* context);

  void initialize(const G3MContext* context,
                  FrameTasksExecutor* frameTasksExecutor);

  const std::string statistics();

  void start();

  void stop();

  long long requestBuffer(const URL& url,
			  long long priority,
			  const TimeInterval& timeToCache,
			  bool readExpired,
			  IBufferDownloadListener* listener,
			  bool deleteListener,
			  const std::string& tag);

  long long requestImage(const URL& url,
			 long long priority,
			 const TimeInterval& timeToCache,
			 bool readExpired,
			 IImageDownloadListener* listener,
			 bool deleteListener,
			 const std::string& tag);

  bool cancelRequest(long long requestID);

  void cancelRequestsTagged(const std::string& tag);


  void __heartbeat();

};

#endif
