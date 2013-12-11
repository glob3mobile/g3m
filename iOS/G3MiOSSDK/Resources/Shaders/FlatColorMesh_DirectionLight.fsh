//
//  FlatColorMesh_DirectionLight
//
//  Created by José Miguel Santana Núñez
//

precision highp float;

uniform lowp vec4 uFlatColor;
varying vec3 lightColor;

void main() {
  gl_FragColor.r = uFlatColor.r * lightColor.r;
  gl_FragColor.g = uFlatColor.g * lightColor.r;
  gl_FragColor.b = uFlatColor.b * lightColor.r;
  gl_FragColor.a = uFlatColor.a;
}