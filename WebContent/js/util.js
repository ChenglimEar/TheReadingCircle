
function signIn(reader, successHandler) {
	$.ajax(

			{

			url : "api/login",

			type: 'POST',
			
			contentType:"application/json; charset=utf-8",

			//dataType : "application/json",

			data: JSON.stringify({"name":reader}),

			success:successHandler,

			error: function() {alert(data); }

			}

			);

}

function checkin(item, successHandler) {
	$.ajax(

			{

			url : "api/checkin",

			type: 'POST',
			
			contentType:"application/json; charset=utf-8",

			//dataType : "application/json",

			data: JSON.stringify({"name":item,"reader":sessionName}),

			success:successHandler,

			error: function() {alert(data); }

			}

			);

}

function checkout(item, successHandler) {
	$.ajax(

			{

			url : "api/checkout",

			type: 'POST',
			
			contentType:"application/json; charset=utf-8",

			//dataType : "application/json",

			data: JSON.stringify({"name":item,"reader":sessionName}),

			success:successHandler,

			error: function() {alert(data); }

			}

			);

}

function markAtDesk(item, successHandler) {
	$.ajax(

			{

			url : "api/markAtMyDesk",

			type: 'POST',
			
			contentType:"application/json; charset=utf-8",

			//dataType : "application/json",

			data: JSON.stringify({"name":item,"reader":sessionName}),

			success:successHandler,

			error: function() {alert(data); }

			}

			);

}

function markUnknown(item, successHandler) {
	$.ajax(

			{

			url : "api/markUnknown",

			type: 'POST',
			
			contentType:"application/json; charset=utf-8",

			//dataType : "application/json",

			data: JSON.stringify({"name":item,"reader":sessionName}),

			success:successHandler,

			error: function() {alert(data); }

			}

			);

}
function getCollection(successHandler) {
	doGet("collection",successHandler);	
}

function getCurrentlyReading(successHandler) {
	doGet("reading",successHandler);	
}

function getAtMyDesk(successHandler) {
	doGet("available",successHandler);	
}

function getUnknowns(successHandler) {
	doGet("unknown",successHandler);	
}

function doGet(method, successHandler) {
	$.ajax(

			{
			method: "GET",
			url : "api/" + method,
			data: { "reader": sessionName},
			success:successHandler,

			error: function() {alert(data); }

			}

			);
	
}

function fillItemsSlot(list) {
	var items = "";
	var i = 0;
	for (;i < list.length; i++) {
		var links = "";
		var item = list[i];
		var name = item["name"];
		var title = item["title"];
		var reader = item["reader"];
		var status = item["status"];
		if (reader == null) {
			// unknown status
			reader = "";
			status = "";
			links = $('#item_checkout').html().replace("item",name) + " " + $('#item_at_my_desk').html().replace("item",name);
			items = items + $("#collection_item_unknown").html().replace("$(title)",title).replace("links",links);
		} else {
			// handle incomplete info
			if (status == null)
				status = "";
			if (title == null)
				title = "<failed to retrieve title>";
			// known status
			if (reader == sessionName) {
				if (status == "reading") {
					links = $('#item_checkin').html().replace("item",name) + " ";
				} else {
					links = $('#item_status_unknown').html().replace("item",name) + " ";
				}
			}
			if (status == "done") {
				links = links + $('#item_checkout').html().replace("item",name);
			}
			items = items + $("#collection_item").html().replace("$(title)",title).replace("reader",reader).replace("status",status).replace("links",links);
		}
	}
	$("#body .items").html(items);
	
}

function buildLogo() {
	var numLetters = $("#logo span").length;
	var step = 360/numLetters;
	var i;
	for (i = 0; i < numLetters; i++) {
		if (i >= logoOffset) {
			$("#logo .char" + i).css({
				"left":(50+step*(i-logoOffset)) + "px"
			});
		} else {
			$("#logo .char" + i).css({
				"left":"50px",
				"-ms-transform":"rotate(" + (step*i - step*logoOffset) + "deg)",
				"-webkit-transform":"rotate(" + (step*i - step*logoOffset) + "deg)",
				"transform":"rotate(" + (step*i - step*logoOffset) + "deg)"
			});
		}
	}
	logoOffset += 1;

}

function hello() {
	$.ajax(

			{
			method: "GET",
			url : "api/msg",

			success:function(data) {
				alert(data["msg"]);
			},

			error: function() {alert(data); }

			}

			);
	
}

function test() {
	alert("Hi!");
	
}

function test2(){
	$("#body").html("Test 2");
	
}

function test3(){
	// replace to the nav and body slots
	$("#nav").html($("#home_nav").html());
	$("#body").html($("#home_body").html());

}

function test4() {
	// replace to the nav and body slots
	$("#nav").html($("#collections_nav").html());
	$("#body").html($("#collections_body").html());
	
}
