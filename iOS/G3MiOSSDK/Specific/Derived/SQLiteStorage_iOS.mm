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
#import "NSString_CppAdditions.h"


NSString* SQLiteStorage_iOS::getDBPath() const {
  NSArray*  paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
  NSString* documentsDirectory = [paths objectAtIndex:0];
  NSString* dbPath = [documentsDirectory stringByAppendingPathComponent: [NSString stringWithCppString: _databaseName] ];
  //NSLog(@"dbPath=%@", dbPath);
  return dbPath;
}

bool SQLiteStorage_iOS::addSkipBackupAttributeToItemAtPath(NSString* path) {
  assert([[NSFileManager defaultManager] fileExistsAtPath: path]);

  NSURL* url = [NSURL URLWithString: [NSString stringWithFormat:@"file://%@", path]];

  NSError *error = nil;
  BOOL success = [url setResourceValue: [NSNumber numberWithBool: YES]
                                forKey: NSURLIsExcludedFromBackupKey
                                 error: &error];
  if (!success) {
    NSLog(@"Error excluding %@ from backup %@", url, error);
  }
  return success;
}

SQLiteStorage_iOS::SQLiteStorage_iOS(const std::string &databaseName) :
_databaseName(databaseName)
{
  _lock = [[NSLock alloc] init];

  NSString* dbPath = getDBPath();

  _writeDB = [SQDatabase databaseWithPath:dbPath];
  if (!_writeDB) {
    printf("Can't open write-database \"%s\"\n", databaseName.c_str());
  }
  else {
    [_writeDB openReadWrite];

    addSkipBackupAttributeToItemAtPath(dbPath);

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

    if (![_writeDB executeNonQuery:@"CREATE TABLE IF NOT EXISTS buffer2 (name TEXT, contents TEXT, expiration TEXT);"]) {
      printf("Can't create table \"buffer\" on database \"%s\"\n",
             databaseName.c_str());
      return;
    }

    if (![_writeDB executeNonQuery:@"CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer2(name);"]) {
      printf("Can't create index \"buffer_name\" on database \"%s\"\n",
             databaseName.c_str());
      return;
    }

    if (![_writeDB executeNonQuery:@"CREATE TABLE IF NOT EXISTS image2 (name TEXT, contents TEXT, expiration TEXT);"]) {
      printf("Can't create table \"image\" on database \"%s\"\n",
             databaseName.c_str());
      return;
    }

    if (![_writeDB executeNonQuery:@"CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image2(name);"]) {
      printf("Can't create index \"image_name\" on database \"%s\"\n",
             databaseName.c_str());
      return;
    }

    _readDB = [SQDatabase databaseWithPath:dbPath];
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
  SQResultSet* rs1 = [_readDB executeQuery:@"SELECT COUNT(*), SUM(LENGTH(contents)) FROM buffer2"];
  if ([rs1 next]) {
    NSInteger count     = [rs1 integerColumnByIndex: 0];
    NSInteger usedSpace = [rs1 integerColumnByIndex: 1];

    NSLog(@"Initialized Storage on DB \"%@\", buffers=%d, usedSpace=%fMb",
          [NSString stringWithCppString: _databaseName],
          count,
          (float) ((double)usedSpace / 1024 / 1024));
  }

  [rs1 close];


  SQResultSet* rs2 = [_readDB executeQuery:@"SELECT COUNT(*), SUM(LENGTH(contents)) FROM image2"];
  if ([rs2 next]) {
    NSInteger count     = [rs2 integerColumnByIndex: 0];
    NSInteger usedSpace = [rs2 integerColumnByIndex: 1];

    NSLog(@"Initialized Storage on DB \"%@\", images=%d, usedSpace=%fMb",
          [NSString stringWithCppString: _databaseName],
          count,
          (float) ((double)usedSpace / 1024 / 1024));
  }

  [rs2 close];
}

void SQLiteStorage_iOS::rawSave(NSString* table,
                                NSString* name,
                                NSData* contents,
                                const TimeInterval& timeToExpires) {
  [_lock lock];

  NSString* statement = [NSString stringWithFormat:@"INSERT OR REPLACE INTO %@ (name, contents, expiration) VALUES (?, ?, ?)", table ];

  NSDate* expiration = [NSDate dateWithTimeIntervalSinceNow:timeToExpires.seconds()];

  NSString* expirationS = [NSString stringWithFormat:@"%f", [expiration timeIntervalSince1970]];

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
  const ByteBuffer_iOS* buffer_iOS = (const ByteBuffer_iOS*) buffer;

  NSString* name     = [NSString stringWithCppString: url.getPath()];
  NSData*   contents = [NSData dataWithBytes: buffer_iOS->getPointer()
                                      length: buffer_iOS->size()];

  if (saveInBackground) {
    _context->getThreadUtils()->invokeInBackground(new SaverTask(this, @"buffer2", name, contents, timeToExpires),
                                                   true);
  }
  else {
    rawSave(@"buffer2", name, contents, timeToExpires);
  }
}

void SQLiteStorage_iOS::saveImage(const URL& url,
                                  const IImage* image,
                                  const TimeInterval& timeToExpires,
                                  bool saveInBackground) {
  const Image_iOS* image_iOS = (const Image_iOS*) image;
  UIImage* uiImage = image_iOS->getUIImage();

  NSString* name = [NSString stringWithCppString: url.getPath()];

  NSData* contents = image_iOS->getSourceBuffer();
  if (contents == NULL) {
    contents = UIImagePNGRepresentation(uiImage);
  }
  else {
    image_iOS->releaseSourceBuffer();
  }

  if (saveInBackground) {
    _context->getThreadUtils()->invokeInBackground(new SaverTask(this, @"image2", name, contents, timeToExpires),
                                                   true);
  }
  else {
    rawSave(@"image2", name, contents, timeToExpires);
  }
}

IImageResult SQLiteStorage_iOS::readImage(const URL& url,
                                          bool readExpired) {
//  @autoreleasepool {
    //  NSDate* startAll = [NSDate date];

    IImage* image = NULL;
    bool expired = false;

    //  double parsedTime = 0;

    NSString* name = [NSString stringWithCppString: url.getPath()];
    SQResultSet* rs = [_readDB executeQuery:@"SELECT contents, expiration FROM image2 WHERE (name = ?)", name];
    if ([rs next]) {
      NSData* data = [rs dataColumnByIndex: 0];
      const double expirationInterval = [[rs stringColumnByIndex:1] doubleValue];
      NSDate* expiration = [NSDate dateWithTimeIntervalSince1970:expirationInterval];

      expired = ( [expiration compare:[NSDate date]] != NSOrderedDescending );

      if (readExpired || !expired) {
        //      NSDate* startParse = [NSDate date];
        UIImage* uiImage = [UIImage imageWithData:data];
        //      parsedTime = ([startParse timeIntervalSinceNow] * -1000.0);

        if (uiImage) {
          image = new Image_iOS(uiImage,
                                NULL /* data is not needed */);
        }
        else {
          ILogger::instance()->logError("Can't create image with contents of storage.");
        }
      }
    }

    [rs close];

    //  NSLog(@"STORAGE: read image in %f (parse=%f)",
    //        ([startAll timeIntervalSinceNow] * -1000.0),
    //        parsedTime);
    
    return IImageResult(image, expired);
//  }
}


IByteBufferResult SQLiteStorage_iOS::readBuffer(const URL& url,
                                                bool readExpired) {
//  @autoreleasepool {
    IByteBuffer* buffer = NULL;
    bool expired = false;

    NSString* name = [NSString stringWithCppString: url.getPath()];
    SQResultSet* rs = [_readDB executeQuery:@"SELECT contents, expiration FROM buffer2 WHERE (name = ?)", name];
    if ([rs next]) {
      NSData* nsData = [rs dataColumnByIndex: 0];
      const double expirationInterval = [[rs stringColumnByIndex:1] doubleValue];
      NSDate* expiration = [NSDate dateWithTimeIntervalSince1970:expirationInterval];

      expired = [expiration compare:[NSDate date]] != NSOrderedDescending;

      if (readExpired || !expired) {
        NSUInteger length = [nsData length];
        unsigned char* bytes = new unsigned char[length];
        [nsData getBytes: bytes
                  length: length];

        buffer = IFactory::instance()->createByteBuffer(bytes, length);
      }
    }

    [rs close];
    
    return IByteBufferResult(buffer, expired);
//  }
}
