package org.glob3.mobile.specific;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.URL;

public class Downloader_Android implements IDownloader {

   private long                                       _requestIdCounter;
   private long                                       _requestsCounter;
   private long                                       _cancelsCounter;
   private ArrayList<Downloader_Android_WorkerThread> _workers;
   private Map<String, Downloader_Android_Handler>    _downloadingHandlers;
   private Map<String, Downloader_Android_Handler>    _queuedHandlers;


   Downloader_Android(int memoryCapacity,
                      int diskCapacity,
                      String diskPath,
                      int maxConcurrentOperationCount) {
      // TODO memoryCapacity ??
      // TODO diskCapacity ??
      // TODO diskPath ??

      _requestIdCounter = 1;
      _requestsCounter = 0;
      _cancelsCounter = 0;
      // TODO String or Url as key??
      _downloadingHandlers = new HashMap<String, Downloader_Android_Handler>();
      _queuedHandlers = new HashMap<String, Downloader_Android_Handler>();
      _workers = new ArrayList<Downloader_Android_WorkerThread>(maxConcurrentOperationCount);

      for (int i = 0; i < maxConcurrentOperationCount; i++) {
         _workers.add(new Downloader_Android_WorkerThread(this));
      }
   }


   @Override
   public void start() {
      Iterator<Downloader_Android_WorkerThread> iter = _workers.iterator();
      while (iter.hasNext()) {
         iter.next().execute();
      }
   }
   
   // TODO stop()
//   @Override
   public void stop() {
      Iterator<Downloader_Android_WorkerThread> iter = _workers.iterator();
      while (iter.hasNext()) {
         iter.next().stop();
      }
   }


   @Override
   public int request(URL url, int priority, IDownloadListener listener, boolean deleteListener) {

      Downloader_Android_Handler handler = null;
      long requestId;

      synchronized (this) {
         _requestsCounter++;
         requestId = _requestIdCounter++;
         handler = _downloadingHandlers.get(url.getPath());

         if (handler != null) {
            // the URL is being downloaded, just add the new listener
            handler.addListener(listener, priority, requestId);
         }
         else {
            handler = _queuedHandlers.get(url);
            if (handler != null) {
               // the URL is queued for future download, just add the new listener
               handler.addListener(listener, priority, requestId);
            }
            else {
               // new handler, queue it
               handler = new Downloader_Android_Handler(url, listener, priority, requestId);
               _queuedHandlers.put(url.getPath(), handler);
            }
         }
      }

      return requestId;
   }


   @Override
   public void cancelRequest(int requestId) {
      if (requestId < 0) {
         return;
      }

      synchronized (this) {
         _cancelsCounter++;

         boolean found = false;
         Iterator<Map.Entry<String, Downloader_Android_Handler>> iter = _queuedHandlers.entrySet().iterator();

         while (iter.hasNext() && !found) {
            Map.Entry<String, Downloader_Android_Handler> e = iter.next();
            String url = e.getKey();
            Downloader_Android_Handler handler = e.getValue();

            if (handler.removeListenerForRequestId(requestId)) {
               if (!handler.hasListener()) {
                  _queuedHandlers.remove(url);
               }
               found = true;
            }
         }

         if (!found) {
            iter = _downloadingHandlers.entrySet().iterator();

            while (iter.hasNext() && !found) {
               Map.Entry<String, Downloader_Android_Handler> e = iter.next();
               Downloader_Android_Handler handler = e.getValue();

               if (handler.cancelListenerForRequestId(requestId)) {
                  found = true;
               }
            }
         }
      }
   }


   public synchronized void removeDownloadHandlerForUrl(String url) {
      _downloadingHandlers.remove(url);
   }


   public Downloader_Android_Handler getHandlerToRun() {
      long selectedPriority = -100000000; // TODO: LONG_MAX_VALUE;
      Downloader_Android_Handler selectedHandler = null;
      String selectedURL = null;

      synchronized (this) {
         Iterator<Map.Entry<String, Downloader_Android_Handler>> it = _queuedHandlers.entrySet().iterator();

         while (it.hasNext()) {
            Map.Entry<String, Downloader_Android_Handler> e = it.next();
            String url = e.getKey();
            Downloader_Android_Handler handler = e.getValue();
            long priority = handler.getPriority();

            if (priority > selectedPriority) {
               selectedPriority = priority;
               selectedHandler = handler;
               selectedURL = url;
            }
         }

         if (selectedHandler != null) {
            // move the selected handler to _downloadingHandlers collection
            _queuedHandlers.remove(selectedURL);
            _downloadingHandlers.put(selectedURL, selectedHandler);
         }
      }

      return selectedHandler;
   }

   @Override
   public String statistics() {
      // TODO Auto-generated method stub
      return null;
   }

}

//PRELIMINAR IMPLEMENTATION NOT FINISHED YET
//DO NOT ERASE!

/*

public class Downloader_Android extends IDownloader {

	private ArrayList<Downloader_Android_WorkerThread> _workers;

	// downloads current in progress
	private Map<String, Downloader_Android_Handler> _downloadingHandlers = new HashMap<String, Downloader_Android_Handler>();

	// queued downloads
	private Map<String, Downloader_Android_Handler> _queuedHandlers = new HashMap<String, Downloader_Android_Handler>();

	long _requestIdCounter;

	Downloader_Android(int memoryCapacity, int diskCapacity, String diskPath,
			int maxConcurrentOperationCount) {
		// NSURLCache *sharedCache = [[NSURLCache alloc] initWithMemoryCapacity:
		// memoryCapacity
		// diskCapacity: diskCapacity
		// diskPath: toNSString(diskPath)];
		// [NSURLCache setSharedURLCache:sharedCache];

		for (int i = 0; i < maxConcurrentOperationCount; i++) {
			Downloader_Android_WorkerThread worker = new Downloader_Android_WorkerThread(
					this);
			_workers.add(worker);
		}
	}

	Downloader_Android_Handler getHandlerToRun() {
		long selectedPriority = -1000000;
		Downloader_Android_Handler selectedHandler = null;

		Iterator<Entry<String, Downloader_Android_Handler>> it = _queuedHandlers
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Downloader_Android_Handler> e = it.next();
			Downloader_Android_Handler handler = e.getValue();

			if (handler._priority > selectedPriority) {
				selectedPriority = handler._priority;
				selectedHandler = handler;
			}
		}

		return selectedHandler;
	}

	@Override
	public synchronized void start() {
		for (int i = 0; i < _workers.size(); i++) {
			Downloader_Android_WorkerThread worker = _workers.get(i);
			worker.start();
		}
	}

	@Override
	public synchronized long request(Url url, int priority,
			IDownloadListener listener) {
		Downloader_Android_Handler handler = null;
		final long requestId = _requestIdCounter++;

		handler = _downloadingHandlers.get(url.getPath());

		if (handler != null) {
			// the URL is being downloaded, just add the new listener.
			handler.addListener(listener, priority, requestId);
		}

		if (handler == null) {
			handler = _queuedHandlers.get(url.getPath());
			if (handler != null) {
				// the URL is queued for future download, just add the new
				// listener.
				handler.addListener(listener, priority, requestId);
			}
		}

		if (handler != null) {
			// new handler, queue it
			Downloader_Android_Handler h = new Downloader_Android_Handler(url,
					listener, priority, requestId);
			_queuedHandlers.put(url.getPath(), h);
		}
		return requestId;
	}

	@Override
	public synchronized void cancelRequest(int requestId) {
		Iterator<Entry<String, Downloader_Android_Handler>> it = _queuedHandlers
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Downloader_Android_Handler> e = it.next();
			Downloader_Android_Handler handler = e.getValue();

			handler.removeListenerForRequestId(requestId);
			if (!handler.hasListeners()) {
				_queuedHandlers.remove(handler.getURL().getPath());
			}
		}
	}

}

class Downloader_Android_Handler {

	class ListenerEntry {
		public final IDownloadListener _listener;
		public final long _requestID;

		public ListenerEntry(IDownloadListener dl, long rid) {
			_listener = dl;
			_requestID = rid;
		}
	};

	private ArrayList<ListenerEntry> _listeners = new ArrayList<ListenerEntry>();
	private long _priority;
	private Url _url;

	Downloader_Android_Handler(Url url, IDownloadListener listener,
			long priority, long requestID) {
		_priority = priority;
		_url = url;

		_listeners.add(new ListenerEntry(listener, requestID));
	}

	public synchronized void addListener(IDownloadListener listener,
			long priority, long requestId) {
		_listeners.add(new ListenerEntry(listener, requestId));

		if (_priority < priority) {
			_priority = priority;
		}
	}

	public synchronized boolean removeListenerForRequestId(long requestId) {
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

	public synchronized boolean hasListeners() {
		return _listeners.size() > 0;
	}

	public synchronized long getPriority() {
		return _priority;
	}
	
	public synchronized Url getURL() {
		return _url;
	}

	public synchronized void runWithDownloader(Object downloaderV){
	  Downloader_Android downloader = (Downloader_Android) downloaderV;
	  
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
//	       ILogger::instance()->logError("Can't load %s, response=%s, error=%s",
//	       [ [_nsURL      description] cStringUsingEncoding: NSUTF8StringEncoding ],
//	       (urlResponse!=0)? [ [urlResponse description] cStringUsingEncoding: NSUTF8StringEncoding ] : "NULL",
//	       [ [error       description] cStringUsingEncoding: NSUTF8StringEncoding ] );
	      
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
}

class Downloader_Android_WorkerThread extends AsyncTask<String, Void, Void> {

	byte[] _data = null;
	boolean finished = false;

	final IDownloader _downloader = null;

	Downloader_Android_WorkerThread(IDownloader downloader) {
		_downloader = downloader;
	}

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
			tmpOut = new ByteArrayOutputStream(16384); // Pick some appropriate
														// size
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
		tmpOut.close(); // No effect, but good to do anyway to keep the metaphor
						// alive

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
*/
