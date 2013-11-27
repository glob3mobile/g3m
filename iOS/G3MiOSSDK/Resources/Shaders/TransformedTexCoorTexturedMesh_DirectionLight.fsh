//
//  TransformedTexCoorTexturedMesh_DirectionLight
//
//  Created by José Miguel Santana Núñez
//

precision highp float;

varying mediump vec2 TextureCoordOut;

uniform sampler2D Sampler;

varying vec3 lightColor;

void main() {
  vec4 texColor = texture2D(Sampler, TextureCoordOut);
  gl_FragColor.r = texColor.r * lightColor.r;
  gl_FragColor.g = texColor.g * lightColor.r;
  gl_FragColor.b = texColor.b * lightColor.r;
  gl_FragColor.a = texColor.a;
}