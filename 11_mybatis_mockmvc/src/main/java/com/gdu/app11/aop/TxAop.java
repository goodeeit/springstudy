package com.gdu.app11.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class TxAop {

  @Pointcut("execution(* com.gdu.app11.service.*Impl.*(..))")
  public void setPointCut() { }
  
  @Around("setPointCut()")
  public Object doTx(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    
    try {
      
      // @Before
      
      Object obj = proceedingJoinPoint.proceed();
      
      // @AfterReturning 
      
      return obj;
      
    } catch(Exception e) {
      // @AfterThrowing
      
    } finally {
      // @After
      
    }
    
  }
  
}
