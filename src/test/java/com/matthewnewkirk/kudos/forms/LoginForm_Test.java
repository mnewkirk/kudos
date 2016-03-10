package com.matthewnewkirk.kudos.forms;

import com.matthewnewkirk.kudos.util.UserUtil;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Matt Newkirk 3/3/2016
 */
public class LoginForm_Test {

  @DataProvider
  private Object[][] formData() {
    String formName = "LoginForm";
    String rightLengthUsername =
      new String(new char[UserUtil.MIN_USERNAME]).replace("\0", "a");
    String rightLengthPassword =
      new String(new char[UserUtil.MIN_PASSWORD]).replace("\0", "b");
    return new Object[][] {
      new Object[] {
        new LoginForm(LoginForm.defaultUsername, LoginForm.defaultPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        2,
        "Should have failed with default username AND default password."
      },
      new Object[] {
        new LoginForm(rightLengthUsername, LoginForm.defaultPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with default password."
      },
      new Object[] {
        new LoginForm(LoginForm.defaultUsername, rightLengthPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with default username."
      },
      new Object[] {
        new LoginForm(rightLengthUsername.substring(1), rightLengthPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with too short username."
      },
      new Object[] {
        new LoginForm(new String(new char[UserUtil.MAX_USERNAME + 1]).replace("\0", "a"),
          rightLengthPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with too long username."
      },
      new Object[] {
        new LoginForm(rightLengthUsername + " d", rightLengthPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with a space."
      },
      new Object[] {
        new LoginForm(rightLengthUsername + "\td", rightLengthPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with a tab."
      },
      new Object[] {
        new LoginForm(rightLengthUsername + "\nd", rightLengthPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with a line feed."
      },
      new Object[] {
        new LoginForm(rightLengthUsername + "\rd", rightLengthPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with a carriage return."
      },
      new Object[] {
        new LoginForm(rightLengthUsername, rightLengthPassword.substring(1)),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with a too short password."
      },
      new Object[] {
        new LoginForm(rightLengthUsername,
          new String(new char[UserUtil.MAX_PASSWORD + 1]).replace("\0", "b")),
        new DirectFieldBindingResult(new LoginForm(), formName),
        1,
        "Should have failed with a too long password."
      },
      new Object[] {
        new LoginForm(rightLengthUsername, rightLengthPassword),
        new DirectFieldBindingResult(new LoginForm(), formName),
        0,
        "Should have succeeded."
      },
    };
  }

  @Test(dataProvider = "formData")
  public void testValidation(LoginForm form, BindingResult bindingResult,
                             int expectedErrors, String expectedMessage) {
    form.validate(bindingResult);
    Assert.assertEquals(bindingResult.getErrorCount(), expectedErrors,
      ErrorHelper.collateErrors(bindingResult.getAllErrors(), expectedMessage));

  }
}
