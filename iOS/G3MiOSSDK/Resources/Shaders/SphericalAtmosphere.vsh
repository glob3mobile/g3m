attribute vec4 aPosition; //Position of ZNear Frame corners relative to the camera
uniform mat4 uModelview; //Model + Projection

uniform float uPointSize;

varying vec3 rayDirection;

void main() {
  gl_Position = uModelview * aPosition;
  gl_Position.z = 0.0;

  gl_PointSize = uPointSize;
  //Ray [O + tD = X]
  rayDirection = aPosition.xyz;
}
