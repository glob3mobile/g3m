//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec2 aPosition2D;
//attribute vec2 aTextureCoord;
uniform float uPointSize;

//varying vec2 TextureCoordOut;

void main() {
  gl_Position = vec4(aPosition2D.x, aPosition2D.y, 0, 1);

  //TextureCoordOut = aTextureCoord;
  
  gl_PointSize = uPointSize;
}
