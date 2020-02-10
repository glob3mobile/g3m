//
//  Downloader_iOS_Listener.m
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//

#import "Downloader_iOS_Listener.h"

#import "Image_iOS.hpp"
#import "ByteBuffer_iOS.hpp"


@implementation Downloader_iOS_Listener

-(id)initWithCPPBufferListener:(IBufferDownloadListener*)cppListener
                deleteListener:(bool)deleteListener
                           tag:(const std::string&)tag
{
  self = [super init];
  if (self) {
    _cppBufferListener = cppListener;
    _cppImageListener  = NULL;
    _deleteListener    = deleteListener;
    _tag               = tag;
  }
  return self;
}

-(id)initWithCPPImageListener:(IImageDownloadListener*)cppListener
               deleteListener:(bool)deleteListener
                          tag:(const std::string&)tag
{
  self = [super init];
  if (self) {
    _cppBufferListener = NULL;
    _cppImageListener  = cppListener;
    _deleteListener    = deleteListener;
    _tag               = tag;
  }
  return self;
}

-(void) onDownloadURL:(const URL&)url
                 data:(NSData*) data
{
  if (_cppBufferListener) {
    const size_t length = [data length];
    unsigned char* bytes = new unsigned char[ length ]; // will be deleted by IByteBuffer's destructor
    [data getBytes: bytes
            length: length];

    IByteBuffer* buffer = new ByteBuffer_iOS(bytes, length);

    _cppBufferListener->onDownload(url, buffer, false);
  }

  if (_cppImageListener) {
    UIImage* uiImage = [UIImage imageWithData:data];
    if (uiImage) {
      IImage* image = new Image_iOS(uiImage, data);
      _cppImageListener->onDownload(url, image, false);
    }
    else {
      _cppImageListener->onError(url);
    }
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
                         data:(NSData*) data
{
  if (_cppBufferListener) {
    const size_t length = [data length];
    unsigned char* bytes = new unsigned char[ length ]; // will be deleted by IByteBuffer's destructor
    [data getBytes: bytes
            length: length];

    IByteBuffer* buffer = new ByteBuffer_iOS(bytes, length);
    _cppBufferListener->onCanceledDownload(url, buffer, false);
    delete buffer;
  }

  if (_cppImageListener) {
    UIImage* uiImage = [UIImage imageWithData:data];
    if (uiImage) {
      IImage* image = new Image_iOS(uiImage, data);
      _cppImageListener->onCanceledDownload(url, image, false);
      delete image;
    }
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

-(const std::string) tag
{
  return _tag;
}

@end
