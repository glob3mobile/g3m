package org.glob3.mobile.generated;//
//  IBuffer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  IBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//




public abstract class IBuffer
{

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public void dispose()
  {
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void dispose();
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long getID() const = 0;
  public abstract long getID();

  /**
   Answer the size (the count of elements) of the buffer
   **/
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int size() const = 0;
  public abstract int size();

  /**
   Answer the timestamp of the buffer.
   
   This number will be different each time the buffer changes its contents.
   It provides a fast method to check if the Buffer has changed.
   **/
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int timestamp() const = 0;
  public abstract int timestamp();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String description() const = 0;
  public abstract String description();

}
