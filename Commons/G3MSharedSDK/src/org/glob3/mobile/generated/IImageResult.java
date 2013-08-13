package org.glob3.mobile.generated; 
//
//  IStorage.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 05/11/12.
//
//

//
//  Storage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class URL;
//class IByteBuffer;
//class IImage;
//class G3MContext;
//class TimeInterval;



public class IImageResult extends Disposable
{
  private IImage _image;
  private final boolean _expired;

  public IImageResult(IImage image, boolean expired)
  {
     _image = image;
     _expired = expired;
  }

  public void dispose()
  {
  super.dispose();

  }

  public final IImage getImage()
  {
    return _image;
  }

  public final boolean isExpired()
  {
    return _expired;
  }
}