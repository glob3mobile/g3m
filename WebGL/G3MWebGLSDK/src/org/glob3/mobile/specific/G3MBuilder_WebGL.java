
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;

public class G3MBuilder_WebGL extends IG3MBuilder {

   private final G3MWidget_WebGL _nativeWidget;

   public G3MBuilder_WebGL() {
      this(new G3MWidget_WebGL());
   }

   public G3MBuilder_WebGL(final G3MWidget_WebGL widget) {
      _nativeWidget = widget;
   }

   private void addGPUProgramSources() {
      final BasicShadersGL2 shaders = new BasicShadersGL2();
      final int             size    = shaders.size();
      for (int i = 0; i < size; i++) {
         addGPUProgramSources(shaders.get(i));
      }
   }

   public G3MWidget_WebGL createWidget() {
      if (_nativeWidget.isWebGLSupported()) {
         addGPUProgramSources();

         setGL(_nativeWidget.getGL());

         _nativeWidget.setG3MWidget(create());
         _nativeWidget.startWidget();
      }

      return _nativeWidget;
   }

   @Override
   protected IDownloader createDefaultDownloader() {
      final int     maxConcurrentOperationCount = 8;
      final int     delayMillis                 = 10;
      final String  proxy                       = null;
      final boolean verboseErrors               = true;
      return new Downloader_WebGL(maxConcurrentOperationCount, delayMillis, proxy, verboseErrors);
   }

   @Override
   protected IThreadUtils createDefaultThreadUtils() {
      final int delayMillis = 10;
      return new ThreadUtils_WebGL(delayMillis);
   }

   @Override
   protected IStorage createDefaultStorage() {
      // TODO To be implemented when Storage_WebGL is implemented.
      return null;
   }

}
