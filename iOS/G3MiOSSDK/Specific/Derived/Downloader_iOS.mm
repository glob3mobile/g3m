//
//  Downloader_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Downloader_iOS.hpp"

#import "Downloader_iOS_Handler.h"


Downloader_iOS::Downloader_iOS(int memoryCapacity,
                               int diskCapacity,
                               std::string diskPath) :
_requestIdCounter(0)
{
  _downloadingHandlers = [NSMutableDictionary dictionary];
  
  
  NSURLCache *sharedCache = [[NSURLCache alloc] initWithMemoryCapacity: memoryCapacity
                                                          diskCapacity: diskCapacity
                                                              diskPath: toNSString(diskPath)];
  [NSURLCache setSharedURLCache:sharedCache];
  
}

void Downloader_iOS::cancelRequest(long requestId) {
  int __TODO_new_downloader;
}

long Downloader_iOS::request(const URL &url,
                             long priority,
                             IDownloadListener* cppListener) {
  int __TODO_new_downloader;
  
  const long requestId = _requestIdCounter++;
  
  NSURL* nsURL = [NSURL URLWithString: toNSString(url.getPath())];
  
  Downloader_iOS_Listener* iosListener = [[Downloader_iOS_Listener alloc] initWithCPPListener: cppListener];
  
  Downloader_iOS_Handler* handler = [_downloadingHandlers objectForKey: nsURL];
  if (handler) {
    [handler addListener: iosListener
                priority: priority
               requestId: requestId];
  }
  else {
    handler = [[Downloader_iOS_Handler alloc] initWithUrl: nsURL
                                                 listener: iosListener
                                                 priority: priority
                                                requestId: requestId];
    [_downloadingHandlers setObject: handler
                             forKey: nsURL];
  }
  
  return requestId;
  
  
  //    NSURLRequest *request = [NSURLRequest requestWithURL:nsURL
  //                                             cachePolicy:NSURLRequestUseProtocolCachePolicy
  //                                         timeoutInterval:60.0];
  //    
  //    // create the connection with the request
  //    // and start loading the data
  //    NSURLConnection *connection=[[NSURLConnection alloc] initWithRequest: request
  //                                                                delegate: handler];
  //    if (connection) {
  //      // Create the NSMutableData to hold the received data.
  //      // receivedData is an instance variable declared elsewhere.
  //      receivedData = [NSMutableData data];
  //    } else {
  //      // Inform the user that the connection failed.
  //    }
  
}
