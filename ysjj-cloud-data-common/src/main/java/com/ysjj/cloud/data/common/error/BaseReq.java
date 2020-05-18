package com.ysjj.cloud.data.common.error;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 参数校验以及分页基类 <br>
 * @Date: 2020/5/18 15:41 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
public class BaseReq implements Serializable {
    //当前页
    private int pageNum = 1;
    //每页的数量
    private int pageSize = 20;


    /**
     * 检查参数是否合法
     *
     * @return
     */
    public void checkData() {
        Map<String, Object> checkParam = new HashMap<>();
        ParamsUtil.hasEmptyParamMap(checkParam);
    }

    public void trimData() {

    }

    public interface Add {

    }

    public interface Save {

    }

    public interface Page {

    }

    public interface Select {

    }

    public interface Modify {

    }

    public interface Delete {

    }

    public interface Query {

    }

    public interface Status {

    }

    public interface Detail {

    }

    public interface Submit {

    }

    public interface Permission {

    }

    public interface Author {

    }
}
