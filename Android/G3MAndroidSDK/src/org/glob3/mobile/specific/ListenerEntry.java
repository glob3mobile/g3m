package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDownloadListener;

public class ListenerEntry {

   private boolean           _canceled;
   private long              _requestId;
   private IDownloadListener _listener;


   public ListenerEntry(IDownloadListener listener,
                        long requestId) {
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
         Logger_Android.instance().logError("ListenerEntry: Listener for requestId=" + _requestId + " already canceled");
      }
      _canceled = true;
   }
   
   public boolean isCanceled() {
      return _canceled;
   }

}
