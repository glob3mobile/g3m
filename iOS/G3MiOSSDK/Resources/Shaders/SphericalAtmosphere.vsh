attribute vec4 aPosition; //Position of ZNear Frame corners in world-space
uniform mat4 uModelview; //Model + Projection

uniform float uPointSize;

uniform vec3 uCameraPosition;
varying vec3 rayDirection;

void main() {
  gl_Position = uModelview * aPosition;
  gl_Position.z = 0.0;

  gl_PointSize = uPointSize;
  vec3 planePos = aPosition.xyz;

  //Ray [O + tD = X]
  rayDirection = planePos - uCameraPosition;
}
