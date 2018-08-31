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

uniform lowp vec4 uColorAt0;
uniform lowp vec4 uColorAt1;
varying highp float colorValue;

varying lowp vec4 vertexColor;

void main() {
  gl_Position = uModelview * aPosition;
  gl_PointSize = uPointSize;
    colorValue = aColorValue;
    
    vertexColor = mix(uColorAt0, uColorAt1, aColorValue);
}
