var exec = require('cordova/exec');

var Push = {
		
	Listen: function(success, failure, config) {
        exec(success, failure, 'Push', 'initialize', [config]);
    }
};

/* @Deprecated */
window.Push = Push;

module.exports = Push;
    
