package org.glob3.mobile.specific;

import org.glob3.mobile.generated.GPUProgramSources;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IG3MBuilder;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;

public class G3MBuilder_WebGL extends IG3MBuilder {

	private final G3MWidget_WebGL _nativeWidget;

	public G3MBuilder_WebGL() {
		super();

		_nativeWidget = new G3MWidget_WebGL();
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

}
