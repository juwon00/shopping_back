package com.shopping2.error;

import com.shopping2.status.ErrorCode;
import com.shopping2.status.ErrorMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 필터에서 생기는 예외를 잡을 때
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            setErrorResponse(request, response, e.getErrorCode());
        }
    }

    private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(
                new ErrorMessage(
                        errorCode.getHttpStatus().value(),
                        String.valueOf(errorCode.getHttpStatus()),
                        errorCode.getMessage(),
                        request.getPathInfo()
                ).convertToJson()
        );
    }
}
