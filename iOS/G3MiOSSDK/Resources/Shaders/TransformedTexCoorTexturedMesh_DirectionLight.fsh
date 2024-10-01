#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

varying mediump vec2 TextureCoordOut;

uniform sampler2D Sampler;

varying mediump vec3 lightColor;

void main() {
  vec4 texColor = texture2D(Sampler, TextureCoordOut);
  gl_FragColor.rgb = texColor.rgb * lightColor.rgb;
  gl_FragColor.a   = texColor.a;
}
