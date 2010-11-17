/*
 * Replace the ModalBox dialog with a simple window.confirm() dialog (which can be properly
 * scripted by Selenium). 
 */
Tapx.runModalDialog = function(title, message, proceed) {
	if (window.confirm(message))
		proceed.defer();
};
