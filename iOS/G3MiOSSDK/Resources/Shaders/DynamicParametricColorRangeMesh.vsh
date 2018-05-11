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
varying highp float colorValueAt0;
varying highp float colorValueAt1;

void main() {
  gl_Position = uModelview * aPosition;
  gl_PointSize = uPointSize;
    colorValueAt0 = aColorValue;
    colorValueAt1 = aColorValueNext;
}
