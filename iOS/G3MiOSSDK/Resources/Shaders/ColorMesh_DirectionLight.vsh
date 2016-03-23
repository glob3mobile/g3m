//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec4 aColor;

uniform mat4 uModelview;

uniform float uPointSize;

varying vec4 VertexColor;

void main() {
  vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));
  vec3 lightDirNormalized = normalize( uDiffuseLightDirection );
  
  float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);
  
  gl_Position = uModelview * aPosition;
  
  gl_PointSize = uPointSize;
  
  //Computing Total Light in Vertex
  lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;
  lightColor.x = min(lightColor.x, 1.0);
  lightColor.y = min(lightColor.y, 1.0);
  lightColor.z = min(lightColor.z, 1.0);
  
  //Passing color
  VertexColor = aColor;
  
}
