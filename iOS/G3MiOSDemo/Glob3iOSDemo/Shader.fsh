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

    if (EnableFlatColor || EnableColorPerVertex){

    lowp vec4 color;
    if (EnableFlatColor){
      color = FlatColor;
    } else{
      color = vec4(1.0, 1.0, 1.0, 1.0);
    }
    if (EnableColorPerVertex)
    {
      color = color * VertexColor;
    }
    
    gl_FragColor = mix( gl_FragColor,
                      VertexColor,
                      ColorPerVertexIntensity);
                      
    }
    
  }
  else {
    if (EnableColorPerVertex && !EnableFlatColor){
      gl_FragColor = VertexColor;
    } else
    if (EnableColorPerVertex && EnableFlatColor){
      gl_FragColor = mix( VertexColor,
                        FlatColor,
                        FlatColorIntensity);
    } else
      gl_FragColor = FlatColor;
  }
}
