package org.glob3.mobile.specific;

import android.opengl.GLES20;
import android.util.Log;

public final class GL2Shaders {

	public final static String _defaultFragmentShader = "varying mediump vec2 TextureCoordOut;\n"
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
			+ "                         intensity);\n"
			+ "    }\n"
			+ "  }\n"
			+ "  else {\n"
			+ "    \n"
			+ "    if (EnableColorPerVertex) {\n"
			+ "      gl_FragColor = VertexColor;\n"
			+ "      if (EnableFlatColor) {\n"
			+ "        gl_FragColor = gl_FragColor * FlatColor;\n"
			+ "      }\n"
			+ "    }\n"
			+ "    else {\n"
			+ "      gl_FragColor = FlatColor;\n"
			+ "    }\n"
			+ "    \n"
			+ "  }\n" + "  \n" + "}";

	public final static String _defaultVertexShader = "attribute vec4 Position;\n"
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
			+ "  \n"
			+ "  VertexColor = Color;\n"
			+ "  \n"
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
	
	public final static String _TexturedMesh_DirectionLightFragmentShader = "precision highp float;\n" + 
			"\n" + 
			"varying mediump vec2 TextureCoordOut;\n" + 
			"varying mediump vec4 VertexColor;\n" + 
			"\n" + 
			"uniform sampler2D Sampler;\n" + 
			"uniform float uAmbientLight;\n" + 
			"\n" + 
			"uniform vec4 uLightColor;\n" + 
			"\n" + 
			"varying float diffuseLightIntensity;\n" + 
			"\n" + 
			"\n" + 
			"void main() {\n" + 
			"  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" + 
			"  \n" + 
			"  vec4 lightColor = vec4(uAmbientLight,uAmbientLight,uAmbientLight,1.0) + uLightColor * diffuseLightIntensity;\n" + 
			"  gl_FragColor *= lightColor;\n" + 
			"}";

	public final static String _TexturedMesh_DirectionLightVertexShader = "attribute vec4 aPosition;\n" + 
			"attribute vec2 aTextureCoord;\n" + 
			"attribute vec3 aNormal;\n" + 
			"\n" + 
			"uniform mat4 uModelview;\n" + 
			"uniform mat4 uModel;\n" + 
			"\n" + 
			"uniform float uPointSize;\n" + 
			"\n" + 
			"varying vec4 VertexColor;\n" + 
			"varying vec2 TextureCoordOut;\n" + 
			"\n" + 
			"uniform vec3 uLightDirection; //MUST BE NORMALIZED\n" + 
			"varying float diffuseLightIntensity;\n" + 
			"\n" + 
			"\n" + 
			"void main() {\n" + 
			"\n" + 
			"  vec3 normal = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" + 
			"  vec3 lightDir = normalize( uLightDirection );\n" + 
			"  \n" + 
			"  diffuseLightIntensity = max(dot(normal, lightDir), 0.0);\n" + 
			"\n" + 
			"  gl_Position = uModelview * aPosition;\n" + 
			"  \n" + 
			"  TextureCoordOut = aTextureCoord;\n" + 
			"\n" + 
			"  gl_PointSize = uPointSize;\n" + 
			"}";

	// //////////////////////////////////////////////////////////////////////////////////////

	private final static String _fragmentShader = "varying mediump vec2 TextureCoordOut;"
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
			+ "                         intensity);"
			+ "    }"
			+ "  }"
			+ "  else {"
			+ "    "
			+ "    if (EnableColorPerVertex) {"
			+ "      gl_FragColor = VertexColor;"
			+ "      if (EnableFlatColor) {"
			+ "        gl_FragColor = gl_FragColor * FlatColor;"
			+ "      }"
			+ "    }"
			+ "    else {"
			+ "      gl_FragColor = FlatColor;"
			+ "    }" + "    " + "  }" + "  " + "}";

	private final static String _vertexShader = "attribute vec4 Position;"
			+ "attribute vec2 TextureCoord;"
			+ "attribute vec4 Color;"
			+ "uniform mat4 Projection;"
			+ "uniform mat4 Modelview;"
			+ "uniform bool BillBoard;"
			+ "uniform vec2 TextureExtent;"
			+ "uniform vec2 ViewPortExtent;"
			+ "uniform float PointSize;"
			+ "varying vec4 VertexColor;"
			+ "varying vec2 TextureCoordOut;"
			+ "void main() {"
			+ "  gl_Position = Projection * Modelview * Position;"
			+ "  if (BillBoard) {"
			+ "    gl_Position.x += ((TextureCoord.x - 0.5) * 2.0 * TextureExtent.x / ViewPortExtent.x) * gl_Position.w;"
			+ "    gl_Position.y -= ((TextureCoord.y - 0.5) * 2.0 * TextureExtent.y / ViewPortExtent.y) * gl_Position.w;"
			+ "  }" + "  TextureCoordOut = TextureCoord;"
			+ "  VertexColor = Color;" + "  gl_PointSize = PointSize;" + "}";

	public static String getFragmentShader() {
		return _fragmentShader;
	}

	public static String getVertexShader() {
		return _vertexShader;
	}

	public static int loadShader(final int shaderType, final String source) {
		int shader = GLES20.glCreateShader(shaderType);
		if (shader != 0) {
			GLES20.glShaderSource(shader, source);
			GLES20.glCompileShader(shader);
			final int[] compiled = new int[1];
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {
				Log.e("GL2Shaders", "Could not compile shader " + shaderType
						+ ":");
				Log.e("GL2Shaders", GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}
		} else {
			final int error = GLES20.glGetError();
			Log.d("GL", "ERROR CREATING SHADER " + error);

		}
		return shader;
	}

	public static int createProgram(final String vertexSource,
			final String fragmentSource) {
		final int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
				vertexSource);
		if (vertexShader == 0) {
			return 0;
		}

		final int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentSource);
		if (pixelShader == 0) {
			return 0;
		}

		int program = GLES20.glCreateProgram();
		if (program != 0) {
			GLES20.glAttachShader(program, vertexShader);
			checkGlError("glAttachShader");
			GLES20.glAttachShader(program, pixelShader);
			checkGlError("glAttachShader");
			GLES20.glLinkProgram(program);
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if (linkStatus[0] != GLES20.GL_TRUE) {
				Log.e("GL2Shaders", "Could not link program: ");
				Log.e("GL2Shaders", GLES20.glGetProgramInfoLog(program));
				GLES20.glDeleteProgram(program);
				program = 0;
			}
		}
		return program;
	}

	public static void checkGlError(final String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e("GL2Shaders", op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}

}
