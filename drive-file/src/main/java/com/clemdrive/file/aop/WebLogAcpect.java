package com.clemdrive.file.aop;

import com.clemdrive.common.anno.MyLog;
import com.clemdrive.common.result.RestResult;
import com.clemdrive.common.util.security.JwtUser;
import com.clemdrive.common.util.security.SessionUtil;
import com.clemdrive.file.api.IOperationLogService;
import com.clemdrive.file.util.OperationLogUtil;
import com.clemdrive.file.vo.user.UserLoginVo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class WebLogAcpect {
    @Resource
    IOperationLogService operationLogService;

    private String operation = "";
    private String module = "";
    private HttpServletRequest request;


    /**
     * 定义切入点，切入点为com.example.aop下的所有函数
     */
    @Pointcut("@annotation(com.clemdrive.common.anno.MyLog)")
    public void webLog() {
    }

    /**
     * 前置通知：在连接点之前执行的通知
     *
     * @param joinPoint 切入点
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取操作
        MyLog myLog = method.getAnnotation(MyLog.class);

        if (myLog != null) {
            operation = myLog.operation();
            module = myLog.module();
        }

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        request = attributes.getRequest();


    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {

        if (ret instanceof RestResult) {
            boolean isSuccess = ((RestResult) ret).getSuccess();
            String errorMessage = ((RestResult) ret).getMessage();
            JwtUser sessionUser = SessionUtil.getSession();
            String userId = "";
            if (sessionUser != null) {
                userId = sessionUser.getUserId();
            }

            Integer code = ((RestResult) ret).getCode();
            if (code != null && code == 200001) {
                UserLoginVo data = (UserLoginVo) ((RestResult) ret).getData();
                userId = data.getUserId();
            }
            if (isSuccess) {

                operationLogService.insertOperationLog(
                        OperationLogUtil.getOperationLogObj(request, userId, "成功", module, operation, "操作成功"));
            } else {
                operationLogService.insertOperationLog(
                        OperationLogUtil.getOperationLogObj(request, userId, "失败", module, operation, errorMessage));
            }
        }


    }

    /**
     * 获取参数Map集合
     *
     * @param joinPoint
     * @return
     */
    Map<String, Object> getNameAndValue(JoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            param.put(paramNames[i], paramValues[i]);
        }
        return param;
    }
}