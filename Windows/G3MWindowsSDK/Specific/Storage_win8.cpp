//
//  Storage_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Storage_win8.hpp"

#include <string>

void Storage_win8::initialize(const G3MContext* context){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

IByteBufferResult Storage_win8::readBuffer(const URL& url, bool readExpired){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

IImageResult Storage_win8::readImage(const URL& url, bool readExpired){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void Storage_win8::saveBuffer(const URL& url, const IByteBuffer* buffer, const TimeInterval& timeToExpires, bool saveInBackground){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void Storage_win8::saveImage(const URL& url, const IImage* image, const TimeInterval& timeToExpires, bool saveInBackground){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void Storage_win8::onResume(const G3MContext* context){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void Storage_win8::onPause(const G3MContext* context){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void Storage_win8::onDestroy(const G3MContext* context){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

bool Storage_win8::isAvailable(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}