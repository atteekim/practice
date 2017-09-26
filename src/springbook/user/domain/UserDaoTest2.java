package springbook.user.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static springbook.user.domain.UserServiceImpl.MIN_LOGCOUNT_ROF_SILVER;
import static springbook.user.domain.UserServiceImpl.MIN_RECCOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")

@DirtiesContext
public class UserDaoTest2 {
    @Autowired
    ApplicationContext context;
    @Autowired
    UserDao dao;
    @Autowired
    DataSource dataSource;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    UserServiceImpl.MailSender mailSender;
    @Autowired
    UserServiceImpl userServiceImpl;

    List<User> users;
    User user;

    @Before
    public void setUp() {
        user  = new User();

        this.dao = this.context.getBean("userDao", UserDao.class);
        System.out.println(this.context);
        System.out.println(this);

        users = Arrays.asList(
                new User("a", "aa", "aaa", User.Level.BASIC, MIN_LOGCOUNT_ROF_SILVER-1, 0, "aa@ee.com"),
                new User("b", "bb", "bbb", User.Level.BASIC, MIN_LOGCOUNT_ROF_SILVER, 0, "bb@ee.com"),
                new User("c", "cc", "ccc", User.Level.SILVER, 60, MIN_RECCOMMEND_FOR_GOLD-1, "cc@ee.com"),
                new User("d", "dd", "ddd", User.Level.SILVER, 60, MIN_RECCOMMEND_FOR_GOLD, "dd@ee.com"),
                new User("e", "ee", "eee", User.Level.GOLD, 100, Integer.MAX_VALUE, "ee@ee.com")
        );
    }

//    private void checkSomeUser(User user1, User user2) {
//        assertThat(user1.getId(), is(user2.getId()));
//        assertThat(user1.getName(), is(user2.getName()));
//        assertThat(user1.getPassword(), is(user2.getPassword()));
//        assertThat(user1.getLevel(), is(user2.getLevel()));
//        assertThat(user1.getLogin(), is(user2.getLogin()));
//        assertThat(user1.getRecommend(), is(user2.getRecommend()));
//    }

//    @Test
//    public void bean() {
//        assertThat(this.userService, is(notNullValue()));
//    }

//    @Test
//    public void upgradeLevels() {
//        dao.deleteAll();
//        for(User user : users) dao.add(user);
//
//        userService.upgradeLevels();
//
//        checkLevel(users.get(0), User.Level.BASIC);
//        checkLevel(users.get(1), User.Level.SILVER);
//        checkLevel(users.get(2), User.Level.SILVER);
//        checkLevel(users.get(3), User.Level.GOLD);
//        checkLevel(users.get(4), User.Level.GOLD);
//    }


//    private void checkLevel(User user, User.Level expectedLevel) {
//        User userUpdate = dao.get(user.getId());
//        assertThat(userUpdate.getLevel(), is(expectedLevel));
//    }

//    @Test
//    public void add() {
//        dao.deleteAll();
//
//        User userWithLevel = users.get(4);
//        User userWithoutLevel = users.get(0);
//        userWithoutLevel.setLevel(null);
//
//        userService.add(userWithLevel);
//        userService.add(userWithoutLevel);
//
//        User userWithLevelRead = dao.get(userWithLevel.getId());
//        User userWithoutLevelRead = dao.get(userWithoutLevel.getId());
//
//        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
//        assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
//    }

    @Test
    public void upgradeLevel() {
        User.Level[] levels = User.Level.values();
        for(User.Level level : levels) {
            if (level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }

//    @Test(expected = IllegalStateException.class)
//    public void cannotUpgradeLever() {
//        User.Level[] levels = User.Level.values();
//        for(User.Level level : levels) {
//            if(level.nextLevel() != null) {
//                continue;
//            }
//            user.setLevel(level);
//            user.upgradeLevel();
//
//        }
//    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = dao.get(user.getId());
        if(upgraded) { assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel())); }
        else { assertThat(userUpdate.getLevel(), is(user.getLevel())); }
    }

//    public interface UserLevelUpgradePolicy {
//        boolean canUpgradeLevel(User user);
//        void upgradeLevel(User user);
//    }

    static class TestUserService extends UserServiceImpl {
        private String id;

        public TestUserService(String id) {
            this.id = id;
        }
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

        static class TestUserServiceException extends RuntimeException {
        }
    }

    @Test
    @DirtiesContext
    public void upgradeLevels() throws Exception {
        dao.deleteAll();
        for(User user : users) dao.add(user);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(), is(2));
        assertThat(request.get(0), is(users.get(1).getEmail()));
        assertThat(request.get(1), is(users.get(3).getEmail()));

    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(dao);
        testUserService.setMailSender(mailSender);

        UserServiceTx txUserService = new UserServiceTx();
        txUserService.setTransactionManager(transactionManager);
        txUserService.setUserService(testUserService);

        dao.deleteAll();
        for(User user : users) dao.add(user);

        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserService.TestUserServiceException e) { }
        checkLevelUpgraded(users.get(1), false);
    }

    static class MockMailSender implements UserServiceImpl.MailSender {
        private List<String> requests = new ArrayList<String>();

        public List<String> getRequests() {
            return requests;
        }

        public void send(SimpleMailMessage mailMessage) throws MailException {
            requests.add(mailMessage.getTo()[0]);
        }

        public void send(SimpleMailMessage[] mailMessages) throws MailException{
        }
    }




}

