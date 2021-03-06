package com.lingxiao.blog.controller.user;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.po.User;
import com.lingxiao.blog.bean.vo.UserVo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.user.UserService;
import com.lingxiao.blog.utils.CookieUtils;
import com.lingxiao.blog.utils.IPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Api("用户管理接口")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册，注册成功返回token", notes = "注册")
    @ApiImplicitParam(name = "user", value = "user对象")
    @PostMapping(value = "/register")
    @OperationLogDetail(detail = "用户注册", operationType = OperationType.LOGIN)
    public ResponseEntity<String> register(@RequestBody @Valid User user,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        String token = userService.register(user, IPUtils.getIpAddress(request));
        CookieUtils.setCookie(request, response, ContentValue.LOGIN_TOKEN_NAME, token, ContentValue.COOKIE_MAXAGE);
        return ResponseEntity.ok(token);
    }

    @ApiOperation(value = "验证用户是否登录，返回用户信息", notes = "登录状态")
    @GetMapping(value = "/verify")
    //@OperationLogDetail(detail = "验证用户是否登录",operationType = OperationType.LOGIN)
    public ResponseEntity<ResponseResult<UserVo>> verify(
            @CookieValue(value = ContentValue.LOGIN_TOKEN_NAME, required = false) String cookieToken,
            @RequestParam(name = "token", required = false) String token,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        //Map<String, String> headersInfo = getHeadersInfo(request);
        token = request.getHeader(ContentValue.LOGIN_TOKEN_NAME);
        User user = userService.verify(token);
        //CookieUtils.setCookie(request,response, ContentValue.LOGIN_TOKEN_NAME,token,ContentValue.COOKIE_MAXAGE);
        ResponseResult<UserVo> result = new ResponseResult<>(userService.getUserVo(user));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "发送验证码", notes = "验证邮箱")
    @ApiImplicitParam(name = "address", value = "邮箱地址")
    @PostMapping(value = "/email/{address}")
    @OperationLogDetail(detail = "发送邮箱验证码", operationType = OperationType.UNKNOWN)
    public ResponseEntity<Void> sendEmail(@PathVariable(value = "address") String address) {
        userService.sendEmail(address);
        return ResponseEntity.ok().build();
    }



    @ApiOperation(value = "用户列表", notes = "获取用户列表")
    @ApiImplicitParam(name = "address", value = "邮箱地址")
    @GetMapping(value = "/list")
    @OperationLogDetail(detail = "获取用户列表", operationType = OperationType.SELECT)
    public ResponseEntity<PageResult<UserVo>> getUserList(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize,
                                            @RequestParam(value = "userId",defaultValue = "")Long userId) {
        PageResult<UserVo> userList = userService.getUserList(pageNum, pageSize, userId);
        return ResponseEntity.ok(userList);
    }

    @ApiOperation(value = "修改用户基本信息", notes = "修改用户基本信息")
    @ApiImplicitParam(name = "user", value = "用户基本信息")
    @PostMapping
    public ResponseEntity<Void> changeUserBaseInfo(@RequestBody UserVo userVo){
        User user = new User();
        user.setUserId(Long.valueOf(userVo.getUserId()));
        user.setAge(userVo.getAge());
        user.setBirthday(userVo.getBirthday());
        user.setPhoneNumber(userVo.getPhoneNumber());
        user.setHeadPortrait(userVo.getHeadPortrait());

        userService.changeUser(user);
        return ResponseEntity.ok().build();
    }
}
