package com.github.middleware.httpadapter.core.client;

import com.github.middleware.httpadapter.core.server.ResponseEntity;

/**
 * @Author: alex
 * @Description: 服务端与客户端之间中转的处理（一般用做类型转换）
 * @Date: created in 2018/1/30.
 */
public interface TransferLevelResponseHandler extends ResponseHandleChain<ResponseEntity, Object> {

    /**
     * 获取级别（高优先级的处理还是用户自定义的处理）
     *
     * @return
     */
    @Override
    default ResponseLevel getLevel() {
        return ResponseLevel.TRANSFER;
    }

}
