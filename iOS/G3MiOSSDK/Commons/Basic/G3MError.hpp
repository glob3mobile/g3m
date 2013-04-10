//
//  G3MError.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

#ifndef __G3MiOSSDK__G3MError__
#define __G3MiOSSDK__G3MError__

#include <string>

class G3MError {
private:
  const std::string _description;

public:

  G3MError(const std::string& description) :
  _description(description)
  {

  }

};

#endif
