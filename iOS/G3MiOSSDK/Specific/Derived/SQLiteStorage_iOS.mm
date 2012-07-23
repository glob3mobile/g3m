//
//  SQLiteStorage.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 25/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "SQLiteStorage_iOS.hpp"
#include <sqlite3.h>


SQLiteStorage_iOS::SQLiteStorage_iOS(const std::string databaseName, const std::string table)
{
    _databaseName = databaseName;
    _table = table;
    SQLiteStorage_iOS::createEditableCopyOfDatabaseIfNeeded();
    SQLiteStorage_iOS::testConnection();
    SQLiteStorage_iOS::checkDataBaseConnection();
    SQLiteStorage_iOS::checkTableExist();
    
}

bool SQLiteStorage_iOS::contains(std::string filename)
{   
    bool contain = false;
    sqlite3 *db;
    if(SQLITE_OK != sqlite3_open(writableDBPath, &db)){
        NSLog(@"ERROR Opening Database For contains: %s.",sqlite3_errmsg(db));
    }else{
        sqlite3_stmt *ppStmt;
        char consulta[128];
        sprintf(consulta, "SELECT COUNT(*) FROM %s WHERE filename=@filename;", _table.c_str());
        if( sqlite3_prepare_v2(db, consulta, -1, &ppStmt, NULL)!=SQLITE_OK ){
            NSLog(@"Error: %s ", sqlite3_errmsg(db));
        } else {
            sqlite3_bind_text(ppStmt, sqlite3_bind_parameter_index(ppStmt, "@filename"), filename.c_str(), -1, SQLITE_STATIC);
            if(SQLITE_ROW == sqlite3_step(ppStmt)){
                int count = sqlite3_column_int(ppStmt, 0);
                if(count > 0){
                    contain = true;
                }else{
                    contain = false;
                }
            }
        }
        sqlite3_finalize(ppStmt);
        sqlite3_close(db);
    }
    return contain;
}

void SQLiteStorage_iOS::save(std::string filename, const ByteBuffer& bb){
    sqlite3 *db;
    if(SQLITE_OK != sqlite3_open(writableDBPath, &db)){
        NSLog(@"ERROR Opening Database For save: %s.",sqlite3_errmsg(db));
    }else{
        sqlite3_stmt *ppStmt;
        char consulta[128];
        sqlite3_exec(db, "BEGIN", 0, 0, 0);
        sprintf(consulta, "INSERT INTO %s (filename, file) VALUES (@filename, @file);", _table.c_str());
        if (sqlite3_prepare_v2(db, consulta, -1, &ppStmt, NULL) == SQLITE_OK) {
            sqlite3_bind_text(ppStmt, sqlite3_bind_parameter_index(ppStmt, "@filename"), filename.c_str(), -1, SQLITE_TRANSIENT);
            sqlite3_bind_blob(ppStmt, sqlite3_bind_parameter_index(ppStmt, "@file"), bb.getData(), bb.getDataLength(), SQLITE_TRANSIENT);
            //sqlite3_step(ppStmt);
            if(SQLITE_DONE != sqlite3_step(ppStmt)) {
                NSLog(@"Error: %s ", sqlite3_errmsg(db));
            }

            if(SQLITE_OK != sqlite3_finalize(ppStmt)){
                NSLog(@"Error in finalize: %s ", sqlite3_errmsg(db));
            }
        }
        sqlite3_exec(db, "END", 0, 0, 0);
    }
    sqlite3_close(db);
}

ByteBuffer SQLiteStorage_iOS::getByteBuffer(std::string filename)
{
    return SQLiteStorage_iOS::findFileFromFileName(filename);
}



ByteBuffer SQLiteStorage_iOS::findFileFromFileName(const std::string filename) {
    unsigned char *raw = NULL, *myRaw = NULL;
    int rawLen = 0;
    sqlite3 *db;
    if(SQLITE_OK != sqlite3_open(writableDBPath, &db)){
        NSLog(@"ERROR Opening Database For findFileFromFileName: %s.",sqlite3_errmsg(db));
    }else{
        sqlite3_stmt *ppStmt;
        char consulta[128];
        
        sprintf(consulta, "SELECT rowId,filename,file FROM %s WHERE filename=@filename;", _table.c_str());
        if( sqlite3_prepare_v2(db, consulta, -1, &ppStmt, NULL)!=SQLITE_OK ){
            NSLog(@"ERROR sqlite3_prepare_v2 For findFileFromFileName: %s.",sqlite3_errmsg(db));
        } else {
            sqlite3_bind_text(ppStmt, sqlite3_bind_parameter_index(ppStmt, "@filename"), filename.c_str(), -1, SQLITE_STATIC);
            while(SQLITE_ROW == sqlite3_step(ppStmt)) {
                raw = (unsigned char *)sqlite3_column_blob(ppStmt, 2);
                rawLen = sqlite3_column_bytes(ppStmt, 2);
                myRaw = new unsigned char[rawLen];
                for (int i = 0; i < rawLen; i++) {
                    myRaw[i] = raw[i];
                }
            }
            sqlite3_finalize(ppStmt);
        }
    }
    sqlite3_exec(db, "COMMIT", NULL, NULL, NULL);
    sqlite3_close(db);
    ByteBuffer bb(myRaw, rawLen);
    return bb;
}

void SQLiteStorage_iOS::createEditableCopyOfDatabaseIfNeeded() {
    // First, test for existence.
    const NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *database = [[NSString alloc] initWithCString:_databaseName.c_str() encoding:NSUTF8StringEncoding];
    NSError *error;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *writableDBPathNS = [documentsDirectory stringByAppendingPathComponent:database];
    writableDBPath = [writableDBPathNS cStringUsingEncoding:NSUTF8StringEncoding];
    NSLog(@"writableDBPath: %s ", writableDBPath);
    if (![fileManager fileExistsAtPath:writableDBPathNS]){
        NSLog(@"Writable database file NOT exist");
        // The writable database does not exist, so copy the default to the appropriate location.
        NSString *defaultDBPath = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:database];
        if (![fileManager copyItemAtPath:defaultDBPath toPath:writableDBPathNS error:&error]) {
            NSLog(@"Failed to create writable database file with message '%@'.", [error localizedDescription]);
        }
    }else{
        NSLog(@"Writable database file exist");
    }
}


bool SQLiteStorage_iOS::checkDataBaseConnection() const{
    sqlite3 *db;
    bool ok = true;
    if(SQLITE_OK != sqlite3_open(writableDBPath, &db)){
        printf("ERROR Opening Database %s \n", _databaseName.c_str());
        ok = false;
    }else{
        printf("Opening Database %s OK\n", _databaseName.c_str());
    }
    sqlite3_close(db);
    return ok;
}

bool SQLiteStorage_iOS::checkTableExist() const{
    sqlite3 *db;
    bool ok = true;
    if(SQLITE_OK != sqlite3_open(writableDBPath, &db)){
        printf("ERROR Opening Database %s \n", _databaseName.c_str());
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
    sqlite3 *db;
    if(SQLITE_OK != sqlite3_open(writableDBPath, &db)){
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
        for(int i = 0; i < 6; i++){
            if ( sqlite3_exec(db, pSQL[i], 0, 0, 0)!=SQLITE_OK ){
                printf ("\Error: %s ", sqlite3_errmsg(db));
                break; // break the loop if error occur
            } else if (i == 3){
                sqlite3_stmt *ppStmt;
                char consulta[64];
                strcpy(consulta, "select rowId,* from myTable;");
                if( sqlite3_prepare_v2(db, consulta, -1, &ppStmt, NULL)!=SQLITE_OK ){
                    printf ("\Error: %s ", sqlite3_errmsg(db));
                } else {
                    while(SQLITE_ROW == sqlite3_step(ppStmt)) {
                        printf ("\nID: %i ", sqlite3_column_int(ppStmt, 0));
                        printf ("\nFirstName: %s ", sqlite3_column_text(ppStmt, 1));
                        printf ("\nLastName: %s ", sqlite3_column_text(ppStmt, 2));
                        printf ("\nAge: %i ", sqlite3_column_int(ppStmt, 3));
                    }
                }
                sqlite3_finalize(ppStmt);
            }
        }
        sqlite3_close(db);
    }
}