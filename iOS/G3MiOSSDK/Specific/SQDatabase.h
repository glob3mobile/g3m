//
//  SQDatabase.h
//  wikiglob3-ios
//
//  Created by Diego Gomez Deck on 08/03/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>


@class SQDatabase;


@interface SQResultSet : NSObject

@property (readonly, strong, atomic) NSString*   sql;
@property (readonly, atomic)         NSInteger   queryParamCount;
@property (readonly, atomic)         NSInteger   columnCount;
@property (readonly, weak, atomic)   SQDatabase* db;

+ (SQResultSet*) resultSetWithDB: (SQDatabase*) db
                           query: (NSString*)   sql
                       arguments: (NSArray*)    args;

- (id) initWithWithDB: (SQDatabase*) db
                query: (NSString*)   sql
            arguments: (NSArray*)    args;

- (BOOL) next;

- (void) close;

- (NSInteger) integerColumnByIndex: (NSInteger) index;

- (NSString*) stringColumnByIndex: (NSInteger) index;

- (NSData*) dataColumnByIndex: (NSInteger) index;

- (double) doubleColumnByIndex: (NSInteger) index;

- (BOOL)isNullColumnByIndex: (NSInteger) index;

@end



@interface SQDatabase : NSObject {
  sqlite3 *_db;
}

@property (readonly, strong, nonatomic) NSString* dbPath;
@property (readwrite, atomic)           NSInteger busyRetryTimeout;

+ (SQDatabase*) databaseWithPath: (NSString*) dbPath;

- (id) initWithPath: (NSString*) dbPath;

- (BOOL) isOpen;

- (BOOL) openReadWrite;

- (BOOL) openReadOnly;

- (void) close;

- (SQResultSet *) executeQuery: (NSString *) sql, ...;

- (SQResultSet *) executeQuery: (NSString *) sql
                     arguments: (NSArray *)  args;

- (BOOL) prepareSql: (NSString*)      sql
        inStatement: (sqlite3_stmt**) stmt;

- (BOOL) hasData: (sqlite3_stmt*) stmt;

- (NSInteger) errorCode;

- (NSString*) errorMessage;

- (void) bindObject: (id)            obj
           toColumn: (int)           idx
        inStatement: (sqlite3_stmt*) stmt;

- (BOOL) executeNonQuery: (NSString*) sql, ...;

- (BOOL) executeNonQuery: (NSString*) sql
               arguments: (NSArray*)  args;

- (BOOL) beginTransaction;

- (BOOL) commit;

- (BOOL) rollback;

- (BOOL) beginDeferredTransaction;


@end
