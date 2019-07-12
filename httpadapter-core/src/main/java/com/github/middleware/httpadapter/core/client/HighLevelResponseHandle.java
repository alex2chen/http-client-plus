package com.github.middleware.httpadapter.core.client;

import com.github.middleware.httpadapter.core.server.ResponseEntity;

/**
 * @Author: alex
 * @Description: 服务端响应处理(反序列前)
 * @Date: created in 2018/1/24.
 */
public interface HighLevelResponseHandle extends ResponseHandleChain<ResponseEntity, Object> {

    @Override
    default ResponseLevel getLevel() {
        return ResponseLevel.HIGH;
    }

}
