var id = 15341;
for(var i = id ; i > 15000 ; i-- ){
	$.ajax({
		url: "http://localhost:8080/timing/" + i,	
		beforeSend: function (xhr) {
	            xhr.setRequestHeader("Accept", "application/json");
	    },
		success: function(data,status){
			$("body:last-child").append("- " + data.id + ", " + data.differenceTime + "ms , " + data.invokingClassName + " : " + data.invokingMethodName + "<br />");
			//alert(status);
		},
		fail: function(data,status){
			alert(status);
		}
	});
}