//
//  Storage_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Storage_win8.hpp"
//#include <sqlite3.h>
#include <string>
#include "IStringBuilder.hpp"
#include "GTask.hpp"
#include "TimeInterval.hpp"
#include "ByteBuffer_win8.hpp"
#include "Image_win8.hpp"
#include "URL.hpp"
//#include <System.IO::Path>

#include <msclr\marshal_cppstd.h>
using namespace msclr::interop;
using namespace System::IO;

//#include <windows.h>
//using namespace Windows;


Storage_win8::Storage_win8(const std::string &databaseName)
{
	_databaseName = databaseName.c_str();

	//_lock = [[NSLock alloc] init]; TODO: ???

	std::string* dbPath = getDBPath();

	_writeDB = SQDatabase::initWithPath(*dbPath);

	if (!_writeDB) {
		ILogger::instance()->logError("Can't open write-database \"%s\"\n", databaseName.c_str());
	}
	else {
		_writeDB->openReadWrite();

		// addSkipBackupAttributeToItemAtPath(dbPath); //

		//if (![_writeDB executeNonQuery : @"DROP TABLE IF EXISTS buffer;"]) {
		std::string query = "DROP TABLE IF EXISTS buffer;";
		if (!_writeDB->executeNonQuery(&query)) {
			ILogger::instance()->logError("Can't drop table \"buffer\" from database \"%s\"\n", databaseName.c_str());
			return;
		}

		query = "DROP TABLE IF EXISTS image;";
		//if (![_writeDB executeNonQuery : @"DROP TABLE IF EXISTS image;"]) {
		if (!_writeDB->executeNonQuery(&query)) {
			ILogger::instance()->logError("Can't drop table \"image\" from database \"%s\"\n", databaseName.c_str());
			return;
		}

		query = "CREATE TABLE IF NOT EXISTS buffer2 (name TEXT, contents TEXT, expiration TEXT);";
		//if (![_writeDB executeNonQuery : @"CREATE TABLE IF NOT EXISTS buffer2 (name TEXT, contents TEXT, expiration TEXT);"]) {
		if (!_writeDB->executeNonQuery(&query)) {
			ILogger::instance()->logError("Can't create table \"buffer\" on database \"%s\"\n", databaseName.c_str());
			return;
		}

		query = "CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer2(name);";
		//if (![_writeDB executeNonQuery : @"CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer2(name);"]) {
		if (!_writeDB->executeNonQuery(&query)) {
			ILogger::instance()->logError("Can't create index \"buffer_name\" on database \"%s\"\n", databaseName.c_str());
			return;
		}

		query = "CREATE TABLE IF NOT EXISTS image2 (name TEXT, contents TEXT, expiration TEXT);";
		//if (![_writeDB executeNonQuery : @"CREATE TABLE IF NOT EXISTS image2 (name TEXT, contents TEXT, expiration TEXT);"]) {
		if (!_writeDB->executeNonQuery(&query)) {
			ILogger::instance()->logError("Can't create table \"image\" on database \"%s\"\n", databaseName.c_str());
			return;
		}

		query = "CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image2(name);";
		//if (![_writeDB executeNonQuery : @"CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image2(name);"]) {
		if (!_writeDB->executeNonQuery(&query)) {
			ILogger::instance()->logError("Can't create index \"image_name\" on database \"%s\"\n", databaseName.c_str());
			return;
		}

		_readDB = SQDatabase::initWithPath(*dbPath);

		if (!_readDB) {
			ILogger::instance()->logError("Can't open read-database \"%s\"\n", databaseName.c_str());
		}
		else {
			_readDB->openReadOnly();
		}

		if (false) {
			showStatistics();
		}
	}
}


void Storage_win8::showStatistics() const {

	std::string query = "SELECT COUNT(*), SUM(LENGTH(contents)) FROM buffer2";
	SQResultSet* rs1 = _readDB->executeQuery(&query);
	if (rs1->next()) {
		int count = rs1->integerColumnByIndex(0);
		int usedSpace = rs1->integerColumnByIndex(1);
		ILogger::instance()->logInfo("Initialized Storage on DB \"%s\", buffers=%d, usedSpace=%fMb", _databaseName, count, (float)((double)usedSpace / 1024 / 1024));
	}

	rs1->close();

	query = "SELECT COUNT(*), SUM(LENGTH(contents)) FROM image2";
	SQResultSet* rs2 = _readDB->executeQuery(&query);
	if (rs2->next()) {
		int count = rs2->integerColumnByIndex(0);
		int usedSpace = rs2->integerColumnByIndex(1);
		ILogger::instance()->logInfo("Initialized Storage on DB \"%s\", images=%d, usedSpace=%fMb", _databaseName, count, (float)((double)usedSpace / 1024 / 1024));
	}

	rs2->close();
}

std::string* Storage_win8::getDBPath() const{

	//std::string dbPath = Path.Combine(Windows.Storage.ApplicationData.Current.LocalFolder.Path, dbName);
	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
	Platform::String^ folderPath = localFolder->Path;
	//std::string stdFolderPath = marshal_as<std::string>(folderPath);
	//std::string dbPath = stdFolderPath.append("\\", _databaseName);
	System::String^ combinedPath = Path::Combine(folderPath, _databaseName);
	std::string dbPath = marshal_as<std::string>(combinedPath);

	ILogger::instance()->logInfo("Data base path: %s", dbPath);

	return &dbPath;
}

void Storage_win8::rawSave(std::string* table,
	std::string* name,
	void* contents,
	const TimeInterval& timeToExpires) {
	
	//[_lock lock]; TODO: ??

	//std::string* statement = [NSString stringWithFormat : @"INSERT OR REPLACE INTO %@ (name, contents, expiration) VALUES (?, ?, ?)", table];
	IStringBuilder* stmBuilder = IStringBuilder::newStringBuilder();
	stmBuilder->addString("INSERT OR REPLACE INTO ");
	stmBuilder->addString(*table);
	stmBuilder->addString(" (name, contents, expiration) VALUES(? , ? , ? )");

	const std::string statement = stmBuilder->getString();


	NSDate* expiration = [NSDate dateWithTimeIntervalSinceNow : timeToExpires.seconds()];

	std::string* expirationS = [NSString stringWithFormat : @"%f", [expiration timeIntervalSince1970]];

	if (![_writeDB executeNonQuery : statement, name, contents, expirationS]) {
		printf("Can't save \"%s\"\n", [name cStringUsingEncoding : NSUTF8StringEncoding]);
	}

	//[_lock unlock];
}


/*
void Storage_win8::initialize(const G3MContext* context){
std::string errMsg("TODO: Implementation");
throw std::exception(errMsg.c_str());
}
*/


class SaverTask : public GTask {
private:
	Storage_win8* _storage;
	std::string*          _table;
	std::string*          _name;
	void*            _contents;
	const TimeInterval _timeToExpires;

public:
	SaverTask(Storage_win8* storage,
		std::string* table,
		std::string* name,
		void* contents,
		const TimeInterval timeToExpires) :
		_storage(storage),
		_table(table),
		_name(name),
		_contents(contents),
		_timeToExpires(timeToExpires)
	{

	}

	void run(const G3MContext* context) {
		_storage->rawSave(_table, _name, _contents, _timeToExpires);
	}
};

IByteBufferResult Storage_win8::readBuffer(const URL& url, bool readExpired){

	IByteBuffer* buffer = NULL;
	bool expired = false;

	//NSString* name = [NSString stringWithCppString : url._path];
	std::string name = url._path;

	//SQResultSet* rs = [_readDB executeQuery : @"SELECT contents, expiration FROM buffer2 WHERE (name = ?)", name];
	IStringBuilder* queryBuilder = IStringBuilder::newStringBuilder();
	queryBuilder->addString("SELECT contents, expiration FROM buffer2 WHERE (name = ";
	queryBuilder->addString(name);
	queryBuilder->addString(")");

	std::string query = queryBuilder->getString();
	SQResultSet* rs = _readDB->executeQuery(&query);

	if (rs->next()) {
		//NSData* nsData = [rs dataColumnByIndex : 0];
		const void* data = rs->dataColumnByIndex(0);
		const double expirationInterval = [[rs stringColumnByIndex : 1] doubleValue];
		NSDate* expiration = [NSDate dateWithTimeIntervalSince1970 : expirationInterval];

		expired = [expiration compare : [NSDate date]] != NSOrderedDescending;

		if (readExpired || !expired) {
			NSUInteger length = [nsData length];
			unsigned char* bytes = new unsigned char[length];
			[nsData getBytes : bytes
			length : length];

			buffer = IFactory::instance()->createByteBuffer(bytes, length);
		}
	}

	rs->close();

	return IByteBufferResult(buffer, expired);
}

IImageResult Storage_win8::readImage(const URL& url, bool readExpired){
	
	IImage* image = NULL;
	bool expired = false;

	//NSString* name = [NSString stringWithCppString : url._path];
	std::string name = url._path;
	//SQResultSet* rs = [_readDB executeQuery : @"SELECT contents, expiration FROM image2 WHERE (name = ?)", name];
	IStringBuilder* queryBuilder = IStringBuilder::newStringBuilder();
	queryBuilder->addString("SELECT contents, expiration FROM image2 WHERE (name = ";
	queryBuilder->addString(name);
	queryBuilder->addString(")");

	std::string query = queryBuilder->getString();
	SQResultSet* rs = _readDB->executeQuery(&query);

	if (rs->next()) {
		//NSData* data = [rs dataColumnByIndex : 0];
		unsigned char* data = rs->dataColumnByIndex(0);
		//const double expirationInterval = [[rs stringColumnByIndex : 1] doubleValue];
		const double expirationInterval = (double) rs->stringColumnByIndex(1);
		NSDate* expiration = [NSDate dateWithTimeIntervalSince1970 : expirationInterval];

		expired = ([expiration compare : [NSDate date]] != NSOrderedDescending);

		if (readExpired || !expired) {
			//      NSDate* startParse = [NSDate date];
			UIImage* uiImage = UIImage->imageWithData(data);
			//      parsedTime = ([startParse timeIntervalSinceNow] * -1000.0);

			if (uiImage) {
				image = new Image_iOS(uiImage,
					NULL /* data is not needed */);
			}
			else {
				ILogger::instance()->logError("Can't create image with contents of storage.");
			}
		}
	}

	rs->close();

	return IImageResult(image, expired);
}

void Storage_win8::saveBuffer(const URL& url, const IByteBuffer* buffer, const TimeInterval& timeToExpires, bool saveInBackground){
	
	const ByteBuffer_win8* buffer_win8 = (const ByteBuffer_win8*)buffer;

	const std::string name = url._path; // [NSString stringWithCppString : url._path];
	unsigned char* contents = buffer_win8->getPointer();

	if (saveInBackground) {
		_context->getThreadUtils()->invokeInBackground(new SaverTask(this, "buffer2", name, contents, timeToExpires),
			true);
	}
	else {
		rawSave("buffer2", name, contents, timeToExpires);
	}
}

void Storage_win8::saveImage(const URL& url, const IImage* image, const TimeInterval& timeToExpires, bool saveInBackground){
	
	const Image_win8* image_win8 = (const Image_win8*)image;
	UIImage* uiImage = image_win8->getUIImage();

	//NSString* name = [NSString stringWithCppString : url._path];
	std::string name = url._path;

	NSData* contents = image_win8->getSourceBuffer();
	if (contents == NULL) {
		contents = UIImagePNGRepresentation(uiImage);
	}
	else {
		image_iOS->releaseSourceBuffer();
	}

	if (saveInBackground) {
		_context->getThreadUtils()->invokeInBackground(new SaverTask(this, @"image2", name, contents, timeToExpires),
			true);
	}
	else {
		rawSave(@"image2", name, contents, timeToExpires);
	}
}

void Storage_win8::onResume(const G3MContext* context){
	//do nothing in windows
}

void Storage_win8::onPause(const G3MContext* context){
	//do nothing in windows
}

void Storage_win8::onDestroy(const G3MContext* context){
	//do nothing in windows
}

bool Storage_win8::isAvailable(){
	std::string errMsg("TODO: isAvailable() test pending..");
	return (_readDB != NULL) && (_writeDB != NULL);
}