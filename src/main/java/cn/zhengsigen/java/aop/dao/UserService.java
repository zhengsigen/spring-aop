package cn.zhengsigen.java.aop.dao;

		import java.sql.Connection;
		import java.sql.SQLException;
		import java.util.Map;

		import cn.zhengsigen.java.aop.pojo.User;

public interface UserService {

	public void addUser(Connection connection,User user) throws SQLException ;

	public void deleteUser(Connection connection,User user) throws SQLException ;

	Map<Integer, User> getList(Connection connection) throws SQLException;

	public void updateUser(Connection connection,User user) throws SQLException;

	void updateUser(User user) throws Exception;
}
