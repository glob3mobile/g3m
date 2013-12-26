//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec2 TextureCoordOut;
varying mediump vec2 TextureCoordOut2;
varying mediump vec2 TextureCoordOut3;

uniform sampler2D Sampler;
uniform sampler2D Sampler2;
uniform sampler2D Sampler3;

void main() {

  mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);
  mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);
  mediump vec4 tex3 = texture2D(Sampler3, TextureCoordOut3);

  gl_FragColor = tex1 * tex2;

  gl_FragColor.w *= tex3.w;
}