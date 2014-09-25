//
//  Storage_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef __G3MWindowsSDK_Storage_win8__
#define __G3MWindowsSDK_Storage_win8__

#include "Storage.hpp"
#include <string>
#include <ctime>

class SQDatabase;
class URL;


class Storage_win8 : public Storage {

private:

	const char* _databaseName;
	SQDatabase* _readDB = NULL;
	SQDatabase* _writeDB = NULL;

	std::string getDBPath() const;
	void showStatistics() const;

public:

	Storage_win8(const std::string &databaseName);
	//void initialize(const G3MContext* context);
	void rawSave(std::string* table, std::string* name, IByteBuffer* buffer, const TimeInterval& timeToExpires);
	IByteBufferResult readBuffer(const URL& url, bool readExpired);
	IImageResult readImage(const URL& url, bool readExpired);
	void saveBuffer(const URL& url, const IByteBuffer* buffer, const TimeInterval& timeToExpires, bool saveInBackground);
	void saveImage(const URL& url, const IImage* image, const TimeInterval& timeToExpires, bool saveInBackground);
	void onResume(const G3MContext* context);
	void onPause(const G3MContext* context);
	void onDestroy(const G3MContext* context);
	bool isAvailable();
};


#endif