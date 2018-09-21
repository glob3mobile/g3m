//
//  Condition.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 19/09/2018.
//

#ifndef Condition_h
#define Condition_h

class Condition{
    
public:
    virtual ~Condition(){}
  
    virtual bool check() = 0;
    
};

#endif /* Condition_h */
