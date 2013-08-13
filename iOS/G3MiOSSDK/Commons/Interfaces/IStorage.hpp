//
//  Storage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Storage_h
#define G3MiOSSDK_Storage_h

class URL;
class IByteBuffer;
class IImage;
class G3MContext;
class TimeInterval;

#include <stddef.h>
#include "Disposable.hpp"


class IImageResult : public Disposable {
private:
  IImage*    _image;
  const bool _expired;

public:
  IImageResult(IImage* image,
               bool expired) :
  _image(image),
  _expired(expired)
  {
  }

  ~IImageResult() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  IImage* getImage() const {
    return _image;
  }

  bool isExpired() const {
    return _expired;
  }
};


class IByteBufferResult : public Disposable {
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
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  IByteBuffer* getBuffer() const {
    return _buffer;
  }

  bool isExpired() const {
    return _expired;
  }
};


class IStorage : public Disposable {
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
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  virtual void initialize(const G3MContext* context);


  virtual IByteBufferResult readBuffer(const URL& url,
                                       bool readExpired) = 0;

  virtual IImageResult readImage(const URL& url,
                                 bool readExpired) = 0;

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
