

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.URL;


public class Downloader_WebGL
         implements
            IDownloader {

   @Override
   public void start() {
      // TODO Auto-generated method stub

   }


   @Override
   public void stop() {
      // TODO Auto-generated method stub

   }


   @Override
   public long request(final URL url,
                       final long priority,
                       final IDownloadListener listener,
                       final boolean deleteListener) {
      // TODO Auto-generated method stub
      return 0;
   }


   @Override
   public void cancelRequest(final long requestId) {
      // TODO Auto-generated method stub

   }


   @Override
   public String statistics() {
      final StringBuilder_WebGL sb = new StringBuilder_WebGL();

      //      sb.add("Downloader_WebGL(downloading=").add(_downloadingHandlers.size());
      //      sb.add(", queued=").add(_queuedHandlers.size());
      //      sb.add(", totalRequests=").add(_requestsCounter);
      //      sb.add(", totalCancels=").add(_cancelsCounter);

      return sb.getString();
   }

}
