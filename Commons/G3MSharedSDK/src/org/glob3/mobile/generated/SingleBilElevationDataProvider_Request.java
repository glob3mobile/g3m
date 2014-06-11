package org.glob3.mobile.generated; 
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




//class SingleBilElevationDataProvider_BufferDownloadListener;
//class IDownloader;

public class SingleBilElevationDataProvider_Request
{
  public final Sector _sector ;
  public final Vector2I _extent;
  public final IElevationDataListener _listener;
  public final boolean _autodeleteListener;

  public SingleBilElevationDataProvider_Request(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
  {
     _sector = new Sector(sector);
     _extent = extent;
     _listener = listener;
     _autodeleteListener = autodeleteListener;
  }

  public void dispose()
  {
  }

}