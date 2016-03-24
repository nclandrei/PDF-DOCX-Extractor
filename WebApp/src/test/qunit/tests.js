QUnit.begin(function( details ) {
  console.log( "Test amount:", details.totalTests );
});

/*// Not using asyncTest as it will be deprecated soon
function setupModule() {
  $('input[name="text"]').on('click', function() {
       $('.has-error').hide();
  })
  $('input[name="text"]').on('keypress', function() {
      $('.has-error').hide();
  });
}

module('tests', {setup:setupModule});

test("errors should be hidden on key press", function() {
  $('input[name="text"]').trigger('keypress')
  equal($('.has-error').is(':visible'), false);
});

test("errors not be hidden unless there is a keypress", function() {
  equal($('.has-error').is(':visible'), true);
});

test("errors should be hidden on click", function() {
  $('input[name="text"]').click()
  equal($('.has-error').is(':visible'),false);
});*/

QUnit.test( "multiple call done()", function( assert ) {
  assert.expect( 3 );
  var done = assert.async( 3 );

  setTimeout(function() {
  //var button = Dropbox.createChooseButton({})
  //$('#localUpload label').click();

  //var formData = new FormData(document.getElementById("localUpload"));
  var formData = new FormData();
  formData.append("file",myFileInput.files[0], "sample.pdf");

  $.ajax({
              type: "POST",
              url: "/uploadFile",
              enctype: 'multipart/form-data',
              data: formData,
              processData: false,
              contentType: false,
              success: function (result) {
                  if(result.status != 1){
                      alert(result.message);
                  } else { location.href = result.href; }
              },
              error: function (e) {
                  alert('Failure ' + e.status);
              }
          });

    assert.ok( true, "first call done." );
    done();
  }, 500 );

  setTimeout(function() {
    assert.ok( true, "second call done." );
    done();
  }, 1500 );

  setTimeout(function() {
    assert.ok( true, "third call done." );
    done();
  }, 2500 );
});