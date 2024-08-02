package com.xunhang.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MybatistHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        //this.strictInsertFill(metaObject, "createUser",Long.class, BaseContext.getCurrentId());
        //this.strictInsertFill(metaObject, "updateUser",Long.class, BaseContext.getCurrentId());
        // 其他插入时需要填充的字段
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        //this.strictInsertFill(metaObject, "updateUser",Long.class, BaseContext.getCurrentId());
        // 其他更新时需要填充的字段
    }
}
