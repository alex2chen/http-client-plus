package com.github.middleware.httpadapter.esb;

import com.github.middleware.httpadapter.esb.contract.Test2RequestExecutor;
import com.github.middleware.httpadapter.esb.contract.TestRequestExecutor;
import com.github.middleware.httpadapter.esb.dto.CompanyInfo;
import com.github.middleware.httpadapter.esb.dto.ServiceRegionInfo;
import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Maps;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/2/8.
 */
public class QuickStart {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuickStart.class);
    private static final String CONFIG_LOCATION = "classpath:spring-application.xml";
    private static String orgId = "0015191a-2d84-e511-9d72-40a8f0257e10";
    private static List<String> regionIds = Lists.newArrayList("0ed7dada-463d-440a-a3bb-cb7a3be016a8", "142266c3-129e-45c9-8b3c-b98f0e0e7f72");
    private static TestRequestExecutor testRequestExecutor;
    private static Test2RequestExecutor test2RequestExecutor;

    @BeforeClass
    public static void init() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        testRequestExecutor = applicationContext.getBean(TestRequestExecutor.class);
        test2RequestExecutor = applicationContext.getBean(Test2RequestExecutor.class);
    }

    @Test
    public void asyncRun() throws ExecutionException, InterruptedException {
        Future<CompanyInfo> f1 = testRequestExecutor.asyncFutureTest(orgId);
        CompanyInfo r1 = f1.get();
        LOGGER.info("{}", r1);
        Assert.isTrue(r1 != null, "asyncFutureTest Error.");

        Future<List<ServiceRegionInfo>> f3 = testRequestExecutor.asyncFutureListTest(regionIds);
        List<ServiceRegionInfo> r2 = f3.get();
        Assert.isTrue(r2.size() == 2, "asyncFutureListTest Error.");
    }

    @Test
    public void callBackRun() throws InterruptedException {
        testRequestExecutor.callBackTest(orgId);
        Thread.sleep(5000);
    }

    @Test
    public void simpleRun() {
        String r4 = testRequestExecutor.stringTest(orgId);
        LOGGER.info(r4);
        Assert.notNull(r4, "stringTest Error.");

        CompanyInfo r1 = testRequestExecutor.requestParamTest(orgId);
        LOGGER.info("{}", r1);
        Assert.isTrue(r1 != null, "kxRequestParamTest Error.");
        Map<String, String> queryParam = Maps.newHashMap();
        queryParam.put("orgId", orgId);
        CompanyInfo r2 = testRequestExecutor.requestBodyTest(queryParam);
        Assert.isTrue(r2 != null, "requestBodyTest Error.");

        CompanyInfo r3 = testRequestExecutor.mapTest(queryParam);
        Assert.isTrue(r3 != null, "mapTest Error.");

        testRequestExecutor.voidTest(orgId);

        List<ServiceRegionInfo> r6 = testRequestExecutor.listTest(regionIds);
        Assert.isTrue(r6.size() == 2, "listTest Error.");
    }

    @Test
    public void afterResponseChainRun() {
        String result = test2RequestExecutor.httpPostTest(regionIds);
        LOGGER.info(result);
        Assert.hasLength(result, "httpPostTest is error!");
        CompanyInfo commpany = test2RequestExecutor.getCommpany(orgId);
        Assert.notNull(commpany, "getCommpany is error!");
        CompanyInfo commpany2 = test2RequestExecutor.getCommpany2(orgId);
        Assert.notNull(commpany2, "getCommpany2 is error!");
    }

    @Test
    public void repeatableSubmit() throws ExecutionException, InterruptedException {
        ThreadPoolTaskExecutor poolTaskExecutor = new EsbStartConfig().getPoolTaskExecutor();
        Future<CompanyInfo> a = poolTaskExecutor.submit(new Callable<CompanyInfo>() {
            @Override
            public CompanyInfo call() throws Exception {
                return testRequestExecutor.lockTest(orgId);
            }
        });
        Future<CompanyInfo> b = poolTaskExecutor.submit(new Callable<CompanyInfo>() {
            @Override
            public CompanyInfo call() throws Exception {
                return testRequestExecutor.lockTest(orgId);
            }
        });
        a.get();
        b.get();
    }

    @Test
    public void optionRun() throws ExecutionException, InterruptedException {
        Optional<CompanyInfo> r1 = testRequestExecutor.requestParamTest2(orgId);
        r1.ifPresent(x -> LOGGER.info("{}", x));
        Assert.isTrue(r1.isPresent(), "requestParamTest2 Error.");
        Optional<List<ServiceRegionInfo>> r6 = testRequestExecutor.listTest2(regionIds);
        Assert.isTrue(r6.isPresent(), "listTest2 Error.");
        Future<Optional<CompanyInfo>> f1 = testRequestExecutor.asyncFutureTest2(orgId);
        Assert.isTrue(f1.get().get() != null, "asyncFutureTest2 Error.");

        Future<Optional<List<ServiceRegionInfo>>> f3 = testRequestExecutor.asyncFutureListTest2(regionIds);
        Assert.isTrue(f3.get().get().size() == 2, "asyncFutureListTest2 Error.");
    }

}
