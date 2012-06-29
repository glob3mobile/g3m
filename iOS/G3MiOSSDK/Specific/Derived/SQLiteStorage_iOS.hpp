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

class SQLiteStorage_iOS: public IStorage {
public:
    virtual IFile* findFileFromFileName(const std::string filename) const;
    
private:
    virtual void openConexion() const;
    virtual void testConnection() const;
};

#endif
