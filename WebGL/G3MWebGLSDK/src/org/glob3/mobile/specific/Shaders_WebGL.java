

package org.glob3.mobile.specific;

import com.google.gwt.core.client.JavaScriptObject;


public final class Shaders_WebGL {


   private final static String    _fragmentShader = "varying mediump vec2 TextureCoordOut;"
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
                                                    + "                         intensity);" + "    }" + "  }" + "  else {"
                                                    + "    " + "    if (EnableColorPerVertex) {"
                                                    + "      gl_FragColor = VertexColor;" + "      if (EnableFlatColor) {"
                                                    + "        gl_FragColor = gl_FragColor * FlatColor;" + "      }" + "    }"
                                                    + "    else {" + "      gl_FragColor = FlatColor;" + "    }" + "    " + "  }"
                                                    + "  " + "}";

   private final static String    _vertexShader   = "attribute vec4 Position;"
                                                    + "attribute vec2 TextureCoord; "
                                                    + "attribute vec4 Color;"
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
                                                    + "  }" + "  TextureCoordOut = TextureCoord;" + "  VertexColor = Color;"
                                                    + "  gl_PointSize = PointSize;" + "}";

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
