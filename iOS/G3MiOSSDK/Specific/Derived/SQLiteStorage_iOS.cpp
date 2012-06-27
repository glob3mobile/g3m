//
//  SQLiteStorage.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 25/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>
#include "SQLiteStorage_iOS.hpp"
#include <sqlite3.h>

/*
void SQLiteStorage_iOS::openConexion(){
    int rc;
    sqlite3 *db;
    
    rc = sqlite3_open("files.db", &db);
    if(SQLITE_OK != rc){
        printf("Connection KO\n");

    }else{
        printf("Connection OK\n");

    }
    
    sqlite3_close(db);
};
 */
void IFile* SQLiteStorage_iOS::findFileFromFileName(const std::string filename) const {}
void SQLiteStorage_iOS::openConexion(){}


int main(int argc, char *argv[])
{
    //MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(0,0,_halfSize));

    //SQLiteStorage_iOS::openConexion();
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
};
