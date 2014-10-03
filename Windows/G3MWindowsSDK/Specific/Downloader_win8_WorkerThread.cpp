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

using namespace Windows::System::Threading;



bool Downloader_win8_WorkerThread::setPriorityBellowNormal(){

	
	//thread::native_handle_type nhandlet= this->native_handle();
	//std::this_thread::

	//std::thread::native_handle handle = _worker.native_handle();
	//_worker.native_handle();

	//std::thread::native_handle handle = this->native_handle();
	/*::Concurrency::details::
	::Concurrency::details::_GetConcurrency()
	::Concurrency::details::_Context::
	::Concurrency::details::_Scheduler::
	::Concurrency::details::_CurrentScheduler::*/
	
	
}


Downloader_win8_WorkerThread::Downloader_win8_WorkerThread(Downloader_win8* downloader) :
	_downloader(downloader)
{
	_stopping = false;
}


void Downloader_win8_WorkerThread::start(){

	//std::thread(this->run());
	std::thread _worker(&Downloader_win8_WorkerThread::run, this);
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
		Downloader_win8_Handler* handler = _downloader->getHandlerToRun();
		if (handler) {
			handler->runWithDownloader(_downloader);
		}
		else {
			// sleep for 25 milliseconds
			std::this_thread::sleep_for(std::chrono::milliseconds(25));
		}
	}
}

Downloader_win8_WorkerThread::~Downloader_win8_WorkerThread(){
	//TODO: ?
}
