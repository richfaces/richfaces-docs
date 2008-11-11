// attach handler to window object
  Event.observe(window,'load',initializeEmailClient,false);

// initialize email application
function initializeEmailClient(){
  Event.observe('feedback-mailform', 'submit', sendEmail);
}

 // send http request
function sendEmail(e){
  var params='subject='+$F('subject')+'&message='+escape($F('message'))+'&name='+$F('name')+'&email='+$F('email');
  var xmlobj=new Ajax.Updater('feedback-state','script/send_mail.php',{method:'post',parameters: params});

  // prevent form from submitting
  Event.stop(e);
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