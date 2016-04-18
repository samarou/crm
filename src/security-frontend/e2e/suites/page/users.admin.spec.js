/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Users under role ADMIN', function () {
  var loginService = require('../../login.service.js');
  var usersPage = require('../../pages/users.po.js');
  var credentials = require('../../credentials');

  var ROLE_BLOCKING_USER = 'MANAGER';
  var ROLE_FILTERING_USER = 'ADMIN';
  var BLOCKING_USER = credentials.manager;
  var FILTERING_USER = credentials.admin;
  beforeEach(function () {
    loginService.login(credentials.admin)
  });

  it('should be able to filter user records by roles', function () {

    usersPage.getOption(ROLE_FILTERING_USER).click();
    iterateThroughPages(userIsOnPage)
      .then(function (adminFound) {
        expect(adminFound).toBe(true);
      });

    function iterateThroughPages(checkingFunction) {
      return checkingFunction()
        .then(function (isFound) {
          return checkingLoop(isFound, checkingFunction);
        }).then(function (elementInPage) {
          return elementInPage.found;
        });
    }

    function checkingLoop(adminInPage, checkingFunction) {
      return goToNextPageIfNotLast(adminInPage.found)
        .then(function (adminInP) {
          if (adminInP.found) {
            return adminInP;
          } else {
            return checkingFunction(adminInP.lastPage)
              .then(function (elementOnPage) {
                if (adminInP.lastPage) {
                  return elementOnPage;
                } else {
                  return checkingLoop(elementOnPage, checkingFunction);
                }
              });
          }
        });
    }

    function goToNextPageIfNotLast(elementFound) {
      return usersPage.isLastPage()
        .then(function (isLastPage) {
            if (!isLastPage) {
              return usersPage.nextPageButton().click()
                .then(function () {
                  return ElementInPage(elementFound, isLastPage);
                });
            } else {
              return ElementInPage(elementFound, isLastPage);
            }
          }
        );
    }

    function ElementInPage(elementFound, isLastPage) {
      return {found: elementFound, lastPage: isLastPage}
    }

    function userIsOnPage(isLastPage) {
      return checkUserOnPage(FILTERING_USER.username)
        .then(function (found) {
          return ElementInPage(found, isLastPage);
        })
    }
  });

  it('should be able to block the user and check if it`s blocked', function () {
    var userToBlock = BLOCKING_USER.username;
    usersPage.searchTab.sendKeys(userToBlock);
    usersPage.getOption(ROLE_BLOCKING_USER).click();
    expect(checkUserOnPage(userToBlock)).toBe(true);
    activateUser(false, userToBlock);
    expect(checkUserOnPage(userToBlock)).toBe(false);
    usersPage.activeUserFlag.click();
    expect(checkUserOnPage(userToBlock)).toBe(true);
    activateUser(true, userToBlock);
    usersPage.activeUserFlag.click();
    expect(checkUserOnPage(userToBlock)).toBe(true);

    function activateUser(isMakeActive, userName) {
      usersPage.clickOnCheckboxOfUser(userName);
      if (isMakeActive) {
        usersPage.userActivation.click();
      } else {
        usersPage.userDeactivation.click();
      }
    }
  });

  afterEach(function () {
    loginService.logout();
  });

  function checkUserOnPage(userName) {
    return usersPage.getUserNamesInTable().then(function (users) {
      return checkIfUserInArray(users, userName);
    })
  }

  function checkIfUserInArray(users, name) {
    return (users.indexOf(name)) != -1;
  }
});
