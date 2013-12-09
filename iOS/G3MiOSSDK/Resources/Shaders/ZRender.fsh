//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//


varying highp float R;
varying highp float G;
varying highp float B;

void main() {
  // writes zvalue
  gl_FragColor = vec4(R, G, B, 0.0);
}