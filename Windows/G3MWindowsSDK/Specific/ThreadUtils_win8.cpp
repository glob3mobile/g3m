//
//  ThreadUtils_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//
//

#include "ThreadUtils_win8.hpp"
#include "ILogger.hpp"
#include <string>
#include "StringUtils_win8.hpp"
#include <ppltasks.h>
//#include <concrt.h>


using namespace Windows::System;
using namespace Windows::Foundation;


void ThreadUtils_win8::initialize(const G3MContext* context){
	_context = context;

	ILogger::instance()->logInfo("ThreadUtils_win8 initialized \n");
	//_backgroundQueue = std::queue<Threading::WorkItemHandler^>();
	_running = true;
}



void ThreadUtils_win8::invokeInRendererThread(GTask* task, bool autoDelete) const {
	//--http://msdn.microsoft.com/en-us/library/windows/apps/hh750290.aspx
	//--If code must be executed on the UI thread (for example, if you need to call an object on the UI thread), 
	//-- use the CoreDispatcher to dispatch the call to the UI thread. 

	//-- run in ASTA (Application Single threaded apartment)
	auto UIDispacher = Windows::UI::Core::CoreWindow::GetForCurrentThread()->Dispatcher;
	//auto currentDispacher = Windows::ApplicationModel::Core::CoreApplication::GetCurrentView()->CoreWindow->Dispatcher;
	//auto currentDispacher = Windows::ApplicationModel::Core::CoreApplication::MainView->CoreWindow->Dispatcher;
	UIDispacher->RunAsync(Windows::UI::Core::CoreDispatcherPriority::Normal,
		ref new Windows::UI::Core::DispatchedHandler([this, task, autoDelete]()
	{
		task->run(this->_context);
		if (autoDelete) {
			delete task;
		}
	}));
}


void ThreadUtils_win8::invokeInBackground(GTask* task, bool autoDelete) const {
	// run as a MTA (Multi-threaded apartment)
	//create new thread in the threadPool at low priority
	auto workItem = ref new Threading::WorkItemHandler(
		[this, task, autoDelete](IAsyncAction^ workItem)
	{
		task->run(this->_context);

		if (autoDelete) {
			delete task;
		}
	}, Platform::CallbackContext::Any);

	auto workerThread = Threading::ThreadPool::RunAsync(workItem, Threading::WorkItemPriority::Low);
}



void ThreadUtils_win8::onResume(const G3MContext* context){
	// only for android
}

void ThreadUtils_win8::onPause(const G3MContext* context){
	// only for android
}

void ThreadUtils_win8::onDestroy(const G3MContext* context){
	// only for android
}




//=================================================================================
//==					TESTS
//=================================================================================

//void ThreadUtils_win8::initialize(const G3MContext* context){
//	_context = context;
//
//	ILogger::instance()->logInfo("ThreadUtils_win8 initialized \n");
//	//_backgroundQueue = std::queue<Threading::WorkItemHandler^>();
//	_running = true;
//
//
//	/*concurrency::create_task([this]
//	{
//	this->executeInBackground();
//	});*/
//
//	//auto workItem = ref new Threading::WorkItemHandler(
//	//	[this](IAsyncAction^ workItem)
//	//{
//	//	auto tarea = concurrency::create_task([this]
//	//	{
//	//		//_dispatcher = Windows::UI::Core::CoreWindow::GetForCurrentThread()->Dispatcher;
//	//		this->runInBackground();
//	//	//});
//	//	}, concurrency::task_continuation_context::use_arbitrary());
//	//	//concurrency::task_group pepe = concurrency::task_group::task_group();
//	//
//	//}, Platform::CallbackContext::Any);
//	//
//	//auto workerThread = Threading::ThreadPool::RunAsync(workItem, Threading::WorkItemPriority::Low);
//}

//void ThreadUtils_win8::invokeInRendererThread(GTask* task, bool autoDelete) const {
//
//	try{
//		auto workItem = ref new Threading::Core::PreallocatedWorkItem(
//			ref new Threading::WorkItemHandler([this, task, autoDelete](IAsyncAction^ workItem)
//		{
//			//auto currentDispacher = Windows::UI::Core::CoreWindow::GetForCurrentThread()->Dispatcher;
//			//auto currentDispacher = Windows::ApplicationModel::Core::CoreApplication::GetCurrentView()->CoreWindow->Dispatcher;
//			auto currentDispacher = Windows::ApplicationModel::Core::CoreApplication::MainView->CoreWindow->Dispatcher;
//			currentDispacher->RunAsync(Windows::UI::Core::CoreDispatcherPriority::Normal,
//				ref new Windows::UI::Core::DispatchedHandler([this, task]()
//			{
//				task->run(this->_context);
//			}));
//
//			if (autoDelete) {
//				delete task;
//			}
//		}));
//		
//		if (workItem){
//			try{
//				workItem->RunAsync();
//			}
//			catch (Platform::Exception ^ ex)
//			{
//				ILogger::instance()->logError("Pre-allocated ThreadUtils_win8::invokeInRendererThread item was already submitted.");
//			}
//		}
//	}
//	catch (Platform::Exception ^ ex)
//	{
//		ILogger::instance()->logError("Pre-allocated ThreadUtils_win8::invokeInRendererThread item could not be created.");
//	}
//}

//void ThreadUtils_win8::invokeInRendererThread(GTask* task, bool autoDelete) const {
//
//	auto workItem = ref new Threading::WorkItemHandler(
//		[this, task, autoDelete](IAsyncAction^ workItem)
//	{
//		task->run(this->_context);
//
//		if (autoDelete) {
//			delete task;
//		}
//	});
//
//	auto workerThread = Threading::ThreadPool::RunAsync(workItem, Threading::WorkItemPriority::Normal);
//}

//void ThreadUtils_win8::invokeInBackground(GTask* task, bool autoDelete) const {
//	
//	auto workItem = ref new Threading::WorkItemHandler(
//		[this, task, autoDelete](IAsyncAction^ workItem)
//	{
//		concurrency::create_task([this, task, autoDelete]
//		{
//			task->run(this->_context);
//			if (autoDelete) {
//				delete task;
//			}
//		});
//		
//	}, Platform::CallbackContext::Any);
//
//	auto workerThread = Threading::ThreadPool::RunAsync(workItem, Threading::WorkItemPriority::Low);
//}

//void ThreadUtils_win8::invokeInBackground(GTask* task, bool autoDelete) const {
//	//TODO: crear una sola task y una cola de procesos que debe mirar continuamente.
//	auto workItem = ref new Threading::WorkItemHandler(
//		[this, task, autoDelete](IAsyncAction^ workItem)
//	{
//		task->run(this->_context);
//		if (autoDelete) {
//			delete task;
//		}
//	});
//	
//	//_lock.lock();
//	
//	if (_backgroundQueue->empty()){
//		auto workerThread = Threading::ThreadPool::RunAsync(workItem, Threading::WorkItemPriority::Low);
//	}
//	else{
//		_backgroundQueue->push(workItem);
//	}
//}

//void ThreadUtils_win8::invokeInBackground(GTask* task, bool autoDelete) const {
//
//	auto currentDispacher = Windows::UI::Core::CoreWindow::GetForCurrentThread()->Dispatcher;
//	//auto currentDispacher = Windows::ApplicationModel::Core::CoreApplication::GetCurrentView()->CoreWindow->Dispatcher;
//	//auto currentDispacher = Windows::ApplicationModel::Core::CoreApplication::MainView->CoreWindow->Dispatcher;
//	currentDispacher->RunAsync(Windows::UI::Core::CoreDispatcherPriority::Normal,
//		ref new Windows::UI::Core::DispatchedHandler([this, task, autoDelete]()
//	{
//		task->run(this->_context);
//		if (autoDelete) {
//			delete task;
//		}
//	}));
//	currentDispacher->ProcessEvents(Windows::UI::Core::CoreProcessEventsOption::ProcessAllIfPresent);
//}


//void ThreadUtils_win8::invokeInBackground(GTask* task, bool autoDelete) const {
//
//	_lock->lock();
//	_backgroundQueue->push(std::pair<GTask*, bool>(task, autoDelete));
//	_lock->unlock();
//}

//void ThreadUtils_win8::invokeInBackground(GTask* task, bool autoDelete) const {
//
//	//auto currentDispacher = Windows::UI::Core::CoreWindow::GetForCurrentThread()->Dispatcher;
//	//auto currentDispacher = Windows::ApplicationModel::Core::CoreApplication::GetCurrentView()->CoreWindow->Dispatcher;
//	//auto currentDispacher = Windows::ApplicationModel::Core::CoreApplication::MainView->CoreWindow->Dispatcher;
//	if (_dispatcher){
//		_dispatcher->RunAsync(Windows::UI::Core::CoreDispatcherPriority::Low,
//			ref new Windows::UI::Core::DispatchedHandler([this, task, autoDelete]()
//		{
//			task->run(this->_context);
//			if (autoDelete) {
//				delete task;
//			}
//		}));
//	}
//}

//void ThreadUtils_win8::runInBackground() const {
//
//	while (_running){
//		if (!_backgroundQueue->empty()){
//			Threading::WorkItemHandler^ workItem = _backgroundQueue->front();
//			auto workerThread = Threading::ThreadPool::RunAsync(workItem, Threading::WorkItemPriority::Low);
//			_backgroundQueue->pop();
//		}
//	}
//}

//void ThreadUtils_win8::runInBackground() const {
//
//	while (_running){
//		std::pair<GTask*, bool>* data = NULL;
//		_lock->lock();
//		if (!_backgroundQueue->empty()){
//			*data = _backgroundQueue->front();
//			_backgroundQueue->pop();
//		}
//		_lock->unlock();
//
//		if (data){
//			GTask* task = data->first;
//			bool autoDelete = data->second;
//			task->run(this->_context);
//			if (autoDelete) {
//				delete task;
//			}
//		}
//	}
//}

//void ThreadUtils_win8::runInBackground() const {
//
//	while (_running){
//		_dispatcher = Windows::UI::Core::CoreWindow::GetForCurrentThread()->Dispatcher;
//		if (_dispatcher){
//			_dispatcher->ProcessEvents(Windows::UI::Core::CoreProcessEventsOption::ProcessOneAndAllPending);
//		}
//	}
//}