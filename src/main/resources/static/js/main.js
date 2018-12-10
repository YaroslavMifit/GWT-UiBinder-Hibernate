'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

var dataTable1 = document.querySelector('#dataTable1');
var dataTable2 = document.querySelector('#dataTable2');
var dataTable3 = document.querySelector('#dataTable3');
var checkBoxDataTable1 = document.querySelector('#checkBoxDataTable1');
var checkBoxDataTable2 = document.querySelector('#checkBoxDataTable2');
var checkBoxDataTable3 = document.querySelector('#checkBoxDataTable3');
var createJasperReportButton = document.querySelector('#createJasperReportButton');


function uploadSingleFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadFile");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            singleFileUploadError.style.display = "none";

            for(var i = 1; i < dataTable1.rows.length;)
            {
                dataTable1.deleteRow(i);
            }

            for(var i = 0; i < response.creditOrganization.length; i++) {
                var row = dataTable1.insertRow(i+1);

                var cell1 = row.insertCell(0);
                var element = document.createElement("input");
                element.type = "checkbox";
                element.name = response.creditOrganization[i].id;
                element.checked = false;
                cell1.appendChild(element);

                var cell2 = row.insertCell(1);
                cell2.style.width = '95%';
                cell2.innerHTML = response.creditOrganization[i].creditOrganizationName;
            }

            for(var i = 1; i < dataTable2.rows.length;)
            {
                dataTable2.deleteRow(i);
            }

            for(var i = 0; i < response.score.length; i++) {
                var row = dataTable2.insertRow(i+1);

                var cell1 = row.insertCell(0);
                var element = document.createElement("input");
                element.type = "checkbox";
                element.name = response.score[i].id;
                element.checked = false;
                cell1.appendChild(element);

                var cell2 = row.insertCell(1);
                cell2.style.width = '97%';
                cell2.innerHTML = response.score[i].scoreName;
            }

            for(var i = 1; i < dataTable3.rows.length;)
            {
                dataTable3.deleteRow(i);
            }

            for(var i = 0; i < response.userDataTitle.length; i++) {
                var row = dataTable3.insertRow(i+1);

                var cell1 = row.insertCell(0);
                var element = document.createElement("input");
                element.type = "checkbox";
                element.name = i;
                element.checked = false;
                cell1.appendChild(element);

                var cell2 = row.insertCell(1);
                cell2.style.width = '95%';
                cell2.innerHTML = response.userDataTitle[i];
            }

            singleFileUploadSuccess.innerHTML = "<p><h3>All Files Uploaded Successfully</h3></p>";
            singleFileUploadSuccess.style.display = "block";

            createJasperReportButton.innerHTML = "<button id=\"createJasperReportButton\" type=\"submit\" class=\"primary create-btn\">Create Jasper Report</button>"
            createJasperReportButton.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            createJasperReportButton.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            singleFileUploadError.style.display = "block";
        }
    }

    xhr.send(formData);
}

function createJasperReport() {
    var setCheckedCreditOrganizationId = new Set();
    var setCheckedScoreId = new Set();
    var setCheckedUserDataTitle = new Set();

    for (var i = 1; i < dataTable1.rows.length; i++)
    {
        if(dataTable1.rows[i].cells[0].children[0].checked) {
            setCheckedCreditOrganizationId.add(dataTable1.rows[i].cells[0].children[0].name);
        }
    }

    for (var i = 1; i < dataTable2.rows.length; i++)
    {
        if(dataTable2.rows[i].cells[0].children[0].checked) {
            setCheckedScoreId.add(dataTable2.rows[i].cells[0].children[0].name);
        }
    }

    for (var i = 1; i < dataTable3.rows.length; i++)
    {
        if(dataTable3.rows[i].cells[0].children[0].checked) {
            setCheckedUserDataTitle.add(dataTable3.rows[i].cells[0].children[0].name);
        }
    }

    var formData = new FormData();
    formData.append("creditOrganizationId", Array.from(setCheckedCreditOrganizationId));
    formData.append("scoreId", Array.from(setCheckedScoreId));
    formData.append("userDataTitle", Array.from(setCheckedUserDataTitle));
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/userDataReport");
    xhr.onload = function() {
        console.log(xhr.responseText);

        if (xhr.status == 200) {
            window.open(xhr.responseURL);
            createJasperReportButton.innerHTML = "<button id=\"createJasperReportButton\" type=\"submit\" class=\"primary create-btn\">Create Jasper Report</button>"
        } else {
            createJasperReportButton.innerHTML = "Произошла ошибка, попробуйте ещё раз"
            window.alert("Неполучилось")
        }
    }
    xhr.send(formData);
}

function checkAll (tableID)
{
    var val = tableID.rows[0].cells[0].children[0].checked;
    for (var i = 1; i < tableID.rows.length; i++)
    {
        tableID.rows[i].cells[0].children[0].checked = val;
    }
}

checkBoxDataTable1.addEventListener('change',function(){
    checkAll(dataTable1);
});

checkBoxDataTable2.addEventListener('change',function(){
    checkAll(dataTable2);
});

checkBoxDataTable3.addEventListener('change',function(){
    checkAll(dataTable3);
});

createJasperReportButton.addEventListener('click', function(){
    createJasperReportButton.innerHTML = "Пожалуйста подождите, идёт загрузка отчета";
    createJasperReport();
});

singleUploadForm.addEventListener('submit', function(event){
    createJasperReportButton.innerHTML = "Пожалуйста подождите, идёт загрузка файлов в базу данных";
    var files = singleFileUploadInput.files;
    singleFileUploadSuccess.style.display = "none";
    if(files.length === 0) {
        createJasperReportButton.style.display = "none";
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true);

