package pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * 测试PageHelper分页
 */
public class TestPageHelper {

    @Test
    public void testPageHelper() {
        //1.初始化spring容器。
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/*.xml");
        //2.获取mapper对象
        TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
        //3使用pageHelper设置条件,且只对第一条查询有效
        PageHelper.startPage(1,3);
        //4、查询tb_item表,example没有加条件相当于select * from tb_item;
        TbItemExample example = new TbItemExample();
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        //5、取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        //6、打印查询结果
        for(TbItem item : tbItems) {
            System.out.println(item.getTitle());
        }
    }
}
