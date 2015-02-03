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
    $(document).on("click", "[name=btnDelete]", function(evt) {
        evt.preventDefault();
        $().deleteApplication(evt);
    });
});

(function ($) {
    $.fn.extend({
        deleteApplication: function (evt) {
            var id = $(evt.target).parents("tr").find("td:eq(0)").text();

            $.ajax({
                type: "DELETE" ,
                url: "http://localhost:80/rest/applications?application=" + id.replace("#", ""),
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                dataType: "json",
                cache: false,
                success: function(data) {
                    $("#footerMessage").find("span").remove();
                    $("<span>Success! Data deleted.</span>").appendTo("#footerMessage");
                    console.log("Success! Data submitted: " + data);
                },
                error: function(jqXHR) {
                    $("#footerMessage").find("span").remove();
                    $("<span>It looks like we had an error.</span>").appendTo("#footerMessage");
                    console.log("Error message: " + jqXHR.statusText +" code " + jqXHR.status);
                }
            });
        },
        submitApplication: function() {
            var jsonArray = $("#frmInput").serializeArray();
            console.log(jsonArray);

            $.ajax({
                type: "POST",
                url: "http://localhost:80/rest/applications",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(jsonArray),
                dataType: "json",
                cache: false,
                success: function(data) {
                    $("#footerMessage").find("span").remove();
                    $("<span>Success! Data submitted.</span>").appendTo("#footerMessage");
                    console.log("Success! Data submitted: " + data);
                },
                error: function(jqXHR) {
                    $("#footerMessage").find("span").remove();
                    $("<span>It looks like we had an error.</span>").appendTo("#footerMessage");
                    console.log("Error message: " + jqXHR.statusText +" code " + jqXHR.status);
                }
            });

            setTimeout(function() {
                $().clearForm();
            }, 500);
        },
        clearForm: function() {
            $("[name=company]").val("Company Name");
            $("[name=position]").val("Position or Title");
            $("[name=location]").val("Location");
            $("[name=dateApplied]").val("2015-01-01");
            $("[name=contactName]").val("A Contact Name");
            $("[name=contactMethod]").val("A Contact Method");
            $("[name=contactedMeFirst]").prop("selectedIndex", 0);
            $("[name=status]").prop("selectedIndex", 0);
            $("[name=notes]").val("Enter some general notes.");
        },
        addApplication: function () {
            $("#applicationCreation").removeClass("not");
        },
        removeRows: function() {
            // Remove previously appended rows before executing the GET and to remove the rows we'll look for <td>
            var tblObject = $("#tblApplications");
            if (tblObject.find("td").length > 0) {
                tblObject.find("td").remove();
                tblObject.find("input").remove();
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
                                case "id":
                                    row.after().append($("<td>").text("#" + innerObject.toString()));
                                    break;
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
                                    // row.append($("<td>").text("Currently not displaying notes."));
                                    break;
                                default:
                                    console.log("Error iterating over innerObject: " + innerKey + " " + innerObject);
                            }
                        });
                        var btnEdit = $('<input type="button" name="btnEdit" value="Edit" />');
                        btnEdit.appendTo(row);
                        var btnDelete = $('<input type="button" name="btnDelete" value="Delete" />');
                        btnDelete.appendTo(row);
                        var btnNotes = $('<input type="button" name="btnNotes" value="Notes" />');
                        btnNotes.appendTo(row);
                        $("#tblApplications").append(row);
                    });
                    $("#footerMessage").find("span").remove();
                    $("<span>Success! Data retrieved.</span>").appendTo("#footerMessage");
                },
                error: function(jqXHR) {
                    $("#footerMessage").find("span").remove();
                    $("<span>It looks like we had an error.</span>").appendTo("#footerMessage");
                    console.log("Error message: " + jqXHR.statusText +" code " + jqXHR.status);
                }
            });
        }
    });
}) (jQuery);