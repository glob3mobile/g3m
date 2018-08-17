package org.glob3.mobile.generated;//
//  ElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  ElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;
//class Vector2I;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ElevationData;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;



public abstract class IElevationDataListener
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

  /**
   Callback method for ElevationData creation. Pointer is owned by Listener
   */
  public abstract void onData(Sector sector, Vector2I extent, ElevationData elevationData);

  public abstract void onError(Sector sector, Vector2I extent);

  public abstract void onCancel(Sector sector, Vector2I extent);

}
