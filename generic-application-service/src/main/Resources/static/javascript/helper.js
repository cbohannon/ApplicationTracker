(function ($) {
    $.fn.extend({
        editApplication: function (evt) {
            // TODO: Lots of rendering and key capture errors need to be resolved
            var originalContent = $(evt.target).text();

            $(evt.target).addClass("cellEditing");
            $(evt.target).html("<input type='text' value='" + originalContent + "' />");
            $(evt.target).children().first().focus();

            $(evt.target).children().first().keypress(function (e) {
                // 13 is a key press "Enter"
                if (e.which == 13) {
                    var newContent = e.currentTarget.value;
                    $(evt.target).text(newContent);
                    $(evt.target).removeClass("cellEditing");
                    // Only enable the button for the edited row
                    $(evt.target).parent().find("[name=btnCommit]").removeAttr("disabled");
                }
            });

            $(evt.target).children().first().blur(function () {
                $(evt.target).text(originalContent);
                $(evt.target).removeClass("cellEditing");
            });
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
            // TODO: Find a better way to "clear" because this function causes a flicker effect
            // Remove previously appended rows before executing the GET and to remove the rows we'll look for <td>
            var tblObject = $("#tblApplications");
            if (tblObject.find("td").length > 0) {
                tblObject.find("td").remove();
                tblObject.find("input").remove();
                $("#footerMessage").find("span").remove();
            }
        }
    })
}) (jQuery);
