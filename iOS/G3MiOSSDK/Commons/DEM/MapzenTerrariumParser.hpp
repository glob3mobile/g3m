//
//  MapzenTerrariumParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//

#ifndef MapzenTerrariumParser_hpp
#define MapzenTerrariumParser_hpp

#include "GAsyncTask.hpp"

class FloatBufferDEMGrid;
class IImage;
#include "Sector.hpp"


class MapzenTerrariumParser {
private:
  MapzenTerrariumParser() {
  }


public:

  class Listener {
  public:
    virtual ~Listener() {

    }

    virtual void onGrid(FloatBufferDEMGrid* grid) = 0;
  };

private:
  class ParserTask : public GAsyncTask {
  private:
    const IImage* _image;
    const Sector  _sector;
    const double  _deltaHeight;
    MapzenTerrariumParser::Listener* _listener;
    const bool _deleteListener;

    FloatBufferDEMGrid* _result;

  public:
    ParserTask(const IImage* image,
               const Sector& sector,
               const double  deltaHeight,
               MapzenTerrariumParser::Listener* listener,
               bool deleteListener);

    virtual ~ParserTask();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };


public:
  static FloatBufferDEMGrid* parse(const IImage* image,
                                   const Sector& sector,
                                   double deltaHeight);

  static void parse(const G3MContext* context,
                    const IImage* image,
                    const Sector& sector,
                    double deltaHeight,
                    MapzenTerrariumParser::Listener* listener,
                    bool deleteListener);
  
};

#endif
