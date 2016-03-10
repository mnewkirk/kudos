package com.matthewnewkirk.kudos.forms;

import com.matthewnewkirk.kudos.util.UserUtil;

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
    String formName = "RegisterUserForm";
    String rightLengthUsername =
      new String(new char[UserUtil.MIN_USERNAME]).replace("\0", "a");
    String rightLengthPassword =
      new String(new char[UserUtil.MIN_PASSWORD]).replace("\0", "b");
    String tooShortPassword = rightLengthPassword.substring(1);
    String validEmail = "sue@example.com";
    return new Object[][] {
      new Object[] {
        new RegisterUserForm(RegisterUserForm.DEFAULT_USERNAME, 
          RegisterUserForm.DEFAULT_EMAIL, rightLengthPassword, rightLengthPassword),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        2,
        "Should have failed for default username and default email."
      },
      new Object[] {
        new RegisterUserForm(rightLengthUsername, RegisterUserForm.DEFAULT_EMAIL, 
          rightLengthPassword, rightLengthPassword),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed for default email."
      },
      new Object[] {
        new RegisterUserForm(rightLengthUsername.replaceFirst("\\S", " "),
          validEmail, rightLengthPassword, rightLengthPassword),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed for space in username."
      },
      new Object[] {
        new RegisterUserForm(rightLengthUsername.substring(1), 
          validEmail, rightLengthPassword, rightLengthPassword),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed for too short username."
      },
      new Object[] {
        new RegisterUserForm(new String(new char[UserUtil.MAX_USERNAME + 1]).replace("\0", "a"),
          validEmail, rightLengthPassword, rightLengthPassword),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed for too long username."
      },
      new Object[] {
        new RegisterUserForm(rightLengthUsername, "sue@example", 
          rightLengthPassword, rightLengthPassword),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed for invalid email address format."
      },
      new Object[] {
        new RegisterUserForm(rightLengthUsername, validEmail, 
          rightLengthPassword, tooShortPassword),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed for confirmation password mismatch."
      },
      new Object[] {
        new RegisterUserForm(rightLengthUsername, validEmail, 
          tooShortPassword, tooShortPassword),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed for too-short password."
      },
      new Object[] {
        new RegisterUserForm(rightLengthUsername, validEmail, "", ""),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed for blank password."
      },
      new Object[] {
        new RegisterUserForm(rightLengthUsername, validEmail, 
          rightLengthPassword, rightLengthPassword),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        0,
        "Should have succeeded."
      },
    };
  }

  @Test(dataProvider = "formData")
  public void testValidation(RegisterUserForm form, BindingResult bindingResult,
                             int expectedErrors, String expectedMessage) {
    form.validate(bindingResult);
    Assert.assertEquals(bindingResult.getErrorCount(), expectedErrors,
      ErrorHelper.collateErrors(bindingResult.getAllErrors(), expectedMessage));

  }
}
