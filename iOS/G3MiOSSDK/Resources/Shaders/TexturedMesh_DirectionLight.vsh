//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;
attribute vec3 aNormal;

uniform mat4 uModelview;
uniform mat4 uModel;

uniform float uPointSize;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;

uniform vec3 uLightDirection; //MUST BE NORMALIZED
varying float diffuseLightIntensity;


void main() {

  vec3 normal = normalize( vec3(uModel * vec4(aNormal, 0.0) ));
  vec3 lightDir = normalize( uLightDirection );
  
  diffuseLightIntensity = max(dot(normal, lightDir), 0.0);

  gl_Position = uModelview * aPosition;
  
  TextureCoordOut = aTextureCoord;

  gl_PointSize = uPointSize;
}
