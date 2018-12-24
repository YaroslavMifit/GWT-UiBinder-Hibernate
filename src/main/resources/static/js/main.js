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

            getInfoSuccess("Все файлы записанны в базу успешно");

        } else {
            getInfoException(response.message, false);
        }
    };

    xhr.send(formData);
}

function createJasperReport() {
    var setCheckedCreditOrganizationId = new Set();
    var setCheckedScoreId = new Set();
    var setCheckedUserDataTitle = new Set();

    setChecked(dataTable1, setCheckedCreditOrganizationId);
    setChecked(dataTable2, setCheckedScoreId);
    setChecked(dataTable3, setCheckedUserDataTitle);

    if(setCheckedCreditOrganizationId.size == 0){
        getInfoException("Выберете хотя бы одну КРЕДИТНУЮ ОРГАНИЗАЦИЮ для формирования отчета");
    } else if(setCheckedScoreId.size == 0){
        getInfoException("Выберете хотя бы одно НАИМЕНОВАНИЕ СЧЕТА для формирования отчета");
    } else if(setCheckedUserDataTitle.size == 0){
        getInfoException("Выберете хотя бы одни ПОЛЬЗОВАТЕЛЬСКИЕ ДАННЫЕ для формирования отчета");
    } else {

        getInfoAction("Пожалуйста подождите, идёт загрузка отчета");
        var formData = new FormData();
        formData.append("creditOrganizationId", Array.from(setCheckedCreditOrganizationId));
        formData.append("scoreId", Array.from(setCheckedScoreId));
        formData.append("userDataTitle", Array.from(setCheckedUserDataTitle));
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/userDataReport");
        xhr.onload = function () {
            console.log(xhr.responseText);

            if (xhr.status == 200) {
                window.open(xhr.responseURL);
                getInfoSuccess("Успешно сформировали отчет");
            } else {
                getInfoException("Произошла ошибка, попробуйте ещё раз", false);
            }
        };
        xhr.send(formData);
    }
}

function getInfoSuccess(text){
    singleFileUploadSuccess.innerHTML ="<p><h3>" + text + "</h3></p>";
    singleFileUploadSuccess.style.color = "#23ff59";
    singleFileUploadSuccess.style.display = "block";
    createJasperReportButton.innerHTML = "<button id=\"createJasperReportButton\" type=\"submit\" class=\"primary create-btn\">Создать Jasper Report</button>";
    createJasperReportButton.style.display = "block";
    singleFileUploadError.style.display = "none";
}

function getInfoAction(text){
    createJasperReportButton.innerHTML = text;
    createJasperReportButton.style.color = "#1830ff";
    createJasperReportButton.style.display = "block";
    singleFileUploadSuccess.style.display = "none";
    singleFileUploadError.style.display = "none";
}

function getInfoException (text, button){
    singleFileUploadError.innerHTML = text;
    singleFileUploadError.style.color = "#f00";
    if(button != false) {
        createJasperReportButton.innerHTML = "<button id=\"createJasperReportButton\" type=\"submit\" class=\"primary create-btn\">Создать Jasper Report</button>";
    } else {
        createJasperReportButton.style.display = "none"
    }
    singleFileUploadSuccess.style.display = "none";
    singleFileUploadError.style.display = "block";
}

function setChecked (tableID, collection){
    for (var i = 1; i < tableID.rows.length; i++)
    {
        if(tableID.rows[i].cells[0].children[0].checked) {
            collection.add(tableID.rows[i].cells[0].children[0].name);
        }
    }
}

function checkAll (tableID){
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
    createJasperReport();
});

singleUploadForm.addEventListener('submit', function(event){
    getInfoAction("Пожалуйста подождите, идёт загрузка файлов в базу данных");
    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        getInfoException("Please select a file");

    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true);

