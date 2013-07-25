//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform mat4 uModelview;
uniform float uPointSize;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;

uniform float uAmbientLight;
varying float AmbientLightIntensity;

void main() {

  float x = uAmbientLight;
  AmbientLightIntensity = uAmbientLight;

  gl_Position = uModelview * aPosition;
  
  TextureCoordOut = aTextureCoord;

  
  
  gl_PointSize = uPointSize;
}
