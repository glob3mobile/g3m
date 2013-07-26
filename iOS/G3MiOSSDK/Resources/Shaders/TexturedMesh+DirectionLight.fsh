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

varying float diffuseLightIntensity;

void main() {
  gl_FragColor = texture2D(Sampler, TextureCoordOut);
  
  gl_FragColor *= (uAmbientLight + diffuseLightIntensity);
}