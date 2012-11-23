//
//  G3MBuilder_iOS.mm
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#import "G3MBuilder_iOS.hpp"
#include "Factory_iOS.hpp"
#include "GL.hpp"
#include "NativeGL2_iOS.hpp"

G3MBuilder_iOS::G3MBuilder_iOS(G3MWidget_iOS* nativeWidget) {
    _nativeWidget = nativeWidget;
    
    [_nativeWidget initSingletons];
}

void G3MBuilder_iOS::initializeWidget() {
    NativeGL2_iOS* nativeGL = new NativeGL2_iOS();
    setNativeGL(nativeGL);

    [_nativeWidget setWidget: create()];
}
