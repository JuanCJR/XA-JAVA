"use strict";

var app = angular.module('app');

/**
 * Calculates and applies the appropriate max-width CSS style to fields 
 * depending on the maxlength/fieldlength attribute of input field. This is 
 * required to preserve the original appearance of the grid and also maintain
 * its responsiveness.
 *
 * @author Robin Rizvi
 * @version (2014-05-13.16:16:21)
 */
app.directive('preserveGridLayout', function(UiUtils, Utils) {
	return {
		restrict: 'AC',
        priority: 1001,
		link: function(scope, element) {
			scope.$watch('gridVariable', function() {
				UiUtils.setupProgramUI();
				element.find('input, select').each(function(index, elem) {
					try {
						elem = $(elem);

						var fieldSize;

						if (elem.is('[maxlength]')) {
							fieldSize = parseInt(elem.attr('maxlength'), 10);
						}
						else if (elem.parent().is("[fieldlength]")) {
							fieldSize =
								parseInt(elem.parent().attr('fieldlength'), 10);
						}
						else if (elem.is("[fieldlength]")) {
							fieldSize = parseInt(elem.attr('fieldlength'), 10);
						}
						else {
							return;
						}

						var fontSize = parseInt(elem.css("font-size"), 10);

						var padding =
							(fieldSize < 11)
							? (
								parseInt(elem.css("padding-left"), 10) +
								parseInt(elem.css("padding-right"), 10)
							) : 0;

						var extraWidth = 0;

						if (elem.is('select')) {
							var browser = Utils.getBrowser();

							if (browser == 'chrome') {
								extraWidth = 26;
							}
							else if (browser == 'firefox') {
								extraWidth = 19;
							}
							else if (browser == 'ie') {
								extraWidth = 20;
							}
							else {
								extraWidth = 24;
							}
						}
						else {
							if (elem.siblings().length != 0) {
								// Input having promptFld
								var browser = Utils.getBrowser();

								if ((browser == 'firefox') ||
										(browser == 'ie')) {
									var minWidth = (fieldSize * fontSize) - 10;
									elem.css('min-width', minWidth + "px");
								}
							}
						}

						var maxWidth =
							fieldSize * fontSize + padding + extraWidth;
						elem.css('max-width', maxWidth + "px");

						if (elem.is('select')) {
							elem.css('min-width', maxWidth + "px");
						}
					}
					catch (e) {
						elem.css("width", "auto");
					}
				});
			});
		}
	};
});