package com.clemdrive.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "commonfile")
@Entity
@TableName("commonfile")
public class CommonFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "varchar(20)")
    public String commonFileId;
    @Column(columnDefinition = "varchar(20) comment '用户文件id'")
    public String userFileId;
//    @Column(columnDefinition="int(2) comment '文件权限'")
//    public Integer filePermission;
}
