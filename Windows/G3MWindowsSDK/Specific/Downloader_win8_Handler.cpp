//
//  Storage_win8.hpp
//  G3MWindowsSDK
//
//  Created by F. Pulido on 26/09/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "pch.h"
#include "Downloader_win8_Handler.hpp"
#include "Downloader_win8.hpp"
#include "ILogger.hpp"
#include "StringUtils_win8.hpp"


//===================== class Listener_win8_Entry implementation ========================================

Listener_win8_Entry::Listener_win8_Entry(Downloader_win8_Listener* listener, long long requestId):
	_listener(listener),
	_requestId(requestId)
{
	_canceled = false;
}

long long Listener_win8_Entry::getRequestId(){
	return _requestId;
}

void Listener_win8_Entry::cancel(){

	if (_canceled) {
		ILogger::instance()->logInfo("Listener for RequestId = %d already canceled \n", _requestId);
	}
	_canceled = true;
}

bool Listener_win8_Entry::isCanceled(){
	return _canceled;
}

Downloader_win8_Listener* Listener_win8_Entry::getListener(){
	return _listener;
}

Listener_win8_Entry::~Listener_win8_Entry(){
	//TODO: ?
}


//==================== class Downloader_win8_Handler implementation =========================================

Downloader_win8_Handler::Downloader_win8_Handler(URL* url, Downloader_win8_Listener* listener, long long priority, long long requestId):
_g3mURL(url),
_priority(priority)
{
	_sUtils = (StringUtils_win8*)IStringUtils::instance();
	_winURL = ref new Uri(_sUtils->toStringHat(url->getPath()));
	_listeners.push_back(new Listener_win8_Entry(listener, priority));
}


void Downloader_win8_Handler::addListener(Downloader_win8_Listener* listener, long long priority, long long requestId){

	//[_lock lock]; TODO:

	_listeners.push_back(new Listener_win8_Entry(listener, requestId));

	if (priority > _priority) {
		_priority = priority;
	}

	//[_lock unlock];
}

bool Downloader_win8_Handler::cancelListenerForRequestId(long long requestId){

	bool canceled = false;

	//[_lock lock]; TODO:

	const int listenersCount = _listeners.size();
	for (int i = 0; i < listenersCount; i++) {
		Listener_win8_Entry* entry = _listeners.at(i);
		if (entry->getRequestId() == requestId){
			entry->cancel();
			canceled = true;
			break;
		}
	}

	//[_lock unlock];

	return canceled;
}

bool Downloader_win8_Handler::removeListenerForRequestId(long long requestId){

	bool removed = false;

	//[_lock lock]; TODO:

	const int listenersCount = _listeners.size();
	for (int i = 0; i < listenersCount; i++) {
		Listener_win8_Entry* entry = _listeners.at(i);
		if (entry->getRequestId() == requestId){
			entry->getListener()->onCancel(*_g3mURL);
			_listeners.erase(_listeners.begin()+i);
			removed = true;
			break;
		}
	}

	//[_lock unlock];

	return removed;
}

bool Downloader_win8_Handler::hasListeners(){

	//[_lock lock]; TODO:

	const bool hasListeners = _listeners.size() > 0;

	//[_lock unlock];

	return hasListeners;
}

long long Downloader_win8_Handler::priority(){

	//[_lock lock]; TODO:

	const long long result = _priority;

	//[_lock unlock];

	return result;
}

void Downloader_win8_Handler::runWithDownloader(Downloader_win8* downloader){

	if (_g3mURL->isFileProtocol()) {

	}
	else{

	}
}

void Downloader_win8_Handler::dealloc(){

	delete _g3mURL;
}

Downloader_win8_Handler::~Downloader_win8_Handler(){
	//TODO: ?
}
