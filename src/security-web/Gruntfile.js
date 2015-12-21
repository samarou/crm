'use strict';

module.exports = function (grunt) {

    grunt.initConfig({
        'bower-install-simple': {
            options: {
                color: true,
                directory: "src/main/webapp/bower_components/"
            },
            "prod": {
                options: {
                    production: true
                }
            },
            "dev": {
                options: {
                    production: false
                }
            }
        }
    });
    grunt.loadNpmTasks("grunt-bower-install-simple");
    grunt.registerTask("default", [ "bower-install-simple" ]);
};