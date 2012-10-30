

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IG3MJSONBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.SceneParser;
import org.glob3.mobile.generated.UserData;


public class G3MJSONBuilder_Android
         extends
            IG3MJSONBuilder {

   G3MWidget_Android _g3mWidget;


   public G3MJSONBuilder_Android(final String jsonSource,
                                 final G3MWidget_Android g3mWidget) {
      super(jsonSource);
      _g3mWidget = g3mWidget;
   }


   @Override
   public void initWidgetWithCameraConstraints(final ArrayList<ICameraConstrainer> cameraConstraints,
                                               final LayerSet layerSet,
                                               final ArrayList<Renderer> renderers,
                                               final UserData userData,
                                               final GTask initializationTask,
                                               final ArrayList<PeriodicalTask> periodicalTasks) {


      final SQLiteStorage_Android storage = new SQLiteStorage_Android("g3m.cache", _g3mWidget.getContext());

      final int connectTimeout = 60000;
      final int readTimeout = 60000;

      final boolean saveInBackground = true;
      final IDownloader downloader = new CachedDownloader(new Downloader_Android(8, connectTimeout, readTimeout), storage,
               saveInBackground);
      downloader.start();

      SceneParser.instance().parse(layerSet, downloader, renderers, _jsonSource);

      _g3mWidget.initWidget(cameraConstraints, layerSet, renderers, userData, initializationTask);

   }


}
