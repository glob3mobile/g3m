package org.glob3.mobile.generated; 
//
//  Storage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public abstract class IStorage
{
  public abstract boolean contains(String url);

  public abstract void save(String url, ByteBuffer bb);

  public abstract ByteBuffer read(String url);

  public void dispose()
  {
  }

}