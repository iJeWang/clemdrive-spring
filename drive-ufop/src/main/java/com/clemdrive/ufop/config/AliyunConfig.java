package com.clemdrive.ufop.config;

import com.clemdrive.ufop.domain.AliyunOSS;
import lombok.Data;

@Data
public class AliyunConfig {
    private AliyunOSS oss = new AliyunOSS();


}
