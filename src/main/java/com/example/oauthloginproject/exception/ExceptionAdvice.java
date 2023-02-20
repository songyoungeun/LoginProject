package com.example.oauthloginproject.exception;

import com.example.oauthloginproject.exception.custom.*;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice // @ControllerAdvice와 @ResponseBody을 가지고 있다. @Controller처럼 작동하며 @ResponseBody를 통해 객체를 리턴할 수 있다.
public class ExceptionAdvice { // @ExceptionHandler : Controller계층에서 발생하는 에러를 잡아서 메서드로 처리해주는 기능

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<String> defaultException(Exception e) throws Exception {
        e.printStackTrace();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg", e.getMessage());

        JSONArray result = JSONArray.fromObject(resultMap);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.get(0).toString());
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            UnsatisfiedServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> badRequestException(Exception e) throws Exception {
        e.printStackTrace();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", e.getMessage());

        JSONArray result = JSONArray.fromObject(resultMap);

        // error message 중에 " default message [이메일은 필수 입력 값 입니다.] " 해당 부분만 뽑아내고 싶다!

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.get(0).toString());
    }

    @ExceptionHandler({AuthenticationEntryPointException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<String> accessDeniedException(Exception e) throws Exception {
        e.printStackTrace();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", e.getMessage());

        JSONArray result = JSONArray.fromObject(resultMap);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result.get(0).toString());
    }

    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseEntity<String> forbiddenException(ForbiddenException e) throws Exception {
        e.printStackTrace();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", e.getMessage());

        JSONArray result = JSONArray.fromObject(resultMap);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result.get(0).toString());
    }

    @ExceptionHandler({
            NotFoundException.class,
            UserNotFoundException.class,
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<String> notFoundException(Exception e) throws Exception { //NotFoundException e 로 했을땐 Could not resolve parameter [0] in public 이런 오류났음

        e.printStackTrace();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", e.getMessage());

        JSONArray result = JSONArray.fromObject(resultMap);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.get(0).toString());
    }
//    @ExceptionHandler(UserNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<String> usernotFoundException(UserNotFoundException e) throws Exception {
//        e.printStackTrace();
//
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        resultMap.put("msg", e.getMessage());
//
//        JSONArray result = JSONArray.fromObject(resultMap);
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.get(0).toString());
//    }
    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseEntity<String> DuplicatedException(DuplicatedException e) throws Exception {
        e.printStackTrace();

        Map<String, Object> resultMap = new HashMap<String, Object>(); //Map에 Value 값으로 한가지로 정해지지 않고 여러 형태의 Collection을 넣고 싶다면?Value를  Collection의 최상위 클래스인 Object로 지정
        resultMap.put("msg", e.getMessage());

        JSONArray result = JSONArray.fromObject(resultMap);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result.get(0).toString());
    }

    @ExceptionHandler(ApiOtherException.class)
    protected ResponseEntity<String> ApiOtherException(ApiOtherException e) throws Exception {
        e.printStackTrace();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", e.getMessage());

        JSONArray result = JSONArray.fromObject(resultMap);
        return ResponseEntity.status(700).body(result.get(0).toString());
    }

    // 사용자가 입력한 값에 문제가 생겼을 때 사용하는 예외 메소드 재정의
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
//                                                                  final HttpHeaders headers,
//                                                                  final HttpStatus status,
//                                                                  final WebRequest request) {
//        ErrorResponse errorResponse = makeErrorResponse(ex.getBindingResult());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(errorResponse);
//
//    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) throws Exception{
//        log.warn("MethodArgumentNotValidException 발생 : ", request.getRequestURL(), e.getStackTrace());
//        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
//        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
//
//    }
//    private ErrorResponse makeErrorResponse(BindingResult bindingResult) {
//
//        String code = "";
//        String description = "";
//        String detail = "";
//
//        if(bindingResult.hasErrors()) {
//
//            //DTO에 설정한 message값을 가져옴
//            detail = bindingResult.getFieldError().getDefaultMessage();
//            //DTO에 걸어논 어노테이션 명 가져옴
//            String bindResultCode = bindingResult.getFieldError().getCode();
//
//            switch (bindResultCode) {
//                case "NotBlank":
//                    code = ErrorCode.NOT_BLANK.getCode();
//                    description = ErrorCode.NOT_BLANK.getDescription();
//                    break;
//                case "Email":
//                    code = ErrorCode.EMAIL.getCode();
//                    description = ErrorCode.EMAIL.getDescription();
//                    break;
//            }
//
//        }
//        return new ErrorResponse(code, description, detail);
//    }
}