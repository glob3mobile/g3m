//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;
attribute vec2 aTextureCoord2;
attribute vec2 aTextureCoord3;

uniform mat4 uModelview;
uniform float uPointSize;

varying vec2 TextureCoordOut;
varying vec2 TextureCoordOut2;
varying vec2 TextureCoordOut3;

void main() {
  gl_Position = uModelview * aPosition;

  TextureCoordOut = aTextureCoord;
  TextureCoordOut2 = aTextureCoord2;
  TextureCoordOut3 = aTextureCoord3;
  
  gl_PointSize = uPointSize;
}
