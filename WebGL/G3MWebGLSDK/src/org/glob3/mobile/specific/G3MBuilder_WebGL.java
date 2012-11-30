

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IG3MBuilder;
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


   public G3MWidget_WebGL createWidget() {
      if (_nativeWidget.isSupported()) {
         final NativeGL_WebGL nativeGL = new NativeGL_WebGL(_nativeWidget.getWebGLContext());
         setNativeGL(nativeGL);

         _nativeWidget.setG3MWidget(create());
         _nativeWidget.startWidget();
      }

      return _nativeWidget;
   }


   @Override
   protected IThreadUtils createThreadUtils() {
      final int delayMillis = 10;
      final IThreadUtils threadUtils = new ThreadUtils_WebGL(delayMillis);

      return threadUtils;
   }


   @Override
   protected IStorage createStorage() {
      // TODO To be implemented when Storage_WebGL is implemented.
      return null;
   }


   @Override
   protected IDownloader createDownloader() {
      final int delayMillis = 10;
      final IDownloader downloader = new Downloader_WebGL(8, delayMillis, "");

      return downloader;
   }

}
