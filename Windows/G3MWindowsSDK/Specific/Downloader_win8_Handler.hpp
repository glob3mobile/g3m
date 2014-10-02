//
//  Storage_win8.hpp
//  G3MWindowsSDK
//
//  Created by F. Pulido on 26/09/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef __G3MWindowsSDK_Downloader_win8_Handler__
#define __G3MWindowsSDK_Downloader_win8_Handler__

#pragma once

#include "Downloader_win8_Listener.hpp"
#include "URL.hpp"

class Downloader_win8;
class StringUtils_win8;
class G3MContext;

using namespace Windows::Foundation;

class Listener_win8_Entry{

private:
	Downloader_win8_Listener* _listener;
	long long                _requestId;
	bool                     _canceled;
	

public:
	Listener_win8_Entry(Downloader_win8_Listener* listener, long long requestId);

	long long getRequestId();

	void cancel();

	bool isCanceled();

	Downloader_win8_Listener* getListener();

	~Listener_win8_Entry();
};


class Downloader_win8_Handler{

private:
	std::vector<Listener_win8_Entry*> _listeners = std::vector<Listener_win8_Entry*>();
	long long					_priority;
	URL*						_g3mURL;
	Windows::Foundation::Uri^	_winURL;
	

	const StringUtils_win8* _sUtils;
	
public:
	Downloader_win8_Handler(URL* url, Downloader_win8_Listener* listener, long long priority, long long requestId);

	void addListener(Downloader_win8_Listener* listener, long long priority, long long requestId);

	bool cancelListenerForRequestId (long long requestId);

	bool removeListenerForRequestId(long long requestId);

	bool hasListeners();

	long long priority();

	void runWithDownloader(Downloader_win8* downloader, G3MContext* context);

	void dealloc();

	~Downloader_win8_Handler();
};

#endif