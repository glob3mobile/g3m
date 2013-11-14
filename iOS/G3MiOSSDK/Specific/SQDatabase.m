//
//  SQDatabase.m
//  wikiglob3-ios
//
//  Created by Diego Gomez Deck on 08/03/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import "SQDatabase.h"

@implementation SQResultSet{
  sqlite3_stmt *_stmt;
}

@synthesize queryParamCount = _queryParamCount;
@synthesize columnCount = _columnCount;
@synthesize sql = _sql;
@synthesize db = _db;

+ (SQResultSet*) resultSetWithDB: (SQDatabase*) db
                           query: (NSString*)   sql
                       arguments: (NSArray*)    args
{
  return [[SQResultSet alloc] initWithWithDB: (SQDatabase*) db
                                       query: sql
                                   arguments: args];
}

- (id) initWithWithDB: (SQDatabase*) db
                query: (NSString*)   sql
            arguments: (NSArray*)    args;
{
  self = [super init];
  if (self) {
    _db = db;
    _stmt = NULL;
    if (![db prepareSql: sql
            inStatement: &_stmt]) {
      NSLog(@"Can't prepare statement \"%@\", error=(%i:%@)",
            sql,
            [_db errorCode],
            [_db errorMessage]);
      return nil;
    }

    _queryParamCount = sqlite3_bind_parameter_count(_stmt);
    //NSLog(@"queryParamCount=%i", _queryParamCount);

    int i = 0;
    while (i++ < _queryParamCount) {
      [_db bindObject: [args objectAtIndex:(NSUInteger)(i - 1)]
             toColumn: i
          inStatement: _stmt];
    }

    _columnCount = sqlite3_column_count(_stmt);
    //NSLog(@"columnCount=%i", _columnCount);
  }
  return self;
}

- (void) close
{
  if (_stmt) {
    //    if (!sqlite3_finalize(_stmt)) {
    //      NSLog(@"Can't finalize ResulSet \"%@\"", _sql);
    //    }
    sqlite3_finalize(_stmt);

    _stmt = NULL;
  }
}

- (BOOL) next
{
  BOOL result = [_db hasData: _stmt];
  if (!result) {
    //NSLog(@"- Auto closing ResultSet");
    [self close];
  }
  return result;
}

-(void) dealloc
{
  [self close];
}

- (BOOL) isNullColumnByIndex: (NSInteger) index
{
  int columnType = sqlite3_column_type(_stmt, index);
	return (columnType == SQLITE_NULL);
}

- (NSInteger) integerColumnByIndex: (NSInteger) index
{
  int columnType = sqlite3_column_type(_stmt, index);

	if (columnType == SQLITE_NULL) {
		return 0;
  }

  return sqlite3_column_int(_stmt, index);
}

- (NSString*) stringColumnByIndex: (NSInteger) index
{
  int columnType = sqlite3_column_type(_stmt, index);

	if (columnType == SQLITE_NULL) {
		return nil;
  }

  return [NSString stringWithUTF8String:(const char* ) sqlite3_column_text(_stmt, index)];
}

- (NSData*) dataColumnByIndex: (NSInteger) index
{
  NSInteger blobLength = sqlite3_column_bytes(_stmt, index);
  return [NSData dataWithBytes: sqlite3_column_blob(_stmt, index)
                        length: (NSUInteger)blobLength];
}

- (double) doubleColumnByIndex: (NSInteger) index
{
  int columnType = sqlite3_column_type(_stmt, index);

	if (columnType == SQLITE_NULL) {
		return 0;
  }

  return sqlite3_column_double(_stmt, index);
}

@end



@implementation SQDatabase

@synthesize dbPath           = _dbPath;
@synthesize busyRetryTimeout = _busyRetryTimeout;


+ (SQDatabase*) databaseWithPath: (NSString *) dbPath
{
  return [[SQDatabase alloc] initWithPath: dbPath];
}

- (id) initWithPath: (NSString*) dbPath
{
  self = [super init];
  if (self) {
    _db               = NULL;
    _dbPath           = dbPath;
    _busyRetryTimeout = 10;
  }
  return self;
}

- (BOOL) isOpen
{
  if (_db)
    return YES;
  else
    return NO;
}

- (BOOL) openReadWrite
{
  [self close];

  const char* dbpathC = [self.dbPath UTF8String];

  if (sqlite3_open_v2 (dbpathC, &_db, SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE, NULL) == SQLITE_OK) {
    return YES;
  }
  else {
    _db = NULL;

    NSLog(@"Can't open readwrite database \"%@\"", self.dbPath);
    return NO;
  }
}

- (BOOL) openReadOnly
{
  [self close];

  const char* dbpathC = [self.dbPath UTF8String];

  if (sqlite3_open_v2(dbpathC, &_db, SQLITE_OPEN_READONLY, NULL) == SQLITE_OK) {
    return YES;
  }
  else {
    _db = NULL;

    NSLog(@"Can't open readonly database \"%@\"", self.dbPath);
    return NO;
  }
}


- (void) close
{
  if ([self isOpen]) {
    //    if (sqlite3_close(_db)) {
    //      _db = NULL;
    //    }
    //    else {
    //      NSLog(@"Can't close database \"%@\"", self.dbPath);
    //    }
    sqlite3_close(_db);
    _db = NULL;
  }
}

- (BOOL) prepareSql: (NSString*)      sql
        inStatement: (sqlite3_stmt**) stmt
{
	int numOfRetries = 0;

	do {
		int rc = sqlite3_prepare_v2(_db, [sql UTF8String], -1, stmt, NULL);
		if (rc == SQLITE_OK) {
			return YES;
    }

		if (rc == SQLITE_BUSY) {
			if (numOfRetries == _busyRetryTimeout) {
				NSLog(@"SQLite Busy 1: %@", _dbPath);
				break;
			}

      usleep(10000 /* 10ms */);
		}
    else {
			NSLog(@"SQLite Prepare Failed: %s", sqlite3_errmsg(_db));
			NSLog(@" - Query: %@", sql);
			break;
		}
	}
  while (numOfRetries++ <= _busyRetryTimeout);

	return NO;
}

- (SQResultSet*) executeQuery: (NSString *) sql, ...
{
  va_list args;
	va_start(args, sql);

	NSMutableArray *argsArray = [[NSMutableArray alloc] init];
	NSUInteger i;
	for (i = 0; i < [sql length]; ++i) {
		if ([sql characterAtIndex:i] == '?')
			[argsArray addObject:va_arg(args, id)];
	}

	va_end(args);

	return [self executeQuery: sql
                  arguments: argsArray];
}

- (SQResultSet*) executeQuery: (NSString*) sql
                    arguments: (NSArray*)  args
{
  return [SQResultSet resultSetWithDB: self
                                query: sql
                            arguments: args];
}


- (BOOL) hasData:( sqlite3_stmt*) stmt
{
	int numOfRetries = 0;

	do {
    //    if (numOfRetries > 0) {
    //      NSLog(@"SQLite Busy, step %i: %@", numOfRetries, _dbPath);
    //    }

		int rc = sqlite3_step(stmt);

		if (rc == SQLITE_ROW) {
			return YES;
    }

		if (rc == SQLITE_DONE) {
			break;
    }

		if (rc == SQLITE_BUSY) {
			if (numOfRetries == _busyRetryTimeout) {
				NSLog(@"SQLite Busy 2: %@", _dbPath);
				break;
			}

      usleep(10000 /* 10ms */);
		}
    else {
			NSLog(@"SQLite Prepare Failed: %s", sqlite3_errmsg(_db));
			break;
		}
	}
  while (numOfRetries++ <= _busyRetryTimeout);

	return NO;
}

- (NSInteger) errorCode
{
	return sqlite3_errcode(_db);
}

- (NSString*) errorMessage
{
	return [NSString stringWithFormat:@"%s", sqlite3_errmsg(_db)];
}

- (void) bindObject: (id)            obj
           toColumn: (int)           idx
        inStatement: (sqlite3_stmt*) stmt
{
	if (obj == nil || obj == [NSNull null]) {
		sqlite3_bind_null(stmt, idx);
	}
  else if ([obj isKindOfClass:[NSData class]]) {
		sqlite3_bind_blob(stmt, idx, [obj bytes], (int) [obj length], SQLITE_STATIC);
	}
  else if ([obj isKindOfClass:[NSDate class]]) {
		sqlite3_bind_double(stmt, idx, [obj timeIntervalSince1970]);
	}
  else if ([obj isKindOfClass:[NSNumber class]]) {
		if (!strcmp([obj objCType], @encode(BOOL))) {
			sqlite3_bind_int(stmt, idx, [obj boolValue] ? 1 : 0);
		}
    else if (!strcmp([obj objCType], @encode(int))) {
			sqlite3_bind_int64(stmt, idx, [obj longValue]);
		}
    else if (!strcmp([obj objCType], @encode(long))) {
			sqlite3_bind_int64(stmt, idx, [obj longValue]);
		}
    else if (!strcmp([obj objCType], @encode(float))) {
			sqlite3_bind_double(stmt, idx, [obj floatValue]);
		}
    else if (!strcmp([obj objCType], @encode(double))) {
			sqlite3_bind_double(stmt, idx, [obj doubleValue]);
		}
    else {
			sqlite3_bind_text(stmt, idx, [[obj description] UTF8String], -1, SQLITE_STATIC);
		}
	}
  else {
		sqlite3_bind_text(stmt, idx, [[obj description] UTF8String], -1, SQLITE_STATIC);
	}
}

- (BOOL) executeStatament: (sqlite3_stmt *) stmt
{
	int numOfRetries = 0;
	int rc;

	do {
		rc = sqlite3_step(stmt);
		if (rc == SQLITE_OK || rc == SQLITE_DONE)
			return YES;

		if (rc == SQLITE_BUSY) {
			if (numOfRetries == _busyRetryTimeout) {
				NSLog(@"SQLite Busy 3: %@", _dbPath);
				break;
			}

      usleep(10000 /* 10ms */);
		}
    else {
			NSLog(@"SQLite Step Failed: %s", sqlite3_errmsg(_db));
			break;
		}
	}
  while (numOfRetries++ <= _busyRetryTimeout);

	return NO;
}

- (BOOL) executeNonQuery: (NSString*) sql, ...
{
	va_list args;
	va_start(args, sql);

	NSMutableArray *argsArray = [[NSMutableArray alloc] init];
	NSUInteger i;
	for (i = 0; i < [sql length]; ++i) {
		if ([sql characterAtIndex:i] == '?')
			[argsArray addObject:va_arg(args, id)];
	}

	va_end(args);

	BOOL success = [self executeNonQuery:sql arguments:argsArray];

	return success;
}

- (BOOL) executeNonQuery: (NSString*) sql
               arguments: (NSArray*)  args
{
	sqlite3_stmt *sqlStmt;

	if (![self prepareSql:sql inStatement:(&sqlStmt)]) {
		return NO;
  }

	int i = 0;
	int queryParamCount = sqlite3_bind_parameter_count(sqlStmt);
	while (i++ < queryParamCount) {
		[self bindObject:[args objectAtIndex:(NSUInteger) (i - 1)] toColumn:i inStatement:sqlStmt];
  }

	BOOL success = [self executeStatament:sqlStmt];

	sqlite3_finalize(sqlStmt);
	return success;
}

- (BOOL) commit
{
	return [self executeNonQuery:@"COMMIT TRANSACTION;"];
}

- (BOOL) rollback
{
	return [self executeNonQuery:@"ROLLBACK TRANSACTION;"];
}

- (BOOL) beginTransaction
{
	return [self executeNonQuery:@"BEGIN EXCLUSIVE TRANSACTION;"];
}

- (BOOL) beginDeferredTransaction
{
	return [self executeNonQuery:@"BEGIN DEFERRED TRANSACTION;"];
}

@end
