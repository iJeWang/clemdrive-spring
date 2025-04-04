package com.clemdrive.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "image")
@Entity
@TableName("image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20)")
    private Long imageId;
    @Column(columnDefinition = "varchar(20) comment '文件id'")
    private String fileId;
    @Column(columnDefinition = "int(5) comment '图像的宽'")
    private Integer imageWidth;
    @Column(columnDefinition = "int(5) comment '图像的高'")
    private Integer imageHeight;
}
