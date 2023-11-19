package com.example.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class TimingAspect {
	private Logger logger = LoggerFactory.getLogger(TimingAspect.class);
	
	@Pointcut("execution(public * com.example.demo..service.*ServiceImpl.*(..))")
	private void aroundTarget() {}
	
	@Around("aroundTarget()")
	public Object timingAdvice(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			throw e;
		} finally {
			stopWatch.stop();
			
			String classPath = pjp.getTarget().getClass().getName();
			String methodName = pjp.getSignature().getName();
			logger.debug("{}.{} 걸린시간: {}밀리초", classPath, methodName, stopWatch.getLastTaskTimeMillis());
		}
		
		return result;
	}
}
