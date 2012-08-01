//
//  Downloader_iOS_Listener.m
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import "Downloader_iOS_Listener.h"

@implementation Downloader_iOS_Listener

-(id)initWithCPPListener:(IDownloadListener*)cppListener
{
  self = [super init];
  if (self) {
    _cppListener = cppListener;
  }
  return self;
}

-(void) onDownload:(Response&)response
{
  _cppListener->onDownload(response);
}

-(void) onError:(Response&)response
{
  _cppListener->onError(response);
}

-(void) onCancel:(const URL&)url
{
  _cppListener->onCancel(url);
}

@end
