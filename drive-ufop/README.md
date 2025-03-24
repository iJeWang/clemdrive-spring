# ufop-spring-boot-starter

#### 介绍

UFOP (Unified File Operation Platform) 统一文件操作平台，通过引入该依赖，可以实现文件操作的统一管理

此模块为Clem网盘核心功能，主要功能如下：

1. 本地文件上传、下载，删除，预览，重命名，读文件流，写文件流
2. 阿里云OSS上传，下载，删除，预览，重命名，读文件流，写文件流
3. FastDFS上传，下载，删除，预览，重命名，读文件流，写文件流
4. FastDFS+Redis实现集群化部署
5. 图片支持缩略图预览
#### 使用说明

1. application.properties配置文件说明

配置磁盘存储方式, 0-本地存储， 1-阿里云OSS存储， 2-fastDFS存储, 3-minio存储, 4-七牛云KODO对象存储

```properties
ufop.storage-type=0
```

当选择0-本地磁盘存储之后，你还可以继续配置本地文件存储路径

```properties
ufop.local-storage-path=D://test
```

当选择1-阿里云OSS存储之后，需要配置阿里云OSS相关信息，

```properties
#阿里云oss基本配置
ufop.aliyun.oss.endpoint=
ufop.aliyun.oss.access-key-id=
ufop.aliyun.oss.access-key-secret=
ufop.aliyun.oss.bucket-name=
```

当选择2-FastDFS存储之后，则需要配置FastDFS服务器信息

```properties
#FastDFS配置
fdfs.so-timeout=1501
fdfs.connect-timeout=601
fdfs.thumb-image.width=150
fdfs.thumb-image.height=150
fdfs.tracker-list=127.0.0.1:22122 
```

除了0-本地存储外，其他存储方式需要配置redis信息

```properties

# Redis数据库索引（默认为0）
spring.redis.database=0  
# Redis服务器地址
spring.redis.host=127.0.0.1
# Redis服务器连接端口
spring.redis.port=6379
# Redis服务器连接密码（默认为空）
spring.redis.password=ma123456
# 连接池最大连接数（使用负值表示没有限制） 默认 8
spring.redis.lettuce.pool.max-active=8
# 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
spring.redis.lettuce.pool.max-wait=10000
# 连接池中的最大空闲连接 默认 8
spring.redis.lettuce.pool.max-idle=30
# 连接池中的最小空闲连接 默认 0
spring.redis.lettuce.pool.min-idle=10
#连接超时时间（毫秒）
spring.redis.timeout=5000
```

当配置完基础信息之后，使用就非常简单了，伪代码如下：

注入UFOPFactory

```java
@Resource
UFOPFactory ufopFactory;
```

上传文件操作，具体这个上传操作是哪种存储实现，由`ufop.storage-type`配置项决定，

```java

//上传操作
Uploader uploader = ufopFactory.getUploader();
uploader.upload(request, uploadFile);
```

下载和删除则需要用户自己传入文件存储类型

```java
//下载操作
Downloader downloader = ufopFactory.getDownloader(fileBean.getStorageType());
downloader.download(httpServletResponse, downloadFile);
//删除操作
Deleter deleter = ufopFactory.getDeleter(fileBean.getStorageType());
deleter.delete(deleteFile);

```
