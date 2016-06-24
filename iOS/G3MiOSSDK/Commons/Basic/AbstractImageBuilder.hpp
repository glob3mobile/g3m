//
//  AbstractImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#ifndef __G3MiOSSDK__AbstractImageBuilder__
#define __G3MiOSSDK__AbstractImageBuilder__

#include "IImageBuilder.hpp"

#include <stddef.h>

class AbstractImageBuilder : public IImageBuilder {
private:
  ChangedListener* _changeListener;

protected:
  const bool _scaleToDeviceResolution;

  void changed();

public:
<<<<<<< HEAD
  AbstractImageBuilder(bool scaleToDeviceResolution) :
  _scaleToDeviceResolution(scaleToDeviceResolution),
  _listener(NULL)
=======
  AbstractImageBuilder() :
  _changeListener(NULL)
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
  {
  }

  virtual ~AbstractImageBuilder() {
  }
  
  void setChangeListener(ChangedListener* changeListener);

};

#endif
