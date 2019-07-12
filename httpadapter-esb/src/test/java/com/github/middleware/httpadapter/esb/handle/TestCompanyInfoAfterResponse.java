package com.github.middleware.httpadapter.esb.handle;

import com.github.middleware.httpadapter.esb.dto.CompanyInfo;
import com.github.middleware.httpadapter.core.client.support.CustomLevelAbstractResponseHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/31.
 */
public class TestCompanyInfoAfterResponse extends CustomLevelAbstractResponseHandle<CompanyInfo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestCompanyInfoAfterResponse.class);

    @Override
    public CompanyInfo doAfterResponse(CompanyInfo companyInfo) {
        LOGGER.info("Executing After Response For companyInfo：{}", companyInfo);
        return companyInfo;
    }

}
