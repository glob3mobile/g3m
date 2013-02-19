package org.glob3.mobile.generated; 
//
//  TileMillLayer.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 19/02/13.
//
//

//
//  TileMillLayer.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 19/02/13.
//
//



public class TileMillLayer extends Layer
{
    private final Sector _sector ;
    private URL _mapServerURL = new URL();
    private final String _dataBaseMBTiles;

    public TileMillLayer(String layerName, URL mapServerURL, LayerCondition condition, Sector sector, String dataBaseMBTiles, TimeInterval timeToCache)
    {
       super(condition, layerName, timeToCache);
       _sector = new Sector(sector);
       _mapServerURL = new URL(mapServerURL);
       _dataBaseMBTiles = dataBaseMBTiles;

    }

    public final java.util.ArrayList<Petition> getMapPetitions(G3MRenderContext rc, Tile tile, int width, int height)
    {
        java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
        final Sector tileSector = tile.getSector();
    
        if (!_sector.touchesWith(tileSector))
        {
            return petitions;
        }
        final Sector sector = tileSector.intersection(_sector);
        String req = _mapServerURL.getPath();
        //If the server refer to itself as localhost...
        int pos = req.indexOf("localhost");
    
        if (pos != -1)
        {
            req = req.substring(pos+9);
            int pos2 = req.indexOf("/", 8);
            String newHost = req.substring(0, pos2);
            req = newHost + req;
        }
        IStringBuilder isb = IStringBuilder.newStringBuilder();
        isb.addString(req);
        isb.addString("db=");
        isb.addString(_dataBaseMBTiles);
        isb.addString("&z=");
        isb.addInt(tile.getLevel()+1);
        isb.addString("&x=");
        isb.addInt(tile.getColumn());
        isb.addString("&y=");
        isb.addInt(tile.getRow());
    
        ILogger.instance().logInfo("%s", isb.getString());
    
        petitions.add(new Petition(sector, new URL(isb.getString(), false), _timeToCache));
    
        return petitions;
    }
    public final URL getFeatureInfoURL(Geodetic2D g, IFactory factory, Sector tileSector, int width, int height)
    {
        return URL.nullURL();
    
    }




}