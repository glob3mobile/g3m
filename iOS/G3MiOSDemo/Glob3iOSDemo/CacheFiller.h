//
//  CacheFiller.hpp
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 27/2/15.
//
//

#ifndef G3MiOSDemo_CacheFiller_hpp
#define G3MiOSDemo_CacheFiller_hpp


#import "SQDatabase.h"

@interface CacheFiller: NSObject{
  SQDatabase* _db;
  
  NSLock* _lock;
}

- (id) initWithDBPath:(NSString*) dbPath;

- (void) saveImage:(NSString*) url;

@end


#endif
