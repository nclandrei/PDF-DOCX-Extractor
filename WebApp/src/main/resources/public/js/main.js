function GreetingService() {
}

GreetingService.prototype.greeting = "Hello";

GreetingService.prototype.greet = function(name) {
	'use strict';
	if (!name) {
		name = "anonymous";
	}
	return this.greeting + ", " + name;
};

$(document).ready(function () {

    $('.modal-trigger').leanModal();

    $('#bug-report-button').on('click', function () {
        $('#modal1').openModal();
    });

    $('#team-button').on('click', function () {
        $('#modal3').openModal();
    });

    $('#textarea1').trigger('autoresize');

    $('.button-collapse').sideNav({
            closeOnClick: true
        }
    );

    $('#file-upload').on('change', function () {
        var myFile = $(this).val();
        var ext = myFile.split('.').pop();
        if (ext != "docx" && ext != "pdf" && ext != null) {
            //noinspection JSUnusedAssignment
            myFile = $(this).val(null);
            alert("Extension ." + ext + " is not supported."
                    + " This tool does work with Microsoft Office .docx and .pdf documents only!");
            $('#submit-button').attr("disabled", true);
        } else {
            $('#submit-button').attr("disabled", false);
        }
    });

    $('#localUpload').submit(function (e) {
        e.preventDefault();
        $('#spinner').spin('default');
        var formData = new FormData(document.getElementById("localUpload"));
        $.ajax({
            type: "POST",
            url: "/uploadFile",
            enctype: 'multipart/form-data',
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                console.log("inside ajax");
                $('#spinner').spin(false);
                if(result.status != 1){
                    alert(result.message);
                } else { location.href = result.href; }
            },
            error: function (e) {
                alert('Failure ' + e.status);
            }
        });
    });

    // drag and drop
    new Dropzone(document.body, {
        url: "/uploadFile",
        maxFilesize: 25,
        uploadMultiple: false,
        maxFiles: 1,
        acceptedFiles: "application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        accept: function(file, done) {
            $('#spinner').spin('default');
            done();
        },
        processData: false,
        contentType: false,
        previewsContainer: "#previews",
        success: function (result) {
            $('#spinner').spin(false);
            //noinspection JSValidateTypes
            location.href = result.xhr.response;
        },
        error: function (e) {
            alert('Failure ' + e.status);
        }
    });

    $('#bug-report').validate({
        errorClass: 'invalid',
        errorPlacement: function (error, element) {
            element.next("label").attr("data-error", error.contents().text());
        }
    });

    $('#bug-report').submit(function (e) {
        e.preventDefault();
        var isvalidate=$("#bug-report").valid();
        if(isvalidate) {
            $('#modal1').closeModal();
            $('#spinner').spin('default');
            var formData = new FormData(document.getElementById("bug-report"));
            $.ajax({
                type: "POST",
                url: "/send-mail",
                enctype: 'multipart/form-data',
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    $('#spinner').spin(false);
                    alert(result);
                },
                error: function (e) {
                    alert('Failure ' + e.status);
                }
            });
        }
    });

    var button = Dropbox.createChooseButton({
        // Required. Called when a user selects an item in the Chooser.
        success: function (files) {
            //noinspection JSUnresolvedVariable
            if (files[0].bytes <= 26214400) {
                $('#spinner').spin('default');
                $.ajax({
                    type: "POST",
                    url: "/uploadFileDropbox",
                    data: {
                        file: files[0].link,
                        fileName: files[0].name
                    },
                    success: function (result) {
                        $('#spinner').spin(false);
                        location.href = result;
                    },
                    error: function (e) {
                        alert('Failure ' + e.status);
                    }
                });
            } else {
                //noinspection JSUnresolvedVariable
                alert(files[0].name +
                        $([files[0].bytes]).map(function () { return " (" + (this / 1024 / 1024).toFixed(2) + " MB)" })[0] +
                        " is too big! Maximum file size is 25MB.")
            }
        },
        cancel: function () {

        },
        linkType: "direct",
        multiselect: false,
        extensions: ['.pdf', '.docx']
    });

    document.getElementById("dropbox").appendChild(button);

    // loading spinner
    (function (factory) {
        //noinspection JSUnresolvedVariable
        if (typeof exports == 'object') {
            // CommonJS
            //noinspection JSUnresolvedFunction
            factory(require('jquery'), require('spin.js'))
        } else { //noinspection JSUnresolvedVariable
            if (typeof define == 'function' && define.amd) {
                // AMD, register as anonymous module
                //noinspection JSUnresolvedFunction
                define(['jquery', 'spin'], factory)
            } else {
                // Browser globals
                if (!window.Spinner) throw new Error('Spin.js not present');
                factory(window.jQuery, window.Spinner)
            }
        }

    }(function ($, Spinner) {

        $.fn.spin = function (opts, color) {

            return this.each(function () {
                var $this = $(this)
                        , data = $this.data();

                if (data.spinner) {
                    data.spinner.stop();
                    delete data.spinner
                }
                if (opts !== false) {
                    opts = $.extend(
                            { color: color || $this.css('color') }
                            , $.fn.spin.presets[opts] || opts
                    );
                    data.spinner = new Spinner(opts).spin(this)
                }
            })
        };

        $.fn.spin.presets = {
            tiny: { lines: 8, length: 2, width: 2, radius: 3 }
            , small: { lines: 8, length: 4, width: 3, radius: 5 }
            , large: { lines: 10, length: 8, width: 4, radius: 8 }
            , default: {
                lines: 13 // The number of lines to draw
                , length: 28 // The length of each line
                , width: 14 // The line thickness
                , radius: 42 // The radius of the inner circle
                , scale: 1 // Scales overall size of the spinner
                , corners: 1 // Corner roundness (0..1)
                , color: '#000' // #rgb or #rrggbb or array of colors
                , opacity: 0.25 // Opacity of the lines
                , rotate: 0 // The rotation offset
                , direction: 1 // 1: clockwise, -1: counterclockwise
                , speed: 1 // Rounds per second
                , trail: 60 // Afterglow percentage
                , fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
                , zIndex: 2e9 // The z-index (defaults to 2000000000)
                , className: 'spinner' // The CSS class to assign to the spinner
                , top: '50%' // Top position relative to parent
                , left: '50%' // Left position relative to parent
                , shadow: false // Whether to render a shadow
                , hwaccel: false // Whether to use hardware acceleration
                , position: 'absolute' // Element positioning
            }
        }
    }));
});