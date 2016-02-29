'use strict';

module.exports = function (grunt) {

    var buildDirectory = grunt.option('buildDirectory');

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
        },
        ngtemplates: {
            app: {
                cwd: 'src/main/webapp',
                src: ["app/dialog/**.html"],
                dest: buildDirectory + "/app/app.templates.js"
            },
            options: {
                htmlmin: {
                    collapseBooleanAttributes:      true,
                    collapseWhitespace:             true
                }
            }
        }
    });
    grunt.loadNpmTasks("grunt-bower-install-simple");
    grunt.loadNpmTasks("grunt-angular-templates");
    grunt.registerTask("default", ["bower-install-simple", "ngtemplates"]);
};