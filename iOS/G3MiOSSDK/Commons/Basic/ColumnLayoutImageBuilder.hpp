//
//  ColumnLayoutImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#ifndef __G3MiOSSDK__ColumnLayoutImageBuilder__
#define __G3MiOSSDK__ColumnLayoutImageBuilder__

#include "LayoutImageBuilder.hpp"

class ColumnLayoutImageBuilder : public LayoutImageBuilder {
protected:
  void doLayout(const G3MContext* context,
                IImageBuilderListener* listener,
                bool deleteListener,
                const std::vector<ChildResult*>& results);

public:

  ColumnLayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                           int margin = 4,
                           int borderWidth = 0,
                           const Color& borderColor = Color::transparent(),
                           int padding = 2);

  ColumnLayoutImageBuilder(IImageBuilder* child0,
                           IImageBuilder* child1,
                           int margin = 4,
                           int borderWidth = 0,
                           const Color& borderColor = Color::transparent(),
                           int padding = 2);

};


#endif
