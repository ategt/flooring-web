/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {

    $('#create-submit').on('click', function (e) {

        e.preventDefault();

        var addressData = JSON.stringify({
            firstName: $("#firstName").val(),
            lastName: $("#lastName").val(),
            company: $("#company").val(),
            streetNumber: $("#streetNumber").val(),
            streetName: $("#streetName").val(),
            city: $("#city").val(),
            state: $("#state").val(),
            zip: $("#zip").val(),
            id: 0
        });

        $.ajax({
            url: addressPath,
            type: "POST",
            data: addressData,
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-type", "application/json");
            },
            success: function (data, status) {
                resetCreateForm();

                var tableRow = buildAddressRow(data);
                $('#address-table').append($(tableRow));
                $("#showDetailModal").modal();
                populateModal(data.id);
            },
            error: function (data, status) {
                var errors = data.responseJSON.errors;
                var validationErrorMessage = "";

                resetCreateFormColors();

                $(".color-container").addClass("has-success");
                $(".error-container").html("");

                $.each(errors, function (index, error) {
                    var errorFieldName = error.fieldName;
                    validationErrorMessage += errorFieldName + ":" + error.message + "<br />";

                    updateErrorField(errorFieldName, error);
                });
                
                $("#add-contact-validation-errors").html(validationErrorMessage);
            }
        });
    });

    function buildAddressRow(data) {

        var strFooter = "<tr id=\"address-row-" + data.id + "\" >\n\
                                \n\
        <td>" + data.id + "</td>\n\
        <td><a data-address-id=\"" + data.id + "\" data-toggle=\"modal\" data-target=\"#showDetailModal\">" + data.firstName + "</a></td>\n\
        <td><a href=\"contact/show/" + data.id + "\">" + data.lastName + "</a></td>\n\
        <td><a data-address-id=\"" + data.id + "\" data-toggle=\"modal\" data-target=\"#editDetailModal\">Edit</a></td>\n\
        <td><a data-address-id=\"" + data.id + "\" class=\"delete-link\">Delete</a></td>\n\
                                                                                        \n\
        </tr>";
        
        return strFooter;
    }

    $('#showDetailModal').on('show.bs.modal', function (e) {
        var link = $(e.relatedTarget);
        var addressId = link.data('address-id');
        populateModal(addressId);
    });

    function populateModal(addressId) {
        $.ajax({
            url: addressPath + addressId,
            type: "GET",
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
            },
            success: function (data, status) {
                $('#address-first-name').text(data.firstName);
                $('#address-last-name').text(data.lastName);
                $('#address-company').text(data.company);
                $('#address-street-name').text(data.streetName);
                $('#address-street-number').text(data.streetNumber);
                $('#address-city').text(data.city);
                $('#address-state').text(data.state);
                $('#address-zipcode').text(data.zip);
            },
            error: function (data, status) {

            }
        });
    }

    $('#editDetailModal').on('show.bs.modal', function (e) {

        var link = $(e.relatedTarget);

        var addressId = link.data('address-id');

        $.ajax({
            url: addressPath + addressId,
            type: "GET",
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json")
            },
            success: function (data, status) {
                $('#edit-address-first-name').val(data.firstName);
                $('#edit-address-last-name').val(data.lastName);
                $('#edit-address-company').val(data.company);
                $('#edit-address-street-name').val(data.streetName);
                $('#edit-address-street-number').val(data.streetNumber);
                $('#edit-address-city').val(data.city);
                $('#edit-address-state').val(data.state);
                $('#edit-address-zipcode').val(data.zip);
                $('#edit-id').val(data.id);
            },
            error: function (data, status) {

            }
        });
    });

    $('#edit-address-button').on('click', function (e) {

        e.preventDefault();

        var addressData = JSON.stringify({
            id: $("#edit-id").val(),
            firstName: $("#edit-address-first-name").val(),
            lastName: $("#edit-address-last-name").val(),
            company: $("#edit-address-company").val(),
            streetNumber: $("#edit-address-street-number").val(),
            streetName: $("#edit-address-street-name").val(),
            city: $("#edit-address-city").val(),
            state: $("#edit-address-state").val(),
            zip: $("#edit-address-zipcode").val()
        });

        $.ajax({
            url: addressPath,
            type: "PUT",
            data: addressData,
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-type", "application/json");
            },
            success: function (data, status) {
                $('#editDetailModal').modal('hide');

                var tableRow = buildAddressRow(data);

                $('#address-row-' + data.id).replaceWith($(tableRow));
            },
            error: function (data, status) {
                alert("error");
            }
        });
    });

    $(document).on('click', '.delete-link', function (e) {

        e.preventDefault();

        var addressId = $(e.target).data('address-id');

        $.ajax({
            type: "DELETE",
            url: addressPath + addressId,
            success: function (data, status) {
                $('#address-row-' + addressId).remove();
            },
            error: function (data, status) {

            }
        });
    });

    function resetCreateForm() {
        resetCreateFormColors();

        $(".create-address-form").val('');
        $(".error-container").html('');
        $("#add-contact-validation-errors").html('');
    }

    function resetCreateFormColors() {
        $(".color-container").removeClass("has-error");
        $(".color-container").removeClass("has-success");
    }

    function updateErrorField(errorFieldName, error) {
        console.log(errorFieldName);
        $("#" + errorFieldName + "-errors").html("");
        $("#" + errorFieldName + "-group").removeClass("has-error");
        $("#" + errorFieldName + "-group").removeClass("has-success");
        $("#" + errorFieldName + "-errors").html(error.message);
        $("#" + errorFieldName + "-group").addClass("has-error");
    }

});