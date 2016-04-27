var SearchService = function () {
  'use strict';

  var self = this;
  self.search = function (page, elementName, searchCriteria) {
    self.page = page;
    self.elementName = elementName;
    self.searchCriteria = searchCriteria;
    self.page.searchTab.sendKeys(self.elementName);
    return iterate();
  };

  function iterate() {
    var elements = findElements(self.elementName);
    return elements.count().then(function (count) {
      if (count) {
        return elements.first();
      } else {
        return isLastPage(self.page.nextPageButton).then(function (isLast) {
          if (isLast) {
            return null;
          } else {
            self.page.nextPageButton.element(by.css('a')).click();
            return iterate();
          }
        });
      }
    });
  }

  function findElements(element) {
    return self.page.pageList().filter(function (row) {
      return row.element(self.searchCriteria).getText().then(function (text) {
        return text === element;
      });
    })
  }

  function isLastPage(element) {
    return hasClass(element, 'disabled').then(function (isDisabled) {
      return isDisabled;
    });
  }

  function hasClass(element, clazz) {
    return element.getAttribute('class').then(function (classes) {
      return classes.split(' ').indexOf(clazz) !== -1;
    });
  }
};

module.exports = new SearchService();

