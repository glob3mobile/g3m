//
//  Downloader_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef __G3MWindowsSDK_Downloader_win8__
#define __G3MWindowsSDK_Downloader_win8__

#include "IDownloader.hpp"

class Downloader_win8 : public IDownloader {
	void initialize(const G3MContext* context, FrameTasksExecutor* frameTasksExecutor);
	void onResume(const G3MContext* context);
	void onPause(const G3MContext* context);
	void onDestroy(const G3MContext* context);
	void start();
	void stop();
	long long requestBuffer(const URL& url, long long priority,
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

	const std::string statistics();
};


#endif