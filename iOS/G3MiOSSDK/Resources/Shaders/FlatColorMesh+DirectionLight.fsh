//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

precision highp float;

uniform sampler2D Sampler;
uniform float uAmbientLight;

uniform vec4 uLightColor;

varying float diffuseLightIntensity;

uniform lowp vec4 uFlatColor;

void main() {
  gl_FragColor = uFlatColor;
  
  vec4 lightColor = vec4(uAmbientLight, uAmbientLight, uAmbientLight, 1.0) + uLightColor * diffuseLightIntensity;
  gl_FragColor *= lightColor;
}