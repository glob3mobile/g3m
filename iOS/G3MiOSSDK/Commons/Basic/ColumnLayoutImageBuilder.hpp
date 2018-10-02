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
                           const Vector2F&                    margin             = Vector2F::zero(),
                           float                              borderWidth        = 0,
                           const Color&                       borderColor        = Color::transparent(),
                           const Vector2F&                    padding            = Vector2F::zero(),
                           const Color&                       backgroundColor    = Color::transparent(),
                           float                              cornerRadius       = 0,
                           int                                childrenSeparation = 0);

  ColumnLayoutImageBuilder(IImageBuilder*  child0,
                           IImageBuilder*  child1,
                           const Vector2F& margin             = Vector2F::zero(),
                           float           borderWidth        = 0,
                           const Color&    borderColor        = Color::transparent(),
                           const Vector2F& padding            = Vector2F::zero(),
                           const Color&    backgroundColor    = Color::transparent(),
                           float           cornerRadius       = 0,
                           int             childrenSeparation = 0);

};

#endif
