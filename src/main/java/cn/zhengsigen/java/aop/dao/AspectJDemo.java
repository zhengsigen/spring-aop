package cn.zhengsigen.java.aop.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cn.zhengsigen.java.aop.controller.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

//@Aspect

public class AspectJDemo {

    Connection connection;

    @Pointcut("execution(* cn.zhengsigen.java.aop.service.**.*(..))")
    public void pointcut() {}


    @Before(value = "pointcut()")
    public Connection before() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/0412spring?user=root&password=123&useSSL=false");
        return connection;
    }
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();
        Class<?> aClass = proceedingJoinPoint.getTarget().getClass();

        //如果没有Transaction注解,不调用切面
        if(!aClass.isAnnotationPresent(Transaction.class)){
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = aClass.getMethod(signature.getMethod().getName(), signature.getMethod().getParameterTypes());
            if(method==null||!method.isAnnotationPresent(Transaction.class)){
                System.out.println("没有注解");
                return proceedingJoinPoint.proceed(args);
            }
        }
        connection.setAutoCommit(false);
        args[0] =connection;
        try {
            Object proceed = proceedingJoinPoint.proceed(args);
            ((Connection) args[0]).commit();
            System.out.println("有注解，无异常");
            return proceed;

        } catch (Exception e) {
            System.err.println("异常："+proceedingJoinPoint.getSignature().getName()+":"+e);
            ((Connection) args[0]).rollback();
            System.out.println("数据已回滚");
            System.out.println("有注解，有异常");
            return null;
        }
    }
}
