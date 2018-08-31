//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

precision highp float;


varying vec4 VertexColor;
varying vec3 lightColor;
varying highp float alpha;

void main() {
    if (alpha <= 0.0){
        discard;
    }
    
    gl_FragColor.r = VertexColor.r * lightColor.r;
    gl_FragColor.g = VertexColor.g * lightColor.g;
    gl_FragColor.b = VertexColor.b * lightColor.b;
    gl_FragColor.a = VertexColor.a * alpha;
}
