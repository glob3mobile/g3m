//
//  SQLiteStorage_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 16/08/12.
//
//

#include "SQLiteStorage_iOS.hpp"

#include "ByteBuffer_iOS.hpp"
#include "IFactory.hpp"

NSString* SQLiteStorage_iOS::getDBPath() const {
  
  NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
  NSString *documentsDirectory = [paths objectAtIndex:0];
  NSString *dbPath = [documentsDirectory stringByAppendingPathComponent: toNSString(_databaseName)];
  
  //NSLog(@"dbPath=%@", dbPath);
  
  return dbPath;
}


SQLiteStorage_iOS::SQLiteStorage_iOS(const std::string &databaseName) :
_databaseName(databaseName)
{
  _db = [SQDatabase databaseWithPath:getDBPath()];
  if (!_db) {
    printf("Can't open database \"%s\"\n",
           databaseName.c_str());
  }
  
  [_db open];
  
  if (![_db executeNonQuery:@"CREATE TABLE IF NOT EXISTS entry (name TEXT, contents TEXT);"]) {
    printf("Can't create table \"entry\" on database \"%s\"\n",
           databaseName.c_str());
    return;
  }
  
  if (![_db executeNonQuery:@"CREATE UNIQUE INDEX IF NOT EXISTS entry_name ON entry(name);"]) {
    printf("Can't create index \"entry_name\" on database \"%s\"\n",
           databaseName.c_str());
    return;
  }
  
  if (false) {
    showStatistics();
  }
}

void SQLiteStorage_iOS::showStatistics() const {
  SQResultSet* rs = [_db executeQuery:@"SELECT COUNT(*), SUM(LENGTH(contents)) FROM entry"];
  if ([rs next]) {
    NSInteger count     = [rs integerColumnByIndex: 0];
    NSInteger usedSpace = [rs integerColumnByIndex: 1];
    
    NSLog(@"Initialized Storage on DB \"%@\", entries=%d, usedSpace=%fMb",
          toNSString(_databaseName), //getDBPath(),
          count,
          (float) ((double)usedSpace / 1024 / 1024));
  }
  
  [rs close];
}


bool SQLiteStorage_iOS::contains(const URL& url) {
  NSString* name = toNSString(url.getPath());
  
  SQResultSet* rs = [_db executeQuery:@"SELECT 1 FROM entry WHERE (name = ?)", name];
  
  BOOL hasAny = [rs next];
  
  [rs close];
  
  return hasAny;
}

void SQLiteStorage_iOS::save(const URL& url,
                             const IByteBuffer& buffer) {
  
  const ByteBuffer_iOS* buffer_iOS = (const ByteBuffer_iOS*) &buffer;
  
  NSString* name = toNSString(url.getPath());
  NSData* contents = [NSData dataWithBytes: buffer_iOS->getPointer()
                                    length: buffer_iOS->size()];
  
  if (![_db executeNonQuery:@"INSERT OR REPLACE INTO entry (name, contents) VALUES (?, ?)", name, contents]) {
    printf("Can't save \"%s\"\n", url.getPath().c_str());
  }
}

const IByteBuffer* SQLiteStorage_iOS::read(const URL& url) {
  IByteBuffer* result = NULL;
  
  NSString* name = toNSString(url.getPath());
  SQResultSet* rs = [_db executeQuery:@"SELECT contents FROM entry WHERE (name = ?)", name];
  if ([rs next]) {
    NSData* nsData = [rs dataColumnByIndex: 0];
    
    NSUInteger length = [nsData length];
    unsigned char* bytes = new unsigned char[length];
    [nsData getBytes: bytes
              length: length];
    
    result = GFactory.createByteBuffer(bytes, length);
  }
  
  [rs close];
  
  return result;
}
