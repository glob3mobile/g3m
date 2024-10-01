#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

varying vec2 TextureCoordOut;
varying vec2 TextureCoordOut2;

uniform sampler2D Sampler;
uniform sampler2D Sampler2;

void main() {
  vec4 tex1 = texture2D(Sampler, TextureCoordOut);
  vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);

  gl_FragColor = tex1 * tex2;
}
