//
//  Storage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Storage_h
#define G3MiOSSDK_Storage_h

#include "URL.hpp"
#include "IByteBuffer.hpp"
#include "IImage.hpp"
class G3MContext;

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

  virtual bool containsBuffer(const URL& url) = 0;
  
  virtual void saveBuffer(const URL& url,
                          const IByteBuffer* buffer,
                          bool saveInBackground) = 0;
  
  virtual const IByteBuffer* readBuffer(const URL& url) = 0;
  
  
  virtual bool containsImage(const URL& url) = 0;
  
  virtual void saveImage(const URL& url,
                         const IImage* image,
                         bool saveInBackground) = 0;
  
  virtual const IImage* readImage(const URL& url) = 0;
  
  
  virtual void onResume(const G3MContext* context) = 0;
  
  virtual void onPause(const G3MContext* context) = 0;

  virtual void onDestroy(const G3MContext* context) = 0;

  
  virtual bool isAvailable() = 0;
  
};

#endif
