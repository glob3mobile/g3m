//
//  Downloader_iOS_Listener.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#include "IBufferDownloadListener.hpp"
#include "IImageDownloadListener.hpp"

// Objective-C wrapper for C++ IBufferDownloadListener
@interface Downloader_iOS_Listener : NSObject
{
  IBufferDownloadListener* _cppBufferListener;
  IImageDownloadListener*  _cppImageListener;
  bool                     _deleteListener;
}

-(id)initWithCPPBufferListener:(IBufferDownloadListener*)cppListener
                deleteListener:(bool)deleteListener;

-(id)initWithCPPImageListener:(IImageDownloadListener*)cppListener
               deleteListener:(bool)deleteListener;


//const URL& url,
//const IByteBuffer& data

-(void) onDownloadURL:(const URL&) url
                 data:(NSData*) data;

-(void) onErrorURL:(const URL&) url;

-(void) onCancel:(const URL&) url;

-(void) onCanceledDownloadURL:(const URL&) url
                         data:(NSData*) data;

-(void) dealloc;

@end
