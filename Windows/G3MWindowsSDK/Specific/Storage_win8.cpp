//
//  Storage_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Storage_win8.hpp"
#include "SQDatabase.hpp"
#include "IStringBuilder.hpp"
#include "GTask.hpp"
#include "TimeInterval.hpp"
#include "ByteBuffer_win8.hpp"
#include "Image_win8.hpp"
//#include "URL.hpp"
#include "IFactory.hpp"
#include "Context.hpp"
#include "ThreadUtils_win8.hpp"
#include <sstream>
//#include <ppltasks.h>

//using namespace concurrency;



Storage_win8::Storage_win8(const std::string &databaseName)
{
	_databaseName = databaseName.c_str();
	
	//_lock = [[NSLock alloc] init]; TODO: ???

	const std::string dbPath = getDBPath();
	ILogger::instance()->logInfo("Storage full database path: \"%s\"\n", dbPath.c_str());

	_writeDB = SQDatabase::initWithPath(dbPath);
	//_writeDB = new SQDatabase(dbPath);

	//if (!_writeDB) {
	if (!_writeDB->openReadWrite()) {
		ILogger::instance()->logError("Can't open read-write database \"%s\"\n", databaseName.c_str());
	}
	else {
		//_writeDB->openReadWrite();

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

		_readDB = SQDatabase::initWithPath(dbPath);

		if (!_readDB->openReadOnly()) {
			ILogger::instance()->logError("Can't open read-only database \"%s\"\n", databaseName.c_str());
		}
		//else {
		//	_readDB->openReadOnly();
		//}

		if (true) {
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

//Platform::String^ ToStringHat(std::string str)
//{
//	std::wstring wid_str = std::wstring(str.begin(), str.end());
//	const wchar_t* w_char = wid_str.c_str();
//	Platform::String^ p_string = ref new Platform::String(w_char);
//	return p_string;
//}
//
//std::string ToStringStd(Platform::String^ strhat)
//{
//	std::wstring wstr(strhat->Data());
//	std::string stdStr(wstr.begin(), wstr.end());
//	return stdStr;
//}

std::string getAppDataAbsoluteFilename(const std::string filename)
{	
	//from: http://jne100.blogspot.com.es/2012/10/winrt-accessing-app-data-files-by.html
	/*std::wstring appDataFolder(Windows::ApplicationModel::Package::
		Current->InstalledLocation->Path->Data());*/

	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
	Platform::String^ folderPath = localFolder->Path;

	std::wstring appDataFolder(folderPath->Data());
	std::wstring separator(L"\\");
	std::wstring appFilename = std::wstring(filename.begin(), filename.end());
	std::wstring absPath = (appDataFolder + separator + appFilename);

	std::string stdAbsPath(absPath.begin(), absPath.end());
	return stdAbsPath;
}

std::string Storage_win8::getDBPath() const{

	return getAppDataAbsoluteFilename(_databaseName);
}


std::time_t incrementTime(time_t init, long seconds) {
	if (init == NULL) return NULL;
	struct tm timeinfo;
	//struct tm* tm = localtime(&init);
	localtime_s(&timeinfo, &init);
	timeinfo.tm_sec += seconds;
	return mktime(&timeinfo);
}

std::string getTimeStamp(std::time_t t) {
	std::stringstream timeStamp;
	timeStamp << t;
	return timeStamp.str();
}

double timeDifferenceFromNow(double then) {

	std::time_t now = time(NULL);
	//std::string nowS = marshal_as<std::string>(now.ToString());
	std::string nowS = getTimeStamp(now);
	double nowD = atof(nowS.c_str());

	return (then - nowD);
}

bool isStringType(unsigned char* data, int length){

	char* ch = (char*)data + length - 1;
	return (*ch == '\0');

	/*if (*ch == '\0'){
		std::string* str = new std::string((char*)data);
		const char * c = str->c_str();
		ILogger::instance()->logInfo("ESTO ES UN STRING: \"%s\"\n", c);
		return true;
	}
	return false;*/
}

void Storage_win8::rawSave(std::string* table,
						   std::string* name,
						   IByteBuffer* buffer,
						   const TimeInterval& timeToExpires) {
	
	//[_lock lock]; TODO: ??

	//std::string* statement = [NSString stringWithFormat : @"INSERT OR REPLACE INTO %@ (name, contents, expiration) VALUES (?, ?, ?)", table];
	IStringBuilder* stmBuilder = IStringBuilder::newStringBuilder();
	stmBuilder->addString("INSERT OR REPLACE INTO ");
	stmBuilder->addString(*table);
	stmBuilder->addString(" (name, contents, expiration) VALUES (? , ? , ?)");

	std::string statement = stmBuilder->getString();
	
	//NSDate* expiration = [NSDate dateWithTimeIntervalSinceNow : timeToExpires.seconds()];
	//std::string* expirationS = [NSString stringWithFormat : @"%f", [expiration timeIntervalSince1970]];
	std::time_t currentTime = std::time(NULL);
	//time_t expiration = currentTime + (long) timeToExpires.seconds();
	time_t expiration = incrementTime(currentTime, (long)timeToExpires.seconds());
	//std::string expirationS = marshal_as<std::string>(expiration.ToString());
	std::string expirationS = getTimeStamp(expiration);

	const ByteBuffer_win8* buffer_win8 = (const ByteBuffer_win8*)buffer;
	unsigned char* contents = buffer_win8->getPointer();

	//std::string dataType = isStringType(contents, length) ? "string" : "unsigned char";

	std::vector<ContentValue*> contentValues = std::vector<ContentValue*>();
	contentValues.reserve(3);
	contentValues.push_back(new ContentValue((unsigned char*)name, name->length(), "string"));
	contentValues.push_back(new ContentValue(contents, buffer->size(), "unsigned char"));
	contentValues.push_back(new ContentValue((unsigned char*)&expirationS, expirationS.length(), "string"));
	
	if (!_writeDB->executeNonQuery(&statement, contentValues)){
		ILogger::instance()->logError("Can't save data for \"%s\"\n", name->c_str());
	}

	delete stmBuilder;
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
	Storage_win8*	_storage;
	std::string*          _table;
	std::string*          _name;
	IByteBuffer*            _buffer;
	const TimeInterval _timeToExpires;

public:
	SaverTask(Storage_win8* storage,
		std::string* table,
		std::string* name,
		IByteBuffer* buffer,
		const TimeInterval timeToExpires) :
		_storage(storage),
		_table(table),
		_name(name),
		_buffer(buffer),
		_timeToExpires(timeToExpires)
	{

	}

	void run(const G3MContext* context) {
		_storage->rawSave(_table, _name, _buffer, _timeToExpires);
	}
};

IByteBufferResult Storage_win8::readBuffer(const URL& url, bool readExpired){

	IByteBuffer* buffer = NULL;
	bool expired = false;

	//NSString* name = [NSString stringWithCppString : url._path];
	std::string name = url._path;

	//SQResultSet* rs = [_readDB executeQuery : @"SELECT contents, expiration FROM buffer2 WHERE (name = ?)", name];
	IStringBuilder* queryBuilder = IStringBuilder::newStringBuilder();
	queryBuilder->addString("SELECT contents, expiration FROM buffer2 WHERE (name = '");
	queryBuilder->addString(name);
	queryBuilder->addString("')");
	std::string query = queryBuilder->getString();

	SQResultSet* rs = _readDB->executeQuery(&query);

	if (rs->next()) {
		//NSData* nsData = [rs dataColumnByIndex : 0];
		int dataLength = 0;
		const unsigned char* data = rs->dataColumnByIndex(0, dataLength);
		//const double expirationInterval = [[rs stringColumnByIndex : 1] doubleValue];
		std::string* expirationIntervalS = rs->stringColumnByIndex(1);
		const double expirationInterval = atof(expirationIntervalS->c_str());
		//NSDate* expiration = [NSDate dateWithTimeIntervalSince1970 : expirationInterval];

		//expired = [expiration compare : [NSDate date]] != NSOrderedDescending;
		expired = timeDifferenceFromNow(expirationInterval) <= 0;

		if (readExpired || !expired) {
			//NSUInteger length = [nsData length];
			unsigned char* bytes = new unsigned char[dataLength];
			//[nsData getBytes : bytes length : length];
			memcpy(bytes, data, dataLength);
			buffer = IFactory::instance()->createByteBuffer(bytes, dataLength);
		}
	}

	rs->close();
	delete queryBuilder;

	return IByteBufferResult(buffer, expired);
}

IImageResult Storage_win8::readImage(const URL& url, bool readExpired){
	
	IImage* image = NULL;
	bool expired = false;

	//NSString* name = [NSString stringWithCppString : url._path];
	std::string name = url._path;
	//SQResultSet* rs = [_readDB executeQuery : @"SELECT contents, expiration FROM image2 WHERE (name = ?)", name];
	IStringBuilder* queryBuilder = IStringBuilder::newStringBuilder();
	queryBuilder->addString("SELECT contents, expiration FROM image2 WHERE (name = '");
	queryBuilder->addString(name);
	queryBuilder->addString("')");
	std::string query = queryBuilder->getString();

	SQResultSet* rs = _readDB->executeQuery(&query);

	if (rs->next()) {
		//NSData* data = [rs dataColumnByIndex : 0];
		int dataLength = 0;
		const unsigned char* data = rs->dataColumnByIndex(0, dataLength);
		//const double expirationInterval = [[rs stringColumnByIndex : 1] doubleValue];
		std::string* expirationIntervalS = rs->stringColumnByIndex(1);
		const double expirationInterval = atof(expirationIntervalS->c_str());
		//NSDate* expiration = [NSDate dateWithTimeIntervalSince1970 : expirationInterval];

		//expired = ([expiration compare : [NSDate date]] != NSOrderedDescending);
		expired = timeDifferenceFromNow(expirationInterval) <= 0;

		if (readExpired || !expired) { //TODO:
			
			//UIImage* uiImage = UIImage->imageWithData(data);
			//BYTE* imageData = (BYTE*)malloc(dataLength);
			BYTE* imageData = new BYTE[dataLength];
			memcpy(imageData, data, dataLength);
			//IWICBitmap* bitmap = Image_win8::imageWithData(imageData, dataLength);
			//IWICBitmap* bitmap = Image_win8::imageWithData((BYTE*)data, dataLength);
			IByteBuffer* imgBuffer = IFactory::instance()->createByteBuffer(imageData, dataLength);
			IWICBitmap* bitmap = Image_win8::imageWithData(imgBuffer);
			//delete[] data;

			if (bitmap) {
				image = new Image_win8(bitmap, imgBuffer);
			}
			else {
				ILogger::instance()->logError("Can't create image with contents of storage.");
			}	
		}
	}

	rs->close();
	delete queryBuilder;

	return IImageResult(image, expired);
}

void Storage_win8::saveBuffer(const URL& url, const IByteBuffer* buffer, const TimeInterval& timeToExpires, bool saveInBackground){
	
	const ByteBuffer_win8* buffer_win8 = (const ByteBuffer_win8*)buffer;
	unsigned char* contents = buffer_win8->getPointer();
	IByteBuffer* buffer2 = IFactory::instance()->createByteBuffer(contents, buffer->size());

	std::string name = url._path; // [NSString stringWithCppString : url._path];
	std::string table = "buffer2";
	
	if (saveInBackground) { 
		_context->getThreadUtils()->invokeInBackground(new SaverTask(this, &table, &name, buffer2, timeToExpires), true);
	}
	else {
		rawSave(&table, &name, buffer2, timeToExpires);
	}
}

void Storage_win8::saveImage(const URL& url, const IImage* image, const TimeInterval& timeToExpires, bool saveInBackground){
	
	const Image_win8* imagew8 = (const Image_win8*)image;
	//UIImage* uiImage = image_win8->getUIImage();
	IWICBitmap* bitmap = imagew8->getBitmap();

	//NSString* name = [NSString stringWithCppString : url._path];
	std::string name = url._path;
	std::string table = "image2";
	
	//BYTE* contents = imagew8->getSourceBuffer();
	IByteBuffer* buffer = imagew8->getSourceBuffer();
	
	if (buffer == NULL) {
		buffer = imagew8->createImageBuffer();
	}
	else {
		imagew8->releaseSourceBuffer();
	}
	
	if (saveInBackground) {
		_context->getThreadUtils()->invokeInBackground(new SaverTask(this, &table, &name, buffer, timeToExpires), true);
	}
	else {
		rawSave(&table, &name, buffer, timeToExpires);
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
	//std::string errMsg("TODO: isAvailable() test pending..");
	return (_readDB != NULL) && (_writeDB != NULL);
}