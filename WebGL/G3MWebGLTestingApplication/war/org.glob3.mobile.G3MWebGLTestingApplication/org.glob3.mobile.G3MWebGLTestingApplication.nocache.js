function org_glob3_mobile_G3MWebGLTestingApplication(){
  var $intern_0 = 'bootstrap', $intern_1 = 'begin', $intern_2 = 'gwt.codesvr.org.glob3.mobile.G3MWebGLTestingApplication=', $intern_3 = 'gwt.codesvr=', $intern_4 = 'org.glob3.mobile.G3MWebGLTestingApplication', $intern_5 = 'startup', $intern_6 = 'DUMMY', $intern_7 = 0, $intern_8 = 1, $intern_9 = 'iframe', $intern_10 = 'javascript:""', $intern_11 = 'position:absolute; width:0; height:0; border:none; left: -1000px;', $intern_12 = ' top: -1000px;', $intern_13 = 'CSS1Compat', $intern_14 = '<!doctype html>', $intern_15 = '', $intern_16 = '<html><head><\/head><body><\/body><\/html>', $intern_17 = 'undefined', $intern_18 = 'DOMContentLoaded', $intern_19 = 50, $intern_20 = 'script', $intern_21 = 'javascript', $intern_22 = 'org_glob3_mobile_G3MWebGLTestingApplication', $intern_23 = 'Failed to load ', $intern_24 = 'moduleStartup', $intern_25 = 'scriptTagAdded', $intern_26 = 'moduleRequested', $intern_27 = 'meta', $intern_28 = 'name', $intern_29 = 'org.glob3.mobile.G3MWebGLTestingApplication::', $intern_30 = '::', $intern_31 = 'gwt:property', $intern_32 = 'content', $intern_33 = '=', $intern_34 = 'gwt:onPropertyErrorFn', $intern_35 = 'Bad handler "', $intern_36 = '" for "gwt:onPropertyErrorFn"', $intern_37 = 'gwt:onLoadErrorFn', $intern_38 = '" for "gwt:onLoadErrorFn"', $intern_39 = '#', $intern_40 = '?', $intern_41 = '/', $intern_42 = 'img', $intern_43 = 'clear.cache.gif', $intern_44 = 'baseUrl', $intern_45 = 'org.glob3.mobile.G3MWebGLTestingApplication.nocache.js', $intern_46 = 'base', $intern_47 = '//', $intern_48 = 'user.agent', $intern_49 = 'webkit', $intern_50 = 'safari', $intern_51 = 'msie', $intern_52 = 10, $intern_53 = 11, $intern_54 = 'ie10', $intern_55 = 9, $intern_56 = 'ie9', $intern_57 = 8, $intern_58 = 'ie8', $intern_59 = 'gecko', $intern_60 = 'gecko1_8', $intern_61 = 2, $intern_62 = 3, $intern_63 = 4, $intern_64 = 'selectingPermutation', $intern_65 = 'org.glob3.mobile.G3MWebGLTestingApplication.devmode.js', $intern_66 = 'E89E9D41643F4E4D28EF842C6FB2FC26', $intern_67 = ':1', $intern_68 = ':2', $intern_69 = ':3', $intern_70 = ':4', $intern_71 = ':', $intern_72 = '.cache.js', $intern_73 = 'loadExternalRefs', $intern_74 = 'end', $intern_75 = 'http:', $intern_76 = 'file:', $intern_77 = '_gwt_dummy_', $intern_78 = '__gwtDevModeHook:org.glob3.mobile.G3MWebGLTestingApplication', $intern_79 = 'Ignoring non-whitelisted Dev Mode URL: ', $intern_80 = ':moduleBase', $intern_81 = 'head';
  var $wnd = window;
  var $doc = document;
  sendStats($intern_0, $intern_1);
  function isHostedMode(){
    var query = $wnd.location.search;
    return query.indexOf($intern_2) != -1 || query.indexOf($intern_3) != -1;
  }

  function sendStats(evtGroupString, typeString){
    if ($wnd.__gwtStatsEvent) {
      $wnd.__gwtStatsEvent({moduleName:$intern_4, sessionId:$wnd.__gwtStatsSessionId, subSystem:$intern_5, evtGroup:evtGroupString, millis:(new Date).getTime(), type:typeString});
    }
  }

  org_glob3_mobile_G3MWebGLTestingApplication.__sendStats = sendStats;
  org_glob3_mobile_G3MWebGLTestingApplication.__moduleName = $intern_4;
  org_glob3_mobile_G3MWebGLTestingApplication.__errFn = null;
  org_glob3_mobile_G3MWebGLTestingApplication.__moduleBase = $intern_6;
  org_glob3_mobile_G3MWebGLTestingApplication.__softPermutationId = $intern_7;
  org_glob3_mobile_G3MWebGLTestingApplication.__computePropValue = null;
  org_glob3_mobile_G3MWebGLTestingApplication.__getPropMap = null;
  org_glob3_mobile_G3MWebGLTestingApplication.__installRunAsyncCode = function(){
  }
  ;
  org_glob3_mobile_G3MWebGLTestingApplication.__gwtStartLoadingFragment = function(){
    return null;
  }
  ;
  org_glob3_mobile_G3MWebGLTestingApplication.__gwt_isKnownPropertyValue = function(){
    return false;
  }
  ;
  org_glob3_mobile_G3MWebGLTestingApplication.__gwt_getMetaProperty = function(){
    return null;
  }
  ;
  var __propertyErrorFunction = null;
  var activeModules = $wnd.__gwt_activeModules = $wnd.__gwt_activeModules || {};
  activeModules[$intern_4] = {moduleName:$intern_4};
  org_glob3_mobile_G3MWebGLTestingApplication.__moduleStartupDone = function(permProps){
    var oldBindings = activeModules[$intern_4].bindings;
    activeModules[$intern_4].bindings = function(){
      var props = oldBindings?oldBindings():{};
      var embeddedProps = permProps[org_glob3_mobile_G3MWebGLTestingApplication.__softPermutationId];
      for (var i = $intern_7; i < embeddedProps.length; i++) {
        var pair = embeddedProps[i];
        props[pair[$intern_7]] = pair[$intern_8];
      }
      return props;
    }
    ;
  }
  ;
  var frameDoc;
  function getInstallLocationDoc(){
    setupInstallLocation();
    return frameDoc;
  }

  function setupInstallLocation(){
    if (frameDoc) {
      return;
    }
    var scriptFrame = $doc.createElement($intern_9);
    scriptFrame.src = $intern_10;
    scriptFrame.id = $intern_4;
    scriptFrame.style.cssText = $intern_11 + $intern_12;
    scriptFrame.tabIndex = -1;
    $doc.body.appendChild(scriptFrame);
    frameDoc = scriptFrame.contentDocument;
    if (!frameDoc) {
      frameDoc = scriptFrame.contentWindow.document;
    }
    frameDoc.open();
    var doctype = document.compatMode == $intern_13?$intern_14:$intern_15;
    frameDoc.write(doctype + $intern_16);
    frameDoc.close();
  }

  function installScript(filename){
    function setupWaitForBodyLoad(callback){
      function isBodyLoaded(){
        if (typeof $doc.readyState == $intern_17) {
          return typeof $doc.body != $intern_17 && $doc.body != null;
        }
        return /loaded|complete/.test($doc.readyState);
      }

      var bodyDone = isBodyLoaded();
      if (bodyDone) {
        callback();
        return;
      }
      function onBodyDone(){
        if (!bodyDone) {
          bodyDone = true;
          callback();
          if ($doc.removeEventListener) {
            $doc.removeEventListener($intern_18, onBodyDone, false);
          }
          if (onBodyDoneTimerId) {
            clearInterval(onBodyDoneTimerId);
          }
        }
      }

      if ($doc.addEventListener) {
        $doc.addEventListener($intern_18, onBodyDone, false);
      }
      var onBodyDoneTimerId = setInterval(function(){
        if (isBodyLoaded()) {
          onBodyDone();
        }
      }
      , $intern_19);
    }

    function installCode(code_0){
      var doc = getInstallLocationDoc();
      var docbody = doc.body;
      var script = doc.createElement($intern_20);
      script.language = $intern_21;
      script.src = code_0;
      if (org_glob3_mobile_G3MWebGLTestingApplication.__errFn) {
        script.onerror = function(){
          org_glob3_mobile_G3MWebGLTestingApplication.__errFn($intern_22, new Error($intern_23 + code_0));
        }
        ;
      }
      docbody.appendChild(script);
      sendStats($intern_24, $intern_25);
    }

    sendStats($intern_24, $intern_26);
    setupWaitForBodyLoad(function(){
      installCode(filename);
    }
    );
  }

  org_glob3_mobile_G3MWebGLTestingApplication.__startLoadingFragment = function(fragmentFile){
    return computeUrlForResource(fragmentFile);
  }
  ;
  org_glob3_mobile_G3MWebGLTestingApplication.__installRunAsyncCode = function(code_0){
    var doc = getInstallLocationDoc();
    var docbody = doc.body;
    var script = doc.createElement($intern_20);
    script.language = $intern_21;
    script.text = code_0;
    docbody.appendChild(script);
  }
  ;
  function processMetas(){
    var metaProps = {};
    var propertyErrorFunc;
    var onLoadErrorFunc;
    var metas = $doc.getElementsByTagName($intern_27);
    for (var i = $intern_7, n = metas.length; i < n; ++i) {
      var meta = metas[i], name_0 = meta.getAttribute($intern_28), content;
      if (name_0) {
        name_0 = name_0.replace($intern_29, $intern_15);
        if (name_0.indexOf($intern_30) >= $intern_7) {
          continue;
        }
        if (name_0 == $intern_31) {
          content = meta.getAttribute($intern_32);
          if (content) {
            var value_0, eq = content.indexOf($intern_33);
            if (eq >= $intern_7) {
              name_0 = content.substring($intern_7, eq);
              value_0 = content.substring(eq + $intern_8);
            }
             else {
              name_0 = content;
              value_0 = $intern_15;
            }
            metaProps[name_0] = value_0;
          }
        }
         else if (name_0 == $intern_34) {
          content = meta.getAttribute($intern_32);
          if (content) {
            try {
              propertyErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_35 + content + $intern_36);
            }
          }
        }
         else if (name_0 == $intern_37) {
          content = meta.getAttribute($intern_32);
          if (content) {
            try {
              onLoadErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_35 + content + $intern_38);
            }
          }
        }
      }
    }
    __gwt_getMetaProperty = function(name_0){
      var value_0 = metaProps[name_0];
      return value_0 == null?null:value_0;
    }
    ;
    __propertyErrorFunction = propertyErrorFunc;
    org_glob3_mobile_G3MWebGLTestingApplication.__errFn = onLoadErrorFunc;
  }

  function computeScriptBase(){
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf($intern_39);
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf($intern_40);
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf($intern_41, Math.min(queryIndex, hashIndex));
      return slashIndex >= $intern_7?path.substring($intern_7, slashIndex + $intern_8):$intern_15;
    }

    function ensureAbsoluteUrl(url_0){
      if (url_0.match(/^\w+:\/\//)) {
      }
       else {
        var img = $doc.createElement($intern_42);
        img.src = url_0 + $intern_43;
        url_0 = getDirectoryOfFile(img.src);
      }
      return url_0;
    }

    function tryMetaTag(){
      var metaVal = __gwt_getMetaProperty($intern_44);
      if (metaVal != null) {
        return metaVal;
      }
      return $intern_15;
    }

    function tryNocacheJsTag(){
      var scriptTags = $doc.getElementsByTagName($intern_20);
      for (var i = $intern_7; i < scriptTags.length; ++i) {
        if (scriptTags[i].src.indexOf($intern_45) != -1) {
          return getDirectoryOfFile(scriptTags[i].src);
        }
      }
      return $intern_15;
    }

    function tryBaseTag(){
      var baseElements = $doc.getElementsByTagName($intern_46);
      if (baseElements.length > $intern_7) {
        return baseElements[baseElements.length - $intern_8].href;
      }
      return $intern_15;
    }

    function isLocationOk(){
      var loc = $doc.location;
      return loc.href == loc.protocol + $intern_47 + loc.host + loc.pathname + loc.search + loc.hash;
    }

    var tempBase = tryMetaTag();
    if (tempBase == $intern_15) {
      tempBase = tryNocacheJsTag();
    }
    if (tempBase == $intern_15) {
      tempBase = tryBaseTag();
    }
    if (tempBase == $intern_15 && isLocationOk()) {
      tempBase = getDirectoryOfFile($doc.location.href);
    }
    tempBase = ensureAbsoluteUrl(tempBase);
    return tempBase;
  }

  function computeUrlForResource(resource){
    if (resource.match(/^\//)) {
      return resource;
    }
    if (resource.match(/^[a-zA-Z]+:\/\//)) {
      return resource;
    }
    return org_glob3_mobile_G3MWebGLTestingApplication.__moduleBase + resource;
  }

  function getCompiledCodeFilename(){
    var answers = [];
    var softPermutationId = $intern_7;
    function unflattenKeylistIntoAnswers(propValArray, value_0){
      var answer = answers;
      for (var i = $intern_7, n = propValArray.length - $intern_8; i < n; ++i) {
        answer = answer[propValArray[i]] || (answer[propValArray[i]] = []);
      }
      answer[propValArray[n]] = value_0;
    }

    var values = [];
    var providers = [];
    function computePropValue(propName){
      var value_0 = providers[propName](), allowedValuesMap = values[propName];
      if (value_0 in allowedValuesMap) {
        return value_0;
      }
      var allowedValuesList = [];
      for (var k in allowedValuesMap) {
        allowedValuesList[allowedValuesMap[k]] = k;
      }
      if (__propertyErrorFunction) {
        __propertyErrorFunction(propName, allowedValuesList, value_0);
      }
      throw null;
    }

    providers[$intern_48] = function(){
      var ua = navigator.userAgent.toLowerCase();
      var docMode = $doc.documentMode;
      if (function(){
        return ua.indexOf($intern_49) != -1;
      }
      ())
        return $intern_50;
      if (function(){
        return ua.indexOf($intern_51) != -1 && (docMode >= $intern_52 && docMode < $intern_53);
      }
      ())
        return $intern_54;
      if (function(){
        return ua.indexOf($intern_51) != -1 && (docMode >= $intern_55 && docMode < $intern_53);
      }
      ())
        return $intern_56;
      if (function(){
        return ua.indexOf($intern_51) != -1 && (docMode >= $intern_57 && docMode < $intern_53);
      }
      ())
        return $intern_58;
      if (function(){
        return ua.indexOf($intern_59) != -1 || docMode >= $intern_53;
      }
      ())
        return $intern_60;
      return $intern_15;
    }
    ;
    values[$intern_48] = {gecko1_8:$intern_7, ie10:$intern_8, ie8:$intern_61, ie9:$intern_62, safari:$intern_63};
    __gwt_isKnownPropertyValue = function(propName, propValue){
      return propValue in values[propName];
    }
    ;
    org_glob3_mobile_G3MWebGLTestingApplication.__getPropMap = function(){
      var result = {};
      for (var key in values) {
        if (values.hasOwnProperty(key)) {
          result[key] = computePropValue(key);
        }
      }
      return result;
    }
    ;
    org_glob3_mobile_G3MWebGLTestingApplication.__computePropValue = computePropValue;
    $wnd.__gwt_activeModules[$intern_4].bindings = org_glob3_mobile_G3MWebGLTestingApplication.__getPropMap;
    sendStats($intern_0, $intern_64);
    if (isHostedMode()) {
      return computeUrlForResource($intern_65);
    }
    var strongName;
    try {
      unflattenKeylistIntoAnswers([$intern_60], $intern_66);
      unflattenKeylistIntoAnswers([$intern_54], $intern_66 + $intern_67);
      unflattenKeylistIntoAnswers([$intern_58], $intern_66 + $intern_68);
      unflattenKeylistIntoAnswers([$intern_56], $intern_66 + $intern_69);
      unflattenKeylistIntoAnswers([$intern_50], $intern_66 + $intern_70);
      strongName = answers[computePropValue($intern_48)];
      var idx = strongName.indexOf($intern_71);
      if (idx != -1) {
        softPermutationId = parseInt(strongName.substring(idx + $intern_8), $intern_52);
        strongName = strongName.substring($intern_7, idx);
      }
    }
     catch (e) {
    }
    org_glob3_mobile_G3MWebGLTestingApplication.__softPermutationId = softPermutationId;
    return computeUrlForResource(strongName + $intern_72);
  }

  function loadExternalStylesheets(){
    if (!$wnd.__gwt_stylesLoaded) {
      $wnd.__gwt_stylesLoaded = {};
    }
    sendStats($intern_73, $intern_1);
    sendStats($intern_73, $intern_74);
  }

  processMetas();
  org_glob3_mobile_G3MWebGLTestingApplication.__moduleBase = computeScriptBase();
  activeModules[$intern_4].moduleBase = org_glob3_mobile_G3MWebGLTestingApplication.__moduleBase;
  var filename = getCompiledCodeFilename();
  if ($wnd) {
    var devModePermitted = !!($wnd.location.protocol == $intern_75 || $wnd.location.protocol == $intern_76);
    $wnd.__gwt_activeModules[$intern_4].canRedirect = devModePermitted;
    function supportsSessionStorage(){
      var key = $intern_77;
      try {
        $wnd.sessionStorage.setItem(key, key);
        $wnd.sessionStorage.removeItem(key);
        return true;
      }
       catch (e) {
        return false;
      }
    }

    if (devModePermitted && supportsSessionStorage()) {
      var devModeKey = $intern_78;
      var devModeUrl = $wnd.sessionStorage[devModeKey];
      if (!/^http:\/\/(localhost|127\.0\.0\.1)(:\d+)?\/.*$/.test(devModeUrl)) {
        if (devModeUrl && (window.console && console.log)) {
          console.log($intern_79 + devModeUrl);
        }
        devModeUrl = $intern_15;
      }
      if (devModeUrl && !$wnd[devModeKey]) {
        $wnd[devModeKey] = true;
        $wnd[devModeKey + $intern_80] = computeScriptBase();
        var devModeScript = $doc.createElement($intern_20);
        devModeScript.src = devModeUrl;
        var head = $doc.getElementsByTagName($intern_81)[$intern_7];
        head.insertBefore(devModeScript, head.firstElementChild || head.children[$intern_7]);
        return false;
      }
    }
  }
  loadExternalStylesheets();
  sendStats($intern_0, $intern_74);
  installScript(filename);
  return true;
}

org_glob3_mobile_G3MWebGLTestingApplication.succeeded = org_glob3_mobile_G3MWebGLTestingApplication();
