//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition; //Position of ZNear Frame corners in world-space
uniform mat4 uModelview; //Model + Projection

uniform float uPointSize;

varying highp vec3 planePos;

void main() {
  gl_Position = uModelview * aPosition;
  gl_Position.z = 0.0;
  
  gl_PointSize = uPointSize;
  planePos = aPosition.xyz;
}
