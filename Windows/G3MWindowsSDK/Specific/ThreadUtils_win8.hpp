//
//  ThreadUtils_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//
//

#ifndef __G3MWindowsSDK_ThreadUtils_win8__
#define __G3MWindowsSDK_ThreadUtils_win8__

#include "IThreadUtils.hpp"
#include <queue> 
#include <mutex> 

using namespace Windows::System;

class ThreadUtils_win8 : public IThreadUtils{

private:
	const G3MContext* _context = NULL;
	bool _running = false;
	//std::queue<Threading::WorkItemHandler^>* const _backgroundQueue = new std::queue<Threading::WorkItemHandler^>();
	//std::queue<std::pair<GTask*, bool>>* const _backgroundQueue = new std::queue<std::pair<GTask*, bool>>();
	//std::mutex* const _lock = new std::mutex();
	//mutable Windows::UI::Core::CoreDispatcher^ _dispatcher;
	//void runInBackground() const;

public:

	void invokeInRendererThread(GTask* task, bool autoDelete) const;

	void invokeInBackground(GTask* task, bool autoDelete) const;

	void onResume(const G3MContext* context);

	void onPause(const G3MContext* context);

	void onDestroy(const G3MContext* context);

	void initialize(const G3MContext* context);
};


#endif