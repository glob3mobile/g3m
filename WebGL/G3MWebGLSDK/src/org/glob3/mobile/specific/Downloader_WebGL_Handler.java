

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

      //      log(LogLevel.InfoLevel, ": runWithDownloader url=" + _url.getPath());

      _dl = (Downloader_WebGL) downloader;

      jsRequest(_url.getPath());

      //      IThreadUtils.instance().invokeInRendererThread(new ProcessResponseGTask(statusCode, data, this), true);
   }


   public void removeFromDownloaderDownloadingHandlers() {

      _dl.removeDownloadingHandlerForUrl(_url);

   }


   public void processResponse(final int statusCode,
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
         log(LogLevel.ErrorLevel, ": Error runWithDownloader: statusCode=" + statusCode + ", url=" + _url.getPath());

         for (final ListenerEntry entry : _listeners) {
            entry.onError(_url);
         }
      }
   }


   private native void jsRequest(String url) /*-{
		//		debugger;
		//		console.log("jsRequest url=" + url);

		var thisInstance = this;
		if (thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::_requestingImage) {

			var createImageFromBlob = function(blob) {
				var auxImg = new Image();
				auxImg.id = url;
				var imgURL = $wnd.g3mURL.createObjectURL(blob);

				auxImg.onload = function() {
					thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, auxImg);
					$wnd.g3mURL.revokeObjectURL(imgURL);
				};

				auxImg.onerror = function() {
					thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, null);
					$wnd.g3mURL.revokeObjectURL(imgURL);
				};
				auxImg.onabort = function() {
					thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, null);
					$wnd.g3mURL.revokeObjectURL(imgURL);
				};

				auxImg.src = imgURL;
			};

			var xhr = new XMLHttpRequest();
			xhr.open("GET", url, true);
			xhr.responseType = "blob";
			xhr.setRequestHeader("Cache-Control", "max-age=31536000");
			xhr.onload = function() {
				if (xhr.readyState == 4) {
					// inform downloader to remove myself, to avoid adding new Listener
					thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::removeFromDownloaderDownloadingHandlers()();
					var response;
					if (xhr.status === 200) {
						createImageFromBlob(xhr.response);
					} else {
						console.log("Error Retriving Data!");
						response = null;
						thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, response);
					}
				}
			};
			xhr.send();
		} else {
			var xhr = new XMLHttpRequest();
			var buf = new ArrayBuffer();
			xhr.open("GET", url, true);
			xhr.responseType = "arraybuffer";
			xhr.setRequestHeader("Cache-Control", "max-age=31536000");
			xhr.onload = function() {
				if (xhr.readyState == 4) {
					// inform downloader to remove myself, to avoid adding new Listener
					thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::removeFromDownloaderDownloadingHandlers()();
					var response;
					if (xhr.status == 200) {
						response = xhr.response;
					} else {
						console.log("Error Retriving Data!");
						response = null;
					}
					thisInstance.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, response);
				}
			};
			xhr.send(buf);
		}
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
