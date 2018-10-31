var WRITER = {};

var WRITER.write = function(){
    var validator = COMM.validate();
    if (!validator.valid) {
        alert(validator.messages);
        return;
    }
};

$(document).ready(function() {
    $('#btnWrite').on('click', function() {
        WRITER.write();
    });
});
