var days = ["Sun", "Mon", "Tues", "Wed", "Thu", "Fri", "Sat"];

function convertDateString(epochMillis) {
    //var epochMillis = Date.parse(dateString);
    var localeDate = new Date(epochMillis);
    return formatDate(localeDate);
}

function formatDate(date) {
    return days[date.getDay()] + " " + date.getFullYear() + "/" + padNum(date.getMonth() + 1) + "/" + padNum(date.getDate()) + " " + padNum(date.getHours()) + ":" + padNum(date.getMinutes());
}

function padNum(num) {
    return num < 10 ? "0" + num : num;
}

function padDecimal(num, decimals) {
    return num.toFixed(decimals);
}

function infoDiv(infoMessage) {
    return "<div class='alert alert-info'>" + infoMessage + "</div>";
}

function errorDiv(errorMessage) {
    return "<div class='alert alert-danger'>" + errorMessage + "</div>";
}