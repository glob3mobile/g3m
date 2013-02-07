package org.glob3.mobile.generated; 
//
//  NullStorage.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public abstract class NullStorage extends IStorage {
//  bool containsBuffer(const URL& url) {
//    return false;
//  }

  public final void saveBuffer(URL url, IByteBuffer buffer) {

  }

  public final IByteBuffer readBuffer(URL url) {
    return null;
  }

//  bool containsImage(const URL& url) {
//    return false;
//  }

  public final void saveImage(URL url, IImage buffer) {

  }

  public final IImage readImage(URL url) {
    return null;
  }

}