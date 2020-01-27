//
//  Info.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 02/09/14.
//
//

#ifndef __G3MiOSSDK__Info__
#define __G3MiOSSDK__Info__

#include <string>

class Info {
private:
  const std::string _text;
  
public:
  
  Info(const std::string& text);
  
  virtual ~Info();
  
  const std::string getText() const;
  
};

#endif
