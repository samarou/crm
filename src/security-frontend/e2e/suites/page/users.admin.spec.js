/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Users under role ADMIN', function () {
  var auth = require('../../authentication.js');


  beforeAll(function () {
    auth.login('admin', 'admin');
  });

  it('should be able to filter user records by roles', function () {
    var usersPage = require('../../pages/users.po.js');
    browser.get('/#/users').then(function () {
      usersPage.getOption('ADMIN').click()
        .then(iterateThroughPages)
        .then(function (adminFound) {
          expect(adminFound.found).toBe(true);
        });
    });

    afterAll(function () {
      auth.logout();
    });


    function goToNextPageIfNotLast(adminFound) {
      return usersPage.isLastPage()
        .then(function (isLastPage) {
            if (!isLastPage) {
              return usersPage.nextPageButton().click().then(function () {
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
            return additionalCheckIfAdminOnPage();
          } else {
            return additionalCheckIfAdminOnPage().then(checkingLoop);
          }
        });
    }

    function additionalCheckIfAdminOnPage() {
      return checkAdminOnPage().then(function (found) {
        return {found: found, lastPage: true};
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
});
