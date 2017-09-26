package springbook.user.domain;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

public class UserServiceImpl implements UserService {
    public static final int MIN_LOGCOUNT_ROF_SILVER = 50;
    public static final int MIN_RECCOMMEND_FOR_GOLD = 30;

    UserDao userDao;
    MailSender mailSender;

    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;

    public void setMailSender(MailSender mailSender) { this.mailSender = mailSender; }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for( User user : users ) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }

//        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
//        try {
//            upgradeLevelsInternal();
//            this.transactionManager.commit(status);
//        } catch (Exception e) {
//            this.transactionManager.rollback(status);
//            throw e;
//        }
    }
//
//    private void upgradeLevelsInternal() {
//        List<User> users = userDao.getAll();
//        for( User user : users ) {
//            if (canUpgradeLevel(user)) {
//                upgradeLevel(user);
//            }
//        }
//    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(("mail.server.com"));

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());

        this.mailSender.send(mailMessage);
    }

    public interface MailSender {
        void send(SimpleMailMessage simpleMailMessage) throws MailException;
        void send(SimpleMailMessage[] simpleMailMessages) throws MailException;
    }

    private boolean canUpgradeLevel(User user) {
        User.Level currentLevel = user.getLevel();
        switch(currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_ROF_SILVER);
            case SILVER: return (user.getRecommend() >=MIN_RECCOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level : " + currentLevel);
        }
    }

    public void add(User user) {
        if (user.getLevel() == null ) user.setLevel(User.Level.BASIC);
        userDao.add(user);
    }
}

