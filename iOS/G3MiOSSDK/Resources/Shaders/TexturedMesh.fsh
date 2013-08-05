//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec2 TextureCoordOut;
varying mediump vec4 VertexColor;

uniform sampler2D Sampler;

void main() {
  gl_FragColor = texture2D(Sampler, TextureCoordOut);
}