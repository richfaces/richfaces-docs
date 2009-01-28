/*
// attach handler to window object
  Event.observe(window,'load',initializeEmailClient,false);

// initialize email application
function initializeEmailClient(){
  Event.observe('feedback-mailform', 'submit', sendEmail);
}

 // send http request
function sendEmail(e){

  // prevent form from submitting
  Event.stop(e);
  var params='subject='+$F('subject')+'&emailContent='+escape($F('emailContent'))+'&senderName='+$F('senderName')+'&from='+$F('from')+'&path='+window.location;
  var xmlobj=new Ajax.Updater('feedback-state','http://192.168.0.194:8090/feedback/FeedbackRF',{method:'get',parameters: params});
}
*/
function feedbackAppear(){
			document.getElementById('feedback-maincontainer').style.display = "block";
		}
		function feedbackClose(){
			document.getElementById('feedback-maincontainer').style.display = "none";
		}

	window.onload = function(){
		var array = new Array();
		for(i=2; i<=6; i++){
		    array[i]=document.getElementsByTagName('h'+i).item('a');
		}	
		for (var i in array){
			if(array[i] != null && array[i].className == 'title'){
				array[i].childNodes[0].setAttribute("href",window.location.toString().replace(/#[0-9A-Za-z_\-]*/, "")+"#"+array[i].childNodes[0].id);		
			}
		}
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
