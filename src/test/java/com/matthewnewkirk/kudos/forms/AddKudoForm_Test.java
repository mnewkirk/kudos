package com.matthewnewkirk.kudos.forms;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Matt Newkirk 3/3/2016
 */
public class AddKudoForm_Test {

  @DataProvider
  private Object[][] formData() {
    String formName = "AddKudoForm";
    return new Object[][] {
      new Object[] {
        new AddKudoForm(AddKudoForm.defaultText, "userFrom",
          "userTo"),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed with default kudo text."
      },
      new Object[] {
        new AddKudoForm("", "userFrom", "userTo"),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        1,
        "Should have failed with empty kudo text."
      },
      new Object[] {
        new AddKudoForm("kudo for testing", "sue@example.com", "bob@example.com"),
        new DirectFieldBindingResult(new AddKudoForm(), formName),
        0,
        "Should not have failed."
      },
    };
  }

  @Test(dataProvider = "formData")
  public void testValidation(AddKudoForm form, BindingResult bindingResult,
                             int expectedErrors, String expectedMessage) {
    form.validate(bindingResult);
    Assert.assertEquals(bindingResult.getErrorCount(), expectedErrors,
      ErrorHelper.collateErrors(bindingResult.getAllErrors(), expectedMessage));

  }
}
