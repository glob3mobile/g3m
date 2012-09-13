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
  public final boolean contains(URL url)
  {
	return false;
  }

  public final void save(URL url, ByteArrayWrapper buffer)
  {
  }

  public final ByteArrayWrapper read(URL url)
  {
	return null;
  }
}