//
//  Downloader_win8.cpp
//  G3MWindowsSDK
//
//  Created by F. Pulido on 26/09/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Downloader_win8.hpp"
#include "Downloader_win8_WorkerThread.hpp"
#include "Downloader_win8_Handler.hpp"
#include "Downloader_win8_Listener.hpp"
#include "IStringBuilder.hpp"


Downloader_win8::Downloader_win8(int maxConcurrentOperationCount) :
	_maxConcurrentOperationCount(maxConcurrentOperationCount),
	_requestIdCounter(1),
	_requestsCounter(0),
	_cancelsCounter(0),
	_started(false)
{
	_workers = std::vector<Downloader_win8_WorkerThread*>();
	_workers.reserve(maxConcurrentOperationCount);
	_downloadingHandlers = std::map<std::string, Downloader_win8_Handler*>();
	_queuedHandlers = std::map<std::string, Downloader_win8_Handler*>();

	/*for (int i = 0; i < maxConcurrentOperationCount; i++){
		_workers.push_back(new Downloader_win8_WorkerThread(this));
	}*/
}

void Downloader_win8::initialize(const G3MContext* context, FrameTasksExecutor* frameTasksExecutor){

	//std::string errMsg("TODO: Implementation of Downloader_win8::initialize ??");
	//throw std::exception(errMsg.c_str());
	//ILogger::instance()->logInfo("Downloader_win8.initialize.. do nothing by the moment."); // TODO: remove
	for (int i = 0; i < _maxConcurrentOperationCount; i++){
		_workers.push_back(new Downloader_win8_WorkerThread(this));
	}
}

long long  Downloader_win8::request(const URL &url,
	long long priority,
	Downloader_win8_Listener* listener){

	ILogger::instance()->logInfo("requested URL: \"%s\"\n", url._path.c_str());
	if (url.isNull() || (url.getPath().find("NULL") != std::string::npos)){
		ILogger::instance()->logError("URL is NULL.");
		return -1;
	}

	std::string path = url.getPath();
	Downloader_win8_Handler* handler = NULL;

	_lock.lock();

	_requestsCounter++;

	const long long requestId = _requestIdCounter++;
	
	if (_downloadingHandlers.count(path)>0){
		handler = _downloadingHandlers.at(path);
	}

	if (handler) {
		// the URL is being downloaded, just add the new listener.
		handler->addListener(listener, priority, requestId);
	}
	else {
		if (_queuedHandlers.count(path) > 0){
			handler = _queuedHandlers.at(path);
		}
		if (handler) {
			// the URL is queued for future download, just add the new listener.
			handler->addListener(listener, priority, requestId);
		}
		else {
			// new handler and queue it
			handler = new Downloader_win8_Handler(new URL(url), listener, priority, requestId);
			_queuedHandlers.insert(std::pair<std::string, Downloader_win8_Handler*>(path, handler));
		}
	}

	_lock.unlock();

	return requestId;
}

void Downloader_win8::start(){

	if (!_started) {
		for (int i = 0; i < _workers.size(); i++) {
			_workers.at(i)->start();
		}
		_started = true;
	}
}

void Downloader_win8::stop(){

	if (_started) {
		for (int i = 0; i < _workers.size(); i++) {
			_workers.at(i)->stop();
		}
		_started = false;
	}
}

long long Downloader_win8::requestBuffer(const URL& url, 
										 long long priority,
										 const TimeInterval& timeToCache,
										 bool readExpired,
										 IBufferDownloadListener* listener,
										 bool deleteListener){

	Downloader_win8_Listener* win8Listener = new Downloader_win8_Listener(listener, deleteListener);
	return request(url, priority, win8Listener);
}

long long Downloader_win8::requestImage(const URL& url,
										long long priority,
										const TimeInterval& timeToCache,
										bool readExpired,
										IImageDownloadListener* listener,
										bool deleteListener){

	Downloader_win8_Listener* win8Listener = new Downloader_win8_Listener(listener, deleteListener);
	return request(url, priority, win8Listener);
}

void Downloader_win8::cancelRequest(long long requestId){
	
	if (requestId < 0) {
		return;
	}

	_lock.lock();

	_cancelsCounter++;

	bool found = false;

	std::map<std::string, Downloader_win8_Handler*>::iterator it = _queuedHandlers.begin();

	for (it = _queuedHandlers.begin(); it != _queuedHandlers.end() && !found; ++it){
		std::string url = it->first;
		Downloader_win8_Handler* handler = it->second;

		if (handler->removeListenerForRequestId(requestId)) {

			if (!handler->hasListeners()) {
				_queuedHandlers.erase(url);
			}
			found = true;
		}
	}

	if (!found) {
		it = _downloadingHandlers.begin();

		for (it = _downloadingHandlers.begin(); it != _downloadingHandlers.end() && !found; ++it){
			Downloader_win8_Handler* handler = it->second;
			if (handler->cancelListenerForRequestId(requestId)) {
				found = true;
			}
		}
	}

	_lock.unlock();
}


Downloader_win8_Handler* Downloader_win8::getHandlerToRun() {

	long long					selectedPriority = -100000000; // TODO: LONG_MIN_VALUE;
	Downloader_win8_Handler*	selectedHandler = NULL;
	std::string					selectedURL;
	
	_lock.lock();

	std::map<std::string, Downloader_win8_Handler*>::iterator it = _queuedHandlers.begin();

	for (it = _queuedHandlers.begin(); it != _queuedHandlers.end(); ++it){
		std::string url = it->first;
		Downloader_win8_Handler* handler = it->second;
		const long long priority = handler->priority();

		if (priority > selectedPriority) {
			selectedPriority = priority;
			selectedHandler = handler;
			selectedURL = url;
		}
	}

	if (selectedHandler) {
		// move the selected handler to _downloadingHandlers collection
		_queuedHandlers.erase(selectedURL);
		_downloadingHandlers.insert(std::pair<std::string, Downloader_win8_Handler*>(selectedURL, selectedHandler));
	}

	_lock.unlock();

	return selectedHandler;

}


void Downloader_win8::removeDownloadingHandlerForUrl(const std::string url){

	_lock.lock();
	_downloadingHandlers.erase(url);
	_lock.unlock();
}


const std::string Downloader_win8::statistics(){

	IStringBuilder* isb = IStringBuilder::newStringBuilder();
	isb->addString("Downloader_win8(downloading=");
	isb->addInt(_downloadingHandlers.size());
	isb->addString(", queued=");
	isb->addInt(_queuedHandlers.size());
	isb->addString(", totalRequests=");
	isb->addLong(_requestsCounter);
	isb->addString(", totalCancels=");
	isb->addLong(_cancelsCounter);
	const std::string s = isb->getString();
	delete isb;
	return s;
}


Downloader_win8::~Downloader_win8(){

	stop();
}