//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec2 aPosition2D;
uniform float uPointSize;

uniform vec2 uTranslation2D;
uniform vec2 uViewPortExtent;

void main() {
  
  vec2 pixel = aPosition2D;
  pixel.x -= uViewPortExtent.x / 2.0;
  pixel.y += uViewPortExtent.y / 2.0;
  
  gl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),
                     (pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),
                     0, 1);
  
  gl_PointSize = uPointSize;
}
