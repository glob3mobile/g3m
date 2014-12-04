

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.BasicShadersGL2;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraMouseWheelHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IG3MBuilder;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;


public class G3MBuilder_WebGL
         extends
            IG3MBuilder {

   private final G3MWidget_WebGL _nativeWidget;


   public G3MBuilder_WebGL() {
      super();
      _nativeWidget = new G3MWidget_WebGL();
   }


   public G3MBuilder_WebGL(final G3MWidget_WebGL widget) {
      super();
      _nativeWidget = widget;
   }


   public G3MWidget_WebGL createWidget() {
      if (_nativeWidget.isSupported()) {

         final BasicShadersGL2 shaders = new BasicShadersGL2();
         for (int i = 0; i < shaders.size(); i++) {
            addGPUProgramSources(shaders.get(i));
         }

         setGL(_nativeWidget.getGL());
         

         _nativeWidget.setG3MWidget(create());
         _nativeWidget.startWidget();
      }

      return _nativeWidget;
   }


   @Override
   protected IThreadUtils createDefaultThreadUtils() {
      final int delayMillis = 10;
      final IThreadUtils threadUtils = new ThreadUtils_WebGL(delayMillis);

      return threadUtils;
   }


   @Override
   protected IStorage createDefaultStorage() {
      // TODO To be implemented when Storage_WebGL is implemented.
      return null;
   }


   @Override
   protected IDownloader createDefaultDownloader() {
      final int delayMillis = 10;
      final IDownloader downloader = new Downloader_WebGL(8, delayMillis, "");

      return downloader;
   }
   
   @Override
   protected CameraRenderer createDefaultCameraRenderer()
   {
	   ILogger.instance().logInfo("Creating Camera Renderer for WebGL handling");
     CameraRenderer cameraRenderer = new CameraRenderer();
     final boolean useInertia = true;
     cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
     final boolean allowRotation = true;
     cameraRenderer.addHandler(new CameraDoubleDragHandler(allowRotation));
     cameraRenderer.addHandler(new CameraRotationHandler());
     cameraRenderer.addHandler(new CameraDoubleTapHandler());
     cameraRenderer.addHandler(new CameraMouseWheelHandler());
   
     return cameraRenderer;
   }

}
