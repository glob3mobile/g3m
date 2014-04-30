package org.glob3.mobile.generated; 
public class RLTIP_ImageDownloadListener extends IImageDownloadListener
{
  private RasterLayerTileImageProvider _rasterLayerTileImageProvider;
  private final String _tileId;
  private TileImageListener _listener;
  private final boolean _deleteListener;
  private TileImageContribution _contribution;

  public RLTIP_ImageDownloadListener(RasterLayerTileImageProvider rasterLayerTileImageProvider, String tileId, TileImageContribution contribution, TileImageListener listener, boolean deleteListener)
  {
     _rasterLayerTileImageProvider = rasterLayerTileImageProvider;
     _tileId = tileId;
     _contribution = contribution;
     _listener = listener;
     _deleteListener = deleteListener;
  }

  public void dispose()
  {
    _rasterLayerTileImageProvider.requestFinish(_tileId);

    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }

    TileImageContribution.deleteContribution(_contribution);
    super.dispose();
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    _listener.imageCreated(_tileId, image, url.getPath(), _contribution);
    _contribution = null; // moves ownership of _contribution to _listener
  }

  public final void onError(URL url)
  {
    _listener.imageCreationError(_tileId, "Download error - " + url.getPath());
  }

  public final void onCancel(URL url)
  {
    _listener.imageCreationCanceled(_tileId);
  }

  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {
    // do nothing
  }
}