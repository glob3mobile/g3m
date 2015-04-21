//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform mat4 uModelview;
uniform float uPointSize;

varying vec2 TextureCoordOut;

void main() {
  gl_Position = uModelview * aPosition;

  TextureCoordOut = aTextureCoord;
  
  gl_PointSize = uPointSize;
}
