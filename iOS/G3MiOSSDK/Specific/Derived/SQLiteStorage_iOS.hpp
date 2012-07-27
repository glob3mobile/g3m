//
//  SQLiteStorage.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 25/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SQLiteStorage_iOS_h
#define G3MiOSSDK_SQLiteStorage_iOS_h

#include "IStorage.hpp"

class SQLiteStorage_iOS: public IStorage
{
    const char *_writableDBPath;
    std::string _databaseName;
    std::string _table;
  
public:
    SQLiteStorage_iOS(const std::string databaseName, const std::string table);
    
    bool contains(const std::string filename);
    void save(const std::string filename, const ByteBuffer& bb);
    ByteBuffer* read(const std::string filename);
    
    
private:
    /*
     The most appropriate place for .db is in the resources folder.
     
     Then in your application delegate code file, in the appDidFinishLaunching method, you need to first check if a writable copy of the the SQLite file has already been created - ie: a copy of the SQLite file has been created in the users document folder on the iPhone's file system. If yes, you don't do anything (else you would overwrite it with the default Xcode SQLite copy
     
     If no, then you copy the SQLite file there - to make it writable.
     */
    virtual void createEditableCopyOfDatabaseIfNeeded();
    virtual ByteBuffer* findFileFromFileName(const std::string filename);
    virtual void testConnection() const;
    virtual bool checkDataBaseConnection() const;
    virtual bool checkTableExist() const;
};

#endif
