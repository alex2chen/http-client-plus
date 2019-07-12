package com.github.middleware.httpadapter;

import com.github.middleware.httpadapter.contract.HttpClientRequestExecutor;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/2/8.
 */
public class QueryStart {
    private static final Logger LOGGER= LoggerFactory.getLogger(QueryStart.class);
    private static final String CONFIG_LOCATION = "classpath:spring-application.xml";
    private static HttpClientRequestExecutor requestExecutor;

    @BeforeClass
    public static void init() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        requestExecutor = applicationContext.getBean(HttpClientRequestExecutor.class);
    }

    @Test
    public void httpGetTest() {
        Object r13 = requestExecutor.httpGetTest("GuestPriceRate", 19);
        Assert.notNull(r13, "result is null.");
    }

    @Test
    public void httpPostTest() {
        String r14 = requestExecutor.httpPostTest(Lists.newArrayList("00057939-3ff7-42a3-aed9-ee2586c2277e"));
        LOGGER.info(r14);
        Assert.isTrue(!Strings.isNullOrEmpty(r14), "httpPostTest Error.");
    }
}
