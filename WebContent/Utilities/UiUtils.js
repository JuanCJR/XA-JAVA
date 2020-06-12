"use strict";

var app = angular.module('app');

/**
 * Contains utility methods to handle UI operations.
 *
 * @author Robin Rizvi
 * @since (2014-02-18.19:50:23)
 */
app.factory('UiUtils', function() {
    var UiUtils = {
        setupProgramUI: function() {
    		// Set focus on the first input field
        	$('input[type=text]:not([ng-readonly=true]):enabled:visible:first').focus();

            // Make boxes sortable using jquery UI
            $(".connectedSortable").sortable({
                placeholder: "sort-highlight",
                connectWith: ".connectedSortable",
                handle: ".box-header, .nav-tabs",
                forcePlaceholderSize: true,
                zIndex: 999999
            });

            // Display "move" cursor over box header
            $(".connectedSortable .box-header, .nav-tabs").css("cursor", "move");

            // Activate tooltips
            $("[data-toggle='tooltip']").tooltip();

            // Add collapse and expand event handlers to boxes
            $("[data-widget='collapse']").off('click').on('click', function() {
                // Find the box parent        
                var box = $(this).parents(".box").first();
                // Find the body and the footer
                var bf = box.find(".box-body, .box-footer");

                if (!box.hasClass("collapsed-box")) {
                    box.addClass("collapsed-box");
                    bf.slideUp();
                    $(this).children("i").removeClass('fa-minus').addClass('fa-square-o');
                    $(this).attr('title', 'Maximize').tooltip('fixTitle');
                }
                else {
                    box.removeClass("collapsed-box");
                    bf.slideDown();
                    $(this).children("i").removeClass('fa-square-o').addClass('fa-minus');
                    $(this).attr('title', 'Minimize').tooltip('fixTitle');
                }
            });

            // Add remove/close event handler to boxes
            $("[data-widget='remove']").off('click').on('click', function() {
                // Find the box parent        
                var box = $(this).parents(".box").first();
                box.slideUp();
            });

            // Handles enter key binding for program's view 
            $("body").off('keyup').on('keyup', function(e) {
            	if (e.keyCode != 13) {
            		return;
            	}

            	if ($(e.target).attr('ui-keypress') && 
            			$(e.target).attr('ui-keypress').contains('enter')) {
            		return;
            	}

            	var submitBtn = ($('#confirm').get(0) != null) ?
        				$('#confirm') : $('#updRec');

            	if (submitBtn.length != 0) {
            		submitBtn.click();
            	}
            });

            // Initializing all checkbox and radio inputs to iCheck plugin in
            /*$("input").icheck('destroy');
            $("input").icheck({
                checkboxClass: 'icheckbox_minimal',
                radioClass: 'iradio_minimal'
            });*/

            // Activate maxlength plugin on fields having maxlength attribute
            /*$('input[maxlength]').maxlength({
                threshold: 10,
                warningClass: "label label-success",
                limitReachedClass: "label label-danger"
            });*/

            // Hide the .filter-box on initial program load
            if (!$(".filter-box").hasClass("collapsed-box")) {
                $(".filter-box").addClass("collapsed-box");
                $(".filter-box").find(".box-body, .box-footer").slideUp();
                $(".filter-box [data-widget='collapse']").children("i").removeClass('fa-minus').addClass('fa-square-o');
                $(".filter-box [data-widget='collapse']").attr('title', 'Maximize').tooltip('fixTitle');
            }
        },

        resetProgramUI: function() {
            if (!$(".filter-box").hasClass("collapsed-box")) {
                $(".filter-box").addClass("collapsed-box");
                $(".filter-box").find(".box-body, .box-footer").slideUp();
                $(".filter-box [data-widget='collapse']").children("i").removeClass('fa-minus').addClass('fa-square-o');
                $(".filter-box [data-widget='collapse']").attr('title', 'Maximize').tooltip('fixTitle');
            }

            $('.box-danger').slideDown();
        },

        setupUI: function() {
            /*
             * ADD SLIMSCROLL TO THE TOP NAV DROPDOWNS
             * ---------------------------------------
             */
            $(".navbar .menu").slimscroll({
                height: "200px",
                alwaysVisible: false,
                size: "3px"
            }).css("width", "100%");

            // Add hover support for touch devices
            $('.btn').bind('touchstart', function() {
                $(this).addClass('hover');
            }).bind('touchend', function() {
                $(this).removeClass('hover');
            });

            // Activate tooltips
            $("[data-toggle='tooltip']").tooltip();

            /* 
             * Assign a min-height value every time the
             * wrapper gets resized and upon page load. We will use
             * Ben Alman's method for detecting the resize event.
             **/
            function _fix() {
                // Get window height and the wrapper height
                var height = $(window).height() - $("body > .header").height();
                $(".wrapper").css("min-height", height + "px");
                var content = $(".wrapper").height();
                // If the wrapper height is greater than the window
                if (content > height) {
                    $("html, body").css("min-height", content + "px");
                }
                else {
                    $(".program-section .content, html, body").css("min-height", height + "px");
                }
            }

            // Fire when wrapper is resized
            $(".wrapper").resize(function() {
                _fix();
            });

            // Fire upon load
            _fix();
        },

        showLoadingIndicator: function(elem, parentSelector) {
            if (typeof(elem) == "string") {
                elem = $(elem);
            }

            if ((parentSelector != null) && (parentSelector != undefined)) {
                elem = $(elem).parents(parentSelector).get(0);
            }

            $(elem).append('<div class="overlay"></div><div class="loading-img"></div>');
        },

        hideLoadingIndicator: function(elem, parentSelector) {
            if (typeof(elem) == "string") {
                elem = $(elem);
            }

            if ((parentSelector != null) && (parentSelector != undefined)) {
                elem = $(elem).parents(parentSelector).get(0);
            }

            $(elem).find('.overlay, .loading-img').remove();
        },

        getSkins: function() {
            var skins = [
                {
                    name: 'light-blue',
                    backgroundClassName: 'bg-light-blue',
                    foregroundClassName: 'text-white',
                    displayName: 'Light Blue'
                },
                {
                    name: 'black',
                    backgroundClassName: 'bg-black',
                    foregroundClassName: 'text-white',
                    displayName: 'Black'
                },
                {
                    name: 'olive',
                    backgroundClassName: 'bg-olive',
                    foregroundClassName: 'text-white',
                    displayName: 'Olive'
                },
                {
                    name: 'red',
                    backgroundClassName: 'bg-red',
                    foregroundClassName: 'text-white',
                    displayName: 'Red'
                },
                {
                    name: 'green',
                    backgroundClassName: 'bg-green',
                    foregroundClassName: 'text-white',
                    displayName: 'Green'
                },
                {
                    name: 'aqua',
                    backgroundClassName: 'bg-aqua',
                    foregroundClassName: 'text-white',
                    displayName: 'Aqua'
                },
                {
                    name: 'yellow',
                    backgroundClassName: 'bg-yellow',
                    foregroundClassName: 'text-white',
                    displayName: 'Yellow'
                },
                {
                    name: 'light-green',
                    backgroundClassName: 'bg-light-green',
                    foregroundClassName: 'text-white',
                    displayName: 'Light Green'
                },
                {
                    name: 'blue',
                    backgroundClassName: 'bg-blue',
                    foregroundClassName: 'text-white',
                    displayName: 'Blue'
                },
                {
                    name: 'navy',
                    backgroundClassName: 'bg-navy',
                    foregroundClassName: 'text-white',
                    displayName: 'Navy'
                },
                {
                    name: 'teal',
                    backgroundClassName: 'bg-teal',
                    foregroundClassName: 'text-white',
                    displayName: 'Teal'
                },
                {
                    name: 'lime',
                    backgroundClassName: 'bg-lime',
                    foregroundClassName: 'text-white',
                    displayName: 'Lime'
                },
                {
                    name: 'orange',
                    backgroundClassName: 'bg-orange',
                    foregroundClassName: 'text-white',
                    displayName: 'Orange'
                },
                {
                    name: 'fuchsia',
                    backgroundClassName: 'bg-fuchsia',
                    foregroundClassName: 'text-white',
                    displayName: 'Fuchsia'
                },
                {
                    name: 'purple',
                    backgroundClassName: 'bg-purple',
                    foregroundClassName: 'text-white',
                    displayName: 'Purple'
                },
                {
                    name: 'maroon',
                    backgroundClassName: 'bg-maroon',
                    foregroundClassName: 'text-white',
                    displayName: 'Maroon'
                },
                {
                    name: 'gray',
                    backgroundClassName: 'bg-gray',
                    foregroundClassName: 'text-black',
                    displayName: 'Gray'
                },
                {
                    name: 'cinnabar',
                    backgroundClassName: 'bg-cinnabar',
                    foregroundClassName: 'text-white',
                    displayName: 'Cinnabar'
                },
                {
                    name: 'rose',
                    backgroundClassName: 'bg-rose',
                    foregroundClassName: 'text-white',
                    displayName: 'Rose'
                },
                {
                    name: 'cherokee',
                    backgroundClassName: 'bg-cherokee',
                    foregroundClassName: 'text-black',
                    displayName: 'Cherokee'
                },
                {
                    name: 'mauvelous',
                    backgroundClassName: 'bg-mauvelous',
                    foregroundClassName: 'text-white',
                    displayName: 'Mauvelous'
                },
                {
                    name: 'west-side',
                    backgroundClassName: 'bg-west-side',
                    foregroundClassName: 'text-black',
                    displayName: 'West Side'
                },
                {
                    name: 'gallery',
                    backgroundClassName: 'bg-gallery',
                    foregroundClassName: 'text-black',
                    displayName: 'Gallery'
                },
                {
                    name: 'polar',
                    backgroundClassName: 'bg-polar',
                    foregroundClassName: 'text-black',
                    displayName: 'Polar'
                },
                {
                    name: 'raffia',
                    backgroundClassName: 'bg-raffia',
                    foregroundClassName: 'text-black',
                    displayName: 'Raffia'
                },
                {
                    name: 'polo-blue',
                    backgroundClassName: 'bg-polo-blue',
                    foregroundClassName: 'text-black',
                    displayName: 'Polo Blue'
                },
                {
                    name: 'olive-tone',
                    backgroundClassName: 'bg-olive-tone',
                    foregroundClassName: 'text-black',
                    displayName: 'Olive Tone'
                },
                {
                    name: 'turtle-green',
                    backgroundClassName: 'bg-turtle-green',
                    foregroundClassName: 'text-white',
                    displayName: 'Turtle-Green'
                },
                {
                    name: 'cerise',
                    backgroundClassName: 'bg-cerise',
                    foregroundClassName: 'text-white',
                    displayName: 'Cerise'
                },
                {
                    name: 'yellow-orange',
                    backgroundClassName: 'bg-yellow-orange',
                    foregroundClassName: 'text-black',
                    displayName: 'Yellow Orange'
                },
                {
                    name: 'curious-blue',
                    backgroundClassName: 'bg-curious-blue',
                    foregroundClassName: 'text-black',
                    displayName: 'Curious Blue'
                },
                {
                    name: 'pelorous',
                    backgroundClassName: 'bg-pelorous',
                    foregroundClassName: 'text-black',
                    displayName: 'Pelorous'
                },
                {
                    name: 'spindle',
                    backgroundClassName: 'bg-spindle',
                    foregroundClassName: 'text-black',
                    displayName: 'Spindle'
                },
                {
                    name: 'venetian-red',
                    backgroundClassName: 'bg-venetian-red',
                    foregroundClassName: 'text-white',
                    displayName: 'Venetian Red'
                },
                {
                    name: 'periwinkle-gray',
                    backgroundClassName: 'bg-periwinkle-gray',
                    foregroundClassName: 'text-black',
                    displayName: 'Periwinkle Gray'
                },
                {
                    name: 'blue-3D-1',
                    backgroundClassName: 'bg-blue-3D-1',
                    foregroundClassName: 'text-black',
                    displayName: 'Blue 3D 1'
                },
                {
                    name: 'blue-3D-2',
                    backgroundClassName: 'bg-blue-3D-2',
                    foregroundClassName: 'text-black',
                    displayName: 'Blue 3D 2'
                },
                {
                    name: 'blue-3D-3',
                    backgroundClassName: 'bg-blue-3D-3',
                    foregroundClassName: 'text-black',
                    displayName: 'Blue 3D 3'
                },
                {
                    name: 'blue-3D-4',
                    backgroundClassName: 'bg-blue-3D-4',
                    foregroundClassName: 'text-black',
                    displayName: 'Blue 3D 4'
                },
                {
                    name: 'blue-gloss-1',
                    backgroundClassName: 'bg-blue-gloss-1',
                    foregroundClassName: 'text-black',
                    displayName: 'Blue-gloss-1'
                },
                {
                    name: 'brown-3D',
                    backgroundClassName: 'bg-brown-3D',
                    foregroundClassName: 'text-black',
                    displayName: 'Brown 3D'
                },
                {
                    name: 'gold-3D',
                    backgroundClassName: 'bg-gold-3D',
                    foregroundClassName: 'text-black',
                    displayName: 'Gold 3D'
                },
                {
                    name: 'green-3D-2',
                    backgroundClassName: 'bg-green-3D-2',
                    foregroundClassName: 'text-black',
                    displayName: 'Green 3D 2'
                },
                {
                    name: 'green-3D-4',
                    backgroundClassName: 'bg-green-3D-4',
                    foregroundClassName: 'text-black',
                    displayName: 'Green 3D 4'
                },
                {
                    name: 'green-gloss-1',
                    backgroundClassName: 'bg-green-gloss-1',
                    foregroundClassName: 'text-black',
                    displayName: 'Green Gloss 1'
                },
                {
                    name: 'green-gloss-2',
                    backgroundClassName: 'bg-green-gloss-2',
                    foregroundClassName: 'text-black',
                    displayName: 'Green Gloss 2'
                },
                {
                    name: 'grey-3D',
                    backgroundClassName: 'bg-grey-3D',
                    foregroundClassName: 'text-black',
                    displayName: 'Grey 3D'
                },
                {
                    name: 'grey-gloss-1',
                    backgroundClassName: 'bg-grey-gloss-1',
                    foregroundClassName: 'text-black',
                    displayName: 'Grey Gloss 1'
                },
                {
                    name: 'black-gloss',
                    backgroundClassName: 'bg-black-gloss',
                    foregroundClassName: 'text-white',
                    displayName: 'Black Gloss'
                },
                {
                    name: 'neon',
                    backgroundClassName: 'bg-neon',
                    foregroundClassName: 'text-black',
                    displayName: 'Neon'
                },
                {
                    name: 'olive-3D-3',
                    backgroundClassName: 'bg-olive-3D-3',
                    foregroundClassName: 'text-white',
                    displayName: 'Olive 3D 3'
                },
                {
                    name: 'orange-3D-1',
                    backgroundClassName: 'bg-orange-3D-1',
                    foregroundClassName: 'text-black',
                    displayName: 'Orange 3D 1'
                },
                {
                    name: 'orange-3D-2',
                    backgroundClassName: 'bg-orange-3D-2',
                    foregroundClassName: 'text-black',
                    displayName: 'Orange 3D 2'
                },
                {
                    name: 'purple-3D',
                    backgroundClassName: 'bg-purple-3D',
                    foregroundClassName: 'text-black',
                    displayName: 'Purple 3D'
                },
                {
                    name: 'red-3D-2',
                    backgroundClassName: 'bg-red-3D-2',
                    foregroundClassName: 'text-black',
                    displayName: 'Red 3D 2'
                },
                {
                    name: 'red-3D-3',
                    backgroundClassName: 'bg-red-3D-3',
                    foregroundClassName: 'text-black',
                    displayName: 'Red 3D 3'
                },
                {
                    name: 'red-gloss-1',
                    backgroundClassName: 'bg-red-gloss-1',
                    foregroundClassName: 'text-black',
                    displayName: 'Red Gloss 1'
                },
                {
                    name: 'red-gloss-2',
                    backgroundClassName: 'bg-red-gloss-2',
                    foregroundClassName: 'text-black',
                    displayName: 'Red Gloss 2'
                },
                {
                    name: 'orange-gloss',
                    backgroundClassName: 'bg-orange-gloss',
                    foregroundClassName: 'text-black',
                    displayName: 'Orange-Gloss'
                },
                {
                    name: 'wax-3D-1',
                    backgroundClassName: 'bg-wax-3D-1',
                    foregroundClassName: 'text-black',
                    displayName: 'Wax 3D 1'
                },
                {
                    name: 'wax-3D-2',
                    backgroundClassName: 'bg-wax-3D-2',
                    foregroundClassName: 'text-black',
                    displayName: 'Wax 3D 2'
                },
                {
                    name: 'black-3D-1',
                    backgroundClassName: 'bg-black-3D-1',
                    foregroundClassName: 'text-white',
                    displayName: 'Black 3D 1'
                },
                {
                    name: 'black-3D-2',
                    backgroundClassName: 'bg-black-3D-2',
                    foregroundClassName: 'text-white',
                    displayName: 'Black 3D 2'
                },
                {
                    name: 'white-gloss',
                    backgroundClassName: 'bg-white-gloss',
                    foregroundClassName: 'text-black',
                    displayName: 'White Gloss'
                },
                {
                    name: 'pattern-1249',
                    backgroundClassName: 'bg-pattern-1249',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1249'
                },
                {
                    name: 'pattern-1252',
                    backgroundClassName: 'bg-pattern-1252',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1252'
                },
                {
                    name: 'pattern-1334',
                    backgroundClassName: 'bg-pattern-1334',
                    foregroundClassName: 'text-black',
                    displayName: 'Pattern #1334'
                },
                {
                    name: 'pattern-1335',
                    backgroundClassName: 'bg-pattern-1335',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1335'
                },
                {
                    name: 'pattern-1379',
                    backgroundClassName: 'bg-pattern-1379',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1379'
                },
                {
                    name: 'pattern-1384',
                    backgroundClassName: 'bg-pattern-1384',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1384'
                },
                {
                    name: 'pattern-1393',
                    backgroundClassName: 'bg-pattern-1393',
                    foregroundClassName: 'text-black',
                    displayName: 'Pattern #1393'
                },
                {
                    name: 'pattern-1394',
                    backgroundClassName: 'bg-pattern-1394',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1394'
                },
                {
                    name: 'pattern-1397',
                    backgroundClassName: 'bg-pattern-1397',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1397'
                },
                {
                    name: 'pattern-1398',
                    backgroundClassName: 'bg-pattern-1398',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1398'
                },
                {
                    name: 'pattern-1401',
                    backgroundClassName: 'bg-pattern-1401',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1401'
                },
                {
                    name: 'pattern-1411',
                    backgroundClassName: 'bg-pattern-1411',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #1411'
                },
                {
                    name: 'pattern-456',
                    backgroundClassName: 'bg-pattern-456',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #456'
                },
                {
                    name: 'pattern-881',
                    backgroundClassName: 'bg-pattern-881',
                    foregroundClassName: 'text-black',
                    displayName: 'Pattern #881'
                },
                {
                    name: 'pattern-885',
                    backgroundClassName: 'bg-pattern-885',
                    foregroundClassName: 'text-white',
                    displayName: 'Pattern #885'
                }
            ];

            return skins;
        }
    };

    return UiUtils;
});