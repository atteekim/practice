package springbook.user.domain;

import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

public class DaoEx {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        JUnitCore.main("springbook.user.domain.UserDaoTest");

//        User user = new User();
//        user.setId("SingleTon ");
//        user.setName("SINGLE");
//        user.setPassword("married");
//
//
//        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//        UserDao dao = context.getBean("userDao", UserDao.class);
//
//
//        dao.add(user);
//        System.out.println(user.getId() + " 등록성공");

//        User user2 = dao.get(user.getName());
//        System.out.println(user2.getId());
//        System.out.println(user2.getPassword());
//
//        System.out.println(user2.getName() + " 조회 성공");

//        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
//        System.out.println("Connection counter : " + ccm.getCounter());

    }
}
