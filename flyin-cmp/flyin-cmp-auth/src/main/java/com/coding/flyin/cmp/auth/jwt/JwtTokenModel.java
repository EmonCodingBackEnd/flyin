package com.coding.flyin.cmp.auth.jwt;

import com.coding.flyin.cmp.auth.metadata.AppMetadata;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public final class JwtTokenModel implements Serializable {

    private static final long serialVersionUID = -2219619148026861313L;

    private String identity;

    private AppMetadata metadata;

    private Date lastPasswordResetDate;
}
