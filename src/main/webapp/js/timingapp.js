$(document).ready(function () {
    $.ajax({
        url: timingPath,
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
        }
    }).done(function (data, status) {
        $(document.body).append("<div class=\"timing-container\"><a href=\"" + timingPath + data.id + "\">Last Request Processed in " + data.differenceTime + " ms</a></div>");
    });
});