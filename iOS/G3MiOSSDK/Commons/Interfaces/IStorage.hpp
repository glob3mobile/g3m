//
//  Storage.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Storage
#define G3MiOSSDK_Storage

class URL;
class IByteBuffer;
class IImage;
class G3MContext;
class TimeInterval;

#include <stddef.h>


class IImageResult {
public:
  IImage*    _image;
  const bool _expired;


  IImageResult(IImage* image,
               bool expired) :
  _image(image),
  _expired(expired)
  {
  }

  ~IImageResult() {
  }

//  IImage* getImage() const {
//    return _image;
//  }

//  bool isExpired() const {
//    return _expired;
//  }
  
};


class IByteBufferResult {
private:
  IByteBuffer* _buffer;
  const bool   _expired;

public:
  IByteBufferResult(IByteBuffer* buffer,
                    bool expired) :
  _buffer(buffer),
  _expired(expired)
  {
  }

  ~IByteBufferResult() {
  }

  IByteBuffer* getBuffer() const {
    return _buffer;
  }

  bool isExpired() const {
    return _expired;
  }
};


//class IStorageImageListener {
//public:
//#if C_CODE
//  virtual ~IStorageImageListener() { }
//#endif
//#ifdef JAVA_CODE
//  void dispose();
//#endif
//
//  /**
//   Callback method image reading.
//   The image will be NULL if it doesn't exist
//   The image has to be deleted in C++ / and disposed() en Java.
//   */
//  virtual void imageRead(IImage* image,
//                         bool expired) = 0;
//
//};


class IStorage {
protected:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  protected G3MContext _context;
#endif

public:
  IStorage() :
  _context(NULL)
  {
  }

  virtual ~IStorage() {
  }

  virtual void initialize(const G3MContext* context);


  virtual IByteBufferResult readBuffer(const URL& url,
                                       bool readExpired) = 0;

  virtual IImageResult readImage(const URL& url,
                                 bool readExpired) = 0;

//  virtual void readImage(const URL& url,
//                         bool readExpired,
//                         IStorageImageListener* imageListener,
//                         bool autodeleteImageListener) = 0;

  virtual void saveBuffer(const URL& url,
                          const IByteBuffer* buffer,
                          const TimeInterval& timeToExpires,
                          bool saveInBackground) = 0;

  virtual void saveImage(const URL& url,
                         const IImage* image,
                         const TimeInterval& timeToExpires,
                         bool saveInBackground) = 0;


  virtual void onResume(const G3MContext* context) = 0;

  virtual void onPause(const G3MContext* context) = 0;

  virtual void onDestroy(const G3MContext* context) = 0;


  virtual bool isAvailable() = 0;
  
};

#endif
