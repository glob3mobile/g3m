//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform mediump vec2 uTranslationTexCoord;
uniform mediump vec2 uScaleTexCoord;
//uniform mat4 uModelview;
uniform mat4 uProjection;
uniform mat4 uCameraModel;
uniform mat4 uModel;

uniform float uPointSize;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;

void main() {
//  gl_Position = uModelview * aPosition;
  gl_Position = uProjection * uCameraModel * uModel * aPosition;

  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;
  
  gl_PointSize = uPointSize;
}
