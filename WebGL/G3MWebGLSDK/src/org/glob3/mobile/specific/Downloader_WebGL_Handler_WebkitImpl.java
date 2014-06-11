

package org.glob3.mobile.specific;


public final class Downloader_WebGL_Handler_WebkitImpl
         extends
            Downloader_WebGL_Handler_DefaultImpl {

   private static final boolean _isChrome = jsIsChrome();


   public Downloader_WebGL_Handler_WebkitImpl() {
   }


   private native static boolean jsIsChrome() /*-{
		return $wnd.navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
   }-*/;


   @Override
   public native void jsRequest(String url) /*-{
		//              debugger;
		//		console.log("jsRequest safari");

		var that = this;

		var xhr = new XMLHttpRequest();
		xhr.open("GET", url, true);
		xhr.responseType = "arraybuffer";
		//xhr.setRequestHeader("Cache-Control", "max-age=31536000");
		xhr.onload = function() {
			if (xhr.readyState == 4) {
				// inform downloader to remove myself, to avoid adding new Listener
				that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::removeFromDownloaderDownloadingHandlers()();
				var response = null;
				if (xhr.status === 200) {
					if (that.@org.glob3.mobile.specific.Downloader_WebGL_Handler_DefaultImpl::_requestingImage) {
						if (@org.glob3.mobile.specific.Downloader_WebGL_Handler_WebkitImpl::_isChrome) {
							var dataView = new DataView(xhr.response);
							response = new Blob([ dataView ], {
								type : 'image/png'
							});
						}
						else {
							response = new Blob([ xhr.response ]);
						}
						that.@org.glob3.mobile.specific.Downloader_WebGL_Handler_DefaultImpl::jsCreateImageFromBlob(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, response);
					}
					else {
						response = xhr.response;
						that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, response);
					}
				}
				else {
					console.log("Error Retrieving Data!");
					that.@org.glob3.mobile.specific.Downloader_WebGL_Handler::processResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(xhr.status, response);
				}
			}
		};
		xhr.send();
   }-*/;
}
