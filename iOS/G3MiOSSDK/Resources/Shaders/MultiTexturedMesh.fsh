//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec2 TextureCoordOut;
varying mediump vec2 TextureCoordOut2;
varying mediump vec4 VertexColor;

uniform sampler2D Sampler, Sampler2;

void main() {

  vec4 tex1 = texture2D(Sampler, TextureCoordOut);
  vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);

  gl_FragColor = tex1 * tex2;
}