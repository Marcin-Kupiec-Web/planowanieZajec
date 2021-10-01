

	// Set timeout variables.
	var timoutWarning = 240000; // Display warning in 14 Mins.
	var timoutNow = 300000; // Timeout in 15 mins.
	var logoutUrl = window.location.origin+'/dziennikSzkolenPlany/index.xhtml'; // URL to logout page.

	var warningTimer;
	var timeoutTimer;

	// Start timers.
	function StartTimers() {
	    warningTimer = setTimeout("IdleWarning()", timoutWarning);
	    timeoutTimer = setTimeout("IdleTimeout()", timoutNow);
	}

	// Reset timers.
	function ResetTimers() {
		
	    clearTimeout(warningTimer);
	    clearTimeout(timeoutTimer);
	    StartTimers();
	    $("#timeout").css({'display':'none'});
	}

	// Show idle timeout warning dialog.
	function IdleWarning() {
		
		$("#timeout").css({'display':'block'});
	}

	// Logout the user.
	function IdleTimeout() {
	    window.location = logoutUrl;
	}

	function myFunction() {
		  var x = document.getElementById("myTopnav");
		  if (x.className === "topnav") {
		    x.className += " responsive";
		  } else {
		    x.className = "topnav";
		  }
		}
