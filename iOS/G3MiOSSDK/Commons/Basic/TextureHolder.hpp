//
//  TextureHolder.h
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 01/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureHolder_h
#define G3MiOSSDK_TextureHolder_h

#include <string>

class TextureHolder {
public:
    const std::string _textureId;
    const int         _textureWidth;
    const int         _textureHeight;
    int _glTextureId;
    
    long _referenceCounter;
    
    TextureHolder(const std::string textureId,
                  const int textureWidth,
                  const int textureHeight) :
    _textureId(textureId),
    _textureWidth(textureWidth),
    _textureHeight(textureHeight)
    {
        _referenceCounter = 1;
        _glTextureId = -1;
    }
    
    ~TextureHolder() {
    }
    
    void retain() {
        _referenceCounter++;
    }
    
    void release() {
        _referenceCounter--;
    }
    
    bool isRetained() {
        return _referenceCounter > 0;
    }
    
    bool hasKey(const std::string textureId,
                const int textureWidth,
                const int textureHeight) {
        if (_textureWidth != textureWidth) {
            return false;
        }
        if (_textureHeight != textureHeight) {
            return false;
        }
        
        if (_textureId.compare(textureId) != 0) {
            return false;
        }
        
        return true;
    }
};


#endif
