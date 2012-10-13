

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.InitializationContext;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.JavaScriptObject;


public final class IndexedDBStorage_WebGL
         implements
            IStorage {

   private JavaScriptObject _db;


   public IndexedDBStorage_WebGL() {
      jsCreateOrOpenDB();
   }


   @Override
   public boolean containsBuffer(final URL url) {
      return false;
   }


   @Override
   public IByteBuffer readBuffer(final URL url) {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public boolean containsImage(final URL url) {
      return false;
   }


   @Override
   public IImage readImage(final URL url) {
      return null;
   }


   @Override
   public void onResume(final InitializationContext ic) {
   }


   @Override
   public void onPause(final InitializationContext ic) {
   }


   private native void jsCreateOrOpenDB() /*-{
		//		debugger;
		var thisInstance = this;

		console.log($wnd.g3mIDB);

		var request = $wnd.g3mIDB.open("g3mCache", $wnd.g3mDBVersion);
		var db;

		request.onerror = function(event) {
			console.log("Error creating/accessing IndexedDB database");
		};

		request.onsuccess = function(event) {
			console.log("Success creating/accessing IndexedDB database");
			db = request.result;

			db.onerror = function(event) {
				console.log("Error creating/accessing IndexedDB database");
			};

			db.onsuccess = function() {
				thisInstance.@org.glob3.mobile.specific.IndexedDBStorage_WebGL::_db = db;
				console.log("Success creating/accessing IDB database");
			};

			// Interim solution for Google Chrome to create an objectStore. Will be deprecated
			//			if (db.setVersion) {
			//				if (db.version != g3mDBVersion) {
			//					var setVersion = db.setVersion(g3mDBVersion);
			//					setVersion.onsuccess = function() {
			//						createObjectStore(db);
			//						getImageFile();
			//					};
			//				} else {
			//					getImageFile();
			//				}
			//			} else {
			//				getImageFile();
			//			}
		}

		// For future use. Currently only in latest Firefox versions
		//		request.onupgradeneeded = function(event) {
		//			//			createObjectStore(event.target.result);
		//		};
   }-*/;


   @Override
   public boolean isAvailable() {
      return false;
   }


   @Override
   public void saveBuffer(final URL url,
                          final IByteBuffer buffer,
                          final boolean saveInBackground) {
   }


   @Override
   public void saveImage(final URL url,
                         final IImage image,
                         final boolean saveInBackground) {
   }

}
