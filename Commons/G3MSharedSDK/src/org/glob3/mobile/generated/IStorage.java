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


public abstract class IStorage
{
  protected G3MContext _context;

  public IStorage()
  {
     _context = null;

  }

  public void dispose()
  {

  }

  public void initialize(G3MContext context)
  {
    _context = context;
  }


  public abstract void saveBuffer(URL url, IByteBuffer buffer, TimeInterval timeToExpires, boolean saveInBackground);

  public abstract IByteBuffer readBuffer(URL url);


  public abstract void saveImage(URL url, IImage image, TimeInterval timeToExpires, boolean saveInBackground);

  public abstract IImage readImage(URL url);


  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);


  public abstract boolean isAvailable();

}