//
//  IBufferDownloadListener.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IBufferDownloadListener_h
#define G3MiOSSDK_IBufferDownloadListener_h

#include "URL.hpp"
#include "IByteBuffer.hpp"

class IBufferDownloadListener {
public:
#ifdef C_CODE
  virtual ~IBufferDownloadListener() {  }
#endif
  
  virtual void onDownload(const URL& url,
                          const IByteBuffer* buffer) = 0;
  
  virtual void onError(const URL& url) = 0;
  
  virtual void onCancel(const URL& url) = 0;
  
  /* this method will be call, before onCancel, when the data arrived before the cancelation */
  virtual void onCanceledDownload(const URL& url,
                                  const IByteBuffer* data) = 0;
  
};

#endif
