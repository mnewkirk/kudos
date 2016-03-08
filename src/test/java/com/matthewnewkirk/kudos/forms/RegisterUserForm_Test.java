package com.matthewnewkirk.kudos.forms;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Matt Newkirk 3/3/2016
 */
public class RegisterUserForm_Test {

  @DataProvider
  private Object[][] formData() {
    return new Object[][] {
      new Object[] {
        new RegisterUserForm(RegisterUserForm.defaultUsername, RegisterUserForm.defaultEmail, "foobar01", "foobar01"),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        2
      },
      new Object[] {
        new RegisterUserForm("test_user", RegisterUserForm.defaultEmail, "foobar01", "foobar01"),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        1
      },
      new Object[] {
        new RegisterUserForm("test user", "sue@example.com", "foobar01", "foobar01"),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        1
      },
      new Object[] {
        new RegisterUserForm("te", "sue@example.com", "foobar01", "foobar01"),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        1
      },
      new Object[] {
        new RegisterUserForm("test_username", "sue@example.com", "foobar01", "foobar01"),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        1
      },
      new Object[] {
        new RegisterUserForm("test_user", "sue@example", "foobar01", "foobar01"),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        1
      },
      new Object[] {
        new RegisterUserForm("test_user", "sue@example.com", "foobar01", "bar"),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        1
      },
      new Object[] {
        new RegisterUserForm("test_user", "sue@example.com", "foobar0", "foobar0"),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        1
      },
      new Object[] {
        new RegisterUserForm("test_user", "sue@example.com", "", ""),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        1
      },
      new Object[] {
        new RegisterUserForm("test_user", "sue@example.com", "foobar01", "foobar01"),
        new DirectFieldBindingResult(new AddKudoForm(), "RegisterUserForm"),
        0
      },
    };
  }

  @Test(dataProvider = "formData")
  public void testValidation(RegisterUserForm form, BindingResult bindingResult, int expectedErrors) {
    form.validate(bindingResult);
    Assert.assertEquals(bindingResult.getErrorCount(), expectedErrors,
      ErrorHelper.collateErrors(bindingResult.getAllErrors()));

  }
}
