

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.URL;


public class IImageDownloadListenerTileCache
         extends
            IImageDownloadListener {

   private final long                _requestID;
   private final TileVisitorListener _listener;


   public IImageDownloadListenerTileCache(final long requestID,
                                          final TileVisitorListener listener) {
      _requestID = requestID;
      _listener = listener;
   }


   @Override
   public void onError(final URL pUrl) {
      ILogger.instance().logError("FAIL Downloaded id: " + _requestID);
   }


   @Override
   public void onCancel(final URL pUrl) {
      // TODO Auto-generated method stub

   }


   @Override
   public void onCanceledDownload(final URL arg0,
                                  final IImage arg1,
                                  final boolean arg2) {
      ILogger.instance().logError("CANCELED Downloaded id: " + _requestID);

   }


   @Override
   public void onDownload(final URL arg0,
                          final IImage arg1,
                          final boolean arg2) {
      _listener.onTileDownloaded();
   }

}
