// TODO: Provide better messaging in the footer
(function($) {
    $.fn.extend({
        commitApplicationEdits: function(evt) {
            $(evt.target).parents("tr").find("#btnCommit").prop("disabled", true);

            // TODO: Should I bother to build and pass .json or just pass an array?
            var id = $(evt.target).parents("tr").find("td:eq(0)").text();

            // Get all the data in the row except for the "id"
            var tableData = [];
            tableData.push("{" + "\"company\":" + "\"" + $(evt.target).parents("tr").find("td:eq(1)").text() + "\"");
            tableData.push("\"position\":" + "\"" + $(evt.target).parents("tr").find("td:eq(2)").text() + "\"");
            tableData.push("\"location\":" + "\"" + $(evt.target).parents("tr").find("td:eq(3)").text() + "\"");
            tableData.push("\"dateApplied\":" + "\"" + $(evt.target).parents("tr").find("td:eq(4)").text() + "\"");
            tableData.push("\"contactName\":" + "\"" + $(evt.target).parents("tr").find("td:eq(5)").text() + "\"");
            tableData.push("\"contactMethod\":" + "\"" + $(evt.target).parents("tr").find("td:eq(6)").text() + "\"");
            tableData.push("\"contactedMeFirst\":" + "\"" + $(evt.target).parents("tr").find("td:eq(7)").text() + "\"");
            tableData.push("\"status\":" + "\"" + $(evt.target).parents("tr").find("td:eq(8)").text() + "\"");
            tableData.push("\"notes\":" + "\"" + $(evt.target).parents("tr").find("td:eq(9)").text() + "\"" + "}");

            $.ajax({
                type: "PUT",
                url: "http://localhost:8181/rest/applications?id=" + id.replace("#", ""),
                contentType: "application/json; charset=utf-8",
                data: tableData.toString(),
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
            var jsonArray = $("#frmInput").serializeArray();
            console.log(jsonArray);

            $.ajax({
                type: "POST",
                url: "http://localhost:8181/rest/applications",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(jsonArray),
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