package com.uestc.exception;

import com.uestc.result.CodeMsg;

public class GlobleException extends RuntimeException{

    private CodeMsg codeMsg;

    public GlobleException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
