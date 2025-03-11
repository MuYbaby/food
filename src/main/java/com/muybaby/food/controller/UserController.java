package com.muybaby.food.controller;


import com.muybaby.food.bean.*;
import com.muybaby.food.service.UserService;
import com.muybaby.food.utils.*;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author MuYbaby
 * @create 2025/3/7
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;
    @GetMapping("/{user_id}")
    public Result getUser(@PathVariable("user_id") Integer user_id) {
        User user = userService.getUser(user_id);
        if (user == null) {
            return Result.error(HttpStatus.OK,"用户不存在"); // Or handle user not found scenario appropriately
        }
        return Result.success(user);
    }

    // 发送验证码
    @PostMapping("/sendVerificationCode")
    public Result sendVerificationCode(@RequestBody SendCodeRequest request) {
        String email = request.getEmail();

        // 检查邮箱是否已注册
        if (userService.getUserByEmail(email) != null) {
            return Result.error(HttpStatus.OK, "邮箱已注册");
        }

        // 检查是否可以发送验证码（是否超过了限制时间）
        if (!verificationCodeService.canSendCode(email)) {
            int remainingSeconds = verificationCodeService.getRemainingWaitTime(email);
            return Result.error(HttpStatus.TOO_MANY_REQUESTS, "请求过于频繁，请 " + remainingSeconds + " 秒后再试");
        }

        try {
            // 生成验证码
            String code = EmailUtil.generateVerificationCode();
            // 保存验证码
            verificationCodeService.saveVerificationCode(email, code);
            // 发送验证码
            EmailUtil.sendVerificationEmail(email, code);
            return Result.success("验证码已发送");
        } catch (MessagingException e) {
            e.printStackTrace();
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, "验证码发送失败");
        }
    }

    // 用户注册
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registrationRequest) {
        User user = registrationRequest.getUser();
        String verificationCode = registrationRequest.getVerificationCode();

        // 验证用户名是否已存在
        if (userService.getUserByName(user.getUsername()) != null) {
            return Result.error(HttpStatus.OK, "用户名已存在");
        }

        // 验证邮箱是否已注册
        if (userService.getUserByEmail(user.getEmail()) != null) {
            return Result.error(HttpStatus.OK, "邮箱已注册");
        }

        // 验证验证码
        if (!verificationCodeService.verifyCode(user.getEmail(), verificationCode)) {
            return Result.error(HttpStatus.OK, "验证码无效或已过期");
        }

        // 加密密码
        String encodedPassword = PasswordEncoderUtil.encodePassword(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);

        // 添加用户
        int result = userService.addUser(user);
        if (result == 1) {
            return Result.success("注册成功");
        } else {
            return Result.error(HttpStatus.OK, "注册失败");
        }
    }

    @PostMapping("/login")
    public Result<TokenDTO> login(@RequestBody User user) {
        System.out.println("Login request: " + user);
        User getuser = userService.getUserByName(user.getUsername());
        if (getuser == null)
            return Result.error(HttpStatus.OK, "用户不存在");
        if (!PasswordEncoderUtil.checkPassword(user.getPasswordHash(), getuser.getPasswordHash()))
            return Result.error(HttpStatus.OK, "密码错误");
        String token = JwtUtil.generateToken(getuser);
        TokenDTO tokenDTO = new TokenDTO(
                getuser.getUserId(),
                getuser.getUsername(),
                getuser.getRole(),
                token
        );
        return Result.success("登录成功", tokenDTO);
    }
}

