//
//  CacheFiller.m
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 27/2/15.
//
//

#include "CacheFiller.h"


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
    
    
    NSData* data = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]];
    
    [_lock lock];
    
    NSString* statement = [NSString stringWithFormat:@"INSERT OR REPLACE INTO %@ (name, contents, expiration) VALUES (?, ?, ?)", @"image2"];
    
    NSDate* expiration = [NSDate dateWithTimeIntervalSinceNow:timeToExpires.seconds()];
    
    NSString* expirationS = [NSString stringWithFormat:@"%f", [expiration timeIntervalSince1970]];
    
    if (![_writeDB executeNonQuery:statement, name, contents, expirationS]) {
      printf("Can't save \"%s\"\n",  [name cStringUsingEncoding:NSUTF8StringEncoding ] );
    }
    
    [_lock unlock];
  
  }
  
}


@end