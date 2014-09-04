
#include "SQDatabase.hpp"
#include "ILogger.hpp"
#include "IStringBuilder.hpp"
#include <thread>
#include <ctime>
#include <sstream>
#include <cstdarg>
//#include <stdarg.h>



ContentValue::ContentValue(unsigned char* content, int size, std::string type){

	_content = content;
	_size = size;
	_type = type;
}

//ContentValue::~ContentValue(){
//
//}

//----------------------------------------------------------------------------------



SQResultSet* SQResultSet::initForDatabase(const SQDatabase* db, std::string* query, std::vector<ContentValue*> args){

	try{
		return new SQResultSet(db, query, args);
	}
	catch (std::string e){
		ILogger::instance()->logError(e.c_str());
	}
	return NULL;
}

SQResultSet::SQResultSet(const SQDatabase* db, std::string* query, std::vector<ContentValue*> args){

	_db = db;
	_stmt = NULL;
	_sql = query;

	if (!_db->prepareSql(query, &_stmt)){
		IStringBuilder* errBuilder = IStringBuilder::newStringBuilder();
		errBuilder->addString("Can't prepare statement: %s, error=[");
		errBuilder->addInt(_db->errorCode());
		errBuilder->addString(": ");
		errBuilder->addString(*_db->errorMessage());

		std::string error = errBuilder->getString();
		delete errBuilder;

		throw std::exception(error.c_str());
	}

	_queryParamCount = sqlite3_bind_parameter_count(_stmt);
	ILogger::instance()->logInfo("Query param count: %d", _queryParamCount);

	int i = 0;
	while (i++ < _queryParamCount) {
		_db->bindObjectAtColumnToStatement(args.at(i - 1), i, _stmt);
	}

	_columnCount = sqlite3_column_count(_stmt);
	ILogger::instance()->logInfo("Column count: %d", _columnCount);
}

bool SQResultSet::next() const{
	bool result = _db->hasData(_stmt);
	if (!result) {
		this->close();
	}
	return result;
}

void SQResultSet::close() const{
	if (_stmt) {
		sqlite3_finalize(_stmt);
		_stmt = NULL;
	}
}

int SQResultSet::integerColumnByIndex(int index) const{
	int columnType = sqlite3_column_type(_stmt, index);

	if (columnType == SQLITE_NULL) {
		return 0;
	}

	return sqlite3_column_int(_stmt, index);
}

std::string* SQResultSet::stringColumnByIndex(int index) const{
	int columnType = sqlite3_column_type(_stmt, index);

	if (columnType == SQLITE_NULL) {
		return NULL;
	}

	const char* text = (const char*)sqlite3_column_text(_stmt, index);
	std::string* str = new std::string(text);// str(text);

	return str;
}

const unsigned char* SQResultSet::dataColumnByIndex(int index, int &length) const{
	int columnType = sqlite3_column_type(_stmt, index);

	if (columnType == SQLITE_NULL) {
		return NULL;
	}
	length = sqlite3_column_bytes(_stmt, index);
	return (const unsigned char*)sqlite3_column_blob(_stmt, index);

	//return[NSData dataWithBytes : sqlite3_column_blob(_stmt, index)
	//length : (NSUInteger)blobLength];
}



double SQResultSet::doubleColumnByIndex(int index) const{
	int columnType = sqlite3_column_type(_stmt, index);

	if (columnType == SQLITE_NULL) {
		return 0;
	}

	return sqlite3_column_double(_stmt, index);
}

bool SQResultSet::isNullColumnByIndex(int index) const{
	int columnType = sqlite3_column_type(_stmt, index);
	return (columnType == SQLITE_NULL);
}


//----------------------------------------------------------------------------------




SQDatabase::SQDatabase(const std::string dbPath){

		_dbPath = dbPath;
		_busyRetryTimeout = 10;
		_db = NULL;
}

SQDatabase* SQDatabase::initWithPath(const std::string dbPath) {

	try{
		return new SQDatabase(dbPath);
	}
	catch (std::string e){
		ILogger::instance()->logError(e.c_str());
	}
	return NULL;
}

bool SQDatabase::isOpen() const{
	if (_db != NULL)
		return true;
	else
		return false;
}

bool SQDatabase::openReadWrite() const{
	this->close();

	const char* dbpathC = _dbPath.c_str();

	if (sqlite3_open_v2(dbpathC, &_db, SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE, NULL) == SQLITE_OK) {
		return true;
	}
	else {
		_db = NULL;
		ILogger::instance()->logError("Can't open readwrite database: %s", dbpathC);
		return false;
	}
}

bool SQDatabase::openReadOnly() const{
	this->close();

	const char* dbpathC = _dbPath.c_str();

	if (sqlite3_open_v2(dbpathC, &_db, SQLITE_OPEN_READONLY, NULL) == SQLITE_OK) {
		return true;
	}
	else {
		_db = NULL;
		ILogger::instance()->logError("Can't open readonly database: %s", dbpathC);
		return false;
	}
}

void SQDatabase::close() const{
	if (this->isOpen()) {
		sqlite3_close(_db);
		_db = NULL;
	}
}

SQResultSet* SQDatabase::executeQuery(std::string* sql) const{
	
	//va_list args;
	//va_start(args, sql);

	//std::vector<unsigned char*> argsArray;
	//
	//for (int i = 0; i < sql->length(); ++i) {
	//	if (sql->at(i) == '?'){
	//		argsArray.push_back(va_arg(args, unsigned char*)); //TODO: ??
	//	}
	//}

	//va_end(args);

	std::vector<ContentValue*> argsArray = std::vector<ContentValue*>();

	return this->executeQuery(sql, argsArray);
}

SQResultSet* SQDatabase::executeQuery(std::string* sql, std::vector<ContentValue*> args) const{

	return SQResultSet::initForDatabase(this, sql, args);
}

bool SQDatabase::prepareSql(std::string* sql, sqlite3_stmt** stmt) const{
	int numOfRetries = 0;

	do {
		int rc = sqlite3_prepare_v2(_db, sql->c_str(), -1, stmt, NULL);
		if (rc == SQLITE_OK) {
			return true;
		}

		if (rc == SQLITE_BUSY) {
			if (numOfRetries == _busyRetryTimeout) {
				ILogger::instance()->logError("SQLite Busy 1: %s", _dbPath.c_str());
				break;
			}

			std::this_thread::sleep_for(std::chrono::milliseconds(10) /* 10ms */);
		}
		else {
			ILogger::instance()->logError("SQLite Prepare Failed: %s", sqlite3_errmsg(_db));
			ILogger::instance()->logError(" - Query: %s", sql->c_str());
			break;
		}
	} while (numOfRetries++ <= _busyRetryTimeout);

	return false;
}

bool SQDatabase::hasData(sqlite3_stmt* stmt) const{

	int numOfRetries = 0;

	do {
		int rc = sqlite3_step(stmt);

		if (rc == SQLITE_ROW) {
			return true;
		}

		if (rc == SQLITE_DONE) {
			break;
		}

		if (rc == SQLITE_BUSY) {
			if (numOfRetries == _busyRetryTimeout) {
				ILogger::instance()->logError("SQLite Busy 2: %s", _dbPath.c_str());
				break;
			}

			std::this_thread::sleep_for(std::chrono::milliseconds(10) /* 10ms */);
		}
		else {
			ILogger::instance()->logError("SQLite Prepare Failed: %s", sqlite3_errmsg(_db));
			break;
		}
	} while (numOfRetries++ <= _busyRetryTimeout);

	return false;
}

int SQDatabase::errorCode() const{
	return sqlite3_errcode(_db);
}

const std::string* SQDatabase::errorMessage() const{
	const char* text = sqlite3_errmsg(_db);
	std::string str(text);

	return &str;
}

double getTimeStamp(std::time_t* t) {
	std::stringstream ts;
	ts << *t;
	std::string timeStamp = ts.str();

	return atof(timeStamp.c_str());
}

void SQDatabase::bindObjectAtColumnToStatement(ContentValue* obj, int idx, sqlite3_stmt* stmt) const{

	
	if (obj == NULL) {
		sqlite3_bind_null(stmt, idx);
	}
	else if (obj->_type == "string"){
		std::string* str = (std::string*)(obj->_content);
		//const char * cstr = str->c_str();
		//std::string* str = new std::string((const char*)(obj->_content));
		//const char* c = (const char*)(obj->_content);
		ILogger::instance()->logInfo("Typeid del OBJ: %s", obj->_type.c_str());
		ILogger::instance()->logInfo("Content del OBJ: %s", str->c_str());
		sqlite3_bind_text(stmt, idx, str->c_str(), -1, SQLITE_STATIC);
	}
	else {
		//char* dataArr = (char*)(obj->_content);
		//int length = strlen(dataArr);
		ILogger::instance()->logInfo("Typeid del OBJ: %s", obj->_type.c_str());
		sqlite3_bind_blob(stmt, idx, obj->_content, obj->_size, SQLITE_STATIC);
	}
	//else if (obj->_type == "unsigned char"){
	//	
	//	if (typeid(obj->_content).name() == "time_t"){
	//		sqlite3_bind_double(stmt, idx, getTimeStamp((time_t*)(obj->_content)));
	//	}
	//	else if (typeid(obj->_content).name() == "bool"){
	//		sqlite3_bind_int(stmt, idx, *((bool*)(obj->_content)) ? 1 : 0);
	//	}
	//	else if (typeid(obj->_content).name() == "int"){
	//		sqlite3_bind_int64(stmt, idx, (long)*((int*)(obj->_content)));
	//	}
	//	else if (typeid(obj->_content).name() == "long"){
	//		sqlite3_bind_int64(stmt, idx, *((long*)(obj->_content)));
	//	}
	//	else if (typeid(obj->_content).name() == "float"){
	//		sqlite3_bind_double(stmt, idx, (double)*((float*)(obj->_content)));
	//	}
	//	else if (typeid(obj->_content).name() == "double"){
	//		sqlite3_bind_double(stmt, idx, *((double*)(obj->_content)));
	//	}
	//	else {
	//		char* dataArr = (char*)(obj->_content);
	//		//int length = strlen(dataArr);
	//		sqlite3_bind_blob(stmt, idx, obj->_content, strlen(dataArr), SQLITE_STATIC);
	//	}
	//}
}

bool SQDatabase::executeStatament(sqlite3_stmt * stmt) const{
	int numOfRetries = 0;
	int rc;

	do {
		rc = sqlite3_step(stmt);
		if (rc == SQLITE_OK || rc == SQLITE_DONE)
			return true;

		if (rc == SQLITE_BUSY) {
			if (numOfRetries == _busyRetryTimeout) {
				ILogger::instance()->logError("SQLite Busy 3: %s", _dbPath.c_str());
				break;
			}

			std::this_thread::sleep_for(std::chrono::milliseconds(10) /* 10ms */);
		}
		else {
			ILogger::instance()->logError("SQLite Step Failed: %s", sqlite3_errmsg(_db));
			break;
		}
	} while (numOfRetries++ <= _busyRetryTimeout);

	return false;
}

bool SQDatabase::executeNonQuery(std::string* sql) const{

	//va_list args;
	//va_start(args, sql);

	//std::vector<unsigned char*> argsArray;
	////argsArray.reserve(3);
	//ILogger::instance()->logInfo("EXECUTENONQUERY SQL= %s", sql->c_str());
	//for (int i = 0; i < sql->length(); ++i) {
	//	if (sql->at(i) == '?'){
	//		ILogger::instance()->logInfo("Typeid del OBJ: %s", typeid(args).name());
	//		argsArray.push_back(va_arg(args, unsigned char*)); //TODO: works??
	//	}
	//}

	//va_end(args);
	ILogger::instance()->logInfo("EXECUTENONQUERY SQL= %s", sql->c_str());
	std::vector<ContentValue*> argsArray = std::vector<ContentValue*>(); //empty args

	return this->executeNonQuery(sql, argsArray);
}

bool SQDatabase::executeNonQuery(std::string* sql, std::vector<ContentValue*> args) const{

	sqlite3_stmt *sqlStmt;

	if (!this->prepareSql(sql, (&sqlStmt))){
		return false;
	}

	int i = 0;
	int queryParamCount = sqlite3_bind_parameter_count(sqlStmt);
	while (i++ < queryParamCount) {
		this->bindObjectAtColumnToStatement(args.at(i - 1), i, sqlStmt);
	}

	bool success = this->executeStatament(sqlStmt);

	sqlite3_finalize(sqlStmt);
	return success;
}

bool SQDatabase::beginTransaction() const{
	std::string str("BEGIN EXCLUSIVE TRANSACTION;");
	return this->executeNonQuery(&str);
}

bool SQDatabase::commit() const{
	std::string str("COMMIT TRANSACTION;");
	return this->executeNonQuery(&str);
}

bool SQDatabase::rollback() const{
	std::string str("ROLLBACK TRANSACTION;");
	return this->executeNonQuery(&str);
}

bool SQDatabase::beginDeferredTransaction() const{
	std::string str("BEGIN DEFERRED TRANSACTION;");
	return this->executeNonQuery(&str);
}

SQDatabase::~SQDatabase(){
	//TODO: 
}
