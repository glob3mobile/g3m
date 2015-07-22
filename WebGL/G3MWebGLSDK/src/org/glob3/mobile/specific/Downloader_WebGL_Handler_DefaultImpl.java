

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


public class Downloader_WebGL_Handler_DefaultImpl
         implements
            Downloader_WebGL_Handler {

   private final static String      TAG = "Downloader_WebGL_HandlerImpl";

   private long                     _priority;
   private URL                      _url;
   private ArrayList<ListenerEntry> _listeners;
   private boolean                  _requestingImage;

   private Downloader_WebGL         _dl;


   public Downloader_WebGL_Handler_DefaultImpl() {
   }


   @Override
   final public void init(final URL url,
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


   @Override
   final public void init(final URL url,
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


   @Override
   final public boolean isRequestingImage() {
      return _requestingImage;
   }


   @Override
   final public void addListener(final IBufferDownloadListener listener,
                                 final long priority,
                                 final long requestId) {
      final ListenerEntry entry = new ListenerEntry(listener, null, requestId);

      _listeners.add(entry);

      if (priority > _priority) {
         _priority = priority;
      }
   }


   @Override
   final public void addListener(final IImageDownloadListener listener,
                                 final long priority,
                                 final long requestId) {
      final ListenerEntry entry = new ListenerEntry(null, listener, requestId);

      _listeners.add(entry);

      if (priority > _priority) {
         _priority = priority;
      }
   }


   @Override
   final public long getPriority() {
      return _priority;
   }


   @Override
   final public boolean cancelListenerForRequestId(final long requestId) {
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


   @Override
   final public boolean removeListenerForRequestId(final long requestId) {
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


   @Override
   final public boolean hasListener() {
      return !_listeners.isEmpty();
   }


   @Override
   final public void runWithDownloader(final IDownloader downloader) {

      //      log(LogLevel.InfoLevel, ": runWithDownloader url=" + _url._path);

      _dl = (Downloader_WebGL) downloader;

      jsRequest(_url._path);

      //      IThreadUtils.instance().invokeInRendererThread(new ProcessResponseGTask(statusCode, data, this), true);
   }


   @Override
   final public void removeFromDownloaderDownloadingHandlers() {

      _dl.removeDownloadingHandlerForUrl(_url);

   }


   @Override
   final public void processResponse(final int statusCode,
                                     final JavaScriptObject data) {
      final boolean dataIsValid = (data != null) && (statusCode == 200);

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
         log(LogLevel.ErrorLevel, ": Error runWithDownloader: statusCode=" + statusCode + ", data=" + data + ", url="
                                  + _url._path);

         for (final ListenerEntry entry : _listeners) {
            entry.onError(_url);
         }
      }
   }


   @Override
   public native void jsRequest(String url) /*-{
		//		debugger;
		//		console.log("jsRequest url=" + url);

		var that = this;
		var xhr = new XMLHttpRequest();
		xhr.open("GET", url, true);
		xhr.responseType = (that.@org.glob3.mobile.specific.Downloader_WebGL_Handler_DefaultImpl::_requestingImage) ? "blob"
				: "arraybuffer";
		//xhr.setRequestHeader("Cache-Control", "max-age=31536000");
		xhr.onload = function() {
			if (xhr.readyState == 4) {
				// inform downloader to remove myself, to avoid adding new Listener
				that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::removeFromDownloaderDownloadingHandlers()();
				if (xhr.status === 200) {
					if (that.@org.glob3.mobile.specific.Downloader_WebGL_Handler_DefaultImpl::_requestingImage) {
						that.@org.glob3.mobile.specific.Downloader_WebGL_Handler_DefaultImpl::jsCreateImageFromBlob(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, xhr.response);
					}
					else {
						that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, xhr.response);
					}
				}
				else {
					console.log("Error Retrieving Data!");
					that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, null);
				}
			}
		};
		xhr.send();
   }-*/;


   private native void jsCreateImageFromBlob(final int xhrStatus,
                                             final JavaScriptObject blob) /*-{
		var that = this;

		var auxImg = new Image();
		var imgURL = $wnd.g3mURL.createObjectURL(blob);

		auxImg.onload = function() {
			that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhrStatus, auxImg);
			$wnd.g3mURL.revokeObjectURL(imgURL);
		};
		auxImg.onerror = function() {
			that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhrStatus, null);
			$wnd.g3mURL.revokeObjectURL(imgURL);
		};
		auxImg.onabort = function() {
			that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhrStatus, null);
			$wnd.g3mURL.revokeObjectURL(imgURL);
		};

		auxImg.src = imgURL;
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
