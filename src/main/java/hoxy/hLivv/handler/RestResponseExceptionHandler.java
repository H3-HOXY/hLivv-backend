package hoxy.hLivv.handler;

import hoxy.hLivv.dto.ErrorDto;
import hoxy.hLivv.exception.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(value = {DuplicateMemberException.class, DuplicateMemberCouponException.class})
    @ResponseBody
    protected ErrorDto conflict(RuntimeException ex, WebRequest request) {
        return new ErrorDto(CONFLICT.value(), ex.getMessage());
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = {AccessDeniedMemberException.class,
            NotFoundRestoreException.class,
            NotFoundMemberException.class,
            NotFoundCouponException.class,
            NotFoundProductException.class,
            NotFoundCartItemException.class,
            AccessDeniedException.class})
    @ResponseBody
    protected ErrorDto forbidden(RuntimeException ex, WebRequest request) {
        return new ErrorDto(FORBIDDEN.value(), ex.getMessage());
    }

    @ResponseStatus(NO_CONTENT)
    @ExceptionHandler(value = {CartItemRemovedException.class})
    @ResponseBody
    protected ErrorDto noContent(RuntimeException ex, WebRequest request) {
        return new ErrorDto(NO_CONTENT.value(), ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {StockOverFlowException.class,
            InvalidPaymentException.class,
            InvalidPointException.class,
            AlreadyUsedCouponException.class,
            ExpiredCouponException.class})
    @ResponseBody
    protected ErrorDto badRequest(RuntimeException ex, WebRequest request) {
        return new ErrorDto(BAD_REQUEST.value(), ex.getMessage());
    }


}
