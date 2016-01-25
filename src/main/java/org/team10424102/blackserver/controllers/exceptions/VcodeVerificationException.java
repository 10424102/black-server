package org.team10424102.blackserver.controllers.exceptions;

import org.team10424102.blackserver.controllers.Error;

public class VcodeVerificationException extends BusinessException {
    public VcodeVerificationException(String phone, String vcode) {
        super(String.format("短信验证码验证失败 [手机号 = %s, 验证码 = %s]", phone, vcode));
    }

    @Override
    public int getErrorCode() {
        return Error.VCODE_VERIFICATION_FAILED.ordinal();
    }

    @Override
    public String getErrorMessage() {
        return "手机验证失败";
    }
}
