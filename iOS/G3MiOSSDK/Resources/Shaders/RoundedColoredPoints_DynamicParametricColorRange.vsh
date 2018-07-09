//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec4 aColor;

uniform mat4 uModelview;

uniform float uPointSize;

attribute float aColorValue; //Between 0..1
attribute float aColorValueNext; //Between 0..1
uniform highp float uTime; //Between 0..1

uniform lowp vec4 uColorAt0;
uniform lowp vec4 uColorAt1;
varying lowp vec4 vertexColor;
varying lowp float currentValue;

void main() {
  gl_Position = uModelview * aPosition;
  gl_PointSize = uPointSize;
    
    currentValue = mix(aColorValue, aColorValueNext, uTime);
    vertexColor = mix(uColorAt0, uColorAt1, currentValue);
}
