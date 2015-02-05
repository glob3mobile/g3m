//
//  IBufferDownloadListener.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IBufferDownloadListener
#define G3MiOSSDK_IBufferDownloadListener

#include "URL.hpp"
#include "IByteBuffer.hpp"


class IBufferDownloadListener {
public:
  virtual ~IBufferDownloadListener() {
  }

  /**
   Callback method invoked on a successful download.  The buffer has to be deleted in C++ / .disposed() in Java
   */
  virtual void onDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) = 0;

  /**
   Callback method invoke after an error trying to download url
   */
  virtual void onError(const URL& url) = 0;

  /**
   Callback method invoke after canceled request
   */
  virtual void onCancel(const URL& url) = 0;

  /**
   This method will be call, before onCancel, when the data arrived before the cancelation.

   The buffer WILL be deleted/disposed after the method finishs.  If you need to keep the buffer, use shallowCopy() to store a copy of the buffer.
   */
  virtual void onCanceledDownload(const URL& url,
                                  IByteBuffer* buffer,
                                  bool expired) = 0;

};

#endif
