var Authentication = function () {
  var self = this;

  self.logout = function () {
      element(by.css('a.navbar-brand')).click();
  };

  self.login = function (username, password) {

    var userInput = byId('username');
    var passwordInput = byId('password');
    var form = element(by.css('form[name="form"]'));
    return browser.wait(
      function () {
        userInput.sendKeys(username)
          .then(passwordInput.sendKeys(password))
          .then(form.submit());
        return true;
      }, 10000);
    function byId(id) {
      return browser.driver.findElement(by.id(id));
    }
  };
};
module.exports = new Authentication();
