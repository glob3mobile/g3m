//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

struct PointLight{
  vec3 position;
  vec4 color;
};

uniform PointLight uPointLight;

attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform mat4 uModelview;
uniform float uPointSize;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;

void main() {
  gl_Position = uModelview * aPosition;
  
  TextureCoordOut = aTextureCoord;
  
  gl_PointSize = uPointSize;
}
