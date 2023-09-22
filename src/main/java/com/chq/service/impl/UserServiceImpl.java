package com.chq.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chq.common.R;
import com.chq.controller.advice.exception.AuthException;
import com.chq.mapper.ScoreMapper;
import com.chq.pojo.Score;
import com.chq.pojo.User;
import com.chq.mapper.UserMapper;
import com.chq.pojo.dto.LoginDto;
import com.chq.pojo.dto.MsgDto;
import com.chq.pojo.dto.PasswordDto;
import com.chq.pojo.dto.UserDto;
import com.chq.pojo.eo.UserEo;
import com.chq.pojo.vo.UserVo;
import com.chq.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chq.util.Sender;
import com.chq.util.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.chq.cache.CachePool.CLASS_CACHE;
import static com.chq.cache.CachePool.POSITION_CACHE;
import static com.chq.common.RedisConstant.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-07
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


   @Autowired
   private ScoreMapper scoreMapper;

   @Autowired
   @Qualifier(value = "${sender}")
   private Sender sender;

    @Override
    public R add(User user) {
        String password = user.getPassword();
        String tail = RandomUtil.randomString(4);
        String pwd = DigestUtil.md5Hex(password + tail);
        user.setPassword(pwd);
        user.setSalt(tail);
        save(user);
        return R.ok();
    }

    @Override
    public R del(Integer id) {
        removeById(id);
        return R.ok();
    }

    @Override
    public R up(User user) {
        String s = user.getPassword();
        String pwd = DigestUtil.md5Hex(s + user.getSalt());
        user.setPassword(pwd);
        updateById(user);
        return R.ok();
    }

    @Override
    public R get(Integer id) {
        User user = getById(id);
        return R.ok(user);
    }

    @Override
    public R<List<UserVo>> getList(Integer currentPage) {
        Page<User> page = new Page<>(currentPage,10);
        Page<User> page1 = page(page);
        List<User> records = page1.getRecords();
        List<UserVo> collect = records.stream().map(item -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(item, vo);
            vo.setClassName(CLASS_CACHE.get(item.getClassId()));
            vo.setPosition(POSITION_CACHE.get(item.getPositionId()));
            return vo;
        }).collect(Collectors.toList());
        return R.ok(collect,page1.getTotal());
    }

    @Override
    public R adminLogin(LoginDto loginDto) throws AuthException {
        String phone = loginDto.getPhone();
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (one==null) {
            throw new AuthException("账号或密码错误");
        }
        String password = loginDto.getPassword();
        if (one.getPositionId()!=1||!DigestUtil.md5Hex(password+one.getSalt()).equals(one.getPassword())) {
          throw new AuthException("账号或密码错误");
        }
        String token = UUID.randomUUID().toString(true);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(one,userDto);
        stringRedisTemplate.opsForValue().set(LOGIN_KEY+token,JSONUtil.toJsonStr(userDto));
        return R.ok(token);
    }

    @Override
    public R logout(String token) {
        stringRedisTemplate.delete(LOGIN_KEY + token);
        return R.ok();
    }

    @Override
    public R login(LoginDto loginDto) throws AuthException {

        String phone = loginDto.getPhone();
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (one==null) {
            throw new AuthException("账号或密码错误");
        }
        String pwd=loginDto.getPassword()+one.getSalt();
        String password = DigestUtil.md5Hex(pwd);
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone)
                .eq(User::getPassword, password)
                .eq(User::getStatus,1)
        );
        if (user==null) {
            throw new AuthException("账号或密码错误");
        }
        String token = UUID.randomUUID().toString(true);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(one,userDto);
        stringRedisTemplate.opsForValue().set(LOGIN_KEY+token,JSONUtil.toJsonStr(userDto),LOGIN_TTL,TimeUnit.HOURS);
        return R.ok(token);
    }

    @Override
    public R getMe() {
        UserDto userDto = UserHolder.getUser();
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(userDto,vo);
        vo.setClassName(CLASS_CACHE.get(vo.getClassId()));
        vo.setPosition(POSITION_CACHE.get(vo.getPositionId()));
        return R.ok(vo);
    }

    @Override
    public R<List<UserDto>> listByClassId(Integer classId,Integer examId) {
        List<User> list = lambdaQuery().eq(User::getClassId, classId).eq(User::getPositionId,2).list();
        List<UserDto> collect = list.stream().filter(item -> {
            if (scoreMapper.selectOne(new LambdaQueryWrapper<Score>().eq(Score::getExamId, examId).eq(Score::getUserId, item.getId())) == null)
                return true;
            return false;
        }).map(item -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(item, userDto);
            return userDto;
        }).collect(Collectors.toList());
        return R.ok(collect);

    }

    @Override
    public R excelAdd(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UserEo.class, new PageReadListener<UserEo>(dataList -> {
            for (UserEo eo: dataList) {
                log.info("对象,{}",eo);
                User user = new User();
                BeanUtils.copyProperties(eo,user);
                String salt = RandomUtil.randomString(4);
                user.setSalt(salt);
                String pwd=DigestUtil.md5Hex("123456"+salt);
                user.setPassword(pwd);
                save(user);
            }
        })).sheet().doRead();
        return R.ok();
    }

    @Override
    public R updatePwd(PasswordDto passwordDto) throws AuthException {
        UserDto user = UserHolder.getUser();
        if (!passwordDto.getCode().equals(stringRedisTemplate.opsForValue().get(AUTH_CODE+user.getId())))
            throw new AuthException("验证码出错");
        String password = passwordDto.getPassword();
        User user1 = getById(user.getId());
        String salt = user1.getSalt();
        String pwd=DigestUtil.md5Hex(password+salt);
        update(new LambdaUpdateWrapper<User>().eq(User::getId,user.getId()).set(User::getPassword,pwd));
        return R.ok();
    }

    @Override
    public R sendCode() {
        UserDto user = UserHolder.getUser();
        String email = user.getEmail().trim();
        if (email==null||email=="") return R.fail("未注册邮箱,请联系管理员");
        String code = RandomUtil.randomString(6);
        stringRedisTemplate.opsForValue().set(AUTH_CODE+user.getId(),code,CODE_TTL, TimeUnit.MINUTES);
        if (sender.sendAuthCode(new MsgDto(email, "密码修改", code))) {
            return R.ok();
        }
        return R.fail("服务器异常");
    }
}
