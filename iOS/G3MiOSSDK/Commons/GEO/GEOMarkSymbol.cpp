//
//  GEOMarkSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//

#include "GEOMarkSymbol.hpp"

#include "GEOSymbolizationContext.hpp"
#include "ILogger.hpp"
#include "Mark.hpp"
#include "MarksRenderer.hpp"

GEOMarkSymbol::~GEOMarkSymbol() {
  delete _mark;
}

void GEOMarkSymbol::symbolize(const G3MRenderContext* rc,
                              const GEOSymbolizationContext& sc) const {

  MarksRenderer* marksRenderer = sc.getMarksRenderer();
  if (marksRenderer == NULL) {
    ILogger::instance()->logError("Can't simbolize with Mark, MarksRenderer was not set");
    delete _mark;
  }
  else {
    marksRenderer->addMark(_mark);
  }
  _mark = NULL;
}
