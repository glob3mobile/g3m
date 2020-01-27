//
//  IGLUniformID.hpp
//  G3M
//
//  Created by José Miguel S N on 19/09/12.
//

#ifndef G3M_IGLUniformID
#define G3M_IGLUniformID

class IGLUniformID {
public:
  virtual bool isValid() const = 0;
  
#ifdef C_CODE
  virtual ~IGLUniformID() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
};

#endif
