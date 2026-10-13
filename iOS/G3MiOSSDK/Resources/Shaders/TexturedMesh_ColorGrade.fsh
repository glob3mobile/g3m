#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

varying vec2 TextureCoordOut;

uniform sampler2D Sampler;
uniform mat4 uColorMatrix;

void main() {
  vec4 color = texture2D(Sampler, TextureCoordOut);
  vec3 gradedColor = (uColorMatrix * vec4(color.rgb, 1.0)).rgb;
  gl_FragColor = vec4(clamp(gradedColor, 0.0, 1.0), color.a);
}
