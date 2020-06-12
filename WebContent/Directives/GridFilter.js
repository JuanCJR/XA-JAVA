"use strict";
var app = angular.module('app');

/**
 * HTML directive to generate filter (positionTo) fields on view.
 * It supports 3 modes: grid, box, prompt. Allows filter element to be placed in 
 * the grid itself, as a popup or as a separate box.
 *
 * @author Robin Rizvi
 * @since (2014-07-15.19:47:23)
 */
app.directive('gridFilter', function() {
    return {
        restrict: 'E',
        compile: function(element, attrs) {
            if (element.next().is('table')) {
            	// Filter in grid
                if (element.children().length) {
                    var markup = '<tr';

                    for (var attr in attrs.$attr) {
                        markup += ' ' + attrs.$attr[attr] + '="' + attrs[attr] + '"';
                    }

                    markup += '>';
                    markup += '<td><button class="btn btn-primary btn-xs" data-toggle="tooltip" title="Filter" ng-click="applyFilter()"><i class="fa fa-filter"></i></button></td>';

                    element.next('table').find('tbody tr').children().each(function() {
                        if ($(this).is('td') && !($(this).is("td:first-child"))) {
                            markup += '<td>';

                            var valueBinding = '';

                            if ($(this).children(":first-child").attr('ng-model') != null) {
                                // Editable grid
                                valueBinding = $(this).children(":first-child").attr('ng-model');
                            }
                            else {
                                // Normal grid
                                valueBinding = $(this).html();
                            }

                            if (valueBinding.indexOf('row.') != -1) {
                            	// {{ row.country | uppercase }}
                            	// {{ row.dtKostennotalijn | date:dateFormat }}
                            	valueBinding = valueBinding.replace(new RegExp('{{ | }}|row.| \\| uppercase| \\| date:dateFormat', 'gi'), '');

                            	element.children().each(function() {
                            		//if ($(this).attr('ng-model').replace('filterData.', '') == valueBinding) {
                            		if (($(this).attr('ng-model').replace('filterData.', '') == valueBinding) ||
                            				($(this).attr('name') == valueBinding)) {
                            			markup += '<div class="no-padding">' + this.outerHTML + '</div';

                            			return false;
                                    }
                            	});
                            }

                            markup += '</td>';
                        }
                    });

                    markup += '</tr>';
                    element.next('table').children('tbody').prepend(markup);
                }

                element.remove();
            }
            else if (element.parent('.box-tools').length) {
            	// Filter as popup
                if (element.children().length) {
                    var markup = '<button class="btn btn-primary btn-xs" data-toggle="tooltip" title="Filter" onclick="alert(\'Filter prompt not supported.\')"><i class="fa fa-filter"></i></button>';
                    element.replaceWith(markup);
                }
            }
            else {
            	// Filter as box
                if (element.children().length) {
                    var markup = '<div class="row" ng-show="requestInProgress.$resolved">\
                        <section class="col-lg-80 connectedSortable">\
                            <div class="box box-primary filter-box">\
                                <div class="box-header">\
                                    <!-- tools box -->\
                                    <div class="pull-right box-tools">\
                                        <button class="btn btn-primary btn-xs" data-widget="collapse" data-toggle="tooltip" title="Minimize"><i class="fa fa-minus"></i></button>\
                                        <button class="btn btn-primary btn-xs" data-widget="remove" data-toggle="tooltip" title="Close"><i class="fa fa-times"></i></button>\
                                    </div><!-- /. tools -->\
                                    <i class="fa fa-filter"></i>\
                                                                \
                                    <h3 class="box-title">Filter</h3>\
                                </div><!-- /.box-header -->\
                                <div class="box-body">\
                                    <div class="row">\
                                        <div class="col-lg-27 col-sm-53 col-md-40 col-xs-80">\
                                            <form class="form-horizontal" ng-submit="applyFilter()">';

                    element.children().each(function() {
                        markup += '<div class="form-group">';
                        markup += '<label class="control-label col-sm-27" for="' + $(this).attr('placeholder') + '">' + $(this).attr('placeholder') + '</label>';
                        markup += '<div class="col-sm-53">';
                        markup += this.outerHTML + '</div></div>';
                    });

                    markup += '         </form>\
                                          </div>\
                                      </div>\
                                  </div>\
                              </div>\
                          </section>\
                        </div>';

                    element.replaceWith(markup);
                }
            }
        }
    };
});