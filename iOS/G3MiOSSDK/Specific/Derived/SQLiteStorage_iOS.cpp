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



/*IFile* SQLiteStorage_iOS::findFileFromFileName(const std::string filename) const{
    this->testConnection();
    this->openConexion();
    return NULL;}
*/
SQLiteStorage_iOS::SQLiteStorage_iOS(const std::string database, const std::string table)
{
    _database = database;
    _table = table;
    SQLiteStorage_iOS::checkDataBaseConnection();
    SQLiteStorage_iOS::checkTableExist();
    
}

bool SQLiteStorage_iOS::contains(std::string filename)
{   
    ByteBuffer bb = SQLiteStorage_iOS::findFileFromFileName(filename);
    if(bb.getDataLength() > 0){
        printf("\nData: %s and DataLength:%i \n\n",bb.getData(),bb.getDataLength());
        return true;
    }
    return false;
}

void SQLiteStorage_iOS::save(std::string filename, const ByteBuffer& bb){
    sqlite3 *db;
    if(SQLITE_OK != sqlite3_open(_database.c_str(), &db)){
        printf("Open Database For save KO\n");
    }else{
        printf("Open Database For save OK\n");
        sqlite3_stmt *ppStmt;
        char consulta[128];
        
        sprintf(consulta, "INSERT INTO %s (filename, file) VALUES (@filename, @file);", _table.c_str());
        if (sqlite3_prepare_v2(db, consulta, -1, &ppStmt, NULL) == SQLITE_OK) {
            sqlite3_bind_text(ppStmt, sqlite3_bind_parameter_index(ppStmt, "@filename"), filename.c_str(), -1, SQLITE_STATIC);
            
            for(int i = 0; i < bb.getDataLength(); i++){
                printf("%c\n",bb.getData()[i]);
            }
            
            sqlite3_bind_blob(ppStmt, sqlite3_bind_parameter_index(ppStmt, "@file"), bb.getData(), bb.getDataLength(), SQLITE_TRANSIENT);
            //std::string resp = (char*)bb.getData();
            //printf("\nFileName: %s;\nData: %s;\nDataLength:%i;\n\n",filename.c_str(), resp.c_str(), bb.getDataLength());
            sqlite3_step(ppStmt);
            sqlite3_finalize(ppStmt);
        }
    }
    sqlite3_exec(db, "COMMIT", NULL, NULL, NULL);
    sqlite3_close(db);
}

ByteBuffer& SQLiteStorage_iOS::getByteBuffer(std::string filename)
{
    return SQLiteStorage_iOS::findFileFromFileName(filename);
}



ByteBuffer& SQLiteStorage_iOS::findFileFromFileName(const std::string filename) {
    unsigned char *raw = NULL;
    int rawLen = 0;
    sqlite3 *db;
    if(SQLITE_OK != sqlite3_open(_database.c_str(), &db)){
        printf("Open Database for findFileFromFileName KO\n");
    }else{
        printf("Open Database for findFileFromFileName OK\n");
        sqlite3_stmt *ppStmt;
        char consulta[128];
        
        sprintf(consulta, "SELECT rowId,filename,file FROM %s WHERE filename=@filename;", _table.c_str());
        if( sqlite3_prepare_v2(db, consulta, -1, &ppStmt, NULL)!=SQLITE_OK ){
            printf ("\Error: %s ", sqlite3_errmsg(db));
        } else {
            sqlite3_bind_text(ppStmt, sqlite3_bind_parameter_index(ppStmt, "@filename"), filename.c_str(), -1, SQLITE_STATIC);
            while(SQLITE_ROW == sqlite3_step(ppStmt)) {
                printf ("\nID: %i ", sqlite3_column_int(ppStmt, 0));
                printf ("\nFileName: %s ", sqlite3_column_text(ppStmt, 1));
                printf ("\nFile (null):  ");
                raw = (unsigned char *)sqlite3_column_blob(ppStmt, 2);
                rawLen = sqlite3_column_bytes(ppStmt, 2);
            }
            sqlite3_finalize(ppStmt);
        }

    }
    sqlite3_exec(db, "COMMIT", NULL, NULL, NULL);
    sqlite3_close(db);
    ByteBuffer bb(raw, rawLen);
    for(int i = 0; i < bb.getDataLength(); i++){
        printf("%c\n",bb.getData()[i]);
    }
    return bb;
}

bool SQLiteStorage_iOS::checkDataBaseConnection() const{
    sqlite3 *db;
    bool ok = true;
    if(SQLITE_OK != sqlite3_open(_database.c_str(), &db)){
        printf("ERROR Opening Database %s \n", _database.c_str());
        ok = false;
    }else{
        printf("Opening Database %s OK\n", _database.c_str());
    }
    sqlite3_close(db);
    return ok;
}

bool SQLiteStorage_iOS::checkTableExist() const{
    sqlite3 *db;
    bool ok = true;
    if(SQLITE_OK != sqlite3_open(_database.c_str(), &db)){
        printf("ERROR Opening Database %s \n", _database.c_str());
        ok = false;
    }else{
        char consulta[128];
        sprintf(consulta, "SELECT COUNT(*) FROM %s;", _table.c_str());
        if(SQLITE_OK != sqlite3_exec(db, consulta, 0, 0, 0)) {
            printf("ERROR TABLE %s DON'T EXIST\n", _table.c_str());
            ok = false;
        }
    }
    if(ok){
        printf("TABLE %s EXIST\n", _table.c_str());
    }
    
    sqlite3_close(db);
    return ok;
}

void SQLiteStorage_iOS::testConnection() const{
    int rc;
    sqlite3 *db;
    
    rc = sqlite3_open("/Users/vidalete/repository/IGO-GIT-Repository/g3m/iOS/test.db", &db);
    if(SQLITE_OK != rc){
        printf("Connection KO\n");
    }else{

    const char *pSQL[6];
    
    // Create a new myTable in database
    pSQL[0] = "create table myTable (FirstName varchar(30), LastName varchar(30), Age smallint)";
    
    // Insert first data item into myTable
    pSQL[1] = "insert into myTable (FirstName, LastName, Age) values ('Woody', 'Alan', 45)";
    
    // Insert second data item into myTable
    pSQL[2] = "insert into myTable (FirstName, LastName, Age) values ('Micheal', 'Bay', 38)";
    
    // Select all data in myTable
    pSQL[3] = "select * from myTable";
    
    // Remove all data in myTable
    pSQL[4] = "delete from myTable";
    
    // Drop the table from database
    pSQL[5] = "drop table myTable";
    
    // execute all the sql statements
    for(int i = 0; i < 6; i++)
    {
        rc = sqlite3_exec(db, pSQL[i], 0, 0, 0);
        if( rc!=SQLITE_OK ){
            printf ("\Error: %s ", sqlite3_errmsg(db));
            break; // break the loop if error occur
        }else if(i == 3){
            sqlite3_stmt *ppStmt;
            char consulta[64];
            strcpy(consulta, "select rowId,* from myTable;");
            rc = sqlite3_prepare_v2(db, consulta, -1, &ppStmt, NULL);
            if( rc!=SQLITE_OK ){
                printf ("\Error: %s ", sqlite3_errmsg(db));
            } else {
                while(SQLITE_ROW == sqlite3_step(ppStmt)) {
                    printf ("\nID: %i ", sqlite3_column_int(ppStmt, 0));
                    printf ("\nFirstName: %s ", sqlite3_column_text(ppStmt, 1));
                    printf ("\nLastName: %s ", sqlite3_column_text(ppStmt, 2));
                    printf ("\nAge: %i ", sqlite3_column_int(ppStmt, 3));
                }
                sqlite3_finalize(ppStmt);
            }

        }
    }
    }
}