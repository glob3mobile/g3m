//
//  ThreadUtils_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//
//

#include "ThreadUtils_win8.hpp"
#include <string>

void ThreadUtils_win8::onResume(const G3MContext* context){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void ThreadUtils_win8::onPause(const G3MContext* context){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void ThreadUtils_win8::onDestroy(const G3MContext* context){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void ThreadUtils_win8::initialize(const G3MContext* context){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());

}

void ThreadUtils_win8::invokeInRendererThread(GTask* task, bool autoDelete) const {
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void ThreadUtils_win8::invokeInBackground(GTask* task, bool autoDelete) const {
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}