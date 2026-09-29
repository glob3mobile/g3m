attribute vec4 aPosition;   // ribbon center line
attribute vec3 aRibbonSide; // unit side vector, +1/-1 per ribbon edge

uniform mat4 uModelview;
uniform float uPointSize;
uniform vec2 uViewPortExtent;
uniform vec2 uRibbonWidth; // x = width in meters, y = minimum width in pixels

void main() {
  vec4 center = uModelview * aPosition;
  vec4 side   = uModelview * (aPosition + vec4(aRibbonSide * (uRibbonWidth.x * 0.5), 0.0));

  if ((center.w <= 0.0) || (side.w <= 0.0)) {
    // behind the camera: perspective division is meaningless, keep the plain meters offset
    gl_Position = side;
  }
  else {
    vec2 halfViewport = uViewPortExtent * 0.5;
    vec2 dirPx = (side.xy / side.w - center.xy / center.w) * halfViewport;
    float metersPx = length(dirPx);

    gl_Position = center;
    if (metersPx > 0.0) {
      float halfPx = max(metersPx, uRibbonWidth.y * 0.5);
      vec2 offsetNDC = (dirPx / metersPx) * halfPx / halfViewport;
      gl_Position.xy += offsetNDC * center.w;
    }
  }

  gl_PointSize = uPointSize;
}
