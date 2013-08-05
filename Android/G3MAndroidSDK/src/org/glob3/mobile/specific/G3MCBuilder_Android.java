

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.G3MCBuilder;
import org.glob3.mobile.generated.G3MCSceneChangeListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

import android.content.Context;


public class G3MCBuilder_Android
         extends
            G3MCBuilder {

   private final G3MWidget_Android _nativeWidget;


   public G3MCBuilder_Android(final Context context,
                              final URL serverURL,
                              final URL tubesURL,
                              final boolean useWebSockets,
                              final String sceneId,
                              final G3MCSceneChangeListener sceneListener) {
      super(serverURL, tubesURL, useWebSockets, sceneId, sceneListener);

      _nativeWidget = new G3MWidget_Android(context);

   }


   @Override
   protected IStorage createStorage() {
      return new SQLiteStorage_Android("g3m.cache", _nativeWidget.getContext());
   }


   @Override
   protected IDownloader createDownloader() {
      final TimeInterval connectTimeout = TimeInterval.fromSeconds(10);
      final TimeInterval readTimeout = TimeInterval.fromSeconds(15);
      final boolean saveInBackground = true;
      return new CachedDownloader( //
               new Downloader_Android(8, connectTimeout, readTimeout, _nativeWidget.getContext()), //
               getStorage(), //
               saveInBackground);
   }


   @Override
   protected IThreadUtils createThreadUtils() {
      return new ThreadUtils_Android(_nativeWidget);
   }


   public G3MWidget_Android createWidget() {
      setGL(_nativeWidget.getGL());

      _nativeWidget.setWidget(create());

      return _nativeWidget;
   }


}
