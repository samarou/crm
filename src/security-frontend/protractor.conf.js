'use strict';

var paths = require('./.yo-rc.json')['generator-gulp-angular'].props.paths;
var path = require('path');
var Jasmine2HtmlReporter = require('protractor-jasmine2-screenshot-reporter');

var reporter = new Jasmine2HtmlReporter({
	dest: 'reports',
	filename: 'crmReport.html'
});

exports.config = {

	framework: 'jasmine2',

	// The address of a running selenium server.
	seleniumAddress: 'http://localhost:4444/wd/hub',
	baseUrl: 'http://localhost:3000/',

	params: {
		environment: {
			// development by default
			name: 'developer'
		}
	},

	beforeLaunch: function () {
		return new Promise(function (resolve) {
			reporter.beforeLaunch(resolve);
		});
	},

	// Assign the test reporter to each running instance
	onPrepare: function () {
		jasmine.getEnv().addReporter(reporter);
		browser.get('/#/');
	},

	// Close the report after all tests finish
	afterLaunch: function (exitCode) {
		return new Promise(function (resolve) {
			reporter.afterLaunch(resolve.bind(this, exitCode));
		});
	},

	// seleniumServerJar: deprecated, this should be set on node_modules/protractor/config.json

	// Capabilities to be passed to the webdriver instance.
	capabilities: {
		browserName: 'chrome'
	},

	// Spec patterns are relative to the current working directory when
	// protractor is called.
	specs: [paths.e2e + '/**/*.spec.js'],

	// Options to be passed to Jasmine-node.
	jasmineNodeOpts: {
		showColors: true,
		defaultTimeoutInterval: 30000
	}
};
