//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
uniform mat4 uModelview;

uniform float uPointSize;

void main() {
  gl_Position = uModelview * aPosition;
  gl_PointSize = uPointSize;
}
