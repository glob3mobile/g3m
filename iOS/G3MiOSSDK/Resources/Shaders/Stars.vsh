attribute vec4 aPosition;
// the hue at full brightness (red, green, blue) and the visual magnitude
attribute vec4 aColor;

uniform mat4 uModelview;

// diameter in pixels of the faintest stars
uniform float uPointSize;
// a star of this magnitude is drawn opaque at the smallest diameter; fainter stars fade, brighter stars grow
uniform float uFullStarMagnitude;
// the diameter grows as the light to this power (Stellarium's relative star scale)
uniform float uStarSizeExponent;

varying vec4  StarColor;
varying float StarPointSize;

// The eye sees colour only on stars brighter than about magnitude 1: the fainter ones are seen by the rods, which are colour blind
const float colourVisionMagnitude = 1.0;

// light from the magnitude: 5 magnitudes are 100 times the light
float lightOfMagnitude(float magnitude, float referenceMagnitude) {
  return pow(10.0, 0.4 * (referenceMagnitude - magnitude));
}

void main() {
  gl_Position = uModelview * aPosition;

  float magnitude = aColor.a;
  float light = lightOfMagnitude(magnitude, uFullStarMagnitude);

  float colourSaturation = min(lightOfMagnitude(magnitude, colourVisionMagnitude), 1.0);
  vec3 colour = mix(vec3(1.0), aColor.rgb, colourSaturation);

  StarColor = vec4(colour, min(light, 1.0));
  StarPointSize = uPointSize * pow(max(light, 1.0), uStarSizeExponent);
  gl_PointSize = StarPointSize;
}
