//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec4 aColor;

uniform mat4 uModelview;

uniform float uPointSize;

varying vec4 VertexColor;

void main() {
  gl_Position = uModelview * aPosition;
  VertexColor = aColor;
  gl_PointSize = uPointSize;
}
