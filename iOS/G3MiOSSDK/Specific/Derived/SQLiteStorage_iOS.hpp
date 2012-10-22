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

class SQLiteStorage_iOS : public IStorage {
private:
  const std::string _databaseName;
  
  SQDatabase* _readDB;
  SQDatabase* _writeDB;
  
  NSLock* _lock;
  
  NSString* toNSString(const std::string& cppStr) const {
    return [ NSString stringWithCString: cppStr.c_str()
                               encoding: NSUTF8StringEncoding ];
  }
  
  NSString* getDBPath() const;
  
  void showStatistics() const;
  
public:
  void rawSave(NSString* table,
               NSString* name,
               NSData* contents);
  
  SQLiteStorage_iOS(const std::string &databaseName);
  
  virtual ~SQLiteStorage_iOS() {
  }
  
  bool containsBuffer(const URL& url);
  
  void saveBuffer(const URL& url,
                  const IByteBuffer* buffer,
                  bool saveInBackground);
  
  const IByteBuffer* readBuffer(const URL& url);
  
  
  bool containsImage(const URL& url);
  
  void saveImage(const URL& url,
                 const IImage* buffer,
                 bool saveInBackground);
  
  const IImage* readImage(const URL& url);
  
  void onResume(const InitializationContext* ic) {
    
  }
  
  void onPause(const InitializationContext* ic) {
    
  }
  
  bool isAvailable() {
    return (_readDB != NULL) && (_writeDB != NULL);
  }
  
};

#endif
