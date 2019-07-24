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

package com.seleniumtests.tests;

import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.core.SeleniumTestPlan;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Using Matchers.
 */
public class RetryTest extends SeleniumTestPlan {

    @Test
    public void retryFailedTest() {
        CustomAssertion.assertThat("Soft failure", false);
        CustomAssertion.assertThat("Soft failure 2, executed despite first failure", false);
        CustomAssertion.assertThat("This never fails", true);
        CustomAssertion.assertThat("This fails again", false);
        // with out assertion check, soft failures would not be caught
        assert CustomAssertion.getVerificationFailures().isEmpty():"Verification Errors";
    }

    private int count = 0;

    @Test
    public void test() {
        count++;
        if (count % 3 != 0) {
            Assert.fail("Injected Failure");
        }
        count = 0;
    }
}
