//
//  IStringBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IStringBuilder
#define G3MiOSSDK_IStringBuilder

#include <string>

class IStringBuilder {
  
  static IStringBuilder* _instance;
  
  
protected:
  
  virtual IStringBuilder* getNewInstance() const = 0;
  
public:
  static void setInstance(IStringBuilder* isb);
  
  static IStringBuilder* newStringBuilder();
  
  virtual IStringBuilder* addDouble(double d) = 0;
  virtual IStringBuilder* addFloat(float f) = 0;
  
  virtual IStringBuilder* addInt(int i) = 0;
  virtual IStringBuilder* addLong(long long l) = 0;
  
  virtual IStringBuilder* addString(const std::string& s) = 0;
  virtual IStringBuilder* addBool(bool b) = 0;
  
  virtual const std::string getString() const = 0;
  
  // a virtual destructor is needed for conversion to Java
  virtual ~IStringBuilder() {
  }
  
};


#endif
