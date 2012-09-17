//
//  SQLiteStorage_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 16/08/12.
//
//

#include "SQLiteStorage_iOS.hpp"

#include "IFactory.hpp"
#include "ByteBuffer_iOS.hpp"
#include "Image_iOS.hpp"
#include "ILogger.hpp"

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
  
  if (![_db executeNonQuery:@"CREATE TABLE IF NOT EXISTS buffer (name TEXT, contents TEXT);"]) {
    printf("Can't create table \"buffer\" on database \"%s\"\n",
           databaseName.c_str());
    return;
  }
  
  if (![_db executeNonQuery:@"CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer(name);"]) {
    printf("Can't create index \"buffer_name\" on database \"%s\"\n",
           databaseName.c_str());
    return;
  }
  
  if (![_db executeNonQuery:@"CREATE TABLE IF NOT EXISTS image (name TEXT, contents TEXT);"]) {
    printf("Can't create table \"image\" on database \"%s\"\n",
           databaseName.c_str());
    return;
  }
  
  if (![_db executeNonQuery:@"CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image(name);"]) {
    printf("Can't create index \"image_name\" on database \"%s\"\n",
           databaseName.c_str());
    return;
  }
  
  if (false) {
    showStatistics();
  }
}

void SQLiteStorage_iOS::showStatistics() const {
  SQResultSet* rs1 = [_db executeQuery:@"SELECT COUNT(*), SUM(LENGTH(contents)) FROM buffer"];
  if ([rs1 next]) {
    NSInteger count     = [rs1 integerColumnByIndex: 0];
    NSInteger usedSpace = [rs1 integerColumnByIndex: 1];
    
    NSLog(@"Initialized Storage on DB \"%@\", buffers=%d, usedSpace=%fMb",
          toNSString(_databaseName), //getDBPath(),
          count,
          (float) ((double)usedSpace / 1024 / 1024));
  }
  
  [rs1 close];
  
  
  SQResultSet* rs2 = [_db executeQuery:@"SELECT COUNT(*), SUM(LENGTH(contents)) FROM image"];
  if ([rs2 next]) {
    NSInteger count     = [rs2 integerColumnByIndex: 0];
    NSInteger usedSpace = [rs2 integerColumnByIndex: 1];
    
    NSLog(@"Initialized Storage on DB \"%@\", images=%d, usedSpace=%fMb",
          toNSString(_databaseName), //getDBPath(),
          count,
          (float) ((double)usedSpace / 1024 / 1024));
  }
  
  [rs2 close];
}


bool SQLiteStorage_iOS::containsBuffer(const URL& url) {
  NSString* name = toNSString(url.getPath());
  
  SQResultSet* rs = [_db executeQuery:@"SELECT 1 FROM buffer WHERE (name = ?)", name];
  
  BOOL hasAny = [rs next];
  
  [rs close];
  
  return hasAny;
}

void SQLiteStorage_iOS::saveBuffer(const URL& url,
                                   const IByteBuffer* buffer) {
  
  const ByteBuffer_iOS* buffer_iOS = (const ByteBuffer_iOS*) buffer;
  
  NSString* name = toNSString(url.getPath());
  NSData* contents = [NSData dataWithBytes: buffer_iOS->getPointer()
                                    length: buffer_iOS->size()];
  
  if (![_db executeNonQuery:@"INSERT OR REPLACE INTO buffer (name, contents) VALUES (?, ?)", name, contents]) {
    printf("Can't save \"%s\"\n", url.getPath().c_str());
  }
}

const IByteBuffer* SQLiteStorage_iOS::readBuffer(const URL& url) {
  IByteBuffer* result = NULL;
  
  NSString* name = toNSString(url.getPath());
  SQResultSet* rs = [_db executeQuery:@"SELECT contents FROM buffer WHERE (name = ?)", name];
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

bool SQLiteStorage_iOS::containsImage(const URL& url) {
  NSString* name = toNSString(url.getPath());
  
  SQResultSet* rs = [_db executeQuery:@"SELECT 1 FROM image WHERE (name = ?)", name];
  
  BOOL hasAny = [rs next];
  
  [rs close];
  
  return hasAny;
}

void SQLiteStorage_iOS::saveImage(const URL& url,
                                  const IImage* image) {
  
  const Image_iOS* image_iOS = (const Image_iOS*) image;
  UIImage* uiImage = image_iOS->getUIImage();
  
  NSString* name = toNSString(url.getPath());
  
  NSData* contents = image_iOS->getSourceBuffer();
  if (contents == NULL) {
    contents = UIImagePNGRepresentation(uiImage);
  }
  else {
    image_iOS->releaseSourceBuffer();
  }
  
  if (![_db executeNonQuery:@"INSERT OR REPLACE INTO image (name, contents) VALUES (?, ?)", name, contents]) {
    printf("Can't save \"%s\"\n", url.getPath().c_str());
  }
}

const IImage* SQLiteStorage_iOS::readImage(const URL& url) {
  IImage* result = NULL;
  
  NSString* name = toNSString(url.getPath());
  SQResultSet* rs = [_db executeQuery:@"SELECT contents FROM image WHERE (name = ?)", name];
  if ([rs next]) {
    NSData* data = [rs dataColumnByIndex: 0];
    
    UIImage* uiImage = [UIImage imageWithData:data];
    if (uiImage) {
      result = new Image_iOS(uiImage,
                             NULL/* data is not needed */);
    }
    else {
      ILogger::instance()->logError("Can't create image with contents of storage.");
    }
  }
  
  [rs close];
  
  return result;
}
