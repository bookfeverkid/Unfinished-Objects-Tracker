
document.getElementById("yourDiv").style.width = project.percentComplete;

function changeWidth(newWidth) {
var progressBar = document.propertySelector('progress-bar');
progressBar.style.width = newWidth + '%';
};


function hideorshow() {
    if (document.getElementById('radiobuttonid').checked) {
     document.getElementById('reveal-if-active').style.display = 'block';
     } else {
        document.getElementById('reveal-if-active').style.display = 'none';
       }
 }