"use strict";

var app = angular.module('app');

/**
 * Provides methods for performing operations on collections.
 *
 * @author Robin Rizvi
 * @since (2014-07-02.13:36:23)
 */
app.factory('CollectionUtils', function() {
    var CollectionUtils = {
        /**
         * Enables extension methods for window.Array.
         */
        enableExtensionMethods: function() {
            /*
             * Array.find
             */
            if (!Array.prototype.find) {
                Object.defineProperty(Array.prototype, 'find', {
                    enumerable: false,
                    configurable: true,
                    writable: true,
                    value: function(predicate) {
                        if (this == null) {
                            throw new TypeError('Array.prototype.find called on null or undefined');
                        }

                        if (typeof predicate !== 'function') {
                            throw new TypeError('predicate must be a function');
                        }

                        var list = Object(this);
                        var length = list.length >>> 0;
                        var thisArg = arguments[1];
                        var value;

                        for (var i = 0; i < length; i++) {
                            if (i in list) {
                                value = list[i];

                                if (predicate.call(thisArg, value, i, list)) {
                                    return value;
                                }
                            }
                        }

                        return undefined;
                    }
                });
            }

            /*
             * Array.forEach
             */
            if (!Array.prototype.forEach) {
                Array.prototype.forEach = function(fun) {
                    if ((this === void 0) || (this === null)) {
                    	throw new TypeError();
                    }

                    var t = Object(this);
                    var len = t.length >>> 0;

                    if (typeof fun !== "function") {
                    	throw new TypeError();
                    }

                    var thisArg = arguments.length >= 2 ? arguments[1] : void 0;

                    for (var i = 0; i < len; i++) {
                        if (i in t) fun.call(thisArg, t[i], i, t);
                    }
                };
            }
        }
    };

    return CollectionUtils;
});