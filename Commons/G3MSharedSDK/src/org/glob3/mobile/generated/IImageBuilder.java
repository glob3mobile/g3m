package org.glob3.mobile.generated;//
//  IImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

//
//  IImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageBuilderListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ChangedListener;

public abstract class IImageBuilder
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
//ORIGINAL LINE: virtual boolean isMutable() const = 0;
  public abstract boolean isMutable();

  public abstract void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener);

  public abstract void setChangeListener(ChangedListener listener);

}
