package org.glob3.mobile.generated;import java.util.*;

//
//  SingleBilElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

//
//  SingleBilElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SingleBilElevationDataProvider_BufferDownloadListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;

public class SingleBilElevationDataProvider_Request
{
  public final Sector _sector = new Sector();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public final Vector2I _extent = new Vector2I();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Vector2I _extent = new public();
//#endif
  public final IElevationDataListener _listener;
  public final boolean _autodeleteListener;

  public SingleBilElevationDataProvider_Request(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
  {
	  _sector = new Sector(sector);
	  _extent = new Vector2I(extent);
	  _listener = listener;
	  _autodeleteListener = autodeleteListener;
  }

  public void dispose()
  {
  }

}
