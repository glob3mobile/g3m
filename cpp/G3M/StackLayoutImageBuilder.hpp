//
//  StackLayoutImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#ifndef __G3M__StackLayoutImageBuilder__
#define __G3M__StackLayoutImageBuilder__

#include "LayoutImageBuilder.hpp"

class StackLayoutImageBuilder : public LayoutImageBuilder {
protected:
  ~StackLayoutImageBuilder() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void doLayout(const G3MContext* context,
                IImageBuilderListener* listener,
                bool deleteListener,
                const std::vector<ChildResult*>& results);

public:

  StackLayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                          const ImageBackground*             background = NULL);

  StackLayoutImageBuilder(IImageBuilder*         child0,
                          IImageBuilder*         child1,
                          const ImageBackground* background = NULL);

};


#endif
