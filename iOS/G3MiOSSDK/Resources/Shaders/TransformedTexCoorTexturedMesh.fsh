//
//  Default.vsh
//

#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

varying mediump vec2 TextureCoordOut;
varying mediump vec4 VertexColor;

uniform sampler2D Sampler;

void main() {
  gl_FragColor = texture2D(Sampler, TextureCoordOut);
}
