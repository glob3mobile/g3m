//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform mediump vec2 uTranslationTexCoord;
uniform mediump vec2 uScaleTexCoord;
uniform mat4 uModelview;

uniform float uPointSize;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;

void main() {
  gl_Position = uModelview * aPosition;

  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;
  
  gl_PointSize = uPointSize;
}
