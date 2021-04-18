
module.exports = function (grunt) {
	require('load-grunt-tasks')(grunt);
	const sass = require('node-sass');
 
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		sass: {
			dist: {
				options: {
					implementation: sass,
					sourceMap: false
				},
				files: [{
					expand: true,
					cwd: 'src/scss',
					src: [
						'main.scss'
					],
					dest: 'src/build/css',
					ext: '.build.css'
				}]
			}
		},
		watch: {
			options: {
				spawn: false
			},
			css: {
				files: [
					'src/css/**/*.css'
				],
				tasks: ['runStylesSaas']
			},
			sass: {
				files: [
					'src/scss/**/*.scss'
				],
				tasks: ['runStylesSaas']
			},
			js: {
				files: ['src/js/**/*.js'],
				tasks: ['runScripts']
			}
		},
		babel: {
			options: {
				sourceMap: false,
				presets: ['babel-preset-es2015']
			},
			dist: {
				files: {
					'src/build/js/app.build-babel.js': 'src/build/js/app.build.js'
				}
			}
		},
		concat: {
			jsapp: {
				src: [
					'src/js/app/app.editable.eventObserver.js',
					'src/js/scripts.js',
					'src/js/app/app.schemas.js',
					'src/js/app/app.utils.js',
					'src/js/app/app.modal.js',
					'src/js/app/app.modal.mt-details.js',
					'src/js/app/app.editable.mt.*.js',
					'src/js/app/app.modal.mt-simulations.js',
					'src/js/app/app.simulation.js',
					'src/js/app/app.mt.details.js',
					'src/js/app/app.table.js',
					'src/js/app/app.table.mt.js',
					'src/js/app/app.ui.js',
					'src/js/app/app.landingpage.js',
                    'specs/template.js',
                    'specs/SPOT.json'
				],
				dest: 'src/build/js/app.build.js'
			},
			jsvendors: {
				src: [
					'src/js/lib/popper.min.js',
					'src/js/lib/bootstrap.bundle.min.js',
					'src/js/lib/bootstrap-table.js',
					'src/js/lib/bootstrap-touchspin.js',
					'src/js/lib/bootstrap-datepicker.js',
					'src/js/lib/**/*.js'
				],
				dest: 'src/build/js/lib.build.js'
			},
			jsall : {
				src: [
					'src/build/js/lib.build.js',
					'src/build/js/app.build-babel.js'
				],
				dest: 'js-lib/bfr/fskapp/app_1.0.0.js'
			},
			css: {
				src: [
					'src/css/**/*.css',
					'src/build/css/*.build.css'
				],
				dest: 'js-lib/bfr/fskapp/css/styles.css'
			}
		},
		jshint: {
			options: {
				'esversion': 6,
			},
			all: [
				'src/js/*.js',
				'src/js/scripts.js'
			]
		},
		postcss: {
			options: {
				grid : 'autoplace',
				map: false,
				processors: [
					require('autoprefixer')({ grid : true }), // add vendor prefixes
				]
			},
			dist: {
				src: 'src/build/css/styles.css',
				dest: 'assets/css/styles.css'
			}
		},
		cssmin: { 
			options: {
				report: 'gzip',
				keepSpecialComments: 0
			},
			target: {
				files: [{
					expand: true,
					cwd: 'assets/css',
					src: ['*.css', '!*.min.css'],
					dest: 'assets/css',
					ext: '.min.css'
				}]
			}
		},
		uglify: {
			options: {
				mangle: false,
				report: 'gzip'
			},
			default: {
				files: {
					'assets/js/app.min.js': ['assets/js/app.js'],
					'assets/js/scripts.min.js': ['assets/js/scripts.js']
				}
			}
		}
	});

	// register task
	grunt.registerTask('runScripts', ['jshint', 'concat:jsapp', 'concat:jsvendors', 'babel', 'concat:jsall', 'uglify']);
	// grunt.registerTask('runStylesLess', ['less', 'concat:css' ,'postcss' ,'cssmin', 'compress:css']);
	grunt.registerTask('runStylesSaas', ['sass', 'concat:css' ,'postcss' ,'cssmin']);

	// standard
	grunt.registerTask('default', ['runScripts', 'runStylesSaas']);
};