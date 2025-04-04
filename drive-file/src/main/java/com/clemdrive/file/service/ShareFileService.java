package com.clemdrive.file.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clemdrive.file.api.IShareFileService;
import com.clemdrive.file.domain.ShareFile;
import com.clemdrive.file.mapper.ShareFileMapper;
import com.clemdrive.file.vo.share.ShareFileListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ShareFileService extends ServiceImpl<ShareFileMapper, ShareFile> implements IShareFileService {
    @Resource
    ShareFileMapper shareFileMapper;

    @Override
    public List<ShareFileListVO> selectShareFileList(String shareBatchNum, String filePath) {
        return shareFileMapper.selectShareFileList(shareBatchNum, filePath);
    }

}
