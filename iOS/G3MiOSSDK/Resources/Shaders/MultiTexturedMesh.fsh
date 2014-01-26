//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec2 TextureCoordOut;
varying mediump vec2 TextureCoordOut2;

uniform sampler2D Sampler;
uniform sampler2D Sampler2;

void main() {

  mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);
  mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);

  gl_FragColor = tex1 * tex2;
}