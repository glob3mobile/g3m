package org.glob3.mobile.generated; 
//
//  IBufferDownloadListener.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public abstract class IBufferDownloadListener
{
  public void dispose()
  {
  }

  /**
   Callback method invoked on a successful download.  The buffer has to be deleted in C++ / .disposed() in Java
   */
  public abstract void onDownload(URL url, IByteBuffer buffer, boolean expired);

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

   The buffer WILL be deleted/disposed after the method finishs.  If you need to keep the buffer, use shallowCopy() to store a copy of the buffer.
   */
  public abstract void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired);

}