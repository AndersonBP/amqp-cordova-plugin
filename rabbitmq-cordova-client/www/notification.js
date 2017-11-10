var exec = require('cordova/exec');

var Push = {
		
	Start: function(success, failure, config) {
        exec(success, failure, 'Push', 'initialize', [config]);
    },
    SaveConfigs: function(success, failure, config) {
        exec(success, failure, 'Push', 'saveConfigs', [config]);
    },
    ClearConfigs: function(success, failure, config) {
        exec(success, failure, 'Push', 'clearConfigs', [config]);
    }
    
};

/* @Deprecated */
window.Push = Push;

module.exports = Push;
