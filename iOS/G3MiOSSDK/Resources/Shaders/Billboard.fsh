//
//  Billboard.fsh
//
//  Created by José Miguel Santana Núñez
//
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

varying mediump vec2 TextureCoordOut;
uniform sampler2D Sampler;

void main() {
  gl_FragColor = texture2D(Sampler, TextureCoordOut);
}
