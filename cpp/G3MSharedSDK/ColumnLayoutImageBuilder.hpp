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
private:
  const int _childrenSeparation;

protected:
  void doLayout(const G3MContext* context,
                IImageBuilderListener* listener,
                bool deleteListener,
                const std::vector<ChildResult*>& results);

public:

  ColumnLayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                           const ImageBackground*             background         = NULL,
                           const int                          childrenSeparation = 0);

  ColumnLayoutImageBuilder(IImageBuilder*         child0,
                           IImageBuilder*         child1,
                           const ImageBackground* background         = NULL,
                           const int              childrenSeparation = 0);

  ColumnLayoutImageBuilder(IImageBuilder*         child0,
                           const ImageBackground* background         = NULL,
                           const int              childrenSeparation = 0);

};

#endif
