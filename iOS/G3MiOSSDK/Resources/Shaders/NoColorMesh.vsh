//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;

uniform mat4 uModelview;
uniform float uPointSize;

uniform mat4 uModel;

void main() {

  mat4 model = uModel;

  gl_Position = uModelview * aPosition;
  gl_PointSize = uPointSize;
}
