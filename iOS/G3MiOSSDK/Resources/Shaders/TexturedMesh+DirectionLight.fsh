//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

precision highp float;

varying mediump vec2 TextureCoordOut;
varying mediump vec4 VertexColor;

uniform sampler2D Sampler;
uniform float uAmbientLight;

uniform vec4 uLightColor;

uniform vec3 uLightDirection; //MUST BE NORMALIZED

//varying float diffuseLightIntensity;

varying vec3 vertex_normal;

void main() {
  gl_FragColor = texture2D(Sampler, TextureCoordOut);
  
//  vec4 lightColor = vec4(1.0,1.0,1.0,1.0) * uAmbientLight + uLightColor * diffuseLightIntensity;
//  gl_FragColor += lightColor;

  float diffuseLightIntensity = max(dot(vertex_normal, uLightDirection ), 0.0);


  gl_FragColor *= (uAmbientLight + diffuseLightIntensity);
}