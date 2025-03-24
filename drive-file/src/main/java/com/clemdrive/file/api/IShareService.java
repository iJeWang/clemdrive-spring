package com.clemdrive.file.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clemdrive.file.domain.Share;
import com.clemdrive.file.dto.sharefile.ShareListDTO;
import com.clemdrive.file.vo.share.ShareListVO;

import java.util.List;

public interface IShareService extends IService<Share> {
    List<ShareListVO> selectShareList(ShareListDTO shareListDTO, String userId);

    int selectShareListTotalCount(ShareListDTO shareListDTO, String userId);
}
