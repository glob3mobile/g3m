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
  // Texels are premultiplied on every platform: the matrix offset scales with alpha and the result stays a valid premultiplied colour.
  vec3 gradedColor = (uColorMatrix * color).rgb;
  gl_FragColor = vec4(clamp(gradedColor, 0.0, color.a), color.a);
}
