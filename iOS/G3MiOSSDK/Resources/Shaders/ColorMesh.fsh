//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec4 VertexColor;

void main() {
    if (VertexColor.a > 0.0){
        gl_FragColor = VertexColor;
    } else{
        discard;
    }
}
