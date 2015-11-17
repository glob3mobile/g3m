function org_glob3_mobile_G3MWebGLTestingApplication(){
  var $wnd_0 = window, $doc_0 = document, gwtOnLoad_0, bodyDone, base = '', metaProps = {}, values = [], providers = [], answers = [], softPermutationId = 0, onLoadErrorFunc, propertyErrorFunc;
  if (!$wnd_0.__gwt_stylesLoaded) {
    $wnd_0.__gwt_stylesLoaded = {};
  }
  if (!$wnd_0.__gwt_scriptsLoaded) {
    $wnd_0.__gwt_scriptsLoaded = {};
  }
  function isHostedMode(){
    var result = false;
    try {
      var query = $wnd_0.location.search;
      return (query.indexOf('gwt.codesvr=') != -1 || (query.indexOf('gwt.hosted=') != -1 || $wnd_0.external && $wnd_0.external.gwtOnLoad)) && query.indexOf('gwt.hybrid') == -1;
    }
     catch (e) {
    }
    isHostedMode = function(){
      return result;
    }
    ;
    return result;
  }

  function maybeStartModule(){
    if (gwtOnLoad_0 && bodyDone) {
      gwtOnLoad_0(onLoadErrorFunc, 'org.glob3.mobile.G3MWebGLTestingApplication', base, softPermutationId);
    }
  }

  function computeScriptBase(){
    var thisScript, markerId = '__gwt_marker_org.glob3.mobile.G3MWebGLTestingApplication', markerScript;
    $doc_0.write('<script id="' + markerId + '"><\/script>');
    markerScript = $doc_0.getElementById(markerId);
    thisScript = markerScript && markerScript.previousSibling;
    while (thisScript && thisScript.tagName != 'SCRIPT') {
      thisScript = thisScript.previousSibling;
    }
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

    ;
    if (thisScript && thisScript.src) {
      base = getDirectoryOfFile(thisScript.src);
    }
    if (base == '') {
      var baseElements = $doc_0.getElementsByTagName('base');
      if (baseElements.length > 0) {
        base = baseElements[baseElements.length - 1].href;
      }
       else {
        base = getDirectoryOfFile($doc_0.location.href);
      }
    }
     else if (base.match(/^\w+:\/\//)) {
    }
     else {
      var img = $doc_0.createElement('img');
      img.src = base + 'clear.cache.gif';
      base = getDirectoryOfFile(img.src);
    }
    if (markerScript) {
      markerScript.parentNode.removeChild(markerScript);
    }
  }

  function processMetas(){
    var metas = document.getElementsByTagName('meta');
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name_0 = meta.getAttribute('name'), content_0;
      if (name_0) {
        if (name_0 == 'gwt:property') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            var value_0, eq = content_0.indexOf('=');
            if (eq >= 0) {
              name_0 = content_0.substring(0, eq);
              value_0 = content_0.substring(eq + 1);
            }
             else {
              name_0 = content_0;
              value_0 = '';
            }
            metaProps[name_0] = value_0;
          }
        }
         else if (name_0 == 'gwt:onPropertyErrorFn') {
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
         else if (name_0 == 'gwt:onLoadErrorFn') {
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
  }

  __gwt_isKnownPropertyValue = function(propName, propValue){
    return propValue in values[propName];
  }
  ;
  __gwt_getMetaProperty = function(name_0){
    var value_0 = metaProps[name_0];
    return value_0 == null?null:value_0;
  }
  ;
  function unflattenKeylistIntoAnswers(propValArray, value_0){
    var answer = answers;
    for (var i = 0, n = propValArray.length - 1; i < n; ++i) {
      answer = answer[propValArray[i]] || (answer[propValArray[i]] = []);
    }
    answer[propValArray[n]] = value_0;
  }

  function computePropValue(propName){
    var value_0 = providers[propName](), allowedValuesMap = values[propName];
    if (value_0 in allowedValuesMap) {
      return value_0;
    }
    var allowedValuesList = [];
    for (var k in allowedValuesMap) {
      allowedValuesList[allowedValuesMap[k]] = k;
    }
    if (propertyErrorFunc) {
      propertyErrorFunc(propName, allowedValuesList, value_0);
    }
    throw null;
  }

  providers['user.agent'] = function(){
    var ua = navigator.userAgent.toLowerCase();
    var docMode = $doc_0.documentMode;
    if (function(){
      return ua.indexOf('webkit') != -1;
    }
    ())
      return 'safari';
    if (function(){
      return ua.indexOf('msie') != -1 && (docMode >= 10 && docMode < 11);
    }
    ())
      return 'ie10';
    if (function(){
      return ua.indexOf('msie') != -1 && (docMode >= 9 && docMode < 11);
    }
    ())
      return 'ie9';
    if (function(){
      return ua.indexOf('msie') != -1 && (docMode >= 8 && docMode < 11);
    }
    ())
      return 'ie8';
    if (function(){
      return ua.indexOf('gecko') != -1 || docMode >= 11;
    }
    ())
      return 'gecko1_8';
    return '';
  }
  ;
  values['user.agent'] = {gecko1_8:0, ie10:1, ie8:2, ie9:3, safari:4};
  org_glob3_mobile_G3MWebGLTestingApplication.onScriptLoad = function(gwtOnLoadFunc){
    org_glob3_mobile_G3MWebGLTestingApplication = null;
    gwtOnLoad_0 = gwtOnLoadFunc;
    maybeStartModule();
  }
  ;
  if (isHostedMode()) {
    alert('Single-script hosted mode not yet implemented. See issue ' + 'http://code.google.com/p/google-web-toolkit/issues/detail?id=2079');
    return;
  }
  computeScriptBase();
  processMetas();
  try {
    var strongName;
    unflattenKeylistIntoAnswers(['gecko1_8'], '3D8C6F7611D982CF3D64B47DB7E9AD5B');
    unflattenKeylistIntoAnswers(['ie10'], '3D8C6F7611D982CF3D64B47DB7E9AD5B' + ':1');
    unflattenKeylistIntoAnswers(['ie8'], '3D8C6F7611D982CF3D64B47DB7E9AD5B' + ':2');
    unflattenKeylistIntoAnswers(['ie9'], '3D8C6F7611D982CF3D64B47DB7E9AD5B' + ':3');
    unflattenKeylistIntoAnswers(['safari'], '3D8C6F7611D982CF3D64B47DB7E9AD5B' + ':4');
    strongName = answers[computePropValue('user.agent')];
    var idx = strongName.indexOf(':');
    if (idx != -1) {
      softPermutationId = Number(strongName.substring(idx + 1));
    }
  }
   catch (e) {
    return;
  }
  var onBodyDoneTimerId;
  function onBodyDone(){
    if (!bodyDone) {
      bodyDone = true;
      maybeStartModule();
      if ($doc_0.removeEventListener) {
        $doc_0.removeEventListener('DOMContentLoaded', onBodyDone, false);
      }
      if (onBodyDoneTimerId) {
        clearInterval(onBodyDoneTimerId);
      }
    }
  }

  if ($doc_0.addEventListener) {
    $doc_0.addEventListener('DOMContentLoaded', function(){
      onBodyDone();
    }
    , false);
  }
  var onBodyDoneTimerId = setInterval(function(){
    if (/loaded|complete/.test($doc_0.readyState)) {
      onBodyDone();
    }
  }
  , 50);
}

org_glob3_mobile_G3MWebGLTestingApplication();
(function () {var $gwt_version = "2.7.0";var $wnd = window;var $doc = $wnd.document;var $moduleName, $moduleBase;var $stats = $wnd.__gwtStatsEvent ? function(a) {$wnd.__gwtStatsEvent(a)} : null;var $strongName = '3D8C6F7611D982CF3D64B47DB7E9AD5B';var $intern_0 = {44:1, 38:1, 42:1, 41:1, 45:1, 33:1, 29:1}, $intern_1 = {3:1, 13:1}, $intern_2 = {3:1, 4:1, 415:1}, $intern_3 = 65535, $intern_4 = {3:1, 4:1}, $intern_5 = {159:1, 51:1, 3:1, 26:1, 15:1}, $intern_6 = {84:1, 3:1, 13:1}, $intern_7 = 4194303, $intern_8 = 1048575, $intern_9 = 524288, $intern_10 = 4194304, $intern_11 = 17592186044416, $intern_12 = -9223372036854775808, $intern_13 = {3:1, 4:1, 214:1}, $intern_14 = 32768, $intern_15 = 16384, $intern_16 = 65536, $intern_17 = 131072, $intern_18 = 262144, $intern_19 = 1048576, $intern_20 = 2097152, $intern_21 = 8388608, $intern_22 = 16777216, $intern_23 = 33554432, $intern_24 = 67108864, $intern_25 = {44:1, 38:1, 42:1, 41:1, 82:1, 45:1, 33:1, 29:1}, $intern_26 = {44:1, 38:1, 42:1, 41:1, 213:1, 45:1, 33:1, 29:1}, $intern_27 = {44:1, 38:1, 42:1, 41:1, 82:1, 45:1, 96:1, 33:1, 29:1}, $intern_28 = {134:1}, $intern_29 = {3:1}, $intern_30 = 1.52587890625E-5, $intern_31 = {94:1}, $intern_32 = {22:1}, $intern_33 = {3:1, 37:1, 108:1}, $intern_34 = 1000000000000, $intern_35 = -1000000000000, $intern_36 = 57.29577951308232, $intern_37 = 3.141592653589793, $intern_38 = -1.5707963267948966, $intern_39 = {64:1}, $intern_40 = {91:1}, $intern_41 = {9:1, 49:1}, $intern_42 = {27:1, 70:1, 9:1}, $intern_43 = 1.5707963267948966, $intern_44 = 1.0E-5, $intern_45 = 10000000000, $intern_46 = -10000000000, $intern_47 = {50:1, 27:1, 9:1}, $intern_48 = {90:1}, $intern_49 = {56:1, 35:1}, $intern_50 = {86:1, 89:1, 9:1}, $intern_51 = {30:1, 35:1}, $intern_52 = {28:1, 107:1, 9:1}, $intern_53 = {28:1, 130:1, 9:1}, $intern_54 = {28:1, 88:1, 9:1}, $intern_55 = 0.4000000059604645, $intern_56 = 6378137, $intern_57 = 6356752.314245, $intern_58 = 0.30000001192092896, $intern_59 = {34:1}, $intern_60 = {59:1, 9:1}, $intern_61 = 85.0511287798, $intern_62 = 1.4844222297452172, $intern_63 = -85.0511287798, $intern_64 = -1.4844222297452172, $intern_65 = 1.7976931348623157E308, $intern_66 = -1.7976931348623157E308;
var _, initFnList_0, prototypesByTypeId_0 = {}, permutationId = -1;
function org_glob3_mobile_specific_Downloader_1WebGL_1Handler(){
  if (permutationId == 4) {
    return new Downloader_WebGL_Handler_WebkitImpl;
  }
  return new Downloader_WebGL_Handler_DefaultImpl;
}

function com_google_gwt_useragent_client_UserAgent(){
  switch (permutationId) {
    case 4:
      return new UserAgentImplSafari;
    case 1:
      return new UserAgentImplIe10;
    case 3:
      return new UserAgentImplIe9;
    case 2:
      return new UserAgentImplIe8;
  }
  return new UserAgentImplGecko1_8;
}

function com_google_gwt_user_client_ui_impl_FocusImpl(){
  switch (permutationId) {
    case 0:
      return new FocusImplStandard;
    case 4:
      return new FocusImplSafari;
    case 1:
      return new FocusImpl;
  }
  return new FocusImplIE6;
}

function com_google_gwt_user_client_impl_WindowImpl(){
  switch (permutationId) {
    case 0:
      return new WindowImplMozilla;
    case 4:
      return new WindowImpl;
  }
  return new WindowImplIE;
}

function com_google_gwt_user_client_impl_DOMImpl(){
  switch (permutationId) {
    case 2:
      return new DOMImplIE8_0;
    case 0:
      return new DOMImplMozilla_0;
    case 4:
      return new DOMImplWebkit_0;
  }
  return new DOMImplIE9_0;
}

function com_google_gwt_dom_client_DOMImpl(){
  switch (permutationId) {
    case 2:
      return new DOMImplIE8;
    case 0:
      return new DOMImplMozilla;
    case 4:
      return new DOMImplWebkit;
  }
  return new DOMImplIE9;
}

function com_google_gwt_canvas_client_Canvas_CanvasElementSupportDetector(){
  if (permutationId == 2) {
    return new Canvas$CanvasElementSupportDetectedNo;
  }
  return new Canvas$CanvasElementSupportDetectedMaybe;
}

function typeMarkerFn(){
}

function portableObjCreate(obj){
  function F(){
  }

  ;
  F.prototype = obj || {};
  return new F;
}

function modernizeBrowser(){
  !Array.isArray && (Array.isArray = function(vArg){
    return Object.prototype.toString.call(vArg) === '[object Array]';
  }
  );
}

function maybeGetClassLiteralFromPlaceHolder_0(entry){
  return entry instanceof Array?entry[0]:null;
}

function emptyMethod(){
}

function defineClass(typeId, superTypeId, castableTypeMap){
  var prototypesByTypeId = prototypesByTypeId_0;
  var createSubclassPrototype = createSubclassPrototype_0;
  var maybeGetClassLiteralFromPlaceHolder = maybeGetClassLiteralFromPlaceHolder_0;
  var prototype_0 = prototypesByTypeId[typeId];
  var clazz = maybeGetClassLiteralFromPlaceHolder(prototype_0);
  if (prototype_0 && !clazz) {
    _ = prototype_0;
  }
   else {
    _ = prototypesByTypeId[typeId] = !superTypeId?{}:createSubclassPrototype(superTypeId);
    _.castableTypeMap$ = castableTypeMap;
    _.constructor = _;
    !superTypeId && (_.typeMarker$ = typeMarkerFn);
  }
  for (var i_0 = 3; i_0 < arguments.length; ++i_0) {
    arguments[i_0].prototype = _;
  }
  clazz && (_.___clazz$ = clazz);
}

function createSubclassPrototype_0(superTypeId){
  var prototypesByTypeId = prototypesByTypeId_0;
  return portableObjCreate(prototypesByTypeId[superTypeId]);
}

function setGwtProperty(propertyName, propertyValue){
  typeof window === 'object' && typeof window['$gwt'] === 'object' && (window['$gwt'][propertyName] = propertyValue);
}

function registerEntry(){
  return entry_0;
}

function gwtOnLoad_0(errFn, modName, modBase, softPermutationId){
  ensureModuleInit();
  var initFnList = initFnList_0;
  $moduleName = modName;
  $moduleBase = modBase;
  permutationId = softPermutationId;
  function initializeModules(){
    for (var i_0 = 0; i_0 < initFnList.length; i_0++) {
      initFnList[i_0]();
    }
  }

  if (errFn) {
    try {
      $entry(initializeModules)();
    }
     catch (e) {
      errFn(modName, e);
    }
  }
   else {
    $entry(initializeModules)();
  }
}

function ensureModuleInit(){
  initFnList_0 == null && (initFnList_0 = []);
}

function addInitFunctions(){
  ensureModuleInit();
  var initFnList = initFnList_0;
  for (var i_0 = 0; i_0 < arguments.length; i_0++) {
    initFnList.push(arguments[i_0]);
  }
}

function Object_0(){
}

function equals_Ljava_lang_Object__Z__devirtual$(this$static, other){
  return isJavaString(this$static)?$equals(this$static, other):hasJavaObjectVirtualDispatch(this$static)?this$static.equals$(other):isJavaArray(this$static)?this$static === other:this$static === other;
}

function getClass__Ljava_lang_Class___devirtual$(this$static){
  return isJavaString(this$static)?Ljava_lang_String_2_classLit:hasJavaObjectVirtualDispatch(this$static)?this$static.___clazz$:isJavaArray(this$static)?this$static.___clazz$:Lcom_google_gwt_core_client_JavaScriptObject_2_classLit;
}

function hashCode__I__devirtual$(this$static){
  return isJavaString(this$static)?getHashCode_0(this$static):hasJavaObjectVirtualDispatch(this$static)?this$static.hashCode$():isJavaArray(this$static)?getHashCode(this$static):getHashCode(this$static);
}

defineClass(1, null, {}, Object_0);
_.equals$ = function equals(other){
  return this === other;
}
;
_.getClass$ = function getClass_0(){
  return this.___clazz$;
}
;
_.hashCode$ = function hashCode_0(){
  return getHashCode(this);
}
;
_.toString$ = function toString_0(){
  return $getName(getClass__Ljava_lang_Class___devirtual$(this)) + '@' + (hashCode__I__devirtual$(this) >>> 0).toString(16);
}
;
_.toString = function(){
  return this.toString$();
}
;
stringCastMap = {3:1, 499:1, 26:1, 2:1};
modernizeBrowser();
function canCast(src_0, dstId){
  return isJavaString(src_0) && !!stringCastMap[dstId] || src_0.castableTypeMap$ && !!src_0.castableTypeMap$[dstId];
}

function dynamicCast(src_0, dstId){
  if (src_0 != null && !canCast(src_0, dstId)) {
    throw new ClassCastException;
  }
  return src_0;
}

function dynamicCastJso(src_0){
  if (src_0 != null && !(!isJavaString(src_0) && !hasTypeMarker(src_0))) {
    throw new ClassCastException;
  }
  return src_0;
}

function dynamicCastToString(src_0){
  if (src_0 != null && !isJavaString(src_0)) {
    throw new ClassCastException;
  }
  return src_0;
}

function hasJavaObjectVirtualDispatch(src_0){
  return !instanceofArray(src_0) && hasTypeMarker(src_0);
}

function instanceOf(src_0, dstId){
  return src_0 != null && canCast(src_0, dstId);
}

function instanceOfJso(src_0){
  return src_0 != null && !isJavaString(src_0) && !hasTypeMarker(src_0);
}

function instanceofArray(src_0){
  return Array.isArray(src_0);
}

function isJavaArray(src_0){
  return instanceofArray(src_0) && hasTypeMarker(src_0);
}

function isJavaString(src_0){
  return typeof src_0 === 'string';
}

function maskUndefined(src_0){
  return src_0 == null?null:src_0;
}

function narrow_short(x_0){
  return x_0 << 16 >> 16;
}

function round_int(x_0){
  return ~~Math.max(Math.min(x_0, 2147483647), -2147483648);
}

function throwClassCastExceptionUnlessNull(o){
  if (o != null) {
    throw new ClassCastException;
  }
  return null;
}

var stringCastMap;
function $ensureNamesAreInitialized(this$static){
  if (this$static.typeName != null) {
    return;
  }
  initializeNames(this$static);
}

function $getName(this$static){
  $ensureNamesAreInitialized(this$static);
  return this$static.typeName;
}

function Class(){
  ++nextSequentialId;
  this.typeName = null;
  this.simpleName = null;
  this.packageName = null;
  this.compoundName = null;
  this.canonicalName = null;
  this.typeId = null;
  this.arrayLiterals = null;
}

function createClassObject(packageName, compoundClassName){
  var clazz;
  clazz = new Class;
  clazz.packageName = packageName;
  clazz.compoundName = compoundClassName;
  return clazz;
}

function createForClass(packageName, compoundClassName, typeId){
  var clazz;
  clazz = createClassObject(packageName, compoundClassName);
  maybeSetClassLiteral(typeId, clazz);
  return clazz;
}

function createForEnum(packageName, compoundClassName, typeId, enumConstantsFunc){
  var clazz;
  clazz = createClassObject(packageName, compoundClassName);
  maybeSetClassLiteral(typeId, clazz);
  clazz.modifiers = enumConstantsFunc?8:0;
  return clazz;
}

function createForInterface(packageName, compoundClassName){
  var clazz;
  clazz = createClassObject(packageName, compoundClassName);
  clazz.modifiers = 2;
  return clazz;
}

function createForPrimitive(className, primitiveTypeId){
  var clazz;
  clazz = createClassObject('', className);
  clazz.typeId = primitiveTypeId;
  clazz.modifiers = 1;
  return clazz;
}

function getClassLiteralForArray_0(leafClass, dimensions){
  var arrayLiterals = leafClass.arrayLiterals = leafClass.arrayLiterals || [];
  return arrayLiterals[dimensions] || (arrayLiterals[dimensions] = leafClass.createClassLiteralForArray(dimensions));
}

function getPrototypeForClass(clazz){
  if (clazz.isPrimitive()) {
    return null;
  }
  var typeId = clazz.typeId;
  var prototype_0 = prototypesByTypeId_0[typeId];
  return prototype_0;
}

function initializeNames(clazz){
  if (clazz.isArray_0()) {
    var componentType = clazz.componentType;
    componentType.isPrimitive()?(clazz.typeName = '[' + componentType.typeId):!componentType.isArray_0()?(clazz.typeName = '[L' + componentType.getName() + ';'):(clazz.typeName = '[' + componentType.getName());
    clazz.canonicalName = componentType.getCanonicalName() + '[]';
    clazz.simpleName = componentType.getSimpleName() + '[]';
    return;
  }
  var packageName = clazz.packageName;
  var compoundName = clazz.compoundName;
  compoundName = compoundName.split('/');
  clazz.typeName = join_0('.', [packageName, join_0('$', compoundName)]);
  clazz.canonicalName = join_0('.', [packageName, join_0('.', compoundName)]);
  clazz.simpleName = compoundName[compoundName.length - 1];
}

function join_0(separator, strings){
  var i_0 = 0;
  while (!strings[i_0] || strings[i_0] == '') {
    i_0++;
  }
  var result = strings[i_0++];
  for (; i_0 < strings.length; i_0++) {
    if (!strings[i_0] || strings[i_0] == '') {
      continue;
    }
    result += separator + strings[i_0];
  }
  return result;
}

function maybeSetClassLiteral(typeId, clazz){
  var proto;
  if (!typeId) {
    return;
  }
  clazz.typeId = typeId;
  var prototype_0 = getPrototypeForClass(clazz);
  if (!prototype_0) {
    prototypesByTypeId_0[typeId] = [clazz];
    return;
  }
  prototype_0.___clazz$ = clazz;
}

defineClass(160, 1, {}, Class);
_.createClassLiteralForArray = function createClassLiteralForArray(dimensions){
  var clazz;
  clazz = new Class;
  clazz.modifiers = 4;
  dimensions > 1?(clazz.componentType = getClassLiteralForArray_0(this, dimensions - 1)):(clazz.componentType = this);
  return clazz;
}
;
_.getCanonicalName = function getCanonicalName(){
  $ensureNamesAreInitialized(this);
  return this.canonicalName;
}
;
_.getName = function getName(){
  return $getName(this);
}
;
_.getSimpleName = function getSimpleName(){
  $ensureNamesAreInitialized(this);
  return this.simpleName;
}
;
_.isArray_0 = function isArray(){
  return (this.modifiers & 4) != 0;
}
;
_.isPrimitive = function isPrimitive(){
  return (this.modifiers & 1) != 0;
}
;
_.toString$ = function toString_10(){
  return ((this.modifiers & 2) != 0?'interface ':(this.modifiers & 1) != 0?'':'class ') + ($ensureNamesAreInitialized(this) , this.typeName);
}
;
_.modifiers = 0;
var nextSequentialId = 1;
var Ljava_lang_Object_2_classLit = createForClass('java.lang', 'Object', 1), Lcom_google_gwt_core_client_JavaScriptObject_2_classLit = createForClass('com.google.gwt.core.client', 'JavaScriptObject$', 0), Ljava_lang_Class_2_classLit = createForClass('java.lang', 'Class', 160);
function $getElement(this$static){
  return $clinit_DOM() , this$static.element;
}

function $setElement(this$static, elem){
  $setElement_0(this$static, ($clinit_DOM() , elem));
}

function $setElement_0(this$static, elem){
  this$static.element = elem;
}

function $setPixelSize(this$static, width_0, height){
  width_0 >= 0 && (($clinit_DOM() , this$static.element).style['width'] = width_0 + 'px' , undefined);
  height >= 0 && (($clinit_DOM() , this$static.element).style['height'] = height + 'px' , undefined);
}

defineClass(33, 1, {41:1, 33:1});
_.resolvePotentialElement = function resolvePotentialElement(){
  throw new UnsupportedOperationException;
}
;
_.toString$ = function toString_1(){
  if (!this.element) {
    return '(null handle)';
  }
  return $getString(($clinit_DOM() , this.element));
}
;
var Lcom_google_gwt_user_client_ui_UIObject_2_classLit = createForClass('com.google.gwt.user.client.ui', 'UIObject', 33);
function $onAttach(this$static){
  var bitsToAdd;
  if (this$static.isAttached()) {
    throw new IllegalStateException_0("Should only call onAttach when the widget is detached from the browser's document");
  }
  this$static.attached = true;
  $clinit_DOM();
  setEventListener(this$static.element, this$static);
  bitsToAdd = this$static.eventsToSink;
  this$static.eventsToSink = -1;
  bitsToAdd > 0 && (this$static.eventsToSink == -1?sinkEvents(this$static.element, bitsToAdd | (this$static.element.__eventBits || 0)):(this$static.eventsToSink |= bitsToAdd));
  this$static.doAttachChildren();
}

function $onBrowserEvent(this$static, event_0){
  var related;
  switch ($clinit_DOM() , $eventGetTypeInt(($clinit_DOMImpl() , event_0).type)) {
    case 16:
    case 32:
      related = impl_1.eventGetRelatedTarget(event_0);
      if (!!related && $isOrHasChild(this$static.element, related)) {
        return;
      }

  }
  fireNativeEvent($clinit_DOM());
}

function $removeFromParent(this$static){
  if (!this$static.parent_0) {
    $clinit_RootPanel();
    $contains_0(widgetsToDetach, this$static) && detachNow(this$static);
  }
   else if (instanceOf(this$static.parent_0, 82)) {
    dynamicCast(this$static.parent_0, 82).remove(this$static);
  }
   else if (this$static.parent_0) {
    throw new IllegalStateException_0("This widget's parent does not implement HasWidgets");
  }
}

function $setParent(this$static, parent_0){
  var oldParent;
  oldParent = this$static.parent_0;
  if (!parent_0) {
    try {
      !!oldParent && oldParent.isAttached() && this$static.onDetach();
    }
     finally {
      this$static.parent_0 = null;
    }
  }
   else {
    if (oldParent) {
      throw new IllegalStateException_0('Cannot set a new parent without first clearing the old parent');
    }
    this$static.parent_0 = parent_0;
    parent_0.isAttached() && this$static.onAttach();
  }
}

function $sinkEvents(this$static, eventBitsToAdd){
  this$static.eventsToSink == -1?sinkEvents(($clinit_DOM() , this$static.element), eventBitsToAdd | (this$static.element.__eventBits || 0)):(this$static.eventsToSink |= eventBitsToAdd);
}

defineClass(29, 33, $intern_0);
_.doAttachChildren = function doAttachChildren(){
}
;
_.doDetachChildren = function doDetachChildren(){
}
;
_.isAttached = function isAttached(){
  return this.attached;
}
;
_.onAttach = function onAttach(){
  $onAttach(this);
}
;
_.onBrowserEvent = function onBrowserEvent(event_0){
  $onBrowserEvent(this, event_0);
}
;
_.onDetach = function onDetach(){
  if (!this.isAttached()) {
    throw new IllegalStateException_0("Should only call onDetach when the widget is attached to the browser's document");
  }
  try {
    this.doDetachChildren();
  }
   finally {
    $clinit_DOM();
    setEventListener(this.element, null);
    this.attached = false;
  }
}
;
_.attached = false;
_.eventsToSink = 0;
var Lcom_google_gwt_user_client_ui_Widget_2_classLit = createForClass('com.google.gwt.user.client.ui', 'Widget', 29);
function $clinit_FocusWidget(){
  $clinit_FocusWidget = emptyMethod;
  impl_0 = ($clinit_FocusImpl() , $clinit_FocusImpl() , implWidget);
}

function $setFocus(this$static){
  impl_0.focus_0(($clinit_DOM() , this$static.element));
}

defineClass(449, 29, $intern_0);
_.onAttach = function onAttach_0(){
  var tabIndex;
  $onAttach(this);
  tabIndex = $getTabIndex(($clinit_DOM() , this.element));
  -1 == tabIndex && (this.element.tabIndex = 0 , undefined);
}
;
var impl_0;
var Lcom_google_gwt_user_client_ui_FocusWidget_2_classLit = createForClass('com.google.gwt.user.client.ui', 'FocusWidget', 449);
function $setCoordinateSpaceHeight(this$static, height){
  $setHeight(($clinit_DOM() , this$static.element), height);
}

function $setCoordinateSpaceWidth(this$static, width_0){
  $setWidth(($clinit_DOM() , this$static.element), width_0);
}

function Canvas_0(element){
  $setElement_0(this, ($clinit_DOM() , element));
}

function createIfSupported(){
  $clinit_FocusWidget();
  var element;
  !detector && (detector = com_google_gwt_canvas_client_Canvas_CanvasElementSupportDetector());
  if (!detector.isSupportedCompileTime()) {
    return null;
  }
  element = $createCanvasElement($doc);
  if (!element.getContext) {
    return null;
  }
  return new Canvas_0(element);
}

defineClass(301, 449, $intern_0, Canvas_0);
_.getCanvasElement = function getCanvasElement(){
  return $clinit_DOM() , this.element;
}
;
var detector;
var Lcom_google_gwt_canvas_client_Canvas_2_classLit = createForClass('com.google.gwt.canvas.client', 'Canvas', 301);
defineClass(450, 1, {});
_.isSupportedCompileTime = function isSupportedCompileTime(){
  return false;
}
;
var Lcom_google_gwt_canvas_client_Canvas$CanvasElementSupportDetector_2_classLit = createForClass('com.google.gwt.canvas.client', 'Canvas/CanvasElementSupportDetector', 450);
function Canvas$CanvasElementSupportDetectedMaybe(){
}

defineClass(302, 450, {}, Canvas$CanvasElementSupportDetectedMaybe);
_.isSupportedCompileTime = function isSupportedCompileTime_0(){
  return true;
}
;
var Lcom_google_gwt_canvas_client_Canvas$CanvasElementSupportDetectedMaybe_2_classLit = createForClass('com.google.gwt.canvas.client', 'Canvas/CanvasElementSupportDetectedMaybe', 302);
function Canvas$CanvasElementSupportDetectedNo(){
}

defineClass(303, 450, {}, Canvas$CanvasElementSupportDetectedNo);
_.isSupportedCompileTime = function isSupportedCompileTime_1(){
  return false;
}
;
var Lcom_google_gwt_canvas_client_Canvas$CanvasElementSupportDetectedNo_2_classLit = createForClass('com.google.gwt.canvas.client', 'Canvas/CanvasElementSupportDetectedNo', 303);
function Duration(){
  this.start_0 = now_1();
}

defineClass(289, 1, {}, Duration);
_.start_0 = 0;
var Lcom_google_gwt_core_client_Duration_2_classLit = createForClass('com.google.gwt.core.client', 'Duration', 289);
function isScript(){
  return true;
}

function setUncaughtExceptionHandler(handler){
  uncaughtExceptionHandler = handler;
}

var uncaughtExceptionHandler = null;
function $fillInStackTrace(this$static){
  this$static.stackTrace = null;
  captureStackTrace(this$static, this$static.detailMessage);
  return this$static;
}

function $printStackTrace(this$static, out){
  var element, element$array, element$index, element$max, t, stackTrace;
  for (t = this$static; t; t = t.cause) {
    t != this$static && $append_0(out.builder, 'Caused by: ');
    $append_0(out.builder, '' + t);
    $append_0(out.builder, '\n');
    for (element$array = (t.stackTrace == null && (t.stackTrace = ($clinit_StackTraceCreator() , stackTrace = collector.getStackTrace(t) , dropInternalFrames(stackTrace))) , t.stackTrace) , element$index = 0 , element$max = element$array.length; element$index < element$max; ++element$index) {
      element = element$array[element$index];
      $append_0(out.builder, '\tat ' + element);
      $append_0(out.builder, '\n');
    }
  }
}

function Throwable(message, cause){
  this.cause = cause;
  this.detailMessage = message;
  $fillInStackTrace(this);
}

defineClass(13, 1, $intern_1);
_.getMessage = function getMessage(){
  return this.detailMessage;
}
;
_.toString$ = function toString_2(){
  var className, msg;
  className = $getName(this.___clazz$);
  msg = this.getMessage();
  return msg != null?className + ': ' + msg:className;
}
;
var Ljava_lang_Throwable_2_classLit = createForClass('java.lang', 'Throwable', 13);
defineClass(110, 13, $intern_1);
var Ljava_lang_Exception_2_classLit = createForClass('java.lang', 'Exception', 110);
function RuntimeException(){
  $fillInStackTrace(this);
}

function RuntimeException_0(message){
  this.detailMessage = message;
  $fillInStackTrace(this);
}

function RuntimeException_1(message, cause){
  Throwable.call(this, message, cause);
}

defineClass(17, 110, $intern_1, RuntimeException_0);
var Ljava_lang_RuntimeException_2_classLit = createForClass('java.lang', 'RuntimeException', 17);
defineClass(220, 17, $intern_1);
var Lcom_google_gwt_core_client_impl_JavaScriptExceptionBase_2_classLit = createForClass('com.google.gwt.core.client.impl', 'JavaScriptExceptionBase', 220);
function $clinit_JavaScriptException(){
  $clinit_JavaScriptException = emptyMethod;
  NOT_SET = new Object_0;
}

function $ensureInit(this$static){
  var exception;
  if (this$static.message_0 == null) {
    exception = maskUndefined(this$static.e) === maskUndefined(NOT_SET)?null:this$static.e;
    this$static.name_0 = exception == null?'null':instanceOfJso(exception)?getExceptionName0(dynamicCastJso(exception)):isJavaString(exception)?'String':$getName(getClass__Ljava_lang_Class___devirtual$(exception));
    this$static.description = this$static.description + ': ' + (instanceOfJso(exception)?getExceptionDescription0(dynamicCastJso(exception)):exception + '');
    this$static.message_0 = '(' + this$static.name_0 + ') ' + this$static.description;
  }
}

function JavaScriptException(e){
  $clinit_JavaScriptException();
  this.cause = null;
  this.detailMessage = null;
  this.description = '';
  this.e = e;
  this.description = '';
}

function getExceptionDescription0(e){
  return e == null?null:e.message;
}

function getExceptionName0(e){
  return e == null?null:e.name;
}

defineClass(62, 220, {62:1, 3:1, 13:1}, JavaScriptException);
_.getMessage = function getMessage_0(){
  $ensureInit(this);
  return this.message_0;
}
;
_.getThrown = function getThrown(){
  return maskUndefined(this.e) === maskUndefined(NOT_SET)?null:this.e;
}
;
var NOT_SET;
var Lcom_google_gwt_core_client_JavaScriptException_2_classLit = createForClass('com.google.gwt.core.client', 'JavaScriptException', 62);
function $push(this$static, value_0){
  this$static[this$static.length] = value_0;
}

function create(milliseconds){
  return new Date(milliseconds);
}

function now_1(){
  if (Date.now) {
    return Date.now();
  }
  return (new Date).getTime();
}

defineClass(417, 1, {});
var Lcom_google_gwt_core_client_Scheduler_2_classLit = createForClass('com.google.gwt.core.client', 'Scheduler', 417);
function apply_0(jsFunction, thisObj, args){
  return jsFunction.apply(thisObj, args);
  var __0;
}

function enter(){
  var now_0;
  if (entryDepth != 0) {
    now_0 = now_1();
    if (now_0 - watchdogEntryDepthLastScheduled > 2000) {
      watchdogEntryDepthLastScheduled = now_0;
      watchdogEntryDepthTimerId = $wnd.setTimeout(watchdogEntryDepthRun, 10);
    }
  }
  if (entryDepth++ == 0) {
    $flushEntryCommands(($clinit_SchedulerImpl() , INSTANCE));
    return true;
  }
  return false;
}

function entry_0(jsFunction){
  return function(){
    if (isScript()) {
      return entry0(jsFunction, this, arguments);
    }
     else {
      var __0 = entry0(jsFunction, this, arguments);
      __0 != null && (__0 = __0.val);
      return __0;
    }
  }
  ;
}

function entry0(jsFunction, thisObj, args){
  var initialEntry, t;
  initialEntry = enter();
  try {
    if (uncaughtExceptionHandler) {
      try {
        return apply_0(jsFunction, thisObj, args);
      }
       catch ($e0) {
        $e0 = wrap($e0);
        if (instanceOf($e0, 13)) {
          t = $e0;
          reportUncaughtException(t);
          return undefined;
        }
         else 
          throw unwrap($e0);
      }
    }
     else {
      return apply_0(jsFunction, thisObj, args);
    }
  }
   finally {
    exit(initialEntry);
  }
}

function exit(initialEntry){
  initialEntry && $flushFinallyCommands(($clinit_SchedulerImpl() , INSTANCE));
  --entryDepth;
  if (initialEntry) {
    if (watchdogEntryDepthTimerId != -1) {
      watchdogEntryDepthCancel(watchdogEntryDepthTimerId);
      watchdogEntryDepthTimerId = -1;
    }
  }
}

function getHashCode(o){
  return o.$H || (o.$H = ++sNextHashId);
}

function reportToBrowser(e){
  $wnd.setTimeout(function(){
    throw e;
  }
  , 0);
}

function reportUncaughtException(e){
  var handler;
  handler = uncaughtExceptionHandler;
  if (handler) {
    if (handler == uncaughtExceptionHandlerForTest) {
      return;
    }
    $log_2(handler.val$log2, ($clinit_Level() , SEVERE), e.getMessage(), e);
    return;
  }
  reportToBrowser(instanceOf(e, 62)?dynamicCast(e, 62).getThrown():e);
}

function watchdogEntryDepthCancel(timerId){
  $wnd.clearTimeout(timerId);
}

function watchdogEntryDepthRun(){
  entryDepth != 0 && (entryDepth = 0);
  watchdogEntryDepthTimerId = -1;
}

var entryDepth = 0, sNextHashId = 0, uncaughtExceptionHandlerForTest, watchdogEntryDepthLastScheduled = 0, watchdogEntryDepthTimerId = -1;
function $clinit_SchedulerImpl(){
  $clinit_SchedulerImpl = emptyMethod;
  INSTANCE = new SchedulerImpl;
}

function $flushEntryCommands(this$static){
  var oldQueue, rescheduled;
  if (this$static.entryCommands) {
    rescheduled = null;
    do {
      oldQueue = this$static.entryCommands;
      this$static.entryCommands = null;
      rescheduled = runScheduledTasks(oldQueue, rescheduled);
    }
     while (this$static.entryCommands);
    this$static.entryCommands = rescheduled;
  }
}

function $flushFinallyCommands(this$static){
  var oldQueue, rescheduled;
  if (this$static.finallyCommands) {
    rescheduled = null;
    do {
      oldQueue = this$static.finallyCommands;
      this$static.finallyCommands = null;
      rescheduled = runScheduledTasks(oldQueue, rescheduled);
    }
     while (this$static.finallyCommands);
    this$static.finallyCommands = rescheduled;
  }
}

function $flushPostEventPumpCommands(this$static){
  var oldDeferred;
  if (this$static.deferredCommands) {
    oldDeferred = this$static.deferredCommands;
    this$static.deferredCommands = null;
    !this$static.incrementalCommands && (this$static.incrementalCommands = []);
    runScheduledTasks(oldDeferred, this$static.incrementalCommands);
  }
  !!this$static.incrementalCommands && (this$static.incrementalCommands = $runRepeatingTasks(this$static.incrementalCommands));
}

function $isWorkQueued(this$static){
  return !!this$static.deferredCommands || !!this$static.incrementalCommands;
}

function $maybeSchedulePostEventPumpCommands(this$static){
  if (!this$static.shouldBeRunning) {
    this$static.shouldBeRunning = true;
    !this$static.flusher && (this$static.flusher = new SchedulerImpl$Flusher(this$static));
    scheduleFixedDelayImpl(this$static.flusher, 1);
    !this$static.rescue && (this$static.rescue = new SchedulerImpl$Rescuer(this$static));
    scheduleFixedDelayImpl(this$static.rescue, 50);
  }
}

function $runRepeatingTasks(tasks){
  var canceledSomeTasks, duration, executedSomeTask, i_0, length_0, newTasks, t;
  length_0 = tasks.length;
  if (length_0 == 0) {
    return null;
  }
  canceledSomeTasks = false;
  duration = new Duration;
  while (now_1() - duration.start_0 < 16) {
    executedSomeTask = false;
    for (i_0 = 0; i_0 < length_0; i_0++) {
      t = tasks[i_0];
      if (!t) {
        continue;
      }
      executedSomeTask = true;
      if (!t[0].execute()) {
        tasks[i_0] = null;
        canceledSomeTasks = true;
      }
    }
    if (!executedSomeTask) {
      break;
    }
  }
  if (canceledSomeTasks) {
    newTasks = [];
    for (i_0 = 0; i_0 < length_0; i_0++) {
      !!tasks[i_0] && $push(newTasks, tasks[i_0]);
    }
    return newTasks.length == 0?null:newTasks;
  }
   else {
    return tasks;
  }
}

function $scheduleDeferred(this$static, cmd){
  this$static.deferredCommands = push_0(this$static.deferredCommands, [cmd, false]);
  $maybeSchedulePostEventPumpCommands(this$static);
}

function SchedulerImpl(){
}

function execute(cmd){
  return cmd.execute();
}

function push_0(queue, task){
  !queue && (queue = []);
  $push(queue, task);
  return queue;
}

function runScheduledTasks(tasks, rescheduled){
  var e, i_0, j, t;
  for (i_0 = 0 , j = tasks.length; i_0 < j; i_0++) {
    t = tasks[i_0];
    try {
      t[1]?t[0].execute() && (rescheduled = push_0(rescheduled, t)):($onTouchEvent_0(t[0].this$01._widget, t[0].val$event2) , undefined);
    }
     catch ($e0) {
      $e0 = wrap($e0);
      if (instanceOf($e0, 13)) {
        e = $e0;
        reportUncaughtException(e);
      }
       else 
        throw unwrap($e0);
    }
  }
  return rescheduled;
}

function scheduleFixedDelayImpl(cmd, delayMs){
  $clinit_SchedulerImpl();
  function callback(){
    var ret = $entry(execute)(cmd);
    !isScript() && (ret = ret == true);
    ret && $wnd.setTimeout(callback, delayMs);
  }

  $wnd.setTimeout(callback, delayMs);
}

defineClass(262, 417, {}, SchedulerImpl);
_.flushRunning = false;
_.shouldBeRunning = false;
var INSTANCE;
var Lcom_google_gwt_core_client_impl_SchedulerImpl_2_classLit = createForClass('com.google.gwt.core.client.impl', 'SchedulerImpl', 262);
function SchedulerImpl$Flusher(this$0){
  this.this$01 = this$0;
}

defineClass(263, 1, {}, SchedulerImpl$Flusher);
_.execute = function execute_0(){
  this.this$01.flushRunning = true;
  $flushPostEventPumpCommands(this.this$01);
  this.this$01.flushRunning = false;
  return this.this$01.shouldBeRunning = $isWorkQueued(this.this$01);
}
;
var Lcom_google_gwt_core_client_impl_SchedulerImpl$Flusher_2_classLit = createForClass('com.google.gwt.core.client.impl', 'SchedulerImpl/Flusher', 263);
function SchedulerImpl$Rescuer(this$0){
  this.this$01 = this$0;
}

defineClass(264, 1, {}, SchedulerImpl$Rescuer);
_.execute = function execute_1(){
  this.this$01.flushRunning && scheduleFixedDelayImpl(this.this$01.flusher, 1);
  return this.this$01.shouldBeRunning;
}
;
var Lcom_google_gwt_core_client_impl_SchedulerImpl$Rescuer_2_classLit = createForClass('com.google.gwt.core.client.impl', 'SchedulerImpl/Rescuer', 264);
function $clinit_StackTraceCreator(){
  $clinit_StackTraceCreator = emptyMethod;
  var c, enforceLegacy;
  enforceLegacy = !(!!Error.stackTraceLimit || 'stack' in new Error);
  c = new StackTraceCreator$CollectorModernNoSourceMap;
  collector = enforceLegacy?new StackTraceCreator$CollectorLegacy:c;
}

function captureStackTrace(throwable, reference){
  $clinit_StackTraceCreator();
  collector.collect(throwable, reference);
}

function dropInternalFrames(stackTrace){
  var dropFrameUntilFnName, i_0, numberOfFrameToSearch;
  dropFrameUntilFnName = 'captureStackTrace';
  numberOfFrameToSearch = min_1(stackTrace.length, 5);
  for (i_0 = 0; i_0 < numberOfFrameToSearch; i_0++) {
    if ($equals(stackTrace[i_0].methodName, dropFrameUntilFnName)) {
      return dynamicCast((stackTrace.length >= i_0 + 1 && stackTrace.splice(0, i_0 + 1) , stackTrace), 415);
    }
  }
  return stackTrace;
}

function extractFunctionName(fnName){
  var fnRE = /function(?:\s+([\w$]+))?\s*\(/;
  var match_0 = fnRE.exec(fnName);
  return match_0 && match_0[1] || 'anonymous';
}

function parseInt_0(number){
  $clinit_StackTraceCreator();
  return parseInt(number) || -1;
}

var collector;
defineClass(428, 1, {});
var Lcom_google_gwt_core_client_impl_StackTraceCreator$Collector_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/Collector', 428);
function StackTraceCreator$CollectorLegacy(){
}

defineClass(221, 428, {}, StackTraceCreator$CollectorLegacy);
_.collect = function collect(t, thrownIgnored){
  var seen = {}, name_1;
  t.fnStack = [];
  var callee = arguments.callee.caller;
  while (callee) {
    var name_0 = ($clinit_StackTraceCreator() , callee.name || (callee.name = extractFunctionName(callee.toString())));
    t.fnStack.push(name_0);
    var keyName = ':' + name_0;
    var withThisName = seen[keyName];
    if (withThisName) {
      var i_0, j;
      for (i_0 = 0 , j = withThisName.length; i_0 < j; i_0++) {
        if (withThisName[i_0] === callee) {
          return;
        }
      }
    }
    (withThisName || (seen[keyName] = [])).push(callee);
    callee = callee.caller;
  }
}
;
_.getStackTrace = function getStackTrace(t){
  var i_0, length_0, stack_0, stackTrace;
  stack_0 = ($clinit_StackTraceCreator() , t && t.fnStack && t.fnStack instanceof Array?t.fnStack:[]);
  length_0 = stack_0.length;
  stackTrace = initDim(Ljava_lang_StackTraceElement_2_classLit, $intern_2, 63, length_0, 0, 1);
  for (i_0 = 0; i_0 < length_0; i_0++) {
    stackTrace[i_0] = new StackTraceElement(stack_0[i_0], null, -1);
  }
  return stackTrace;
}
;
var Lcom_google_gwt_core_client_impl_StackTraceCreator$CollectorLegacy_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/CollectorLegacy', 221);
function $clinit_StackTraceCreator$CollectorModern(){
  $clinit_StackTraceCreator$CollectorModern = emptyMethod;
  Error.stackTraceLimit = 64;
}

function $parse(this$static, stString){
  var closeParen, col, endFileUrlIndex, fileName, index_0, lastColonIndex, line, location_0, toReturn;
  if (!stString.length) {
    return this$static.createSte('Unknown', 'anonymous', -1, -1);
  }
  toReturn = $trim(stString);
  $equals(toReturn.substr(0, 3), 'at ') && (toReturn = __substr(toReturn, 3, toReturn.length - 3));
  toReturn = toReturn.replace(/\[.*?\]/g, '');
  index_0 = toReturn.indexOf('(');
  if (index_0 == -1) {
    index_0 = toReturn.indexOf('@');
    if (index_0 == -1) {
      location_0 = toReturn;
      toReturn = '';
    }
     else {
      location_0 = $trim(__substr(toReturn, index_0 + 1, toReturn.length - (index_0 + 1)));
      toReturn = $trim(toReturn.substr(0, index_0));
    }
  }
   else {
    closeParen = toReturn.indexOf(')', index_0);
    location_0 = toReturn.substr(index_0 + 1, closeParen - (index_0 + 1));
    toReturn = $trim(toReturn.substr(0, index_0));
  }
  index_0 = $indexOf_0(toReturn, fromCodePoint(46));
  index_0 != -1 && (toReturn = __substr(toReturn, index_0 + 1, toReturn.length - (index_0 + 1)));
  (!toReturn.length || $equals(toReturn, 'Anonymous function')) && (toReturn = 'anonymous');
  lastColonIndex = $lastIndexOf(location_0, fromCodePoint(58));
  endFileUrlIndex = $lastIndexOf_0(location_0, fromCodePoint(58), lastColonIndex - 1);
  line = -1;
  col = -1;
  fileName = 'Unknown';
  if (lastColonIndex != -1 && endFileUrlIndex != -1) {
    fileName = location_0.substr(0, endFileUrlIndex);
    line = parseInt_0(location_0.substr(endFileUrlIndex + 1, lastColonIndex - (endFileUrlIndex + 1)));
    col = parseInt_0(__substr(location_0, lastColonIndex + 1, location_0.length - (lastColonIndex + 1)));
  }
  return this$static.createSte(fileName, toReturn, line, col);
}

defineClass(429, 428, {});
_.collect = function collect_0(t, jsThrown){
  function fixIE(e){
    if (!('stack' in e)) {
      try {
        throw e;
      }
       catch (ignored) {
      }
    }
    return e;
  }

  var backingJsError;
  typeof jsThrown == 'string'?(backingJsError = fixIE(new Error(jsThrown))):jsThrown instanceof Object && 'stack' in jsThrown?(backingJsError = jsThrown):(backingJsError = fixIE(new Error));
  t.__gwt$backingJsError = backingJsError;
}
;
_.createSte = function createSte(fileName, method, line, col){
  return new StackTraceElement(method, fileName + '@' + col, line < 0?-1:line);
}
;
_.getStackTrace = function getStackTrace_0(t){
  var addIndex, i_0, length_0, stack_0, stackTrace, ste, e;
  stack_0 = ($clinit_StackTraceCreator() , e = t.__gwt$backingJsError , e && e.stack?e.stack.split('\n'):[]);
  stackTrace = initDim(Ljava_lang_StackTraceElement_2_classLit, $intern_2, 63, 0, 0, 1);
  addIndex = 0;
  length_0 = stack_0.length;
  if (length_0 == 0) {
    return stackTrace;
  }
  ste = $parse(this, stack_0[0]);
  $equals(ste.methodName, 'anonymous') || (stackTrace[addIndex++] = ste);
  for (i_0 = 1; i_0 < length_0; i_0++) {
    stackTrace[addIndex++] = $parse(this, stack_0[i_0]);
  }
  return stackTrace;
}
;
var Lcom_google_gwt_core_client_impl_StackTraceCreator$CollectorModern_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/CollectorModern', 429);
function StackTraceCreator$CollectorModernNoSourceMap(){
  $clinit_StackTraceCreator$CollectorModern();
}

defineClass(222, 429, {}, StackTraceCreator$CollectorModernNoSourceMap);
_.createSte = function createSte_0(fileName, method, line, col){
  return new StackTraceElement(method, fileName, -1);
}
;
var Lcom_google_gwt_core_client_impl_StackTraceCreator$CollectorModernNoSourceMap_2_classLit = createForClass('com.google.gwt.core.client.impl', 'StackTraceCreator/CollectorModernNoSourceMap', 222);
function checkArrayType(expression, errorMessage){
  if (!expression) {
    throw new ArrayStoreException_0('' + errorMessage);
  }
}

function checkCriticalArgument(expression, errorMessage){
  if (!expression) {
    throw new IllegalArgumentException('' + errorMessage);
  }
}

function checkCriticalArgument_0(expression, errorMessageArgs){
  if (!expression) {
    throw new IllegalArgumentException(format_0('%s > %s', errorMessageArgs));
  }
}

function checkCriticalElement(expression){
  if (!expression) {
    throw new NoSuchElementException;
  }
}

function checkElementIndex(index_0, size_0){
  if (index_0 < 0 || index_0 >= size_0) {
    throw new IndexOutOfBoundsException_0('Index: ' + index_0 + ', Size: ' + size_0);
  }
}

function checkNotNull(reference){
  if (reference == null) {
    throw new NullPointerException;
  }
  return reference;
}

function checkNotNull_0(reference, errorMessage){
  if (reference == null) {
    throw new NullPointerException_0('' + errorMessage);
  }
}

function checkPositionIndex(index_0, size_0){
  if (index_0 < 0 || index_0 > size_0) {
    throw new IndexOutOfBoundsException_0('Index: ' + index_0 + ', Size: ' + size_0);
  }
}

function checkState(expression){
  if (!expression) {
    throw new IllegalStateException;
  }
}

function format_0(template, args){
  var builder, i_0, placeholderStart, templateStart;
  template = '' + template;
  builder = new StringBuilder_0(template.length + 16 * args.length);
  templateStart = 0;
  i_0 = 0;
  while (i_0 < args.length) {
    placeholderStart = template.indexOf('%s', templateStart);
    if (placeholderStart == -1) {
      break;
    }
    $append_0(builder, template.substr(templateStart, placeholderStart - templateStart));
    $append(builder, args[i_0++]);
    templateStart = placeholderStart + 2;
  }
  $append_0(builder, __substr(template, templateStart, template.length - templateStart));
  if (i_0 < args.length) {
    builder.string += ' [';
    $append(builder, args[i_0++]);
    while (i_0 < args.length) {
      builder.string += ', ';
      $append(builder, args[i_0++]);
    }
    builder.string += ']';
  }
  return builder.string;
}

function $appendChild(this$static, newChild){
  return this$static.appendChild(newChild);
}

function $isOrHasChild(this$static, child){
  return ($clinit_DOMImpl() , impl_1).isOrHasChild(this$static, child);
}

function $removeChild(this$static, oldChild){
  return this$static.removeChild(oldChild);
}

function $getAbsoluteLeft(this$static){
  return ($clinit_DOMImpl() , impl_1).getAbsoluteLeft(this$static);
}

function $getAbsoluteTop(this$static){
  return ($clinit_DOMImpl() , impl_1).getAbsoluteTop(this$static);
}

function $getScrollLeft(this$static){
  return ($clinit_DOMImpl() , impl_1).getScrollLeft_0(this$static);
}

function $getString(this$static){
  return ($clinit_DOMImpl() , impl_1).toString_0(this$static);
}

function $getSubPixelScrollWidth(this$static){
  return this$static.scrollWidth || 0;
}

function $setInnerText(this$static, text_0){
  ($clinit_DOMImpl() , impl_1).setInnerText(this$static, text_0);
}

function $setPropertyString(this$static, name_0, value_0){
  this$static[name_0] = value_0;
}

function $setHeight(this$static, height){
  this$static.height = height;
}

function $setWidth(this$static, width_0){
  this$static.width = width_0;
}

function $clinit_DOMImpl(){
  $clinit_DOMImpl = emptyMethod;
  impl_1 = com_google_gwt_dom_client_DOMImpl();
}

function $getFirstChildElement(elem){
  var child = elem.firstChild;
  while (child && child.nodeType != 1)
    child = child.nextSibling;
  return child;
}

function $getParentElement(node){
  var parent_0 = node.parentNode;
  (!parent_0 || parent_0.nodeType != 1) && (parent_0 = null);
  return parent_0;
}

function $getSubPixelAbsoluteLeft(elem){
  var left = 0;
  var curr = elem;
  while (curr.offsetParent) {
    left -= curr.scrollLeft;
    curr = curr.parentNode;
  }
  while (elem) {
    left += elem.offsetLeft;
    elem = elem.offsetParent;
  }
  return left;
}

function $getSubPixelAbsoluteTop(elem){
  var top_0 = 0;
  var curr = elem;
  while (curr.offsetParent) {
    top_0 -= curr.scrollTop;
    curr = curr.parentNode;
  }
  while (elem) {
    top_0 += elem.offsetTop;
    elem = elem.offsetParent;
  }
  return top_0;
}

function $getSubPixelScrollLeft(elem){
  return elem.scrollLeft || 0;
}

function toInt32(val){
  $clinit_DOMImpl();
  return val | 0;
}

defineClass(461, 1, {});
_.createElement_0 = function createElement(doc, tag){
  return doc.createElement(tag);
}
;
_.createScriptElement = function createScriptElement(doc, source){
  var elem;
  elem = this.createElement_0(doc, 'script');
  elem.text = source;
  return elem;
}
;
_.eventGetButton = function eventGetButton(evt){
  return evt.button | 0;
}
;
_.eventGetCurrentTarget = function eventGetCurrentTarget(event_0){
  return event_0.currentTarget;
}
;
_.getAbsoluteLeft = function getAbsoluteLeft(elem){
  return toInt32($getSubPixelAbsoluteLeft(elem));
}
;
_.getAbsoluteTop = function getAbsoluteTop(elem){
  return toInt32($getSubPixelAbsoluteTop(elem));
}
;
_.getScrollLeft = function getScrollLeft(doc){
  return $getScrollLeft($equals(doc.compatMode, 'CSS1Compat')?doc.documentElement:doc.body);
}
;
_.getScrollLeft_0 = function getScrollLeft_0(elem){
  return toInt32($getSubPixelScrollLeft(elem));
}
;
_.getScrollTop = function getScrollTop(doc){
  return (($equals(doc.compatMode, 'CSS1Compat')?doc.documentElement:doc.body).scrollTop || 0) | 0;
}
;
_.getTabIndex = function getTabIndex(elem){
  return elem.tabIndex;
}
;
_.getTagName = function getTagName(elem){
  return elem.tagName;
}
;
_.setInnerText = function setInnerText(elem, text_0){
  while (elem.firstChild) {
    elem.removeChild(elem.firstChild);
  }
  text_0 != null && elem.appendChild(elem.ownerDocument.createTextNode(text_0));
}
;
_.toString_0 = function toString_3(elem){
  return elem.outerHTML;
}
;
var impl_1;
var Lcom_google_gwt_dom_client_DOMImpl_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImpl', 461);
function $getBoundingClientRectLeft(elem){
  try {
    return elem.getBoundingClientRect().left;
  }
   catch (e) {
    return 0;
  }
}

function $getBoundingClientRectTop(elem){
  try {
    return elem.getBoundingClientRect().top;
  }
   catch (e) {
    return 0;
  }
}

function isOrHasChildImpl(parent_0, child){
  if (parent_0.nodeType != 1 && parent_0.nodeType != 9) {
    return parent_0 == child;
  }
  if (child.nodeType != 1) {
    child = child.parentNode;
    if (!child) {
      return false;
    }
  }
  if (parent_0.nodeType == 9) {
    return parent_0 === child || parent_0.body && parent_0.body.contains(child);
  }
   else {
    return parent_0 === child || parent_0.contains(child);
  }
}

defineClass(468, 461, {});
_.createElement_0 = function createElement_0(doc, tagName){
  var container, elem;
  if (tagName.indexOf(':') != -1) {
    container = (!doc.__gwt_container && (doc.__gwt_container = doc.createElement('div')) , doc.__gwt_container);
    container.innerHTML = '<' + tagName + '/>' || '';
    elem = $getFirstChildElement(($clinit_DOMImpl() , container));
    container.removeChild(elem);
    return elem;
  }
  return doc.createElement(tagName);
}
;
_.eventGetCurrentTarget = function eventGetCurrentTarget_0(event_0){
  return currentEventTarget;
}
;
_.eventGetRelatedTarget = function eventGetRelatedTarget(evt){
  return evt.relatedTarget || (evt.type == 'mouseout'?evt.toElement:evt.fromElement);
}
;
_.eventPreventDefault = function eventPreventDefault(evt){
  evt.returnValue = false;
}
;
_.getTagName = function getTagName_0(elem){
  var scopeName, tagName;
  tagName = elem.tagName;
  scopeName = elem.scopeName;
  if (scopeName == null || $equalsIgnoreCase('html', scopeName)) {
    return tagName;
  }
  return scopeName + ':' + tagName;
}
;
_.isOrHasChild = function isOrHasChild(parent_0, child){
  return isOrHasChildImpl(parent_0, child);
}
;
_.setInnerText = function setInnerText_0(elem, text_0){
  elem.innerText = text_0 || '';
}
;
var currentEventTarget;
var Lcom_google_gwt_dom_client_DOMImplTrident_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplTrident', 468);
function DOMImplIE8(){
  $clinit_DOMImpl();
}

defineClass(329, 468, {}, DOMImplIE8);
_.getAbsoluteLeft = function getAbsoluteLeft_0(elem){
  var doc;
  doc = elem.ownerDocument;
  return $getBoundingClientRectLeft(elem) + impl_1.getScrollLeft(doc);
}
;
_.getAbsoluteTop = function getAbsoluteTop_0(elem){
  var doc;
  doc = elem.ownerDocument;
  return $getBoundingClientRectTop(elem) + impl_1.getScrollTop(doc);
}
;
_.getScrollLeft_0 = function getScrollLeft_1(elem){
  if (elem.currentStyle.direction == 'rtl') {
    return -toInt32($getSubPixelScrollLeft(elem));
  }
  return toInt32($getSubPixelScrollLeft(elem));
}
;
var Lcom_google_gwt_dom_client_DOMImplIE8_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplIE8', 329);
defineClass(469, 461, {});
_.eventGetButton = function eventGetButton_0(evt){
  var button = evt.button;
  if (button == 1) {
    return 4;
  }
   else if (button == 2) {
    return 2;
  }
  return 1;
}
;
_.eventGetRelatedTarget = function eventGetRelatedTarget_0(evt){
  return evt.relatedTarget;
}
;
_.eventPreventDefault = function eventPreventDefault_0(evt){
  evt.preventDefault();
}
;
_.isOrHasChild = function isOrHasChild_0(parent_0, child){
  return parent_0.contains(child);
}
;
_.setInnerText = function setInnerText_1(elem, text_0){
  elem.textContent = text_0 || '';
}
;
var Lcom_google_gwt_dom_client_DOMImplStandard_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplStandard', 469);
function $isRTL(elem){
  return elem.ownerDocument.defaultView.getComputedStyle(elem, '').direction == 'rtl';
}

function getAbsoluteLeftUsingOffsets(elem){
  if (elem.offsetLeft == null) {
    return 0;
  }
  var left = 0;
  var doc = elem.ownerDocument;
  var curr = elem.parentNode;
  if (curr) {
    while (curr.offsetParent) {
      left -= curr.scrollLeft;
      doc.defaultView.getComputedStyle(curr, '').getPropertyValue('direction') == 'rtl' && (left += curr.scrollWidth - curr.clientWidth);
      curr = curr.parentNode;
    }
  }
  while (elem) {
    left += elem.offsetLeft;
    if (doc.defaultView.getComputedStyle(elem, '')['position'] == 'fixed') {
      left += doc.body.scrollLeft;
      return left;
    }
    var parent_0 = elem.offsetParent;
    parent_0 && $wnd.devicePixelRatio && (left += parseInt(doc.defaultView.getComputedStyle(parent_0, '').getPropertyValue('border-left-width')));
    if (parent_0 && parent_0.tagName == 'BODY' && elem.style.position == 'absolute') {
      break;
    }
    elem = parent_0;
  }
  return left;
}

function getAbsoluteTopUsingOffsets(elem){
  if (elem.offsetTop == null) {
    return 0;
  }
  var top_0 = 0;
  var doc = elem.ownerDocument;
  var curr = elem.parentNode;
  if (curr) {
    while (curr.offsetParent) {
      top_0 -= curr.scrollTop;
      curr = curr.parentNode;
    }
  }
  while (elem) {
    top_0 += elem.offsetTop;
    if (doc.defaultView.getComputedStyle(elem, '')['position'] == 'fixed') {
      top_0 += doc.body.scrollTop;
      return top_0;
    }
    var parent_0 = elem.offsetParent;
    parent_0 && $wnd.devicePixelRatio && (top_0 += parseInt(doc.defaultView.getComputedStyle(parent_0, '').getPropertyValue('border-top-width')));
    if (parent_0 && parent_0.tagName == 'BODY' && elem.style.position == 'absolute') {
      break;
    }
    elem = parent_0;
  }
  return top_0;
}

function getBoundingClientRect(element){
  return element.getBoundingClientRect && element.getBoundingClientRect();
}

defineClass(470, 469, {});
_.createScriptElement = function createScriptElement_0(doc, source){
  var elem;
  elem = doc.createElement('script');
  impl_1.setInnerText(elem, source);
  return elem;
}
;
_.eventGetCurrentTarget = function eventGetCurrentTarget_1(event_0){
  return event_0.currentTarget || $wnd;
}
;
_.getAbsoluteLeft = function getAbsoluteLeft_1(elem){
  var left, rect;
  rect = getBoundingClientRect(elem);
  left = rect?rect.left + $getScrollLeft(elem.ownerDocument.body):getAbsoluteLeftUsingOffsets(elem);
  return toInt32(left);
}
;
_.getAbsoluteTop = function getAbsoluteTop_1(elem){
  var rect, top_0, top_1;
  rect = getBoundingClientRect(elem);
  top_0 = rect?rect.top + ((elem.ownerDocument.body.scrollTop || 0) | 0):getAbsoluteTopUsingOffsets(elem);
  return toInt32(top_0);
}
;
_.getScrollLeft = function getScrollLeft_2(doc){
  return doc.documentElement.scrollLeft || doc.body.scrollLeft;
}
;
_.getScrollLeft_0 = function getScrollLeft_3(elem){
  if (!$equalsIgnoreCase('body', impl_1.getTagName(elem)) && $isRTL(elem)) {
    return toInt32($getSubPixelScrollLeft(elem)) - (($getSubPixelScrollWidth(elem) | 0) - (elem.clientWidth | 0));
  }
  return toInt32($getSubPixelScrollLeft(elem));
}
;
_.getScrollTop = function getScrollTop_0(doc){
  return doc.documentElement.scrollTop || doc.body.scrollTop;
}
;
_.getTabIndex = function getTabIndex_0(elem){
  return typeof elem.tabIndex != 'undefined'?elem.tabIndex:-1;
}
;
var Lcom_google_gwt_dom_client_DOMImplStandardBase_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplStandardBase', 470);
function $getBoundingClientRectLeft_0(elem){
  try {
    return elem.getBoundingClientRect().left;
  }
   catch (e) {
    return 0;
  }
}

function $getBoundingClientRectTop_0(elem){
  try {
    return elem.getBoundingClientRect().top;
  }
   catch (e) {
    return 0;
  }
}

function $getParentOffsetDelta(elem){
  var offsetParent = elem.offsetParent;
  if (offsetParent) {
    return offsetParent.offsetWidth - offsetParent.clientWidth;
  }
  return 0;
}

function DOMImplIE9(){
  $clinit_DOMImpl();
}

defineClass(331, 470, {}, DOMImplIE9);
_.getAbsoluteLeft = function getAbsoluteLeft_2(elem){
  var left;
  left = $getBoundingClientRectLeft_0(elem) + $wnd.pageXOffset;
  $isRTL(elem) && (left += $getParentOffsetDelta(elem));
  return toInt32(left);
}
;
_.getAbsoluteTop = function getAbsoluteTop_2(elem){
  return toInt32($getBoundingClientRectTop_0(elem) + $wnd.pageYOffset);
}
;
_.getScrollLeft = function getScrollLeft_4(doc){
  return toInt32($wnd.pageXOffset);
}
;
_.getScrollLeft_0 = function getScrollLeft_5(elem){
  var left;
  left = toInt32(elem.scrollLeft || 0);
  $isRTL(elem) && (left = -left);
  return left;
}
;
_.getScrollTop = function getScrollTop_1(doc){
  return toInt32($wnd.pageYOffset);
}
;
_.getTabIndex = function getTabIndex_1(elem){
  return elem.tabIndex < $intern_3?elem.tabIndex:-(elem.tabIndex % $intern_3) - 1;
}
;
_.isOrHasChild = function isOrHasChild_1(parent_0, child){
  return isOrHasChildImpl(parent_0, child);
}
;
var Lcom_google_gwt_dom_client_DOMImplIE9_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplIE9', 331);
function $getAbsoluteLeftImpl(viewport, elem){
  if (Element.prototype.getBoundingClientRect) {
    return elem.getBoundingClientRect().left + viewport.scrollLeft | 0;
  }
   else {
    var doc = elem.ownerDocument;
    return doc.getBoxObjectFor(elem).screenX - doc.getBoxObjectFor(doc.documentElement).screenX;
  }
}

function $getAbsoluteTopImpl(viewport, elem){
  if (Element.prototype.getBoundingClientRect) {
    return elem.getBoundingClientRect().top + viewport.scrollTop | 0;
  }
   else {
    var doc = elem.ownerDocument;
    return doc.getBoxObjectFor(elem).screenY - doc.getBoxObjectFor(doc.documentElement).screenY;
  }
}

function DOMImplMozilla(){
  $clinit_DOMImpl();
}

function getGeckoVersion(){
  var result = /rv:([0-9]+)\.([0-9]+)(\.([0-9]+))?.*?/.exec(navigator.userAgent.toLowerCase());
  if (result && result.length >= 3) {
    var version = parseInt(result[1]) * 1000000 + parseInt(result[2]) * 1000 + parseInt(result.length >= 5 && !isNaN(result[4])?result[4]:0);
    return version;
  }
  return -1;
}

defineClass(330, 469, {}, DOMImplMozilla);
_.eventGetRelatedTarget = function eventGetRelatedTarget_1(evt){
  var relatedTarget = evt.relatedTarget;
  if (!relatedTarget) {
    return null;
  }
  try {
    var nodeName = relatedTarget.nodeName;
    return relatedTarget;
  }
   catch (e) {
    return null;
  }
}
;
_.getAbsoluteLeft = function getAbsoluteLeft_3(elem){
  return $getAbsoluteLeftImpl($getViewportElement(elem.ownerDocument), elem);
}
;
_.getAbsoluteTop = function getAbsoluteTop_3(elem){
  return $getAbsoluteTopImpl($getViewportElement(elem.ownerDocument), elem);
}
;
_.getScrollLeft_0 = function getScrollLeft_6(elem){
  var geckoVersion, style;
  if (!(geckoVersion = getGeckoVersion() , geckoVersion != -1 && geckoVersion >= 1009000) && (style = elem.ownerDocument.defaultView.getComputedStyle(elem, null) , style.direction == 'rtl')) {
    return toInt32($getSubPixelScrollLeft(elem)) - (($getSubPixelScrollWidth(elem) | 0) - (elem.clientWidth | 0));
  }
  return toInt32($getSubPixelScrollLeft(elem));
}
;
_.isOrHasChild = function isOrHasChild_2(parent_0, child){
  return parent_0 === child || !!(parent_0.compareDocumentPosition(child) & 16);
}
;
_.toString_0 = function toString_4(elem){
  var doc = elem.ownerDocument;
  var temp = elem.cloneNode(true);
  var tempDiv = doc.createElement('DIV');
  tempDiv.appendChild(temp);
  outer = tempDiv.innerHTML;
  temp.innerHTML = '';
  return outer;
}
;
var Lcom_google_gwt_dom_client_DOMImplMozilla_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplMozilla', 330);
function DOMImplWebkit(){
  $clinit_DOMImpl();
}

defineClass(332, 470, {}, DOMImplWebkit);
var Lcom_google_gwt_dom_client_DOMImplWebkit_2_classLit = createForClass('com.google.gwt.dom.client', 'DOMImplWebkit', 332);
function $createCanvasElement(this$static){
  return ($clinit_DOMImpl() , impl_1).createElement_0(this$static, 'canvas');
}

function $createDivElement(this$static){
  return ($clinit_DOMImpl() , impl_1).createElement_0(this$static, 'div');
}

function $createScriptElement(this$static){
  return ($clinit_DOMImpl() , impl_1).createScriptElement(this$static, 'function __gwt_initWindowCloseHandler(beforeunload, unload) {\n  var wnd = window\n  , oldOnBeforeUnload = wnd.onbeforeunload\n  , oldOnUnload = wnd.onunload;\n  \n  wnd.onbeforeunload = function(evt) {\n    var ret, oldRet;\n    try {\n      ret = beforeunload();\n    } finally {\n      oldRet = oldOnBeforeUnload && oldOnBeforeUnload(evt);\n    }\n    // Avoid returning null as IE6 will coerce it into a string.\n    // Ensure that "" gets returned properly.\n    if (ret != null) {\n      return ret;\n    }\n    if (oldRet != null) {\n      return oldRet;\n    }\n    // returns undefined.\n  };\n  \n  wnd.onunload = function(evt) {\n    try {\n      unload();\n    } finally {\n      oldOnUnload && oldOnUnload(evt);\n      wnd.onresize = null;\n      wnd.onscroll = null;\n      wnd.onbeforeunload = null;\n      wnd.onunload = null;\n    }\n  };\n  \n  // Remove the reference once we\'ve initialize the handler\n  wnd.__gwt_initWindowCloseHandler = undefined;\n}\n');
}

function $createTBodyElement(this$static){
  return ($clinit_DOMImpl() , impl_1).createElement_0(this$static, 'tbody');
}

function $createTDElement(this$static){
  return ($clinit_DOMImpl() , impl_1).createElement_0(this$static, 'td');
}

function $createTRElement(this$static){
  return ($clinit_DOMImpl() , impl_1).createElement_0(this$static, 'tr');
}

function $createTableElement(this$static){
  return ($clinit_DOMImpl() , impl_1).createElement_0(this$static, 'table');
}

function $getScrollLeft_0(this$static){
  return ($clinit_DOMImpl() , impl_1).getScrollLeft(this$static);
}

function $getScrollTop(this$static){
  return ($clinit_DOMImpl() , impl_1).getScrollTop(this$static);
}

function $getViewportElement(this$static){
  return $equals(this$static.compatMode, 'CSS1Compat')?this$static.documentElement:this$static.body;
}

function $setPropertyImpl(this$static, name_0, value_0){
  this$static[name_0] = value_0;
}

function $compareTo(this$static, other){
  return this$static.ordinal - other.ordinal;
}

function $toString(this$static){
  return this$static.name_0 != null?this$static.name_0:'' + this$static.ordinal;
}

function Enum(name_0, ordinal){
  this.name_0 = name_0;
  this.ordinal = ordinal;
}

defineClass(15, 1, {3:1, 26:1, 15:1});
_.compareTo = function compareTo(other){
  return $compareTo(this, dynamicCast(other, 15));
}
;
_.equals$ = function equals_0(other){
  return this === other;
}
;
_.hashCode$ = function hashCode_1(){
  return getHashCode(this);
}
;
_.toString$ = function toString_5(){
  return $toString(this);
}
;
_.ordinal = 0;
var Ljava_lang_Enum_2_classLit = createForClass('java.lang', 'Enum', 15);
function $clinit_Style$TextAlign(){
  $clinit_Style$TextAlign = emptyMethod;
  CENTER = new Style$TextAlign$1;
  JUSTIFY = new Style$TextAlign$2;
  LEFT = new Style$TextAlign$3;
  RIGHT = new Style$TextAlign$4;
}

function Style$TextAlign(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_0(){
  $clinit_Style$TextAlign();
  return initValues(getClassLiteralForArray(Lcom_google_gwt_dom_client_Style$TextAlign_2_classLit, 1), $intern_4, 51, 0, [CENTER, JUSTIFY, LEFT, RIGHT]);
}

defineClass(51, 15, $intern_5);
var CENTER, JUSTIFY, LEFT, RIGHT;
var Lcom_google_gwt_dom_client_Style$TextAlign_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/TextAlign', 51, values_0);
function Style$TextAlign$1(){
  Style$TextAlign.call(this, 'CENTER', 0);
}

defineClass(354, 51, $intern_5, Style$TextAlign$1);
var Lcom_google_gwt_dom_client_Style$TextAlign$1_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/TextAlign/1', 354, null);
function Style$TextAlign$2(){
  Style$TextAlign.call(this, 'JUSTIFY', 1);
}

defineClass(355, 51, $intern_5, Style$TextAlign$2);
var Lcom_google_gwt_dom_client_Style$TextAlign$2_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/TextAlign/2', 355, null);
function Style$TextAlign$3(){
  Style$TextAlign.call(this, 'LEFT', 2);
}

defineClass(356, 51, $intern_5, Style$TextAlign$3);
var Lcom_google_gwt_dom_client_Style$TextAlign$3_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/TextAlign/3', 356, null);
function Style$TextAlign$4(){
  Style$TextAlign.call(this, 'RIGHT', 3);
}

defineClass(357, 51, $intern_5, Style$TextAlign$4);
var Lcom_google_gwt_dom_client_Style$TextAlign$4_2_classLit = createForEnum('com.google.gwt.dom.client', 'Style/TextAlign/4', 357, null);
function $getRelativeX(this$static, target){
  return toInt32(($clinit_DOMImpl() , this$static).clientX || 0) - impl_1.getAbsoluteLeft(target) + impl_1.getScrollLeft_0(target) + $getScrollLeft_0(target.ownerDocument);
}

function $getRelativeY(this$static, target){
  return toInt32(($clinit_DOMImpl() , this$static).clientY || 0) - impl_1.getAbsoluteTop(target) + ((target.scrollTop || 0) | 0) + $getScrollTop(target.ownerDocument);
}

defineClass(441, 1, {});
_.toString$ = function toString_6(){
  return 'An event type';
}
;
var Lcom_google_web_bindery_event_shared_Event_2_classLit = createForClass('com.google.web.bindery.event.shared', 'Event', 441);
function $overrideSource(this$static, source){
  this$static.source = source;
}

defineClass(442, 441, {});
_.dead = false;
var Lcom_google_gwt_event_shared_GwtEvent_2_classLit = createForClass('com.google.gwt.event.shared', 'GwtEvent', 442);
function fireNativeEvent(){
}

function CloseEvent_0(){
}

function fire(source){
  var event_0;
  if (TYPE) {
    event_0 = new CloseEvent_0;
    $fireEvent(source, event_0);
  }
}

defineClass(299, 442, {}, CloseEvent_0);
_.dispatch = function dispatch(handler){
  detachWidgets();
}
;
_.getAssociatedType = function getAssociatedType(){
  return TYPE;
}
;
var TYPE;
var Lcom_google_gwt_event_logical_shared_CloseEvent_2_classLit = createForClass('com.google.gwt.event.logical.shared', 'CloseEvent', 299);
defineClass(280, 1, {});
_.hashCode$ = function hashCode_2(){
  return this.index_0;
}
;
_.toString$ = function toString_7(){
  return 'Event type';
}
;
_.index_0 = 0;
var nextHashCode = 0;
var Lcom_google_web_bindery_event_shared_Event$Type_2_classLit = createForClass('com.google.web.bindery.event.shared', 'Event/Type', 280);
function GwtEvent$Type(){
  this.index_0 = ++nextHashCode;
}

defineClass(176, 280, {}, GwtEvent$Type);
var Lcom_google_gwt_event_shared_GwtEvent$Type_2_classLit = createForClass('com.google.gwt.event.shared', 'GwtEvent/Type', 176);
function $addHandler(this$static, type_0, handler){
  return new LegacyHandlerWrapper($doAdd(this$static.eventBus, type_0, handler));
}

function $fireEvent(this$static, event_0){
  var e, oldSource;
  !event_0.dead || (event_0.dead = false , event_0.source = null);
  oldSource = event_0.source;
  $overrideSource(event_0, this$static.source);
  try {
    $doFire(this$static.eventBus, event_0);
  }
   catch ($e0) {
    $e0 = wrap($e0);
    if (instanceOf($e0, 84)) {
      e = $e0;
      throw new UmbrellaException_0(e.causes);
    }
     else 
      throw unwrap($e0);
  }
   finally {
    oldSource == null?(event_0.dead = true , event_0.source = null):(event_0.source = oldSource);
  }
}

defineClass(174, 1, {38:1});
var Lcom_google_gwt_event_shared_HandlerManager_2_classLit = createForClass('com.google.gwt.event.shared', 'HandlerManager', 174);
defineClass(443, 1, {});
var Lcom_google_web_bindery_event_shared_EventBus_2_classLit = createForClass('com.google.web.bindery.event.shared', 'EventBus', 443);
function $defer(this$static, command){
  !this$static.deferredDeltas && (this$static.deferredDeltas = new ArrayList);
  $add_3(this$static.deferredDeltas, command);
}

function $doAdd(this$static, type_0, handler){
  if (!type_0) {
    throw new NullPointerException_0('Cannot add a handler with a null type');
  }
  this$static.firingDepth > 0?$defer(this$static, new SimpleEventBus$2(this$static, type_0, handler)):$doAddNow(this$static, type_0, null, handler);
  return new SimpleEventBus$1;
}

function $doAddNow(this$static, type_0, source, handler){
  var l;
  l = $ensureHandlerList(this$static, type_0, source);
  l.add_1(handler);
}

function $doFire(this$static, event_0){
  var causes, e, handler, handlers, it;
  try {
    ++this$static.firingDepth;
    handlers = $getDispatchList(this$static, event_0.getAssociatedType());
    causes = null;
    it = this$static.isReverseOrder?handlers.listIterator_0(handlers.size_1()):handlers.listIterator();
    while (this$static.isReverseOrder?it.hasPrevious():it.hasNext()) {
      handler = this$static.isReverseOrder?it.previous():it.next_0();
      try {
        event_0.dispatch(dynamicCast(handler, 500));
      }
       catch ($e0) {
        $e0 = wrap($e0);
        if (instanceOf($e0, 13)) {
          e = $e0;
          !causes && (causes = new HashSet);
          $add_4(causes, e);
        }
         else 
          throw unwrap($e0);
      }
    }
    if (causes) {
      throw new UmbrellaException(causes);
    }
  }
   finally {
    --this$static.firingDepth;
    this$static.firingDepth == 0 && $handleQueuedAddsAndRemoves(this$static);
  }
}

function $ensureHandlerList(this$static, type_0, source){
  var handlers, sourceMap;
  sourceMap = dynamicCast($get(this$static.map_0, type_0), 93);
  if (!sourceMap) {
    sourceMap = new HashMap;
    $put(this$static.map_0, type_0, sourceMap);
  }
  handlers = dynamicCast(sourceMap.get_0(source), 37);
  if (!handlers) {
    handlers = new ArrayList;
    sourceMap.put(source, handlers);
  }
  return handlers;
}

function $getDispatchList(this$static, type_0){
  var directHandlers;
  directHandlers = $getHandlerList(this$static, type_0);
  return directHandlers;
}

function $getHandlerList(this$static, type_0){
  var handlers, sourceMap;
  sourceMap = dynamicCast($get(this$static.map_0, type_0), 93);
  if (!sourceMap) {
    return $clinit_Collections() , $clinit_Collections() , EMPTY_LIST;
  }
  handlers = dynamicCast(sourceMap.get_0(null), 37);
  if (!handlers) {
    return $clinit_Collections() , $clinit_Collections() , EMPTY_LIST;
  }
  return handlers;
}

function $handleQueuedAddsAndRemoves(this$static){
  var c, c$iterator;
  if (this$static.deferredDeltas) {
    try {
      for (c$iterator = new AbstractList$IteratorImpl(this$static.deferredDeltas); c$iterator.i < c$iterator.this$01_0.size_1();) {
        c = (checkCriticalElement(c$iterator.i < c$iterator.this$01_0.size_1()) , dynamicCast(c$iterator.this$01_0.get_1(c$iterator.last = c$iterator.i++), 501));
        $doAddNow(c.this$01, c.val$type2, c.val$source3, c.val$handler4);
      }
    }
     finally {
      this$static.deferredDeltas = null;
    }
  }
}

defineClass(281, 443, {});
_.firingDepth = 0;
_.isReverseOrder = false;
var Lcom_google_web_bindery_event_shared_SimpleEventBus_2_classLit = createForClass('com.google.web.bindery.event.shared', 'SimpleEventBus', 281);
function HandlerManager$Bus(){
  this.map_0 = new HashMap;
  this.isReverseOrder = false;
}

defineClass(282, 281, {}, HandlerManager$Bus);
var Lcom_google_gwt_event_shared_HandlerManager$Bus_2_classLit = createForClass('com.google.gwt.event.shared', 'HandlerManager/Bus', 282);
function LegacyHandlerWrapper(){
}

defineClass(328, 1, {}, LegacyHandlerWrapper);
var Lcom_google_gwt_event_shared_LegacyHandlerWrapper_2_classLit = createForClass('com.google.gwt.event.shared', 'LegacyHandlerWrapper', 328);
function UmbrellaException(causes){
  RuntimeException_1.call(this, makeMessage(causes), makeCause(causes));
  this.causes = causes;
}

function makeCause(causes){
  var iterator;
  iterator = causes.iterator();
  if (!iterator.hasNext()) {
    return null;
  }
  return dynamicCast(iterator.next_0(), 13);
}

function makeMessage(causes){
  var b, count, first, t, t$iterator;
  count = causes.size_1();
  if (count == 0) {
    return null;
  }
  b = new StringBuilder_1(count == 1?'Exception caught: ':count + ' exceptions caught: ');
  first = true;
  for (t$iterator = causes.iterator(); t$iterator.hasNext();) {
    t = dynamicCast(t$iterator.next_0(), 13);
    first?(first = false):(b.string += '; ' , b);
    $append_0(b, t.getMessage());
  }
  return b.string;
}

defineClass(84, 17, $intern_6, UmbrellaException);
var Lcom_google_web_bindery_event_shared_UmbrellaException_2_classLit = createForClass('com.google.web.bindery.event.shared', 'UmbrellaException', 84);
function UmbrellaException_0(causes){
  UmbrellaException.call(this, causes);
}

defineClass(163, 84, $intern_6, UmbrellaException_0);
var Lcom_google_gwt_event_shared_UmbrellaException_2_classLit = createForClass('com.google.gwt.event.shared', 'UmbrellaException', 163);
function throwIfNull(value_0){
  if (null == value_0) {
    throw new NullPointerException_0('encodedURLComponent cannot be null');
  }
}

function getDirectionOnElement(elem){
  var dirPropertyValue;
  dirPropertyValue = elem['dir'] == null?null:String(elem['dir']);
  if ($equalsIgnoreCase('rtl', dirPropertyValue)) {
    return $clinit_HasDirection$Direction() , RTL;
  }
   else if ($equalsIgnoreCase('ltr', dirPropertyValue)) {
    return $clinit_HasDirection$Direction() , LTR;
  }
  return $clinit_HasDirection$Direction() , DEFAULT;
}

function setDirectionOnElement(elem, direction){
  switch (direction.ordinal) {
    case 0:
      {
        $setPropertyString(elem, 'dir', 'rtl');
        break;
      }

    case 1:
      {
        $setPropertyString(elem, 'dir', 'ltr');
        break;
      }

    case 2:
      {
        getDirectionOnElement(elem) != ($clinit_HasDirection$Direction() , DEFAULT) && $setPropertyString(elem, 'dir', '');
        break;
      }

  }
}

function $clinit_HasDirection$Direction(){
  $clinit_HasDirection$Direction = emptyMethod;
  RTL = new HasDirection$Direction('RTL', 0);
  LTR = new HasDirection$Direction('LTR', 1);
  DEFAULT = new HasDirection$Direction('DEFAULT', 2);
}

function HasDirection$Direction(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_1(){
  $clinit_HasDirection$Direction();
  return initValues(getClassLiteralForArray(Lcom_google_gwt_i18n_client_HasDirection$Direction_2_classLit, 1), $intern_4, 99, 0, [RTL, LTR, DEFAULT]);
}

defineClass(99, 15, {99:1, 3:1, 26:1, 15:1}, HasDirection$Direction);
var DEFAULT, LTR, RTL;
var Lcom_google_gwt_i18n_client_HasDirection$Direction_2_classLit = createForEnum('com.google.gwt.i18n.client', 'HasDirection/Direction', 99, values_1);
function clone(array){
  return cloneSubrange(array, array.length);
}

function cloneSubrange(array, toIndex){
  var result;
  result = array.slice(0, toIndex);
  initValues(getClass__Ljava_lang_Class___devirtual$(array), array.castableTypeMap$, array.__elementTypeId$, array.__elementTypeCategory$, result);
  return result;
}

function createFrom(array, length_0){
  var result;
  result = initializeArrayElementsWithDefaults(0, length_0);
  initValues(getClass__Ljava_lang_Class___devirtual$(array), array.castableTypeMap$, array.__elementTypeId$, array.__elementTypeCategory$, result);
  return result;
}

function getClassLiteralForArray(clazz, dimensions){
  return getClassLiteralForArray_0(clazz, dimensions);
}

function initDim(leafClassLiteral, castableTypeMap, elementTypeId, length_0, elementTypeCategory, dimensions){
  var result;
  result = initializeArrayElementsWithDefaults(elementTypeCategory, length_0);
  initValues(getClassLiteralForArray(leafClassLiteral, dimensions), castableTypeMap, elementTypeId, elementTypeCategory, result);
  return result;
}

function initValues(arrayClass, castableTypeMap, elementTypeId, elementTypeCategory, array){
  array.___clazz$ = arrayClass;
  array.castableTypeMap$ = castableTypeMap;
  array.typeMarker$ = typeMarkerFn;
  array.__elementTypeId$ = elementTypeId;
  array.__elementTypeCategory$ = elementTypeCategory;
  return array;
}

function initializeArrayElementsWithDefaults(elementTypeCategory, length_0){
  var array = new Array(length_0);
  var initValue;
  switch (elementTypeCategory) {
    case 6:
      initValue = {l:0, m:0, h:0};
      break;
    case 7:
      initValue = 0;
      break;
    case 8:
      initValue = false;
      break;
    default:return array;
  }
  for (var i_0 = 0; i_0 < length_0; ++i_0) {
    array[i_0] = initValue;
  }
  return array;
}

function nativeArraySplice(src_0, srcOfs, dest, destOfs, len, overwrite){
  if (src_0 === dest) {
    src_0 = src_0.slice(srcOfs, srcOfs + len);
    srcOfs = 0;
  }
  for (var batchStart = srcOfs, end = srcOfs + len; batchStart < end;) {
    var batchEnd = Math.min(batchStart + 10000, end);
    len = batchEnd - batchStart;
    Array.prototype.splice.apply(dest, [destOfs, overwrite?len:0].concat(src_0.slice(batchStart, batchEnd)));
    batchStart = batchEnd;
    destOfs += len;
  }
}

function setCheck(array, index_0, value_0){
  var elementTypeId;
  if (value_0 != null) {
    switch (array.__elementTypeCategory$) {
      case 4:
        if (!isJavaString(value_0)) {
          throw new ArrayStoreException;
        }

        break;
      case 0:
        {
          elementTypeId = array.__elementTypeId$;
          if (!canCast(value_0, elementTypeId)) {
            throw new ArrayStoreException;
          }
          break;
        }

      case 2:
        if (!(!isJavaString(value_0) && !hasTypeMarker(value_0))) {
          throw new ArrayStoreException;
        }

        break;
      case 1:
        {
          elementTypeId = array.__elementTypeId$;
          if (!(!isJavaString(value_0) && !hasTypeMarker(value_0)) && !canCast(value_0, elementTypeId)) {
            throw new ArrayStoreException;
          }
          break;
        }

    }
  }
  return array[index_0] = value_0;
}

function cacheJavaScriptException(e, jse){
  if (e && typeof e == 'object') {
    try {
      e.__gwt$exception = jse;
    }
     catch (ignored) {
    }
  }
}

function unwrap(e){
  var jse;
  if (instanceOf(e, 62)) {
    jse = dynamicCast(e, 62);
    if (maskUndefined(jse.e) !== maskUndefined(($clinit_JavaScriptException() , NOT_SET))) {
      return maskUndefined(jse.e) === maskUndefined(NOT_SET)?null:jse.e;
    }
  }
  return e;
}

function wrap(e){
  var jse;
  if (instanceOf(e, 13)) {
    return e;
  }
  jse = e && e.__gwt$exception;
  if (!jse) {
    jse = new JavaScriptException(e);
    captureStackTrace(jse, e);
    cacheJavaScriptException(e, jse);
  }
  return jse;
}

function create_0(value_0){
  var a0, a1, a2;
  a0 = value_0 & $intern_7;
  a1 = value_0 >> 22 & $intern_7;
  a2 = value_0 < 0?$intern_8:0;
  return create0(a0, a1, a2);
}

function create_1(a){
  return create0(a.l, a.m, a.h);
}

function create0(l, m, h){
  return {l:l, m:m, h:h};
}

function divMod(a, b, computeRemainder){
  var aIsCopy, aIsMinValue, aIsNegative, bpower, c, negative;
  if (b.l == 0 && b.m == 0 && b.h == 0) {
    throw new ArithmeticException;
  }
  if (a.l == 0 && a.m == 0 && a.h == 0) {
    computeRemainder && (remainder = create0(0, 0, 0));
    return create0(0, 0, 0);
  }
  if (b.h == $intern_9 && b.m == 0 && b.l == 0) {
    return divModByMinValue(a, computeRemainder);
  }
  negative = false;
  if (b.h >> 19 != 0) {
    b = neg(b);
    negative = true;
  }
  bpower = powerOfTwo(b);
  aIsNegative = false;
  aIsMinValue = false;
  aIsCopy = false;
  if (a.h == $intern_9 && a.m == 0 && a.l == 0) {
    aIsMinValue = true;
    aIsNegative = true;
    if (bpower == -1) {
      a = create_1(($clinit_LongLib$Const() , MAX_VALUE));
      aIsCopy = true;
      negative = !negative;
    }
     else {
      c = shr(a, bpower);
      negative && negate(c);
      computeRemainder && (remainder = create0(0, 0, 0));
      return c;
    }
  }
   else if (a.h >> 19 != 0) {
    aIsNegative = true;
    a = neg(a);
    aIsCopy = true;
    negative = !negative;
  }
  if (bpower != -1) {
    return divModByShift(a, bpower, negative, aIsNegative, computeRemainder);
  }
  if (!gte_0(a, b)) {
    computeRemainder && (aIsNegative?(remainder = neg(a)):(remainder = create0(a.l, a.m, a.h)));
    return create0(0, 0, 0);
  }
  return divModHelper(aIsCopy?a:create0(a.l, a.m, a.h), b, negative, aIsNegative, aIsMinValue, computeRemainder);
}

function divModByMinValue(a, computeRemainder){
  if (a.h == $intern_9 && a.m == 0 && a.l == 0) {
    computeRemainder && (remainder = create0(0, 0, 0));
    return create_1(($clinit_LongLib$Const() , ONE));
  }
  computeRemainder && (remainder = create0(a.l, a.m, a.h));
  return create0(0, 0, 0);
}

function divModByShift(a, bpower, negative, aIsNegative, computeRemainder){
  var c;
  c = shr(a, bpower);
  negative && negate(c);
  if (computeRemainder) {
    a = maskRight(a, bpower);
    aIsNegative?(remainder = neg(a)):(remainder = create0(a.l, a.m, a.h));
  }
  return c;
}

function divModHelper(a, b, negative, aIsNegative, aIsMinValue, computeRemainder){
  var bshift, gte, quotient, shift_0, a1, a2, a0;
  shift_0 = numberOfLeadingZeros(b) - numberOfLeadingZeros(a);
  bshift = shl(b, shift_0);
  quotient = create0(0, 0, 0);
  while (shift_0 >= 0) {
    gte = trialSubtract(a, bshift);
    if (gte) {
      shift_0 < 22?(quotient.l |= 1 << shift_0 , undefined):shift_0 < 44?(quotient.m |= 1 << shift_0 - 22 , undefined):(quotient.h |= 1 << shift_0 - 44 , undefined);
      if (a.l == 0 && a.m == 0 && a.h == 0) {
        break;
      }
    }
    a1 = bshift.m;
    a2 = bshift.h;
    a0 = bshift.l;
    setH(bshift, a2 >>> 1);
    bshift.m = a1 >>> 1 | (a2 & 1) << 21;
    bshift.l = a0 >>> 1 | (a1 & 1) << 21;
    --shift_0;
  }
  negative && negate(quotient);
  if (computeRemainder) {
    if (aIsNegative) {
      remainder = neg(a);
      aIsMinValue && (remainder = sub_0(remainder, ($clinit_LongLib$Const() , ONE)));
    }
     else {
      remainder = create0(a.l, a.m, a.h);
    }
  }
  return quotient;
}

function maskRight(a, bits){
  var b0, b1, b2;
  if (bits <= 22) {
    b0 = a.l & (1 << bits) - 1;
    b1 = b2 = 0;
  }
   else if (bits <= 44) {
    b0 = a.l;
    b1 = a.m & (1 << bits - 22) - 1;
    b2 = 0;
  }
   else {
    b0 = a.l;
    b1 = a.m;
    b2 = a.h & (1 << bits - 44) - 1;
  }
  return create0(b0, b1, b2);
}

function negate(a){
  var neg0, neg1, neg2;
  neg0 = ~a.l + 1 & $intern_7;
  neg1 = ~a.m + (neg0 == 0?1:0) & $intern_7;
  neg2 = ~a.h + (neg0 == 0 && neg1 == 0?1:0) & $intern_8;
  setL(a, neg0);
  setM(a, neg1);
  setH(a, neg2);
}

function numberOfLeadingZeros(a){
  var b1, b2;
  b2 = numberOfLeadingZeros_0(a.h);
  if (b2 == 32) {
    b1 = numberOfLeadingZeros_0(a.m);
    return b1 == 32?numberOfLeadingZeros_0(a.l) + 32:b1 + 20 - 10;
  }
   else {
    return b2 - 12;
  }
}

function powerOfTwo(a){
  var h, l, m;
  l = a.l;
  if ((l & l - 1) != 0) {
    return -1;
  }
  m = a.m;
  if ((m & m - 1) != 0) {
    return -1;
  }
  h = a.h;
  if ((h & h - 1) != 0) {
    return -1;
  }
  if (h == 0 && m == 0 && l == 0) {
    return -1;
  }
  if (h == 0 && m == 0 && l != 0) {
    return numberOfTrailingZeros(l);
  }
  if (h == 0 && m != 0 && l == 0) {
    return numberOfTrailingZeros(m) + 22;
  }
  if (h != 0 && m == 0 && l == 0) {
    return numberOfTrailingZeros(h) + 44;
  }
  return -1;
}

function setH(a, x_0){
  a.h = x_0;
}

function setL(a, x_0){
  a.l = x_0;
}

function setM(a, x_0){
  a.m = x_0;
}

function toDoubleHelper(a){
  return a.l + a.m * $intern_10 + a.h * $intern_11;
}

function trialSubtract(a, b){
  var sum0, sum1, sum2;
  sum2 = a.h - b.h;
  if (sum2 < 0) {
    return false;
  }
  sum0 = a.l - b.l;
  sum1 = a.m - b.m + (sum0 >> 22);
  sum2 += sum1 >> 22;
  if (sum2 < 0) {
    return false;
  }
  setL(a, sum0 & $intern_7);
  setM(a, sum1 & $intern_7);
  setH(a, sum2 & $intern_8);
  return true;
}

var remainder;
function add_0(a, b){
  var sum0, sum1, sum2;
  sum0 = a.l + b.l;
  sum1 = a.m + b.m + (sum0 >> 22);
  sum2 = a.h + b.h + (sum1 >> 22);
  return {l:sum0 & $intern_7, m:sum1 & $intern_7, h:sum2 & $intern_8};
}

function eq(a, b){
  return a.l == b.l && a.m == b.m && a.h == b.h;
}

function fromDouble(value_0){
  var a0, a1, a2, negative, result;
  if (isNaN_0(value_0)) {
    return $clinit_LongLib$Const() , ZERO;
  }
  if (value_0 < $intern_12) {
    return $clinit_LongLib$Const() , MIN_VALUE;
  }
  if (value_0 >= 9223372036854775807) {
    return $clinit_LongLib$Const() , MAX_VALUE;
  }
  negative = false;
  if (value_0 < 0) {
    negative = true;
    value_0 = -value_0;
  }
  a2 = 0;
  if (value_0 >= $intern_11) {
    a2 = round_int(value_0 / $intern_11);
    value_0 -= a2 * $intern_11;
  }
  a1 = 0;
  if (value_0 >= $intern_10) {
    a1 = round_int(value_0 / $intern_10);
    value_0 -= a1 * $intern_10;
  }
  a0 = round_int(value_0);
  result = create0(a0, a1, a2);
  negative && negate(result);
  return result;
}

function fromInt(value_0){
  var rebase, result;
  if (value_0 > -129 && value_0 < 128) {
    rebase = value_0 + 128;
    boxedValues == null && (boxedValues = initDim(Lcom_google_gwt_lang_LongLibBase$LongEmul_2_classLit, $intern_4, 506, 256, 0, 1));
    result = boxedValues[rebase];
    !result && (result = boxedValues[rebase] = create_0(value_0));
    return result;
  }
  return create_0(value_0);
}

function gt(a, b){
  var signa, signb;
  signa = a.h >> 19;
  signb = b.h >> 19;
  return signa == 0?signb != 0 || a.h > b.h || a.h == b.h && a.m > b.m || a.h == b.h && a.m == b.m && a.l > b.l:!(signb == 0 || a.h < b.h || a.h == b.h && a.m < b.m || a.h == b.h && a.m == b.m && a.l <= b.l);
}

function gte_0(a, b){
  var signa, signb;
  signa = a.h >> 19;
  signb = b.h >> 19;
  return signa == 0?signb != 0 || a.h > b.h || a.h == b.h && a.m > b.m || a.h == b.h && a.m == b.m && a.l >= b.l:!(signb == 0 || a.h < b.h || a.h == b.h && a.m < b.m || a.h == b.h && a.m == b.m && a.l < b.l);
}

function lt(a, b){
  return !gte_0(a, b);
}

function neg(a){
  var neg0, neg1, neg2;
  neg0 = ~a.l + 1 & $intern_7;
  neg1 = ~a.m + (neg0 == 0?1:0) & $intern_7;
  neg2 = ~a.h + (neg0 == 0 && neg1 == 0?1:0) & $intern_8;
  return create0(neg0, neg1, neg2);
}

function neq(a, b){
  return a.l != b.l || a.m != b.m || a.h != b.h;
}

function or(a, b){
  return {l:a.l | b.l, m:a.m | b.m, h:a.h | b.h};
}

function shl(a, n){
  var res0, res1, res2;
  n &= 63;
  if (n < 22) {
    res0 = a.l << n;
    res1 = a.m << n | a.l >> 22 - n;
    res2 = a.h << n | a.m >> 22 - n;
  }
   else if (n < 44) {
    res0 = 0;
    res1 = a.l << n - 22;
    res2 = a.m << n - 22 | a.l >> 44 - n;
  }
   else {
    res0 = 0;
    res1 = 0;
    res2 = a.l << n - 44;
  }
  return {l:res0 & $intern_7, m:res1 & $intern_7, h:res2 & $intern_8};
}

function shr(a, n){
  var a2, negative, res0, res1, res2;
  n &= 63;
  a2 = a.h;
  negative = (a2 & $intern_9) != 0;
  negative && (a2 |= -1048576);
  if (n < 22) {
    res2 = a2 >> n;
    res1 = a.m >> n | a2 << 22 - n;
    res0 = a.l >> n | a.m << 22 - n;
  }
   else if (n < 44) {
    res2 = negative?$intern_8:0;
    res1 = a2 >> n - 22;
    res0 = a.m >> n - 22 | a2 << 44 - n;
  }
   else {
    res2 = negative?$intern_8:0;
    res1 = negative?$intern_7:0;
    res0 = a2 >> n - 44;
  }
  return {l:res0 & $intern_7, m:res1 & $intern_7, h:res2 & $intern_8};
}

function shru(a, n){
  var a2, res0, res1, res2;
  n &= 63;
  a2 = a.h & $intern_8;
  if (n < 22) {
    res2 = a2 >>> n;
    res1 = a.m >> n | a2 << 22 - n;
    res0 = a.l >> n | a.m << 22 - n;
  }
   else if (n < 44) {
    res2 = 0;
    res1 = a2 >>> n - 22;
    res0 = a.m >> n - 22 | a.h << 44 - n;
  }
   else {
    res2 = 0;
    res1 = 0;
    res0 = a2 >>> n - 44;
  }
  return {l:res0 & $intern_7, m:res1 & $intern_7, h:res2 & $intern_8};
}

function sub_0(a, b){
  var sum0, sum1, sum2;
  sum0 = a.l - b.l;
  sum1 = a.m - b.m + (sum0 >> 22);
  sum2 = a.h - b.h + (sum1 >> 22);
  return {l:sum0 & $intern_7, m:sum1 & $intern_7, h:sum2 & $intern_8};
}

function toDouble(a){
  if (eq(a, ($clinit_LongLib$Const() , MIN_VALUE))) {
    return $intern_12;
  }
  if (!gte_0(a, ZERO)) {
    return -toDoubleHelper(neg(a));
  }
  return a.l + a.m * $intern_10 + a.h * $intern_11;
}

function toInt(a){
  return a.l | a.m << 22;
}

function toString_8(a){
  var digits, rem, res, tenPowerLong, zeroesNeeded;
  if (a.l == 0 && a.m == 0 && a.h == 0) {
    return '0';
  }
  if (a.h == $intern_9 && a.m == 0 && a.l == 0) {
    return '-9223372036854775808';
  }
  if (a.h >> 19 != 0) {
    return '-' + toString_8(neg(a));
  }
  rem = a;
  res = '';
  while (!(rem.l == 0 && rem.m == 0 && rem.h == 0)) {
    tenPowerLong = fromInt(1000000000);
    rem = divMod(rem, tenPowerLong, true);
    digits = '' + toInt(remainder);
    if (!(rem.l == 0 && rem.m == 0 && rem.h == 0)) {
      zeroesNeeded = 9 - digits.length;
      for (; zeroesNeeded > 0; zeroesNeeded--) {
        digits = '0' + digits;
      }
    }
    res = digits + res;
  }
  return res;
}

function xor(a, b){
  return {l:a.l ^ b.l, m:a.m ^ b.m, h:a.h ^ b.h};
}

var boxedValues;
function $clinit_LongLib$Const(){
  $clinit_LongLib$Const = emptyMethod;
  MAX_VALUE = create0($intern_7, $intern_7, 524287);
  MIN_VALUE = create0(0, 0, $intern_9);
  ONE = fromInt(1);
  fromInt(2);
  ZERO = fromInt(0);
}

var MAX_VALUE, MIN_VALUE, ONE, ZERO;
function hasTypeMarker(o){
  return o.typeMarker$ === typeMarkerFn;
}

function init(){
  $wnd.setTimeout($entry(assertCompileTimeUserAgent));
  $onModuleLoad_0();
  $onModuleLoad($clinit_LogConfiguration());
  $onModuleLoad_1(new G3MWebGLTestingApplication);
}

function $getLevel(this$static){
  if (this$static.level) {
    return this$static.level;
  }
  return $clinit_Level() , ALL;
}

function $setFormatter(this$static, newFormatter){
  this$static.formatter = newFormatter;
}

function $setLevel(this$static, newLevel){
  this$static.level = newLevel;
}

defineClass(98, 1, {98:1});
var Ljava_util_logging_Handler_2_classLit = createForClass('java.util.logging', 'Handler', 98);
function ConsoleLogHandler(){
  $setFormatter(this, new TextLogFormatter(true));
  $setLevel(this, ($clinit_Level() , ALL));
}

defineClass(290, 98, {98:1}, ConsoleLogHandler);
_.publish = function publish(record){
  var msg, val;
  if (!window.console || ($getLevel(this) , -2147483648 > record.level.intValue_0())) {
    return;
  }
  msg = $format(this.formatter, record);
  val = record.level.intValue_0();
  val >= ($clinit_Level() , 1000)?(window.console.error(msg) , undefined):val >= 900?(window.console.warn(msg) , undefined):val >= 800?(window.console.info(msg) , undefined):(window.console.log(msg) , undefined);
}
;
var Lcom_google_gwt_logging_client_ConsoleLogHandler_2_classLit = createForClass('com.google.gwt.logging.client', 'ConsoleLogHandler', 290);
function DevelopmentModeLogHandler(){
  $setFormatter(this, new TextLogFormatter(false));
  $setLevel(this, ($clinit_Level() , ALL));
}

defineClass(291, 98, {98:1}, DevelopmentModeLogHandler);
_.publish = function publish_0(record){
  return;
}
;
var Lcom_google_gwt_logging_client_DevelopmentModeLogHandler_2_classLit = createForClass('com.google.gwt.logging.client', 'DevelopmentModeLogHandler', 291);
function $clinit_LogConfiguration(){
  $clinit_LogConfiguration = emptyMethod;
  impl_2 = new LogConfiguration$LogConfigurationImplRegular;
}

function $onModuleLoad(){
  var log_0;
  $configureClientSideLogging(impl_2);
  if (!uncaughtExceptionHandler) {
    log_0 = getLogger(($ensureNamesAreInitialized(Lcom_google_gwt_logging_client_LogConfiguration_2_classLit) , Lcom_google_gwt_logging_client_LogConfiguration_2_classLit.typeName));
    setUncaughtExceptionHandler(new LogConfiguration$1(log_0));
  }
}

var impl_2;
var Lcom_google_gwt_logging_client_LogConfiguration_2_classLit = createForClass('com.google.gwt.logging.client', 'LogConfiguration', null);
function LogConfiguration$1(val$log){
  this.val$log2 = val$log;
}

defineClass(217, 1, {}, LogConfiguration$1);
var Lcom_google_gwt_logging_client_LogConfiguration$1_2_classLit = createForClass('com.google.gwt.logging.client', 'LogConfiguration/1', 217);
function $configureClientSideLogging(this$static){
  this$static.root = (new LoggerImplRegular , $ensureLogger(getLogManager(), ''));
  this$static.root.impl.useParentHandlers = false;
  $setLevels(this$static.root);
  $setDefaultHandlers(this$static.root);
}

function $setDefaultHandlers(l){
  var console_0, dev;
  console_0 = new ConsoleLogHandler;
  $addHandler_0(l.impl, console_0);
  dev = new DevelopmentModeLogHandler;
  $addHandler_0(l.impl, dev);
}

function $setLevels(l){
  var level, levelParam, paramsForName;
  levelParam = (ensureListParameterMap() , paramsForName = dynamicCast(listParamMap.get_0('logLevel'), 37) , !paramsForName?null:dynamicCastToString(paramsForName.get_1(paramsForName.size_1() - 1)));
  level = levelParam == null?null:($clinit_Level() , $parse_0(levelParam));
  level?$setLevel_0(l.impl, level):$setLevel_1(l, ($clinit_Level() , ALL));
}

function LogConfiguration$LogConfigurationImplRegular(){
}

defineClass(216, 1, {}, LogConfiguration$LogConfigurationImplRegular);
var Lcom_google_gwt_logging_client_LogConfiguration$LogConfigurationImplRegular_2_classLit = createForClass('com.google.gwt.logging.client', 'LogConfiguration/LogConfigurationImplRegular', 216);
defineClass(459, 1, {});
var Ljava_util_logging_Formatter_2_classLit = createForClass('java.util.logging', 'Formatter', 459);
defineClass(460, 459, {});
var Lcom_google_gwt_logging_impl_FormatterImpl_2_classLit = createForClass('com.google.gwt.logging.impl', 'FormatterImpl', 460);
function $format(this$static, event_0){
  var message, date, s;
  message = new StringBuilder;
  $append_0(message, (date = new Date_0(event_0.millis) , s = new StringBuilder , $append_0(s, $toString_2(date)) , s.string += ' ' , $append_0(s, event_0.loggerName) , s.string += '\n' , $append_0(s, event_0.level.getName()) , s.string += ': ' , s.string));
  $append_0(message, event_0.msg);
  this$static.showStackTraces && !!event_0.thrown && $printStackTrace(event_0.thrown, new StackTracePrintStream(message));
  return message.string;
}

function TextLogFormatter(showStackTraces){
  this.showStackTraces = showStackTraces;
}

defineClass(180, 460, {}, TextLogFormatter);
_.showStackTraces = false;
var Lcom_google_gwt_logging_client_TextLogFormatter_2_classLit = createForClass('com.google.gwt.logging.client', 'TextLogFormatter', 180);
function $parse_0(name_0){
  name_0 = name_0.toUpperCase();
  if ($equals(name_0, 'ALL')) {
    return $clinit_Level() , ALL;
  }
   else if ($equals(name_0, 'CONFIG')) {
    return $clinit_Level() , CONFIG;
  }
   else if ($equals(name_0, 'FINE')) {
    return $clinit_Level() , FINE;
  }
   else if ($equals(name_0, 'FINER')) {
    return $clinit_Level() , FINER;
  }
   else if ($equals(name_0, 'FINEST')) {
    return $clinit_Level() , FINEST;
  }
   else if ($equals(name_0, 'INFO')) {
    return $clinit_Level() , INFO;
  }
   else if ($equals(name_0, 'OFF')) {
    return $clinit_Level() , OFF;
  }
   else if ($equals(name_0, 'SEVERE')) {
    return $clinit_Level() , SEVERE;
  }
   else if ($equals(name_0, 'WARNING')) {
    return $clinit_Level() , WARNING;
  }
  throw new IllegalArgumentException('Invalid level "' + name_0 + '"');
}

function $addHandler_0(this$static, handler){
  $add_3(this$static.handlers, handler);
}

function $getEffectiveLevel(this$static){
  var effectiveLevel, logger;
  if (this$static.level) {
    return this$static.level;
  }
  logger = this$static.parent_0;
  while (logger) {
    effectiveLevel = logger.impl.level;
    if (effectiveLevel) {
      return effectiveLevel;
    }
    logger = logger.impl.parent_0;
  }
  return $clinit_Level() , INFO;
}

function $getHandlers(this$static){
  return dynamicCast($toArray(this$static.handlers, initDim(Ljava_util_logging_Handler_2_classLit, $intern_13, 98, this$static.handlers.array.length, 0, 1)), 214);
}

function $isLoggable(this$static, messageLevel){
  return $getEffectiveLevel(this$static).intValue_0() <= messageLevel.intValue_0();
}

function $log(this$static, level, msg, thrown){
  var record;
  if ($getEffectiveLevel(this$static).intValue_0() <= level.intValue_0()) {
    record = new LogRecord(level, msg);
    record.thrown = thrown;
    $setLoggerName(record, this$static.name_0);
    $log_0(this$static, record);
  }
}

function $log_0(this$static, record){
  var handler, handler$array, handler$array0, handler$index, handler$index0, handler$max, handler$max0, logger;
  if ($isLoggable(this$static, record.level)) {
    for (handler$array0 = dynamicCast($toArray(this$static.handlers, initDim(Ljava_util_logging_Handler_2_classLit, $intern_13, 98, this$static.handlers.array.length, 0, 1)), 214) , handler$index0 = 0 , handler$max0 = handler$array0.length; handler$index0 < handler$max0; ++handler$index0) {
      handler = handler$array0[handler$index0];
      handler.publish(record);
    }
    logger = this$static.useParentHandlers?this$static.parent_0:null;
    while (logger) {
      for (handler$array = $getHandlers(logger.impl) , handler$index = 0 , handler$max = handler$array.length; handler$index < handler$max; ++handler$index) {
        handler = handler$array[handler$index];
        handler.publish(record);
      }
      logger = logger.impl.useParentHandlers?logger.impl.parent_0:null;
    }
  }
}

function $setLevel_0(this$static, newLevel){
  this$static.level = newLevel;
}

function $setName(this$static, newName){
  this$static.name_0 = newName;
}

function $setParent_0(this$static, newParent){
  !!newParent && (this$static.parent_0 = newParent);
}

function LoggerImplRegular(){
  this.useParentHandlers = true;
  this.handlers = new ArrayList;
}

defineClass(114, 1, {}, LoggerImplRegular);
_.level = null;
_.useParentHandlers = false;
var Lcom_google_gwt_logging_impl_LoggerImplRegular_2_classLit = createForClass('com.google.gwt.logging.impl', 'LoggerImplRegular', 114);
defineClass(437, 1, {});
var Ljava_io_OutputStream_2_classLit = createForClass('java.io', 'OutputStream', 437);
defineClass(438, 437, {});
var Ljava_io_FilterOutputStream_2_classLit = createForClass('java.io', 'FilterOutputStream', 438);
defineClass(439, 438, {});
var Ljava_io_PrintStream_2_classLit = createForClass('java.io', 'PrintStream', 439);
function StackTracePrintStream(builder){
  this.builder = builder;
}

defineClass(315, 439, {}, StackTracePrintStream);
var Lcom_google_gwt_logging_impl_StackTracePrintStream_2_classLit = createForClass('com.google.gwt.logging.impl', 'StackTracePrintStream', 315);
function $clinit_DOM(){
  $clinit_DOM = emptyMethod;
  impl_3 = com_google_gwt_user_client_impl_DOMImpl();
}

function dispatchEvent_1(evt, elem, listener){
  $clinit_DOM();
  var prevCurrentEvent;
  prevCurrentEvent = currentEvent;
  currentEvent = evt;
  elem == sCaptureElem && $eventGetTypeInt(($clinit_DOMImpl() , evt).type) == 8192 && (sCaptureElem = null);
  listener.onBrowserEvent(evt);
  currentEvent = prevCurrentEvent;
}

function getParent(elem){
  $clinit_DOM();
  return $getParentElement(($clinit_DOMImpl() , elem));
}

function isPotential(o){
  $clinit_DOM();
  try {
    return !!o && !!o.__gwt_resolve;
  }
   catch (e) {
    return false;
  }
}

function previewEvent(evt){
  $clinit_DOM();
  return true;
}

function resolve(maybePotential){
  $clinit_DOM();
  return maybePotential.__gwt_resolve?maybePotential.__gwt_resolve():maybePotential;
}

function sinkEvents(elem, eventBits){
  $clinit_DOM();
  impl_3.sinkEvents(elem, eventBits);
}

var currentEvent = null, impl_3, sCaptureElem;
function $onModuleLoad_0(){
  var allowedModes, currentMode, i_0;
  currentMode = $doc.compatMode;
  allowedModes = initValues(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_4, 2, 4, ['CSS1Compat']);
  for (i_0 = 0; i_0 < allowedModes.length; i_0++) {
    if ($equals(allowedModes[i_0], currentMode)) {
      return;
    }
  }
  allowedModes.length == 1 && $equals('CSS1Compat', allowedModes[0]) && $equals('BackCompat', currentMode)?"GWT no longer supports Quirks Mode (document.compatMode=' BackCompat').<br>Make sure your application's host HTML page has a Standards Mode (document.compatMode=' CSS1Compat') doctype,<br>e.g. by using &lt;!doctype html&gt; at the start of your application's HTML page.<br><br>To continue using this unsupported rendering mode and risk layout problems, suppress this message by adding<br>the following line to your*.gwt.xml module file:<br>&nbsp;&nbsp;&lt;extend-configuration-property name=\"document.compatMode\" value=\"" + currentMode + '"/&gt;':"Your *.gwt.xml module configuration prohibits the use of the current document rendering mode (document.compatMode=' " + currentMode + "').<br>Modify your application's host HTML page doctype, or update your custom " + "'document.compatMode' configuration property settings.";
}

function $cancel(this$static){
  if (!this$static.timerId) {
    return;
  }
  ++this$static.cancelCounter;
  this$static.isRepeating?clearInterval_0(this$static.timerId.value_0):clearTimeout_0(this$static.timerId.value_0);
  this$static.timerId = null;
}

function $schedule(this$static, delayMillis){
  if (delayMillis < 0) {
    throw new IllegalArgumentException('must be non-negative');
  }
  !!this$static.timerId && $cancel(this$static);
  this$static.isRepeating = false;
  this$static.timerId = valueOf(setTimeout_0(createCallback(this$static, this$static.cancelCounter), delayMillis));
}

function Timer(){
}

function clearInterval_0(timerId){
  $wnd.clearInterval(timerId);
}

function clearTimeout_0(timerId){
  $wnd.clearTimeout(timerId);
}

function createCallback(timer, cancelCounter){
  return $entry(function(){
    timer.fire(cancelCounter);
  }
  );
}

function setTimeout_0(func, time){
  return $wnd.setTimeout(func, time);
}

defineClass(189, 1, {});
_.fire = function fire_0(scheduleCancelCounter){
  if (scheduleCancelCounter != this.cancelCounter) {
    return;
  }
  this.isRepeating || (this.timerId = null);
  this.run();
}
;
_.cancelCounter = 0;
_.isRepeating = false;
_.timerId = null;
var Lcom_google_gwt_user_client_Timer_2_classLit = createForClass('com.google.gwt.user.client', 'Timer', 189);
function $clinit_Window(){
  $clinit_Window = emptyMethod;
  impl_4 = com_google_gwt_user_client_impl_WindowImpl();
}

function addCloseHandler(handler){
  $clinit_Window();
  maybeInitializeCloseHandlers();
  return addHandler(TYPE?TYPE:(TYPE = new GwtEvent$Type), handler);
}

function addHandler(type_0, handler){
  return $addHandler((!handlers_0 && (handlers_0 = new Window$WindowHandlers) , handlers_0), type_0, handler);
}

function maybeInitializeCloseHandlers(){
  if (!closeHandlersInitialized) {
    impl_4.initWindowCloseHandler();
    closeHandlersInitialized = true;
  }
}

function onClosed(){
  $clinit_Window();
  closeHandlersInitialized && fire((!handlers_0 && (handlers_0 = new Window$WindowHandlers) , handlers_0));
}

function onClosing(){
  $clinit_Window();
  var event_0;
  if (closeHandlersInitialized) {
    event_0 = new Window$ClosingEvent;
    !!handlers_0 && $fireEvent(handlers_0, event_0);
    return null;
  }
  return null;
}

var closeHandlersInitialized = false, handlers_0, impl_4;
function $clinit_Window$ClosingEvent(){
  $clinit_Window$ClosingEvent = emptyMethod;
  TYPE_0 = new GwtEvent$Type;
}

function Window$ClosingEvent(){
  $clinit_Window$ClosingEvent();
}

defineClass(279, 442, {}, Window$ClosingEvent);
_.dispatch = function dispatch_0(handler){
  throwClassCastExceptionUnlessNull(handler);
  null.nullMethod();
}
;
_.getAssociatedType = function getAssociatedType_0(){
  return TYPE_0;
}
;
var TYPE_0;
var Lcom_google_gwt_user_client_Window$ClosingEvent_2_classLit = createForClass('com.google.gwt.user.client', 'Window/ClosingEvent', 279);
function buildListParamMap(queryString){
  var entry, entry$iterator, key, kv, kvPair, kvPair$array, kvPair$index, kvPair$max, out, qs, val, values, regexp;
  out = new HashMap;
  if (queryString != null && queryString.length > 1) {
    qs = __substr(queryString, 1, queryString.length - 1);
    for (kvPair$array = $split(qs, '&', 0) , kvPair$index = 0 , kvPair$max = kvPair$array.length; kvPair$index < kvPair$max; ++kvPair$index) {
      kvPair = kvPair$array[kvPair$index];
      kv = $split(kvPair, '=', 2);
      key = kv[0];
      if (!key.length) {
        continue;
      }
      val = kv.length > 1?kv[1]:'';
      try {
        val = (throwIfNull(val) , regexp = /\+/g , decodeURIComponent(val.replace(regexp, '%20')));
      }
       catch ($e0) {
        $e0 = wrap($e0);
        if (!instanceOf($e0, 62))
          throw unwrap($e0);
      }
      values = dynamicCast(out.get_0(key), 37);
      if (!values) {
        values = new ArrayList;
        out.put(key, values);
      }
      values.add_1(val);
    }
  }
  for (entry$iterator = out.entrySet_0().iterator(); entry$iterator.hasNext();) {
    entry = dynamicCast(entry$iterator.next_0(), 22);
    entry.setValue(unmodifiableList(dynamicCast(entry.getValue(), 37)));
  }
  out = ($clinit_Collections() , new Collections$UnmodifiableMap(out));
  return out;
}

function ensureListParameterMap(){
  var currentQueryString;
  currentQueryString = ($clinit_Window() , impl_4).getQueryString();
  if (!listParamMap || !$equals(cachedQueryString, currentQueryString)) {
    listParamMap = buildListParamMap(currentQueryString);
    cachedQueryString = currentQueryString;
  }
}

var cachedQueryString = '', listParamMap;
function Window$WindowHandlers(){
  this.eventBus = new HandlerManager$Bus;
  this.source = null;
}

defineClass(175, 174, {38:1}, Window$WindowHandlers);
var Lcom_google_gwt_user_client_Window$WindowHandlers_2_classLit = createForClass('com.google.gwt.user.client', 'Window/WindowHandlers', 175);
function $eventGetTypeInt(eventType){
  switch (eventType) {
    case 'blur':
      return 4096;
    case 'change':
      return 1024;
    case 'click':
      return 1;
    case 'dblclick':
      return 2;
    case 'focus':
      return 2048;
    case 'keydown':
      return 128;
    case 'keypress':
      return 256;
    case 'keyup':
      return 512;
    case 'load':
      return $intern_14;
    case 'losecapture':
      return 8192;
    case 'mousedown':
      return 4;
    case 'mousemove':
      return 64;
    case 'mouseout':
      return 32;
    case 'mouseover':
      return 16;
    case 'mouseup':
      return 8;
    case 'scroll':
      return $intern_15;
    case 'error':
      return $intern_16;
    case 'DOMMouseScroll':
    case 'mousewheel':
      return $intern_17;
    case 'contextmenu':
      return $intern_18;
    case 'paste':
      return $intern_9;
    case 'touchstart':
      return $intern_19;
    case 'touchmove':
      return $intern_20;
    case 'touchend':
      return $intern_10;
    case 'touchcancel':
      return $intern_21;
    case 'gesturestart':
      return $intern_22;
    case 'gesturechange':
      return $intern_23;
    case 'gestureend':
      return $intern_24;
    default:return -1;
  }
}

function $maybeInitializeEventSystem(this$static){
  if (!eventSystemIsInitialized) {
    this$static.initEventSystem();
    eventSystemIsInitialized = true;
  }
}

function getEventListener_0(elem){
  var maybeListener = elem.__listener;
  return !instanceOfJso(maybeListener) && instanceOf(maybeListener, 42)?maybeListener:null;
}

function setEventListener(elem, listener){
  elem.__listener = listener;
}

defineClass(444, 1, {});
var eventSystemIsInitialized = false;
var Lcom_google_gwt_user_client_impl_DOMImpl_2_classLit = createForClass('com.google.gwt.user.client.impl', 'DOMImpl', 444);
function $sinkEventsImpl(elem, bits){
  var chMask = (elem.__eventBits || 0) ^ bits;
  elem.__eventBits = bits;
  if (!chMask)
    return;
  chMask & 1 && (elem.onclick = bits & 1?callDispatchEvent:null);
  chMask & 3 && (elem.ondblclick = bits & 3?callDispatchDblClickEvent:null);
  chMask & 4 && (elem.onmousedown = bits & 4?callDispatchEvent:null);
  chMask & 8 && (elem.onmouseup = bits & 8?callDispatchEvent:null);
  chMask & 16 && (elem.onmouseover = bits & 16?callDispatchEvent:null);
  chMask & 32 && (elem.onmouseout = bits & 32?callDispatchEvent:null);
  chMask & 64 && (elem.onmousemove = bits & 64?callDispatchEvent:null);
  chMask & 128 && (elem.onkeydown = bits & 128?callDispatchEvent:null);
  chMask & 256 && (elem.onkeypress = bits & 256?callDispatchEvent:null);
  chMask & 512 && (elem.onkeyup = bits & 512?callDispatchEvent:null);
  chMask & 1024 && (elem.onchange = bits & 1024?callDispatchEvent:null);
  chMask & 2048 && (elem.onfocus = bits & 2048?callDispatchEvent:null);
  chMask & 4096 && (elem.onblur = bits & 4096?callDispatchEvent:null);
  chMask & 8192 && (elem.onlosecapture = bits & 8192?callDispatchEvent:null);
  chMask & $intern_15 && (elem.onscroll = bits & $intern_15?callDispatchEvent:null);
  chMask & $intern_14 && (elem.nodeName == 'IFRAME'?bits & $intern_14?elem.attachEvent('onload', callDispatchOnLoadEvent):elem.detachEvent('onload', callDispatchOnLoadEvent):(elem.onload = bits & $intern_14?callDispatchUnhandledEvent:null));
  chMask & $intern_16 && (elem.onerror = bits & $intern_16?callDispatchEvent:null);
  chMask & $intern_17 && (elem.onmousewheel = bits & $intern_17?callDispatchEvent:null);
  chMask & $intern_18 && (elem.oncontextmenu = bits & $intern_18?callDispatchEvent:null);
  chMask & $intern_9 && (elem.onpaste = bits & $intern_9?callDispatchEvent:null);
}

function previewEventImpl(){
  var isCancelled = false;
  for (var i_0 = 0; i_0 < $wnd.__gwt_globalEventArray.length; i_0++) {
    !$wnd.__gwt_globalEventArray[i_0]() && (isCancelled = true);
  }
  return !isCancelled;
}

defineClass(445, 444, {});
_.initEventSystem = function initEventSystem(){
  $wnd.__gwt_globalEventArray == null && ($wnd.__gwt_globalEventArray = new Array);
  $wnd.__gwt_globalEventArray[$wnd.__gwt_globalEventArray.length] = $entry(function(){
    return previewEvent($wnd.event);
  }
  );
  var dispatchEvent_0 = $entry(function(){
    var oldEventTarget = ($clinit_DOMImpl() , currentEventTarget);
    currentEventTarget = this;
    if ($wnd.event.returnValue == null) {
      $wnd.event.returnValue = true;
      if (!previewEventImpl()) {
        currentEventTarget = oldEventTarget;
        return;
      }
    }
    var getEventListener = getEventListener_0;
    var listener, curElem = this;
    while (curElem && !(listener = getEventListener(curElem))) {
      curElem = curElem.parentElement;
    }
    listener && dispatchEvent_1($wnd.event, curElem, listener);
    currentEventTarget = oldEventTarget;
  }
  );
  var dispatchDblClickEvent = $entry(function(){
    var newEvent = $doc.createEventObject();
    $wnd.event.returnValue == null && $wnd.event.srcElement.fireEvent && $wnd.event.srcElement.fireEvent('onclick', newEvent);
    if (this.__eventBits & 2) {
      dispatchEvent_0.call(this);
    }
     else if ($wnd.event.returnValue == null) {
      $wnd.event.returnValue = true;
      previewEventImpl();
    }
  }
  );
  var dispatchUnhandledEvent = $entry(function(){
    this.__gwtLastUnhandledEvent = $wnd.event.type;
    dispatchEvent_0.call(this);
  }
  );
  var moduleName = $moduleName.replace(/\./g, '_');
  $wnd['__gwt_dispatchEvent_' + moduleName] = dispatchEvent_0;
  callDispatchEvent = (new Function('w', 'return function() { w.__gwt_dispatchEvent_' + moduleName + '.call(this) }'))($wnd);
  $wnd['__gwt_dispatchDblClickEvent_' + moduleName] = dispatchDblClickEvent;
  callDispatchDblClickEvent = (new Function('w', 'return function() { w.__gwt_dispatchDblClickEvent_' + moduleName + '.call(this)}'))($wnd);
  $wnd['__gwt_dispatchUnhandledEvent_' + moduleName] = dispatchUnhandledEvent;
  callDispatchUnhandledEvent = (new Function('w', 'return function() { w.__gwt_dispatchUnhandledEvent_' + moduleName + '.call(this)}'))($wnd);
  callDispatchOnLoadEvent = (new Function('w', 'return function() { w.__gwt_dispatchUnhandledEvent_' + moduleName + '.call(w.event.srcElement)}'))($wnd);
  var bodyDispatcher = $entry(function(){
    dispatchEvent_0.call($doc.body);
  }
  );
  var bodyDblClickDispatcher = $entry(function(){
    dispatchDblClickEvent.call($doc.body);
  }
  );
  $doc.body.attachEvent('onclick', bodyDispatcher);
  $doc.body.attachEvent('onmousedown', bodyDispatcher);
  $doc.body.attachEvent('onmouseup', bodyDispatcher);
  $doc.body.attachEvent('onmousemove', bodyDispatcher);
  $doc.body.attachEvent('onmousewheel', bodyDispatcher);
  $doc.body.attachEvent('onkeydown', bodyDispatcher);
  $doc.body.attachEvent('onkeypress', bodyDispatcher);
  $doc.body.attachEvent('onkeyup', bodyDispatcher);
  $doc.body.attachEvent('onfocus', bodyDispatcher);
  $doc.body.attachEvent('onblur', bodyDispatcher);
  $doc.body.attachEvent('ondblclick', bodyDblClickDispatcher);
  $doc.body.attachEvent('oncontextmenu', bodyDispatcher);
}
;
_.sinkEvents = function sinkEvents_0(elem, bits){
  $maybeInitializeEventSystem(this);
  $sinkEventsImpl(elem, bits);
}
;
var callDispatchDblClickEvent, callDispatchEvent, callDispatchOnLoadEvent, callDispatchUnhandledEvent;
var Lcom_google_gwt_user_client_impl_DOMImplTrident_2_classLit = createForClass('com.google.gwt.user.client.impl', 'DOMImplTrident', 445);
function DOMImplIE8_0(){
}

defineClass(292, 445, {}, DOMImplIE8_0);
var Lcom_google_gwt_user_client_impl_DOMImplIE8_2_classLit = createForClass('com.google.gwt.user.client.impl', 'DOMImplIE8', 292);
function $clinit_DOMImplStandard(){
  $clinit_DOMImplStandard = emptyMethod;
  bitlessEventDispatchers = {_default_:dispatchEvent_3, dragenter:dispatchDragEvent, dragover:dispatchDragEvent};
  captureEventDispatchers = {click:dispatchCapturedMouseEvent, dblclick:dispatchCapturedMouseEvent, mousedown:dispatchCapturedMouseEvent, mouseup:dispatchCapturedMouseEvent, mousemove:dispatchCapturedMouseEvent, mouseover:dispatchCapturedMouseEvent, mouseout:dispatchCapturedMouseEvent, mousewheel:dispatchCapturedMouseEvent, keydown:dispatchCapturedEvent, keyup:dispatchCapturedEvent, keypress:dispatchCapturedEvent, touchstart:dispatchCapturedMouseEvent, touchend:dispatchCapturedMouseEvent, touchmove:dispatchCapturedMouseEvent, touchcancel:dispatchCapturedMouseEvent, gesturestart:dispatchCapturedMouseEvent, gestureend:dispatchCapturedMouseEvent, gesturechange:dispatchCapturedMouseEvent};
}

function $initEventSystem(){
  dispatchEvent_2 = $entry(dispatchEvent_3);
  dispatchUnhandledEvent_0 = $entry(dispatchUnhandledEvent_1);
  var foreach = foreach_0;
  var bitlessEvents = bitlessEventDispatchers;
  foreach(bitlessEvents, function(e, fn){
    bitlessEvents[e] = $entry(fn);
  }
  );
  var captureEvents_0 = captureEventDispatchers;
  foreach(captureEvents_0, function(e, fn){
    captureEvents_0[e] = $entry(fn);
  }
  );
  foreach(captureEvents_0, function(e, fn){
    $wnd.addEventListener(e, fn, true);
  }
  );
}

function $sinkEventsImpl_0(elem, bits){
  var chMask = (elem.__eventBits || 0) ^ bits;
  elem.__eventBits = bits;
  if (!chMask)
    return;
  chMask & 1 && (elem.onclick = bits & 1?dispatchEvent_2:null);
  chMask & 2 && (elem.ondblclick = bits & 2?dispatchEvent_2:null);
  chMask & 4 && (elem.onmousedown = bits & 4?dispatchEvent_2:null);
  chMask & 8 && (elem.onmouseup = bits & 8?dispatchEvent_2:null);
  chMask & 16 && (elem.onmouseover = bits & 16?dispatchEvent_2:null);
  chMask & 32 && (elem.onmouseout = bits & 32?dispatchEvent_2:null);
  chMask & 64 && (elem.onmousemove = bits & 64?dispatchEvent_2:null);
  chMask & 128 && (elem.onkeydown = bits & 128?dispatchEvent_2:null);
  chMask & 256 && (elem.onkeypress = bits & 256?dispatchEvent_2:null);
  chMask & 512 && (elem.onkeyup = bits & 512?dispatchEvent_2:null);
  chMask & 1024 && (elem.onchange = bits & 1024?dispatchEvent_2:null);
  chMask & 2048 && (elem.onfocus = bits & 2048?dispatchEvent_2:null);
  chMask & 4096 && (elem.onblur = bits & 4096?dispatchEvent_2:null);
  chMask & 8192 && (elem.onlosecapture = bits & 8192?dispatchEvent_2:null);
  chMask & $intern_15 && (elem.onscroll = bits & $intern_15?dispatchEvent_2:null);
  chMask & $intern_14 && (elem.onload = bits & $intern_14?dispatchUnhandledEvent_0:null);
  chMask & $intern_16 && (elem.onerror = bits & $intern_16?dispatchEvent_2:null);
  chMask & $intern_17 && (elem.onmousewheel = bits & $intern_17?dispatchEvent_2:null);
  chMask & $intern_18 && (elem.oncontextmenu = bits & $intern_18?dispatchEvent_2:null);
  chMask & $intern_9 && (elem.onpaste = bits & $intern_9?dispatchEvent_2:null);
  chMask & $intern_19 && (elem.ontouchstart = bits & $intern_19?dispatchEvent_2:null);
  chMask & $intern_20 && (elem.ontouchmove = bits & $intern_20?dispatchEvent_2:null);
  chMask & $intern_10 && (elem.ontouchend = bits & $intern_10?dispatchEvent_2:null);
  chMask & $intern_21 && (elem.ontouchcancel = bits & $intern_21?dispatchEvent_2:null);
  chMask & $intern_22 && (elem.ongesturestart = bits & $intern_22?dispatchEvent_2:null);
  chMask & $intern_23 && (elem.ongesturechange = bits & $intern_23?dispatchEvent_2:null);
  chMask & $intern_24 && (elem.ongestureend = bits & $intern_24?dispatchEvent_2:null);
}

function dispatchCapturedEvent(evt){
  $clinit_DOM();
}

function dispatchCapturedMouseEvent(evt){
  $clinit_DOMImplStandard();
  $clinit_DOM();
  return;
}

function dispatchDragEvent(evt){
  ($clinit_DOMImpl() , impl_1).eventPreventDefault(evt);
  dispatchEvent_3(evt);
}

function dispatchEvent_3(evt){
  var element;
  element = getFirstAncestorWithListener(evt);
  if (!element) {
    return;
  }
  dispatchEvent_1(evt, element.nodeType != 1?null:element, getEventListener_0(element));
}

function dispatchUnhandledEvent_1(evt){
  var element;
  element = ($clinit_DOMImpl() , impl_1).eventGetCurrentTarget(evt);
  $setPropertyString(element, '__gwtLastUnhandledEvent', evt.type);
  dispatchEvent_3(evt);
}

function getFirstAncestorWithListener(evt){
  var curElem;
  curElem = ($clinit_DOMImpl() , impl_1).eventGetCurrentTarget(evt);
  while (!!curElem && !getEventListener_0(curElem)) {
    curElem = curElem.parentNode;
  }
  return curElem;
}

defineClass(446, 444, {});
_.initEventSystem = function initEventSystem_0(){
  $initEventSystem();
}
;
_.sinkEvents = function sinkEvents_1(elem, bits){
  $maybeInitializeEventSystem(this);
  $sinkEventsImpl_0(elem, bits);
}
;
var bitlessEventDispatchers, captureElem, captureEventDispatchers, dispatchEvent_2, dispatchUnhandledEvent_0;
var Lcom_google_gwt_user_client_impl_DOMImplStandard_2_classLit = createForClass('com.google.gwt.user.client.impl', 'DOMImplStandard', 446);
defineClass(447, 446, {});
var Lcom_google_gwt_user_client_impl_DOMImplStandardBase_2_classLit = createForClass('com.google.gwt.user.client.impl', 'DOMImplStandardBase', 447);
function DOMImplIE9_0(){
  $clinit_DOMImplStandard();
}

defineClass(294, 447, {}, DOMImplIE9_0);
var Lcom_google_gwt_user_client_impl_DOMImplIE9_2_classLit = createForClass('com.google.gwt.user.client.impl', 'DOMImplIE9', 294);
function $clinit_DOMImplMozilla(){
  $clinit_DOMImplMozilla = emptyMethod;
  $clinit_DOMImplStandard();
  captureEventDispatchers['DOMMouseScroll'] = dispatchCapturedMouseEvent;
}

function $initSyntheticMouseUpEvents(){
  $wnd.addEventListener('mouseout', $entry(function(evt){
    var cap = ($clinit_DOMImplStandard() , captureElem);
    if (cap && !evt.relatedTarget) {
      if ('html' == evt.target.tagName.toLowerCase()) {
        var muEvent = $doc.createEvent('MouseEvents');
        muEvent.initMouseEvent('mouseup', true, true, $wnd, 0, evt.screenX, evt.screenY, evt.clientX, evt.clientY, evt.ctrlKey, evt.altKey, evt.shiftKey, evt.metaKey, evt.button, null);
        cap.dispatchEvent(muEvent);
      }
    }
  }
  ), true);
}

function DOMImplMozilla_0(){
  $clinit_DOMImplMozilla();
}

defineClass(293, 446, {}, DOMImplMozilla_0);
_.initEventSystem = function initEventSystem_1(){
  $initEventSystem();
  $initSyntheticMouseUpEvents();
}
;
_.sinkEvents = function sinkEvents_2(elem, bits){
  $maybeInitializeEventSystem(this);
  $sinkEventsImpl_0(elem, bits);
  bits & $intern_17 && elem.addEventListener('DOMMouseScroll', ($clinit_DOMImplStandard() , dispatchEvent_2), false);
}
;
var Lcom_google_gwt_user_client_impl_DOMImplMozilla_2_classLit = createForClass('com.google.gwt.user.client.impl', 'DOMImplMozilla', 293);
function DOMImplWebkit_0(){
  $clinit_DOMImplStandard();
}

defineClass(295, 447, {}, DOMImplWebkit_0);
var Lcom_google_gwt_user_client_impl_DOMImplWebkit_2_classLit = createForClass('com.google.gwt.user.client.impl', 'DOMImplWebkit', 295);
function foreach_0(map_0, fn){
  for (var e in map_0) {
    map_0.hasOwnProperty(e) && fn(e, map_0[e]);
  }
}

function WindowImpl(){
}

defineClass(296, 1, {}, WindowImpl);
_.getQueryString = function getQueryString(){
  return $wnd.location.search;
}
;
_.initWindowCloseHandler = function initWindowCloseHandler(){
  var oldOnBeforeUnload = $wnd.onbeforeunload;
  var oldOnUnload = $wnd.onunload;
  $wnd.onbeforeunload = function(evt){
    var ret, oldRet;
    try {
      ret = $entry(onClosing)();
    }
     finally {
      oldRet = oldOnBeforeUnload && oldOnBeforeUnload(evt);
    }
    if (ret != null) {
      return ret;
    }
    if (oldRet != null) {
      return oldRet;
    }
  }
  ;
  $wnd.onunload = $entry(function(evt){
    try {
      $clinit_Window();
      closeHandlersInitialized && fire((!handlers_0 && (handlers_0 = new Window$WindowHandlers) , handlers_0));
    }
     finally {
      oldOnUnload && oldOnUnload(evt);
      $wnd.onresize = null;
      $wnd.onscroll = null;
      $wnd.onbeforeunload = null;
      $wnd.onunload = null;
    }
  }
  );
}
;
var Lcom_google_gwt_user_client_impl_WindowImpl_2_classLit = createForClass('com.google.gwt.user.client.impl', 'WindowImpl', 296);
function WindowImplIE(){
}

defineClass(297, 296, {}, WindowImplIE);
_.getQueryString = function getQueryString_0(){
  var href_0 = $wnd.location.href;
  var hashLoc = href_0.indexOf('#');
  hashLoc >= 0 && (href_0 = href_0.substring(0, hashLoc));
  var questionLoc = href_0.indexOf('?');
  return questionLoc > 0?href_0.substring(questionLoc):'';
}
;
_.initWindowCloseHandler = function initWindowCloseHandler_0(){
  var scriptElem;
  scriptElem = $createScriptElement($doc);
  $appendChild($doc.body, scriptElem);
  $wnd.__gwt_initWindowCloseHandler($entry(onClosing), $entry(onClosed));
  $removeChild($doc.body, scriptElem);
}
;
var Lcom_google_gwt_user_client_impl_WindowImplIE_2_classLit = createForClass('com.google.gwt.user.client.impl', 'WindowImplIE', 297);
function WindowImplMozilla(){
}

defineClass(298, 296, {}, WindowImplMozilla);
var Lcom_google_gwt_user_client_impl_WindowImplMozilla_2_classLit = createForClass('com.google.gwt.user.client.impl', 'WindowImplMozilla', 298);
defineClass(434, 29, $intern_25);
_.doAttachChildren = function doAttachChildren_0(){
  tryCommand(this, ($clinit_AttachDetachException() , attachCommand));
}
;
_.doDetachChildren = function doDetachChildren_0(){
  tryCommand(this, ($clinit_AttachDetachException() , detachCommand));
}
;
var Lcom_google_gwt_user_client_ui_Panel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'Panel', 434);
function $add(this$static, child, container){
  $removeFromParent(child);
  $add_2(this$static.children, child);
  $clinit_DOM();
  $appendChild(container, resolve(child.element));
  $setParent(child, this$static);
}

function $remove(this$static, w){
  var elem;
  if (w.parent_0 != this$static) {
    return false;
  }
  try {
    $setParent(w, null);
  }
   finally {
    elem = ($clinit_DOM() , w.element);
    $removeChild((null , $getParentElement(($clinit_DOMImpl() , elem))), elem);
    $remove_1(this$static.children, w);
  }
  return true;
}

function ComplexPanel(){
  this.children = new WidgetCollection(this);
}

defineClass(162, 434, $intern_25);
_.iterator = function iterator_0(){
  return new WidgetCollection$WidgetIterator(this.children);
}
;
_.remove = function remove(w){
  return $remove(this, w);
}
;
var Lcom_google_gwt_user_client_ui_ComplexPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'ComplexPanel', 162);
function $add_0(this$static, w){
  $add(this$static, w, ($clinit_DOM() , this$static.element));
}

function changeToStaticPositioning(elem){
  $setPropertyImpl(elem.style, 'left', '');
  $setPropertyImpl(elem.style, 'top', '');
  $setPropertyImpl(elem.style, 'position', '');
}

defineClass(231, 162, $intern_25);
_.remove = function remove_0(w){
  var removed;
  removed = $remove(this, w);
  removed && changeToStaticPositioning(($clinit_DOM() , w.element));
  return removed;
}
;
var Lcom_google_gwt_user_client_ui_AbsolutePanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AbsolutePanel', 231);
function $clinit_AttachDetachException(){
  $clinit_AttachDetachException = emptyMethod;
  attachCommand = new AttachDetachException$1;
  detachCommand = new AttachDetachException$2;
}

function AttachDetachException(causes){
  UmbrellaException_0.call(this, causes);
}

function tryCommand(hasWidgets, c){
  $clinit_AttachDetachException();
  var caught, e, w, w$iterator;
  caught = null;
  for (w$iterator = hasWidgets.iterator(); w$iterator.hasNext();) {
    w = dynamicCast(w$iterator.next_0(), 29);
    try {
      c.execute_0(w);
    }
     catch ($e0) {
      $e0 = wrap($e0);
      if (instanceOf($e0, 13)) {
        e = $e0;
        !caught && (caught = new HashSet);
        $add_4(caught, e);
      }
       else 
        throw unwrap($e0);
    }
  }
  if (caught) {
    throw new AttachDetachException(caught);
  }
}

defineClass(235, 163, $intern_6, AttachDetachException);
var attachCommand, detachCommand;
var Lcom_google_gwt_user_client_ui_AttachDetachException_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AttachDetachException', 235);
function AttachDetachException$1(){
}

defineClass(236, 1, {}, AttachDetachException$1);
_.execute_0 = function execute_2(w){
  w.onAttach();
}
;
var Lcom_google_gwt_user_client_ui_AttachDetachException$1_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AttachDetachException/1', 236);
function AttachDetachException$2(){
}

defineClass(237, 1, {}, AttachDetachException$2);
_.execute_0 = function execute_3(w){
  w.onDetach();
}
;
var Lcom_google_gwt_user_client_ui_AttachDetachException$2_2_classLit = createForClass('com.google.gwt.user.client.ui', 'AttachDetachException/2', 237);
function $setCellHorizontalAlignment(td, align_0){
  $setPropertyString(($clinit_DOM() , td), 'align', align_0.textAlignString);
}

function $setCellVerticalAlignment(td, align_0){
  $setCellVerticalAlignment_0(($clinit_DOM() , td), align_0);
}

function $setCellVerticalAlignment_0(td, align_0){
  $setPropertyImpl(td.style, 'verticalAlign', align_0.verticalAlignString);
}

defineClass(304, 162, $intern_25);
var Lcom_google_gwt_user_client_ui_CellPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'CellPanel', 304);
function $checkInit(this$static){
  if (!this$static.widget) {
    throw new IllegalStateException_0('initWidget() is not called yet');
  }
}

function $initWidget(this$static, widget){
  var elem;
  if (this$static.widget) {
    throw new IllegalStateException_0('Composite.initWidget() may only be called once.');
  }
  if (!widget) {
    throw new NullPointerException_0('widget cannot be null');
  }
  instanceOf(widget, 213) && dynamicCast(widget, 213);
  $removeFromParent(widget);
  elem = ($clinit_DOM() , widget.element);
  $setElement_0(this$static, elem);
  ($clinit_PotentialElement() , isPotential(elem)) && $setResolver(elem, this$static);
  this$static.widget = widget;
  $setParent(widget, this$static);
}

defineClass(433, 29, $intern_26);
_.isAttached = function isAttached_0(){
  if (this.widget) {
    return this.widget.isAttached();
  }
  return false;
}
;
_.onAttach = function onAttach_1(){
  $checkInit(this);
  if (this.eventsToSink != -1) {
    $sinkEvents(this.widget, this.eventsToSink);
    this.eventsToSink = -1;
  }
  this.widget.onAttach();
  $clinit_DOM();
  setEventListener(this.element, this);
}
;
_.onBrowserEvent = function onBrowserEvent_0(event_0){
  $onBrowserEvent(this, event_0);
  this.widget.onBrowserEvent(event_0);
}
;
_.onDetach = function onDetach_0(){
  this.widget.onDetach();
}
;
_.resolvePotentialElement = function resolvePotentialElement_0(){
  $setElement_0(this, ($clinit_DOM() , this.widget.resolvePotentialElement()));
  return this.element;
}
;
var Lcom_google_gwt_user_client_ui_Composite_2_classLit = createForClass('com.google.gwt.user.client.ui', 'Composite', 433);
function $setTextOrHtml(this$static, content_0){
  $setInnerText(this$static.element, content_0);
  if (this$static.textDir != this$static.initialElementDir) {
    this$static.textDir = this$static.initialElementDir;
    setDirectionOnElement(this$static.element, this$static.initialElementDir);
  }
}

function DirectionalTextHelper(element){
  this.element = element;
  this.initialElementDir = getDirectionOnElement(element);
  this.textDir = this.initialElementDir;
}

defineClass(358, 1, {}, DirectionalTextHelper);
var Lcom_google_gwt_user_client_ui_DirectionalTextHelper_2_classLit = createForClass('com.google.gwt.user.client.ui', 'DirectionalTextHelper', 358);
function $clinit_HasHorizontalAlignment(){
  $clinit_HasHorizontalAlignment = emptyMethod;
  new HasHorizontalAlignment$HorizontalAlignmentConstant(($clinit_Style$TextAlign() , 'center'));
  new HasHorizontalAlignment$HorizontalAlignmentConstant('justify');
  ALIGN_LEFT = new HasHorizontalAlignment$HorizontalAlignmentConstant('left');
  new HasHorizontalAlignment$HorizontalAlignmentConstant('right');
  ALIGN_LOCALE_START = ALIGN_LEFT;
  ALIGN_DEFAULT = ALIGN_LOCALE_START;
}

var ALIGN_DEFAULT, ALIGN_LEFT, ALIGN_LOCALE_START;
defineClass(451, 1, {});
var Lcom_google_gwt_user_client_ui_HasHorizontalAlignment$AutoHorizontalAlignmentConstant_2_classLit = createForClass('com.google.gwt.user.client.ui', 'HasHorizontalAlignment/AutoHorizontalAlignmentConstant', 451);
function HasHorizontalAlignment$HorizontalAlignmentConstant(textAlignString){
  this.textAlignString = textAlignString;
}

defineClass(116, 451, {}, HasHorizontalAlignment$HorizontalAlignmentConstant);
var Lcom_google_gwt_user_client_ui_HasHorizontalAlignment$HorizontalAlignmentConstant_2_classLit = createForClass('com.google.gwt.user.client.ui', 'HasHorizontalAlignment/HorizontalAlignmentConstant', 116);
function $clinit_HasVerticalAlignment(){
  $clinit_HasVerticalAlignment = emptyMethod;
  new HasVerticalAlignment$VerticalAlignmentConstant('bottom');
  new HasVerticalAlignment$VerticalAlignmentConstant('middle');
  ALIGN_TOP = new HasVerticalAlignment$VerticalAlignmentConstant('top');
}

var ALIGN_TOP;
function HasVerticalAlignment$VerticalAlignmentConstant(verticalAlignString){
  this.verticalAlignString = verticalAlignString;
}

defineClass(142, 1, {}, HasVerticalAlignment$VerticalAlignmentConstant);
var Lcom_google_gwt_user_client_ui_HasVerticalAlignment$VerticalAlignmentConstant_2_classLit = createForClass('com.google.gwt.user.client.ui', 'HasVerticalAlignment/VerticalAlignmentConstant', 142);
function LabelBase(element){
  $setElement_0(this, ($clinit_DOM() , element));
  this.directionalTextHelper = new DirectionalTextHelper(this.element);
}

defineClass(182, 29, $intern_0);
var Lcom_google_gwt_user_client_ui_LabelBase_2_classLit = createForClass('com.google.gwt.user.client.ui', 'LabelBase', 182);
function Label(text_0){
  LabelBase.call(this, $createDivElement($doc));
  ($clinit_DOM() , this.element).className = 'gwt-Label';
  $setTextOrHtml(this.directionalTextHelper, text_0);
}

defineClass(143, 182, $intern_0, Label);
var Lcom_google_gwt_user_client_ui_Label_2_classLit = createForClass('com.google.gwt.user.client.ui', 'Label', 143);
function $clinit_PotentialElement(){
  $clinit_PotentialElement = emptyMethod;
  declareShim();
}

function $setResolver(this$static, resolver){
  $clinit_PotentialElement();
  this$static.__gwt_resolve = buildResolveCallback(resolver);
}

function buildResolveCallback(resolver){
  return function(){
    this.__gwt_resolve = cannotResolveTwice;
    return resolver.resolvePotentialElement();
  }
  ;
}

function cannotResolveTwice(){
  throw 'A PotentialElement cannot be resolved twice.';
}

function declareShim(){
  var shim = function(){
  }
  ;
  shim.prototype = {className:'', clientHeight:0, clientWidth:0, dir:'', getAttribute:function(name_0, value_0){
    return this[name_0];
  }
  , href:'', id:'', lang:'', nodeType:1, removeAttribute:function(name_0, value_0){
    this[name_0] = undefined;
  }
  , setAttribute:function(name_0, value_0){
    this[name_0] = value_0;
  }
  , src:'', style:{}, title:''};
  $wnd.GwtPotentialElementShim = shim;
}

function $clinit_RootPanel(){
  $clinit_RootPanel = emptyMethod;
  maybeDetachCommand = new RootPanel$1;
  rootPanels = new HashMap;
  widgetsToDetach = new HashSet;
}

function RootPanel(elem){
  ComplexPanel.call(this);
  $setElement_0(this, ($clinit_DOM() , elem));
  $onAttach(this);
}

function detachNow(widget){
  $clinit_RootPanel();
  try {
    widget.onDetach();
  }
   finally {
    $remove_6(widgetsToDetach, widget);
  }
}

function detachWidgets(){
  $clinit_RootPanel();
  try {
    tryCommand(widgetsToDetach, maybeDetachCommand);
  }
   finally {
    $reset(widgetsToDetach.map_0);
    $reset(rootPanels);
  }
}

function get_0(){
  $clinit_RootPanel();
  var elem, rp;
  rp = dynamicCast($getStringValue(rootPanels, 'g3mWidgetHolder'), 96);
  if (!(elem = $doc.getElementById('g3mWidgetHolder'))) {
    return null;
  }
  if (rp) {
    if (!elem || ($clinit_DOM() , rp.element == elem)) {
      return rp;
    }
  }
  rootPanels.size_0 == 0 && addCloseHandler(new RootPanel$2);
  !elem?(rp = new RootPanel$DefaultRootPanel):(rp = new RootPanel(elem));
  $putStringValue(rootPanels, 'g3mWidgetHolder', rp);
  $add_4(widgetsToDetach, rp);
  return rp;
}

defineClass(96, 231, $intern_27, RootPanel);
var maybeDetachCommand, rootPanels, widgetsToDetach;
var Lcom_google_gwt_user_client_ui_RootPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel', 96);
function RootPanel$1(){
}

defineClass(233, 1, {}, RootPanel$1);
_.execute_0 = function execute_4(w){
  w.isAttached() && w.onDetach();
}
;
var Lcom_google_gwt_user_client_ui_RootPanel$1_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel/1', 233);
function RootPanel$2(){
}

defineClass(234, 1, {500:1}, RootPanel$2);
var Lcom_google_gwt_user_client_ui_RootPanel$2_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel/2', 234);
function RootPanel$DefaultRootPanel(){
  RootPanel.call(this, $doc.body);
}

defineClass(232, 96, $intern_27, RootPanel$DefaultRootPanel);
var Lcom_google_gwt_user_client_ui_RootPanel$DefaultRootPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'RootPanel/DefaultRootPanel', 232);
function $add_1(this$static, w){
  var td, tr, td_0;
  tr = ($clinit_DOM() , $createTRElement($doc));
  td = (td_0 = $createTDElement($doc) , $setCellHorizontalAlignment(td_0, this$static.horzAlign) , $setCellVerticalAlignment(td_0, this$static.vertAlign) , td_0);
  $appendChild(tr, resolve(td));
  $appendChild(this$static.body_0, resolve(tr));
  $add(this$static, w, td);
}

function VerticalPanel(){
  ComplexPanel.call(this);
  this.table = ($clinit_DOM() , $createTableElement($doc));
  this.body_0 = $createTBodyElement($doc);
  $appendChild(this.table, resolve(this.body_0));
  $setElement(this, this.table);
  this.horzAlign = ($clinit_HasHorizontalAlignment() , ALIGN_DEFAULT);
  this.vertAlign = ($clinit_HasVerticalAlignment() , ALIGN_TOP);
  $setPropertyString(this.table, 'cellSpacing', '0');
  $setPropertyString(this.table, 'cellPadding', '0');
}

defineClass(305, 304, $intern_25, VerticalPanel);
_.remove = function remove_1(w){
  var removed, td;
  td = getParent(($clinit_DOM() , w.element));
  removed = $remove(this, w);
  removed && $removeChild(this.body_0, $getParentElement(($clinit_DOMImpl() , td)));
  return removed;
}
;
var Lcom_google_gwt_user_client_ui_VerticalPanel_2_classLit = createForClass('com.google.gwt.user.client.ui', 'VerticalPanel', 305);
function $add_2(this$static, w){
  $insert(this$static, w, this$static.size_0);
}

function $indexOf(this$static, w){
  var i_0;
  for (i_0 = 0; i_0 < this$static.size_0; ++i_0) {
    if (this$static.array[i_0] == w) {
      return i_0;
    }
  }
  return -1;
}

function $insert(this$static, w, beforeIndex){
  var i_0, i0, newArray;
  if (beforeIndex < 0 || beforeIndex > this$static.size_0) {
    throw new IndexOutOfBoundsException;
  }
  if (this$static.size_0 == this$static.array.length) {
    newArray = initDim(Lcom_google_gwt_user_client_ui_Widget_2_classLit, $intern_4, 29, this$static.array.length * 2, 0, 1);
    for (i0 = 0; i0 < this$static.array.length; ++i0) {
      setCheck(newArray, i0, this$static.array[i0]);
    }
    this$static.array = newArray;
  }
  ++this$static.size_0;
  for (i_0 = this$static.size_0 - 1; i_0 > beforeIndex; --i_0) {
    setCheck(this$static.array, i_0, this$static.array[i_0 - 1]);
  }
  setCheck(this$static.array, beforeIndex, w);
}

function $remove_0(this$static, index_0){
  var i_0;
  if (index_0 < 0 || index_0 >= this$static.size_0) {
    throw new IndexOutOfBoundsException;
  }
  --this$static.size_0;
  for (i_0 = index_0; i_0 < this$static.size_0; ++i_0) {
    setCheck(this$static.array, i_0, this$static.array[i_0 + 1]);
  }
  setCheck(this$static.array, this$static.size_0, null);
}

function $remove_1(this$static, w){
  var index_0;
  index_0 = $indexOf(this$static, w);
  if (index_0 == -1) {
    throw new NoSuchElementException;
  }
  $remove_0(this$static, index_0);
}

function WidgetCollection(parent_0){
  this.parent_0 = parent_0;
  this.array = initDim(Lcom_google_gwt_user_client_ui_Widget_2_classLit, $intern_4, 29, 4, 0, 1);
}

defineClass(300, 1, {}, WidgetCollection);
_.iterator = function iterator_1(){
  return new WidgetCollection$WidgetIterator(this);
}
;
_.size_0 = 0;
var Lcom_google_gwt_user_client_ui_WidgetCollection_2_classLit = createForClass('com.google.gwt.user.client.ui', 'WidgetCollection', 300);
function $next(this$static){
  if (this$static.index_0 >= this$static.this$01.size_0) {
    throw new NoSuchElementException;
  }
  this$static.currentWidget = this$static.this$01.array[this$static.index_0];
  ++this$static.index_0;
  return this$static.currentWidget;
}

function WidgetCollection$WidgetIterator(this$0){
  this.this$01 = this$0;
}

defineClass(178, 1, {}, WidgetCollection$WidgetIterator);
_.hasNext = function hasNext(){
  return this.index_0 < this.this$01.size_0;
}
;
_.next_0 = function next_0(){
  return $next(this);
}
;
_.remove_0 = function remove_2(){
  if (!this.currentWidget) {
    throw new IllegalStateException;
  }
  this.this$01.parent_0.remove(this.currentWidget);
  --this.index_0;
  this.currentWidget = null;
}
;
_.index_0 = 0;
var Lcom_google_gwt_user_client_ui_WidgetCollection$WidgetIterator_2_classLit = createForClass('com.google.gwt.user.client.ui', 'WidgetCollection/WidgetIterator', 178);
function $clinit_FocusImpl(){
  $clinit_FocusImpl = emptyMethod;
  implPanel = com_google_gwt_user_client_ui_impl_FocusImpl();
  implWidget = instanceOf(implPanel, 119)?new FocusImpl:implPanel;
}

function $getTabIndex(elem){
  return ($clinit_DOMImpl() , impl_1).getTabIndex(elem);
}

function FocusImpl(){
  $clinit_FocusImpl();
}

defineClass(181, 1, {}, FocusImpl);
_.focus_0 = function focus_0(elem){
  elem.focus();
}
;
var implPanel, implWidget;
var Lcom_google_gwt_user_client_ui_impl_FocusImpl_2_classLit = createForClass('com.google.gwt.user.client.ui.impl', 'FocusImpl', 181);
function FocusImplIE6(){
  $clinit_FocusImpl();
}

defineClass(333, 181, {}, FocusImplIE6);
_.focus_0 = function focus_1(elem){
  try {
    elem.focus();
  }
   catch (e) {
    if (!elem || !elem.focus) {
      throw e;
    }
  }
}
;
var Lcom_google_gwt_user_client_ui_impl_FocusImplIE6_2_classLit = createForClass('com.google.gwt.user.client.ui.impl', 'FocusImplIE6', 333);
function FocusImplStandard(){
  $clinit_FocusImpl();
}

defineClass(119, 181, {119:1}, FocusImplStandard);
var Lcom_google_gwt_user_client_ui_impl_FocusImplStandard_2_classLit = createForClass('com.google.gwt.user.client.ui.impl', 'FocusImplStandard', 119);
function FocusImplSafari(){
  $clinit_FocusImpl();
}

defineClass(334, 119, {119:1}, FocusImplSafari);
_.focus_0 = function focus_2(elem){
  $wnd.setTimeout(function(){
    elem.focus();
  }
  , 0);
}
;
var Lcom_google_gwt_user_client_ui_impl_FocusImplSafari_2_classLit = createForClass('com.google.gwt.user.client.ui.impl', 'FocusImplSafari', 334);
function assertCompileTimeUserAgent(){
  var compileTimeValue, impl, runtimeValue;
  impl = dynamicCast(com_google_gwt_useragent_client_UserAgent(), 134);
  compileTimeValue = impl.getCompileTimeValue();
  runtimeValue = impl.getRuntimeValue();
  if (!$equals(compileTimeValue, runtimeValue)) {
    throw new UserAgentAsserter$UserAgentAssertionError(compileTimeValue, runtimeValue);
  }
}

function Error_0(message, cause){
  Throwable.call(this, message, cause);
}

defineClass(136, 13, $intern_1);
var Ljava_lang_Error_2_classLit = createForClass('java.lang', 'Error', 136);
defineClass(31, 136, $intern_1);
var Ljava_lang_AssertionError_2_classLit = createForClass('java.lang', 'AssertionError', 31);
function UserAgentAsserter$UserAgentAssertionError(compileTimeValue, runtimeValue){
  Error_0.call(this, '' + ('Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (' + compileTimeValue + ') ' + 'does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.'), instanceOf('Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (' + compileTimeValue + ') ' + 'does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.', 13)?dynamicCast('Possible problem with your *.gwt.xml module file.\nThe compile time user.agent value (' + compileTimeValue + ') ' + 'does not match the runtime user.agent value (' + runtimeValue + ').\n' + 'Expect more errors.', 13):null);
}

defineClass(218, 31, $intern_1, UserAgentAsserter$UserAgentAssertionError);
var Lcom_google_gwt_useragent_client_UserAgentAsserter$UserAgentAssertionError_2_classLit = createForClass('com.google.gwt.useragent.client', 'UserAgentAsserter/UserAgentAssertionError', 218);
function UserAgentImplGecko1_8(){
}

defineClass(265, 1, $intern_28, UserAgentImplGecko1_8);
_.getCompileTimeValue = function getCompileTimeValue(){
  return 'gecko1_8';
}
;
_.getRuntimeValue = function getRuntimeValue(){
  var ua = navigator.userAgent.toLowerCase();
  var docMode = $doc.documentMode;
  if (function(){
    return ua.indexOf('webkit') != -1;
  }
  ())
    return 'safari';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 10 && docMode < 11;
  }
  ())
    return 'ie10';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 9 && docMode < 11;
  }
  ())
    return 'ie9';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 8 && docMode < 11;
  }
  ())
    return 'ie8';
  if (function(){
    return ua.indexOf('gecko') != -1 || docMode >= 11;
  }
  ())
    return 'gecko1_8';
  return 'unknown';
}
;
var Lcom_google_gwt_useragent_client_UserAgentImplGecko1_18_2_classLit = createForClass('com.google.gwt.useragent.client', 'UserAgentImplGecko1_8', 265);
function UserAgentImplIe10(){
}

defineClass(267, 1, $intern_28, UserAgentImplIe10);
_.getCompileTimeValue = function getCompileTimeValue_0(){
  return 'ie10';
}
;
_.getRuntimeValue = function getRuntimeValue_0(){
  var ua = navigator.userAgent.toLowerCase();
  var docMode = $doc.documentMode;
  if (function(){
    return ua.indexOf('webkit') != -1;
  }
  ())
    return 'safari';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 10 && docMode < 11;
  }
  ())
    return 'ie10';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 9 && docMode < 11;
  }
  ())
    return 'ie9';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 8 && docMode < 11;
  }
  ())
    return 'ie8';
  if (function(){
    return ua.indexOf('gecko') != -1 || docMode >= 11;
  }
  ())
    return 'gecko1_8';
  return 'unknown';
}
;
var Lcom_google_gwt_useragent_client_UserAgentImplIe10_2_classLit = createForClass('com.google.gwt.useragent.client', 'UserAgentImplIe10', 267);
function UserAgentImplIe8(){
}

defineClass(269, 1, $intern_28, UserAgentImplIe8);
_.getCompileTimeValue = function getCompileTimeValue_1(){
  return 'ie8';
}
;
_.getRuntimeValue = function getRuntimeValue_1(){
  var ua = navigator.userAgent.toLowerCase();
  var docMode = $doc.documentMode;
  if (function(){
    return ua.indexOf('webkit') != -1;
  }
  ())
    return 'safari';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 10 && docMode < 11;
  }
  ())
    return 'ie10';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 9 && docMode < 11;
  }
  ())
    return 'ie9';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 8 && docMode < 11;
  }
  ())
    return 'ie8';
  if (function(){
    return ua.indexOf('gecko') != -1 || docMode >= 11;
  }
  ())
    return 'gecko1_8';
  return 'unknown';
}
;
var Lcom_google_gwt_useragent_client_UserAgentImplIe8_2_classLit = createForClass('com.google.gwt.useragent.client', 'UserAgentImplIe8', 269);
function UserAgentImplIe9(){
}

defineClass(268, 1, $intern_28, UserAgentImplIe9);
_.getCompileTimeValue = function getCompileTimeValue_2(){
  return 'ie9';
}
;
_.getRuntimeValue = function getRuntimeValue_2(){
  var ua = navigator.userAgent.toLowerCase();
  var docMode = $doc.documentMode;
  if (function(){
    return ua.indexOf('webkit') != -1;
  }
  ())
    return 'safari';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 10 && docMode < 11;
  }
  ())
    return 'ie10';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 9 && docMode < 11;
  }
  ())
    return 'ie9';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 8 && docMode < 11;
  }
  ())
    return 'ie8';
  if (function(){
    return ua.indexOf('gecko') != -1 || docMode >= 11;
  }
  ())
    return 'gecko1_8';
  return 'unknown';
}
;
var Lcom_google_gwt_useragent_client_UserAgentImplIe9_2_classLit = createForClass('com.google.gwt.useragent.client', 'UserAgentImplIe9', 268);
function UserAgentImplSafari(){
}

defineClass(266, 1, $intern_28, UserAgentImplSafari);
_.getCompileTimeValue = function getCompileTimeValue_3(){
  return 'safari';
}
;
_.getRuntimeValue = function getRuntimeValue_3(){
  var ua = navigator.userAgent.toLowerCase();
  var docMode = $doc.documentMode;
  if (function(){
    return ua.indexOf('webkit') != -1;
  }
  ())
    return 'safari';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 10 && docMode < 11;
  }
  ())
    return 'ie10';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 9 && docMode < 11;
  }
  ())
    return 'ie9';
  if (function(){
    return ua.indexOf('msie') != -1 && docMode >= 8 && docMode < 11;
  }
  ())
    return 'ie8';
  if (function(){
    return ua.indexOf('gecko') != -1 || docMode >= 11;
  }
  ())
    return 'gecko1_8';
  return 'unknown';
}
;
var Lcom_google_gwt_useragent_client_UserAgentImplSafari_2_classLit = createForClass('com.google.gwt.useragent.client', 'UserAgentImplSafari', 266);
function SimpleEventBus$1(){
}

defineClass(283, 1, {}, SimpleEventBus$1);
var Lcom_google_web_bindery_event_shared_SimpleEventBus$1_2_classLit = createForClass('com.google.web.bindery.event.shared', 'SimpleEventBus/1', 283);
function SimpleEventBus$2(this$0, val$type, val$handler){
  this.this$01 = this$0;
  this.val$type2 = val$type;
  this.val$source3 = null;
  this.val$handler4 = val$handler;
}

defineClass(284, 1, {501:1}, SimpleEventBus$2);
var Lcom_google_web_bindery_event_shared_SimpleEventBus$2_2_classLit = createForClass('com.google.web.bindery.event.shared', 'SimpleEventBus/2', 284);
function AbstractStringBuilder(string){
  this.string = string;
}

defineClass(137, 1, {});
_.toString$ = function toString_9(){
  return this.string;
}
;
var Ljava_lang_AbstractStringBuilder_2_classLit = createForClass('java.lang', 'AbstractStringBuilder', 137);
function ArithmeticException(){
  RuntimeException_0.call(this, 'divide by zero');
}

defineClass(229, 17, $intern_1, ArithmeticException);
var Ljava_lang_ArithmeticException_2_classLit = createForClass('java.lang', 'ArithmeticException', 229);
function ArrayStoreException(){
  RuntimeException.call(this);
}

function ArrayStoreException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(95, 17, $intern_1, ArrayStoreException, ArrayStoreException_0);
var Ljava_lang_ArrayStoreException_2_classLit = createForClass('java.lang', 'ArrayStoreException', 95);
function ClassCastException(){
  RuntimeException.call(this);
}

defineClass(83, 17, $intern_1, ClassCastException);
var Ljava_lang_ClassCastException_2_classLit = createForClass('java.lang', 'ClassCastException', 83);
defineClass(111, 1, {3:1, 111:1});
var Ljava_lang_Number_2_classLit = createForClass('java.lang', 'Number', 111);
function $clinit_Double(){
  $clinit_Double = emptyMethod;
  powers = initValues(getClassLiteralForArray(D_classLit, 1), $intern_29, 0, 7, [1.3407807929942597E154, 1.157920892373162E77, 3.4028236692093846E38, 1.8446744073709552E19, 4294967296, $intern_16, 256, 16, 4, 2]);
  invPowers = initValues(getClassLiteralForArray(D_classLit, 1), $intern_29, 0, 7, [7.458340731200207E-155, 8.636168555094445E-78, 2.9387358770557188E-39, 5.421010862427522E-20, 2.3283064365386963E-10, $intern_30, 0.00390625, 0.0625, 0.25, 0.5]);
}

function $compareTo_0(this$static, b){
  return compare(this$static.value_0, b.value_0);
}

function Double(value_0){
  $clinit_Double();
  this.value_0 = value_0;
}

function compare(x_0, y_0){
  if (x_0 < y_0) {
    return -1;
  }
  if (x_0 > y_0) {
    return 1;
  }
  if (x_0 == y_0) {
    return 0;
  }
  return isNaN_0(x_0)?isNaN_0(y_0)?0:1:-1;
}

function doubleToLongBits(value_0){
  $clinit_Double();
  var bit, exp_0, i_0, ihi, ilo, negative;
  if (isNaN_0(value_0)) {
    return {l:0, m:0, h:524160};
  }
  negative = false;
  if (value_0 == 0) {
    return 1 / value_0 == -Infinity?{l:0, m:0, h:$intern_9}:{l:0, m:0, h:0};
  }
  if (value_0 < 0) {
    negative = true;
    value_0 = -value_0;
  }
  if (!isFinite(value_0) && !isNaN(value_0)) {
    return negative?{l:0, m:0, h:1048320}:{l:0, m:0, h:524032};
  }
  exp_0 = 0;
  if (value_0 < 1) {
    bit = 512;
    for (i_0 = 0; i_0 < 10; ++i_0 , bit >>= 1) {
      if (value_0 < invPowers[i_0] && exp_0 - bit >= -1023) {
        value_0 *= powers[i_0];
        exp_0 -= bit;
      }
    }
    if (value_0 < 1 && exp_0 - 1 >= -1023) {
      value_0 *= 2;
      --exp_0;
    }
  }
   else if (value_0 >= 2) {
    bit = 512;
    for (i_0 = 0; i_0 < 10; ++i_0 , bit >>= 1) {
      if (value_0 >= powers[i_0]) {
        value_0 *= invPowers[i_0];
        exp_0 += bit;
      }
    }
  }
  exp_0 > -1023?(value_0 -= 1):(value_0 *= 0.5);
  ihi = fromDouble(value_0 * $intern_19);
  value_0 -= toDouble(ihi) * 9.5367431640625E-7;
  ilo = fromDouble(value_0 * 4503599627370496);
  ihi = or(ihi, fromInt(exp_0 + 1023 << 20));
  negative && (ihi = or(ihi, {l:0, m:512, h:0}));
  return or(shl(ihi, 32), ilo);
}

function isNaN_0(x_0){
  $clinit_Double();
  return isNaN(x_0);
}

defineClass(32, 111, {3:1, 26:1, 32:1, 111:1}, Double);
_.compareTo = function compareTo_0(b){
  return $compareTo_0(this, dynamicCast(b, 32));
}
;
_.equals$ = function equals_1(o){
  return instanceOf(o, 32) && dynamicCast(o, 32).value_0 == this.value_0;
}
;
_.hashCode$ = function hashCode_3(){
  return round_int(this.value_0);
}
;
_.toString$ = function toString_11(){
  return '' + this.value_0;
}
;
_.value_0 = 0;
var invPowers, powers;
var Ljava_lang_Double_2_classLit = createForClass('java.lang', 'Double', 32);
function IllegalArgumentException(message){
  RuntimeException_0.call(this, message);
}

defineClass(113, 17, $intern_1, IllegalArgumentException);
var Ljava_lang_IllegalArgumentException_2_classLit = createForClass('java.lang', 'IllegalArgumentException', 113);
function IllegalStateException(){
  RuntimeException.call(this);
}

function IllegalStateException_0(s){
  RuntimeException_0.call(this, s);
}

defineClass(55, 17, $intern_1, IllegalStateException, IllegalStateException_0);
var Ljava_lang_IllegalStateException_2_classLit = createForClass('java.lang', 'IllegalStateException', 55);
function IndexOutOfBoundsException(){
  RuntimeException.call(this);
}

function IndexOutOfBoundsException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(77, 17, $intern_1, IndexOutOfBoundsException, IndexOutOfBoundsException_0);
var Ljava_lang_IndexOutOfBoundsException_2_classLit = createForClass('java.lang', 'IndexOutOfBoundsException', 77);
function $compareTo_1(this$static, b){
  return compare_0(this$static.value_0, b.value_0);
}

function Integer(value_0){
  this.value_0 = value_0;
}

function compare_0(x_0, y_0){
  return x_0 < y_0?-1:x_0 > y_0?1:0;
}

function numberOfLeadingZeros_0(i_0){
  var m, n, y_0;
  if (i_0 < 0) {
    return 0;
  }
   else if (i_0 == 0) {
    return 32;
  }
   else {
    y_0 = -(i_0 >> 16);
    m = y_0 >> 16 & 16;
    n = 16 - m;
    i_0 = i_0 >> m;
    y_0 = i_0 - 256;
    m = y_0 >> 16 & 8;
    n += m;
    i_0 <<= m;
    y_0 = i_0 - 4096;
    m = y_0 >> 16 & 4;
    n += m;
    i_0 <<= m;
    y_0 = i_0 - $intern_15;
    m = y_0 >> 16 & 2;
    n += m;
    i_0 <<= m;
    y_0 = i_0 >> 14;
    m = y_0 & ~(y_0 >> 1);
    return n + 2 - m;
  }
}

function numberOfTrailingZeros(i_0){
  var r, rtn;
  if (i_0 == 0) {
    return 32;
  }
   else {
    rtn = 0;
    for (r = 1; (r & i_0) == 0; r <<= 1) {
      ++rtn;
    }
    return rtn;
  }
}

function valueOf(i_0){
  var rebase, result;
  if (i_0 > -129 && i_0 < 128) {
    rebase = i_0 + 128;
    result = ($clinit_Integer$BoxedValues() , boxedValues_0)[rebase];
    !result && (result = boxedValues_0[rebase] = new Integer(i_0));
    return result;
  }
  return new Integer(i_0);
}

defineClass(72, 111, {3:1, 26:1, 72:1, 111:1}, Integer);
_.compareTo = function compareTo_1(b){
  return $compareTo_1(this, dynamicCast(b, 72));
}
;
_.equals$ = function equals_2(o){
  return instanceOf(o, 72) && dynamicCast(o, 72).value_0 == this.value_0;
}
;
_.hashCode$ = function hashCode_4(){
  return this.value_0;
}
;
_.toString$ = function toString_12(){
  return '' + this.value_0;
}
;
_.value_0 = 0;
var Ljava_lang_Integer_2_classLit = createForClass('java.lang', 'Integer', 72);
function $clinit_Integer$BoxedValues(){
  $clinit_Integer$BoxedValues = emptyMethod;
  boxedValues_0 = initDim(Ljava_lang_Integer_2_classLit, $intern_4, 72, 256, 0, 1);
}

var boxedValues_0;
function $compareTo_2(this$static, b){
  return compare_1(this$static.value_0, b.value_0);
}

function Long(value_0){
  this.value_0 = value_0;
}

function compare_1(x_0, y_0){
  return lt(x_0, y_0)?-1:gt(x_0, y_0)?1:0;
}

function valueOf_0(i_0){
  var rebase, result;
  if (gt(i_0, {l:4194175, m:$intern_7, h:$intern_8}) && lt(i_0, {l:128, m:0, h:0})) {
    rebase = toInt(i_0) + 128;
    result = ($clinit_Long$BoxedValues() , boxedValues_1)[rebase];
    !result && (result = boxedValues_1[rebase] = new Long(i_0));
    return result;
  }
  return new Long(i_0);
}

defineClass(46, 111, {3:1, 26:1, 46:1, 111:1}, Long);
_.compareTo = function compareTo_2(b){
  return $compareTo_2(this, dynamicCast(b, 46));
}
;
_.equals$ = function equals_3(o){
  return instanceOf(o, 46) && eq(dynamicCast(o, 46).value_0, this.value_0);
}
;
_.hashCode$ = function hashCode_5(){
  return toInt(this.value_0);
}
;
_.toString$ = function toString_13(){
  return '' + toString_8(this.value_0);
}
;
_.value_0 = {l:0, m:0, h:0};
var Ljava_lang_Long_2_classLit = createForClass('java.lang', 'Long', 46);
function $clinit_Long$BoxedValues(){
  $clinit_Long$BoxedValues = emptyMethod;
  boxedValues_1 = initDim(Ljava_lang_Long_2_classLit, $intern_4, 46, 256, 0, 1);
}

var boxedValues_1;
function abs_0(x_0){
  return x_0 <= 0?0 - x_0:x_0;
}

function acos_0(x_0){
  return Math.acos(x_0);
}

function asin_0(x_0){
  return Math.asin(x_0);
}

function atan2_0(y_0, x_0){
  return Math.atan2(y_0, x_0);
}

function ceil_0(x_0){
  return Math.ceil(x_0);
}

function cos_0(x_0){
  return Math.cos(x_0);
}

function max_1(y_0){
  return 0 > y_0?0:y_0;
}

function min_1(x_0, y_0){
  return x_0 < y_0?x_0:y_0;
}

function pow_0(x_0, exp_0){
  return Math.pow(x_0, exp_0);
}

function round_0(x_0){
  return Math.round(x_0);
}

function sin_0(x_0){
  return Math.sin(x_0);
}

function sqrt_0(x_0){
  return Math.sqrt(x_0);
}

function NullPointerException(){
  RuntimeException.call(this);
}

function NullPointerException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(97, 17, $intern_1, NullPointerException, NullPointerException_0);
var Ljava_lang_NullPointerException_2_classLit = createForClass('java.lang', 'NullPointerException', 97);
function StackTraceElement(methodName, fileName, lineNumber){
  this.className_0 = 'Unknown';
  this.methodName = methodName;
  this.fileName = fileName;
  this.lineNumber = lineNumber;
}

defineClass(63, 1, {3:1, 63:1}, StackTraceElement);
_.equals$ = function equals_4(other){
  var st;
  if (instanceOf(other, 63)) {
    st = dynamicCast(other, 63);
    return this.lineNumber == st.lineNumber && equals_15(this.methodName, st.methodName) && equals_15(this.className_0, st.className_0) && equals_15(this.fileName, st.fileName);
  }
  return false;
}
;
_.hashCode$ = function hashCode_6(){
  return hashCode_12(initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(this.lineNumber), this.className_0, this.methodName, this.fileName]));
}
;
_.toString$ = function toString_14(){
  return this.className_0 + '.' + this.methodName + '(' + (this.fileName != null?this.fileName:'Unknown Source') + (this.lineNumber >= 0?':' + this.lineNumber:'') + ')';
}
;
_.lineNumber = 0;
var Ljava_lang_StackTraceElement_2_classLit = createForClass('java.lang', 'StackTraceElement', 63);
function $charAt(this$static, index_0){
  return this$static.charCodeAt(index_0);
}

function $equals(this$static, other){
  return this$static === other;
}

function $equalsIgnoreCase(this$static, other){
  if (other == null) {
    return false;
  }
  if (this$static == other) {
    return true;
  }
  return this$static.length == other.length && this$static.toLowerCase() == other.toLowerCase();
}

function $indexOf_0(this$static, str){
  return this$static.indexOf(str);
}

function $lastIndexOf(this$static, str){
  return this$static.lastIndexOf(str);
}

function $lastIndexOf_0(this$static, str, start_0){
  return this$static.lastIndexOf(str, start_0);
}

function $split(this$static, regex, maxMatch){
  var compiled = new RegExp(regex, 'g');
  var out = [];
  var count = 0;
  var trail = this$static;
  var lastTrail = null;
  while (true) {
    var matchObj = compiled.exec(trail);
    if (matchObj == null || trail == '' || count == maxMatch - 1 && maxMatch > 0) {
      out[count] = trail;
      break;
    }
     else {
      out[count] = trail.substring(0, matchObj.index);
      trail = trail.substring(matchObj.index + matchObj[0].length, trail.length);
      compiled.lastIndex = 0;
      if (lastTrail == trail) {
        out[count] = trail.substring(0, 1);
        trail = trail.substring(1);
      }
      lastTrail = trail;
      count++;
    }
  }
  if (maxMatch == 0 && this$static.length > 0) {
    var lastNonEmpty = out.length;
    while (lastNonEmpty > 0 && out[lastNonEmpty - 1] == '') {
      --lastNonEmpty;
    }
    lastNonEmpty < out.length && out.splice(lastNonEmpty, out.length - lastNonEmpty);
  }
  var jr = __createArray(out.length);
  for (var i_0 = 0; i_0 < out.length; ++i_0) {
    jr[i_0] = out[i_0];
  }
  return jr;
}

function $trim(this$static){
  if (this$static.length == 0 || this$static[0] > ' ' && this$static[this$static.length - 1] > ' ') {
    return this$static;
  }
  return this$static.replace(/^[\u0000-\u0020]*|[\u0000-\u0020]*$/g, '');
}

function __createArray(numElements){
  return initDim(Ljava_lang_String_2_classLit, $intern_4, 2, numElements, 4, 1);
}

function __substr(str, beginIndex, len){
  return str.substr(beginIndex, len);
}

function compareTo_3(thisStr, otherStr){
  if (thisStr == otherStr) {
    return 0;
  }
  return thisStr < otherStr?-1:1;
}

function compareTo_Ljava_lang_Object__I__devirtual$(this$static, other){
  return isJavaString(this$static)?compareTo_3(this$static, dynamicCastToString(other)):this$static.compareTo(other);
}

function fromCodePoint(codePoint){
  var hiSurrogate, loSurrogate;
  if (codePoint >= $intern_16) {
    hiSurrogate = 55296 + (codePoint - $intern_16 >> 10 & 1023) & $intern_3;
    loSurrogate = 56320 + (codePoint - $intern_16 & 1023) & $intern_3;
    return valueOf_1(hiSurrogate) + valueOf_1(loSurrogate);
  }
   else {
    return String.fromCharCode(codePoint & $intern_3);
  }
}

function valueOf_1(x_0){
  return String.fromCharCode(x_0);
}

var Ljava_lang_String_2_classLit = createForClass('java.lang', 'String', 2);
function $clinit_String$HashCache(){
  $clinit_String$HashCache = emptyMethod;
  back_0 = {};
  front = {};
}

function compute(str){
  var hashCode, i_0, n, nBatch;
  hashCode = 0;
  n = str.length;
  nBatch = n - 4;
  i_0 = 0;
  while (i_0 < nBatch) {
    hashCode = str.charCodeAt(i_0 + 3) + 31 * (str.charCodeAt(i_0 + 2) + 31 * (str.charCodeAt(i_0 + 1) + 31 * (str.charCodeAt(i_0) + 31 * hashCode)));
    hashCode = ~~hashCode;
    i_0 += 4;
  }
  while (i_0 < n) {
    hashCode = hashCode * 31 + $charAt(str, i_0++);
  }
  hashCode = ~~hashCode;
  return hashCode;
}

function getHashCode_0(str){
  $clinit_String$HashCache();
  var key = ':' + str;
  var result = front[key];
  if (result != null) {
    return result;
  }
  result = back_0[key];
  result == null && (result = compute(str));
  increment();
  return front[key] = result;
}

function increment(){
  if (count_0 == 256) {
    back_0 = front;
    front = {};
    count_0 = 0;
  }
  ++count_0;
}

var back_0, count_0 = 0, front;
function $append(this$static, x_0){
  this$static.string += x_0;
  return this$static;
}

function $append_0(this$static, x_0){
  this$static.string += x_0;
  return this$static;
}

function StringBuilder(){
  AbstractStringBuilder.call(this, '');
}

function StringBuilder_0(){
  AbstractStringBuilder.call(this, '');
}

function StringBuilder_1(s){
  AbstractStringBuilder.call(this, s);
}

defineClass(73, 137, {499:1}, StringBuilder, StringBuilder_0, StringBuilder_1);
var Ljava_lang_StringBuilder_2_classLit = createForClass('java.lang', 'StringBuilder', 73);
function arraycopy(src_0, srcOfs, dest, destOfs, len){
  var destArray, destComp, destEnd, destType, destlen, srcArray, srcComp, srcType, srclen;
  checkNotNull_0(src_0, 'src');
  checkNotNull_0(dest, 'dest');
  srcType = getClass__Ljava_lang_Class___devirtual$(src_0);
  destType = getClass__Ljava_lang_Class___devirtual$(dest);
  checkArrayType((srcType.modifiers & 4) != 0, 'srcType is not an array');
  checkArrayType((destType.modifiers & 4) != 0, 'destType is not an array');
  srcComp = srcType.componentType;
  destComp = destType.componentType;
  checkArrayType((srcComp.modifiers & 1) != 0?srcComp == destComp:(destComp.modifiers & 1) == 0, "Array types don't match");
  srclen = src_0.length;
  destlen = dest.length;
  if (srcOfs < 0 || destOfs < 0 || len < 0 || srcOfs + len > srclen || destOfs + len > destlen) {
    throw new IndexOutOfBoundsException;
  }
  if (((srcComp.modifiers & 1) == 0 || (srcComp.modifiers & 4) != 0) && srcType != destType) {
    srcArray = dynamicCast(src_0, 4);
    destArray = dynamicCast(dest, 4);
    if (maskUndefined(src_0) === maskUndefined(dest) && srcOfs < destOfs) {
      srcOfs += len;
      for (destEnd = destOfs + len; destEnd-- > destOfs;) {
        setCheck(destArray, destEnd, srcArray[--srcOfs]);
      }
    }
     else {
      for (destEnd = destOfs + len; destOfs < destEnd;) {
        setCheck(destArray, destOfs++, srcArray[srcOfs++]);
      }
    }
  }
   else 
    len > 0 && nativeArraySplice(src_0, srcOfs, dest, destOfs, len, true);
}

function UnsupportedOperationException(){
  RuntimeException.call(this);
}

function UnsupportedOperationException_0(message){
  RuntimeException_0.call(this, message);
}

defineClass(54, 17, $intern_1, UnsupportedOperationException, UnsupportedOperationException_0);
var Ljava_lang_UnsupportedOperationException_2_classLit = createForClass('java.lang', 'UnsupportedOperationException', 54);
function $advanceToFind(this$static, o){
  var e, iter;
  for (iter = this$static.iterator(); iter.hasNext();) {
    e = iter.next_0();
    if (maskUndefined(o) === maskUndefined(e) || o != null && equals_Ljava_lang_Object__Z__devirtual$(o, e)) {
      return true;
    }
  }
  return false;
}

function $containsAll(this$static, c){
  var e, e$iterator;
  checkNotNull(c);
  for (e$iterator = c.iterator(); e$iterator.hasNext();) {
    e = e$iterator.next_0();
    if (!this$static.contains_0(e)) {
      return false;
    }
  }
  return true;
}

function $toString_0(this$static){
  var comma, e, e$iterator, sb;
  sb = new StringBuilder_1('[');
  comma = false;
  for (e$iterator = this$static.iterator(); e$iterator.hasNext();) {
    e = e$iterator.next_0();
    comma?(sb.string += ', ' , sb):(comma = true);
    sb.string += e === this$static?'(this Collection)':'' + e;
  }
  sb.string += ']';
  return sb.string;
}

defineClass(431, 1, {});
_.contains_0 = function contains(o){
  return $advanceToFind(this, o);
}
;
_.toString$ = function toString_15(){
  return $toString_0(this);
}
;
var Ljava_util_AbstractCollection_2_classLit = createForClass('java.util', 'AbstractCollection', 431);
function $containsEntry(this$static, entry){
  var key, ourValue, value_0;
  key = entry.getKey();
  value_0 = entry.getValue();
  ourValue = isJavaString(key)?$getStringValue(this$static, key):getEntryValueOrNull($getEntry(this$static.hashCodeMap, key));
  if (!(maskUndefined(value_0) === maskUndefined(ourValue) || value_0 != null && equals_Ljava_lang_Object__Z__devirtual$(value_0, ourValue))) {
    return false;
  }
  if (ourValue == null && !(isJavaString(key)?$hasStringValue(this$static, key):!!$getEntry(this$static.hashCodeMap, key))) {
    return false;
  }
  return true;
}

function $implFindEntry(this$static, key){
  var entry, iter, k;
  for (iter = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this$static)).this$01); $hasNext(iter);) {
    entry = (checkStructuralChange(iter.this$01, iter) , checkCriticalElement($hasNext(iter)) , iter.last = iter.current , dynamicCast(iter.current.next_0(), 22));
    k = entry.getKey();
    if (maskUndefined(key) === maskUndefined(k) || key != null && equals_Ljava_lang_Object__Z__devirtual$(key, k)) {
      return entry;
    }
  }
  return null;
}

function $toString_1(this$static, o){
  return o === this$static?'(this Map)':'' + o;
}

function getEntryValueOrNull(entry){
  return !entry?null:entry.getValue();
}

defineClass(430, 1, {93:1});
_.equals$ = function equals_5(obj){
  var entry, entry$iterator, otherMap;
  if (obj === this) {
    return true;
  }
  if (!instanceOf(obj, 93)) {
    return false;
  }
  otherMap = dynamicCast(obj, 93);
  if (this.size_0 != otherMap.size_1()) {
    return false;
  }
  for (entry$iterator = otherMap.entrySet_0().iterator(); entry$iterator.hasNext();) {
    entry = dynamicCast(entry$iterator.next_0(), 22);
    if (!$containsEntry(this, entry)) {
      return false;
    }
  }
  return true;
}
;
_.get_0 = function get_1(key){
  return getEntryValueOrNull($implFindEntry(this, key));
}
;
_.hashCode$ = function hashCode_7(){
  return hashCode_13(new AbstractHashMap$EntrySet(this));
}
;
_.put = function put(key, value_0){
  throw new UnsupportedOperationException_0('Put not supported on this map');
}
;
_.size_1 = function size_1(){
  return (new AbstractHashMap$EntrySet(this)).this$01.size_0;
}
;
_.toString$ = function toString_16(){
  var comma, entry, entry$iterator, sb;
  sb = new StringBuilder_1('{');
  comma = false;
  for (entry$iterator = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this)).this$01); $hasNext(entry$iterator);) {
    entry = (checkStructuralChange(entry$iterator.this$01, entry$iterator) , checkCriticalElement($hasNext(entry$iterator)) , entry$iterator.last = entry$iterator.current , dynamicCast(entry$iterator.current.next_0(), 22));
    comma?(sb.string += ', ' , sb):(comma = true);
    $append_0(sb, $toString_1(this, entry.getKey()));
    sb.string += '=';
    $append_0(sb, $toString_1(this, entry.getValue()));
  }
  sb.string += '}';
  return sb.string;
}
;
var Ljava_util_AbstractMap_2_classLit = createForClass('java.util', 'AbstractMap', 430);
function $containsKey(this$static, key){
  return isJavaString(key)?$hasStringValue(this$static, key):!!$getEntry(this$static.hashCodeMap, key);
}

function $containsValue(this$static, value_0){
  return this$static.stringMap.containsValue(value_0) || this$static.hashCodeMap.containsValue(value_0);
}

function $elementAdded(this$static){
  ++this$static.size_0;
  structureChanged(this$static);
}

function $elementRemoved(this$static){
  --this$static.size_0;
  structureChanged(this$static);
}

function $get(this$static, key){
  return isJavaString(key)?$getStringValue(this$static, key):getEntryValueOrNull($getEntry(this$static.hashCodeMap, key));
}

function $getStringValue(this$static, key){
  return key == null?getEntryValueOrNull($getEntry(this$static.hashCodeMap, null)):this$static.stringMap.get_2(key);
}

function $hasStringValue(this$static, key){
  return key == null?!!$getEntry(this$static.hashCodeMap, null):!(this$static.stringMap.get_2(key) === undefined);
}

function $put(this$static, key, value_0){
  return isJavaString(key)?$putStringValue(this$static, key, value_0):$put_0(this$static.hashCodeMap, key, value_0);
}

function $putStringValue(this$static, key, value_0){
  return key == null?$put_0(this$static.hashCodeMap, null, value_0):this$static.stringMap.put_0(key, value_0);
}

function $remove_2(this$static, key){
  return isJavaString(key)?$removeStringValue(this$static, key):$remove_7(this$static.hashCodeMap, key);
}

function $removeStringValue(this$static, key){
  return key == null?$remove_7(this$static.hashCodeMap, null):this$static.stringMap.remove_2(key);
}

function $reset(this$static){
  $clinit_InternalJsMapFactory$BackwardCompatibleJsMapFactory();
  this$static.hashCodeMap = delegate.createJsHashCodeMap();
  this$static.hashCodeMap.host = this$static;
  this$static.stringMap = delegate.createJsStringMap();
  this$static.stringMap.host = this$static;
  this$static.size_0 = 0;
  structureChanged(this$static);
}

defineClass(223, 430, {93:1});
_.entrySet_0 = function entrySet(){
  return new AbstractHashMap$EntrySet(this);
}
;
_.get_0 = function get_2(key){
  return $get(this, key);
}
;
_.put = function put_0(key, value_0){
  return $putStringValue(this, key, value_0);
}
;
_.size_1 = function size_2(){
  return this.size_0;
}
;
_.size_0 = 0;
var Ljava_util_AbstractHashMap_2_classLit = createForClass('java.util', 'AbstractHashMap', 223);
defineClass(432, 431, $intern_31);
_.equals$ = function equals_6(o){
  var other;
  if (o === this) {
    return true;
  }
  if (!instanceOf(o, 94)) {
    return false;
  }
  other = dynamicCast(o, 94);
  if (other.size_1() != this.size_1()) {
    return false;
  }
  return $containsAll(this, other);
}
;
_.hashCode$ = function hashCode_8(){
  return hashCode_13(this);
}
;
var Ljava_util_AbstractSet_2_classLit = createForClass('java.util', 'AbstractSet', 432);
function $contains(this$static, o){
  if (instanceOf(o, 22)) {
    return $containsEntry(this$static.this$01, dynamicCast(o, 22));
  }
  return false;
}

function AbstractHashMap$EntrySet(this$0){
  this.this$01 = this$0;
}

defineClass(43, 432, $intern_31, AbstractHashMap$EntrySet);
_.contains_0 = function contains_0(o){
  return $contains(this, o);
}
;
_.iterator = function iterator_2(){
  return new AbstractHashMap$EntrySetIterator(this.this$01);
}
;
_.size_1 = function size_3(){
  return this.this$01.size_0;
}
;
var Ljava_util_AbstractHashMap$EntrySet_2_classLit = createForClass('java.util', 'AbstractHashMap/EntrySet', 43);
function $hasNext(this$static){
  if (this$static.current.hasNext()) {
    return true;
  }
  if (this$static.current != this$static.stringMapEntries) {
    return false;
  }
  this$static.current = this$static.this$01.hashCodeMap.entries();
  return this$static.current.hasNext();
}

function $next_0(this$static){
  return checkStructuralChange(this$static.this$01, this$static) , checkCriticalElement($hasNext(this$static)) , this$static.last = this$static.current , dynamicCast(this$static.current.next_0(), 22);
}

function $remove_3(this$static){
  checkState(!!this$static.last);
  checkStructuralChange(this$static.this$01, this$static);
  this$static.last.remove_0();
  this$static.last = null;
  recordLastKnownStructure(this$static.this$01, this$static);
}

function AbstractHashMap$EntrySetIterator(this$0){
  this.this$01 = this$0;
  this.stringMapEntries = this.this$01.stringMap.entries();
  this.current = this.stringMapEntries;
  setModCount(this, this$0._gwt_modCount);
}

defineClass(47, 1, {}, AbstractHashMap$EntrySetIterator);
_.hasNext = function hasNext_0(){
  return $hasNext(this);
}
;
_.next_0 = function next_1(){
  return $next_0(this);
}
;
_.remove_0 = function remove_3(){
  $remove_3(this);
}
;
var Ljava_util_AbstractHashMap$EntrySetIterator_2_classLit = createForClass('java.util', 'AbstractHashMap/EntrySetIterator', 47);
function $indexOf_1(this$static, toFind){
  var i_0, n;
  for (i_0 = 0 , n = this$static.array.length; i_0 < n; ++i_0) {
    if (equals_15(toFind, (checkElementIndex(i_0, this$static.array.length) , this$static.array[i_0]))) {
      return i_0;
    }
  }
  return -1;
}

defineClass(435, 431, {37:1});
_.add_0 = function add_1(index_0, element){
  throw new UnsupportedOperationException_0('Add not supported on this list');
}
;
_.add_1 = function add_2(obj){
  this.add_0(this.size_1(), obj);
  return true;
}
;
_.equals$ = function equals_7(o){
  var elem, elem$iterator, elemOther, iterOther, other;
  if (o === this) {
    return true;
  }
  if (!instanceOf(o, 37)) {
    return false;
  }
  other = dynamicCast(o, 37);
  if (this.size_1() != other.size_1()) {
    return false;
  }
  iterOther = other.iterator();
  for (elem$iterator = this.iterator(); elem$iterator.hasNext();) {
    elem = elem$iterator.next_0();
    elemOther = iterOther.next_0();
    if (!(maskUndefined(elem) === maskUndefined(elemOther) || elem != null && equals_Ljava_lang_Object__Z__devirtual$(elem, elemOther))) {
      return false;
    }
  }
  return true;
}
;
_.hashCode$ = function hashCode_9(){
  return hashCode_14(this);
}
;
_.iterator = function iterator_3(){
  return new AbstractList$IteratorImpl(this);
}
;
_.listIterator = function listIterator(){
  return this.listIterator_0(0);
}
;
_.listIterator_0 = function listIterator_0(from){
  return new AbstractList$ListIteratorImpl(this, from);
}
;
_.remove_1 = function remove_4(index_0){
  throw new UnsupportedOperationException_0('Remove not supported on this list');
}
;
var Ljava_util_AbstractList_2_classLit = createForClass('java.util', 'AbstractList', 435);
function $remove_4(this$static){
  checkState(this$static.last != -1);
  this$static.this$01_0.remove_1(this$static.last);
  this$static.i = this$static.last;
  this$static.last = -1;
}

function AbstractList$IteratorImpl(this$0){
  this.this$01_0 = this$0;
}

defineClass(53, 1, {}, AbstractList$IteratorImpl);
_.hasNext = function hasNext_1(){
  return this.i < this.this$01_0.size_1();
}
;
_.next_0 = function next_2(){
  return checkCriticalElement(this.i < this.this$01_0.size_1()) , this.this$01_0.get_1(this.last = this.i++);
}
;
_.remove_0 = function remove_5(){
  $remove_4(this);
}
;
_.i = 0;
_.last = -1;
var Ljava_util_AbstractList$IteratorImpl_2_classLit = createForClass('java.util', 'AbstractList/IteratorImpl', 53);
function AbstractList$ListIteratorImpl(this$0, start_0){
  this.this$01 = this$0;
  AbstractList$IteratorImpl.call(this, this$0);
  checkPositionIndex(start_0, this$0.size_1());
  this.i = start_0;
}

defineClass(246, 53, {}, AbstractList$ListIteratorImpl);
_.hasPrevious = function hasPrevious(){
  return this.i > 0;
}
;
_.previous = function previous_0(){
  checkCriticalElement(this.i > 0);
  return this.this$01.get_1(this.last = --this.i);
}
;
var Ljava_util_AbstractList$ListIteratorImpl_2_classLit = createForClass('java.util', 'AbstractList/ListIteratorImpl', 246);
function $iterator(this$static){
  var outerIter;
  outerIter = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this$static.this$01)).this$01);
  return new AbstractMap$1$1(outerIter);
}

function AbstractMap$1(this$0){
  this.this$01 = this$0;
}

defineClass(161, 432, $intern_31, AbstractMap$1);
_.contains_0 = function contains_1(key){
  return $containsKey(this.this$01, key);
}
;
_.iterator = function iterator_4(){
  return $iterator(this);
}
;
_.size_1 = function size_4(){
  return this.this$01.size_0;
}
;
var Ljava_util_AbstractMap$1_2_classLit = createForClass('java.util', 'AbstractMap/1', 161);
function AbstractMap$1$1(val$outerIter){
  this.val$outerIter2 = val$outerIter;
}

defineClass(226, 1, {}, AbstractMap$1$1);
_.hasNext = function hasNext_2(){
  return $hasNext(this.val$outerIter2);
}
;
_.next_0 = function next_3(){
  var entry;
  entry = $next_0(this.val$outerIter2);
  return entry.getKey();
}
;
_.remove_0 = function remove_6(){
  $remove_3(this.val$outerIter2);
}
;
var Ljava_util_AbstractMap$1$1_2_classLit = createForClass('java.util', 'AbstractMap/1/1', 226);
function $iterator_0(this$static){
  var outerIter;
  outerIter = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this$static.this$01)).this$01);
  return new AbstractMap$2$1(outerIter);
}

function AbstractMap$2(this$0){
  this.this$01 = this$0;
}

defineClass(227, 431, {}, AbstractMap$2);
_.contains_0 = function contains_2(value_0){
  return $containsValue(this.this$01, value_0);
}
;
_.iterator = function iterator_5(){
  return $iterator_0(this);
}
;
_.size_1 = function size_5(){
  return this.this$01.size_0;
}
;
var Ljava_util_AbstractMap$2_2_classLit = createForClass('java.util', 'AbstractMap/2', 227);
function $next_1(this$static){
  var entry;
  entry = $next_0(this$static.val$outerIter2);
  return entry.getValue();
}

function AbstractMap$2$1(val$outerIter){
  this.val$outerIter2 = val$outerIter;
}

defineClass(228, 1, {}, AbstractMap$2$1);
_.hasNext = function hasNext_3(){
  return $hasNext(this.val$outerIter2);
}
;
_.next_0 = function next_4(){
  return $next_1(this);
}
;
_.remove_0 = function remove_7(){
  $remove_3(this.val$outerIter2);
}
;
var Ljava_util_AbstractMap$2$1_2_classLit = createForClass('java.util', 'AbstractMap/2/1', 228);
defineClass(224, 1, $intern_32);
_.equals$ = function equals_8(other){
  var entry;
  if (!instanceOf(other, 22)) {
    return false;
  }
  entry = dynamicCast(other, 22);
  return equals_15(this.key, entry.getKey()) && equals_15(this.value_0, entry.getValue());
}
;
_.getKey = function getKey(){
  return this.key;
}
;
_.getValue = function getValue(){
  return this.value_0;
}
;
_.hashCode$ = function hashCode_10(){
  return hashCode_20(this.key) ^ hashCode_20(this.value_0);
}
;
_.setValue = function setValue(value_0){
  var oldValue;
  oldValue = this.value_0;
  this.value_0 = value_0;
  return oldValue;
}
;
_.toString$ = function toString_17(){
  return this.key + '=' + this.value_0;
}
;
var Ljava_util_AbstractMap$AbstractEntry_2_classLit = createForClass('java.util', 'AbstractMap/AbstractEntry', 224);
function AbstractMap$SimpleEntry(key, value_0){
  this.key = key;
  this.value_0 = value_0;
}

defineClass(225, 224, $intern_32, AbstractMap$SimpleEntry);
var Ljava_util_AbstractMap$SimpleEntry_2_classLit = createForClass('java.util', 'AbstractMap/SimpleEntry', 225);
defineClass(436, 1, $intern_32);
_.equals$ = function equals_9(other){
  var entry;
  if (!instanceOf(other, 22)) {
    return false;
  }
  entry = dynamicCast(other, 22);
  return equals_15(this.getKey(), entry.getKey()) && equals_15(this.getValue(), entry.getValue());
}
;
_.hashCode$ = function hashCode_11(){
  return hashCode_20(this.getKey()) ^ hashCode_20(this.getValue());
}
;
_.toString$ = function toString_18(){
  return this.getKey() + '=' + this.getValue();
}
;
var Ljava_util_AbstractMapEntry_2_classLit = createForClass('java.util', 'AbstractMapEntry', 436);
defineClass(471, 435, {37:1});
_.add_0 = function add_3(index_0, element){
  var iter;
  iter = $listIterator(this, index_0);
  $addNode(iter.this$01, element, iter.currentNode.prev, iter.currentNode);
  ++iter.currentIndex;
  iter.lastNode = null;
}
;
_.get_1 = function get_3(index_0){
  var iter;
  iter = $listIterator(this, index_0);
  try {
    return checkCriticalElement(iter.currentNode != iter.this$01.tail) , iter.lastNode = iter.currentNode , iter.currentNode = iter.currentNode.next , ++iter.currentIndex , iter.lastNode.value_0;
  }
   catch ($e0) {
    $e0 = wrap($e0);
    if (instanceOf($e0, 68)) {
      throw new IndexOutOfBoundsException_0("Can't get element " + index_0);
    }
     else 
      throw unwrap($e0);
  }
}
;
_.iterator = function iterator_6(){
  return $listIterator(this, 0);
}
;
_.remove_1 = function remove_8(index_0){
  var iter, old;
  iter = $listIterator(this, index_0);
  try {
    old = (checkCriticalElement(iter.currentNode != iter.this$01.tail) , iter.lastNode = iter.currentNode , iter.currentNode = iter.currentNode.next , ++iter.currentIndex , iter.lastNode.value_0);
    $remove_9(iter);
    return old;
  }
   catch ($e0) {
    $e0 = wrap($e0);
    if (instanceOf($e0, 68)) {
      throw new IndexOutOfBoundsException_0("Can't remove element " + index_0);
    }
     else 
      throw unwrap($e0);
  }
}
;
var Ljava_util_AbstractSequentialList_2_classLit = createForClass('java.util', 'AbstractSequentialList', 471);
function $$init(this$static){
  this$static.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
}

function $add_3(this$static, o){
  setCheck(this$static.array, this$static.array.length, o);
  return true;
}

function $addAll(this$static, c){
  var cArray, len;
  cArray = cloneSubrange(c.array, c.array.length);
  len = cArray.length;
  if (len == 0) {
    return false;
  }
  $insertAt(this$static, this$static.array.length, cArray);
  return true;
}

function $get_0(this$static, index_0){
  checkElementIndex(index_0, this$static.array.length);
  return this$static.array[index_0];
}

function $indexOf_2(this$static, o, index_0){
  for (; index_0 < this$static.array.length; ++index_0) {
    if (equals_15(o, this$static.array[index_0])) {
      return index_0;
    }
  }
  return -1;
}

function $insertAt(this$static, index_0, values){
  nativeArraySplice(values, 0, this$static.array, index_0, values.length, false);
}

function $remove_5(this$static, index_0){
  var previous;
  previous = (checkElementIndex(index_0, this$static.array.length) , this$static.array[index_0]);
  splice(this$static.array, index_0, 1);
  return previous;
}

function $set(this$static, index_0, o){
  var previous;
  previous = (checkElementIndex(index_0, this$static.array.length) , this$static.array[index_0]);
  setCheck(this$static.array, index_0, o);
  return previous;
}

function $toArray(this$static, out){
  var i_0, size_0;
  size_0 = this$static.array.length;
  out.length < size_0 && (out = createFrom(out, size_0));
  for (i_0 = 0; i_0 < size_0; ++i_0) {
    setCheck(out, i_0, this$static.array[i_0]);
  }
  out.length > size_0 && setCheck(out, size_0, null);
  return out;
}

function ArrayList(){
  $$init(this);
}

function ArrayList_0(initialCapacity){
  $$init(this);
  checkCriticalArgument(initialCapacity >= 0, 'Initial capacity must not be negative');
}

function ArrayList_1(c){
  $$init(this);
  $insertAt(this, 0, clone(c.array));
}

function splice(array, index_0, deleteCount){
  array.splice(index_0, deleteCount);
}

function splice_0(array, index_0, deleteCount, value_0){
  array.splice(index_0, deleteCount, value_0);
}

defineClass(7, 435, $intern_33, ArrayList, ArrayList_0, ArrayList_1);
_.add_0 = function add_4(index_0, o){
  checkPositionIndex(index_0, this.array.length);
  splice_0(this.array, index_0, 0, o);
}
;
_.add_1 = function add_5(o){
  return $add_3(this, o);
}
;
_.contains_0 = function contains_3(o){
  return $indexOf_2(this, o, 0) != -1;
}
;
_.get_1 = function get_4(index_0){
  return $get_0(this, index_0);
}
;
_.remove_1 = function remove_9(index_0){
  return $remove_5(this, index_0);
}
;
_.size_1 = function size_6(){
  return this.array.length;
}
;
var Ljava_util_ArrayList_2_classLit = createForClass('java.util', 'ArrayList', 7);
function hashCode_12(a){
  var e, e$index, e$max, hashCode;
  hashCode = 1;
  for (e$index = 0 , e$max = a.length; e$index < e$max; ++e$index) {
    e = a[e$index];
    hashCode = 31 * hashCode + (e != null?hashCode__I__devirtual$(e):0);
    hashCode = ~~hashCode;
  }
  return hashCode;
}

function insertionSort(array, low, high, comp){
  var i_0, j, t;
  for (i_0 = low + 1; i_0 < high; ++i_0) {
    for (j = i_0; j > low && comp.compare(array[j - 1], array[j]) > 0; --j) {
      t = array[j];
      setCheck(array, j, array[j - 1]);
      setCheck(array, j - 1, t);
    }
  }
}

function merge(src_0, srcLow, srcMid, srcHigh, dest, destLow, destHigh, comp){
  var topIdx;
  topIdx = srcMid;
  while (destLow < destHigh) {
    topIdx >= srcHigh || srcLow < srcMid && comp.compare(src_0[srcLow], src_0[topIdx]) <= 0?setCheck(dest, destLow++, src_0[srcLow++]):setCheck(dest, destLow++, src_0[topIdx++]);
  }
}

function mergeSort(x_0, fromIndex, toIndex, comp){
  var temp, newLength, length_0, copy;
  !comp && (comp = ($clinit_Comparators() , $clinit_Comparators() , NATURAL));
  temp = (newLength = (length_0 = toIndex - fromIndex , checkCriticalArgument_0(length_0 >= 0, initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(fromIndex), valueOf(toIndex)])) , length_0) , copy = createFrom(x_0, newLength) , arraycopy(x_0, fromIndex, copy, 0, min_1(x_0.length - fromIndex, newLength)) , copy);
  mergeSort_0(temp, x_0, fromIndex, toIndex, -fromIndex, comp);
}

function mergeSort_0(temp, array, low, high, ofs, comp){
  var length_0, tempHigh, tempLow, tempMid;
  length_0 = high - low;
  if (length_0 < 7) {
    insertionSort(array, low, high, comp);
    return;
  }
  tempLow = low + ofs;
  tempHigh = high + ofs;
  tempMid = tempLow + (tempHigh - tempLow >> 1);
  mergeSort_0(array, temp, tempLow, tempMid, -ofs, comp);
  mergeSort_0(array, temp, tempMid, tempHigh, -ofs, comp);
  if (comp.compare(temp[tempMid - 1], temp[tempMid]) <= 0) {
    while (low < high) {
      setCheck(array, low++, temp[tempLow++]);
    }
    return;
  }
  merge(temp, tempLow, tempMid, tempHigh, array, low, high, comp);
}

function Arrays$ArrayList(array){
  this.array = array;
}

defineClass(372, 435, $intern_33, Arrays$ArrayList);
_.contains_0 = function contains_4(o){
  return $indexOf_1(this, o) != -1;
}
;
_.get_1 = function get_5(index_0){
  return checkElementIndex(index_0, this.array.length) , this.array[index_0];
}
;
_.size_1 = function size_7(){
  return this.array.length;
}
;
var Ljava_util_Arrays$ArrayList_2_classLit = createForClass('java.util', 'Arrays/ArrayList', 372);
function $clinit_Collections(){
  $clinit_Collections = emptyMethod;
  EMPTY_LIST = new Collections$EmptyList;
}

function hashCode_13(collection){
  $clinit_Collections();
  var e, e$iterator, hashCode;
  hashCode = 0;
  for (e$iterator = collection.iterator(); e$iterator.hasNext();) {
    e = e$iterator.next_0();
    hashCode = hashCode + (e != null?hashCode__I__devirtual$(e):0);
    hashCode = ~~hashCode;
  }
  return hashCode;
}

function hashCode_14(list){
  $clinit_Collections();
  var e, e$iterator, hashCode;
  hashCode = 1;
  for (e$iterator = list.iterator(); e$iterator.hasNext();) {
    e = e$iterator.next_0();
    hashCode = 31 * hashCode + (e != null?hashCode__I__devirtual$(e):0);
    hashCode = ~~hashCode;
  }
  return hashCode;
}

function replaceContents(target, x_0){
  var i_0, size_0;
  size_0 = target.array.length;
  for (i_0 = 0; i_0 < size_0; i_0++) {
    $set(target, i_0, x_0[i_0]);
  }
}

function sort_0(target, c){
  $clinit_Collections();
  var x_0;
  x_0 = cloneSubrange(target.array, target.array.length);
  mergeSort(x_0, 0, x_0.length, c);
  replaceContents(target, x_0);
}

function unmodifiableList(list){
  $clinit_Collections();
  return instanceOf(list, 108)?new Collections$UnmodifiableRandomAccessList(list):new Collections$UnmodifiableList(list);
}

var EMPTY_LIST;
function Collections$EmptyList(){
}

defineClass(238, 435, $intern_33, Collections$EmptyList);
_.contains_0 = function contains_5(object){
  return false;
}
;
_.get_1 = function get_6(location_0){
  checkElementIndex(location_0, 0);
  return null;
}
;
_.iterator = function iterator_7(){
  return $clinit_Collections() , $clinit_Collections$EmptyListIterator() , INSTANCE_0;
}
;
_.listIterator = function listIterator_1(){
  return $clinit_Collections() , $clinit_Collections$EmptyListIterator() , INSTANCE_0;
}
;
_.size_1 = function size_8(){
  return 0;
}
;
var Ljava_util_Collections$EmptyList_2_classLit = createForClass('java.util', 'Collections/EmptyList', 238);
function $clinit_Collections$EmptyListIterator(){
  $clinit_Collections$EmptyListIterator = emptyMethod;
  INSTANCE_0 = new Collections$EmptyListIterator;
}

function Collections$EmptyListIterator(){
}

defineClass(239, 1, {}, Collections$EmptyListIterator);
_.hasNext = function hasNext_4(){
  return false;
}
;
_.hasPrevious = function hasPrevious_0(){
  return false;
}
;
_.next_0 = function next_5(){
  throw new NoSuchElementException;
}
;
_.previous = function previous_1(){
  throw new NoSuchElementException;
}
;
_.remove_0 = function remove_10(){
  throw new IllegalStateException;
}
;
var INSTANCE_0;
var Ljava_util_Collections$EmptyListIterator_2_classLit = createForClass('java.util', 'Collections/EmptyListIterator', 239);
function Collections$UnmodifiableCollection(coll){
  this.coll = coll;
}

defineClass(164, 1, {});
_.add_1 = function add_6(o){
  throw new UnsupportedOperationException;
}
;
_.iterator = function iterator_8(){
  return new Collections$UnmodifiableCollectionIterator(this.coll.iterator());
}
;
_.size_1 = function size_9(){
  return this.coll.size_1();
}
;
_.toString$ = function toString_19(){
  return this.coll.toString$();
}
;
var Ljava_util_Collections$UnmodifiableCollection_2_classLit = createForClass('java.util', 'Collections/UnmodifiableCollection', 164);
function Collections$UnmodifiableCollectionIterator(it){
  this.it = it;
}

defineClass(166, 1, {}, Collections$UnmodifiableCollectionIterator);
_.hasNext = function hasNext_5(){
  return this.it.hasNext();
}
;
_.next_0 = function next_6(){
  return this.it.next_0();
}
;
_.remove_0 = function remove_11(){
  throw new UnsupportedOperationException;
}
;
var Ljava_util_Collections$UnmodifiableCollectionIterator_2_classLit = createForClass('java.util', 'Collections/UnmodifiableCollectionIterator', 166);
function Collections$UnmodifiableList(list){
  Collections$UnmodifiableCollection.call(this, list);
  this.list = list;
}

defineClass(165, 164, {37:1}, Collections$UnmodifiableList);
_.equals$ = function equals_10(o){
  return this.list.equals$(o);
}
;
_.get_1 = function get_7(index_0){
  return this.list.get_1(index_0);
}
;
_.hashCode$ = function hashCode_15(){
  return this.list.hashCode$();
}
;
_.listIterator = function listIterator_2(){
  return new Collections$UnmodifiableListIterator(this.list.listIterator_0(0));
}
;
_.listIterator_0 = function listIterator_3(from){
  return new Collections$UnmodifiableListIterator(this.list.listIterator_0(from));
}
;
var Ljava_util_Collections$UnmodifiableList_2_classLit = createForClass('java.util', 'Collections/UnmodifiableList', 165);
function Collections$UnmodifiableListIterator(lit){
  Collections$UnmodifiableCollectionIterator.call(this, lit);
  this.lit = lit;
}

defineClass(167, 166, {}, Collections$UnmodifiableListIterator);
_.hasPrevious = function hasPrevious_1(){
  return this.lit.hasPrevious();
}
;
_.previous = function previous_2(){
  return this.lit.previous();
}
;
var Ljava_util_Collections$UnmodifiableListIterator_2_classLit = createForClass('java.util', 'Collections/UnmodifiableListIterator', 167);
function Collections$UnmodifiableMap(map_0){
  this.map_0 = map_0;
}

defineClass(240, 1, {93:1}, Collections$UnmodifiableMap);
_.entrySet_0 = function entrySet_0(){
  !this.entrySet && (this.entrySet = new Collections$UnmodifiableMap$UnmodifiableEntrySet(this.map_0.entrySet_0()));
  return this.entrySet;
}
;
_.equals$ = function equals_11(o){
  return this.map_0.equals$(o);
}
;
_.get_0 = function get_8(key){
  return this.map_0.get_0(key);
}
;
_.hashCode$ = function hashCode_16(){
  return this.map_0.hashCode$();
}
;
_.put = function put_1(key, value_0){
  throw new UnsupportedOperationException;
}
;
_.size_1 = function size_10(){
  return this.map_0.size_1();
}
;
_.toString$ = function toString_20(){
  return this.map_0.toString$();
}
;
var Ljava_util_Collections$UnmodifiableMap_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap', 240);
defineClass(241, 164, $intern_31);
_.equals$ = function equals_12(o){
  return this.coll.equals$(o);
}
;
_.hashCode$ = function hashCode_17(){
  return this.coll.hashCode$();
}
;
var Ljava_util_Collections$UnmodifiableSet_2_classLit = createForClass('java.util', 'Collections/UnmodifiableSet', 241);
function Collections$UnmodifiableMap$UnmodifiableEntrySet(s){
  Collections$UnmodifiableCollection.call(this, s);
}

defineClass(242, 241, $intern_31, Collections$UnmodifiableMap$UnmodifiableEntrySet);
_.iterator = function iterator_9(){
  var it;
  it = this.coll.iterator();
  return new Collections$UnmodifiableMap$UnmodifiableEntrySet$1(it);
}
;
var Ljava_util_Collections$UnmodifiableMap$UnmodifiableEntrySet_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap/UnmodifiableEntrySet', 242);
function Collections$UnmodifiableMap$UnmodifiableEntrySet$1(val$it){
  this.val$it2 = val$it;
}

defineClass(245, 1, {}, Collections$UnmodifiableMap$UnmodifiableEntrySet$1);
_.hasNext = function hasNext_6(){
  return this.val$it2.hasNext();
}
;
_.next_0 = function next_7(){
  return new Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry(dynamicCast(this.val$it2.next_0(), 22));
}
;
_.remove_0 = function remove_12(){
  throw new UnsupportedOperationException;
}
;
var Ljava_util_Collections$UnmodifiableMap$UnmodifiableEntrySet$1_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap/UnmodifiableEntrySet/1', 245);
function Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry(entry){
  this.entry = entry;
}

defineClass(243, 1, $intern_32, Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry);
_.equals$ = function equals_13(o){
  return this.entry.equals$(o);
}
;
_.getKey = function getKey_0(){
  return this.entry.getKey();
}
;
_.getValue = function getValue_0(){
  return this.entry.getValue();
}
;
_.hashCode$ = function hashCode_18(){
  return this.entry.hashCode$();
}
;
_.setValue = function setValue_0(value_0){
  throw new UnsupportedOperationException;
}
;
_.toString$ = function toString_21(){
  return this.entry.toString$();
}
;
var Ljava_util_Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry_2_classLit = createForClass('java.util', 'Collections/UnmodifiableMap/UnmodifiableEntrySet/UnmodifiableEntry', 243);
function Collections$UnmodifiableRandomAccessList(list){
  Collections$UnmodifiableList.call(this, list);
}

defineClass(244, 165, {37:1, 108:1}, Collections$UnmodifiableRandomAccessList);
var Ljava_util_Collections$UnmodifiableRandomAccessList_2_classLit = createForClass('java.util', 'Collections/UnmodifiableRandomAccessList', 244);
function $clinit_Comparators(){
  $clinit_Comparators = emptyMethod;
  NATURAL = new Comparators$1;
}

var NATURAL;
function Comparators$1(){
}

defineClass(389, 1, {}, Comparators$1);
_.compare = function compare_2(o1, o2){
  checkNotNull(o1);
  checkNotNull(o2);
  return compareTo_Ljava_lang_Object__I__devirtual$(dynamicCast(o1, 26), o2);
}
;
var Ljava_util_Comparators$1_2_classLit = createForClass('java.util', 'Comparators/1', 389);
function checkStructuralChange(host, iterator){
  if (iterator._gwt_modCount != host._gwt_modCount) {
    throw new ConcurrentModificationException;
  }
}

function recordLastKnownStructure(host, iterator){
  setModCount(iterator, host._gwt_modCount);
}

function setModCount(o, modCount){
  o._gwt_modCount = modCount;
}

function structureChanged(map_0){
  var modCount;
  modCount = map_0._gwt_modCount | 0;
  setModCount(map_0, modCount + 1);
}

function ConcurrentModificationException(){
  RuntimeException.call(this);
}

defineClass(368, 17, $intern_1, ConcurrentModificationException);
var Ljava_util_ConcurrentModificationException_2_classLit = createForClass('java.util', 'ConcurrentModificationException', 368);
function $compareTo_3(this$static, other){
  return compare_1(fromDouble(this$static.jsdate.getTime()), fromDouble(other.jsdate.getTime()));
}

function $toString_2(this$static){
  var hourOffset, minuteOffset, offset;
  offset = -this$static.jsdate.getTimezoneOffset();
  hourOffset = (offset >= 0?'+':'') + ~~(offset / 60);
  minuteOffset = (offset < 0?-offset:offset) % 60 < 10?'0' + (offset < 0?-offset:offset) % 60:'' + (offset < 0?-offset:offset) % 60;
  return ($clinit_Date$StringData() , DAYS)[this$static.jsdate.getDay()] + ' ' + MONTHS[this$static.jsdate.getMonth()] + ' ' + padTwo(this$static.jsdate.getDate()) + ' ' + padTwo(this$static.jsdate.getHours()) + ':' + padTwo(this$static.jsdate.getMinutes()) + ':' + padTwo(this$static.jsdate.getSeconds()) + ' GMT' + hourOffset + minuteOffset + ' ' + this$static.jsdate.getFullYear();
}

function Date_0(date){
  this.jsdate = create(toDouble(date));
}

function padTwo(number){
  return number < 10?'0' + number:'' + number;
}

defineClass(104, 1, {3:1, 26:1, 104:1}, Date_0);
_.compareTo = function compareTo_4(other){
  return $compareTo_3(this, dynamicCast(other, 104));
}
;
_.equals$ = function equals_14(obj){
  return instanceOf(obj, 104) && eq(fromDouble(this.jsdate.getTime()), fromDouble(dynamicCast(obj, 104).jsdate.getTime()));
}
;
_.hashCode$ = function hashCode_19(){
  var time;
  time = fromDouble(this.jsdate.getTime());
  return toInt(xor(time, shru(time, 32)));
}
;
_.toString$ = function toString_22(){
  return $toString_2(this);
}
;
var Ljava_util_Date_2_classLit = createForClass('java.util', 'Date', 104);
function $clinit_Date$StringData(){
  $clinit_Date$StringData = emptyMethod;
  DAYS = initValues(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_4, 2, 4, ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']);
  MONTHS = initValues(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), $intern_4, 2, 4, ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']);
}

var DAYS, MONTHS;
function $equals_0(value1, value2){
  return maskUndefined(value1) === maskUndefined(value2) || value1 != null && equals_Ljava_lang_Object__Z__devirtual$(value1, value2);
}

function $getHashCode(key){
  var hashCode;
  hashCode = hashCode__I__devirtual$(key);
  return ~~hashCode;
}

function HashMap(){
  $reset(this);
}

defineClass(24, 223, {3:1, 93:1}, HashMap);
var Ljava_util_HashMap_2_classLit = createForClass('java.util', 'HashMap', 24);
function $add_4(this$static, o){
  var old;
  old = $put(this$static.map_0, o, this$static);
  return old == null;
}

function $contains_0(this$static, o){
  return $containsKey(this$static.map_0, o);
}

function $remove_6(this$static, o){
  return $remove_2(this$static.map_0, o) != null;
}

function HashSet(){
  this.map_0 = new HashMap;
}

defineClass(139, 432, {3:1, 94:1}, HashSet);
_.contains_0 = function contains_6(o){
  return $contains_0(this, o);
}
;
_.iterator = function iterator_10(){
  return $iterator(new AbstractMap$1(this.map_0));
}
;
_.size_1 = function size_11(){
  return this.map_0.size_0;
}
;
_.toString$ = function toString_23(){
  return $toString_0(new AbstractMap$1(this.map_0));
}
;
var Ljava_util_HashSet_2_classLit = createForClass('java.util', 'HashSet', 139);
function $ensureChain(this$static, hashCode){
  var map_0 = this$static.backingMap;
  return map_0[hashCode] || (map_0[hashCode] = []);
}

function $getChain(this$static, hashCode){
  return this$static.backingMap[hashCode];
}

function $getChainOrEmpty(this$static, hashCode){
  return this$static.backingMap[hashCode] || [];
}

function $getEntry(this$static, key){
  var entry, entry$array, entry$index, entry$max;
  for (entry$array = $getChainOrEmpty(this$static, key == null?'0':'' + $getHashCode(key)) , entry$index = 0 , entry$max = entry$array.length; entry$index < entry$max; ++entry$index) {
    entry = entry$array[entry$index];
    if ($equals_0(key, entry.getKey())) {
      return entry;
    }
  }
  return null;
}

function $keys(this$static){
  return Object.getOwnPropertyNames(this$static.backingMap);
}

function $put_0(this$static, key, value_0){
  var chain, entry, entry$index, entry$max;
  chain = $ensureChain(this$static, key == null?'0':'' + $getHashCode(key));
  for (entry$index = 0 , entry$max = chain.length; entry$index < entry$max; ++entry$index) {
    entry = chain[entry$index];
    if ($equals_0(key, entry.getKey())) {
      return entry.setValue(value_0);
    }
  }
  setCheck(chain, chain.length, new AbstractMap$SimpleEntry(key, value_0));
  $elementAdded(this$static.host);
  return null;
}

function $remove_7(this$static, key){
  var chain, entry, hashCode, i_0;
  hashCode = key == null?'0':'' + $getHashCode(key);
  chain = $getChainOrEmpty(this$static, hashCode);
  for (i_0 = 0; i_0 < chain.length; i_0++) {
    entry = chain[i_0];
    if ($equals_0(key, entry.getKey())) {
      chain.length == 1?(delete this$static.backingMap[hashCode] , undefined):(chain.splice(i_0, 1) , undefined);
      $elementRemoved(this$static.host);
      return entry.getValue();
    }
  }
  return null;
}

function InternalJsHashCodeMap(){
  this.backingMap = this.createMap();
}

defineClass(173, 1, {}, InternalJsHashCodeMap);
_.containsValue = function containsValue(value_0){
  var entry, entry$array, entry$index, entry$max, hashCode, hashCode$array, hashCode$index, hashCode$max;
  for (hashCode$array = $keys(this) , hashCode$index = 0 , hashCode$max = hashCode$array.length; hashCode$index < hashCode$max; ++hashCode$index) {
    hashCode = hashCode$array[hashCode$index];
    for (entry$array = this.backingMap[hashCode] , entry$index = 0 , entry$max = entry$array.length; entry$index < entry$max; ++entry$index) {
      entry = entry$array[entry$index];
      if ($equals_0(value_0, entry.getValue())) {
        return true;
      }
    }
  }
  return false;
}
;
_.createMap = function createMap(){
  return Object.create(null);
}
;
_.entries = function entries(){
  return new InternalJsHashCodeMap$1(this);
}
;
var Ljava_util_InternalJsHashCodeMap_2_classLit = createForClass('java.util', 'InternalJsHashCodeMap', 173);
function $hasNext_0(this$static){
  if (this$static.itemIndex < this$static.chain.length) {
    return true;
  }
  if (this$static.chainIndex < this$static.keys_0.length - 1) {
    this$static.chain = $getChain(this$static.this$01, this$static.keys_0[++this$static.chainIndex]);
    this$static.itemIndex = 0;
    return true;
  }
  return false;
}

function InternalJsHashCodeMap$1(this$0){
  this.this$01 = this$0;
  this.keys_0 = $keys(this.this$01);
  this.chain = initDim(Ljava_util_Map$Entry_2_classLit, $intern_4, 22, 0, 0, 1);
}

defineClass(261, 1, {}, InternalJsHashCodeMap$1);
_.hasNext = function hasNext_7(){
  return $hasNext_0(this);
}
;
_.next_0 = function next_8(){
  return checkCriticalElement($hasNext_0(this)) , this.lastChain = this.chain , this.lastEntry = this.chain[this.itemIndex++] , this.lastEntry;
}
;
_.remove_0 = function remove_13(){
  checkState(!!this.lastEntry);
  $remove_7(this.this$01, this.lastEntry.getKey());
  maskUndefined(this.chain) === maskUndefined(this.lastChain) && this.chain.length != 1 && --this.itemIndex;
  this.lastEntry = null;
}
;
_.chainIndex = -1;
_.itemIndex = 0;
_.lastChain = null;
_.lastEntry = null;
var Ljava_util_InternalJsHashCodeMap$1_2_classLit = createForClass('java.util', 'InternalJsHashCodeMap/1', 261);
function InternalJsHashCodeMap$InternalJsHashCodeMapLegacy(){
  InternalJsHashCodeMap.call(this);
}

defineClass(259, 173, {}, InternalJsHashCodeMap$InternalJsHashCodeMapLegacy);
_.containsValue = function containsValue_0(value_0){
  var map_0 = this.backingMap;
  for (var hashCode in map_0) {
    if (hashCode == parseInt(hashCode, 10)) {
      var array = map_0[hashCode];
      for (var i_0 = 0, c = array.length; i_0 < c; ++i_0) {
        var entry = array[i_0];
        var entryValue = entry.getValue();
        if (this.equalsBridge(value_0, entryValue)) {
          return true;
        }
      }
    }
  }
  return false;
}
;
_.createMap = function createMap_0(){
  return {};
}
;
_.entries = function entries_0(){
  var list = this.newEntryList();
  var map_0 = this.backingMap;
  for (var hashCode in map_0) {
    if (hashCode == parseInt(hashCode, 10)) {
      var array = map_0[hashCode];
      for (var i_0 = 0, c = array.length; i_0 < c; ++i_0) {
        list.add_1(array[i_0]);
      }
    }
  }
  return list.iterator();
}
;
_.equalsBridge = function equalsBridge(value1, value2){
  return maskUndefined(value1) === maskUndefined(value2) || value1 != null && equals_Ljava_lang_Object__Z__devirtual$(value1, value2);
}
;
_.newEntryList = function newEntryList(){
  return new InternalJsHashCodeMap$InternalJsHashCodeMapLegacy$1(this);
}
;
var Ljava_util_InternalJsHashCodeMap$InternalJsHashCodeMapLegacy_2_classLit = createForClass('java.util', 'InternalJsHashCodeMap/InternalJsHashCodeMapLegacy', 259);
function InternalJsHashCodeMap$InternalJsHashCodeMapLegacy$1(this$1){
  this.this$11 = this$1;
  ArrayList.call(this);
}

defineClass(260, 7, $intern_33, InternalJsHashCodeMap$InternalJsHashCodeMapLegacy$1);
_.remove_1 = function remove_14(index_0){
  var removed;
  return removed = dynamicCast($remove_5(this, index_0), 22) , $remove_7(this.this$11, removed.getKey()) , removed;
}
;
var Ljava_util_InternalJsHashCodeMap$InternalJsHashCodeMapLegacy$1_2_classLit = createForClass('java.util', 'InternalJsHashCodeMap/InternalJsHashCodeMapLegacy/1', 260);
function InternalJsMapFactory(){
}

defineClass(256, 1, {}, InternalJsMapFactory);
_.createJsHashCodeMap = function createJsHashCodeMap(){
  return new InternalJsHashCodeMap;
}
;
_.createJsStringMap = function createJsStringMap(){
  return new InternalJsStringMap;
}
;
var Ljava_util_InternalJsMapFactory_2_classLit = createForClass('java.util', 'InternalJsMapFactory', 256);
function $clinit_InternalJsMapFactory$BackwardCompatibleJsMapFactory(){
  $clinit_InternalJsMapFactory$BackwardCompatibleJsMapFactory = emptyMethod;
  delegate = createFactory();
}

function canHandleProto(){
  var protoField = '__proto__';
  var map_0 = Object.create(null);
  if (map_0[protoField] !== undefined) {
    return false;
  }
  var keys_0 = Object.getOwnPropertyNames(map_0);
  if (keys_0.length != 0) {
    return false;
  }
  map_0[protoField] = 42;
  if (map_0[protoField] !== 42) {
    return false;
  }
  return true;
}

function createFactory(){
  var map_0;
  if (Object.create && Object.getOwnPropertyNames && canHandleProto()) {
    return (map_0 = Object.create(null) , map_0['__proto__'] = 42 , Object.getOwnPropertyNames(map_0).length == 0)?new InternalJsMapFactory$KeysWorkaroundJsMapFactory:new InternalJsMapFactory;
  }
  return new InternalJsMapFactory$LegacyInternalJsMapFactory;
}

var delegate;
function InternalJsMapFactory$KeysWorkaroundJsMapFactory(){
}

defineClass(258, 256, {}, InternalJsMapFactory$KeysWorkaroundJsMapFactory);
_.createJsStringMap = function createJsStringMap_0(){
  return new InternalJsStringMap$InternalJsStringMapWithKeysWorkaround;
}
;
var Ljava_util_InternalJsMapFactory$KeysWorkaroundJsMapFactory_2_classLit = createForClass('java.util', 'InternalJsMapFactory/KeysWorkaroundJsMapFactory', 258);
function InternalJsMapFactory$LegacyInternalJsMapFactory(){
}

defineClass(257, 256, {}, InternalJsMapFactory$LegacyInternalJsMapFactory);
_.createJsHashCodeMap = function createJsHashCodeMap_0(){
  return new InternalJsHashCodeMap$InternalJsHashCodeMapLegacy;
}
;
_.createJsStringMap = function createJsStringMap_1(){
  return new InternalJsStringMap$InternalJsStringMapLegacy;
}
;
var Ljava_util_InternalJsMapFactory$LegacyInternalJsMapFactory_2_classLit = createForClass('java.util', 'InternalJsMapFactory/LegacyInternalJsMapFactory', 257);
function $containsValue_0(this$static, value_0){
  var map_0 = this$static.backingMap;
  for (var key in map_0) {
    if (this$static.equalsBridge_0(value_0, map_0[key])) {
      return true;
    }
  }
  return false;
}

function $keys_0(this$static){
  return Object.getOwnPropertyNames(this$static.backingMap);
}

function $put_1(this$static, key, value_0){
  var oldValue;
  oldValue = this$static.backingMap[key];
  oldValue === undefined && $elementAdded(this$static.host);
  $set_0(this$static, key, value_0 === undefined?null:value_0);
  return oldValue;
}

function $remove_8(this$static, key){
  var value_0;
  value_0 = this$static.backingMap[key];
  if (!(value_0 === undefined)) {
    delete this$static.backingMap[key];
    $elementRemoved(this$static.host);
  }
  return value_0;
}

function $set_0(this$static, key, value_0){
  this$static.backingMap[key] = value_0;
}

function InternalJsStringMap(){
  this.backingMap = this.createMap_0();
}

defineClass(138, 1, {}, InternalJsStringMap);
_.containsValue = function containsValue_1(value_0){
  return $containsValue_0(this, value_0);
}
;
_.createMap_0 = function createMap_1(){
  return Object.create(null);
}
;
_.entries = function entries_1(){
  var keys_0;
  keys_0 = this.keys_1();
  return new InternalJsStringMap$1(this, keys_0);
}
;
_.equalsBridge_0 = function equalsBridge_0(value1, value2){
  return maskUndefined(value1) === maskUndefined(value2) || value1 != null && equals_Ljava_lang_Object__Z__devirtual$(value1, value2);
}
;
_.get_2 = function get_9(key){
  return this.backingMap[key];
}
;
_.keys_1 = function keys_1(){
  return $keys_0(this);
}
;
_.newMapEntry = function newMapEntry(key){
  return new InternalJsStringMap$2(this, key);
}
;
_.put_0 = function put_2(key, value_0){
  return $put_1(this, key, value_0);
}
;
_.remove_2 = function remove_15(key){
  return $remove_8(this, key);
}
;
var Ljava_util_InternalJsStringMap_2_classLit = createForClass('java.util', 'InternalJsStringMap', 138);
function InternalJsStringMap$1(this$0, val$keys){
  this.this$01 = this$0;
  this.val$keys2 = val$keys;
}

defineClass(250, 1, {}, InternalJsStringMap$1);
_.hasNext = function hasNext_8(){
  return this.i < this.val$keys2.length;
}
;
_.next_0 = function next_9(){
  return checkCriticalElement(this.i < this.val$keys2.length) , new InternalJsStringMap$2(this.this$01, this.val$keys2[this.last = this.i++]);
}
;
_.remove_0 = function remove_16(){
  checkState(this.last != -1);
  this.this$01.remove_2(this.val$keys2[this.last]);
  this.last = -1;
}
;
_.i = 0;
_.last = -1;
var Ljava_util_InternalJsStringMap$1_2_classLit = createForClass('java.util', 'InternalJsStringMap/1', 250);
function InternalJsStringMap$2(this$0, val$key){
  this.this$01 = this$0;
  this.val$key2 = val$key;
}

defineClass(168, 436, $intern_32, InternalJsStringMap$2);
_.getKey = function getKey_1(){
  return this.val$key2;
}
;
_.getValue = function getValue_1(){
  return this.this$01.get_2(this.val$key2);
}
;
_.setValue = function setValue_1(object){
  return this.this$01.put_0(this.val$key2, object);
}
;
var Ljava_util_InternalJsStringMap$2_2_classLit = createForClass('java.util', 'InternalJsStringMap/2', 168);
function InternalJsStringMap$InternalJsStringMapLegacy(){
  InternalJsStringMap.call(this);
}

defineClass(247, 138, {}, InternalJsStringMap$InternalJsStringMapLegacy);
_.containsValue = function containsValue_2(value_0){
  var map_0 = this.backingMap;
  for (var key in map_0) {
    if (key.charCodeAt(0) == 58) {
      var entryValue = map_0[key];
      if (this.equalsBridge_0(value_0, entryValue)) {
        return true;
      }
    }
  }
  return false;
}
;
_.createMap_0 = function createMap_2(){
  return {};
}
;
_.entries = function entries_2(){
  var list = this.newEntryList_0();
  for (var key in this.backingMap) {
    if (key.charCodeAt(0) == 58) {
      var entry = this.newMapEntry(key.substring(1));
      list.add_1(entry);
    }
  }
  return list.iterator();
}
;
_.get_2 = function get_10(key){
  return this.backingMap[':' + key];
}
;
_.newEntryList_0 = function newEntryList_0(){
  return new InternalJsStringMap$InternalJsStringMapLegacy$1(this);
}
;
_.put_0 = function put_3(key, value_0){
  return $put_1(this, ':' + key, value_0);
}
;
_.remove_2 = function remove_17(key){
  return $remove_8(this, ':' + key);
}
;
var Ljava_util_InternalJsStringMap$InternalJsStringMapLegacy_2_classLit = createForClass('java.util', 'InternalJsStringMap/InternalJsStringMapLegacy', 247);
function InternalJsStringMap$InternalJsStringMapLegacy$1(this$1){
  this.this$11 = this$1;
  ArrayList.call(this);
}

defineClass(249, 7, $intern_33, InternalJsStringMap$InternalJsStringMapLegacy$1);
_.remove_1 = function remove_18(index_0){
  var removed;
  return removed = dynamicCast($remove_5(this, index_0), 22) , $remove_8(this.this$11, ':' + dynamicCastToString(removed.getKey())) , removed;
}
;
var Ljava_util_InternalJsStringMap$InternalJsStringMapLegacy$1_2_classLit = createForClass('java.util', 'InternalJsStringMap/InternalJsStringMapLegacy/1', 249);
function InternalJsStringMap$InternalJsStringMapWithKeysWorkaround(){
  InternalJsStringMap.call(this);
}

defineClass(248, 138, {}, InternalJsStringMap$InternalJsStringMapWithKeysWorkaround);
_.containsValue = function containsValue_3(value_0){
  var protoValue;
  protoValue = this.backingMap['__proto__'];
  if (!(protoValue === undefined) && (maskUndefined(value_0) === maskUndefined(protoValue) || value_0 != null && equals_Ljava_lang_Object__Z__devirtual$(value_0, protoValue))) {
    return true;
  }
  return $containsValue_0(this, value_0);
}
;
_.keys_1 = function keys_2(){
  var keys_0;
  keys_0 = $keys_0(this);
  !(this.backingMap['__proto__'] === undefined) && (keys_0[keys_0.length] = '__proto__');
  return keys_0;
}
;
var Ljava_util_InternalJsStringMap$InternalJsStringMapWithKeysWorkaround_2_classLit = createForClass('java.util', 'InternalJsStringMap/InternalJsStringMapWithKeysWorkaround', 248);
function $addFirst(this$static, o){
  $addNode(this$static, o, this$static.header, this$static.header.next);
}

function $addLast(this$static, o){
  $addNode(this$static, o, this$static.tail.prev, this$static.tail);
}

function $addNode(this$static, o, prev, next){
  var node;
  node = new LinkedList$Node;
  node.value_0 = o;
  node.prev = prev;
  node.next = next;
  next.prev = prev.next = node;
  ++this$static.size_0;
}

function $getFirst(this$static){
  checkCriticalElement(this$static.size_0 != 0);
  return this$static.header.next.value_0;
}

function $getLast(this$static){
  checkCriticalElement(this$static.size_0 != 0);
  return this$static.tail.prev.value_0;
}

function $listIterator(this$static, index_0){
  var i_0, node;
  checkPositionIndex(index_0, this$static.size_0);
  if (index_0 >= this$static.size_0 >> 1) {
    node = this$static.tail;
    for (i_0 = this$static.size_0; i_0 > index_0; --i_0) {
      node = node.prev;
    }
  }
   else {
    node = this$static.header.next;
    for (i_0 = 0; i_0 < index_0; ++i_0) {
      node = node.next;
    }
  }
  return new LinkedList$ListIteratorImpl(this$static, index_0, node);
}

function $removeFirst(this$static){
  checkCriticalElement(this$static.size_0 != 0);
  return $removeNode(this$static, this$static.header.next);
}

function $removeLast(this$static){
  checkCriticalElement(this$static.size_0 != 0);
  return $removeNode(this$static, this$static.tail.prev);
}

function $removeNode(this$static, node){
  var oldValue;
  oldValue = node.value_0;
  node.next.prev = node.prev;
  node.prev.next = node.next;
  node.next = node.prev = null;
  node.value_0 = null;
  --this$static.size_0;
  return oldValue;
}

function LinkedList(){
  this.header = new LinkedList$Node;
  this.tail = new LinkedList$Node;
  this.header.next = this.tail;
  this.tail.prev = this.header;
  this.header.prev = this.tail.next = null;
  this.size_0 = 0;
}

defineClass(186, 471, {3:1, 37:1}, LinkedList);
_.add_1 = function add_7(o){
  $addNode(this, o, this.tail.prev, this.tail);
  return true;
}
;
_.listIterator_0 = function listIterator_4(index_0){
  return $listIterator(this, index_0);
}
;
_.size_1 = function size_12(){
  return this.size_0;
}
;
_.size_0 = 0;
var Ljava_util_LinkedList_2_classLit = createForClass('java.util', 'LinkedList', 186);
function $remove_9(this$static){
  var nextNode;
  checkState(!!this$static.lastNode);
  nextNode = this$static.lastNode.next;
  $removeNode(this$static.this$01, this$static.lastNode);
  this$static.currentNode == this$static.lastNode?(this$static.currentNode = nextNode):--this$static.currentIndex;
  this$static.lastNode = null;
}

function LinkedList$ListIteratorImpl(this$0, index_0, startNode){
  this.this$01 = this$0;
  this.currentNode = startNode;
  this.currentIndex = index_0;
}

defineClass(337, 1, {}, LinkedList$ListIteratorImpl);
_.hasNext = function hasNext_9(){
  return this.currentNode != this.this$01.tail;
}
;
_.hasPrevious = function hasPrevious_2(){
  return this.currentNode.prev != this.this$01.header;
}
;
_.next_0 = function next_10(){
  return checkCriticalElement(this.currentNode != this.this$01.tail) , this.lastNode = this.currentNode , this.currentNode = this.currentNode.next , ++this.currentIndex , this.lastNode.value_0;
}
;
_.previous = function previous_3(){
  checkCriticalElement(this.currentNode.prev != this.this$01.header);
  this.lastNode = this.currentNode = this.currentNode.prev;
  --this.currentIndex;
  return this.lastNode.value_0;
}
;
_.remove_0 = function remove_19(){
  $remove_9(this);
}
;
_.currentIndex = 0;
_.lastNode = null;
var Ljava_util_LinkedList$ListIteratorImpl_2_classLit = createForClass('java.util', 'LinkedList/ListIteratorImpl', 337);
function LinkedList$Node(){
}

defineClass(144, 1, {}, LinkedList$Node);
var Ljava_util_LinkedList$Node_2_classLit = createForClass('java.util', 'LinkedList/Node', 144);
var Ljava_util_Map$Entry_2_classLit = createForInterface('java.util', 'Map/Entry');
function NoSuchElementException(){
  RuntimeException.call(this);
}

defineClass(68, 17, {3:1, 13:1, 68:1}, NoSuchElementException);
var Ljava_util_NoSuchElementException_2_classLit = createForClass('java.util', 'NoSuchElementException', 68);
function equals_15(a, b){
  return maskUndefined(a) === maskUndefined(b) || a != null && equals_Ljava_lang_Object__Z__devirtual$(a, b);
}

function hashCode_20(o){
  return o != null?hashCode__I__devirtual$(o):0;
}

function $clinit_Random(){
  $clinit_Random = emptyMethod;
  var i_0, i0, twoToTheXMinus24Tmp, twoToTheXMinus48Tmp;
  twoToTheXMinus24 = initDim(D_classLit, $intern_29, 0, 25, 7, 1);
  twoToTheXMinus48 = initDim(D_classLit, $intern_29, 0, 33, 7, 1);
  twoToTheXMinus48Tmp = $intern_30;
  for (i0 = 32; i0 >= 0; i0--) {
    twoToTheXMinus48[i0] = twoToTheXMinus48Tmp;
    twoToTheXMinus48Tmp *= 0.5;
  }
  twoToTheXMinus24Tmp = 1;
  for (i_0 = 24; i_0 >= 0; i_0--) {
    twoToTheXMinus24[i_0] = twoToTheXMinus24Tmp;
    twoToTheXMinus24Tmp *= 0.5;
  }
}

function Random(){
  $clinit_Random();
  var hi, seed;
  seed = uniqueSeed++ + now_1();
  hi = round_int(Math.floor(seed * 5.9604644775390625E-8)) & 16777215;
  round_int(seed - hi * $intern_22);
}

defineClass(353, 1, {}, Random);
var twoToTheXMinus24, twoToTheXMinus48, uniqueSeed = 0;
var Ljava_util_Random_2_classLit = createForClass('java.util', 'Random', 353);
function $clinit_Level(){
  $clinit_Level = emptyMethod;
  ALL = new Level$LevelAll;
  CONFIG = new Level$LevelConfig;
  FINE = new Level$LevelFine;
  FINER = new Level$LevelFiner;
  FINEST = new Level$LevelFinest;
  INFO = new Level$LevelInfo;
  OFF = new Level$LevelOff;
  SEVERE = new Level$LevelSevere;
  WARNING = new Level$LevelWarning;
}

defineClass(440, 1, $intern_29);
_.getName = function getName_0(){
  return 'DUMMY';
}
;
_.intValue_0 = function intValue(){
  return -1;
}
;
_.toString$ = function toString_24(){
  return this.getName();
}
;
var ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE, WARNING;
var Ljava_util_logging_Level_2_classLit = createForClass('java.util.logging', 'Level', 440);
function Level$LevelAll(){
}

defineClass(270, 440, $intern_29, Level$LevelAll);
_.getName = function getName_1(){
  return 'ALL';
}
;
_.intValue_0 = function intValue_0(){
  return -2147483648;
}
;
var Ljava_util_logging_Level$LevelAll_2_classLit = createForClass('java.util.logging', 'Level/LevelAll', 270);
function Level$LevelConfig(){
}

defineClass(271, 440, $intern_29, Level$LevelConfig);
_.getName = function getName_2(){
  return 'CONFIG';
}
;
_.intValue_0 = function intValue_1(){
  return 700;
}
;
var Ljava_util_logging_Level$LevelConfig_2_classLit = createForClass('java.util.logging', 'Level/LevelConfig', 271);
function Level$LevelFine(){
}

defineClass(272, 440, $intern_29, Level$LevelFine);
_.getName = function getName_3(){
  return 'FINE';
}
;
_.intValue_0 = function intValue_2(){
  return 500;
}
;
var Ljava_util_logging_Level$LevelFine_2_classLit = createForClass('java.util.logging', 'Level/LevelFine', 272);
function Level$LevelFiner(){
}

defineClass(273, 440, $intern_29, Level$LevelFiner);
_.getName = function getName_4(){
  return 'FINER';
}
;
_.intValue_0 = function intValue_3(){
  return 400;
}
;
var Ljava_util_logging_Level$LevelFiner_2_classLit = createForClass('java.util.logging', 'Level/LevelFiner', 273);
function Level$LevelFinest(){
}

defineClass(274, 440, $intern_29, Level$LevelFinest);
_.getName = function getName_5(){
  return 'FINEST';
}
;
_.intValue_0 = function intValue_4(){
  return 300;
}
;
var Ljava_util_logging_Level$LevelFinest_2_classLit = createForClass('java.util.logging', 'Level/LevelFinest', 274);
function Level$LevelInfo(){
}

defineClass(275, 440, $intern_29, Level$LevelInfo);
_.getName = function getName_6(){
  return 'INFO';
}
;
_.intValue_0 = function intValue_5(){
  return 800;
}
;
var Ljava_util_logging_Level$LevelInfo_2_classLit = createForClass('java.util.logging', 'Level/LevelInfo', 275);
function Level$LevelOff(){
}

defineClass(276, 440, $intern_29, Level$LevelOff);
_.getName = function getName_7(){
  return 'OFF';
}
;
_.intValue_0 = function intValue_6(){
  return 2147483647;
}
;
var Ljava_util_logging_Level$LevelOff_2_classLit = createForClass('java.util.logging', 'Level/LevelOff', 276);
function Level$LevelSevere(){
}

defineClass(277, 440, $intern_29, Level$LevelSevere);
_.getName = function getName_8(){
  return 'SEVERE';
}
;
_.intValue_0 = function intValue_7(){
  return 1000;
}
;
var Ljava_util_logging_Level$LevelSevere_2_classLit = createForClass('java.util.logging', 'Level/LevelSevere', 277);
function Level$LevelWarning(){
}

defineClass(278, 440, $intern_29, Level$LevelWarning);
_.getName = function getName_9(){
  return 'WARNING';
}
;
_.intValue_0 = function intValue_8(){
  return 900;
}
;
var Ljava_util_logging_Level$LevelWarning_2_classLit = createForClass('java.util.logging', 'Level/LevelWarning', 278);
function $addLoggerImpl(this$static, logger){
  $putStringValue(this$static.loggerMap, logger.impl.name_0, logger);
}

function $ensureLogger(this$static, name_0){
  var logger, newLogger, name_1, parentName;
  logger = dynamicCast($getStringValue(this$static.loggerMap, name_0), 112);
  if (!logger) {
    newLogger = new Logger(name_0);
    name_1 = newLogger.impl.name_0;
    parentName = __substr(name_1, 0, max_1($lastIndexOf(name_1, fromCodePoint(46))));
    $setParent_1(newLogger, $ensureLogger(this$static, parentName));
    $putStringValue(this$static.loggerMap, newLogger.impl.name_0, newLogger);
    return newLogger;
  }
  return logger;
}

function LogManager(){
  this.loggerMap = new HashMap;
}

function getLogManager(){
  var rootLogger;
  if (!singleton) {
    singleton = new LogManager;
    rootLogger = new Logger('');
    $setLevel_1(rootLogger, ($clinit_Level() , INFO));
    $addLoggerImpl(singleton, rootLogger);
  }
  return singleton;
}

defineClass(251, 1, {}, LogManager);
var singleton;
var Ljava_util_logging_LogManager_2_classLit = createForClass('java.util.logging', 'LogManager', 251);
function $setLoggerName(this$static, newName){
  this$static.loggerName = newName;
}

function LogRecord(level, msg){
  this.level = level;
  this.msg = msg;
  this.millis = fromDouble(now_1());
}

defineClass(314, 1, $intern_29, LogRecord);
_.loggerName = '';
_.millis = {l:0, m:0, h:0};
_.thrown = null;
var Ljava_util_logging_LogRecord_2_classLit = createForClass('java.util.logging', 'LogRecord', 314);
function $log_1(this$static, level, msg){
  $log(this$static.impl, level, msg, null);
}

function $log_2(this$static, level, msg, thrown){
  $log(this$static.impl, level, msg, thrown);
}

function $setLevel_1(this$static, newLevel){
  $setLevel_0(this$static.impl, newLevel);
}

function $setParent_1(this$static, newParent){
  $setParent_0(this$static.impl, newParent);
}

function Logger(name_0){
  this.impl = new LoggerImplRegular;
  $setName(this.impl, name_0);
}

function getLogger(name_0){
  new LoggerImplRegular;
  return $ensureLogger(getLogManager(), name_0);
}

defineClass(112, 1, {112:1}, Logger);
var Ljava_util_logging_Logger_2_classLit = createForClass('java.util.logging', 'Logger', 112);
function $onModuleLoad_1(this$static){
  var g3mWidgetHolder, builder, layerSet, cr;
  g3mWidgetHolder = get_0();
  this$static._g3mWidget = (builder = new G3MBuilder_WebGL , layerSet = new LayerSet , $addLayer(layerSet, newOSM_0(new TimeInterval({l:4114432, m:617, h:0}))) , $setLayerSet((!builder._planetRendererBuilder && (builder._planetRendererBuilder = new PlanetRendererBuilder) , builder._planetRendererBuilder), layerSet) , cr = new CameraRenderer , $addHandler_1(cr, new DeviceAttitudeCameraHandler) , $setCameraRenderer(builder, cr) , $createWidget(builder));
  $add_0(g3mWidgetHolder, this$static._g3mWidget);
  $setAnimatedCameraPosition_2(this$static._g3mWidget, new Geodetic3D(new Angle(28.034468668529083, 0.48929378231302334), new Angle(-15.904092315837872, -0.27757877545250076), 1634079));
}

function G3MWebGLTestingApplication(){
}

defineClass(219, 1, {}, G3MWebGLTestingApplication);
_._g3mWidget = null;
var Lorg_glob3_mobile_client_G3MWebGLTestingApplication_2_classLit = createForClass('org.glob3.mobile.client', 'G3MWebGLTestingApplication', 219);
function $render(this$static, rc, parentGLState){
  this$static._enable && this$static.rawRender(rc, parentGLState);
}

function Mesh(){
  this._enable = true;
}

defineClass(102, 1, {});
_._enable = false;
var Lorg_glob3_mobile_generated_Mesh_2_classLit = createForClass('org.glob3.mobile.generated', 'Mesh', 102);
function $computeBoundingVolume(this$static){
  var i_0, i3, maxX, maxY, maxZ, minX, minY, minZ, vertexCount, x_0, y_0, z_0;
  vertexCount = ~~(this$static._vertices._buffer.length / 3);
  if (vertexCount == 0) {
    return null;
  }
  minX = $intern_34;
  minY = $intern_34;
  minZ = $intern_34;
  maxX = $intern_35;
  maxY = $intern_35;
  maxZ = $intern_35;
  for (i_0 = 0; i_0 < vertexCount; i_0++) {
    i3 = i_0 * 3;
    x_0 = $get_5(this$static._vertices, i3) + this$static._center._x;
    y_0 = $get_5(this$static._vertices, i3 + 1) + this$static._center._y;
    z_0 = $get_5(this$static._vertices, i3 + 2) + this$static._center._z;
    x_0 < minX && (minX = x_0);
    x_0 > maxX && (maxX = x_0);
    y_0 < minY && (minY = y_0);
    y_0 > maxY && (maxY = y_0);
    z_0 < minZ && (minZ = z_0);
    z_0 > maxZ && (maxZ = z_0);
  }
  return new Box(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
}

defineClass(390, 102, {});
_.rawRender = function rawRender(rc, parentGLState){
  $setParent_2(this._glState, parentGLState);
  $rawRender(this, rc);
}
;
_._lineWidth = 0;
_._ownsVertices = false;
_._pointSize = 0;
_._primitive = 0;
var Lorg_glob3_mobile_generated_AbstractGeometryMesh_2_classLit = createForClass('org.glob3.mobile.generated', 'AbstractGeometryMesh', 390);
defineClass(475, 1, {});
var Lorg_glob3_mobile_generated_AbstractImageBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'AbstractImageBuilder', 475);
function $createGLState(this$static){
  $addGLFeature(this$static._glState, new GeometryGLFeature(this$static._vertices, this$static._depthTest, 0, 0, this$static._lineWidth, this$static._pointSize), false);
  !!this$static._translationMatrix && $addGLFeature(this$static._glState, new ModelTransformGLFeature($asMatrix44D(this$static._translationMatrix)), false);
  if (!!this$static._flatColor && !this$static._colors) {
    $addGLFeature(this$static._glState, new FlatColorGLFeature(this$static._flatColor, this$static._flatColor._alpha < 1, _srcAlpha, _oneMinusSrcAlpha), false);
    return;
  }
  !!this$static._colors && $addGLFeature(this$static._glState, new ColorGLFeature(this$static._colors, _srcAlpha, _oneMinusSrcAlpha), false);
}

function $dispose(this$static){
  if (this$static._owner) {
    !!this$static._vertices && $dispose_15(this$static._vertices);
    !!this$static._colors && $dispose_15(this$static._colors);
  }
  !!this$static._translationMatrix && $dispose_9(this$static._translationMatrix);
  $_release(this$static._glState);
}

function AbstractMesh(primitive, center, vertices, flatColor, colors, depthTest){
  Mesh.call(this);
  this._primitive = primitive;
  this._owner = true;
  this._vertices = vertices;
  this._flatColor = flatColor;
  this._colors = colors;
  new Vector3D_0(center);
  this._translationMatrix = center._x != center._x || center._y != center._y || center._z != center._z || center._x == 0 && center._y == 0 && center._z == 0?null:new MutableMatrix44D_1(new MutableMatrix44D_0(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, center._x, center._y, center._z, 1));
  this._lineWidth = 1;
  this._pointSize = 1;
  this._depthTest = depthTest;
  this._glState = new GLState;
  $createGLState(this);
}

defineClass(197, 102, {});
_.rawRender = function rawRender_0(rc, parentGLState){
  $setParent_2(this._glState, parentGLState);
  this.rawRender_0(rc);
}
;
_._depthTest = false;
_._lineWidth = 0;
_._owner = false;
_._pointSize = 0;
_._primitive = 0;
var Lorg_glob3_mobile_generated_AbstractMesh_2_classLit = createForClass('org.glob3.mobile.generated', 'AbstractMesh', 197);
function $add_5(this$static, a){
  var r;
  r = this$static._radians + a._radians;
  return new Angle(r * $intern_36, r);
}

function $description(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  $addDouble(isb, this$static._degrees);
  isb._string += 'd';
  s = isb._string;
  return s;
}

function $div(this$static, k){
  var r;
  r = this$static._radians / k;
  return new Angle(r * $intern_36, r);
}

function $div_0(this$static, k){
  return this$static._radians / k._radians;
}

function $equals_1(this$static, obj){
  var other;
  if (this$static === obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  if (Lorg_glob3_mobile_generated_Angle_2_classLit != getClass__Ljava_lang_Class___devirtual$(obj)) {
    return false;
  }
  other = dynamicCast(obj, 6);
  if (neq(doubleToLongBits(this$static._radians), doubleToLongBits(other._radians))) {
    return false;
  }
  return true;
}

function $greaterThan(this$static, a){
  return this$static._radians > a._radians;
}

function $hashCode(this$static){
  var result, temp;
  temp = doubleToLongBits(this$static._radians);
  result = 31 + toInt(xor(temp, shru(temp, 32)));
  return result;
}

function $isBetween(this$static, min_0, max_0){
  return this$static._radians >= min_0._radians && this$static._radians <= max_0._radians;
}

function $isEquals(this$static, that){
  return $isEquals_2(this$static._degrees, that._degrees) || $isEquals_2(this$static._radians, that._radians);
}

function $isNan(this$static){
  return this$static._degrees != this$static._degrees;
}

function $lowerThan(this$static, a){
  return this$static._radians < a._radians;
}

function $sub(this$static, a){
  var r;
  r = this$static._radians - a._radians;
  return new Angle(r * $intern_36, r);
}

function $times(this$static, k){
  var r;
  r = k * this$static._radians;
  return new Angle(r * $intern_36, r);
}

function Angle(degrees, radians){
  this._degrees = degrees;
  this._radians = radians;
}

function Angle_0(angle){
  this._degrees = angle._degrees;
  this._radians = angle._radians;
}

function fromDegrees(degrees){
  return new Angle(degrees, degrees / 180 * $intern_37);
}

function fromRadians(radians){
  return new Angle(radians * $intern_36, radians);
}

function linearInterpolation(from, to, alpha_0){
  return fromRadians((1 - alpha_0) * from._radians + alpha_0 * to._radians);
}

function max_2(a1, a2){
  return a1._degrees > a2._degrees?a1:a2;
}

function midAngle(angle1, angle2){
  return fromRadians((angle1._radians + angle2._radians) / 2);
}

function min_2(a1, a2){
  return a1._degrees < a2._degrees?a1:a2;
}

defineClass(6, 1, {6:1}, Angle, Angle_0);
_.equals$ = function equals_16(obj){
  return $equals_1(this, obj);
}
;
_.hashCode$ = function hashCode_21(){
  return $hashCode(this);
}
;
_.toString$ = function toString_25(){
  return $description(this);
}
;
_._degrees = 0;
_._radians = 0;
var Lorg_glob3_mobile_generated_Angle_2_classLit = createForClass('org.glob3.mobile.generated', 'Angle', 6);
function $add_6(this$static, ps){
  $add_3(this$static._sources, ps);
}

function $get_1(this$static, name_0){
  var i_0, size_0;
  size_0 = this$static._sources.array.length;
  for (i_0 = 0; i_0 < size_0; i_0++) {
    if (compareTo_3(dynamicCast($get_0(this$static._sources, i_0), 21)._name, name_0) == 0) {
      return dynamicCast($get_0(this$static._sources, i_0), 21);
    }
  }
  return null;
}

function GPUProgramFactory(){
  this._sources = new ArrayList;
}

defineClass(177, 1, {}, GPUProgramFactory);
var Lorg_glob3_mobile_generated_GPUProgramFactory_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUProgramFactory', 177);
function BasicShadersGL2(){
  var sourcesBillboard, sourcesBillboard_TransformedTexCoor, sourcesColorMesh, sourcesDefault, sourcesFlatColor2DMesh, sourcesFlatColorMesh, sourcesFlatColorMesh_DirectionLight, sourcesFullTransformedTexCoorMultiTexturedMesh, sourcesFullTransformedTexCoorTexturedMesh, sourcesMultiTexturedMesh, sourcesNoColorMesh, sourcesShader, sourcesTextured2DMesh, sourcesTexturedMesh, sourcesTexturedMesh_DirectionLight, sourcesTransformedTexCoorMultiTexturedMesh, sourcesTransformedTexCoorTexturedMesh, sourcesTransformedTexCoorTexturedMesh_DirectionLight;
  GPUProgramFactory.call(this);
  sourcesBillboard = new GPUProgramSources('Billboard', 'attribute vec2 aTextureCoord;\nuniform mat4 uModelview;\nuniform vec4 uBillboardPosition;\nuniform vec2 uBillboardAnchor; //Anchor in UV (texture-like) coordinates\nuniform vec2 uTextureExtent;\nuniform vec2 uViewPortExtent;\nvarying vec2 TextureCoordOut;\nvoid main() {\ngl_Position = uModelview * uBillboardPosition;\nfloat fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;\nfloat fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;\ngl_Position.x += ((aTextureCoord.x - 0.5) - (uBillboardAnchor.x - 0.5)) * fx;\ngl_Position.y -= ((aTextureCoord.y - 0.5) - (uBillboardAnchor.y - 0.5)) * fy;\nTextureCoordOut = aTextureCoord;\n}\n', 'varying mediump vec2 TextureCoordOut;\nuniform sampler2D Sampler;\nvoid main() {\ngl_FragColor = texture2D(Sampler, TextureCoordOut);\n}\n');
  $add_3(this._sources, sourcesBillboard);
  sourcesBillboard_TransformedTexCoor = new GPUProgramSources('Billboard_TransformedTexCoor', 'attribute vec2 aTextureCoord;\nuniform mat4 uModelview;\nuniform vec4 uBillboardPosition;\nuniform vec2 uBillboardAnchor; //Anchor in UV (texture-like) coordinates\nuniform vec2 uTextureExtent;\nuniform vec2 uViewPortExtent;\nuniform mediump vec2 uTranslationTexCoord;\nuniform mediump vec2 uScaleTexCoord;\nvarying vec2 TextureCoordOut;\nvoid main() {\ngl_Position = uModelview * uBillboardPosition;\nfloat fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;\nfloat fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;\ngl_Position.x += ((aTextureCoord.x - 0.5) - (uBillboardAnchor.x - 0.5)) * fx;\ngl_Position.y -= ((aTextureCoord.y - 0.5) - (uBillboardAnchor.y - 0.5)) * fy;\nTextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n}\n', 'varying mediump vec2 TextureCoordOut;\nuniform sampler2D Sampler;\nvoid main() {\ngl_FragColor = texture2D(Sampler, TextureCoordOut);\n}\n');
  $add_3(this._sources, sourcesBillboard_TransformedTexCoor);
  sourcesColorMesh = new GPUProgramSources('ColorMesh', 'attribute vec4 aPosition;\nattribute vec4 aColor;\nuniform mat4 uModelview;\nuniform float uPointSize;\nvarying vec4 VertexColor;\nvoid main() {\ngl_Position = uModelview * aPosition;\nVertexColor = aColor;\ngl_PointSize = uPointSize;\n}\n', 'varying mediump vec4 VertexColor;\nvoid main() {\ngl_FragColor = VertexColor;\n}\n');
  $add_3(this._sources, sourcesColorMesh);
  sourcesDefault = new GPUProgramSources('Default', 'attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nattribute vec4 aColor;\nuniform mediump vec2 uTranslationTexCoord;\nuniform mediump vec2 uScaleTexCoord;\nuniform mat4 uModelview;\nuniform float uPointSize;\nvarying vec4 VertexColor;\nvarying vec2 TextureCoordOut;\nvoid main() {\ngl_Position = uModelview * aPosition;\nTextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\nVertexColor = aColor;\ngl_PointSize = uPointSize;\n}\n', 'varying mediump vec2 TextureCoordOut;\nvarying mediump vec4 VertexColor;\nuniform sampler2D Sampler;\nuniform bool EnableTexture;\nuniform lowp vec4 uFlatColor;\nuniform bool EnableColorPerVertex;\nuniform bool EnableFlatColor;\nuniform mediump float FlatColorIntensity;\nuniform mediump float ColorPerVertexIntensity;\nvoid main() {\nif (EnableTexture) {\ngl_FragColor = texture2D(Sampler, TextureCoordOut);\nif (EnableFlatColor || EnableColorPerVertex) {\nlowp vec4 color;\nif (EnableFlatColor) {\ncolor = uFlatColor;\nif (EnableColorPerVertex) {\ncolor = color * VertexColor;\n}\n}\nelse {\ncolor = VertexColor;\n}\nlowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\ngl_FragColor = mix(gl_FragColor,\nVertexColor,\nintensity);\n}\n}\nelse {\nif (EnableColorPerVertex) {\ngl_FragColor = VertexColor;\nif (EnableFlatColor) {\ngl_FragColor = gl_FragColor * uFlatColor;\n}\n}\nelse {\ngl_FragColor = uFlatColor;\n}\n}\n}\n');
  $add_3(this._sources, sourcesDefault);
  sourcesFlatColor2DMesh = new GPUProgramSources('FlatColor2DMesh', 'attribute vec2 aPosition2D;\nuniform float uPointSize;\nuniform vec2 uTranslation2D;\nuniform vec2 uViewPortExtent;\nvoid main() {\nvec2 pixel = aPosition2D;\npixel.x -= uViewPortExtent.x / 2.0;\npixel.y += uViewPortExtent.y / 2.0;\ngl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),\n(pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),\n0, 1);\ngl_PointSize = uPointSize;\n}\n', 'uniform lowp vec4 uFlatColor;\nvoid main() {\ngl_FragColor = uFlatColor;\n}\n');
  $add_3(this._sources, sourcesFlatColor2DMesh);
  sourcesFlatColorMesh = new GPUProgramSources('FlatColorMesh', 'attribute vec4 aPosition;\nuniform mat4 uModelview;\nuniform float uPointSize;\nvoid main() {\ngl_Position = uModelview * aPosition;\ngl_PointSize = uPointSize;\n}\n', 'uniform lowp vec4 uFlatColor;\nvoid main() {\ngl_FragColor = uFlatColor;\n}\n');
  $add_3(this._sources, sourcesFlatColorMesh);
  sourcesFlatColorMesh_DirectionLight = new GPUProgramSources('FlatColorMesh_DirectionLight', 'attribute vec4 aPosition;\nattribute vec3 aNormal;\nuniform mat4 uModelview;\nuniform mat4 uModel;\nuniform float uPointSize;\nuniform vec3 uAmbientLightColor;\nuniform vec3 uDiffuseLightColor;\nuniform vec3 uDiffuseLightDirection; //We must normalize\nvarying vec3 lightColor;\nvoid main() {\nvec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\nvec3 lightDirNormalized = normalize( uDiffuseLightDirection );\nfloat diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\ngl_Position = uModelview * aPosition;\ngl_PointSize = uPointSize;\nlightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\nlightColor.x = min(lightColor.x, 1.0);\nlightColor.y = min(lightColor.y, 1.0);\nlightColor.z = min(lightColor.z, 1.0);\n}\n', 'precision highp float;\nuniform lowp vec4 uFlatColor;\nvarying vec3 lightColor;\nvoid main() {\ngl_FragColor.r = uFlatColor.r * lightColor.r;\ngl_FragColor.g = uFlatColor.g * lightColor.r;\ngl_FragColor.b = uFlatColor.b * lightColor.r;\ngl_FragColor.a = uFlatColor.a;\n}\n');
  $add_3(this._sources, sourcesFlatColorMesh_DirectionLight);
  sourcesFullTransformedTexCoorMultiTexturedMesh = new GPUProgramSources('FullTransformedTexCoorMultiTexturedMesh', 'attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nattribute vec2 aTextureCoord2;\nuniform mat4 uModelview;\nuniform float uPointSize;\nvarying vec2 TextureCoordOut;\nvarying vec2 TextureCoordOut2;\nuniform mediump vec2 uTranslationTexCoord;\nuniform mediump vec2 uScaleTexCoord;\nuniform float uRotationAngleTexCoord;\nuniform vec2 uRotationCenterTexCoord;\nvoid main() {\ngl_Position = uModelview * aPosition;\nfloat s = sin( uRotationAngleTexCoord );\nfloat c = cos( uRotationAngleTexCoord );\nTextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\nTextureCoordOut = TextureCoordOut - uRotationCenterTexCoord;\nTextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s),\n(-TextureCoordOut.x * s) + (TextureCoordOut.y * c));\nTextureCoordOut += uRotationCenterTexCoord;\nTextureCoordOut2 = aTextureCoord2;\ngl_PointSize = uPointSize;\n}\n', 'varying mediump vec2 TextureCoordOut;\nvarying mediump vec2 TextureCoordOut2;\nuniform sampler2D Sampler;\nuniform sampler2D Sampler2;\nvoid main() {\nmediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);\nmediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\ngl_FragColor = tex1 * tex2;\n}\n');
  $add_3(this._sources, sourcesFullTransformedTexCoorMultiTexturedMesh);
  sourcesFullTransformedTexCoorTexturedMesh = new GPUProgramSources('FullTransformedTexCoorTexturedMesh', 'attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nuniform mediump vec2 uTranslationTexCoord;\nuniform mediump vec2 uScaleTexCoord;\nuniform mat4 uModelview;\nuniform float uPointSize;\nuniform float uRotationAngleTexCoord;\nuniform vec2 uRotationCenterTexCoord;\nvarying vec4 VertexColor;\nvarying vec2 TextureCoordOut;\nvoid main() {\ngl_Position = uModelview * aPosition;\nfloat s = sin( uRotationAngleTexCoord );\nfloat c = cos( uRotationAngleTexCoord );\nTextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\nTextureCoordOut = TextureCoordOut - uRotationCenterTexCoord;\nTextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s),\n(-TextureCoordOut.x * s) + (TextureCoordOut.y * c));\nTextureCoordOut += uRotationCenterTexCoord;\ngl_PointSize = uPointSize;\n}\n', 'varying mediump vec2 TextureCoordOut;\nvarying mediump vec4 VertexColor;\nuniform sampler2D Sampler;\nvoid main() {\ngl_FragColor = texture2D(Sampler, TextureCoordOut);\n}\n');
  $add_3(this._sources, sourcesFullTransformedTexCoorTexturedMesh);
  sourcesMultiTexturedMesh = new GPUProgramSources('MultiTexturedMesh', 'attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nattribute vec2 aTextureCoord2;\nuniform mat4 uModelview;\nuniform float uPointSize;\nvarying vec2 TextureCoordOut;\nvarying vec2 TextureCoordOut2;\nvoid main() {\ngl_Position = uModelview * aPosition;\nTextureCoordOut = aTextureCoord;\nTextureCoordOut2 = aTextureCoord2;\ngl_PointSize = uPointSize;\n}\n', 'varying mediump vec2 TextureCoordOut;\nvarying mediump vec2 TextureCoordOut2;\nuniform sampler2D Sampler;\nuniform sampler2D Sampler2;\nvoid main() {\nmediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);\nmediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\ngl_FragColor = tex1 * tex2;\n}\n');
  $add_3(this._sources, sourcesMultiTexturedMesh);
  sourcesNoColorMesh = new GPUProgramSources('NoColorMesh', 'attribute vec4 aPosition;\nuniform mat4 uModelview;\nuniform float uPointSize;\nvoid main() {\ngl_Position = uModelview * aPosition;\ngl_PointSize = uPointSize;\n}\n', 'void main() {\ngl_FragColor = vec4(1.0, 0.0, 0.0, 1.0); //RED\n}\n');
  $add_3(this._sources, sourcesNoColorMesh);
  sourcesShader = new GPUProgramSources('Shader', 'attribute vec4 Position;\nattribute vec2 TextureCoord;\nattribute vec4 Color;\nuniform mediump vec2 TranslationTexCoord;\nuniform mediump vec2 ScaleTexCoord;\nuniform mat4 Projection;\nuniform mat4 Modelview;\nuniform bool BillBoard;\nuniform vec2 TextureExtent;\nuniform vec2 ViewPortExtent;\nuniform float PointSize;\nvarying vec4 VertexColor;\nvarying vec2 TextureCoordOut;\nvoid main() {\ngl_Position = Projection * Modelview * Position;\nif (BillBoard) {\ngl_Position.x += ((TextureCoord.x - 0.5) * 2.0 * TextureExtent.x / ViewPortExtent.x) * gl_Position.w;\ngl_Position.y -= ((TextureCoord.y - 0.5) * 2.0 * TextureExtent.y / ViewPortExtent.y) * gl_Position.w;\n}\nTextureCoordOut = (TextureCoord * ScaleTexCoord) + TranslationTexCoord;\nVertexColor = Color;\ngl_PointSize = PointSize;\n}\n', 'varying mediump vec2 TextureCoordOut;\nvarying mediump vec4 VertexColor;\nuniform sampler2D Sampler;\nuniform bool EnableTexture;\nuniform lowp vec4 FlatColor;\nuniform bool EnableColorPerVertex;\nuniform bool EnableFlatColor;\nuniform mediump float FlatColorIntensity;\nuniform mediump float ColorPerVertexIntensity;\nvoid main() {\nif (EnableTexture) {\ngl_FragColor = texture2D(Sampler, TextureCoordOut);\nif (EnableFlatColor || EnableColorPerVertex) {\nlowp vec4 color;\nif (EnableFlatColor) {\ncolor = FlatColor;\nif (EnableColorPerVertex) {\ncolor = color * VertexColor;\n}\n}\nelse {\ncolor = VertexColor;\n}\nlowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\ngl_FragColor = mix(gl_FragColor,\nVertexColor,\nintensity);\n}\n}\nelse {\nif (EnableColorPerVertex) {\ngl_FragColor = VertexColor;\nif (EnableFlatColor) {\ngl_FragColor = gl_FragColor * FlatColor;\n}\n}\nelse {\ngl_FragColor = FlatColor;\n}\n}\n}\n');
  $add_3(this._sources, sourcesShader);
  sourcesTextured2DMesh = new GPUProgramSources('Textured2DMesh', 'attribute vec2 aPosition2D;\nattribute vec2 aTextureCoord;\nuniform float uPointSize;\nvarying vec2 TextureCoordOut;\nuniform vec2 uTranslation2D;\nuniform vec2 uViewPortExtent;\nvoid main() {\nvec2 pixel = aPosition2D;\npixel.x -= uViewPortExtent.x / 2.0;\npixel.y += uViewPortExtent.y / 2.0;\ngl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),\n(pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),\n0, 1);\nTextureCoordOut = aTextureCoord;\ngl_PointSize = uPointSize;\n}\n', 'varying mediump vec2 TextureCoordOut;\nuniform sampler2D Sampler;\nvoid main() {\ngl_FragColor = texture2D(Sampler, TextureCoordOut);\n}\n');
  $add_3(this._sources, sourcesTextured2DMesh);
  sourcesTexturedMesh = new GPUProgramSources('TexturedMesh', 'attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nuniform mat4 uModelview;\nuniform float uPointSize;\nvarying vec2 TextureCoordOut;\nvoid main() {\ngl_Position = uModelview * aPosition;\nTextureCoordOut = aTextureCoord;\ngl_PointSize = uPointSize;\n}\n', 'varying mediump vec2 TextureCoordOut;\nuniform sampler2D Sampler;\nvoid main() {\ngl_FragColor = texture2D(Sampler, TextureCoordOut);\n}\n');
  $add_3(this._sources, sourcesTexturedMesh);
  sourcesTexturedMesh_DirectionLight = new GPUProgramSources('TexturedMesh_DirectionLight', 'attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nattribute vec3 aNormal;\nuniform mat4 uModelview;\nuniform mat4 uModel;\nuniform float uPointSize;\nvarying vec2 TextureCoordOut;\nuniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED IN SHADER\nvarying float diffuseLightIntensity;\nuniform vec3 uAmbientLightColor;\nuniform vec3 uDiffuseLightColor;\nvarying vec3 lightColor;\nvoid main() {\nvec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\nvec3 lightDirNormalized = normalize( uDiffuseLightDirection );\nfloat diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\ngl_Position = uModelview * aPosition;\nTextureCoordOut = aTextureCoord;\ngl_PointSize = uPointSize;\nlightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\nlightColor.x = min(lightColor.x, 1.0);\nlightColor.y = min(lightColor.y, 1.0);\nlightColor.z = min(lightColor.z, 1.0);\n}\n', 'precision highp float;\nvarying mediump vec2 TextureCoordOut;\nuniform sampler2D Sampler;\nvarying vec3 lightColor;\nvoid main() {\nvec4 texColor = texture2D(Sampler, TextureCoordOut);\ngl_FragColor.r = texColor.r * lightColor.r;\ngl_FragColor.g = texColor.g * lightColor.r;\ngl_FragColor.b = texColor.b * lightColor.r;\ngl_FragColor.a = texColor.a;\n}\n');
  $add_3(this._sources, sourcesTexturedMesh_DirectionLight);
  sourcesTransformedTexCoorMultiTexturedMesh = new GPUProgramSources('TransformedTexCoorMultiTexturedMesh', 'attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nattribute vec2 aTextureCoord2;\nuniform mat4 uModelview;\nuniform float uPointSize;\nvarying vec2 TextureCoordOut;\nvarying vec2 TextureCoordOut2;\nuniform mediump vec2 uTranslationTexCoord;\nuniform mediump vec2 uScaleTexCoord;\nvoid main() {\ngl_Position = uModelview * aPosition;\nTextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\nTextureCoordOut2 = aTextureCoord2;\ngl_PointSize = uPointSize;\n}\n', 'varying mediump vec2 TextureCoordOut;\nvarying mediump vec2 TextureCoordOut2;\nuniform sampler2D Sampler;\nuniform sampler2D Sampler2;\nvoid main() {\nmediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);\nmediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\ngl_FragColor = tex1 * tex2;\n}\n');
  $add_3(this._sources, sourcesTransformedTexCoorMultiTexturedMesh);
  sourcesTransformedTexCoorTexturedMesh = new GPUProgramSources('TransformedTexCoorTexturedMesh', 'attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nuniform mediump vec2 uTranslationTexCoord;\nuniform mediump vec2 uScaleTexCoord;\nuniform mat4 uModelview;\nuniform float uPointSize;\nvarying vec4 VertexColor;\nvarying vec2 TextureCoordOut;\nvoid main() {\ngl_Position = uModelview * aPosition;\nTextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\ngl_PointSize = uPointSize;\n}\n', 'varying mediump vec2 TextureCoordOut;\nvarying mediump vec4 VertexColor;\nuniform sampler2D Sampler;\nvoid main() {\ngl_FragColor = texture2D(Sampler, TextureCoordOut);\n}\n');
  $add_3(this._sources, sourcesTransformedTexCoorTexturedMesh);
  sourcesTransformedTexCoorTexturedMesh_DirectionLight = new GPUProgramSources('TransformedTexCoorTexturedMesh_DirectionLight', 'attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nattribute vec3 aNormal;\nuniform mat4 uModelview;\nuniform mat4 uModel;\nuniform float uPointSize;\nvarying vec2 TextureCoordOut;\nuniform mediump vec2 uTranslationTexCoord;\nuniform mediump vec2 uScaleTexCoord;\nuniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED\nvarying float diffuseLightIntensity;\nuniform vec3 uAmbientLightColor;\nuniform vec3 uDiffuseLightColor;\nvarying vec3 lightColor;\nvoid main() {\nvec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\nvec3 lightDirNormalized = normalize( uDiffuseLightDirection );\nfloat diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\ngl_Position = uModelview * aPosition;\nTextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\ngl_PointSize = uPointSize;\nlightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\nlightColor.x = min(lightColor.x, 1.0);\nlightColor.y = min(lightColor.y, 1.0);\nlightColor.z = min(lightColor.z, 1.0);\n}\n', 'precision highp float;\nvarying mediump vec2 TextureCoordOut;\nuniform sampler2D Sampler;\nvarying vec3 lightColor;\nvoid main() {\nvec4 texColor = texture2D(Sampler, TextureCoordOut);\ngl_FragColor.r = texColor.r * lightColor.r;\ngl_FragColor.g = texColor.g * lightColor.r;\ngl_FragColor.b = texColor.b * lightColor.r;\ngl_FragColor.a = texColor.a;\n}\n');
  $add_3(this._sources, sourcesTransformedTexCoorTexturedMesh_DirectionLight);
}

defineClass(287, 177, {}, BasicShadersGL2);
var Lorg_glob3_mobile_generated_BasicShadersGL2_2_classLit = createForClass('org.glob3.mobile.generated', 'BasicShadersGL2', 287);
defineClass(481, 1, {});
var Lorg_glob3_mobile_generated_BoundingVolume_2_classLit = createForClass('org.glob3.mobile.generated', 'BoundingVolume', 481);
function $getCornersArray(this$static){
  if (this$static._cornersArray == null) {
    this$static._cornersArray = initDim(Lorg_glob3_mobile_generated_Vector3F_2_classLit, $intern_4, 40, 8, 0, 1);
    this$static._cornersArray[0] = new Vector3F(this$static._lower._x, this$static._lower._y, this$static._lower._z);
    this$static._cornersArray[1] = new Vector3F(this$static._lower._x, this$static._lower._y, this$static._upper._z);
    this$static._cornersArray[2] = new Vector3F(this$static._lower._x, this$static._upper._y, this$static._lower._z);
    this$static._cornersArray[3] = new Vector3F(this$static._lower._x, this$static._upper._y, this$static._upper._z);
    this$static._cornersArray[4] = new Vector3F(this$static._upper._x, this$static._lower._y, this$static._lower._z);
    this$static._cornersArray[5] = new Vector3F(this$static._upper._x, this$static._lower._y, this$static._upper._z);
    this$static._cornersArray[6] = new Vector3F(this$static._upper._x, this$static._upper._y, this$static._lower._z);
    this$static._cornersArray[7] = new Vector3F(this$static._upper._x, this$static._upper._y, this$static._upper._z);
  }
  return this$static._cornersArray;
}

function $touchesBox(this$static, that){
  if (this$static._lower._x > that._upper._x) {
    return false;
  }
  if (this$static._upper._x < that._lower._x) {
    return false;
  }
  if (this$static._lower._y > that._upper._y) {
    return false;
  }
  if (this$static._upper._y < that._lower._y) {
    return false;
  }
  if (this$static._lower._z > that._upper._z) {
    return false;
  }
  if (this$static._upper._z < that._lower._z) {
    return false;
  }
  return true;
}

function Box(lower, upper){
  this._lower = new Vector3D_0(lower);
  this._upper = new Vector3D_0(upper);
}

defineClass(209, 481, {}, Box);
_._cornersArray = null;
var Lorg_glob3_mobile_generated_Box_2_classLit = createForClass('org.glob3.mobile.generated', 'Box', 209);
function gently(x_0){
  if (x_0 < 0.25) {
    return 2.6666666666666665 * x_0 * x_0;
  }
  if (x_0 > 0.75) {
    return -2.6666666666666665 * x_0 * x_0 + 5.333333333333333 * x_0 + -1.6666666666666665;
  }
  return 1.3333333333333333 * x_0 + -0.16666666666666666;
}

function pace(f){
  var result;
  if (f < 0)
    return 0;
  if (f > 1)
    return 1;
  result = gently(f);
  if (result < 0)
    return 0;
  if (result > 1)
    return 1;
  return result;
}

defineClass(477, 1, {});
_.dispose = function dispose(){
}
;
var Lorg_glob3_mobile_generated_Effect_2_classLit = createForClass('org.glob3.mobile.generated', 'Effect', 477);
defineClass(480, 477, {});
_.doStep = function doStep(rc, when){
}
;
_.isDone = function isDone(rc, when){
  return false;
}
;
var Lorg_glob3_mobile_generated_EffectNeverEnding_2_classLit = createForClass('org.glob3.mobile.generated', 'EffectNeverEnding', 480);
function BusyMeshEffect(renderer){
  this._renderer = renderer;
}

defineClass(370, 480, {}, BusyMeshEffect);
_.cancel = function cancel(when){
}
;
_.doStep = function doStep_0(rc, when){
  var deltaDegrees, ellapsed, now_0;
  now_0 = when._milliseconds;
  ellapsed = sub_0(now_0, this._lastMS);
  this._lastMS = now_0;
  deltaDegrees = 0.3 * toDouble(ellapsed);
  $incDegrees(this._renderer, deltaDegrees);
}
;
_.start_1 = function start_1(rc, when){
  this._lastMS = when._milliseconds;
}
;
_.stop_0 = function stop_0(rc, when){
}
;
_._lastMS = {l:0, m:0, h:0};
var Lorg_glob3_mobile_generated_BusyMeshEffect_2_classLit = createForClass('org.glob3.mobile.generated', 'BusyMeshEffect', 370);
function $createGLState_0(this$static){
  if (!this$static._projectionFeature) {
    this$static._projectionFeature = new ProjectionGLFeature($asMatrix44D(this$static._projectionMatrix));
    $addGLFeature(this$static._glState, this$static._projectionFeature, false);
  }
   else {
    $setMatrix(this$static._projectionFeature, $asMatrix44D(this$static._projectionMatrix));
  }
  if (!this$static._modelFeature) {
    this$static._modelFeature = new ModelGLFeature($asMatrix44D(this$static._modelviewMatrix));
    $addGLFeature(this$static._glState, this$static._modelFeature, false);
  }
   else {
    $setMatrix(this$static._modelFeature, $asMatrix44D(this$static._modelviewMatrix));
  }
}

function $createMesh(rc){
  var angle, c, camera, colors, indices, indicesCounter, minSize, outerRadius, result, s, step, vertices, viewPortHeight, viewPortWidth;
  vertices = new FloatBufferBuilderFromCartesian3D(($clinit_Vector3D() , zero));
  colors = new FloatBufferBuilderFromColor;
  indices = new ShortBufferBuilder;
  indicesCounter = 0;
  camera = rc._currentCamera;
  viewPortWidth = camera._viewPortWidth;
  viewPortHeight = camera._viewPortHeight;
  minSize = viewPortWidth < viewPortHeight?viewPortWidth:viewPortHeight;
  outerRadius = minSize / 15;
  for (step = 0; step <= 5; step++) {
    angle = step * 2 * $intern_37 / 5;
    c = cos_0(angle);
    s = sin_0(angle);
    $add_9(vertices, 0, 0);
    $add_9(vertices, outerRadius * c, outerRadius * s);
    $add_16(indices, narrow_short(indicesCounter++));
    $add_16(indices, narrow_short(indicesCounter++));
    $add_10(colors, 1);
    $add_10(colors, 0);
  }
  $push_back_0(indices._values, 0);
  $push_back_0(indices._values, 1);
  result = new IndexedMesh(_triangleStrip, new Vector3D(vertices._cx, vertices._cy, vertices._cz), new FloatBuffer_WebGL_0(vertices._values._array, vertices._values._size), new ShortBuffer_WebGL(indices._values._array, indices._values._size), new FloatBuffer_WebGL_0(colors._values._array, colors._values._size));
  return result;
}

function $incDegrees(this$static, value_0){
  this$static._degrees += value_0;
  this$static._degrees > 360 && (this$static._degrees -= 360);
  $copyValue(this$static._modelviewMatrix, createRotationMatrix(fromDegrees(this$static._degrees), new Vector3D(0, 0, -1)));
}

function $onResizeViewportEvent(this$static, width_0, height){
  var halfHeight, halfWidth;
  halfWidth = ~~(width_0 / 2);
  halfHeight = ~~(height / 2);
  $copyValue(this$static._projectionMatrix, createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth));
  !!this$static._mesh && $dispose_6(this$static._mesh);
  this$static._mesh = null;
}

function $render_0(this$static, rc){
  var gl, mesh;
  gl = rc._gl;
  $createGLState_0(this$static);
  $clearScreen(gl, this$static._backgroundColor);
  mesh = (!this$static._mesh && (this$static._mesh = $createMesh(rc)) , this$static._mesh);
  !!mesh && $render(mesh, rc, this$static._glState);
}

function BusyMeshRenderer(backgroundColor){
  this._projectionMatrix = new MutableMatrix44D;
  this._modelviewMatrix = new MutableMatrix44D;
  this._degrees = 0;
  this._backgroundColor = backgroundColor;
  this._projectionFeature = null;
  this._modelFeature = null;
  this._glState = new GLState;
  this._mesh = null;
}

defineClass(326, 1, {}, BusyMeshRenderer);
_.initialize = function initialize(context){
}
;
_.onResizeViewportEvent = function onResizeViewportEvent(ec, width_0, height){
  $onResizeViewportEvent(this, width_0, height);
}
;
_.render = function render(rc, glState){
  $render_0(this, rc);
}
;
_.start_2 = function start_2(rc){
  var effect;
  effect = new BusyMeshEffect(this);
  $startEffect(rc._effectsScheduler, effect, this);
}
;
_.stop_1 = function stop_1(rc){
  $cancelAllEffectsFor(rc._effectsScheduler, this);
  !!this._mesh && $dispose_6(this._mesh);
  this._mesh = null;
}
;
_._degrees = 0;
var Lorg_glob3_mobile_generated_BusyMeshRenderer_2_classLit = createForClass('org.glob3.mobile.generated', 'BusyMeshRenderer', 326);
function $_getCartesianCenterOfView(this$static){
  if (this$static._dirtyFlags._cartesianCenterOfViewDirty) {
    this$static._dirtyFlags._cartesianCenterOfViewDirty = false;
    $copyFrom_2(this$static._cartesianCenterOfView, $closestIntersection(this$static._planet, $asVector3D(this$static._position), new Vector3D(this$static._center._x - this$static._position._x, this$static._center._y - this$static._position._y, this$static._center._z - this$static._position._z)));
  }
  return this$static._cartesianCenterOfView;
}

function $_getGeodeticCenterOfView(this$static){
  if (this$static._dirtyFlags._geodeticCenterOfViewDirty) {
    this$static._dirtyFlags._geodeticCenterOfViewDirty = false;
    this$static._geodeticCenterOfView = new Geodetic3D_1($toGeodetic3D(this$static._planet, $asVector3D($_getCartesianCenterOfView(this$static))));
  }
  return this$static._geodeticCenterOfView;
}

function $applyTransform(this$static, M){
  $setCartesianPosition(this$static, $transformedBy(this$static._position, M, 1));
  $setCenter(this$static, $transformedBy(this$static._center, M, 1));
  $setUp(this$static, $transformedBy(this$static._up, M, 0));
}

function $calculateFrustumData(this$static){
  var bottom, height, left, ratio, ratioScreen, right, tanHalfHFOV, tanHalfVFOV, top_0, zFar, zNear;
  height = (!this$static._geodeticPosition && (this$static._geodeticPosition = new Geodetic3D_1($toGeodetic3D(this$static._planet, $asVector3D(this$static._position)))) , this$static._geodeticPosition)._height;
  zNear = height * 0.1;
  zFar = $distanceToHorizon(this$static._planet, $asVector3D(this$static._position));
  ratio = zFar / zNear;
  ratio < 1000 && (zNear = zFar / 1000);
  tanHalfHFOV = this$static._tanHalfHorizontalFieldOfView;
  tanHalfVFOV = this$static._tanHalfVerticalFieldOfView;
  if (tanHalfHFOV != tanHalfHFOV || tanHalfVFOV != tanHalfVFOV) {
    ratioScreen = this$static._viewPortHeight / this$static._viewPortWidth;
    if (tanHalfHFOV != tanHalfHFOV && tanHalfVFOV != tanHalfVFOV) {
      tanHalfVFOV = 0.3;
      tanHalfHFOV = 0.3 / ratioScreen;
    }
     else {
      tanHalfHFOV != tanHalfHFOV?(tanHalfHFOV = tanHalfVFOV / ratioScreen):tanHalfVFOV != tanHalfVFOV && (tanHalfVFOV = tanHalfHFOV * ratioScreen);
    }
  }
  right = tanHalfHFOV * zNear;
  left = -right;
  top_0 = tanHalfVFOV * zNear;
  bottom = -top_0;
  return new FrustumData_0(left, right, bottom, top_0, zNear, zFar);
}

function $copyFrom(this$static, that){
  if (eq(this$static._timestamp, that._timestamp)) {
    return;
  }
  $_getGeodeticCenterOfView(that);
  $_getCartesianCenterOfView(that);
  $getFrustumInModelCoordinates(that);
  $asMatrix44D($getProjectionMatrix(that));
  $asMatrix44D($getModelMatrix(that));
  $asMatrix44D($getModelViewMatrix(that));
  this$static._timestamp = that._timestamp;
  this$static._viewPortWidth = that._viewPortWidth;
  this$static._viewPortHeight = that._viewPortHeight;
  this$static._planet = that._planet;
  $copyFrom_1(this$static._position, that._position);
  $copyFrom_1(this$static._center, that._center);
  $copyFrom_1(this$static._up, that._up);
  $copyFrom_1(this$static._normalizedPosition, that._normalizedPosition);
  $copyFrom_0(this$static._dirtyFlags, that._dirtyFlags);
  this$static._frustumData = that._frustumData;
  $copyValue(this$static._projectionMatrix, that._projectionMatrix);
  $copyValue(this$static._modelMatrix, that._modelMatrix);
  $copyValue(this$static._modelViewMatrix, that._modelViewMatrix);
  $copyFrom_1(this$static._cartesianCenterOfView, that._cartesianCenterOfView);
  this$static._geodeticCenterOfView = that._geodeticCenterOfView;
  this$static._frustum = that._frustum;
  this$static._frustumInModelCoordinates = that._frustumInModelCoordinates;
  this$static._geodeticPosition = that._geodeticPosition;
  this$static._angle2Horizon = that._angle2Horizon;
  this$static._tanHalfVerticalFieldOfView = that._tanHalfVerticalFieldOfView;
  this$static._tanHalfHorizontalFieldOfView = that._tanHalfHorizontalFieldOfView;
}

function $getEstimatedPixelDistance(this$static, point0, point1){
  var angleInRadians, distanceInMeters, frustumData;
  $putSub(this$static._ray0, this$static._position, point0);
  $putSub(this$static._ray1, this$static._position, point1);
  angleInRadians = angleInRadiansBetween(this$static._ray1, this$static._ray0);
  frustumData = $getFrustumData(this$static);
  distanceInMeters = frustumData._znear * Math.tan(angleInRadians / 2);
  return distanceInMeters * this$static._viewPortHeight / frustumData._top;
}

function $getFrustum(this$static){
  if (this$static._dirtyFlags._frustumDirty) {
    this$static._dirtyFlags._frustumDirty = false;
    this$static._frustum = new Frustum_0($getFrustumData(this$static));
  }
  return this$static._frustum;
}

function $getFrustumData(this$static){
  if (this$static._dirtyFlags._frustumDataDirty) {
    this$static._dirtyFlags._frustumDataDirty = false;
    this$static._frustumData = $calculateFrustumData(this$static);
  }
  return this$static._frustumData;
}

function $getFrustumInModelCoordinates(this$static){
  if (this$static._dirtyFlags._frustumMCDirty) {
    this$static._dirtyFlags._frustumMCDirty = false;
    this$static._frustumInModelCoordinates = $transformedBy_P($getFrustum(this$static), $getModelMatrix(this$static));
  }
  return this$static._frustumInModelCoordinates;
}

function $getGeodeticPosition(this$static){
  !this$static._geodeticPosition && (this$static._geodeticPosition = new Geodetic3D_1($toGeodetic3D(this$static._planet, $asVector3D(this$static._position))));
  return this$static._geodeticPosition;
}

function $getHeadingPitchRoll(this$static){
  var cameraRS, localRS;
  localRS = $getCoordinateSystemAt(this$static._planet, (!this$static._geodeticPosition && (this$static._geodeticPosition = new Geodetic3D_1($toGeodetic3D(this$static._planet, $asVector3D(this$static._position)))) , this$static._geodeticPosition));
  cameraRS = new CoordinateSystem(new Vector3D(this$static._center._x - this$static._position._x, this$static._center._y - this$static._position._y, this$static._center._z - this$static._position._z), $asVector3D(this$static._up), $asVector3D(this$static._position));
  return $getTaitBryanAngles(cameraRS, localRS);
}

function $getLookAtParamsInto(this$static, position, center, up){
  $copyFrom_1(position, this$static._position);
  $copyFrom_1(center, this$static._center);
  $copyFrom_1(up, this$static._up);
}

function $getModelMatrix(this$static){
  if (this$static._dirtyFlags._modelMatrixDirty) {
    this$static._dirtyFlags._modelMatrixDirty = false;
    $copyValue(this$static._modelMatrix, createModelMatrix(this$static._position, this$static._center, this$static._up));
  }
  return this$static._modelMatrix;
}

function $getModelViewMatrix(this$static){
  if (this$static._dirtyFlags._modelViewMatrixDirty) {
    this$static._dirtyFlags._modelViewMatrixDirty = false;
    $copyValueOfMultiplication(this$static._modelViewMatrix, $getProjectionMatrix(this$static), $getModelMatrix(this$static));
  }
  return this$static._modelViewMatrix;
}

function $getModelViewMatrixInto(this$static, matrix){
  $copyValue(matrix, $getModelViewMatrix(this$static));
}

function $getProjectionMatrix(this$static){
  if (this$static._dirtyFlags._projectionMatrixDirty) {
    this$static._dirtyFlags._projectionMatrixDirty = false;
    $copyValue(this$static._projectionMatrix, createProjectionMatrix_0($getFrustumData(this$static)));
  }
  return this$static._projectionMatrix;
}

function $getUpMutable(this$static, result){
  $copyFrom_1(result, this$static._up);
}

function $getViewDirectionInto(this$static, result){
  $set_4(result, this$static._center._x - this$static._position._x, this$static._center._y - this$static._position._y, this$static._center._z - this$static._position._z);
}

function $getViewPortInto(this$static, viewport){
  $set_3(viewport, this$static._viewPortWidth, this$static._viewPortHeight);
}

function $initialize(this$static, context){
  this$static._planet = context._planet;
  $setCartesianPosition(this$static, new MutableVector3D_0($maxAxis(this$static._planet._ellipsoid._radii) * 5, 0, 0));
  $setUp(this$static, new MutableVector3D_0(0, 0, 1));
  $setAllDirty(this$static._dirtyFlags);
}

function $moveForward(this$static, d){
  var view;
  view = $normalized_0(new Vector3D(this$static._center._x - this$static._position._x, this$static._center._y - this$static._position._y, this$static._center._z - this$static._position._z));
  $applyTransform(this$static, createTranslationMatrix(new Vector3D(view._x * d, view._y * d, view._z * d)));
}

function $pixel2Ray(this$static, pixel){
  var obj, pixel3D, px, py;
  px = pixel._x;
  py = this$static._viewPortHeight - pixel._y;
  pixel3D = new Vector3D(px, py, 0);
  obj = $unproject($getModelViewMatrix(this$static), pixel3D, this$static._viewPortWidth, this$static._viewPortHeight);
  if (obj._x != obj._x || obj._y != obj._y || obj._z != obj._z) {
    $logWarning(_instance_3, 'Pixel to Ray return NaN', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return obj;
  }
  return $sub_2(obj, $asVector3D(this$static._position));
}

function $resizeViewport(this$static, width_0, height){
  this$static._timestamp = add_0(this$static._timestamp, {l:1, m:0, h:0});
  this$static._viewPortWidth = width_0;
  this$static._viewPortHeight = height;
  $setAllDirty(this$static._dirtyFlags);
}

function $rotateWithAxis(this$static, axis_0, delta){
  $applyTransform(this$static, createRotationMatrix(delta, axis_0));
}

function $rotateWithAxisAndPoint(this$static, axis_0, point, delta){
  var m;
  m = createGeneralRotationMatrix(delta, axis_0, point);
  $applyTransform(this$static, m);
}

function $setCameraCoordinateSystem(this$static, rs){
  this$static._timestamp = add_0(this$static._timestamp, {l:1, m:0, h:0});
  $copyFrom_1(this$static._center, this$static._position);
  $addInPlace(this$static._center, rs._y);
  $copyFrom_2(this$static._up, rs._z);
  $setAllDirty(this$static._dirtyFlags);
}

function $setCartesianPosition(this$static, v){
  var distanceToPlanetCenter, planetRadius;
  if (!$equalTo(v, this$static._position)) {
    this$static._timestamp = add_0(this$static._timestamp, {l:1, m:0, h:0});
    $copyFrom_1(this$static._position, v);
    this$static._geodeticPosition = null;
    $setAllDirty(this$static._dirtyFlags);
    distanceToPlanetCenter = $length(this$static._position);
    planetRadius = distanceToPlanetCenter - (!this$static._geodeticPosition && (this$static._geodeticPosition = new Geodetic3D_1($toGeodetic3D(this$static._planet, $asVector3D(this$static._position)))) , this$static._geodeticPosition)._height;
    this$static._angle2Horizon = Math.acos(planetRadius / distanceToPlanetCenter);
    $copyFrom_1(this$static._normalizedPosition, this$static._position);
    $normalize(this$static._normalizedPosition);
  }
}

function $setCenter(this$static, v){
  if (!$equalTo(v, this$static._center)) {
    this$static._timestamp = add_0(this$static._timestamp, {l:1, m:0, h:0});
    $copyFrom_1(this$static._center, v);
    $setAllDirty(this$static._dirtyFlags);
  }
}

function $setGeodeticPosition(this$static, g3d){
  var dragMatrix, heading, pitch;
  heading = $getHeadingPitchRoll(this$static)._heading;
  pitch = $getHeadingPitchRoll(this$static)._pitch;
  $setPitch(this$static, new Angle(-90, $intern_38));
  dragMatrix = $drag(this$static._planet, (!this$static._geodeticPosition && (this$static._geodeticPosition = new Geodetic3D_1($toGeodetic3D(this$static._planet, $asVector3D(this$static._position)))) , this$static._geodeticPosition), g3d);
  dragMatrix._isValid && $applyTransform(this$static, dragMatrix);
  $setHeading(this$static, heading);
  $setPitch(this$static, pitch);
}

function $setHeading(this$static, angle){
  var angles, cameraRS, localRS;
  angles = $getHeadingPitchRoll(this$static);
  localRS = $getCoordinateSystemAt(this$static._planet, (!this$static._geodeticPosition && (this$static._geodeticPosition = new Geodetic3D_1($toGeodetic3D(this$static._planet, $asVector3D(this$static._position)))) , this$static._geodeticPosition));
  cameraRS = $applyTaitBryanAngles(localRS, angle, angles._pitch, angles._roll);
  $setCameraCoordinateSystem(this$static, cameraRS);
}

function $setLookAtParams(this$static, position, center, up){
  $setCartesianPosition(this$static, position);
  $setCenter(this$static, center);
  $setUp(this$static, up);
}

function $setPitch(this$static, angle){
  var angles, cameraRS, localRS;
  angles = $getHeadingPitchRoll(this$static);
  localRS = $getCoordinateSystemAt(this$static._planet, (!this$static._geodeticPosition && (this$static._geodeticPosition = new Geodetic3D_1($toGeodetic3D(this$static._planet, $asVector3D(this$static._position)))) , this$static._geodeticPosition));
  cameraRS = $applyTaitBryanAngles(localRS, angles._heading, angle, angles._roll);
  $setCameraCoordinateSystem(this$static, cameraRS);
}

function $setRoll(this$static, angle){
  var angles, cameraRS, localRS;
  angles = $getHeadingPitchRoll(this$static);
  localRS = $getCoordinateSystemAt(this$static._planet, (!this$static._geodeticPosition && (this$static._geodeticPosition = new Geodetic3D_1($toGeodetic3D(this$static._planet, $asVector3D(this$static._position)))) , this$static._geodeticPosition));
  cameraRS = $applyTaitBryanAngles(localRS, angles._heading, angles._pitch, angle);
  $setCameraCoordinateSystem(this$static, cameraRS);
}

function $setUp(this$static, v){
  if (!$equalTo(v, this$static._up)) {
    this$static._timestamp = add_0(this$static._timestamp, {l:1, m:0, h:0});
    $copyFrom_1(this$static._up, v);
    $setAllDirty(this$static._dirtyFlags);
  }
}

function Camera(timestamp){
  this._ray0 = new MutableVector3D;
  this._ray1 = new MutableVector3D;
  this._position = new MutableVector3D;
  this._center = new MutableVector3D;
  this._up = new MutableVector3D;
  this._normalizedPosition = new MutableVector3D;
  this._dirtyFlags = new CameraDirtyFlags;
  this._frustumData = new FrustumData;
  this._projectionMatrix = new MutableMatrix44D;
  this._modelMatrix = new MutableMatrix44D;
  this._modelViewMatrix = new MutableMatrix44D;
  this._cartesianCenterOfView = new MutableVector3D;
  this._planet = null;
  this._position = new MutableVector3D_0(0, 0, 0);
  this._center = new MutableVector3D_0(0, 0, 0);
  this._up = new MutableVector3D_0(0, 0, 1);
  this._dirtyFlags = new CameraDirtyFlags;
  this._frustumData = new FrustumData;
  this._projectionMatrix = new MutableMatrix44D;
  this._modelMatrix = new MutableMatrix44D;
  this._modelViewMatrix = new MutableMatrix44D;
  this._cartesianCenterOfView = new MutableVector3D_0(0, 0, 0);
  this._geodeticCenterOfView = null;
  this._frustum = null;
  this._frustumInModelCoordinates = null;
  this._camEffectTarget = new Camera$CameraEffectTarget;
  this._geodeticPosition = null;
  this._angle2Horizon = -99;
  this._normalizedPosition = new MutableVector3D_0(0, 0, 0);
  this._tanHalfVerticalFieldOfView = NaN;
  this._tanHalfHorizontalFieldOfView = NaN;
  this._timestamp = timestamp;
  $resizeViewport(this, 0, 0);
  $setAllDirty(this._dirtyFlags);
}

function pixel2Ray(position, pixel, viewport, modelViewMatrix){
  var obj, pixel3D, px, py;
  px = pixel._x;
  py = viewport._y - pixel._y;
  pixel3D = new Vector3D(px, py, 0);
  obj = $unproject(modelViewMatrix, pixel3D, viewport._x, viewport._y);
  if (obj._x != obj._x || obj._y != obj._y || obj._z != obj._z) {
    return obj;
  }
  return $sub_2(obj, new Vector3D(position._x, position._y, position._z));
}

function pixel2RayInto(position, pixel, viewport, modelViewMatrix, ray){
  var obj, pixel3D, px, py;
  px = pixel._x;
  py = viewport._y - pixel._y;
  pixel3D = new Vector3D(px, py, 0);
  obj = $unproject(modelViewMatrix, pixel3D, viewport._x, viewport._y);
  obj._x != obj._x || obj._y != obj._y || obj._z != obj._z?$copyFrom_2(ray, obj):$set_4(ray, obj._x - position._x, obj._y - position._y, obj._z - position._z);
}

defineClass(185, 1, {}, Camera);
_.getGeodeticPosition = function getGeodeticPosition(){
  return $getGeodeticPosition(this);
}
;
_.getHeading = function getHeading(){
  return $getHeadingPitchRoll(this)._heading;
}
;
_.getPitch = function getPitch(){
  return $getHeadingPitchRoll(this)._pitch;
}
;
_._angle2Horizon = 0;
_._tanHalfHorizontalFieldOfView = 0;
_._tanHalfVerticalFieldOfView = 0;
_._timestamp = {l:0, m:0, h:0};
_._viewPortHeight = 0;
_._viewPortWidth = 0;
var Lorg_glob3_mobile_generated_Camera_2_classLit = createForClass('org.glob3.mobile.generated', 'Camera', 185);
function Camera$CameraEffectTarget(){
}

defineClass(336, 1, {}, Camera$CameraEffectTarget);
var Lorg_glob3_mobile_generated_Camera$CameraEffectTarget_2_classLit = createForClass('org.glob3.mobile.generated', 'Camera/CameraEffectTarget', 336);
function $setCurrentGesture(this$static, gesture){
  this$static._currentGesture = gesture;
}

function CameraContext(gesture, nextCamera){
  this._currentGesture = gesture;
  this._nextCamera = nextCamera;
}

defineClass(286, 1, {}, CameraContext);
var Lorg_glob3_mobile_generated_CameraContext_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraContext', 286);
function $copyFrom_0(this$static, other){
  this$static._frustumDataDirty = other._frustumDataDirty;
  this$static._projectionMatrixDirty = other._projectionMatrixDirty;
  this$static._modelMatrixDirty = other._modelMatrixDirty;
  this$static._modelViewMatrixDirty = other._modelViewMatrixDirty;
  this$static._cartesianCenterOfViewDirty = other._cartesianCenterOfViewDirty;
  this$static._geodeticCenterOfViewDirty = other._geodeticCenterOfViewDirty;
  this$static._frustumDirty = other._frustumDirty;
  this$static._frustumMCDirty = other._frustumMCDirty;
}

function $setAllDirty(this$static){
  this$static._frustumDataDirty = true;
  this$static._projectionMatrixDirty = true;
  this$static._modelMatrixDirty = true;
  this$static._modelViewMatrixDirty = true;
  this$static._cartesianCenterOfViewDirty = true;
  this$static._geodeticCenterOfViewDirty = true;
  this$static._frustumDirty = true;
  this$static._frustumMCDirty = true;
}

function CameraDirtyFlags(){
  $setAllDirty(this);
}

defineClass(190, 1, {}, CameraDirtyFlags);
_.toString$ = function toString_26(){
  var d;
  return d = '' , this._frustumDataDirty && (d += 'FD ') , this._projectionMatrixDirty && (d += 'PM ') , this._modelMatrixDirty && (d += 'MM ') , this._modelViewMatrixDirty && (d += 'MVM ') , this._cartesianCenterOfViewDirty && (d += 'CCV ') , this._geodeticCenterOfViewDirty && (d += 'GCV ') , this._frustumDirty && (d += 'F ') , this._frustumMCDirty && (d += 'FMC ') , d;
}
;
_._cartesianCenterOfViewDirty = false;
_._frustumDataDirty = false;
_._frustumDirty = false;
_._frustumMCDirty = false;
_._geodeticCenterOfViewDirty = false;
_._modelMatrixDirty = false;
_._modelViewMatrixDirty = false;
_._projectionMatrixDirty = false;
var Lorg_glob3_mobile_generated_CameraDirtyFlags_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraDirtyFlags', 190);
defineClass(64, 1, $intern_39);
var Lorg_glob3_mobile_generated_CameraEventHandler_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraEventHandler', 64);
function $onDown(this$static, eventContext, touchEvent, cameraContext){
  var camera, initialRay0, initialRay1, pixel0, pixel1;
  camera = cameraContext._nextCamera;
  $getLookAtParamsInto(camera, this$static._cameraPosition, this$static._cameraCenter, this$static._cameraUp);
  $getModelViewMatrixInto(camera, this$static._cameraModelViewMatrix);
  $getViewPortInto(camera, this$static._cameraViewPort);
  pixel0 = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
  pixel1 = dynamicCast($get_0(touchEvent._touchs, 1), 10)._pos;
  initialRay0 = $pixel2Ray(camera, pixel0);
  initialRay1 = $pixel2Ray(camera, pixel1);
  if (initialRay0._x != initialRay0._x || initialRay0._y != initialRay0._y || initialRay0._z != initialRay0._z || initialRay1._x != initialRay1._x || initialRay1._y != initialRay1._y || initialRay1._z != initialRay1._z)
    return;
  $setCurrentGesture(cameraContext, ($clinit_Gesture() , DoubleDrag));
  $beginDoubleDrag(eventContext._planet, $asVector3D(camera._position), new Vector3D(camera._center._x - camera._position._x, camera._center._y - camera._position._y, camera._center._z - camera._position._z), $pixel2Ray(camera, pixel0), $pixel2Ray(camera, pixel1));
}

function $onMove(this$static, eventContext, touchEvent, cameraContext){
  var initialRay0, initialRay1, matrix, pixel0, pixel1, planet;
  if (cameraContext._currentGesture != ($clinit_Gesture() , DoubleDrag))
    return;
  planet = eventContext._planet;
  pixel0 = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
  pixel1 = dynamicCast($get_0(touchEvent._touchs, 1), 10)._pos;
  initialRay0 = pixel2Ray(this$static._cameraPosition, pixel0, this$static._cameraViewPort, this$static._cameraModelViewMatrix);
  initialRay1 = pixel2Ray(this$static._cameraPosition, pixel1, this$static._cameraViewPort, this$static._cameraModelViewMatrix);
  if (initialRay0._x != initialRay0._x || initialRay0._y != initialRay0._y || initialRay0._z != initialRay0._z || initialRay1._x != initialRay1._x || initialRay1._y != initialRay1._y || initialRay1._z != initialRay1._z)
    return;
  matrix = $doubleDrag(planet, initialRay0, initialRay1);
  if (!matrix._isValid)
    return;
  $setLookAtParams(cameraContext._nextCamera, $transformedBy(this$static._cameraPosition, matrix, 1), $transformedBy(this$static._cameraCenter, matrix, 1), $transformedBy(this$static._cameraUp, matrix, 0));
}

function CameraDoubleDragHandler(){
  this._cameraPosition = new MutableVector3D;
  this._cameraCenter = new MutableVector3D;
  this._cameraUp = new MutableVector3D;
  this._cameraViewPort = new MutableVector2I;
  this._cameraModelViewMatrix = new MutableMatrix44D;
}

defineClass(346, 64, $intern_39, CameraDoubleDragHandler);
_.onTouchEvent = function onTouchEvent(eventContext, touchEvent, cameraContext){
  if (touchEvent._touchs.array.length != 2)
    return false;
  switch (touchEvent._eventType.ordinal) {
    case 0:
      $onDown(this, eventContext, touchEvent, cameraContext);
      break;
    case 2:
      $onMove(this, eventContext, touchEvent, cameraContext);
      break;
    case 1:
      $setCurrentGesture(cameraContext, ($clinit_Gesture() , None));
  }
  return true;
}
;
_.render_0 = function render_0(rc, cameraContext){
}
;
var Lorg_glob3_mobile_generated_CameraDoubleDragHandler_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraDoubleDragHandler', 346);
function $onDown_0(eventContext, touchEvent, cameraContext){
  var camera, effect, pixel, planet, target;
  pixel = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
  planet = eventContext._planet;
  camera = cameraContext._nextCamera;
  effect = $createDoubleTapEffect(planet, $asVector3D(camera._position), new Vector3D(camera._center._x - camera._position._x, camera._center._y - camera._position._y, camera._center._z - camera._position._z), $pixel2Ray(camera, pixel));
  if (effect) {
    target = cameraContext._nextCamera._camEffectTarget;
    $startEffect(eventContext._effectsScheduler, effect, target);
  }
}

function CameraDoubleTapHandler(){
}

defineClass(348, 64, $intern_39, CameraDoubleTapHandler);
_.onTouchEvent = function onTouchEvent_0(eventContext, touchEvent, cameraContext){
  if (touchEvent._touchs.array.length != 1)
    return false;
  if ($getTapCount(touchEvent) != 2)
    return false;
  if (touchEvent._eventType != ($clinit_TouchEventType() , Down))
    return false;
  $onDown_0(eventContext, touchEvent, cameraContext);
  return true;
}
;
_.render_0 = function render_1(rc, cameraContext){
}
;
var Lorg_glob3_mobile_generated_CameraDoubleTapHandler_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraDoubleTapHandler', 348);
defineClass(458, 1, {});
var Lorg_glob3_mobile_generated_SceneLighting_2_classLit = createForClass('org.glob3.mobile.generated', 'SceneLighting', 458);
function $modifyGLState(this$static, glState, rc){
  var camera, cameraVector, f, lightDir, rotationLightDirAxis;
  camera = rc._currentCamera;
  $getViewDirectionInto(camera, this$static._camDir);
  $getUpMutable(camera, this$static._up);
  if (this$static._cameraDirX == this$static._camDir._x && this$static._cameraDirY == this$static._camDir._y && this$static._cameraDirZ == this$static._camDir._z && this$static._upX == this$static._up._x && this$static._upY == this$static._up._y && this$static._upZ == this$static._up._z) {
    return;
  }
  cameraVector = $times_0(this$static._camDir);
  rotationLightDirAxis = $cross(this$static._up, cameraVector);
  lightDir = $rotateAroundAxis(cameraVector, rotationLightDirAxis, new Angle(45, 0.7853981633974483));
  f = dynamicCast($getGLFeature(glState, 11), 148);
  if (!f) {
    $clearGLFeatureGroup(glState, ($clinit_GLFeatureGroupName() , LIGHTING_GROUP));
    $addGLFeature(glState, new DirectionLightGLFeature(new Vector3D(lightDir._x, lightDir._y, lightDir._z), this$static._diffuseColor, this$static._ambientColor), false);
  }
   else {
    $setLightDirection(f, new Vector3D(lightDir._x, lightDir._y, lightDir._z));
  }
  this$static._cameraDirX = this$static._camDir._x;
  this$static._cameraDirY = this$static._camDir._y;
  this$static._cameraDirZ = this$static._camDir._z;
  this$static._upX = this$static._up._x;
  this$static._upY = this$static._up._y;
  this$static._upZ = this$static._up._z;
}

function CameraFocusSceneLighting(ambient, diffuse){
  this._camDir = new MutableVector3D;
  this._up = new MutableVector3D;
  this._ambientColor = new Color_0(ambient);
  this._diffuseColor = new Color_0(diffuse);
  this._cameraDirX = 0;
  this._cameraDirY = 0;
  this._cameraDirZ = 0;
}

defineClass(327, 458, {}, CameraFocusSceneLighting);
_._cameraDirX = 0;
_._cameraDirY = 0;
_._cameraDirZ = 0;
_._upX = 0;
_._upY = 0;
_._upZ = 0;
var Lorg_glob3_mobile_generated_CameraFocusSceneLighting_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraFocusSceneLighting', 327);
function $getAlpha(this$static, when){
  var percent;
  percent = $percentDone(this$static, when);
  return this$static._linearTiming?percent:pace(percent);
}

function $percentDone(this$static, when){
  var elapsed, percent;
  elapsed = sub_0(when._milliseconds, this$static._started);
  percent = toDouble(elapsed) / toDouble(this$static._durationMS);
  if (percent > 1)
    return 1;
  if (percent < 0)
    return 0;
  return percent;
}

function EffectWithDuration(duration){
  this._durationMS = duration._milliseconds;
  this._linearTiming = false;
  this._started = {l:0, m:0, h:0};
}

defineClass(194, 477, {});
_.isDone = function isDone_0(rc, when){
  var percent;
  percent = $percentDone(this, when);
  return percent >= 1;
}
;
_.start_1 = function start_3(rc, when){
  this._started = when._milliseconds;
}
;
_._durationMS = {l:0, m:0, h:0};
_._linearTiming = false;
_._started = {l:0, m:0, h:0};
var Lorg_glob3_mobile_generated_EffectWithDuration_2_classLit = createForClass('org.glob3.mobile.generated', 'EffectWithDuration', 194);
function $calculateMaxHeight(this$static, planet){
  var averageHeight, delta, deltaLatInDeg, deltaLonInDeg, distanceInDeg, maxHeight, middleHeight;
  maxHeight = $axisAverage(planet._ellipsoid._radii) * 5;
  deltaLatInDeg = this$static._fromPosition._latitude._degrees - this$static._toPosition._latitude._degrees;
  deltaLonInDeg = this$static._fromPosition._longitude._degrees - this$static._toPosition._longitude._degrees;
  distanceInDeg = Math.sqrt(deltaLatInDeg * deltaLatInDeg + deltaLonInDeg * deltaLonInDeg);
  if (distanceInDeg >= 180) {
    return maxHeight;
  }
  middleHeight = distanceInDeg / 180 * maxHeight;
  averageHeight = (this$static._fromPosition._height + this$static._toPosition._height) / 2;
  if (middleHeight < averageHeight) {
    delta = (averageHeight - middleHeight) / 2;
    return averageHeight + delta;
  }
  return middleHeight;
}

function CameraGoToPositionEffect(duration, fromPosition, toPosition, fromHeading, toHeading, fromPitch, toPitch){
  EffectWithDuration.call(this, duration);
  this._fromPosition = new Geodetic3D_1(fromPosition);
  this._toPosition = new Geodetic3D_1(toPosition);
  this._fromHeading = new Angle_0(fromHeading);
  this._toHeading = new Angle_0(toHeading);
  this._fromPitch = new Angle_0(fromPitch);
  this._toPitch = new Angle_0(toPitch);
  this._linearHeight = false;
}

defineClass(367, 194, {}, CameraGoToPositionEffect);
_.cancel = function cancel_0(when){
}
;
_.doStep = function doStep_1(rc, when){
  var alpha_0, camera, heading, height, middlePitch;
  alpha_0 = $getAlpha(this, when);
  this._linearHeight?(height = $linearInterpolation(this._fromPosition._height, this._toPosition._height, alpha_0)):(height = $quadraticBezierInterpolation(this._fromPosition._height, this._middleHeight, this._toPosition._height, alpha_0));
  camera = rc._nextCamera;
  $setGeodeticPosition(camera, new Geodetic3D(linearInterpolation(this._fromPosition._latitude, this._toPosition._latitude, alpha_0), linearInterpolation(this._fromPosition._longitude, this._toPosition._longitude, alpha_0), height));
  heading = linearInterpolation(this._fromHeading, this._toHeading, alpha_0);
  $setHeading(camera, heading);
  middlePitch = new Angle(-90, $intern_38);
  alpha_0 <= 0.1?$setPitch(camera, linearInterpolation(this._fromPitch, middlePitch, alpha_0 * 10)):alpha_0 >= 0.9?$setPitch(camera, linearInterpolation(middlePitch, this._toPitch, (alpha_0 - 0.9) * 10)):$setPitch(camera, middlePitch);
}
;
_.start_1 = function start_4(rc, when){
  this._started = when._milliseconds;
  this._middleHeight = $calculateMaxHeight(this, rc._planet);
}
;
_.stop_0 = function stop_2(rc, when){
  var camera;
  camera = rc._nextCamera;
  $setGeodeticPosition(camera, this._toPosition);
  $setPitch(camera, this._toPitch);
  $setHeading(camera, this._toHeading);
}
;
_._linearHeight = false;
_._middleHeight = 0;
var Lorg_glob3_mobile_generated_CameraGoToPositionEffect_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraGoToPositionEffect', 367);
function $addHandler_1(this$static, handler){
  $add_3(this$static._handlers, handler);
}

function $onTouchEvent(this$static, ec, touchEvent){
  var handlersSize, i_0, target;
  if (this$static._processTouchEvents) {
    if (touchEvent._eventType == ($clinit_TouchEventType() , Down)) {
      target = this$static._cameraContext._nextCamera._camEffectTarget;
      $cancelAllEffectsFor(ec._effectsScheduler, target);
    }
    handlersSize = this$static._handlers.array.length;
    for (i_0 = 0; i_0 < handlersSize; i_0++) {
      if (dynamicCast($get_0(this$static._handlers, i_0), 64).onTouchEvent(ec, touchEvent, this$static._cameraContext)) {
        return true;
      }
    }
  }
  return false;
}

function $render_1(this$static, rc){
  var handlersSize, i_0;
  !this$static._cameraContext && (this$static._cameraContext = new CameraContext(($clinit_Gesture() , None), rc._nextCamera));
  handlersSize = this$static._handlers.array.length;
  for (i_0 = 0; i_0 < handlersSize; i_0++) {
    dynamicCast($get_0(this$static._handlers, i_0), 64).render_0(rc, this$static._cameraContext);
  }
}

function CameraRenderer(){
  this._handlers = new ArrayList;
  this._cameraContext = null;
  this._processTouchEvents = true;
}

defineClass(171, 1, {}, CameraRenderer);
_.initialize = function initialize_0(context){
}
;
_.onResizeViewportEvent = function onResizeViewportEvent_0(ec, width_0, height){
}
;
_.render = function render_2(rc, glState){
  $render_1(this, rc);
}
;
_.start_2 = function start_5(rc){
}
;
_.stop_1 = function stop_3(rc){
}
;
_._processTouchEvents = false;
var Lorg_glob3_mobile_generated_CameraRenderer_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraRenderer', 171);
function $onDown_1(this$static, touchEvent, cameraContext){
  var averagePixel, camera, pixel0, pixel1, pixel2;
  camera = cameraContext._nextCamera;
  $getLookAtParamsInto(camera, this$static._cameraPosition, this$static._cameraCenter, this$static._cameraUp);
  $setCurrentGesture(cameraContext, ($clinit_Gesture() , Rotate));
  pixel0 = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
  pixel1 = dynamicCast($get_0(touchEvent._touchs, 1), 10)._pos;
  pixel2 = dynamicCast($get_0(touchEvent._touchs, 2), 10)._pos;
  averagePixel = $div_1($add_17(new Vector2F(pixel0._x + pixel1._x, pixel0._y + pixel1._y), pixel2));
  this$static._pivotPixel = new MutableVector2F_0(averagePixel._x, averagePixel._y);
  $copyFrom_2(this$static._pivotPoint, $asVector3D($_getCartesianCenterOfView(camera)));
  if ($isNan_0(this$static._pivotPoint)) {
    $logError(_instance_3, 'CAMERA ERROR: center point does not intersect globe!!\n', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    $setCurrentGesture(cameraContext, None);
  }
}

function $onMove_0(this$static, eventContext, touchEvent, cameraContext){
  var angle_v, c0, c1, c2, camera, cm, delta, dot, finalAngle, initialAngle, normal, u, view, M;
  if (cameraContext._currentGesture != ($clinit_Gesture() , Rotate))
    return;
  c0 = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
  c1 = dynamicCast($get_0(touchEvent._touchs, 1), 10)._pos;
  c2 = dynamicCast($get_0(touchEvent._touchs, 2), 10)._pos;
  cm = $div_1($add_17(new Vector2F(c0._x + c1._x, c0._y + c1._y), c2));
  normal = $geodeticSurfaceNormal(eventContext._planet, this$static._pivotPoint);
  camera = cameraContext._nextCamera;
  $setLookAtParams(camera, this$static._cameraPosition, this$static._cameraCenter, this$static._cameraUp);
  angle_v = fromDegrees((this$static._pivotPixel._x - cm._x) * 0.25);
  $rotateWithAxisAndPoint(camera, normal, $asVector3D(this$static._pivotPoint), angle_v);
  view = new Vector3D(camera._center._x - camera._position._x, camera._center._y - camera._position._y, camera._center._z - camera._position._z);
  dot = $dot_0($normalized_0(normal), $times_2($normalized_0(view), -1));
  initialAngle = acos_0(dot) / $intern_37 * 180;
  delta = (cm._y - this$static._pivotPixel._y) * 0.25;
  finalAngle = initialAngle + delta;
  finalAngle > 85 && (delta = 85 - initialAngle);
  finalAngle < 0 && (delta = -initialAngle);
  $getLookAtParamsInto(camera, this$static._tempCameraPosition, this$static._tempCameraCenter, this$static._tempCameraUp);
  u = (M = $getModelMatrix(camera) , new Vector3D(M._m00, M._m01, M._m02));
  $rotateWithAxisAndPoint(camera, u, $asVector3D(this$static._pivotPoint), new Angle(delta, delta / 180 * $intern_37));
  $isNan_1($asVector3D($_getCartesianCenterOfView(camera))) && $setLookAtParams(camera, this$static._tempCameraPosition, this$static._tempCameraCenter, this$static._tempCameraUp);
}

function CameraRotationHandler(){
  this._pivotPoint = new MutableVector3D;
  this._pivotPixel = new MutableVector2F;
  this._cameraPosition = new MutableVector3D;
  this._cameraCenter = new MutableVector3D;
  this._cameraUp = new MutableVector3D;
  this._tempCameraPosition = new MutableVector3D;
  this._tempCameraCenter = new MutableVector3D;
  this._tempCameraUp = new MutableVector3D;
  this._pivotPoint = new MutableVector3D_0(0, 0, 0);
  this._pivotPixel = new MutableVector2F_0(0, 0);
}

defineClass(347, 64, $intern_39, CameraRotationHandler);
_.onTouchEvent = function onTouchEvent_1(eventContext, touchEvent, cameraContext){
  if (touchEvent._touchs.array.length != 3)
    return false;
  switch (touchEvent._eventType.ordinal) {
    case 0:
      $onDown_1(this, touchEvent, cameraContext);
      break;
    case 2:
      $onMove_0(this, eventContext, touchEvent, cameraContext);
      break;
    case 1:
      $setCurrentGesture(cameraContext, ($clinit_Gesture() , None));
      this._pivotPixel = new MutableVector2F_0(0, 0);
  }
  return true;
}
;
_.render_0 = function render_3(rc, cameraContext){
}
;
var Lorg_glob3_mobile_generated_CameraRotationHandler_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraRotationHandler', 347);
function $onDown_2(this$static, eventContext, touchEvent, cameraContext){
  var camera, initialRay, pixel;
  camera = cameraContext._nextCamera;
  $getLookAtParamsInto(camera, this$static._cameraPosition, this$static._cameraCenter, this$static._cameraUp);
  $getModelViewMatrixInto(camera, this$static._cameraModelViewMatrix);
  $getViewPortInto(camera, this$static._cameraViewPort);
  pixel = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
  initialRay = $pixel2Ray(camera, pixel);
  if (!(initialRay._x != initialRay._x || initialRay._y != initialRay._y || initialRay._z != initialRay._z)) {
    $setCurrentGesture(cameraContext, ($clinit_Gesture() , Drag));
    $beginSingleDrag(eventContext._planet, $asVector3D(camera._position), initialRay);
  }
}

function $onMove_1(this$static, eventContext, touchEvent, cameraContext){
  var matrix, pixel, planet;
  if (cameraContext._currentGesture != ($clinit_Gesture() , Drag))
    return;
  pixel = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
  pixel2RayInto(this$static._cameraPosition, pixel, this$static._cameraViewPort, this$static._cameraModelViewMatrix, this$static._finalRay);
  if ($isNan_0(this$static._finalRay))
    return;
  planet = eventContext._planet;
  matrix = $singleDrag(planet, $asVector3D(this$static._finalRay));
  if (!matrix._isValid)
    return;
  $setLookAtParams(cameraContext._nextCamera, $transformedBy(this$static._cameraPosition, matrix, 1), $transformedBy(this$static._cameraCenter, matrix, 1), $transformedBy(this$static._cameraUp, matrix, 0));
}

function $onUp(this$static, eventContext, touchEvent, cameraContext){
  var currPixel, desp, effect, planet, prevPixel, target, touch;
  planet = eventContext._planet;
  if (this$static._useInertia) {
    touch = dynamicCast($get_0(touchEvent._touchs, 0), 10);
    currPixel = touch._pos;
    prevPixel = touch._prevPos;
    desp = $length_0(new Vector2F(currPixel._x - prevPixel._x, currPixel._y - prevPixel._y));
    $getDeviceInfo(_instance_1);
    if (cameraContext._currentGesture == ($clinit_Gesture() , Drag) && desp > 0.7559055344274556) {
      effect = $createEffectFromLastSingleDrag(planet);
      if (effect) {
        target = cameraContext._nextCamera._camEffectTarget;
        $startEffect(eventContext._effectsScheduler, effect, target);
      }
    }
  }
  $setCurrentGesture(cameraContext, ($clinit_Gesture() , None));
}

function CameraSingleDragHandler(){
  this._cameraPosition = new MutableVector3D;
  this._cameraCenter = new MutableVector3D;
  this._cameraUp = new MutableVector3D;
  this._cameraViewPort = new MutableVector2I;
  this._cameraModelViewMatrix = new MutableMatrix44D;
  this._finalRay = new MutableVector3D;
  this._useInertia = true;
}

defineClass(345, 64, $intern_39, CameraSingleDragHandler);
_.onTouchEvent = function onTouchEvent_2(eventContext, touchEvent, cameraContext){
  if (touchEvent._touchs.array.length != 1)
    return false;
  if ($getTapCount(touchEvent) > 1)
    return false;
  switch (touchEvent._eventType.ordinal) {
    case 0:
      $onDown_2(this, eventContext, touchEvent, cameraContext);
      break;
    case 2:
      $onMove_1(this, eventContext, touchEvent, cameraContext);
      break;
    case 1:
      $onUp(this, eventContext, touchEvent, cameraContext);
  }
  return true;
}
;
_.render_0 = function render_4(rc, cameraContext){
}
;
_._useInertia = false;
var Lorg_glob3_mobile_generated_CameraSingleDragHandler_2_classLit = createForClass('org.glob3.mobile.generated', 'CameraSingleDragHandler', 345);
defineClass(91, 1, $intern_40);
var Lorg_glob3_mobile_generated_CanvasElement_2_classLit = createForClass('org.glob3.mobile.generated', 'CanvasElement', 91);
function $build(this$static, listener){
  var canvas;
  canvas = $getCanvas(this$static);
  $buildOnCanvas(this$static, canvas);
  $createImage(canvas, new CanvasImageBuilder_ImageListener('_DefaultChessCanvasImage_' + ('' + this$static._width) + '_' + ('' + this$static._height) + '_' + $toID(this$static._backgroundColor) + '_' + $toID(this$static._boxColor) + '_' + ('' + this$static._splits), listener));
}

function $getCanvas(this$static){
  if (!this$static._canvas || this$static._canvasWidth != this$static._width || this$static._canvasHeight != this$static._height) {
    this$static._canvas = new Canvas_WebGL;
    $initialize_2(this$static._canvas, this$static._width, this$static._height);
    this$static._canvasWidth = this$static._width;
    this$static._canvasHeight = this$static._height;
  }
   else {
    $setFillColor(this$static._canvas, new Color(0, 0, 0, 0));
    $fillRectangle(this$static._canvas, this$static._width, this$static._height);
  }
  return this$static._canvas;
}

defineClass(360, 475, {});
_._canvasHeight = 0;
_._canvasWidth = 0;
_._height = 0;
_._width = 0;
var Lorg_glob3_mobile_generated_CanvasImageBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'CanvasImageBuilder', 360);
defineClass(473, 1, {});
_.dispose = function dispose_0(){
}
;
var Lorg_glob3_mobile_generated_IImageListener_2_classLit = createForClass('org.glob3.mobile.generated', 'IImageListener', 473);
function CanvasImageBuilder_ImageListener(imageName, listener){
  this._imageName = imageName;
  this._listener = listener;
}

defineClass(375, 473, {}, CanvasImageBuilder_ImageListener);
_.dispose = function dispose_1(){
}
;
_.imageCreated = function imageCreated(image){
  $imageCreated_1(this._listener, image, this._imageName);
  this._listener = null;
}
;
var Lorg_glob3_mobile_generated_CanvasImageBuilder_1ImageListener_2_classLit = createForClass('org.glob3.mobile.generated', 'CanvasImageBuilder_ImageListener', 375);
function $_release(this$static){
  if (--this$static._referenceCounter == 0) {
    this$static.dispose();
    return true;
  }
  return false;
}

function $dispose_0(this$static){
  if (this$static._referenceCounter != 0) {
    throw new RuntimeException_0('Deleted RCObject with unreleased references!');
  }
}

function RCObject(){
  this._referenceCounter = 1;
}

defineClass(9, 1, {9:1});
_.dispose = function dispose_2(){
  $dispose_0(this);
}
;
_._referenceCounter = 0;
var Lorg_glob3_mobile_generated_RCObject_2_classLit = createForClass('org.glob3.mobile.generated', 'RCObject', 9);
function TileImageProvider(){
  RCObject.call(this);
}

defineClass(49, 9, $intern_41);
_.dispose = function dispose_3(){
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_TileImageProvider_2_classLit = createForClass('org.glob3.mobile.generated', 'TileImageProvider', 49);
defineClass(393, 49, $intern_41);
_.dispose = function dispose_4(){
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_CanvasTileImageProvider_2_classLit = createForClass('org.glob3.mobile.generated', 'CanvasTileImageProvider', 393);
function $setInfo(this$static, info){
  this$static._info.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
  $addAll(this$static._info, info);
}

function ChildRenderer(renderer){
  this._info = new ArrayList;
  this._renderer = renderer;
}

defineClass(36, 1, {36:1}, ChildRenderer);
var Lorg_glob3_mobile_generated_ChildRenderer_2_classLit = createForClass('org.glob3.mobile.generated', 'ChildRenderer', 36);
function $toID(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  $addFloat(isb, this$static._red);
  isb._string += '/';
  $addFloat(isb, this$static._green);
  isb._string += '/';
  $addFloat(isb, this$static._blue);
  isb._string += '/';
  $addFloat(isb, this$static._alpha);
  s = isb._string;
  return s;
}

function Color(red, green, blue, alpha_0){
  this._red = red;
  this._green = green;
  this._blue = blue;
  this._alpha = alpha_0;
}

function Color_0(that){
  this._red = that._red;
  this._green = that._green;
  this._blue = that._blue;
  this._alpha = that._alpha;
}

defineClass(25, 1, {}, Color, Color_0);
_.toString$ = function toString_27(){
  var isb;
  return isb = new StringBuilder_WebGL , isb._string += '[Color red=' , $addFloat(isb, this._red) , isb._string += ', green=' , $addFloat(isb, this._green) , isb._string += ', blue=' , $addFloat(isb, this._blue) , isb._string += ', alpha=' , $addFloat(isb, this._alpha) , isb._string += ']' , isb._string;
}
;
_._alpha = 0;
_._blue = 0;
_._green = 0;
_._red = 0;
var Lorg_glob3_mobile_generated_Color_2_classLit = createForClass('org.glob3.mobile.generated', 'Color', 25);
function GLFeature(group, id_0){
  RCObject.call(this);
  this._group = group;
  this._id = id_0;
  this._values = new GPUVariableValueSet;
}

defineClass(27, 9, {27:1, 9:1});
_.dispose = function dispose_5(){
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
_._id = 0;
var Lorg_glob3_mobile_generated_GLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'GLFeature', 27);
function PriorityGLFeature(g, id_0, priority){
  GLFeature.call(this, g, id_0);
  this._priority = priority;
}

defineClass(70, 27, $intern_42);
_.dispose = function dispose_6(){
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
_._priority = 0;
var Lorg_glob3_mobile_generated_PriorityGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'PriorityGLFeature', 70);
function $blendingOnGlobalGLState(this$static, state){
  if (this$static._blend) {
    state._blend = true;
    $setBlendFactors(state, this$static._sFactor, this$static._dFactor);
  }
   else {
    state._blend = false;
  }
}

function GLColorGroupFeature(id_0, priority, blend, sFactor, dFactor){
  PriorityGLFeature.call(this, ($clinit_GLFeatureGroupName() , COLOR_GROUP), id_0, priority);
  this._blend = blend;
  this._sFactor = sFactor;
  this._dFactor = dFactor;
}

defineClass(128, 70, $intern_42);
_.dispose = function dispose_7(){
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
_._blend = false;
_._dFactor = 0;
_._sFactor = 0;
var Lorg_glob3_mobile_generated_GLColorGroupFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'GLColorGroupFeature', 128);
function ColorGLFeature(colors, sFactor, dFactor){
  var value_0;
  GLColorGroupFeature.call(this, 7, 3, true, sFactor, dFactor);
  value_0 = new GPUAttributeValueVec4Float(colors, 4);
  $addAttributeValue(this._values, ($clinit_GPUAttributeKey() , COLOR), value_0);
}

defineClass(392, 128, $intern_42, ColorGLFeature);
_.applyOnGlobalGLState = function applyOnGlobalGLState(state){
  $blendingOnGlobalGLState(this, state);
}
;
_.dispose = function dispose_8(){
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_ColorGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'ColorGLFeature', 392);
function $add_7(this$static, child){
  $add_3(this$static._children, child);
  this$static._extent = null;
  this$static._rawExtent = null;
}

function $drawAt(this$static, left, top_0, canvas){
  var extent, extra;
  extent = $getExtent(this$static, canvas);
  $setFillColor(canvas, this$static._color);
  $fillRoundedRectangle(canvas, left + this$static._margin, top_0 + this$static._margin, extent._x - this$static._margin * 2, extent._y - this$static._margin * 2, this$static._cornerRadius);
  extra = this$static._margin + this$static._padding;
  $rawDrawAt(this$static, left + extra, top_0 + extra, this$static._rawExtent, canvas);
}

function $getExtent(this$static, canvas){
  var extra;
  if (!this$static._extent) {
    this$static._rawExtent = $calculateExtent(this$static, canvas);
    extra = (this$static._margin + this$static._padding) * 2;
    this$static._extent = new Vector2F(this$static._rawExtent._x + extra, this$static._rawExtent._y + extra);
  }
  return this$static._extent;
}

defineClass(398, 91, $intern_40);
_.drawAt = function drawAt(left, top_0, canvas){
  $drawAt(this, left, top_0, canvas);
}
;
_.getExtent = function getExtent(canvas){
  return $getExtent(this, canvas);
}
;
_._cornerRadius = 0;
_._margin = 0;
_._padding = 0;
var Lorg_glob3_mobile_generated_GroupCanvasElement_2_classLit = createForClass('org.glob3.mobile.generated', 'GroupCanvasElement', 398);
function $calculateExtent(this$static, canvas){
  var child, childExtent, childrenSize, height, i_0, width_0;
  width_0 = 0;
  height = 0;
  childrenSize = this$static._children.array.length;
  for (i_0 = 0; i_0 < childrenSize; i_0++) {
    child = dynamicCast($get_0(this$static._children, i_0), 91);
    childExtent = child.getExtent(canvas);
    childExtent._x > width_0 && (width_0 = childExtent._x);
    height += childExtent._y;
  }
  return new Vector2F(width_0, height);
}

function $rawDrawAt(this$static, left, top_0, extent, canvas){
  var child, childExtent, childrenSize, cursorLeft, cursorTop, halfWidth, i_0;
  halfWidth = extent._x / 2;
  cursorTop = top_0;
  childrenSize = this$static._children.array.length;
  for (i_0 = 0; i_0 < childrenSize; i_0++) {
    child = dynamicCast($get_0(this$static._children, i_0), 91);
    childExtent = child.getExtent(canvas);
    switch (this$static._elementAlign) {
      case 0:
        cursorLeft = left;
        break;
      case 2:
        cursorLeft = left + extent._x - childExtent._x;
        break;
      case 1:
      default:cursorLeft = left + halfWidth - childExtent._x / 2;
    }
    child.drawAt(cursorLeft, cursorTop, canvas);
    cursorTop += childExtent._y;
  }
}

function ColumnCanvasElement(color_0){
  this._children = new ArrayList;
  this._color = new Color_0(color_0);
  this._margin = 0;
  this._padding = 16;
  this._cornerRadius = 8;
  this._extent = null;
  this._rawExtent = null;
  this._elementAlign = 1;
}

defineClass(399, 398, $intern_40, ColumnCanvasElement);
_._elementAlign = 0;
var Lorg_glob3_mobile_generated_ColumnCanvasElement_2_classLit = createForClass('org.glob3.mobile.generated', 'ColumnCanvasElement', 399);
function $addChildRenderer(this$static, renderer){
  $add_3(this$static._renderers, renderer);
  this$static._renderersSize = this$static._renderers.array.length;
  !!this$static._context && renderer._renderer.initialize(this$static._context);
  renderer._renderer.setChangedRendererInfoListener(this$static, this$static._renderers.array.length - 1);
}

function $getInfo(this$static){
  var child, childInfo, i_0;
  this$static._info.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
  for (i_0 = 0; i_0 < this$static._renderersSize; i_0++) {
    child = dynamicCast($get_0(this$static._renderers, i_0), 36);
    childInfo = child._info;
    $addAll(this$static._info, childInfo);
  }
  return this$static._info;
}

function CompositeRenderer(){
  this._info = new ArrayList;
  this._renderers = new ArrayList;
  this._errors = new ArrayList;
  this._context = null;
  this._enable = true;
  this._renderersSize = 0;
  this._changedInfoListener = null;
}

defineClass(117, 1, {117:1, 135:1}, CompositeRenderer);
_.changedRendererInfo = function changedRendererInfo(rendererIdentifier, info){
  rendererIdentifier < this._renderersSize?$setInfo(dynamicCast($get_0(this._renderers, rendererIdentifier), 36), info):$logWarning(_instance_3, 'Child Render not found: %d', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(rendererIdentifier)]));
  !!this._changedInfoListener && this._changedInfoListener.changedRendererInfo(-1, $getInfo(this));
}
;
_.getPlanetRenderer = function getPlanetRenderer(){
  var i_0, planetRenderer, renderer, result;
  result = null;
  for (i_0 = 0; i_0 < this._renderersSize; i_0++) {
    renderer = dynamicCast($get_0(this._renderers, i_0), 36)._renderer;
    planetRenderer = renderer.getPlanetRenderer();
    !!planetRenderer && (!result?(result = planetRenderer):$logError(_instance_3, 'Inconsistency in Renderers: more than one PlanetRenderer', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [])));
  }
  return result;
}
;
_.getRenderState = function getRenderState(rc){
  var busyFlag, child, childErrors, childRenderState, childRenderStateType, errorFlag, i_0;
  this._errors.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
  busyFlag = false;
  errorFlag = false;
  for (i_0 = 0; i_0 < this._renderersSize; i_0++) {
    child = dynamicCast($get_0(this._renderers, i_0), 36)._renderer;
    if (child.isEnable()) {
      childRenderState = child.getRenderState(rc);
      childRenderStateType = childRenderState._type;
      if (childRenderStateType == 2) {
        errorFlag = true;
        childErrors = childRenderState._errors;
        $addAll(this._errors, childErrors);
      }
       else 
        childRenderStateType == 1 && (busyFlag = true);
    }
  }
  return errorFlag?($clinit_RenderState() , new RenderState(this._errors)):busyFlag?($clinit_RenderState() , $clinit_RenderState() , BUSY):($clinit_RenderState() , $clinit_RenderState() , READY);
}
;
_.getSurfaceElevationProvider = function getSurfaceElevationProvider(){
  var childSurfaceElevationProvider, i_0, renderer, result;
  result = null;
  for (i_0 = 0; i_0 < this._renderersSize; i_0++) {
    renderer = dynamicCast($get_0(this._renderers, i_0), 36)._renderer;
    childSurfaceElevationProvider = renderer.getSurfaceElevationProvider();
    !!childSurfaceElevationProvider && (!result?(result = childSurfaceElevationProvider):$logError(_instance_3, 'Inconsistency in Renderers: more than one SurfaceElevationProvider', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [])));
  }
  return result;
}
;
_.initialize = function initialize_1(context){
  var i_0;
  this._context = context;
  for (i_0 = 0; i_0 < this._renderersSize; i_0++) {
    dynamicCast($get_0(this._renderers, i_0), 36)._renderer.initialize(context);
  }
}
;
_.isEnable = function isEnable(){
  var i_0;
  if (!this._enable) {
    return false;
  }
  for (i_0 = 0; i_0 < this._renderersSize; i_0++) {
    if (dynamicCast($get_0(this._renderers, i_0), 36)._renderer.isEnable()) {
      return true;
    }
  }
  return false;
}
;
_.isPlanetRenderer = function isPlanetRenderer(){
  return false;
}
;
_.onResizeViewportEvent = function onResizeViewportEvent_1(ec, width_0, height){
  var i_0;
  for (i_0 = this._renderersSize - 1; i_0 >= 0; i_0--) {
    dynamicCast($get_0(this._renderers, i_0), 36)._renderer.onResizeViewportEvent(ec, width_0, height);
  }
}
;
_.onTouchEvent_0 = function onTouchEvent_3(ec, touchEvent){
  var i_0, renderer;
  for (i_0 = this._renderersSize - 1; i_0 >= 0; i_0--) {
    renderer = dynamicCast($get_0(this._renderers, i_0), 36)._renderer;
    if (renderer.isEnable()) {
      if (renderer.onTouchEvent_0(ec, touchEvent)) {
        return true;
      }
    }
  }
  return false;
}
;
_.render = function render_5(rc, glState){
  var i_0, renderer;
  for (i_0 = 0; i_0 < this._renderersSize; i_0++) {
    renderer = dynamicCast($get_0(this._renderers, i_0), 36)._renderer;
    renderer.isEnable() && renderer.render(rc, glState);
  }
}
;
_.setChangedRendererInfoListener = function setChangedRendererInfoListener(changedInfoListener, rendererIdentifier){
  !!this._changedInfoListener && $logError(_instance_3, 'Changed Renderer Info Listener of CompositeRenderer already set', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  this._changedInfoListener = changedInfoListener;
  !!this._changedInfoListener && this._changedInfoListener.changedRendererInfo(-1, $getInfo(this));
}
;
_.start_2 = function start_6(rc){
  var i_0;
  for (i_0 = 0; i_0 < this._renderersSize; i_0++) {
    dynamicCast($get_0(this._renderers, i_0), 36)._renderer.start_2(rc);
  }
}
;
_.stop_1 = function stop_4(rc){
  var i_0;
  for (i_0 = 0; i_0 < this._renderersSize; i_0++) {
    dynamicCast($get_0(this._renderers, i_0), 36)._renderer.stop_1(rc);
  }
}
;
_._enable = false;
_._renderersSize = 0;
var Lorg_glob3_mobile_generated_CompositeRenderer_2_classLit = createForClass('org.glob3.mobile.generated', 'CompositeRenderer', 117);
function $clinit_TileImageContribution(){
  $clinit_TileImageContribution = emptyMethod;
  FULL_COVERAGE_OPAQUE = new TileImageContribution_0(false, 1);
}

function TileImageContribution(sector, isTransparent, alpha_0){
  $clinit_TileImageContribution();
  RCObject.call(this);
  this._isFullCoverage = false;
  this._sector = new Sector_0(sector);
  this._isTransparent = isTransparent;
  this._alpha = alpha_0;
}

function TileImageContribution_0(isTransparent, alpha_0){
  RCObject.call(this);
  this._isFullCoverage = true;
  this._sector = new Sector_0(($clinit_Sector() , NAN_SECTOR));
  this._isTransparent = isTransparent;
  this._alpha = alpha_0;
}

function fullCoverageTransparent(alpha_0){
  $clinit_TileImageContribution();
  if (alpha_0 <= 0.01) {
    return null;
  }
  if (!_lastFullCoverageTransparent || _lastFullCoverageTransparent._alpha != alpha_0) {
    !!_lastFullCoverageTransparent && $_release(_lastFullCoverageTransparent);
    _lastFullCoverageTransparent = new TileImageContribution_0(true, alpha_0);
  }
  ++_lastFullCoverageTransparent._referenceCounter;
  return _lastFullCoverageTransparent;
}

function partialCoverageTransparent(sector, alpha_0){
  $clinit_TileImageContribution();
  return alpha_0 <= 0.01?null:new TileImageContribution(sector, true, alpha_0);
}

function releaseContribution(contribution){
  $clinit_TileImageContribution();
  !!contribution && $_release(contribution);
}

function retainContribution(contribution){
  $clinit_TileImageContribution();
  !!contribution && ++contribution._referenceCounter;
}

defineClass(105, 9, {9:1}, TileImageContribution, TileImageContribution_0);
_.dispose = function dispose_9(){
  $dispose_0(this);
}
;
_._alpha = 0;
_._isFullCoverage = false;
_._isTransparent = false;
var FULL_COVERAGE_OPAQUE, _lastFullCoverageTransparent = null;
var Lorg_glob3_mobile_generated_TileImageContribution_2_classLit = createForClass('org.glob3.mobile.generated', 'TileImageContribution', 105);
function CompositeTileImageContribution(contributions, transparent){
  TileImageContribution_0.call(this, transparent, 1);
  this._contributions = contributions;
}

function create_2(contributions){
  $clinit_TileImageContribution();
  var firsChild;
  if (contributions.array.length == 0) {
    return null;
  }
  firsChild = (checkElementIndex(0, contributions.array.length) , dynamicCast(contributions.array[0], 80));
  return new CompositeTileImageContribution(contributions, firsChild._contribution._alpha >= 0.99);
}

defineClass(155, 105, {155:1, 9:1}, CompositeTileImageContribution);
_.dispose = function dispose_10(){
  var contribution, contributionsSize, i_0;
  contributionsSize = this._contributions.array.length;
  for (i_0 = 0; i_0 < contributionsSize; i_0++) {
    contribution = dynamicCast($get_0(this._contributions, i_0), 80);
    releaseContribution(contribution._contribution);
  }
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_CompositeTileImageContribution_2_classLit = createForClass('org.glob3.mobile.generated', 'CompositeTileImageContribution', 155);
function CompositeTileImageContribution$ChildContribution(childIndex, contribution){
  this._childIndex = childIndex;
  this._contribution = contribution;
}

defineClass(80, 1, {80:1}, CompositeTileImageContribution$ChildContribution);
_._childIndex = 0;
var Lorg_glob3_mobile_generated_CompositeTileImageContribution$ChildContribution_2_classLit = createForClass('org.glob3.mobile.generated', 'CompositeTileImageContribution/ChildContribution', 80);
function $cancelChildren(this$static, tileId, compositeContribution){
  var child, contributionsSize, i_0, i0, indexes;
  contributionsSize = compositeContribution._contributions.array.length;
  indexes = initDim(I_classLit, $intern_29, 0, contributionsSize, 7, 1);
  for (i0 = 0; i0 < contributionsSize; i0++) {
    indexes[i0] = dynamicCast($get_0(compositeContribution._contributions, i0), 80)._childIndex;
  }
  for (i_0 = 0; i_0 < contributionsSize; i_0++) {
    child = dynamicCast($get_0(this$static._children, indexes[i_0]), 49);
    child.cancel_0(tileId);
  }
}

function $composerDone(this$static, composer){
  $removeStringValue(this$static._composers, composer._tileId);
  $_release(composer);
}

function CompositeTileImageProvider(){
  TileImageProvider.call(this);
  new Color(0, 0, 0, 0);
  this._children = new ArrayList;
  this._composers = new HashMap;
  this._childrenSize = 0;
}

defineClass(394, 393, $intern_41, CompositeTileImageProvider);
_.cancel_0 = function cancel_1(tileId){
  var composer;
  composer = dynamicCast($removeStringValue(this._composers, tileId), 153);
  !!composer && (composer._canceled = true , $cancelChildren(composer._compositeTileImageProvider, tileId, composer._compositeContribution));
}
;
_.contribution = function contribution_0(tile){
  var child, childContribution, childrenContributions, childrenContributionsSize, i_0, j, previousContribution;
  childrenContributions = new ArrayList;
  for (i_0 = 0; i_0 < this._childrenSize; i_0++) {
    child = dynamicCast($get_0(this._children, i_0), 49);
    childContribution = child.contribution(tile);
    if (childContribution) {
      childrenContributionsSize = childrenContributions.array.length;
      if (childrenContributionsSize > 0 && childContribution._isFullCoverage && !childContribution._isTransparent && childContribution._alpha >= 0.99) {
        for (j = 0; j < childrenContributionsSize; j++) {
          previousContribution = (checkElementIndex(j, childrenContributions.array.length) , dynamicCast(childrenContributions.array[j], 80));
          releaseContribution(previousContribution._contribution);
        }
        childrenContributions.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
      }
      $add_3(childrenContributions, new CompositeTileImageContribution$ChildContribution(i_0, childContribution));
    }
  }
  return create_2(childrenContributions);
}
;
_.create_0 = function create_3(tile, contribution, resolution, tileDownloadPriority, logDownloadActivity, listener, deleteListener, frameTasksExecutor){
  var child, childContribution, composer, compositeContribution, contributionsSize, i_0, tileId;
  compositeContribution = dynamicCast(contribution, 155);
  tileId = tile._id;
  composer = new CompositeTileImageProvider$Composer(resolution._x, resolution._y, this, tileId, tile._sector, listener, deleteListener, compositeContribution, frameTasksExecutor);
  $putStringValue(this._composers, tileId, composer);
  contributionsSize = compositeContribution._contributions.array.length;
  for (i_0 = 0; i_0 < contributionsSize; i_0++) {
    childContribution = dynamicCast($get_0(compositeContribution._contributions, i_0), 80);
    child = dynamicCast($get_0(this._children, childContribution._childIndex), 49);
    retainContribution(childContribution._contribution);
    child.create_0(tile, childContribution._contribution, resolution, tileDownloadPriority, logDownloadActivity, new CompositeTileImageProvider$ChildTileImageListener(composer, i_0), true, frameTasksExecutor);
  }
}
;
_.dispose = function dispose_11(){
  var child, i_0;
  for (i_0 = 0; i_0 < this._childrenSize; i_0++) {
    child = dynamicCast($get_0(this._children, i_0), 49);
    $_release(child);
  }
  $dispose_0(this);
}
;
_._childrenSize = 0;
var Lorg_glob3_mobile_generated_CompositeTileImageProvider_2_classLit = createForClass('org.glob3.mobile.generated', 'CompositeTileImageProvider', 394);
function CompositeTileImageProvider$ChildResult(isError, isCanceled, image, imageId, contribution, error){
  this._isError = isError;
  this._isCanceled = isCanceled;
  this._image = image;
  this._imageId = imageId;
  this._contribution = contribution;
  this._error = error;
}

defineClass(60, 1, {60:1}, CompositeTileImageProvider$ChildResult);
_._isCanceled = false;
_._isError = false;
var Lorg_glob3_mobile_generated_CompositeTileImageProvider$ChildResult_2_classLit = createForClass('org.glob3.mobile.generated', 'CompositeTileImageProvider/ChildResult', 60);
defineClass(485, 1, {});
_.dispose = function dispose_12(){
}
;
var Lorg_glob3_mobile_generated_TileImageListener_2_classLit = createForClass('org.glob3.mobile.generated', 'TileImageListener', 485);
function CompositeTileImageProvider$ChildTileImageListener(composer, index_0){
  this._composer = composer;
  this._index = index_0;
  ++this._composer._referenceCounter;
}

defineClass(397, 485, {}, CompositeTileImageProvider$ChildTileImageListener);
_.dispose = function dispose_13(){
  $_release(this._composer);
}
;
_.imageCreated_0 = function imageCreated_0(tileId, image, imageId, contribution){
  $imageCreated(this._composer, image, imageId, contribution, this._index);
}
;
_.imageCreationCanceled = function imageCreationCanceled(tileId){
  $imageCreationCanceled(this._composer, this._index);
}
;
_.imageCreationError = function imageCreationError(tileId, error){
  $imageCreationError(this._composer, error, this._index);
}
;
_._index = 0;
var Lorg_glob3_mobile_generated_CompositeTileImageProvider$ChildTileImageListener_2_classLit = createForClass('org.glob3.mobile.generated', 'CompositeTileImageProvider/ChildTileImageListener', 397);
function $cleanUp(this$static){
  if (this$static._deleteListener) {
    !!this$static._listener && this$static._listener.dispose();
    this$static._listener = null;
  }
  $composerDone(this$static._compositeTileImageProvider, this$static);
}

function $done(this$static){
  var childResult, composedError, contributionSector, i_0, simpleCase, singleResult;
  if (this$static._canceled) {
    $cleanUp(this$static);
    return;
  }
  if (this$static._contributionsSize == 1) {
    singleResult = dynamicCast($get_0(this$static._results, 0), 60);
    contributionSector = !singleResult._contribution?null:singleResult._contribution._sector;
    simpleCase = !contributionSector || contributionSector._lower._latitude._degrees != contributionSector._lower._latitude._degrees || $isEquals_3(this$static._tileSector, contributionSector);
  }
   else {
    simpleCase = false;
  }
  if (simpleCase) {
    singleResult = dynamicCast($get_0(this$static._results, 0), 60);
    if (singleResult._isError) {
      this$static._listener.imageCreationError(this$static._tileId, singleResult._error);
    }
     else if (singleResult._isCanceled) {
      this$static._listener.imageCreationCanceled(this$static._tileId);
    }
     else {
      retainContribution(singleResult._contribution);
      this$static._listener.imageCreated_0(singleResult._imageId, new Image_WebGL(singleResult._image._imgObject), singleResult._imageId, singleResult._contribution);
    }
    $cleanUp(this$static);
  }
   else {
    if (this$static._anyError) {
      composedError = '';
      for (i_0 = 0; i_0 < this$static._contributionsSize; i_0++) {
        childResult = dynamicCast($get_0(this$static._results, i_0), 60);
        childResult._isError && (composedError += childResult._error + ' ');
      }
      this$static._listener.imageCreationError(this$static._tileId, composedError);
      $cleanUp(this$static);
    }
     else if (this$static._anyCancelation) {
      this$static._listener.imageCreationCanceled(this$static._tileId);
      $cleanUp(this$static);
    }
     else {
      $addPreRenderTask(this$static._frameTasksExecutor, new CompositeTileImageProvider$ComposerFrameTask(this$static));
    }
  }
}

function $imageCreated(this$static, image, imageId, contribution, index_0){
  $set(this$static._results, index_0, new CompositeTileImageProvider$ChildResult(false, false, image, imageId, contribution, ''));
  ++this$static._stepsDone;
  this$static._stepsDone == this$static._contributionsSize && $done(this$static);
}

function $imageCreated_0(this$static, image){
  var compositeContribution;
  compositeContribution = this$static._compositeContribution;
  this$static._compositeContribution = null;
  this$static._listener.imageCreated_0(this$static._tileId, image, this$static._imageId, compositeContribution);
  $cleanUp(this$static);
}

function $imageCreationCanceled(this$static, index_0){
  $set(this$static._results, index_0, new CompositeTileImageProvider$ChildResult(false, true, null, '', null, ''));
  this$static._anyCancelation = true;
  ++this$static._stepsDone;
  this$static._stepsDone == this$static._contributionsSize && $done(this$static);
}

function $imageCreationError(this$static, error, index_0){
  $set(this$static._results, index_0, new CompositeTileImageProvider$ChildResult(true, false, null, '', null, error));
  this$static._anyError = true;
  ++this$static._stepsDone;
  this$static._stepsDone == this$static._contributionsSize && $done(this$static);
}

function $mixResult(this$static){
  var alpha_0, canvas, destRect, i_0, image, imageId, imageSector, result, srcRect, visibleContributionSector;
  if (this$static._canceled) {
    $cleanUp(this$static);
    return;
  }
  canvas = new Canvas_WebGL;
  $initialize_2(canvas, this$static._width, this$static._height);
  imageId = new StringBuilder_WebGL;
  for (i_0 = 0; i_0 < this$static._contributionsSize; i_0++) {
    result = dynamicCast($get_0(this$static._results, i_0), 60);
    $addString(imageId, result._imageId);
    imageId._string += '|';
    image = result._image;
    alpha_0 = result._contribution._alpha;
    if (result._contribution._isFullCoverage) {
      if (result._contribution._alpha >= 0.99) {
        $drawImage(canvas, image, this$static._width, this$static._height);
      }
       else {
        imageId._string += alpha_0;
        imageId._string += '|';
        $drawImage_0(canvas, image, this$static._width, this$static._height, alpha_0);
      }
    }
     else {
      imageSector = result._contribution._sector;
      visibleContributionSector = $intersection(imageSector, this$static._tileSector);
      $addString(imageId, $id_0(visibleContributionSector));
      imageId._string += '|';
      srcRect = calculateInnerRectangleFromSector($getWidth(image), $getHeight(image), imageSector, visibleContributionSector);
      destRect = calculateInnerRectangleFromSector(this$static._width, this$static._height, this$static._tileSector, visibleContributionSector);
      $addString(imageId, $id(destRect));
      imageId._string += '|';
      $drawImage_1(canvas, image, srcRect._x, srcRect._y, srcRect._width, srcRect._height, destRect._x, destRect._y, destRect._width, destRect._height, alpha_0);
    }
  }
  this$static._imageId = imageId._string;
  $createImage(canvas, new CompositeTileImageProvider$ComposerImageListener(this$static));
}

function CompositeTileImageProvider$Composer(width_0, height, compositeTileImageProvider, tileId, tileSector, listener, deleteListener, compositeContribution, frameTasksExecutor){
  var i_0;
  RCObject.call(this);
  this._results = new ArrayList;
  this._width = width_0;
  this._height = height;
  this._compositeTileImageProvider = compositeTileImageProvider;
  this._tileId = tileId;
  this._listener = listener;
  this._deleteListener = deleteListener;
  this._compositeContribution = compositeContribution;
  this._contributionsSize = compositeContribution._contributions.array.length;
  this._frameTasksExecutor = frameTasksExecutor;
  this._stepsDone = 0;
  this._anyError = false;
  this._anyCancelation = false;
  this._canceled = false;
  this._tileSector = new Sector_0(tileSector);
  ++this._compositeTileImageProvider._referenceCounter;
  for (i_0 = 0; i_0 < this._contributionsSize; i_0++) {
    $add_3(this._results, null);
  }
}

defineClass(153, 9, {153:1, 9:1}, CompositeTileImageProvider$Composer);
_.dispose = function dispose_14(){
  var i_0, result;
  for (i_0 = 0; i_0 < this._contributionsSize; i_0++) {
    result = dynamicCast($get_0(this._results, i_0), 60);
    !!result && releaseContribution(result._contribution);
  }
  releaseContribution(this._compositeContribution);
  $_release(this._compositeTileImageProvider);
  $dispose_0(this);
}
;
_._anyCancelation = false;
_._anyError = false;
_._canceled = false;
_._contributionsSize = 0;
_._deleteListener = false;
_._height = 0;
_._stepsDone = 0;
_._width = 0;
var Lorg_glob3_mobile_generated_CompositeTileImageProvider$Composer_2_classLit = createForClass('org.glob3.mobile.generated', 'CompositeTileImageProvider/Composer', 153);
defineClass(103, 1, {103:1});
_.dispose = function dispose_15(){
}
;
var Lorg_glob3_mobile_generated_FrameTask_2_classLit = createForClass('org.glob3.mobile.generated', 'FrameTask', 103);
function CompositeTileImageProvider$ComposerFrameTask(composer){
  this._composer = composer;
  ++this._composer._referenceCounter;
}

defineClass(396, 103, {103:1}, CompositeTileImageProvider$ComposerFrameTask);
_.dispose = function dispose_16(){
  $_release(this._composer);
}
;
_.execute_1 = function execute_5(rc){
  $mixResult(this._composer);
}
;
_.isCanceled = function isCanceled_0(rc){
  return false;
}
;
var Lorg_glob3_mobile_generated_CompositeTileImageProvider$ComposerFrameTask_2_classLit = createForClass('org.glob3.mobile.generated', 'CompositeTileImageProvider/ComposerFrameTask', 396);
function CompositeTileImageProvider$ComposerImageListener(composer){
  this._composer = composer;
  ++this._composer._referenceCounter;
}

defineClass(395, 473, {}, CompositeTileImageProvider$ComposerImageListener);
_.dispose = function dispose_17(){
  $_release(this._composer);
}
;
_.imageCreated = function imageCreated_1(image){
  $imageCreated_0(this._composer, image);
}
;
var Lorg_glob3_mobile_generated_CompositeTileImageProvider$ComposerImageListener_2_classLit = createForClass('org.glob3.mobile.generated', 'CompositeTileImageProvider/ComposerImageListener', 395);
function $applyRotation(this$static, m){
  return new CoordinateSystem_0($transformedBy_0(this$static._x, m, 1), $transformedBy_0(this$static._y, m, 1), $transformedBy_0(this$static._z, m, 1), this$static._origin);
}

function $applyTaitBryanAngles(this$static, heading, pitch, roll){
  var hm, isHeadingZero, isPitchZero, isRollZero, pm, rm, u, up, uppp, v, vp, vpp, w, wpp, wppp;
  u = this$static._x;
  v = this$static._y;
  w = this$static._z;
  isHeadingZero = heading._degrees == 0;
  hm = isHeadingZero?new MutableMatrix44D_2:createGeneralRotationMatrix(heading, w, ($clinit_Vector3D() , zero));
  up = isHeadingZero?u:new Vector3D(u._x * hm._m00 + u._y * hm._m01 + u._z * hm._m02 + hm._m03, u._x * hm._m10 + u._y * hm._m11 + u._z * hm._m12 + hm._m13, u._x * hm._m20 + u._y * hm._m21 + u._z * hm._m22 + hm._m23);
  vp = isHeadingZero?v:new Vector3D(v._x * hm._m00 + v._y * hm._m01 + v._z * hm._m02 + hm._m03, v._x * hm._m10 + v._y * hm._m11 + v._z * hm._m12 + hm._m13, v._x * hm._m20 + v._y * hm._m21 + v._z * hm._m22 + hm._m23);
  isPitchZero = pitch._degrees == 0;
  pm = isPitchZero?new MutableMatrix44D_2:createGeneralRotationMatrix(pitch, up, ($clinit_Vector3D() , zero));
  vpp = isPitchZero?vp:new Vector3D(vp._x * pm._m00 + vp._y * pm._m01 + vp._z * pm._m02 + pm._m03, vp._x * pm._m10 + vp._y * pm._m11 + vp._z * pm._m12 + pm._m13, vp._x * pm._m20 + vp._y * pm._m21 + vp._z * pm._m22 + pm._m23);
  wpp = isPitchZero?w:new Vector3D(w._x * pm._m00 + w._y * pm._m01 + w._z * pm._m02 + pm._m03, w._x * pm._m10 + w._y * pm._m11 + w._z * pm._m12 + pm._m13, w._x * pm._m20 + w._y * pm._m21 + w._z * pm._m22 + pm._m23);
  isRollZero = roll._degrees == 0;
  rm = isRollZero?new MutableMatrix44D_2:createGeneralRotationMatrix(roll, vpp, ($clinit_Vector3D() , zero));
  uppp = isRollZero?up:new Vector3D(up._x * rm._m00 + up._y * rm._m01 + up._z * rm._m02 + rm._m03, up._x * rm._m10 + up._y * rm._m11 + up._z * rm._m12 + rm._m13, up._x * rm._m20 + up._y * rm._m21 + up._z * rm._m22 + rm._m23);
  wppp = isRollZero?wpp:new Vector3D(wpp._x * rm._m00 + wpp._y * rm._m01 + wpp._z * rm._m02 + rm._m03, wpp._x * rm._m10 + wpp._y * rm._m11 + wpp._z * rm._m12 + rm._m13, wpp._x * rm._m20 + wpp._y * rm._m21 + wpp._z * rm._m22 + rm._m23);
  return new CoordinateSystem_0(uppp, vpp, wppp, this$static._origin);
}

function $copyValueOfRotationMatrix(this$static, m){
  $setValue(m, this$static._x._x, this$static._x._y, this$static._x._z, this$static._y._x, this$static._y._y, this$static._y._z, this$static._z._x, this$static._z._y, this$static._z._z);
}

function $getTaitBryanAngles(this$static, global){
  var heading, heading0, pitch, pitch0, roll, roll0, u, up, v, vp, vppp, w, wpp, wppp, x_0;
  u = global._x;
  v = global._y;
  w = global._z;
  vppp = this$static._y;
  wppp = this$static._z;
  x_0 = vppp._x * w._x + vppp._y * w._y + vppp._z * w._z;
  if (x_0 < -0.99999 && x_0 > -1.000001) {
    pitch0 = new Angle(-90, $intern_38);
    roll0 = new Angle(0, 0);
    heading0 = $signedAngleBetween(v, wppp, w);
    return new TaitBryanAngles(heading0, pitch0, roll0);
  }
   else if (x_0 > 0.99999 && x_0 < 1.000001) {
    pitch0 = new Angle(90, $intern_43);
    roll0 = new Angle(0, 0);
    heading0 = $sub($signedAngleBetween(v, wppp, w), new Angle(180, $intern_37));
    return new TaitBryanAngles(heading0, pitch0, roll0);
  }
  up = new Vector3D(vppp._y * w._z - vppp._z * w._y, vppp._z * w._x - vppp._x * w._z, vppp._x * w._y - vppp._y * w._x);
  vp = new Vector3D(w._y * up._z - w._z * up._y, w._z * up._x - w._x * up._z, w._x * up._y - w._y * up._x);
  wpp = new Vector3D(up._y * vppp._z - up._z * vppp._y, up._z * vppp._x - up._x * vppp._z, up._x * vppp._y - up._y * vppp._x);
  heading = $signedAngleBetween(u, up, w);
  pitch = $signedAngleBetween(vp, vppp, up);
  roll = $signedAngleBetween(wpp, wppp, vppp);
  return new TaitBryanAngles(heading, pitch, roll);
}

function CoordinateSystem(viewDirection, up, origin){
  this._x = new Vector3D_0($normalized_0(new Vector3D(viewDirection._y * up._z - viewDirection._z * up._y, viewDirection._z * up._x - viewDirection._x * up._z, viewDirection._x * up._y - viewDirection._y * up._x)));
  this._y = new Vector3D_0($normalized_0(viewDirection));
  this._z = new Vector3D_0($normalized_0(up));
  this._origin = new Vector3D_0(origin);
  if (!checkConsistency(this._x, this._y, this._z)) {
    $logError(_instance_3, 'Inconsistent CoordinateSystem created.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    throw new RuntimeException_0('Inconsistent CoordinateSystem created.');
  }
}

function CoordinateSystem_0(x_0, y_0, z_0, origin){
  this._x = new Vector3D_0($normalized_0(x_0));
  this._y = new Vector3D_0($normalized_0(y_0));
  this._z = new Vector3D_0($normalized_0(z_0));
  this._origin = new Vector3D_0(origin);
  if (!checkConsistency(x_0, y_0, z_0)) {
    $logError(_instance_3, 'Inconsistent CoordinateSystem created.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    throw new RuntimeException_0('Inconsistent CoordinateSystem created.');
  }
}

function checkConsistency(x_0, y_0, z_0){
  if (x_0._x != x_0._x || x_0._y != x_0._y || x_0._z != x_0._z || y_0._x != y_0._x || y_0._y != y_0._y || y_0._z != y_0._z || z_0._x != z_0._x || z_0._y != z_0._y || z_0._z != z_0._z) {
    return false;
  }
  return $abs(x_0._x * y_0._x + x_0._y * y_0._y + x_0._z * y_0._z) < $intern_44 && $abs(x_0._x * z_0._x + x_0._y * z_0._y + x_0._z * z_0._z) < $intern_44 && $abs(y_0._x * z_0._x + y_0._y * z_0._y + y_0._z * z_0._z) < $intern_44;
}

defineClass(58, 1, {}, CoordinateSystem, CoordinateSystem_0);
var Lorg_glob3_mobile_generated_CoordinateSystem_2_classLit = createForClass('org.glob3.mobile.generated', 'CoordinateSystem', 58);
function $imageCreated_1(this$static, image, imageName){
  $setDefaultBackGroundImage(this$static._defaultTileTesturizer, image);
  $setDefaultBackGroundImageName(this$static._defaultTileTesturizer, imageName);
  this$static._defaultTileTesturizer._defaultBackGroundImageLoaded = true;
  $logInfo(_instance_3, 'Default Background Image loaded...', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
}

function DTT_IImageBuilderListener(defaultTileTesturizer){
  this._defaultTileTesturizer = defaultTileTesturizer;
}

defineClass(369, 1, {}, DTT_IImageBuilderListener);
var Lorg_glob3_mobile_generated_DTT_1IImageBuilderListener_2_classLit = createForClass('org.glob3.mobile.generated', 'DTT_IImageBuilderListener', 369);
defineClass(486, 1, {});
var Lorg_glob3_mobile_generated_LazyTextureMappingInitializer_2_classLit = createForClass('org.glob3.mobile.generated', 'LazyTextureMappingInitializer', 486);
function $getScale(this$static){
  return new Vector2F(this$static._scaleU, this$static._scaleV);
}

function $getTranslation(this$static){
  return new Vector2F(this$static._translationU, this$static._translationV);
}

function $initialize_0(this$static){
  var lowerTextCoordUV, tileSector, upperTextCoordUV;
  if (this$static._tile != this$static._ancestor) {
    tileSector = this$static._tile._sector;
    lowerTextCoordUV = $getTextCoord(this$static._ancestor, tileSector._lower);
    upperTextCoordUV = $getTextCoord(this$static._ancestor, tileSector._upper);
    this$static._translationU = lowerTextCoordUV._x;
    this$static._translationV = upperTextCoordUV._y;
    this$static._scaleU = upperTextCoordUV._x - lowerTextCoordUV._x;
    this$static._scaleV = lowerTextCoordUV._y - upperTextCoordUV._y;
  }
}

function DTT_LTMInitializer(tile, ancestor){
  this._tile = tile;
  this._ancestor = ancestor;
  this._translationU = 0;
  this._translationV = 0;
  this._scaleU = 1;
  this._scaleV = 1;
}

defineClass(208, 486, {}, DTT_LTMInitializer);
_._scaleU = 0;
_._scaleV = 0;
_._translationU = 0;
_._translationV = 0;
var Lorg_glob3_mobile_generated_DTT_1LTMInitializer_2_classLit = createForClass('org.glob3.mobile.generated', 'DTT_LTMInitializer', 208);
function DTT_NotFullProviderImageListener(builder, imageId){
  this._builder = builder;
  this._imageId = imageId;
  ++this._builder._referenceCounter;
}

defineClass(414, 473, {}, DTT_NotFullProviderImageListener);
_.dispose = function dispose_18(){
  !!this._builder && $_release(this._builder);
}
;
_.imageCreated = function imageCreated_2(image){
  $imageCreated_2(this._builder, image, this._imageId, ($clinit_TileImageContribution() , ++FULL_COVERAGE_OPAQUE._referenceCounter , $clinit_TileImageContribution() , FULL_COVERAGE_OPAQUE));
}
;
var Lorg_glob3_mobile_generated_DTT_1NotFullProviderImageListener_2_classLit = createForClass('org.glob3.mobile.generated', 'DTT_NotFullProviderImageListener', 414);
function DTT_TileImageListener(builder, tile, tileTextureResolution, backGroundTileImage, backGroundTileImageName){
  this._builder = builder;
  this._tileSector = new Sector_0(tile._sector);
  this._tileTextureResolution = tileTextureResolution;
  this._backGroundTileImage = backGroundTileImage;
  this._backGroundTileImageName = backGroundTileImageName;
  ++this._builder._referenceCounter;
}

defineClass(391, 485, {}, DTT_TileImageListener);
_.dispose = function dispose_19(){
  !!this._builder && $_release(this._builder);
}
;
_.imageCreated_0 = function imageCreated_3(tileId, image, imageId, contribution){
  var alpha_0, auxImageId, canvas, destRect, height, imageSector, srcRect, visibleContributionSector, width_0;
  if (contribution._isFullCoverage && !contribution._isTransparent && contribution._alpha >= 0.99) {
    $imageCreated_2(this._builder, image, imageId, contribution);
  }
   else {
    auxImageId = new StringBuilder_WebGL;
    canvas = new Canvas_WebGL;
    width_0 = this._tileTextureResolution._x;
    height = this._tileTextureResolution._y;
    $initialize_2(canvas, width_0, height);
    if (this._backGroundTileImage) {
      $addString(auxImageId, this._backGroundTileImageName);
      auxImageId._string += '|';
      $drawImage(canvas, this._backGroundTileImage, width_0, height);
    }
    auxImageId._string += imageId;
    auxImageId._string += '|';
    alpha_0 = contribution._alpha;
    if (contribution._isFullCoverage) {
      auxImageId._string += alpha_0;
      auxImageId._string += '|';
      canvas._canvasWidth > 0 && canvas._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
      $_drawImage(canvas, image, 0, 0, width_0, height, alpha_0);
    }
     else {
      imageSector = contribution._sector;
      visibleContributionSector = $intersection(imageSector, this._tileSector);
      $addString(auxImageId, $id_0(visibleContributionSector));
      auxImageId._string += '|';
      srcRect = calculateInnerRectangleFromSector($getWidth(image), $getHeight(image), imageSector, visibleContributionSector);
      destRect = calculateInnerRectangleFromSector(width_0, height, this._tileSector, visibleContributionSector);
      $addString(auxImageId, $id(destRect));
      auxImageId._string += '|';
      $drawImage_1(canvas, image, srcRect._x, srcRect._y, srcRect._width, srcRect._height, destRect._x, destRect._y, destRect._width, destRect._height, alpha_0);
    }
    $createImage(canvas, new DTT_NotFullProviderImageListener(this._builder, auxImageId._string));
    $clinit_TileImageContribution();
    !!contribution && $_release(contribution);
  }
}
;
_.imageCreationCanceled = function imageCreationCanceled_0(tileId){
}
;
_.imageCreationError = function imageCreationError_0(tileId, error){
  $logError(_instance_3, '%s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [error]));
}
;
var Lorg_glob3_mobile_generated_DTT_1TileImageListener_2_classLit = createForClass('org.glob3.mobile.generated', 'DTT_TileImageListener', 391);
function $cancel_0(this$static, cleanTile){
  this$static._texturedMesh = null;
  cleanTile && (this$static._tile = null);
  if (!this$static._canceled) {
    this$static._canceled = true;
    this$static._tileImageProvider.cancel_0(this$static._tileId);
  }
}

function $imageCreated_2(this$static, image, imageId, contribution){
  var glTextureId;
  contribution._isFullCoverage && !contribution._isTransparent && contribution._alpha >= 0.99 || $logWarning(_instance_3, "Contribution isn't full covearge and opaque before to upload tuxtuer", initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  !this$static._canceled && !!this$static._tile && !!this$static._texturedMesh && (glTextureId = $getTextureIDReference(this$static._texturesHandler, image, _rgba, imageId, this$static._generateMipmap) , $setGLTextureIdForLevel(this$static._texturedMesh, 0, glTextureId) || $releaseGLTextureId_1(glTextureId._texHandler, glTextureId._id) , true) && $setTextureSolved(this$static._tile, true);
  $clinit_TileImageContribution();
  !!contribution && $_release(contribution);
}

function $start(this$static){
  var contribution;
  if (!this$static._canceled) {
    contribution = this$static._tileImageProvider.contribution(this$static._tile);
    !contribution?!!this$static._tile && $imageCreated_2(this$static, new Image_WebGL(this$static._backGroundTileImage._imgObject), this$static._backGroundTileImageName, ($clinit_TileImageContribution() , ++FULL_COVERAGE_OPAQUE._referenceCounter , $clinit_TileImageContribution() , FULL_COVERAGE_OPAQUE)):this$static._tileImageProvider.create_0(this$static._tile, contribution, this$static._tileTextureResolution, this$static._tileDownloadPriority, this$static._logTilesPetitions, new DTT_TileImageListener(this$static, this$static._tile, this$static._tileTextureResolution, this$static._backGroundTileImage, this$static._backGroundTileImageName), true, this$static._frameTasksExecutor);
  }
}

function DTT_TileTextureBuilder(rc, layerTilesRenderParameters, tileImageProvider, tile, tessellatorMesh, tileDownloadPriority, logTilesPetitions, frameTasksExecutor, backGroundTileImage, backGroundTileImageName){
  RCObject.call(this);
  this._tileImageProvider = tileImageProvider;
  this._texturesHandler = rc._texturesHandler;
  this._tileTextureResolution = layerTilesRenderParameters._tileTextureResolution;
  this._tile = tile;
  this._tileId = tile._id;
  this._texturedMesh = null;
  this._canceled = false;
  this._tileDownloadPriority = tileDownloadPriority;
  this._logTilesPetitions = logTilesPetitions;
  this._frameTasksExecutor = frameTasksExecutor;
  this._backGroundTileImage = backGroundTileImage;
  this._backGroundTileImageName = backGroundTileImageName;
  this._ownedTexCoords = true;
  this._transparent = false;
  this._generateMipmap = true;
  ++this._tileImageProvider._referenceCounter;
  this._texturedMesh = createMesh(tile, tessellatorMesh, this._texturesHandler, backGroundTileImage, backGroundTileImageName, this._ownedTexCoords, this._transparent, this._generateMipmap);
}

function createMesh(tile, tessellatorMesh, texturesHandler, backGroundTileImage, backGroundTileImageName, ownedTexCoords, transparent, generateMipmap){
  var ancestor, fallbackSolved, glTextureId, glTextureIdRetainedCopy, mapping, mappings, mesh;
  mappings = new ArrayList;
  ancestor = tile;
  fallbackSolved = false;
  while (!!ancestor && !fallbackSolved) {
    mapping = new LazyTextureMapping(new DTT_LTMInitializer(tile, ancestor), ownedTexCoords, transparent);
    if (ancestor != tile) {
      glTextureId = (mesh = ancestor._texturizedMesh , !mesh?null:$getTopLevelTextureId(mesh));
      if (glTextureId) {
        glTextureIdRetainedCopy = ($retainGLTextureId(glTextureId._texHandler, glTextureId._id) , new TextureIDReference(glTextureId._id, glTextureId._isPremultiplied, glTextureId._texHandler));
        $releaseGLTextureId(mapping);
        mapping._glTextureId = glTextureIdRetainedCopy;
        fallbackSolved = true;
      }
    }
    setCheck(mappings.array, mappings.array.length, mapping);
    ancestor = ancestor._parent;
  }
  if (!fallbackSolved && !!backGroundTileImage) {
    mapping = new LazyTextureMapping(new DTT_LTMInitializer(tile, tile), true, false);
    glTextureId = $getTextureIDReference(texturesHandler, backGroundTileImage, _rgba, backGroundTileImageName, generateMipmap);
    $releaseGLTextureId(mapping);
    mapping._glTextureId = glTextureId;
    setCheck(mappings.array, mappings.array.length, mapping);
  }
  return new LeveledTexturedMesh(tessellatorMesh, mappings);
}

defineClass(381, 9, {9:1}, DTT_TileTextureBuilder);
_.dispose = function dispose_20(){
  $_release(this._tileImageProvider);
  $dispose_0(this);
}
;
_._canceled = false;
_._generateMipmap = false;
_._logTilesPetitions = false;
_._ownedTexCoords = false;
_._tileDownloadPriority = {l:0, m:0, h:0};
_._transparent = false;
var Lorg_glob3_mobile_generated_DTT_1TileTextureBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'DTT_TileTextureBuilder', 381);
function $dispose_1(this$static){
  !!this$static._builder && $_release(this$static._builder);
}

function DTT_TileTextureBuilderHolder(builder){
  this._builder = builder;
}

defineClass(380, 1, {}, DTT_TileTextureBuilderHolder);
var Lorg_glob3_mobile_generated_DTT_1TileTextureBuilderHolder_2_classLit = createForClass('org.glob3.mobile.generated', 'DTT_TileTextureBuilderHolder', 380);
function DTT_TileTextureBuilderStartTask(builder){
  this._builder = builder;
  ++this._builder._referenceCounter;
}

defineClass(384, 103, {103:1}, DTT_TileTextureBuilderStartTask);
_.dispose = function dispose_21(){
  $_release(this._builder);
}
;
_.execute_1 = function execute_6(rc){
  $start(this._builder);
}
;
_.isCanceled = function isCanceled_1(rc){
  return this._builder._canceled;
}
;
var Lorg_glob3_mobile_generated_DTT_1TileTextureBuilderStartTask_2_classLit = createForClass('org.glob3.mobile.generated', 'DTT_TileTextureBuilderStartTask', 384);
function $buildOnCanvas(this$static, canvas){
  var col, height, row, width_0, x_0, x2, xInterval, y_0, y2, yInterval;
  width_0 = canvas._canvasWidth;
  height = canvas._canvasHeight;
  $setFillColor(canvas, this$static._backgroundColor);
  canvas._canvasWidth > 0 && canvas._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $_fillRectangle(canvas, 0, 0, width_0, height);
  $setFillColor(canvas, this$static._boxColor);
  xInterval = width_0 / this$static._splits;
  yInterval = height / this$static._splits;
  for (col = 0; col < this$static._splits; col += 2) {
    x_0 = col * xInterval;
    x2 = (col + 1) * xInterval;
    for (row = 0; row < this$static._splits; row += 2) {
      y_0 = row * yInterval;
      y2 = (row + 1) * yInterval;
      canvas._canvasWidth > 0 && canvas._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
      $roundRect(canvas, x_0 + 2, y_0 + 2, xInterval - 4, yInterval - 4, 4, true, false);
      canvas._canvasWidth > 0 && canvas._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
      $roundRect(canvas, x2 + 2, y2 + 2, xInterval - 4, yInterval - 4, 4, true, false);
    }
  }
}

function DefaultChessCanvasImageBuilder(backgroundColor, boxColor){
  this._width = 256;
  this._height = 256;
  this._canvas = null;
  this._canvasWidth = 0;
  this._canvasHeight = 0;
  this._backgroundColor = new Color_0(backgroundColor);
  this._boxColor = new Color_0(boxColor);
  this._splits = 4;
}

defineClass(361, 360, {}, DefaultChessCanvasImageBuilder);
_._splits = 0;
var Lorg_glob3_mobile_generated_DefaultChessCanvasImageBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'DefaultChessCanvasImageBuilder', 361);
function $notifyChangedInfo(this$static, info){
  !!this$static._changedInfoListener && this$static._enable && this$static._changedInfoListener.changedRendererInfo(this$static._rendererIdentifier, info);
}

function DefaultRenderer(){
  this._info = new ArrayList;
  this._enable = true;
}

defineClass(179, 1, {135:1});
_.getPlanetRenderer = function getPlanetRenderer_0(){
  return null;
}
;
_.getRenderState = function getRenderState_0(rc){
  return $clinit_RenderState() , $clinit_RenderState() , READY;
}
;
_.getSurfaceElevationProvider = function getSurfaceElevationProvider_0(){
  return null;
}
;
_.initialize = function initialize_2(context){
  this._context = context;
}
;
_.isEnable = function isEnable_0(){
  return this._enable;
}
;
_.isPlanetRenderer = function isPlanetRenderer_0(){
  return false;
}
;
_.onTouchEvent_0 = function onTouchEvent_4(ec, touchEvent){
  return false;
}
;
_.setChangedRendererInfoListener = function setChangedRendererInfoListener_0(changedInfoListener, rendererIdentifier){
  if (this._changedInfoListener) {
    $logError(_instance_3, 'Changed Renderer Info Listener of DefaultRenderer already set', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
   else {
    this._changedInfoListener = changedInfoListener;
    this._rendererIdentifier = rendererIdentifier;
    $notifyChangedInfo(this, this._info);
  }
}
;
_.start_2 = function start_7(rc){
}
;
_.stop_1 = function stop_5(rc){
}
;
_._changedInfoListener = null;
_._enable = false;
_._rendererIdentifier = 0;
var Lorg_glob3_mobile_generated_DefaultRenderer_2_classLit = createForClass('org.glob3.mobile.generated', 'DefaultRenderer', 179);
defineClass(452, 1, {});
var Lorg_glob3_mobile_generated_TileTexturizer_2_classLit = createForClass('org.glob3.mobile.generated', 'TileTexturizer', 452);
function $ancestorTexturedSolvedChanged(tile, ancestorTile, textureSolved){
  var ancestorMesh, glTextureId, glTextureIdRetainedCopy, level, tileMesh;
  if (!textureSolved) {
    return;
  }
  if (tile._textureSolved) {
    return;
  }
  ancestorMesh = $getMesh(ancestorTile);
  if (!ancestorMesh) {
    return;
  }
  glTextureId = $getTopLevelTextureId(ancestorMesh);
  if (!glTextureId) {
    return;
  }
  tileMesh = $getMesh(tile);
  if (!tileMesh) {
    return;
  }
  glTextureIdRetainedCopy = ($retainGLTextureId(glTextureId._texHandler, glTextureId._id) , new TextureIDReference(glTextureId._id, glTextureId._isPremultiplied, glTextureId._texHandler));
  level = tile._level - ancestorTile._level;
  $setGLTextureIdForLevel(tileMesh, level, glTextureIdRetainedCopy) || $releaseGLTextureId_1(glTextureIdRetainedCopy._texHandler, glTextureIdRetainedCopy._id);
}

function $getMesh(tile){
  var tileBuilderHolder;
  tileBuilderHolder = tile._texturizerData;
  return !tileBuilderHolder?null:tileBuilderHolder._builder._texturedMesh;
}

function $getRenderState(this$static, layerSet){
  if (this$static._errors.array.length > 0) {
    return $clinit_RenderState() , new RenderState(this$static._errors);
  }
  if (!this$static._defaultBackGroundImageLoaded) {
    return $clinit_RenderState() , $clinit_RenderState() , BUSY;
  }
  if (layerSet) {
    return $getRenderState_0(layerSet);
  }
  return $clinit_RenderState() , $clinit_RenderState() , READY;
}

function $initialize_1(this$static){
  $logInfo(_instance_3, 'Initializing texturizer...', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $build(this$static._defaultBackGroundImageBuilder, new DTT_IImageBuilderListener(this$static));
}

function $onTerrainTouchEvent(position, tile, layerSet){
  return !!layerSet && $onTerrainTouchEvent_0(layerSet, position, tile);
}

function $setDefaultBackGroundImage(this$static, defaultBackGroundImage){
  this$static._defaultBackGroundImage = defaultBackGroundImage;
}

function $setDefaultBackGroundImageName(this$static, defaultBackGroundImageName){
  this$static._defaultBackGroundImageName = defaultBackGroundImageName;
}

function $texturize(this$static, rc, layerTilesRenderParameters, layerSet, forceFullRender, tileDownloadPriority, tile, tessellatorMesh, logTilesPetitions){
  var builder, builderHolder, texturizedMesh, tileImageProvider;
  builderHolder = tile._texturizerData;
  tileImageProvider = (!layerSet._tileImageProvider && (layerSet._tileImageProvider = $createTileImageProvider(layerSet, rc, layerTilesRenderParameters)) , layerSet._tileImageProvider);
  if (!tileImageProvider) {
    $setTextureSolved(tile, true);
    tile._texturizerDirty = false;
    return null;
  }
  if (!builderHolder) {
    builder = new DTT_TileTextureBuilder(rc, layerTilesRenderParameters, tileImageProvider, tile, tessellatorMesh, tileDownloadPriority, logTilesPetitions, rc._frameTasksExecutor, this$static._defaultBackGroundImage, this$static._defaultBackGroundImageName);
    builderHolder = new DTT_TileTextureBuilderHolder(builder);
    $setTexturizerData(tile, builderHolder);
  }
   else {
    builder = builderHolder._builder;
  }
  texturizedMesh = builder._texturedMesh;
  forceFullRender?$start(builder):$addPreRenderTask(rc._frameTasksExecutor, new DTT_TileTextureBuilderStartTask(builder));
  tile._texturizerDirty = false;
  return texturizedMesh;
}

function $tileMeshToBeDeleted(tile){
  var builder, builderHolder;
  builderHolder = tile._texturizerData;
  if (builderHolder) {
    builder = builderHolder._builder;
    $cancel_0(builder, false);
  }
}

function $tileToBeDeleted(tile){
  var builder, builderHolder;
  builderHolder = tile._texturizerData;
  if (builderHolder) {
    builder = builderHolder._builder;
    $cancel_0(builder, true);
  }
}

function DefaultTileTexturizer(defaultBackGroundImageBuilder){
  this._errors = new ArrayList;
  this._defaultBackGroundImageBuilder = defaultBackGroundImageBuilder;
  this._defaultBackGroundImageLoaded = false;
  $logInfo(_instance_3, 'Create texturizer...', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
}

defineClass(339, 452, {}, DefaultTileTexturizer);
_._defaultBackGroundImageLoaded = false;
var Lorg_glob3_mobile_generated_DefaultTileTexturizer_2_classLit = createForClass('org.glob3.mobile.generated', 'DefaultTileTexturizer', 339);
function DeviceAttitudeCameraHandler(){
  this._localRM = new MutableMatrix44D;
  this._attitudeMatrix = new MutableMatrix44D;
  this._camRM = new MutableMatrix44D;
  this._updateLocation = true;
}

defineClass(172, 64, $intern_39, DeviceAttitudeCameraHandler);
_.onTouchEvent = function onTouchEvent_5(eventContext, touchEvent, cameraContext){
  return false;
}
;
_.render_0 = function render_6(rc, cameraContext){
  var camCS, camPosition, devAtt, finalCS, g, loc, local, nextCamera, ori, d;
  devAtt = _instance;
  nextCamera = rc._nextCamera;
  if (!devAtt) {
    $logError(_instance_3, 'IDeviceAttitude not initilized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  devAtt._isTracking || (trackGyroscope(devAtt) , devAtt._isTracking = true);
  $copyValueOfRotationMatrix_0(_instance, this._attitudeMatrix);
  if (!this._attitudeMatrix._isValid) {
    return;
  }
  camPosition = (!nextCamera._geodeticPosition && (nextCamera._geodeticPosition = new Geodetic3D_1($toGeodetic3D(nextCamera._planet, $asVector3D(nextCamera._position)))) , nextCamera._geodeticPosition);
  ori = _instance._currentIO;
  camCS = $getCameraCoordinateSystemForInterfaceOrientation(_instance, ori);
  local = $getCoordinateSystemAt(rc._planet, camPosition);
  $copyValueOfRotationMatrix(local, this._localRM);
  $copyValueOfMultiplication(this._camRM, this._localRM, this._attitudeMatrix);
  finalCS = $applyRotation(camCS, this._camRM);
  $setCameraCoordinateSystem(nextCamera, finalCS);
  if (this._updateLocation) {
    loc = _instance_0;
    loc._isTracking || (loc._isTracking = $startTrackingLocationJS(loc) , loc._isTracking);
    g = fromDegrees_1(_lat, _lon, _altitude);
    $isNan(g._latitude) || $isNan(g._longitude) || ((d = $squaredDistanceTo(nextCamera._center, nextCamera._position) , d > 0 && d == d)?$setGeodeticPosition(nextCamera, g):$logWarning(_instance_3, 'Trying to set position of unvalid camera. ViewDirection: %s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [$description_8(new Vector3D(nextCamera._center._x - nextCamera._position._x, nextCamera._center._y - nextCamera._position._y, nextCamera._center._z - nextCamera._position._z))])));
  }
}
;
_._updateLocation = false;
var Lorg_glob3_mobile_generated_DeviceAttitudeCameraHandler_2_classLit = createForClass('org.glob3.mobile.generated', 'DeviceAttitudeCameraHandler', 172);
function DirectMesh(primitive, center, vertices){
  AbstractMesh.call(this, primitive, center, vertices, null, null, true);
  this._renderVerticesCount = ~~(vertices._buffer.length / 3);
}

defineClass(198, 197, {}, DirectMesh);
_.rawRender_0 = function rawRender_1(rc){
  var gl;
  gl = rc._gl;
  $drawArrays(gl, this._primitive, this._renderVerticesCount, this._glState, rc._gpuProgramManager);
}
;
_._renderVerticesCount = 0;
var Lorg_glob3_mobile_generated_DirectMesh_2_classLit = createForClass('org.glob3.mobile.generated', 'DirectMesh', 198);
function $setLightDirection(this$static, lightDir){
  var dirN;
  dirN = $normalized_0(lightDir);
  $changeValue_1(this$static._lightDirectionUniformValue, dirN._x, dirN._y, dirN._z);
}

function DirectionLightGLFeature(diffuseLightDirection, diffuseLightColor, ambientLightColor){
  var dirN;
  GLFeature.call(this, ($clinit_GLFeatureGroupName() , LIGHTING_GROUP), 11);
  $addUniformValue(this._values, ($clinit_GPUUniformKey() , AMBIENT_LIGHT_COLOR), new GPUUniformValueVec3Float(ambientLightColor));
  dirN = $normalized_0(diffuseLightDirection);
  this._lightDirectionUniformValue = new GPUUniformValueVec3FloatMutable(dirN._x, dirN._y, dirN._z);
  $addUniformValue(this._values, DIFFUSE_LIGHT_DIRECTION, this._lightDirectionUniformValue);
  $addUniformValue(this._values, DIFFUSE_LIGHT_COLOR, new GPUUniformValueVec3Float(diffuseLightColor));
}

defineClass(148, 27, {148:1, 27:1, 9:1}, DirectionLightGLFeature);
_.applyOnGlobalGLState = function applyOnGlobalGLState_0(state){
}
;
_.dispose = function dispose_22(){
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_DirectionLightGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'DirectionLightGLFeature', 148);
function DoubleTapRotationEffect(duration, axis_0, angle, distance){
  EffectWithDuration.call(this, duration);
  this._axis = new Vector3D_0(axis_0);
  this._angle = new Angle_0(angle);
  this._distance = distance;
}

defineClass(211, 194, {}, DoubleTapRotationEffect);
_.cancel = function cancel_2(when){
}
;
_.doStep = function doStep_2(rc, when){
  var alpha_0, camera, step;
  alpha_0 = $getAlpha(this, when);
  camera = rc._nextCamera;
  step = alpha_0 - this._lastAlpha;
  $rotateWithAxis(camera, this._axis, $times(this._angle, step));
  $moveForward(camera, this._distance * step);
  this._lastAlpha = alpha_0;
}
;
_.start_1 = function start_8(rc, when){
  this._started = when._milliseconds;
  this._lastAlpha = 0;
}
;
_.stop_0 = function stop_6(rc, when){
  var camera, step;
  camera = rc._nextCamera;
  step = 1 - this._lastAlpha;
  $rotateWithAxis(camera, this._axis, $times(this._angle, step));
  $moveForward(camera, this._distance * step);
}
;
_._distance = 0;
_._lastAlpha = 0;
var Lorg_glob3_mobile_generated_DoubleTapRotationEffect_2_classLit = createForClass('org.glob3.mobile.generated', 'DoubleTapRotationEffect', 211);
defineClass(408, 477, {});
_.doStep = function doStep_3(rc, when){
  this._force *= this._friction;
}
;
_.isDone = function isDone_1(rc, when){
  return $abs(this._force) < 0.005;
}
;
_._force = 0;
_._friction = 0;
var Lorg_glob3_mobile_generated_EffectWithForce_2_classLit = createForClass('org.glob3.mobile.generated', 'EffectWithForce', 408);
function $cancelAllEffectsFor(this$static, target){
  var effectRun, effectsToCancelSize, i_0, iterator, now_0;
  now_0 = new TimeInterval(fromDouble(now_1()));
  this$static._effectsToCancel.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
  iterator = new AbstractList$IteratorImpl(this$static._effectsRuns);
  while (iterator.i < iterator.this$01_0.size_1()) {
    effectRun = (checkCriticalElement(iterator.i < iterator.this$01_0.size_1()) , dynamicCast(iterator.this$01_0.get_1(iterator.last = iterator.i++), 75));
    if (effectRun._target == target) {
      effectRun._started?$add_3(this$static._effectsToCancel, effectRun):!!effectRun._effect && effectRun._effect.dispose();
      $remove_4(iterator);
    }
  }
  effectsToCancelSize = this$static._effectsToCancel.array.length;
  for (i_0 = 0; i_0 < effectsToCancelSize; i_0++) {
    effectRun = dynamicCast($get_0(this$static._effectsToCancel, i_0), 75);
    effectRun._effect.cancel(now_0);
    !!effectRun._effect && effectRun._effect.dispose();
  }
}

function $doOneCyle(this$static, rc){
  var effect, effectRun, effectsRunsSize, i_0, now_0;
  if (this$static._effectsRuns.array.length != 0) {
    now_0 = new TimeInterval(fromDouble(now_1()));
    $processFinishedEffects(this$static, rc, now_0);
    effectsRunsSize = this$static._effectsRuns.array.length;
    for (i_0 = 0; i_0 < effectsRunsSize; i_0++) {
      effectRun = dynamicCast($get_0(this$static._effectsRuns, i_0), 75);
      effect = effectRun._effect;
      if (!effectRun._started) {
        effect.start_1(rc, now_0);
        effectRun._started = true;
      }
      effect.doStep(rc, now_0);
    }
  }
}

function $processFinishedEffects(this$static, rc, when){
  var effectRun, effectsToStopSize, i_0, iterator;
  this$static._effectsToStop.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
  iterator = new AbstractList$IteratorImpl(this$static._effectsRuns);
  while (iterator.i < iterator.this$01_0.size_1()) {
    effectRun = (checkCriticalElement(iterator.i < iterator.this$01_0.size_1()) , dynamicCast(iterator.this$01_0.get_1(iterator.last = iterator.i++), 75));
    if (effectRun._started) {
      if (effectRun._effect.isDone(rc, when)) {
        $remove_4(iterator);
        $add_3(this$static._effectsToStop, effectRun);
      }
    }
  }
  effectsToStopSize = this$static._effectsToStop.array.length;
  for (i_0 = 0; i_0 < effectsToStopSize; i_0++) {
    effectRun = dynamicCast($get_0(this$static._effectsToStop, i_0), 75);
    effectRun._effect.stop_0(rc, when);
    !!effectRun._effect && effectRun._effect.dispose();
  }
}

function $startEffect(this$static, effect, target){
  $add_3(this$static._effectsRuns, new EffectsScheduler$EffectRun(effect, target));
}

function EffectsScheduler(){
  this._effectsRuns = new ArrayList;
  this._effectsToStop = new ArrayList;
  this._effectsToCancel = new ArrayList;
  this._effectsRuns = new ArrayList;
}

defineClass(335, 1, {}, EffectsScheduler);
var Lorg_glob3_mobile_generated_EffectsScheduler_2_classLit = createForClass('org.glob3.mobile.generated', 'EffectsScheduler', 335);
function EffectsScheduler$EffectRun(effect, target){
  this._effect = effect;
  this._target = target;
  this._started = false;
}

defineClass(75, 1, {75:1}, EffectsScheduler$EffectRun);
_._started = false;
var Lorg_glob3_mobile_generated_EffectsScheduler$EffectRun_2_classLit = createForClass('org.glob3.mobile.generated', 'EffectsScheduler/EffectRun', 75);
function $getMeanRadius(this$static){
  return (this$static._radii._x + this$static._radii._y + this$static._radii._y) / 3;
}

function $intersectionsDistances(this$static, originX, originY, originZ, directionX, directionY, directionZ){
  var a, b, c, discriminant, intersections, root1, root2, t;
  intersections = new ArrayList;
  a = directionX * directionX * this$static._oneOverRadiiSquared._x + directionY * directionY * this$static._oneOverRadiiSquared._y + directionZ * directionZ * this$static._oneOverRadiiSquared._z;
  b = 2 * (originX * directionX * this$static._oneOverRadiiSquared._x + originY * directionY * this$static._oneOverRadiiSquared._y + originZ * directionZ * this$static._oneOverRadiiSquared._z);
  c = originX * originX * this$static._oneOverRadiiSquared._x + originY * originY * this$static._oneOverRadiiSquared._y + originZ * originZ * this$static._oneOverRadiiSquared._z - 1;
  discriminant = b * b - 4 * a * c;
  if (discriminant < 0) {
    return intersections;
  }
   else if (discriminant == 0) {
    $add_3(intersections, ($clinit_Double() , new Double(-0.5 * b / a)));
    return intersections;
  }
  t = -0.5 * (b + (b > 0?1:-1) * sqrt_0(discriminant));
  root1 = t / a;
  root2 = c / t;
  if (root1 < root2) {
    $add_3(intersections, ($clinit_Double() , new Double(root1)));
    $add_3(intersections, new Double(root2));
  }
   else {
    $add_3(intersections, ($clinit_Double() , new Double(root2)));
    $add_3(intersections, new Double(root1));
  }
  return intersections;
}

function Ellipsoid(center, radii){
  new Vector3D_0(center);
  this._radii = new Vector3D_0(radii);
  this._radiiSquared = new Vector3D_0(new Vector3D(radii._x * radii._x, radii._y * radii._y, radii._z * radii._z));
  this._radiiToTheFourth = new Vector3D_0(new Vector3D(this._radiiSquared._x * this._radiiSquared._x, this._radiiSquared._y * this._radiiSquared._y, this._radiiSquared._z * this._radiiSquared._z));
  this._oneOverRadiiSquared = new Vector3D_0(new Vector3D(1 / (radii._x * radii._x), 1 / (radii._y * radii._y), 1 / (radii._z * radii._z)));
}

defineClass(188, 1, {}, Ellipsoid);
var Lorg_glob3_mobile_generated_Ellipsoid_2_classLit = createForClass('org.glob3.mobile.generated', 'Ellipsoid', 188);
function $closestIntersection(this$static, pos, ray){
  var distances;
  if (pos._x != pos._x || pos._y != pos._y || pos._z != pos._z || ray._x != ray._x || ray._y != ray._y || ray._z != ray._z) {
    return $clinit_Vector3D() , new Vector3D(NaN, NaN, NaN);
  }
  distances = $intersectionsDistances_0(this$static, pos._x, pos._y, pos._z, ray._x, ray._y, ray._z);
  if (distances.array.length == 0) {
    return $clinit_Vector3D() , new Vector3D(NaN, NaN, NaN);
  }
  return $add_18(pos, $times_2(ray, (checkElementIndex(0, distances.array.length) , dynamicCast(distances.array[0], 32)).value_0));
}

function $getCoordinateSystemAt(this$static, geo){
  var origin, x_0, y_0, z_0;
  origin = $toCartesian(this$static, geo._latitude, geo._longitude, geo._height);
  z_0 = $normalized_0(origin);
  y_0 = $projectionInPlane(($clinit_Vector3D() , new Vector3D(0, 0, 1)), z_0);
  x_0 = new Vector3D(y_0._y * z_0._z - y_0._z * z_0._y, y_0._z * z_0._x - y_0._x * z_0._z, y_0._x * z_0._y - y_0._y * z_0._x);
  return new CoordinateSystem_0(x_0, y_0, z_0, origin);
}

defineClass(454, 1, {});
var Lorg_glob3_mobile_generated_Planet_2_classLit = createForClass('org.glob3.mobile.generated', 'Planet', 454);
function $beginDoubleDrag(this$static, origin, centerRay, initialRay0, initialRay1){
  var g, g0, g1;
  this$static._origin = new MutableVector3D_0(origin._x, origin._y, origin._z);
  this$static._centerRay = new MutableVector3D_0(centerRay._x, centerRay._y, centerRay._z);
  this$static._initialPoint0 = $asMutableVector3D($closestIntersection(this$static, origin, initialRay0));
  this$static._initialPoint1 = $asMutableVector3D($closestIntersection(this$static, origin, initialRay1));
  this$static._angleBetweenInitialPoints = $angleBetween(this$static._initialPoint0, this$static._initialPoint1)._degrees;
  this$static._centerPoint = $asMutableVector3D($closestIntersection(this$static, origin, centerRay));
  this$static._angleBetweenInitialRays = fromRadians(angleInRadiansBetween_0(initialRay0, initialRay1))._degrees;
  g0 = $toGeodetic2D(this$static, $asVector3D(this$static._initialPoint0));
  g1 = $toGeodetic2D(this$static, $asVector3D(this$static._initialPoint1));
  g = $getMidPoint(this$static, g0, g1);
  this$static._initialPoint = $asMutableVector3D($toCartesian(this$static, g._latitude, g._longitude, 0));
}

function $beginSingleDrag(this$static, origin, initialRay){
  $copyFrom_2(this$static._origin, origin);
  $copyFrom_2(this$static._initialPoint, $closestIntersection(this$static, origin, initialRay));
  this$static._validSingleDrag = false;
}

function $closestPointToSphere(this$static, pos, ray){
  var D, O2, OU, R0, U2, a_, b, b_, c, c_, co2, rad, rad_, result, t;
  t = 0;
  R0 = $getMeanRadius(this$static._ellipsoid);
  U2 = ray._x * ray._x + ray._y * ray._y + ray._z * ray._z;
  O2 = pos._x * pos._x + pos._y * pos._y + pos._z * pos._z;
  OU = pos._x * ray._x + pos._y * ray._y + pos._z * ray._z;
  b = 2 * OU;
  c = O2 - R0 * R0;
  rad = b * b - 4 * U2 * c;
  if (rad > 0) {
    t = (-b - sqrt_0(rad)) / (2 * U2);
    t < 1 && (t = (-b + sqrt_0(rad)) / (2 * U2));
    t < 1 && (rad = -12345);
  }
  if (rad < 0) {
    D = sqrt_0(O2);
    co2 = R0 * R0 / (D * D);
    a_ = OU * OU - co2 * O2 * U2;
    b_ = 2 * OU * O2 - co2 * 2 * OU * O2;
    c_ = O2 * O2 - co2 * O2 * O2;
    rad_ = b_ * b_ - 4 * a_ * c_;
    t = (-b_ - sqrt_0(rad_)) / (2 * a_);
  }
  result = $add_18(pos, new Vector3D(ray._x * t, ray._y * t, ray._z * t));
  return result;
}

function $createDoubleTapEffect(this$static, origin, centerRay, tapRay){
  var angle, axis_0, centerPoint, distance, height, initialPoint;
  initialPoint = $closestIntersection(this$static, origin, tapRay);
  if (initialPoint._x != initialPoint._x || initialPoint._y != initialPoint._y || initialPoint._z != initialPoint._z)
    return null;
  centerPoint = $closestIntersection(this$static, origin, centerRay);
  axis_0 = new Vector3D(initialPoint._y * centerPoint._z - initialPoint._z * centerPoint._y, initialPoint._z * centerPoint._x - initialPoint._x * centerPoint._z, initialPoint._x * centerPoint._y - initialPoint._y * centerPoint._x);
  angle = fromRadians(-asin_0(sqrt_0(axis_0._x * axis_0._x + axis_0._y * axis_0._y + axis_0._z * axis_0._z) / sqrt_0(initialPoint._x * initialPoint._x + initialPoint._y * initialPoint._y + initialPoint._z * initialPoint._z) / sqrt_0(centerPoint._x * centerPoint._x + centerPoint._y * centerPoint._y + centerPoint._z * centerPoint._z)));
  height = $toGeodetic3D(this$static, origin)._height;
  distance = height * 0.6;
  return new DoubleTapRotationEffect(new TimeInterval({l:750, m:0, h:0}), axis_0, angle, distance);
}

function $createEffectFromLastSingleDrag(this$static){
  if (!this$static._validSingleDrag || $isNan_0(this$static._lastDragAxis))
    return null;
  return new RotateWithAxisEffect($asVector3D(this$static._lastDragAxis), fromRadians(this$static._lastDragRadiansStep));
}

function $distanceToHorizon(this$static, position){
  var D, R;
  R = $minAxis(this$static._ellipsoid._radii);
  D = sqrt_0(position._x * position._x + position._y * position._y + position._z * position._z);
  return Math.sqrt(D * D - R * R);
}

function $doubleDrag(this$static, finalRay0, finalRay1){
  var P0, P1, angle, angle0, angle1, angle_n, angle_n1, centerPoint2, d, dAccum, distance, factor, finalPoint, finalRaysAngle, g, initialPoint, matrix, normal, p0, point0, point1, positionCamera, precision, ray0, ray1, rotation, rotationAxis, rotationDelta, sign, translation, translation2, v0, v1, viewDirection;
  if ($isNan_0(this$static._initialPoint0) || $isNan_0(this$static._initialPoint1))
    return new MutableMatrix44D_2;
  positionCamera = this$static._origin;
  finalRaysAngle = fromRadians(angleInRadiansBetween_0(finalRay0, finalRay1))._degrees;
  factor = finalRaysAngle / this$static._angleBetweenInitialRays;
  dAccum = 0;
  distance = $length($sub_0(this$static._origin, this$static._centerPoint));
  d = distance * (factor - 1) / factor;
  translation = createTranslationMatrix($times_2($normalized_0($asVector3D(this$static._centerRay)), d));
  positionCamera = new MutableVector3D_0(positionCamera._x * translation._m00 + positionCamera._y * translation._m01 + positionCamera._z * translation._m02 + translation._m03, positionCamera._x * translation._m10 + positionCamera._y * translation._m11 + positionCamera._z * translation._m12 + translation._m13, positionCamera._x * translation._m20 + positionCamera._y * translation._m21 + positionCamera._z * translation._m22 + translation._m23);
  dAccum += d;
  point0 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), finalRay0);
  point1 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), finalRay1);
  angle0 = fromRadians(angleInRadiansBetween_0(point0, point1))._degrees;
  if (angle0 != angle0)
    return new MutableMatrix44D_2;
  d = (distance - d) * 0.3 <= 0?0 - (distance - d) * 0.3:(distance - d) * 0.3;
  angle0 < this$static._angleBetweenInitialPoints && (d *= -1);
  $copyValue(translation, createTranslationMatrix($times_2($normalized_0($asVector3D(this$static._centerRay)), d)));
  positionCamera = new MutableVector3D_0(positionCamera._x * translation._m00 + positionCamera._y * translation._m01 + positionCamera._z * translation._m02 + translation._m03, positionCamera._x * translation._m10 + positionCamera._y * translation._m11 + positionCamera._z * translation._m12 + translation._m13, positionCamera._x * translation._m20 + positionCamera._y * translation._m21 + positionCamera._z * translation._m22 + translation._m23);
  dAccum += d;
  point0 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), finalRay0);
  point1 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), finalRay1);
  angle1 = fromRadians(angleInRadiansBetween_0(point0, point1))._degrees;
  if (angle1 != angle1)
    return new MutableMatrix44D_2;
  precision = pow_0(10, Math.log(distance) * Math.LOG10E - 8);
  angle_n1 = angle0;
  angle_n = angle1;
  while ($abs(angle_n - this$static._angleBetweenInitialPoints) > precision) {
    (angle_n1 - angle_n) / (angle_n - this$static._angleBetweenInitialPoints) < 0 && (d *= -0.5);
    $copyValue(translation, createTranslationMatrix($times_2($normalized_0($asVector3D(this$static._centerRay)), d)));
    positionCamera = new MutableVector3D_0(positionCamera._x * translation._m00 + positionCamera._y * translation._m01 + positionCamera._z * translation._m02 + translation._m03, positionCamera._x * translation._m10 + positionCamera._y * translation._m11 + positionCamera._z * translation._m12 + translation._m13, positionCamera._x * translation._m20 + positionCamera._y * translation._m21 + positionCamera._z * translation._m22 + translation._m23);
    dAccum += d;
    angle_n1 = angle_n;
    point0 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), finalRay0);
    point1 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), finalRay1);
    angle_n = fromRadians(angleInRadiansBetween_0(point0, point1))._degrees;
    if (angle_n != angle_n)
      return new MutableMatrix44D_2;
  }
  matrix = new MutableMatrix44D_0(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  positionCamera = this$static._origin;
  viewDirection = this$static._centerRay;
  ray0 = new MutableVector3D_0(finalRay0._x, finalRay0._y, finalRay0._z);
  ray1 = new MutableVector3D_0(finalRay1._x, finalRay1._y, finalRay1._z);
  initialPoint = $asVector3D(this$static._initialPoint);
  rotationAxis = $cross_0(initialPoint, $asVector3D(this$static._centerPoint));
  rotationDelta = fromRadians(-acos_0($dot($normalized(this$static._initialPoint), $normalized(this$static._centerPoint))));
  if (rotationDelta._degrees != rotationDelta._degrees)
    return new MutableMatrix44D_2;
  rotation = createRotationMatrix(rotationDelta, rotationAxis);
  positionCamera = new MutableVector3D_0(positionCamera._x * rotation._m00 + positionCamera._y * rotation._m01 + positionCamera._z * rotation._m02 + rotation._m03, positionCamera._x * rotation._m10 + positionCamera._y * rotation._m11 + positionCamera._z * rotation._m12 + rotation._m13, positionCamera._x * rotation._m20 + positionCamera._y * rotation._m21 + positionCamera._z * rotation._m22 + rotation._m23);
  viewDirection = new MutableVector3D_0(viewDirection._x * rotation._m00 + viewDirection._y * rotation._m01 + viewDirection._z * rotation._m02, viewDirection._x * rotation._m10 + viewDirection._y * rotation._m11 + viewDirection._z * rotation._m12, viewDirection._x * rotation._m20 + viewDirection._y * rotation._m21 + viewDirection._z * rotation._m22);
  ray0 = new MutableVector3D_0(ray0._x * rotation._m00 + ray0._y * rotation._m01 + ray0._z * rotation._m02, ray0._x * rotation._m10 + ray0._y * rotation._m11 + ray0._z * rotation._m12, ray0._x * rotation._m20 + ray0._y * rotation._m21 + ray0._z * rotation._m22);
  ray1 = new MutableVector3D_0(ray1._x * rotation._m00 + ray1._y * rotation._m01 + ray1._z * rotation._m02, ray1._x * rotation._m10 + ray1._y * rotation._m11 + ray1._z * rotation._m12, ray1._x * rotation._m20 + ray1._y * rotation._m21 + ray1._z * rotation._m22);
  $copyValueOfMultiplication(matrix, rotation, matrix);
  translation2 = createTranslationMatrix($times_2($normalized_0(new Vector3D(viewDirection._x, viewDirection._y, viewDirection._z)), dAccum));
  positionCamera = new MutableVector3D_0(positionCamera._x * translation2._m00 + positionCamera._y * translation2._m01 + positionCamera._z * translation2._m02 + translation2._m03, positionCamera._x * translation2._m10 + positionCamera._y * translation2._m11 + positionCamera._z * translation2._m12 + translation2._m13, positionCamera._x * translation2._m20 + positionCamera._y * translation2._m21 + positionCamera._z * translation2._m22 + translation2._m23);
  $copyValueOfMultiplication(matrix, translation2, matrix);
  centerPoint2 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), new Vector3D(viewDirection._x, viewDirection._y, viewDirection._z));
  P0 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), new Vector3D(ray0._x, ray0._y, ray0._z));
  P1 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), new Vector3D(ray1._x, ray1._y, ray1._z));
  g = $getMidPoint(this$static, $toGeodetic2D(this$static, P0), $toGeodetic2D(this$static, P1));
  finalPoint = $toCartesian(this$static, g._latitude, g._longitude, 0);
  rotationAxis = new Vector3D(centerPoint2._y * finalPoint._z - centerPoint2._z * finalPoint._y, centerPoint2._z * finalPoint._x - centerPoint2._x * finalPoint._z, centerPoint2._x * finalPoint._y - centerPoint2._y * finalPoint._x);
  rotationDelta = fromRadians(-acos_0($dot_0($normalized_0(centerPoint2), $normalized_0(finalPoint))));
  if (rotationDelta._degrees != rotationDelta._degrees)
    return new MutableMatrix44D_2;
  rotation = createRotationMatrix(rotationDelta, rotationAxis);
  positionCamera = new MutableVector3D_0(positionCamera._x * rotation._m00 + positionCamera._y * rotation._m01 + positionCamera._z * rotation._m02 + rotation._m03, positionCamera._x * rotation._m10 + positionCamera._y * rotation._m11 + positionCamera._z * rotation._m12 + rotation._m13, positionCamera._x * rotation._m20 + positionCamera._y * rotation._m21 + positionCamera._z * rotation._m22 + rotation._m23);
  new MutableVector3D_0(viewDirection._x * rotation._m00 + viewDirection._y * rotation._m01 + viewDirection._z * rotation._m02, viewDirection._x * rotation._m10 + viewDirection._y * rotation._m11 + viewDirection._z * rotation._m12, viewDirection._x * rotation._m20 + viewDirection._y * rotation._m21 + viewDirection._z * rotation._m22);
  ray0 = new MutableVector3D_0(ray0._x * rotation._m00 + ray0._y * rotation._m01 + ray0._z * rotation._m02, ray0._x * rotation._m10 + ray0._y * rotation._m11 + ray0._z * rotation._m12, ray0._x * rotation._m20 + ray0._y * rotation._m21 + ray0._z * rotation._m22);
  new MutableVector3D_0(ray1._x * rotation._m00 + ray1._y * rotation._m01 + ray1._z * rotation._m02, ray1._x * rotation._m10 + ray1._y * rotation._m11 + ray1._z * rotation._m12, ray1._x * rotation._m20 + ray1._y * rotation._m21 + ray1._z * rotation._m22);
  $copyValueOfMultiplication(matrix, rotation, matrix);
  normal = $normalized_0($times_3(centerPoint2, this$static._ellipsoid._oneOverRadiiSquared));
  v0 = $projectionInPlane($sub_2($asVector3D(this$static._initialPoint0), centerPoint2), normal);
  p0 = $closestIntersection(this$static, new Vector3D(positionCamera._x, positionCamera._y, positionCamera._z), new Vector3D(ray0._x, ray0._y, ray0._z));
  v1 = $projectionInPlane(new Vector3D(p0._x - centerPoint2._x, p0._y - centerPoint2._y, p0._z - centerPoint2._z), normal);
  angle = fromRadians(angleInRadiansBetween_0(v0, v1))._degrees;
  sign = $dot_0(new Vector3D(v1._y * v0._z - v1._z * v0._y, v1._z * v0._x - v1._x * v0._z, v1._x * v0._y - v1._y * v0._x), normal);
  sign < 0 && (angle = -angle);
  rotation = createGeneralRotationMatrix(new Angle(angle, angle / 180 * $intern_37), normal, centerPoint2);
  $copyValueOfMultiplication(matrix, rotation, matrix);
  return matrix;
}

function $drag(this$static, origin, destination){
  var P0, P1, angle, axis_0, rotatedP0, rotation, traslation;
  P0 = $toCartesian(this$static, origin._latitude, origin._longitude, origin._height);
  P1 = $toCartesian(this$static, destination._latitude, destination._longitude, destination._height);
  axis_0 = new Vector3D(P0._y * P1._z - P0._z * P1._y, P0._z * P1._x - P0._x * P1._z, P0._x * P1._y - P0._y * P1._x);
  if (sqrt_0(axis_0._x * axis_0._x + axis_0._y * axis_0._y + axis_0._z * axis_0._z) < 0.001)
    return new MutableMatrix44D_2;
  angle = fromRadians(angleInRadiansBetween_0(P0, P1));
  rotation = createRotationMatrix(angle, axis_0);
  rotatedP0 = new Vector3D(P0._x * rotation._m00 + P0._y * rotation._m01 + P0._z * rotation._m02 + rotation._m03, P0._x * rotation._m10 + P0._y * rotation._m11 + P0._z * rotation._m12 + rotation._m13, P0._x * rotation._m20 + P0._y * rotation._m21 + P0._z * rotation._m22 + rotation._m23);
  traslation = createTranslationMatrix(new Vector3D(P1._x - rotatedP0._x, P1._y - rotatedP0._y, P1._z - rotatedP0._z));
  return $multiply(traslation, rotation);
}

function $geodeticSurfaceNormal(this$static, positionOnEllipsoidalPlanet){
  return $asVector3D($normalized($times_1(positionOnEllipsoidalPlanet, this$static._ellipsoid._oneOverRadiiSquared)));
}

function $getDefaultCameraPosition(this$static, rendereSector){
  var ane, asw, height;
  asw = $toCartesian_0(this$static, rendereSector._lower);
  ane = $toCartesian_0(this$static, rendereSector._upper);
  height = $length_1(new Vector3D(asw._x - ane._x, asw._y - ane._y, asw._z - ane._z)) * 1.9;
  return new Geodetic3D_0(rendereSector._center, height);
}

function $getMidPoint(this$static, P0, P1){
  var midPoint, normal, theta, v0, v1;
  v0 = $toCartesian(this$static, P0._latitude, P0._longitude, 0);
  v1 = $toCartesian(this$static, P1._latitude, P1._longitude, 0);
  normal = $normalized_0(new Vector3D(v0._y * v1._z - v0._z * v1._y, v0._z * v1._x - v0._x * v1._z, v0._x * v1._y - v0._y * v1._x));
  theta = fromRadians(angleInRadiansBetween_0(v0, v1));
  midPoint = $scaleToGeocentricSurface(this$static, $rotateAroundAxis_0(v0, normal, $times(theta, 0.5)));
  return $toGeodetic2D(this$static, midPoint);
}

function $intersectionsDistances_0(this$static, originX, originY, originZ, directionX, directionY, directionZ){
  return $intersectionsDistances(this$static._ellipsoid, originX, originY, originZ, directionX, directionY, directionZ);
}

function $scaleToGeocentricSurface(this$static, position){
  var beta_0, oneOverRadiiSquared;
  oneOverRadiiSquared = this$static._ellipsoid._oneOverRadiiSquared;
  beta_0 = 1 / sqrt_0(position._x * position._x * oneOverRadiiSquared._x + position._y * position._y * oneOverRadiiSquared._y + position._z * position._z * oneOverRadiiSquared._z);
  return new Vector3D(position._x * beta_0, position._y * beta_0, position._z * beta_0);
}

function $scaleToGeodeticSurface(this$static, position){
  var alpha_0, beta_0, dSdA, da, da2, da3, db, db2, db3, dc, dc2, dc3, n, oneOverRadiiSquared, radiiSquared, radiiToTheFourth, s, x2, y2, z2;
  oneOverRadiiSquared = this$static._ellipsoid._oneOverRadiiSquared;
  radiiSquared = this$static._ellipsoid._radiiSquared;
  radiiToTheFourth = this$static._ellipsoid._radiiToTheFourth;
  beta_0 = 1 / sqrt_0(position._x * position._x * oneOverRadiiSquared._x + position._y * position._y * oneOverRadiiSquared._y + position._z * position._z * oneOverRadiiSquared._z);
  n = $length_1(new Vector3D(beta_0 * position._x * oneOverRadiiSquared._x, beta_0 * position._y * oneOverRadiiSquared._y, beta_0 * position._z * oneOverRadiiSquared._z));
  alpha_0 = (1 - beta_0) * (sqrt_0(position._x * position._x + position._y * position._y + position._z * position._z) / n);
  x2 = position._x * position._x;
  y2 = position._y * position._y;
  z2 = position._z * position._z;
  s = 0;
  dSdA = 1;
  do {
    alpha_0 -= s / dSdA;
    da = 1 + alpha_0 * oneOverRadiiSquared._x;
    db = 1 + alpha_0 * oneOverRadiiSquared._y;
    dc = 1 + alpha_0 * oneOverRadiiSquared._z;
    da2 = da * da;
    db2 = db * db;
    dc2 = dc * dc;
    da3 = da * da2;
    db3 = db * db2;
    dc3 = dc * dc2;
    s = x2 / (radiiSquared._x * da2) + y2 / (radiiSquared._y * db2) + z2 / (radiiSquared._z * dc2) - 1;
    dSdA = -2 * (x2 / (radiiToTheFourth._x * da3) + y2 / (radiiToTheFourth._y * db3) + z2 / (radiiToTheFourth._z * dc3));
  }
   while ((s <= 0?0 - s:s) > 1.0E-10);
  return new Vector3D(position._x / da, position._y / db, position._z / dc);
}

function $singleDrag(this$static, finalRay){
  var finalPoint, origin, radians, rotationAxis, rotationDelta, sinus;
  if ($isNan_0(this$static._initialPoint))
    return new MutableMatrix44D_2;
  origin = $asVector3D(this$static._origin);
  finalPoint = $asMutableVector3D($closestIntersection(this$static, origin, finalRay));
  if (finalPoint._x != finalPoint._x || finalPoint._y != finalPoint._y || finalPoint._z != finalPoint._z) {
    $copyFrom_2(finalPoint, $closestPointToSphere(this$static, origin, finalRay));
    if (finalPoint._x != finalPoint._x || finalPoint._y != finalPoint._y || finalPoint._z != finalPoint._z) {
      $logWarning(_instance_3, 'EllipsoidalPlanet::singleDrag-> finalPoint is NaN', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
      return new MutableMatrix44D_2;
    }
  }
  rotationAxis = $asVector3D($cross(this$static._initialPoint, finalPoint));
  sinus = sqrt_0(rotationAxis._x * rotationAxis._x + rotationAxis._y * rotationAxis._y + rotationAxis._z * rotationAxis._z) / $length(this$static._initialPoint) / sqrt_0(finalPoint._x * finalPoint._x + finalPoint._y * finalPoint._y + finalPoint._z * finalPoint._z);
  rotationDelta = fromRadians(-asin_0(sinus));
  if (rotationDelta._degrees != rotationDelta._degrees)
    return new MutableMatrix44D_2;
  $copyFrom_2(this$static._lastDragAxis, rotationAxis);
  radians = rotationDelta._radians;
  this$static._lastDragRadiansStep = radians - this$static._lastDragRadians;
  this$static._lastDragRadians = radians;
  this$static._validSingleDrag = true;
  return createRotationMatrix(rotationDelta, rotationAxis);
}

function $toCartesian(this$static, latitude, longitude, height){
  var gamma_0, k, n, rSurface, cosLatitude;
  n = (cosLatitude = cos_0(latitude._radians) , new Vector3D(cosLatitude * cos_0(longitude._radians), cosLatitude * sin_0(longitude._radians), sin_0(latitude._radians)));
  k = $times_3(this$static._ellipsoid._radiiSquared, n);
  gamma_0 = sqrt_0(k._x * n._x + k._y * n._y + k._z * n._z);
  rSurface = new Vector3D(k._x / gamma_0, k._y / gamma_0, k._z / gamma_0);
  return $add_18(rSurface, new Vector3D(n._x * height, n._y * height, n._z * height));
}

function $toCartesian_0(this$static, geodetic){
  return $toCartesian(this$static, geodetic._latitude, geodetic._longitude, 0);
}

function $toCartesian_1(this$static, geodetic, height){
  return $toCartesian(this$static, geodetic._latitude, geodetic._longitude, height);
}

function $toGeodetic2D(this$static, positionOnEllipsoidalPlanet){
  var n;
  n = $normalized_0($times_3(positionOnEllipsoidalPlanet, this$static._ellipsoid._oneOverRadiiSquared));
  return new Geodetic2D(fromRadians(asin_0(n._z)), fromRadians(atan2_0(n._y, n._x)));
}

function $toGeodetic3D(this$static, position){
  var h, height, p;
  p = $scaleToGeodeticSurface(this$static, position);
  h = new Vector3D(position._x - p._x, position._y - p._y, position._z - p._z);
  height = h._x * position._x + h._y * position._y + h._z * position._z < 0?-sqrt_0(h._x * h._x + h._y * h._y + h._z * h._z):sqrt_0(h._x * h._x + h._y * h._y + h._z * h._z);
  return new Geodetic3D_0($toGeodetic2D(this$static, p), height);
}

function EllipsoidalPlanet(ellipsoid){
  this._origin = new MutableVector3D;
  this._initialPoint = new MutableVector3D;
  this._centerPoint = new MutableVector3D;
  this._centerRay = new MutableVector3D;
  this._initialPoint0 = new MutableVector3D;
  this._initialPoint1 = new MutableVector3D;
  this._lastDragAxis = new MutableVector3D;
  this._ellipsoid = ellipsoid;
}

defineClass(187, 454, {}, EllipsoidalPlanet);
_._angleBetweenInitialPoints = 0;
_._angleBetweenInitialRays = 0;
_._lastDragRadians = 0;
_._lastDragRadiansStep = 0;
_._validSingleDrag = false;
var Lorg_glob3_mobile_generated_EllipsoidalPlanet_2_classLit = createForClass('org.glob3.mobile.generated', 'EllipsoidalPlanet', 187);
function FlatColorGLFeature(color_0, blend, sFactor, dFactor){
  GLColorGroupFeature.call(this, 8, 2, blend, sFactor, dFactor);
  $addUniformValue(this._values, ($clinit_GPUUniformKey() , FLAT_COLOR), new GPUUniformValueVec4Float(color_0._red, color_0._green, color_0._blue, color_0._alpha));
}

defineClass(204, 128, $intern_42, FlatColorGLFeature);
_.applyOnGlobalGLState = function applyOnGlobalGLState_1(state){
  $blendingOnGlobalGLState(this, state);
}
;
_.dispose = function dispose_23(){
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_FlatColorGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'FlatColorGLFeature', 204);
function $dispose_2(this$static){
  this$static._ownedMesh && !!this$static._mesh && $dispose_5(this$static._mesh);
  $_release(this$static._glState);
}

function FlatColorMesh(mesh, color_0){
  Mesh.call(this);
  this._mesh = mesh;
  this._ownedMesh = false;
  this._flatColor = color_0;
  this._glState = new GLState;
  $addGLFeature(this._glState, new FlatColorGLFeature(this._flatColor, this._flatColor._alpha < 1, _srcAlpha, _oneMinusSrcAlpha), false);
}

defineClass(378, 102, {}, FlatColorMesh);
_.rawRender = function rawRender_2(rc, parentState){
  $setParent_2(this._glState, parentState);
  $render(this._mesh, rc, this._glState);
}
;
_._ownedMesh = false;
var Lorg_glob3_mobile_generated_FlatColorMesh_2_classLit = createForClass('org.glob3.mobile.generated', 'FlatColorMesh', 378);
function $create(this$static){
  return new FloatBuffer_WebGL_0(this$static._values._array, this$static._values._size);
}

function $getVector2D(this$static, i_0){
  var pos;
  pos = i_0 * 2;
  return new Vector2D($get_2(this$static._values, pos), $get_2(this$static._values, pos + 1));
}

function FloatBufferBuilder(){
  this._values = new FloatBufferBuilder$FloatArrayList;
}

defineClass(122, 1, {});
var Lorg_glob3_mobile_generated_FloatBufferBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'FloatBufferBuilder', 122);
function $ensureCapacity(this$static, mincap){
  var newcap, olddata;
  if (mincap > this$static._array.length) {
    newcap = (this$static._array.length * 3 >> 1) + 1;
    olddata = this$static._array;
    this$static._array = initDim(F_classLit, $intern_29, 0, newcap < mincap?mincap:newcap, 7, 1);
    arraycopy(olddata, 0, this$static._array, 0, this$static._size);
  }
}

function $get_2(this$static, index_0){
  return this$static._array[index_0];
}

function $push_back(this$static, element){
  $ensureCapacity(this$static, this$static._size + 1);
  this$static._array[this$static._size++] = element;
}

function FloatBufferBuilder$FloatArrayList(){
  this._array = initDim(F_classLit, $intern_29, 0, 1024, 7, 1);
  this._size = 0;
}

defineClass(196, 1, {}, FloatBufferBuilder$FloatArrayList);
_._size = 0;
var Lorg_glob3_mobile_generated_FloatBufferBuilder$FloatArrayList_2_classLit = createForClass('org.glob3.mobile.generated', 'FloatBufferBuilder/FloatArrayList', 196);
function $add_8(this$static, x_0, y_0){
  $push_back(this$static._values, x_0);
  $push_back(this$static._values, y_0);
}

function FloatBufferBuilderFromCartesian2D(){
  FloatBufferBuilder.call(this);
}

defineClass(201, 122, {}, FloatBufferBuilderFromCartesian2D);
var Lorg_glob3_mobile_generated_FloatBufferBuilderFromCartesian2D_2_classLit = createForClass('org.glob3.mobile.generated', 'FloatBufferBuilderFromCartesian2D', 201);
function $add_9(this$static, x_0, y_0){
  this$static._centerStrategy == 1 && this$static._values._size == 0 && $setCenter_0(this$static, x_0, y_0, 0);
  if (this$static._centerStrategy == 0) {
    $push_back(this$static._values, x_0);
    $push_back(this$static._values, y_0);
    $push_back(this$static._values, 0);
  }
   else {
    $push_back(this$static._values, x_0 - this$static._cx);
    $push_back(this$static._values, y_0 - this$static._cy);
    $push_back(this$static._values, 0 - this$static._cz);
  }
}

function $setCenter_0(this$static, x_0, y_0, z_0){
  this$static._cx = x_0;
  this$static._cy = y_0;
  this$static._cz = z_0;
}

function FloatBufferBuilderFromCartesian3D(center){
  FloatBufferBuilder.call(this);
  this._centerStrategy = 0;
  $setCenter_0(this, center._x, center._y, center._z);
}

defineClass(195, 122, {}, FloatBufferBuilderFromCartesian3D);
_._centerStrategy = 0;
_._cx = 0;
_._cy = 0;
_._cz = 0;
var Lorg_glob3_mobile_generated_FloatBufferBuilderFromCartesian3D_2_classLit = createForClass('org.glob3.mobile.generated', 'FloatBufferBuilderFromCartesian3D', 195);
function $add_10(this$static, a){
  $push_back(this$static._values, 1);
  $push_back(this$static._values, 1);
  $push_back(this$static._values, 1);
  $push_back(this$static._values, a);
}

function FloatBufferBuilderFromColor(){
  FloatBufferBuilder.call(this);
}

defineClass(379, 122, {}, FloatBufferBuilderFromColor);
var Lorg_glob3_mobile_generated_FloatBufferBuilderFromColor_2_classLit = createForClass('org.glob3.mobile.generated', 'FloatBufferBuilderFromColor', 379);
function $add_11(this$static, latitude, longitude, height){
  var vector;
  vector = $toCartesian(this$static._planet, latitude, longitude, height);
  this$static._centerStrategy == 1 && this$static._values._size == 0 && $setCenter_1(this$static, vector);
  if (this$static._centerStrategy == 0) {
    $push_back(this$static._values, vector._x);
    $push_back(this$static._values, vector._y);
    $push_back(this$static._values, vector._z);
  }
   else {
    $push_back(this$static._values, vector._x - this$static._cx);
    $push_back(this$static._values, vector._y - this$static._cy);
    $push_back(this$static._values, vector._z - this$static._cz);
  }
}

function $add_12(this$static, position, height){
  $add_11(this$static, position._latitude, position._longitude, height);
}

function $setCenter_1(this$static, center){
  this$static._cx = center._x;
  this$static._cy = center._y;
  this$static._cz = center._z;
}

function FloatBufferBuilderFromGeodetic(planet, center){
  FloatBufferBuilder.call(this);
  this._planet = planet;
  this._centerStrategy = 2;
  $setCenter_1(this, $toCartesian_0(this._planet, center));
}

defineClass(202, 122, {}, FloatBufferBuilderFromGeodetic);
_._centerStrategy = 0;
_._cx = 0;
_._cy = 0;
_._cz = 0;
var Lorg_glob3_mobile_generated_FloatBufferBuilderFromGeodetic_2_classLit = createForClass('org.glob3.mobile.generated', 'FloatBufferBuilderFromGeodetic', 202);
function $addPreRenderTask(this$static, task){
  $addLast(this$static._tasks, task);
}

function $canExecutePreRenderStep(this$static, rc, executedCounter){
  var tasksCount;
  tasksCount = this$static._tasks.size_0;
  if (tasksCount <= this$static._minimumExecutionsPerFrame) {
    this$static._debug && this$static._stressed && $logWarning(rc._logger, 'FTE: Abandon STRESSED mode', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    this$static._stressed = false;
  }
  if (tasksCount == 0) {
    return false;
  }
  if (executedCounter < this$static._minimumExecutionsPerFrame) {
    return true;
  }
  if (tasksCount > this$static._maximumQueuedTasks) {
    this$static._debug && (this$static._stressed || $logWarning(rc._logger, 'FTE: Too many queued tasks (%d). Goes to STRESSED mode', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(this$static._tasks.size_0)])));
    this$static._stressed = true;
  }
  if (this$static._stressed) {
    return lt($elapsedTimeInMilliseconds(rc._frameStartTimer), this$static._maxTimePerFrameStressedMS);
  }
  if (executedCounter >= this$static._maximumExecutionsPerFrame) {
    return false;
  }
  return lt($elapsedTimeInMilliseconds(rc._frameStartTimer), this$static._maxTimePerFrameMS);
}

function $doPreRenderCycle(this$static, rc){
  var canceledCounter, executedCounter, i_0, isCanceled, task;
  if (this$static._tasks.size_0 == 0) {
    return;
  }
  canceledCounter = 0;
  i_0 = $listIterator(this$static._tasks, 0);
  while (i_0.currentNode != i_0.this$01.tail) {
    task = (checkCriticalElement(i_0.currentNode != i_0.this$01.tail) , i_0.lastNode = i_0.currentNode , i_0.currentNode = i_0.currentNode.next , ++i_0.currentIndex , dynamicCast(i_0.lastNode.value_0, 103));
    isCanceled = task.isCanceled(rc);
    if (isCanceled) {
      !!task && task.dispose();
      $remove_9(i_0);
      ++canceledCounter;
    }
  }
  this$static._debug && canceledCounter > 0 && $logInfo(rc._logger, 'FTE: Removed %d tasks, active %d tasks.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(canceledCounter), valueOf(this$static._tasks.size_0)]));
  executedCounter = 0;
  while ($canExecutePreRenderStep(this$static, rc, executedCounter)) {
    task = dynamicCast($getFirst(this$static._tasks), 103);
    $removeFirst(this$static._tasks);
    task.execute_1(rc);
    !!task && task.dispose();
    ++executedCounter;
  }
  this$static._debug && $showDebugInfo(this$static, rc, executedCounter, canceledCounter);
}

function $showDebugInfo(this$static, rc, executedCounter, canceledCounter){
  var isb, msg, preRenderTasksSize;
  preRenderTasksSize = this$static._tasks.size_0;
  if (executedCounter > 0 || canceledCounter > 0 || preRenderTasksSize > 0) {
    isb = new StringBuilder_WebGL;
    isb._string += 'FTE: Tasks';
    if (canceledCounter > 0) {
      isb._string += ' canceled=';
      isb._string += canceledCounter;
    }
    if (executedCounter > 0) {
      isb._string += ' executed=';
      isb._string += executedCounter;
      isb._string += ' in ';
      $addLong(isb, $elapsedTimeInMilliseconds(rc._frameStartTimer));
      isb._string += 'ms';
    }
    isb._string += ' queued=';
    isb._string += toString_8(fromInt(preRenderTasksSize));
    this$static._stressed && (isb._string += ' *Stressed*' , isb);
    msg = isb._string;
    $logInfo(rc._logger, msg, initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
}

function FrameTasksExecutor(){
  this._tasks = new LinkedList;
  this._minimumExecutionsPerFrame = 1;
  this._maximumExecutionsPerFrame = 4;
  this._maximumQueuedTasks = 64;
  this._maxTimePerFrameMS = {l:10, m:0, h:0};
  this._maxTimePerFrameStressedMS = {l:20, m:0, h:0};
  this._stressed = false;
  this._debug = false;
}

defineClass(350, 1, {}, FrameTasksExecutor);
_._debug = false;
_._maxTimePerFrameMS = {l:0, m:0, h:0};
_._maxTimePerFrameStressedMS = {l:0, m:0, h:0};
_._maximumExecutionsPerFrame = 0;
_._maximumQueuedTasks = 0;
_._minimumExecutionsPerFrame = 0;
_._stressed = false;
var Lorg_glob3_mobile_generated_FrameTasksExecutor_2_classLit = createForClass('org.glob3.mobile.generated', 'FrameTasksExecutor', 350);
function $computeBoundingVolume_0(this$static){
  var maxx, maxy, maxz, minx, miny, minz;
  minx = $intern_45;
  miny = $intern_45;
  minz = $intern_45;
  maxx = $intern_46;
  maxy = $intern_46;
  maxz = $intern_46;
  this$static._ltn._x < $intern_45 && (minx = this$static._ltn._x);
  this$static._ltn._x > $intern_46 && (maxx = this$static._ltn._x);
  this$static._ltn._y < $intern_45 && (miny = this$static._ltn._y);
  this$static._ltn._y > $intern_46 && (maxy = this$static._ltn._y);
  this$static._ltn._z < $intern_45 && (minz = this$static._ltn._z);
  this$static._ltn._z > $intern_46 && (maxz = this$static._ltn._z);
  this$static._rtn._x < minx && (minx = this$static._rtn._x);
  this$static._rtn._x > maxx && (maxx = this$static._rtn._x);
  this$static._rtn._y < miny && (miny = this$static._rtn._y);
  this$static._rtn._y > maxy && (maxy = this$static._rtn._y);
  this$static._rtn._z < minz && (minz = this$static._rtn._z);
  this$static._rtn._z > maxz && (maxz = this$static._rtn._z);
  this$static._lbn._x < minx && (minx = this$static._lbn._x);
  this$static._lbn._x > maxx && (maxx = this$static._lbn._x);
  this$static._lbn._y < miny && (miny = this$static._lbn._y);
  this$static._lbn._y > maxy && (maxy = this$static._lbn._y);
  this$static._lbn._z < minz && (minz = this$static._lbn._z);
  this$static._lbn._z > maxz && (maxz = this$static._lbn._z);
  this$static._rbn._x < minx && (minx = this$static._rbn._x);
  this$static._rbn._x > maxx && (maxx = this$static._rbn._x);
  this$static._rbn._y < miny && (miny = this$static._rbn._y);
  this$static._rbn._y > maxy && (maxy = this$static._rbn._y);
  this$static._rbn._z < minz && (minz = this$static._rbn._z);
  this$static._rbn._z > maxz && (maxz = this$static._rbn._z);
  this$static._ltf._x < minx && (minx = this$static._ltf._x);
  this$static._ltf._x > maxx && (maxx = this$static._ltf._x);
  this$static._ltf._y < miny && (miny = this$static._ltf._y);
  this$static._ltf._y > maxy && (maxy = this$static._ltf._y);
  this$static._ltf._z < minz && (minz = this$static._ltf._z);
  this$static._ltf._z > maxz && (maxz = this$static._ltf._z);
  this$static._rtf._x < minx && (minx = this$static._rtf._x);
  this$static._rtf._x > maxx && (maxx = this$static._rtf._x);
  this$static._rtf._y < miny && (miny = this$static._rtf._y);
  this$static._rtf._y > maxy && (maxy = this$static._rtf._y);
  this$static._rtf._z < minz && (minz = this$static._rtf._z);
  this$static._rtf._z > maxz && (maxz = this$static._rtf._z);
  this$static._lbf._x < minx && (minx = this$static._lbf._x);
  this$static._lbf._x > maxx && (maxx = this$static._lbf._x);
  this$static._lbf._y < miny && (miny = this$static._lbf._y);
  this$static._lbf._y > maxy && (maxy = this$static._lbf._y);
  this$static._lbf._z < minz && (minz = this$static._lbf._z);
  this$static._lbf._z > maxz && (maxz = this$static._lbf._z);
  this$static._rbf._x < minx && (minx = this$static._rbf._x);
  this$static._rbf._x > maxx && (maxx = this$static._rbf._x);
  this$static._rbf._y < miny && (miny = this$static._rbf._y);
  this$static._rbf._y > maxy && (maxy = this$static._rbf._y);
  this$static._rbf._z < minz && (minz = this$static._rbf._z);
  this$static._rbf._z > maxz && (maxz = this$static._rbf._z);
  return new Box(new Vector3D(minx, miny, minz), new Vector3D(maxx, maxy, maxz));
}

function $touchesWithBox(this$static, that){
  var corners;
  !this$static._boundingVolume && (this$static._boundingVolume = $computeBoundingVolume_0(this$static));
  if (!$touchesBox(this$static._boundingVolume, that)) {
    return false;
  }
  corners = $getCornersArray(that);
  return !($signedDistance(this$static._leftPlane, corners[0]) >= 0 && $signedDistance(this$static._leftPlane, corners[1]) >= 0 && $signedDistance(this$static._leftPlane, corners[2]) >= 0 && $signedDistance(this$static._leftPlane, corners[3]) >= 0 && $signedDistance(this$static._leftPlane, corners[4]) >= 0 && $signedDistance(this$static._leftPlane, corners[5]) >= 0 && $signedDistance(this$static._leftPlane, corners[6]) >= 0 && $signedDistance(this$static._leftPlane, corners[7]) >= 0) && !($signedDistance(this$static._bottomPlane, corners[0]) >= 0 && $signedDistance(this$static._bottomPlane, corners[1]) >= 0 && $signedDistance(this$static._bottomPlane, corners[2]) >= 0 && $signedDistance(this$static._bottomPlane, corners[3]) >= 0 && $signedDistance(this$static._bottomPlane, corners[4]) >= 0 && $signedDistance(this$static._bottomPlane, corners[5]) >= 0 && $signedDistance(this$static._bottomPlane, corners[6]) >= 0 && $signedDistance(this$static._bottomPlane, corners[7]) >= 0) && !($signedDistance(this$static._rightPlane, corners[0]) >= 0 && $signedDistance(this$static._rightPlane, corners[1]) >= 0 && $signedDistance(this$static._rightPlane, corners[2]) >= 0 && $signedDistance(this$static._rightPlane, corners[3]) >= 0 && $signedDistance(this$static._rightPlane, corners[4]) >= 0 && $signedDistance(this$static._rightPlane, corners[5]) >= 0 && $signedDistance(this$static._rightPlane, corners[6]) >= 0 && $signedDistance(this$static._rightPlane, corners[7]) >= 0) && !($signedDistance(this$static._topPlane, corners[0]) >= 0 && $signedDistance(this$static._topPlane, corners[1]) >= 0 && $signedDistance(this$static._topPlane, corners[2]) >= 0 && $signedDistance(this$static._topPlane, corners[3]) >= 0 && $signedDistance(this$static._topPlane, corners[4]) >= 0 && $signedDistance(this$static._topPlane, corners[5]) >= 0 && $signedDistance(this$static._topPlane, corners[6]) >= 0 && $signedDistance(this$static._topPlane, corners[7]) >= 0) && !($signedDistance(this$static._nearPlane, corners[0]) >= 0 && $signedDistance(this$static._nearPlane, corners[1]) >= 0 && $signedDistance(this$static._nearPlane, corners[2]) >= 0 && $signedDistance(this$static._nearPlane, corners[3]) >= 0 && $signedDistance(this$static._nearPlane, corners[4]) >= 0 && $signedDistance(this$static._nearPlane, corners[5]) >= 0 && $signedDistance(this$static._nearPlane, corners[6]) >= 0 && $signedDistance(this$static._nearPlane, corners[7]) >= 0) && !($signedDistance(this$static._farPlane, corners[0]) >= 0 && $signedDistance(this$static._farPlane, corners[1]) >= 0 && $signedDistance(this$static._farPlane, corners[2]) >= 0 && $signedDistance(this$static._farPlane, corners[3]) >= 0 && $signedDistance(this$static._farPlane, corners[4]) >= 0 && $signedDistance(this$static._farPlane, corners[5]) >= 0 && $signedDistance(this$static._farPlane, corners[6]) >= 0 && $signedDistance(this$static._farPlane, corners[7]) >= 0);
}

function $transformedBy_P(this$static, matrix){
  return new Frustum(this$static, matrix, $inversed(matrix));
}

function Frustum(that, matrix, inverse){
  this._ltn = new Vector3D_0($transformedBy_0(that._ltn, inverse, 1));
  this._rtn = new Vector3D_0($transformedBy_0(that._rtn, inverse, 1));
  this._lbn = new Vector3D_0($transformedBy_0(that._lbn, inverse, 1));
  this._rbn = new Vector3D_0($transformedBy_0(that._rbn, inverse, 1));
  this._ltf = new Vector3D_0($transformedBy_0(that._ltf, inverse, 1));
  this._rtf = new Vector3D_0($transformedBy_0(that._rtf, inverse, 1));
  this._lbf = new Vector3D_0($transformedBy_0(that._lbf, inverse, 1));
  this._rbf = new Vector3D_0($transformedBy_0(that._rbf, inverse, 1));
  this._leftPlane = $transformedByTranspose(that._leftPlane, matrix);
  this._rightPlane = $transformedByTranspose(that._rightPlane, matrix);
  this._bottomPlane = $transformedByTranspose(that._bottomPlane, matrix);
  this._topPlane = $transformedByTranspose(that._topPlane, matrix);
  this._nearPlane = $transformedByTranspose(that._nearPlane, matrix);
  this._farPlane = $transformedByTranspose(that._farPlane, matrix);
  this._boundingVolume = null;
}

function Frustum_0(data_0){
  this._ltn = new Vector3D_0(new Vector3D(data_0._left, data_0._top, -data_0._znear));
  this._rtn = new Vector3D_0(new Vector3D(data_0._right, data_0._top, -data_0._znear));
  this._lbn = new Vector3D_0(new Vector3D(data_0._left, data_0._bottom, -data_0._znear));
  this._rbn = new Vector3D_0(new Vector3D(data_0._right, data_0._bottom, -data_0._znear));
  this._ltf = new Vector3D_0(new Vector3D(data_0._zfar / data_0._znear * data_0._left, data_0._zfar / data_0._znear * data_0._top, -data_0._zfar));
  this._rtf = new Vector3D_0(new Vector3D(data_0._zfar / data_0._znear * data_0._right, data_0._zfar / data_0._znear * data_0._top, -data_0._zfar));
  this._lbf = new Vector3D_0(new Vector3D(data_0._zfar / data_0._znear * data_0._left, data_0._zfar / data_0._znear * data_0._bottom, -data_0._zfar));
  this._rbf = new Vector3D_0(new Vector3D(data_0._zfar / data_0._znear * data_0._right, data_0._zfar / data_0._znear * data_0._bottom, -data_0._zfar));
  this._leftPlane = fromPoints(($clinit_Vector3D() , zero), new Vector3D(data_0._left, data_0._top, -data_0._znear), new Vector3D(data_0._left, data_0._bottom, -data_0._znear));
  this._bottomPlane = fromPoints(zero, new Vector3D(data_0._left, data_0._bottom, -data_0._znear), new Vector3D(data_0._right, data_0._bottom, -data_0._znear));
  this._rightPlane = fromPoints(zero, new Vector3D(data_0._right, data_0._bottom, -data_0._znear), new Vector3D(data_0._right, data_0._top, -data_0._znear));
  this._topPlane = fromPoints(zero, new Vector3D(data_0._right, data_0._top, -data_0._znear), new Vector3D(data_0._left, data_0._top, -data_0._znear));
  this._nearPlane = new Plane_0(new Vector3D(0, 0, 1), data_0._znear);
  this._farPlane = new Plane_0(new Vector3D(0, 0, -1), -data_0._zfar);
  this._boundingVolume = null;
}

defineClass(193, 1, {}, Frustum, Frustum_0);
var Lorg_glob3_mobile_generated_Frustum_2_classLit = createForClass('org.glob3.mobile.generated', 'Frustum', 193);
function FrustumData(){
  this._left = -1;
  this._right = 1;
  this._bottom = -1;
  this._top = 1;
  this._znear = 1;
  this._zfar = 10;
}

function FrustumData_0(left, right, bottom, top_0, znear, zfar){
  this._left = left;
  this._right = right;
  this._bottom = bottom;
  this._top = top_0;
  this._znear = znear;
  this._zfar = zfar;
}

defineClass(147, 1, {}, FrustumData, FrustumData_0);
_._bottom = 0;
_._left = 0;
_._right = 0;
_._top = 0;
_._zfar = 0;
_._znear = 0;
var Lorg_glob3_mobile_generated_FrustumData_2_classLit = createForClass('org.glob3.mobile.generated', 'FrustumData', 147);
function G3MContext(factory, threadUtils, logger, planet, downloader, effectsScheduler){
  this._factory = factory;
  this._threadUtils = threadUtils;
  this._logger = logger;
  this._planet = planet;
  this._downloader = downloader;
  this._effectsScheduler = effectsScheduler;
}

defineClass(141, 1, {}, G3MContext);
var Lorg_glob3_mobile_generated_G3MContext_2_classLit = createForClass('org.glob3.mobile.generated', 'G3MContext', 141);
function G3MEventContext(factory, threadUtils, logger, planet, downloader, scheduler){
  G3MContext.call(this, factory, threadUtils, logger, planet, downloader, scheduler);
}

defineClass(184, 141, {}, G3MEventContext);
var Lorg_glob3_mobile_generated_G3MEventContext_2_classLit = createForClass('org.glob3.mobile.generated', 'G3MEventContext', 184);
function G3MRenderContext(frameTasksExecutor, factory, threadUtils, logger, planet, gl, currentCamera, nextCamera, texturesHandler, downloader, scheduler, frameStartTimer, gpuProgramManager){
  G3MContext.call(this, factory, threadUtils, logger, planet, downloader, scheduler);
  this._frameTasksExecutor = frameTasksExecutor;
  this._gl = gl;
  this._currentCamera = currentCamera;
  this._nextCamera = nextCamera;
  this._texturesHandler = texturesHandler;
  this._frameStartTimer = frameStartTimer;
  this._gpuProgramManager = gpuProgramManager;
}

defineClass(352, 141, {}, G3MRenderContext);
var Lorg_glob3_mobile_generated_G3MRenderContext_2_classLit = createForClass('org.glob3.mobile.generated', 'G3MRenderContext', 352);
function $addPeriodicalTask(this$static, periodicalTask){
  $add_3(this$static._periodicalTasks, periodicalTask);
}

function $calculateRendererState(this$static){
  var busyFlag, cameraRendererRenderState, mainRendererRenderState;
  if (this$static._forceBusyRenderer) {
    return $clinit_RenderState() , $clinit_RenderState() , BUSY;
  }
  if (!this$static._initializationTaskReady) {
    return $clinit_RenderState() , $clinit_RenderState() , BUSY;
  }
  busyFlag = false;
  cameraRendererRenderState = ($clinit_RenderState() , $clinit_RenderState() , READY);
  if (cameraRendererRenderState._type == 2) {
    return cameraRendererRenderState;
  }
   else 
    cameraRendererRenderState._type == 1 && (busyFlag = true);
  mainRendererRenderState = this$static._mainRenderer.getRenderState(this$static._renderContext);
  if (mainRendererRenderState._type == 2) {
    return mainRendererRenderState;
  }
   else 
    mainRendererRenderState._type == 1 && (busyFlag = true);
  return busyFlag?(null , BUSY):(null , READY);
}

function $notifyTouchEvent(this$static, ec, touchEvent){
  var handled, renderStateType;
  renderStateType = this$static._rendererState._type;
  switch (renderStateType) {
    case 0:
      {
        handled = false;
        this$static._mainRenderer.isEnable() && (handled = this$static._mainRenderer.onTouchEvent_0(ec, touchEvent));
        handled || $onTouchEvent(this$static._cameraRenderer, ec, touchEvent);
        break;
      }

  }
}

function $onResizeViewportEvent_0(this$static, width_0, height){
  var ec;
  ec = new G3MEventContext(_instance_1, this$static._threadUtils, _instance_3, this$static._planet, this$static._downloader, this$static._effectsScheduler);
  $resizeViewport(this$static._nextCamera, width_0, height);
  $resizeViewport(this$static._currentCamera, width_0, height);
  this$static._mainRenderer.onResizeViewportEvent(ec, width_0, height);
  $onResizeViewportEvent(this$static._busyRenderer, width_0, height);
  $onResizeViewportEvent_1(this$static._errorRenderer, ec, width_0, height);
}

function $onTouchEvent_0(this$static, touchEvent){
  var downUpEvent, ec, eventType, movePosition, pos, sd, touch;
  ec = new G3MEventContext(_instance_1, this$static._threadUtils, _instance_3, this$static._planet, this$static._downloader, this$static._effectsScheduler);
  $notifyTouchEvent(this$static, ec, touchEvent);
  if (touchEvent._touchs.array.length == 1) {
    eventType = touchEvent._eventType;
    if (eventType == ($clinit_TouchEventType() , Down)) {
      this$static._clickOnProcess = true;
      pos = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
      this$static._touchDownPositionX = pos._x;
      this$static._touchDownPositionY = pos._y;
    }
     else {
      if (eventType == Up) {
        if (this$static._clickOnProcess) {
          touch = dynamicCast($get_0(touchEvent._touchs, 0), 10);
          downUpEvent = create_5(DownUp, new Touch_0(touch));
          $notifyTouchEvent(this$static, ec, downUpEvent);
          $dispose_14(downUpEvent);
        }
      }
      if (this$static._clickOnProcess) {
        if (eventType == Move) {
          movePosition = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
          sd = $squaredDistanceTo_0(movePosition, this$static._touchDownPositionX, this$static._touchDownPositionY);
          $getDeviceInfo(this$static._context._factory);
          sd > 14.28482899873035 && (this$static._clickOnProcess = false);
        }
         else {
          this$static._clickOnProcess = false;
        }
      }
    }
  }
   else {
    this$static._clickOnProcess = false;
  }
}

function $render_2(this$static, width_0, height){
  var averageTimePerRender, cacheStatistics, cameraConstrainersCount, constrainer, elapsedTimeMS, fps, i_0, i0, periodicalTasksCount, position, renderStateType;
  if (this$static._paused) {
    return;
  }
  if (this$static._width != width_0 || this$static._height != height) {
    this$static._width = width_0;
    this$static._height = height;
    $onResizeViewportEvent_0(this$static, this$static._width, this$static._height);
  }
  if (!this$static._initialCameraPositionHasBeenSet) {
    this$static._initialCameraPositionHasBeenSet = true;
    position = $getCameraPosition(this$static._planet, this$static._mainRenderer.getPlanetRenderer());
    $setGeodeticPosition(this$static._currentCamera, position);
    $setHeading(this$static._currentCamera, new Angle(0, 0));
    $setPitch(this$static._currentCamera, new Angle(-90, $intern_38));
    $setRoll(this$static._currentCamera, new Angle(0, 0));
    $setGeodeticPosition(this$static._nextCamera, position);
    $setHeading(this$static._nextCamera, new Angle(0, 0));
    $setPitch(this$static._nextCamera, new Angle(-90, $intern_38));
    $setRoll(this$static._nextCamera, new Angle(0, 0));
  }
  this$static._timer._startTimeInMilliseconds = fromDouble(now_1());
  ++this$static._renderCounter;
  periodicalTasksCount = this$static._periodicalTasks.array.length;
  for (i0 = 0; i0 < periodicalTasksCount; i0++) {
    throwClassCastExceptionUnlessNull($get_0(this$static._periodicalTasks, i0));
    null.nullMethod();
  }
  cameraConstrainersCount = this$static._cameraConstrainers.array.length;
  for (i_0 = 0; i_0 < cameraConstrainersCount; i_0++) {
    constrainer = dynamicCast($get_0(this$static._cameraConstrainers, i_0), 416);
    constrainer.onCameraChange(this$static._planet, this$static._currentCamera, this$static._nextCamera);
  }
  $copyFrom(this$static._currentCamera, this$static._nextCamera);
  this$static._rendererState = $calculateRendererState(this$static);
  renderStateType = this$static._rendererState._type;
  this$static._renderContext._frameStartTimer._startTimeInMilliseconds = fromDouble(now_1());
  $doOneCyle(this$static._effectsScheduler, this$static._renderContext);
  $doPreRenderCycle(this$static._frameTasksExecutor, this$static._renderContext);
  $clearScreen(this$static._gl, this$static._backgroundColor);
  !this$static._rootState && (this$static._rootState = new GLState);
  switch (renderStateType) {
    case 0:
      $setSelectedRenderer(this$static, this$static._mainRenderer);
      $render_1(this$static._cameraRenderer, this$static._renderContext);
      $modifyGLState(this$static._sceneLighting, this$static._rootState, this$static._renderContext);
      this$static._mainRenderer.isEnable() && this$static._mainRenderer.render(this$static._renderContext, this$static._rootState);
      break;
    case 1:
      $setSelectedRenderer(this$static, this$static._busyRenderer);
      $render_0(this$static._busyRenderer, this$static._renderContext);
      break;
    default:$setErrors(this$static._errorRenderer, this$static._rendererState._errors);
      $setSelectedRenderer(this$static, this$static._errorRenderer);
      $render_3(this$static._errorRenderer, this$static._renderContext, this$static._rootState);
  }
  this$static._renderCounter % this$static._nFramesBeetweenProgramsCleanUp == 0 && $removeUnused(this$static._gpuProgramManager);
  elapsedTimeMS = $elapsedTimeInMilliseconds(this$static._timer);
  if (this$static._logFPS) {
    this$static._totalRenderTime = toInt(add_0(fromInt(this$static._totalRenderTime), elapsedTimeMS));
    if (!this$static._renderStatisticsTimer || gt($elapsedTimeInMilliseconds(this$static._renderStatisticsTimer), {l:2000, m:0, h:0})) {
      averageTimePerRender = this$static._totalRenderTime / this$static._renderCounter;
      fps = 1000 / averageTimePerRender;
      $logInfo(_instance_3, 'FPS=%f', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [($clinit_Double() , new Double(fps))]));
      this$static._renderCounter = 0;
      this$static._totalRenderTime = 0;
      !this$static._renderStatisticsTimer?(this$static._renderStatisticsTimer = new Timer_WebGL):(this$static._renderStatisticsTimer._startTimeInMilliseconds = fromDouble(now_1()));
    }
  }
  if (this$static._logDownloaderStatistics) {
    cacheStatistics = '';
    !!this$static._downloader && (cacheStatistics = $statistics(this$static._downloader));
    if (!$equals(this$static._lastCacheStatistics, cacheStatistics)) {
      $logInfo(_instance_3, '%s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [cacheStatistics]));
      this$static._lastCacheStatistics = cacheStatistics;
    }
  }
}

function $setAnimatedCameraPosition(this$static, position, heading, pitch){
  $setAnimatedCameraPosition_0(this$static, new TimeInterval({l:3000, m:0, h:0}), position, heading, pitch);
}

function $setAnimatedCameraPosition_0(this$static, interval, position, heading, pitch){
  var fromHeading, fromPitch, fromPosition;
  fromPosition = $getGeodeticPosition(this$static._nextCamera);
  fromHeading = $getHeadingPitchRoll(this$static._nextCamera)._heading;
  fromPitch = $getHeadingPitchRoll(this$static._nextCamera)._pitch;
  $setAnimatedCameraPosition_1(this$static, interval, fromPosition, position, fromHeading, heading, fromPitch, pitch);
}

function $setAnimatedCameraPosition_1(this$static, interval, fromPosition, toPosition, fromHeading, toHeading, fromPitch, toPitch){
  var finalLatInDegrees, finalLonInDegrees, finalToPosition, target;
  if ($isEquals(fromPosition._latitude, toPosition._latitude) && $isEquals(fromPosition._longitude, toPosition._longitude) && fromPosition._height == toPosition._height && ($isEquals_2(fromHeading._degrees, toHeading._degrees) || $isEquals_2(fromHeading._radians, toHeading._radians)) && ($isEquals_2(fromPitch._degrees, toPitch._degrees) || $isEquals_2(fromPitch._radians, toPitch._radians))) {
    return;
  }
  finalLatInDegrees = toPosition._latitude._degrees;
  finalLonInDegrees = toPosition._longitude._degrees;
  while (finalLatInDegrees > 90) {
    finalLatInDegrees -= 360;
  }
  while (finalLatInDegrees < -90) {
    finalLatInDegrees += 360;
  }
  while (finalLonInDegrees > 360) {
    finalLonInDegrees -= 360;
  }
  while (finalLonInDegrees < 0) {
    finalLonInDegrees += 360;
  }
  abs_0(finalLonInDegrees - fromPosition._longitude._degrees) > 180 && (finalLonInDegrees -= 360);
  finalToPosition = fromDegrees_1(finalLatInDegrees, finalLonInDegrees, toPosition._height);
  target = this$static._nextCamera._camEffectTarget;
  $cancelAllEffectsFor(this$static._effectsScheduler, target);
  $startEffect(this$static._effectsScheduler, new CameraGoToPositionEffect(interval, fromPosition, finalToPosition, fromHeading, toHeading, fromPitch, toPitch), this$static._nextCamera._camEffectTarget);
}

function $setSelectedRenderer(this$static, selectedRenderer){
  if (selectedRenderer != this$static._selectedRenderer) {
    !!this$static._selectedRenderer && this$static._selectedRenderer.stop_1(this$static._renderContext);
    this$static._selectedRenderer = selectedRenderer;
    this$static._selectedRenderer.start_2(this$static._renderContext);
  }
}

function G3MWidget(gl, downloader, threadUtils, planet, cameraConstrainers, cameraRenderer, mainRenderer, busyRenderer, errorRenderer, backgroundColor, logFPS, logDownloaderStatistics, periodicalTasks, gpuProgramManager, sceneLighting){
  var i_0;
  this._cameraConstrainers = new ArrayList;
  this._periodicalTasks = new ArrayList;
  this._frameTasksExecutor = new FrameTasksExecutor;
  this._effectsScheduler = new EffectsScheduler;
  this._gl = gl;
  this._downloader = downloader;
  this._threadUtils = threadUtils;
  this._texturesHandler = new TexturesHandler(this._gl);
  this._planet = planet;
  this._cameraConstrainers = cameraConstrainers;
  this._cameraRenderer = cameraRenderer;
  this._mainRenderer = mainRenderer;
  this._busyRenderer = busyRenderer;
  this._errorRenderer = errorRenderer;
  this._width = 1;
  this._height = 1;
  this._currentCamera = new Camera({l:1, m:0, h:0});
  this._nextCamera = new Camera({l:2, m:0, h:0});
  this._backgroundColor = new Color_0(backgroundColor);
  this._timer = new Timer_WebGL;
  this._renderCounter = 0;
  this._totalRenderTime = 0;
  this._logFPS = logFPS;
  this._rendererState = new RenderState_0(($clinit_RenderState() , $clinit_RenderState() , BUSY));
  this._selectedRenderer = null;
  this._renderStatisticsTimer = null;
  this._logDownloaderStatistics = logDownloaderStatistics;
  mainRenderer.getSurfaceElevationProvider();
  this._context = new G3MContext(_instance_1, threadUtils, _instance_3, this._planet, downloader, this._effectsScheduler, mainRenderer.getSurfaceElevationProvider());
  this._paused = false;
  this._initializationTaskReady = true;
  this._clickOnProcess = false;
  this._gpuProgramManager = gpuProgramManager;
  this._sceneLighting = sceneLighting;
  this._rootState = null;
  this._initialCameraPositionHasBeenSet = false;
  this._forceBusyRenderer = false;
  this._nFramesBeetweenProgramsCleanUp = 500;
  this._touchDownPositionX = 0;
  this._touchDownPositionY = 0;
  new Timer_WebGL;
  this._mainRenderer.initialize(this._context);
  $initialize(this._currentCamera, this._context);
  $initialize(this._nextCamera, this._context);
  !!this._downloader && $start_0(this._downloader);
  for (i_0 = 0; i_0 < periodicalTasks.array.length; i_0++) {
    $addPeriodicalTask(this, (checkElementIndex(i_0, periodicalTasks.array.length) , throwClassCastExceptionUnlessNull(periodicalTasks.array[i_0])));
  }
  this._mainRenderer.setChangedRendererInfoListener(this, -1);
  this._renderContext = new G3MRenderContext(this._frameTasksExecutor, _instance_1, this._threadUtils, _instance_3, this._planet, this._gl, this._currentCamera, this._nextCamera, this._texturesHandler, this._downloader, this._effectsScheduler, new Timer_WebGL, this._gpuProgramManager);
}

function initSingletons(logger, factory, stringUtils, stringBuilder, mathUtils, jsonParser, textUtils, devAttitude, devLocation){
  if (!_instance_3) {
    !!_instance_3 && $logWarning(_instance_3, 'ILooger instance already set!', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    _instance_3 = logger;
    !!_instance_1 && $logWarning(_instance_3, 'IFactory instance already set!', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    _instance_1 = factory;
    !!_instance_6 && $logWarning(_instance_3, 'IStringUtils instance already set!', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    _instance_6 = stringUtils;
    !!_instance_5 && $logWarning(_instance_3, 'IStringBuilder set two times', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    _instance_5 = stringBuilder;
    !!_instance_4 && $logWarning(_instance_3, 'IMathUtils instance already set!', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    _instance_4 = mathUtils;
    !!_instance_2 && $logWarning(_instance_3, 'IJSONParser instance already set!', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    _instance_2 = jsonParser;
    !!_instance_7 && $logWarning(_instance_3, 'ITextUtils instance already set!', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    _instance_7 = textUtils;
    !!_instance && $logWarning(_instance_3, 'ILooger instance already set!', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    _instance = devAttitude;
    !!_instance_0 && $logWarning(_instance_3, 'IDeviceLocation instance already set!', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    _instance_0 = devLocation;
  }
   else {
    $logWarning(_instance_3, 'Singletons already set', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
}

defineClass(255, 1, {}, G3MWidget);
_.changedRendererInfo = function changedRendererInfo_0(rendererIdentifier, info){
}
;
_.getCurrentCamera = function getCurrentCamera(){
  return this._currentCamera;
}
;
_._clickOnProcess = false;
_._forceBusyRenderer = false;
_._height = 0;
_._initialCameraPositionHasBeenSet = false;
_._initializationTaskReady = false;
_._logDownloaderStatistics = false;
_._logFPS = false;
_._nFramesBeetweenProgramsCleanUp = 0;
_._paused = false;
_._renderCounter = 0;
_._totalRenderTime = 0;
_._touchDownPositionX = 0;
_._touchDownPositionY = 0;
_._width = 0;
var Lorg_glob3_mobile_generated_G3MWidget_2_classLit = createForClass('org.glob3.mobile.generated', 'G3MWidget', 255);
function GFont(size_0){
  this._name = 'sans-serif';
  this._size = size_0;
  this._bold = false;
  this._italic = false;
}

function GFont_0(that){
  this._name = that._name;
  this._size = that._size;
  this._bold = that._bold;
  this._italic = that._italic;
}

defineClass(124, 1, {}, GFont, GFont_0);
_.toString$ = function toString_28(){
  var isb;
  return isb = new StringBuilder_WebGL , isb._string += '(GFont name="' , $addString(isb, this._name) , isb._string += '", size=' , $addFloat(isb, this._size) , this._bold && (isb._string += ', bold' , isb) , this._italic && (isb._string += ', italic' , isb) , isb._string += ')' , isb._string;
}
;
_._bold = false;
_._italic = false;
_._size = 0;
var Lorg_glob3_mobile_generated_GFont_2_classLit = createForClass('org.glob3.mobile.generated', 'GFont', 124);
function $attachShader(this$static, program, shader){
  $attachShader_0(this$static._nativeGL, program, shader);
}

function $clearScreen(this$static, color_0){
  $setClearColor(this$static._clearScreenState, color_0);
  $applyChanges(this$static._clearScreenState, this$static, this$static._currentGLGlobalState);
  $clear_0(this$static._nativeGL, _colorBuffer | _depthBuffer);
}

function $createShader(this$static, type_0){
  return $createShader_0(this$static._nativeGL, type_0);
}

function $deleteProgram(this$static, program){
  if (this$static._currentGPUProgram == program) {
    --this$static._currentGPUProgram._nReferences;
    this$static._currentGPUProgram = null;
  }
  return $deleteProgram_0(this$static._nativeGL, program._programID);
}

function $deleteTexture(this$static, textureId){
  if (textureId) {
    $onTextureDelete(this$static._currentGLGlobalState, textureId);
    $deleteTexture_0(this$static._nativeGL, textureId)?$addLast(this$static._texturesIdBag, textureId):!!textureId && undefined;
  }
}

function $disableVertexAttribArray(this$static, location_0){
  $disableVertexAttribArray_0(this$static._nativeGL, location_0);
}

function $drawArrays(this$static, mode, count, state, progManager){
  $applyOnGPU(state, this$static, progManager);
  $drawArrays_0(this$static._nativeGL, mode, 0, count);
}

function $drawElements(this$static, mode, indices, state, progManager){
  $applyOnGPU(state, this$static, progManager);
  $drawElements_0(this$static._nativeGL, mode, indices._buffer.length, indices);
}

function $enableVertexAttribArray(this$static, location_0){
  $enableVertexAttribArray_0(this$static._nativeGL, location_0);
}

function $getGLTextureId(this$static){
  var i_0, ids, idsCount, result;
  if (this$static._texturesIdBag.size_0 == 0) {
    ids = $genTextures(this$static._nativeGL, 1024);
    idsCount = ids.array.length;
    for (i_0 = 0; i_0 < idsCount; i_0++) {
      $addFirst(this$static._texturesIdBag, (checkElementIndex(i_0, ids.array.length) , dynamicCast(ids.array[i_0], 215)));
    }
    this$static._texturesIdAllocationCounter += idsCount;
    $logInfo(_instance_3, '= Created %d texturesIds (accumulated %d).', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(idsCount), valueOf(this$static._texturesIdAllocationCounter)]));
  }
  if (this$static._texturesIdBag.size_0 == 0) {
    $logError(_instance_3, 'TextureIds bag exhausted', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return null;
  }
  result = dynamicCast($getLast(this$static._texturesIdBag), 215);
  $removeLast(this$static._texturesIdBag);
  return result;
}

function $getProgramiv(this$static, program, pname){
  return $getProgramiv_0(this$static._nativeGL, program, pname);
}

function $linkProgram(this$static, program){
  return $linkProgram_0(this$static._nativeGL, program);
}

function $uniform1f(this$static, loc, x_0){
  $uniform1f_0(this$static._nativeGL, loc, x_0);
}

function $uniform1i(this$static, loc, v){
  $uniform1i_0(this$static._nativeGL, loc, v);
}

function $uniform2f(this$static, loc, x_0, y_0){
  $uniform2f_0(this$static._nativeGL, loc, x_0, y_0);
}

function $uniform3f(this$static, location_0, v0, v1, v2){
  $uniform3f_0(this$static._nativeGL, location_0, v0, v1, v2);
}

function $uniform4f(this$static, location_0, v0, v1, v2, v3){
  $uniform4f_0(this$static._nativeGL, location_0, v0, v1, v2, v3);
}

function $uniformMatrix4fv(this$static, location_0, matrix){
  $uniformMatrix4fv_0(this$static._nativeGL, location_0, false, matrix);
}

function $uploadTexture(this$static, image, format, generateMipmap){
  var clampToEdge, linear, newState, texId, texture2D;
  texId = $getGLTextureId(this$static);
  if (texId) {
    newState = new GLGlobalState;
    newState._pixelStoreIAlignmentUnpack = 1;
    $bindTexture(newState, 0, texId);
    $applyChanges(newState, this$static, this$static._currentGLGlobalState);
    texture2D = _texture2D;
    linear = _linear;
    generateMipmap?$texParameteri(this$static._nativeGL, texture2D, _minFilter, _linearMipmapNearest):$texParameteri(this$static._nativeGL, texture2D, _minFilter, linear);
    $texParameteri(this$static._nativeGL, texture2D, _magFilter, linear);
    clampToEdge = _clampToEdge;
    $texParameteri(this$static._nativeGL, texture2D, _wrapS, clampToEdge);
    $texParameteri(this$static._nativeGL, texture2D, _wrapT, clampToEdge);
    $texImage2D(this$static._nativeGL, image, format);
    generateMipmap && (isPowerOfTwo($getWidth(image)) && isPowerOfTwo($getHeight(image))?$generateMipmap(this$static._nativeGL, texture2D):$logError(_instance_3, "Can't generate bitmap. Texture dimensions are not power of two.", initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [])));
  }
   else {
    $logError(_instance_3, "can't get a valid texture id\n", initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return null;
  }
  return texId;
}

function $useProgram(this$static, program){
  if (program) {
    if (this$static._currentGPUProgram != program) {
      if (this$static._currentGPUProgram) {
        $onUnused(this$static._currentGPUProgram, this$static);
        --this$static._currentGPUProgram._nReferences;
      }
      $useProgram_0(this$static._nativeGL, program);
      this$static._currentGPUProgram = program;
      ++this$static._currentGPUProgram._nReferences;
    }
  }
}

function $vertexAttribPointer(this$static, index_0, size_0, normalized, stride, buffer){
  $vertexAttribPointer_0(this$static._nativeGL, index_0, size_0, normalized, stride, buffer);
}

function GL(nativeGL){
  this._texturesIdBag = new LinkedList;
  this._nativeGL = nativeGL;
  this._texturesIdAllocationCounter = 0;
  this._currentGPUProgram = null;
  this._clearScreenState = null;
  init_3(this._nativeGL);
  init_2(this._nativeGL);
  init_7(this._nativeGL);
  init_11(this._nativeGL);
  init_6(this._nativeGL);
  init_1(this._nativeGL);
  init_10(this._nativeGL);
  init_8(this._nativeGL);
  init_9(this._nativeGL);
  init_0(this._nativeGL);
  init_5(this._nativeGL);
  init_12(this._nativeGL);
  init_4(this._nativeGL);
  _initializationAvailable = true;
  this._currentGLGlobalState = new GLGlobalState;
  this._clearScreenState = new GLGlobalState;
}

function isPowerOfTwo(x_0){
  return x_0 >= 0 && (x_0 == 1 || x_0 == 2 || x_0 == 4 || x_0 == 8 || x_0 == 16 || x_0 == 32 || x_0 == 64 || x_0 == 128 || x_0 == 256 || x_0 == 512 || x_0 == 1024 || x_0 == 2048 || x_0 == 4096 || x_0 == 8192 || x_0 == $intern_15 || x_0 == $intern_14 || x_0 == $intern_16 || x_0 == $intern_17 || x_0 == $intern_18 || x_0 == $intern_9 || x_0 == $intern_19 || x_0 == $intern_20 || x_0 == $intern_10 || x_0 == $intern_21 || x_0 == $intern_22 || x_0 == $intern_23 || x_0 == $intern_24 || x_0 == 134217728 || x_0 == 268435456 || x_0 == 536870912 || x_0 == 1073741824);
}

defineClass(288, 1, {}, GL);
_._texturesIdAllocationCounter = 0;
var Lorg_glob3_mobile_generated_GL_2_classLit = createForClass('org.glob3.mobile.generated', 'GL', 288);
function init_0(ngl){
  ngl._gl.PACK_ALIGNMENT;
  _unpack = ngl._gl.UNPACK_ALIGNMENT;
}

var _unpack = 0;
function init_1(ngl){
  _srcAlpha = ngl._gl.SRC_ALPHA;
  _oneMinusSrcAlpha = ngl._gl.ONE_MINUS_SRC_ALPHA;
  _one = ngl._gl.ONE;
  _zero = ngl._gl.ZERO;
}

var _one = 0, _oneMinusSrcAlpha = 0, _srcAlpha = 0, _zero = 0;
function init_2(ngl){
  _colorBuffer = ngl._gl.COLOR_BUFFER_BIT;
  _depthBuffer = ngl._gl.DEPTH_BUFFER_BIT;
}

var _colorBuffer = 0, _depthBuffer = 0;
function $setMatrix(this$static, matrix){
  $setMatrix_0(this$static._matrixHolder, matrix);
}

function GLCameraGroupFeature(matrix, id_0){
  GLFeature.call(this, ($clinit_GLFeatureGroupName() , CAMERA_GROUP), id_0);
  this._matrixHolder = new Matrix44DHolder(matrix);
}

defineClass(50, 27, $intern_47);
_.applyOnGlobalGLState = function applyOnGlobalGLState_2(state){
}
;
_.dispose = function dispose_24(){
  $_release(this._matrixHolder);
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
_._matrixHolder = null;
var Lorg_glob3_mobile_generated_GLCameraGroupFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'GLCameraGroupFeature', 50);
function init_3(ngl){
  ngl._gl.FRONT;
  _back = ngl._gl.BACK;
  ngl._gl.FRONT_AND_BACK;
}

var _back = 0;
function init_4(ngl){
  _noError = ngl._gl.NO_ERROR;
}

var _noError = 0;
function applyToAllGroups(features, vs, state){
  var groupName, i_0, i0;
  if (_groups == null) {
    _groups = initDim(Lorg_glob3_mobile_generated_GLFeatureGroup_2_classLit, $intern_4, 90, 4, 0, 1);
    for (i0 = 0; i0 < 4; i0++) {
      groupName = getGroupName(i0);
      setCheck(_groups, i0, createGroup(groupName));
    }
  }
  for (i_0 = 0; i_0 < 4; i_0++) {
    _groups[i_0].apply_0(features, vs, state);
  }
}

function createGroup(name_0){
  switch (name_0.ordinal) {
    default:case 0:
      return null;
    case 1:
      return new GLFeatureNoGroup;
    case 2:
      return new GLFeatureCameraGroup;
    case 3:
      return new GLFeatureColorGroup;
    case 4:
      return new GLFeatureLightingGroup;
  }
}

function getGroupName(i_0){
  switch (i_0) {
    case 0:
      return $clinit_GLFeatureGroupName() , NO_GROUP;
    case 1:
      return $clinit_GLFeatureGroupName() , CAMERA_GROUP;
    case 2:
      return $clinit_GLFeatureGroupName() , COLOR_GROUP;
    case 3:
      return $clinit_GLFeatureGroupName() , LIGHTING_GROUP;
    default:return $clinit_GLFeatureGroupName() , UNRECOGNIZED_GROUP;
  }
}

defineClass(90, 1, $intern_48);
var _groups = null;
var Lorg_glob3_mobile_generated_GLFeatureGroup_2_classLit = createForClass('org.glob3.mobile.generated', 'GLFeatureGroup', 90);
function GLFeatureCameraGroup(){
}

defineClass(403, 90, $intern_48, GLFeatureCameraGroup);
_.apply_0 = function apply_1(features, vs, state){
  var cf, f, featuresSize, group, i_0, id_0, modelTransformHolderBuilder, modelViewHolderBuilder, modelViewProvider, normalsAvailable, prov;
  modelViewHolderBuilder = new Matrix44DMultiplicationHolderBuilder;
  modelTransformHolderBuilder = new Matrix44DMultiplicationHolderBuilder;
  normalsAvailable = false;
  featuresSize = features._nFeatures;
  for (i_0 = 0; i_0 < featuresSize; i_0++) {
    f = $get_3(features, i_0);
    group = f._group;
    id_0 = f._id;
    if (group == ($clinit_GLFeatureGroupName() , CAMERA_GROUP)) {
      cf = dynamicCast(f, 50);
      id_0 == 5?$add_15(modelTransformHolderBuilder, cf._matrixHolder):$add_15(modelViewHolderBuilder, cf._matrixHolder);
    }
     else {
      group == LIGHTING_GROUP && id_0 == 12 && (normalsAvailable = true);
    }
  }
  if (modelTransformHolderBuilder._providers.array.length > 0) {
    prov = $create_2(modelTransformHolderBuilder);
    $add_3(modelViewHolderBuilder._providers, prov);
    ++prov._referenceCounter;
    normalsAvailable && $addUniformValue(vs, ($clinit_GPUUniformKey() , MODEL), new GPUUniformValueMatrix4(prov));
    $_release(prov);
  }
  if (modelViewHolderBuilder._providers.array.length > 0) {
    modelViewProvider = $create_2(modelViewHolderBuilder);
    $addUniformValue(vs, ($clinit_GPUUniformKey() , MODELVIEW), new GPUUniformValueMatrix4(modelViewProvider));
    $_release(modelViewProvider);
  }
  $dispose_8(modelViewHolderBuilder);
  $dispose_8(modelTransformHolderBuilder);
}
;
var Lorg_glob3_mobile_generated_GLFeatureCameraGroup_2_classLit = createForClass('org.glob3.mobile.generated', 'GLFeatureCameraGroup', 403);
function GLFeatureColorGroup(){
}

defineClass(404, 90, $intern_48, GLFeatureColorGroup);
_.apply_0 = function apply_2(features, vs, state){
  var f, featuresSize, i_0, i0, pf, priority;
  featuresSize = features._nFeatures;
  priority = -1;
  for (i0 = 0; i0 < featuresSize; i0++) {
    f = $get_3(features, i0);
    if (f._group == ($clinit_GLFeatureGroupName() , COLOR_GROUP)) {
      pf = dynamicCast(f, 70);
      pf._priority > priority && pf._id != 14 && (priority = pf._priority);
    }
  }
  for (i_0 = 0; i_0 < featuresSize; i_0++) {
    f = $get_3(features, i_0);
    if (f._group == ($clinit_GLFeatureGroupName() , COLOR_GROUP)) {
      pf = dynamicCast(f, 70);
      if (pf._priority == priority) {
        pf.applyOnGlobalGLState(state);
        $combineWith(vs, f._values);
      }
    }
  }
}
;
var Lorg_glob3_mobile_generated_GLFeatureColorGroup_2_classLit = createForClass('org.glob3.mobile.generated', 'GLFeatureColorGroup', 404);
function $clinit_GLFeatureGroupName(){
  $clinit_GLFeatureGroupName = emptyMethod;
  UNRECOGNIZED_GROUP = new GLFeatureGroupName('UNRECOGNIZED_GROUP', 0, -1);
  NO_GROUP = new GLFeatureGroupName('NO_GROUP', 1, 0);
  CAMERA_GROUP = new GLFeatureGroupName('CAMERA_GROUP', 2, 1);
  COLOR_GROUP = new GLFeatureGroupName('COLOR_GROUP', 3, 2);
  LIGHTING_GROUP = new GLFeatureGroupName('LIGHTING_GROUP', 4, 3);
}

function GLFeatureGroupName(enum$name, enum$ordinal, value_0){
  Enum.call(this, enum$name, enum$ordinal);
  $put((!mappings_0 && !mappings_0 && (mappings_0 = new HashMap) , mappings_0), valueOf(value_0), this);
}

function values_2(){
  $clinit_GLFeatureGroupName();
  return initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_GLFeatureGroupName_2_classLit, 1), $intern_4, 79, 0, [UNRECOGNIZED_GROUP, NO_GROUP, CAMERA_GROUP, COLOR_GROUP, LIGHTING_GROUP]);
}

defineClass(79, 15, {3:1, 26:1, 15:1, 79:1}, GLFeatureGroupName);
var CAMERA_GROUP, COLOR_GROUP, LIGHTING_GROUP, NO_GROUP, UNRECOGNIZED_GROUP, mappings_0;
var Lorg_glob3_mobile_generated_GLFeatureGroupName_2_classLit = createForEnum('org.glob3.mobile.generated', 'GLFeatureGroupName', 79, values_2);
function GLFeatureLightingGroup(){
}

defineClass(405, 90, $intern_48, GLFeatureLightingGroup);
_.apply_0 = function apply_3(features, vs, state){
  var f, i_0, i0, normalsAvailable, size_0;
  size_0 = features._nFeatures;
  normalsAvailable = false;
  for (i0 = 0; i0 < size_0; i0++) {
    f = $get_3(features, i0);
    if (f._id == 12) {
      normalsAvailable = true;
      break;
    }
  }
  if (normalsAvailable) {
    for (i_0 = 0; i_0 < size_0; i_0++) {
      f = $get_3(features, i_0);
      if (f._group == ($clinit_GLFeatureGroupName() , LIGHTING_GROUP)) {
        f.applyOnGlobalGLState(state);
        $combineWith(vs, f._values);
      }
    }
  }
}
;
var Lorg_glob3_mobile_generated_GLFeatureLightingGroup_2_classLit = createForClass('org.glob3.mobile.generated', 'GLFeatureLightingGroup', 405);
function GLFeatureNoGroup(){
}

defineClass(402, 90, $intern_48, GLFeatureNoGroup);
_.apply_0 = function apply_4(features, vs, state){
  var f, i_0, size_0;
  size_0 = features._nFeatures;
  for (i_0 = 0; i_0 < size_0; i_0++) {
    f = $get_3(features, i_0);
    if (f._group == ($clinit_GLFeatureGroupName() , NO_GROUP)) {
      f.applyOnGlobalGLState(state);
      $combineWith(vs, f._values);
    }
  }
}
;
var Lorg_glob3_mobile_generated_GLFeatureNoGroup_2_classLit = createForClass('org.glob3.mobile.generated', 'GLFeatureNoGroup', 402);
function $add_13(this$static, f){
  setCheck(this$static._features, this$static._nFeatures++, f);
  ++f._referenceCounter;
  this$static._nFeatures >= 20 && $logError(_instance_3, 'MAX_CONCURRENT_FEATURES_PER_GROUP REACHED', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
}

function $add_14(this$static, fs){
  var i_0, size_0;
  size_0 = fs._nFeatures;
  for (i_0 = 0; i_0 < size_0; i_0++) {
    $add_13(this$static, $get_3(fs, i_0));
  }
}

function $clearFeatures(this$static, g){
  var f, i_0, j;
  for (i_0 = 0; i_0 < this$static._nFeatures; i_0++) {
    f = this$static._features[i_0];
    if (f._group == g) {
      $_release(f);
      for (j = i_0; j < this$static._nFeatures; j++) {
        j + 1 >= 20?setCheck(this$static._features, j, null):setCheck(this$static._features, j, this$static._features[j + 1]);
      }
      --i_0;
      --this$static._nFeatures;
    }
  }
}

function $dispose_3(this$static){
  var i_0;
  for (i_0 = 0; i_0 < this$static._nFeatures; i_0++) {
    $_release(this$static._features[i_0]);
  }
}

function $get_3(this$static, i_0){
  if (this$static._nFeatures < i_0) {
    return null;
  }
  return this$static._features[i_0];
}

function GLFeatureSet(){
  var i_0;
  this._features = initDim(Lorg_glob3_mobile_generated_GLFeature_2_classLit, $intern_4, 27, 20, 0, 1);
  this._nFeatures = 0;
  for (i_0 = 0; i_0 < 20; i_0++) {
    setCheck(this._features, i_0, null);
  }
}

defineClass(192, 1, {}, GLFeatureSet);
_._nFeatures = 0;
var Lorg_glob3_mobile_generated_GLFeatureSet_2_classLit = createForClass('org.glob3.mobile.generated', 'GLFeatureSet', 192);
function init_5(ngl){
  _rgba = ngl._gl.RGBA;
}

var _rgba = 0;
function $applyChanges(this$static, gl, currentState){
  var i_0, nativeGL, gl_0;
  nativeGL = gl._nativeGL;
  if (this$static._depthTest != currentState._depthTest) {
    this$static._depthTest?$enable(nativeGL, _depthTest):$disable(nativeGL, _depthTest);
    currentState._depthTest = this$static._depthTest;
  }
  if (this$static._blend != currentState._blend) {
    this$static._blend?$enable(nativeGL, _blend):$disable(nativeGL, _blend);
    currentState._blend = this$static._blend;
  }
  if (this$static._cullFace != currentState._cullFace) {
    currentState._cullFace = this$static._cullFace;
    if (this$static._cullFace) {
      $enable(nativeGL, _cullFace);
      if (this$static._culledFace != currentState._culledFace) {
        $cullFace(nativeGL, this$static._culledFace);
        currentState._culledFace = this$static._culledFace;
      }
    }
     else {
      $disable(nativeGL, _cullFace);
    }
  }
  if (this$static._lineWidth != currentState._lineWidth) {
    $lineWidth(nativeGL, this$static._lineWidth);
    currentState._lineWidth = this$static._lineWidth;
  }
  if (this$static._polygonOffsetFill != currentState._polygonOffsetFill) {
    currentState._polygonOffsetFill = this$static._polygonOffsetFill;
    if (this$static._polygonOffsetFill) {
      $enable(nativeGL, _polygonOffsetFill);
      if (this$static._polygonOffsetFactor != currentState._polygonOffsetFactor || this$static._polygonOffsetUnits != currentState._polygonOffsetUnits) {
        $polygonOffset(nativeGL, this$static._polygonOffsetFactor, this$static._polygonOffsetUnits);
        currentState._polygonOffsetUnits = this$static._polygonOffsetUnits;
        currentState._polygonOffsetFactor = this$static._polygonOffsetFactor;
      }
    }
     else {
      $disable(nativeGL, _polygonOffsetFill);
    }
  }
  if (this$static._blendDFactor != currentState._blendDFactor || this$static._blendSFactor != currentState._blendSFactor) {
    $blendFunc(nativeGL, this$static._blendSFactor, this$static._blendDFactor);
    currentState._blendDFactor = this$static._blendDFactor;
    currentState._blendSFactor = this$static._blendSFactor;
  }
  for (i_0 = 0; i_0 < 4; i_0++) {
    if (this$static._boundTextureId[i_0]) {
      if (!currentState._boundTextureId[i_0] || !$isEquals_4(this$static._boundTextureId[i_0], currentState._boundTextureId[i_0])) {
        gl_0 = nativeGL._gl;
        gl_0.activeTexture(gl_0.TEXTURE0 + i_0);
        $bindTexture_0(nativeGL, _texture2D, this$static._boundTextureId[i_0]);
        setCheck(currentState._boundTextureId, i_0, this$static._boundTextureId[i_0]);
      }
    }
  }
  if (this$static._pixelStoreIAlignmentUnpack != -1 && this$static._pixelStoreIAlignmentUnpack != currentState._pixelStoreIAlignmentUnpack) {
    $pixelStorei(nativeGL, _unpack, this$static._pixelStoreIAlignmentUnpack);
    currentState._pixelStoreIAlignmentUnpack = this$static._pixelStoreIAlignmentUnpack;
  }
  if (this$static._pixelStoreIAlignmentUnpack != -1 && this$static._pixelStoreIAlignmentUnpack != currentState._pixelStoreIAlignmentUnpack) {
    $pixelStorei(nativeGL, _unpack, this$static._pixelStoreIAlignmentUnpack);
    currentState._pixelStoreIAlignmentUnpack = this$static._pixelStoreIAlignmentUnpack;
  }
  if (this$static._clearColorR != currentState._clearColorR || this$static._clearColorG != currentState._clearColorG || this$static._clearColorB != currentState._clearColorB || this$static._clearColorA != currentState._clearColorA) {
    $clearColor(nativeGL, this$static._clearColorR, this$static._clearColorG, this$static._clearColorB, this$static._clearColorA);
    currentState._clearColorR = this$static._clearColorR;
    currentState._clearColorG = this$static._clearColorG;
    currentState._clearColorB = this$static._clearColorB;
    currentState._clearColorA = this$static._clearColorA;
  }
}

function $bindTexture(this$static, target, textureId){
  if (target > 4) {
    $logError(_instance_3, 'WRONG TARGET FOR TEXTURE', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  setCheck(this$static._boundTextureId, target, textureId);
}

function $enableCullFace(this$static, face_0){
  this$static._cullFace = true;
  this$static._culledFace = face_0;
}

function $enablePolygonOffsetFill(this$static, factor, units){
  this$static._polygonOffsetFill = true;
  this$static._polygonOffsetFactor = factor;
  this$static._polygonOffsetUnits = units;
}

function $onTextureDelete(this$static, textureId){
  var i_0;
  for (i_0 = 0; i_0 < 4; i_0++) {
    this$static._boundTextureId[i_0] == textureId && setCheck(this$static._boundTextureId, i_0, null);
  }
}

function $setBlendFactors(this$static, sFactor, dFactor){
  this$static._blendSFactor = sFactor;
  this$static._blendDFactor = dFactor;
}

function $setClearColor(this$static, color_0){
  this$static._clearColorR = color_0._red;
  this$static._clearColorG = color_0._green;
  this$static._clearColorB = color_0._blue;
  this$static._clearColorA = color_0._alpha;
}

function $setLineWidth(this$static, lineWidth){
  this$static._lineWidth = lineWidth;
}

function GLGlobalState(){
  var i_0;
  this._boundTextureId = initDim(Lorg_glob3_mobile_generated_IGLTextureId_2_classLit, $intern_4, 215, 4, 0, 1);
  this._depthTest = false;
  this._blend = false;
  this._cullFace = true;
  this._culledFace = _back;
  this._lineWidth = 1;
  this._polygonOffsetFactor = 0;
  this._polygonOffsetUnits = 0;
  this._polygonOffsetFill = false;
  this._blendDFactor = _zero;
  this._blendSFactor = _one;
  this._pixelStoreIAlignmentUnpack = -1;
  this._clearColorR = 0;
  this._clearColorG = 0;
  this._clearColorB = 0;
  this._clearColorA = 0;
  _initializationAvailable || $logError(_instance_3, 'GLGlobalState creation before it is available.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  for (i_0 = 0; i_0 < 4; i_0++) {
    setCheck(this._boundTextureId, i_0, null);
  }
}

defineClass(118, 1, {}, GLGlobalState);
_._blend = false;
_._blendDFactor = 0;
_._blendSFactor = 0;
_._clearColorA = 0;
_._clearColorB = 0;
_._clearColorG = 0;
_._clearColorR = 0;
_._cullFace = false;
_._culledFace = 0;
_._depthTest = false;
_._lineWidth = 0;
_._pixelStoreIAlignmentUnpack = 0;
_._polygonOffsetFactor = 0;
_._polygonOffsetFill = false;
_._polygonOffsetUnits = 0;
var _initializationAvailable = false;
var Lorg_glob3_mobile_generated_GLGlobalState_2_classLit = createForClass('org.glob3.mobile.generated', 'GLGlobalState', 118);
function init_6(ngl){
  ngl._gl.TRIANGLES;
  _triangleStrip = ngl._gl.TRIANGLE_STRIP;
  ngl._gl.TRIANGLE_FAN;
  ngl._gl.LINES;
  ngl._gl.LINE_STRIP;
  _lineLoop = ngl._gl.LINE_LOOP;
  ngl._gl.POINTS;
}

var _lineLoop = 0, _triangleStrip = 0;
function init_7(ngl){
  _polygonOffsetFill = ngl._gl.POLYGON_OFFSET_FILL;
  _depthTest = ngl._gl.DEPTH_TEST;
  _blend = ngl._gl.BLEND;
  _cullFace = ngl._gl.CULL_FACE;
}

var _blend = 0, _cullFace = 0, _depthTest = 0, _polygonOffsetFill = 0;
function $addGLFeature(this$static, f, mustRetain){
  $add_13(this$static._features, f);
  mustRetain || $_release(f);
  $hasChangedStructure(this$static);
}

function $applyOnGPU(this$static, gl, progManager){
  var accumulatedFeatures, attributesCode, uniformsCode;
  if (!this$static._valuesSet && !this$static._globalState) {
    this$static._valuesSet = new GPUVariableValueSet;
    this$static._globalState = new GLGlobalState;
    accumulatedFeatures = $getAccumulatedFeatures(this$static);
    applyToAllGroups(accumulatedFeatures, this$static._valuesSet, this$static._globalState);
    uniformsCode = $getUniformsCode(this$static._valuesSet);
    attributesCode = $getAttributesCode(this$static._valuesSet);
    this$static._linkedProgram = $getProgram(progManager, gl, uniformsCode, attributesCode);
  }
  if (!this$static._valuesSet || !this$static._globalState) {
    $logError(_instance_3, 'GLState logic error.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  if (this$static._linkedProgram) {
    $useProgram(gl, this$static._linkedProgram);
    $applyValuesToProgram(this$static._valuesSet, this$static._linkedProgram);
    $applyChanges(this$static._globalState, gl, gl._currentGLGlobalState);
    $applyChanges_1(this$static._linkedProgram, gl);
  }
   else {
    $logError(_instance_3, 'No GPUProgram found.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
}

function $clearGLFeatureGroup(this$static, g){
  $clearFeatures(this$static._features, g);
  $hasChangedStructure(this$static);
}

function $getAccumulatedFeatures(this$static){
  var parents;
  if (!this$static._accumulatedFeatures) {
    this$static._accumulatedFeatures = new GLFeatureSet;
    if (this$static._parentGLState) {
      parents = $getAccumulatedFeatures(this$static._parentGLState);
      !!parents && $add_14(this$static._accumulatedFeatures, parents);
    }
    $add_14(this$static._accumulatedFeatures, this$static._features);
  }
  return this$static._accumulatedFeatures;
}

function $getGLFeature(this$static, id_0){
  var f, i_0, size_0;
  size_0 = this$static._features._nFeatures;
  for (i_0 = 0; i_0 < size_0; i_0++) {
    f = $get_3(this$static._features, i_0);
    if (f._id == id_0) {
      return f;
    }
  }
  return null;
}

function $hasChangedStructure(this$static){
  ++this$static._timestamp;
  !!this$static._valuesSet && $dispose_4(this$static._valuesSet);
  this$static._valuesSet = null;
  this$static._globalState = null;
  if (this$static._linkedProgram) {
    --this$static._linkedProgram._nReferences;
    this$static._linkedProgram = null;
  }
  !!this$static._accumulatedFeatures && $dispose_3(this$static._accumulatedFeatures);
  this$static._accumulatedFeatures = null;
}

function $setParent_2(this$static, parent_0){
  var parentsTimestamp;
  if (!parent_0) {
    if (this$static._parentGLState) {
      this$static._parentGLState = null;
      this$static._parentsTimestamp = -1;
      $hasChangedStructure(this$static);
    }
  }
   else {
    parentsTimestamp = parent_0._timestamp;
    if (parent_0 != this$static._parentGLState || this$static._parentsTimestamp != parentsTimestamp) {
      if (this$static._parentGLState != parent_0) {
        !!this$static._parentGLState && $_release(this$static._parentGLState);
        this$static._parentGLState = parent_0;
        ++this$static._parentGLState._referenceCounter;
      }
      this$static._parentsTimestamp = parentsTimestamp;
      $hasChangedStructure(this$static);
    }
  }
}

function GLState(){
  RCObject.call(this);
  this._parentGLState = null;
  this._linkedProgram = null;
  this._parentsTimestamp = -1;
  this._timestamp = 0;
  this._valuesSet = null;
  this._globalState = null;
  this._accumulatedFeatures = null;
  this._features = new GLFeatureSet;
}

defineClass(57, 9, {9:1}, GLState);
_.dispose = function dispose_25(){
  !!this._features && $dispose_3(this._features);
  !!this._accumulatedFeatures && $dispose_3(this._accumulatedFeatures);
  !!this._valuesSet && $dispose_4(this._valuesSet);
  !!this._parentGLState && $_release(this._parentGLState);
  !!this._linkedProgram && --this._linkedProgram._nReferences;
}
;
_._parentsTimestamp = 0;
_._timestamp = 0;
var Lorg_glob3_mobile_generated_GLState_2_classLit = createForClass('org.glob3.mobile.generated', 'GLState', 57);
function init_8(ngl){
  _minFilter = ngl._gl.TEXTURE_MIN_FILTER;
  _magFilter = ngl._gl.TEXTURE_MAG_FILTER;
  _wrapS = ngl._gl.TEXTURE_WRAP_S;
  _wrapT = ngl._gl.TEXTURE_WRAP_T;
}

var _magFilter = 0, _minFilter = 0, _wrapS = 0, _wrapT = 0;
function init_9(ngl){
  ngl._gl.NEAREST;
  _linear = ngl._gl.LINEAR;
  ngl._gl.NEAREST_MIPMAP_NEAREST;
  ngl._gl.NEAREST_MIPMAP_LINEAR;
  _linearMipmapNearest = ngl._gl.LINEAR_MIPMAP_NEAREST;
  ngl._gl.LINEAR_MIPMAP_LINEAR;
  _clampToEdge = ngl._gl.CLAMP_TO_EDGE;
}

var _clampToEdge = 0, _linear = 0, _linearMipmapNearest = 0;
function init_10(ngl){
  _texture2D = ngl._gl.TEXTURE_2D;
}

var _texture2D = 0;
function init_11(ngl){
  _float = ngl._gl.FLOAT;
  ngl._gl.UNSIGNED_BYTE;
  ngl._gl.UNSIGNED_INT;
  _int = ngl._gl.INT;
  _vec2Float = ngl._gl.FLOAT_VEC2;
  _vec3Float = ngl._gl.FLOAT_VEC3;
  _vec4Float = ngl._gl.FLOAT_VEC4;
  _bool = ngl._gl.BOOL;
  _matrix4Float = ngl._gl.FLOAT_MAT4;
}

var _bool = 0, _float = 0, _int = 0, _matrix4Float = 0, _vec2Float = 0, _vec3Float = 0, _vec4Float = 0;
function init_12(ngl){
  ngl._gl.VIEWPORT;
  _activeAttributes = ngl._gl.ACTIVE_ATTRIBUTES;
  _activeUniforms = ngl._gl.ACTIVE_UNIFORMS;
}

var _activeAttributes = 0, _activeUniforms = 0;
function GPUVariable(name_0){
  this._name = name_0;
}

function getAttributeCode(a){
  var index_0;
  if (a == ($clinit_GPUAttributeKey() , UNRECOGNIZED_ATTRIBUTE)) {
    return 0;
  }
  index_0 = a.intValue;
  return 1 << index_0;
}

function getAttributeKey(name_0){
  if (compareTo_3(name_0, 'aPosition') == 0) {
    return $clinit_GPUAttributeKey() , POSITION;
  }
  if (compareTo_3(name_0, 'aColor') == 0) {
    return $clinit_GPUAttributeKey() , COLOR;
  }
  if (compareTo_3(name_0, 'aTextureCoord') == 0) {
    return $clinit_GPUAttributeKey() , TEXTURE_COORDS;
  }
  if (compareTo_3(name_0, 'aTextureCoord2') == 0) {
    return $clinit_GPUAttributeKey() , TEXTURE_COORDS_2;
  }
  if (compareTo_3(name_0, 'aTextureCoord3') == 0) {
    return $clinit_GPUAttributeKey() , TEXTURE_COORDS_3;
  }
  if (compareTo_3(name_0, 'aNormal') == 0) {
    return $clinit_GPUAttributeKey() , NORMAL;
  }
  if (compareTo_3(name_0, 'aPosition2D') == 0) {
    return $clinit_GPUAttributeKey() , POSITION_2D;
  }
  return $clinit_GPUAttributeKey() , UNRECOGNIZED_ATTRIBUTE;
}

function getUniformCode(u){
  var index_0;
  if (u == ($clinit_GPUUniformKey() , UNRECOGNIZED_UNIFORM)) {
    return 0;
  }
  index_0 = u.intValue;
  return 1 << index_0;
}

function getUniformKey(name_0){
  if (compareTo_3(name_0, 'uFlatColor') == 0) {
    return $clinit_GPUUniformKey() , FLAT_COLOR;
  }
  if (compareTo_3(name_0, 'uModelview') == 0) {
    return $clinit_GPUUniformKey() , MODELVIEW;
  }
  if (compareTo_3(name_0, 'uTextureExtent') == 0) {
    return $clinit_GPUUniformKey() , TEXTURE_EXTENT;
  }
  if (compareTo_3(name_0, 'uViewPortExtent') == 0) {
    return $clinit_GPUUniformKey() , VIEWPORT_EXTENT;
  }
  if (compareTo_3(name_0, 'uTranslationTexCoord') == 0) {
    return $clinit_GPUUniformKey() , TRANSLATION_TEXTURE_COORDS;
  }
  if (compareTo_3(name_0, 'uScaleTexCoord') == 0) {
    return $clinit_GPUUniformKey() , SCALE_TEXTURE_COORDS;
  }
  if (compareTo_3(name_0, 'uPointSize') == 0) {
    return $clinit_GPUUniformKey() , POINT_SIZE;
  }
  if (compareTo_3(name_0, 'uAmbientLightColor') == 0) {
    return $clinit_GPUUniformKey() , AMBIENT_LIGHT_COLOR;
  }
  if (compareTo_3(name_0, 'uDiffuseLightDirection') == 0) {
    return $clinit_GPUUniformKey() , DIFFUSE_LIGHT_DIRECTION;
  }
  if (compareTo_3(name_0, 'uDiffuseLightColor') == 0) {
    return $clinit_GPUUniformKey() , DIFFUSE_LIGHT_COLOR;
  }
  if (compareTo_3(name_0, 'uProjection') == 0) {
    return $clinit_GPUUniformKey() , PROJECTION;
  }
  if (compareTo_3(name_0, 'uCameraModel') == 0) {
    return $clinit_GPUUniformKey() , CAMERA_MODEL;
  }
  if (compareTo_3(name_0, 'uModel') == 0) {
    return $clinit_GPUUniformKey() , MODEL;
  }
  if (compareTo_3(name_0, 'uBillboardPosition') == 0) {
    return $clinit_GPUUniformKey() , BILLBOARD_POSITION;
  }
  if (compareTo_3(name_0, 'uRotationCenterTexCoord') == 0) {
    return $clinit_GPUUniformKey() , ROTATION_CENTER_TEXTURE_COORDS;
  }
  if (compareTo_3(name_0, 'uRotationAngleTexCoord') == 0) {
    return $clinit_GPUUniformKey() , ROTATION_ANGLE_TEXTURE_COORDS;
  }
  if (compareTo_3(name_0, 'Sampler') == 0) {
    return $clinit_GPUUniformKey() , SAMPLER;
  }
  if (compareTo_3(name_0, 'Sampler2') == 0) {
    return $clinit_GPUUniformKey() , SAMPLER2;
  }
  if (compareTo_3(name_0, 'Sampler3') == 0) {
    return $clinit_GPUUniformKey() , SAMPLER3;
  }
  if (compareTo_3(name_0, 'uTranslation2D') == 0) {
    return $clinit_GPUUniformKey() , TRANSLATION_2D;
  }
  if (compareTo_3(name_0, 'uBillboardAnchor') == 0) {
    return $clinit_GPUUniformKey() , BILLBOARD_ANCHOR;
  }
  return $clinit_GPUUniformKey() , UNRECOGNIZED_UNIFORM;
}

function hasAttribute(code_0, a){
  var index_0;
  if (a == ($clinit_GPUAttributeKey() , UNRECOGNIZED_ATTRIBUTE)) {
    return false;
  }
  index_0 = a.intValue;
  return (code_0 >> index_0 & 1) != 0;
}

function hasUniform(code_0, u){
  var index_0;
  if (u == ($clinit_GPUUniformKey() , UNRECOGNIZED_UNIFORM)) {
    return false;
  }
  index_0 = u.intValue;
  return (code_0 >> index_0 & 1) != 0;
}

defineClass(35, 1, {35:1});
var Lorg_glob3_mobile_generated_GPUVariable_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUVariable', 35);
function $applyChanges_0(this$static, gl){
  if (!this$static._value) {
    this$static._enabled && $logError(_instance_3, 'Attribute ' + this$static._name + ' was not set but it is enabled.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
   else {
    if (this$static._dirty) {
      if (this$static._value._enabled) {
        if (!this$static._enabled) {
          $enableVertexAttribArray(gl, this$static._id);
          this$static._enabled = true;
        }
        this$static._value.setAttribute(gl, this$static._id);
      }
       else {
        if (this$static._enabled) {
          $disableVertexAttribArray(gl, this$static._id);
          this$static._enabled = false;
        }
      }
      this$static._dirty = false;
    }
  }
}

function $set_1(this$static, v){
  if (v != this$static._value) {
    if (v._enabled && this$static._type != v._type) {
      $logError(_instance_3, 'Attempting to set attribute ' + this$static._name + 'with invalid value type.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
      return;
    }
    if (!this$static._value || !this$static._value.isEquals(v)) {
      this$static._dirty = true;
      !!this$static._value && $_release(this$static._value);
      this$static._value = v;
      ++this$static._value._referenceCounter;
    }
  }
}

function $unset(this$static, gl){
  if (this$static._value) {
    $_release(this$static._value);
    this$static._value = null;
  }
  this$static._enabled = false;
  this$static._dirty = false;
  $disableVertexAttribArray(gl, this$static._id);
}

function GPUAttribute(name_0, id_0, type_0){
  GPUVariable.call(this, name_0, $clinit_GPUVariableType());
  this._id = id_0;
  this._dirty = false;
  this._value = null;
  this._type = type_0;
  this._enabled = false;
  this._key = getAttributeKey(name_0);
}

defineClass(56, 35, $intern_49);
_._dirty = false;
_._enabled = false;
_._id = 0;
_._type = 0;
var Lorg_glob3_mobile_generated_GPUAttribute_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUAttribute', 56);
function $clinit_GPUAttributeKey(){
  $clinit_GPUAttributeKey = emptyMethod;
  UNRECOGNIZED_ATTRIBUTE = new GPUAttributeKey('UNRECOGNIZED_ATTRIBUTE', 0, -1);
  POSITION = new GPUAttributeKey('POSITION', 1, 0);
  TEXTURE_COORDS = new GPUAttributeKey('TEXTURE_COORDS', 2, 1);
  COLOR = new GPUAttributeKey('COLOR', 3, 2);
  NORMAL = new GPUAttributeKey('NORMAL', 4, 3);
  TEXTURE_COORDS_2 = new GPUAttributeKey('TEXTURE_COORDS_2', 5, 4);
  TEXTURE_COORDS_3 = new GPUAttributeKey('TEXTURE_COORDS_3', 6, 5);
  POSITION_2D = new GPUAttributeKey('POSITION_2D', 7, 6);
}

function GPUAttributeKey(enum$name, enum$ordinal, value_0){
  Enum.call(this, enum$name, enum$ordinal);
  this.intValue = value_0;
  $put((!mappings_1 && !mappings_1 && (mappings_1 = new HashMap) , mappings_1), valueOf(value_0), this);
}

function values_3(){
  $clinit_GPUAttributeKey();
  return initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_GPUAttributeKey_2_classLit, 1), $intern_4, 52, 0, [UNRECOGNIZED_ATTRIBUTE, POSITION, TEXTURE_COORDS, COLOR, NORMAL, TEXTURE_COORDS_2, TEXTURE_COORDS_3, POSITION_2D]);
}

defineClass(52, 15, {3:1, 26:1, 15:1, 52:1}, GPUAttributeKey);
_.intValue = 0;
var COLOR, NORMAL, POSITION, POSITION_2D, TEXTURE_COORDS, TEXTURE_COORDS_2, TEXTURE_COORDS_3, UNRECOGNIZED_ATTRIBUTE, mappings_1;
var Lorg_glob3_mobile_generated_GPUAttributeKey_2_classLit = createForEnum('org.glob3.mobile.generated', 'GPUAttributeKey', 52, values_3);
function GPUAttributeValue(type_0, attributeSize, arrayElementSize){
  RCObject.call(this);
  this._enabled = true;
  this._type = type_0;
  this._attributeSize = attributeSize;
  this._index = 0;
  this._stride = 0;
  this._normalized = false;
  this._arrayElementSize = arrayElementSize;
}

defineClass(86, 9, {86:1, 9:1});
_.dispose = function dispose_26(){
  $dispose_0(this);
}
;
_.toString$ = function toString_29(){
  return this.description_0();
}
;
_._arrayElementSize = 0;
_._attributeSize = 0;
_._enabled = false;
_._index = 0;
_._normalized = false;
_._stride = 0;
_._type = 0;
var Lorg_glob3_mobile_generated_GPUAttributeValue_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUAttributeValue', 86);
function GPUAttributeValueVecFloat(buffer, attributeSize, arrayElementSize){
  GPUAttributeValue.call(this, _float, attributeSize, arrayElementSize);
  this._buffer = buffer;
  this._timestamp = 0;
  this._id = buffer._id;
}

defineClass(89, 86, $intern_50);
_.description_0 = function description(){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'Attribute Value Float.';
  isb._string += ' ArrayElementSize:';
  $addInt(isb, this._arrayElementSize);
  isb._string += ' AttributeSize:';
  $addInt(isb, this._attributeSize);
  isb._string += ' Index:';
  $addInt(isb, this._index);
  isb._string += ' Stride:';
  $addInt(isb, this._stride);
  isb._string += ' Normalized:';
  $addBool(isb, this._normalized);
  s = isb._string;
  return s;
}
;
_.dispose = function dispose_27(){
  $dispose_0(this);
}
;
_.isEquals = function isEquals(v){
  var equal, vecV;
  if (!v._enabled) {
    return false;
  }
  vecV = dynamicCast(v, 89);
  equal = eq(this._id, vecV._buffer._id) && this._timestamp == vecV._timestamp && this._type == v._type && this._attributeSize == v._attributeSize && this._stride == v._stride && this._normalized == v._normalized;
  return equal;
}
;
_.setAttribute = function setAttribute(gl, id_0){
  this._index != 0 && $logError(_instance_3, 'INDEX NO 0', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $vertexAttribPointer(gl, id_0, this._arrayElementSize, this._normalized, this._stride, this._buffer);
}
;
_._id = {l:0, m:0, h:0};
_._timestamp = 0;
var Lorg_glob3_mobile_generated_GPUAttributeValueVecFloat_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUAttributeValueVecFloat', 89);
function GPUAttributeValueVec2Float(buffer){
  GPUAttributeValueVecFloat.call(this, buffer, 2, 2);
}

defineClass(410, 89, $intern_50, GPUAttributeValueVec2Float);
_.dispose = function dispose_28(){
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_GPUAttributeValueVec2Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUAttributeValueVec2Float', 410);
function GPUAttributeValueVec4Float(buffer, arrayElementSize){
  GPUAttributeValueVecFloat.call(this, buffer, 4, arrayElementSize);
}

defineClass(210, 89, $intern_50, GPUAttributeValueVec4Float);
_.dispose = function dispose_29(){
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_GPUAttributeValueVec4Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUAttributeValueVec4Float', 210);
function GPUAttributeVec2Float(name_0, id_0){
  GPUAttribute.call(this, name_0, id_0, _float);
}

defineClass(498, 56, $intern_49, GPUAttributeVec2Float);
var Lorg_glob3_mobile_generated_GPUAttributeVec2Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUAttributeVec2Float', 498);
function GPUAttributeVec3Float(name_0, id_0){
  GPUAttribute.call(this, name_0, id_0, _float);
}

defineClass(496, 56, $intern_49, GPUAttributeVec3Float);
var Lorg_glob3_mobile_generated_GPUAttributeVec3Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUAttributeVec3Float', 496);
function GPUAttributeVec4Float(name_0, id_0){
  GPUAttribute.call(this, name_0, id_0, _float);
}

defineClass(497, 56, $intern_49, GPUAttributeVec4Float);
var Lorg_glob3_mobile_generated_GPUAttributeVec4Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUAttributeVec4Float', 497);
function $applyChanges_1(this$static, gl){
  var attribute, i_0, i0, uniform;
  for (i0 = 0; i0 < this$static._nUniforms; i0++) {
    uniform = this$static._createdUniforms[i0];
    !!uniform && $applyChanges_2(uniform, gl);
  }
  for (i_0 = 0; i_0 < this$static._nAttributes; i_0++) {
    attribute = this$static._createdAttributes[i_0];
    !!attribute && $applyChanges_0(attribute, gl);
  }
}

function $compileShader(this$static, gl, shader, source){
  var result;
  result = $compileShader_0(gl._nativeGL, shader, source);
  result?$attachShader(gl, this$static._programID, shader):$logError(_instance_3, 'GPUProgram: Problem encountered while compiling shader.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  return result;
}

function $getVariables(this$static, gl){
  var a, code_0, counter, i_0, i0, i1, u;
  for (i0 = 0; i0 < 32; i0++) {
    setCheck(this$static._uniforms, i0, null);
    setCheck(this$static._attributes, i0, null);
  }
  this$static._uniformsCode = 0;
  this$static._nUniforms = $getProgramiv(gl, this$static, _activeUniforms);
  counter = 0;
  this$static._createdUniforms = initDim(Lorg_glob3_mobile_generated_GPUUniform_2_classLit, $intern_4, 30, this$static._nUniforms, 0, 1);
  for (i1 = 0; i1 < this$static._nUniforms; i1++) {
    u = $getActiveUniform(gl._nativeGL, this$static, i1);
    if (u) {
      setCheck(this$static._uniforms, u._key.intValue, u);
      code_0 = getUniformCode(u._key);
      this$static._uniformsCode = this$static._uniformsCode | code_0;
    }
    setCheck(this$static._createdUniforms, counter++, u);
  }
  this$static._attributesCode = 0;
  this$static._nAttributes = $getProgramiv(gl, this$static, _activeAttributes);
  counter = 0;
  this$static._createdAttributes = initDim(Lorg_glob3_mobile_generated_GPUAttribute_2_classLit, $intern_4, 56, this$static._nAttributes, 0, 1);
  for (i_0 = 0; i_0 < this$static._nAttributes; i_0++) {
    a = $getActiveAttribute(gl._nativeGL, this$static, i_0);
    if (a) {
      setCheck(this$static._attributes, a._key.intValue, a);
      code_0 = getAttributeCode(a._key);
      this$static._attributesCode = this$static._attributesCode | code_0;
    }
    setCheck(this$static._createdAttributes, counter++, a);
  }
}

function $onUnused(this$static, gl){
  var i_0, i0;
  for (i0 = 0; i0 < this$static._nUniforms; i0++) {
    !!this$static._createdUniforms[i0] && $unset_0(this$static._createdUniforms[i0]);
  }
  for (i_0 = 0; i_0 < this$static._nAttributes; i_0++) {
    !!this$static._createdAttributes[i_0] && $unset(this$static._createdAttributes[i_0], gl);
  }
}

function $setGPUAttributeValue(this$static, key, v){
  var a;
  a = this$static._attributes[key];
  if (!a) {
    $logError(_instance_3, 'Attribute [key=%d] not found in program %s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(key), this$static._name]));
    return;
  }
  $set_1(a, v);
}

function $setGPUUniformValue(this$static, key, v){
  var u;
  u = this$static._uniforms[key];
  if (!u) {
    $logError(_instance_3, 'Uniform [key=%d] not found in program %s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(key), this$static._name]));
    return;
  }
  $set_2(u, v);
}

function GPUProgram(){
  this._uniforms = initDim(Lorg_glob3_mobile_generated_GPUUniform_2_classLit, $intern_4, 30, 32, 0, 1);
  this._attributes = initDim(Lorg_glob3_mobile_generated_GPUAttribute_2_classLit, $intern_4, 56, 32, 0, 1);
  this._createdAttributes = null;
  this._createdUniforms = null;
  this._nUniforms = 0;
  this._nAttributes = 0;
  this._uniformsCode = 0;
  this._attributesCode = 0;
  this._nReferences = 0;
}

function createProgram(gl, name_0, vertexSource, fragmentSource){
  var fragmentShader, p, vertexShader;
  p = new GPUProgram;
  p._name = name_0;
  p._programID = $createProgram(gl._nativeGL);
  vertexShader = $createShader(gl, ($clinit_ShaderType() , VERTEX_SHADER));
  if (!$compileShader(p, gl, vertexShader, vertexSource)) {
    $logError(_instance_3, 'GPUProgram: ERROR compiling vertex shader :\n %s\n', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [vertexSource]));
    $printShaderInfoLog(gl._nativeGL, vertexShader);
    $deleteShader(gl._nativeGL, vertexShader) || $logError(_instance_3, 'GPUProgram: Problem encountered while deleting shader.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    $deleteProgram(gl, p) || $logError(_instance_3, 'GPUProgram: Problem encountered while deleting program.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return null;
  }
  fragmentShader = $createShader(gl, FRAGMENT_SHADER);
  if (!$compileShader(p, gl, fragmentShader, fragmentSource)) {
    $logError(_instance_3, 'GPUProgram: ERROR compiling fragment shader :\n %s\n', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [fragmentSource]));
    $printShaderInfoLog(gl._nativeGL, fragmentShader);
    $deleteShader(gl._nativeGL, fragmentShader) || $logError(_instance_3, 'GPUProgram: Problem encountered while deleting shader.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    $deleteProgram(gl, p) || $logError(_instance_3, 'GPUProgram: Problem encountered while deleting program.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return null;
  }
  if (!$linkProgram(gl, p._programID)) {
    $logError(_instance_3, 'GPUProgram: ERROR linking graphic program\n', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    $deleteShader(gl._nativeGL, vertexShader) || $logError(_instance_3, 'GPUProgram: Problem encountered while deleting shader.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    $deleteShader(gl._nativeGL, fragmentShader) || $logError(_instance_3, 'GPUProgram: Problem encountered while deleting shader.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    $deleteProgram(gl, p) || $logError(_instance_3, 'GPUProgram: Problem encountered while deleting program.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    $logError(_instance_3, 'GPUProgram: ERROR linking graphic program', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return null;
  }
  $deleteShader(gl._nativeGL, vertexShader) || $logError(_instance_3, 'GPUProgram: Problem encountered while deleting shader.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $deleteShader(gl._nativeGL, fragmentShader) || $logError(_instance_3, 'GPUProgram: Problem encountered while deleting shader.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $getVariables(p, gl);
  $getError(gl._nativeGL) != _noError && $logError(_instance_3, 'Error while compiling program', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  return p;
}

defineClass(101, 1, {101:1}, GPUProgram);
_.getProgramID = function getProgramID(){
  return this._programID;
}
;
_._attributesCode = 0;
_._nAttributes = 0;
_._nReferences = 0;
_._nUniforms = 0;
_._programID = 0;
_._uniformsCode = 0;
var Lorg_glob3_mobile_generated_GPUProgram_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUProgram', 101);
function $compileProgramWithName(this$static, gl, name_0){
  var prog, ps;
  prog = dynamicCast($getStringValue(this$static._programs, name_0), 101);
  if (!prog) {
    ps = $get_1(this$static._factory, name_0);
    if (ps) {
      prog = createProgram(gl, ps._name, ps._vertexSource, ps._fragmentSource);
      if (!prog) {
        $logError(_instance_3, 'Problem at creating program named %s.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [name_0]));
        return null;
      }
      $putStringValue(this$static._programs, name_0, prog);
    }
     else {
      $logError(_instance_3, 'No shader sources for program named %s.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [name_0]));
    }
  }
  return prog;
}

function $getCompiledProgram(this$static, uniformsCode, attributesCode){
  var p, p$iterator;
  for (p$iterator = $iterator_0(new AbstractMap$2(this$static._programs)); $hasNext(p$iterator.val$outerIter2);) {
    p = dynamicCast($next_1(p$iterator), 101);
    if (p._uniformsCode == uniformsCode && p._attributesCode == attributesCode) {
      return p;
    }
  }
  return null;
}

function $getNewProgram(this$static, gl, uniformsCode, attributesCode){
  var billboard, color_0, flatColor, hasLight, hasTexture2, is2D, rotationTC, texture, transformTC;
  texture = hasAttribute(attributesCode, ($clinit_GPUAttributeKey() , TEXTURE_COORDS));
  flatColor = hasUniform(uniformsCode, ($clinit_GPUUniformKey() , FLAT_COLOR));
  billboard = hasUniform(uniformsCode, VIEWPORT_EXTENT);
  color_0 = hasAttribute(attributesCode, COLOR);
  transformTC = hasUniform(uniformsCode, TRANSLATION_TEXTURE_COORDS) || hasUniform(uniformsCode, SCALE_TEXTURE_COORDS);
  rotationTC = hasUniform(uniformsCode, ROTATION_ANGLE_TEXTURE_COORDS);
  hasLight = hasUniform(uniformsCode, AMBIENT_LIGHT_COLOR);
  hasTexture2 = hasUniform(uniformsCode, SAMPLER2);
  is2D = hasAttribute(attributesCode, POSITION_2D);
  if (is2D) {
    if (flatColor) {
      return $compileProgramWithName(this$static, gl, 'FlatColor2DMesh');
    }
    return $compileProgramWithName(this$static, gl, 'Textured2DMesh');
  }
  if (billboard) {
    if (transformTC) {
      return $compileProgramWithName(this$static, gl, 'Billboard_TransformedTexCoor');
    }
    return $compileProgramWithName(this$static, gl, 'Billboard');
  }
  if (flatColor && !texture && !color_0) {
    if (hasLight) {
      return $compileProgramWithName(this$static, gl, 'FlatColorMesh_DirectionLight');
    }
    return $compileProgramWithName(this$static, gl, 'FlatColorMesh');
  }
  if (!flatColor && texture && !color_0) {
    if (hasTexture2) {
      if (transformTC) {
        if (rotationTC) {
          return $compileProgramWithName(this$static, gl, 'FullTransformedTexCoorMultiTexturedMesh');
        }
        return $compileProgramWithName(this$static, gl, 'TransformedTexCoorMultiTexturedMesh');
      }
      return $compileProgramWithName(this$static, gl, 'MultiTexturedMesh');
    }
    if (hasLight) {
      if (transformTC) {
        return $compileProgramWithName(this$static, gl, 'TransformedTexCoorTexturedMesh_DirectionLight');
      }
      return $compileProgramWithName(this$static, gl, 'TexturedMesh_DirectionLight');
    }
    if (transformTC) {
      if (rotationTC) {
        return $compileProgramWithName(this$static, gl, 'FullTransformedTexCoorTexturedMesh');
      }
      return $compileProgramWithName(this$static, gl, 'TransformedTexCoorTexturedMesh');
    }
    return $compileProgramWithName(this$static, gl, 'TexturedMesh');
  }
  if (!flatColor && !texture && color_0) {
    return $compileProgramWithName(this$static, gl, 'ColorMesh');
  }
  if (!flatColor && !texture && !color_0) {
    return $compileProgramWithName(this$static, gl, 'NoColorMesh');
  }
  return null;
}

function $getProgram(this$static, gl, uniformsCode, attributesCode){
  var p;
  p = $getCompiledProgram(this$static, uniformsCode, attributesCode);
  if (!p) {
    p = $getNewProgram(this$static, gl, uniformsCode, attributesCode);
    if (!p) {
      $logError(_instance_3, 'Problem at compiling program.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
      return null;
    }
    (p._attributesCode != attributesCode || p._uniformsCode != uniformsCode) && $logError(_instance_3, 'New compiled program does not match GL state.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
  ++p._nReferences;
  return p;
}

function $removeUnused(this$static){
  var entry, iterator, program;
  iterator = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this$static._programs)).this$01);
  while ($hasNext(iterator)) {
    entry = (checkStructuralChange(iterator.this$01, iterator) , checkCriticalElement($hasNext(iterator)) , iterator.last = iterator.current , dynamicCast(iterator.current.next_0(), 22));
    program = dynamicCast(entry.getValue(), 101);
    if (program._nReferences == 0) {
      $logInfo(_instance_3, 'Deleting program %s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [program._name]));
      $remove_3(iterator);
    }
  }
}

function GPUProgramManager(factory){
  this._programs = new HashMap;
  this._factory = factory;
}

defineClass(311, 1, {}, GPUProgramManager);
var Lorg_glob3_mobile_generated_GPUProgramManager_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUProgramManager', 311);
function GPUProgramSources(name_0, vertexSource, fragmentSource){
  this._name = name_0;
  this._vertexSource = vertexSource;
  this._fragmentSource = fragmentSource;
}

defineClass(21, 1, {21:1}, GPUProgramSources);
var Lorg_glob3_mobile_generated_GPUProgramSources_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUProgramSources', 21);
function $applyChanges_2(this$static, gl){
  if (this$static._dirty) {
    this$static._value.setUniform(gl, this$static._id);
    this$static._dirty = false;
  }
   else {
    !this$static._value && $logError(_instance_3, 'Uniform ' + this$static._name + ' was not set.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
}

function $set_2(this$static, v){
  if (this$static._type == v._type) {
    if (!this$static._value || !this$static._value.isEquals_0(v)) {
      this$static._dirty = true;
      ++v._referenceCounter;
      !!this$static._value && $_release(this$static._value);
      this$static._value = v;
    }
  }
   else {
    $logError(_instance_3, 'Attempting to set uniform ' + this$static._name + ' with invalid value type.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
}

function $unset_0(this$static){
  if (this$static._value) {
    $_release(this$static._value);
    this$static._value = null;
  }
  this$static._dirty = false;
}

function GPUUniform(name_0, id_0, type_0){
  GPUVariable.call(this, name_0, $clinit_GPUVariableType());
  this._id = id_0;
  this._dirty = false;
  this._value = null;
  this._type = type_0;
  this._key = getUniformKey(name_0);
}

defineClass(30, 35, $intern_51);
_._dirty = false;
_._type = 0;
var Lorg_glob3_mobile_generated_GPUUniform_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniform', 30);
function GPUUniformBool(name_0, id_0){
  GPUUniform.call(this, name_0, id_0, _bool);
}

defineClass(494, 30, $intern_51, GPUUniformBool);
var Lorg_glob3_mobile_generated_GPUUniformBool_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformBool', 494);
function GPUUniformFloat(name_0, id_0){
  GPUUniform.call(this, name_0, id_0, _float);
}

defineClass(491, 30, $intern_51, GPUUniformFloat);
var Lorg_glob3_mobile_generated_GPUUniformFloat_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformFloat', 491);
function $clinit_GPUUniformKey(){
  $clinit_GPUUniformKey = emptyMethod;
  UNRECOGNIZED_UNIFORM = new GPUUniformKey('UNRECOGNIZED_UNIFORM', 0, -1);
  FLAT_COLOR = new GPUUniformKey('FLAT_COLOR', 1, 0);
  MODELVIEW = new GPUUniformKey('MODELVIEW', 2, 1);
  TEXTURE_EXTENT = new GPUUniformKey('TEXTURE_EXTENT', 3, 2);
  VIEWPORT_EXTENT = new GPUUniformKey('VIEWPORT_EXTENT', 4, 3);
  TRANSLATION_TEXTURE_COORDS = new GPUUniformKey('TRANSLATION_TEXTURE_COORDS', 5, 4);
  SCALE_TEXTURE_COORDS = new GPUUniformKey('SCALE_TEXTURE_COORDS', 6, 5);
  POINT_SIZE = new GPUUniformKey('POINT_SIZE', 7, 6);
  AMBIENT_LIGHT_COLOR = new GPUUniformKey('AMBIENT_LIGHT_COLOR', 8, 7);
  DIFFUSE_LIGHT_DIRECTION = new GPUUniformKey('DIFFUSE_LIGHT_DIRECTION', 9, 8);
  DIFFUSE_LIGHT_COLOR = new GPUUniformKey('DIFFUSE_LIGHT_COLOR', 10, 9);
  PROJECTION = new GPUUniformKey('PROJECTION', 11, 10);
  CAMERA_MODEL = new GPUUniformKey('CAMERA_MODEL', 12, 11);
  MODEL = new GPUUniformKey('MODEL', 13, 12);
  POINT_LIGHT_POSITION = new GPUUniformKey('POINT_LIGHT_POSITION', 14, 13);
  POINT_LIGHT_COLOR = new GPUUniformKey('POINT_LIGHT_COLOR', 15, 14);
  BILLBOARD_POSITION = new GPUUniformKey('BILLBOARD_POSITION', 16, 15);
  ROTATION_CENTER_TEXTURE_COORDS = new GPUUniformKey('ROTATION_CENTER_TEXTURE_COORDS', 17, 16);
  ROTATION_ANGLE_TEXTURE_COORDS = new GPUUniformKey('ROTATION_ANGLE_TEXTURE_COORDS', 18, 17);
  SAMPLER = new GPUUniformKey('SAMPLER', 19, 18);
  SAMPLER2 = new GPUUniformKey('SAMPLER2', 20, 19);
  SAMPLER3 = new GPUUniformKey('SAMPLER3', 21, 20);
  TRANSLATION_2D = new GPUUniformKey('TRANSLATION_2D', 22, 21);
  BILLBOARD_ANCHOR = new GPUUniformKey('BILLBOARD_ANCHOR', 23, 22);
}

function GPUUniformKey(enum$name, enum$ordinal, value_0){
  Enum.call(this, enum$name, enum$ordinal);
  this.intValue = value_0;
  $put((!mappings_2 && !mappings_2 && (mappings_2 = new HashMap) , mappings_2), valueOf(value_0), this);
}

function values_4(){
  $clinit_GPUUniformKey();
  return initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_GPUUniformKey_2_classLit, 1), $intern_4, 18, 0, [UNRECOGNIZED_UNIFORM, FLAT_COLOR, MODELVIEW, TEXTURE_EXTENT, VIEWPORT_EXTENT, TRANSLATION_TEXTURE_COORDS, SCALE_TEXTURE_COORDS, POINT_SIZE, AMBIENT_LIGHT_COLOR, DIFFUSE_LIGHT_DIRECTION, DIFFUSE_LIGHT_COLOR, PROJECTION, CAMERA_MODEL, MODEL, POINT_LIGHT_POSITION, POINT_LIGHT_COLOR, BILLBOARD_POSITION, ROTATION_CENTER_TEXTURE_COORDS, ROTATION_ANGLE_TEXTURE_COORDS, SAMPLER, SAMPLER2, SAMPLER3, TRANSLATION_2D, BILLBOARD_ANCHOR]);
}

defineClass(18, 15, {3:1, 26:1, 15:1, 18:1}, GPUUniformKey);
_.intValue = 0;
var AMBIENT_LIGHT_COLOR, BILLBOARD_ANCHOR, BILLBOARD_POSITION, CAMERA_MODEL, DIFFUSE_LIGHT_COLOR, DIFFUSE_LIGHT_DIRECTION, FLAT_COLOR, MODEL, MODELVIEW, POINT_LIGHT_COLOR, POINT_LIGHT_POSITION, POINT_SIZE, PROJECTION, ROTATION_ANGLE_TEXTURE_COORDS, ROTATION_CENTER_TEXTURE_COORDS, SAMPLER, SAMPLER2, SAMPLER3, SCALE_TEXTURE_COORDS, TEXTURE_EXTENT, TRANSLATION_2D, TRANSLATION_TEXTURE_COORDS, UNRECOGNIZED_UNIFORM, VIEWPORT_EXTENT, mappings_2;
var Lorg_glob3_mobile_generated_GPUUniformKey_2_classLit = createForEnum('org.glob3.mobile.generated', 'GPUUniformKey', 18, values_4);
function GPUUniformMatrix4Float(name_0, id_0){
  GPUUniform.call(this, name_0, id_0, _matrix4Float);
}

defineClass(489, 30, $intern_51, GPUUniformMatrix4Float);
var Lorg_glob3_mobile_generated_GPUUniformMatrix4Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformMatrix4Float', 489);
function GPUUniformSampler2D(name_0, id_0){
  GPUUniform.call(this, name_0, id_0, _int);
}

defineClass(495, 30, $intern_51, GPUUniformSampler2D);
var Lorg_glob3_mobile_generated_GPUUniformSampler2D_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformSampler2D', 495);
function GPUUniformValue(type_0){
  RCObject.call(this);
  this._type = type_0;
}

defineClass(28, 9, {28:1, 9:1});
_.dispose = function dispose_30(){
  $dispose_0(this);
}
;
_.toString$ = function toString_30(){
  return this.description_0();
}
;
_._type = 0;
var Lorg_glob3_mobile_generated_GPUUniformValue_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValue', 28);
function GPUUniformValueFloat(d){
  GPUUniformValue.call(this, _float);
  this._value = d;
}

defineClass(107, 28, $intern_52, GPUUniformValueFloat);
_.description_0 = function description_0(){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'Uniform Value Float: ';
  $addDouble(isb, this._value);
  s = isb._string;
  return s;
}
;
_.dispose = function dispose_31(){
  $dispose_0(this);
}
;
_.isEquals_0 = function isEquals_0(v){
  var v2;
  v2 = dynamicCast(v, 107);
  return this._value == v2._value;
}
;
_.setUniform = function setUniform(gl, id_0){
  $uniform1f(gl, id_0, this._value);
}
;
_._value = 0;
var Lorg_glob3_mobile_generated_GPUUniformValueFloat_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValueFloat', 107);
function $changeValue(this$static, x_0){
  this$static._value = x_0;
}

function GPUUniformValueFloatMutable(x_0){
  GPUUniformValueFloat.call(this, x_0);
}

defineClass(401, 107, $intern_52, GPUUniformValueFloatMutable);
_.dispose = function dispose_32(){
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_GPUUniformValueFloatMutable_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValueFloatMutable', 401);
function GPUUniformValueInt(b){
  GPUUniformValue.call(this, _int);
  this._value = b;
}

defineClass(157, 28, {28:1, 157:1, 9:1}, GPUUniformValueInt);
_.description_0 = function description_1(){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'Uniform Value Integer: ';
  $addInt(isb, this._value);
  s = isb._string;
  return s;
}
;
_.dispose = function dispose_33(){
  $dispose_0(this);
}
;
_.isEquals_0 = function isEquals_1(v){
  return this._value == dynamicCast(v, 157)._value;
}
;
_.setUniform = function setUniform_0(gl, id_0){
  $uniform1i(gl, id_0, this._value);
}
;
_._value = 0;
var Lorg_glob3_mobile_generated_GPUUniformValueInt_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValueInt', 157);
function GPUUniformValueMatrix4(provider){
  GPUUniformValue.call(this, _matrix4Float);
  this._provider = provider;
  this._lastModelSet = null;
  ++this._provider._referenceCounter;
}

defineClass(131, 28, {28:1, 131:1, 9:1}, GPUUniformValueMatrix4);
_.description_0 = function description_2(){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'Uniform Value Matrix44D.';
  s = isb._string;
  return s;
}
;
_.dispose = function dispose_34(){
  $_release(this._provider);
  !!this._lastModelSet && $_release(this._lastModelSet);
  $dispose_0(this);
}
;
_.isEquals_0 = function isEquals_2(v){
  if (this._lastModelSet == $getMatrix(dynamicCast(v, 131)._provider)) {
    return true;
  }
  return false;
}
;
_.setUniform = function setUniform_1(gl, id_0){
  !!this._lastModelSet && $_release(this._lastModelSet);
  this._lastModelSet = $getMatrix(this._provider);
  ++this._lastModelSet._referenceCounter;
  $uniformMatrix4fv(gl, id_0, this._lastModelSet);
}
;
var Lorg_glob3_mobile_generated_GPUUniformValueMatrix4_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValueMatrix4', 131);
defineClass(130, 28, $intern_53);
_.description_0 = function description_3(){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'Uniform Value Vec2Float: x:';
  $addDouble(isb, this._x);
  isb._string += 'y:';
  $addDouble(isb, this._y);
  s = isb._string;
  return s;
}
;
_.dispose = function dispose_35(){
  $dispose_0(this);
}
;
_.isEquals_0 = function isEquals_3(v){
  var v2;
  v2 = dynamicCast(v, 130);
  return this._x == v2._x && this._y == v2._y;
}
;
_.setUniform = function setUniform_2(gl, id_0){
  $uniform2f(gl, id_0, this._x, this._y);
}
;
_._x = 0;
_._y = 0;
var Lorg_glob3_mobile_generated_GPUUniformValueVec2Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValueVec2Float', 130);
function $changeValue_0(this$static, x_0, y_0){
  this$static._x = x_0;
  this$static._y = y_0;
}

function GPUUniformValueVec2FloatMutable(x_0, y_0){
  GPUUniformValue.call(this, _vec2Float);
  this._x = x_0;
  this._y = y_0;
}

defineClass(156, 130, $intern_53, GPUUniformValueVec2FloatMutable);
_.dispose = function dispose_36(){
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_GPUUniformValueVec2FloatMutable_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValueVec2FloatMutable', 156);
function GPUUniformValueVec3Float(color_0){
  GPUUniformValue.call(this, _vec3Float);
  this._x = color_0._red;
  this._y = color_0._green;
  this._z = color_0._blue;
}

defineClass(88, 28, $intern_54, GPUUniformValueVec3Float);
_.description_0 = function description_4(){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'Uniform Value Vec4Float: x:';
  $addDouble(isb, this._x);
  isb._string += 'y:';
  $addDouble(isb, this._y);
  isb._string += 'z:';
  $addDouble(isb, this._z);
  s = isb._string;
  return s;
}
;
_.dispose = function dispose_37(){
  $dispose_0(this);
}
;
_.isEquals_0 = function isEquals_4(v){
  var v2;
  v2 = dynamicCast(v, 88);
  return this._x == v2._x && this._y == v2._y && this._z == v2._z;
}
;
_.setUniform = function setUniform_3(gl, id_0){
  $uniform3f(gl, id_0, this._x, this._y, this._z);
}
;
_._x = 0;
_._y = 0;
_._z = 0;
var Lorg_glob3_mobile_generated_GPUUniformValueVec3Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValueVec3Float', 88);
function $changeValue_1(this$static, x_0, y_0, z_0){
  this$static._x = x_0;
  this$static._y = y_0;
  this$static._z = z_0;
}

function GPUUniformValueVec3FloatMutable(x_0, y_0, z_0){
  GPUUniformValue.call(this, _vec3Float);
  this._x = x_0;
  this._y = y_0;
  this._z = z_0;
}

defineClass(371, 88, $intern_54, GPUUniformValueVec3FloatMutable);
_.dispose = function dispose_38(){
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_GPUUniformValueVec3FloatMutable_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValueVec3FloatMutable', 371);
function GPUUniformValueVec4Float(x_0, y_0, z_0, w){
  GPUUniformValue.call(this, _vec4Float);
  this._x = x_0;
  this._y = y_0;
  this._z = z_0;
  this._w = w;
}

defineClass(154, 28, {28:1, 154:1, 9:1}, GPUUniformValueVec4Float);
_.description_0 = function description_5(){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'Uniform Value Vec4Float: x:';
  $addDouble(isb, this._x);
  isb._string += 'y:';
  $addDouble(isb, this._y);
  isb._string += 'z:';
  $addDouble(isb, this._z);
  isb._string += 'w:';
  $addDouble(isb, this._w);
  s = isb._string;
  return s;
}
;
_.dispose = function dispose_39(){
  $dispose_0(this);
}
;
_.isEquals_0 = function isEquals_5(v){
  var v2;
  v2 = dynamicCast(v, 154);
  return this._x == v2._x && this._y == v2._y && this._z == v2._z && this._w == v2._w;
}
;
_.setUniform = function setUniform_4(gl, id_0){
  $uniform4f(gl, id_0, this._x, this._y, this._z, this._w);
}
;
_._w = 0;
_._x = 0;
_._y = 0;
_._z = 0;
var Lorg_glob3_mobile_generated_GPUUniformValueVec4Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformValueVec4Float', 154);
function GPUUniformVec2Float(name_0, id_0){
  GPUUniform.call(this, name_0, id_0, _vec2Float);
}

defineClass(492, 30, $intern_51, GPUUniformVec2Float);
var Lorg_glob3_mobile_generated_GPUUniformVec2Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformVec2Float', 492);
function GPUUniformVec3Float(name_0, id_0){
  GPUUniform.call(this, name_0, id_0, _vec3Float);
}

defineClass(493, 30, $intern_51, GPUUniformVec3Float);
var Lorg_glob3_mobile_generated_GPUUniformVec3Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformVec3Float', 493);
function GPUUniformVec4Float(name_0, id_0){
  GPUUniform.call(this, name_0, id_0, _vec4Float);
}

defineClass(490, 30, $intern_51, GPUUniformVec4Float);
var Lorg_glob3_mobile_generated_GPUUniformVec4Float_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUUniformVec4Float', 490);
function $clinit_GPUVariableType(){
  $clinit_GPUVariableType = emptyMethod;
  ATTRIBUTE = new GPUVariableType('ATTRIBUTE', 0, 1);
  UNIFORM = new GPUVariableType('UNIFORM', 1, 2);
}

function GPUVariableType(enum$name, enum$ordinal, value_0){
  Enum.call(this, enum$name, enum$ordinal);
  $put((!mappings_3 && !mappings_3 && (mappings_3 = new HashMap) , mappings_3), valueOf(value_0), this);
}

function values_5(){
  $clinit_GPUVariableType();
  return initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_GPUVariableType_2_classLit, 1), $intern_4, 133, 0, [ATTRIBUTE, UNIFORM]);
}

defineClass(133, 15, {3:1, 26:1, 15:1, 133:1}, GPUVariableType);
var ATTRIBUTE, UNIFORM, mappings_3;
var Lorg_glob3_mobile_generated_GPUVariableType_2_classLit = createForEnum('org.glob3.mobile.generated', 'GPUVariableType', 133, values_5);
function $addAttributeValue(this$static, key, v){
  var index_0;
  index_0 = key.intValue;
  setCheck(this$static._attributeValues, index_0, v);
  index_0 > this$static._highestAttributeKey && (this$static._highestAttributeKey = index_0);
}

function $addUniformValue(this$static, key, v){
  var index_0;
  index_0 = key.intValue;
  setCheck(this$static._uniformValues, index_0, v);
  index_0 > this$static._highestUniformKey && (this$static._highestUniformKey = index_0);
}

function $applyValuesToProgram(this$static, prog){
  var a, i_0, i0, u;
  for (i0 = 0; i0 <= this$static._highestUniformKey; i0++) {
    u = this$static._uniformValues[i0];
    !!u && $setGPUUniformValue(prog, i0, u);
  }
  for (i_0 = 0; i_0 <= this$static._highestAttributeKey; i_0++) {
    a = this$static._attributeValues[i_0];
    !!a && $setGPUAttributeValue(prog, i_0, a);
  }
}

function $combineWith(this$static, vs){
  var i_0, i0;
  for (i0 = 0; i0 <= vs._highestUniformKey; i0++) {
    if (vs._uniformValues[i0]) {
      setCheck(this$static._uniformValues, i0, vs._uniformValues[i0]);
      ++this$static._uniformValues[i0]._referenceCounter;
      i0 > this$static._highestUniformKey && (this$static._highestUniformKey = i0);
    }
  }
  for (i_0 = 0; i_0 <= vs._highestAttributeKey; i_0++) {
    if (vs._attributeValues[i_0]) {
      setCheck(this$static._attributeValues, i_0, vs._attributeValues[i_0]);
      ++this$static._attributeValues[i_0]._referenceCounter;
      i_0 > this$static._highestAttributeKey && (this$static._highestAttributeKey = i_0);
    }
  }
}

function $dispose_4(this$static){
  var a, i_0, i0, u;
  for (i0 = 0; i0 <= this$static._highestUniformKey; i0++) {
    u = this$static._uniformValues[i0];
    !!u && $_release(u);
  }
  for (i_0 = 0; i_0 <= this$static._highestAttributeKey; i_0++) {
    a = this$static._attributeValues[i_0];
    !!a && $_release(a);
  }
}

function $getAttributesCode(this$static){
  var i_0;
  if (this$static._attributeCode == 0) {
    for (i_0 = 0; i_0 <= this$static._highestAttributeKey; i_0++) {
      !!this$static._attributeValues[i_0] && (this$static._attributeCode = this$static._attributeCode | 1 << i_0);
    }
  }
  return this$static._attributeCode;
}

function $getUniformsCode(this$static){
  var i_0;
  if (this$static._uniformsCode == 0) {
    for (i_0 = 0; i_0 <= this$static._highestUniformKey; i_0++) {
      !!this$static._uniformValues[i_0] && (this$static._uniformsCode = this$static._uniformsCode | 1 << i_0);
    }
  }
  return this$static._uniformsCode;
}

function $removeUniformValue(this$static, key){
  var i_0, index_0;
  index_0 = key.intValue;
  if (this$static._uniformValues[index_0]) {
    $_release(this$static._uniformValues[index_0]);
    setCheck(this$static._uniformValues, index_0, null);
  }
  for (i_0 = 0; i_0 < 32; i_0++) {
    !!this$static._uniformValues[i_0] && (this$static._highestUniformKey = i_0);
  }
}

function GPUVariableValueSet(){
  var i_0;
  this._uniformValues = initDim(Lorg_glob3_mobile_generated_GPUUniformValue_2_classLit, $intern_4, 28, 32, 0, 1);
  this._attributeValues = initDim(Lorg_glob3_mobile_generated_GPUAttributeValue_2_classLit, $intern_4, 86, 32, 0, 1);
  this._highestAttributeKey = 0;
  this._highestUniformKey = 0;
  this._uniformsCode = 0;
  this._attributeCode = 0;
  for (i_0 = 0; i_0 < 32; i_0++) {
    setCheck(this._uniformValues, i_0, null);
    setCheck(this._attributeValues, i_0, null);
  }
}

defineClass(191, 1, {}, GPUVariableValueSet);
_._attributeCode = 0;
_._highestAttributeKey = 0;
_._highestUniformKey = 0;
_._uniformsCode = 0;
var Lorg_glob3_mobile_generated_GPUVariableValueSet_2_classLit = createForClass('org.glob3.mobile.generated', 'GPUVariableValueSet', 191);
defineClass(457, 1, {});
var Lorg_glob3_mobile_generated_GTask_2_classLit = createForClass('org.glob3.mobile.generated', 'GTask', 457);
function $description_0(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += '(lat=';
  $addString(isb, $description(this$static._latitude));
  isb._string += ', lon=';
  $addString(isb, $description(this$static._longitude));
  isb._string += ')';
  s = isb._string;
  return s;
}

function $equals_2(this$static, obj){
  var other;
  if (this$static === obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  if (Lorg_glob3_mobile_generated_Geodetic2D_2_classLit != getClass__Ljava_lang_Class___devirtual$(obj)) {
    return false;
  }
  other = dynamicCast(obj, 12);
  if (!this$static._latitude) {
    if (other._latitude) {
      return false;
    }
  }
   else if (!$equals_1(this$static._latitude, other._latitude)) {
    return false;
  }
  if (!this$static._longitude) {
    if (other._longitude) {
      return false;
    }
  }
   else if (!$equals_1(this$static._longitude, other._longitude)) {
    return false;
  }
  return true;
}

function $hashCode_0(this$static){
  var result;
  result = 31 + (!this$static._latitude?0:$hashCode(this$static._latitude));
  result = 31 * result + (!this$static._longitude?0:$hashCode(this$static._longitude));
  return result;
}

function $isEquals_0(this$static, that){
  return $isEquals(this$static._latitude, that._latitude) && $isEquals(this$static._longitude, that._longitude);
}

function Geodetic2D(latitude, longitude){
  this._latitude = new Angle_0(latitude);
  this._longitude = new Angle_0(longitude);
}

function Geodetic2D_0(g){
  this._latitude = new Angle_0(g._latitude);
  this._longitude = new Angle_0(g._longitude);
}

function fromDegrees_0(lat, lon){
  return new Geodetic2D(new Angle(lat, lat / 180 * $intern_37), new Angle(lon, lon / 180 * $intern_37));
}

defineClass(12, 1, {12:1}, Geodetic2D, Geodetic2D_0);
_.equals$ = function equals_17(obj){
  return $equals_2(this, obj);
}
;
_.hashCode$ = function hashCode_22(){
  return $hashCode_0(this);
}
;
_.toString$ = function toString_31(){
  return $description_0(this);
}
;
var Lorg_glob3_mobile_generated_Geodetic2D_2_classLit = createForClass('org.glob3.mobile.generated', 'Geodetic2D', 12);
function $description_1(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += '(lat=';
  $addString(isb, $description(this$static._latitude));
  isb._string += ', lon=';
  $addString(isb, $description(this$static._longitude));
  isb._string += ', height=';
  $addDouble(isb, this$static._height);
  isb._string += ')';
  s = isb._string;
  return s;
}

function Geodetic3D(latitude, longitude, height){
  this._latitude = new Angle_0(latitude);
  this._longitude = new Angle_0(longitude);
  this._height = height;
}

function Geodetic3D_0(g2, height){
  this._latitude = new Angle_0(g2._latitude);
  this._longitude = new Angle_0(g2._longitude);
  this._height = height;
}

function Geodetic3D_1(g){
  this._latitude = new Angle_0(g._latitude);
  this._longitude = new Angle_0(g._longitude);
  this._height = g._height;
}

function fromDegrees_1(lat, lon, height){
  return new Geodetic3D(new Angle(lat, lat / 180 * $intern_37), new Angle(lon, lon / 180 * $intern_37), height);
}

defineClass(20, 1, {}, Geodetic3D, Geodetic3D_0, Geodetic3D_1);
_.toString$ = function toString_32(){
  return $description_1(this);
}
;
_._height = 0;
var Lorg_glob3_mobile_generated_Geodetic3D_2_classLit = createForClass('org.glob3.mobile.generated', 'Geodetic3D', 20);
function GeometryGLFeature(buffer, depthTestEnabled, polygonOffsetFactor, polygonOffsetUnits, lineWidth, pointSize){
  GLFeature.call(this, ($clinit_GLFeatureGroupName() , NO_GROUP), 2);
  this._depthTestEnabled = depthTestEnabled;
  this._cullFace = false;
  this._culledFace = 0;
  this._polygonOffsetFill = false;
  this._polygonOffsetFactor = polygonOffsetFactor;
  this._polygonOffsetUnits = polygonOffsetUnits;
  this._lineWidth = lineWidth;
  this._position = new GPUAttributeValueVec4Float(buffer, 3);
  $addAttributeValue(this._values, ($clinit_GPUAttributeKey() , POSITION), this._position);
  $addUniformValue(this._values, ($clinit_GPUUniformKey() , POINT_SIZE), new GPUUniformValueFloat(pointSize));
}

defineClass(205, 27, {27:1, 9:1}, GeometryGLFeature);
_.applyOnGlobalGLState = function applyOnGlobalGLState_3(state){
  this._depthTestEnabled?(state._depthTest = true):(state._depthTest = false);
  this._cullFace?$enableCullFace(state, this._culledFace):(state._cullFace = false);
  this._polygonOffsetFill?$enablePolygonOffsetFill(state, this._polygonOffsetFactor, this._polygonOffsetUnits):(state._polygonOffsetFill = false);
  $setLineWidth(state, this._lineWidth);
}
;
_.dispose = function dispose_40(){
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
_._cullFace = false;
_._culledFace = 0;
_._depthTestEnabled = false;
_._lineWidth = 0;
_._polygonOffsetFactor = 0;
_._polygonOffsetFill = false;
_._polygonOffsetUnits = 0;
var Lorg_glob3_mobile_generated_GeometryGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'GeometryGLFeature', 205);
function $clinit_Gesture(){
  $clinit_Gesture = emptyMethod;
  None = new Gesture('None', 0);
  Drag = new Gesture('Drag', 1);
  Zoom = new Gesture('Zoom', 2);
  Rotate = new Gesture('Rotate', 3);
  DoubleDrag = new Gesture('DoubleDrag', 4);
}

function Gesture(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_6(){
  $clinit_Gesture();
  return initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_Gesture_2_classLit, 1), $intern_4, 78, 0, [None, Drag, Zoom, Rotate, DoubleDrag]);
}

defineClass(78, 15, {3:1, 26:1, 15:1, 78:1}, Gesture);
var DoubleDrag, Drag, None, Rotate, Zoom;
var Lorg_glob3_mobile_generated_Gesture_2_classLit = createForEnum('org.glob3.mobile.generated', 'Gesture', 78, values_6);
function $onResizeViewportEvent_1(this$static, ec, width_0, height){
  $onResizeViewportEvent_2(this$static._hudImageRenderer, width_0, height);
}

function $render_3(this$static, rc, glState){
  $render_4(this$static._hudImageRenderer, rc);
}

function $setErrors(this$static, errors){
  var factory;
  factory = this$static._hudImageRenderer._imageFactory;
  $setErrors_0(factory, errors) && $recreateImage(this$static._hudImageRenderer);
}

function HUDErrorRenderer(){
  this._hudImageRenderer = new HUDImageRenderer(new HUDErrorRenderer_ImageFactory);
}

defineClass(183, 1, {}, HUDErrorRenderer);
_.initialize = function initialize_3(context){
}
;
_.onResizeViewportEvent = function onResizeViewportEvent_2(ec, width_0, height){
  $onResizeViewportEvent_1(this, ec, width_0, height);
}
;
_.render = function render_7(rc, glState){
  $render_3(this, rc, glState);
}
;
_.start_2 = function start_9(rc){
}
;
_.stop_1 = function stop_7(rc){
  $stop(this._hudImageRenderer);
}
;
var Lorg_glob3_mobile_generated_HUDErrorRenderer_2_classLit = createForClass('org.glob3.mobile.generated', 'HUDErrorRenderer', 183);
function $create_0(this$static, width_0, height, listener){
  var canvas;
  canvas = new Canvas_WebGL;
  $initialize_2(canvas, width_0, height);
  $setFillColor(canvas, new Color(0, 0, 0, 1));
  canvas._canvasWidth > 0 && canvas._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $_fillRectangle(canvas, 0, 0, width_0, height);
  drawStringsOn(this$static._errors, canvas, width_0, height, new Color(1, 1, 1, 1), new Color(0.8999999761581421, $intern_55, $intern_55, 1), new Color(0, 0, 0, 0));
  canvas._canvasWidth > 0 && canvas._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $_createImage(canvas, listener, true);
}

defineClass(476, 1, {});
var Lorg_glob3_mobile_generated_HUDImageRenderer$CanvasImageFactory_2_classLit = createForClass('org.glob3.mobile.generated', 'HUDImageRenderer/CanvasImageFactory', 476);
function $isEquals_1(v1, v2){
  var i_0, size1, size2, str1, str2;
  size1 = v1.array.length;
  size2 = v2.array.length;
  if (size1 != size2) {
    return false;
  }
  for (i_0 = 0; i_0 < size1; i_0++) {
    str1 = (checkElementIndex(i_0, v1.array.length) , dynamicCastToString(v1.array[i_0]));
    str2 = (checkElementIndex(i_0, v2.array.length) , dynamicCastToString(v2.array[i_0]));
    if (!$equals(str1, str2)) {
      return false;
    }
  }
  return true;
}

function $setErrors_0(this$static, errors){
  if ($isEquals_1(this$static._errors, errors)) {
    return false;
  }
  this$static._errors.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
  $addAll(this$static._errors, errors);
  return true;
}

function HUDErrorRenderer_ImageFactory(){
  this._errors = new ArrayList;
}

defineClass(366, 476, {}, HUDErrorRenderer_ImageFactory);
var Lorg_glob3_mobile_generated_HUDErrorRenderer_1ImageFactory_2_classLit = createForClass('org.glob3.mobile.generated', 'HUDErrorRenderer_ImageFactory', 366);
function $createMesh_0(this$static, rc){
  var $tmp, camera, halfHeight, halfWidth, mesh, texCoords, texId, textureMapping, textureName, vertices;
  this$static._creatingMesh = false;
  if (this$static._mesh) {
    !!this$static._mesh && $dispose_13(this$static._mesh);
    this$static._mesh = null;
  }
  textureName = 'HUDImageRenderer' + ('' + toString_8(this$static._instanceID)) + '/' + ('' + toString_8(($tmp = this$static._changeCounter , this$static._changeCounter = add_0(this$static._changeCounter, {l:1, m:0, h:0}) , $tmp)));
  texId = $getTextureIDReference(rc._texturesHandler, this$static._image, _rgba, textureName, false);
  this$static._image = null;
  this$static._image = null;
  camera = rc._currentCamera;
  halfWidth = camera._viewPortWidth / 2;
  halfHeight = camera._viewPortHeight / 2;
  vertices = new FloatBufferBuilderFromCartesian3D(($clinit_Vector3D() , zero));
  $add_9(vertices, -halfWidth, halfHeight);
  $add_9(vertices, -halfWidth, -halfHeight);
  $add_9(vertices, halfWidth, halfHeight);
  $add_9(vertices, halfWidth, -halfHeight);
  mesh = new DirectMesh(_triangleStrip, new Vector3D(vertices._cx, vertices._cy, vertices._cz), new FloatBuffer_WebGL_0(vertices._values._array, vertices._values._size));
  texCoords = new FloatBufferBuilderFromCartesian2D;
  $push_back(texCoords._values, 0);
  $push_back(texCoords._values, 0);
  $push_back(texCoords._values, 0);
  $push_back(texCoords._values, 1);
  $push_back(texCoords._values, 1);
  $push_back(texCoords._values, 0);
  $push_back(texCoords._values, 1);
  $push_back(texCoords._values, 1);
  textureMapping = new SimpleTextureMapping(texId, new FloatBuffer_WebGL_0(texCoords._values._array, texCoords._values._size));
  return new TexturedMesh(mesh, textureMapping);
}

function $getMesh_0(this$static, rc){
  var camera, height, width_0;
  if (!this$static._mesh) {
    if (!this$static._creatingMesh) {
      if (!this$static._image) {
        this$static._creatingMesh = true;
        camera = rc._currentCamera;
        width_0 = camera._viewPortWidth;
        height = camera._viewPortHeight;
        $create_0(this$static._imageFactory, width_0, height, new HUDImageRenderer$ImageListener(this$static));
      }
    }
    !!this$static._image && (this$static._mesh = $createMesh_0(this$static, rc));
  }
  return this$static._mesh;
}

function $onResizeViewportEvent_2(this$static, width_0, height){
  var halfHeight, halfWidth, pr, projectionMatrix;
  halfWidth = ~~(width_0 / 2);
  halfHeight = ~~(height / 2);
  projectionMatrix = createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
  pr = dynamicCast($getGLFeature(this$static._glState, 4), 120);
  !pr?$addGLFeature(this$static._glState, new ProjectionGLFeature((!projectionMatrix._matrix44D && (projectionMatrix._matrix44D = new Matrix44D(projectionMatrix._m00, projectionMatrix._m10, projectionMatrix._m20, projectionMatrix._m30, projectionMatrix._m01, projectionMatrix._m11, projectionMatrix._m21, projectionMatrix._m31, projectionMatrix._m02, projectionMatrix._m12, projectionMatrix._m22, projectionMatrix._m32, projectionMatrix._m03, projectionMatrix._m13, projectionMatrix._m23, projectionMatrix._m33)) , projectionMatrix._matrix44D)), false):$setMatrix(pr, (!projectionMatrix._matrix44D && (projectionMatrix._matrix44D = new Matrix44D(projectionMatrix._m00, projectionMatrix._m10, projectionMatrix._m20, projectionMatrix._m30, projectionMatrix._m01, projectionMatrix._m11, projectionMatrix._m21, projectionMatrix._m31, projectionMatrix._m02, projectionMatrix._m12, projectionMatrix._m22, projectionMatrix._m32, projectionMatrix._m03, projectionMatrix._m13, projectionMatrix._m23, projectionMatrix._m33)) , projectionMatrix._matrix44D));
  $recreateImage(this$static);
}

function $recreateImage(this$static){
  this$static._creatingMesh = false;
  !!this$static._mesh && $dispose_13(this$static._mesh);
  this$static._mesh = null;
  this$static._image = null;
  this$static._image = null;
}

function $render_4(this$static, rc){
  var mesh;
  mesh = $getMesh_0(this$static, rc);
  !!mesh && $render(mesh, rc, this$static._glState);
}

function $setImage(this$static, image){
  this$static._image = image;
}

function $stop(this$static){
  $recreateImage(this$static);
}

function HUDImageRenderer(imageFactory){
  var $tmp;
  DefaultRenderer.call(this);
  this._imageFactory = imageFactory;
  this._glState = new GLState;
  this._creatingMesh = false;
  this._image = null;
  this._mesh = null;
  this._instanceID = ($tmp = INSTANCE_COUNTER , INSTANCE_COUNTER = add_0(INSTANCE_COUNTER, {l:1, m:0, h:0}) , $tmp);
  this._changeCounter = {l:0, m:0, h:0};
}

defineClass(364, 179, {135:1}, HUDImageRenderer);
_.initialize = function initialize_4(context){
}
;
_.onResizeViewportEvent = function onResizeViewportEvent_3(ec, width_0, height){
  $onResizeViewportEvent_2(this, width_0, height);
}
;
_.render = function render_8(rc, glState){
  $render_4(this, rc);
}
;
_.stop_1 = function stop_8(rc){
  $stop(this);
}
;
_._changeCounter = {l:0, m:0, h:0};
_._creatingMesh = false;
_._instanceID = {l:0, m:0, h:0};
var INSTANCE_COUNTER = {l:0, m:0, h:0};
var Lorg_glob3_mobile_generated_HUDImageRenderer_2_classLit = createForClass('org.glob3.mobile.generated', 'HUDImageRenderer', 364);
function HUDImageRenderer$ImageListener(hudImageRenderer){
  this._hudImageRenderer = hudImageRenderer;
}

defineClass(365, 473, {}, HUDImageRenderer$ImageListener);
_.imageCreated = function imageCreated_4(image){
  $setImage(this._hudImageRenderer, image);
}
;
var Lorg_glob3_mobile_generated_HUDImageRenderer$ImageListener_2_classLit = createForClass('org.glob3.mobile.generated', 'HUDImageRenderer/ImageListener', 365);
function $createImage(this$static, listener){
  this$static._canvasWidth > 0 && this$static._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $_createImage(this$static, listener, true);
}

function $drawImage(this$static, image, destWidth, destHeight){
  var context, imageJS;
  this$static._canvasWidth > 0 && this$static._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  context = this$static._domCanvasContext;
  imageJS = image.getImage();
  context.drawImage(imageJS, 0, 0, destWidth, destHeight);
}

function $drawImage_0(this$static, image, destWidth, destHeight, transparency){
  this$static._canvasWidth > 0 && this$static._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $_drawImage(this$static, image, 0, 0, destWidth, destHeight, transparency);
}

function $drawImage_1(this$static, image, srcLeft, srcTop, srcWidth, srcHeight, destLeft, destTop, destWidth, destHeight, transparency){
  var context, imageJS, context_0, imageJS_0;
  this$static._canvasWidth > 0 && this$static._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  if (fullContains($getWidth(image), $getHeight(image), srcLeft, srcTop, srcWidth, srcHeight)) {
    if (transparency <= 0) {
      return;
    }
    transparency >= 1?(context = this$static._domCanvasContext , imageJS = image.getImage() , context.drawImage(imageJS, srcLeft, srcTop, srcWidth, srcHeight, destLeft, destTop, destWidth, destHeight) , undefined):(context_0 = this$static._domCanvasContext , imageJS_0 = image.getImage() , context_0.globalAlpha = transparency , context_0.drawImage(imageJS_0, srcLeft, srcTop, srcWidth, srcHeight, destLeft, destTop, destWidth, destHeight) , context_0.globalAlpha = 1 , undefined);
  }
   else {
    $logError(_instance_3, 'Invalid source rectangle in drawImage', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
}

function $fillRectangle(this$static, width_0, height){
  this$static._canvasWidth > 0 && this$static._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $_fillRectangle(this$static, 0, 0, width_0, height);
}

function $fillRoundedRectangle(this$static, left, top_0, width_0, height, radius){
  this$static._canvasWidth > 0 && this$static._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  $roundRect(this$static, left, top_0, width_0, height, radius, true, false);
}

function $fillText(this$static, text_0, left, top_0){
  var context, textHeight;
  this$static._canvasWidth > 0 && this$static._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  !this$static._currentFont && $logError(_instance_3, 'Current font no set', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  context = this$static._domCanvasContext;
  textHeight = this$static._currentFontSize * 1.66;
  context.fillText(text_0, left, top_0 + textHeight);
}

function $initialize_2(this$static, width_0, height){
  var canvas;
  if (width_0 <= 0 || height <= 0) {
    $logError(_instance_3, 'Invalid extent', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  if (this$static._canvasWidth > 0 && this$static._canvasHeight > 0) {
    $logError(_instance_3, 'Canvas already initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  this$static._canvasWidth = width_0;
  this$static._canvasHeight = height;
  canvas = this$static._domCanvas;
  canvas.width = width_0;
  canvas.height = height;
  this$static.tryToSetCurrentFontToContext();
}

function $setFillColor(this$static, color_0){
  var jsColor;
  this$static._canvasWidth > 0 && this$static._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  jsColor = createDOMColor(color_0);
  this$static._domCanvasContext.fillStyle = jsColor;
}

function $setFont(this$static, font){
  var domFont;
  this$static._currentFont = new GFont_0(font);
  this$static._currentDOMFont = (domFont = '' , font._italic && (domFont += 'italic ') , font._bold && (domFont += 'bold ') , domFont += round_0(font._size * 0.6000000238418579) + 'pt' , compareTo_3(font._name, 'serif') == 0?(domFont += ' serif'):compareTo_3(font._name, 'sans-serif') == 0?(domFont += ' sans-serif'):compareTo_3(font._name, 'monospaced') == 0 && (domFont += ' monospace') , domFont);
  this$static._currentFontSize = font._size;
  $tryToSetCurrentFontToContext(this$static);
}

function $textExtent(this$static, text_0){
  var width_0, height;
  !this$static._currentFont && $logError(_instance_3, 'Current font no set', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  return width_0 = this$static._domCanvasContext.measureText(text_0).width , height = Math.round(this$static._currentFontSize * 1.66) , new Vector2F(width_0, height);
}

defineClass(373, 1, {});
_._canvasHeight = 0;
_._canvasWidth = 0;
var Lorg_glob3_mobile_generated_ICanvas_2_classLit = createForClass('org.glob3.mobile.generated', 'ICanvas', 373);
function drawStringsOn(strings, canvas, width_0, height, color_0, backgroundColor, shadowColor){
  var allFit, column, extent, extent0, fontSize, i_0, i0, labelFont, maxWidth, position, stringsSize, context, left, top_0;
  if (strings.array.length == 0) {
    return;
  }
  maxWidth = width_0 - 32;
  stringsSize = strings.array.length;
  fontSize = 20;
  allFit = false;
  while (!allFit && fontSize > 5) {
    allFit = true;
    $setFont(canvas, new GFont(fontSize));
    for (i0 = 0; i0 < stringsSize; i0++) {
      extent0 = $textExtent(canvas, (checkElementIndex(i0, strings.array.length) , dynamicCastToString(strings.array[i0])));
      if (extent0._x > maxWidth) {
        allFit = false;
        --fontSize;
        continue;
      }
    }
  }
  canvas._canvasWidth > 0 && canvas._canvasHeight > 0 || $logError(_instance_3, 'Canvas is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  context = canvas._domCanvasContext;
  context.shadowColor = createDOMColor(shadowColor);
  context.shadowBlur = 1;
  context.shadowOffsetX = 0;
  context.shadowOffsetY = 0;
  column = new ColumnCanvasElement(backgroundColor);
  labelFont = new GFont(fontSize);
  for (i_0 = 0; i_0 < stringsSize; i_0++) {
    $add_7(column, new TextCanvasElement((checkElementIndex(i_0, strings.array.length) , dynamicCastToString(strings.array[i_0])), labelFont, color_0));
  }
  extent = $getExtent(column, canvas);
  position = (left = ~~(width_0 / 2) - extent._x / 2 , top_0 = ~~(height / 2) - extent._y / 2 , new Vector2F(left, top_0));
  $drawAt(column, position._x, position._y, canvas);
}

function $getCameraCoordinateSystemForInterfaceOrientation(this$static, orientation){
  if (!this$static._camCSLL) {
    this$static._camCSPortrait = new CoordinateSystem_0(new Vector3D(1, 0, 0), new Vector3D(0, 0, -1), new Vector3D(0, 1, 0), ($clinit_Vector3D() , zero));
    this$static._camCSPortraitUD = new CoordinateSystem_0(new Vector3D(1, 0, 0), new Vector3D(0, 0, -1), new Vector3D(0, -1, 0), zero);
    this$static._camCSLL = new CoordinateSystem_0(new Vector3D(0, 1, 0), new Vector3D(0, 0, -1), new Vector3D(-1, 0, 0), zero);
    this$static._camCSLR = new CoordinateSystem_0(new Vector3D(0, 1, 0), new Vector3D(0, 0, -1), new Vector3D(1, 0, 0), zero);
  }
  switch (orientation.ordinal) {
    case 0:
      {
        return this$static._camCSPortrait;
      }

    case 1:
      {
        return this$static._camCSPortraitUD;
      }

    case 3:
      {
        return this$static._camCSLL;
      }

    default:case 2:
      {
        return this$static._camCSLR;
      }

  }
}

defineClass(317, 1, {});
var _instance;
var Lorg_glob3_mobile_generated_IDeviceAttitude_2_classLit = createForClass('org.glob3.mobile.generated', 'IDeviceAttitude', 317);
defineClass(474, 1, {});
var Lorg_glob3_mobile_generated_IDeviceInfo_2_classLit = createForClass('org.glob3.mobile.generated', 'IDeviceInfo', 474);
defineClass(467, 1, {});
var _instance_0 = null;
var Lorg_glob3_mobile_generated_IDeviceLocation_2_classLit = createForClass('org.glob3.mobile.generated', 'IDeviceLocation', 467);
defineClass(455, 1, {});
var Lorg_glob3_mobile_generated_IDownloader_2_classLit = createForClass('org.glob3.mobile.generated', 'IDownloader', 455);
function $getDeviceInfo(this$static){
  !this$static._deviceInfo && (this$static._deviceInfo = new DeviceInfo_WebGL);
  return this$static._deviceInfo;
}

defineClass(316, 1, {});
var _instance_1 = null;
var Lorg_glob3_mobile_generated_IFactory_2_classLit = createForClass('org.glob3.mobile.generated', 'IFactory', 316);
defineClass(478, 1, {});
var Lorg_glob3_mobile_generated_IFloatBuffer_2_classLit = createForClass('org.glob3.mobile.generated', 'IFloatBuffer', 478);
function $addCameraConstraint(this$static, cameraConstraint){
  $add_3((!this$static._cameraConstraints && (this$static._cameraConstraints = $createDefaultCameraConstraints()) , this$static._cameraConstraints), cameraConstraint);
}

function $addGPUProgramSources(this$static, s){
  $add_3(this$static._sources, s);
}

function $containsPlanetRenderer(renderers){
  var i_0;
  for (i_0 = 0; i_0 < renderers.array.length; i_0++) {
    if ((checkElementIndex(i_0, renderers.array.length) , dynamicCast(renderers.array[i_0], 135)).isPlanetRenderer()) {
      return true;
    }
  }
  return false;
}

function $create_1(this$static){
  var g3mWidget, i_0, initialCameraPosition, mainRenderer, shownSector, cameraRenderer;
  shownSector = ($clinit_Sector() , $clinit_Sector() , FULL_SPHERE);
  $setRenderedSector((!this$static._planetRendererBuilder && (this$static._planetRendererBuilder = new PlanetRendererBuilder) , this$static._planetRendererBuilder), shownSector);
  if ((!this$static._renderers && (this$static._renderers = new ArrayList) , this$static._renderers).array.length > 0) {
    mainRenderer = new CompositeRenderer;
    $containsPlanetRenderer((!this$static._renderers && (this$static._renderers = new ArrayList) , this$static._renderers)) || $addChildRenderer(dynamicCast(mainRenderer, 117), new ChildRenderer($create_4((!this$static._planetRendererBuilder && (this$static._planetRendererBuilder = new PlanetRendererBuilder) , this$static._planetRendererBuilder))));
    for (i_0 = 0; i_0 < (!this$static._renderers && (this$static._renderers = new ArrayList) , this$static._renderers).array.length; i_0++) {
      $addChildRenderer(dynamicCast(mainRenderer, 117), new ChildRenderer(dynamicCast($get_0((!this$static._renderers && (this$static._renderers = new ArrayList) , this$static._renderers), i_0), 135)));
    }
  }
   else {
    mainRenderer = $create_4((!this$static._planetRendererBuilder && (this$static._planetRendererBuilder = new PlanetRendererBuilder) , this$static._planetRendererBuilder));
  }
  initialCameraPosition = (!this$static._planet && (this$static._planet = new EllipsoidalPlanet(new Ellipsoid(($clinit_Vector3D() , zero), new Vector3D($intern_56, $intern_56, $intern_57)))) , $getDefaultCameraPosition(this$static._planet, shownSector));
  $addCameraConstraint(this$static, new RenderedSectorCameraConstrainer(mainRenderer.getPlanetRenderer(), initialCameraPosition._height * 1.2));
  g3mWidget = new G3MWidget((!this$static._gl && $logError(_instance_3, 'LOGIC ERROR: gl not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [])) , this$static._gl), (!this$static._downloader && (this$static._downloader = new Downloader_WebGL) , this$static._downloader), (!this$static._threadUtils && (this$static._threadUtils = new ThreadUtils_WebGL) , this$static._threadUtils), (!this$static._planet && (this$static._planet = new EllipsoidalPlanet(new Ellipsoid(($clinit_Vector3D() , zero), new Vector3D($intern_56, $intern_56, $intern_57)))) , this$static._planet), (!this$static._cameraConstraints && (this$static._cameraConstraints = $createDefaultCameraConstraints()) , this$static._cameraConstraints), (!this$static._cameraRenderer && (this$static._cameraRenderer = (cameraRenderer = new CameraRenderer , $addHandler_1(cameraRenderer, new CameraSingleDragHandler) , $addHandler_1(cameraRenderer, new CameraDoubleDragHandler) , $addHandler_1(cameraRenderer, new CameraRotationHandler) , $addHandler_1(cameraRenderer, new CameraDoubleTapHandler) , cameraRenderer)) , this$static._cameraRenderer), mainRenderer, (!this$static._busyRenderer && (this$static._busyRenderer = new BusyMeshRenderer(new Color(0, 0, 0, 1))) , this$static._busyRenderer), (!this$static._errorRenderer && (this$static._errorRenderer = new HUDErrorRenderer) , this$static._errorRenderer), (!this$static._backgroundColor && (this$static._backgroundColor = new Color(0, 0.10000000149011612, 0.20000000298023224, 1)) , this$static._backgroundColor), this$static._logFPS, this$static._logDownloaderStatistics, (!this$static._periodicalTasks && (this$static._periodicalTasks = new ArrayList) , this$static._periodicalTasks), $getGPUProgramManager(this$static), (!this$static._sceneLighting && (this$static._sceneLighting = new CameraFocusSceneLighting(new Color($intern_58, $intern_58, $intern_58, 1), new Color(1, 1, 0, 1))) , this$static._sceneLighting));
  this$static._gl = null;
  this$static._downloader = null;
  this$static._threadUtils = null;
  this$static._planet = null;
  this$static._cameraConstraints = null;
  this$static._cameraConstraints = null;
  this$static._cameraRenderer = null;
  this$static._renderers = null;
  this$static._renderers = null;
  this$static._busyRenderer = null;
  this$static._errorRenderer = null;
  this$static._periodicalTasks = null;
  this$static._periodicalTasks = null;
  return g3mWidget;
}

function $createDefaultCameraConstraints(){
  var cameraConstraints, scc;
  cameraConstraints = new ArrayList;
  scc = new SimpleCameraConstrainer;
  setCheck(cameraConstraints.array, cameraConstraints.array.length, scc);
  return cameraConstraints;
}

function $getGPUProgramManager(this$static){
  var gpuProgramFactory, gpuProgramManager, i_0;
  gpuProgramFactory = new GPUProgramFactory;
  for (i_0 = 0; i_0 < this$static._sources.array.length; i_0++) {
    $add_6(gpuProgramFactory, dynamicCast($get_0(this$static._sources, i_0), 21));
  }
  gpuProgramManager = new GPUProgramManager(gpuProgramFactory);
  return gpuProgramManager;
}

function $setCameraRenderer(this$static, cameraRenderer){
  if (this$static._cameraRenderer) {
    $logError(_instance_3, 'LOGIC ERROR: cameraRenderer already initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  this$static._cameraRenderer = cameraRenderer;
}

function $setGL(this$static, gl){
  if (this$static._gl) {
    $logError(_instance_3, 'LOGIC ERROR: gl already initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  if (!gl) {
    $logError(_instance_3, 'LOGIC ERROR: gl cannot be NULL', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  this$static._gl = gl;
}

defineClass(252, 1, {});
_._logDownloaderStatistics = false;
_._logFPS = false;
var Lorg_glob3_mobile_generated_IG3MBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'IG3MBuilder', 252);
var Lorg_glob3_mobile_generated_IGLTextureId_2_classLit = createForInterface('org.glob3.mobile.generated', 'IGLTextureId');
defineClass(479, 1, {});
_.toString$ = function toString_33(){
  return 'Image_WebGL ' + $getWidth(this) + ' x ' + $getHeight(this) + ', _image=(' + this._imgObject + ')';
}
;
var Lorg_glob3_mobile_generated_IImage_2_classLit = createForClass('org.glob3.mobile.generated', 'IImage', 479);
defineClass(487, 1, {});
var Lorg_glob3_mobile_generated_IImageDownloadListener_2_classLit = createForClass('org.glob3.mobile.generated', 'IImageDownloadListener', 487);
defineClass(465, 1, {});
var _instance_2 = null;
var Lorg_glob3_mobile_generated_IJSONParser_2_classLit = createForClass('org.glob3.mobile.generated', 'IJSONParser', 465);
defineClass(285, 1, {});
_._level = 0;
var _instance_3 = null;
var Lorg_glob3_mobile_generated_ILogger_2_classLit = createForClass('org.glob3.mobile.generated', 'ILogger', 285);
function $isEquals_2(x_0, y_0){
  if (x_0 == y_0) {
    return true;
  }
  return (x_0 - y_0 <= 0?0 - (x_0 - y_0):x_0 - y_0) <= 1.0E-8 * (((x_0 <= 0?0 - x_0:x_0) > (y_0 <= 0?0 - y_0:y_0)?x_0 <= 0?0 - x_0:x_0:y_0 <= 0?0 - y_0:y_0) > 1?(x_0 <= 0?0 - x_0:x_0) > (y_0 <= 0?0 - y_0:y_0)?x_0 <= 0?0 - x_0:x_0:y_0 <= 0?0 - y_0:y_0:1);
}

function $linearInterpolation(from, to, alpha_0){
  return from + (to - from) * alpha_0;
}

function $quadraticBezierInterpolation(from, middle, to, alpha_0){
  var oneMinusAlpha;
  oneMinusAlpha = 1 - alpha_0;
  return oneMinusAlpha * oneMinusAlpha * from + 2 * oneMinusAlpha * alpha_0 * middle + alpha_0 * alpha_0 * to;
}

defineClass(464, 1, {});
var _instance_4 = null;
var Lorg_glob3_mobile_generated_IMathUtils_2_classLit = createForClass('org.glob3.mobile.generated', 'IMathUtils', 464);
defineClass(448, 1, {});
var Lorg_glob3_mobile_generated_INativeGL_2_classLit = createForClass('org.glob3.mobile.generated', 'INativeGL', 448);
defineClass(483, 1, {});
var Lorg_glob3_mobile_generated_IShortBuffer_2_classLit = createForClass('org.glob3.mobile.generated', 'IShortBuffer', 483);
defineClass(463, 1, {});
var _instance_5 = null;
var Lorg_glob3_mobile_generated_IStringBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'IStringBuilder', 463);
defineClass(462, 1, {});
var _instance_6;
var Lorg_glob3_mobile_generated_IStringUtils_2_classLit = createForClass('org.glob3.mobile.generated', 'IStringUtils', 462);
defineClass(466, 1, {});
var _instance_7 = null;
var Lorg_glob3_mobile_generated_ITextUtils_2_classLit = createForClass('org.glob3.mobile.generated', 'ITextUtils', 466);
defineClass(456, 1, {});
var Lorg_glob3_mobile_generated_IThreadUtils_2_classLit = createForClass('org.glob3.mobile.generated', 'IThreadUtils', 456);
defineClass(472, 1, {});
var Lorg_glob3_mobile_generated_ITimer_2_classLit = createForClass('org.glob3.mobile.generated', 'ITimer', 472);
function $dispose_5(this$static){
  this$static._ownsIndices && !!this$static._indices && $dispose_16(this$static._indices);
  this$static._ownsVertices && !!this$static._vertices && $dispose_15(this$static._vertices);
  !!this$static._translationMatrix && $dispose_9(this$static._translationMatrix);
  $_release(this$static._glState);
}

function $rawRender(this$static, rc){
  var gl;
  gl = rc._gl;
  $drawElements(gl, this$static._primitive, this$static._indices, this$static._glState, rc._gpuProgramManager);
}

function IndexedGeometryMesh(primitive, center, vertices, indices){
  Mesh.call(this);
  this._primitive = primitive;
  this._vertices = vertices;
  this._ownsVertices = true;
  this._extent = null;
  this._center = new Vector3D_0(center);
  this._translationMatrix = center._x != center._x || center._y != center._y || center._z != center._z || center._x == 0 && center._y == 0 && center._z == 0?null:new MutableMatrix44D_1(new MutableMatrix44D_0(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, center._x, center._y, center._z, 1));
  this._lineWidth = 1;
  this._pointSize = 1;
  this._glState = new GLState;
  $addGLFeature(this._glState, new GeometryGLFeature(this._vertices, true, 0, 0, this._lineWidth, this._pointSize), false);
  !!this._translationMatrix && $addGLFeature(this._glState, new ModelTransformGLFeature($asMatrix44D(this._translationMatrix)), false);
  this._indices = indices;
  this._ownsIndices = true;
}

defineClass(203, 390, {}, IndexedGeometryMesh);
_._ownsIndices = false;
var Lorg_glob3_mobile_generated_IndexedGeometryMesh_2_classLit = createForClass('org.glob3.mobile.generated', 'IndexedGeometryMesh', 203);
function $dispose_6(this$static){
  this$static._owner && !!this$static._indices && $dispose_16(this$static._indices);
  $dispose(this$static);
}

function $rawRender_0(this$static, rc){
  var gl;
  gl = rc._gl;
  $drawElements(gl, this$static._primitive, this$static._indices, this$static._glState, rc._gpuProgramManager);
}

function IndexedMesh(primitive, center, vertices, indices, colors){
  IndexedMesh_1.call(this, primitive, center, vertices, indices, null, colors, true);
}

function IndexedMesh_0(primitive, center, vertices, indices, flatColor){
  IndexedMesh_1.call(this, primitive, center, vertices, indices, flatColor, null, false);
}

function IndexedMesh_1(primitive, center, vertices, indices, flatColor, colors, depthTest){
  AbstractMesh.call(this, primitive, center, vertices, flatColor, colors, depthTest);
  this._indices = indices;
}

defineClass(123, 197, {}, IndexedMesh, IndexedMesh_0);
_.rawRender_0 = function rawRender_3(rc){
  $rawRender_0(this, rc);
}
;
var Lorg_glob3_mobile_generated_IndexedMesh_2_classLit = createForClass('org.glob3.mobile.generated', 'IndexedMesh', 123);
function $clinit_InterfaceOrientation(){
  $clinit_InterfaceOrientation = emptyMethod;
  PORTRAIT = new InterfaceOrientation('PORTRAIT', 0);
  PORTRAIT_UPSIDEDOWN = new InterfaceOrientation('PORTRAIT_UPSIDEDOWN', 1);
  LANDSCAPE_RIGHT = new InterfaceOrientation('LANDSCAPE_RIGHT', 2);
  LANDSCAPE_LEFT = new InterfaceOrientation('LANDSCAPE_LEFT', 3);
}

function InterfaceOrientation(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_7(){
  $clinit_InterfaceOrientation();
  return initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_InterfaceOrientation_2_classLit, 1), $intern_4, 85, 0, [PORTRAIT, PORTRAIT_UPSIDEDOWN, LANDSCAPE_RIGHT, LANDSCAPE_LEFT]);
}

defineClass(85, 15, {3:1, 26:1, 15:1, 85:1}, InterfaceOrientation);
var LANDSCAPE_LEFT, LANDSCAPE_RIGHT, PORTRAIT, PORTRAIT_UPSIDEDOWN;
var Lorg_glob3_mobile_generated_InterfaceOrientation_2_classLit = createForEnum('org.glob3.mobile.generated', 'InterfaceOrientation', 85, values_7);
function $isAvailable(this$static){
  if (!this$static._enable) {
    return false;
  }
  return true;
}

function $onLayerTouchEventListener(this$static){
  var i_0, listenersSize;
  listenersSize = this$static._listeners.array.length;
  for (i_0 = 0; i_0 < listenersSize; i_0++) {
    throwClassCastExceptionUnlessNull($get_0(this$static._listeners, i_0));
  }
  return false;
}

defineClass(34, 1, $intern_59);
_.toString$ = function toString_34(){
  return this.description_0();
}
;
_._enable = false;
_._transparency = 0;
var Lorg_glob3_mobile_generated_Layer_2_classLit = createForClass('org.glob3.mobile.generated', 'Layer', 34);
function $addLayer(this$static, layer){
  !!layer._layerSet && $logError(_instance_3, 'LayerSet already set.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  layer._layerSet = this$static;
  $add_3(this$static._layers, layer);
  $layersChanged(this$static);
  !!this$static._changedInfoListener && $changedInfo(this$static._changedInfoListener, $getInfo_0(this$static));
}

function $checkAndComposeLayerTilesRenderParameters(enableLayers, errors){
  var i_0, i0, layer, layerParametersVector, layerParametersVectorSize, multiProjectionLayers, mutableLayerTilesRenderParameters;
  mutableLayerTilesRenderParameters = new MutableLayerTilesRenderParameters;
  multiProjectionLayers = new ArrayList;
  for (i0 = 0; i0 < enableLayers.array.length; i0++) {
    layer = (checkElementIndex(i0, enableLayers.array.length) , dynamicCast(enableLayers.array[i0], 34));
    layerParametersVector = layer.getLayerTilesRenderParametersVector();
    layerParametersVectorSize = layerParametersVector.array.length;
    if (layerParametersVectorSize == 0) {
      continue;
    }
     else if (layerParametersVectorSize == 1) {
      if (!$update_0(mutableLayerTilesRenderParameters, (checkElementIndex(0, layerParametersVector.array.length) , dynamicCast(layerParametersVector.array[0], 76)), errors)) {
        return null;
      }
    }
     else {
      setCheck(multiProjectionLayers.array, multiProjectionLayers.array.length, layer);
    }
  }
  for (i_0 = 0; i_0 < multiProjectionLayers.array.length; i_0++) {
    layer = (checkElementIndex(i_0, multiProjectionLayers.array.length) , dynamicCast(multiProjectionLayers.array[i_0], 34));
    if (!$update(mutableLayerTilesRenderParameters, layer, errors)) {
      return null;
    }
  }
  return $create_3(mutableLayerTilesRenderParameters, errors);
}

function $checkLayersDataSector(this$static, forceFirstLevelTilesRenderOnStart, errors){
  var biggestArea, biggestDataSector, dataSectorsInconsistency, i_0, i0, layer, layerArea, layersCount;
  if (forceFirstLevelTilesRenderOnStart) {
    biggestDataSector = null;
    layersCount = this$static._layers.array.length;
    biggestArea = 0;
    for (i0 = 0; i0 < layersCount; i0++) {
      layer = dynamicCast($get_0(this$static._layers, i0), 34);
      if (layer._enable) {
        layerArea = $getAngularAreaInSquaredDegrees(layer.getDataSector());
        if (layerArea > biggestArea) {
          biggestDataSector = new Sector_0(layer.getDataSector());
          biggestArea = layerArea;
        }
      }
    }
    if (biggestDataSector) {
      dataSectorsInconsistency = false;
      for (i_0 = 0; i_0 < layersCount; i_0++) {
        layer = dynamicCast($get_0(this$static._layers, i_0), 34);
        if (layer._enable) {
          if (!$fullContains(biggestDataSector, layer.getDataSector())) {
            dataSectorsInconsistency = true;
            break;
          }
        }
      }
      if (dataSectorsInconsistency) {
        setCheck(errors.array, errors.array.length, 'Inconsistency in layers data sectors');
        return false;
      }
    }
  }
  return true;
}

function $checkLayersRenderState(this$static, errors, enableLayers){
  var i_0, layer, layerErrors, layerRenderState, layerRenderStateType, layerSetNotReadyFlag;
  layerSetNotReadyFlag = false;
  for (i_0 = 0; i_0 < this$static._layers.array.length; i_0++) {
    layer = dynamicCast($get_0(this$static._layers, i_0), 34);
    if (layer._enable) {
      setCheck(enableLayers.array, enableLayers.array.length, layer);
      layerRenderState = layer.getRenderState_0();
      layerRenderStateType = layerRenderState._type;
      if (layerRenderStateType != 0) {
        if (layerRenderStateType == 2) {
          layerErrors = layerRenderState._errors;
          $addAll(errors, layerErrors);
        }
        layerSetNotReadyFlag = true;
      }
    }
  }
  return !layerSetNotReadyFlag;
}

function $createLayerTilesRenderParameters(this$static, forceFirstLevelTilesRenderOnStart, errors){
  var enableLayers;
  if (!$checkLayersDataSector(this$static, forceFirstLevelTilesRenderOnStart, errors)) {
    return null;
  }
  enableLayers = new ArrayList;
  if (!$checkLayersRenderState(this$static, errors, enableLayers)) {
    return null;
  }
  return $checkAndComposeLayerTilesRenderParameters(enableLayers, errors);
}

function $createTileImageProvider(this$static, rc, layerTilesRenderParameters){
  var compositeTileImageProvider, i_0, layer, layerTileImageProvider, layersSize, singleTileImageProvider;
  singleTileImageProvider = null;
  compositeTileImageProvider = null;
  layersSize = this$static._layers.array.length;
  for (i_0 = 0; i_0 < layersSize; i_0++) {
    layer = dynamicCast($get_0(this$static._layers, i_0), 34);
    if (layer._enable) {
      layerTileImageProvider = layer.createTileImageProvider(rc, layerTilesRenderParameters);
      if (layerTileImageProvider) {
        if (compositeTileImageProvider) {
          $add_3(compositeTileImageProvider._children, layerTileImageProvider);
          compositeTileImageProvider._childrenSize = compositeTileImageProvider._children.array.length;
        }
         else if (!singleTileImageProvider) {
          singleTileImageProvider = layerTileImageProvider;
        }
         else {
          compositeTileImageProvider = new CompositeTileImageProvider;
          $add_3(compositeTileImageProvider._children, singleTileImageProvider);
          compositeTileImageProvider._childrenSize = compositeTileImageProvider._children.array.length;
          $add_3(compositeTileImageProvider._children, layerTileImageProvider);
          compositeTileImageProvider._childrenSize = compositeTileImageProvider._children.array.length;
        }
      }
    }
  }
  return !compositeTileImageProvider?singleTileImageProvider:compositeTileImageProvider;
}

function $getInfo_0(this$static){
  var i_0, infoSize, j, layer, layerInfo, layersCount;
  this$static._infos.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
  layersCount = this$static._layers.array.length;
  for (i_0 = 0; i_0 < layersCount; i_0++) {
    layer = dynamicCast($get_0(this$static._layers, i_0), 34);
    if (layer._enable) {
      layerInfo = layer._layerInfo;
      infoSize = layerInfo.array.length;
      for (j = 0; j < infoSize; j++) {
        $add_3(this$static._infos, (checkElementIndex(j, layerInfo.array.length) , throwClassCastExceptionUnlessNull(layerInfo.array[j])));
      }
    }
  }
  return this$static._infos;
}

function $getRenderState_0(this$static){
  var busyFlag, child, childErrors, childRenderState, childRenderStateType, errorFlag, i_0, layersCount;
  this$static._errors.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
  busyFlag = false;
  errorFlag = false;
  layersCount = this$static._layers.array.length;
  for (i_0 = 0; i_0 < layersCount; i_0++) {
    child = dynamicCast($get_0(this$static._layers, i_0), 34);
    if (child._enable) {
      childRenderState = child.getRenderState_0();
      childRenderStateType = childRenderState._type;
      if (childRenderStateType == 2) {
        errorFlag = true;
        childErrors = childRenderState._errors;
        $addAll(this$static._errors, childErrors);
      }
       else 
        childRenderStateType == 1 && (busyFlag = true);
    }
  }
  if (errorFlag) {
    return $clinit_RenderState() , new RenderState(this$static._errors);
  }
   else if (busyFlag) {
    return $clinit_RenderState() , $clinit_RenderState() , BUSY;
  }
  return $clinit_RenderState() , $clinit_RenderState() , READY;
}

function $initialize_3(this$static){
  var i_0, layersCount;
  layersCount = this$static._layers.array.length;
  for (i_0 = 0; i_0 < layersCount; i_0++) {
    dynamicCast($get_0(this$static._layers, i_0), 34);
  }
}

function $layersChanged(this$static){
  if (this$static._tileImageProvider) {
    $_release(this$static._tileImageProvider);
    this$static._tileImageProvider = null;
  }
  !!this$static._listener && $changed(this$static._listener);
}

function $onTerrainTouchEvent_0(this$static, position, tile){
  var i_0, layer;
  for (i_0 = this$static._layers.array.length - 1; i_0 >= 0; i_0--) {
    layer = dynamicCast($get_0(this$static._layers, i_0), 34);
    if ($isAvailable(layer)) {
      new LayerTouchEvent(position, tile._sector);
      if ($onLayerTouchEventListener(layer)) {
        return true;
      }
    }
  }
  return false;
}

function $setChangeListener(this$static, listener){
  !!this$static._listener && $logError(_instance_3, 'Listener already set', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  this$static._listener = listener;
}

function $setChangedInfoListener(this$static, changedInfoListener){
  if (this$static._changedInfoListener) {
    $logError(_instance_3, 'Changed Info Listener of LayerSet already set', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  this$static._changedInfoListener = changedInfoListener;
  !!this$static._changedInfoListener && $changedInfo(this$static._changedInfoListener, $getInfo_0(this$static));
}

function LayerSet(){
  this._layers = new ArrayList;
  this._errors = new ArrayList;
  this._infos = new ArrayList;
  this._listener = null;
  this._tileImageProvider = null;
  this._changedInfoListener = null;
}

defineClass(170, 1, {}, LayerSet);
var Lorg_glob3_mobile_generated_LayerSet_2_classLit = createForClass('org.glob3.mobile.generated', 'LayerSet', 170);
function LayerTilesRenderParameters(topSector, topSectorSplitsByLatitude, topSectorSplitsByLongitude, firstLevel, maxLevel, tileTextureResolution, tileMeshResolution, mercator){
  this._topSector = new Sector_0(topSector);
  this._topSectorSplitsByLatitude = topSectorSplitsByLatitude;
  this._topSectorSplitsByLongitude = topSectorSplitsByLongitude;
  this._firstLevel = firstLevel;
  this._maxLevel = maxLevel;
  this._maxLevelForPoles = 4;
  this._tileTextureResolution = tileTextureResolution;
  this._tileMeshResolution = tileMeshResolution;
  this._mercator = mercator;
}

defineClass(76, 1, {76:1}, LayerTilesRenderParameters);
_._firstLevel = 0;
_._maxLevel = 0;
_._maxLevelForPoles = 0;
_._mercator = false;
_._topSectorSplitsByLatitude = 0;
_._topSectorSplitsByLongitude = 0;
var Lorg_glob3_mobile_generated_LayerTilesRenderParameters_2_classLit = createForClass('org.glob3.mobile.generated', 'LayerTilesRenderParameters', 76);
function LayerTouchEvent(position, sector){
  new Geodetic3D_1(position);
  new Sector_0(sector);
}

defineClass(406, 1, {}, LayerTouchEvent);
var Lorg_glob3_mobile_generated_LayerTouchEvent_2_classLit = createForClass('org.glob3.mobile.generated', 'LayerTouchEvent', 406);
defineClass(484, 1, {});
var Lorg_glob3_mobile_generated_TextureMapping_2_classLit = createForClass('org.glob3.mobile.generated', 'TextureMapping', 484);
function $modifyGLState_0(this$static, state){
  var scale, translation;
  if (!this$static._initialized) {
    $initialize_0(this$static._initializer);
    scale = $getScale(this$static._initializer);
    this$static._scaleU = scale._x;
    this$static._scaleV = scale._y;
    translation = $getTranslation(this$static._initializer);
    this$static._translationU = translation._x;
    this$static._translationV = translation._y;
    this$static._texCoords = $createTextCoords(this$static._initializer._tile);
    this$static._initializer = null;
    this$static._initialized = true;
  }
  if (this$static._texCoords) {
    $clearGLFeatureGroup(state, ($clinit_GLFeatureGroupName() , COLOR_GROUP));
    this$static._scaleU != 1 || this$static._scaleV != 1 || this$static._translationU != 0 || this$static._translationV != 0?$addGLFeature(state, new TextureGLFeature_0(this$static._glTextureId._id, this$static._texCoords, this$static._transparent, this$static._glTextureId._isPremultiplied?_one:_srcAlpha, _oneMinusSrcAlpha, this$static._translationU, this$static._translationV, this$static._scaleU, this$static._scaleV, 0, 0, 0), false):$addGLFeature(state, new TextureGLFeature(this$static._glTextureId._id, this$static._texCoords, this$static._transparent, this$static._glTextureId._isPremultiplied?_one:_srcAlpha, _oneMinusSrcAlpha), false);
  }
   else {
    $logError(_instance_3, 'LazyTextureMapping::bind() with _texCoords == NULL', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
}

function $releaseGLTextureId(this$static){
  if (this$static._glTextureId) {
    $dispose_12(this$static._glTextureId);
    this$static._glTextureId = null;
  }
}

function $setGLTextureId(this$static, glTextureId){
  $releaseGLTextureId(this$static);
  this$static._glTextureId = glTextureId;
}

function LazyTextureMapping(initializer, ownedTexCoords, transparent){
  this._initializer = initializer;
  this._glTextureId = null;
  this._initialized = false;
  this._texCoords = null;
  this._translationU = 0;
  this._translationV = 0;
  this._scaleU = 1;
  this._scaleV = 1;
  this._ownedTexCoords = ownedTexCoords;
  this._transparent = transparent;
}

defineClass(61, 484, {61:1}, LazyTextureMapping);
_._initialized = false;
_._ownedTexCoords = false;
_._scaleU = 0;
_._scaleV = 0;
_._translationU = 0;
_._translationV = 0;
_._transparent = false;
var Lorg_glob3_mobile_generated_LazyTextureMapping_2_classLit = createForClass('org.glob3.mobile.generated', 'LazyTextureMapping', 61);
function $dispose_7(this$static){
  var i_0, mapping;
  this$static._ownedMesh && !!this$static._mesh && $dispose_5(this$static._mesh);
  if (this$static._mappings) {
    for (i_0 = 0; i_0 < this$static._mappings.array.length; i_0++) {
      mapping = dynamicCast($get_0(this$static._mappings, i_0), 61);
      !!mapping && (mapping._initializer = null , mapping._ownedTexCoords && !!mapping._texCoords && $dispose_15(mapping._texCoords) , mapping._texCoords = null , $releaseGLTextureId(mapping) , undefined);
    }
    this$static._mappings = null;
  }
  $_release(this$static._glState);
}

function $getCurrentTextureMapping(this$static){
  var i_0, i0, levelsCount, mapping, newCurrentLevel;
  if (!this$static._mappings) {
    return null;
  }
  if (this$static._currentLevel < 0) {
    newCurrentLevel = -1;
    levelsCount = this$static._mappings.array.length;
    for (i0 = 0; i0 < levelsCount; i0++) {
      mapping = dynamicCast($get_0(this$static._mappings, i0), 61);
      if (mapping) {
        if (mapping._glTextureId) {
          newCurrentLevel = i0;
          break;
        }
      }
    }
    if (newCurrentLevel >= 0) {
      this$static._currentLevel = newCurrentLevel;
      $modifyGLState_0(dynamicCast($get_0(this$static._mappings, this$static._currentLevel), 61), this$static._glState);
      if (this$static._currentLevel < levelsCount - 1) {
        for (i_0 = levelsCount - 1; i_0 > this$static._currentLevel; i_0--) {
          mapping = dynamicCast($get_0(this$static._mappings, i_0), 61);
          !!mapping && (mapping._initializer = null , mapping._ownedTexCoords && !!mapping._texCoords && $dispose_15(mapping._texCoords) , mapping._texCoords = null , $releaseGLTextureId(mapping) , undefined);
          this$static._mappings.remove_1(i_0);
        }
      }
    }
  }
  return this$static._currentLevel >= 0?dynamicCast($get_0(this$static._mappings, this$static._currentLevel), 61):null;
}

function $getTopLevelTextureId(this$static){
  var mapping;
  mapping = $getCurrentTextureMapping(this$static);
  if (mapping) {
    if (this$static._currentLevel == 0) {
      return mapping._glTextureId;
    }
  }
  return null;
}

function $setGLTextureIdForLevel(this$static, level, glTextureId){
  if (this$static._mappings.array.length > 0) {
    if (this$static._currentLevel < 0 || level < this$static._currentLevel) {
      $setGLTextureId(dynamicCast($get_0(this$static._mappings, level), 61), glTextureId);
      this$static._currentLevel = -1;
      return true;
    }
  }
  return false;
}

function LeveledTexturedMesh(mesh, mappings){
  Mesh.call(this);
  this._mesh = mesh;
  this._ownedMesh = false;
  this._mappings = mappings;
  this._currentLevel = -1;
  this._glState = new GLState;
  if (!this._mappings) {
    throw new RuntimeException_0("LeveledTexturedMesh: mappings can't be NULL!");
  }
  if (this._mappings.array.length <= 0) {
    throw new RuntimeException_0('LeveledTexturedMesh: empty mappings');
  }
}

defineClass(383, 102, {}, LeveledTexturedMesh);
_.rawRender = function rawRender_4(rc, parentGLState){
  var mapping;
  mapping = $getCurrentTextureMapping(this);
  if (!mapping) {
    $logError(_instance_3, 'LeveledTexturedMesh: No Texture Mapping', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    $render(this._mesh, rc, parentGLState);
  }
   else {
    $setParent_2(this._glState, parentGLState);
    $render(this._mesh, rc, this._glState);
  }
}
;
_._currentLevel = 0;
_._ownedMesh = false;
var Lorg_glob3_mobile_generated_LeveledTexturedMesh_2_classLit = createForClass('org.glob3.mobile.generated', 'LeveledTexturedMesh', 383);
function $getParentTileOfSuitableLevel(this$static, tile){
  var maxLevel, result;
  maxLevel = this$static._parameters._maxLevel;
  result = tile;
  while (!!result && result._level > maxLevel) {
    result = result._parent;
  }
  return result;
}

function $requestImage(this$static, tile, downloader, tileDownloadPriority, logDownloadActivity, listener){
  var suitableTile, url_0;
  suitableTile = $getParentTileOfSuitableLevel(this$static, tile);
  url_0 = this$static.createURL(suitableTile);
  logDownloadActivity && $logInfo(_instance_3, 'Downloading %s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [url_0._path]));
  return $requestImage_0(downloader, url_0, tileDownloadPriority, listener);
}

function RasterLayer(parameters, layerInfo){
  this._listeners = new ArrayList;
  new ArrayList;
  this._transparency = 1;
  this._layerInfo = layerInfo;
  this._layerSet = null;
  this._enable = true;
  this._parameters = parameters;
  this._tileImageProvider = null;
}

defineClass(253, 34, $intern_59);
_.createTileImageProvider = function createTileImageProvider(rc, layerTilesRenderParameters){
  !this._tileImageProvider && (this._tileImageProvider = new RasterLayerTileImageProvider(this, rc._downloader));
  ++this._tileImageProvider._referenceCounter;
  return this._tileImageProvider;
}
;
_.getLayerTilesRenderParametersVector = function getLayerTilesRenderParametersVector(){
  var parametersVector;
  parametersVector = new ArrayList;
  !!this._parameters && $add_3(parametersVector, this._parameters);
  return parametersVector;
}
;
_.selectLayerTilesRenderParameters = function selectLayerTilesRenderParameters(index_0){
  throw new RuntimeException_0('Logic error');
}
;
var Lorg_glob3_mobile_generated_RasterLayer_2_classLit = createForClass('org.glob3.mobile.generated', 'RasterLayer', 253);
defineClass(254, 253, $intern_59);
_.createURL = function createURL(tile){
  var column, isb, level, numRows, path, row, subdomainsIndex, subdomainsSize;
  level = tile._level;
  column = tile._column;
  numRows = round_int(pow_0(2, level));
  row = numRows - tile._row - 1;
  isb = new StringBuilder_WebGL;
  $addString(isb, this._protocol);
  subdomainsSize = this._subdomains.array.length;
  if (subdomainsSize > 0) {
    subdomainsIndex = (level + column + row < 0?-(level + column + row):level + column + row) % subdomainsSize;
    $addString(isb, dynamicCastToString($get_0(this._subdomains, subdomainsIndex)));
  }
  $addString(isb, this._domain);
  isb._string += '/';
  isb._string += level;
  isb._string += '/';
  isb._string += column;
  isb._string += '/';
  isb._string += row;
  isb._string += '.';
  $addString(isb, this._imageFormat);
  path = isb._string;
  return new URL_1(path);
}
;
_.description_0 = function description_6(){
  return '[MercatorTiledLayer]';
}
;
_.getDataSector = function getDataSector(){
  return $clinit_Sector() , $clinit_Sector() , FULL_SPHERE;
}
;
_.getRenderState_0 = function getRenderState_1(){
  return $clinit_RenderState() , $clinit_RenderState() , READY;
}
;
_.rawContribution = function rawContribution(tile){
  var requestedImageSector, tileP;
  tileP = $getParentTileOfSuitableLevel(this, tile);
  if (!tileP) {
    return null;
  }
  if (tile == tileP) {
    return this._transparency < 1?fullCoverageTransparent(this._transparency):($clinit_TileImageContribution() , ++FULL_COVERAGE_OPAQUE._referenceCounter , $clinit_TileImageContribution() , FULL_COVERAGE_OPAQUE);
  }
  requestedImageSector = tileP._sector;
  return this._transparency < 1?partialCoverageTransparent(requestedImageSector, this._transparency):($clinit_TileImageContribution() , new TileImageContribution(requestedImageSector, false, 1));
}
;
var Lorg_glob3_mobile_generated_MercatorTiledLayer_2_classLit = createForClass('org.glob3.mobile.generated', 'MercatorTiledLayer', 254);
function MapQuestLayer(subdomains){
  MapQuestLayer_0.call(this, subdomains, new ArrayList);
}

function MapQuestLayer_0(subdomains, layerInfo){
  RasterLayer.call(this, new LayerTilesRenderParameters(($clinit_Sector() , $clinit_Sector() , FULL_SPHERE), 1, 1, 2, 19, new Vector2I(256, 256), new Vector2I(16, 16), true), layerInfo);
  this._protocol = 'http://';
  this._domain = 'mqcdn.com/tiles/1.0.0/map';
  this._subdomains = subdomains;
  this._imageFormat = 'jpg';
}

function getSubdomains(){
  var result;
  result = new ArrayList;
  setCheck(result.array, result.array.length, 'otile1.');
  setCheck(result.array, result.array.length, 'otile2.');
  setCheck(result.array, result.array.length, 'otile3.');
  setCheck(result.array, result.array.length, 'otile4.');
  return result;
}

function newOSM(){
  return new MapQuestLayer(getSubdomains());
}

function newOSM_0(){
  return new MapQuestLayer(getSubdomains());
}

defineClass(140, 254, $intern_59, MapQuestLayer);
_.description_0 = function description_7(){
  return '[MapQuestLayer]';
}
;
_.getRenderState_0 = function getRenderState_2(){
  return $clinit_RenderState() , $clinit_RenderState() , READY;
}
;
var Lorg_glob3_mobile_generated_MapQuestLayer_2_classLit = createForClass('org.glob3.mobile.generated', 'MapQuestLayer', 140);
function $createMultiplication(this$static, that){
  var m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33, that00, that01, that02, that03, that10, that11, that12, that13, that20, that21, that22, that23, that30, that31, that32, that33;
  that00 = that._m00;
  that10 = that._m10;
  that20 = that._m20;
  that30 = that._m30;
  that01 = that._m01;
  that11 = that._m11;
  that21 = that._m21;
  that31 = that._m31;
  that02 = that._m02;
  that12 = that._m12;
  that22 = that._m22;
  that32 = that._m32;
  that03 = that._m03;
  that13 = that._m13;
  that23 = that._m23;
  that33 = that._m33;
  m00 = this$static._m00 * that00 + this$static._m01 * that10 + this$static._m02 * that20 + this$static._m03 * that30;
  m01 = this$static._m00 * that01 + this$static._m01 * that11 + this$static._m02 * that21 + this$static._m03 * that31;
  m02 = this$static._m00 * that02 + this$static._m01 * that12 + this$static._m02 * that22 + this$static._m03 * that32;
  m03 = this$static._m00 * that03 + this$static._m01 * that13 + this$static._m02 * that23 + this$static._m03 * that33;
  m10 = this$static._m10 * that00 + this$static._m11 * that10 + this$static._m12 * that20 + this$static._m13 * that30;
  m11 = this$static._m10 * that01 + this$static._m11 * that11 + this$static._m12 * that21 + this$static._m13 * that31;
  m12 = this$static._m10 * that02 + this$static._m11 * that12 + this$static._m12 * that22 + this$static._m13 * that32;
  m13 = this$static._m10 * that03 + this$static._m11 * that13 + this$static._m12 * that23 + this$static._m13 * that33;
  m20 = this$static._m20 * that00 + this$static._m21 * that10 + this$static._m22 * that20 + this$static._m23 * that30;
  m21 = this$static._m20 * that01 + this$static._m21 * that11 + this$static._m22 * that21 + this$static._m23 * that31;
  m22 = this$static._m20 * that02 + this$static._m21 * that12 + this$static._m22 * that22 + this$static._m23 * that32;
  m23 = this$static._m20 * that03 + this$static._m21 * that13 + this$static._m22 * that23 + this$static._m23 * that33;
  m30 = this$static._m30 * that00 + this$static._m31 * that10 + this$static._m32 * that20 + this$static._m33 * that30;
  m31 = this$static._m30 * that01 + this$static._m31 * that11 + this$static._m32 * that21 + this$static._m33 * that31;
  m32 = this$static._m30 * that02 + this$static._m31 * that12 + this$static._m32 * that22 + this$static._m33 * that32;
  m33 = this$static._m30 * that03 + this$static._m31 * that13 + this$static._m32 * that23 + this$static._m33 * that33;
  return new Matrix44D(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
}

function $getColumnMajorFloatArray(this$static){
  if (this$static._columnMajorFloatArray == null) {
    this$static._columnMajorFloatArray = initDim(F_classLit, $intern_29, 0, 16, 7, 1);
    this$static._columnMajorFloatArray[0] = this$static._m00;
    this$static._columnMajorFloatArray[1] = this$static._m10;
    this$static._columnMajorFloatArray[2] = this$static._m20;
    this$static._columnMajorFloatArray[3] = this$static._m30;
    this$static._columnMajorFloatArray[4] = this$static._m01;
    this$static._columnMajorFloatArray[5] = this$static._m11;
    this$static._columnMajorFloatArray[6] = this$static._m21;
    this$static._columnMajorFloatArray[7] = this$static._m31;
    this$static._columnMajorFloatArray[8] = this$static._m02;
    this$static._columnMajorFloatArray[9] = this$static._m12;
    this$static._columnMajorFloatArray[10] = this$static._m22;
    this$static._columnMajorFloatArray[11] = this$static._m32;
    this$static._columnMajorFloatArray[12] = this$static._m03;
    this$static._columnMajorFloatArray[13] = this$static._m13;
    this$static._columnMajorFloatArray[14] = this$static._m23;
    this$static._columnMajorFloatArray[15] = this$static._m33;
  }
  return this$static._columnMajorFloatArray;
}

function Matrix44D(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33){
  RCObject.call(this);
  this._m00 = m00;
  this._m01 = m01;
  this._m02 = m02;
  this._m03 = m03;
  this._m10 = m10;
  this._m11 = m11;
  this._m12 = m12;
  this._m13 = m13;
  this._m20 = m20;
  this._m21 = m21;
  this._m22 = m22;
  this._m23 = m23;
  this._m30 = m30;
  this._m31 = m31;
  this._m32 = m32;
  this._m33 = m33;
  this._columnMajorFloatArray = null;
  this._columnMajorFloatBuffer = null;
}

function Matrix44D_0(m){
  RCObject.call(this);
  this._m00 = m._m00;
  this._m01 = m._m01;
  this._m02 = m._m02;
  this._m03 = m._m03;
  this._m10 = m._m10;
  this._m11 = m._m11;
  this._m12 = m._m12;
  this._m13 = m._m13;
  this._m20 = m._m20;
  this._m21 = m._m21;
  this._m22 = m._m22;
  this._m23 = m._m23;
  this._m30 = m._m30;
  this._m31 = m._m31;
  this._m32 = m._m32;
  this._m33 = m._m33;
  this._columnMajorFloatArray = null;
  this._columnMajorFloatBuffer = null;
}

defineClass(65, 9, {65:1, 9:1}, Matrix44D, Matrix44D_0);
_.dispose = function dispose_41(){
  this._columnMajorFloatArray = null;
  !!this._columnMajorFloatBuffer && $dispose_15(this._columnMajorFloatBuffer);
  $dispose_0(this);
}
;
_.getColumnMajorFloatBuffer = function getColumnMajorFloatBuffer(){
  !this._columnMajorFloatBuffer && (this._columnMajorFloatBuffer = new FloatBuffer_WebGL(this._m00, this._m10, this._m20, this._m30, this._m01, this._m11, this._m21, this._m31, this._m02, this._m12, this._m22, this._m32, this._m03, this._m13, this._m23, this._m33));
  return this._columnMajorFloatBuffer;
}
;
_._m00 = 0;
_._m01 = 0;
_._m02 = 0;
_._m03 = 0;
_._m10 = 0;
_._m11 = 0;
_._m12 = 0;
_._m13 = 0;
_._m20 = 0;
_._m21 = 0;
_._m22 = 0;
_._m23 = 0;
_._m30 = 0;
_._m31 = 0;
_._m32 = 0;
_._m33 = 0;
var Lorg_glob3_mobile_generated_Matrix44D_2_classLit = createForClass('org.glob3.mobile.generated', 'Matrix44D', 65);
function Matrix44DProvider(){
  RCObject.call(this);
}

defineClass(59, 9, $intern_60);
_.dispose = function dispose_42(){
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_Matrix44DProvider_2_classLit = createForClass('org.glob3.mobile.generated', 'Matrix44DProvider', 59);
function $setMatrix_0(this$static, matrix){
  if (!matrix) {
    throw new RuntimeException_0('Setting NULL in Matrix44D Holder');
  }
  if (matrix != this$static._matrix) {
    !!this$static._matrix && $_release(this$static._matrix);
    this$static._matrix = matrix;
    ++this$static._matrix._referenceCounter;
  }
}

function Matrix44DHolder(matrix){
  Matrix44DProvider.call(this);
  this._matrix = matrix;
  if (!matrix) {
    throw new RuntimeException_0('Setting NULL in Matrix44D Holder');
  }
  ++this._matrix._referenceCounter;
}

defineClass(376, 59, $intern_60, Matrix44DHolder);
_.dispose = function dispose_43(){
  $_release(this._matrix);
  $dispose_0(this);
}
;
_.getMatrix = function getMatrix(){
  return this._matrix;
}
;
var Lorg_glob3_mobile_generated_Matrix44DHolder_2_classLit = createForClass('org.glob3.mobile.generated', 'Matrix44DHolder', 376);
function $getMatrix(this$static){
  var i_0, m, m2, m3;
  if (this$static._modelview) {
    for (i_0 = 0; i_0 < this$static._matricesSize; i_0++) {
      m = this$static._providers[i_0].getMatrix();
      !m && $logError(_instance_3, 'Modelview multiplication failure', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
      if (this$static._matrices[i_0] != m) {
        $_release(this$static._modelview);
        this$static._modelview = null;
        $pullMatrixes(this$static);
        break;
      }
    }
  }
  if (!this$static._modelview) {
    this$static._modelview = new Matrix44D_0(this$static._matrices[0]);
    for (i_0 = 1; i_0 < this$static._matricesSize; i_0++) {
      m2 = this$static._matrices[i_0];
      m3 = $createMultiplication(this$static._modelview, m2);
      $_release(this$static._modelview);
      this$static._modelview = m3;
    }
  }
  return this$static._modelview;
}

function $pullMatrixes(this$static){
  var j, newMatrix;
  for (j = 0; j < this$static._matricesSize; j++) {
    newMatrix = this$static._providers[j].getMatrix();
    if (newMatrix != this$static._matrices[j]) {
      !!this$static._matrices[j] && $_release(this$static._matrices[j]);
      this$static._matrices[j] = newMatrix;
      ++this$static._matrices[j]._referenceCounter;
    }
  }
}

function Matrix44DMultiplicationHolder(providers, matricesSize){
  var i_0;
  Matrix44DProvider.call(this);
  this._matricesSize = matricesSize;
  this._modelview = null;
  this._matrices = initDim(Lorg_glob3_mobile_generated_Matrix44D_2_classLit, $intern_4, 65, this._matricesSize, 0, 1);
  this._providers = initDim(Lorg_glob3_mobile_generated_Matrix44DProvider_2_classLit, $intern_4, 59, this._matricesSize, 0, 1);
  for (i_0 = 0; i_0 < this._matricesSize; i_0++) {
    this._matrices[i_0] = null;
    setCheck(this._providers, i_0, providers[i_0]);
    ++this._providers[i_0]._referenceCounter;
  }
  $pullMatrixes(this);
}

defineClass(411, 59, $intern_60, Matrix44DMultiplicationHolder);
_.dispose = function dispose_44(){
  var j;
  for (j = 0; j < this._matricesSize; j++) {
    !!this._matrices[j] && $_release(this._matrices[j]);
    $_release(this._providers[j]);
  }
  !!this._modelview && $_release(this._modelview);
  $dispose_0(this);
}
;
_.getMatrix = function getMatrix_0(){
  return $getMatrix(this);
}
;
_._matricesSize = 0;
var Lorg_glob3_mobile_generated_Matrix44DMultiplicationHolder_2_classLit = createForClass('org.glob3.mobile.generated', 'Matrix44DMultiplicationHolder', 411);
function $add_15(this$static, provider){
  $add_3(this$static._providers, provider);
  ++provider._referenceCounter;
}

function $create_2(this$static){
  var i_0, providersSize, ps;
  providersSize = this$static._providers.array.length;
  ps = initDim(Lorg_glob3_mobile_generated_Matrix44DProvider_2_classLit, $intern_4, 59, providersSize, 0, 1);
  for (i_0 = 0; i_0 < providersSize; i_0++) {
    setCheck(ps, i_0, dynamicCast($get_0(this$static._providers, i_0), 59));
  }
  return new Matrix44DMultiplicationHolder(ps, providersSize);
}

function $dispose_8(this$static){
  var i_0, providersSize;
  providersSize = this$static._providers.array.length;
  for (i_0 = 0; i_0 < providersSize; i_0++) {
    $_release(dynamicCast($get_0(this$static._providers, i_0), 59));
  }
}

function Matrix44DMultiplicationHolderBuilder(){
  this._providers = new ArrayList;
}

defineClass(212, 1, {}, Matrix44DMultiplicationHolderBuilder);
var Lorg_glob3_mobile_generated_Matrix44DMultiplicationHolderBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'Matrix44DMultiplicationHolderBuilder', 212);
function $clinit_MercatorUtils(){
  $clinit_MercatorUtils = emptyMethod;
  new Angle($intern_61, $intern_62);
  new Angle($intern_63, $intern_64);
  new Angle($intern_61, $intern_62);
  new Angle($intern_63, $intern_64);
}

function calculateSplitLatitude(lowerLatitude, upperLatitude){
  var exp_0, atan_0;
  $clinit_MercatorUtils();
  var middleV;
  middleV = (getMercatorV(lowerLatitude) + getMercatorV(upperLatitude)) / 2;
  return exp_0 = Math.exp(-6.283185307179586 * (1 - middleV - 0.5)) , atan_0 = Math.atan(exp_0) , new Angle(($intern_43 - 2 * atan_0) * $intern_36, $intern_43 - 2 * atan_0);
}

function getMercatorV(latitude){
  $clinit_MercatorUtils();
  var latSin;
  if (latitude._degrees >= _upperLimitInDegrees) {
    return 0;
  }
  if (latitude._degrees <= $intern_63) {
    return 1;
  }
  latSin = sin_0(latitude._radians);
  return 1 - (Math.log((1 + latSin) / (1 - latSin)) / 12.566370614359172 + 0.5);
}

var _upperLimitInDegrees = $intern_61;
function ModelGLFeature(model){
  GLCameraGroupFeature.call(this, model, 3);
}

defineClass(349, 50, $intern_47, ModelGLFeature);
_.dispose = function dispose_45(){
  $_release(this._matrixHolder);
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_ModelGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'ModelGLFeature', 349);
function ModelTransformGLFeature(transform){
  GLCameraGroupFeature.call(this, transform, 5);
}

defineClass(206, 50, $intern_47, ModelTransformGLFeature);
_.dispose = function dispose_46(){
  $_release(this._matrixHolder);
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_ModelTransformGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'ModelTransformGLFeature', 206);
function ModelViewGLFeature(camera){
  GLCameraGroupFeature.call(this, $asMatrix44D($getModelViewMatrix(camera)), 13);
}

defineClass(149, 50, {50:1, 27:1, 149:1, 9:1}, ModelViewGLFeature);
_.dispose = function dispose_47(){
  $_release(this._matrixHolder);
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_ModelViewGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'ModelViewGLFeature', 149);
function $create_3(this$static, errors){
  if (!this$static._topSector) {
    setCheck(errors.array, errors.array.length, "Can't find any enabled Layer");
    return null;
  }
  return new LayerTilesRenderParameters(this$static._topSector, this$static._topSectorSplitsByLatitude, this$static._topSectorSplitsByLongitude, this$static._firstLevel, this$static._maxLevel, new Vector2I(this$static._tileTextureWidth, this$static._tileTextureHeight), new Vector2I(this$static._tileMeshWidth, this$static._tileMeshHeight), this$static._mercator);
}

function $update(this$static, layer, errors){
  var foundI, i_0, layerParametersVector, parameters;
  layerParametersVector = layer.getLayerTilesRenderParametersVector();
  if (!this$static._topSector) {
    return $update_0(this$static, (checkElementIndex(0, layerParametersVector.array.length) , dynamicCast(layerParametersVector.array[0], 76)), errors);
  }
  foundI = -1;
  for (i_0 = 0; i_0 < layerParametersVector.array.length; i_0++) {
    parameters = (checkElementIndex(i_0, layerParametersVector.array.length) , dynamicCast(layerParametersVector.array[i_0], 76));
    if (parameters._mercator == this$static._mercator) {
      foundI = i_0;
      break;
    }
  }
  if (foundI < 0) {
    $add_3(errors, "Can't find a compatible LayerTilesRenderParameters in layer " + layer.description_0());
    return false;
  }
  layer.selectLayerTilesRenderParameters(foundI);
  return $update_0(this$static, (checkElementIndex(foundI, layerParametersVector.array.length) , dynamicCast(layerParametersVector.array[foundI], 76)), errors);
}

function $update_0(this$static, parameters, errors){
  if (!this$static._topSector) {
    this$static._topSector = new Sector_0(parameters._topSector);
    this$static._topSectorSplitsByLatitude = parameters._topSectorSplitsByLatitude;
    this$static._topSectorSplitsByLongitude = parameters._topSectorSplitsByLongitude;
    this$static._firstLevel = parameters._firstLevel;
    this$static._maxLevel = parameters._maxLevel;
    this$static._tileTextureWidth = parameters._tileTextureResolution._x;
    this$static._tileTextureHeight = parameters._tileTextureResolution._y;
    this$static._tileMeshWidth = parameters._tileMeshResolution._x;
    this$static._tileMeshHeight = parameters._tileMeshResolution._y;
    this$static._mercator = parameters._mercator;
    return true;
  }
  if (this$static._mercator != parameters._mercator) {
    setCheck(errors.array, errors.array.length, "Inconsistency in Layer's Parameters: mercator");
    return false;
  }
  if (!$isEquals_3(this$static._topSector, parameters._topSector)) {
    setCheck(errors.array, errors.array.length, "Inconsistency in Layer's Parameters: topSector");
    return false;
  }
  if (this$static._topSectorSplitsByLatitude != parameters._topSectorSplitsByLatitude) {
    setCheck(errors.array, errors.array.length, "Inconsistency in Layer's Parameters: topSectorSplitsByLatitude");
    return false;
  }
  if (this$static._topSectorSplitsByLongitude != parameters._topSectorSplitsByLongitude) {
    setCheck(errors.array, errors.array.length, "Inconsistency in Layer's Parameters: topSectorSplitsByLongitude");
    return false;
  }
  if (this$static._tileTextureWidth != parameters._tileTextureResolution._x || this$static._tileTextureHeight != parameters._tileTextureResolution._y) {
    setCheck(errors.array, errors.array.length, "Inconsistency in Layer's Parameters: tileTextureResolution");
    return false;
  }
  if (this$static._tileMeshWidth != parameters._tileMeshResolution._x || this$static._tileMeshHeight != parameters._tileMeshResolution._y) {
    setCheck(errors.array, errors.array.length, "Inconsistency in Layer's Parameters: tileMeshResolution");
    return false;
  }
  if (this$static._maxLevel < parameters._maxLevel) {
    $logWarning(_instance_3, "Inconsistency in Layer's Parameters: maxLevel (upgrading from %d to %d)", initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(this$static._maxLevel), valueOf(parameters._maxLevel)]));
    this$static._maxLevel = parameters._maxLevel;
  }
  if (this$static._firstLevel < parameters._firstLevel) {
    $logWarning(_instance_3, "Inconsistency in Layer's Parameters: firstLevel (upgrading from %d to %d)", initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(this$static._firstLevel), valueOf(parameters._firstLevel)]));
    this$static._firstLevel = parameters._firstLevel;
  }
  return true;
}

function MutableLayerTilesRenderParameters(){
  this._topSector = null;
  this._topSectorSplitsByLatitude = 0;
  this._topSectorSplitsByLongitude = 0;
  this._firstLevel = 0;
  this._maxLevel = 0;
  this._tileTextureWidth = 0;
  this._tileTextureHeight = 0;
  this._tileMeshWidth = 0;
  this._tileMeshHeight = 0;
  this._mercator = false;
}

defineClass(385, 1, {}, MutableLayerTilesRenderParameters);
_._firstLevel = 0;
_._maxLevel = 0;
_._mercator = false;
_._tileMeshHeight = 0;
_._tileMeshWidth = 0;
_._tileTextureHeight = 0;
_._tileTextureWidth = 0;
_._topSectorSplitsByLatitude = 0;
_._topSectorSplitsByLongitude = 0;
var Lorg_glob3_mobile_generated_MutableLayerTilesRenderParameters_2_classLit = createForClass('org.glob3.mobile.generated', 'MutableLayerTilesRenderParameters', 385);
function $asMatrix44D(this$static){
  !this$static._matrix44D && (this$static._matrix44D = new Matrix44D(this$static._m00, this$static._m10, this$static._m20, this$static._m30, this$static._m01, this$static._m11, this$static._m21, this$static._m31, this$static._m02, this$static._m12, this$static._m22, this$static._m32, this$static._m03, this$static._m13, this$static._m23, this$static._m33));
  return this$static._matrix44D;
}

function $copyValue(this$static, m){
  if (!!this$static._matrix44D && this$static._matrix44D == m._matrix44D) {
    return;
  }
  this$static._isValid = m._isValid;
  this$static._m00 = m._m00;
  this$static._m01 = m._m01;
  this$static._m02 = m._m02;
  this$static._m03 = m._m03;
  this$static._m10 = m._m10;
  this$static._m11 = m._m11;
  this$static._m12 = m._m12;
  this$static._m13 = m._m13;
  this$static._m20 = m._m20;
  this$static._m21 = m._m21;
  this$static._m22 = m._m22;
  this$static._m23 = m._m23;
  this$static._m30 = m._m30;
  this$static._m31 = m._m31;
  this$static._m32 = m._m32;
  this$static._m33 = m._m33;
  !!this$static._matrix44D && $_release(this$static._matrix44D);
  this$static._matrix44D = m._matrix44D;
  !!this$static._matrix44D && ++this$static._matrix44D._referenceCounter;
}

function $copyValueOfMultiplication(this$static, m1, m2){
  var m1_00, m1_01, m1_02, m1_03, m1_10, m1_11, m1_12, m1_13, m1_20, m1_21, m1_22, m1_23, m1_30, m1_31, m1_32, m1_33, m2_00, m2_01, m2_02, m2_03, m2_10, m2_11, m2_12, m2_13, m2_20, m2_21, m2_22, m2_23, m2_30, m2_31, m2_32, m2_33;
  m1_00 = m1._m00;
  m1_10 = m1._m10;
  m1_20 = m1._m20;
  m1_30 = m1._m30;
  m1_01 = m1._m01;
  m1_11 = m1._m11;
  m1_21 = m1._m21;
  m1_31 = m1._m31;
  m1_02 = m1._m02;
  m1_12 = m1._m12;
  m1_22 = m1._m22;
  m1_32 = m1._m32;
  m1_03 = m1._m03;
  m1_13 = m1._m13;
  m1_23 = m1._m23;
  m1_33 = m1._m33;
  m2_00 = m2._m00;
  m2_10 = m2._m10;
  m2_20 = m2._m20;
  m2_30 = m2._m30;
  m2_01 = m2._m01;
  m2_11 = m2._m11;
  m2_21 = m2._m21;
  m2_31 = m2._m31;
  m2_02 = m2._m02;
  m2_12 = m2._m12;
  m2_22 = m2._m22;
  m2_32 = m2._m32;
  m2_03 = m2._m03;
  m2_13 = m2._m13;
  m2_23 = m2._m23;
  m2_33 = m2._m33;
  this$static._m00 = m1_00 * m2_00 + m1_01 * m2_10 + m1_02 * m2_20 + m1_03 * m2_30;
  this$static._m01 = m1_00 * m2_01 + m1_01 * m2_11 + m1_02 * m2_21 + m1_03 * m2_31;
  this$static._m02 = m1_00 * m2_02 + m1_01 * m2_12 + m1_02 * m2_22 + m1_03 * m2_32;
  this$static._m03 = m1_00 * m2_03 + m1_01 * m2_13 + m1_02 * m2_23 + m1_03 * m2_33;
  this$static._m10 = m1_10 * m2_00 + m1_11 * m2_10 + m1_12 * m2_20 + m1_13 * m2_30;
  this$static._m11 = m1_10 * m2_01 + m1_11 * m2_11 + m1_12 * m2_21 + m1_13 * m2_31;
  this$static._m12 = m1_10 * m2_02 + m1_11 * m2_12 + m1_12 * m2_22 + m1_13 * m2_32;
  this$static._m13 = m1_10 * m2_03 + m1_11 * m2_13 + m1_12 * m2_23 + m1_13 * m2_33;
  this$static._m20 = m1_20 * m2_00 + m1_21 * m2_10 + m1_22 * m2_20 + m1_23 * m2_30;
  this$static._m21 = m1_20 * m2_01 + m1_21 * m2_11 + m1_22 * m2_21 + m1_23 * m2_31;
  this$static._m22 = m1_20 * m2_02 + m1_21 * m2_12 + m1_22 * m2_22 + m1_23 * m2_32;
  this$static._m23 = m1_20 * m2_03 + m1_21 * m2_13 + m1_22 * m2_23 + m1_23 * m2_33;
  this$static._m30 = m1_30 * m2_00 + m1_31 * m2_10 + m1_32 * m2_20 + m1_33 * m2_30;
  this$static._m31 = m1_30 * m2_01 + m1_31 * m2_11 + m1_32 * m2_21 + m1_33 * m2_31;
  this$static._m32 = m1_30 * m2_02 + m1_31 * m2_12 + m1_32 * m2_22 + m1_33 * m2_32;
  this$static._m33 = m1_30 * m2_03 + m1_31 * m2_13 + m1_32 * m2_23 + m1_33 * m2_33;
  if (this$static._matrix44D) {
    $_release(this$static._matrix44D);
    this$static._matrix44D = null;
  }
}

function $description_2(this$static){
  var f, i_0, isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'MUTABLE MATRIX 44D: ';
  f = $getColumnMajorFloatArray((!this$static._matrix44D && (this$static._matrix44D = new Matrix44D(this$static._m00, this$static._m10, this$static._m20, this$static._m30, this$static._m01, this$static._m11, this$static._m21, this$static._m31, this$static._m02, this$static._m12, this$static._m22, this$static._m32, this$static._m03, this$static._m13, this$static._m23, this$static._m33)) , this$static._matrix44D));
  for (i_0 = 0; i_0 < 16; i_0++) {
    $addDouble(isb, f[i_0]);
    i_0 < 15 && (isb._string += ', ' , isb);
  }
  s = isb._string;
  return s;
}

function $dispose_9(this$static){
  !!this$static._matrix44D && $_release(this$static._matrix44D);
}

function $inversed(this$static){
  var a0, a1, a2, a3, a4, a5, b0, b1, b2, b3, b4, b5, determinant, m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;
  a0 = this$static._m00 * this$static._m11 - this$static._m01 * this$static._m10;
  a1 = this$static._m00 * this$static._m12 - this$static._m02 * this$static._m10;
  a2 = this$static._m00 * this$static._m13 - this$static._m03 * this$static._m10;
  a3 = this$static._m01 * this$static._m12 - this$static._m02 * this$static._m11;
  a4 = this$static._m01 * this$static._m13 - this$static._m03 * this$static._m11;
  a5 = this$static._m02 * this$static._m13 - this$static._m03 * this$static._m12;
  b0 = this$static._m20 * this$static._m31 - this$static._m21 * this$static._m30;
  b1 = this$static._m20 * this$static._m32 - this$static._m22 * this$static._m30;
  b2 = this$static._m20 * this$static._m33 - this$static._m23 * this$static._m30;
  b3 = this$static._m21 * this$static._m32 - this$static._m22 * this$static._m31;
  b4 = this$static._m21 * this$static._m33 - this$static._m23 * this$static._m31;
  b5 = this$static._m22 * this$static._m33 - this$static._m23 * this$static._m32;
  determinant = a0 * b5 - a1 * b4 + a2 * b3 + a3 * b2 - a4 * b1 + a5 * b0;
  if (determinant == 0) {
    return new MutableMatrix44D_2;
  }
  m00 = (this$static._m11 * b5 - this$static._m12 * b4 + this$static._m13 * b3) / determinant;
  m10 = (-this$static._m10 * b5 + this$static._m12 * b2 - this$static._m13 * b1) / determinant;
  m20 = (this$static._m10 * b4 - this$static._m11 * b2 + this$static._m13 * b0) / determinant;
  m30 = (-this$static._m10 * b3 + this$static._m11 * b1 - this$static._m12 * b0) / determinant;
  m01 = (-this$static._m01 * b5 + this$static._m02 * b4 - this$static._m03 * b3) / determinant;
  m11 = (this$static._m00 * b5 - this$static._m02 * b2 + this$static._m03 * b1) / determinant;
  m21 = (-this$static._m00 * b4 + this$static._m01 * b2 - this$static._m03 * b0) / determinant;
  m31 = (this$static._m00 * b3 - this$static._m01 * b1 + this$static._m02 * b0) / determinant;
  m02 = (this$static._m31 * a5 - this$static._m32 * a4 + this$static._m33 * a3) / determinant;
  m12 = (-this$static._m30 * a5 + this$static._m32 * a2 - this$static._m33 * a1) / determinant;
  m22 = (this$static._m30 * a4 - this$static._m31 * a2 + this$static._m33 * a0) / determinant;
  m32 = (-this$static._m30 * a3 + this$static._m31 * a1 - this$static._m32 * a0) / determinant;
  m03 = (-this$static._m21 * a5 + this$static._m22 * a4 - this$static._m23 * a3) / determinant;
  m13 = (this$static._m20 * a5 - this$static._m22 * a2 + this$static._m23 * a1) / determinant;
  m23 = (-this$static._m20 * a4 + this$static._m21 * a2 - this$static._m23 * a0) / determinant;
  m33 = (this$static._m20 * a3 - this$static._m21 * a1 + this$static._m22 * a0) / determinant;
  return new MutableMatrix44D_0(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
}

function $isIdentity(this$static){
  var identity;
  identity = new MutableMatrix44D_0(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  return this$static._m00 == identity._m00 && this$static._m01 == identity._m01 && this$static._m02 == identity._m02 && this$static._m03 == identity._m03 && this$static._m10 == identity._m10 && this$static._m11 == identity._m11 && this$static._m12 == identity._m12 && this$static._m13 == identity._m13 && this$static._m20 == identity._m20 && this$static._m21 == identity._m21 && this$static._m22 == identity._m22 && this$static._m23 == identity._m23 && this$static._m30 == identity._m30 && this$static._m31 == identity._m31 && this$static._m32 == identity._m32 && this$static._m33 == identity._m33;
}

function $multiply(this$static, that){
  var m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33, that00, that01, that02, that03, that10, that11, that12, that13, that20, that21, that22, that23, that30, that31, that32, that33;
  if ($isIdentity(this$static)) {
    return that;
  }
  if ($isIdentity(that)) {
    return this$static;
  }
  that00 = that._m00;
  that10 = that._m10;
  that20 = that._m20;
  that30 = that._m30;
  that01 = that._m01;
  that11 = that._m11;
  that21 = that._m21;
  that31 = that._m31;
  that02 = that._m02;
  that12 = that._m12;
  that22 = that._m22;
  that32 = that._m32;
  that03 = that._m03;
  that13 = that._m13;
  that23 = that._m23;
  that33 = that._m33;
  m00 = this$static._m00 * that00 + this$static._m01 * that10 + this$static._m02 * that20 + this$static._m03 * that30;
  m01 = this$static._m00 * that01 + this$static._m01 * that11 + this$static._m02 * that21 + this$static._m03 * that31;
  m02 = this$static._m00 * that02 + this$static._m01 * that12 + this$static._m02 * that22 + this$static._m03 * that32;
  m03 = this$static._m00 * that03 + this$static._m01 * that13 + this$static._m02 * that23 + this$static._m03 * that33;
  m10 = this$static._m10 * that00 + this$static._m11 * that10 + this$static._m12 * that20 + this$static._m13 * that30;
  m11 = this$static._m10 * that01 + this$static._m11 * that11 + this$static._m12 * that21 + this$static._m13 * that31;
  m12 = this$static._m10 * that02 + this$static._m11 * that12 + this$static._m12 * that22 + this$static._m13 * that32;
  m13 = this$static._m10 * that03 + this$static._m11 * that13 + this$static._m12 * that23 + this$static._m13 * that33;
  m20 = this$static._m20 * that00 + this$static._m21 * that10 + this$static._m22 * that20 + this$static._m23 * that30;
  m21 = this$static._m20 * that01 + this$static._m21 * that11 + this$static._m22 * that21 + this$static._m23 * that31;
  m22 = this$static._m20 * that02 + this$static._m21 * that12 + this$static._m22 * that22 + this$static._m23 * that32;
  m23 = this$static._m20 * that03 + this$static._m21 * that13 + this$static._m22 * that23 + this$static._m23 * that33;
  m30 = this$static._m30 * that00 + this$static._m31 * that10 + this$static._m32 * that20 + this$static._m33 * that30;
  m31 = this$static._m30 * that01 + this$static._m31 * that11 + this$static._m32 * that21 + this$static._m33 * that31;
  m32 = this$static._m30 * that02 + this$static._m31 * that12 + this$static._m32 * that22 + this$static._m33 * that32;
  m33 = this$static._m30 * that03 + this$static._m31 * that13 + this$static._m32 * that23 + this$static._m33 * that33;
  return new MutableMatrix44D_0(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
}

function $setValue(this$static, m00, m10, m20, m01, m11, m21, m02, m12, m22){
  this$static._m00 = m00;
  this$static._m01 = m01;
  this$static._m02 = m02;
  this$static._m03 = 0;
  this$static._m10 = m10;
  this$static._m11 = m11;
  this$static._m12 = m12;
  this$static._m13 = 0;
  this$static._m20 = m20;
  this$static._m21 = m21;
  this$static._m22 = m22;
  this$static._m23 = 0;
  this$static._m30 = 0;
  this$static._m31 = 0;
  this$static._m32 = 0;
  this$static._m33 = 1;
  this$static._isValid = true;
}

function $unproject(this$static, pixel3D, vpWidth, vpHeight){
  var in0, in1, in2, m, objx, objy, objz, out0, out1, out2, out3, winx, winy, winz;
  winx = pixel3D._x;
  winy = pixel3D._y;
  winz = pixel3D._z;
  in0 = winx * 2 / vpWidth - 1;
  in1 = winy * 2 / vpHeight - 1;
  in2 = 2 * winz - 1;
  m = $inversed(this$static);
  out0 = m._m00 * in0 + m._m01 * in1 + m._m02 * in2 + m._m03;
  out1 = m._m10 * in0 + m._m11 * in1 + m._m12 * in2 + m._m13;
  out2 = m._m20 * in0 + m._m21 * in1 + m._m22 * in2 + m._m23;
  out3 = m._m30 * in0 + m._m31 * in1 + m._m32 * in2 + m._m33;
  if (out3 == 0) {
    return $clinit_Vector3D() , new Vector3D(NaN, NaN, NaN);
  }
  objx = out0 / out3;
  objy = out1 / out3;
  objz = out2 / out3;
  return new Vector3D(objx, objy, objz);
}

function MutableMatrix44D(){
  this._isValid = true;
  this._matrix44D = null;
  this._m00 = 0;
  this._m01 = 0;
  this._m02 = 0;
  this._m03 = 0;
  this._m10 = 0;
  this._m11 = 0;
  this._m12 = 0;
  this._m13 = 0;
  this._m20 = 0;
  this._m21 = 0;
  this._m22 = 0;
  this._m23 = 0;
  this._m30 = 0;
  this._m31 = 0;
  this._m32 = 0;
  this._m33 = 0;
}

function MutableMatrix44D_0(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33){
  this._isValid = true;
  this._matrix44D = null;
  this._m00 = m00;
  this._m01 = m01;
  this._m02 = m02;
  this._m03 = m03;
  this._m10 = m10;
  this._m11 = m11;
  this._m12 = m12;
  this._m13 = m13;
  this._m20 = m20;
  this._m21 = m21;
  this._m22 = m22;
  this._m23 = m23;
  this._m30 = m30;
  this._m31 = m31;
  this._m32 = m32;
  this._m33 = m33;
  this._matrix44D = null;
}

function MutableMatrix44D_1(m){
  this._isValid = m._isValid;
  this._m00 = m._m00;
  this._m01 = m._m01;
  this._m02 = m._m02;
  this._m03 = m._m03;
  this._m10 = m._m10;
  this._m11 = m._m11;
  this._m12 = m._m12;
  this._m13 = m._m13;
  this._m20 = m._m20;
  this._m21 = m._m21;
  this._m22 = m._m22;
  this._m23 = m._m23;
  this._m30 = m._m30;
  this._m31 = m._m31;
  this._m32 = m._m32;
  this._m33 = m._m33;
  this._matrix44D = m._matrix44D;
  !!this._matrix44D && ++this._matrix44D._referenceCounter;
}

function MutableMatrix44D_2(){
  this._isValid = false;
  this._matrix44D = null;
}

function createGeneralRotationMatrix(angle, axis_0, point){
  var R, T1, T2;
  T1 = createTranslationMatrix(new Vector3D(-point._x, -point._y, -point._z));
  R = createRotationMatrix(angle, axis_0);
  T2 = new MutableMatrix44D_0(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, point._x, point._y, point._z, 1);
  return $multiply($multiply(T2, R), T1);
}

function createModelMatrix(pos, center, up){
  var pe, u, v, w;
  w = $normalized(new MutableVector3D_0(center._x - pos._x, center._y - pos._y, center._z - pos._z));
  pe = w._x * up._x + w._y * up._y + w._z * up._z;
  v = $normalized($sub_0(up, new MutableVector3D_0(w._x * pe, w._y * pe, w._z * pe)));
  u = new MutableVector3D_0(w._y * v._z - w._z * v._y, w._z * v._x - w._x * v._z, w._x * v._y - w._y * v._x);
  return new MutableMatrix44D_0(u._x, v._x, -w._x, 0, u._y, v._y, -w._y, 0, u._z, v._z, -w._z, 0, -(pos._x * u._x + pos._y * u._y + pos._z * u._z), -(pos._x * v._x + pos._y * v._y + pos._z * v._z), pos._x * w._x + pos._y * w._y + pos._z * w._z, 1);
}

function createOrthographicProjectionMatrix(left, right, bottom, top_0, znear, zfar){
  var fn, rl, tb;
  rl = right - left;
  tb = top_0 - bottom;
  fn = zfar - znear;
  return new MutableMatrix44D_0(2 / rl, 0, 0, 0, 0, 2 / tb, 0, 0, 0, 0, -2 / fn, 0, -(right + left) / rl, -(top_0 + bottom) / tb, -(zfar + znear) / fn, 1);
}

function createProjectionMatrix(left, right, bottom, top_0, znear, zfar){
  var fn, rl, tb;
  rl = right - left;
  tb = top_0 - bottom;
  fn = zfar - znear;
  return new MutableMatrix44D_0(2 * znear / rl, 0, 0, 0, 0, 2 * znear / tb, 0, 0, (right + left) / rl, (top_0 + bottom) / tb, -(zfar + znear) / fn, -1, 0, 0, -2 * zfar / fn * znear, 0);
}

function createProjectionMatrix_0(data_0){
  return createProjectionMatrix(data_0._left, data_0._right, data_0._bottom, data_0._top, data_0._znear, data_0._zfar);
}

function createRotationMatrix(angle, axis_0){
  var a, c, s;
  a = $normalized_0(axis_0);
  c = cos_0(angle._radians);
  s = sin_0(angle._radians);
  return new MutableMatrix44D_0(a._x * a._x * (1 - c) + c, a._x * a._y * (1 - c) + a._z * s, a._x * a._z * (1 - c) - a._y * s, 0, a._y * a._x * (1 - c) - a._z * s, a._y * a._y * (1 - c) + c, a._y * a._z * (1 - c) + a._x * s, 0, a._x * a._z * (1 - c) + a._y * s, a._y * a._z * (1 - c) - a._x * s, a._z * a._z * (1 - c) + c, 0, 0, 0, 0, 1);
}

function createTranslationMatrix(t){
  return new MutableMatrix44D_0(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, t._x, t._y, t._z, 1);
}

defineClass(11, 1, {}, MutableMatrix44D, MutableMatrix44D_0, MutableMatrix44D_1, MutableMatrix44D_2);
_.toString$ = function toString_35(){
  return $description_2(this);
}
;
_._isValid = false;
_._m00 = 0;
_._m01 = 0;
_._m02 = 0;
_._m03 = 0;
_._m10 = 0;
_._m11 = 0;
_._m12 = 0;
_._m13 = 0;
_._m20 = 0;
_._m21 = 0;
_._m22 = 0;
_._m23 = 0;
_._m30 = 0;
_._m31 = 0;
_._m32 = 0;
_._m33 = 0;
var Lorg_glob3_mobile_generated_MutableMatrix44D_2_classLit = createForClass('org.glob3.mobile.generated', 'MutableMatrix44D', 11);
function MutableVector2F(){
  this._x = 0;
  this._y = 0;
}

function MutableVector2F_0(x_0, y_0){
  this._x = x_0;
  this._y = y_0;
}

defineClass(121, 1, {}, MutableVector2F, MutableVector2F_0);
_._x = 0;
_._y = 0;
var Lorg_glob3_mobile_generated_MutableVector2F_2_classLit = createForClass('org.glob3.mobile.generated', 'MutableVector2F', 121);
function $set_3(this$static, x_0, y_0){
  this$static._x = x_0;
  this$static._y = y_0;
}

function MutableVector2I(){
  this._x = 0;
  this._y = 0;
}

defineClass(199, 1, {}, MutableVector2I);
_._x = 0;
_._y = 0;
var Lorg_glob3_mobile_generated_MutableVector2I_2_classLit = createForClass('org.glob3.mobile.generated', 'MutableVector2I', 199);
function $addInPlace(this$static, that){
  this$static._x += that._x;
  this$static._y += that._y;
  this$static._z += that._z;
}

function $angleBetween(this$static, other){
  var c, v1, v2;
  v1 = $normalized(this$static);
  v2 = $normalized(other);
  c = v1._x * v2._x + v1._y * v2._y + v1._z * v2._z;
  c > 1?(c = 1):c < -1 && (c = -1);
  return fromRadians(acos_0(c));
}

function $asVector3D(this$static){
  return new Vector3D(this$static._x, this$static._y, this$static._z);
}

function $copyFrom_1(this$static, that){
  this$static._x = that._x;
  this$static._y = that._y;
  this$static._z = that._z;
}

function $copyFrom_2(this$static, that){
  this$static._x = that._x;
  this$static._y = that._y;
  this$static._z = that._z;
}

function $cross(this$static, other){
  return new MutableVector3D_0(this$static._y * other._z - this$static._z * other._y, this$static._z * other._x - this$static._x * other._z, this$static._x * other._y - this$static._y * other._x);
}

function $dot(this$static, v){
  return this$static._x * v._x + this$static._y * v._y + this$static._z * v._z;
}

function $equalTo(this$static, v){
  return v._x == this$static._x && v._y == this$static._y && v._z == this$static._z;
}

function $isNan_0(this$static){
  return this$static._x != this$static._x || this$static._y != this$static._y || this$static._z != this$static._z;
}

function $length(this$static){
  return sqrt_0(this$static._x * this$static._x + this$static._y * this$static._y + this$static._z * this$static._z);
}

function $normalize(this$static){
  var d;
  d = sqrt_0(this$static._x * this$static._x + this$static._y * this$static._y + this$static._z * this$static._z);
  this$static._x /= d;
  this$static._y /= d;
  this$static._z /= d;
}

function $normalized(this$static){
  var d;
  d = sqrt_0(this$static._x * this$static._x + this$static._y * this$static._y + this$static._z * this$static._z);
  return new MutableVector3D_0(this$static._x / d, this$static._y / d, this$static._z / d);
}

function $putSub(this$static, a, b){
  this$static._x = a._x - b._x;
  this$static._y = a._y - b._y;
  this$static._z = a._z - b._z;
}

function $rotateAroundAxis(this$static, axis_0, theta){
  var cosTheta, m, ms, sinTheta, u, v, w;
  u = axis_0._x;
  v = axis_0._y;
  w = axis_0._z;
  cosTheta = cos_0(theta._radians);
  sinTheta = sin_0(theta._radians);
  ms = axis_0._x * axis_0._x + axis_0._y * axis_0._y + axis_0._z * axis_0._z;
  m = sqrt_0(ms);
  return new MutableVector3D_0((u * (u * this$static._x + v * this$static._y + w * this$static._z) + (this$static._x * (v * v + w * w) - u * (v * this$static._y + w * this$static._z)) * cosTheta + m * (-w * this$static._y + v * this$static._z) * sinTheta) / ms, (v * (u * this$static._x + v * this$static._y + w * this$static._z) + (this$static._y * (u * u + w * w) - v * (u * this$static._x + w * this$static._z)) * cosTheta + m * (w * this$static._x - u * this$static._z) * sinTheta) / ms, (w * (u * this$static._x + v * this$static._y + w * this$static._z) + (this$static._z * (u * u + v * v) - w * (u * this$static._x + v * this$static._y)) * cosTheta + m * (-(v * this$static._x) + u * this$static._y) * sinTheta) / ms);
}

function $set_4(this$static, x_0, y_0, z_0){
  this$static._x = x_0;
  this$static._y = y_0;
  this$static._z = z_0;
}

function $squaredDistanceTo(this$static, that){
  var dx, dy, dz;
  dx = this$static._x - that._x;
  dy = this$static._y - that._y;
  dz = this$static._z - that._z;
  return dx * dx + dy * dy + dz * dz;
}

function $sub_0(this$static, v){
  return new MutableVector3D_0(this$static._x - v._x, this$static._y - v._y, this$static._z - v._z);
}

function $times_0(this$static){
  return new MutableVector3D_0(-this$static._x, -this$static._y, -this$static._z);
}

function $times_1(this$static, v){
  return new MutableVector3D_0(this$static._x * v._x, this$static._y * v._y, this$static._z * v._z);
}

function $transformedBy(this$static, m, homogeneus){
  return new MutableVector3D_0(this$static._x * m._m00 + this$static._y * m._m01 + this$static._z * m._m02 + homogeneus * m._m03, this$static._x * m._m10 + this$static._y * m._m11 + this$static._z * m._m12 + homogeneus * m._m13, this$static._x * m._m20 + this$static._y * m._m21 + this$static._z * m._m22 + homogeneus * m._m23);
}

function MutableVector3D(){
  this._x = 0;
  this._y = 0;
  this._z = 0;
}

function MutableVector3D_0(x_0, y_0, z_0){
  this._x = x_0;
  this._y = y_0;
  this._z = z_0;
}

function angleInRadiansBetween(a, b){
  var c, aLength, a_x, a_y, a_z, bLength, b_x, b_y, b_z;
  c = (aLength = sqrt_0(a._x * a._x + a._y * a._y + a._z * a._z) , a_x = a._x / aLength , a_y = a._y / aLength , a_z = a._z / aLength , bLength = sqrt_0(b._x * b._x + b._y * b._y + b._z * b._z) , b_x = b._x / bLength , b_y = b._y / bLength , b_z = b._z / bLength , a_x * b_x + a_y * b_y + a_z * b_z);
  c > 1?(c = 1):c < -1 && (c = -1);
  return acos_0(c);
}

defineClass(8, 1, {}, MutableVector3D, MutableVector3D_0);
_._x = 0;
_._y = 0;
_._z = 0;
var Lorg_glob3_mobile_generated_MutableVector3D_2_classLit = createForClass('org.glob3.mobile.generated', 'MutableVector3D', 8);
function $signedDistance(this$static, point){
  return this$static._normalF._x * point._x + this$static._normalF._y * point._y + this$static._normalF._z * point._z + this$static._dF;
}

function $transformedByTranspose(this$static, M){
  var a, b, c, d;
  a = this$static._normal._x * M._m00 + this$static._normal._y * M._m10 + this$static._normal._z * M._m20 + this$static._d * M._m30;
  b = this$static._normal._x * M._m01 + this$static._normal._y * M._m11 + this$static._normal._z * M._m21 + this$static._d * M._m31;
  c = this$static._normal._x * M._m02 + this$static._normal._y * M._m12 + this$static._normal._z * M._m22 + this$static._d * M._m32;
  d = this$static._normal._x * M._m03 + this$static._normal._y * M._m13 + this$static._normal._z * M._m23 + this$static._d * M._m33;
  return new Plane(a, b, c, d);
}

function Plane(a, b, c, d){
  this._normal = new Vector3D_0($normalized_0(new Vector3D(a, b, c)));
  this._d = d;
  this._normalF = $normalized_1(new Vector3F(a, b, c));
  this._dF = d;
}

function Plane_0(normal, d){
  this._normal = new Vector3D_0($normalized_0(normal));
  this._d = d;
  this._normalF = $normalized_1(new Vector3F(normal._x, normal._y, normal._z));
  this._dF = d;
}

function fromPoints(point0, point1, point2){
  var d, normal;
  normal = $normalized_0($cross_0(new Vector3D(point1._x - point0._x, point1._y - point0._y, point1._z - point0._z), new Vector3D(point2._x - point0._x, point2._y - point0._y, point2._z - point0._z)));
  d = -(normal._x * point0._x + normal._y * point0._y + normal._z * point0._z);
  return new Plane_0(normal, d);
}

defineClass(125, 1, {}, Plane, Plane_0);
_._d = 0;
_._dF = 0;
var Lorg_glob3_mobile_generated_Plane_2_classLit = createForClass('org.glob3.mobile.generated', 'Plane', 125);
function $addVisibleSectorListener(this$static, stabilizationInterval){
  $add_3(this$static._visibleSectorListeners, new VisibleSectorListenerEntry(stabilizationInterval));
}

function $changed(this$static){
  if (!this$static._recreateTilesPending) {
    this$static._recreateTilesPending = true;
    !this$static._context?$logError(_instance_3, '_context is not initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [])):$invokeTask(this$static._context._threadUtils, new RecreateTilesTask(this$static));
  }
}

function $changedInfo(this$static, info){
  !!this$static._changedInfoListener && this$static._changedInfoListener.changedRendererInfo(this$static._rendererIdentifier, info);
}

function $clearFirstLevelTiles(this$static){
  var firstLevelTilesCount, i_0, tile;
  firstLevelTilesCount = this$static._firstLevelTiles.array.length;
  for (i_0 = 0; i_0 < firstLevelTilesCount; i_0++) {
    tile = dynamicCast($get_0(this$static._firstLevelTiles, i_0), 23);
    $toBeDeleted(tile, this$static._texturizer, this$static._elevationDataProvider, this$static._tilesStoppedRendering);
    !!tile && (!!tile._debugMesh && $dispose_6(tile._debugMesh) , tile._debugMesh = null , !!tile._flatColorMesh && $dispose_2(tile._flatColorMesh) , tile._flatColorMesh = null , !!tile._tessellatorMesh && $dispose_5(tile._tessellatorMesh) , tile._tessellatorMesh = null , !!tile._texturizerData && $dispose_1(tile._texturizerData) , tile._texturizerData = null , !!tile._texturizedMesh && $dispose_7(tile._texturizedMesh) , tile._texturizedMesh = null , null);
  }
  this$static._firstLevelTiles.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
}

function $createFirstLevelTiles(this$static, firstLevelTiles, tile, firstLevel){
  var child, children, childrenSize, i_0, lower, sector, splitLatitude, splitLongitude, upper;
  if (tile._level == firstLevel) {
    setCheck(firstLevelTiles.array, firstLevelTiles.array.length, tile);
  }
   else {
    sector = tile._sector;
    lower = sector._lower;
    upper = sector._upper;
    splitLongitude = midAngle(lower._longitude, upper._longitude);
    splitLatitude = tile._mercator?calculateSplitLatitude(lower._latitude, upper._latitude):midAngle(lower._latitude, upper._latitude);
    children = $createSubTiles(tile, splitLatitude, splitLongitude, false);
    childrenSize = children.array.length;
    for (i_0 = 0; i_0 < childrenSize; i_0++) {
      child = (checkElementIndex(i_0, children.array.length) , dynamicCast(children.array[i_0], 23));
      $createFirstLevelTiles(this$static, firstLevelTiles, child, firstLevel);
    }
    !!tile && (!!tile._debugMesh && $dispose_6(tile._debugMesh) , tile._debugMesh = null , !!tile._flatColorMesh && $dispose_2(tile._flatColorMesh) , tile._flatColorMesh = null , !!tile._tessellatorMesh && $dispose_5(tile._tessellatorMesh) , tile._tessellatorMesh = null , !!tile._texturizerData && $dispose_1(tile._texturizerData) , tile._texturizerData = null , !!tile._texturizedMesh && $dispose_7(tile._texturizedMesh) , tile._texturizedMesh = null , null);
  }
}

function $createFirstLevelTiles_0(this$static, context){
  var col, deltaLan, deltaLon, fromLatitude, fromLongitude, i_0, parameters, row, sector, tile, tileHeight, tileLatFrom, tileLatTo, tileLonFrom, tileLonTo, tileLower, tileUpper, tileWidth, topLevelTiles, topLevelTilesSize, topSectorSplitsByLatitude, topSectorSplitsByLongitude;
  parameters = $getLayerTilesRenderParameters(this$static);
  if (!parameters) {
    return;
  }
  topLevelTiles = new ArrayList;
  fromLatitude = parameters._topSector._lower._latitude;
  fromLongitude = parameters._topSector._lower._longitude;
  deltaLan = parameters._topSector._deltaLatitude;
  deltaLon = parameters._topSector._deltaLongitude;
  topSectorSplitsByLatitude = parameters._topSectorSplitsByLatitude;
  topSectorSplitsByLongitude = parameters._topSectorSplitsByLongitude;
  tileHeight = $div(deltaLan, topSectorSplitsByLatitude);
  tileWidth = $div(deltaLon, topSectorSplitsByLongitude);
  for (row = 0; row < topSectorSplitsByLatitude; row++) {
    tileLatFrom = $add_5($times(tileHeight, row), fromLatitude);
    tileLatTo = $add_5(tileLatFrom, tileHeight);
    for (col = 0; col < topSectorSplitsByLongitude; col++) {
      tileLonFrom = $add_5($times(tileWidth, col), fromLongitude);
      tileLonTo = $add_5(tileLonFrom, tileWidth);
      tileLower = new Geodetic2D(tileLatFrom, tileLonFrom);
      tileUpper = new Geodetic2D(tileLatTo, tileLonTo);
      sector = new Sector(tileLower, tileUpper);
      if (!this$static._renderedSector || $touchesWith(sector, this$static._renderedSector)) {
        tile = new Tile(this$static._texturizer, null, sector, parameters._mercator, 0, row, col, this$static);
        parameters._firstLevel == 0?$add_3(this$static._firstLevelTiles, tile):(setCheck(topLevelTiles.array, topLevelTiles.array.length, tile) , true);
      }
    }
  }
  if (parameters._firstLevel > 0) {
    topLevelTilesSize = topLevelTiles.array.length;
    for (i_0 = 0; i_0 < topLevelTilesSize; i_0++) {
      tile = (checkElementIndex(i_0, topLevelTiles.array.length) , dynamicCast(topLevelTiles.array[i_0], 23));
      $createFirstLevelTiles(this$static, this$static._firstLevelTiles, tile, parameters._firstLevel);
    }
  }
  sort_0(this$static._firstLevelTiles, new PlanetRenderer$1);
  $logInfo(context._logger, 'Created %d first level tiles', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(this$static._firstLevelTiles.array.length)]));
  this$static._firstLevelTiles.array.length > 64 && $logWarning(context._logger, '%d tiles are many for the first level. We recommend a number of those less than 64. You can review some parameters (Render Sector and/or First Level) to reduce the number of tiles.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(this$static._firstLevelTiles.array.length)]));
  this$static._firstLevelTilesJustCreated = true;
}

function $getLayerTilesRenderParameters(this$static){
  if (this$static._layerTilesRenderParametersDirty) {
    this$static._errors.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
    this$static._layerTilesRenderParameters = null;
    this$static._layerTilesRenderParameters = $createLayerTilesRenderParameters(this$static._layerSet, this$static._tilesRenderParameters._forceFirstLevelTilesRenderOnStart, this$static._errors);
    !this$static._layerTilesRenderParameters && $logError(_instance_3, "LayerSet returned a NULL for LayerTilesRenderParameters, can't render planet", initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    this$static._layerTilesRenderParametersDirty = false;
  }
  return this$static._layerTilesRenderParameters;
}

function $pruneFirstLevelTiles(this$static){
  var firstLevelTilesCount, i_0, tile;
  firstLevelTilesCount = this$static._firstLevelTiles.array.length;
  for (i_0 = 0; i_0 < firstLevelTilesCount; i_0++) {
    tile = dynamicCast($get_0(this$static._firstLevelTiles, i_0), 23);
    $prune(tile, this$static._texturizer, this$static._elevationDataProvider, this$static._tilesStoppedRendering);
  }
}

function $recreateTiles(this$static){
  $pruneFirstLevelTiles(this$static);
  $clearFirstLevelTiles(this$static);
  this$static._layerTilesRenderParameters = null;
  this$static._layerTilesRenderParameters = null;
  this$static._layerTilesRenderParametersDirty = true;
  this$static._firstRender = true;
  this$static._allFirstLevelTilesAreTextureSolved = false;
  $createFirstLevelTiles_0(this$static, this$static._context);
  this$static._recreateTilesPending = false;
}

function PlanetRenderer(tessellator, elevationDataProvider, verticalExaggeration, texturizer, layerSet, tilesRenderParameters, showStatistics, tileDownloadPriority, renderedSector, renderTileMeshes, logTilesPetitions, changedInfoListener, touchEventTypeOfTerrainTouchListener){
  DefaultRenderer.call(this);
  this._statistics = new TilesStatistics;
  this._firstLevelTiles = new ArrayList;
  this._visibleSectorListeners = new ArrayList;
  this._errors = new ArrayList;
  this._terrainTouchListeners = new ArrayList;
  this._toVisit = new ArrayList;
  this._toVisitInNextIteration = new ArrayList;
  this._tessellator = tessellator;
  this._elevationDataProvider = elevationDataProvider;
  this._verticalExaggeration = verticalExaggeration;
  this._texturizer = texturizer;
  this._layerSet = layerSet;
  this._tilesRenderParameters = tilesRenderParameters;
  this._showStatistics = showStatistics;
  this._firstLevelTilesJustCreated = false;
  this._lastSplitTimer = null;
  this._lastCamera = null;
  this._firstRender = false;
  this._lastVisibleSector = null;
  this._tileDownloadPriority = tileDownloadPriority;
  this._allFirstLevelTilesAreTextureSolved = false;
  this._recreateTilesPending = false;
  this._glState = new GLState;
  this._renderedSector = $isEquals_3(renderedSector, ($clinit_Sector() , $clinit_Sector() , FULL_SPHERE))?null:new Sector_0(renderedSector);
  this._layerTilesRenderParameters = null;
  this._layerTilesRenderParametersDirty = true;
  this._renderTileMeshes = renderTileMeshes;
  this._logTilesPetitions = logTilesPetitions;
  this._touchEventTypeOfTerrainTouchListener = touchEventTypeOfTerrainTouchListener;
  this._context = null;
  this._changedInfoListener = changedInfoListener;
  $setChangeListener(this._layerSet, this);
  $setChangedInfoListener(this._layerSet, this);
  this._tilesStoppedRendering = null;
  this._rendererIdentifier = -1;
}

defineClass(308, 179, {135:1}, PlanetRenderer);
_.getPlanetRenderer = function getPlanetRenderer_1(){
  return this;
}
;
_.getRenderState = function getRenderState_3(rc){
  var firstLevelTilesCount, i_0, i0, layerSetRenderState, layerTilesRenderParameters, texturizerRenderState, tile;
  if (!this._tessellator) {
    return error_0('Tessellator is null');
  }
  if (!this._texturizer) {
    return error_0('Texturizer is null');
  }
  layerTilesRenderParameters = $getLayerTilesRenderParameters(this);
  if (!layerTilesRenderParameters) {
    if (this._errors.array.length == 0) {
      if (this._tilesRenderParameters._forceFirstLevelTilesRenderOnStart) {
        return $clinit_RenderState() , $clinit_RenderState() , BUSY;
      }
    }
     else {
      return $clinit_RenderState() , new RenderState(this._errors);
    }
  }
  layerSetRenderState = $getRenderState_0(this._layerSet);
  if (layerSetRenderState._type != 0) {
    return layerSetRenderState;
  }
  texturizerRenderState = $getRenderState(this._texturizer, this._layerSet);
  if (texturizerRenderState._type != 0) {
    return texturizerRenderState;
  }
  if (this._firstLevelTilesJustCreated) {
    this._firstLevelTilesJustCreated = false;
    firstLevelTilesCount = this._firstLevelTiles.array.length;
    if (this._tilesRenderParameters._forceFirstLevelTilesRenderOnStart) {
      $clear(this._statistics);
      for (i0 = 0; i0 < firstLevelTilesCount; i0++) {
        tile = dynamicCast($get_0(this._firstLevelTiles, i0), 23);
        $prepareForFullRendering(tile, rc, this._texturizer, this._tessellator, layerTilesRenderParameters, this._layerSet, this._tileDownloadPriority, this._verticalExaggeration, this._logTilesPetitions);
      }
    }
    for (i_0 = 0; i_0 < firstLevelTilesCount; i_0++) {
      dynamicCast($get_0(this._firstLevelTiles, i_0), 23);
    }
  }
  if (this._tilesRenderParameters._forceFirstLevelTilesRenderOnStart && !this._allFirstLevelTilesAreTextureSolved) {
    firstLevelTilesCount = this._firstLevelTiles.array.length;
    for (i_0 = 0; i_0 < firstLevelTilesCount; i_0++) {
      tile = dynamicCast($get_0(this._firstLevelTiles, i_0), 23);
      if (!tile._textureSolved) {
        return $clinit_RenderState() , $clinit_RenderState() , BUSY;
      }
    }
    this._allFirstLevelTilesAreTextureSolved = true;
  }
  return $clinit_RenderState() , $clinit_RenderState() , READY;
}
;
_.getSurfaceElevationProvider = function getSurfaceElevationProvider_1(){
  return this;
}
;
_.initialize = function initialize_5(context){
  this._context = context;
  $pruneFirstLevelTiles(this);
  $clearFirstLevelTiles(this);
  $createFirstLevelTiles_0(this, context);
  this._lastSplitTimer = new Timer_WebGL;
  $initialize_3(this._layerSet);
  $initialize_1(this._texturizer);
}
;
_.isPlanetRenderer = function isPlanetRenderer_1(){
  return true;
}
;
_.onResizeViewportEvent = function onResizeViewportEvent_4(ec, width_0, height){
}
;
_.onTouchEvent_0 = function onTouchEvent_6(ec, touchEvent){
  var firstLevelTilesCount, i_0, j, normalizedPixel, origin, pixel, planet, position, positionCartesian, ray, terrainTouchListenersSize, tile, tileDimension;
  if (!this._lastCamera) {
    return false;
  }
  if (touchEvent._eventType == this._touchEventTypeOfTerrainTouchListener) {
    pixel = dynamicCast($get_0(touchEvent._touchs, 0), 10)._pos;
    ray = $pixel2Ray(this._lastCamera, pixel);
    origin = $asVector3D(this._lastCamera._position);
    planet = ec._planet;
    positionCartesian = $closestIntersection(planet, origin, ray);
    if (positionCartesian._x != positionCartesian._x || positionCartesian._y != positionCartesian._y || positionCartesian._z != positionCartesian._z) {
      $logWarning(_instance_3, 'PlanetRenderer::onTouchEvent: positionCartesian ( - planet->closestIntersection(origin, ray) - ) is NaN', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
      return false;
    }
    position = $toGeodetic3D(planet, positionCartesian);
    firstLevelTilesCount = this._firstLevelTiles.array.length;
    for (i_0 = 0; i_0 < firstLevelTilesCount; i_0++) {
      tile = $getDeepestTileContaining(dynamicCast($get_0(this._firstLevelTiles, i_0), 23), position);
      if (tile) {
        tileDimension = new Vector2I(256, 256);
        normalizedPixel = $getNormalizedPixelsFromPosition(tile, new Geodetic2D(position._latitude, position._longitude), tileDimension);
        $logInfo(_instance_3, 'Touched on %s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [$description_6(tile)]));
        $logInfo(_instance_3, 'Touched on position %s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [$description_1(position)]));
        $logInfo(_instance_3, 'Touched on pixels %s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [$description_7(normalizedPixel)]));
        $logInfo(_instance_3, 'Camera position=%s heading=%f pitch=%f', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [$description_1($getGeodeticPosition(this._lastCamera)), ($clinit_Double() , new Double($getHeadingPitchRoll(this._lastCamera)._heading._degrees)), new Double($getHeadingPitchRoll(this._lastCamera)._pitch._degrees)]));
        if ($onTerrainTouchEvent(position, tile, this._layerSet)) {
          return true;
        }
        terrainTouchListenersSize = this._terrainTouchListeners.array.length;
        for (j = terrainTouchListenersSize - 1; j >= 0; j--) {
          throwClassCastExceptionUnlessNull($get_0(this._terrainTouchListeners, j));
          if (null.nullMethod()) {
            return true;
          }
        }
        return false;
      }
    }
  }
  return false;
}
;
_.render = function render_9(rc, glState){
  var cameraFrustumInModelCoordinates, correctionFactor, entry, factor, firstLevelTilesCount, i_0, i0, i1, layerTilesRenderParameters, nowInMS, previousLastVisibleSector, texHeight, texHeightSquared, texWidth, texWidthSquared, tile, toVisitInNextIterationSize, toVisitSize, visibleSectorListenersCount, camera, f;
  layerTilesRenderParameters = $getLayerTilesRenderParameters(this);
  if (!layerTilesRenderParameters) {
    return;
  }
  camera = rc._currentCamera;
  f = dynamicCast($getGLFeature(this._glState, 13), 149);
  !f?$addGLFeature(this._glState, new ModelViewGLFeature(camera), true):$setMatrix(f, $asMatrix44D($getModelViewMatrix(camera)));
  $setParent_2(this._glState, glState);
  this._lastCamera = rc._currentCamera;
  $clear(this._statistics);
  $getDeviceInfo(_instance_1);
  factor = this._tilesRenderParameters._texturePixelsPerInch;
  correctionFactor = 96 / factor;
  texWidth = correctionFactor * layerTilesRenderParameters._tileTextureResolution._x;
  texHeight = correctionFactor * layerTilesRenderParameters._tileTextureResolution._y;
  texWidthSquared = texWidth * texWidth;
  texHeightSquared = texHeight * texHeight;
  firstLevelTilesCount = this._firstLevelTiles.array.length;
  cameraFrustumInModelCoordinates = $getFrustumInModelCoordinates(this._lastCamera);
  nowInMS = toDouble(fromDouble(now_1()));
  if (this._firstRender && this._tilesRenderParameters._forceFirstLevelTilesRenderOnStart) {
    for (i_0 = 0; i_0 < firstLevelTilesCount; i_0++) {
      tile = dynamicCast($get_0(this._firstLevelTiles, i_0), 23);
      $render_5(tile, rc, this._glState, null, cameraFrustumInModelCoordinates, this._statistics, this._verticalExaggeration, layerTilesRenderParameters, this._texturizer, this._tilesRenderParameters, this._lastSplitTimer, this._elevationDataProvider, this._tessellator, this._layerSet, this._renderedSector, this._firstRender, this._tileDownloadPriority, texWidthSquared, texHeightSquared, nowInMS, this._renderTileMeshes, this._logTilesPetitions, this._tilesStoppedRendering);
    }
  }
   else {
    this._toVisit.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
    for (i0 = 0; i0 < firstLevelTilesCount; i0++) {
      $add_3(this._toVisit, dynamicCast($get_0(this._firstLevelTiles, i0), 23));
    }
    while (this._toVisit.array.length != 0) {
      this._toVisitInNextIteration.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
      toVisitSize = this._toVisit.array.length;
      for (i1 = 0; i1 < toVisitSize; i1++) {
        tile = dynamicCast($get_0(this._toVisit, i1), 23);
        $render_5(tile, rc, this._glState, this._toVisitInNextIteration, cameraFrustumInModelCoordinates, this._statistics, this._verticalExaggeration, layerTilesRenderParameters, this._texturizer, this._tilesRenderParameters, this._lastSplitTimer, this._elevationDataProvider, this._tessellator, this._layerSet, this._renderedSector, this._firstRender, this._tileDownloadPriority, texWidthSquared, texHeightSquared, nowInMS, this._renderTileMeshes, this._logTilesPetitions, this._tilesStoppedRendering);
      }
      this._toVisit.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
      toVisitInNextIterationSize = this._toVisitInNextIteration.array.length;
      for (i_0 = 0; i_0 < toVisitInNextIterationSize; i_0++) {
        $add_3(this._toVisit, dynamicCast($get_0(this._toVisitInNextIteration, i_0), 23));
      }
    }
  }
  this._firstRender = false;
  this._showStatistics && $log_3(this._statistics, rc._logger);
  previousLastVisibleSector = this._lastVisibleSector;
  this._lastVisibleSector = $updateVisibleSector(this._statistics, this._lastVisibleSector);
  if (previousLastVisibleSector != this._lastVisibleSector) {
    if (this._lastVisibleSector) {
      visibleSectorListenersCount = this._visibleSectorListeners.array.length;
      for (i_0 = 0; i_0 < visibleSectorListenersCount; i_0++) {
        entry = dynamicCast($get_0(this._visibleSectorListeners, i_0), 145);
        $tryToNotifyListener(entry, this._lastVisibleSector, rc);
      }
    }
  }
}
;
_.setChangedRendererInfoListener = function setChangedRendererInfoListener_1(changedInfoListener, rendererIdentifier){
  !!this._changedInfoListener && $logWarning(_instance_3, 'Changed Renderer Info Listener of PlanetRenderer already set', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  this._rendererIdentifier = rendererIdentifier;
  this._changedInfoListener = changedInfoListener;
  !!this._changedInfoListener && this._changedInfoListener.changedRendererInfo(rendererIdentifier, $getInfo_0(this._layerSet));
}
;
_.start_2 = function start_10(rc){
  this._firstRender = true;
}
;
_.stop_1 = function stop_9(rc){
  this._firstRender = false;
}
;
_._allFirstLevelTilesAreTextureSolved = false;
_._firstLevelTilesJustCreated = false;
_._firstRender = false;
_._layerTilesRenderParametersDirty = false;
_._logTilesPetitions = false;
_._recreateTilesPending = false;
_._renderTileMeshes = false;
_._showStatistics = false;
_._tileDownloadPriority = {l:0, m:0, h:0};
_._verticalExaggeration = 0;
var Lorg_glob3_mobile_generated_PlanetRenderer_2_classLit = createForClass('org.glob3.mobile.generated', 'PlanetRenderer', 308);
function $compare(i_0, j){
  var columnI, columnJ, rowI, rowJ;
  rowI = i_0._row;
  rowJ = j._row;
  if (rowI < rowJ) {
    return -1;
  }
  if (rowI > rowJ) {
    return 1;
  }
  columnI = i_0._column;
  columnJ = j._column;
  if (columnI < columnJ) {
    return -1;
  }
  if (columnI > columnJ) {
    return 1;
  }
  return 0;
}

function PlanetRenderer$1(){
}

defineClass(309, 1, {}, PlanetRenderer$1);
_.compare = function compare_3(i_0, j){
  return $compare(dynamicCast(i_0, 23), dynamicCast(j, 23));
}
;
var Lorg_glob3_mobile_generated_PlanetRenderer$1_2_classLit = createForClass('org.glob3.mobile.generated', 'PlanetRenderer/1', 309);
function $create_4(this$static){
  var geoVectorLayer, geoVectorLayersSize, i_0, i0, layerSet, planetRenderer, layerSet_0;
  layerSet = (!this$static._layerSet && (this$static._layerSet = (layerSet_0 = new LayerSet , $addLayer(layerSet_0, newOSM(new TimeInterval({l:4114432, m:617, h:0}))) , layerSet_0)) , this$static._layerSet);
  geoVectorLayersSize = this$static._geoVectorLayers.array.length;
  for (i0 = 0; i0 < geoVectorLayersSize; i0++) {
    geoVectorLayer = throwClassCastExceptionUnlessNull($get_0(this$static._geoVectorLayers, i0));
    $addLayer(layerSet, geoVectorLayer);
  }
  planetRenderer = new PlanetRenderer((!this$static._tileTessellator && (this$static._tileTessellator = new PlanetTileTessellator($getRenderedSector(this$static))) , this$static._tileTessellator), this$static._elevationDataProvider, (this$static._verticalExaggeration <= 0 && (this$static._verticalExaggeration = 1) , this$static._verticalExaggeration), (!this$static._texturizer && (this$static._texturizer = new DefaultTileTexturizer(new DefaultChessCanvasImageBuilder(new Color(0, 0, 0, 1), new Color(1, 1, 1, 1)))) , this$static._texturizer), layerSet, (!this$static._parameters && (this$static._parameters = new TilesRenderParameters(this$static._renderDebug, this$static._useTilesSplitBudget, this$static._forceFirstLevelTilesRenderOnStart, this$static._incrementalTileQuality, this$static._quality)) , this$static._parameters), this$static._showStatistics, this$static._tileDownloadPriority, $getRenderedSector(this$static), this$static._renderTileMeshes, this$static._logTilesPetitions, this$static._changedInfoListener, this$static._touchEventTypeOfTerrainTouchListener);
  for (i_0 = 0; i_0 < (!this$static._visibleSectorListeners && (this$static._visibleSectorListeners = new ArrayList) , this$static._visibleSectorListeners).array.length; i_0++) {
    $addVisibleSectorListener(planetRenderer, (throwClassCastExceptionUnlessNull($get_0((!this$static._visibleSectorListeners && (this$static._visibleSectorListeners = new ArrayList) , this$static._visibleSectorListeners), i_0)) , new TimeInterval(dynamicCast($get_0((!this$static._stabilizationMilliSeconds && (this$static._stabilizationMilliSeconds = new ArrayList) , this$static._stabilizationMilliSeconds), i_0), 46).value_0)));
  }
  this$static._parameters = null;
  this$static._layerSet = null;
  this$static._texturizer = null;
  this$static._tileTessellator = null;
  this$static._visibleSectorListeners = null;
  this$static._visibleSectorListeners = null;
  this$static._stabilizationMilliSeconds = null;
  this$static._stabilizationMilliSeconds = null;
  this$static._elevationDataProvider = null;
  this$static._renderedSector = null;
  this$static._geoVectorLayers.array = initDim(Ljava_lang_Object_2_classLit, $intern_4, 1, 0, 3, 1);
  return planetRenderer;
}

function $getRenderedSector(this$static){
  if (!this$static._renderedSector) {
    return $clinit_Sector() , $clinit_Sector() , FULL_SPHERE;
  }
  return this$static._renderedSector;
}

function $setLayerSet(this$static, layerSet){
  if (this$static._layerSet) {
    $logError(_instance_3, 'LOGIC ERROR: _layerSet already initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  this$static._layerSet = layerSet;
}

function $setRenderedSector(this$static, sector){
  if (this$static._renderedSector) {
    $logError(_instance_3, 'LOGIC ERROR: _renderedSector already initialized', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  this$static._renderedSector = new Sector_0(sector);
}

function PlanetRendererBuilder(){
  this._geoVectorLayers = new ArrayList;
  this._showStatistics = false;
  this._renderDebug = false;
  this._useTilesSplitBudget = true;
  this._forceFirstLevelTilesRenderOnStart = true;
  this._incrementalTileQuality = false;
  this._quality = ($clinit_Quality() , QUALITY_LOW);
  this._parameters = null;
  this._layerSet = null;
  this._texturizer = null;
  this._tileTessellator = null;
  this._visibleSectorListeners = null;
  this._stabilizationMilliSeconds = null;
  this._tileDownloadPriority = {l:10000, m:0, h:0};
  this._elevationDataProvider = null;
  this._verticalExaggeration = 0;
  this._renderedSector = null;
  this._renderTileMeshes = true;
  this._logTilesPetitions = false;
  this._changedInfoListener = null;
  this._touchEventTypeOfTerrainTouchListener = ($clinit_TouchEventType() , LongPress);
}

defineClass(115, 1, {}, PlanetRendererBuilder);
_._forceFirstLevelTilesRenderOnStart = false;
_._incrementalTileQuality = false;
_._logTilesPetitions = false;
_._renderDebug = false;
_._renderTileMeshes = false;
_._showStatistics = false;
_._tileDownloadPriority = {l:0, m:0, h:0};
_._useTilesSplitBudget = false;
_._verticalExaggeration = 0;
var Lorg_glob3_mobile_generated_PlanetRendererBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'PlanetRendererBuilder', 115);
function $getTextCoord(tile, position){
  return $getTextCoord_0(tile, position._latitude, position._longitude);
}

defineClass(453, 1, {});
var Lorg_glob3_mobile_generated_TileTessellator_2_classLit = createForClass('org.glob3.mobile.generated', 'TileTessellator', 453);
function $createEastSkirt(meshSector, meshResolution, skirtHeight, vertices, indices, textCoords){
  var firstSkirtVertex, g, j, rx, ry, skirtIndex, southEastCorner, surfaceIndex, uv, y_0;
  firstSkirtVertex = narrow_short(~~(vertices._values._size / 3));
  rx = narrow_short(meshResolution._x);
  ry = narrow_short(meshResolution._y);
  southEastCorner = narrow_short(rx * ry - 1);
  skirtIndex = firstSkirtVertex;
  surfaceIndex = southEastCorner;
  for (j = ry - 1; j >= 0; j--) {
    y_0 = j / (ry - 1);
    g = new Geodetic2D(linearInterpolation(meshSector._lower._latitude, meshSector._upper._latitude, 1 - y_0), linearInterpolation(meshSector._lower._longitude, meshSector._upper._longitude, 1));
    $add_11(vertices, g._latitude, g._longitude, skirtHeight);
    uv = $getVector2D(textCoords, surfaceIndex);
    $push_back(textCoords._values, uv._x);
    $push_back(textCoords._values, uv._y);
    $push_back_0(indices._values, surfaceIndex);
    $push_back_0(indices._values, skirtIndex);
    ++skirtIndex;
    surfaceIndex = surfaceIndex - rx;
  }
  $add_16(indices, narrow_short(surfaceIndex + rx));
  $add_16(indices, narrow_short(surfaceIndex + rx));
}

function $createNorthSkirt(meshSector, meshResolution, skirtHeight, vertices, indices, textCoords){
  var firstSkirtVertex, g, i_0, northEastCorner, rx, skirtIndex, surfaceIndex, uv, x_0;
  firstSkirtVertex = narrow_short(~~(vertices._values._size / 3));
  rx = narrow_short(meshResolution._x);
  northEastCorner = narrow_short(rx - 1);
  skirtIndex = firstSkirtVertex;
  surfaceIndex = northEastCorner;
  $push_back_0(indices._values, northEastCorner);
  for (i_0 = rx - 1; i_0 >= 0; i_0--) {
    x_0 = i_0 / (rx - 1);
    g = new Geodetic2D(linearInterpolation(meshSector._lower._latitude, meshSector._upper._latitude, 1), linearInterpolation(meshSector._lower._longitude, meshSector._upper._longitude, x_0));
    $add_11(vertices, g._latitude, g._longitude, skirtHeight);
    uv = $getVector2D(textCoords, surfaceIndex);
    $push_back(textCoords._values, uv._x);
    $push_back(textCoords._values, uv._y);
    $push_back_0(indices._values, surfaceIndex);
    $push_back_0(indices._values, skirtIndex);
    ++skirtIndex;
    surfaceIndex = narrow_short(surfaceIndex - 1);
  }
  $add_16(indices, narrow_short(surfaceIndex + 1));
  $add_16(indices, narrow_short(surfaceIndex + 1));
}

function $createSouthSkirt(meshSector, meshResolution, skirtHeight, vertices, indices, textCoords){
  var firstSkirtVertex, g, i_0, rx, ry, skirtIndex, southWestCorner, surfaceIndex, uv, x_0;
  firstSkirtVertex = narrow_short(~~(vertices._values._size / 3));
  rx = narrow_short(meshResolution._x);
  ry = narrow_short(meshResolution._y);
  southWestCorner = narrow_short(rx * (ry - 1));
  skirtIndex = firstSkirtVertex;
  surfaceIndex = southWestCorner;
  $push_back_0(indices._values, southWestCorner);
  for (i_0 = 0; i_0 < rx; i_0++) {
    x_0 = i_0 / (rx - 1);
    g = new Geodetic2D(linearInterpolation(meshSector._lower._latitude, meshSector._upper._latitude, 0), linearInterpolation(meshSector._lower._longitude, meshSector._upper._longitude, x_0));
    $add_11(vertices, g._latitude, g._longitude, skirtHeight);
    uv = $getVector2D(textCoords, surfaceIndex);
    $add_8(textCoords, uv._x, uv._y);
    $push_back_0(indices._values, surfaceIndex);
    $add_16(indices, skirtIndex++);
    surfaceIndex = narrow_short(surfaceIndex + 1);
  }
  $add_16(indices, narrow_short(surfaceIndex - 1));
  $add_16(indices, narrow_short(surfaceIndex - 1));
}

function $createSurface(tileSector, meshSector, meshResolution, mercator, vertices, indices, textCoords, data_0){
  var i_0, j, j0, jTimesResolution, m_u, m_v, mercatorDeltaGlobalV, mercatorGlobalV, mercatorLowerGlobalV, mercatorUpperGlobalV, minElevation, position, rx, ry, u, uv, v;
  rx = meshResolution._x;
  ry = meshResolution._y;
  mercatorLowerGlobalV = getMercatorV(tileSector._lower._latitude);
  mercatorUpperGlobalV = getMercatorV(tileSector._upper._latitude);
  mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
  for (j0 = 0; j0 < ry; j0++) {
    v = j0 / (ry - 1);
    for (i_0 = 0; i_0 < rx; i_0++) {
      u = i_0 / (rx - 1);
      position = new Geodetic2D(linearInterpolation(meshSector._lower._latitude, meshSector._upper._latitude, 1 - v), linearInterpolation(meshSector._lower._longitude, meshSector._upper._longitude, u));
      $add_11(vertices, position._latitude, position._longitude, 0);
      if (mercator) {
        m_u = $getUCoordinate(tileSector, position._longitude);
        mercatorGlobalV = getMercatorV(position._latitude);
        m_v = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
        $add_8(textCoords, m_u, m_v);
      }
       else {
        uv = $getUVCoordinates(tileSector, position._latitude, position._longitude);
        $push_back(textCoords._values, uv._x);
        $push_back(textCoords._values, uv._y);
      }
    }
  }
  minElevation = 0;
  data_0._averageHeight = 0 / (rx * ry);
  for (j = 0; j < ry - 1; j++) {
    jTimesResolution = narrow_short(j * rx);
    j > 0 && $push_back_0(indices._values, jTimesResolution);
    for (i_0 = 0; i_0 < rx; i_0++) {
      $add_16(indices, narrow_short(jTimesResolution + i_0));
      $add_16(indices, narrow_short(jTimesResolution + i_0 + rx));
    }
    $add_16(indices, narrow_short(jTimesResolution + 2 * rx - 1));
  }
  return minElevation;
}

function $createTextCoords(tile){
  var data_0;
  data_0 = tile._tessellatorData;
  if (!data_0 || !data_0._textCoords) {
    $logError(_instance_3, 'Logic error on PlanetTileTessellator::createTextCoord', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return null;
  }
  return $create(data_0._textCoords);
}

function $createTileDebugMesh(this$static, planet, rawResolution, tile){
  var color_0, i_0, i0, indices, j, j0, nw, offset, posS, resolutionXMinus1, resolutionYMinus1, result, sector, sw, vertices;
  sector = $getRenderedSectorForTile(this$static, tile);
  resolutionXMinus1 = rawResolution._x - 1;
  resolutionYMinus1 = rawResolution._y - 1;
  posS = 0;
  sw = $toCartesian_0(planet, sector._lower);
  nw = $toCartesian_0(planet, new Geodetic2D(sector._upper._latitude, sector._lower._longitude));
  offset = $length_1(new Vector3D(nw._x - sw._x, nw._y - sw._y, nw._z - sw._z)) * 0.001;
  vertices = new FloatBufferBuilderFromGeodetic(planet, sector._center);
  indices = new ShortBufferBuilder;
  for (j0 = 0; j0 < resolutionYMinus1; j0++) {
    $add_12(vertices, $getInnerPoint(sector, 0, j0 / resolutionYMinus1), offset);
    $add_16(indices, posS++);
  }
  for (i0 = 0; i0 < resolutionXMinus1; i0++) {
    $add_12(vertices, $getInnerPoint(sector, i0 / resolutionXMinus1, 1), offset);
    $add_16(indices, posS++);
  }
  for (j = resolutionYMinus1; j > 0; j--) {
    $add_12(vertices, $getInnerPoint(sector, 1, j / resolutionYMinus1), offset);
    $add_16(indices, posS++);
  }
  for (i_0 = resolutionXMinus1; i_0 > 0; i_0--) {
    $add_12(vertices, $getInnerPoint(sector, i_0 / resolutionXMinus1, 0), offset);
    $add_16(indices, posS++);
  }
  color_0 = new Color(1, 0, 0, 1);
  result = new IndexedMesh_0(_lineLoop, new Vector3D(vertices._cx, vertices._cy, vertices._cz), new FloatBuffer_WebGL_0(vertices._values._array, vertices._values._size), new ShortBuffer_WebGL(indices._values._array, indices._values._size), color_0);
  return result;
}

function $createTileMesh(this$static, planet, rawResolution, tile, data_0){
  var absoluteSkirtHeight, indices, indicesB, meshResolution, meshSector, minElevation, relativeSkirtHeight, result, textCoords, tileSector, vertices, verticesB, sector, latRatio, lonRatio, resX, resY;
  tileSector = tile._sector;
  meshSector = $getRenderedSectorForTile(this$static, tile);
  meshResolution = (sector = tile._sector , latRatio = sector._deltaLatitude._degrees / meshSector._deltaLatitude._degrees , lonRatio = sector._deltaLongitude._degrees / meshSector._deltaLongitude._degrees , resX = round_int(ceil_0(rawResolution._x / lonRatio)) , resX < 2 && (resX = 2) , resY = round_int(ceil_0(rawResolution._y / latRatio)) , resY < 2 && (resY = 2) , new Vector2I(resX, resY));
  vertices = new FloatBufferBuilderFromGeodetic(planet, meshSector._center);
  indices = new ShortBufferBuilder;
  textCoords = new FloatBufferBuilderFromCartesian2D;
  minElevation = $createSurface(tileSector, meshSector, meshResolution, tile._mercator, vertices, indices, textCoords, data_0);
  if (this$static._skirted) {
    relativeSkirtHeight = minElevation - skirtDepthForSector(planet, tileSector);
    absoluteSkirtHeight = 0;
    !!this$static._renderedSector && (absoluteSkirtHeight = -skirtDepthForSector(planet, this$static._renderedSector));
    $createEastSkirt(meshSector, meshResolution, $needsEastSkirt(this$static, tileSector)?relativeSkirtHeight:absoluteSkirtHeight, vertices, indices, textCoords);
    $createNorthSkirt(meshSector, meshResolution, $needsNorthSkirt(this$static, tileSector)?relativeSkirtHeight:absoluteSkirtHeight, vertices, indices, textCoords);
    $createWestSkirt(meshSector, meshResolution, $needsWestSkirt(this$static, tileSector)?relativeSkirtHeight:absoluteSkirtHeight, vertices, indices, textCoords);
    $createSouthSkirt(meshSector, meshResolution, $needsSouthSkirt(this$static, tileSector)?relativeSkirtHeight:absoluteSkirtHeight, vertices, indices, textCoords);
  }
  $setTessellatorData(tile, new PlanetTileTessellatorData(textCoords));
  verticesB = new FloatBuffer_WebGL_0(vertices._values._array, vertices._values._size);
  indicesB = new ShortBuffer_WebGL(indices._values._array, indices._values._size);
  result = new IndexedGeometryMesh(_triangleStrip, new Vector3D(vertices._cx, vertices._cy, vertices._cz), verticesB, indicesB);
  return result;
}

function $createWestSkirt(meshSector, meshResolution, skirtHeight, vertices, indices, textCoords){
  var firstSkirtVertex, g, j, rx, ry, skirtIndex, surfaceIndex, uv, y_0;
  firstSkirtVertex = narrow_short(~~(vertices._values._size / 3));
  rx = narrow_short(meshResolution._x);
  ry = narrow_short(meshResolution._y);
  skirtIndex = firstSkirtVertex;
  surfaceIndex = 0;
  $push_back_0(indices._values, surfaceIndex);
  for (j = 0; j < ry; j++) {
    y_0 = j / (ry - 1);
    g = new Geodetic2D(linearInterpolation(meshSector._lower._latitude, meshSector._upper._latitude, 1 - y_0), linearInterpolation(meshSector._lower._longitude, meshSector._upper._longitude, 0));
    $add_11(vertices, g._latitude, g._longitude, skirtHeight);
    uv = $getVector2D(textCoords, surfaceIndex);
    $push_back(textCoords._values, uv._x);
    $push_back(textCoords._values, uv._y);
    $push_back_0(indices._values, surfaceIndex);
    $push_back_0(indices._values, skirtIndex);
    ++skirtIndex;
    surfaceIndex = surfaceIndex + rx;
  }
  $add_16(indices, narrow_short(surfaceIndex - rx));
  $add_16(indices, narrow_short(surfaceIndex - rx));
}

function $getRenderedSectorForTile(this$static, tile){
  if (!this$static._renderedSector) {
    return tile._sector;
  }
  return $intersection(tile._sector, this$static._renderedSector);
}

function $getTextCoord_0(tile, latitude, longitude){
  var deltaGlobalV, globalV, linearUV, localV, lowerGlobalV, sector, upperGlobalV;
  sector = tile._sector;
  linearUV = new Vector2F((longitude._radians - sector._lower._longitude._radians) / sector._deltaLongitude._radians, (sector._upper._latitude._radians - latitude._radians) / sector._deltaLatitude._radians);
  if (!tile._mercator) {
    return linearUV;
  }
  lowerGlobalV = getMercatorV(sector._lower._latitude);
  upperGlobalV = getMercatorV(sector._upper._latitude);
  deltaGlobalV = lowerGlobalV - upperGlobalV;
  globalV = getMercatorV(latitude);
  localV = (globalV - upperGlobalV) / deltaGlobalV;
  return new Vector2F(linearUV._x, localV);
}

function $needsEastSkirt(this$static, tileSector){
  if (!this$static._renderedSector) {
    return true;
  }
  return $greaterThan(this$static._renderedSector._upper._longitude, tileSector._upper._longitude);
}

function $needsNorthSkirt(this$static, tileSector){
  if (!this$static._renderedSector) {
    return true;
  }
  return $greaterThan(this$static._renderedSector._upper._latitude, tileSector._upper._latitude);
}

function $needsSouthSkirt(this$static, tileSector){
  if (!this$static._renderedSector) {
    return true;
  }
  return $lowerThan(this$static._renderedSector._lower._latitude, tileSector._lower._latitude);
}

function $needsWestSkirt(this$static, tileSector){
  if (!this$static._renderedSector) {
    return true;
  }
  return $lowerThan(this$static._renderedSector._lower._longitude, tileSector._lower._longitude);
}

function PlanetTileTessellator(sector){
  this._skirted = true;
  this._renderedSector = $isEquals_3(sector, ($clinit_Sector() , $clinit_Sector() , FULL_SPHERE))?null:new Sector_0(sector);
}

function skirtDepthForSector(planet, sector){
  var diagonalLength, nw, se, sideLength;
  se = $toCartesian_0(planet, new Geodetic2D(sector._lower._latitude, sector._upper._longitude));
  nw = $toCartesian_0(planet, new Geodetic2D(sector._upper._latitude, sector._lower._longitude));
  diagonalLength = $length_1(new Vector3D(nw._x - se._x, nw._y - se._y, nw._z - se._z));
  sideLength = diagonalLength * 0.70710678118;
  return sideLength / 20;
}

defineClass(359, 453, {}, PlanetTileTessellator);
_._skirted = false;
var Lorg_glob3_mobile_generated_PlanetTileTessellator_2_classLit = createForClass('org.glob3.mobile.generated', 'PlanetTileTessellator', 359);
function PlanetTileTessellatorData(textCoords){
  this._textCoords = textCoords;
}

defineClass(374, 1, {}, PlanetTileTessellatorData);
var Lorg_glob3_mobile_generated_PlanetTileTessellatorData_2_classLit = createForClass('org.glob3.mobile.generated', 'PlanetTileTessellatorData', 374);
function ProjectionGLFeature(projection){
  GLCameraGroupFeature.call(this, projection, 4);
}

defineClass(120, 50, {50:1, 27:1, 120:1, 9:1}, ProjectionGLFeature);
_.dispose = function dispose_48(){
  $_release(this._matrixHolder);
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_ProjectionGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'ProjectionGLFeature', 120);
function $clinit_Quality(){
  $clinit_Quality = emptyMethod;
  QUALITY_LOW = new Quality('QUALITY_LOW', 0);
  QUALITY_MEDIUM = new Quality('QUALITY_MEDIUM', 1);
  QUALITY_HIGH = new Quality('QUALITY_HIGH', 2);
}

function Quality(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_8(){
  $clinit_Quality();
  return initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_Quality_2_classLit, 1), $intern_4, 100, 0, [QUALITY_LOW, QUALITY_MEDIUM, QUALITY_HIGH]);
}

defineClass(100, 15, {3:1, 26:1, 15:1, 100:1}, Quality);
var QUALITY_HIGH, QUALITY_LOW, QUALITY_MEDIUM;
var Lorg_glob3_mobile_generated_Quality_2_classLit = createForEnum('org.glob3.mobile.generated', 'Quality', 100, values_8);
function $dispose_10(this$static){
  $requestFinish(this$static._rasterLayerTileImageProvider, this$static._tileId);
  this$static._deleteListener && !!this$static._listener && this$static._listener.dispose();
  releaseContribution(this$static._contribution);
}

function $onCancel(this$static){
  this$static._listener.imageCreationCanceled(this$static._tileId);
}

function $onDownload(this$static, url_0, image){
  var contribution;
  contribution = this$static._contribution;
  this$static._contribution = null;
  this$static._listener.imageCreated_0(this$static._tileId, image, url_0._path, contribution);
}

function $onError(this$static, url_0){
  this$static._listener.imageCreationError(this$static._tileId, 'Download error - ' + url_0._path);
}

function RLTIP_ImageDownloadListener(rasterLayerTileImageProvider, tileId, contribution, listener, deleteListener){
  this._rasterLayerTileImageProvider = rasterLayerTileImageProvider;
  this._tileId = tileId;
  this._contribution = contribution;
  this._listener = listener;
  this._deleteListener = deleteListener;
}

defineClass(407, 487, {}, RLTIP_ImageDownloadListener);
_._deleteListener = false;
var Lorg_glob3_mobile_generated_RLTIP_1ImageDownloadListener_2_classLit = createForClass('org.glob3.mobile.generated', 'RLTIP_ImageDownloadListener', 407);
function $requestFinish(this$static, tileId){
  $removeStringValue(this$static._requestsIdsPerTile, tileId);
}

function RasterLayerTileImageProvider(layer, downloader){
  TileImageProvider.call(this);
  this._requestsIdsPerTile = new HashMap;
  this._layer = layer;
  this._downloader = downloader;
}

defineClass(338, 49, $intern_41, RasterLayerTileImageProvider);
_.cancel_0 = function cancel_3(tileId){
  var requestId;
  requestId = dynamicCast($removeStringValue(this._requestsIdsPerTile, tileId), 46);
  !!requestId && $cancelRequest(this._downloader, requestId.value_0);
}
;
_.contribution = function contribution_1(tile){
  return !this._layer?null:this._layer.rawContribution(tile);
}
;
_.create_0 = function create_4(tile, contribution, resolution, tileDownloadPriority, logDownloadActivity, listener, deleteListener, frameTasksExecutor){
  var requestId, tileId;
  tileId = tile._id;
  requestId = $requestImage(this._layer, tile, this._downloader, tileDownloadPriority, logDownloadActivity, new RLTIP_ImageDownloadListener(this, tileId, contribution, listener, deleteListener));
  gte_0(requestId, {l:0, m:0, h:0}) && $putStringValue(this._requestsIdsPerTile, tileId, valueOf_0(requestId));
}
;
_.dispose = function dispose_49(){
  var entry, entry$iterator;
  for (entry$iterator = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this._requestsIdsPerTile)).this$01); $hasNext(entry$iterator);) {
    entry = (checkStructuralChange(entry$iterator.this$01, entry$iterator) , checkCriticalElement($hasNext(entry$iterator)) , entry$iterator.last = entry$iterator.current , dynamicCast(entry$iterator.current.next_0(), 22));
    $cancelRequest(this._downloader, dynamicCast(entry.getValue(), 46).value_0);
  }
  $dispose_0(this);
}
;
var Lorg_glob3_mobile_generated_RasterLayerTileImageProvider_2_classLit = createForClass('org.glob3.mobile.generated', 'RasterLayerTileImageProvider', 338);
function RecreateTilesTask(planetRenderer){
  this._planetRenderer = planetRenderer;
}

defineClass(363, 457, {}, RecreateTilesTask);
var Lorg_glob3_mobile_generated_RecreateTilesTask_2_classLit = createForClass('org.glob3.mobile.generated', 'RecreateTilesTask', 363);
function $id(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'RectangleF|';
  $addDouble(isb, this$static._x);
  isb._string += '|';
  $addDouble(isb, this$static._y);
  isb._string += '|';
  $addDouble(isb, this$static._width);
  isb._string += '|';
  $addDouble(isb, this$static._height);
  s = isb._string;
  return s;
}

function RectangleF(x_0, y_0, width_0, height){
  this._x = x_0;
  this._y = y_0;
  this._width = width_0;
  this._height = height;
  (this._width < 0 || this._height < 0) && $logError(_instance_3, 'Invalid rectangle extent', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
}

function calculateInnerRectangleFromSector(wholeSectorWidth, wholeSectorHeight, wholeSector, innerSector){
  var deltaHeightFactor, deltaWidthFactor, heightFactor, heightFactor2, lowerUV, upperUV, widthFactor, widthFactor2;
  if (wholeSector._lower._latitude._degrees != wholeSector._lower._latitude._degrees || innerSector._lower._latitude._degrees != innerSector._lower._latitude._degrees) {
    $logError(_instance_3, 'Testing this case: view code', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
  }
  if ($isEquals_0(wholeSector._lower, innerSector._lower) && $isEquals_0(wholeSector._upper, innerSector._upper)) {
    return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
  }
  widthFactor2 = $div_0(innerSector._deltaLongitude, wholeSector._deltaLongitude);
  heightFactor2 = $div_0(innerSector._deltaLatitude, wholeSector._deltaLatitude);
  lowerUV = $getUVCoordinates_0(wholeSector, new Geodetic2D(innerSector._upper._latitude, innerSector._lower._longitude));
  upperUV = $getUVCoordinates_0(wholeSector, new Geodetic2D(innerSector._lower._latitude, innerSector._upper._longitude));
  widthFactor = upperUV._x - lowerUV._x;
  heightFactor = upperUV._y - lowerUV._y;
  deltaWidthFactor = widthFactor - widthFactor2;
  deltaHeightFactor = heightFactor - heightFactor2;
  (deltaWidthFactor < -1.0E-5 || deltaWidthFactor > $intern_44 || deltaHeightFactor < -1.0E-5 || deltaHeightFactor > $intern_44) && $logWarning(_instance_3, 'Testing this case (view code): factors are diferents: %f and %f', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [($clinit_Double() , new Double(widthFactor - widthFactor2)), new Double(heightFactor - heightFactor2)]));
  return new RectangleF(lowerUV._x * wholeSectorWidth, lowerUV._y * wholeSectorHeight, widthFactor * wholeSectorWidth, heightFactor * wholeSectorHeight);
}

function fullContains(outerWidth_0, outerHeight_0, innerX, innerY, innerWidth_0, innerHeight_0){
  if (!(innerX >= 0 && innerX <= outerWidth_0)) {
    return false;
  }
  if (!(innerX + innerWidth_0 >= 0 && innerX + innerWidth_0 <= outerWidth_0)) {
    return false;
  }
  if (!(innerY >= 0 && innerY <= outerHeight_0)) {
    return false;
  }
  if (!(innerY + innerHeight_0 >= 0 && innerY + innerHeight_0 <= outerHeight_0)) {
    return false;
  }
  return true;
}

defineClass(158, 1, {}, RectangleF);
_._height = 0;
_._width = 0;
_._x = 0;
_._y = 0;
var Lorg_glob3_mobile_generated_RectangleF_2_classLit = createForClass('org.glob3.mobile.generated', 'RectangleF', 158);
function $clinit_RenderState(){
  $clinit_RenderState = emptyMethod;
  READY = new RenderState_1(0);
  BUSY = new RenderState_1(1);
}

function RenderState(errors){
  $clinit_RenderState();
  this._type = 2;
  this._errors = errors;
}

function RenderState_0(that){
  $clinit_RenderState();
  this._type = that._type;
  this._errors = that._errors;
}

function RenderState_1(type_0){
  this._type = type_0;
  this._errors = null;
}

function error_0(error){
  $clinit_RenderState();
  var errors;
  errors = new ArrayList;
  setCheck(errors.array, errors.array.length, error);
  return new RenderState(errors);
}

defineClass(66, 1, {}, RenderState, RenderState_0, RenderState_1);
_._type = 0;
var BUSY, READY;
var Lorg_glob3_mobile_generated_RenderState_2_classLit = createForClass('org.glob3.mobile.generated', 'RenderState', 66);
function RenderedSectorCameraConstrainer(planetRenderer, maxHeight){
  this._planetRenderer = planetRenderer;
  this._maxHeight = maxHeight;
}

defineClass(310, 1, {416:1}, RenderedSectorCameraConstrainer);
_.onCameraChange = function onCameraChange(planet, previousCamera, nextCamera){
  var center, isValidHeight, isValidPosition, position, sector;
  if (this._planetRenderer) {
    sector = this._planetRenderer._renderedSector;
    position = (!nextCamera._geodeticPosition && (nextCamera._geodeticPosition = new Geodetic3D_1($toGeodetic3D(nextCamera._planet, $asVector3D(nextCamera._position)))) , nextCamera._geodeticPosition);
    isValidHeight = position._height <= this._maxHeight;
    if (!sector) {
      isValidHeight || $setGeodeticPosition(nextCamera, new Geodetic3D(position._latitude, position._longitude, this._maxHeight));
    }
     else {
      center = $_getGeodeticCenterOfView(nextCamera);
      isValidPosition = $contains_1(sector, center._latitude, center._longitude);
      isValidPosition?isValidHeight || $setGeodeticPosition(nextCamera, new Geodetic3D(position._latitude, position._longitude, this._maxHeight)):$copyFrom(nextCamera, previousCamera);
    }
  }
  return true;
}
;
_._maxHeight = 0;
var Lorg_glob3_mobile_generated_RenderedSectorCameraConstrainer_2_classLit = createForClass('org.glob3.mobile.generated', 'RenderedSectorCameraConstrainer', 310);
function RotateWithAxisEffect(axis_0, angle){
  this._force = 1;
  this._friction = 0.975;
  this._axis = new Vector3D_0(axis_0);
  this._degrees = angle._degrees;
}

defineClass(409, 408, {}, RotateWithAxisEffect);
_.cancel = function cancel_4(when){
}
;
_.dispose = function dispose_50(){
}
;
_.doStep = function doStep_4(rc, when){
  this._force *= this._friction;
  $rotateWithAxis(rc._nextCamera, this._axis, fromDegrees(this._degrees * this._force));
}
;
_.start_1 = function start_11(rc, when){
}
;
_.stop_0 = function stop_10(rc, when){
  $rotateWithAxis(rc._nextCamera, this._axis, fromDegrees(this._degrees * this._force));
}
;
_._degrees = 0;
var Lorg_glob3_mobile_generated_RotateWithAxisEffect_2_classLit = createForClass('org.glob3.mobile.generated', 'RotateWithAxisEffect', 409);
function $clinit_Sector(){
  $clinit_Sector = emptyMethod;
  FULL_SPHERE = fromDegrees_2(-90, -180, 90, 180);
  NAN_SECTOR = fromDegrees_2(NaN, NaN, NaN, NaN);
}

function $contains_1(this$static, latitude, longitude){
  return $isBetween(latitude, this$static._lower._latitude, this$static._upper._latitude) && $isBetween(longitude, this$static._lower._longitude, this$static._upper._longitude);
}

function $description_3(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += '(Sector ';
  $addString(isb, $description_0(this$static._lower));
  isb._string += ' - ';
  $addString(isb, $description_0(this$static._upper));
  isb._string += ')';
  s = isb._string;
  return s;
}

function $fullContains(this$static, that){
  return $contains_1(this$static, that._upper._latitude, that._upper._longitude) && $contains_1(this$static, that._lower._latitude, that._lower._longitude);
}

function $getAngularAreaInSquaredDegrees(this$static){
  return this$static._deltaLatitude._degrees * this$static._deltaLongitude._degrees;
}

function $getInnerPoint(this$static, u, v){
  return new Geodetic2D(linearInterpolation(this$static._lower._latitude, this$static._upper._latitude, 1 - v), linearInterpolation(this$static._lower._longitude, this$static._upper._longitude, u));
}

function $getNW(this$static){
  return new Geodetic2D(this$static._upper._latitude, this$static._lower._longitude);
}

function $getSE(this$static){
  return new Geodetic2D(this$static._lower._latitude, this$static._upper._longitude);
}

function $getUCoordinate(this$static, longitude){
  return (longitude._radians - this$static._lower._longitude._radians) / this$static._deltaLongitude._radians;
}

function $getUVCoordinates(this$static, latitude, longitude){
  return new Vector2D((longitude._radians - this$static._lower._longitude._radians) / this$static._deltaLongitude._radians, (this$static._upper._latitude._radians - latitude._radians) / this$static._deltaLatitude._radians);
}

function $getUVCoordinates_0(this$static, point){
  return $getUVCoordinates(this$static, point._latitude, point._longitude);
}

function $id_0(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += 'Sector|';
  $addDouble(isb, this$static._lower._latitude._degrees);
  isb._string += '|';
  $addDouble(isb, this$static._lower._longitude._degrees);
  isb._string += '|';
  $addDouble(isb, this$static._upper._latitude._degrees);
  isb._string += '|';
  $addDouble(isb, this$static._upper._longitude._degrees);
  isb._string += '|';
  s = isb._string;
  return s;
}

function $intersection(this$static, that){
  var low, lowLat, lowLon, up, upLat, upLon;
  lowLat = max_2(this$static._lower._latitude, that._lower._latitude);
  lowLon = max_2(this$static._lower._longitude, that._lower._longitude);
  upLat = min_2(this$static._upper._latitude, that._upper._latitude);
  upLon = min_2(this$static._upper._longitude, that._upper._longitude);
  if (lowLat._radians < upLat._radians && lowLon._radians < upLon._radians) {
    low = new Geodetic2D(lowLat, lowLon);
    up = new Geodetic2D(upLat, upLon);
    return new Sector(low, up);
  }
  return fromDegrees_2(0, 0, 0, 0);
}

function $isEquals_3(this$static, that){
  return $isEquals_0(this$static._lower, that._lower) && $isEquals_0(this$static._upper, that._upper);
}

function $touchesPoles(this$static){
  return this$static._upper._latitude._degrees >= 89.9 || this$static._lower._latitude._degrees <= -89.9;
}

function $touchesWith(this$static, that){
  if (this$static._upper._latitude._radians < that._lower._latitude._radians || this$static._lower._latitude._radians > that._upper._latitude._radians) {
    return false;
  }
  if (this$static._upper._longitude._radians < that._lower._longitude._radians || this$static._lower._longitude._radians > that._upper._longitude._radians) {
    return false;
  }
  return true;
}

function Sector(lower, upper){
  $clinit_Sector();
  this._lower = new Geodetic2D_0(lower);
  this._upper = new Geodetic2D_0(upper);
  this._deltaLatitude = new Angle_0($sub(upper._latitude, lower._latitude));
  this._deltaLongitude = new Angle_0($sub(upper._longitude, lower._longitude));
  this._center = new Geodetic2D(midAngle(lower._latitude, upper._latitude), midAngle(lower._longitude, upper._longitude));
  this._deltaRadiusInRadians = -1;
  this._normalizedCartesianCenter = null;
}

function Sector_0(sector){
  $clinit_Sector();
  var normalizedCartesianCenter;
  this._lower = new Geodetic2D_0(sector._lower);
  this._upper = new Geodetic2D_0(sector._upper);
  this._deltaLatitude = new Angle_0(sector._deltaLatitude);
  this._deltaLongitude = new Angle_0(sector._deltaLongitude);
  this._center = new Geodetic2D_0(sector._center);
  this._deltaRadiusInRadians = sector._deltaRadiusInRadians;
  if (!sector._normalizedCartesianCenter) {
    this._normalizedCartesianCenter = null;
  }
   else {
    normalizedCartesianCenter = sector._normalizedCartesianCenter;
    this._normalizedCartesianCenter = new Vector3D_0(normalizedCartesianCenter);
  }
}

function fromDegrees_2(minLat, minLon, maxLat, maxLon){
  var lower, upper;
  lower = new Geodetic2D(new Angle(minLat, minLat / 180 * $intern_37), new Angle(minLon, minLon / 180 * $intern_37));
  upper = new Geodetic2D(new Angle(maxLat, maxLat / 180 * $intern_37), new Angle(maxLon, maxLon / 180 * $intern_37));
  return new Sector(lower, upper);
}

defineClass(19, 1, {19:1}, Sector, Sector_0);
_.equals$ = function equals_18(obj){
  var other;
  if (this === obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  if (Lorg_glob3_mobile_generated_Sector_2_classLit != getClass__Ljava_lang_Class___devirtual$(obj)) {
    return false;
  }
  other = dynamicCast(obj, 19);
  if (!this._lower) {
    if (other._lower) {
      return false;
    }
  }
   else if (!$equals_2(this._lower, other._lower)) {
    return false;
  }
  if (!this._upper) {
    if (other._upper) {
      return false;
    }
  }
   else if (!$equals_2(this._upper, other._upper)) {
    return false;
  }
  return true;
}
;
_.hashCode$ = function hashCode_23(){
  var result;
  result = 31 + (!this._lower?0:$hashCode_0(this._lower));
  result = 31 * result + (!this._upper?0:$hashCode_0(this._upper));
  return result;
}
;
_.toString$ = function toString_36(){
  return $description_3(this);
}
;
_._deltaRadiusInRadians = 0;
var FULL_SPHERE, NAN_SECTOR;
var Lorg_glob3_mobile_generated_Sector_2_classLit = createForClass('org.glob3.mobile.generated', 'Sector', 19);
function $clinit_ShaderType(){
  $clinit_ShaderType = emptyMethod;
  VERTEX_SHADER = new ShaderType('VERTEX_SHADER', 0);
  FRAGMENT_SHADER = new ShaderType('FRAGMENT_SHADER', 1);
}

function ShaderType(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_9(){
  $clinit_ShaderType();
  return initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_ShaderType_2_classLit, 1), $intern_4, 132, 0, [VERTEX_SHADER, FRAGMENT_SHADER]);
}

defineClass(132, 15, {3:1, 26:1, 15:1, 132:1}, ShaderType);
var FRAGMENT_SHADER, VERTEX_SHADER;
var Lorg_glob3_mobile_generated_ShaderType_2_classLit = createForEnum('org.glob3.mobile.generated', 'ShaderType', 132, values_9);
function $add_16(this$static, value_0){
  $push_back_0(this$static._values, value_0);
}

function $description_4(this$static){
  var i_0, isb, s, v;
  isb = new StringBuilder_WebGL;
  isb._string += 'ShortBufferBuilder: ';
  for (i_0 = 0; i_0 < this$static._values._size; i_0++) {
    v = $get_4(this$static._values, i_0);
    isb._string += v;
    isb._string += ', ';
  }
  s = isb._string;
  return s;
}

function ShortBufferBuilder(){
  this._values = new ShortBufferBuilder$ShortArrayList;
}

defineClass(150, 1, {}, ShortBufferBuilder);
_.toString$ = function toString_37(){
  return $description_4(this);
}
;
var Lorg_glob3_mobile_generated_ShortBufferBuilder_2_classLit = createForClass('org.glob3.mobile.generated', 'ShortBufferBuilder', 150);
function $ensureCapacity_0(this$static, mincap){
  var newcap, olddata;
  if (mincap > this$static._array.length) {
    newcap = (this$static._array.length * 3 >> 1) + 1;
    olddata = this$static._array;
    this$static._array = initDim(S_classLit, $intern_29, 0, newcap < mincap?mincap:newcap, 7, 1);
    arraycopy(olddata, 0, this$static._array, 0, this$static._size);
  }
}

function $get_4(this$static, index_0){
  return this$static._array[index_0];
}

function $push_back_0(this$static, element){
  $ensureCapacity_0(this$static, this$static._size + 1);
  this$static._array[this$static._size++] = element;
}

function ShortBufferBuilder$ShortArrayList(){
  this._array = initDim(S_classLit, $intern_29, 0, 1024, 7, 1);
  this._size = 0;
}

defineClass(200, 1, {}, ShortBufferBuilder$ShortArrayList);
_._size = 0;
var Lorg_glob3_mobile_generated_ShortBufferBuilder$ShortArrayList_2_classLit = createForClass('org.glob3.mobile.generated', 'ShortBufferBuilder/ShortArrayList', 200);
function SimpleCameraConstrainer(){
}

defineClass(344, 1, {416:1}, SimpleCameraConstrainer);
_.onCameraChange = function onCameraChange_0(planet, previousCamera, nextCamera){
  var height, maxHeight, radii;
  radii = $maxAxis(planet._ellipsoid._radii);
  maxHeight = radii * 9;
  height = (!nextCamera._geodeticPosition && (nextCamera._geodeticPosition = new Geodetic3D_1($toGeodetic3D(nextCamera._planet, $asVector3D(nextCamera._position)))) , nextCamera._geodeticPosition)._height;
  (height < 10 || height > maxHeight) && $copyFrom(nextCamera, previousCamera);
  return true;
}
;
var Lorg_glob3_mobile_generated_SimpleCameraConstrainer_2_classLit = createForClass('org.glob3.mobile.generated', 'SimpleCameraConstrainer', 344);
function $getCameraPosition(planet, planetRenderer){
  var sector;
  sector = !planetRenderer?null:planetRenderer._renderedSector;
  return !sector?$getDefaultCameraPosition(planet, ($clinit_Sector() , $clinit_Sector() , FULL_SPHERE)):$getDefaultCameraPosition(planet, sector);
}

defineClass(386, 484, {});
_._rotationCenterU = 0;
_._rotationCenterV = 0;
_._rotationInRadians = 0;
_._scaleU = 0;
_._scaleV = 0;
_._translationU = 0;
_._translationV = 0;
var Lorg_glob3_mobile_generated_TransformableTextureMapping_2_classLit = createForClass('org.glob3.mobile.generated', 'TransformableTextureMapping', 386);
function $dispose_11(this$static){
  this$static._ownedTexCoords && !!this$static._texCoords && $dispose_15(this$static._texCoords);
  $releaseGLTextureId_0(this$static);
}

function $modifyGLState_1(this$static, state){
  var tglf;
  if (!this$static._texCoords) {
    $logError(_instance_3, 'SimpleTextureMapping::bind() with _texCoords == NULL', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
   else {
    tglf = dynamicCast($getGLFeature(state, 6), 71);
    if (!!tglf && tglf._texID == this$static._glTextureId._id) {
      $setScale(tglf, this$static._scaleU, this$static._scaleV);
      $setTranslation(tglf, this$static._translationU, this$static._translationV);
      $setRotationAngleInRadiansAndRotationCenter(tglf, this$static._rotationInRadians, this$static._rotationCenterU, this$static._rotationCenterV);
    }
     else {
      $clearGLFeatureGroup(state, ($clinit_GLFeatureGroupName() , COLOR_GROUP));
      this$static._scaleU != 1 || this$static._scaleV != 1 || this$static._translationU != 0 || this$static._translationV != 0 || this$static._rotationInRadians != 0?$addGLFeature(state, new TextureGLFeature_0(this$static._glTextureId._id, this$static._texCoords, this$static._transparent, this$static._glTextureId._isPremultiplied?_one:_srcAlpha, _oneMinusSrcAlpha, this$static._translationU, this$static._translationV, this$static._scaleU, this$static._scaleV, this$static._rotationInRadians, this$static._rotationCenterU, this$static._rotationCenterV), false):$addGLFeature(state, new TextureGLFeature(this$static._glTextureId._id, this$static._texCoords, this$static._transparent, this$static._glTextureId._isPremultiplied?_one:_srcAlpha, _oneMinusSrcAlpha), false);
    }
  }
}

function $releaseGLTextureId_0(this$static){
  if (this$static._glTextureId) {
    $dispose_12(this$static._glTextureId);
    this$static._glTextureId = null;
  }
   else {
    $logError(_instance_3, 'Releasing invalid simple texture mapping', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
}

function SimpleTextureMapping(glTextureId, texCoords){
  this._translationU = 0;
  this._translationV = 0;
  this._scaleU = 1;
  this._scaleV = 1;
  this._rotationInRadians = 0;
  this._rotationCenterU = 0;
  this._rotationCenterV = 0;
  this._glTextureId = glTextureId;
  this._texCoords = texCoords;
  this._ownedTexCoords = true;
  this._transparent = true;
}

defineClass(387, 386, {}, SimpleTextureMapping);
_._ownedTexCoords = false;
_._transparent = false;
var Lorg_glob3_mobile_generated_SimpleTextureMapping_2_classLit = createForClass('org.glob3.mobile.generated', 'SimpleTextureMapping', 387);
function TaitBryanAngles(heading, pitch, roll){
  this._heading = new Angle_0(heading);
  this._pitch = new Angle_0(pitch);
  this._roll = new Angle_0(roll);
}

defineClass(146, 1, {}, TaitBryanAngles);
var Lorg_glob3_mobile_generated_TaitBryanAngles_2_classLit = createForClass('org.glob3.mobile.generated', 'TaitBryanAngles', 146);
function TextCanvasElement(text_0, font, color_0){
  this._text = text_0;
  this._font = new GFont_0(font);
  this._color = new Color_0(color_0);
}

defineClass(400, 91, $intern_40, TextCanvasElement);
_.drawAt = function drawAt_0(left, top_0, canvas){
  $setFont(canvas, this._font);
  $setFillColor(canvas, this._color);
  $fillText(canvas, this._text, left, top_0);
}
;
_.getExtent = function getExtent_0(canvas){
  $setFont(canvas, this._font);
  return $textExtent(canvas, this._text);
}
;
var Lorg_glob3_mobile_generated_TextCanvasElement_2_classLit = createForClass('org.glob3.mobile.generated', 'TextCanvasElement', 400);
function $createBasicValues(this$static, texCoords){
  var texUnit, value_0;
  value_0 = new GPUAttributeValueVec2Float(texCoords);
  texUnit = new GPUUniformValueInt(this$static._target);
  switch (this$static._target) {
    case 0:
      $addUniformValue(this$static._values, ($clinit_GPUUniformKey() , SAMPLER), texUnit);
      $addAttributeValue(this$static._values, ($clinit_GPUAttributeKey() , TEXTURE_COORDS), value_0);
      break;
    case 1:
      $addUniformValue(this$static._values, ($clinit_GPUUniformKey() , SAMPLER2), texUnit);
      $addAttributeValue(this$static._values, ($clinit_GPUAttributeKey() , TEXTURE_COORDS_2), value_0);
      break;
    case 2:
      $addUniformValue(this$static._values, ($clinit_GPUUniformKey() , SAMPLER3), texUnit);
      $addAttributeValue(this$static._values, ($clinit_GPUAttributeKey() , TEXTURE_COORDS_3), value_0);
      break;
    default:$_release(value_0);
      $_release(texUnit);
      $logError(_instance_3, 'Wrong texture target.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
}

function $setRotationAngleInRadiansAndRotationCenter(this$static, angle, u, v){
  if (!this$static._rotationAngle || !this$static._rotationCenter) {
    if (angle != 0) {
      this$static._rotationCenter = new GPUUniformValueVec2FloatMutable(u, v);
      $addUniformValue(this$static._values, ($clinit_GPUUniformKey() , ROTATION_CENTER_TEXTURE_COORDS), this$static._rotationCenter);
      this$static._rotationAngle = new GPUUniformValueFloatMutable(angle);
      $addUniformValue(this$static._values, ROTATION_ANGLE_TEXTURE_COORDS, this$static._rotationAngle);
    }
  }
   else {
    if (angle == 0) {
      $removeUniformValue(this$static._values, ($clinit_GPUUniformKey() , ROTATION_CENTER_TEXTURE_COORDS));
      $removeUniformValue(this$static._values, ROTATION_ANGLE_TEXTURE_COORDS);
    }
     else {
      $changeValue_0(this$static._rotationCenter, u, v);
      $changeValue(this$static._rotationAngle, angle);
    }
  }
}

function $setScale(this$static, u, v){
  if (!this$static._scale) {
    this$static._scale = new GPUUniformValueVec2FloatMutable(u, v);
    $addUniformValue(this$static._values, ($clinit_GPUUniformKey() , SCALE_TEXTURE_COORDS), this$static._scale);
  }
   else {
    u == 1 && v == 1?$removeUniformValue(this$static._values, ($clinit_GPUUniformKey() , SCALE_TEXTURE_COORDS)):$changeValue_0(this$static._scale, u, v);
  }
}

function $setTranslation(this$static, u, v){
  if (!this$static._translation) {
    this$static._translation = new GPUUniformValueVec2FloatMutable(u, v);
    $addUniformValue(this$static._values, ($clinit_GPUUniformKey() , TRANSLATION_TEXTURE_COORDS), this$static._translation);
  }
   else {
    u == 0 && v == 0?$removeUniformValue(this$static._values, ($clinit_GPUUniformKey() , TRANSLATION_TEXTURE_COORDS)):$changeValue_0(this$static._translation, u, v);
  }
}

function TextureGLFeature(texID, texCoords, blend, sFactor, dFactor){
  GLColorGroupFeature.call(this, 6, 4, blend, sFactor, dFactor);
  this._texID = texID;
  this._target = 0;
  this._translation = null;
  this._scale = null;
  this._rotationCenter = null;
  this._rotationAngle = null;
  $createBasicValues(this, texCoords);
}

function TextureGLFeature_0(texID, texCoords, blend, sFactor, dFactor, translateU, translateV, scaleU, scaleV, rotationAngleInRadians, rotationCenterU, rotationCenterV){
  GLColorGroupFeature.call(this, 6, 4, blend, sFactor, dFactor);
  this._texID = texID;
  this._target = 0;
  this._translation = null;
  this._scale = null;
  this._rotationCenter = null;
  this._rotationAngle = null;
  $createBasicValues(this, texCoords);
  $setTranslation(this, translateU, translateV);
  $setScale(this, scaleU, scaleV);
  $setRotationAngleInRadiansAndRotationCenter(this, rotationAngleInRadians, rotationCenterU, rotationCenterV);
}

defineClass(71, 128, {27:1, 70:1, 9:1, 71:1}, TextureGLFeature, TextureGLFeature_0);
_.applyOnGlobalGLState = function applyOnGlobalGLState_4(state){
  $blendingOnGlobalGLState(this, state);
  $bindTexture(state, this._target, this._texID);
}
;
_.dispose = function dispose_51(){
  !!this._values && $dispose_4(this._values);
  $dispose_0(this);
}
;
_._target = 0;
_._texID = null;
var Lorg_glob3_mobile_generated_TextureGLFeature_2_classLit = createForClass('org.glob3.mobile.generated', 'TextureGLFeature', 71);
function TextureHolder(textureSpec){
  this._referenceCounter = 1;
  this._textureSpec = textureSpec;
  this._glTextureId = null;
}

defineClass(106, 1, {106:1}, TextureHolder);
_._referenceCounter = 0;
var Lorg_glob3_mobile_generated_TextureHolder_2_classLit = createForClass('org.glob3.mobile.generated', 'TextureHolder', 106);
function $dispose_12(this$static){
  $releaseGLTextureId_1(this$static._texHandler, this$static._id);
}

function TextureIDReference(id_0, isPremultiplied, texHandler){
  this._id = id_0;
  this._isPremultiplied = isPremultiplied;
  this._texHandler = texHandler;
}

defineClass(126, 1, {}, TextureIDReference);
_._isPremultiplied = false;
var Lorg_glob3_mobile_generated_TextureIDReference_2_classLit = createForClass('org.glob3.mobile.generated', 'TextureIDReference', 126);
function $description_5(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += '(';
  $addString(isb, this$static._id);
  isb._string += ' ';
  $addInt(isb, this$static._width);
  isb._string += 'x';
  $addInt(isb, this$static._height);
  isb._string += ')';
  s = isb._string;
  return s;
}

function $equalsTo(this$static, that){
  return compareTo_3(this$static._id, that._id) == 0 && this$static._width == that._width && this$static._height == that._height;
}

function TextureSpec(id_0, width_0, height, generateMipmap){
  this._id = id_0;
  this._width = width_0;
  this._height = height;
  this._generateMipmap = generateMipmap;
}

defineClass(152, 1, {152:1}, TextureSpec);
_.equals$ = function equals_19(obj){
  var other;
  if (this === obj)
    return true;
  if (obj == null)
    return false;
  if (Lorg_glob3_mobile_generated_TextureSpec_2_classLit != getClass__Ljava_lang_Class___devirtual$(obj))
    return false;
  other = dynamicCast(obj, 152);
  if (this._height != other._height)
    return false;
  if (this._id == null) {
    if (other._id != null)
      return false;
  }
   else if (!$equals(this._id, other._id))
    return false;
  if (this._width != other._width)
    return false;
  return true;
}
;
_.hashCode$ = function hashCode_24(){
  var result;
  result = 31 + this._height;
  result = 31 * result + (this._id == null?0:getHashCode_0(this._id));
  result = 31 * result + this._width;
  return result;
}
;
_.toString$ = function toString_38(){
  return $description_5(this);
}
;
_._generateMipmap = false;
_._height = 0;
_._width = 0;
var Lorg_glob3_mobile_generated_TextureSpec_2_classLit = createForClass('org.glob3.mobile.generated', 'TextureSpec', 152);
function $dispose_13(this$static){
  this$static._ownedMesh && !!this$static._mesh && $dispose(this$static._mesh);
  this$static._ownedTexMapping && !!this$static._textureMapping && $dispose_11(this$static._textureMapping);
  $_release(this$static._glState);
}

function TexturedMesh(mesh, textureMapping){
  Mesh.call(this);
  this._mesh = mesh;
  this._ownedMesh = true;
  this._textureMapping = textureMapping;
  this._ownedTexMapping = true;
  this._glState = new GLState;
}

defineClass(388, 102, {}, TexturedMesh);
_.rawRender = function rawRender_5(rc, parentState){
  $modifyGLState_1(this._textureMapping, this._glState);
  $setParent_2(this._glState, parentState);
  $render(this._mesh, rc, this._glState);
}
;
_._ownedMesh = false;
_._ownedTexMapping = false;
var Lorg_glob3_mobile_generated_TexturedMesh_2_classLit = createForClass('org.glob3.mobile.generated', 'TexturedMesh', 388);
function $getGLTextureIdIfAvailable(this$static, textureSpec){
  var _textureHoldersSize, holder, i_0;
  _textureHoldersSize = this$static._textureHolders.array.length;
  for (i_0 = 0; i_0 < _textureHoldersSize; i_0++) {
    holder = dynamicCast($get_0(this$static._textureHolders, i_0), 106);
    if ($equalsTo(holder._textureSpec, textureSpec)) {
      ++holder._referenceCounter;
      return holder._glTextureId;
    }
  }
  return null;
}

function $getTextureIDReference(this$static, image, format, name_0, generateMipmap){
  var holder, previousId, textureSpec;
  textureSpec = new TextureSpec(name_0, $getWidth(image), $getHeight(image), generateMipmap);
  previousId = $getGLTextureIdIfAvailable(this$static, textureSpec);
  if (previousId) {
    return new TextureIDReference(previousId, false, this$static);
  }
  holder = new TextureHolder(textureSpec);
  holder._glTextureId = $uploadTexture(this$static._gl, image, format, textureSpec._generateMipmap);
  this$static._verbose && $logInfo(_instance_3, 'Uploaded texture "%s" to GPU with texId=%s', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [$description_5(textureSpec), 'GLTextureId_WebGL ' + getHashCode(holder._glTextureId._webGLTexture)]));
  $add_3(this$static._textureHolders, holder);
  return new TextureIDReference(holder._glTextureId, false, this$static);
}

function $releaseGLTextureId_1(this$static, glTextureId){
  var holder, i_0;
  if (!glTextureId) {
    return;
  }
  for (i_0 = 0; i_0 < this$static._textureHolders.array.length; i_0++) {
    holder = dynamicCast($get_0(this$static._textureHolders, i_0), 106);
    if ($isEquals_4(holder._glTextureId, glTextureId)) {
      --holder._referenceCounter;
      if (holder._referenceCounter <= 0) {
        $deleteTexture(this$static._gl, holder._glTextureId);
        this$static._textureHolders.remove_1(i_0);
      }
      return;
    }
  }
}

function $retainGLTextureId(this$static, glTextureId){
  var holder, i_0;
  if (!glTextureId) {
    return;
  }
  for (i_0 = 0; i_0 < this$static._textureHolders.array.length; i_0++) {
    holder = dynamicCast($get_0(this$static._textureHolders, i_0), 106);
    if ($isEquals_4(holder._glTextureId, glTextureId)) {
      ++holder._referenceCounter;
      return;
    }
  }
  $logInfo(_instance_3, 'break (point) on me 6\n', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
}

function TexturesHandler(gl){
  this._textureHolders = new ArrayList;
  this._gl = gl;
  this._verbose = false;
}

defineClass(351, 1, {}, TexturesHandler);
_._verbose = false;
var Lorg_glob3_mobile_generated_TexturesHandler_2_classLit = createForClass('org.glob3.mobile.generated', 'TexturesHandler', 351);
function $ancestorTexturedSolvedChanged_0(this$static, ancestor, textureSolved){
  var i_0, subtile, subtilesSize;
  if (textureSolved && this$static._textureSolved) {
    return;
  }
  !!this$static._texturizer && $ancestorTexturedSolvedChanged(this$static, ancestor, textureSolved);
  if (this$static._subtiles) {
    subtilesSize = this$static._subtiles.array.length;
    for (i_0 = 0; i_0 < subtilesSize; i_0++) {
      subtile = dynamicCast($get_0(this$static._subtiles, i_0), 23);
      $ancestorTexturedSolvedChanged_0(subtile, ancestor, textureSolved);
    }
  }
}

function $computeTileCorners(this$static, planet){
  var mediumHeight;
  if (!this$static._tessellatorMesh) {
    $logError(_instance_3, 'Error in Tile::computeTileCorners', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  mediumHeight = this$static._tileTessellatorMeshData._averageHeight;
  this$static._northWestPoint = new Vector3D_0($toCartesian_1(planet, $getNW(this$static._sector), mediumHeight));
  this$static._northEastPoint = new Vector3D_0($toCartesian_1(planet, this$static._sector._upper, mediumHeight));
  this$static._southWestPoint = new Vector3D_0($toCartesian_1(planet, this$static._sector._lower, mediumHeight));
  this$static._southEastPoint = new Vector3D_0($toCartesian_1(planet, $getSE(this$static._sector), mediumHeight));
}

function $createSubTile(this$static, lowerLat, lowerLon, upperLat, upperLon, level, row, column, setParent){
  var parent_0;
  parent_0 = setParent?this$static:null;
  return new Tile(this$static._texturizer, parent_0, new Sector(new Geodetic2D(lowerLat, lowerLon), new Geodetic2D(upperLat, upperLon)), this$static._mercator, level, row, column, this$static._planetRenderer);
}

function $createSubTiles(this$static, splitLatitude, splitLongitude, setParent){
  var column2, lower, nextLevel, renderedSector, row2, s1, s2, s3, s4, subTiles, upper;
  lower = this$static._sector._lower;
  upper = this$static._sector._upper;
  nextLevel = this$static._level + 1;
  row2 = 2 * this$static._row;
  column2 = 2 * this$static._column;
  subTiles = new ArrayList;
  renderedSector = this$static._planetRenderer._renderedSector;
  s1 = new Sector(new Geodetic2D(lower._latitude, lower._longitude), new Geodetic2D(splitLatitude, splitLongitude));
  (!renderedSector || $touchesWith(renderedSector, s1)) && $add_3(subTiles, $createSubTile(this$static, lower._latitude, lower._longitude, splitLatitude, splitLongitude, nextLevel, row2, column2, setParent));
  s2 = new Sector(new Geodetic2D(lower._latitude, splitLongitude), new Geodetic2D(splitLatitude, upper._longitude));
  (!renderedSector || $touchesWith(renderedSector, s2)) && $add_3(subTiles, $createSubTile(this$static, lower._latitude, splitLongitude, splitLatitude, upper._longitude, nextLevel, row2, column2 + 1, setParent));
  s3 = new Sector(new Geodetic2D(splitLatitude, lower._longitude), new Geodetic2D(upper._latitude, splitLongitude));
  (!renderedSector || $touchesWith(renderedSector, s3)) && $add_3(subTiles, $createSubTile(this$static, splitLatitude, lower._longitude, upper._latitude, splitLongitude, nextLevel, row2 + 1, column2, setParent));
  s4 = new Sector(new Geodetic2D(splitLatitude, splitLongitude), new Geodetic2D(upper._latitude, upper._longitude));
  (!renderedSector || $touchesWith(renderedSector, s4)) && $add_3(subTiles, $createSubTile(this$static, splitLatitude, splitLongitude, upper._latitude, upper._longitude, nextLevel, row2 + 1, column2 + 1, setParent));
  return subTiles;
}

function $deleteTexturizedMesh(this$static, texturizer){
  if (!!this$static._parent && !!this$static._texturizedMesh) {
    !!texturizer && $tileMeshToBeDeleted(this$static);
    !!this$static._texturizedMesh && $dispose_7(this$static._texturizedMesh);
    this$static._texturizedMesh = null;
    !!this$static._texturizerData && $dispose_1(this$static._texturizerData);
    this$static._texturizerData = null;
    this$static._texturizerDirty = true;
    $setTextureSolved(this$static, false);
  }
}

function $description_6(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += '(Tile';
  isb._string += ' level=';
  $addInt(isb, this$static._level);
  isb._string += ', row=';
  $addInt(isb, this$static._row);
  isb._string += ', column=';
  $addInt(isb, this$static._column);
  isb._string += ', sector=';
  $addString(isb, $description_3(this$static._sector));
  isb._string += ')';
  s = isb._string;
  return s;
}

function $getBoundingVolume(this$static, rc, tessellator, layerTilesRenderParameters){
  var mesh;
  if (!this$static._boundingVolume) {
    mesh = $getTessellatorMesh(this$static, rc, tessellator, layerTilesRenderParameters);
    !!mesh && (this$static._boundingVolume = (!mesh._extent && (mesh._extent = $computeBoundingVolume(mesh)) , mesh._extent));
  }
  return this$static._boundingVolume;
}

function $getDebugMesh(this$static, rc, tessellator, layerTilesRenderParameters){
  var tileMeshResolution;
  if (!this$static._debugMesh) {
    tileMeshResolution = new Vector2I_0(layerTilesRenderParameters._tileMeshResolution);
    this$static._debugMesh = $createTileDebugMesh(tessellator, rc._planet, tileMeshResolution, this$static);
  }
  return this$static._debugMesh;
}

function $getDeepestTileContaining(this$static, position){
  var i_0, subtile, subtileResult;
  if ($contains_1(this$static._sector, position._latitude, position._longitude)) {
    if (!this$static._subtiles) {
      return this$static;
    }
    for (i_0 = 0; i_0 < this$static._subtiles.array.length; i_0++) {
      subtile = dynamicCast($get_0(this$static._subtiles, i_0), 23);
      subtileResult = $getDeepestTileContaining(subtile, position);
      if (subtileResult) {
        return subtileResult;
      }
    }
  }
  return null;
}

function $getNormalizedPixelsFromPosition(this$static, position2D, tileDimension){
  var uv;
  uv = $getUVCoordinates_0(this$static._sector, position2D);
  return new Vector2I(round_int(tileDimension._x * uv._x), round_int(tileDimension._y * uv._y));
}

function $getSubTiles(this$static){
  var lower, splitLatitude, splitLongitude, upper;
  if (this$static._subtiles) {
    return this$static._subtiles;
  }
  lower = this$static._sector._lower;
  upper = this$static._sector._upper;
  splitLongitude = midAngle(lower._longitude, upper._longitude);
  splitLatitude = this$static._mercator?calculateSplitLatitude(lower._latitude, upper._latitude):midAngle(lower._latitude, upper._latitude);
  return $getSubTiles_0(this$static, splitLatitude, splitLongitude);
}

function $getSubTiles_0(this$static, splitLatitude, splitLongitude){
  if (!this$static._subtiles) {
    this$static._subtiles = $createSubTiles(this$static, splitLatitude, splitLongitude, true);
    this$static._justCreatedSubtiles = true;
  }
  return this$static._subtiles;
}

function $getTessellatorMesh(this$static, rc, tessellator, layerTilesRenderParameters){
  if (!this$static._tessellatorMesh || this$static._mustActualizeMeshDueToNewElevationData) {
    this$static._mustActualizeMeshDueToNewElevationData = false;
    this$static._tessellatorMesh = $createTileMesh(tessellator, rc._planet, layerTilesRenderParameters._tileMeshResolution, this$static, this$static._tileTessellatorMeshData);
    $computeTileCorners(this$static, rc._planet);
  }
  return this$static._tessellatorMesh;
}

function $isVisible(this$static, rc, cameraFrustumInModelCoordinates, renderedSector, tessellator, layerTilesRenderParameters){
  var boundingVolume;
  if (!!renderedSector && !$touchesWith(renderedSector, this$static._sector)) {
    return false;
  }
  boundingVolume = $getBoundingVolume(this$static, rc, tessellator, layerTilesRenderParameters);
  return !!boundingVolume && $touchesWithBox(cameraFrustumInModelCoordinates, boundingVolume);
}

function $meetsRenderCriteria(this$static, rc, layerTilesRenderParameters, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS){
  var camera, distanceInPixelsEast, distanceInPixelsNorth, distanceInPixelsSouth, distanceInPixelsSquaredArcEast, distanceInPixelsSquaredArcNorth, distanceInPixelsSquaredArcSouth, distanceInPixelsSquaredArcWest, distanceInPixelsWest;
  if (this$static._level >= layerTilesRenderParameters._maxLevelForPoles && $touchesPoles(this$static._sector)) {
    return true;
  }
  if (this$static._level >= layerTilesRenderParameters._maxLevel) {
    return true;
  }
  if (this$static._lastMeetsRenderCriteriaTimeInMS != 0 && nowInMS - this$static._lastMeetsRenderCriteriaTimeInMS < 250) {
    return this$static._lastMeetsRenderCriteriaResult;
  }
  if (tilesRenderParameters._useTilesSplitBudget) {
    if (!this$static._subtiles) {
      if (lt(sub_0(fromDouble(now_1()), lastSplitTimer._startTimeInMilliseconds), {l:67, m:0, h:0})) {
        return true;
      }
    }
  }
  this$static._lastMeetsRenderCriteriaTimeInMS = nowInMS;
  (this$static._northArcSegmentRatioSquared == 0 || this$static._southArcSegmentRatioSquared == 0 || this$static._eastArcSegmentRatioSquared == 0 || this$static._westArcSegmentRatioSquared == 0) && $prepareTestLODData(this$static);
  camera = rc._currentCamera;
  distanceInPixelsNorth = $getEstimatedPixelDistance(camera, this$static._northWestPoint, this$static._northEastPoint);
  distanceInPixelsSouth = $getEstimatedPixelDistance(camera, this$static._southWestPoint, this$static._southEastPoint);
  distanceInPixelsWest = $getEstimatedPixelDistance(camera, this$static._northWestPoint, this$static._southWestPoint);
  distanceInPixelsEast = $getEstimatedPixelDistance(camera, this$static._northEastPoint, this$static._southEastPoint);
  distanceInPixelsSquaredArcNorth = distanceInPixelsNorth * distanceInPixelsNorth * this$static._northArcSegmentRatioSquared;
  distanceInPixelsSquaredArcSouth = distanceInPixelsSouth * distanceInPixelsSouth * this$static._southArcSegmentRatioSquared;
  distanceInPixelsSquaredArcWest = distanceInPixelsWest * distanceInPixelsWest * this$static._westArcSegmentRatioSquared;
  distanceInPixelsSquaredArcEast = distanceInPixelsEast * distanceInPixelsEast * this$static._eastArcSegmentRatioSquared;
  this$static._lastMeetsRenderCriteriaResult = distanceInPixelsSquaredArcNorth <= texHeightSquared && distanceInPixelsSquaredArcSouth <= texHeightSquared && distanceInPixelsSquaredArcWest <= texWidthSquared && distanceInPixelsSquaredArcEast <= texWidthSquared;
  return this$static._lastMeetsRenderCriteriaResult;
}

function $prepareForFullRendering(this$static, rc, texturizer, tessellator, layerTilesRenderParameters, layerSet, tileDownloadPriority, verticalExaggeration, logTilesPetitions){
  var needsToCallTexturizer, tessellatorMesh;
  verticalExaggeration != this$static._verticalExaggeration && (this$static._verticalExaggeration = verticalExaggeration);
  tessellatorMesh = $getTessellatorMesh(this$static, rc, tessellator, layerTilesRenderParameters);
  if (!tessellatorMesh) {
    return;
  }
  if (texturizer) {
    needsToCallTexturizer = !this$static._texturizedMesh || this$static._texturizerDirty;
    needsToCallTexturizer && (this$static._texturizedMesh = $texturize(texturizer, rc, layerTilesRenderParameters, layerSet, true, tileDownloadPriority, this$static, tessellatorMesh, logTilesPetitions));
  }
}

function $prepareTestLODData(this$static){
  var normalNE, normalNW, normalSE, normalSW;
  if (!this$static._northWestPoint || !this$static._northEastPoint || !this$static._southWestPoint || !this$static._southEastPoint) {
    $logError(_instance_3, 'Error in Tile::prepareTestLODData', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
    return;
  }
  normalNW = $normalized_0(this$static._northWestPoint);
  normalNE = $normalized_0(this$static._northEastPoint);
  normalSW = $normalized_0(this$static._southWestPoint);
  normalSE = $normalized_0(this$static._southEastPoint);
  this$static._northArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNW, normalNE);
  this$static._southArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalSW, normalSE);
  this$static._eastArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNE, normalSE);
  this$static._westArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNW, normalSW);
}

function $prune(this$static, texturizer, elevationDataProvider, tilesStoppedRendering){
  var i_0, subtile, subtilesSize;
  if (this$static._subtiles) {
    subtilesSize = this$static._subtiles.array.length;
    for (i_0 = 0; i_0 < subtilesSize; i_0++) {
      subtile = dynamicCast($get_0(this$static._subtiles, i_0), 23);
      $setIsVisible(subtile, false, texturizer);
      $prune(subtile, texturizer, elevationDataProvider, tilesStoppedRendering);
      !!texturizer && $tileToBeDeleted(subtile);
      !!subtile && (!!subtile._debugMesh && $dispose_6(subtile._debugMesh) , subtile._debugMesh = null , !!subtile._flatColorMesh && $dispose_2(subtile._flatColorMesh) , subtile._flatColorMesh = null , !!subtile._tessellatorMesh && $dispose_5(subtile._tessellatorMesh) , subtile._tessellatorMesh = null , !!subtile._texturizerData && $dispose_1(subtile._texturizerData) , subtile._texturizerData = null , !!subtile._texturizedMesh && $dispose_7(subtile._texturizedMesh) , subtile._texturizedMesh = null , null);
    }
    this$static._subtiles = null;
    this$static._subtiles = null;
  }
}

function $rawRender_1(this$static, rc, glState, texturizer, tessellator, layerTilesRenderParameters, layerSet, forceFullRender, tileDownloadPriority, logTilesPetitions){
  var needsToCallTexturizer, tessellatorMesh;
  tessellatorMesh = $getTessellatorMesh(this$static, rc, tessellator, layerTilesRenderParameters);
  if (!tessellatorMesh) {
    return;
  }
  if (!texturizer) {
    tessellatorMesh._enable && ($setParent_2(tessellatorMesh._glState, glState) , $rawRender(tessellatorMesh, rc));
  }
   else {
    needsToCallTexturizer = !this$static._texturizedMesh || this$static._texturizerDirty;
    needsToCallTexturizer && (this$static._texturizedMesh = $texturize(texturizer, rc, layerTilesRenderParameters, layerSet, forceFullRender, tileDownloadPriority, this$static, tessellatorMesh, logTilesPetitions));
    if (this$static._texturizedMesh) {
      $render(this$static._texturizedMesh, rc, glState);
    }
     else {
      !this$static._flatColorMesh && (this$static._flatColorMesh = new FlatColorMesh(tessellatorMesh, new Color(1, 1, 1, 1)));
      $render(this$static._flatColorMesh, rc, glState);
    }
  }
}

function $render_5(this$static, rc, parentState, toVisitInNextIteration, cameraFrustumInModelCoordinates, tilesStatistics, verticalExaggeration, layerTilesRenderParameters, texturizer, tilesRenderParameters, lastSplitTimer, elevationDataProvider, tessellator, layerSet, renderedSector, forceFullRender, tileDownloadPriority, texWidthSquared, texHeightSquared, nowInMS, renderTileMeshes, logTilesPetitions, tilesStoppedRendering){
  var i_0, isRawRender, rendered, subTile, subTiles, subTilesSize, tileTexturePriority, level, level_0, debugMesh, level_1, sector, lowerLatitudeDegrees, lowerLongitudeDegrees, upperLatitudeDegrees, upperLongitudeDegrees;
  ++tilesStatistics._tilesProcessed;
  level = this$static._level;
  tilesStatistics._tilesProcessedByLevel[level] = tilesStatistics._tilesProcessedByLevel[level] + 1;
  verticalExaggeration != this$static._verticalExaggeration && (this$static._verticalExaggeration = verticalExaggeration);
  rendered = false;
  if ($isVisible(this$static, rc, cameraFrustumInModelCoordinates, renderedSector, tessellator, layerTilesRenderParameters)) {
    $setIsVisible(this$static, true, texturizer);
    ++tilesStatistics._tilesVisible;
    level_0 = this$static._level;
    tilesStatistics._tilesVisibleByLevel[level_0] = tilesStatistics._tilesVisibleByLevel[level_0] + 1;
    isRawRender = !toVisitInNextIteration || $meetsRenderCriteria(this$static, rc, layerTilesRenderParameters, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS) || tilesRenderParameters._incrementalTileQuality && !this$static._textureSolved;
    if (isRawRender) {
      tileTexturePriority = tilesRenderParameters._incrementalTileQuality?sub_0(add_0(tileDownloadPriority, fromInt(layerTilesRenderParameters._maxLevel)), fromInt(this$static._level)):add_0(tileDownloadPriority, fromInt(this$static._level));
      rendered = true;
      renderTileMeshes && $rawRender_1(this$static, rc, parentState, texturizer, tessellator, layerTilesRenderParameters, layerSet, forceFullRender, tileTexturePriority, logTilesPetitions);
      tilesRenderParameters._renderDebug && (debugMesh = $getDebugMesh(this$static, rc, tessellator, layerTilesRenderParameters) , !!debugMesh && debugMesh._enable && ($setParent_2(debugMesh._glState, parentState) , $rawRender_0(debugMesh, rc)) , undefined);
      ++tilesStatistics._tilesRendered;
      level_1 = this$static._level;
      tilesStatistics._tilesRenderedByLevel[level_1] = tilesStatistics._tilesRenderedByLevel[level_1] + 1;
      sector = this$static._sector;
      lowerLatitudeDegrees = sector._lower._latitude._degrees;
      lowerLongitudeDegrees = sector._lower._longitude._degrees;
      upperLatitudeDegrees = sector._upper._latitude._degrees;
      upperLongitudeDegrees = sector._upper._longitude._degrees;
      lowerLatitudeDegrees < tilesStatistics._visibleLowerLatitudeDegrees && (tilesStatistics._visibleLowerLatitudeDegrees = lowerLatitudeDegrees);
      upperLatitudeDegrees < tilesStatistics._visibleLowerLatitudeDegrees && (tilesStatistics._visibleLowerLatitudeDegrees = upperLatitudeDegrees);
      lowerLatitudeDegrees > tilesStatistics._visibleUpperLatitudeDegrees && (tilesStatistics._visibleUpperLatitudeDegrees = lowerLatitudeDegrees);
      upperLatitudeDegrees > tilesStatistics._visibleUpperLatitudeDegrees && (tilesStatistics._visibleUpperLatitudeDegrees = upperLatitudeDegrees);
      lowerLongitudeDegrees < tilesStatistics._visibleLowerLongitudeDegrees && (tilesStatistics._visibleLowerLongitudeDegrees = lowerLongitudeDegrees);
      upperLongitudeDegrees < tilesStatistics._visibleLowerLongitudeDegrees && (tilesStatistics._visibleLowerLongitudeDegrees = upperLongitudeDegrees);
      lowerLongitudeDegrees > tilesStatistics._visibleUpperLongitudeDegrees && (tilesStatistics._visibleUpperLongitudeDegrees = lowerLongitudeDegrees);
      upperLongitudeDegrees > tilesStatistics._visibleUpperLongitudeDegrees && (tilesStatistics._visibleUpperLongitudeDegrees = upperLongitudeDegrees);
      $prune(this$static, texturizer, elevationDataProvider, tilesStoppedRendering);
    }
     else {
      subTiles = $getSubTiles(this$static);
      if (this$static._justCreatedSubtiles) {
        lastSplitTimer._startTimeInMilliseconds = fromDouble(now_1());
        this$static._justCreatedSubtiles = false;
      }
      subTilesSize = subTiles.array.length;
      for (i_0 = 0; i_0 < subTilesSize; i_0++) {
        subTile = (checkElementIndex(i_0, subTiles.array.length) , dynamicCast(subTiles.array[i_0], 23));
        setCheck(toVisitInNextIteration.array, toVisitInNextIteration.array.length, subTile);
      }
    }
  }
   else {
    $setIsVisible(this$static, false, texturizer);
    $prune(this$static, texturizer, elevationDataProvider, tilesStoppedRendering);
  }
  this$static._rendered != rendered && (this$static._rendered = rendered);
}

function $setIsVisible(this$static, isVisible, texturizer){
  if (this$static._isVisible != isVisible) {
    this$static._isVisible = isVisible;
    this$static._isVisible || $deleteTexturizedMesh(this$static, texturizer);
  }
}

function $setTessellatorData(this$static, tessellatorData){
  tessellatorData != this$static._tessellatorData && (this$static._tessellatorData = tessellatorData);
}

function $setTextureSolved(this$static, textureSolved){
  var i_0, subtile, subtilesSize;
  if (textureSolved != this$static._textureSolved) {
    this$static._textureSolved = textureSolved;
    if (this$static._textureSolved) {
      !!this$static._texturizerData && $dispose_1(this$static._texturizerData);
      this$static._texturizerData = null;
    }
    if (this$static._subtiles) {
      subtilesSize = this$static._subtiles.array.length;
      for (i_0 = 0; i_0 < subtilesSize; i_0++) {
        subtile = dynamicCast($get_0(this$static._subtiles, i_0), 23);
        $ancestorTexturedSolvedChanged_0(subtile, this$static, this$static._textureSolved);
      }
    }
  }
}

function $setTexturizerData(this$static, texturizerData){
  if (texturizerData != this$static._texturizerData) {
    !!this$static._texturizerData && $dispose_1(this$static._texturizerData);
    this$static._texturizerData = texturizerData;
  }
}

function $toBeDeleted(this$static, texturizer, elevationDataProvider, tilesStoppedRendering){
  $prune(this$static, texturizer, elevationDataProvider, tilesStoppedRendering);
  !!texturizer && $tileToBeDeleted(this$static);
}

function Tile(texturizer, parent_0, sector, mercator, level, row, column, planetRenderer){
  this._tileTessellatorMeshData = new TileTessellatorMeshData;
  this._texturizer = texturizer;
  this._parent = parent_0;
  this._sector = new Sector_0(sector);
  this._mercator = mercator;
  this._level = level;
  this._row = row;
  this._column = column;
  this._tessellatorMesh = null;
  this._debugMesh = null;
  this._flatColorMesh = null;
  this._texturizedMesh = null;
  this._textureSolved = false;
  this._texturizerDirty = true;
  this._subtiles = null;
  this._justCreatedSubtiles = false;
  this._isVisible = false;
  this._texturizerData = null;
  this._verticalExaggeration = 0;
  this._mustActualizeMeshDueToNewElevationData = false;
  this._boundingVolume = null;
  this._lastMeetsRenderCriteriaTimeInMS = 0;
  this._planetRenderer = planetRenderer;
  this._tessellatorData = null;
  this._northWestPoint = null;
  this._northEastPoint = null;
  this._southWestPoint = null;
  this._southEastPoint = null;
  this._northArcSegmentRatioSquared = 0;
  this._southArcSegmentRatioSquared = 0;
  this._eastArcSegmentRatioSquared = 0;
  this._westArcSegmentRatioSquared = 0;
  this._rendered = false;
  this._id = level + '/' + row + '/' + column;
}

function getSquaredArcSegmentRatio(a, b){
  var angleInRadians, arcSegmentRatio, halfAngleSin;
  angleInRadians = angleInRadiansBetween_0(a, b);
  halfAngleSin = Math.sin(angleInRadians / 2);
  arcSegmentRatio = halfAngleSin == 0?1:angleInRadians / (2 * halfAngleSin);
  return arcSegmentRatio * arcSegmentRatio;
}

defineClass(23, 1, {23:1}, Tile);
_.toString$ = function toString_39(){
  return $description_6(this);
}
;
_._column = 0;
_._eastArcSegmentRatioSquared = 0;
_._isVisible = false;
_._justCreatedSubtiles = false;
_._lastMeetsRenderCriteriaResult = false;
_._lastMeetsRenderCriteriaTimeInMS = 0;
_._level = 0;
_._mercator = false;
_._mustActualizeMeshDueToNewElevationData = false;
_._northArcSegmentRatioSquared = 0;
_._rendered = false;
_._row = 0;
_._southArcSegmentRatioSquared = 0;
_._textureSolved = false;
_._texturizerDirty = false;
_._verticalExaggeration = 0;
_._westArcSegmentRatioSquared = 0;
var Lorg_glob3_mobile_generated_Tile_2_classLit = createForClass('org.glob3.mobile.generated', 'Tile', 23);
function TileTessellatorMeshData(){
}

defineClass(382, 1, {}, TileTessellatorMeshData);
_._averageHeight = 0;
var Lorg_glob3_mobile_generated_TileTessellatorMeshData_2_classLit = createForClass('org.glob3.mobile.generated', 'TileTessellatorMeshData', 382);
function TilesRenderParameters(renderDebug, useTilesSplitBudget, forceFirstLevelTilesRenderOnStart, incrementalTileQuality, quality){
  this._renderDebug = renderDebug;
  this._useTilesSplitBudget = useTilesSplitBudget;
  this._forceFirstLevelTilesRenderOnStart = forceFirstLevelTilesRenderOnStart;
  this._incrementalTileQuality = incrementalTileQuality;
  switch (quality.ordinal) {
    case 0:
      this._texturePixelsPerInch = 64;
      break;
    case 1:
      this._texturePixelsPerInch = 128;
      break;
    default:this._texturePixelsPerInch = 256;
  }
}

defineClass(307, 1, {}, TilesRenderParameters);
_._forceFirstLevelTilesRenderOnStart = false;
_._incrementalTileQuality = false;
_._renderDebug = false;
_._texturePixelsPerInch = 0;
_._useTilesSplitBudget = false;
var Lorg_glob3_mobile_generated_TilesRenderParameters_2_classLit = createForClass('org.glob3.mobile.generated', 'TilesRenderParameters', 307);
function $clear(this$static){
  var i_0;
  this$static._tilesProcessed = 0;
  this$static._tilesVisible = 0;
  this$static._tilesRendered = 0;
  this$static._visibleLowerLatitudeDegrees = $intern_65;
  this$static._visibleLowerLongitudeDegrees = $intern_65;
  this$static._visibleUpperLatitudeDegrees = $intern_66;
  this$static._visibleUpperLongitudeDegrees = $intern_66;
  for (i_0 = 0; i_0 < 128; i_0++) {
    this$static._tilesProcessedByLevel[i_0] = 0;
    this$static._tilesVisibleByLevel[i_0] = 0;
    this$static._tilesRenderedByLevel[i_0] = 0;
  }
}

function $log_3(this$static, logger){
  $logInfo(logger, 'Tiles processed:%d (%s), visible:%d (%s), rendered:%d (%s).', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, [valueOf(this$static._tilesProcessed), asLogString(this$static._tilesProcessedByLevel), valueOf(this$static._tilesVisible), asLogString(this$static._tilesVisibleByLevel), valueOf(this$static._tilesRendered), asLogString(this$static._tilesRenderedByLevel)]));
}

function $updateVisibleSector(this$static, visibleSector){
  if (!visibleSector || visibleSector._lower._latitude._degrees != this$static._visibleLowerLatitudeDegrees || visibleSector._lower._longitude._degrees != this$static._visibleLowerLongitudeDegrees || visibleSector._upper._latitude._degrees != this$static._visibleUpperLatitudeDegrees || visibleSector._upper._longitude._degrees != this$static._visibleUpperLongitudeDegrees) {
    if (this$static._visibleLowerLatitudeDegrees > this$static._visibleUpperLatitudeDegrees || this$static._visibleLowerLongitudeDegrees > this$static._visibleUpperLongitudeDegrees) {
      return null;
    }
    return new Sector(fromDegrees_0(this$static._visibleLowerLatitudeDegrees, this$static._visibleLowerLongitudeDegrees), fromDegrees_0(this$static._visibleUpperLatitudeDegrees, this$static._visibleUpperLongitudeDegrees));
  }
  return visibleSector;
}

function TilesStatistics(){
  this._tilesProcessedByLevel = initDim(I_classLit, $intern_29, 0, 128, 7, 1);
  this._tilesVisibleByLevel = initDim(I_classLit, $intern_29, 0, 128, 7, 1);
  this._tilesRenderedByLevel = initDim(I_classLit, $intern_29, 0, 128, 7, 1);
  $clear(this);
}

function asLogString(m){
  var counter, first, i_0, isb, s;
  first = true;
  isb = new StringBuilder_WebGL;
  for (i_0 = 0; i_0 < 128; i_0++) {
    counter = m[i_0];
    if (counter != 0) {
      first?(first = false):(isb._string += ',' , isb);
      isb._string += i_0;
      isb._string += ':';
      isb._string += counter;
    }
  }
  s = isb._string;
  return s;
}

defineClass(362, 1, {}, TilesStatistics);
_._tilesProcessed = 0;
_._tilesRendered = 0;
_._tilesVisible = 0;
_._visibleLowerLatitudeDegrees = 0;
_._visibleLowerLongitudeDegrees = 0;
_._visibleUpperLatitudeDegrees = 0;
_._visibleUpperLongitudeDegrees = 0;
var Lorg_glob3_mobile_generated_TilesStatistics_2_classLit = createForClass('org.glob3.mobile.generated', 'TilesStatistics', 362);
function TimeInterval(milliseconds){
  this._milliseconds = milliseconds;
}

defineClass(48, 1, {48:1}, TimeInterval);
_.equals$ = function equals_20(obj){
  var other;
  if (this === obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  if (Lorg_glob3_mobile_generated_TimeInterval_2_classLit != getClass__Ljava_lang_Class___devirtual$(obj)) {
    return false;
  }
  other = dynamicCast(obj, 48);
  if (neq(this._milliseconds, other._milliseconds)) {
    return false;
  }
  return true;
}
;
_.hashCode$ = function hashCode_25(){
  var result;
  result = 31 + toInt(xor(this._milliseconds, shru(this._milliseconds, 32)));
  return result;
}
;
_._milliseconds = {l:0, m:0, h:0};
var Lorg_glob3_mobile_generated_TimeInterval_2_classLit = createForClass('org.glob3.mobile.generated', 'TimeInterval', 48);
function Touch_0(other){
  this._pos = other._pos;
  this._prevPos = other._prevPos;
  this._tapCount = other._tapCount;
}

function Touch_1(pos, prev){
  Touch_2.call(this, pos, prev, 0);
}

function Touch_2(pos, prev, tapCount){
  this._pos = pos;
  this._prevPos = prev;
  this._tapCount = tapCount;
}

defineClass(10, 1, {10:1}, Touch_0, Touch_1, Touch_2);
_._tapCount = 0;
var Lorg_glob3_mobile_generated_Touch_2_classLit = createForClass('org.glob3.mobile.generated', 'Touch', 10);
function $dispose_14(this$static){
  var i_0;
  for (i_0 = 0; i_0 < this$static._touchs.array.length; i_0++) {
    $get_0(this$static._touchs, i_0) != null && dynamicCast($get_0(this$static._touchs, i_0), 10);
  }
}

function $getTapCount(this$static){
  if (this$static._touchs.array.length == 0)
    return 0;
  return dynamicCast($get_0(this$static._touchs, 0), 10)._tapCount;
}

function TouchEvent_0(type_0, touchs){
  this._eventType = type_0;
  this._touchs = touchs;
}

function create_5(type_0, touch){
  var touchs;
  touchs = new ArrayList_1(new Arrays$ArrayList(initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_Touch_2_classLit, 1), $intern_4, 10, 0, [touch])));
  return new TouchEvent_0(type_0, touchs);
}

defineClass(39, 1, {39:1}, TouchEvent_0);
var Lorg_glob3_mobile_generated_TouchEvent_2_classLit = createForClass('org.glob3.mobile.generated', 'TouchEvent', 39);
function $clinit_TouchEventType(){
  $clinit_TouchEventType = emptyMethod;
  Down = new TouchEventType('Down', 0);
  Up = new TouchEventType('Up', 1);
  Move = new TouchEventType('Move', 2);
  LongPress = new TouchEventType('LongPress', 3);
  DownUp = new TouchEventType('DownUp', 4);
}

function TouchEventType(enum$name, enum$ordinal){
  Enum.call(this, enum$name, enum$ordinal);
}

function values_10(){
  $clinit_TouchEventType();
  return initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_TouchEventType_2_classLit, 1), $intern_4, 74, 0, [Down, Up, Move, LongPress, DownUp]);
}

defineClass(74, 15, {3:1, 26:1, 15:1, 74:1}, TouchEventType);
var Down, DownUp, LongPress, Move, Up;
var Lorg_glob3_mobile_generated_TouchEventType_2_classLit = createForEnum('org.glob3.mobile.generated', 'TouchEventType', 74, values_10);
function URL_0(path){
  URL_1.call(this, path);
}

function URL_1(path){
  this._path = path;
}

defineClass(92, 1, {92:1}, URL_0, URL_1);
_.equals$ = function equals_21(obj){
  var other;
  if (this === obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  if (Lorg_glob3_mobile_generated_URL_2_classLit != getClass__Ljava_lang_Class___devirtual$(obj)) {
    return false;
  }
  other = dynamicCast(obj, 92);
  if ($equals(this._path, other._path)) {
    return true;
  }
  return false;
}
;
_.hashCode$ = function hashCode_26(){
  return getHashCode_0(this._path);
}
;
_.toString$ = function toString_40(){
  var isb;
  return isb = new StringBuilder_WebGL , isb._string += 'URL(' , $addString(isb, this._path) , isb._string += ')' , isb._string;
}
;
var Lorg_glob3_mobile_generated_URL_2_classLit = createForClass('org.glob3.mobile.generated', 'URL', 92);
function Vector2D(x_0, y_0){
  this._x = x_0;
  this._y = y_0;
}

defineClass(207, 1, {}, Vector2D);
_.toString$ = function toString_41(){
  var isb;
  return isb = new StringBuilder_WebGL , isb._string += '(V2D ' , $addDouble(isb, this._x) , isb._string += ', ' , $addDouble(isb, this._y) , isb._string += ')' , isb._string;
}
;
_._x = 0;
_._y = 0;
var Lorg_glob3_mobile_generated_Vector2D_2_classLit = createForClass('org.glob3.mobile.generated', 'Vector2D', 207);
function $add_17(this$static, v){
  return new Vector2F(this$static._x + v._x, this$static._y + v._y);
}

function $div_1(this$static){
  return new Vector2F(this$static._x / 3, this$static._y / 3);
}

function $length_0(this$static){
  return sqrt_0(this$static._x * this$static._x + this$static._y * this$static._y);
}

function $squaredDistanceTo_0(this$static, x_0, y_0){
  var dx, dy;
  dx = this$static._x - x_0;
  dy = this$static._y - y_0;
  return dx * dx + dy * dy;
}

function $sub_1(this$static, v){
  return new Vector2F(this$static._x - v._x, this$static._y - v._y);
}

function Vector2F(x_0, y_0){
  this._x = x_0;
  this._y = y_0;
}

defineClass(16, 1, {16:1}, Vector2F);
_._x = 0;
_._y = 0;
var Lorg_glob3_mobile_generated_Vector2F_2_classLit = createForClass('org.glob3.mobile.generated', 'Vector2F', 16);
function $description_7(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += '(V2I ';
  $addDouble(isb, this$static._x);
  isb._string += ', ';
  $addDouble(isb, this$static._y);
  isb._string += ')';
  s = isb._string;
  return s;
}

function Vector2I(x_0, y_0){
  this._x = x_0;
  this._y = y_0;
}

function Vector2I_0(that){
  this._x = that._x;
  this._y = that._y;
}

defineClass(67, 1, {}, Vector2I, Vector2I_0);
_.toString$ = function toString_42(){
  return $description_7(this);
}
;
_._x = 0;
_._y = 0;
var Lorg_glob3_mobile_generated_Vector2I_2_classLit = createForClass('org.glob3.mobile.generated', 'Vector2I', 67);
function $clinit_Vector3D(){
  $clinit_Vector3D = emptyMethod;
  zero = new Vector3D(0, 0, 0);
}

function $add_18(this$static, v){
  return new Vector3D(this$static._x + v._x, this$static._y + v._y, this$static._z + v._z);
}

function $asMutableVector3D(this$static){
  return new MutableVector3D_0(this$static._x, this$static._y, this$static._z);
}

function $axisAverage(this$static){
  return (this$static._x + this$static._y + this$static._z) / 3;
}

function $cross_0(this$static, other){
  return new Vector3D(this$static._y * other._z - this$static._z * other._y, this$static._z * other._x - this$static._x * other._z, this$static._x * other._y - this$static._y * other._x);
}

function $description_8(this$static){
  var isb, s;
  isb = new StringBuilder_WebGL;
  isb._string += '(V3D ';
  $addDouble(isb, this$static._x);
  isb._string += ', ';
  $addDouble(isb, this$static._y);
  isb._string += ', ';
  $addDouble(isb, this$static._z);
  isb._string += ')';
  s = isb._string;
  return s;
}

function $dot_0(this$static, v){
  return this$static._x * v._x + this$static._y * v._y + this$static._z * v._z;
}

function $isNan_1(this$static){
  return this$static._x != this$static._x || this$static._y != this$static._y || this$static._z != this$static._z;
}

function $length_1(this$static){
  return sqrt_0(this$static._x * this$static._x + this$static._y * this$static._y + this$static._z * this$static._z);
}

function $maxAxis(this$static){
  return this$static._x >= this$static._y && this$static._x >= this$static._z?this$static._x:this$static._y >= this$static._z?this$static._y:this$static._z;
}

function $minAxis(this$static){
  return this$static._x <= this$static._y && this$static._x <= this$static._z?this$static._x:this$static._y <= this$static._z?this$static._y:this$static._z;
}

function $normalized_0(this$static){
  var d;
  if (this$static._x != this$static._x || this$static._y != this$static._y || this$static._z != this$static._z) {
    return new Vector3D(NaN, NaN, NaN);
  }
  if (this$static._x == 0 && this$static._y == 0 && this$static._z == 0) {
    return zero;
  }
  d = sqrt_0(this$static._x * this$static._x + this$static._y * this$static._y + this$static._z * this$static._z);
  return new Vector3D(this$static._x / d, this$static._y / d, this$static._z / d);
}

function $projectionInPlane(this$static, normal){
  var axis_0, m, projected;
  axis_0 = new Vector3D(normal._y * this$static._z - normal._z * this$static._y, normal._z * this$static._x - normal._x * this$static._z, normal._x * this$static._y - normal._y * this$static._x);
  m = createRotationMatrix(new Angle(90, $intern_43), axis_0);
  projected = $normalized_0(new Vector3D(normal._x * m._m00 + normal._y * m._m01 + normal._z * m._m02, normal._x * m._m10 + normal._y * m._m11 + normal._z * m._m12, normal._x * m._m20 + normal._y * m._m21 + normal._z * m._m22));
  return $times_2(projected, sqrt_0(this$static._x * this$static._x + this$static._y * this$static._y + this$static._z * this$static._z));
}

function $rotateAroundAxis_0(this$static, axis_0, theta){
  var cosTheta, m, ms, sinTheta, u, v, w;
  u = axis_0._x;
  v = axis_0._y;
  w = axis_0._z;
  cosTheta = cos_0(theta._radians);
  sinTheta = sin_0(theta._radians);
  ms = axis_0._x * axis_0._x + axis_0._y * axis_0._y + axis_0._z * axis_0._z;
  m = sqrt_0(ms);
  return new Vector3D((u * (u * this$static._x + v * this$static._y + w * this$static._z) + (this$static._x * (v * v + w * w) - u * (v * this$static._y + w * this$static._z)) * cosTheta + m * (-w * this$static._y + v * this$static._z) * sinTheta) / ms, (v * (u * this$static._x + v * this$static._y + w * this$static._z) + (this$static._y * (u * u + w * w) - v * (u * this$static._x + w * this$static._z)) * cosTheta + m * (w * this$static._x - u * this$static._z) * sinTheta) / ms, (w * (u * this$static._x + v * this$static._y + w * this$static._z) + (this$static._z * (u * u + v * v) - w * (u * this$static._x + v * this$static._y)) * cosTheta + m * (-(v * this$static._x) + u * this$static._y) * sinTheta) / ms);
}

function $signedAngleBetween(this$static, other, up){
  var angle;
  angle = fromRadians(angleInRadiansBetween_0(this$static, other));
  if ($dot_0(new Vector3D(this$static._y * other._z - this$static._z * other._y, this$static._z * other._x - this$static._x * other._z, this$static._x * other._y - this$static._y * other._x), up) > 0) {
    return angle;
  }
  return $times(angle, -1);
}

function $sub_2(this$static, v){
  return new Vector3D(this$static._x - v._x, this$static._y - v._y, this$static._z - v._z);
}

function $times_2(this$static, magnitude){
  return new Vector3D(this$static._x * magnitude, this$static._y * magnitude, this$static._z * magnitude);
}

function $times_3(this$static, v){
  return new Vector3D(this$static._x * v._x, this$static._y * v._y, this$static._z * v._z);
}

function $transformedBy_0(this$static, m, homogeneus){
  return new Vector3D(this$static._x * m._m00 + this$static._y * m._m01 + this$static._z * m._m02 + homogeneus * m._m03, this$static._x * m._m10 + this$static._y * m._m11 + this$static._z * m._m12 + homogeneus * m._m13, this$static._x * m._m20 + this$static._y * m._m21 + this$static._z * m._m22 + homogeneus * m._m23);
}

function Vector3D(x_0, y_0, z_0){
  $clinit_Vector3D();
  this._x = x_0;
  this._y = y_0;
  this._z = z_0;
}

function Vector3D_0(v){
  $clinit_Vector3D();
  this._x = v._x;
  this._y = v._y;
  this._z = v._z;
}

function angleInRadiansBetween_0(a, b){
  var aLength, a_x, a_y, a_z, bLength, b_x, b_y, b_z;
  $clinit_Vector3D();
  var c;
  c = (aLength = sqrt_0(a._x * a._x + a._y * a._y + a._z * a._z) , a_x = a._x / aLength , a_y = a._y / aLength , a_z = a._z / aLength , bLength = sqrt_0(b._x * b._x + b._y * b._y + b._z * b._z) , b_x = b._x / bLength , b_y = b._y / bLength , b_z = b._z / bLength , a_x * b_x + a_y * b_y + a_z * b_z);
  c > 1?(c = 1):c < -1 && (c = -1);
  return acos_0(c);
}

defineClass(5, 1, {}, Vector3D, Vector3D_0);
_.toString$ = function toString_43(){
  return $description_8(this);
}
;
_._x = 0;
_._y = 0;
_._z = 0;
var zero;
var Lorg_glob3_mobile_generated_Vector3D_2_classLit = createForClass('org.glob3.mobile.generated', 'Vector3D', 5);
function $normalized_1(this$static){
  var d;
  d = sqrt_0(this$static._x * this$static._x + this$static._y * this$static._y + this$static._z * this$static._z);
  return new Vector3F(this$static._x / d, this$static._y / d, this$static._z / d);
}

function Vector3F(x_0, y_0, z_0){
  this._x = x_0;
  this._y = y_0;
  this._z = z_0;
}

defineClass(40, 1, {40:1}, Vector3F);
_._x = 0;
_._y = 0;
_._z = 0;
var Lorg_glob3_mobile_generated_Vector3F_2_classLit = createForClass('org.glob3.mobile.generated', 'Vector3F', 40);
function $tryToNotifyListener(this$static, visibleSector, rc){
  var now_0;
  if (eq(this$static._stabilizationIntervalInMS, {l:0, m:0, h:0})) {
    if (!this$static._lastSector || !$isEquals_3(this$static._lastSector, visibleSector)) {
      this$static._lastSector = new Sector_0(visibleSector);
      null.nullMethod($getGeodeticPosition(rc._currentCamera));
    }
  }
   else {
    now_0 = (!this$static._timer && (this$static._timer = new Timer_WebGL) , fromDouble(now_1()));
    if (!this$static._lastSector || !$isEquals_3(this$static._lastSector, visibleSector)) {
      this$static._lastSector = new Sector_0(visibleSector);
      this$static._whenNotifyInMS = add_0(now_0, this$static._stabilizationIntervalInMS);
    }
    if (neq(this$static._whenNotifyInMS, {l:0, m:0, h:0})) {
      if (gte_0(now_0, this$static._whenNotifyInMS)) {
        null.nullMethod($getGeodeticPosition(rc._currentCamera));
        this$static._whenNotifyInMS = {l:0, m:0, h:0};
      }
    }
  }
}

function VisibleSectorListenerEntry(stabilizationInterval){
  this._stabilizationIntervalInMS = stabilizationInterval._milliseconds;
  this._lastSector = null;
  this._timer = null;
  this._whenNotifyInMS = {l:0, m:0, h:0};
}

defineClass(145, 1, {145:1}, VisibleSectorListenerEntry);
_._stabilizationIntervalInMS = {l:0, m:0, h:0};
_._whenNotifyInMS = {l:0, m:0, h:0};
var Lorg_glob3_mobile_generated_VisibleSectorListenerEntry_2_classLit = createForClass('org.glob3.mobile.generated', 'VisibleSectorListenerEntry', 145);
function $_createImage(this$static, listener, autodelete){
  var jsImage = new Image;
  jsImage.onload = function(){
    var result = new Image_WebGL(jsImage);
    listener.imageCreated(result);
    autodelete && listener.dispose();
  }
  ;
  jsImage.src = this$static._domCanvas.toDataURL();
}

function $_drawImage(this$static, image, left, top_0, width_0, height, transparency){
  var context = this$static._domCanvasContext;
  var imageJS = image.getImage();
  context.globalAlpha = transparency;
  context.drawImage(imageJS, left, top_0, width_0, height);
  context.globalAlpha = 1;
}

function $_fillRectangle(this$static, left, top_0, width_0, height){
  var context = this$static._domCanvasContext;
  context.fillRect(left, top_0, width_0, height);
}

function $roundRect(this$static, x_0, y_0, width_0, height, radius, fill, stroke){
  var context = this$static._domCanvasContext;
  context.beginPath();
  context.moveTo(x_0 + radius, y_0);
  context.lineTo(x_0 + width_0 - radius, y_0);
  context.quadraticCurveTo(x_0 + width_0, y_0, x_0 + width_0, y_0 + radius);
  context.lineTo(x_0 + width_0, y_0 + height - radius);
  context.quadraticCurveTo(x_0 + width_0, y_0 + height, x_0 + width_0 - radius, y_0 + height);
  context.lineTo(x_0 + radius, y_0 + height);
  context.quadraticCurveTo(x_0, y_0 + height, x_0, y_0 + height - radius);
  context.lineTo(x_0, y_0 + radius);
  context.quadraticCurveTo(x_0, y_0, x_0 + radius, y_0);
  context.closePath();
  fill && context.fill();
  stroke && context.stroke();
}

function $tryToSetCurrentFontToContext(this$static){
  var currentDOMFont = this$static._currentDOMFont;
  currentDOMFont && (this$static._domCanvasContext.font = currentDOMFont);
}

function Canvas_WebGL(){
  var canvas, context;
  this._canvasWidth = -1;
  this._canvasHeight = -1;
  this._currentFont = null;
  canvas = $doc.createElement('canvas');
  context = canvas.getContext('2d');
  this._domCanvas = canvas;
  this._domCanvasContext = context;
}

function createDOMColor(color_0){
  var a, b, g, r;
  if (!color_0) {
    return null;
  }
  r = round_0(255 * color_0._red);
  g = round_0(255 * color_0._green);
  b = round_0(255 * color_0._blue);
  a = color_0._alpha;
  return 'rgba(' + r + ', ' + g + ', ' + b + ', ' + a + ')';
}

defineClass(127, 373, {}, Canvas_WebGL);
_.tryToSetCurrentFontToContext = function tryToSetCurrentFontToContext(){
  $tryToSetCurrentFontToContext(this);
}
;
_._currentFontSize = 0;
var Lorg_glob3_mobile_specific_Canvas_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'Canvas_WebGL', 127);
function $copyValueOfRotationMatrix_0(this$static, rotationMatrix){
  var cs;
  if (isNaN_0(this$static._beta) || isNaN_0(this$static._gamma) || isNaN_0(this$static._alpha)) {
    rotationMatrix._isValid = false;
    $logError(_instance_3, 'Browser attitude data is undefined.', initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
  }
   else {
    cs = $applyTaitBryanAngles(new CoordinateSystem_0(($clinit_Vector3D() , new Vector3D(1, 0, 0)), new Vector3D(0, 1, 0), new Vector3D(0, 0, 1), zero), fromDegrees(this$static._alpha), fromDegrees(this$static._gamma), fromDegrees(-this$static._beta));
    $copyValue(rotationMatrix, new MutableMatrix44D_0(cs._x._x, cs._x._y, cs._x._z, 0, cs._y._x, cs._y._y, cs._y._z, 0, cs._z._x, cs._z._y, cs._z._z, 0, 0, 0, 0, 1));
  }
}

function $initInterfaceOrientation(devAtt){
  try {
    if ($wnd.screen.orientation !== undefined) {
      console.log('IO CHROME');
      devAtt.storeInterfaceOrientation($wnd.screen.orientation.type);
    }
     else {
      if ($wnd.screen.mozOrientation !== undefined) {
        console.log('IO MOZ');
        devAtt.storeInterfaceOrientation($wnd.screen.mozOrientation);
      }
    }
  }
   catch (err) {
    console.error('Unable to track Interface Orientation. ' + err);
  }
}

function $trackInterfaceOrientation(devAtt){
  try {
    if ($wnd.screen.orientation !== undefined) {
      console.log('IO CHROME');
      $wnd.screen.orientation.onchange = function(){
        devAtt.storeInterfaceOrientation($wnd.screen.orientation.type);
      }
      ;
    }
     else {
      if ($wnd.screen.mozOrientation !== undefined) {
        console.log('IO MOZ');
        $wnd.screen.onmozorientationchange = function(event_0){
          event_0.preventDefault();
          devAtt.storeInterfaceOrientation($wnd.screen.orientation.type);
        }
        ;
      }
    }
  }
   catch (err) {
    console.error('Unable to track Interface Orientation. ' + err);
  }
}

function DeviceAttitude_WebGL(){
  this._camCSPortrait = null;
  this._camCSPortraitUD = null;
  this._camCSLL = null;
  this._camCSLR = null;
  $trackInterfaceOrientation(this);
  $initInterfaceOrientation(this);
}

function trackGyroscope(devAtt){
  try {
    $wnd.addEventListener('deviceorientation', function(event_0){
      devAtt._beta = event_0.beta;
      devAtt._gamma = event_0.gamma;
      devAtt._alpha = event_0.alpha;
      console.log('TG EVENT B:' + devAtt._beta + ' G:' + devAtt._gamma + ' A:' + devAtt._alpha);
    }
    , false);
  }
   catch (err) {
  }
}

defineClass(324, 317, {}, DeviceAttitude_WebGL);
_.storeInterfaceOrientation = function storeInterfaceOrientation(orientation){
  $equalsIgnoreCase(orientation, 'portrait-primary')?(this._currentIO = ($clinit_InterfaceOrientation() , PORTRAIT)):$equalsIgnoreCase(orientation, 'portrait-secondary')?(this._currentIO = ($clinit_InterfaceOrientation() , PORTRAIT_UPSIDEDOWN)):$equalsIgnoreCase(orientation, 'landscape-primary')?(this._currentIO = ($clinit_InterfaceOrientation() , LANDSCAPE_RIGHT)):$equalsIgnoreCase(orientation, 'landscape-secondary') && (this._currentIO = ($clinit_InterfaceOrientation() , LANDSCAPE_LEFT));
  $logInfo(_instance_3, 'SIO ' + orientation + ' -> ' + $toString(this._currentIO), initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
}
;
_._alpha = NaN;
_._beta = NaN;
_._currentIO = null;
_._gamma = NaN;
_._isTracking = false;
var Lorg_glob3_mobile_specific_DeviceAttitude_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'DeviceAttitude_WebGL', 324);
function DeviceInfo_WebGL(){
}

defineClass(377, 474, {}, DeviceInfo_WebGL);
var Lorg_glob3_mobile_specific_DeviceInfo_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'DeviceInfo_WebGL', 377);
function $startTrackingLocationJS(devLoc){
  if ('geolocation' in navigator) {
    devLoc._watchId = navigator.geolocation.watchPosition(devLoc.onPositionChanged, devLoc.onError);
    return true;
  }
  return false;
}

function DeviceLocation_WebGL(){
}

defineClass(325, 467, {}, DeviceLocation_WebGL);
_.onError = function onError(error){
  console.log(error);
}
;
_.onPositionChanged = function onPositionChanged(location_0){
  _lat = location_0.coords.latitude;
  _lon = location_0.coords.longitude;
  if (location_0.coords.altitude == null) {
    console.log('Device altitude is undefined, assuming 0');
    _altitude = 0;
  }
   else {
    _altitude = location_0.coords.altitude;
  }
}
;
_._isTracking = false;
_._watchId = -1;
var _altitude = NaN, _lat = NaN, _lon = NaN;
var Lorg_glob3_mobile_specific_DeviceLocation_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'DeviceLocation_WebGL', 325);
function $cancelRequest(this$static, requestId){
  var e, found, handler, iter;
  if (lt(requestId, {l:0, m:0, h:0})) {
    return;
  }
  this$static._cancelsCounter = add_0(this$static._cancelsCounter, {l:1, m:0, h:0});
  found = false;
  iter = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this$static._queuedHandlers)).this$01);
  while ($hasNext(iter) && !found) {
    e = (checkStructuralChange(iter.this$01, iter) , checkCriticalElement($hasNext(iter)) , iter.last = iter.current , dynamicCast(iter.current.next_0(), 22));
    handler = dynamicCast(e.getValue(), 109);
    if (handler.removeListenerForRequestId(requestId)) {
      handler.hasListener() || $remove_3(iter);
      found = true;
    }
  }
  if (!found) {
    iter = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this$static._downloadingHandlers)).this$01);
    while ($hasNext(iter) && !found) {
      e = (checkStructuralChange(iter.this$01, iter) , checkCriticalElement($hasNext(iter)) , iter.last = iter.current , dynamicCast(iter.current.next_0(), 22));
      handler = dynamicCast(e.getValue(), 109);
      handler.cancelListenerForRequestId(requestId) && (found = true);
    }
  }
}

function $getHandlerToRun(this$static){
  var e, handler, it, priority, selectedHandler, selectedPriority, selectedURL, url_0;
  selectedPriority = {l:663296, m:4194280, h:$intern_8};
  selectedHandler = null;
  selectedURL = null;
  it = new AbstractHashMap$EntrySetIterator((new AbstractHashMap$EntrySet(this$static._queuedHandlers)).this$01);
  while ($hasNext(it)) {
    e = (checkStructuralChange(it.this$01, it) , checkCriticalElement($hasNext(it)) , it.last = it.current , dynamicCast(it.current.next_0(), 22));
    url_0 = dynamicCast(e.getKey(), 92);
    handler = dynamicCast(e.getValue(), 109);
    priority = handler.getPriority();
    if (gt(priority, selectedPriority)) {
      selectedPriority = priority;
      selectedHandler = handler;
      selectedURL = url_0;
    }
  }
  if (selectedHandler) {
    $remove_2(this$static._queuedHandlers, selectedURL);
    $put(this$static._downloadingHandlers, selectedURL, selectedHandler);
  }
  return selectedHandler;
}

function $getProxiedURL(this$static, url_0){
  var urlPath;
  if (this$static._proxy == null) {
    return url_0;
  }
  urlPath = url_0._path;
  if (!$equals(urlPath.substr(0, 7), 'http://') && !$equals(urlPath.substr(0, 8), 'https://')) {
    return url_0;
  }
  return new URL_0(this$static._proxy + urlPath);
}

function $removeDownloadingHandlerForUrl(this$static, url_0){
  $remove_2(this$static._downloadingHandlers, url_0);
}

function $requestImage_0(this$static, url_0, priority, listener){
  var $tmp, handler, proxyUrl, requestId;
  proxyUrl = $getProxiedURL(this$static, url_0);
  this$static._requestsCounter = add_0(this$static._requestsCounter, {l:1, m:0, h:0});
  requestId = ($tmp = this$static._requestIdCounter , this$static._requestIdCounter = add_0(this$static._requestIdCounter, {l:1, m:0, h:0}) , $tmp);
  handler = dynamicCast($get(this$static._downloadingHandlers, proxyUrl), 109);
  if (!!handler && handler.isRequestingImage()) {
    handler.addListener(listener, true, priority, requestId);
  }
   else {
    handler = dynamicCast($get(this$static._queuedHandlers, proxyUrl), 109);
    if (!!handler && handler.isRequestingImage()) {
      handler.addListener(listener, true, priority, requestId);
    }
     else {
      handler = org_glob3_mobile_specific_Downloader_1WebGL_1Handler();
      handler.init(proxyUrl, listener, true, priority, requestId);
      $put(this$static._queuedHandlers, proxyUrl, handler);
    }
  }
  return requestId;
}

function $sendRequest(this$static){
  $schedule(this$static._timer, this$static._delayMillis);
}

function $start_0(this$static){
  $schedule(this$static._timer, this$static._delayMillis);
}

function $statistics(this$static){
  var sb;
  sb = new StringBuilder_WebGL;
  sb._string += 'Downloader_WebGL(downloading=';
  $addInt(sb, this$static._downloadingHandlers.size_0);
  sb._string += ', queued=';
  $addInt(sb, this$static._queuedHandlers.size_0);
  sb._string += ', totalRequests=';
  $addLong(sb, this$static._requestsCounter);
  sb._string += ', totalCancels=';
  $addLong(sb, this$static._cancelsCounter);
  return sb._string;
}

function Downloader_WebGL(){
  var thisDownloader, s, i_0;
  this._maxConcurrentOperationCount = 8;
  this._requestIdCounter = {l:1, m:0, h:0};
  this._requestsCounter = {l:0, m:0, h:0};
  this._cancelsCounter = {l:0, m:0, h:0};
  this._downloadingHandlers = new HashMap;
  this._queuedHandlers = new HashMap;
  this._delayMillis = 10;
  this._proxy = (s = $doc.location.href , i_0 = s.indexOf('#') , i_0 != -1 && (s = s.substring(0, i_0)) , i_0 = s.indexOf('?') , i_0 != -1 && (s = s.substring(0, i_0)) , i_0 = s.lastIndexOf('/') , i_0 != -1 && (s = s.substring(0, i_0)) , s.length > 0?s + '/':'') + 'proxy?url=';
  thisDownloader = this;
  this._timer = new Downloader_WebGL$1(this, thisDownloader);
}

defineClass(340, 455, {}, Downloader_WebGL);
_._cancelsCounter = {l:0, m:0, h:0};
_._delayMillis = 0;
_._maxConcurrentOperationCount = 0;
_._requestIdCounter = {l:0, m:0, h:0};
_._requestsCounter = {l:0, m:0, h:0};
var Lorg_glob3_mobile_specific_Downloader_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'Downloader_WebGL', 340);
function Downloader_WebGL$1(this$0, val$thisDownloader){
  this.this$01 = this$0;
  this.val$thisDownloader2 = val$thisDownloader;
  Timer.call(this);
}

defineClass(341, 189, {}, Downloader_WebGL$1);
_.run = function run(){
  var handler;
  if (this.this$01._downloadingHandlers.size_0 < this.this$01._maxConcurrentOperationCount) {
    handler = $getHandlerToRun(this.this$01);
    !!handler && handler.runWithDownloader(this.val$thisDownloader2);
  }
  $sendRequest(this.this$01);
}
;
var Lorg_glob3_mobile_specific_Downloader_1WebGL$1_2_classLit = createForClass('org.glob3.mobile.specific', 'Downloader_WebGL/1', 341);
function $log_4(msg){
  !!_instance_3 && $logError(_instance_3, 'Downloader_WebGL_HandlerImpl' + msg, initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
}

function Downloader_WebGL_Handler_DefaultImpl(){
}

defineClass(412, 1, {109:1}, Downloader_WebGL_Handler_DefaultImpl);
_.addListener = function addListener(listener, deleteListener, priority, requestId){
  var entry;
  entry = new ListenerEntry(listener, deleteListener, requestId);
  $add_3(this._listeners, entry);
  gt(priority, this._priority) && (this._priority = priority);
}
;
_.cancelListenerForRequestId = function cancelListenerForRequestId(requestId){
  var canceled, entry, iter;
  canceled = false;
  iter = new AbstractList$IteratorImpl(this._listeners);
  while (iter.i < iter.this$01_0.size_1() && !canceled) {
    entry = (checkCriticalElement(iter.i < iter.this$01_0.size_1()) , dynamicCast(iter.this$01_0.get_1(iter.last = iter.i++), 81));
    if (eq(entry._requestId, requestId)) {
      entry._canceled && $log_5(': Listener for requestId=' + toString_8(entry._requestId) + ' already canceled');
      entry._canceled = true;
      canceled = true;
    }
  }
  return canceled;
}
;
_.getPriority = function getPriority(){
  return this._priority;
}
;
_.hasListener = function hasListener(){
  return this._listeners.array.length != 0;
}
;
_.init = function init_13(url_0, imageListener, deleteListener, priority, requestId){
  var entry;
  this._priority = priority;
  this._url = url_0;
  this._listeners = new ArrayList;
  entry = new ListenerEntry(imageListener, deleteListener, requestId);
  $add_3(this._listeners, entry);
  this._requestingImage = true;
}
;
_.isRequestingImage = function isRequestingImage(){
  return this._requestingImage;
}
;
_.jsCreateImageFromBlob = function jsCreateImageFromBlob(xhrStatus, blob){
  var that = this;
  var auxImg = new Image;
  var imgURL = $wnd.g3mURL.createObjectURL(blob);
  auxImg.onload = function(){
    that.processResponse(xhrStatus, auxImg);
    $wnd.g3mURL.revokeObjectURL(imgURL);
  }
  ;
  auxImg.onerror = function(){
    that.processResponse(xhrStatus, null);
    $wnd.g3mURL.revokeObjectURL(imgURL);
  }
  ;
  auxImg.onabort = function(){
    that.processResponse(xhrStatus, null);
    $wnd.g3mURL.revokeObjectURL(imgURL);
  }
  ;
  auxImg.src = imgURL;
}
;
_.jsRequest = function jsRequest(url_0){
  var that = this;
  var xhr = new XMLHttpRequest;
  xhr.open('GET', url_0, true);
  xhr.responseType = that._requestingImage?'blob':'arraybuffer';
  xhr.onload = function(){
    if (xhr.readyState == 4) {
      that.removeFromDownloaderDownloadingHandlers();
      if (xhr.status === 200) {
        that._requestingImage?that.jsCreateImageFromBlob(xhr.status, xhr.response):that.processResponse(xhr.status, xhr.response);
      }
       else {
        console.log('Error Retrieving Data!');
        that.processResponse(xhr.status, null);
      }
    }
  }
  ;
  xhr.send();
}
;
_.processResponse = function processResponse(statusCode, data_0){
  var dataIsValid, entry, entry$iterator;
  dataIsValid = !!data_0 && statusCode == 200;
  if (dataIsValid) {
    for (entry$iterator = new AbstractList$IteratorImpl(this._listeners); entry$iterator.i < entry$iterator.this$01_0.size_1();) {
      entry = (checkCriticalElement(entry$iterator.i < entry$iterator.this$01_0.size_1()) , dynamicCast(entry$iterator.this$01_0.get_1(entry$iterator.last = entry$iterator.i++), 81));
      if (entry._canceled) {
        $onCanceledDownload(entry, this._url, data_0);
        $onCancel_0(entry);
      }
       else {
        $onDownload_0(entry, this._url, data_0);
      }
    }
  }
   else {
    $log_4(': Error runWithDownloader: statusCode=' + statusCode + ', data=' + data_0 + ', url=' + this._url._path);
    for (entry$iterator = new AbstractList$IteratorImpl(this._listeners); entry$iterator.i < entry$iterator.this$01_0.size_1();) {
      entry = (checkCriticalElement(entry$iterator.i < entry$iterator.this$01_0.size_1()) , dynamicCast(entry$iterator.this$01_0.get_1(entry$iterator.last = entry$iterator.i++), 81));
      $onError_0(entry, this._url);
    }
  }
}
;
_.removeFromDownloaderDownloadingHandlers = function removeFromDownloaderDownloadingHandlers(){
  $removeDownloadingHandlerForUrl(this._dl, this._url);
}
;
_.removeListenerForRequestId = function removeListenerForRequestId(requestId){
  var entry, iter, removed;
  removed = false;
  iter = new AbstractList$IteratorImpl(this._listeners);
  while (iter.i < iter.this$01_0.size_1()) {
    entry = (checkCriticalElement(iter.i < iter.this$01_0.size_1()) , dynamicCast(iter.this$01_0.get_1(iter.last = iter.i++), 81));
    if (eq(entry._requestId, requestId)) {
      $onCancel_0(entry);
      $remove_4(iter);
      removed = true;
      break;
    }
  }
  return removed;
}
;
_.runWithDownloader = function runWithDownloader(downloader){
  this._dl = downloader;
  this.jsRequest(this._url._path);
}
;
_._priority = {l:0, m:0, h:0};
_._requestingImage = false;
var Lorg_glob3_mobile_specific_Downloader_1WebGL_1Handler_1DefaultImpl_2_classLit = createForClass('org.glob3.mobile.specific', 'Downloader_WebGL_Handler_DefaultImpl', 412);
function $clinit_Downloader_WebGL_Handler_WebkitImpl(){
  $clinit_Downloader_WebGL_Handler_WebkitImpl = emptyMethod;
  _isChrome = $wnd.navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
}

function Downloader_WebGL_Handler_WebkitImpl(){
  $clinit_Downloader_WebGL_Handler_WebkitImpl();
}

defineClass(413, 412, {109:1}, Downloader_WebGL_Handler_WebkitImpl);
_.jsRequest = function jsRequest_0(url_0){
  var that = this;
  var xhr = new XMLHttpRequest;
  xhr.open('GET', url_0, true);
  xhr.responseType = 'arraybuffer';
  xhr.onload = function(){
    if (xhr.readyState == 4) {
      that.removeFromDownloaderDownloadingHandlers();
      var response = null;
      if (xhr.status === 200) {
        if (that._requestingImage) {
          if (_isChrome) {
            var dataView = new DataView(xhr.response);
            response = new Blob([dataView], {type:'image/png'});
          }
           else {
            response = new Blob([xhr.response]);
          }
          that.jsCreateImageFromBlob(xhr.status, response);
        }
         else {
          response = xhr.response;
          that.processResponse(xhr.status, response);
        }
      }
       else {
        console.log('Error Retrieving Data!');
        that.processResponse(xhr.status, response);
      }
    }
  }
  ;
  xhr.send();
}
;
var _isChrome = false;
var Lorg_glob3_mobile_specific_Downloader_1WebGL_1Handler_1WebkitImpl_2_classLit = createForClass('org.glob3.mobile.specific', 'Downloader_WebGL_Handler_WebkitImpl', 413);
function Factory_WebGL(){
  this._deviceInfo = null;
  new HashMap;
}

defineClass(319, 316, {}, Factory_WebGL);
var Lorg_glob3_mobile_specific_Factory_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'Factory_WebGL', 319);
function $$init_0(this$static){
  var $tmp;
  this$static._id = ($tmp = _nextID , _nextID = add_0(_nextID, {l:1, m:0, h:0}) , $tmp);
}

function $dispose_15(this$static){
  if (this$static._webGLBuffer) {
    this$static._gl.deleteBuffer(this$static._webGLBuffer);
    this$static._webGLBuffer = null;
    this$static._gl = null;
  }
}

function $get_5(this$static, i_0){
  return this$static._buffer[i_0];
}

function FloatBuffer_WebGL(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15){
  var buffer;
  $$init_0(this);
  this._buffer = (buffer = new Float32Array(16) , buffer[0] = f0 , buffer[1] = f1 , buffer[2] = f2 , buffer[3] = f3 , buffer[4] = f4 , buffer[5] = f5 , buffer[6] = f6 , buffer[7] = f7 , buffer[8] = f8 , buffer[9] = f9 , buffer[10] = f10 , buffer[11] = f11 , buffer[12] = f12 , buffer[13] = f13 , buffer[14] = f14 , buffer[15] = f15 , buffer);
}

function FloatBuffer_WebGL_0(array, length_0){
  var i_0;
  $$init_0(this);
  this._buffer = new Float32Array(length_0);
  for (i_0 = 0; i_0 < length_0; i_0++) {
    this._buffer[i_0] = array[i_0];
  }
}

defineClass(69, 478, {}, FloatBuffer_WebGL, FloatBuffer_WebGL_0);
_.bindVBO = function bindVBO(gl){
  var buffer, buffer_0;
  if (!this._webGLBuffer) {
    this._gl = gl;
    this._webGLBuffer = this._gl.createBuffer();
  }
  buffer = this._webGLBuffer;
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
  if (this._bufferTimeStamp != 0) {
    this._bufferTimeStamp = 0;
    buffer_0 = this._buffer;
    gl.bufferData(gl.ARRAY_BUFFER, buffer_0, gl.STATIC_DRAW);
  }
  return this._webGLBuffer;
}
;
_.getBuffer = function getBuffer(){
  return this._buffer;
}
;
_._bufferTimeStamp = -1;
_._gl = null;
_._id = {l:0, m:0, h:0};
_._webGLBuffer = null;
var _nextID = {l:0, m:0, h:0};
var Lorg_glob3_mobile_specific_FloatBuffer_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'FloatBuffer_WebGL', 69);
function $createWidget(this$static){
  var i_0, shaders;
  if ($isWebGLSupported(this$static._nativeWidget)) {
    shaders = new BasicShadersGL2;
    for (i_0 = 0; i_0 < shaders._sources.array.length; i_0++) {
      $addGPUProgramSources(this$static, dynamicCast($get_0(shaders._sources, i_0), 21));
    }
    $setGL(this$static, this$static._nativeWidget._gl);
    $setG3MWidget(this$static._nativeWidget, $create_1(this$static));
    $startWidget(this$static._nativeWidget);
  }
  return this$static._nativeWidget;
}

function G3MBuilder_WebGL(){
  G3MBuilder_WebGL_0.call(this, new G3MWidget_WebGL);
}

function G3MBuilder_WebGL_0(widget){
  this._sources = new ArrayList;
  this._gl = null;
  this._downloader = null;
  this._threadUtils = null;
  this._planet = null;
  this._cameraConstraints = null;
  this._cameraRenderer = null;
  this._backgroundColor = null;
  this._planetRendererBuilder = null;
  this._busyRenderer = null;
  this._errorRenderer = null;
  this._renderers = null;
  this._periodicalTasks = null;
  this._logFPS = false;
  this._logDownloaderStatistics = false;
  this._sceneLighting = null;
  this._nativeWidget = widget;
}

defineClass(169, 252, {}, G3MBuilder_WebGL);
var Lorg_glob3_mobile_specific_G3MBuilder_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'G3MBuilder_WebGL', 169);
function $exportJSFunctions(this$static){
  var that = this$static;
  !$wnd.G3M && ($wnd.G3M = {});
  $wnd.G3M.takeScreenshotAsImage = $entry(function(){
    return that.takeScreenshotAsImage_0();
  }
  );
  $wnd.G3M.takeScreenshotAsBase64 = $entry(function(){
    return that.takeScreenshotAsBase64_0();
  }
  );
  $wnd.G3M.getCameraData = $entry(function(){
    return that.getCameraData_0();
  }
  );
  $wnd.G3M.newGeodetic3D = $entry(function(latitude, longitude, height){
    return that.newGeodetic3D_0(latitude, longitude, height);
  }
  );
  $wnd.G3M.moveCameraTo = $entry(function(position){
    that.moveCameraTo_0(position);
  }
  );
}

function $isWebGLSupported(this$static){
  return !!this$static._canvas && !!this$static._webGLContext;
}

function $jsAddResizeHandler(this$static, jsCanvas){
  var that = this$static;
  $wnd.g3mWidgetResize = function(){
    (jsCanvas.clientWidth != jsCanvas.parentNode.clientWidth || jsCanvas.clientHeight != jsCanvas.parentNode.clientHeight) && that.onSizeChanged(jsCanvas.parentNode.clientWidth, jsCanvas.parentNode.clientHeight);
  }
  ;
  $wnd.g3mWidgetResizeChecker = setInterval($wnd.g3mWidgetResize, 200);
}

function $jsDefineG3MBrowserObjects(this$static){
  var that = this$static;
  $wnd.g3mURL = $wnd.URL || $wnd.webkitURL;
  $wnd.requestAnimFrame = function(){
    return $wnd.requestAnimationFrame || $wnd.webkitRequestAnimationFrame || $wnd.mozRequestAnimationFrame || $wnd.oRequestAnimationFrame || $wnd.msRequestAnimationFrame || function(callback, element){
      return $wnd.setTimeout(callback, 16.666666666666668);
    }
    ;
  }
  ();
  $wnd.cancelAnimFrame = function(){
    return $wnd.cancelAnimationFrame || $wnd.webkitCancelAnimationFrame || $wnd.mozCancelAnimationFrame || $wnd.oCancelAnimationFrame || $wnd.msCancelAnimationFrame || $wnd.clearTimeout;
  }
  ();
  $wnd.g3mTick = function(){
    $wnd.requestAnimFrame($wnd.g3mTick);
    that.renderG3MWidget();
  }
  ;
}

function $jsGetWebGLContext(jsCanvas){
  var context = null;
  var contextNames = ['experimental-webgl', 'webgl', 'webkit-3d', 'moz-webgl'];
  if (jsCanvas != null) {
    for (var cn in contextNames) {
      try {
        context = jsCanvas.getContext(contextNames[cn], {preserveDrawingBuffer:true, alpha:false});
        context.viewportWidth = jsCanvas.width;
        context.viewportHeight = jsCanvas.height;
      }
       catch (e) {
      }
      if (context) {
        jsCanvas.addEventListener('webglcontextlost', function(event_0){
          event_0.preventDefault();
          $wnd.alert('webglcontextlost');
        }
        , false);
        break;
      }
    }
    context == null && alert('No WebGL context available');
  }
   else {
    alert('No canvas available');
  }
  return context;
}

function $jsOnResizeViewport(this$static, width_0, height){
  var webGLContext = this$static._webGLContext;
  webGLContext.viewport(0, 0, width_0, height);
  webGLContext.clear(webGLContext.COLOR_BUFFER_BIT | webGLContext.DEPTH_BUFFER_BIT);
}

function $onSizeChanged(this$static, w, h){
  if (this$static._width != w || this$static._height != h) {
    this$static._width = w;
    this$static._height = h;
    $setPixelSize(this$static, this$static._width, this$static._height);
    $setCoordinateSpaceWidth(this$static._canvas, this$static._width);
    $setCoordinateSpaceHeight(this$static._canvas, this$static._height);
    if (this$static._g3mWidget) {
      $onResizeViewportEvent_0(this$static._g3mWidget, this$static._width, this$static._height);
      $jsOnResizeViewport(this$static, this$static._width, this$static._height);
    }
  }
}

function $setAnimatedCameraPosition_2(this$static, position){
  $setAnimatedCameraPosition(this$static._g3mWidget, position, new Angle(0, 0), new Angle(-90, $intern_38));
}

function $setG3MWidget(this$static, widget){
  this$static._g3mWidget = widget;
}

function $startWidget(this$static){
  if (this$static._g3mWidget) {
    this$static._motionEventProcessor = new MotionEventProcessor(this$static._g3mWidget, $getElement(this$static._canvas));
    $jsAddResizeHandler(this$static, $getElement(this$static._canvas));
    $wnd.g3mTick();
  }
}

function G3MWidget_WebGL(){
  var nativeGL, logger, factory, stringUtils, stringBuilder, mathUtils, jsonParser, textUtils, devAtt, devLoc;
  logger = new Logger_WebGL;
  factory = new Factory_WebGL;
  stringUtils = new StringUtils_WebGL;
  stringBuilder = new StringBuilder_WebGL;
  mathUtils = new MathUtils_WebGL;
  jsonParser = new JSONParser_WebGL;
  textUtils = new TextUtils_WebGL;
  devAtt = new DeviceAttitude_WebGL;
  devLoc = new DeviceLocation_WebGL;
  initSingletons(logger, factory, stringUtils, stringBuilder, mathUtils, jsonParser, textUtils, devAtt, devLoc);
  this._canvas = createIfSupported();
  $getElement(this._canvas).id = '_g3m_canvas';
  if (!this._canvas) {
    $initWidget(this, createUnsupportedMessage('Your browser does not support the HTML5 Canvas.'));
    return;
  }
  this._webGLContext = $jsGetWebGLContext($getElement(this._canvas));
  if (!this._webGLContext) {
    $initWidget(this, createUnsupportedMessage('Your browser does not support WebGL.'));
    return;
  }
  $initWidget(this, this._canvas);
  $onSizeChanged(this, 1, 1);
  nativeGL = new NativeGL_WebGL(this._webGLContext);
  this._gl = new GL(nativeGL);
  $jsDefineG3MBrowserObjects(this);
  this.eventsToSink == -1?sinkEvents(($clinit_DOM() , this.element), 16121982 | (this.element.__eventBits || 0)):(this.eventsToSink |= 16121982);
  $exportJSFunctions(this);
}

function createUnsupportedMessage(message){
  var panel;
  panel = new VerticalPanel;
  $add_1(panel, new Label(message));
  $add_1(panel, new Label('Please upgrade your browser to get this running.'));
  return panel;
}

defineClass(230, 433, $intern_26, G3MWidget_WebGL);
_.getCameraData_0 = function getCameraData(){
  var widget = this._g3mWidget;
  var camera = widget.getCurrentCamera();
  var position = camera.getGeodeticPosition();
  var latitude = position._latitude;
  var longitude = position._longitude;
  var height = position._height;
  var heading = camera.getHeading();
  var pitch = camera.getPitch();
  var result = new Object;
  result.latitude = latitude._degrees;
  result.longitude = longitude._degrees;
  result.height = height;
  result.heading = heading._degrees;
  result.pitch = pitch._degrees;
  return result;
}
;
_.moveCameraTo_0 = function moveCameraTo(position){
  $setAnimatedCameraPosition_0(this._g3mWidget, new TimeInterval({l:5000, m:0, h:0}), position, new Angle(0, 0), new Angle(-90, $intern_38));
}
;
_.newGeodetic3D_0 = function newGeodetic3D(latitude, longitude, height){
  return new Geodetic3D(new Angle(latitude, latitude / 180 * $intern_37), new Angle(longitude, longitude / 180 * $intern_37), height);
}
;
_.onBrowserEvent = function onBrowserEvent_1(event_0){
  $setFocus(this._canvas);
  !!this._motionEventProcessor && $processEvent(this._motionEventProcessor, event_0);
  $onBrowserEvent(this, event_0);
  this.widget.onBrowserEvent(event_0);
}
;
_.onSizeChanged = function onSizeChanged(w, h){
  $onSizeChanged(this, w, h);
}
;
_.renderG3MWidget = function renderG3MWidget(){
  $render_2(this._g3mWidget, this._width, this._height);
}
;
_.takeScreenshotAsBase64_0 = function takeScreenshotAsBase64(){
  var javaCanvas = this._canvas;
  var canvas = javaCanvas.getCanvasElement();
  var dataURL = canvas.toDataURL('image/jpeg');
  return dataURL.replace(/^data:image\/(png|jpg|jpeg);base64,/, '');
}
;
_.takeScreenshotAsImage_0 = function takeScreenshotAsImage(){
  var javaCanvas = this._canvas;
  var canvas = javaCanvas.getCanvasElement();
  var image = new Image;
  image.width = canvas.width;
  image.height = canvas.height;
  image.src = canvas.toDataURL('image/jpeg');
  return image;
}
;
_._height = 0;
_._width = 0;
var Lorg_glob3_mobile_specific_G3MWidget_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'G3MWidget_WebGL', 230);
function $isEquals_4(this$static, that){
  return this$static._webGLTexture == that._webGLTexture;
}

function GLTextureId_WebGL(webGLTexture){
  this._webGLTexture = webGLTexture;
}

defineClass(482, 1, {215:1}, GLTextureId_WebGL);
_.getWebGLTexture = function getWebGLTexture(){
  return this._webGLTexture;
}
;
var Lorg_glob3_mobile_specific_GLTextureId_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'GLTextureId_WebGL', 482);
function GLUniformID_WebGL(id_0){
  this._id = id_0;
}

defineClass(488, 1, {}, GLUniformID_WebGL);
_.getId = function getId(){
  return this._id;
}
;
var Lorg_glob3_mobile_specific_GLUniformID_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'GLUniformID_WebGL', 488);
function $getHeight(this$static){
  var jsImage = this$static._imgObject;
  return jsImage?jsImage.height:0;
}

function $getWidth(this$static){
  var jsImage = this$static._imgObject;
  return jsImage?jsImage.width:0;
}

function Image_WebGL(data_0){
  this._imgObject = data_0;
  ($getWidth(this) <= 0 || $getHeight(this) <= 0) && (this._imgObject = null);
}

defineClass(129, 479, {}, Image_WebGL);
_.getImage = function getImage(){
  return this._imgObject;
}
;
var Lorg_glob3_mobile_specific_Image_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'Image_WebGL', 129);
function JSONParser_WebGL(){
}

defineClass(322, 465, {}, JSONParser_WebGL);
var Lorg_glob3_mobile_specific_JSONParser_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'JSONParser_WebGL', 322);
function $log_5(msg){
  !!_instance_3 && $logError(_instance_3, 'ListenerEntry' + msg, initValues(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), $intern_4, 1, 3, []));
}

function $onCancel_0(this$static){
  if (this$static._imageListener) {
    $onCancel(this$static._imageListener);
    this$static._deleteListener && $dispose_10(this$static._imageListener);
  }
}

function $onCanceledDownload(this$static, url_0, data_0){
  var image;
  if (this$static._imageListener) {
    image = new Image_WebGL(data_0);
    !image._imgObject?$log_5(": Can't create image from data (URL=" + url_0._path + ')'):undefined;
  }
}

function $onDownload_0(this$static, url_0, data_0){
  var image;
  if (this$static._imageListener) {
    image = new Image_WebGL(data_0);
    if (!image._imgObject) {
      $log_5(": Can't create image from data (URL=" + url_0._path + ')');
      $onError(this$static._imageListener, url_0);
    }
     else {
      $onDownload(this$static._imageListener, url_0, image);
    }
    this$static._deleteListener && $dispose_10(this$static._imageListener);
  }
}

function $onError_0(this$static, url_0){
  if (this$static._imageListener) {
    $onError(this$static._imageListener, url_0);
    this$static._deleteListener && $dispose_10(this$static._imageListener);
  }
}

function ListenerEntry(imageListener, deleteListener, requestId){
  this._imageListener = imageListener;
  this._deleteListener = deleteListener;
  this._requestId = requestId;
  this._canceled = false;
}

defineClass(81, 1, {81:1}, ListenerEntry);
_._canceled = false;
_._deleteListener = false;
_._requestId = {l:0, m:0, h:0};
var Lorg_glob3_mobile_specific_ListenerEntry_2_classLit = createForClass('org.glob3.mobile.specific', 'ListenerEntry', 81);
function $logError(this$static, x_0, LegacyParamArray){
  var res;
  if (this$static._level <= 3) {
    res = $stringFormat(x_0, LegacyParamArray);
    $log_1(this$static._logger, ($clinit_Level() , SEVERE), res);
  }
}

function $logInfo(this$static, x_0, LegacyParamArray){
  var res;
  if (this$static._level <= 1) {
    res = $stringFormat(x_0, LegacyParamArray);
    $log_1(this$static._logger, ($clinit_Level() , INFO), res);
  }
}

function $logWarning(this$static, x_0, LegacyParamArray){
  var res;
  if (this$static._level <= 2) {
    res = $stringFormat(x_0, LegacyParamArray);
    $log_1(this$static._logger, ($clinit_Level() , WARNING), res);
  }
}

function $stringFormat(format, args){
  var exp_0, i_0, nextSub, output;
  exp_0 = new RegExp('%[sdf]');
  nextSub = 0;
  output = '';
  for (i_0 = 0; i_0 < format.split(exp_0).length; i_0++) {
    output = output + format.split(exp_0)[i_0];
    i_0 + 1 < format.split(exp_0).length && nextSub < args.length && (output = output + args[nextSub]);
    ++nextSub;
  }
  return output;
}

function Logger_WebGL(){
  this._level = 1;
  this._logger = (new LoggerImplRegular , $ensureLogger(getLogManager(), ''));
}

defineClass(318, 285, {}, Logger_WebGL);
var Lorg_glob3_mobile_specific_Logger_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'Logger_WebGL', 318);
function $abs(v){
  return v <= 0?0 - v:v;
}

function MathUtils_WebGL(){
  new Random;
}

defineClass(321, 464, {}, MathUtils_WebGL);
var Lorg_glob3_mobile_specific_MathUtils_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'MathUtils_WebGL', 321);
function $clinit_MotionEventProcessor(){
  $clinit_MotionEventProcessor = emptyMethod;
  DELTA = new Vector2F(10, 0);
}

function $createTouches(this$static, event_0){
  var currentTouchPosition, currentTouchesPositions, i_0, jsTouch, jsTouches, jsTouchesSize, previousTouchPosition, touchId, touches;
  currentTouchesPositions = new HashMap;
  jsTouches = ($clinit_DOMImpl() , event_0).touches;
  jsTouchesSize = jsTouches.length;
  touches = new ArrayList_0(jsTouchesSize);
  for (i_0 = 0; i_0 < jsTouchesSize; i_0++) {
    jsTouch = jsTouches[i_0];
    currentTouchPosition = new Vector2F($getRelativeX(jsTouch, this$static._canvasElement), $getRelativeY(jsTouch, this$static._canvasElement));
    touchId = valueOf(jsTouch.identifier);
    $put_0(currentTouchesPositions.hashCodeMap, touchId, currentTouchPosition);
    previousTouchPosition = dynamicCast($get(this$static._previousTouchesPositions, touchId), 16);
    !previousTouchPosition && (previousTouchPosition = currentTouchPosition);
    $add_3(touches, new Touch_1(currentTouchPosition, previousTouchPosition));
  }
  this$static._previousTouchesPositions = currentTouchesPositions;
  return touches;
}

function $dispatchEvents(this$static, events){
  var event_0, event$index, event$max, scheduler;
  if (events.length > 0) {
    scheduler = ($clinit_SchedulerImpl() , INSTANCE);
    for (event$index = 0 , event$max = events.length; event$index < event$max; ++event$index) {
      event_0 = events[event$index];
      $scheduleDeferred(scheduler, new MotionEventProcessor$1(this$static, event_0));
    }
  }
}

function $jsAddMouseWheelListener(this$static){
  var thisInstance = this$static;
  var canvas = this$static._canvasElement;
  $wnd.g3mMouseWheelHandler = function(e){
    var e = $wnd.event || e;
    var delta = Math.max(-1, Math.min(1, e.wheelDelta || -e.detail));
    thisInstance.processMouseWheel(delta, e.clientX, e.clientY);
  }
  ;
  if (canvas) {
    if (canvas.addEventListener) {
      canvas.addEventListener('mousewheel', $wnd.g3mMouseWheelHandler, false);
      canvas.addEventListener('DOMMouseScroll', $wnd.g3mMouseWheelHandler, false);
    }
     else {
      canvas.attachEvent('onmousewheel', $wnd.g3mMouseWheelHandler);
    }
  }
}

function $processEvent(this$static, event_0){
  var touchEvent, currentMousePosition, touch, currentMousePosition_0, touch_0;
  touchEvent = null;
  switch ($clinit_DOM() , $eventGetTypeInt(($clinit_DOMImpl() , event_0).type)) {
    case $intern_19:
      impl_1.eventPreventDefault(event_0);
      touchEvent = new TouchEvent_0(($clinit_TouchEventType() , Down), $createTouches(this$static, event_0));
      break;
    case $intern_10:
      impl_1.eventPreventDefault(event_0);
      touchEvent = new TouchEvent_0(($clinit_TouchEventType() , Up), $createTouches(this$static, event_0));
      break;
    case $intern_20:
      impl_1.eventPreventDefault(event_0);
      touchEvent = new TouchEvent_0(($clinit_TouchEventType() , Move), $createTouches(this$static, event_0));
      break;
    case $intern_21:
      impl_1.eventPreventDefault(event_0);
      touchEvent = (this$static._previousTouchesPositions = new HashMap , null);
      break;
    case 64:
      touchEvent = $processMouseMove(this$static, event_0);
      break;
    case 4:
      touchEvent = $processMouseDown(this$static, event_0);
      break;
    case 8:
      touchEvent = $processMouseUp(this$static, event_0);
      break;
    case 2:
      touchEvent = (currentMousePosition = new Vector2F(toInt32(event_0.clientX || 0) - $getAbsoluteLeft(this$static._canvasElement), toInt32(event_0.clientY || 0) - $getAbsoluteTop(this$static._canvasElement)) , touch = new Touch_2(currentMousePosition, currentMousePosition, 2) , create_5(($clinit_TouchEventType() , Down), touch));
      break;
    case $intern_18:
      impl_1.eventPreventDefault(event_0);
      touchEvent = (this$static._mouseDown = false , currentMousePosition_0 = new Vector2F(toInt32(event_0.clientX || 0) - $getAbsoluteLeft(this$static._canvasElement), toInt32(event_0.clientY || 0) - $getAbsoluteTop(this$static._canvasElement)) , touch_0 = new Touch_1(currentMousePosition_0, this$static._previousMousePosition) , this$static._previousMousePosition = currentMousePosition_0 , create_5(($clinit_TouchEventType() , LongPress), touch_0));
      break;
    case $intern_17:
      impl_1.eventPreventDefault(event_0);
      break;
    default:return;
  }
  !!touchEvent && $dispatchEvents(this$static, initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_TouchEvent_2_classLit, 1), $intern_4, 39, 0, [touchEvent]));
}

function $processMouseDown(this$static, event_0){
  var currentMousePosition, touches;
  currentMousePosition = new Vector2F(toInt32(($clinit_DOMImpl() , event_0).clientX || 0) - $getAbsoluteLeft(this$static._canvasElement), toInt32(event_0.clientY || 0) - $getAbsoluteTop(this$static._canvasElement));
  touches = new ArrayList;
  this$static._mouseDown = true;
  if (event_0.shiftKey) {
    $add_3(touches, new Touch_1($sub_1(currentMousePosition, DELTA), $sub_1(this$static._previousMousePosition, DELTA)));
    $add_3(touches, new Touch_1(currentMousePosition, this$static._previousMousePosition));
    $add_3(touches, new Touch_1($add_17(currentMousePosition, DELTA), $add_17(this$static._previousMousePosition, DELTA)));
  }
   else {
    $add_3(touches, new Touch_1(currentMousePosition, this$static._previousMousePosition));
  }
  this$static._previousMousePosition = currentMousePosition;
  return new TouchEvent_0(($clinit_TouchEventType() , Down), touches);
}

function $processMouseMove(this$static, event_0){
  var currentMousePosition, touches;
  if (!this$static._mouseDown) {
    return null;
  }
  currentMousePosition = new Vector2F(toInt32(($clinit_DOMImpl() , event_0).clientX || 0) - $getAbsoluteLeft(this$static._canvasElement), toInt32(event_0.clientY || 0) - $getAbsoluteTop(this$static._canvasElement));
  touches = new ArrayList;
  if (event_0.shiftKey) {
    $add_3(touches, new Touch_1($sub_1(currentMousePosition, DELTA), $sub_1(this$static._previousMousePosition, DELTA)));
    $add_3(touches, new Touch_1(currentMousePosition, this$static._previousMousePosition));
    $add_3(touches, new Touch_1($add_17(currentMousePosition, DELTA), $add_17(this$static._previousMousePosition, DELTA)));
  }
   else {
    $add_3(touches, new Touch_1(currentMousePosition, this$static._previousMousePosition));
  }
  this$static._previousMousePosition = currentMousePosition;
  return new TouchEvent_0(($clinit_TouchEventType() , Move), touches);
}

function $processMouseUp(this$static, event_0){
  var currentMousePosition, touchType, touches;
  currentMousePosition = new Vector2F(toInt32(($clinit_DOMImpl() , event_0).clientX || 0) - $getAbsoluteLeft(this$static._canvasElement), toInt32(event_0.clientY || 0) - $getAbsoluteTop(this$static._canvasElement));
  touches = new ArrayList;
  this$static._mouseDown = false;
  if (event_0.shiftKey) {
    $add_3(touches, new Touch_1($sub_1(currentMousePosition, DELTA), $sub_1(this$static._previousMousePosition, DELTA)));
    $add_3(touches, new Touch_1(currentMousePosition, this$static._previousMousePosition));
    $add_3(touches, new Touch_1($add_17(currentMousePosition, DELTA), $add_17(this$static._previousMousePosition, DELTA)));
    touchType = ($clinit_TouchEventType() , Up);
  }
   else {
    $add_3(touches, new Touch_1(currentMousePosition, this$static._previousMousePosition));
    touchType = !!event_0.ctrlKey && impl_1.eventGetButton(event_0) == 1?($clinit_TouchEventType() , LongPress):($clinit_TouchEventType() , Up);
  }
  this$static._previousMousePosition = currentMousePosition;
  return new TouchEvent_0(touchType, touches);
}

function MotionEventProcessor(widget, canvasElement){
  $clinit_MotionEventProcessor();
  this._previousTouchesPositions = new HashMap;
  this._widget = widget;
  this._canvasElement = canvasElement;
  $jsAddMouseWheelListener(this);
}

defineClass(312, 1, {}, MotionEventProcessor);
_.processMouseWheel = function processMouseWheel(delta, x_0, y_0){
  var beginFirstPosition, beginSecondPosition, beginTouches, endFirstPosition, endSecondPosition, endTouches;
  beginFirstPosition = new Vector2F(x_0 - 10, y_0 - 10);
  beginSecondPosition = new Vector2F(x_0 + 10, y_0 + 10);
  beginTouches = new ArrayList_0(2);
  $add_3(beginTouches, new Touch_1(beginFirstPosition, beginFirstPosition));
  $add_3(beginTouches, new Touch_1(beginSecondPosition, beginSecondPosition));
  endFirstPosition = new Vector2F(beginFirstPosition._x - delta, beginFirstPosition._y - delta);
  endSecondPosition = new Vector2F(beginSecondPosition._x + delta, beginSecondPosition._y + delta);
  endTouches = new ArrayList_0(2);
  $add_3(endTouches, new Touch_1(endFirstPosition, beginFirstPosition));
  $add_3(endTouches, new Touch_1(endSecondPosition, beginSecondPosition));
  $dispatchEvents(this, initValues(getClassLiteralForArray(Lorg_glob3_mobile_generated_TouchEvent_2_classLit, 1), $intern_4, 39, 0, [new TouchEvent_0(($clinit_TouchEventType() , Down), beginTouches), new TouchEvent_0(Move, endTouches), new TouchEvent_0(Up, endTouches)]));
  this._previousMousePosition = new Vector2F(x_0, y_0);
}
;
_._mouseDown = false;
_._previousMousePosition = null;
var DELTA;
var Lorg_glob3_mobile_specific_MotionEventProcessor_2_classLit = createForClass('org.glob3.mobile.specific', 'MotionEventProcessor', 312);
function MotionEventProcessor$1(this$0, val$event){
  this.this$01 = this$0;
  this.val$event2 = val$event;
}

defineClass(313, 1, {}, MotionEventProcessor$1);
var Lorg_glob3_mobile_specific_MotionEventProcessor$1_2_classLit = createForClass('org.glob3.mobile.specific', 'MotionEventProcessor/1', 313);
function $attachShader_0(this$static, program, shader){
  var shaderList = this$static._shaderList;
  var jsoProgram = shaderList.get_1(program);
  var jsoShader = shaderList.get_1(shader);
  this$static._gl.attachShader(jsoProgram, jsoShader);
}

function $bindTexture_0(this$static, target, texture){
  var id_0 = texture.getWebGLTexture();
  this$static._gl.bindTexture(target, id_0);
}

function $blendFunc(this$static, sfactor, dfactor){
  this$static._gl.blendFunc(sfactor, dfactor);
}

function $clear_0(this$static, buffers){
  this$static._gl.clear(buffers);
}

function $clearColor(this$static, red, green, blue, alpha_0){
  this$static._gl.clearColor(red, green, blue, alpha_0);
}

function $compileShader_0(this$static, shader, source){
  var gl = this$static._gl;
  var shaderList = this$static._shaderList;
  var jsoShader = shaderList.get_1(shader);
  gl.shaderSource(jsoShader, source);
  gl.compileShader(jsoShader);
  return gl.getShaderParameter(jsoShader, gl.COMPILE_STATUS);
}

function $createProgram(this$static){
  var shaderList = this$static._shaderList;
  var jsoProgram = this$static._gl.createProgram();
  shaderList.add_1(jsoProgram);
  var id_0 = shaderList.size_1() - 1;
  return id_0;
}

function $createShader_0(this$static, type_0){
  var shaderList = this$static._shaderList;
  var gl = this$static._gl;
  var shaderType;
  switch (type_0) {
    case $clinit_ShaderType() , VERTEX_SHADER:
      shaderType = gl.VERTEX_SHADER;
      break;
    case FRAGMENT_SHADER:
      shaderType = gl.FRAGMENT_SHADER;
      break;
    default:$wnd.alert('Unknown shader type');
      return 0;
  }
  var shader = gl.createShader(shaderType);
  shaderList.add_1(shader);
  var id_0 = shaderList.size_1() - 1;
  return id_0;
}

function $cullFace(this$static, c){
  this$static._gl.cullFace(c);
}

function $deleteProgram_0(this$static, program){
  var shaderList = this$static._shaderList;
  var jsoProgram = shaderList.get_1(program);
  this$static._gl.deleteProgram(jsoProgram);
}

function $deleteShader(this$static, shader){
  var gl = this$static._gl;
  var shaderList = this$static._shaderList;
  var jsoShader = shaderList.get_1(shader);
  return true;
}

function $deleteTexture_0(this$static, texture){
  var textureID = texture.getWebGLTexture();
  this$static._gl.deleteTexture(textureID);
  return false;
}

function $disable(this$static, feature){
  this$static._gl.disable(feature);
}

function $disableVertexAttribArray_0(this$static, location_0){
  this$static._gl.disableVertexAttribArray(location_0);
}

function $drawArrays_0(this$static, mode, first, count){
  this$static._gl.drawArrays(mode, first, count);
}

function $drawElements_0(this$static, mode, count, indices){
  var gl = this$static._gl;
  var webGLBuffer = indices.getWebGLBuffer(gl);
  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, webGLBuffer);
  var array = indices.getBuffer();
  gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, array, gl.STATIC_DRAW);
  gl.drawElements(mode, count, gl.UNSIGNED_SHORT, 0);
}

function $enable(this$static, feature){
  this$static._gl.enable(feature);
}

function $enableVertexAttribArray_0(this$static, location_0){
  this$static._gl.enableVertexAttribArray(location_0);
}

function $genTextures(this$static, n){
  var array = new ArrayList;
  for (i = 0; i < n; i++) {
    var texture = this$static._gl.createTexture();
    var textureID = new GLTextureId_WebGL(texture);
    array.add_1(textureID);
  }
  return array;
}

function $generateMipmap(this$static, target){
  this$static._gl.generateMipmap(target);
}

function $getActiveAttribute(this$static, program, i_0){
  var progInt = program.getProgramID();
  var shaderList = this$static._shaderList;
  var jsoProgram = shaderList.get_1(progInt);
  var gl = this$static._gl;
  var info = gl.getActiveAttrib(jsoProgram, i_0);
  var id_0 = gl.getAttribLocation(jsoProgram, info.name);
  switch (info.type) {
    case gl.FLOAT_VEC3:
      return new GPUAttributeVec3Float(info.name, id_0);
    case gl.FLOAT_VEC4:
      return new GPUAttributeVec4Float(info.name, id_0);
    case gl.FLOAT_VEC2:
      return new GPUAttributeVec2Float(info.name, id_0);
    default:return null;
      break;
  }
}

function $getActiveUniform(this$static, program, i_0){
  var progInt = program.getProgramID();
  var shaderList = this$static._shaderList;
  var jsoProgram = shaderList.get_1(progInt);
  var gl = this$static._gl;
  var info = gl.getActiveUniform(jsoProgram, i_0);
  var id_0 = gl.getUniformLocation(jsoProgram, info.name);
  var glUniformID = new GLUniformID_WebGL(id_0);
  switch (info.type) {
    case gl.FLOAT_MAT4:
      return new GPUUniformMatrix4Float(info.name, glUniformID);
    case gl.FLOAT_VEC4:
      return new GPUUniformVec4Float(info.name, glUniformID);
    case gl.FLOAT:
      return new GPUUniformFloat(info.name, glUniformID);
    case gl.FLOAT_VEC2:
      return new GPUUniformVec2Float(info.name, glUniformID);
    case gl.FLOAT_VEC3:
      return new GPUUniformVec3Float(info.name, glUniformID);
    case gl.BOOL:
      return new GPUUniformBool(info.name, glUniformID);
    case gl.SAMPLER_2D:
      return new GPUUniformSampler2D(info.name, glUniformID);
    default:return null;
      break;
  }
}

function $getError(this$static){
  var gl = this$static._gl;
  var e = gl.getError();
  e == gl.INVALID_ENUM && console.error('NativeGL_WebGL: INVALID_ENUM');
  e == gl.INVALID_VALUE && console.error('NativeGL_WebGL: INVALID_VALUE');
  e == gl.INVALID_OPERATION && console.error('NativeGL_WebGL: INVALID_OPERATION');
  e == gl.OUT_OF_MEMORY && console.error('NativeGL_WebGL: INVALID_OPERATION');
  e == gl.CONTEXT_LOST_WEBGL && console.error('NativeGL_WebGL: CONTEXT_LOST_WEBGL');
  return Number(e);
}

function $getProgramiv_0(this$static, program, param){
  var progInt = program.getProgramID();
  var shaderList = this$static._shaderList;
  var jsoProgram = shaderList.get_1(progInt);
  var gl = this$static._gl;
  return gl.getProgramParameter(jsoProgram, param);
}

function $lineWidth(this$static, width_0){
  this$static._gl.lineWidth(width_0);
}

function $linkProgram_0(this$static, program){
  var gl = this$static._gl;
  var shaderList = this$static._shaderList;
  var jsoProgram = shaderList.get_1(program);
  gl.linkProgram(jsoProgram);
  return gl.getProgramParameter(jsoProgram, gl.LINK_STATUS);
}

function $pixelStorei(this$static, pname, param){
  this$static._gl.pixelStorei(pname, param);
}

function $polygonOffset(this$static, factor, units){
  this$static._gl.polygonOffset(factor, units);
}

function $printShaderInfoLog(this$static, shader){
  var gl = this$static._gl;
  var shaderList = this$static._shaderList;
  var jsoShader = shaderList.get_1(shader);
  !gl.getShaderParameter(jsoShader, gl.COMPILE_STATUS) && $wnd.alert('Error compiling shaders: ' + gl.getShaderInfoLog(jsoShader));
}

function $texImage2D(this$static, image, format){
  var img = image.getImage();
  var gl = this$static._gl;
  var TEXTURE_2D = gl.TEXTURE_2D;
  var UNSIGNED_BYTE = gl.UNSIGNED_BYTE;
  gl.texImage2D(TEXTURE_2D, 0, format, format, UNSIGNED_BYTE, img);
}

function $texParameteri(this$static, target, par, v){
  this$static._gl.texParameteri(target, par, v);
}

function $uniform1f_0(this$static, loc, x_0){
  var locId = loc.getId();
  this$static._gl.uniform1f(locId, x_0);
}

function $uniform1i_0(this$static, loc, v){
  var locId = loc.getId();
  this$static._gl.uniform1i(locId, v);
}

function $uniform2f_0(this$static, loc, x_0, y_0){
  var locId = loc.getId();
  this$static._gl.uniform2f(locId, x_0, y_0);
}

function $uniform3f_0(this$static, location_0, x_0, y_0, z_0){
  var locId = location_0.getId();
  this$static._gl.uniform3f(locId, x_0, y_0, z_0);
}

function $uniform4f_0(this$static, location_0, v0, v1, v2, v3){
  var locId = location_0.getId();
  this$static._gl.uniform4f(locId, v0, v1, v2, v3);
}

function $uniformMatrix4fv_0(this$static, location_0, transpose, matrix){
  var id_0 = location_0.getId();
  var buffer = matrix.getColumnMajorFloatBuffer();
  var value_0 = buffer.getBuffer();
  this$static._gl.uniformMatrix4fv(id_0, transpose, value_0);
}

function $useProgram_0(this$static, program){
  var progInt = program.getProgramID();
  var shaderList = this$static._shaderList;
  var jsoProgram = shaderList.get_1(progInt);
  this$static._gl.useProgram(jsoProgram);
}

function $vertexAttribPointer_0(this$static, index_0, size_0, normalized, stride, buffer){
  var gl = this$static._gl;
  var webGLBuffer = buffer.bindVBO(gl);
  gl.vertexAttribPointer(index_0, size_0, gl.FLOAT, normalized, stride, 0);
}

function NativeGL_WebGL(webGLContext){
  this._shaderList = new ArrayList;
  this._gl = webGLContext;
}

defineClass(306, 448, {}, NativeGL_WebGL);
var Lorg_glob3_mobile_specific_NativeGL_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'NativeGL_WebGL', 306);
function $dispose_16(this$static){
  if (this$static._webGLBuffer) {
    this$static._gl.deleteBuffer(this$static._webGLBuffer);
    this$static._webGLBuffer = null;
    this$static._gl = null;
  }
}

function ShortBuffer_WebGL(array, length_0){
  var i_0;
  _nextID_0 = add_0(_nextID_0, {l:1, m:0, h:0});
  this._buffer = new Uint16Array(length_0);
  for (i_0 = 0; i_0 < length_0; i_0++) {
    (array[i_0] < 0 || array[i_0] > $intern_3) && alert('EXCEDING SHORT RANGE IN UINT16 JAVASCRIPT BUFFER');
    this._buffer[i_0] = array[i_0];
  }
}

defineClass(151, 483, {}, ShortBuffer_WebGL);
_.getBuffer = function getBuffer_0(){
  return this._buffer;
}
;
_.getWebGLBuffer = function getWebGLBuffer(gl){
  if (!this._webGLBuffer) {
    this._gl = gl;
    this._webGLBuffer = this._gl.createBuffer();
  }
  return this._webGLBuffer;
}
;
_._gl = null;
_._webGLBuffer = null;
var _nextID_0 = {l:0, m:0, h:0};
var Lorg_glob3_mobile_specific_ShortBuffer_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'ShortBuffer_WebGL', 151);
function $addBool(this$static, b){
  this$static._string += b;
  return this$static;
}

function $addDouble(this$static, d){
  this$static._string += d;
  return this$static;
}

function $addFloat(this$static, f){
  this$static._string += f;
  return this$static;
}

function $addInt(this$static, i_0){
  this$static._string += i_0;
  return this$static;
}

function $addLong(this$static, l){
  this$static._string += toString_8(l);
  return this$static;
}

function $addString(this$static, s){
  this$static._string += s;
  return this$static;
}

function StringBuilder_WebGL(){
}

defineClass(14, 463, {}, StringBuilder_WebGL);
_._string = '';
var Lorg_glob3_mobile_specific_StringBuilder_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'StringBuilder_WebGL', 14);
function StringUtils_WebGL(){
}

defineClass(320, 462, {}, StringUtils_WebGL);
var Lorg_glob3_mobile_specific_StringUtils_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'StringUtils_WebGL', 320);
function TextUtils_WebGL(){
}

defineClass(323, 466, {}, TextUtils_WebGL);
var Lorg_glob3_mobile_specific_TextUtils_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'TextUtils_WebGL', 323);
function $invokeTask(this$static, task){
  var timer;
  timer = new ThreadUtils_WebGL$1(task);
  $schedule(timer, this$static._delayMillis);
}

function ThreadUtils_WebGL(){
  this._delayMillis = 10;
}

defineClass(342, 456, {}, ThreadUtils_WebGL);
_._delayMillis = 0;
var Lorg_glob3_mobile_specific_ThreadUtils_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'ThreadUtils_WebGL', 342);
function ThreadUtils_WebGL$1(val$task){
  this.val$task2 = val$task;
  Timer.call(this);
}

defineClass(343, 189, {}, ThreadUtils_WebGL$1);
_.run = function run_0(){
  $recreateTiles(this.val$task2._planetRenderer);
}
;
var Lorg_glob3_mobile_specific_ThreadUtils_1WebGL$1_2_classLit = createForClass('org.glob3.mobile.specific', 'ThreadUtils_WebGL/1', 343);
function $elapsedTimeInMilliseconds(this$static){
  return sub_0(fromDouble(now_1()), this$static._startTimeInMilliseconds);
}

function Timer_WebGL(){
  this._startTimeInMilliseconds = fromDouble(now_1());
}

defineClass(87, 472, {}, Timer_WebGL);
_._startTimeInMilliseconds = {l:0, m:0, h:0};
var Lorg_glob3_mobile_specific_Timer_1WebGL_2_classLit = createForClass('org.glob3.mobile.specific', 'Timer_WebGL', 87);
var I_classLit = createForPrimitive('int', 'I'), Lcom_google_gwt_lang_CollapsedPropertyHolder_2_classLit = createForClass('com.google.gwt.lang', 'CollapsedPropertyHolder', 419), Lcom_google_gwt_lang_JavaClassHierarchySetupUtil_2_classLit = createForClass('com.google.gwt.lang', 'JavaClassHierarchySetupUtil', 421), Lcom_google_gwt_lang_LongLibBase$LongEmul_2_classLit = createForClass('com.google.gwt.lang', 'LongLibBase/LongEmul', null), Lcom_google_gwt_lang_ModuleUtils_2_classLit = createForClass('com.google.gwt.lang', 'ModuleUtils', 424), D_classLit = createForPrimitive('double', 'D'), F_classLit = createForPrimitive('float', 'F'), S_classLit = createForPrimitive('short', 'S'), Ljava_util_Map$Entry_2_classLit = createForInterface('java.util', 'Map/Entry'), Lorg_glob3_mobile_generated_IGLTextureId_2_classLit = createForInterface('org.glob3.mobile.generated', 'IGLTextureId');
var $entry = registerEntry();
var gwtOnLoad = gwtOnLoad = gwtOnLoad_0;
addInitFunctions(init);
setGwtProperty('permProps', [[['locale', 'default'], ['user.agent', 'gecko1_8']], [['locale', 'default'], ['user.agent', 'ie10']], [['locale', 'default'], ['user.agent', 'ie8']], [['locale', 'default'], ['user.agent', 'ie9']], [['locale', 'default'], ['user.agent', 'safari']]]);
if (org_glob3_mobile_G3MWebGLTestingApplication) org_glob3_mobile_G3MWebGLTestingApplication.onScriptLoad(gwtOnLoad);})();