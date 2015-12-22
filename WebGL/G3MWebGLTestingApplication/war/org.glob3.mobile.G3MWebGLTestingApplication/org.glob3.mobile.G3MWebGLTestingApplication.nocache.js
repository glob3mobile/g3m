function org_glob3_mobile_G3MWebGLTestingApplication(){
  var $wnd_0 = window;
  var $doc_0 = document;
  sendStats('bootstrap', 'begin');
  function isHostedMode(){
    var query = $wnd_0.location.search;
    return query.indexOf('gwt.codesvr.org.glob3.mobile.G3MWebGLTestingApplication=') != -1 || query.indexOf('gwt.codesvr=') != -1;
  }

  function sendStats(evtGroupString, typeString){
    if ($wnd_0.__gwtStatsEvent) {
      $wnd_0.__gwtStatsEvent({moduleName:'org.glob3.mobile.G3MWebGLTestingApplication', sessionId:$wnd_0.__gwtStatsSessionId, subSystem:'startup', evtGroup:evtGroupString, millis:(new Date).getTime(), type:typeString});
    }
  }

  org_glob3_mobile_G3MWebGLTestingApplication.__sendStats = sendStats;
  org_glob3_mobile_G3MWebGLTestingApplication.__moduleName = 'org.glob3.mobile.G3MWebGLTestingApplication';
  org_glob3_mobile_G3MWebGLTestingApplication.__errFn = null;
  org_glob3_mobile_G3MWebGLTestingApplication.__moduleBase = 'DUMMY';
  org_glob3_mobile_G3MWebGLTestingApplication.__softPermutationId = 0;
  org_glob3_mobile_G3MWebGLTestingApplication.__computePropValue = null;
  org_glob3_mobile_G3MWebGLTestingApplication.__getPropMap = null;
  org_glob3_mobile_G3MWebGLTestingApplication.__gwtInstallCode = function(){
  }
  ;
  org_glob3_mobile_G3MWebGLTestingApplication.__gwtStartLoadingFragment = function(){
    return null;
  }
  ;
  var __gwt_isKnownPropertyValue = function(){
    return false;
  }
  ;
  var __gwt_getMetaProperty = function(){
    return null;
  }
  ;
  __propertyErrorFunction = null;
  var activeModules = $wnd_0.__gwt_activeModules = $wnd_0.__gwt_activeModules || {};
  activeModules['org.glob3.mobile.G3MWebGLTestingApplication'] = {moduleName:'org.glob3.mobile.G3MWebGLTestingApplication'};
  var frameDoc;
  function getInstallLocationDoc(){
    setupInstallLocation();
    return frameDoc;
  }

  function getInstallLocation(){
    setupInstallLocation();
    return frameDoc.getElementsByTagName('body')[0];
  }

  function setupInstallLocation(){
    if (frameDoc) {
      return;
    }
    var scriptFrame = $doc_0.createElement('iframe');
    scriptFrame.src = 'javascript:""';
    scriptFrame.id = 'org.glob3.mobile.G3MWebGLTestingApplication';
    scriptFrame.style.cssText = 'position:absolute; width:0; height:0; border:none; left: -1000px;' + ' top: -1000px;';
    scriptFrame.tabIndex = -1;
    $doc_0.body.appendChild(scriptFrame);
    frameDoc = scriptFrame.contentDocument;
    if (!frameDoc) {
      frameDoc = scriptFrame.contentWindow.document;
    }
    frameDoc.open();
    var doctype = document.compatMode == 'CSS1Compat'?'<!doctype html>':'';
    frameDoc.write(doctype + '<html><head><\/head><body><\/body><\/html>');
    frameDoc.close();
  }

  function installScript(filename){
    function setupWaitForBodyLoad(callback){
      function isBodyLoaded(){
        if (typeof $doc_0.readyState == 'undefined') {
          return typeof $doc_0.body != 'undefined' && $doc_0.body != null;
        }
        return /loaded|complete/.test($doc_0.readyState);
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
          if ($doc_0.removeEventListener) {
            $doc_0.removeEventListener('DOMContentLoaded', onBodyDone, false);
          }
          if (onBodyDoneTimerId) {
            clearInterval(onBodyDoneTimerId);
          }
        }
      }

      if ($doc_0.addEventListener) {
        $doc_0.addEventListener('DOMContentLoaded', onBodyDone, false);
      }
      var onBodyDoneTimerId = setInterval(function(){
        if (isBodyLoaded()) {
          onBodyDone();
        }
      }
      , 50);
    }

    function installCode(code_0){
      function removeScript(body_0, element){
      }

      var docbody = getInstallLocation();
      var doc = getInstallLocationDoc();
      var script;
      if (navigator.userAgent.indexOf('Chrome') > -1 && window.JSON) {
        var scriptFrag = doc.createDocumentFragment();
        scriptFrag.appendChild(doc.createTextNode('eval("'));
        for (var i = 0; i < code_0.length; i++) {
          var c = window.JSON.stringify(code_0[i]);
          scriptFrag.appendChild(doc.createTextNode(c.substring(1, c.length - 1)));
        }
        scriptFrag.appendChild(doc.createTextNode('");'));
        script = doc.createElement('script');
        script.language = 'javascript';
        script.appendChild(scriptFrag);
        docbody.appendChild(script);
        removeScript(docbody, script);
      }
       else {
        for (var i = 0; i < code_0.length; i++) {
          script = doc.createElement('script');
          script.language = 'javascript';
          script.text = code_0[i];
          docbody.appendChild(script);
          removeScript(docbody, script);
        }
      }
    }

    org_glob3_mobile_G3MWebGLTestingApplication.onScriptDownloaded = function(code_0){
      setupWaitForBodyLoad(function(){
        installCode(code_0);
      }
      );
    }
    ;
    sendStats('moduleStartup', 'moduleRequested');
    var script_0 = $doc_0.createElement('script');
    script_0.src = filename;
    $doc_0.getElementsByTagName('head')[0].appendChild(script_0);
  }

  org_glob3_mobile_G3MWebGLTestingApplication.__startLoadingFragment = function(fragmentFile){
    return computeUrlForResource(fragmentFile);
  }
  ;
  org_glob3_mobile_G3MWebGLTestingApplication.__installRunAsyncCode = function(code_0){
    var docbody = getInstallLocation();
    var script = getInstallLocationDoc().createElement('script');
    script.language = 'javascript';
    script.text = code_0;
    docbody.appendChild(script);
  }
  ;
  function processMetas(){
    var metaProps = {};
    var propertyErrorFunc;
    var onLoadErrorFunc;
    var metas = $doc_0.getElementsByTagName('meta');
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name_1 = meta.getAttribute('name'), content_0;
      if (name_1) {
        name_1 = name_1.replace('org.glob3.mobile.G3MWebGLTestingApplication::', '');
        if (name_1.indexOf('::') >= 0) {
          continue;
        }
        if (name_1 == 'gwt:property') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            var value_1, eq = content_0.indexOf('=');
            if (eq >= 0) {
              name_1 = content_0.substring(0, eq);
              value_1 = content_0.substring(eq + 1);
            }
             else {
              name_1 = content_0;
              value_1 = '';
            }
            metaProps[name_1] = value_1;
          }
        }
         else if (name_1 == 'gwt:onPropertyErrorFn') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            try {
              propertyErrorFunc = eval(content_0);
            }
             catch (e) {
              alert('Bad handler "' + content_0 + '" for "gwt:onPropertyErrorFn"');
            }
          }
        }
         else if (name_1 == 'gwt:onLoadErrorFn') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            try {
              onLoadErrorFunc = eval(content_0);
            }
             catch (e) {
              alert('Bad handler "' + content_0 + '" for "gwt:onLoadErrorFn"');
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
      var hashIndex = path.lastIndexOf('#');
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf('?');
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf('/', Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):'';
    }

    function ensureAbsoluteUrl(url_0){
      if (url_0.match(/^\w+:\/\//)) {
      }
       else {
        var img = $doc_0.createElement('img');
        img.src = url_0 + 'clear.cache.gif';
        url_0 = getDirectoryOfFile(img.src);
      }
      return url_0;
    }

    function tryMetaTag(){
      var metaVal = __gwt_getMetaProperty('baseUrl');
      if (metaVal != null) {
        return metaVal;
      }
      return '';
    }

    function tryNocacheJsTag(){
      var scriptTags = $doc_0.getElementsByTagName('script');
      for (var i = 0; i < scriptTags.length; ++i) {
        if (scriptTags[i].src.indexOf('org.glob3.mobile.G3MWebGLTestingApplication.nocache.js') != -1) {
          return getDirectoryOfFile(scriptTags[i].src);
        }
      }
      return '';
    }

    function tryBaseTag(){
      var baseElements = $doc_0.getElementsByTagName('base');
      if (baseElements.length > 0) {
        return baseElements[baseElements.length - 1].href;
      }
      return '';
    }

    function isLocationOk(){
      var loc = $doc_0.location;
      return loc.href == loc.protocol + '//' + loc.host + loc.pathname + loc.search + loc.hash;
    }

    var tempBase = tryMetaTag();
    if (tempBase == '') {
      tempBase = tryNocacheJsTag();
    }
    if (tempBase == '') {
      tempBase = tryBaseTag();
    }
    if (tempBase == '' && isLocationOk()) {
      tempBase = getDirectoryOfFile($doc_0.location.href);
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
    var softPermutationId;
    function unflattenKeylistIntoAnswers(propValArray, value_0){
      var answer = answers;
      for (var i = 0, n = propValArray.length - 1; i < n; ++i) {
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
      if (__propertyErrorFunc) {
        __propertyErrorFunc(propName, allowedValuesList, value_0);
      }
      throw null;
    }

    providers['user.agent'] = function(){
      var ua = navigator.userAgent.toLowerCase();
      var makeVersion = function(result){
        return parseInt(result[1]) * 1000 + parseInt(result[2]);
      }
      ;
      if (function(){
        return ua.indexOf('webkit') != -1;
      }
      ())
        return 'safari';
      if (function(){
        return ua.indexOf('msie') != -1 && $doc_0.documentMode >= 10;
      }
      ())
        return 'ie10';
      if (function(){
        return ua.indexOf('msie') != -1 && $doc_0.documentMode >= 9;
      }
      ())
        return 'ie9';
      if (function(){
        return ua.indexOf('msie') != -1 && $doc_0.documentMode >= 8;
      }
      ())
        return 'ie8';
      if (function(){
        return ua.indexOf('gecko') != -1;
      }
      ())
        return 'gecko1_8';
      return 'unknown';
    }
    ;
    values['user.agent'] = {gecko1_8:0, ie10:1, ie8:2, ie9:3, safari:4};
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
    $wnd_0.__gwt_activeModules['org.glob3.mobile.G3MWebGLTestingApplication'].bindings = org_glob3_mobile_G3MWebGLTestingApplication.__getPropMap;
    sendStats('bootstrap', 'selectingPermutation');
    if (isHostedMode()) {
      return computeUrlForResource('org.glob3.mobile.G3MWebGLTestingApplication.devmode.js');
    }
    var strongName;
    try {
      unflattenKeylistIntoAnswers(['gecko1_8'], 'A16FFF3CE11C988F1DBED734424D986D');
      unflattenKeylistIntoAnswers(['ie10'], 'A16FFF3CE11C988F1DBED734424D986D' + ':1');
      unflattenKeylistIntoAnswers(['ie8'], 'A16FFF3CE11C988F1DBED734424D986D' + ':2');
      unflattenKeylistIntoAnswers(['ie9'], 'A16FFF3CE11C988F1DBED734424D986D' + ':3');
      unflattenKeylistIntoAnswers(['safari'], 'A16FFF3CE11C988F1DBED734424D986D' + ':4');
      strongName = answers[computePropValue('user.agent')];
      var idx = strongName.indexOf(':');
      if (idx != -1) {
        softPermutationId = parseInt(strongName.substring(idx + 1), 10);
        strongName = strongName.substring(0, idx);
      }
    }
     catch (e) {
    }
    org_glob3_mobile_G3MWebGLTestingApplication.__softPermutationId = softPermutationId;
    return computeUrlForResource(strongName + '.cache.js');
  }

  function loadExternalStylesheets(){
    if (!$wnd_0.__gwt_stylesLoaded) {
      $wnd_0.__gwt_stylesLoaded = {};
    }
    sendStats('loadExternalRefs', 'begin');
    sendStats('loadExternalRefs', 'end');
  }

  processMetas();
  org_glob3_mobile_G3MWebGLTestingApplication.__moduleBase = computeScriptBase();
  activeModules['org.glob3.mobile.G3MWebGLTestingApplication'].moduleBase = org_glob3_mobile_G3MWebGLTestingApplication.__moduleBase;
  var filename_0 = getCompiledCodeFilename();
  if ($wnd_0) {
    var devModePermitted = !!($wnd_0.location.protocol == 'http:' || $wnd_0.location.protocol == 'file:');
    $wnd_0.__gwt_activeModules['org.glob3.mobile.G3MWebGLTestingApplication'].canRedirect = devModePermitted;
    if (devModePermitted) {
      var devModeKey = '__gwtDevModeHook:org.glob3.mobile.G3MWebGLTestingApplication';
      var devModeUrl = $wnd_0.sessionStorage[devModeKey];
      if (!/^http:\/\/(localhost|127\.0\.0\.1)(:\d+)?\/.*$/.test(devModeUrl)) {
        if (devModeUrl && (window.console && console.log)) {
          console.log('Ignoring non-whitelisted Dev Mode URL: ' + devModeUrl);
        }
        devModeUrl = '';
      }
      if (devModeUrl && !$wnd_0[devModeKey]) {
        $wnd_0[devModeKey] = true;
        $wnd_0[devModeKey + ':moduleBase'] = computeScriptBase();
        var devModeScript = $doc_0.createElement('script');
        devModeScript.src = devModeUrl;
        var head = $doc_0.getElementsByTagName('head')[0];
        head.insertBefore(devModeScript, head.firstElementChild || head.children[0]);
        return false;
      }
    }
  }
  loadExternalStylesheets();
  sendStats('bootstrap', 'end');
  installScript(filename_0);
  return true;
}

org_glob3_mobile_G3MWebGLTestingApplication.succeeded = org_glob3_mobile_G3MWebGLTestingApplication();
