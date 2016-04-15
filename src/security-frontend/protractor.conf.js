'use strict';

var paths = require('./.yo-rc.json')['generator-gulp-angular'].props.paths;

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

  onPrepare: function () {
    browser.get('/#/');
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
