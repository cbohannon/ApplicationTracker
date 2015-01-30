$(document).ready(function() {
    $("#btnSubmitApplication").click(function(evt) {
        evt.preventDefault();
        $().submitApplication();
    });

    $("#btnClearApplication").click(function(evt) {
        evt.preventDefault();
        $().clearForm();
    });

    $("#btnGetApplications").click(function(evt) {
        evt.preventDefault();
        $().retrieveApplications();
    });

    $("#btnAddApplication").click(function(evt) {
        evt.preventDefault();
        $().addApplication();
    });
});

(function ($) {
    $.fn.extend({
        submitApplication: function() {
            var jsonArray = $("#frmInput").serializeArray();
            console.log("jsonArray");
            $.ajax({
                type: "POST",
                url: "http://localhost:80/rest/applications",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(jsonArray),
                dataType: "json",
                cache: false,
                success: function(data) {
                    $("<span>Success! Data submitted.</span>").appendTo("#footerMessage");
                    console.log("Success! Data submitted: " + data);
                },
                error: function(jqXHR) {
                    $("<span>It looks like we had an error.</span>").appendTo("#footerMessage");
                    console.log("Error message: " + jqXHR.statusText +" code " + jqXHR.status);
                }
            });
            $().clearForm();
        },
        clearForm: function() {
            $("[name=company]").val("");
            $("[name=position]").val("");
            $("[name=location]").val("");
            $("[name=dateApplied]").val("");
            $("[name=contactName]").val("");
            $("[name=contactMethod]").val("");
            $("[name=contactedMeFirst]").prop("selectedIndex", 0);
            $("[name=status]").prop("selectedIndex", 0);
            $("[name=notes]").val("");
        },
        addApplication: function () {
            $("#applicationCreation").removeClass("not");
            $("#btnAddApplication").attr("disabled", true);
        },
        removeRows: function() {
            // Remove previously appended rows before executing the GET
            var tblObject = $("#tblApplications");
            if (tblObject.find("td").length > 0) {
                tblObject.find("td").remove();
                tblObject.find("input").remove();
                // Clear the footer out as well
                $("#footerMessage").find("span").remove();
            }
        },
        retrieveApplications: function() {
            $().removeRows();

            $.ajax({
                type: "GET",
                url: "http://localhost:80/rest/applications",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                cache: false,
                success: function(data) {
                    $.each(data, function (outerKey, outerObject) {
                        var row = $("<tr>");
                        $.each(outerObject, function(innerKey, innerObject) {
                            switch (innerKey) {
                                case "company":
                                    row.after().append($("<td>").text(innerObject.toString()));
                                    break;
                                case "position":
                                    row.append($("<td>").text(innerObject.toString()));
                                    break;
                                case "location":
                                    row.append($("<td>").text(innerObject.toString()));
                                    break;
                                case "dateApplied":
                                    row.append($("<td>").text(innerObject.toString()));
                                    break;
                                case "contactName":
                                    row.append($("<td>").text(innerObject.toString()));
                                    break;
                                case "contactMethod":
                                    row.append($("<td>").text(innerObject.toString()));
                                    break;
                                case "contactedMeFirst":
                                    row.append($("<td>").text(innerObject.toString()));
                                    break;
                                case "status":
                                    row.append($("<td>").text(innerObject.toString()));
                                    break;
                                case "notes":
                                    // TODO: Adjust how notes gets displayed on the page
                                    // row.append($("<td>").text(innerObject.toString()));
                                    row.append($("<td>").text("Currently not displaying notes."));
                                    break;
                                default:
                                    console.log("Error iterating over innerObject: " + innerKey + " " + innerObject);
                            }
                        });
                        var btnEdit = $('<input type="button" name="btnEdit" value="Edit" />');
                        btnEdit.appendTo(row);
                        var btnDelete = $('<input type="button" name="btnDelete" value="Delete" />');
                        btnDelete.appendTo(row);
                        $("#tblApplications").append(row);
                    });
                    $("<span>Success! Data retrieved.</span>").appendTo("#footerMessage");
                },
                error: function(jqXHR) {
                    $("<span>It looks like we had an error.</span>").appendTo("#footerMessage");
                    console.log("Error message: " + jqXHR.statusText +" code " + jqXHR.status);
                }
            });
        }
    });
}) (jQuery);