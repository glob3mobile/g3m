//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

uniform lowp vec4 uFlatColor;
varying highp float alpha;

void main() {
    if (alpha <= 0.0){
        discard;
    }

  gl_FragColor = uFlatColor;
  gl_FragColor.a *= alpha;
}
