#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

varying vec4  StarColor;
varying float StarPointSize;

void main() {
  // a round disc with a soft edge one pixel wide
  float distanceToCentreInPixels = length(gl_PointCoord - vec2(0.5)) * StarPointSize;
  float coverage = clamp(StarPointSize / 2.0 - distanceToCentreInPixels + 0.5, 0.0, 1.0);
  gl_FragColor = vec4(StarColor.rgb, StarColor.a * coverage);
}
