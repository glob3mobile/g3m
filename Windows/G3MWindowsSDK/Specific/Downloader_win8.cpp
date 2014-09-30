//
//  Downloader_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Downloader_win8.hpp"

void Downloader_win8::initialize(const G3MContext* context, FrameTasksExecutor* frameTasksExecutor){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void Downloader_win8::start(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void Downloader_win8::stop(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

long long Downloader_win8::requestBuffer(const URL& url, long long priority,
	const TimeInterval& timeToCache,
	bool readExpired,
	IBufferDownloadListener* listener,
	bool deleteListener){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

long long Downloader_win8::requestImage(const URL& url,
	long long priority,
	const TimeInterval& timeToCache,
	bool readExpired,
	IImageDownloadListener* listener,
	bool deleteListener){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void Downloader_win8::cancelRequest(long long requestId){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

const std::string Downloader_win8::statistics(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void Downloader_win8::removeDownloadingHandlerForUrl(const std::string url){

}