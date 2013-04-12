//
//  G3MError.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#ifndef G3MiOSSDK_G3MError_hpp
#define G3MiOSSDK_G3MError_hpp

#include <string>


class G3MError
#ifndef C_CODE
: public java.lang.RuntimeException
#endif
{
  
private:
  std::string _message;
public:

  G3MError(const std::string& message): _message(message){}
  std::string getMessage() const { return _message;}
  
};




#endif
