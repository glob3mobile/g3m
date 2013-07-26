//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;
attribute vec4 aNormal;

uniform mat4 uModelview;
uniform float uPointSize;
uniform vec4 uLightDirection; //MUST BE NORMALIZED

varying vec4 VertexColor;
varying vec2 TextureCoordOut;

varying float diffuseLightIntensity;

void main() {

  vec4 normal = normalize( aNormal);
  diffuseLightIntensity = max(dot(normal, uLightDirection), 0.0);

  gl_Position = uModelview * aPosition;
  
  TextureCoordOut = aTextureCoord;

  gl_PointSize = uPointSize;
}
