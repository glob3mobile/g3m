//
//  SQLiteStorage.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 25/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SQLiteStorage_iOS_hpp
#define G3MiOSSDK_SQLiteStorage_iOS_hpp

#include "IStorage.hpp"

class SQLiteStorage_iOS: public IStorage
{
     std::string _database;
     std::string _table;
public:
    //virtual IFile* findFileFromFileName(const std::string filename) const;
    SQLiteStorage_iOS(const std::string database, const std::string table);
    
    bool contains(std::string filename);
    void save(std::string filename, const ByteBuffer& bb);
    ByteBuffer getByteBuffer(std::string filename);
    
    
private:
    virtual ByteBuffer findFileFromFileName(const std::string filename) const;
    //virtual void openConexion() const;
    virtual void testConnection() const;
    virtual bool checkDataBaseConnection() const;
    virtual bool checkTableExist() const;
};

#endif
