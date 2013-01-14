

package org.glob3.mobile.specific;

import android.opengl.GLES20;
import android.util.Log;


public final class GL2Shaders {


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
                                                 + "                         intensity);" + "    }" + "  }" + "  else {" + "    "
                                                 + "    if (EnableColorPerVertex) {" + "      gl_FragColor = VertexColor;"
                                                 + "      if (EnableFlatColor) {"
                                                 + "        gl_FragColor = gl_FragColor * FlatColor;" + "      }" + "    }"
                                                 + "    else {" + "      gl_FragColor = FlatColor;" + "    }" + "    " + "  }"
                                                 + "  " + "}";

   private final static String _vertexShader   = "attribute vec4 Position;"
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
                                                 + "  }" + "  TextureCoordOut = TextureCoord;" + "  VertexColor = Color;"
                                                 + "  gl_PointSize = PointSize;" + "}";


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
      }
      else {
         final int error = GLES20.glGetError();
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
