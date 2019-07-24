/*
 * Copyright 2015 www.seleniumtests.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seleniumtests.driver;

import com.seleniumtests.core.SeleniumTestsContextManager;
import com.seleniumtests.customexception.WebSessionEndedException;
import org.openqa.selenium.By;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class DriverExceptionListener implements WebDriverEventListener {
    public void afterClickOn(final WebElement arg0, final WebDriver arg1) { }

    @Override
    public void beforeChangeValueOf(final WebElement webElement, final WebDriver webDriver, final CharSequence[] charSequences) {
        
    }

    @Override
    public void afterChangeValueOf(final WebElement webElement, final WebDriver webDriver, final CharSequence[] charSequences) {

    }

    public void afterFindBy(final By arg0, final WebElement arg1, final WebDriver arg2) { }

    public void afterNavigateBack(final WebDriver arg0) { }

    public void afterNavigateForward(final WebDriver arg0) { }

    @Override
    public void beforeNavigateRefresh(final WebDriver webDriver) {}

    @Override
    public void afterNavigateRefresh(final WebDriver webDriver) {}

    public void afterNavigateTo(final String arg0, final WebDriver arg1) { }

    public void afterScript(final String arg0, final WebDriver arg1) { }

    public void beforeClickOn(final WebElement arg0, final WebDriver arg1) { }

    public void beforeFindBy(final By arg0, final WebElement arg1, final WebDriver arg2) { }

    public void beforeNavigateBack(final WebDriver arg0) { }

    public void beforeNavigateForward(final WebDriver arg0) { }

    @Override
    public void beforeAlertAccept(WebDriver driver) {

    }

    @Override
    public void afterAlertAccept(WebDriver driver) {

    }

    @Override
    public void afterAlertDismiss(WebDriver driver) {

    }

    @Override
    public void beforeAlertDismiss(WebDriver driver) {

    }

    public void beforeNavigateTo(final String arg0, final WebDriver arg1) { }

    public void beforeScript(final String arg0, final WebDriver arg1) { }

    public void onException(final Throwable ex, final WebDriver arg1) {

        if (ex.getMessage() == null) {
            return;
        } else if (ex.getMessage().contains("Element must be user-editable in order to clear it")) {
            return;
        } else if (ex.getMessage().contains("Element is not clickable at point")) {
            return;
        } else if (ex instanceof UnsupportedCommandException) {
            return;
        } else if (ex.getMessage().contains(" read-only")) {
            return;
        } else if (ex.getMessage().contains("No response on ECMAScript evaluation command")) { // Opera

            // customexception
            for (int i = 0; i < ex.getStackTrace().length; i++) {
                final String method = ex.getStackTrace()[i].getMethodName();
                if (method.contains("getTitle") || method.contains("getWindowHandle") || method.contains("click")
                        || method.contains("getPageSource")) {
                    return;
                }
            }

            ex.printStackTrace();
            // return;
        } else if (ex.getMessage().contains("Error communicating with the remote browser. It may have died.")) {

            // Session has lost connection, remove it then ignore quit() method.
            if (WebUIDriver.getWebUIDriver().getConfig().getMode() == DriverMode.ExistingGrid) {
                WebUIDriver.setWebDriver(null);
                throw new WebSessionEndedException(ex);
            }

            return;
        } else if (ex instanceof org.openqa.selenium.remote.UnreachableBrowserException) {
            return;
        } else if (ex instanceof org.openqa.selenium.UnsupportedCommandException) {
            return;
        } else {
            final String message = ex.getMessage().split("\\n")[0];
            System.out.println("Got customexception:" + message);
            if (message.matches("Session (/S*) was terminated due to(.|\\n)*")
                    || message.matches("cannot forward the request Connection to(.|\\n)*")) {
                WebUIDriver.setWebDriver(null); // can't quit anymore, save time.

                // since the session was
                // terminated.
                throw new WebSessionEndedException(ex);
            }
        }

        for (int i = 0; i < ex.getStackTrace().length; i++) // avoid dead loop
        {
            final String method = ex.getStackTrace()[i].getMethodName();
            if (method.contains("getScreenshotAs") || method.contains("captureWebPageSnapshot")) {
                return;
            }
        }

        if (arg1 != null) {

            try {
                System.out.println("Got customexception" + ex.getMessage());
                if (SeleniumTestsContextManager.getThreadContext().getTestType().equalsIgnoreCase(
                            TestType.WEB.toString())) {
                    new ScreenshotUtil(arg1).capturePageSnapshotOnException();
                } else {
                    System.out.println("Capture screenshot is not available for appium");
                }
            } catch (final Exception e) {

                // Ignore all exceptions
                e.printStackTrace();
            }
        }
    }
}
