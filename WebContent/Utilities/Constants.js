"use strict";

var app = angular.module('app');

/**
 * Control String constants.
 *
 * @author Robin Rizvi
 * @since (2014-02-18.17:55:23)
 */
app.factory('Constants', function(ResourceManager) {
    var constants = {
        // Prefix/Suffix to be added to REST urls
        UrlPrefix: function() {
        	return ResourceManager.get("restUrlPrefix", "configuration");
        },

        SELECTED: "SELECTED",
        SUCCESS: "success",
        ERROR: "error",

        DB_DATE_FORMAT: "dd-MM-yy",
        PARAM_VALUES: "PARAM_VALUES",
        URL_PARAM: "parm",
        URL_QUERYSTRING_PARAMS: "REQUEST_PARAMS",
        RESET_PAGER: "resetPager",

        IS_DEP_GRID: ".IS_DEP_GRID",

        EQUAL_KEY_VALS: "_EQUAL_KEY_VALS",

        // Flat Screen Modes
        SCREEN_MODE: "SCREEN_MODE",
        DISPLAY_MODE: "DISPLAY",
        ADD_MODE: "WRITE",
        CHANGE_MODE: "UPDATE",
        DELETE_MODE: "DELETE",
        CONFIRMATION_MODE: "CONFIRMATION",

        NXT_PGM: "nxtPgm",

        // UI look and feel constants
        THEME: "XWDFT",
        FONT_ERROR: "errorFont",
        FONT_WARNING: "warningFont",
        FONT_INFORMATION: "infoFont",
        FONT_FIELD: "fieldFont",
        STYLE_CLASS: "styleClass",

        PARENT_SCREEN: "parent",
        START_SCN: "startScn",
        START_PARENT_SCN: "startParentScn",
        BIZRLZ_BEAN: "BIZRLZ_BEAN",
        PARAMS_LIST: "paramsList",
        RE_INITIALIZE_BIZ_RLZ: "RE_INITIALIZE_BIZ_RLZ",

        // Error constants
        OTHER_ERR_MSG_STATUS: 0,
        CALLING_PGM_ERR_STATUS: 1,
        CALLED_PGM_ERR_STATUS: 2,

        // Function Key Indicators
        KA: 1,
        KB: 2,
        KC: 3,
        KD: 4,
        KE: 5,
        KF: 6,
        KG: 7,
        KH: 8,
        KI: 9,
        KJ: 10,
        KK: 11,
        KL: 12,
        KM: 13,
        KN: 14,
        KP: 15,
        KQ: 16,
        KR: 17,
        KS: 18,
        KT: 19,
        KU: 20,
        KV: 21,
        KW: 22,
        KX: 23,
        KY: 24,

        // Overflow Indicators
        OA: 1,
        OB: 2,
        OC: 3,
        OD: 4,
        OE: 5,
        OF: 6,
        OG: 7,
        OV: 8,

        // DataCrud Enum Constants
        IOACTIONS: {LIOERROR: 0, LIOFOUND: 1, LIOEQUAL: 2, LIOEOF: 3},

        //Progress Message Constants
        SUBMITRECORD: "Saving Record...",
        LOADING: "Loading",
        LOADINGDATA: "Loading Data...",
        PROCESSING: "Processing..."
    };

    return constants;
});