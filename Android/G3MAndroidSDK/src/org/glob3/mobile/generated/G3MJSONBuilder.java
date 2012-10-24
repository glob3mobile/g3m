package org.glob3.mobile.generated; 
//
//  G3MJSONBuilder.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 22/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  G3MJSONBuilder.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 22/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class G3MJSONBuilder
{

	public G3MJSONBuilder(CompositeRenderer composite, LayerSet layerSet, TilesRenderParameters parameters, java.util.ArrayList<Renderer> renderers, IDownloader downloader, URL jsonURL)
	{
		downloader.requestBuffer(jsonURL, 100000000, new SceneJSONDownloadListener(composite, layerSet, parameters, renderers, this), true);
	}

	public G3MJSONBuilder(CompositeRenderer composite, LayerSet layerSet, TilesRenderParameters parameters, java.util.ArrayList<Renderer> renderers, String jsonSource)
	{
		fromSceneJSON(jsonSource, composite, layerSet, parameters, renderers);
	}

	public final void fromSceneJSON(String json, CompositeRenderer composite, LayerSet layerSet, TilesRenderParameters parameters, java.util.ArrayList<Renderer> renderers)
	{
    
		SceneParser.instance().parse(layerSet, json);
    
		if (layerSet != null)
		{
			TileTexturizer texturizer = new MultiLayerTileTexturizer();
    
			final boolean showStatistics = false;
			TileRenderer tr = new TileRenderer(new EllipsoidalTileTessellator(parameters._tileResolution, true), texturizer, layerSet, parameters, showStatistics);
			composite.addRenderer(tr);
		}
	}

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//	public void dispose()

}