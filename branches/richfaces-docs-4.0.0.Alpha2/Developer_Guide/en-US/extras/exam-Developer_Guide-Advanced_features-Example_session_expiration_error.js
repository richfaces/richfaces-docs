A4J.AJAX.onExpired = function(loc, expiredMsg){
    if(window.confirm("Custom onExpired handler "+expiredMsg+" for a location: "+loc)){
        return loc;
    } else {
        return false;
    }
}
