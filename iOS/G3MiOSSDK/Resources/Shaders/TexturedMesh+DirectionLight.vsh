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

uniform vec3 uLightDirection; //MUST BE NORMALIZED
varying float diffuseLightIntensity;

uniform float uAmbientLight;
uniform vec4 uLightColor;

varying vec4 lightColor;

void main() {

  vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));
  vec3 lightDir = normalize( uLightDirection );
  
  float diffuseLightIntensity = max(dot(normalInModel, lightDir), 0.0);

  gl_Position = uModelview * aPosition;
  
  TextureCoordOut = aTextureCoord;

  gl_PointSize = uPointSize;

  lightColor = vec4(uAmbientLight, uAmbientLight, uAmbientLight, 1.0) + uLightColor * diffuseLightIntensity;
}
