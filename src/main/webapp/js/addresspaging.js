var currentPageOfAddresses = 1;
var currentlyLoadingNextPageOfAddresses = false;

$(document).ready(function () {
    considerLoadingMoreAddresses();
    $(window).scroll(considerLoadingMoreAddresses);
});

function considerLoadingMoreAddresses() {
    if (($(document).height() - $(window).height()) < $(window).scrollTop() + $(window).height() + 200)
    {
        if (!currentlyLoadingNextPageOfAddresses) {
            currentlyLoadingNextPageOfAddresses = true;
            console.log("Scroll to Page " + currentPageOfAddresses + "...");
            loadMoreAddresses(currentPageOfAddresses);
            currentPageOfAddresses++;
        }
    }
}

function loadMoreAddresses(page, items) {
    $.ajax({
        url: addressPath + "?page=" + page + (items ? "&results=" + items : ""),
        type: "GET",
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
        },
        success: function (data, status) {
            if (data.length < (items ? items : 1)) {
                $(".paging-footer-parent").hide();
                console.log("All pages loaded.");
            } else {
                currentlyLoadingNextPageOfAddresses = false;
            }
            $.each(data, function (index, item) {
                $('#address-table > tbody:last-child').append(buildAddressRow(item));
            });
        },
        error: function (data, status) {
            currentlyLoadingNextPageOfAddresses = false;
        }
    });
}

function buildAddressRow(data) {
    var result = "<tr id=\"address-row-" + data.id + "\">" +
            "<td>" + data.id + "</td>" +
            "<td><a data-address-id=\"" + data.id + "\" data-toggle=\"modal\" data-target=\"#showDetailModal\">" + data.firstName + "</a></td>" +
            "<td><a href=\"" + data.id + "\">" + data.lastName + "</a></td>" +
            "<td><a data-address-id=\"" + data.id + "\" data-toggle=\"modal\" data-target=\"#editDetailModal\">Edit</a></td>" +
            "<td><a data-address-id=\"" + data.id + "\" class=\"delete-link\">Delete</a></td>" +
            "</tr>";
    return result;
}