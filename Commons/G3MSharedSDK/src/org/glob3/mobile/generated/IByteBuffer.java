package org.glob3.mobile.generated;//
//  IByteBuffer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//




public abstract class IByteBuffer
{

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int size() const = 0;
  public abstract int size();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int timestamp() const = 0;
  public abstract int timestamp();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual byte get(int i) const = 0;
  public abstract byte get(int i);

  public abstract void put(int i, byte value);

  public abstract void rawPut(int i, byte value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String description() const = 0;
  public abstract String description();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String getAsString() const = 0;
  public abstract String getAsString();

}
