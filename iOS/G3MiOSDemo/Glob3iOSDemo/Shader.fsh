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
uniform lowp vec4 FlatColor;

uniform bool EnableColorPerVertex;
uniform bool EnableFlatColor;
uniform mediump float FlatColorIntensity;
uniform mediump float ColorPerVertexIntensity;

void main() {
  
  if (EnableTexture) {
  
    gl_FragColor = texture2D(Sampler, TextureCoordOut);
    
    if (EnableColorPerVertex){
      gl_FragColor = mix( gl_FragColor,
                        VertexColor,
                        ColorPerVertexIntensity);
    }
    
    if (EnableFlatColor){
      gl_FragColor = mix( gl_FragColor,
                        FlatColor,
                        FlatColorIntensity);
    }
    
  }
  else {
    gl_FragColor = FlatColor;
  }
}
