//
//  FileSystemStorage.mm
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "FileSystemStorage.hpp"

FileSystemStorage::FileSystemStorage(const std::string& root)
{
  _root = [[NSString alloc] initWithCString:root.c_str() encoding:NSUTF8StringEncoding];
}

bool FileSystemStorage::contains(std::string url)
{
  NSString *file = generateFileName(url);
  
  return [[NSFileManager defaultManager] fileExistsAtPath:file];
}

void FileSystemStorage::save(std::string url, const ByteBuffer& bb){
  
  NSString *fullPath = generateFileName(url); 	
  NSFileManager *fm = [NSFileManager defaultManager];
  NSData *writeData = [[NSData alloc] initWithBytes: bb.getData() length:bb.getDataLength()];
  
  if (![fm createFileAtPath:fullPath contents:writeData attributes:nil])
  {
    //ASSUMING DIRECTORY MISSING
    NSString* dir = [fullPath stringByDeletingLastPathComponent];
    [fm createDirectoryAtPath:dir withIntermediateDirectories:YES attributes:nil error:nil];
    if (![fm createFileAtPath:fullPath contents:writeData attributes:nil])
    {
      NSLog(@"ERROR WRITING FILE: %d - message: %s", errno, strerror(errno));
    }
  }
}

ByteBuffer FileSystemStorage::getByteBuffer(std::string url)
{
  NSString *file = generateFileName(url);
  NSData *readData = [[NSData alloc] initWithContentsOfFile:file];
  
  if (readData == nil) 
  {
    ByteBuffer bb(NULL, 0);
    return bb;
  }
  
  unsigned char * data = new unsigned char[ [readData length] ];
  [readData getBytes:data length:[readData length] ];
  ByteBuffer bb(data , [readData length]);
  
  return bb;
}


NSString* FileSystemStorage::generateFileName(const std::string& url)
{
  NSString* file = [[NSString alloc] initWithCString:url.c_str() encoding:NSUTF8StringEncoding];
  file = [file stringByReplacingOccurrencesOfString:@"/" withString:@"_"];
  file = [_root stringByAppendingPathComponent:file];
  
  //NSLog(@"%@", file);
  
  return file;
}