"use strict";

var app = angular.module('app');

/**
 * @author Robin Rizvi
 * @since (2014-03-20.16:49:23)
 */
app.factory('Filter', function() {
    var Filter = {
        OP_EQUAL : 0,
        OP_NOT_EQUAL : 1,
        OP_LESS_THAN : 2,
        OP_GREATER_THAN : 3,
        OP_LESS_OR_EQUAL : 4,
        OP_GREATER_OR_EQUAL : 5,
        OP_LIKE : 6,
        OP_ILIKE : 7,
        OP_IN : 8,
        OP_NOT_IN : 9,
        OP_NULL : 10,
        OP_NOT_NULL : 11,
        OP_EMPTY : 12,
        OP_NOT_EMPTY : 13,
        OP_AND : 100,
        OP_OR : 101,
        OP_NOT : 102,
        OP_SOME : 200,
        OP_ALL : 201,
        OP_NONE : 202,
        OP_CUSTOM : 999
    };

    return Filter;
});