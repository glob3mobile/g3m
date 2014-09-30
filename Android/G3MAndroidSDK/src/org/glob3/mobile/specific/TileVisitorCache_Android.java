

package org.glob3.mobile.specific;


import java.util.ArrayList;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.ITileVisitor;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.Tile;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;


public class TileVisitorCache_Android
implements
ITileVisitor {

   private long             _debugCounter = 0;
   private final G3MContext _context;


   public TileVisitorCache_Android(final G3MContext context) {
      _context = context;
   }


   @Override
   public void dispose() {

   }


   public long getDebugCounter() {
      return _debugCounter;
   }


   @Override
   public final void visitTile(final ArrayList<Layer> layers,
                               final Tile tile) {

      final TimeInterval timeToCache = TimeInterval.fromDays(30);

      for (final Layer layer : layers) {
         for (final URL url : layer.getDownloadURLs(tile)) {
            _context.getLogger().logInfo(url._path);
            _context.getDownloader().requestImage(url, 1, timeToCache, true, new IImageDownloadListenerTileCache(_debugCounter),
                     true);
            _debugCounter++;
         }
      }
   }


}
