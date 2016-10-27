//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition; //Position of ZNear Frame corners in world-space
uniform mat4 uModelview; //Model + Projection

uniform float uPointSize;

uniform highp vec3 uCameraPosition;
varying highp vec3 rayDirection;

void main() {
  gl_Position = uModelview * aPosition;
  gl_Position.z = 0.0;
  
  gl_PointSize = uPointSize;
  highp vec3 planePos = aPosition.xyz;
  
  //Ray [O + tD = X]
  rayDirection = planePos - uCameraPosition;
}
