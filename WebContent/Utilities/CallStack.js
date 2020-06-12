"use strict";

var app = angular.module('app');

/**
 * Performs state and stack management for programs.
 *
 * @author Robin Rizvi
 * @since (2014-07-25.16:35:23)
 */
app.factory('CallStack', function(Utils) {
    var CallStack = {
		/**
		 * Maintains the stack of program entries for navigation.
		 */
		callStack: [],

		stateVariable: {},
		nextFunction: {},
		entryParams: {},
		/**
		 * qTypeFun and nextProgram for program's post processing on return.
		 */
		qTypeFun: {},
		nextProgram: {},

		/**
		 * Adds the screen into callStack and saves the state of its controller.
		 *
		 * @param {String} screen screen name
		 * @param {Object} controller program controller
		 */
		add: function(screen, controller) {
			if (screen.endsWith("Grid")) {
				screen = screen.substring(0, screen.length - 4) + "EntryPanel";
			}

			var index = this.callStack.indexOf(screen);

			if (index != -1) {
				// If callStack already contains the screen, then remove all
				// entries in callStack above it
				var size = this.callStack.length;

				for (var i = size - 1; i > index; i--) {
					this.callStack.pop();
				}
			}
			else {
				// Add the screen into callStack and save its state
				this.callStack.push(screen);

				if (controller == null) {
					return;
				}

				var pgm = Utils.getProgramName(screen);

				if (pgm == null) {
					return;
				}

				this.stateVariable[pgm] = controller.stateVariable;
				this.nextFunction[pgm] = controller.nextFunction;
				this.entryParams[pgm] = controller.entryParamterList;
				this.qTypeFun[pgm] = controller.qTypeFun;
				this.nextProgram[pgm] = controller.nextProgram;
			}
        },

		/**
		 * Clears the state of the program's controller.
		 *
		 * @param {Object} controller program controller
		 */
		deleteState: function(controller) {
			if (controller == null) {
				return;
			}

			this.removeState(controller.programName);
		},

		/**
		 * Checks if the program's state exists in the callStack.
		 *
		 * @param {String} pgm current program name
		 * @returns {Boolean} <tt>true</tt> if the program's state exists
		 */
		existsState: function(pgm) {
			return (this.stateVariable[pgm] != null);
		},

		/**
		 * Returns last screen from the stack.
		 *
		 * @returns {String} last screen
		 */
		getLastScreen: function() {
			var size = this.callStack.length;

			if (size == 0) {
        		return null;
			}

			return this.callStack[size - 1];
		},

		/**
		 * Returns the index within this callStack of the occurrence of the
		 * specified program.
		 *
		 * @param {String} pgm current program name
		 * @returns {Integer} the index within this callStack of the occurrence
		 *          of the specified program
		 */
		indexOf: function(pgm) {
			var index = -1;
			var size = this.callStack.length;

			for (var i = size - 1; i >= 0; i--) {
				var screen = this.callStack[i];

				if (pgm == Utils.getProgramName(screen)) {
					index = i;

					break;
				}
			}

			return index;
		},

		/**
		 * Deletes the screens from this program from callStack and clears the
		 * state of their controllers.
		 *
		 * @param {String} pgm current program name
		 */
		remove: function(pgm) {
			var index = this.removeAfter(pgm);

			if (index == -1) {
				return;
			}

			this.callStack.pop();
			this.removeState(pgm);
		},

		/**
		 * Deletes the screens after this program from callStack and clears the
		 * state of their controllers.
		 *
		 * @param {String} pgm current program name
		 * @returns {Integer} the index within this callStack of the occurrence
		 *          of this program
		 */
		removeAfter: function(pgm) {
			var index = this.indexOf(pgm);

			if (index == -1) {
				return -1;
			}

			var size = this.callStack.length;

			// Remove all entries in callStack after this index
			for (var i = size - 1; i > index; i--) {
				var screen = this.callStack[i];
				var pgmNm = Utils.getProgramName(screen);

				this.callStack.pop();
				this.removeState(pgmNm);
			}

			return index;
		},

		/**
		 * Clears the state of the program's controller.
		 *
		 * @param {String} pgm current program name
		 */
		removeState: function(pgm) {
			if (this.stateVariable[pgm]) {
				delete this.stateVariable[pgm];
			}

			if (this.nextFunction[pgm]) {
				delete this.nextFunction[pgm];
			}

			if (this.entryParams[pgm]) {
				delete this.entryParams[pgm];
			}

			if (this.qTypeFun[pgm]) {
				delete this.qTypeFun[pgm];
			}

			if (this.nextProgram[pgm]) {
				delete this.nextProgram[pgm];
			}
		},

		/**
		 * Resets the callStack and clears the preserved state.
		 */
		reset: function() {
			this.callStack = [];
			this.stateVariable = {};
			this.nextFunction = {};
			this.entryParams = {};
			this.qTypeFun = {};
			this.nextProgram = {};
		},

		/**
		 * Restores the state of the program's controller.
		 *
		 * @param {Object} controller program controller
		 */
		restoreState: function(controller) {
			var pgm = controller.programName;

			if (this.stateVariable[pgm]) {
				controller.stateVariable = this.stateVariable[pgm];
			}

			if (this.nextFunction[pgm]) {
				controller.nextFunction = this.nextFunction[pgm];
			}

			if (this.entryParams[pgm]) {
				controller.entryParamterList = this.entryParams[pgm];
			}

			if (this.qTypeFun[pgm]) {
				controller.qTypeFun = this.qTypeFun[pgm];
			}

			if (this.nextProgram[pgm]) {
				controller.nextProgram = this.nextProgram[pgm];
			}
		},

		/**
		 * Sets next program into callStack.
		 *
		 * @param {String} pgm current program name
		 * @param {String} nextProgram next program name
		 */
		setNextProgram: function(pgm, nextProgram) {
			this.nextProgram[pgm] = nextProgram;
		},

		/**
		 * Sets qTypeFun into callStack.
		 *
		 * @param {String} pgm current program name
		 * @param {String} qTypeFun
		 */
		setQTypeFun: function(pgm, qTypeFun) {
			this.qTypeFun[pgm] = qTypeFun;
		}
    };

	return CallStack;
});