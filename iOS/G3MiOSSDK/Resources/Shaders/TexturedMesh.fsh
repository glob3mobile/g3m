//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

varying mediump vec2 TextureCoordOut;

uniform sampler2D Sampler;

void main() {
    lowp vec4 color = texture2D(Sampler, TextureCoordOut);
    if (color.a > 0.0){
        gl_FragColor = color;
    } else{
        discard;
    }
}
