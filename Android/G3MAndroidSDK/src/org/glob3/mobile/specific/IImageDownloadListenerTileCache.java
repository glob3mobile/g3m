

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
<<<<<<< HEAD
   public void onError(final URL pUrl) {
      ILogger.instance().logError("FAIL Downloaded id: " + _requestID);
=======
   public void onError(final URL url) {
      ILogger.instance().logError("FAIL Downloaded id: " + _requestId);
>>>>>>> caceresview
   }


   @Override
<<<<<<< HEAD
   public void onCancel(final URL pUrl) {
      super.dispose();
=======
   public void onCancel(final URL url) {
      // TODO Auto-generated method stub

>>>>>>> caceresview
   }


   @Override
<<<<<<< HEAD
   public void onCanceledDownload(final URL arg0,
                                  final IImage arg1,
                                  final boolean arg2) {
      ILogger.instance().logError("CANCELED Downloaded id: " + _requestID);
=======
   public void onCanceledDownload(final URL url,
                                  final IImage image,
                                  final boolean expired) {
      ILogger.instance().logError("CANCELED Downloaded id: " + _requestId);
      if (image != null) {
         image.dispose();
      }
>>>>>>> caceresview

   }


   @Override
   public void onDownload(final URL arg0,
                          final IImage arg1,
                          final boolean arg2) {
      _listener.onTileDownloaded();
   }
}
