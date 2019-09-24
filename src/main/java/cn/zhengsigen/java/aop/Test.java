package cn.zhengsigen.java.aop;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zhengsigen.java.aop.service.UserServiceImpl;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.zhengsigen.java.aop.dao.AspectJDemo;
import cn.zhengsigen.java.aop.dao.UserService;
import cn.zhengsigen.java.aop.pojo.User;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration//启动类
@ComponentScan//扫描整个项目找到
						//是否显示代理人，       //代理是由jdk还是由动态代理
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = false)
@EnableTransactionManagement
public class Test {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext cont = new AnnotationConfigApplicationContext(Test.class);

		UserService bean = cont.getBean(UserService.class);
		User user = new User();
		user.setName("admin"+System.currentTimeMillis());
		user.setId(30);



	/*	//新增
		bean.addUser(connection,user);
		//删除
		bean.deleteUser(connection, user);
		//列表
		Map<Integer, User> list = bean.getList(connection);
		System.out.println(list);*/
		bean.updateUser(user);

		/*
		 * SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
		 * InputStream is =
		 * Thread.currentThread().getContextClassLoader().getResourceAsStream(
		 * "mybatis-config.xml"); SqlSessionFactory sqlSessionFactory =
		 * factoryBuilder.build(is); SqlSession session =
		 * sqlSessionFactory.openSession(true); UserService mapper =
		 * session.getMapper(UserService.class); User user = new User();
		 * user.setName("123123min"); mapper.addUser(user);
		 */
		//cont.getBean(UserServiceImpl.class).save();
		/*
		 * String[] beanDefinitionNames = cont.getBeanDefinitionNames(); for(String
		 * s:beanDefinitionNames) { System.out.println(s); }
		 */
	}
    @Bean// jdbcTemplate -> Mybatis
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
		return new JdbcTemplate(dataSource);
    }
	@Bean // 将方法的返回值丢进容器 - 对象名称:默认方法
	public DataSource dataSource(){

		final DruidDataSource ds = new DruidDataSource();
		ds.setUrl("jdbc:mysql://127.0.0.1:3306/0412spring?useSSL=false");
		ds.setUsername("root");
		ds.setPassword("123");

		// 连接池配置
		ds.setInitialSize(10);
		ds.setMaxActive(100);
		return ds;
	}

	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource){
		// 单机事务
		// 多台事务 - 分布式
		// JTA

		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}
}
