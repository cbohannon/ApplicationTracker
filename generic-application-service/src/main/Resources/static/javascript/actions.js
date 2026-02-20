// TODO: Provide better messaging in the footer
(function($) {
    $.fn.extend({
        commitApplicationEdits: function(evt) {
            $(evt.target).parents("tr").find("#btnCommit").prop("disabled", true);

            var row = $(evt.target).parents("tr");
            var id = row.find("td:eq(0)").text();

            var payload = {
                company:          row.find("td:eq(1)").text(),
                position:         row.find("td:eq(2)").text(),
                location:         row.find("td:eq(3)").text(),
                dateApplied:      row.find("td:eq(4)").text(),
                contactName:      row.find("td:eq(5)").text(),
                contactMethod:    row.find("td:eq(6)").text(),
                contactedMeFirst: row.find("td:eq(7)").text(),
                status:           row.find("td:eq(8)").text(),
                notes:            row.find("td:eq(9)").text()
            };

            $.ajax({
                type: "PUT",
                url: "http://localhost:8181/rest/applications?id=" + id.replace("#", ""),
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(payload),
                dataType: "json",
                cache: false,
                success: [
                        function(data) {
                        $("#footerMessage").find("span").remove();
                        $("<span>Success! Data submitted.</span>").appendTo("#footerMessage");
                        console.log("Success! Data submitted: " + data);
                    }
                ],
                error: [
                        function(jqXHR) {
                        $("#footerMessage").find("span").remove();
                        $("<span>It looks like we had an error.</span>").appendTo("#footerMessage");
                        console.log("Error message: " + jqXHR.statusText +" code " + jqXHR.status);
                    }
                ]
            });
        },

        deleteApplication: function(evt) {
            var result = confirm("Are you sure?");
            if (!result) {
                $("#footerMessage").find("span").remove();
                $("<span>Action canceled.</span>").appendTo("#footerMessage");
            } else {
                var id = $(evt.target).parents("tr").find("td:eq(0)").text();

                $.ajax({
                    type: "DELETE",
                    url: "http://localhost:8181/rest/applications?application=" + id.replace("#", ""),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    cache: false,
                    success: [
                            function(data) {
                            $("#footerMessage").find("span").remove();
                            $("<span>Success! Data deleted.</span>").appendTo("#footerMessage");
                            console.log("Success! Data submitted: " + data);
                        }
                    ],
                    error: [
                            function(jqXHR) {
                            $("#footerMessage").find("span").remove();
                            $("<span>It looks like we had an error.</span>").appendTo("#footerMessage");
                            console.log("Error message: " + jqXHR.statusText +" code " + jqXHR.status);
                        }
                    ]
                });
            }
        },

        submitApplication: function() {
            var payload = {
                company:          $("#frmInput [name='company']").val(),
                position:         $("#frmInput [name='position']").val(),
                location:         $("#frmInput [name='location']").val(),
                dateApplied:      $("#frmInput [name='dateApplied']").val(),
                contactName:      $("#frmInput [name='contactName']").val(),
                contactMethod:    $("#frmInput [name='contactMethod']").val(),
                contactedMeFirst: $("#frmInput [name='contactedMeFirst']").val(),
                status:           $("#frmInput [name='status']").val(),
                notes:            $("#frmInput [name='notes']").val()
            };
            console.log(payload);

            $.ajax({
                type: "POST",
                url: "http://localhost:8181/rest/applications",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(payload),
                dataType: "json",
                cache: false,
                success: [
                        function(data) {
                        $("#footerMessage").find("span").remove();
                        $("<span>Success! Data submitted.</span>").appendTo("#footerMessage");
                        console.log("Success! Data submitted: " + data);
                    }
                ],
                error: [
                        function(jqXHR) {
                        $("#footerMessage").find("span").remove();
                        $("<span>It looks like we had an error.</span>").appendTo("#footerMessage");
                        console.log("Error message: " + jqXHR.statusText +" code " + jqXHR.status);
                    }
                ]
            });

            setTimeout(function() {
                $().clearForm();
            }, 500);
        },

        retrieveApplications: function() {
            $().removeRows();

            $.ajax({
                type: "GET",
                url: "http://localhost:8181/rest/applications",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                cache: false,
                success: [
                        function(data) {
                        $.each(data, function(outerKey, outerObject) {
                            var row = $("<tr>");
                            $.each(outerObject, function(innerKey, innerObject) {
                                switch (innerKey) {
                                    case "id":
                                        row.append($("<td>").text("#" + innerObject.toString()));
                                        break;
                                    case "company":
                                        row.append($("<td>").addClass("editable").text(innerObject.toString()));
                                        break;
                                    case "position":
                                        row.append($("<td>").addClass("editable").text(innerObject.toString()));
                                        break;
                                    case "location":
                                        row.append($("<td>").addClass("editable").text(innerObject.toString()));
                                        break;
                                    case "dateApplied":
                                        row.append($("<td>").addClass("editable").text(innerObject.toString()));
                                        break;
                                    case "contactName":
                                        row.append($("<td>").addClass("editable").text(innerObject.toString()));
                                        break;
                                    case "contactMethod":
                                        row.append($("<td>").addClass("editable").text(innerObject.toString()));
                                        break;
                                    case "contactedMeFirst":
                                        row.append($("<td>").addClass("editable").text(innerObject.toString()));
                                        break;
                                    case "status":
                                        row.append($("<td>").addClass("editable").text(innerObject.toString()));
                                        break;
                                    case "notes":
                                        row.append($("<td>").addClass("wrap").addClass("editable").text(innerObject.toString()));
                                        break;
                                    default:
                                        console.log("Error iterating over innerObject: " + innerKey + " " + innerObject);
                                }
                            });

                            var btnCommit = $("<button/>", {
                                type: "button",
                                class: "btnSmall",
                                id: "btnCommit",
                                text: "Commit",
                                disabled: "disabled"
                            });

                            var btnDelete = $("<button/>", {
                                type: "button",
                                class: "btnSmall",
                                id: "btnDelete",
                                text: "Delete"
                            });

                            row.append($("<td>").append(btnCommit).append(" ").append(btnDelete));
                            $("#tblApplications").append(row);
                        });
                        $("#footerMessage").find("span").remove();
                        $("<span>Success! Data retrieved.</span>").appendTo("#footerMessage");
                    }
                ],
                error: [
                        function(jqXHR) {
                        $("#footerMessage").find("span").remove();
                        $("<span>It looks like we had an error.</span>").appendTo("#footerMessage");
                        console.log("Error message: " + jqXHR.statusText +" code " + jqXHR.status);
                    }
                ]
            });
        }
    });
}) (jQuery);