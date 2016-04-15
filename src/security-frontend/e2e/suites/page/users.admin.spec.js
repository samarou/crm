/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Users under role ADMIN', function () {
  var loginService = require('../../login.service.js');
  var usersPage = require('../../pages/users.po.js');
  var credentials = require('../../credentials');
  var navbarPO = require('../../pages/navbar.po');
  
  beforeAll(function () {
    loginService.login(credentials.admin)
  });

  it('should be able to filter user records by roles', function () {

    usersPage.getOption('ADMIN').click();
    iterateThroughPages()
      .then(function (adminFound) {
        expect(adminFound.found).toBe(true);
      });

    function iterateThroughPages() {
      return checkAdminOnPage().then(function (isFound) {
        return checkingLoop({found: isFound, lastPage: false})
      });
    }

    function checkingLoop(adminInPage) {
      return goToNextPageIfNotLast(adminInPage.found)
        .then(function (adminInP) {
          if (adminInP.found) {
            return adminInP;
          } else if (adminInP.lastPage) {
            return additionalCheckIfAdminOnPage(adminInP.lastPage);
          } else {
            return additionalCheckIfAdminOnPage(adminInP.lastPage).then(checkingLoop);
          }
        });
    }

    function goToNextPageIfNotLast(adminFound) {
      return usersPage.isLastPage()
        .then(function (isLastPage) {
            if (!isLastPage) {
              return usersPage.nextPageButton().click()
                .then(function () {
                  return AdminInPage(adminFound, isLastPage);
                });
            } else {
              return AdminInPage(adminFound, isLastPage);
            }
          }
        );
    }

    function AdminInPage(adminFound, isLastPage) {
      return {found: adminFound, lastPage: isLastPage}
    }

    function additionalCheckIfAdminOnPage(isLastPage) {
      return checkAdminOnPage().then(function (found) {
        return {found: found, lastPage: isLastPage};
      })
    }

    function checkAdminOnPage() {
      return usersPage.getUsernamesInTable()
        .then(checkIfAdminInArray);
    }

    function checkIfAdminInArray(users) {
      return checkIfUserInArray(users, 'admin');
    }

    function checkIfUserInArray(users, name) {
      return (users.indexOf(name)) != -1;
    }
  });

  afterAll(function () {
    loginService.logout();
  });
});
