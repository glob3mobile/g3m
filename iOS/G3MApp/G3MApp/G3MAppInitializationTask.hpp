//
//  G3MAppInitializationTask.hpp
//  G3MApp
//
//  Created by Mari Luz Mateo on 23/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <G3MiOSSDK/GInitializationTask.hpp>

@class G3MWidget_iOS;

class G3MAppInitializationTask : public GInitializationTask {
private:
  bool _wikiMarkersParsed;
  bool _weatherMarkersParsed;
  G3MWidget_iOS*  _widget;

public:
  G3MAppInitializationTask(G3MWidget_iOS*  widget) :
  _wikiMarkersParsed(false),
  _weatherMarkersParsed(false),
  _widget(widget)
  {
  }

  void run(const G3MContext* context);

  bool isDone(const G3MContext* context);

  void setWikiMarkersParsed(bool parsed);

  void setWeatherMarkersParsed(bool parsed);
  
};
