var sessionName;
var collectionView;
var collectionChoice;
var logoOffset = 0;
var logoStartLine = 0;


// This should be called when clicking a link to go home
function gotoHome(finalMsg) {
	$("#header .choice").removeClass("highlight");
	$("#header .choice1").addClass("highlight");

	// fill in top-level slots
	$("#nav").addClass("hidden");
	$("#body").html($("#home_body").html());
	$("#body .instructions").addClass("hidden");
	
	// now fill in slots inside html blocks
	if (sessionName != null) {
		$("#body .signIn").addClass("hidden");
		$('#body .msg').html('Welcome to The Reading Circle, ' + sessionName + ".");
		$("#body .instructions").removeClass("hidden");
	} else {
		$('#body .form').submit(function() {
			gotoHomeAfterSignIn($("#body .name").val());
			return false;
		});
	}
	if (finalMsg != null) {
		$('#body .msg').addClass("highlight");
		$("#body .msg").html("* " + finalMsg);
	}
}


// This should be called when clicking button to sign in with a name
function gotoHomeAfterSignIn(name) {
	// In the code below, we pass a handler to the signIn function.  This handler (function) has code that will
	// run after the sign in succeeds.
	signIn(name, function(data) { 
		var error=data["error"];
		if (error==null){
			// store the name of the reader that signed in
			sessionName=name;
		}
		// go to the home page
		gotoHome(error);
	});
}

//This should be called when clicking a link to show collections list
function gotoCollection(finalMsg) {
	$("#header .choice").removeClass("highlight");
	$("#header .choice2").addClass("highlight");

	$("#nav").removeClass("hidden");
	// fill in top-level slots
	$("#nav").html($("#collection_nav").html());
	$("#body").html($("#collection_body").html());
	
	// some stuff in the nav
	if (collectionView != null) {
		$("#nav .choice").removeClass("highlight");
		$("#nav " + collectionChoice).addClass("highlight");
	}
	if (sessionName == null) {
		$("#nav .special").addClass("hidden");
	} else {
		$("#nav .special").removeClass("hidden");
	}
	if (finalMsg != null) {
		$('#body .msg').addClass("highlight");
		$("#body .msg").html("* " + finalMsg);
	}
	
	// display the current view selected
	if ((collectionView == null) || (collectionView == 'all')) {
		getCollection(function(data) {
			fillItemsSlot(data)
			if (finalMsg == null)
				$("#body .msg").html("");
		});
	} else if (collectionView == 'reading') {
		getCurrentlyReading(function(data) {
			fillItemsSlot(data)
			if (finalMsg == null)
				$("#body .msg").html("");
		});
	} else if (collectionView == 'available') {
		getAtMyDesk(function(data) {
			fillItemsSlot(data)
			if (finalMsg == null)
				$("#body .msg").html("");
		});
	} else if (collectionView == 'unknown') {
		getUnknowns(function(data) {
			fillItemsSlot(data)
			if (finalMsg == null)
				$("#body .msg").html("");
		});
	}
	
	
}

function gotoCollectionShowAll(selector) {
	collectionView = "all";
	collectionChoice = selector;
	gotoCollection();
}

function gotoCollectionShowCurrentlyReading(selector) {
	collectionView = "reading";
	collectionChoice = selector;
	gotoCollection();
}

function gotoCollectionShowAtMyDesk(selector) {
	collectionView = "available";
	collectionChoice = selector;
	gotoCollection();
}

function gotoCollectionShowUnknowns(selector) {
	collectionView = "unknown";
	collectionChoice = selector;
	gotoCollection();
}


// This should be called when clicking a link to check in an item
function gotoCollectionAfterCheckin(item) {
	// In the code below, we pass a handler to the checkin function.  This handler (function) has code that will
	// run after the checkin succeeds.
	checkin(item, function(data) { 
		var error=data["error"];
		// go to the home page
		gotoCollection(error);
	});
}

//This should be called when clicking a link to check out an item
function gotoCollectionAfterCheckout(item) {
	// In the code below, we pass a handler to the checkout function.  This handler (function) has code that will
	// run after the chckout succeeds.
	$('#body .msg').html("checking out ...");
	checkout(item, function(data) { 
		var error=data["error"];
		// go to the collection page
		gotoCollection(error);
	});
}


// This should be called when clicking a link to mark an item as being at the reader's desk
function gotoCollectionAfterMarkAtMyDesk(item) {
	// In the code below, we pass a handler to the markAtDesk function.  This handler (function) has code that will
	// run after the marking to at my desk succeeds.
	markAtDesk(item, function(data) { 
		var error=data["error"];
		// go to the home page
		gotoCollection(error);
	});
}

// This should be called when clicking a link to mark an item as having an unknown location (somebody forgot to check it out)
function gotoCollectionAfterMarkUnknown(item) {
	// In the code below, we pass a handler to the markUnknown function.  This handler (function) has code that will
	// run after the marking unknown succeeds.
	markUnknown(item, function(data) { 
		var error=data["error"];
		// go to the home page
		gotoCollection(error);
	});
}
