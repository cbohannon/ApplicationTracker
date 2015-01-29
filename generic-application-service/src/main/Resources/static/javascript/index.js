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
            $.ajax({type: "POST",
                url: "http://localhost:80/rest/applications",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(jsonArray),
                dataType: "json",
                success: function(data) {
                    console.log(data);
                }
            })
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
        retrieveApplications: function() {
            // TODO: Clear / remove previously appended rows
            $.get("http://localhost:80/rest/applications", function (data, status) {
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
                    $("#tblApplications").append(row);
                });
                console.log("Status: " + status)
            });
        }
    });
}) (jQuery);