//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//


varying lowp vec4 vertexColor;

void main() {
  gl_FragColor = mix(uColorAt0, uColorAt1, colorValue);
}
