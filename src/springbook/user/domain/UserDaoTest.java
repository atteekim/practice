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
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")

@DirtiesContext
public class UserDaoTest {
    @Autowired
    private ApplicationContext context;
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
//        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        this.dao = this.context.getBean("userDao", UserDao.class);
        System.out.println(this.context);
        System.out.println(this);

        this.user1 = new User("0527", "skgl", "123");
        this.user2 = new User("0909", "rnjsh", "1111");
        this.user3 = new User("0416", "sewol", "00123");


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


        User user2 = dao.get(user.getId());
        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));

        User user3 = new User("gkgk", "qwe11", "2p3j;wie");

    }

    @Test
    public  void count() throws SQLException, ClassNotFoundException {


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

    @Test
    public void getAll() throws SQLException, ClassNotFoundException {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size(), is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));
        checkSomeUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSomeUser(user1, users2.get(0));
        checkSomeUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSomeUser(user3, users3.get(0));
        checkSomeUser(user1, users3.get(1));
        checkSomeUser(user2, users3.get(2));

    }

    private void checkSomeUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
    }


}
