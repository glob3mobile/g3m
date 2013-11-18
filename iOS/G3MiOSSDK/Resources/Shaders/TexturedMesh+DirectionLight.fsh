//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

precision highp float;

varying mediump vec2 TextureCoordOut;

uniform sampler2D Sampler;

varying vec4 lightColor;

void main() {
  gl_FragColor = texture2D(Sampler, TextureCoordOut) * lightColor;
}