//
//  G3MColorGradingPanel.h
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#import <UIKit/UIKit.h>

class G3MColorGradingDemoScene;


// Sliders that build a custom color grade, starting from neutral every time it is shown
@interface G3MColorGradingPanel : UIView

-(instancetype) initWithScene:(G3MColorGradingDemoScene*) scene;

@end
