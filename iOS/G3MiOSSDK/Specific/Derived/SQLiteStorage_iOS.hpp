//
//  SQLiteStorage_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 16/08/12.
//
//

#ifndef __G3MiOSSDK__SQLiteStorage_iOS__
#define __G3MiOSSDK__SQLiteStorage_iOS__

#include "IStorage.hpp"

#import "SQDatabase.h"

#include <string>


class SQLiteStorage_iOS : public IStorage {
private:
  const std::string _databaseName;

  SQDatabase* _readDB;
  SQDatabase* _writeDB;

  NSLock* _lock;

  NSString* getDBPath() const;

  void showStatistics() const;

  bool addSkipBackupAttributeToItemAtPath(NSString* path);

public:
  void rawSave(NSString* table,
               NSString* name,
               NSData* contents,
               const TimeInterval& timeToExpires);

  SQLiteStorage_iOS(const std::string &databaseName);

  virtual ~SQLiteStorage_iOS() {
  }


  IByteBufferResult readBuffer(const URL& url,
                               bool readExpired);

  IImageResult readImage(const URL& url,
                         bool readExpired);


  void saveBuffer(const URL& url,
                  const IByteBuffer* buffer,
                  const TimeInterval& timeToExpires,
                  bool saveInBackground);

  void saveImage(const URL& url,
                 const IImage* buffer,
                 const TimeInterval& timeToExpires,
                 bool saveInBackground);


  void onResume(const G3MContext* context) {
  }

  void onPause(const G3MContext* context) {
  }

  void onDestroy(const G3MContext* context) {
  }

  bool isAvailable() {
    return (_readDB != NULL) && (_writeDB != NULL);
  }
  
};

#endif
