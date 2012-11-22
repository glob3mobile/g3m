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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Context;

public abstract class IStorage
{
  protected Context _context;

  public IStorage()
  {
	  _context = null;

  }

  public void dispose()
  {

  }

  public void initialize(Context context)
  {
	_context = context;
  }

  public abstract boolean containsBuffer(URL url);

  public abstract void saveBuffer(URL url, IByteBuffer buffer, boolean saveInBackground);

  public abstract IByteBuffer readBuffer(URL url);


  public abstract boolean containsImage(URL url);

  public abstract void saveImage(URL url, IImage image, boolean saveInBackground);

  public abstract IImage readImage(URL url);


  public abstract void onResume(Context context);

  public abstract void onPause(Context context);

  public abstract void onDestroy(Context context);


  public abstract boolean isAvailable();

}