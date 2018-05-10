//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

uniform lowp vec4 uColorAt0;
uniform lowp vec4 uColotAt1;
attribute float aColorValue;

void main() {
  gl_FragColor = mix(uColorAt0, uColorAt1, aColorValue);
}
