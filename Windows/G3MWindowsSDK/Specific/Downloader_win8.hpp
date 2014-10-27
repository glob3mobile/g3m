//
//  Downloader_win8.hpp
//  G3MWindowsSDK
//
//  Created by F. Pulido on 26/09/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef __G3MWindowsSDK_Downloader_win8__
#define __G3MWindowsSDK_Downloader_win8__

#include "IDownloader.hpp"
#include <map>
#include <vector>
#include <mutex>

//using namespace Windows::Foundation::Collections::IMap;
//using namespace Windows::Foundation;

class Downloader_win8_WorkerThread;
class Downloader_win8_Handler;
class Downloader_win8_Listener;


class Downloader_win8 : public IDownloader {

private:
	std::vector<Downloader_win8_WorkerThread*> _workers;
	std::map<std::string, Downloader_win8_Handler*> _downloadingHandlers; // downloads current in progress
	std::map<std::string, Downloader_win8_Handler*> _queuedHandlers; // queued downloads   

	long long _requestIdCounter;
	long long _requestsCounter;
	long long _cancelsCounter;
	int _maxConcurrentOperationCount;

	bool _started;

	std::mutex _lock;

	long long request(const URL &url,
					  long long priority,
					  Downloader_win8_Listener* listener);

public:
	Downloader_win8(int maxConcurrentOperationCount);

	void initialize(const G3MContext* context, FrameTasksExecutor* frameTasksExecutor);
	
	void start();

	void stop();

	long long requestBuffer(const URL& url, 
							long long priority,
							const TimeInterval& timeToCache,
							bool readExpired,
							IBufferDownloadListener* listener,
							bool deleteListener);

	long long requestImage(const URL& url,
						   long long priority,
						   const TimeInterval& timeToCache,
						   bool readExpired,
						   IImageDownloadListener* listener,
						   bool deleteListener);

	void cancelRequest(long long requestId);

	Downloader_win8_Handler* getHandlerToRun();

	void removeDownloadingHandlerForUrl(const std::string url);

	const std::string statistics();

	void onResume(const G3MContext* context) {
		//only for android
	}
	void onPause(const G3MContext* context){
		//only for android
	}
	void onDestroy(const G3MContext* context){
		//only for android
	}

	~Downloader_win8();
};


#endif