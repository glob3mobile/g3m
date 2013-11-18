//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec3 aNormal;

uniform mat4 uModelview;
uniform mat4 uModel;

uniform float uPointSize;

uniform float uAmbientLight;
uniform vec4 uDiffuseLightColor;

uniform vec3 uDiffuseLightDirection; //We must normalize
varying vec3 lightColor;

void main() {

  vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));
  vec3 lightDirNormalized = normalize( uDiffuseLightDirection );
  
  float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);

  gl_Position = uModelview * aPosition;

  gl_PointSize = uPointSize;

  vec3 ambientLightColor = vec3(uAmbientLight, uAmbientLight, uAmbientLight);

  //Computing Total Light in Vertex
  lightColor = ambientLightColor + uDiffuseLightColor.rgb * diffuseLightIntensity;
}
