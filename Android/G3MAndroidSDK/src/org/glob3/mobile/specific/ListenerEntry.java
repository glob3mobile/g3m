package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDownloadListener;

import android.util.Log;

public class ListenerEntry {

   private boolean           _canceled;
   private long              _requestId;
   private IDownloadListener _listener;


   public ListenerEntry(IDownloadListener listener,
                        long requestId) {
      // TODO Auto-generated constructor stub
      _listener = listener;
      _requestId = requestId;
      _canceled = false;
   }


   public long getRequestId() {
      return _requestId;
   }


   public IDownloadListener getListener() {
      // TODO Auto-generated method stub
      return _listener;
   }


   public void cancel() {
      if (_canceled) {
         // TODO Logger_Android
         Log.i("ListenerEntry", "Listener for RequestId=" + _requestId + " already canceled");
      }
      _canceled = true;
   }
   
   public boolean isCanceled() {
      return _canceled;
   }

}
