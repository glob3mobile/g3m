package org.glob3.mobile.generated; 
//
//  Storage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class InitializationContext;

public interface IStorage
{

  boolean containsBuffer(URL url);

  void saveBuffer(URL url, IByteBuffer buffer);

  IByteBuffer readBuffer(URL url);


  boolean containsImage(URL url);

  void saveImage(URL url, IImage image);

  IImage readImage(URL url);


  void onResume(InitializationContext ic);

  void onPause(InitializationContext ic);

  boolean isAvailable();


}