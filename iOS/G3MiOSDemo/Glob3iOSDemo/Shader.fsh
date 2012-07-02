//
//  Shader.fsh
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

varying mediump vec2 TextureCoordOut;

varying mediump vec4 VertexColor;

uniform sampler2D Sampler;
uniform bool EnableTexture;
uniform mediump float FlatColorIntensity;
uniform lowp vec4 FlatColor;

void main() {
  
  if (EnableTexture) {
    
    gl_FragColor = mix( texture2D (Sampler, TextureCoordOut),
                        VertexColor,
                        FlatColorIntensity);
    
    //gl_FragColor = texture2D (Sampler, TextureCoordOut);
  }
  else {
    gl_FragColor = FlatColor;
  }
}
