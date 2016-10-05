/* Copyright (c) 2006-2007 Mathias Bank (http://www.mathias-bank.de)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php) 
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 * 
 * Version 2.1
 * 
 * Thanks to 
 * Hinnerk Ruemenapf - http://hinnerk.ruemenapf.de/ for bug reporting and fixing.
 * Tom Leonard for some improvements
 * 
 * Improved by Firas Wu
 * Need jQuery 1.6 or above
 * 2015-11-22
 */
jQuery.fn.extend({
	/**
	 * @param  string paramName  The parameter name
	 * @return string  The parameter values of the given name
	 * @return null    If the desired param does not exist
	 *
	 * To get the parameter values of the page's URL:
	 * @example value = $(document).getUrlParam("paramName");
	 * 
	 * To get the parameter values of a html element attribute (src/href attribute)
	 * @example value = $('#imgLink').getUrlParam("paramName");
	 */
	getUrlParam: function(paramName) {
		paramName = encodeURI(decodeURI(paramName));

		var result = new Array(), params = null;
		var link = null;
		var $this = $(this);
		var nodeType = $this.prop("nodeType");
		
		if (nodeType == 1) { // Element
			link = $this.attr("src");
			if (typeof(link) != "string")
				link = $this.attr("href");
		} else if (nodeType == 9) { // Document
			link = window.location.href;
		}
		
		if (typeof(link) == "string") {
			var index = link.indexOf("?");
			if (index > -1)
				params = link.substring(index + 1).split("&");
		}
		
		if (typeof(params) != "object" || typeof(params.length) != "number")
			return null;

		for (var i = 0; i < params.length; i++) {
			var item = params[i].split("=");
			if (encodeURI(decodeURI(item[0])) == paramName) {
				returnVal.push(item[1]);
			}
        }

		if (returnVal.length == 0) return null;
		if (returnVal.length == 1) return returnVal[0];
		return returnVal;
	},
	
	/**
	 * @param  string paramName  The parameter name
	 * @param  string setVal     The parameter value to be set
	 */
	setUrlParam: function(paramName, setVal) {
		paramName = encodeURI(decodeURI(paramName));
		setVal = encodeURI(setVal);

		var result = new Array(), params = null;
		var link = null;
		var $this = $(this);
		var nodeType = $this.prop("nodeType");
		
		if (nodeType == 1) { // Element
			link = $this.attr("src");
			if (typeof(link) != "string") {
				link = $this.attr("href");
				nodeType = 2; // Just for attribute "href"
			}
		} else if (nodeType == 9) { // Document
			link = window.location.href;
		}
		
		if (typeof(link) == "string") {
			var index = link.indexOf("?");
			if (index > -1) {
				params = link.substring(index + 1).split("&");
				link = link.substring(0, index + 1) + paramName + "=" + setVal + "&";
				for (var i = 0; i < params.length; i++) {
					var item = params[i].split("=");
					if (encodeURI(decodeURI(item[0])) != paramName) {
						link = link + params[i] + "&";
					}
				}
				link = link.substring(0, link.length - 1);
			} else {
				link = link + "?" + paramName + "=" + setVal;
			}
			
			if (nodeType == 1) { // Element with "src" attribute
				$this.attr("src", link);
			} else if (nodeType == 2) { // Element with "href" attribute
				$this.attr("href", link);
			} else (nodeType == 9) { // Document
				window.location.href = link;
			}
		}
	}
});