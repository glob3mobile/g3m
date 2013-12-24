//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;
attribute vec2 aTextureCoord2;

uniform mat4 uModelview;
uniform float uPointSize;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;
varying vec2 TextureCoordOut2;

void main() {
  gl_Position = uModelview * aPosition;

  TextureCoordOut = aTextureCoord;
  
  gl_PointSize = uPointSize;
}
