QUnit.begin(function( details ) {
  console.log( "Test amount:", details.totalTests );
});

var myFileInput;
var chosenFileFlag = false;
//$("#test-message").append('<input id="file-upload" type="file" name="file" id="testInput" accept="application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document" required/>');

$('#testInput').on('change', function () {
    myFileInput = $(this).val();
    var ext = myFileInput.split('.').pop();
    if (ext != "docx" && ext != "pdf" && ext != null) {
        myFileInput = $(this).val(null);
        alert("Extension ." + ext + " is not supported."
                + " This tool does work with Microsoft Office .docx and .pdf documents only!");
        $('#submit-button').attr("disabled", true);
    } else {
        $('#submit-button').attr("disabled", false);
    }
});

QUnit.test( "CI Extraction Tool Tests", function( assert ) {

assert.expect(2);
var done = assert.async(2);


while(chosenFileFlag==false){
    if(!$("#testInput").value) {
       chosenFileFlag = true;
       test1();
    }
}

function test1() {
    setTimeout(function() {
          /*$.ajax({
                  type: "POST",
                  url: "/uploadFile",
                  enctype: 'multipart/form-data',
                  data: myFileInput,
                  processData: false,
                  contentType: false,
                  success: function (result) {
                      assert.ok( true, "first call done." );
                      if(result.status != 1){
                          alert(result.message);
                      } else { location.href = result.href; }
                  },
                  error: function (e) {
                      assert.ok( false, "first call failed." );
                      alert(e.status);
                  }
          })*/

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

                    } else {
                        assert.ok( false, "Incorrect - Dropbox test" );
                    }
                },
                error: function (e) {

                    console.log("wrong")
                    assert.ok( false, "Incorrect - Dropbox test" );
                }
        });
        done();
    }, 100 );
}

  //setTimeout();
//  console.log($("#testInput").val());

//      assert.ok( true, "first call done." );
     // done();
  //}, 500 );

  //setTimeout(function() {
  //  assert.ok( true, "second call done." );
  //  done();
  //}, 1500 );

  //setTimeout(function() {
  //  assert.ok( true, "third call done." );
  //  done();
  //}, 2500 );

});