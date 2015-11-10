

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.BasicShadersGL2;
import org.glob3.mobile.generated.GPUProgramFactory;
import org.glob3.mobile.generated.GPUProgramManager;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.MapBooOLDApplicationChangeListener;
import org.glob3.mobile.generated.MapBooOLDBuilder;
import org.glob3.mobile.generated.MapBooOLD_ViewType;
import org.glob3.mobile.generated.URL;


public class MapBooOLDBuilder_WebGL
   extends
      MapBooOLDBuilder {


   private final G3MWidget_WebGL _nativeWidget;


   public MapBooOLDBuilder_WebGL(final URL serverURL,
                              final URL tubesURL,
                              final String sceneId,
                              final MapBooOLD_ViewType viewType,
                              final MapBooOLDApplicationChangeListener applicationListener,
                              final boolean enableNotifications,
                              final String token) {
      super(serverURL, tubesURL, sceneId, viewType, applicationListener, enableNotifications, token);
      _nativeWidget = new G3MWidget_WebGL();
   }


   public G3MWidget_WebGL createWidget() {
      if (_nativeWidget.isWebGLSupported()) {

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


   @Override
   protected GPUProgramManager createGPUProgramManager() {
      final GPUProgramFactory gpuProgramFactory = new BasicShadersGL2();
      return new GPUProgramManager(gpuProgramFactory);
   }


}
