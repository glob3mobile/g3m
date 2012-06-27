//
//  SQLiteStorage.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 25/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SQLiteStorage_hpp
#define G3MiOSSDK_SQLiteStorage_hpp

#include "IStorage.hpp"
#import <sqlite3.h>
class SQLiteStorage_iOS: public IStorage {
public:
    void IFile* findFileFromFileName(const std::string filename) const {
    }
    
private:
    void openConexion(){
    }
}

#endif
