//
//  IBufferDownloadListener.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IImageDownloadListener_h
#define G3MiOSSDK_IImageDownloadListener_h

#include "IImage.hpp"

class IImageDownloadListener {
public:
#ifdef C_CODE
  virtual ~IImageDownloadListener() {  }
#endif
  
  virtual void onDownload(const URL& url,
                          const IImage* image) = 0;
  
  virtual void onError(const URL& url) = 0;
  
  virtual void onCancel(const URL& url) = 0;
  
  /* this method will be call, before onCancel, when the data arrived before the cancelation */
  virtual void onCanceledDownload(const URL& url,
                                  const IImage* image) = 0;
  
};

#endif
