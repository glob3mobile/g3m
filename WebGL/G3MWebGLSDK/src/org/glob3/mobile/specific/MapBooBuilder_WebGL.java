

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.GPUProgramFactory;
import org.glob3.mobile.generated.GPUProgramManager;
import org.glob3.mobile.generated.GPUProgramSources;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.MapBooApplicationChangeListener;
import org.glob3.mobile.generated.MapBooBuilder;
import org.glob3.mobile.generated.URL;


public class MapBooBuilder_WebGL
         extends
            MapBooBuilder {


   private final G3MWidget_WebGL _nativeWidget;


   public MapBooBuilder_WebGL(final URL serverURL,
                              final URL tubesURL,
                              final boolean useWebSockets,
                              final String sceneId,
                              final MapBooApplicationChangeListener applicationListener) {
      super(serverURL, tubesURL, useWebSockets, sceneId, applicationListener);
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


   @Override
   protected GPUProgramManager createGPUProgramManager() {
      final GPUProgramFactory gpuProgramFactory = new GPUProgramFactory();

      gpuProgramFactory.add(new GPUProgramSources("Billboard", Shaders_WebGL._billboardVertexShader,
               Shaders_WebGL._billboardFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("Default", Shaders_WebGL._defaultVertexShader,
               Shaders_WebGL._defaultFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("ColorMesh", Shaders_WebGL._colorMeshVertexShader,
               Shaders_WebGL._colorMeshFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("TexturedMesh", Shaders_WebGL._texturedMeshVertexShader,
               Shaders_WebGL._texturedMeshFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("TransformedTexCoorTexturedMesh",
               Shaders_WebGL._transformedTexCoortexturedMeshVertexShader,
               Shaders_WebGL._transformedTexCoortexturedMeshFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("FlatColorMesh", Shaders_WebGL._flatColorMeshVertexShader,
               Shaders_WebGL._flatColorMeshFragmentShader));

      return new GPUProgramManager(gpuProgramFactory);
   }


}
