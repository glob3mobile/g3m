//
//  Downloader_iOS_Listener.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#include "IBufferDownloadListener.hpp"

// Objective-C wrapper for C++ IBufferDownloadListener
@interface Downloader_iOS_Listener : NSObject
{
  IBufferDownloadListener* _cppListener;
  bool                     _deleteListener;
}

-(id)initWithCPPListener:(IBufferDownloadListener*)cppListener
          deleteListener: (bool)deleteListener;


//const URL& url,
//const IByteBuffer& data

-(void) onDownloadURL:(const URL&) url
               buffer:(const IByteBuffer&) data;

-(void) onErrorURL:(const URL&) url
            buffer:(const IByteBuffer&) data;

-(void) onCancel:(const URL&) url;

-(void) onCanceledDownloadURL:(const URL&) url
                       buffer:(const IByteBuffer&) data;

-(void) dealloc;

@end
