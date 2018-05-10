//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
uniform mat4 uModelview;

uniform float uPointSize;

attribute float aColorValue;
varying highp float colorValue;

void main() {
  gl_Position = uModelview * aPosition;
  gl_PointSize = uPointSize;
    colorValue = aColorValue;
    
    highp float x = aColorValue;
    x = x +1.0;
}
