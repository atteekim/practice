package springbook.user.domain;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")

@DirtiesContext
public class UserDaoTest {
    @Autowired
    private ApplicationContext context;
    private UserDao dao;

    @Before
    public void setUp() {
//        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        this.dao = this.context.getBean("userDao", UserDao.class);
        System.out.println(this.context);
        System.out.println(this);

    }
    @Test
    public void addAndGet() throws ClassNotFoundException,SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User user = new User();
        user.setId("1dddd23 ");
        user.setName("31dad31");
        user.setPassword("65w4");
        dao.add(user);
        assertThat(dao.getCount(), is(1));


        User user2 = dao.get(user.getName());
        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));

        User user3 = new User("gkgk", "qwe11", "2p3j;wie");

    }

    @Test
    public  void count() throws SQLException, ClassNotFoundException {

        User user1 = new User("0392", "sksk", "asd12");
        User user2 = new User("6345", "sk55sk", "asd1212");
        User user3 = new User("4156", "sk66sk", "asd121212");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(user1.getName(), is(user1.getName()));
        assertThat(user1.getPassword(), is(user1.getPassword()));

        dao.add(user2);
        assertThat(user2.getName(), is(user2.getName()));
        assertThat(user2.getPassword(), is(user2.getPassword()));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    // 텍스트 중에 발생할 것으로 기대하는 예외 클래스를 지정
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }




}
