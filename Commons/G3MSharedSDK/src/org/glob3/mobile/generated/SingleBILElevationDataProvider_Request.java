package org.glob3.mobile.generated;
//
//  SingleBILElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

//
//  SingleBILElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//




//class SingleBILElevationDataProvider_BufferDownloadListener;
//class IDownloader;

public class SingleBILElevationDataProvider_Request
{
  public final Sector _sector ;
  public final Vector2I _extent;
  public final IElevationDataListener _listener;
  public final boolean _autodeleteListener;

  public SingleBILElevationDataProvider_Request(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
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
