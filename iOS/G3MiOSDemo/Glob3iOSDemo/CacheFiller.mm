//
//  CacheFiller.m
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 27/2/15.
//
//

#include "CacheFiller.h"

#include <G3MiOSSDK/TimeInterval.hpp>


@implementation CacheFiller


- (id) initWithDBPath:(NSString*) dbPath{
  
  self = [super init];
  
  _lock = [[NSLock alloc] init];
  if(self){
    _db = [SQDatabase databaseWithPath:dbPath];
    if (!_db) {
      printf("Can't open write-database");
    }
    else {
      [_db openReadWrite];
    }
  }
  
  return self;
}

- (void) saveImage:(NSString*) url{
  
  if (_db != nil){
    
    
    NSData* contents = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]];
    
    printf("DOWNLOADED %d bytes\n", [contents length]);
    
    [_lock lock];
    
    TimeInterval timeToExpires = TimeInterval::forever();
    
    NSString* statement = [NSString stringWithFormat:@"INSERT OR REPLACE INTO %@ (name, contents, expiration) VALUES (?, ?, ?)", @"image2"];
    
    NSDate* expiration = [NSDate dateWithTimeIntervalSinceNow:timeToExpires.seconds()];
    
    NSString* expirationS = [NSString stringWithFormat:@"%f", [expiration timeIntervalSince1970]];
    
    if (![_db executeNonQuery:statement, url, contents, expirationS]) {
      printf("Can't save \"%s\"\n",  [url cStringUsingEncoding:NSUTF8StringEncoding ] );
    }
    
    [_lock unlock];
    
  }
  
}

-(void) close{
  [_db close];
}

-(void) saveImagesWithURLsInFile:(NSString*) urlFileName ofType:(NSString*) fileExtension{
  NSString* urls = [[NSString alloc] initWithContentsOfFile:[[NSBundle mainBundle] pathForResource:urlFileName ofType:fileExtension]
                                                   encoding:NSUTF8StringEncoding
                                                      error:nil];
  
  NSArray* lines = [urls componentsSeparatedByString:@"\n"];
  
  for(unsigned int i = 0; i < [lines count]; i++){
    NSString* url = [lines objectAtIndex:i];
    if ([url length] > 3){
      [self saveImage:[lines objectAtIndex:i]];
    }
  }
}

+(NSString*) copyDBFromBundleToDocuments:(NSString*) fileName ofType:(NSString*) fileExtension{
  NSString* cacheLocation = [[NSBundle mainBundle] pathForResource: fileName ofType: fileExtension];
  
  if (cacheLocation != nil){
    
    NSFileManager* fsm = [[NSFileManager alloc] init];
    NSArray*  paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString* documentsDirectory = [paths objectAtIndex:0];
    NSString* dbPath = [documentsDirectory stringByAppendingPathComponent: [fileName stringByAppendingString:fileExtension] ];
    
    if (![fsm fileExistsAtPath:dbPath]){
      [fsm copyItemAtPath:cacheLocation toPath:dbPath error:nil];
    }
    
    return dbPath;
  }
  return nil;
}


@end