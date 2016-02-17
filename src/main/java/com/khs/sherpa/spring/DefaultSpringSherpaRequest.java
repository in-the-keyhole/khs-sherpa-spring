package com.khs.sherpa.spring;

import com.khs.sherpa.servlet.request.DefaultSherpaRequest;
import org.springframework.cglib.proxy.Enhancer;

public class DefaultSpringSherpaRequest extends DefaultSherpaRequest {
    @Override
    protected boolean isEnhanced(Class clazz) {
        return Enhancer.isEnhanced(clazz);
    }

}
