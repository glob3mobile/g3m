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


  //std::vector<std::string> WMSBilElevationDataProvider_BufferDownloadListener::_urls;
  
  
  public final void initialize(G3MContext context)
  {
    _downloader = context.getDownloader();
  }

  public final long requestElevationData(Sector sector, Vector2I extent, long requestPriority, IElevationDataListener listener, boolean autodeleteListener)
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning CHECK THIS
    /**
     There is some inconsistency between the WMS 1.3.0 standard and the NASA implementation regarding the CRS with EPSG 4326
    
      http: //portal.opengeospatial.org/files/?artifact_id=14416
     EXAMPLE 1 A <BoundingBox> metadata element for a Layer representing the entire Earth in the CRS:84 Layer CRS
     would be written as
     <BoundingBox CRS="CRS:84" minx="-180" miny="-90" maxx="180" maxy="90">.
     A BBOX parameter requesting a map of the entire Earth would be written in this CRS as
     BBOX=-180,-90,180,90.
     EXAMPLE 2 A <BoundingBox> representing the entire Earth in the EPSG:4326 Layer CRS would be written as
     <BoundingBox CRS="EPSG:4326" minx="-90" miny="-180" maxx="90" maxy="180">.
     A BBOX parameter requesting a map of the entire Earth would be written in this CRS as
     BBOX=-90,-180,90,180.
    
     Should be like:
    
     isb->addString("&BBOX=");
     isb->addDouble(sector._lower._latitude._degrees);
     isb->addString(",");
     isb->addDouble(sector._lower._longitude._degrees);
     isb->addString(",");
     isb->addDouble(sector._upper._latitude._degrees);
     isb->addString(",");
     isb->addDouble(sector._upper._longitude._degrees);
    
     **/
  
  
    isb.addString("&BBOX=");
    isb.addDouble(sector._lower._longitude._degrees);
    isb.addString(",");
    isb.addDouble(sector._lower._latitude._degrees);
    isb.addString(",");
    isb.addDouble(sector._upper._longitude._degrees);
    isb.addString(",");
    isb.addDouble(sector._upper._latitude._degrees);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO_WMS_1_1_1;
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
  
  
    return _downloader.requestBuffer(new URL(path, false), requestPriority, TimeInterval.fromDays(30), true, new WMSBilElevationDataProvider_BufferDownloadListener(sector, extent, listener, autodeleteListener, _deltaHeight), true);
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