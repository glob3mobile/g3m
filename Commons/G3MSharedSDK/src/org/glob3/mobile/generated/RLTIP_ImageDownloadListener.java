package org.glob3.mobile.generated; 
public class RLTIP_ImageDownloadListener extends IImageDownloadListener
{
  private RasterLayerTileImageProvider _rasterLayerTileImageProvider;
  private final String _tileID;
  private TileImageListener _listener;
  private final boolean _deleteListener;
  private TileImageContribution _contribution;

  public RLTIP_ImageDownloadListener(RasterLayerTileImageProvider rasterLayerTileImageProvider, String tileID, TileImageContribution contribution, TileImageListener listener, boolean deleteListener)
  {
     _rasterLayerTileImageProvider = rasterLayerTileImageProvider;
     _tileID = tileID;
     _contribution = contribution;
     _listener = listener;
     _deleteListener = deleteListener;
  }

  public void dispose()
  {
    _rasterLayerTileImageProvider.requestFinish(_tileID);

    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }

    TileImageContribution.releaseContribution(_contribution);
    super.dispose();
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    final TileImageContribution contribution = _contribution;
    _contribution = null; // moves ownership of _contribution to _listener
    _listener.imageCreated(_tileID, image, url._path, contribution);
  }

  public final void onError(URL url)
  {
    _listener.imageCreationError(_tileID, "Download error - " + url._path);
  }

  public final void onCancel(URL url)
  {
    _listener.imageCreationCanceled(_tileID);
  }

  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {
    // do nothing
  }
}