//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

uniform lowp vec4 uColorAt0;
uniform lowp vec4 uColorAt1;
varying highp float colorValue;

void main() {
  gl_FragColor = mix(uColorAt0, uColorAt1, colorValue);
}
