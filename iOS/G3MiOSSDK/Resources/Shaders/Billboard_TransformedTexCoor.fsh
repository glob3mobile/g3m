#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

varying vec2 TextureCoordOut;
uniform sampler2D Sampler;
uniform vec4 uBillboardColorFactor;

void main() {
  gl_FragColor = texture2D(Sampler, TextureCoordOut) * uBillboardColorFactor;
}
