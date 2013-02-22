package org.glob3.mobile.generated; 
//
//  WMSBillElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  WMSBillElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//



//class IDownloader;

public class WMSBillElevationDataProvider extends ElevationDataProvider
{
  private IDownloader _downloader;

  public WMSBillElevationDataProvider()
  {
     _downloader = null;

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void initialize(G3MContext context)
  {
    _downloader = context.getDownloader();
  }

  public final long requestElevationData(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener)
  {
    if (_downloader == null)
    {
      ILogger.instance().logError("WMSBillElevationDataProvider was not initialized.");
      return -1;
    }
  
    // http://data.worldwind.arc.nasa.gov/elev?REQUEST=GetMap&SERVICE=WMS&VERSION=1.3.0&LAYERS=srtm30&STYLES=&FORMAT=image/bil&CRS=EPSG:4326&BBOX=-180.0,-90.0,180.0,90.0&WIDTH=10&HEIGHT=10
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("http://data.worldwind.arc.nasa.gov/elev?");
    isb.addString("REQUEST=GetMap");
    isb.addString("&SERVICE=WMS");
    isb.addString("&VERSION=1.3.0");
    isb.addString("&LAYERS=srtm30");
    isb.addString("&STYLES=");
    isb.addString("&FORMAT=image/bil");
    isb.addString("&CRS=EPSG:4326");
  
    isb.addString("&BBOX=");
    isb.addDouble(sector.lower().latitude()._degrees);
    isb.addString(",");
    isb.addDouble(sector.lower().longitude()._degrees);
    isb.addString(",");
    isb.addDouble(sector.upper().latitude()._degrees);
    isb.addString(",");
    isb.addDouble(sector.upper().longitude()._degrees);
  
    isb.addString("&WIDTH=");
    isb.addInt(resolution._x);
    isb.addString("&HEIGHT=");
    isb.addInt(resolution._y);
  
    final String path = isb.getString();
    if (isb != null)
       isb.dispose();
  
  
    final double noDataValue = 0;
  
    return _downloader.requestBuffer(new URL(path, false), 2000000000, TimeInterval.fromDays(30), new WMSBillElevationDataProvider_BufferDownloadListener(sector, resolution, noDataValue, listener, autodeleteListener), true);
  }

  public final void cancelRequest(long requestId)
  {
    _downloader.cancelRequest(requestId);
  }

}