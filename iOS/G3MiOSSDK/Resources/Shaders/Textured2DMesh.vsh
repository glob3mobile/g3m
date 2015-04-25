//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec2 aPosition2D;
attribute vec2 aTextureCoord;
uniform float uPointSize;

varying vec2 TextureCoordOut;

uniform vec2 uTranslation2D;
uniform vec2 uViewPortExtent;

void main() {
  
  vec2 pixel = aPosition2D;
  pixel.x -= uViewPortExtent.x / 2.0;
  pixel.y += uViewPortExtent.y / 2.0;
  
  
  gl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),
                     (pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),
                     0, 1);

  TextureCoordOut = aTextureCoord;
  
  gl_PointSize = uPointSize;
}
