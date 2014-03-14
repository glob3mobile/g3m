//
//  IBufferDownloadListener.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IImageDownloadListener
#define G3MiOSSDK_IImageDownloadListener

#include "IImage.hpp"

class IImageDownloadListener {
public:
  virtual ~IImageDownloadListener() {
  }

  /**
   Callback method invoked on a successful download.  The image has to be deleted in C++ / .disposed() in Java
   */
  virtual void onDownload(const URL& url,
                          IImage* image,
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

   The image WILL be deleted/disposed after the method finishs.  If you need to keep the image, use shallowCopy() to store a copy of the image.
   */
  virtual void onCanceledDownload(const URL& url,
                                  IImage* image,
                                  bool expired) = 0;

};

#endif
