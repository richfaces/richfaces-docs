var inputCorrect = false;
var textCorrect = false;

function getPlace(){
	if(navigator.appName=="Microsoft Internet Explorer" && parseFloat(navigator.appVersion) < 7){
		document.getElementById('place').style.display = "block";
	}
}
	
function showPopup(_popupId) {
	document.getElementById(_popupId).style.display = "block";
	document.getElementById('timeOutDiv').style.display = "block";
	document.getElementById("feedback-maincontainer").style.display = "block";
		document.getElementById("guide_words").style.display = "block";
	getPlace();
}

function hidePopup(_popupId, form, iFrame, but,  container) {
	document.getElementById(_popupId).style.display = "none";
	document.getElementById('timeOutDiv').style.display = "none";
	document.getElementById(iFrame).style.display = "none";
	document.getElementById(form).style.display = "block";
	document.getElementById(but).style.display = "inline";
	document.getElementById(container).style.left="30%";
	document.getElementById(container).style.top="20%";
	document.getElementById(container).style.width="500px";
	document.getElementById(container).style.height="440px";
	getPlace();
}

function showIFrame(form, iFrame, but,  container){
	document.getElementById(form).style.display = "none";
	document.getElementById(but).style.display = "none";
	document.getElementById(iFrame).style.display = "block";
	document.getElementById(container).style.height="99%";
	document.getElementById(container).style.left="10%";
	document.getElementById(container).style.top="0";
	document.getElementById(container).style.width="80%";
	document.getElementById("guide_words").style.display = "none";
	
}
function fillForm(form){
	document.getElementById(form).attributes['action'].value = 
	document.getElementById(form).attributes['action'].value
	+'&priority='+document.getElementById('priority').value
	+'&summary='+document.getElementById('feedback-summary').value
	+'&description='+document.getElementById('feedback-description').value
	+'&environment='+document.getElementById('feedback-environment').value
	+'&components='+document.getElementById('components').value
	+'&versions='+document.getElementById('versions').value
	+'&customfield_12310031='+document.getElementById('customfield_12310031').value;
}

function submitForm(form, iFrame, but,  container){
	document.getElementById(form).submit();
	showIFrame(form, iFrame, but,  container);
}

function setFieldFlag(type, flag){
	if(type=="textarea"){
		textCorrect = flag;
	}else if(type=="text"){
		inputCorrect = flag;
	}
}
function countLeft(fieldToCheck, count, max) {
	var field = document.getElementById(fieldToCheck);
	var left = 'none';
	var char_count = field.value.length;
	var fullStr = field.value + " ";
	var initial_whitespace_rExp = /^[^A-Za-z0-9]+/gi;
	var left_trimmedStr = fullStr.replace(initial_whitespace_rExp, "");
	var non_alphanumerics_rExp = rExp = /[^A-Za-z0-9]+/gi;
	var cleanedStr = left_trimmedStr.replace(non_alphanumerics_rExp, " ");
	var splitString = cleanedStr.split(" ");
	var word_count = splitString.length -1;
	if (fullStr.length <2) {
		word_count = 0;
	}
	if (field.value.length > max){
		field.value = field.value.substring(0, max);
	}else if(count != "none"){
		left = document.getElementById(count);
		if(navigator.appName=="Microsoft Internet Explorer"){
			left.innerText = max - field.value.length;
		}else{
			left.textContent = max - field.value.length;
		}
		
	}
	if (word_count >= 1){
		setFieldFlag(field.type, true);
	}else{
		setFieldFlag(field.type, false);
	}
	if(inputCorrect && textCorrect){
		document.getElementById("feedback-submit").disabled=false;
		document.getElementById("feedback-submit").style.color="#415973";
	}else{
		document.getElementById("feedback-submit").disabled=true;
		document.getElementById("feedback-submit").style.color="#999";
	}
}


function init(input, textarea){
	document.getElementById("feedback-submit").disabled=true;
	countLeft(input, "left", 255);
	countLeft(textarea, "none", 500);
}

function dbToggle(node, expandText, collapseText) {
	var dt = node.parentNode;
	if (dt.nodeName.toLowerCase() == 'dt') {
		var dd =  dt.nextSibling;
		
		if (dd && dd.nodeName.toLowerCase() == 'dd') {
			
			if (dd.style && dd.style.display == 'none') {
				dd.style.display = '';
				node.innerHTML = collapseText;
			} else {
				dd.style.display = 'none';
				node.innerHTML = expandText;
			}
		
		}
		
	}
	
}

var toc = {
	expand: function(node) {
		toc.show(toc.findDD(node))
		toc.hide(node);
		toc.show(node.nextSibling);
	}, 
	collapse : function(node) {
		toc.hide(toc.findDD(node))
		toc.hide(node);
		toc.show(node.previousSibling);
	}, 
	findDD : function(node) {
		return node.parentNode.nextSibling;
	},
	
	hide: function(node) {
		node.style.display = "none";
	},
	show: function(node) {
		node.style.display = "";
	}
};
