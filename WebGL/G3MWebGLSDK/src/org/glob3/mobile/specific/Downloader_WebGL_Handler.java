

package org.glob3.mobile.specific;

import java.util.ArrayList;
import java.util.Iterator;

import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;


public class Downloader_WebGL_Handler {

   final static String                    TAG = "Downloader_WebGL_Handler";

   private long                           _priority;
   private final URL                      _url;
   private final ArrayList<ListenerEntry> _listeners;
   private final boolean                  _requestingImage;

   private Downloader_WebGL               _dl;


   public Downloader_WebGL_Handler(final URL url,
                                   final IBufferDownloadListener bufferListener,
                                   final long priority,
                                   final long requestId) {
      _priority = priority;
      _url = url;
      _listeners = new ArrayList<ListenerEntry>();
      final ListenerEntry entry = new ListenerEntry(bufferListener, null, requestId);
      _listeners.add(entry);
      _requestingImage = false;
   }


   public Downloader_WebGL_Handler(final URL url,
                                   final IImageDownloadListener imageListener,
                                   final long priority,
                                   final long requestId) {
      _priority = priority;
      _url = url;
      _listeners = new ArrayList<ListenerEntry>();
      final ListenerEntry entry = new ListenerEntry(null, imageListener, requestId);
      _listeners.add(entry);
      _requestingImage = true;
   }


   public boolean isRequestingImage() {
      return _requestingImage;
   }


   public void addListener(final IBufferDownloadListener listener,
                           final long priority,
                           final long requestId) {
      final ListenerEntry entry = new ListenerEntry(listener, null, requestId);

      _listeners.add(entry);

      if (priority > _priority) {
         _priority = priority;
      }
   }


   public void addListener(final IImageDownloadListener listener,
                           final long priority,
                           final long requestId) {
      final ListenerEntry entry = new ListenerEntry(null, listener, requestId);

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
            entry.onCancel(_url);
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

      if (_requestingImage) {
         jsRequestImage(_url.getPath());
      }
      else {
         jsRequestBuffer(_url.getPath());
      }

      //      IThreadUtils.instance().invokeInRendererThread(new ProcessResponseGTask(statusCode, data, this), true);
   }


   public void removeFromDownloaderDownloadingHandlers() {

      _dl.removeDownloadingHandlerForUrl(_url);

   }


   public void processResponse(final int statusCode,
                               final JavaScriptObject response) {
      boolean dataIsValid;
      JavaScriptObject data;

      if (_requestingImage) {
         // TODO: create img from response
         data = response;
      }
      else {
         // TODO: create bytearray from response
         data = response;
      }

      dataIsValid = (data != null) && (statusCode == 200);

      if (dataIsValid) {
         for (final ListenerEntry entry : _listeners) {
            if (entry.isCanceled()) {
               entry.onCanceledDownload(_url, data);
               entry.onCancel(_url);
            }
            else {
               entry.onDownload(_url, data);
            }
         }
      }
      else {
         log(LogLevel.ErrorLevel, ": Error runWithDownloader: statusCode=" + statusCode + ", url=" + _url.getPath());

         for (final ListenerEntry entry : _listeners) {
            entry.onError(_url);
         }
      }
   }


   // take too much time (15-20 sec per array)
   public byte[] toJavaArrayBytes(final JsArrayInteger bytes) {
      final int length = bytes.length();
      final byte[] byteArray = new byte[length];
      for (int i = 0; i < length; i++) {
         byteArray[i] = (byte) bytes.get(i);
      }
      return byteArray;
   }


   private native void jsRequestBuffer(String url) /*-{
		//		debugger;
		console.log("jsRequest url=" + url);

		var thisInstance = this;
		var xhr = new XMLHttpRequest();
		var buf = new ArrayBuffer();
		xhr.open("GET", url, true);
		xhr.responseType = "arraybuffer";
		xhr.setRequestHeader("Cache-Control", "max-age=31536000");
		xhr.onload = function() {
			console.log("onload");
			if (xhr.readyState == 4) {
				// inform downloader to remove myself, to avoid adding new Listener
				thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::removeFromDownloaderDownloadingHandlers()();
				var response;
				if (xhr.status == 200) {
					response = xhr.response;
				} else {
					response = null;
					console.log("Error Retriving Data!");
				}
				thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, response);
				//				thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::jsProcessBufferResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(xhReq);
			}
		};
		xhr.send(buf);
   }-*/;


   //   public native void jsProcessBufferResponse(JavaScriptObject xhr) /*-{
   //		debugger;
   //		console.log("jsProcessResponse");
   //
   //		var thisInstance = this;
   //		// inform downloader to remove myself, to avoid adding new Listener
   //		thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::removeFromDownloaderDownloadingHandlers()();
   //		var uint8array;
   //		if (xhr.status == 200) {
   //			uint8array = new Uint8Array(xhr.response);
   //		} else {
   //			uint8array = null;
   //			console.log("Error Retriving Data!");
   //		}
   //		thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processBufferResponse(ILcom/google/gwt/core/client/JsArrayInteger;)(xhr.status, uint8array);
   //   }-*/;


   private native void jsRequestImage(String url) /*-{
		debugger;
		// TODO implements jsRequestImage
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
