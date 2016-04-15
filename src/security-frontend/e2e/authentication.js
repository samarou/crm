var Authentication = function () {
  var self = this;

  self.logout = function () {
    element(by.css('a.navbar-brand')).click();
    browser.driver.sleep(500);
  };

  self.login = function (username, password) {

      var userInput = byId('username');
      var passwordInput = byId('password');
      var form = element(by.css('form[name="form"]'));
      userInput.sendKeys(username)
        .then(passwordInput.sendKeys(password))
        .then(form.submit());
    browser.driver.sleep(500);
    function byId(id) {
      return browser.driver.findElement(by.id(id));
    }
  };
};
module.exports = new Authentication();
