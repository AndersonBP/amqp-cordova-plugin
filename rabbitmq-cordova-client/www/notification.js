var exec = require('cordova/exec');

var Push = {
		
	Start: function(success, failure, config) {
        exec(success, failure, 'Push', 'initialize', [config]);
    },
    Next: function(success, failure, config) {
        exec(success, failure, 'Push', 'next', [config]);
    }
};

/* @Deprecated */
window.Push = Push;

module.exports = Push;
