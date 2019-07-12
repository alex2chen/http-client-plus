# http-client-plus
## 背景
习惯于玩rpc的同学都知道，透明化调用对客户端来说很高效及方便，同时组件升级和客户端升级（更新下jar版本）本来都是很轻松的事情。而http-client-plus是对XX公司mule-esb-api的简单封装，用于发送http请求到esb，减少代码冗余简化操作，应用代码只需要写个interface的声明即可。

## 支持特性
    1：通过@BeforeRequest注解，支持请求前置处理
    2：通过@AfterResponse注解，支持响应后置处理
    3：默认实现了响应状态的判断，和返回结果的转换
    4：支持void，基本数据类型及其包装类，Object，自定义对象，
        List，List<T>，Map，Future，Future<T>，Future<List>，Future<List<T>>类型的返回
    5：支持各式的请求参数
    6：提供@HttpParam这种简单的生成Map类型的请求参数的方法
    7：支持两种方式的异步请求
        7.1：通过将返回类型设置为Future或ListenableFuture
        7.2：返回类型设置为void，并添加@CallBack回调方法（回调方法的[P param]默认为第一个参数，当然也可以指定其他的）
    8：支持ESB（post）和HTTP（get/post）两种请求方式
    9：支持自定义请求执行Bean
    10：支持防重复提交
    11：支持Optional（再也不需要像以前一样写一大堆代码去判断空指针）

## 接入端的隔离性
接入端支持：<br/>
-    httpadapter-core支持接入httpclient
-    httpadapter-esb支持接入esb模块
-	 dubbo后续支持[理论上也是可以的，使用泛化调用的方式]
-	 springcloud后续支持[连接eureka、robbin、hystrix，如果你足够了解Feign需要改造下]

二者可以独立使用，也可以同时接入 <p/>


## 基本用法
第一步配置xml
```xml
    <import resource="spring-http.xml"></import>
    <import resource="spring-esb.xml"></import>
    <!-- 设置扫描对象 -->
    <bean id="httpAdapterPostProcessor" class="com.github.middleware.httpadapter.spring.HttpAdapterPostProcessor">
        <property name="annotationClz"
                  value="com.github.middleware.httpadapter.core.annotation.KxRequestAgent"></property>
        <property name="basePackages">
            <list>
                <value>com.github.middleware.httpadapter</value>
            </list>
        </property>
        <property name="driverDelegates">
            <list>
                <value>com.gillion.esb.api.client.ESBClient</value>
                <value>com.github.middleware.httpadapter.httpclient.HttpClient</value>
            </list>
        </property>
        <property name="startConfig" ref="commonConfig"/>
    </bean>
    <bean id="commonConfig" class="com.github.middleware.httpadapter.esb.EsbStartConfig">
        <property name="httpUrlPrefix" value="dev-"></property>
    </bean>
```
第二部配置contract
```java
@KxRequestAgent
@BeforeRequest(beforeMethods = TestBeforeRequest.class)
@AfterResponse(afterMethods = {DefaultStatusCheckResponseHandle.class, DefaultGenericTypeResponseHandler.class})
public interface TestRequestExecutor {

    @Request(url = "MSHIP_S_00157")
    @AfterResponse(afterMethods = TestCompanyInfoAfterResponse.class)
    CompanyInfo requestParamTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "MSHIP_S_00157")
    @AfterResponse(overrideMethods = {DefaultStatusCheckResponseHandle.class, DefaultGenericTypeResponseHandler.class, TestAfterResponse.class})
    CompanyInfo requestBodyTest(Map<String, String> param);

    @Request(url = "MSHIP_S_00157")
    CompanyInfo mapTest(Map<String, String> param);

    @Request(url = "MSHIP_S_00157")
    String stringTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "MSHIP_S_00157")
    void voidTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "OMS_S_00087")
    List<ServiceRegionInfo> listTest(@KxRequestParam("serverResgionIds") List<String> regionIds);

    @Request(url = "MSHIP_S_00157")
    Future<CompanyInfo> asyncFutureTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "OMS_S_00087")
    ListenableFuture<List<ServiceRegionInfo>> asyncFutureListTest(@KxRequestParam("serverResgionIds") List<String> regionIds);

    @Request(url = "MSHIP_S_00157")
    @UnRepeatable(0)
    CompanyInfo lockTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "MSHIP_S_00157")
    @Callback(TestCallBack.class)
    void callBackTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "xxx.com:8848/omsConfig/setConfig", requestExecutor = "defaultHttpAsyncPoolClient")
    Object httpGetTest(@KxRequestParam(value = "key") String key, @KxRequestParam(value = "value") Integer value);

    @Request(url = "xxx.com:8848/basTransRoute/enable", requestExecutor = "defaultHttpAsyncPoolClient", httpMethod = HttpMethod.POST)
    String httpPostTest(@KxRequestParam(value = "transRouteIds") List<String> transRouteIds);

    @Request(url = "MSHIP_S_00157")
    Optional<CompanyInfo> requestParamTest2(@KxRequestParam("orgId") String orgId);
    @Request(url = "OMS_S_00087")
    Optional<List<ServiceRegionInfo>> listTest2(@KxRequestParam("serverResgionIds") List<String> regionIds);
    @Request(url = "MSHIP_S_00157")
    Future<Optional<CompanyInfo>> asyncFutureTest2(@KxRequestParam("orgId") String orgId);
    @Request(url = "OMS_S_00087")
    ListenableFuture<Optional<List<ServiceRegionInfo>>> asyncFutureListTest2(@KxRequestParam("serverResgionIds") List<String> regionIds);
}
```
第三步使用
```java
/**
** spring环境下
**/
@Test
public void afterResponseChainRun() {
    Test2RequestExecutor test2RequestExecutor = this.getTestRequestExecutor(Test2RequestExecutor.class);
    test2RequestExecutor.getCommpany(orgId);
    test2RequestExecutor.getCommpany2(orgId);
}
```
[更多](httpadapter-esb/src/test/java/com/github/middleware/httpadapter/esb/QuickStart.java)
