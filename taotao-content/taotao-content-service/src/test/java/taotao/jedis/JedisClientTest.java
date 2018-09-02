package taotao.jedis;

import com.taotao.content.jedis.JedisClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class JedisClientTest {

    @Autowired
    private JedisClient jedisClient;

    //jedis单机版
    @Test
    public void jedisClient() {
        Jedis jedis = new Jedis("192.168.1.110",6379);
        jedis.set("我是key","我是value");
        String result = jedis.get("我是key");
        System.out.println(result);
        jedis.close();
    }

    //jedisPool版本
    @Test
    public void jedisPoolClient() {
        JedisPool jedisPool = new JedisPool("192.168.1.110",6379);
        Jedis resource = jedisPool.getResource();
        resource.set("key1","hah");
        String key1 = resource.get("key1");
        System.out.println(key1);
        resource.close();
        jedisPool.close();
    }

    //jedis集群版
    @Test
    public void jedisCluster() {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.1.111", 6379));
        nodes.add(new HostAndPort("192.168.1.112", 6379));
        nodes.add(new HostAndPort("192.168.1.113", 6379));
        nodes.add(new HostAndPort("192.168.1.114", 6379));
        nodes.add(new HostAndPort("192.168.1.115", 6379));
        nodes.add(new HostAndPort("192.168.1.116", 6379));

        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("name", "zhangsan");
        String name = jedisCluster.get("name");
        System.out.println(name);
        jedisCluster.close();
    }

    //jedis集群版，从spring容器中取
    @Test
    public void jedisClusterSpring() throws Exception{
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisClient j = applicationContext.getBean(JedisClient.class);
        j.set("c1","10000");
        String s = j.get("c1");
        System.out.println(s);
    }
}
