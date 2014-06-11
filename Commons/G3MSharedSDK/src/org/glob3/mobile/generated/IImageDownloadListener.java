package org.glob3.mobile.generated; 
//
//  IBufferDownloadListener.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


///#include "IImage.hpp"
//class IImage;
//class URL;

public abstract class IImageDownloadListener
{
  public void dispose()
  {
  }

  /**
   Callback method invoked on a successful download.  The image has to be deleted in C++ / .disposed() in Java
   */
  public abstract void onDownload(URL url, IImage image, boolean expired);

  /**
   Callback method invoke after an error trying to download url
   */
  public abstract void onError(URL url);

  /**
   Callback method invoke after canceled request
   */
  public abstract void onCancel(URL url);

  /**
   This method will be call, before onCancel, when the data arrived before the cancelation.

   The image WILL be deleted/disposed after the method finishs.  If you need to keep the image, use shallowCopy() to store a copy of the image.
   */
  public abstract void onCanceledDownload(URL url, IImage image, boolean expired);

}