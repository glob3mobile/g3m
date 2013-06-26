//
//  Billboard.fsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec2 TextureCoordOut;
uniform sampler2D Sampler;

void main() {
  gl_FragColor = texture2D(Sampler, TextureCoordOut);
}