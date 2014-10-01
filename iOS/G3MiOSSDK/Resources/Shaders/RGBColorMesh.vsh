//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec3 aRGBColor;

uniform mat4 uModelview;

uniform float uPointSize;

varying vec4 VertexColor;

void main() {
  gl_Position = uModelview * aPosition;
  VertexColor = vec4(aRGBColor.x /255.0, aRGBColor.y / 255.0, aRGBColor.z / 255.0, 1.0);
  gl_PointSize = uPointSize;
}
