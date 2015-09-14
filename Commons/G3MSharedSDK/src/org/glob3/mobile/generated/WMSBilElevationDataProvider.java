package org.glob3.mobile.generated; 
//
//  WMSBilElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  WMSBilElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//




//class IDownloader;

public class WMSBilElevationDataProvider extends ElevationDataProvider
{
  private IDownloader _downloader;
  private URL _url = new URL();
  private final String _layerName;
  private Sector _sector ;
  private final double _deltaHeight;

  public WMSBilElevationDataProvider(URL url, String layerName, Sector sector, double deltaHeight)
  {
     _url = new URL(url);
     _sector = new Sector(sector);
     _downloader = null;
     _layerName = layerName;
     _deltaHeight = deltaHeight;

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void initialize(G3MContext context)
  {
    _downloader = context.getDownloader();
  }

  public final long requestElevationData(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
  {
    if (_downloader == null)
    {
      ILogger.instance().logError("WMSBilElevationDataProvider was not initialized.");
      return -1;
    }
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    /*
     // http://data.worldwind.arc.nasa.gov/elev?REQUEST=GetMap&SERVICE=WMS&VERSION=1.3.0&LAYERS=srtm30&STYLES=&FORMAT=image/bil&CRS=EPSG:4326&BBOX=-180.0,-90.0,180.0,90.0&WIDTH=10&HEIGHT=10
     */
  
    //isb->addString("http://data.worldwind.arc.nasa.gov/elev");
    isb.addString(_url._path);
  
    isb.addString("?REQUEST=GetMap");
    isb.addString("&SERVICE=WMS");
    isb.addString("&VERSION=1.3.0");
  //  isb->addString("&LAYERS=srtm30");
    isb.addString("&LAYERS=");
    isb.addString(_layerName);
    isb.addString("&STYLES=");
    isb.addString("&FORMAT=image/bil");
    isb.addString("&CRS=EPSG:4326");
  
  
    isb.addString("&BBOX=");
    isb.addDouble(sector._lower._latitude._degrees);
    isb.addString(",");
    isb.addDouble(sector._lower._longitude._degrees);
    isb.addString(",");
    isb.addDouble(sector._upper._latitude._degrees);
    isb.addString(",");
    isb.addDouble(sector._upper._longitude._degrees);
  
  // TODO: #warning TODO_WMS_1_1_1;
  //  isb->addDouble(sector._lower._longitude._degrees);
  //  isb->addString(",");
  //  isb->addDouble(sector._lower._latitude._degrees);
  //  isb->addString(",");
  //  isb->addDouble(sector._upper._longitude._degrees);
  //  isb->addString(",");
  //  isb->addDouble(sector._upper._latitude._degrees);
  
    isb.addString("&WIDTH=");
    isb.addInt(extent._x);
    isb.addString("&HEIGHT=");
    isb.addInt(extent._y);
  
    final String path = isb.getString();
    if (isb != null)
       isb.dispose();
  
  
    return _downloader.requestBuffer(new URL(path, false), 2000000000, TimeInterval.fromDays(30), true, new WMSBilElevationDataProvider_BufferDownloadListener(sector, extent, listener, autodeleteListener, _deltaHeight), true);
  }

  public final void cancelRequest(long requestId)
  {
    _downloader.cancelRequest(requestId);
  }

  public final java.util.ArrayList<Sector> getSectors()
  {
    final java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
    sectors.add(_sector);
    return sectors;
  }

  public final Vector2I getMinResolution()
  {
//    int WORKING_JM;
    return Vector2I.zero();
  }

}