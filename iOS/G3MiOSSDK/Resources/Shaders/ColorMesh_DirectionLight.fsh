//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

precision highp float;


varying vec4 VertexColor;

varying vec3 lightColor;

void main() {
    
    if (VertexColor.a <= 0.0){
        discard;
    } else{
        gl_FragColor.r = VertexColor.r * lightColor.r;
        gl_FragColor.g = VertexColor.g * lightColor.r;
        gl_FragColor.b = VertexColor.b * lightColor.r;
        gl_FragColor.a = VertexColor.a;
    }
}
