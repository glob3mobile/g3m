package org.glob3.mobile.generated; 
public class RLTIP_ImageDownloadListener extends IImageDownloadListener
{
  private TileImageListener _listener;
  private final boolean _deleteListener;
  private final float _layerAlpha;

  public RLTIP_ImageDownloadListener(TileImageListener listener, boolean deleteListener, float layerAlpha)
  {
     _listener = listener;
     _deleteListener = deleteListener;
     _layerAlpha = layerAlpha;
  }

  public void dispose()
  {
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work
//    _listener->imageCreated(<#const Tile *tile#>,
//                            image,
//                            url.getPath(),
//                            <#const Sector &imageSector#>,
//                            <#const RectangleF &imageRectangle#>,
//                            _layerAlpha);
  }

  public final void onError(URL url)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work
//    _listener->imageCreationError(<#const Tile *tile#>,
//                                  "Download error - " + url.getPath());
  }

  public final void onCancel(URL url)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work
//    _listener->imageCreationCanceled(const Tile *tile);
  }

  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work
  }

}