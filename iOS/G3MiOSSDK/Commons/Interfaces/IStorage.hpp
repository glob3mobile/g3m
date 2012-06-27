//
//  IStorage.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 25/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IStorage_hpp
#define G3MiOSSDK_IStorage_hpp
class IStorage{
public:
    virtual FILE* findFileFromFileName(const std::string filename) const = 0;
    
    // a virtual destructor is needed for conversion to Java
    virtual ~IStorage() {}
}


#endif
