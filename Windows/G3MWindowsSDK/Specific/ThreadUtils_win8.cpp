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
#include <ppltasks.h>

using namespace Windows::System;
using namespace Windows::Foundation;


void ThreadUtils_win8::initialize(const G3MContext* context){
	_context = context;
	ILogger::instance()->logInfo("ThreadUtils_win8 initialized \n");
}

//void ThreadUtils_win8::invokeInRendererThread(GTask* task, bool autoDelete) const {
//	
//	auto workItem = ref new Threading::WorkItemHandler(
//		[this, task, autoDelete](IAsyncAction^ workItem)
//	{
//		concurrency::create_task([this, task]
//		{
//			task->run(this->_context);
//		});
//
//		if (autoDelete) {
//			delete task;
//		}
//	});
//
//	auto workerThread = Threading::ThreadPool::RunAsync(workItem, Threading::WorkItemPriority::Normal);
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

void ThreadUtils_win8::invokeInRendererThread(GTask* task, bool autoDelete) const {

	auto workItem = ref new Threading::WorkItemHandler(
		[this, task, autoDelete](IAsyncAction^ workItem)
	{
		task->run(this->_context);

		if (autoDelete) {
			delete task;
		}
	});

	auto workerThread = Threading::ThreadPool::RunAsync(workItem, Threading::WorkItemPriority::Normal);
}

void ThreadUtils_win8::invokeInBackground(GTask* task, bool autoDelete) const {

	auto workItem = ref new Threading::WorkItemHandler(
		[this, task, autoDelete](IAsyncAction^ workItem)
	{
		concurrency::create_task([this, task]
		{
			task->run(this->_context);
		});
		
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