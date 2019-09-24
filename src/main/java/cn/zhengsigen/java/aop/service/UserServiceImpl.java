package cn.zhengsigen.java.aop.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.zhengsigen.java.aop.controller.Transaction;
import cn.zhengsigen.java.aop.dao.UserService;
import cn.zhengsigen.java.aop.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class UserServiceImpl implements UserService{

	@Override
	public void addUser(Connection connection,User user) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("INSERT INTO user (name) VALUES (?)");
		ps.setString(1, user.getName());
		ps.executeUpdate();
		System.out.println("用户添加成功");
	}

	@Override
	public void deleteUser(Connection connection, User user) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("DELETE FROM user WHERE id=?");
		ps.setString(2, user.getId()+"");
		ps.executeUpdate();
		System.out.println("用户删除成功");
	}

	@Override
	public Map<Integer, User> getList(Connection connection) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("select id,name,create_time,update_time from user");
		ResultSet rs = ps.executeQuery();
		Map<Integer, User> users = new HashMap<Integer, User>();
		while(rs.next()) {
			User u =new User(rs.getInt("id"),rs.getString("name"),
					new Date(rs.getDate("create_time").getTime()),
					new Date(rs.getDate("update_time").getTime()));
			users.put(u.getId(),u);
		}
		return users;
	}
	@Override
	public void updateUser(Connection connection,User user) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("update user set name =? where id=? ");
		ps.setString(1, user.getName());
		ps.setInt(2, user.getId());
		ps.executeUpdate();

	}





	@Autowired
	private JdbcTemplate template; // Spring-JDBC:对原生的JDBC进行封装 - 模板代码

	//@Autowired
	//private DataSource dataSource;



	//@Transactional(noRollbackFor = BizException.class)
	// @Transactional(rollbackFor = Exception.class)
	// @Transactional(readOnly = true)
	//@Transactional
	@Transactional // 启用事默认: 运行时异常回滚(继承RuntimeException的异常 - NullPointException)
	public void updateUser(User user) throws Exception {

		//新增template.update("insert into user(name) values (?)",user.getName());
		//修改template.update("update user set name=? where id=?" ,user.getName(),user.getId());
		//经过事务管理器 -> 启用事务
		//查询
		String sql="select id,name,create_time,update_time from user";



		//	删除template.update("delete from user where id = ?", user.getId());
     /*  template.update("insert into t_user(name, age) values(?, ?)", "admin:" + System.currentTimeMillis(), 20);
        Connection connection = dataSource.getConnection(); // 直接获取连接, 没有经过事务管理器
        template.update("insert into t_user(name, age) values(?, ?)", "admin:" + System.currentTimeMillis(),  20);
       Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis?useSSL=false", "root", "z1uBdBM");
        PreparedStatement prepareStatement = connection.prepareStatement("insert into t_user(name, age) values(?, ?)");
        prepareStatement.setString(1, "admin:" + System.currentTimeMillis());
       prepareStatement.setInt(2, 20);

		throw new Exception();
        throw new BizException();*/
		//throw new NullPointerException();
	}
}
