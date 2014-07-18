#pragma once

#include <string>
#include <vector>
#include <sqlite3.h>

class SQDatabase;

class SQResultSet {

private:
	const std::string* _sql;
	int _queryParamCount;
	int _columnCount;
	const SQDatabase* _db;
	mutable sqlite3_stmt* _stmt;

public:

	SQResultSet (const SQDatabase* db, std::string* query, std::vector<unsigned char*> args);
	~SQResultSet();

	static SQResultSet* initWithDatabase(const SQDatabase* db, std::string* query, std::vector<unsigned char*> args);
	virtual bool next() const;
	virtual void close() const;
	virtual int integerColumnByIndex(int index) const;
	virtual std::string* stringColumnByIndex(int index) const;
	virtual const unsigned char* dataColumnByIndex(int index) const;
	virtual double doubleColumnByIndex(int index) const;
	virtual bool isNullColumnByIndex(int index) const;
};


//----------------------------------------------------------------------------------

class SQDatabase
{
private: 
	mutable sqlite3*_db;
	std::string _dbPath;
	int _busyRetryTimeout;

	virtual bool SQDatabase::executeStatament(sqlite3_stmt * stmt) const;

public:
	SQDatabase(const std::string dbPath);
	~SQDatabase();

	static SQDatabase* initWithPath(const std::string dbPath);
	virtual bool isOpen() const;
	virtual bool openReadWrite() const;
	virtual bool openReadOnly() const;
	virtual void close() const;
	virtual SQResultSet* executeQuery(std::string* sql, ...) const;
	virtual SQResultSet* executeQuery(std::string* sql, std::vector<unsigned char*> args) const;
	virtual bool prepareSql(std::string* sql, sqlite3_stmt** stmt) const;
	virtual bool hasData(sqlite3_stmt* stmt) const;
	virtual int errorCode() const;
	virtual const std::string* errorMessage() const;
	virtual void bindObjectAtColumnToStatement(unsigned char* obj, int idx, sqlite3_stmt* stmt) const;
	virtual bool executeNonQuery(std::string* sql, ...) const;
	virtual bool executeNonQuery(std::string* sql, std::vector<unsigned char*> args) const;
	virtual bool beginTransaction() const;
	virtual bool commit() const;
	virtual bool rollback() const;
	virtual bool beginDeferredTransaction() const;

};

