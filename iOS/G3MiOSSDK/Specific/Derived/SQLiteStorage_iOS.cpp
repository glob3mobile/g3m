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



IFile* SQLiteStorage_iOS::findFileFromFileName(const std::string filename) const{
    this->testConnection();
    this->openConexion();
    return NULL;}


void SQLiteStorage_iOS::openConexion() const{
    int rc;
    sqlite3 *db;
    
    rc = sqlite3_open("/Users/vidalete/repository/IGO-GIT-Repository/g3m/iOS/test.db", &db);
    if(SQLITE_OK != rc){
        printf("Connection KO\n");
        
    }else{
        printf("Connection OK\n");
        sqlite3_stmt *ppStmt;
        char consulta[64];
        
        strcpy(consulta, "SELECT rowId,* FROM file");
        rc = sqlite3_prepare_v2(db, consulta, -1, &ppStmt, NULL);
        if( rc!=SQLITE_OK ){
            //std::cout << "Error: " << sqlite3_errmsg(db) << std::endl;
            printf ("\Error: %s ", sqlite3_errmsg(db));
        } else {
            while(SQLITE_ROW == sqlite3_step(ppStmt)) {
                printf ("\nID: %i ", sqlite3_column_int(ppStmt, 0));
                printf ("\nFileName: %s ", sqlite3_column_text(ppStmt, 1));
                printf ("\nFile (null):  ");

                //printf((const char*)a);
                                //std::cout << "ID:           " << sqlite3_column_int(ppStmt, 0) << std::endl;
                //std::cout << "FileName:     " << a << std::endl;
                //std::cout << "File (null):  " << sqlite3_column_blob(ppStmt, 2) << std::endl;
                printf("jarjur\n");
            }
            sqlite3_finalize(ppStmt);
        }

    }
    
    sqlite3_close(db);
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