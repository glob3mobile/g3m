

package org.glob3.mobile.specific;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GEOJSONDownloadListener;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.IG3MJSONBuilder;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.PanoDownloadListener;
import org.glob3.mobile.generated.SceneParser;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

import android.content.Context;


public class G3MJSONBuilder_Android
         extends
            IG3MJSONBuilder {

   private final Context _context;


   public G3MJSONBuilder_Android(final String jsonSource,
                                 final Context context) {
      super(jsonSource);
      _context = context;
   }


   public G3MWidget_Android create(final LayerSet layerSet,
                                   final MarksRenderer marksRenderer,
                                   final GInitializationTask initializationTask,
                                   final MarkTouchListener markTouchListener,
                                   final MarkTouchListener panoTouchListener) {

      final G3MBuilder_Android builder = new G3MBuilder_Android(_context);

      SceneParser.instance().parse(layerSet, _jsonSource);
      builder.setLayerSet(layerSet);

      if (markTouchListener != null) {
         marksRenderer.setMarkTouchListener(markTouchListener, true);
      }

      builder.setInitializationTask(new G3MJSONBuilderInitializationTask(initializationTask, marksRenderer, panoTouchListener));
      builder.addRenderer(marksRenderer);
      return builder.createWidget();
   }
}


class G3MJSONBuilderInitializationTask
         extends
            GInitializationTask {

   private final GInitializationTask        _customInitializationTask;
   private final MarksRenderer              _marksRenderer;
   HashMap<String, HashMap<String, String>> _mapGeoJSONSources;
   private final ArrayList<String>          _panoSources;
   private final MarkTouchListener          _panoTouchListener;


   public G3MJSONBuilderInitializationTask(final GInitializationTask customInitializationTask,
                                           final MarksRenderer marksRenderer,
                                           final MarkTouchListener panoTouchListener) {
      _customInitializationTask = customInitializationTask;
      _marksRenderer = marksRenderer;
      _mapGeoJSONSources = SceneParser.instance().getMapGeoJSONSources();
      _panoSources = SceneParser.instance().getPanoSources();
      _panoTouchListener = panoTouchListener;
   }


   @Override
   public void run(final G3MContext context) {
      if (!_mapGeoJSONSources.isEmpty()) {
         final Iterator<Entry<String, HashMap<String, String>>> it = _mapGeoJSONSources.entrySet().iterator();
         while (it.hasNext()) {
            final Entry<String, HashMap<String, String>> entry = it.next();
            context.getDownloader().requestBuffer(
                     new URL(entry.getKey(), false),
                     100000000L,
                     TimeInterval.fromDays(30),
                     new GEOJSONDownloadListener(_marksRenderer, entry.getValue().get("urlIcon"), Double.valueOf(
                              entry.getValue().get("minDistance")).doubleValue()), true);
         }
      }

      if (!_panoSources.isEmpty()) {
         final Iterator<String> it = _panoSources.iterator();
         while (it.hasNext()) {
            final IStringBuilder url = IStringBuilder.newStringBuilder();
            url.addString(it.next());
            url.addString("/metadata.json");
            context.getDownloader().requestBuffer(new URL(url.getString(), false), 100000000L, TimeInterval.fromDays(30),
                     new PanoDownloadListener(_marksRenderer, _panoTouchListener), true);
         }
      }

      if (_customInitializationTask != null) {
         _customInitializationTask.run(context);
      }
   }


   @Override
   public boolean isDone(final G3MContext context) {
      return true;
   }
}
