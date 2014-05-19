

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.BasicShadersGL2;
import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IG3MBuilder;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.TimeInterval;

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
      //Adding Default Program Sources
      final BasicShadersGL2 shaders = new BasicShadersGL2();
      for (int i = 0; i < shaders.size(); i++) {
         addGPUProgramSources(shaders.get(i));
      }

      setGL(_nativeWidget.getGL());

      _nativeWidget.setWidget(create());

      return _nativeWidget;
   }


   @Override
   protected IThreadUtils createDefaultThreadUtils() {
      return new ThreadUtils_Android(_nativeWidget);
   }


   @Override
   protected IStorage createDefaultStorage() {
      return new SQLiteStorage_Android("g3m.cache", _nativeWidget.getContext());
   }


   @Override
   protected IDownloader createDefaultDownloader() {
      final TimeInterval connectTimeout = TimeInterval.fromSeconds(60);
      final TimeInterval readTimeout = TimeInterval.fromSeconds(65);
      final boolean saveInBackground = true;
      return new CachedDownloader( //
               new Downloader_Android(4, connectTimeout, readTimeout, _nativeWidget.getContext()), //
               getStorage(), //
               saveInBackground);
   }


}
