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

#include "IThreadUtils.hpp"
#include "Context.hpp"
#include "URL.hpp"
#include "TimeInterval.hpp"

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
  _lock = [[NSLock alloc] init];

  _writeDB = [SQDatabase databaseWithPath:getDBPath()];
  if (!_writeDB) {
    printf("Can't open write-database \"%s\"\n", databaseName.c_str());
  }
  else {
    [_writeDB openReadWrite];

    //    SQResultSet *imagesTable = [_writeDB executeQuery:@""];

    //    xxx = [_writeDB executeQuery:@"SELECT name FROM sqlite_master WHERE type='table' AND name='buffer';"];
    if (![_writeDB executeNonQuery:@"DROP TABLE IF EXISTS buffer;"]) {
      printf("Can't drop table \"buffer\" from database \"%s\"\n",
             databaseName.c_str());
      return;
    }

    if (![_writeDB executeNonQuery:@"DROP TABLE IF EXISTS image;"]) {
      printf("Can't drop table \"image\" from database \"%s\"\n",
             databaseName.c_str());
      return;
    }
      
      if (![_writeDB executeNonQuery:@"DROP TABLE IF EXISTS buffer2;"]) {
          printf("Can't drop table \"buffer\" from database \"%s\"\n",
                 databaseName.c_str());
          return;
      }
      
      if (![_writeDB executeNonQuery:@"DROP TABLE IF EXISTS image2;"]) {
          printf("Can't drop table \"image\" from database \"%s\"\n",
                 databaseName.c_str());
          return;
      }

    if (![_writeDB executeNonQuery:@"CREATE TABLE IF NOT EXISTS buffer3 (name TEXT, contents TEXT, expiration_ms TEXT);"]) {
      printf("Can't create table \"buffer\" on database \"%s\"\n",
             databaseName.c_str());
      return;
    }

    if (![_writeDB executeNonQuery:@"CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer3(name);"]) {
      printf("Can't create index \"buffer_name\" on database \"%s\"\n",
             databaseName.c_str());
      return;
    }

    if (![_writeDB executeNonQuery:@"CREATE TABLE IF NOT EXISTS image3 (name TEXT, contents TEXT, expiration_ms TEXT);"]) {
      printf("Can't create table \"image\" on database \"%s\"\n",
             databaseName.c_str());
      return;
    }

    if (![_writeDB executeNonQuery:@"CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image3(name);"]) {
      printf("Can't create index \"image_name\" on database \"%s\"\n",
             databaseName.c_str());
      return;
    }

    _readDB = [SQDatabase databaseWithPath:getDBPath()];
    if (!_readDB) {
      printf("Can't open read-database \"%s\"\n", databaseName.c_str());
    }
    else {
      [_readDB openReadOnly];
    }

    if (false) {
      showStatistics();
    }
  }
}

void SQLiteStorage_iOS::showStatistics() const {
  SQResultSet* rs1 = [_readDB executeQuery:@"SELECT COUNT(*), SUM(LENGTH(contents)) FROM buffer3"];
  if ([rs1 next]) {
    NSInteger count     = [rs1 integerColumnByIndex: 0];
    NSInteger usedSpace = [rs1 integerColumnByIndex: 1];

    NSLog(@"Initialized Storage on DB \"%@\", buffers=%d, usedSpace=%fMb",
          toNSString(_databaseName), //getDBPath(),
          count,
          (float) ((double)usedSpace / 1024 / 1024));
  }

  [rs1 close];


  SQResultSet* rs2 = [_readDB executeQuery:@"SELECT COUNT(*), SUM(LENGTH(contents)) FROM image3"];
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


//bool SQLiteStorage_iOS::containsBuffer(const URL& url) {
//  NSString* name = toNSString(url.getPath());
//
//  SQResultSet* rs = [_readDB executeQuery:@"SELECT 1 FROM buffer3 WHERE (name = ?)", name];
//
//  BOOL hasAny = [rs next];
//
//  [rs close];
//
//  return hasAny;
//}

void SQLiteStorage_iOS::rawSave(NSString* table,
                                NSString* name,
                                NSData* contents,
                                const TimeInterval& timeToExpires) {
  [_lock lock];

  NSString* statement = [NSString stringWithFormat:@"INSERT OR REPLACE INTO %@ (name, contents, expiration_ms) VALUES (?, ?, ?)", table ];
    
//NSDate* expirationSeg = [NSDate dateWithTimeIntervalSinceNow:timeToExpires.seconds()];
//const double currentTime = [timeIntervalSince1970];
    
  const long expirationTimeInMills = (long)((timeToExpires.seconds() +CACurrentMediaTime())*1000);
  NSString* expirationS = [NSString stringWithFormat:@"%ld", expirationTimeInMills];

  //NSString* expirationS = [NSString stringWithFormat:@"%f", [expirationSeg timeIntervalSince1970]];

  if (![_writeDB executeNonQuery:statement, name, contents, expirationS]) {
    printf("Can't save \"%s\"\n",  [name cStringUsingEncoding:NSUTF8StringEncoding ] );
  }

  [_lock unlock];
}

class SaverTask : public GTask {
private:
  SQLiteStorage_iOS* _storage;
  NSString*          _table;
  NSString*          _name;
  NSData*            _contents;
  const TimeInterval _timeToExpires;

public:
  SaverTask(SQLiteStorage_iOS* storage,
            NSString* table,
            NSString* name,
            NSData* contents,
            const TimeInterval timeToExpires) :
  _storage(storage),
  _table(table),
  _name(name),
  _contents(contents),
  _timeToExpires(timeToExpires)
  {

  }

  void run(const G3MContext* context) {
    _storage->rawSave(_table, _name, _contents, _timeToExpires);
  }
};

void SQLiteStorage_iOS::saveBuffer(const URL& url,
                                   const IByteBuffer* buffer,
                                   const TimeInterval& timeToExpires,
                                   bool saveInBackground) {
  //  const ByteBuffer_iOS* buffer_iOS = (const ByteBuffer_iOS*) buffer;
  //
  //  NSString* name = toNSString(url.getPath());
  //  NSData* contents = [NSData dataWithBytes: buffer_iOS->getPointer()
  //                                    length: buffer_iOS->size()];
  //
  //  if (![_db executeNonQuery:@"INSERT OR REPLACE INTO buffer (name, contents) VALUES (?, ?)", name, contents]) {
  //    printf("Can't save \"%s\"\n", url.getPath().c_str());
  //  }

  const ByteBuffer_iOS* buffer_iOS = (const ByteBuffer_iOS*) buffer;

  NSString* name = toNSString(url.getPath());
  NSData* contents = [NSData dataWithBytes: buffer_iOS->getPointer()
                                    length: buffer_iOS->size()];

  if (saveInBackground) {
    _context->getThreadUtils()->invokeInBackground(new SaverTask(this, @"buffer3", name, contents, timeToExpires),
                                                   true);
  }
  else {
    rawSave(@"buffer3", name, contents, timeToExpires);
  }
}

IByteBuffer* SQLiteStorage_iOS::readBuffer(const URL& url) {
  IByteBuffer* result = NULL;

  NSString* name = toNSString(url.getPath());
  SQResultSet* rs = [_readDB executeQuery:@"SELECT contents, expiration_ms FROM buffer3 WHERE (name = ?)", name];
  if ([rs next]) {
    NSData* nsData = [rs dataColumnByIndex: 0];
    const double expirationIntervalMill = [[rs stringColumnByIndex:1] doubleValue];
    NSDate* expirationSeg = [NSDate dateWithTimeIntervalSince1970:(expirationIntervalMill/1000)];

    if ([expirationSeg compare:[NSDate date]] == NSOrderedDescending) {
      NSUInteger length = [nsData length];
      unsigned char* bytes = new unsigned char[length];
      [nsData getBytes: bytes
                length: length];

      result = IFactory::instance()->createByteBuffer(bytes, length);
    }
  }

  [rs close];

  return result;
}

//bool SQLiteStorage_iOS::containsImage(const URL& url) {
//  NSString* name = toNSString(url.getPath());
//
//  SQResultSet* rs = [_readDB executeQuery:@"SELECT 1 FROM image3 WHERE (name = ?)", name];
//
//  BOOL hasAny = [rs next];
//
//  [rs close];
//
//  return hasAny;
//}

void SQLiteStorage_iOS::saveImage(const URL& url,
                                  const IImage* image,
                                  const TimeInterval& timeToExpires,
                                  bool saveInBackground) {
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

  //  if (![_db executeNonQuery:@"INSERT OR REPLACE INTO image (name, contents) VALUES (?, ?)", name, contents]) {
  //    printf("Can't save \"%s\"\n", url.getPath().c_str());
  //  }

  if (saveInBackground) {
    _context->getThreadUtils()->invokeInBackground(new SaverTask(this, @"image3", name, contents, timeToExpires),
                                                   true);
  }
  else {
    rawSave(@"image3", name, contents, timeToExpires);
  }
}

IImage* SQLiteStorage_iOS::readImage(const URL& url) {
  IImage* result = NULL;

  NSString* name = toNSString(url.getPath());
  SQResultSet* rs = [_readDB executeQuery:@"SELECT contents, expiration_ms FROM image3 WHERE (name = ?)", name];
  if ([rs next]) {
    NSData* data = [rs dataColumnByIndex: 0];
    const double expirationIntervalMill = [[rs stringColumnByIndex:1] doubleValue];
    NSDate* expirationSeg = [NSDate dateWithTimeIntervalSince1970:(expirationIntervalMill/1000)];

    if ([expirationSeg compare:[NSDate date]] == NSOrderedDescending) {
      UIImage* uiImage = [UIImage imageWithData:data];
      if (uiImage) {
        result = new Image_iOS(uiImage,
                               NULL/* data is not needed */);
      }
      else {
        ILogger::instance()->logError("Can't create image with contents of storage.");
      }
    }
  }
  
  [rs close];
  
  return result;
}
