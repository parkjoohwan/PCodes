var COMM = {
    templates: []
};

COMM.loadTemplate = function(templateId, callback) {
    $.get('./templates/' + templateId + '.html', function(data) {
        COMM.templates[templateId] = data;
        if (callback) callback();
    });
}

COMM.getHTMLObj = function(templateId, input) {
    var html = COMM.templates[templateId];
    html = COMM.dynamicExpression(html, input);
    return $(html);
}

COMM.dynamicExpression = function(str, params) {
    var retStr = str;
    for (var key in params) {
        var regex = new RegExp('{' + key + '}', 'g');
        var value = params[key];
        if (value == null || value == 'null' || value == undefined || value == 'undefined') {
            value = '';
        }
        retStr = retStr.replace(regex, value);
    }
    return retStr;
}

COMM.setItem = function(key, value) {
    var saveValue = (typeof value == 'string') ? new String(value) : JSON.stringify(value);
    localStorage.setItem(key, saveValue);
};

COMM.getItem = function(key) {
    var value = localStorage.getItem(key);
    try {
        value = JSON.parse(value);
    } catch (e) {

    }
    return value;
};

COMM.removeItem = function(key) {
    localStorage.removeItem(key);
};

COMM.movePage = function(page, pageData) {
    if (pageData) COMM.setItem('__pageData', pageData);
    location.href = page;
};

COMM.getPageData = function() {
    var data = COMM.getItem('__pageData');
    COMM.removeItem('__pageData');
    return data;
};

COMM.validate = function() {
    var items = $('.mandatory');
    var messages = '', isValid = true;
    for (var inx=0; inx<items.length; inx++) {
        if ($(items[inx]).val() == '') {
            isValid = false;
            messages += $(items[inx]).attr('data') + '은(는) 필수 입력값입니다.\n';
            break;
        }
    }

    return {valid: isValid, messages: messages};
};
COMM.doPost = function(url, input, success, fail) {
    var option = {};
    option.url = url + '?__jsonParams=' + JSON.stringify(input);
    option.type = 'POST';
    option.headers = {'Content-Type': 'application/json'};
    option.success = function(data) {
        try {
            if (typeof data == 'string') data = JSON.parse(data);
            success(data);
        } catch (e) {
            if (fail && typeof fail == 'function') {
                fail(e);
            } else {
                alert(e.stack);
            }
        }
    };
    option.error = function(jqXHR, status, error) {
        if (fail && typeof fail == 'function') {
            fail(error);
        } else {
            alert(error);
        }
    };
    $.ajax(option);
};
