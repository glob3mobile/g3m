//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec4 aColor;

uniform mat4 uModelview;

//uniform mat4 uProjection;
//uniform mat4 uCameraModel;
uniform mat4 uModel;

uniform float uPointSize;

varying vec4 VertexColor;

void main() {
  mat4 model = uModel;
  gl_Position = uModelview * aPosition;
  VertexColor = aColor;
  gl_PointSize = uPointSize;
}
