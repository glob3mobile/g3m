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

class Downloader_win8;

class Downloader_win8_WorkerThread : std::thread{

private:
	const Downloader_win8* _downloader;
	bool            _stopping;

public:
	Downloader_win8_WorkerThread(const Downloader_win8* downloader);

	void start();

	void stop();

	bool isStopping();

	void run();

	~Downloader_win8_WorkerThread();
};

#endif