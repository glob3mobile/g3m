//
//  Downloader_iOS_Listener.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#include "IDownloadListener.hpp"

// Objective-C wrapper for C++ IDownloadListener
@interface Downloader_iOS_Listener : NSObject
{
  IDownloadListener* _cppListener;
}

-(id)initWithCPPListener:(IDownloadListener*)cppListener;

-(void) onDownload:(Response&)response;
-(void) onError:(Response&)response;
-(void) onCancel:(const URL&)url;

-(void) dealloc;

@end
