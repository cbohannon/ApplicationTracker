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

    $(document).on("click", "#btnDelete", function(evt) {
        evt.preventDefault();
        $().deleteApplication(evt);
    });

    $(document).on("click", "#btnCommit", function(evt) {
        evt.preventDefault();
        $().commitApplicationEdits(evt)
    });

    $(document).on("click", ".editable", function(evt) {
        evt.preventDefault();
        $().editApplication(evt)
    })
});