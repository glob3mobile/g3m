//
//  SQLiteStorage.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 25/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "SQLiteStorage_iOS.hpp"
#include <sqlite3.h>



IFile* SQLiteStorage_iOS::findFileFromFileName(const std::string filename) const{return NULL;}
void SQLiteStorage_iOS::openConexion(){
    int rc;
    //typedef struct sqlite3 sqlite3;
    sqlite3 *db;
    
    rc = sqlite3_open("test.db", &db);
    if(SQLITE_OK != rc){
        printf("Connection KO\n");
        
    }else{
        printf("Connection OK\n");
        
    }
    
    sqlite3_close(db);
}