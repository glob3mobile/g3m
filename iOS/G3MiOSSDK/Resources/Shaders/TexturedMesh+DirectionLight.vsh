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

varying vec2 TextureCoordOut;

uniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED
varying float diffuseLightIntensity;

uniform float uAmbientLight;
uniform vec4 uDiffuseLightColor;

varying vec3 lightColor;

void main() {

  vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));
  vec3 lightDirNormalized = normalize( uDiffuseLightDirection );
  
  float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);

  gl_Position = uModelview * aPosition;
  
  TextureCoordOut = aTextureCoord;

  gl_PointSize = uPointSize;

  vec3 ambientLightColor = vec3(uAmbientLight, uAmbientLight, uAmbientLight);

  //Computing Total Light in Vertex
  lightColor = ambientLightColor + uDiffuseLightColor.rgb * diffuseLightIntensity;
}
