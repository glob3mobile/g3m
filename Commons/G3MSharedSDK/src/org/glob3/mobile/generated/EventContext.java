package org.glob3.mobile.generated; 
//************************************************************


//class InitializationContext: public Context {
//public:
//  InitializationContext(const IFactory*     factory,
//                        const IStringUtils* stringUtils,
//                        const IThreadUtils* threadUtils,
//                        const ILogger*      logger,
//                        const IMathUtils*   mathUtils,
//                        const IJSONParser*  jsonParser,
//                        const Planet*       planet,
//                        IDownloader*        downloader,
//                        EffectsScheduler*   effectsScheduler,
//                        IStorage*           storage) :
//  Context(factory,
//          stringUtils,
//          threadUtils,
//          logger,
//          mathUtils,
//          jsonParser,
//          planet,
//          downloader,
//          effectsScheduler,
//          storage) {
//  }
//};

//************************************************************

public class EventContext extends Context
{
  public EventContext(IFactory factory, IStringUtils stringUtils, IThreadUtils threadUtils, ILogger logger, IMathUtils mathUtils, IJSONParser jsonParser, Planet planet, IDownloader downloader, EffectsScheduler scheduler, IStorage storage)
  {
	  super(factory, stringUtils, threadUtils, logger, mathUtils, jsonParser, planet, downloader, scheduler, storage);
  }
}