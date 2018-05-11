//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

uniform lowp vec4 uColorAt0;
uniform lowp vec4 uColorAt1;

varying highp float colorValueAt0;
varying highp float colorValueAt1;

uniform highp float uTime; //Between 0..1

void main() {
    
    highp float currentValue = mix(colorValueAt0, colorValueAt1, uTime);
    
  gl_FragColor = mix(uColorAt0, uColorAt1, currentValue);
}
