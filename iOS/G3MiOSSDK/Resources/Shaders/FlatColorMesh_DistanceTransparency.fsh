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

//  gl_FragColor = uFlatColor;
//  gl_FragColor.a *= alpha;
    
    gl_FragColor.r = uFlatColor.r;
    gl_FragColor.g = uFlatColor.g;
    gl_FragColor.b = uFlatColor.b;
    gl_FragColor.a = uFlatColor.a * alpha;
}
