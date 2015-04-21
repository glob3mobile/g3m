package org.glob3.mobile.generated; 
//
//  NullStorage.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 25/04/14.
//
//

//
//  NullStorage.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class NullStorage extends IStorage
{
  public final IByteBufferResult readBuffer(URL url, boolean readExpired)
  {
    return new IByteBufferResult(null, false);
  }

  public final IImageResult readImage(URL url, boolean readExpired)
  {
    return new IImageResult(null, false);
  }


  public final void saveBuffer(URL url, IByteBuffer buffer, TimeInterval timeToExpires, boolean saveInBackground)
  {

  }

  public final void saveImage(URL url, IImage image, TimeInterval timeToExpires, boolean saveInBackground)
  {

  }


  public final void onResume(G3MContext context)
  {
  }

  public final void onPause(G3MContext context)
  {
  }

  public final void onDestroy(G3MContext context)
  {
  }


  public final boolean isAvailable()
  {
    return false;
  }


}