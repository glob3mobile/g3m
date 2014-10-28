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
#include "Context.hpp"
#include "GTask.hpp"
#include "IThreadUtils.hpp"
#include "IFactory.hpp"
#include "IByteBuffer.hpp"
#include <thread>
#include <ppltasks.h>

using namespace Windows::Storage::Streams;
using namespace Windows::Storage;
using namespace concurrency;

using namespace Windows::Web;
using namespace Windows::System;


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
	//delete _listener;
}



//==================== ProcessResponse task implementation =========================================

//class ProcessResponseGTask : public GTask {
//private:
//	const int    _statusCode;
//	IByteBuffer*	 _buffer;
//	const std::vector<Listener_win8_Entry*> _listeners;
//	const URL*            _g3mURL;
//	std::mutex _lock;
//
//public:
//	ProcessResponseGTask(const int statusCode,
//		IByteBuffer* buffer,
//		const std::vector<Listener_win8_Entry*> listeners,
//		const URL* g3mURL) :
//		_statusCode(statusCode),
//		_buffer(buffer),
//		_listeners(listeners),
//		_g3mURL(g3mURL)
//	{
//	}
//
//	void run(const G3MContext* context) {
//
//		_lock.lock();
//
//		const bool dataIsValid = (_buffer->size()>0) && (_statusCode == 200);
//		if (!dataIsValid) {
//			ILogger::instance()->logError("Error statusCode=%d, URL=%s \n", _statusCode, _g3mURL->getPath().c_str());
//		}
//
//		const int listenersCount = _listeners.size();
//
//		if (dataIsValid) {
//			for (int i = 0; i < listenersCount; i++) {
//				Listener_win8_Entry* entry = _listeners.at(i);
//				Downloader_win8_Listener* listener = entry->getListener();
//
//				if (entry->isCanceled()) {
//					listener->onCanceledDownload(*_g3mURL, _buffer);
//					listener->onCancel(*_g3mURL);
//				}
//				else {
//					listener->onDownload(*_g3mURL, _buffer);
//				}
//			}
//		}
//		else {
//			for (int i = 0; i < listenersCount; i++) {
//				Listener_win8_Entry* entry = _listeners.at(i);
//				Downloader_win8_Listener* listener = entry->getListener();
//				listener->onError(*_g3mURL);
//			}
//		}
//
//		_lock.unlock();
//	}
//};


//==================== class Downloader_win8_Handler implementation =========================================

Platform::String^ getLocalPath(Platform::String^ name){

	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
	Platform::String^ folderPath = localFolder->Path;
	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
	return Platform::String::Concat(tmpPath, name);
}

Downloader_win8_Handler::Downloader_win8_Handler(URL* url, Downloader_win8_Listener* listener, long long priority, long long requestId):
_g3mURL(url),
_priority(priority)
{
	_sUtils = (StringUtils_win8*)IStringUtils::instance();
	if (url->isFileProtocol()){
		std::string filePath = url->getPath().substr(URL::FILE_PROTOCOL.length());
		Platform::String^ localPath = getLocalPath(_sUtils->toStringHat(filePath));
		//ILogger::instance()->logInfo("local path: %s", _sUtils->toStringStd(localPath).c_str());
		_winURL = ref new Uri(localPath);
	}
	else{
		_winURL = ref new Uri(_sUtils->toStringHat(url->getPath()));
	}
	_listeners.push_back(new Listener_win8_Entry(listener, requestId));
}


void Downloader_win8_Handler::addListener(Downloader_win8_Listener* listener, long long priority, long long requestId){

	_lock.lock();

	_listeners.push_back(new Listener_win8_Entry(listener, requestId));

	if (priority > _priority) {
		_priority = priority;
	}

	_lock.unlock();
}

bool Downloader_win8_Handler::cancelListenerForRequestId(long long requestId){

	bool canceled = false;

	_lock.lock();

	const int listenersCount = _listeners.size();
	for (int i = 0; i < listenersCount; i++) {
		Listener_win8_Entry* entry = _listeners.at(i);
		if (entry->getRequestId() == requestId){
			entry->cancel();
			canceled = true;
			break;
		}
	}

	_lock.unlock();

	return canceled;
}

bool Downloader_win8_Handler::removeListenerForRequestId(long long requestId){

	bool removed = false;

	_lock.lock();

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

	_lock.unlock();

	return removed;
}

bool Downloader_win8_Handler::hasListeners(){

	_lock.lock();

	const bool hasListeners = _listeners.size() > 0;

	_lock.unlock();

	return hasListeners;
}

long long Downloader_win8_Handler::priority(){

	_lock.lock();

	const long long result = _priority;

	_lock.unlock();

	return result;
}



void Downloader_win8_Handler::runWithDownloader(Downloader_win8* downloader){

	if (_g3mURL->isFileProtocol()) {

		std::string filePath = _g3mURL->getPath().substr(URL::FILE_PROTOCOL.length());
		Platform::String^ fileLocalPath = getLocalPath(_sUtils->toStringHat(filePath));

		//--http://msdn.microsoft.com/en-us/library/windows/apps/windows.storage.applicationdata.localfolder

		concurrency::task<StorageFile^> readFileOperation(StorageFile::GetFileFromPathAsync(fileLocalPath), concurrency::task_continuation_context::use_arbitrary());
		readFileOperation.then([this, downloader](StorageFile^ file)
		{
			//ILogger::instance()->logInfo("Executing read File protocol GetFileFromPathAsync in thread: %d", std::this_thread::get_id());
			return FileIO::ReadBufferAsync(file);
		}).then([this, downloader](concurrency::task<Streams::IBuffer^> previousOperation) {

			try {
				int statusCode = 0;
				IByteBuffer* byteBuffer = NULL;
				//ILogger::instance()->logInfo("Executing read File protocol previousOperation in thread: %d", std::this_thread::get_id());
				//previousOperation.wait();
				Streams::IBuffer^ buffer = previousOperation.get();
				//--http://stackoverflow.com/questions/11853838/getting-an-array-of-bytes-out-of-windowsstoragestreamsibuffer

				auto reader = Windows::Storage::Streams::DataReader::FromBuffer(buffer);
				unsigned int dataLength = reader->UnconsumedBufferLength;

				if (dataLength > 0){
					BYTE* data = new BYTE[dataLength];
					reader->ReadBytes(Platform::ArrayReference<unsigned char>(data, dataLength));
					byteBuffer = IFactory::instance()->createByteBuffer(data, dataLength);
				}
				statusCode = ((byteBuffer != NULL) && (byteBuffer->size() > 0)) ? 200 : 404;

				// inform downloader to remove myself, to avoid adding new Listener
				downloader->removeDownloadingHandlerForUrl(this->_g3mURL->getPath());
				this->runResponseTask(statusCode, byteBuffer); //TODO: make sense to start another thread if is running in async task???
				//std::thread th(&Downloader_win8_Handler::runResponseTask, this, statusCode, byteBuffer);
				//th.detach();
			}
			catch (...) {
				ILogger::instance()->logError("Downloader error getting data from local file: %s \n", _g3mURL->getPath().c_str());
				int statusCode = 404;
				downloader->removeDownloadingHandlerForUrl(this->_g3mURL->getPath());
				this->runResponseTask(statusCode, NULL);
			}
		}).then([=](concurrency::task<void> t) // Exception handler task
		{ //--http://stackoverflow.com/questions/15119897/exception-handling-winrt-c-concurrency-async-tasks
		  //--http://msdn.microsoft.com/en-us/library/windows/apps/hh780559.aspx
			try {
				t.get();
				// .get() didn't throw, so we succeeded.
			}
			catch (...) { // (Platform::COMException^ e
				// handle error
				ILogger::instance()->logError("Downloader error getting data from local file: %s \n", _g3mURL->getPath().c_str());
				int statusCode = 404;
				downloader->removeDownloadingHandlerForUrl(this->_g3mURL->getPath());
				this->runResponseTask(statusCode, NULL);
			}
		});
	}
	else{
		//ILogger::instance()->logInfo("Haciendo peticion a HttpClient..");
		Http::HttpClient^ client = ref new Http::HttpClient();

		//auto responseMessage = client->GetAsync(_winURL)->GetResults();
		concurrency::task<Http::HttpResponseMessage^> getDataAsyncOperation(client->GetAsync(_winURL), concurrency::task_continuation_context::use_arbitrary());
		getDataAsyncOperation.then([this, downloader](Http::HttpResponseMessage^ responseMessage)
		{
			try {
				//ILogger::instance()->logInfo("Executing read http response in thread: %d", std::this_thread::get_id());
				int statusCode = 0;
				IByteBuffer* byteBuffer = NULL;
				if (responseMessage->IsSuccessStatusCode){
					auto content = responseMessage->Content;
					Streams::IBuffer^ buffer = content->ReadAsBufferAsync()->GetResults();

					auto reader = Windows::Storage::Streams::DataReader::FromBuffer(buffer);
					unsigned int dataLength = reader->UnconsumedBufferLength;

					if (dataLength > 0){
						BYTE* data = new BYTE[dataLength];
						reader->ReadBytes(Platform::ArrayReference<unsigned char>(data, dataLength));
						byteBuffer = IFactory::instance()->createByteBuffer(data, dataLength);
					}
				}

				statusCode = ((byteBuffer != NULL) && (byteBuffer->size() > 0)) ? 200 : 404;

				// inform downloader to remove myself, to avoid adding new Listener
				downloader->removeDownloadingHandlerForUrl(this->_g3mURL->getPath());
				this->runResponseTask(statusCode, byteBuffer);
			}
			catch (...) {
				ILogger::instance()->logError("Downloader error getting data from URL= %s \n", _g3mURL->getPath().c_str());
				int statusCode = 404;
				downloader->removeDownloadingHandlerForUrl(this->_g3mURL->getPath());
				this->runResponseTask(statusCode, NULL);
			}
		}).then([=](concurrency::task<void> t) // Exception handler task
		{ //--http://stackoverflow.com/questions/15119897/exception-handling-winrt-c-concurrency-async-tasks
		  //--http://msdn.microsoft.com/en-us/library/windows/apps/hh780559.aspx
			try {
					t.get();
				// .get() didn't throw, so we succeeded.
			}
			catch (...) { // (Platform::COMException^ e
				// handle error
				ILogger::instance()->logError("Downloader error. Unnable to retrieve data from URL= %s \n", _g3mURL->getPath().c_str());
				int statusCode = 404;
				downloader->removeDownloadingHandlerForUrl(this->_g3mURL->getPath());
				this->runResponseTask(statusCode, NULL);
			}
		});
	}
}


void Downloader_win8_Handler::runResponseTask(const int statusCode, IByteBuffer* buffer) {

	_lock.lock();

	const bool dataIsValid = (statusCode == 200) && ((buffer != NULL) && (buffer->size()>0));
	if (!dataIsValid) {
		ILogger::instance()->logError("Error statusCode=%d, URL=%s \n", statusCode, _g3mURL->getPath().c_str());
	}

	const int listenersCount = _listeners.size();

	if (dataIsValid) {
		for (int i = 0; i < listenersCount; i++) {
			Listener_win8_Entry* entry = _listeners.at(i);
			Downloader_win8_Listener* listener = entry->getListener();

			if (entry->isCanceled()) {
				listener->onCanceledDownload(*_g3mURL, buffer);
				listener->onCancel(*_g3mURL);
			}
			else {
				listener->onDownload(*_g3mURL, buffer);
			}
		}
	}
	else {
		for (int i = 0; i < listenersCount; i++) {
			Listener_win8_Entry* entry = _listeners.at(i);
			Downloader_win8_Listener* listener = entry->getListener();
			listener->onError(*_g3mURL);
		}
	}

	_lock.unlock();
}

void Downloader_win8_Handler::dealloc(){

	delete _g3mURL;
}

Downloader_win8_Handler::~Downloader_win8_Handler(){
	//TODO: ?
	delete _g3mURL;
	delete _winURL;
	delete _sUtils;
}
