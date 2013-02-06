package org.glob3.mobile.generated; 
//
//  IBufferDownloadListener.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public interface IImageDownloadListener
{

  /**
   Callback method invoked on a successful download.  The image has to be deleted in C++ / .disposed() in Java
   */
  void onDownload(URL url, IImage image);

  /**
   Callback method invoke after an error trying to download url
   */
  void onError(URL url);

  /**
   Callback method invoke after canceled request
   */
  void onCancel(URL url);

  /**
   This method will be call, before onCancel, when the data arrived before the cancelation.

   The image WILL be deleted/disposed after the method finishs.  If you need to keep the image, use shallowCopy() to store a copy of the image.
   */
  void onCanceledDownload(URL url, IImage image);

}