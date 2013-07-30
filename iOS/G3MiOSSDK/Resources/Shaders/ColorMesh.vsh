//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec4 aColor;

//uniform mat4 uModelview;

uniform mat4 uProjection;
uniform mat4 uCameraModel;
uniform mat4 uModel;

uniform float uPointSize;

varying vec4 VertexColor;

void main() {
//  gl_Position = uModelview * aPosition;
  gl_Position = uProjection * uCameraModel * uModel * aPosition;
  VertexColor = aColor;
  gl_PointSize = uPointSize;
}
