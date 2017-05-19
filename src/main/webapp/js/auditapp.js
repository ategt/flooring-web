var currentPage = 0;

$(document).ready(function () {
    considerLoadingMoreAudits();
    $(window).scroll(considerLoadingMoreAudits);
});

function considerLoadingMoreAudits() {
    if (($(document).height() - $(window).height()) > $(window).scrollTop() - 200)
    {
        console.log("Scroll to Page " + currentPage + "...");
        loadMoreAudits(currentPage);
        currentPage++;
    }
}

function loadMoreAudits(page, audits = 50) {
    $.ajax({
        url: auditPath + "?page=" + page + "&audits_per_page=" + audits,
        type: "GET",
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
        },
        success: function (data, status) {
            if (data.length < audits) {
                $("#loading-animation").hide();
            }
            $.each(data, function (index, item) {
                $('#audit-table > tbody:last-child').append(buildAuditRow(sanitizeAudit(item)));
            });
        },
        error: function (data, status) {

        }
    });
}

function buildAuditRow(data) {
    var result = "<tr id=\"audit-row-" + data.id + "\">" +
            "<td>" + data.id + "</td>" +
            "<td>" + data.orderid + "</td>" +
            "<td>" + data.actionPerformed + "</td>" +
            "<td>" + data.date + "</td>" +
            "<td>" + data.logDate + "</td>" +
            "<td>" + data.orderName + "</td>" +
            "<td>" + data.orderTotal + "</td>" +
            "</td>";
    return result;
}

function formatDate(date) {
    var monthNames = ["January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

    return monthNames[date.getMonth()] + " " + date.getDate() + ", " + date.getFullYear();
}

function sanitizeAudit(audit) {
    if (audit.id == null) {
        audit.id = "";
    }
    if (audit.orderid == null) {
        audit.orderid = "";
    }
    if (audit.actionPerformed == null) {
        audit.actionPerformed = "";
    }
    if (audit.date == null) {
        audit.date = "";
    } else {
        audit.date = new Date(audit.date);
        audit.date = formatDate(audit.date);
    }
    if (audit.logDate == null) {
        audit.logDate = "";
    } else {
        audit.logDate = new Date(audit.logDate);
        audit.logDate = formatDate(audit.logDate);
    }
    if (audit.orderName == null) {
        audit.orderName = "";
    }
    if (audit.orderTotal == null) {
        audit.orderTotal = "";
    }
    return audit;
}