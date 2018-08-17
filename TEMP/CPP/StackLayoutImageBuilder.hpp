//
//  StackLayoutImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#ifndef __G3MiOSSDK__StackLayoutImageBuilder__
#define __G3MiOSSDK__StackLayoutImageBuilder__

#include "LayoutImageBuilder.hpp"

class StackLayoutImageBuilder : public LayoutImageBuilder {
protected:
  void doLayout(const G3MContext* context,
                IImageBuilderListener* listener,
                bool deleteListener,
                const std::vector<ChildResult*>& results);

public:

  StackLayoutImageBuilder(const std::vector<IImageBuilder*>& children);

  StackLayoutImageBuilder(IImageBuilder* child0,
                          IImageBuilder* child1);

};


#endif
