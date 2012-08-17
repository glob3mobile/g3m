//
//  SQLiteStorage_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 16/08/12.
//
//

#include "SQLiteStorage_iOS.hpp"

NSString* SQLiteStorage_iOS::getDBPath() const {
  
  NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
  NSString *documentsDirectory = [paths objectAtIndex:0];
  NSString *dbPath = [documentsDirectory stringByAppendingPathComponent: toNSString(_databaseName)];
  
//  NSString *tmpDirectory = NSTemporaryDirectory();
//  NSString *dbPath = [tmpDirectory stringByAppendingPathComponent: toNSString(_databaseName)];
  
//  NSLog(@"dbPath=%@", dbPath);
  
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
  
  //  statement.executeUpdate("CREATE TABLE article (id INT, title TEXT, summary TEXT, icon_name TEXT, html TEXT, importance INT, latitude NUMBER, longitude NUMBER);");
  //  statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS article_id ON article (id);");
  //  statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS article_title ON article (title);");
  //  statement.executeUpdate("CREATE INDEX IF NOT EXISTS article_importance ON article (importance);");
  //  statement.executeUpdate("CREATE INDEX IF NOT EXISTS article_latitude ON article (latitude);");
  //  statement.executeUpdate("CREATE INDEX IF NOT EXISTS article_longitude ON article (longitude);");
  //
  
  
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
  
}


bool SQLiteStorage_iOS::contains(const URL& url) {
  NSString* name = toNSString(url.getPath());
  
  SQResultSet* rs = [_db executeQuery:@"SELECT 1 FROM entry WHERE (name = ?)", name];
  
  BOOL hasAny = [rs next];
  
  [rs close];
  
  return hasAny;
}

void SQLiteStorage_iOS::save(const URL& url,
                             const ByteBuffer& buffer) {
  //  insert or replace into Book (Name, TypeID, Level, Seen) values ( ... )
  
  NSString* name = toNSString(url.getPath());
  NSData* contents = [NSData dataWithBytes: buffer.getData()
                                    length: buffer.getLength()];
  
  if (![_db executeNonQuery:@"INSERT OR REPLACE INTO entry (name, contents) VALUES (?, ?)", name, contents]) {
    printf("Can't save \"%s\"\n", url.getPath().c_str());
  }
}

const ByteBuffer* SQLiteStorage_iOS::read(const URL& url) {
  ByteBuffer* result = NULL;
  
  NSString* name = toNSString(url.getPath());
  SQResultSet* rs = [_db executeQuery:@"SELECT contents FROM entry WHERE (name = ?)", name];
  if ([rs next]) {
    NSData* nsData = [rs dataColumnByIndex: 0];
    
    NSUInteger length = [nsData length];
    unsigned char* bytes = new unsigned char[length];
    [nsData getBytes: bytes
              length: length];
    
    result = new ByteBuffer(bytes, length);
  }
  
  [rs close];
  
  return result;
}
