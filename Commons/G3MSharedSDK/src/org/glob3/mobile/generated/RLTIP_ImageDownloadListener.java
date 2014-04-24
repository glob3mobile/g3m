package org.glob3.mobile.generated; 
///#include "IDownloader.hpp"

public class RLTIP_ImageDownloadListener extends IImageDownloadListener
{
  private final String _tileId;
  private final TileImageContribution _contribution;

  private TileImageListener _listener;
  private final boolean _deleteListener;

  public RLTIP_ImageDownloadListener(String tileId, TileImageContribution contribution, TileImageListener listener, boolean deleteListener)
  {
     _tileId = tileId;
     _contribution = contribution;
     _listener = listener;
     _deleteListener = deleteListener;
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
    _listener.imageCreated(_tileId, image, url.getPath(), _contribution);
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