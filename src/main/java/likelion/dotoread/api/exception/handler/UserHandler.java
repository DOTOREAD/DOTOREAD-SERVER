package likelion.dotoread.api.exception.handler;

import likelion.dotoread.api.code.BaseErrorCode;
import likelion.dotoread.api.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
