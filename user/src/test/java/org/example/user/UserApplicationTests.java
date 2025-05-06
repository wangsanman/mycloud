package org.example.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.example.common.JwtUtil;
import org.example.user.config.MqConfig;
import org.example.user.entity.po.User;
import org.example.user.mapper.UserMapper;
import org.example.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@SpringBootTest
@Slf4j
class UserApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MqConfig mqConfig;

    @Autowired
    private JwtUtil jwtUtil;
//    @Autowired
//    private RestTemplate restTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private RestHighLevelClient client;


    @BeforeEach
    void setUp() {
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://123.56.223.66:9200")));
    }


    /**
     * 索引库操作
     * @throws IOException
     */
    @Test
    public void testConnection() throws IOException {
        //创建request对象
        CreateIndexRequest request = new CreateIndexRequest("items");
        //设置json格式请求体
        request.source(MAPPING_TEMPLATE, XContentType.JSON);

        //发起请求
        client.indices().create(request, RequestOptions.DEFAULT);

    }

    @Test
    public void testGetConnection() throws IOException {
        //创建request对象
        GetIndexRequest request = new GetIndexRequest("items");
//判断是否存在
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        assert exists;

        //发起请求
        GetIndexResponse getIndexResponse = client.indices().get(request, RequestOptions.DEFAULT);

    }
    @Test
    public void testDeleteConnection() throws IOException {
        //创建request对象
        DeleteIndexRequest request = new DeleteIndexRequest("items");

        //发起请求
        client.indices().delete(request, RequestOptions.DEFAULT);

    }

    /**
     * 文档操作
     * @throws Exception
     */
    @Test
    void  testIndexDocument() throws IOException {
        //创建request对象
        IndexRequest indexRequest = new IndexRequest("items").id("1");

        indexRequest.source("{\"name\":\"Jack\",\"age\":23}", XContentType.JSON);

        User user = userService.getById(1);

        client.index(indexRequest,RequestOptions.DEFAULT);
    }


    @AfterEach
    void tearDown() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    public final static String MAPPING_TEMPLATE ="{\n" +
            "\t\"mappings\": {\n" +
            "\t\t\"properties\": {\n" +
            "\t\t\t\"id\": {\n" +
            "\t\t\t\t\"type\": \"keyword\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"name\": {\n" +
            "\t\t\t\t\"type\": \"text\",\n" +
            "\t\t\t\t\"analyzer\": \"ik_smart\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"price\": {\n" +
            "\t\t\t\t\"type\": \"integer\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"image\": {\n" +
            "\t\t\t\t\"type\": \"keyword\",\n" +
            "\t\t\t\t\"index\": false\n" +
            "\t\t\t},\n" +
            "\t\t\t\"category\": {\n" +
            "\t\t\t\t\"type\": \"keyword\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"brand\": {\n" +
            "\t\t\t\t\"type\": \"keyword\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sold\":{\n" +
            "\t\t\t  \"type\": \"integer\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"commentCount\":{\n" +
            "\t\t\t  \"type\": \"integer\",\n" +
            "\t\t\t  \"index\": false\n" +
            "\t\t\t},\n" +
            "\t\t\t\"isAD\":{\n" +
            "\t\t\t  \"type\": \"boolean\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"updateTime\":{\n" +
            "\t\t\t  \"type\": \"date\"\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t}\n" +
            "}";

//    @Test
//    public void rabbitPublisherDelayMessageTest() {
//        System.out.println();
//        Message build = MessageBuilder.withBody("hello".getBytes()).build();
//
//
//        rabbitTemplate.convertAndSend("mycloud.direct","blue","你好",message -> {
//            message.getMessageProperties().setDelay(60000);
//            return message;
//        });
//
//        ThreadUtil.sleep(20000);
//    }
//
//    @Test
//    public void rabbitReturnCallbackTest() {
//        System.out.println();
//
//        rabbitTemplate.convertAndSend("mycloud.direct","blue","你好");
//
//        ThreadUtil.sleep(20000);
//    }

    @Test
    public void rabbitConfirmCallbackTest() {
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                //Future发生异常是的处理逻辑, 基本不会触发
                log.error("handle message ack fail",ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                //Future 接收到回执的处理逻辑, 参数中的result即使回执内容
                if (result.isAck()) {
                    log.debug("消息发送成功, 收到ack!");
                }else {
                    log.error("消息发送失败, 收到 nack, reason: {}", result.getReason());
                }
            }
        });



//        rabbitTemplate.convertAndSend("mycloud.dir11","blac","你好嗷嗷",cd);

        ThreadUtil.sleep(20000);
    }

    /**
//     * 测试 mybatis 非空不修改
//     */
//    @Test
//    void contextLoads() {
//        User user = new User();
//        user.setId(1L);
//        user.setAge(1);
//        user.setName("");
//        userService.updateById(user);
//        CollUtil.join(new ArrayList<>(),",");
//        Map map = new HashMap();
//        map.put("ids","1,2,3,4,5,6,7,8,9,10");
//
//        ResponseEntity<List<User>> exchange = restTemplate.exchange(
//                "http://localhost:8081/userService/users/ids/{ids}",
//
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<User>>() {
//                },
//                map
//        );
//
//        if (exchange.getStatusCode().is2xxSuccessful()) {
//            System.out.println(exchange.getStatusCodeValue());
//        }
//
//        exchange.getBody().forEach(System.out::println);
//
//    }
//
//    @Test
//    void tokenTest() {
//        String s = jwtUtil.generateToken("abcd312345");
//        log.info("token: {}", s);
//        jwtUtil.verifyToken(s);
//
//        String userId = jwtUtil.parseToken(s);
//        log.info("userId: {}", userId);
//
//
//    }
//
//
//    @Test
//    void testQueryWrapper() {
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.select("id", "name", "password");
//        wrapper.ge("age", 18);
//        List<User> users = userMapper.selectList(wrapper);
//
//        System.out.println(users);
//        try {
//            Integer a = new Integer(1);
//            Integer b = new Integer(0);
//            Integer c = a / b;
//        } catch (Exception e) {
//            log.error("异常了", e);
//        }
//
//    }
//
//    @Test
//    void testUpdateWrapper() {
//        User user = new User();
//        user.setAge(20);
//        //将id1,2,3的金额都减200
//        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.setSql("balance = balance - 200");
//        updateWrapper.in("id", Arrays.asList(1L, 2L, 3L));
//        userMapper.update(user, updateWrapper);
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//
//    }
//
//    @Test
//    void testLambdaQueryWrapper() {
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.select(User::getId, User::getName, User::getPassword);
//        userMapper.selectList(queryWrapper);
//    }
//
//    @Test
//    void testCustomSqlUpdate() {
//        //将id1,2,3的金额都减200
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        LambdaQueryWrapper<User> lambda = queryWrapper.lambda();
//        queryWrapper.in("id", Arrays.asList(1L, 2L, 3L));
//        userMapper.updateByBalanceByIds(200, queryWrapper);
//    }
//
//    //批处理
//    @Test
//    void testBatchInsert() {
//        List<User> users = new ArrayList<>(1000);
//        long a = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            User user = new User();
//            user.setAge(20);
//            user.setName("user" + i);
//            user.setBalance(1000);
//            user.setPassword(123456);
//            users.add(user);
//
//            if (i % 1000 == 0) {
//                userService.saveBatch(users);
//                users.clear();
//            }
//        }
//        System.out.println("耗时" + (System.currentTimeMillis() - a));
//    }

    //    @SneakyThrows
//    public static void main(String[] args) {
//        Map<DayOfWeek, String> map = new EnumMap<>(DayOfWeek.class);
//        map.put(DayOfWeek.FRIDAY,"周五");
//        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
////        objectObjectHashMap.put()
//        // 构造从start到end的序列：
//        final int start = 10;
//        final int end = 20;
//        List<Integer> list = new ArrayList<>();
//        for (int i = start; i <= end; i++) {
//            list.add(i);
//        }
//        // 随机删除List中的一个元素:
//        int removed = list.remove((int) (Math.random() * list.size()));
//        int found = findMissingNumber(start, end, list);
//        System.out.println(list.toString());
//        System.out.println("missing number: " + found);
//
//
//        ExecutorService executor = Executors.newFixedThreadPool(4);
//// 定义任务:
////        UserController userController = new UserController();
//        Callable<String> task = new UserController().new Task();
//// 提交任务并获得Future:
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(UserController::longTimeCalculation);
//        future.thenAccept(System.out::println);
//
//        System.out.println(removed == found ? "测试成功" : "测试失败");
//        Thread.sleep(20000);
//    }
//
//    class Task implements Callable<String> {
//        public String call() throws Exception {
//            return "longTimeCalculation()";
//        }
//    }
//
//    public static String longTimeCalculation(){
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return "longTimeCalculation()";
//    }
//
//    static int findMissingNumber(int start, int end, List<Integer> list) {
//        Map<Integer, Integer> map = new HashMap<>();
//
//        for (int i = 0; i < list.size(); i++) {
//            if(i+start != list.get(i)){
//                return i+start;
//            }
//        }
//        return end;
//    }
//
//    public static <K> K[] createArray(Class<K> clazz) {
//        return (K[]) Array.newInstance(Pair.class, 5);
//    }
//
//
//    public static void custom.env(Pair<?> pair) {
//        System.out.println(pair.getFirst());
////        System.out.println(first);
//    }
//
//    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
//        for (int i = 0; i < src.size(); i++) {
//            T t = src.get(i); // src是producer
//            dest.add(t); // dest是consumer
//        }
//    }

    /**
     * @ApiImplicitParam 的使用
     */
    //    @GetMapping("get")
//    @ApiOperation(value = "根据id查用户", notes = "根据用户ID获取用户详细信息")
//    //@ApiIgnore//不生成swagger帮助文档
//    @IgnoreToken
//    @ApiImplicitParam(name = "b", value = "b的描述",required = false, paramType = "Integer")//即便实际没有该参数, 也会在文档中生成, 适合描述Map这种参数类型
//    public User getUserById(@ApiParam("用户id")  @PathVariable Long id) {
//        return userService.getById(id);
//    }
}
