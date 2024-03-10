
SessionStorage = {
    get: function (key) {
        var val = sessionStorage.getItem(key);
        if (val && typeof (val) !== "undefined" && val !== "undefined") {
            return JSON.parse(val);
        }
    },
    set: function (key, data) {
        sessionStorage.setItem(key, JSON.stringify(data));
    },
    remove: function (key) {
        sessionStorage.removeItem(key);
    },
    clearAll: function () {
        sessionStorage.clear();
    }
};
