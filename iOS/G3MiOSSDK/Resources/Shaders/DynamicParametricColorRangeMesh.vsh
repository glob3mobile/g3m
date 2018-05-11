//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
uniform mat4 uModelview;

uniform float uPointSize;

attribute float aColorValue; //Between 0..1
attribute float aColorValueNext; //Between 0..1

varying highp float currentColorValue;
uniform highp float uTime; //Between 0..1

void main() {
  gl_Position = uModelview * aPosition;
  gl_PointSize = uPointSize;

    currentColorValue = mix(aColorValue, aColorValueNext, uTime);
}
