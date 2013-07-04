

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.G3MCBuilder;
import org.glob3.mobile.generated.G3MCSceneChangeListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.URL;


public class G3MCBuilder_WebGL
         extends
            G3MCBuilder {


   private final G3MWidget_WebGL _nativeWidget;


   public G3MCBuilder_WebGL(final URL serverURL,
                            final URL tubesURL,
                            final boolean useWebSockets,
                            final String sceneId,
                            final G3MCSceneChangeListener sceneListener) {
      super(serverURL, tubesURL, useWebSockets, sceneId, sceneListener);
      _nativeWidget = new G3MWidget_WebGL();
   }


   public G3MWidget_WebGL createWidget() {
      if (_nativeWidget.isSupported()) {

         setGL(_nativeWidget.getGL());

         _nativeWidget.setG3MWidget(create());
         _nativeWidget.startWidget();
      }

      return _nativeWidget;
   }


   @Override
   protected IStorage createStorage() {
      return null;
   }


   @Override
   protected IDownloader createDownloader() {
      final int delayMillis = 10;
      return new Downloader_WebGL(8, delayMillis, "");
   }


   @Override
   protected IThreadUtils createThreadUtils() {
      final int delayMillis = 10;
      return new ThreadUtils_WebGL(delayMillis);
   }


}
