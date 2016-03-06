package org.projw.blackserver.services;

public interface VcodeService {
    /**
     * 验证手机号码是否是用户自己的
     * @param zone  国际区号
     * @param phone 电话号码
     * @param vcode 验证码
     * @return
     */
    boolean verify(String zone, String phone, String vcode);
}
