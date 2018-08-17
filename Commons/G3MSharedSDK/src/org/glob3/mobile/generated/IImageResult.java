package org.glob3.mobile.generated;import java.util.*;

//
//  IStorage.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 05/11/12.
//
//

//
//  Storage.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class URL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IByteBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TimeInterval;



public class IImageResult
{
  public IImage _image;
  public final boolean _expired;


  public IImageResult(IImage image, boolean expired)
  {
	  _image = image;
	  _expired = expired;
  }

  public void dispose()
  {
  }
}
