

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.GPUProgramFactory;
import org.glob3.mobile.generated.GPUProgramManager;
import org.glob3.mobile.generated.GPUProgramSources;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.MapBooApplicationChangeListener;
import org.glob3.mobile.generated.MapBooBuilder;
import org.glob3.mobile.generated.MapBoo_ViewType;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

import android.content.Context;


public class MapBooBuilder_Android
         extends
            MapBooBuilder {

   private final G3MWidget_Android _nativeWidget;


   @Override
   protected IStorage createStorage() {
      return new SQLiteStorage_Android("g3m.cache", _nativeWidget.getContext());
   }


   @Override
   protected IDownloader createDownloader() {
      final TimeInterval connectTimeout = TimeInterval.fromSeconds(10);
      final TimeInterval readTimeout = TimeInterval.fromSeconds(15);
      final boolean saveInBackground = true;
      return new CachedDownloader( //
               new Downloader_Android(4, connectTimeout, readTimeout, _nativeWidget.getContext()), //
               getStorage(), //
               saveInBackground);
   }


   public MapBooBuilder_Android(final Context context,
                                final URL serverURL,
                                final URL tubesURL,
                                final String sceneId,
                                final MapBoo_ViewType viewType,
                                final MapBooApplicationChangeListener applicationListener,
                                final boolean enableNotifications) {
      super(serverURL, tubesURL, sceneId, viewType, applicationListener, enableNotifications);

      _nativeWidget = new G3MWidget_Android(context);
   }


   @Override
   protected IThreadUtils createThreadUtils() {
      return new ThreadUtils_Android(_nativeWidget);
   }


   public G3MWidget_Android createWidget() {
      setGL(_nativeWidget.getGL());

      _nativeWidget.setWidget(create());

      return _nativeWidget;
   }


   @Override
   protected GPUProgramManager createGPUProgramManager() {
      final GPUProgramFactory gpuProgramFactory = new GPUProgramFactory();

      gpuProgramFactory.add(new GPUProgramSources("Billboard", GL2Shaders._billboardVertexShader,
               GL2Shaders._billboardFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("Default", GL2Shaders._defaultVertexShader, GL2Shaders._defaultFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("ColorMesh", GL2Shaders._colorMeshVertexShader,
               GL2Shaders._colorMeshFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("TexturedMesh", GL2Shaders._texturedMeshVertexShader,
               GL2Shaders._texturedMeshFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("TransformedTexCoorTexturedMesh",
               GL2Shaders._transformedTexCoortexturedMeshVertexShader, GL2Shaders._transformedTexCoortexturedMeshFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("FlatColorMesh", GL2Shaders._flatColorMeshVertexShader,
               GL2Shaders._flatColorMeshFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("NoColorMesh", GL2Shaders._noColorMeshVertexShader,
               GL2Shaders._noColorMeshFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("TexturedMesh+DirectionLight",
               GL2Shaders._TexturedMesh_DirectionLightVertexShader, GL2Shaders._TexturedMesh_DirectionLightFragmentShader));

      gpuProgramFactory.add(new GPUProgramSources("FlatColorMesh+DirectionLight",
               GL2Shaders._FlatColorMesh_DirectionLightVertexShader, GL2Shaders._FlatColorMesh_DirectionLightFragmentShader));
      
      
      gpuProgramFactory.add(new GPUProgramSources("ZRender", GL2Shaders._zRenderVertexShader,
              GL2Shaders._zRenderFragmentShader));



      return new GPUProgramManager(gpuProgramFactory);
   }

}
