

package org.glob3.mobile.specific;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GEOJSONDownloadListener;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IG3MJSONBuilder;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.PanoDownloadListener;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.SceneParser;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.UserData;


public class G3MJSONBuilder_Android
         extends
            IG3MJSONBuilder {

   private final G3MWidget_Android _g3mWidget;


   public G3MJSONBuilder_Android(final String jsonSource,
                                 final G3MWidget_Android g3mWidget) {
      super(jsonSource);
      _g3mWidget = g3mWidget;
   }


   @Override
   public void initWidgetWithCameraConstraints(final ArrayList<ICameraConstrainer> cameraConstraints,
                                               final LayerSet layerSet,
                                               final boolean incrementalTileQuality,
                                               final ArrayList<Renderer> renderers,
                                               final UserData userData,
                                               final GTask initializationTask,
                                               final ArrayList<PeriodicalTask> periodicalTasks,
                                               final MarkTouchListener markTouchListener,
                                               final MarkTouchListener panoTouchListener) {

      final boolean readyWhenMarksReady = false;
      final MarksRenderer marksRenderer = new MarksRenderer(readyWhenMarksReady);

      if (markTouchListener != null) {
         marksRenderer.setMarkTouchListener(markTouchListener, true);
      }
      renderers.add(marksRenderer);

      SceneParser.instance().parse(layerSet, _jsonSource);

      _g3mWidget.initWidget(cameraConstraints, layerSet, renderers, userData, new G3MJSONBuilderInitializationTask(
               initializationTask, marksRenderer, SceneParser.instance().getMapGeoJSONSources(),
               SceneParser.instance().getPanoSources(), _g3mWidget, panoTouchListener), periodicalTasks, incrementalTileQuality);
   }
}


class G3MJSONBuilderInitializationTask
         extends
            GTask {

   private final GTask               _customInitializationTask;
   private final MarksRenderer       _marksRenderer;
   private final Map<String, String> _mapGeoJSONSources;
   private final ArrayList<String>   _panoSources;
   private final MarkTouchListener   _panoTouchListener;
   private final G3MWidget_Android   _g3mWidget;


   public G3MJSONBuilderInitializationTask(final GTask customInitializationTask,
                                           final MarksRenderer marksRenderer,
                                           final Map<String, String> mapGeoJSONSources,
                                           final ArrayList<String> panoSources,
                                           final G3MWidget_Android g3mWidget,
                                           final MarkTouchListener panoTouchListener) {
      _customInitializationTask = customInitializationTask;
      _marksRenderer = marksRenderer;
      _mapGeoJSONSources = mapGeoJSONSources;
      _panoSources = panoSources;
      _panoTouchListener = panoTouchListener;
      _g3mWidget = g3mWidget;
   }


   @Override
   public void run(final G3MContext context) {
      if (!_mapGeoJSONSources.isEmpty()) {
         final Iterator<Entry<String, String>> it = _mapGeoJSONSources.entrySet().iterator();
         while (it.hasNext()) {
            final Entry<String, String> entry = it.next();
            _g3mWidget.getG3MContext().getDownloader().requestBuffer(new URL(entry.getKey(), false), 100000000L,
                     new GEOJSONDownloadListener(_marksRenderer, entry.getValue()), true);
         }
      }

      if (!_panoSources.isEmpty()) {
         final Iterator<String> it = _panoSources.iterator();
         while (it.hasNext()) {
            final IStringBuilder url = IStringBuilder.newStringBuilder();
            url.addString(it.next());
            url.addString("/metadata.json");
            _g3mWidget.getG3MContext().getDownloader().requestBuffer(new URL(url.getString(), false), 100000000L,
                     new PanoDownloadListener(_marksRenderer, _panoTouchListener), true);
         }
      }

      if (_customInitializationTask != null) {
         _customInitializationTask.run(context);
      }
   }
}
