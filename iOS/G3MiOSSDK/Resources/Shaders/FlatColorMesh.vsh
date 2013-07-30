//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
//uniform mat4 uModelview;

uniform mat4 uProjection;
uniform mat4 uCameraModel;
uniform mat4 uModel;

uniform float uPointSize;

void main() {
//  gl_Position = uModelview * aPosition;

    gl_Position = uProjection * uCameraModel * uModel * aPosition;

  gl_PointSize = uPointSize;
}
