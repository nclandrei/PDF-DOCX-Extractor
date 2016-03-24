QUnit.begin(function( details ) {
  console.log( "Test amount:", details.totalTests );
});

QUnit.test( "Upload Tests (POST requests)", function( assert ) {

    assert.expect(2);
    var done = assert.async(2);

    test1();

    function test1() {
        setTimeout(function() {
            $.ajax({
                    type: "POST",
                    url: "/uploadFileDropbox",
                    data: {
                        file: "https://cldup.com/Zfbj7AHekx.docx",
                        fileName: "myfile.docx"
                    },
                    success: function (result) {
                        if(result.status == 1){
                            assert.ok( true, "Correct - Dropbox test" );
                            done();

                        } else {
                            assert.ok( false, "Incorrect - Dropbox test\n Error: " + result );
                            done();
                        }
                    },
                    error: function (e) {
                            assert.ok( false, "Incorrect - Dropbox test\n Error: " + e );
                            done();
                    }
            });
        }, 100 );
    }

    function test2(formData) {
        setTimeout(function() {
              $.ajax({
                      type: "POST",
                      url: "/uploadFile",
                      enctype: 'multipart/form-data',
                      data: formData,
                      processData: false,
                      contentType: false,
                      success: function (result) {
                        if(result.status == 1){
                            assert.ok( true, "Correct - Local upload test" );
                            done();

                        } else {
                            assert.ok( false, "Incorrect - Local upload test\n Error: " + result );
                            done();
                        }
                      },
                      error: function (e) {
                            assert.ok( false, "Incorrect - Local upload test\n Error: " + e );
                            done();
                      }
                  });
        }, 100 );
    }


    $('#file-upload').on('change', function () {
        var myFile = $(this).val();
        var ext = myFile.split('.').pop();
        if (ext != "docx" && ext != "pdf" && ext != null) {
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
        var formData = new FormData(document.getElementById("localUpload"));
        test2(formData);
    });
});