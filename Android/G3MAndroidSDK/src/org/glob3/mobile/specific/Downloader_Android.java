package org.glob3.mobile.specific;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.glob3.mobile.generated.IBIL;
import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Url;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.DownloadListener;

public class Downloader_Android extends IDownloader {
	
	private ArrayList<Downloader_Android_WorkerThread> _workers;
	
	// downloads current in progress
	private Map<Object, Object> _downloadingHandlers = new HashMap<Object, Object>();

	// queued downloads
	private Map<Object, Object> _queuedHandlers = new HashMap<Object, Object>();

	long                 _requestIdCounter;

	Downloader_Android(int memoryCapacity, int diskCapacity, String diskPath, int maxConcurrentOperationCount){
//		  NSURLCache *sharedCache = [[NSURLCache alloc] initWithMemoryCapacity: memoryCapacity
//                                     diskCapacity: diskCapacity
//                                         diskPath: toNSString(diskPath)];
//		  [NSURLCache setSharedURLCache:sharedCache];

		for (int i = 0; i < maxConcurrentOperationCount; i++) {
			Downloader_Android_WorkerThread worker = [Downloader_iOS_WorkerThread workerForDownloader: this];
//[worker start];

[_workers addObject: worker];
}
	}

	Downloader_Android_Handler getHandlerToRun(){
		
	}


	@Override
	public synchronized void start() {
		for (int i = 0; i < _workers.size(); i++){
			Downloader_Android_WorkerThread worker = _workers.get(i);
			worker.start();
		}
	}

	@Override
	public synchronized int request(Url url, int priority, IDownloadListener listener) {
		  Downloader_iOS_Listener iosListener = [[Downloader_iOS_Listener alloc] initWithCPPListener: cppListener];
		  
		  Downloader_iOS_Handler* handler = nil;
		  
		  [_lock lock];

		  const long requestId = _requestIdCounter++;
		  
		  handler = [_downloadingHandlers objectForKey: nsURL];
		  if (handler) {
		    // the URL is being downloaded, just add the new listener.
		    [handler addListener: iosListener
		                priority: priority
		               requestId: requestId];
		  }
		  
		  if (!handler) {
		    handler = [_queuedHandlers objectForKey: nsURL];
		    if (handler) {
		      // the URL is queued for future download, just add the new listener.
		      [handler addListener: iosListener
		                  priority: priority
		                 requestId: requestId];
		    }
		  }
		  
		  if (!handler) {
		    // new handler, queue it
		    handler = [[Downloader_iOS_Handler alloc] initWithNSURL: nsURL
		                                                        url: new Url(url)
		                                                   listener: iosListener
		                                                   priority: priority
		                                                  requestId: requestId];
		    [_queuedHandlers setObject: handler
		                        forKey: nsURL];
		  }
		  
		  [_lock unlock];
		  
		  return requestId;
	}

	@Override
	public synchronized void cancelRequest(int requestId) {
		// TODO Auto-generated method stub

	}

}

class Downloader_Android_Handler{
	
	class ListenerEntry{
		public final IDownloadListener _listener;
		public final long 		_requestID;

		public ListEntry(IDownloadListener dl, long rid){
			_listener = dl;
			_requestID = rid;
		}
	};
	
	ArrayList<ListenerEntry> _listeners = new ArrayList<ListenerEntry>();
	long            _priority;
	Url             _url;
	
	Downloader_Android_Handler(Url url, IDownloadListener listener, long priority, long requestID){
		_priority = priority;
		_url = url;
		
		_listeners.add(new ListenerEntry(listener, requestID));
	}
	
	public synchronized void addListener(IDownloadListener listener, long priority, long requestId){
		_listeners.add(new ListenerEntry(listener, requestId));
		
		if (_priority < priority){
			_priority = priority;
		}
	}
	

	public synchronized boolean removeListenerForRequestId(long requestId){
		  boolean removed = false;

		  final int listenersCount = _listeners.size();
		  for (int i = 0; i < listenersCount; i++) {
		    ListenerEntry entry = _listeners.get(i);
		    if (entry._requestID == requestId) {
		      entry._listener.onCancel(_url);
		      _listeners.remove(i);
		      removed = true;
		      break;
		    }
		  }
		  
		  return removed;
	}

	public synchronized boolean hasListeners(long requestId){
		return _listeners.size() > 0;
	}

	public synchronized long getPriority(){
		return _priority;
	}

	public synchronized void runWithDownloader(Object downloaderV){
	  Downloader_Android downloader = Downloader_Android downloaderV;
	  
	  NSURLRequest *request = [NSURLRequest requestWithURL: _nsURL
	                                           cachePolicy: NSURLRequestUseProtocolCachePolicy
	                                       timeoutInterval: 60.0];
	  
	  NSURLResponse *urlResponse;
	  NSError *error;
	  NSData* data = [NSURLConnection sendSynchronousRequest: request
	                                       returningResponse: &urlResponse
	                                                   error: &error];
	  
	  Url url( [[_nsURL absoluteString] cStringUsingEncoding:NSUTF8StringEncoding] );
	  
	  // inform downloader to remove myself, to avoid adding new Listeners
	  downloader->removeDownloadingHandlerForNSURL(_nsURL);
	  
	  dispatch_async( dispatch_get_main_queue(), ^{
	    // Add code here to update the UI/send notifications based on the
	    // results of the background processing
	    
	    [_lock lock];
	    
	    if (data) {
	      const int length = [data length];
	      unsigned char *bytes = new unsigned char[ length ]; // will be deleted by ByteBuffer's destructor
	      [data getBytes:bytes length: length];
	      ByteBuffer buffer(bytes, length);
	      
	      Response response(url, &buffer);
	      
	      const int listenersCount = [_listeners count];
	      for (int i = 0; i < listenersCount; i++) {
	        ListenerEntry* entry = [_listeners objectAtIndex: i];
	        
	        [[entry listener] onDownload: response];
	      }
	    }
	    else {
	      /*ILogger::instance()->logError("Can't load %s, response=%s, error=%s",
	       [ [_nsURL      description] cStringUsingEncoding: NSUTF8StringEncoding ],
	       (urlResponse!=0)? [ [urlResponse description] cStringUsingEncoding: NSUTF8StringEncoding ] : "NULL",
	       [ [error       description] cStringUsingEncoding: NSUTF8StringEncoding ] );*/
	      
	      //ILogger::instance()->logError("Can't load %s\n", [[_nsURL absoluteString] UTF8String]);
	      printf ("Can't load %s\n", [[_nsURL absoluteString] UTF8String]);
	      
	      
	      ByteBuffer buffer(NULL, 0);
	      
	      Response response(url, &buffer);
	      
	      const int listenersCount = [_listeners count];
	      for (int i = 0; i < listenersCount; i++) {
	        ListenerEntry* entry = [_listeners objectAtIndex: i];
	        
	        [[entry listener] onError: response];
	      }
	    }
	    
	    //  [_listeners removeAllObjects];
	    
	    [_lock unlock];
	    
	  });
	  
	}
	- (void) dealloc;
	
}

class Downloader_Android_WorkerThread extends AsyncTask<String, Void, Void> {

	byte[] _data = null;
	boolean finished = false;
	
	Downloader_Android_WorkerThread()
	
	public static byte[] getAsByteArray(final String url_) throws IOException {

		final URL url = new URL(url_);
		final URLConnection connection = url.openConnection();
		final InputStream in = connection.getInputStream();

		final int contentLength = connection.getContentLength();

		// To avoid having to resize the array over and over and over as
		// bytes are written to the array, provide an accurate estimate of
		// the ultimate size of the byte array
		ByteArrayOutputStream tmpOut;
		if (contentLength != -1) {
			tmpOut = new ByteArrayOutputStream(contentLength);
		} else {
			tmpOut = new ByteArrayOutputStream(16384); // Pick some appropriate size
		}

		final byte[] buf = new byte[512];
		while (true) {
			final int len = in.read(buf);
			if (len == -1) {
				break;
			}
			tmpOut.write(buf, 0, len);
		}
		in.close();
		tmpOut.close(); // No effect, but good to do anyway to keep the metaphor alive

		final byte[] array = tmpOut.toByteArray();
		return array;
	}

	@Override
	protected Void doInBackground(final String... urls) {
		try {
			_data = getAsByteArray(urls[0]);
			finished = true; // NET CAN GET THE DATA
		} catch (final IOException e) {
			Log.d("NET", "FALLO " + urls[0]);
			String msg = e.toString();
			msg = msg + "";
		}
		return null;
	}

}

}
