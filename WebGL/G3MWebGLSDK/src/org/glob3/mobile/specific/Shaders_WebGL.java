

package org.glob3.mobile.specific;

import com.google.gwt.core.client.JavaScriptObject;


public final class Shaders_WebGL {

   public final static String     _defaultFragmentShader   = "varying mediump vec2 TextureCoordOut;\n"
                                                             + "\n"
                                                             + "varying mediump vec4 VertexColor;\n"
                                                             + "\n"
                                                             + "uniform sampler2D Sampler;\n"
                                                             + "uniform bool EnableTexture;\n"
                                                             + "uniform lowp vec4 FlatColor;\n"
                                                             + "\n"
                                                             + "uniform bool EnableColorPerVertex;\n"
                                                             + "uniform bool EnableFlatColor;\n"
                                                             + "uniform mediump float FlatColorIntensity;\n"
                                                             + "uniform mediump float ColorPerVertexIntensity;\n"
                                                             + "\n"
                                                             + "void main() {\n"
                                                             + "  \n"
                                                             + "  if (EnableTexture) {\n"
                                                             + "    gl_FragColor = texture2D(Sampler, TextureCoordOut);\n"
                                                             + "    \n"
                                                             + "    if (EnableFlatColor || EnableColorPerVertex){\n"
                                                             + "      lowp vec4 color;\n"
                                                             + "      if (EnableFlatColor) {\n"
                                                             + "        color = FlatColor;\n"
                                                             + "        if (EnableColorPerVertex) {\n"
                                                             + "          color = color * VertexColor;\n"
                                                             + "        }\n"
                                                             + "      }\n"
                                                             + "      else {\n"
                                                             + "        color = VertexColor;\n"
                                                             + "      }\n"
                                                             + "      \n"
                                                             + "      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\n"
                                                             + "      gl_FragColor = mix(gl_FragColor,\n"
                                                             + "                         VertexColor,\n"
                                                             + "                         intensity);\n" + "    }\n" + "  }\n"
                                                             + "  else {\n" + "    \n" + "    if (EnableColorPerVertex) {\n"
                                                             + "      gl_FragColor = VertexColor;\n"
                                                             + "      if (EnableFlatColor) {\n"
                                                             + "        gl_FragColor = gl_FragColor * FlatColor;\n" + "      }\n"
                                                             + "    }\n" + "    else {\n" + "      gl_FragColor = FlatColor;\n"
                                                             + "    }\n" + "    \n" + "  }\n" + "  \n" + "}";

   public final static String     _defaultVertexShader     = "attribute vec4 Position;\n"
                                                             + "attribute vec2 TextureCoord;\n"
                                                             + "attribute vec4 Color;\n"
                                                             + "\n"
                                                             + "uniform mediump vec2 TranslationTexCoord;\n"
                                                             + "uniform mediump vec2 ScaleTexCoord;\n"
                                                             + "\n"
                                                             + "uniform mat4 Projection;\n"
                                                             + "uniform mat4 Modelview;\n"
                                                             + "\n"
                                                             + "uniform float PointSize;\n"
                                                             + "\n"
                                                             + "varying vec4 VertexColor;\n"
                                                             + "varying vec2 TextureCoordOut;\n"
                                                             + "\n"
                                                             + "\n"
                                                             + "void main() {\n"
                                                             + "  gl_Position = Projection * Modelview * Position;\n"
                                                             + "  \n"
                                                             + "  TextureCoordOut = (TextureCoord * ScaleTexCoord) + TranslationTexCoord;\n"
                                                             + "  \n" + "  VertexColor = Color;\n" + "  \n"
                                                             + "  gl_PointSize = PointSize;\n" + "}";

	public final static String _billboardFragmentShader = "varying mediump vec2 TextureCoordOut;\n" + 
			"uniform sampler2D Sampler;\n" + 
			"\n" + 
			"void main() {\n" + 
			"  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" + 
			"}";

	public final static String _billboardVertexShader = "attribute vec2 aTextureCoord;\n" + 
			"\n" + 
			"uniform mat4 uModelview;\n" + 
			"\n" + 
			"uniform vec4 uBillboardPosition;\n" + 
			"\n" + 
			"uniform vec2 uTextureExtent;\n" + 
			"uniform vec2 uViewPortExtent;\n" + 
			"\n" + 
			"varying vec2 TextureCoordOut;\n" + 
			"\n" + 
			"void main() {\n" + 
			"  gl_Position = uModelview * uBillboardPosition;\n" + 
			"  \n" + 
			"  gl_Position.x += ((aTextureCoord.x - 0.5) * 2.0 * uTextureExtent.x / uViewPortExtent.x) * gl_Position.w;\n" + 
			"  gl_Position.y -= ((aTextureCoord.y - 0.5) * 2.0 * uTextureExtent.y / uViewPortExtent.y) * gl_Position.w;\n" + 
			"  \n" + 
			"  TextureCoordOut = aTextureCoord;\n" + 
			"}";

	public final static String _colorMeshFragmentShader = "varying mediump vec4 VertexColor;\n"
			+ "\n"
			+ "void main() {\n"
			+ "  gl_FragColor = VertexColor;\n"
			+ "}";

	public final static String _colorMeshVertexShader = "attribute vec4 aPosition;\n"
			+ "attribute vec4 aColor;\n"
			+ "\n"
			+ "uniform mat4 uModelview;\n"
			+ "uniform float uPointSize;\n"
			+ "\n"
			+ "varying vec4 VertexColor;\n"
			+ "\n"
			+ "void main() {\n"
			+ "  gl_Position = uModelview * aPosition;\n"
			+ "  VertexColor = aColor;\n"
			+ "  gl_PointSize = uPointSize;\n"
			+ "}";
	
	public final static String _noColorMeshFragmentShader = "void main() {\n" + 
			"  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0); //RED\n" + 
			"}";

	public final static String _noColorMeshVertexShader = "attribute vec4 aPosition;\n" + 
			"\n" + 
			"uniform mat4 uModelview;\n" + 
			"uniform float uPointSize;\n" + 
			"\n" + 
			"void main() {\n" + 
			"  gl_Position = uModelview * aPosition;\n" + 
			"  gl_PointSize = uPointSize;\n" + 
			"}";

	public final static String _texturedMeshFragmentShader = "varying mediump vec2 TextureCoordOut;\n" + 
			"varying mediump vec4 VertexColor;\n" + 
			"\n" + 
			"uniform sampler2D Sampler;\n" + 
			"\n" + 
			"void main() {\n" + 
			"  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" + 
			"}";
	public final static String _texturedMeshVertexShader = "attribute vec4 aPosition;\n" + 
			"attribute vec2 aTextureCoord;\n" + 
			"\n" + 
			"uniform mat4 uModelview;\n" + 
			"uniform float uPointSize;\n" + 
			"\n" + 
			"varying vec4 VertexColor;\n" + 
			"varying vec2 TextureCoordOut;\n" + 
			"\n" + 
			"void main() {\n" + 
			"  gl_Position = uModelview * aPosition;\n" + 
			"  \n" + 
			"  TextureCoordOut = aTextureCoord;\n" + 
			"  \n" + 
			"  gl_PointSize = uPointSize;\n" + 
			"}\n" + 
			"";
	
	public final static String _transformedTexCoortexturedMeshFragmentShader = "varying mediump vec2 TextureCoordOut;\n" + 
			"varying mediump vec4 VertexColor;\n" + 
			"\n" + 
			"uniform sampler2D Sampler;\n" + 
			"\n" + 
			"void main() {\n" + 
			"  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" + 
			"}";
	
	public final static String _transformedTexCoortexturedMeshVertexShader = "attribute vec4 aPosition;\n" + 
			"attribute vec2 aTextureCoord;\n" + 
			"\n" + 
			"uniform mediump vec2 uTranslationTexCoord;\n" + 
			"uniform mediump vec2 uScaleTexCoord;\n" + 
			"uniform mat4 uModelview;\n" + 
			"uniform float uPointSize;\n" + 
			"\n" + 
			"varying vec4 VertexColor;\n" + 
			"varying vec2 TextureCoordOut;\n" + 
			"\n" + 
			"void main() {\n" + 
			"  gl_Position = uModelview * aPosition;\n" + 
			"  \n" + 
			"  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" + 
			"  \n" + 
			"  gl_PointSize = uPointSize;\n" + 
			"}";
	

	public final static String _flatColorMeshFragmentShader = "uniform lowp vec4 uFlatColor;\n"
			+ "\n" + "void main() {\n" + "  gl_FragColor = uFlatColor;\n" + "}";
	public final static String _flatColorMeshVertexShader = "attribute vec4 aPosition;\n" + 
			"uniform mat4 uModelview;\n" + 
			"uniform float uPointSize;\n" + 
			"\n" + 
			"void main() {\n" + 
			"  gl_Position = uModelview * aPosition;\n" + 
			"  gl_PointSize = uPointSize;\n" + 
			"}";


   ////////////////////////////////////////////////////////////////////////////////////////


   private final static String    _fragmentShader          = "varying mediump vec2 TextureCoordOut;"
                                                             + "uniform mediump vec2 TranslationTexCoord;"
                                                             + "uniform mediump vec2 ScaleTexCoord;"
                                                             + ""
                                                             + "varying mediump vec4 VertexColor;"
                                                             + ""
                                                             + "uniform sampler2D Sampler;"
                                                             + "uniform bool EnableTexture;"
                                                             + "uniform lowp vec4 FlatColor;"
                                                             + ""
                                                             + "uniform bool EnableColorPerVertex;"
                                                             + "uniform bool EnableFlatColor;"
                                                             + "uniform mediump float FlatColorIntensity;"
                                                             + "uniform mediump float ColorPerVertexIntensity;"
                                                             + ""
                                                             + "void main() {"
                                                             + "  "
                                                             + "  if (EnableTexture) {"
                                                             + "    gl_FragColor = texture2D(Sampler, TextureCoordOut * ScaleTexCoord + TranslationTexCoord);"
                                                             + ""
                                                             + "    if (EnableFlatColor || EnableColorPerVertex){"
                                                             + "      lowp vec4 color;"
                                                             + "      if (EnableFlatColor) {"
                                                             + "        color = FlatColor;"
                                                             + "        if (EnableColorPerVertex) {"
                                                             + "          color = color * VertexColor;"
                                                             + "        }"
                                                             + "      }"
                                                             + "      else {"
                                                             + "        color = VertexColor;"
                                                             + "      }"
                                                             + "      "
                                                             + "      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;"
                                                             + "      gl_FragColor = mix(gl_FragColor,"
                                                             + "                         VertexColor,"
                                                             + "                         intensity);" + "    }" + "  }"
                                                             + "  else {" + "    " + "    if (EnableColorPerVertex) {"
                                                             + "      gl_FragColor = VertexColor;"
                                                             + "      if (EnableFlatColor) {"
                                                             + "        gl_FragColor = gl_FragColor * FlatColor;" + "      }"
                                                             + "    }" + "    else {" + "      gl_FragColor = FlatColor;"
                                                             + "    }" + "    " + "  }" + "  " + "}";

   private final static String    _vertexShader            = "attribute vec4 Color;"
                                                             + "attribute vec4 Position;"
                                                             + "attribute vec2 TextureCoord; "
                                                             + "uniform mat4 Projection;"
                                                             + "uniform mat4 Modelview;"
                                                             + "uniform bool BillBoard;"
                                                             + "uniform float ViewPortRatio;"
                                                             + "uniform float PointSize;"
                                                             + "varying vec4 VertexColor;"
                                                             + "varying vec2 TextureCoordOut;"
                                                             + "void main() {"
                                                             + "  gl_Position = Projection * Modelview * Position;"
                                                             + "  if (BillBoard) {"
                                                             + "    gl_Position.x += (-0.05 + TextureCoord.x * 0.1) * gl_Position.w;"
                                                             + "    gl_Position.y -= (-0.05 + TextureCoord.y * 0.1) * gl_Position.w * ViewPortRatio;"
                                                             + "  }" + "  TextureCoordOut = TextureCoord;"
                                                             + "  VertexColor = Color;" + "  gl_PointSize = PointSize;" + "}";

   private final JavaScriptObject _gl;


   public Shaders_WebGL(final JavaScriptObject webGLContext) {
      _gl = webGLContext;
   }


   //   public IGLProgramId createProgram() {
   //
   //      final JavaScriptObject vertexS = jsCompileShader(_gl, _vertexShader, "x-shader/x-vertex");
   //      final JavaScriptObject fragmentS = jsCompileShader(_gl, _fragmentShader, "x-shader/x-fragment");
   //
   //      if ((vertexS == null) || (fragmentS == null)) {
   //         throw new RuntimeException("FAILURE CREATING SHADERS");
   //      }
   //
   //      final JavaScriptObject program = jsCreateNewProgram(_gl, vertexS, fragmentS);
   //      if (program == null) {
   //         throw new RuntimeException("FAILURE CREATING PROGRAM");
   //      }
   //
   //      return new GLProgramId_WebGL(program);
   //   }


   // Shader compiler function
   public static native JavaScriptObject jsCompileShader(JavaScriptObject gl,
                                                         String shaderContent,
                                                         String shaderType) /*-{
		var shader;
		if (shaderType == "x-shader/x-fragment") {
			shader = gl.createShader(gl.FRAGMENT_SHADER);
		} else if (shaderType == "x-shader/x-vertex") {
			shader = gl.createShader(gl.VERTEX_SHADER);
		} else {
			return null;
		}

		gl.shaderSource(shader, shaderContent);
		gl.compileShader(shader);

		if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
			alert("Error: " + gl.getShaderInfoLog(shader));
			return null;
		}

		return shader;
   }-*/;


   // Program and shader compiling, uniform variables, attributes and buffers
   // creation
   public static native JavaScriptObject jsCreateNewProgram(JavaScriptObject gl,
                                                            JavaScriptObject vertexShader,
                                                            JavaScriptObject fragmentShader) /*-{
		var newProgram = gl.createProgram();

		gl.attachShader(newProgram, vertexShader);
		gl.attachShader(newProgram, fragmentShader);
		gl.linkProgram(newProgram);

		if (!gl.getProgramParameter(newProgram, gl.LINK_STATUS))
			return null;

		return newProgram;
   }-*/;

}
