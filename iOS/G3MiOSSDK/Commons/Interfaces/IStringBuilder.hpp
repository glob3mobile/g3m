//
//  IStringBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IStringBuilder_hpp
#define G3MiOSSDK_IStringBuilder_hpp

#include <string>

class IStringBuilder{
  
  static IStringBuilder* _instance;
  
  
protected:
  
  virtual IStringBuilder* getNewInstance() const = 0;
  
public:
  static void setInstance(IStringBuilder* isb);
  
  static IStringBuilder* newStringBuilder();
  
  virtual IStringBuilder* add(double d) = 0;
  virtual IStringBuilder* add(const char c[]) = 0;
  virtual IStringBuilder* add(const std::string& s) = 0;
  
  virtual std::string getString() const = 0;

  // a virtual destructor is needed for conversion to Java
  virtual ~IStringBuilder() {}
  
};


#endif
