//
//  Storage_win8.hpp
//  G3MWindowsSDK
//
//  Created by F. Pulido on 26/09/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.

#include "pch.h"
#include "Downloader_win8_WorkerThread.hpp"
#include "Downloader_win8.hpp"
#include "Downloader_win8_Handler.hpp"
//#include <WinBase.h>
//#include <Sched>
//#include <pthread.h>
//#include <ppltasks.h>
//#include <concrt.h>
//#include <concrtrm.h>

//using namespace Windows::System::Threading;
using namespace Windows::System;
//using namespace ::Concurrency;

Downloader_win8_WorkerThread::Downloader_win8_WorkerThread(Downloader_win8* downloader) :
	_downloader(downloader)
{
	_stopping = false;

	_workItem = ref new Threading::WorkItemHandler(
		[this](IAsyncAction^ workItem)
	{
		this->createTask();
	}, Platform::CallbackContext::Any);
}


concurrency::task<void>  Downloader_win8_WorkerThread::createTask(){

	concurrency::task<void> thisTask = concurrency::create_task([this]
	{
		this->run();
	});

	return thisTask;
}

void Downloader_win8_WorkerThread::start(){

	//std::thread _worker(&Downloader_win8_WorkerThread::run, this);
	//_worker.detach();

	/*_workItem = ref new Threading::WorkItemHandler(
		[this](IAsyncAction^ workItem)
		{
			this->doRun();
	}, Platform::CallbackContext::Any);*/

	_workerThread = Threading::ThreadPool::RunAsync(_workItem, Threading::WorkItemPriority::Low);

	//this->doRun();
}

void Downloader_win8_WorkerThread::stop(){

	_lock.lock();
	_stopping = true;
	_lock.unlock();
}

bool Downloader_win8_WorkerThread::isStopping(){

	_lock.lock();
	const bool result = _stopping;
	_lock.unlock();
	return result;
}


void Downloader_win8_WorkerThread::run(){

	while (!isStopping()) {

		Downloader_win8_Handler* handler = this->_downloader->getHandlerToRun();
		if (handler) {
			//ILogger::instance()->logInfo("Executing Downloader_win8_WorkerThread in thread runwithDownloader. Thread: %d", _workerThread->Id);
			//ILogger::instance()->logInfo("Executing Downloader_win8_WorkerThread in thread: %d",std::this_thread::get_id());
			handler->runWithDownloader(this->_downloader);
		}
		else {
			// sleep for 25 milliseconds
			//ILogger::instance()->logInfo("Sleeping Downloader_win8_WorkerThread in thread: %d", _workerThread->Id);
			//ILogger::instance()->logInfo("Sleeping Downloader_win8_WorkerThread in thread: %d", std::this_thread::get_id());
			std::this_thread::sleep_for(std::chrono::milliseconds(25)); //TODO: is this a valid option in Windows Store apps??
			//--http://blogs.msdn.com/b/devosaure/archive/2012/03/23/asynchronisme-et-scenarios-hybrides-avec-le-nouveau-runtime-windows-winrt.aspx
		}
	}
}

Downloader_win8_WorkerThread::~Downloader_win8_WorkerThread(){
	//TODO: ?
	_workerThread->Close();
	delete _workerThread;
	delete _workItem;
}
