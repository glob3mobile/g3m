

package org.glob3.mobile.specific;

import java.util.ArrayList;
import java.util.Iterator;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Response;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;


public class Downloader_WebGL_Handler {


   class ListenerEntry {

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
            log(LogLevel.ErrorLevel, ": Listener for requestId=" + _requestId + " already canceled");
         }
         _canceled = true;
      }


      public boolean isCanceled() {
         return _canceled;
      }

   }


   /*
    * Downloader_WebGL_Handler
    */
   final static String                    TAG = "Downloader_WebGL_Handler";

   private long                           _priority;
   private final URL                      _url;
   private final ArrayList<ListenerEntry> _listeners;

   private Downloader_WebGL               _dl;


   public Downloader_WebGL_Handler(final URL url,
                                   final IDownloadListener listener,
                                   final long priority,
                                   final long requestId) {
      _priority = priority;
      _url = url;
      _listeners = new ArrayList<ListenerEntry>();
      final ListenerEntry entry = new ListenerEntry(listener, requestId);
      _listeners.add(entry);
   }


   public void addListener(final IDownloadListener listener,
                           final long priority,
                           final long requestId) {
      final ListenerEntry entry = new ListenerEntry(listener, requestId);

      _listeners.add(entry);

      if (priority > _priority) {
         _priority = priority;
      }
   }


   public long getPriority() {
      return _priority;
   }


   public boolean cancelListenerForRequestId(final long requestId) {
      boolean canceled = false;

      final Iterator<ListenerEntry> iter = _listeners.iterator();

      while (iter.hasNext() && !canceled) {
         final ListenerEntry entry = iter.next();

         if (entry.getRequestId() == requestId) {
            entry.cancel();
            canceled = true;
         }
      }

      return canceled;
   }


   public boolean removeListenerForRequestId(final long requestId) {
      boolean removed = false;

      final Iterator<ListenerEntry> iter = _listeners.iterator();

      while (iter.hasNext()) {
         final ListenerEntry entry = iter.next();

         if (entry.getRequestId() == requestId) {
            entry.getListener().onCancel(_url);
            iter.remove();
            removed = true;

            break;
         }
      }

      return removed;
   }


   public boolean hasListener() {
      return !_listeners.isEmpty();
   }


   public void runWithDownloader(final IDownloader downloader) {

      log(LogLevel.InfoLevel, ": runWithDownloader url=" + _url.getPath());

      _dl = (Downloader_WebGL) downloader;

      jsRequest(_url.getPath());

      //      IThreadUtils.instance().invokeInRendererThread(new ProcessResponseGTask(statusCode, data, this), true);
   }


   public void removeFromDownloaderDownloadingHandlers() {

      _dl.removeDownloadingHandlerForUrl(_url);

   }


   public void processResponse(final int statusCode,
                               final byte[] data) {

      final boolean dataIsValid = (data != null) && (statusCode == 200);

      if (dataIsValid) {
         final ByteBuffer buffer = new ByteBuffer(data, data.length);
         final Response response = new Response(_url, buffer);
         final Iterator<ListenerEntry> iter = _listeners.iterator();

         while (iter.hasNext()) {
            final ListenerEntry entry = iter.next();
            if (entry.isCanceled()) {
               log(LogLevel.WarningLevel, ": triggering onCanceledDownload");
               entry.getListener().onCanceledDownload(response);

               log(LogLevel.WarningLevel, ": triggering onCancel");
               entry.getListener().onCancel(_url);
            }
            else {
               log(LogLevel.InfoLevel, ": triggering onDownload");
               entry.getListener().onDownload(response);
            }
         }
      }
      else {
         log(LogLevel.ErrorLevel, ": Error runWithDownloader: statusCode=" + statusCode + ", url=" + _url.getPath());

         final ByteBuffer buffer = new ByteBuffer(null, 0);
         final Response response = new Response(_url, buffer);
         final Iterator<ListenerEntry> iter = _listeners.iterator();

         while (iter.hasNext()) {
            final ListenerEntry entry = iter.next();
            log(LogLevel.ErrorLevel, ": triggering onError");
            entry.getListener().onError(response);
         }
      }
   }


   private native void jsRequest(String url) /*-{
		//		debugger;
		console.log("jsRequest url=" + url);

		var thisInstance = this;
		var xhReq = new XMLHttpRequest();
		var buf = new ArrayBuffer();
		xhReq.open("GET", url, true);
		xhReq.responseType = "arraybuffer";
		xhReq.setRequestHeader("Cache-Control", "max-age=31536000");
		xhReq.onload = function() {
			console.log("onload");
			if (xhReq.readyState == 4) {
				thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::jsProcessResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(xhReq);
			}
		};
		xhReq.send(buf);
   }-*/;


   public native void jsProcessResponse(JavaScriptObject xhr) /*-{
		//		debugger;
		console.log("jsProcessResponse");

		var thisInstance = this;
		// inform downloader to remove myself, to avoid adding new Listener
		thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::removeFromDownloaderDownloadingHandlers()();
		var bytes;
		if (xhr.status == 200) {
			bytes = new Uint8Array(xhr.response);
			for ( var i = 0; i < bytes.length; i++) {
				bytes[i] = 0xFF;
			}
		} else {
			bytes = null;
			console.log("Error Retriving Data!");
		}

		thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(I[B)(xhr.status, bytes);
   }-*/;


   public void log(final LogLevel level,
                   final String msg) {
      if (ILogger.instance() != null) {
         switch (level) {
            case InfoLevel:
               ILogger.instance().logInfo(TAG + msg);
               break;
            case WarningLevel:
               ILogger.instance().logWarning(TAG + msg);
               break;
            case ErrorLevel:
               ILogger.instance().logError(TAG + msg);
               break;
            default:
               break;
         }
      }
      else {
         GWT.log(TAG + msg);
      }

   }
}
