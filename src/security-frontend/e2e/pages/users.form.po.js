var UsersFormPage = function () {
  'use strict';

  var self = this;
  self.getRoles = element.all(by.binding('role.name'));

  self.getRole = function (roleName) {
    return self.getRoles.filter(function (elem) {
      return elem.getText().then(function (text) {
        return text === roleName;
      });
    }).first();
  };

  self.checkRole = function (roleName) {
    var roleListItem = self.getRole(roleName);
    roleListItem.element(by.css('.ng-empty')).isPresent().then(function (present) {
      if (present) {
        roleListItem.element(by.css('.ng-empty')).click();
      }
    });

  };
  self.uncheckRole = function (roleName) {
    var roleListItem = self.getRole(roleName);
    if (roleListItem.element(by.css('.ng-not-empty')).isPresent()) {
      roleListItem.element(by.css('.ng-not-empty')).click();
    }
  };

  self.submitButton = element(by.binding('$ctrl.submitText'));
};

module.exports = new UsersFormPage();
