//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

precision highp float;

uniform lowp vec4 uFlatColor;
varying vec4 lightColor;

void main() {
  gl_FragColor = uFlatColor * lightColor;
}