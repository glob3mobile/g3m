

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.URL;


public class IImageDownloadListenerTileCache
    implements
      IImageDownloadListener {

  private final long _requestId;


  public IImageDownloadListenerTileCache(final long requestId) {
    _requestId = requestId;
  }


  public void onDownload(final URL pUrl,
                         final IImage pImage) {
    ILogger.instance().logInfo("Downloaded id: " + _requestId);
  }


  public void onError(final URL pUrl) {
    ILogger.instance().logError("Fail Downloaded id: " + _requestId);
  }


  public void onCancel(final URL pUrl) {
    // TODO Auto-generated method stub

  }


  public void onCanceledDownload(final URL pUrl,
                                 final IImage pImage) {
    // TODO Auto-generated method stub

  }

}
