package org.glob3.mobile.generated; 
//
//  NullStorage.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class NullStorage implements IStorage
{
  public final boolean contains(String url)
  {
	  return false;
  }

  public final void save(String url, ByteBuffer bb)
  {
  }

  public final ByteBuffer getByteBuffer(String url)
  {
	  ByteBuffer bb = new ByteBuffer(null,0);
	  return bb;
  }
}