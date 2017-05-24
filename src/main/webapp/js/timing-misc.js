var id = 16440;
for(var i = 0 ; i < 15 ; i++ ){
	$.ajax({
		url: "http://localhost:8084/FlooringMasteryWeb/timing/" + (id + i),	
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