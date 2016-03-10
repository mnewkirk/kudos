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
    return new Object[][] {
      new Object[] {
        new AddKudoForm(AddKudoForm.defaultText, "userFrom",
          "userTo"),
        new DirectFieldBindingResult(new AddKudoForm(), "AddKudoForm"),
        1
      },
      new Object[] {
        new AddKudoForm("kudo for testing", "sue@example.com", "bob@example.com"),
        new DirectFieldBindingResult(new AddKudoForm(), "AddKudoForm"),
        0
      },
    };
  }

  @Test(dataProvider = "formData")
  public void testValidation(AddKudoForm form, BindingResult bindingResult, int expectedErrors) {
    form.validate(bindingResult);
    Assert.assertEquals(bindingResult.getErrorCount(), expectedErrors,
      ErrorHelper.collateErrors(bindingResult.getAllErrors()));

  }
}
