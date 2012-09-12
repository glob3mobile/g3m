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
          deleteListener:(bool)deleteListener
{
  self = [super init];
  if (self) {
    _cppListener    = cppListener;
    _deleteListener = deleteListener;
  }
  return self;
}

-(void) onDownloadURL:(const URL&)url
               buffer:(const IByteBuffer&)data
{
  if (_cppListener) {
    _cppListener->onDownload(url, data);
  }
}

-(void) onErrorURL:(const URL&)url
            buffer:(const IByteBuffer&)data
{
  if (_cppListener) {
    _cppListener->onError(url, data);
  }
}

-(void) onCancel:(const URL&)url
{
  if (_cppListener) {
    _cppListener->onCancel(url);
  }
}

-(void) onCanceledDownloadURL:(const URL&)url
                       buffer:(const IByteBuffer&)data
{
  if (_cppListener) {
    _cppListener->onCanceledDownload(url, data);
  }
}

-(void) dealloc
{
  if (_cppListener) {
    if (_deleteListener) {
      delete _cppListener;
    }
  }
}

@end
