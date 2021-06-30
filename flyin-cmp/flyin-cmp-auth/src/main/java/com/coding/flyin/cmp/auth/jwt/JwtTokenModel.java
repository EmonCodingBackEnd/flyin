package com.coding.flyin.cmp.auth.jwt;

import com.coding.flyin.cmp.auth.metadata.AppMetadata;
import com.coding.flyin.cmp.auth.metadata.AppSession;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public final class JwtTokenModel<T extends AppSession> implements Serializable {

    private static final long serialVersionUID = -2219619148026861313L;

    private String identity;

    private AppMetadata<T> metadata;

    private Date lastPasswordResetDate;
}
