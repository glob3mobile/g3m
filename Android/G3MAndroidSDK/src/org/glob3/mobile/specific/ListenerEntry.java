

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.ILogger;

import android.util.Log;


public class ListenerEntry {

   final static String             TAG = "Downloader_Android_ListenerEntry";

   private boolean                 _canceled;
   private final long              _requestId;
   private final IDownloadListener _listener;


   public ListenerEntry(final IDownloadListener listener,
                        final long requestId) {
      _listener = listener;
      _requestId = requestId;
      _canceled = false;
   }


   public long getRequestId() {
      return _requestId;
   }


   public IDownloadListener getListener() {
      return _listener;
   }


   public void cancel() {
      if (_canceled) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + ": Listener for requestId=" + _requestId + " already canceled");
         }
         else {
            Log.e(TAG, "Listener for requestId=" + _requestId + " already canceled");
         }
      }
      _canceled = true;
   }


   public boolean isCanceled() {
      return _canceled;
   }

}
