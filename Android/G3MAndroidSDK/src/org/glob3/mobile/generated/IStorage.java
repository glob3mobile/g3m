package org.glob3.mobile.generated; 
//
//  Storage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public interface IStorage
{
  boolean contains(String url);

  void save(String url, ByteBuffer bb);

  ByteBuffer read(String url);
}