//
//  UtilityJSONParser.hpp
//  EIFER App
//
//  Created by Chano on 21/6/18.
//
//

#ifndef UtilityJSONParser_hpp
#define UtilityJSONParser_hpp

class G3MContext;
class URL;
class IThreadUtils;
class JSONArray;

#import <G3MiOSSDK/MeshRenderer.hpp>
#import <G3MiOSSDK/Cylinder.hpp>
#import <G3MiOSSDK/Planet.hpp>
#import <G3MiOSSDK/ElevationData.hpp>
#import <G3MiOSSDK/IDownloader.hpp>


class UtilityJSONParserTwo {
private:
    static const Planet *_planet;
    static IDownloader *_downloader;
    static const ElevationData *_elevationData;
    static MeshRenderer *_mr;
    static double _hOffset;
    static const IThreadUtils *_tUtils;
    
    //private static JSONArray correctHeightIfNecessary(JSONArray a){
    
    
public:
    static void initialize (const G3MContext *context,
                            const ElevationData *elevationData,
                            MeshRenderer *mr,
                            double heightOffset);
    static void parseFromURL(URL &url);
    static void correctHeightIfNecessary(const JSONArray *a);
};

#endif /* UtilityJSONParser_hpp */
