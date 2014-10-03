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

#include <ppltasks.h>

using namespace Windows::Storage::Streams;
using namespace Windows::Storage;
using namespace concurrency;

using namespace Windows::Web::Http;


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


//==================== ProcessResponse task implementation =========================================




class ProcessResponseGTask : public GTask {
private:
	const int    _statusCode;
	IByteBuffer*	 _buffer;
	const std::vector<Listener_win8_Entry*> _listeners;
	const URL*            _g3mURL;
	std::mutex _lock;

public:
	ProcessResponseGTask(const int statusCode,
		IByteBuffer* buffer,
		const std::vector<Listener_win8_Entry*> listeners,
		const URL* g3mURL) :
		_statusCode(statusCode),
		_buffer(buffer),
		_listeners(listeners),
		_g3mURL(g3mURL)
	{
	}

	void run(const G3MContext* context) {

		_lock.lock();

		const bool dataIsValid = (_buffer->size()>0) && (_statusCode == 200);
		if (!dataIsValid) {
			ILogger::instance()->logError("Error statusCode=%d, URL=%s \n", _statusCode, _g3mURL->getPath().c_str());
		}

		const int listenersCount = _listeners.size();

		if (dataIsValid) {
			for (int i = 0; i < listenersCount; i++) {
				Listener_win8_Entry* entry = _listeners.at(i);
				Downloader_win8_Listener* listener = entry->getListener();

				if (entry->isCanceled()) {
					listener->onCanceledDownload(*_g3mURL, _buffer);
					listener->onCancel(*_g3mURL);
				}
				else {
					listener->onDownload(*_g3mURL, _buffer);
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
};


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

void Downloader_win8_Handler::runWithDownloader(Downloader_win8* downloader, G3MContext* context){

	int statusCode = 0;
	//BYTE* data = NULL;
	IByteBuffer* byteBuffer = NULL;

	if (_g3mURL->isFileProtocol()) {
		const std::string fileFullName = _sUtils->replaceSubstring(_g3mURL->getPath(), URL::FILE_PROTOCOL, "");

		//Windows::Storage::FileIO::
		//Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
		//http://msdn.microsoft.com/en-us/library/windows/apps/windows.storage.applicationdata.localfolder

		concurrency::task<StorageFile^> readFileOperation(StorageFile::GetFileFromPathAsync(_sUtils->toStringHat(fileFullName)));
		readFileOperation.then([&byteBuffer](StorageFile^ file)
		{
			//return FileIO::ReadTextAsync(file);
			return FileIO::ReadBufferAsync(file);
		}).then([&byteBuffer](concurrency::task<Streams::IBuffer^> previousOperation) {

			try {
				// Data is contained in timestamp
				Streams::IBuffer^ buffer = previousOperation.get();
				//http://stackoverflow.com/questions/11853838/getting-an-array-of-bytes-out-of-windowsstoragestreamsibuffer

				auto reader = Windows::Storage::Streams::DataReader::FromBuffer(buffer);
				unsigned int dataLength = reader->UnconsumedBufferLength;

				if (dataLength > 0){
					BYTE* data = new BYTE[dataLength];
					reader->ReadBytes(Platform::ArrayReference<unsigned char>(data, dataLength));
					byteBuffer = IFactory::instance()->createByteBuffer(data, dataLength);
				}
			}
			catch (...) {
				ILogger::instance()->logInfo("Downloader error getting data from local file \n");
			}
		});
	}
	else{
		Windows::Web::Http::HttpClient^ client = ref new Windows::Web::Http::HttpClient();
		//Windows::Foundation::IAsyncOperationWithProgress<> getdataOperation 
		try {
			auto responseMessage = client->GetAsync(_winURL)->GetResults();
			//responseMessage->EnsureSuccessStatusCode();
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
		}
		catch (...) {
			ILogger::instance()->logInfo("Downloader error getting data from URL= %s \n", _g3mURL->getPath().c_str());
		}	
	}

	statusCode = (byteBuffer->size() > 0) ? 200 : 404;

	// inform downloader to remove myself, to avoid adding new Listener
	downloader->removeDownloadingHandlerForUrl(_g3mURL->getPath());

	context->getThreadUtils()->invokeInRendererThread(new ProcessResponseGTask(statusCode, byteBuffer, _listeners, _g3mURL), true);
}


void Downloader_win8_Handler::dealloc(){

	delete _g3mURL;
}

Downloader_win8_Handler::~Downloader_win8_Handler(){
	//TODO: ?
}
