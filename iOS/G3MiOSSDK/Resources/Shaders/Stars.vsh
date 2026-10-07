attribute vec4 aPosition;
attribute vec4 aColor;

uniform mat4 uModelview;

// diameter of a star as bright as white
uniform float uPointSize;
// multiplies the brightness of every star; the light above white makes the star bigger
uniform float uStarsIntensity;

varying vec4  StarColor;
varying float StarPointSize;

void main() {
  gl_Position = uModelview * aPosition;

  // the star colours are the hue at full brightness times the star brightness
  float brightness = max(aColor.r, max(aColor.g, aColor.b));
  float light = brightness * uStarsIntensity;

  StarColor = vec4(aColor.rgb / brightness, min(light, 1.0));
  // the disc area grows with the light
  StarPointSize = uPointSize * sqrt(max(light, 1.0));
  gl_PointSize = StarPointSize;
}
