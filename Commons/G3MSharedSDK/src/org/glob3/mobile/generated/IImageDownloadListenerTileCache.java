package org.glob3.mobile.generated; 
//
//  IImageDownloadListenerTileCache.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 04/12/13.
//
//

//
//  IImageDownloadListenerTileCache.h
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 04/12/13.
//
//




public class IImageDownloadListenerTileCache extends IImageDownloadListener
{
  private long _counter;
  private final String _layername;
  private final Tile _tile;

  public void dispose()
  {
    super.dispose();
  }

  public IImageDownloadListenerTileCache(long counter, Tile tile, String layername)
  {
     _counter = counter;
     _tile = tile;
     _layername = layername;

  }



  /**
   Callback method invoked on a successful download.  The image has to be deleted in C++ / .disposed() in Java
   */
  public final void onDownload(URL url, IImage image, boolean expired)
  {
    ILogger.instance().logInfo("Downloaded petition %d of layer %s. Tile z: %d, x: %d, y: %d", _counter, _layername, _tile._level,_tile._column,_tile._row);
    if(image != null)
    {
      if (image != null)
         image.dispose();
      image.dispose();
    }
  }

  /**
   Callback method invoke after an error trying to download url
   */
  public final void onError(URL url)
  {
    ILogger.instance().logError("Error Download petition %d of layer %s. Tile z: %d, x: %d, y: %d", _counter, _layername, _tile._level,_tile._column,_tile._row);
  }

  /**
   Callback method invoke after canceled request
   */
  public final void onCancel(URL url)
  {
    ILogger.instance().logError("Cancel Download petition %d of layer %s. Tile z: %d, x: %d, y: %d", _counter, _layername, _tile._level,_tile._column,_tile._row);
  }

  /**
   This method will be call, before onCancel, when the data arrived before the cancelation.
   
   The image WILL be deleted/disposed after the method finishs.  If you need to keep the image, use shallowCopy() to store a copy of the image.
   */
  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {
    ILogger.instance().logError("Canceled Download petition %d of layer %s. Tile z: %d, x: %d, y: %d", _counter, _layername, _tile._level,_tile._column,_tile._row);
    if(image != null)
    {
      if (image != null)
         image.dispose();
      image.dispose();
    }
  }
}