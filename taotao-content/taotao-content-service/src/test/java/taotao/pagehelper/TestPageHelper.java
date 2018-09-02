package taotao.pagehelper;

import java.util.List;

//import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {
	
//	@Test
	public void testPageHelper() {
		//1.获取mapper代理对象
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
		//2.设置分页
		PageHelper.startPage(1, 30);
		//3.执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		//4.取分页后结果
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
//		System.out.println("Total:" + pageInfo.getTotal());
		System.out.println(pageInfo.getList());
//		System.out.println("Pages:" + pageInfo.getPages());
//		System.out.println("PageSize:" + pageInfo.getPageSize());
	}
}
