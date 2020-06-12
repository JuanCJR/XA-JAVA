"use strict";

var app = angular.module('app');

/**
 * Handles pagination operation for Grid screen.
 *
 * @author Robin Rizvi
 * @since (2014-03-12.12:31:23)
 */
app.factory('Pager', function() {
    var Pager = function(pagesize) {
    	this.pageSize = pagesize;
        this.startRow = 0;
        this.endRow = pagesize - 1;
    	this.rows = [];
        this.eof = false;
        this.page = 0;

        this.addRow = function(row) {
            this.rows.push(row);
        };

        this.appendRows = function(rows) {
            for (var i = 0; i < rows.length; i++) {
                this.addRow(rows[i]);
            }
        };

        this.createViewModel = function(filterRowData) {
            var viewModel = [];

            if (filterRowData != null) {
                viewModel.push(filterRowData);
            }

            for (var i = this.startRow; i <= this.endRow; i++) {
                if ((i >= 0) && (i < this.rows.length)) {
                    viewModel.push(this.rows[i]);
                }
            }

            return viewModel;
        };

        this.fetchNext = function() {
            if (this.endRow < this.rows.length - 1) {
                this.page++;
                this.startRow += this.pageSize;
                this.endRow += this.pageSize;

                // load from current heap
                return true;
            }
            else if (!(this.eof)) {
                this.page++;
                // fetch from database
                this.startRow += this.pageSize;
                this.endRow += this.pageSize;
            }

            return false;
        };

        this.fetchPrevious = function() {
            if ((this.startRow > 0) && (this.startRow <= this.rows.length)) {
                this.page--;
                this.startRow -= this.pageSize;
                this.endRow -= this.pageSize;

                return true;
            }

            return false;
        };

        this.hasNext = function() {
            return !this.eof || (this.endRow < this.rows.length - 1);
        };

        this.hasPrevious = function() {
            return (this.startRow >= this.pageSize);
        };

        this.reset = function() {
            this.rows = [];
            this.eof = false;
            this.page = 0;
            this.startRow = 0;
            this.endRow = this.pageSize - 1;
        };
    };

    return Pager;
});