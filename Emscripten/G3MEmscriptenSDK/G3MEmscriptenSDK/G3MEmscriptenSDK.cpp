//
//  G3MEmscriptenSDK.cpp
//  G3MEmscriptenSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/9/20.
//  Copyright © 2020 DIEGO RAMIRO GOMEZ-DECK. All rights reserved.
//

#include <iostream>
#include "G3MEmscriptenSDK.hpp"
#include "G3MEmscriptenSDKPriv.hpp"

void G3MEmscriptenSDK::HelloWorld(const char * s)
{
    G3MEmscriptenSDKPriv *theObj = new G3MEmscriptenSDKPriv;
    theObj->HelloWorldPriv(s);
    delete theObj;
};

void G3MEmscriptenSDKPriv::HelloWorldPriv(const char * s) 
{
    std::cout << s << std::endl;
};

