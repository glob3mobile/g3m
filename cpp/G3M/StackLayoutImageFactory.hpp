//
//  StackLayoutImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#ifndef __G3M__StackLayoutImageFactory__
#define __G3M__StackLayoutImageFactory__

#include "LayoutImageFactory.hpp"

class StackLayoutImageFactory : public LayoutImageFactory {
protected:
  ~StackLayoutImageFactory() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void doLayout(const G3MContext* context,
                IImageFactoryListener* listener,
                bool deleteListener,
                const std::vector<ChildResult*>& results);

public:

  StackLayoutImageFactory(const std::vector<IImageFactory*>& children,
                          const ImageBackground*             background = NULL);

  StackLayoutImageFactory(IImageFactory*         child0,
                          IImageFactory*         child1,
                          const ImageBackground* background = NULL);

};


#endif
