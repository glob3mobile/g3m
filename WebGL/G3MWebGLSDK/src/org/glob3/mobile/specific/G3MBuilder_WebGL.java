

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.BasicShadersGL2;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IG3MBuilder;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;

<<<<<<< HEAD
public class G3MBuilder_WebGL extends IG3MBuilder {

	private final G3MWidget_WebGL _nativeWidget;

	public G3MBuilder_WebGL() {
		super();
		_nativeWidget = new G3MWidget_WebGL();
	}
	
	public G3MBuilder_WebGL(G3MWidget_WebGL widget) {
		super();
		_nativeWidget = widget;
	}

	public G3MWidget_WebGL createWidget() {
		if (_nativeWidget.isSupported()) {

			// Default gpu programs
			addGPUProgramSources(new GPUProgramSources("Billboard",
					Shaders_WebGL._billboardVertexShader,
					Shaders_WebGL._billboardFragmentShader));
			addGPUProgramSources(new GPUProgramSources("Default",
					Shaders_WebGL._defaultVertexShader,
					Shaders_WebGL._defaultFragmentShader));

			addGPUProgramSources(new GPUProgramSources("ColorMesh",
					Shaders_WebGL._colorMeshVertexShader,
					Shaders_WebGL._colorMeshFragmentShader));

			addGPUProgramSources(new GPUProgramSources("TexturedMesh",
					Shaders_WebGL._texturedMeshVertexShader,
					Shaders_WebGL._texturedMeshFragmentShader));
			
			addGPUProgramSources(new GPUProgramSources("TransformedTexCoorTexturedMesh", 
					Shaders_WebGL._transformedTexCoortexturedMeshVertexShader, 
					Shaders_WebGL._transformedTexCoortexturedMeshFragmentShader));

			addGPUProgramSources(new GPUProgramSources("FlatColorMesh",
					Shaders_WebGL._flatColorMeshVertexShader,
					Shaders_WebGL._flatColorMeshFragmentShader));
			
			addGPUProgramSources(new GPUProgramSources("NoColorMesh",
					Shaders_WebGL._noColorMeshVertexShader,
					Shaders_WebGL._noColorMeshFragmentShader));
			
			addGPUProgramSources(new GPUProgramSources("TexturedMesh_DirectionLight", 
					Shaders_WebGL._TexturedMesh_DirectionLightVertexShader,
					Shaders_WebGL._TexturedMesh_DirectionLightFragmentShader));
			
			addGPUProgramSources(new GPUProgramSources("FlatColorMesh_DirectionLight", 
					Shaders_WebGL._FlatColorMesh_DirectionLightVertexShader,
					Shaders_WebGL._FlatColorMesh_DirectionLightFragmentShader));
			
			addGPUProgramSources(new GPUProgramSources("ZRender", 
					Shaders_WebGL._zRenderVertexShader,
					Shaders_WebGL._zRenderFragmentShader));
		    

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
=======

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
         /*
          * 
          * // Default gpu programs addGPUProgramSources(new
          * GPUProgramSources("Billboard",
          * Shaders_WebGL._billboardVertexShader,
          * Shaders_WebGL._billboardFragmentShader));
          * addGPUProgramSources(new GPUProgramSources("Default",
          * Shaders_WebGL._defaultVertexShader,
          * Shaders_WebGL._defaultFragmentShader));
          * 
          * addGPUProgramSources(new GPUProgramSources("ColorMesh",
          * Shaders_WebGL._colorMeshVertexShader,
          * Shaders_WebGL._colorMeshFragmentShader));
          * 
          * addGPUProgramSources(new GPUProgramSources("TexturedMesh",
          * Shaders_WebGL._texturedMeshVertexShader,
          * Shaders_WebGL._texturedMeshFragmentShader));
          * 
          * addGPUProgramSources(new
          * GPUProgramSources("TransformedTexCoorTexturedMesh",
          * Shaders_WebGL._transformedTexCoortexturedMeshVertexShader,
          * Shaders_WebGL._transformedTexCoortexturedMeshFragmentShader));
          * 
          * addGPUProgramSources(new GPUProgramSources("FlatColorMesh",
          * Shaders_WebGL._flatColorMeshVertexShader,
          * Shaders_WebGL._flatColorMeshFragmentShader));
          * 
          * addGPUProgramSources(new GPUProgramSources("NoColorMesh",
          * Shaders_WebGL._noColorMeshVertexShader,
          * Shaders_WebGL._noColorMeshFragmentShader));
          * 
          * addGPUProgramSources(new
          * GPUProgramSources("TexturedMesh+DirectionLight",
          * Shaders_WebGL._TexturedMesh_DirectionLightVertexShader,
          * Shaders_WebGL._TexturedMesh_DirectionLightFragmentShader));
          * 
          * addGPUProgramSources(new
          * GPUProgramSources("FlatColorMesh+DirectionLight",
          * Shaders_WebGL._FlatColorMesh_DirectionLightVertexShader,
          * Shaders_WebGL._FlatColorMesh_DirectionLightFragmentShader));
          */
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
>>>>>>> purgatory

}
