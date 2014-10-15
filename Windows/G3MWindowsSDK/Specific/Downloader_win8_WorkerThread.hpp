//
//  Storage_win8.hpp
//  G3MWindowsSDK
//
//  Created by F. Pulido on 26/09/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.

#ifndef __G3MWindowsSDK_Downloader_win8_WorkerThread__
#define __G3MWindowsSDK_Downloader_win8_WorkerThread__

#pragma once

#include <thread>
#include <mutex>
#include <chrono>
#include <ppltasks.h>
//#include <sched.h>

class Downloader_win8;

class Downloader_win8_WorkerThread {

private:
	Downloader_win8* _downloader;
	bool            _stopping = false;
	//std::thread _worker;

	std::mutex _lock;

	Windows::Foundation::IAsyncAction^ _workerThread;
	Windows::System::Threading::WorkItemHandler^ _workItem;

	concurrency::task<void> createTask();
	void run();

public:
	Downloader_win8_WorkerThread(Downloader_win8* downloader);

	void start();

	void stop();

	bool isStopping();

	~Downloader_win8_WorkerThread();
};

#endif