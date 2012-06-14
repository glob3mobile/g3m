

package org.glob3.mobile.specific;

import android.opengl.GLES20;
import android.util.Log;


public class GL2Shaders {

   private final static String _fragmentShader = "varying mediump vec2 TextureCoordOut;" +

                                               "uniform sampler2D Sampler;" + "uniform bool EnableTexture;"
                                                 + "uniform lowp vec4 FlatColor;" +

                                                 "void main()" + "{" + "   if (EnableTexture)"
                                                 + "       gl_FragColor = texture2D (Sampler, TextureCoordOut);" + "   else"
                                                 + "        gl_FragColor = FlatColor;" + "}";


   private final static String _vertexShader   = "attribute vec4 Position;"
                                                 + "attribute vec2 TextureCoord;"
                                                 +

                                                 "varying vec2 TextureCoordOut;"
                                                 +

                                                 "uniform mat4 Projection;"
                                                 + "uniform mat4 Modelview;"
                                                 +

                                                 "uniform bool BillBoard;"
                                                 + "uniform float ViewPortRatio;"
                                                 +

                                                 "void main()"
                                                 + "{"
                                                 + "   if (!BillBoard){"
                                                 + "       gl_Position = Projection * Modelview * Position;"
                                                 + "       TextureCoordOut = TextureCoord;"
                                                 + "   }else{"
                                                 + "       gl_Position = Projection * Modelview * Position;"
                                                 + "       gl_Position.x += (-0.05 + TextureCoord.x * 0.1)* gl_Position.w ;"
                                                 + "       gl_Position.y -= (-0.05+ TextureCoord.y *0.1)* gl_Position.w * ViewPortRatio;"
                                                 +

                                                 "      TextureCoordOut = TextureCoord;" +

                                                 "   }" + "}";


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
