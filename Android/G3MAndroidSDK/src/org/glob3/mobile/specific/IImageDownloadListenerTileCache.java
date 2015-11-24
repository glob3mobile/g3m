

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.URL;


public class IImageDownloadListenerTileCache
         extends
            IImageDownloadListener {

   private final long                _requestId;
   private final TileVisitorListener _listener;


   public IImageDownloadListenerTileCache(final long requestId,
                                          final TileVisitorListener listener) {
      _requestId = requestId;
      _listener = listener;
   }


   @Override
   public void onError(final URL url) {
      ILogger.instance().logError("FAIL Downloaded id: " + _requestId);
   }


   @Override
   public void onCancel(final URL url) {
      // TODO Auto-generated method stub

   }


   @Override
   public void onCanceledDownload(final URL url,
                                  final IImage image,
                                  final boolean expired) {
      ILogger.instance().logError("CANCELED Downloaded id: " + _requestId);
      if (image != null) {
         image.dispose();
      }

   }


   @Override
   public void onDownload(final URL arg0,
                          final IImage arg1,
                          final boolean arg2) {
      _listener.onTileDownloaded();
   }
}
