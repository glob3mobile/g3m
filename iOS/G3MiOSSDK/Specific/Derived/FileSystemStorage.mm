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

bool FileSystemStorage::contains(const std::string& url)
{
  NSString *file = generateFileName(url);
  
  return [[NSFileManager defaultManager] fileExistsAtPath:file];
}

void FileSystemStorage::save(const std::string& url,
                             const ByteBuffer& buffer) {
  
  NSString *fullPath = generateFileName(url); 	
  NSFileManager *fileManager = [NSFileManager defaultManager];
  NSData *writeData = [[NSData alloc] initWithBytes: buffer.getData()
                                             length: buffer.getLength()];
  
  if (![fileManager createFileAtPath: fullPath
                            contents: writeData
                          attributes: nil]) {
    //ASSUMING DIRECTORY MISSING
    NSString* dir = [fullPath stringByDeletingLastPathComponent];
    [fileManager createDirectoryAtPath: dir
           withIntermediateDirectories: YES
                            attributes: nil
                                 error: nil];
    if (![fileManager createFileAtPath: fullPath
                              contents: writeData
                            attributes: nil]) {
      NSLog(@"ERROR WRITING FILE: %d - message: %s", errno, strerror(errno));
    }
  }
}

const ByteBuffer* FileSystemStorage::read(const std::string& url)
{
  NSString *file = generateFileName(url);
  NSData *readData = [[NSData alloc] initWithContentsOfFile:file];
  
  if (readData == nil) {
    return NULL;
  }
  
  NSUInteger length = [readData length];
  unsigned char* data = new unsigned char[length];
  [readData getBytes: data
              length: length];
  
  return new ByteBuffer(data, length);
}


NSString* FileSystemStorage::generateFileName(const std::string& url) {
  NSString* fileName = [[NSString alloc] initWithCString:url.c_str()
                                                encoding:NSUTF8StringEncoding];
  
  fileName = [fileName stringByReplacingOccurrencesOfString:@"/"
                                                 withString:@"_"];
  
  //NSLog(@"%@", fileName);
  
  return [_root stringByAppendingPathComponent:fileName];
}
