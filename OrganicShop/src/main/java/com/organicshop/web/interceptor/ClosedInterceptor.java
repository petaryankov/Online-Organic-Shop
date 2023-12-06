package com.organicshop.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalTime;

@Configuration
public class ClosedInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        var requestURI = request.getRequestURI();

        if (!requestURI.equals("/closed")) {

            LocalTime now = LocalTime.now();
            int openTime = 8;
            int closetTime = 22;

            if (openTime > now.getHour() || closetTime < now.getHour()) {

                var secondRequestURI= request.getRequestURI();

                if(secondRequestURI.equals("/orders/finalize")){
                    response.sendRedirect("/closed");
                }

                return true;
            }

        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}