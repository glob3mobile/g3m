//
//  UtilityNetworkParser.hpp
//  EIFER App
//
//  Created by Chano on 30/11/17.
//
//

#ifndef UtilityNetworkParser_hpp
#define UtilityNetworkParser_hpp

class G3MContext;
class URL;
class IThreadUtils;

#import <G3MiOSSDK/MeshRenderer.hpp>
#import <G3MiOSSDK/Cylinder.hpp>
#import <G3MiOSSDK/Planet.hpp>
#import <G3MiOSSDK/ElevationData.hpp>
#import <G3MiOSSDK/IDownloader.hpp>


class UtilityNetworkParser {
private:
    static const Planet *_planet;
    static IDownloader *_downloader;
    static const ElevationData *_elevationData;
    static MeshRenderer *_mr;
    static double _hOffset;
    static const IThreadUtils *_tUtils;

    
    //static void processPipeString(const std::string &pipeList);
    static void processPipeString(const std::string &pipeList, int theId, std::string iMat, std::string eMat, double iDiam, double eDiam, std::string pClass, std::string pType, bool isT, bool isC);
    
public:
    static void initialize (const G3MContext *context,
                            const ElevationData *elevationData,
                            MeshRenderer *mr,
                            double heightOffset);
    static void parseFromURL(URL &url);
    static void parseContent(const std::string &cityGMLString);
};

#endif /* UtilityNetworkParser_hpp */


