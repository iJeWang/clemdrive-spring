package com.clemdrive.ufop.config;

import com.clemdrive.ufop.domain.QiniuyunKodo;
import lombok.Data;

@Data
public class QiniuyunConfig {
    private QiniuyunKodo kodo = new QiniuyunKodo();
}
