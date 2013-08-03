$(document).ready(function() {
    $("a.confirm").click(function(e) {
        e.preventDefault();
        var location = $(this).attr('href');
        bootbox.confirm("Are you sure to proceed?", "No", "Yes", function(confirmed) {
            if(confirmed) {
                window.location.replace(location);
            }
        });
    });
});

function velocityToTxt(velocity) {
    var delta = (60 / velocity) * 60;
    var hours = Math.floor(delta / 3600);
    var minutes = Math.floor(delta / 60) % 60;
    var seconds = Math.round(delta % 60);

    return "every " +
        (hours > 0 ?  hours + " hours " : "") +
        (minutes > 0 ?  minutes + " minutes " : "") +
        (seconds > 0 ?  seconds + " seconds " : "") +
        " [" + Math.round(velocity * 100) / 100 + " posts per hour] ";
}


function logslider(position) {
    // position will be between 0 and 144
    var minp = 0;
    var maxp = 144;

    // The result should be between 1/24 an 120
    var minv = Math.log(1/24);
    var maxv = Math.log(120);

    // calculate adjustment factor
    var scale = (maxv-minv) / (maxp-minp);

    var ret = Math.exp(minv + scale*(position-minp));
    return ret > 120 ? 120 : ret < 1/24 ? 1/24 : ret;
}

function logsliderRev(value) {
    // position will be between 0 and 144
    var minp = 0;
    var maxp = 144;

    // The result should be between 1/12 an 120
    var minv = Math.log(1/24);
    var maxv = Math.log(120);

    // calculate adjustment factor
    var scale = (maxv-minv) / (maxp-minp);

    return (Math.log(value)-minv) / scale + minp;
}