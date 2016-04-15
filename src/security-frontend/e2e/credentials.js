var Credentials = function () {
  'use strict';

  var self = this;
  self.manager = acc('manager', 'manager');
  self.admin = acc('admin', 'admin');
  function acc(name, password) {
    return {name:name,password:password}
  }
};

module.exports = new Credentials();
