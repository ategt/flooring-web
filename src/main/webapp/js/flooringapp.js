/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {

    $('#create-submit').on('click', function (e) {

        e.preventDefault();
        var postableUrl = flooringPath;
        var orderData = JSON.stringify({
            name: $("#name").val(),
            state: $("#state-selector").val(),
            product: $("#product-selector").val(),
            date: $("#jQueryDatePicker").val(),
            area: $("#area").val()
        });
        $.ajax({
            url: postableUrl,
            type: "POST",
            data: orderData,
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-type", "application/json");
            },
            success: function (data, status) {

                var tableRow = buildOrderRow(data);
                $('#order-table').append($(tableRow));
            },
            error: function (data, status) {
                var errors = data.responseJSON.errors;
                $.each(errors, function (index, error) {
                    $("#add-contact-validation-errors").append(error.fieldname + ":" + error.message + "<br />");
                });
            }
        });
    });
    function buildOrderRow(data) {

        var strRowTable = "<tr id=\"order-row-" + data.id + "\" >\n\
                                \n\
        <td><a data-order-id=\"" + data.id + "\" data-toggle=\"modal\" data-target=\"#showDetailModal\">" + data.id + "</a></td>\n\
        <td><a href=\"contact/show/" + data.id + "\">" + data.name + "</a></td>\n\
        <td><a data-order-id=\"" + data.id + "\" data-toggle=\"modal\" data-target=\"#editDetailModal\">Edit</a></td>\n\
        <td><a data-order-id=\"" + data.id + "\" class=\"delete-link\">Delete</a></td>\n\
                                                                                        \n\
        </tr>";
        var strTableRow = "  <tr>\n\
                                <td><a href=\"/FlooringMaster/show/" + data.id + "\">" + data.id + "</a></td>\n\
                                <td><a href=\"/FlooringMaster/show/" + data.id + "\">" + data.name + "</a></td>\n\
                                <td><a href=\"/FlooringMaster/edit/" + data.id + "\">Edit</a></td>\n\
                                <td><a href=\"/FlooringMaster/delete/" + data.id + "\">Delete</a></td>\n\
\n\
                            </tr>";
        return strRowTable;
    }

    function buildSelectorOption() {
        var invalidOption = "<option value=\"Error - Data Invalid\" >Error - Data Invalid</option>";
        return invalidOption;
    }

    $('#showDetailModal').on('show.bs.modal', function (e) {
        var link = $(e.relatedTarget);
        var orderId = link.data('order-id');
        $.ajax({
            url: flooringPath + orderId,
            type: "GET",
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
            },
            success: function (data, status) {

                $('#order-name').text(data.name);
                $('#order-date').text(data.date);
                $('#order-area').text(data.area);
                $('#order-id').text(data.id);
                $('#order-labor-total-cost').text(formatDollar(data.laborCost));
                $('#order-labor-unit-cost').text(formatDollar(data.laborCostPerSquareFoot));
                $('#order-material-cost').text(formatDollar(data.materialCost));
                $('#order-material-unit-cost').text(formatDollar(data.costPerSquareFoot));
                $('#order-total-invoice').text(formatDollar(data.total));
                $('#order-total-tax').text(formatDollar(data.tax));
                $('#order-tax-rate').text(data.taxRate + " %");
                var totalStr = data.total;
                var taxStr = data.tax;
                var total = parseInt(totalStr);
                var tax = parseInt(taxStr);
                var subTotal = eval(total - tax);
                subTotal = formatDollar(subTotal);
                $('#order-subtotal').text(subTotal);
                var orderDate = data.date;
                orderDate = new Date(orderDate);
                if (orderDate === null) {
                    orderDate = new Date();
                }

                $('#order-date-f').text(orderDate.toDateString());
                var stateObj = data.state;
                var displayState = "Error - Data Invalid";
                if (stateObj !== null)
                    displayState = stateObj.stateName;
                $('#order-state').text(displayState);
                var productObj = data.product;
                var displayProduct = "Error - Data Invalid";
                if (productObj !== null)
                    displayProduct = productObj.productName;
                $('#order-product').text(displayProduct);
            },
            error: function (data, status) {
                alert(status);
            }
        });
    });
    $('#editDetailModal').on('show.bs.modal', function (e) {

        var link = $(e.relatedTarget);
        var orderId = link.data('order-id');
        $.ajax({
            url: flooringPath + orderId,
            type: "GET",
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
            },
            success: function (data, status) {
                $('#edit-order-name').val(data.name);
                $('#edit-order-area').val(data.area);
                $('#edit-id').val(data.id);
                $('#edit-display-id').text(data.id);
                var $datepicker = $('#edit-order-date');
                $datepicker.datepicker();
                var orderDate = data.date;
                if (orderDate === null)
                    orderDate = new Date();
                $datepicker.datepicker('setDate', orderDate);
                var stateObj = data.state;
                var displayState = "Error - Data Invalid";
                if (stateObj !== null) {
                    displayState = stateObj.stateName;
                } else {
                    $('#edit-order-state').append($(buildSelectorOption()));
                }

                console.log(displayState);
                $('#edit-order-state').val(displayState);
                var productObj = data.product;
                var displayProduct = "Error - Data Invalid";
                if (productObj !== null) {
                    displayProduct = productObj.productName;
                } else {
                    $('#edit-order-product').append($(buildSelectorOption()));
                }

                $('#edit-order-product').val(displayProduct);
                console.log(displayProduct);
            },
            error: function (data, status) {
                alert(status);
            }
        });
    });
    $('#edit-order-button').on('click', function (e) {

        e.preventDefault();
        var orderData = JSON.stringify({
            id: $("#edit-id").val(),
            name: $("#edit-order-name").val(),
            product: $("#edit-order-product").val(),
            date: $("#edit-order-date").val(),
            state: $("#edit-order-state").val(),
            area: $("#edit-order-area").val()
        });
        $.ajax({
            url: flooringPath,
            type: "PUT",
            data: orderData,
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-type", "application/json");
            },
            success: function (data, status) {
                $('#editDetailModal').modal('hide');
                var tableRow = buildOrderRow(data);
                $('#order-row-' + data.id).replaceWith($(tableRow));
            },
            error: function (data, status) {
                alert("error");
            }
        });
    });
    $(document).on('click', '.delete-link', function (e) {

        e.preventDefault();
        var orderId = $(e.target).data('order-id');
        $.ajax({
            type: "DELETE",
            url: flooringPath + orderId,
            success: function (data, status) {
                $('#order-row-' + orderId).remove();
            },
            error: function (data, status) {
                alert(status);
            }
        });
    });
    $(document).on('blur', '.order-name', function (e) {
        var searchString = $("#name").val();
        $.ajax({
            url: addressPath + searchString + "/search",
            type: "GET",
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
            },
            success: function (data, status) {
                updateAddress(data);
            },
            error: function (data, status) {
                if (status == "parsererror") {
                    $("#name-address").html("No Contact Info Found. <br /> <a href=\"/FlooringMasteryWeb/address/?company=" + searchString + "\" >Click Here To Add Info</a>");
                }
            }
        });
        $("#name-address").html("Searching for Contact Information...");
    });
//    $(document).on('keypress', '.order-name', function (e) {
//        var searchString = $("#name").val();
//
//        $.ajax({
//            url: addressPath + searchString + "/name_completion",
//            type: "GET",
//            dataType: 'json',
//            beforeSend: function (xhr) {
//                xhr.setRequestHeader("Accept", "application/json");
//            },
//            success: function (data, status) {
//                updateAddress(data);
//            },
//            error: function (data, status) {
//                console.log("Autocomplete Status: " + status);
//            }
//        });
//    });

    $('#name').autocomplete({
        source: addressPath + "name_completion",
        minLength: 2,
        select: function (event, ui) {
            $('#name').val(ui.item);
        }

    });

    function updateAddress(address) {
        var addressText = address.lastName + ", " + address.firstName + "<br />" +
                address.company + "<br />" +
                address.streetNumber + " " + address.streetName + "<br />" +
                address.city + ", " + address.state + " " + address.zip;
        $("#name-address").html(addressText);
    }

    function formatDollar(num) {
        var p = num.toFixed(2).split(".");
        return "$" + p[0].split("").reverse().reduce(function (acc, num, i, orig) {
            return  num + (i && !(i % 3) ? "," : "") + acc;
        }, "") + "." + p[1];
    }
});