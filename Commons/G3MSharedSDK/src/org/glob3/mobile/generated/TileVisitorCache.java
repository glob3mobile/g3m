package org.glob3.mobile.generated; 
//
//  TileVisitorCache.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 04/12/13.
//
//

//
//  TileVisitorCache.h
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 04/12/13.
//
//





public class TileVisitorCache implements ITileVisitor
{
  private G3MContext _context;
  private long _numVisits;
  private long _numPetitions;


  public void dispose()
  {
  }

  public TileVisitorCache(G3MContext context)
  {
     _context = context;
    _numVisits = 0;
    _numPetitions = 0;
  }

  public final void visitTile(java.util.ArrayList<Layer> layers, Tile tile)
  {
    for (int i = 0; i < layers.size(); i++)
    {
      _numVisits++;
      final Layer layer = layers.get(i);
      java.util.ArrayList<Petition> petitions = layer.createTileMapPetitions(null, tile);
      for (int j = 0; j < petitions.size(); j++)
      {
        _numPetitions++;
  
        final Petition petition = petitions.get(i);
  
        IImageDownloadListenerTileCache listener = new IImageDownloadListenerTileCache(_numVisits, tile, layer.getTitle());
        //IImageDownloadListenerTileCache(_numVisits, tile, layer->getTitle());
        IDownloader downloader = _context.getDownloader();
  
        long requestId = downloader.requestImage(new URL(petition.getURL()), 1, petition.getTimeToCache(), true, listener, true);
        if(requestId == -1)
        {
          _context.getLogger().logInfo("This request has been cached (z: %d, x: %d, y: %d)",tile._level,tile._column,tile._row);
        }
        else
        {
          _context.getLogger().logInfo("Petition %d has been request (z: %d, x: %d, y: %d)",requestId, tile._level,tile._column,tile._row);
        }
      }
    }
  }

  public final long getNumVisits()
  {
    return _numVisits;
  }

  public final long getNumPetitions()
  {
    return _numPetitions;
  }
}