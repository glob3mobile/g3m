//
//  FlatColorMesh_DirectionLight
//

#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

uniform lowp vec4 uFlatColor;
varying vec3 lightColor;

void main() {
  gl_FragColor.rgb = uFlatColor.rgb * lightColor.rgb;
  gl_FragColor.a   = uFlatColor.a;
}
