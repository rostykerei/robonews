/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.facebook;

import com.restfb.Facebook;
import com.restfb.types.Page;

public class MyPage extends Page {

    @Facebook("is_verified")
    private Boolean isVerified;

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
