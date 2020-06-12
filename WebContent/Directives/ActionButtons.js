"use strict";

var app = angular.module('app');

/**
 * HTML directive to generate action buttons on view.
 *
 * @author Robin Rizvi
 * @since (2014-07-10.11:35:23)
 */
app.directive('actionButtons', function() {
    return {
        restrict: 'A',
        compile: function(element) {
        	var actions;
        	actions = [];

            element.children('button').each(function(index, elem) {
                elem = $(elem);
                var attrs = '';
                var operation = '';

                $.each(this.attributes, function() {
                    if (this.name == 'pre') {
                        operation += 'ng-click = "' + this.value + '"';
                    }
                    else if ((this.name != "action") &&
                    		(this.name != "visible") &&
                    		(this.name != "disable")) {
                        attrs += this.name;

                        if (this.value) {
                            attrs += '="' + this.value + '"';
                        }

                        attrs += ' ';
                    }
                });

                attrs = attrs.trim();

                if (elem.attr('action') !== undefined) {
                    if (operation.contains('ng-click')) {
                        operation += ';' + elem.attr('action');
                    }
                    else {
                        operation += 'ng-click = "' + elem.attr('action') + '"';
                    }
                }

                var action = {
                    attrs: attrs,
                    caption: elem.text() === undefined ? '' : elem.text(),
                    operation: operation,
                    visibility: elem.attr('visible') === undefined ? '' : 'ng-show="' + elem.attr('visible') + '"',
                    disabled: elem.attr('disable') === undefined ? '' : 'ng-disabled="' + elem.attr('disable') + '"'
                };

                actions.push(action);
            });

            var actionButtonsNavBarMarkup = '<div class="btn-group nav navbar hidden-xs">';

            actions.forEach(function(action) {
                actionButtonsNavBarMarkup += '<button ' + action.attrs + ' ' + action.operation + ' type="button" class="btn btn-primary btn-sm"' + ' ' + action.visibility + ' ' + action.disabled + '>' + action.caption + '</button>';
            });

            actionButtonsNavBarMarkup += '</div>';

            var actionButtonsListMarkup = '<div class="input-group visible-xs">\
                                                <div class="input-group-btn">\
                                                <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">Action <span class="fa fa-caret-down"></span></button>\
                                                <ul class="dropdown-menu">';
            actions.forEach(function(action) {
                actionButtonsListMarkup += '<li><a ' + action.attrs + ' ' + action.operation + ' ' + action.visibility + ' ' + action.disabled + '>' + action.caption + '</a></li>';
            });

            actionButtonsListMarkup += '</ul>\
                                </div>\
                            </div>';

            var actionButtonsMarkup = actionButtonsNavBarMarkup + actionButtonsListMarkup;
            element.replaceWith(actionButtonsMarkup);
        }
    };
});