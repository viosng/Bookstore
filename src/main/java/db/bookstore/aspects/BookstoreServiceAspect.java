package db.bookstore.aspects;

import db.bookstore.services.impl.BookstoreServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Created by vio on 18.04.2015.
 */
@Aspect
@Component
public class BookstoreServiceAspect {

    private final static Logger log = LoggerFactory.getLogger(BookstoreServiceImpl.class);

    @AfterReturning(pointcut = "execution(* db.bookstore.services.impl.BookstoreServiceImpl.addAuthor(..))", returning = "returnVal")
    public void addAuthor(JoinPoint pjp, Object returnVal) throws Throwable {
        log.info("Added new author or returned an existing one :{}", returnVal);
    }

    @AfterReturning(pointcut = "execution(* db.bookstore.services.impl.BookstoreServiceImpl.addBook(..))", returning = "returnVal")
    public void addBook(JoinPoint pjp, Object returnVal) throws Throwable {
        log.info("Added new book or returned an existing one : {}", returnVal);
    }

    @Around("execution(* db.bookstore.services.impl.BookstoreServiceImpl.get*(..))")
    public Object getMethods(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Execution of method {} is started", pjp.getSignature().getName());
        Object res = pjp.proceed();
        log.info("Execution result of method {} is {}", pjp.getSignature().getName(), res);
        return res;
    }
}