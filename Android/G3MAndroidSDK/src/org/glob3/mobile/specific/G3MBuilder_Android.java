

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IG3MBuilder;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;

import android.content.Context;


public class G3MBuilder_Android
         extends
            IG3MBuilder {

   private final G3MWidget_Android _nativeWidget;


   public G3MBuilder_Android(final Context context) {
      super();

      _nativeWidget = new G3MWidget_Android(context);
   }


   public G3MWidget_Android createWidget() {
      final NativeGL2_Android nativeGL = new NativeGL2_Android();
      setNativeGL(nativeGL);

      _nativeWidget.setWidget(create());

      return _nativeWidget;
   }


   @Override
   protected IThreadUtils createThreadUtils() {
      final ThreadUtils_Android threadUtils = new ThreadUtils_Android(_nativeWidget);

      return threadUtils;
   }


   @Override
   protected IStorage createStorage() {
      final SQLiteStorage_Android storage = new SQLiteStorage_Android("g3m.cache", _nativeWidget.getContext());

      return storage;
   }


   @Override
   protected IDownloader createDownloader() {
      final int connectTimeout = 20000;
      final int readTimeout = 30000;
      final boolean saveInBackground = true;
      final IDownloader downloader = new CachedDownloader( //
               new Downloader_Android(8, connectTimeout, readTimeout, _nativeWidget.getContext()), //
               (_storage != null) ? _storage : createStorage(), //
               saveInBackground);

      return downloader;
   }
}
