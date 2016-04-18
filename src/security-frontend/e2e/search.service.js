var SearchService = function () {
	'use strict';

	var self = this;
	self.search = function (page, elementName, searchCriteria) {
		self.page = page;
		self.elementName = elementName;
		self.searchCriteria = searchCriteria;
		return iterate();
	};

	function iterate() {
		var elements = findElements(self.elementName);
		return elements.count().then(function (count) {
			if (!count && !self.page.nextPageButton.isEnabled()) {
				fail('Element didn\'t find');
			}
			if (count) {
				return elements.first();
			} else {
				self.page.nextPageButton.click();
				return iterate();
			}
		});
	}


	function findElements(element) {
		self.page.searchTab.sendKeys(self.elementName);
		return self.page.pageList().filter(function (row) {
			return row.element(self.searchCriteria).getText().then(function (text) {
				return text === element;
			});
		})
	}

};

module.exports = new SearchService();

