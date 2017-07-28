var currentPage = 0;
var currentlyLoadingNextPage = false;

$(document).ready(function () {
    considerLoadingMoreItems();
    $(window).scroll(considerLoadingMoreItems);
});

function considerLoadingMoreItems() {
    if (($(document).height() - $(window).height()) < $(window).scrollTop() + $(window).height() + 200)
    {
        if (!currentlyLoadingNextPage) {
            currentlyLoadingNextPage = true;
            console.log("Scroll to Page " + currentPage + "...");
            loadMoreItems(currentPage);
            currentPage++;
        }
    }
}

function loadMoreItems(page, items = 50) {
    $.ajax({
        url: addressPath + "?page=" + page + "&results=" + items,
        type: "GET",
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
        },
        success: function (data, status) {
            if (data.length < items) {
                $("#loading-animation").hide();
                console.log("All pages loaded.");
            } else {
                currentlyLoadingNextPage = false;
            }
            $.each(data, function (index, item) {
                $('#address-table > tbody:last-child').append(buildAddressRow(item));
            });
        },
        error: function (data, status) {
            currentlyLoadingNextPage = false;
        }
    });
}

function buildAddressRow(data) {
    var result = "<tr id=\"address-row-" + data.id + "\">" +
            "<td>" + data.id + "</td>" +
            "<td><a data-address-id=\"" + addressPath + data.id + "\" data-toggle=\"modal\" data-target=\"#showDetailModal\">" + data.firstName + "</a></td>" +
            "<td><a href=\"" + data.id + "\">" + data.lastName + "</a></td>" +
            "<td><a data-address-id=\"" + data.id + "\" data-toggle=\"modal\" data-target=\"#editDetailModal\">Edit</a></td>" +    
            "<td><a data-address-id=\"" + data.id + "\" class=\"delete-link\">Delete</a></td>" +
            "</tr>";
    return result;
}