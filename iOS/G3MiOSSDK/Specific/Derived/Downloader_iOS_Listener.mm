//
//  Downloader_iOS_Listener.m
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import "Downloader_iOS_Listener.h"

#import "IFactory.hpp"

@implementation Downloader_iOS_Listener

-(id)initWithCPPBufferListener:(IBufferDownloadListener*)cppListener
                deleteListener:(bool)deleteListener
{
  self = [super init];
  if (self) {
    _cppBufferListener = cppListener;
    _cppImageListener  = NULL;
    _deleteListener    = deleteListener;
  }
  return self;
}

-(id)initWithCPPImageListener:(IImageDownloadListener*)cppListener
               deleteListener:(bool)deleteListener
{
  self = [super init];
  if (self) {
    _cppBufferListener = NULL;
    _cppImageListener  = cppListener;
    _deleteListener    = deleteListener;
  }
  return self;
}

-(void) onDownloadURL:(const URL&)url
               buffer:(const IByteBuffer*)buffer
{
  if (_cppBufferListener) {
    _cppBufferListener->onDownload(url, buffer);
  }
  if (_cppImageListener) {
    IImage* image = GFactory.createImageFromData(buffer);
    _cppImageListener->onDownload(url, image);
    delete image;
  }
}

-(void) onErrorURL:(const URL&)url
{
  if (_cppBufferListener) {
    _cppBufferListener->onError(url);
  }
  if (_cppImageListener) {
    _cppImageListener->onError(url);
  }
}

-(void) onCancel:(const URL&)url
{
  if (_cppBufferListener) {
    _cppBufferListener->onCancel(url);
  }
  if (_cppImageListener) {
    _cppImageListener->onCancel(url);
  }
}

-(void) onCanceledDownloadURL:(const URL&)url
                       buffer:(const IByteBuffer*)buffer
{
  if (_cppBufferListener) {
    _cppBufferListener->onCanceledDownload(url, buffer);
  }
  if (_cppImageListener) {
    IImage* image = GFactory.createImageFromData(buffer);
    _cppImageListener->onCanceledDownload(url, image);
    delete image;
  }
}

-(void) dealloc
{
  if (_deleteListener) {
    if (_cppBufferListener) {
      delete _cppBufferListener;
    }
    if (_cppImageListener) {
      delete _cppImageListener;
    }
  }
}

@end
