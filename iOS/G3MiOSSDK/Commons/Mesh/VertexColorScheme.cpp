//
//  VertexColorScheme.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 13/07/2018.
//

#include "VertexColorScheme.hpp"

VertexColorScheme* VertexColorScheme::createDynamicParametric(IFloatBuffer* v0,
                                        IFloatBuffer* v1,
                                        const std::vector<Color*>& vc){
    VertexColorScheme* cs = NULL;
    if (vc.size() == 2){
        cs = new Dynamic2ColorScheme(v0, v1, *vc[0], *vc[1]);
    } else{ //3
        cs = new Dynamic3ColorScheme(v0, v1, *vc[0], *vc[1], *vc[2]);
    }
    return cs;
}

VertexColorScheme* VertexColorScheme::createStaticParametric(IFloatBuffer* v0,
                                                    const std::vector<Color*>& vc){
    VertexColorScheme* cs = NULL;
    if (vc.size() == 2){
        cs = new StaticParametric2ColorScheme(v0, *vc[0], *vc[1]);
    } else{ //3
        cs = new Static3ColorScheme(v0, *vc[0], *vc[1], *vc[2]);
    }
    return cs;
}
