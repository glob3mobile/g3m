//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 Position;
uniform mat4 uModelview;

void main() {
  gl_Position = uModelview * Position;
}
