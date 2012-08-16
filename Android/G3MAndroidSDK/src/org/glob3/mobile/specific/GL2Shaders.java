package org.glob3.mobile.specific;

import android.opengl.GLES20;
import android.util.Log;


public class GL2Shaders {


	private final static String _fragmentShader = 
	"varying mediump vec2 TextureCoordOut;\n" + 
	"uniform mediump vec2 TranslationTexCoord;\n" + 
	"uniform mediump vec2 ScaleTexCoord;\n" + 
	"\n" + 
	"varying mediump vec4 VertexColor;\n" + 
	"\n" + 
	"uniform sampler2D Sampler;\n" + 
	"uniform bool EnableTexture;\n" + 
	"uniform lowp vec4 FlatColor;\n" + 
	"\n" + 
	"uniform bool EnableColorPerVertex;\n" + 
	"uniform bool EnableFlatColor;\n" + 
	"uniform mediump float FlatColorIntensity;\n" + 
	"uniform mediump float ColorPerVertexIntensity;\n" + 
	"\n" + 
	"void main() {\n" + 
	"  \n" + 
	"  if (EnableTexture) {\n" + 
	"    gl_FragColor = texture2D(Sampler, TextureCoordOut * ScaleTexCoord + TranslationTexCoord);\n" + 
	"\n" + 
	"    if (EnableFlatColor || EnableColorPerVertex){\n" + 
	"      lowp vec4 color;\n" + 
	"      if (EnableFlatColor) {\n" + 
	"        color = FlatColor;\n" + 
	"        if (EnableColorPerVertex) {\n" + 
	"          color = color * VertexColor;\n" + 
	"        }\n" + 
	"      }\n" + 
	"      else {\n" + 
	"        color = VertexColor;\n" + 
	"      }\n" + 
	"      \n" + 
	"      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\n" + 
	"      gl_FragColor = mix(gl_FragColor,\n" + 
	"                         VertexColor,\n" + 
	"                         intensity);\n" + 
	"    }\n" + 
	"  }\n" + 
	"  else {\n" + 
	"    \n" + 
	"    if (EnableColorPerVertex) {\n" + 
	"      gl_FragColor = VertexColor;\n" + 
	"      if (EnableFlatColor) {\n" + 
	"        gl_FragColor = gl_FragColor * FlatColor;\n" + 
	"      }\n" + 
	"    }\n" + 
	"    else {\n" + 
	"      gl_FragColor = FlatColor;\n" + 
	"    }\n" + 
	"    \n" + 
	"  }\n" + 
	"  \n" + 
	"}";

	private final static String _vertexShader = 
	"uniform mat4 Projection;\n" + 
	"uniform mat4 Modelview;\n" + 
	"\n" + 
	"uniform bool BillBoard;\n" + 
	"uniform float ViewPortRatio;\n" + 
	"uniform float PointSize;\n" + 
	"\n" + 
	"varying vec4 VertexColor;\n" + 
	"varying vec2 TextureCoordOut;\n" + 
	"\n" + 
	"/* //USEFUL VARIABLES FOR LIGHTING\n" + 
	"varying float DiffuseLighting;\n" + 
	"uniform vec3 LightDirection; // light direction in eye coords\n" + 
	"*/\n" + 
	"\n" + 
	"\n" + 
	"void main() {\n" + 
	"  gl_Position = Projection * Modelview * Position;\n" + 
	"  \n" + 
	"  if (BillBoard) {\n" + 
	"    gl_Position.x += (-0.05 + TextureCoord.x * 0.1) * gl_Position.w;\n" + 
	"    gl_Position.y -= (-0.05 + TextureCoord.y * 0.1) * gl_Position.w * ViewPortRatio;\n" + 
	"  }\n" + 
	"  \n" + 
	"  TextureCoordOut = TextureCoord;\n" + 
	"  \n" + 
	"  VertexColor = Color;\n" + 
	"  \n" + 
	"  gl_PointSize = PointSize;\n" + 
	"  \n" + 
	"  vec3 x = Normal; //This line has been added to avoid compiler taking Normal variable away (Remove when Normal has been used)\n" + 
	"}";


   public static String getFragmentShader() {
      return _fragmentShader;
   }


   public static String getVertexShader() {
      return _vertexShader;
   }


   public static int loadShader(final int shaderType,
                                final String source) {
      int shader = GLES20.glCreateShader(shaderType);
      if (shader != 0) {
         GLES20.glShaderSource(shader, source);
         GLES20.glCompileShader(shader);
         final int[] compiled = new int[1];
         GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
         if (compiled[0] == 0) {
            Log.e("GL2Shaders", "Could not compile shader " + shaderType + ":");
            Log.e("GL2Shaders", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
         }
      } else{
    	  int error = GLES20.glGetError();
    	  Log.d("GL", "ERROR CREATING SHADER " + error);
    	  
      }
      return shader;
   }


   public static int createProgram(final String vertexSource,
                                   final String fragmentSource) {
      final int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
      if (vertexShader == 0) {
         return 0;
      }

      final int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
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
