//
//  G3MError.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

#ifndef __G3MiOSSDK__G3MError__
#define __G3MiOSSDK__G3MError__

#ifdef C_CODE

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

#endif

#ifdef JAVA_CODE
public class G3MError extends java.lang.RuntimeException {
  private static final long serialVersionUID = 1L;


  public G3MError(final String description) {
    super(description);
  }
}
#endif
