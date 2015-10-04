package fyp.models;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {

	private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private static Configuration configuration = new Configuration();
	private static StandardServiceRegistry serviceRegistry;
	private static SessionFactory sessionFactory;
	
	public static SessionFactory getSessionFactory(){
		if(null == sessionFactory){
			try{
				configuration.configure(CONFIG_FILE_LOCATION);
				serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
						configuration.getProperties()).build();
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			}catch(HibernateException ex){
				ex.printStackTrace();
			}
		}
		return sessionFactory;
	}
	
	public static Session getSession(){
		Session hibernateSession = threadLocal.get();
		if(null == hibernateSession || !hibernateSession.isOpen()){
			hibernateSession = getSessionFactory().openSession();
			threadLocal.set(hibernateSession);
		}
		return hibernateSession;
	}
}
