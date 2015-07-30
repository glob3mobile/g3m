

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.BasicShadersGL2;
import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.GPUProgramFactory;
import org.glob3.mobile.generated.GPUProgramManager;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.MapBooApplicationChangeListener;
import org.glob3.mobile.generated.MapBooBuilder;
import org.glob3.mobile.generated.MapBoo_ViewType;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

import android.content.Context;


public class MapBooBuilder_Android
         extends
            MapBooBuilder {

   private final G3MWidget_Android _nativeWidget;


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
               new Downloader_Android(4, connectTimeout, readTimeout, _nativeWidget.getContext()), //
               getStorage(), //
               saveInBackground);
   }


   public MapBooBuilder_Android(final Context context,
                                final URL serverURL,
                                final URL tubesURL,
                                final String sceneId,
                                final MapBoo_ViewType viewType,
                                final MapBooApplicationChangeListener applicationListener,
                                final boolean enableNotifications,
                                final String token) {
      super(serverURL, tubesURL, sceneId, viewType, applicationListener, enableNotifications, token);

      _nativeWidget = new G3MWidget_Android(context);
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


   @Override
   protected GPUProgramManager createGPUProgramManager() {
      final GPUProgramFactory gpuProgramFactory = new BasicShadersGL2();
      return new GPUProgramManager(gpuProgramFactory);
   }

}
