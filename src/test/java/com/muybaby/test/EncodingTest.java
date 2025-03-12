package com.muybaby.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j

public class EncodingTest {
    @Test
    public void testLogChinese() {
        log.info("测试中文输出是否正常");
    }
}
