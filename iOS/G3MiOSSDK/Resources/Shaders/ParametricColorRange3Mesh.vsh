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
    
    if (aColorValue < 0.5){
        highp float v = aColorValue * 2.0;
        vertexColor = mix(uColorAt0, uColorAt0_5, v);
    } else{
        highp float v = (aColorValue - 0.5) * 2.0;
        vertexColor = mix(uColorAt0_5, uColorAt1, v);
    }
}
