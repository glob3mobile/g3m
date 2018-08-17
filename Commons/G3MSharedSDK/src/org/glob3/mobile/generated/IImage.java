package org.glob3.mobile.generated;//
//  IImage.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//



public abstract class IImage
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getWidth() const = 0;
  public abstract int getWidth();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getHeight() const = 0;
  public abstract int getHeight();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Vector2I getExtent() const = 0;
  public abstract Vector2I getExtent();

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
//ORIGINAL LINE: virtual boolean isPremultiplied() const = 0;
  public abstract boolean isPremultiplied();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* shallowCopy() const = 0;
  public abstract IImage shallowCopy();
}
