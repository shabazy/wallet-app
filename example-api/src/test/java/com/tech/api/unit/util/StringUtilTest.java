package com.tech.api.unit.util;

import com.tech.api.util.StringUtil;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class StringUtilTest {

    @Test
    public void testGenerateTransactionReference() {
        String transactionReference = StringUtil.generateTransactionReference(10);
        Assertions.assertEquals(10, transactionReference.length());
    }

}
